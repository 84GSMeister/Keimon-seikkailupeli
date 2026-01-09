package keimo.keimoengine.grafiikat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_messageBox;

public class Shader {
    private int vs, fs, program;

    public Shader(String fileName) {
        vs = glCreateShader(GL_VERTEX_SHADER);
        String vertexShaderCode = readFile(fileName + ".vs");
        if (vertexShaderCode == null || vertexShaderCode.equals("")){
            vertexShaderCode = vakioVertexShaderKoodi;
        };
        glShaderSource(vs, vertexShaderCode);
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        String fragmentShaderCode = readFile(fileName + ".fs");
        if (fragmentShaderCode == null || fragmentShaderCode.equals("")){
            fragmentShaderCode = vakioFragmentShaderKoodi;
        };
        glShaderSource(fs, fragmentShaderCode);
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        program = glCreateProgram();
        glAttachShader(program, vs);
        glAttachShader(program, fs);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

	protected void destroy() throws Throwable {
		glDetachShader(program, vs);
		glDetachShader(program, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);
		glDeleteProgram(program);
	}

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform1i(location, value);
        }
    }

    public void setUniform(String uniformName, Vector4f value) {
		int location = glGetUniformLocation(program, uniformName);
		if (location != -1) {
            glUniform4f(location, value.x, value.y, value.z, value.w);
        }
	}

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void nollaaShaderEfektit() {
        setUniform("sampler", 0);
        setUniform("color", new Vector4f(0, 0, 0, 1));
        setUniform("addcolor", new Vector4f(0, 0, 0, 0));
        setUniform("subcolor", new Vector4f(0, 0, 0, 0));
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    String vakioVertexShaderKoodi =
        "uniform mat4 projection;" +
        "attribute vec3 vertices;" +
        "attribute vec2 textures;" +
        "varying vec2 tex_coords;" +
        "void main() {" +
        "    tex_coords = textures;" +
        "    gl_Position = projection * vec4(vertices, 1);" +
        "}";
    String vakioFragmentShaderKoodi =
        "uniform sampler2D sampler;" +
        "uniform vec4 color;" +
        "varying vec2 tex_coords;" +
        "void main() {" +
        "    gl_FragColor = texture2D(sampler, tex_coords) + color;" +
        "}";
    private String readFile(String fileName) {
        StringBuilder string = new StringBuilder();
        BufferedReader reader;
        String tiedostopolku = "tiedostot/shaders/" + fileName;

        try {
            File f = new File(tiedostopolku);
            FileReader fr = new FileReader(f);
            reader = new BufferedReader(fr);
            String line;
            while((line = reader.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            reader.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            tinyfd_messageBox("Virhe ladatessa shader-tiedostoa", "Ei voitu ladata tiedostoa " + tiedostopolku + "\n\nKäytetään vakiovarjostinohjelmaa.\nKaikki visuaalit eivät välttämättä toimi.", "ok", "error", false);
        }
        return string.toString();
    }
}
