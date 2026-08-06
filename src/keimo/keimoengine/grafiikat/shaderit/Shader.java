package keimo.keimoengine.grafiikat.shaderit;

import keimo.keimoengine.ikkuna.DialogiIkkunat;

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

public class Shader {
    protected int vs, fs, program;
    protected float aika = 0;
    protected float loopNopeus = 0;

    public Shader(String fileName) {
        vs = glCreateShader(GL_VERTEX_SHADER);
        String vertexShaderCode = lueTiedosto(fileName + ".vs");
        if (vertexShaderCode == null || vertexShaderCode.equals("")) {
            vertexShaderCode = vakioVertexShaderKoodi;
        };
        glShaderSource(vs, vertexShaderCode);
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        String fragmentShaderCode = lueTiedosto(fileName + ".fs");
        if (fragmentShaderCode == null || fragmentShaderCode.equals("")) {
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

    public void asetaSampler(int value) {
        int location = glGetUniformLocation(program, "sampler");
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

    public void asetaSijainti(Matrix4f value) {
        int location = glGetUniformLocation(program, "projection");
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void nollaaShaderEfektit() {
        asetaSampler(0);
        setUniform("color", new Vector4f(0, 0, 0, 1));
        setUniform("addcolor", new Vector4f(0, 0, 0, 0));
        setUniform("subcolor", new Vector4f(0, 0, 0, 0));
    }

    public void loop() {
        int location = glGetUniformLocation(program, "time");
        glUniform1f(location, aika);
        aika += loopNopeus;
    }

    public void loop(float nopeus) {
        int location = glGetUniformLocation(program, "time");
        glUniform1f(location, aika);
        aika += nopeus;
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
    private String lueTiedosto(String fileName) {
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
            DialogiIkkunat.viestiIkkuna("Virhe ladatessa shader-tiedostoa", "Ei voitu ladata tiedostoa " + tiedostopolku + "\n\nKäytetään vakiovarjostinohjelmaa.\nKaikki visuaalit eivät välttämättä toimi.", "ok", "error", false);
        }
        return string.toString();
    }
}
