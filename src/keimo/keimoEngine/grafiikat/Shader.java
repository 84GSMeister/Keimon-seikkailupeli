package keimo.keimoEngine.grafiikat;

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
    private int vs, fs, program;

    public Shader(String fileName) {
        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(fileName + ".vs"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(fileName + ".fs"));
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

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    private String readFile(String fileName) {
        StringBuilder string = new StringBuilder();
        BufferedReader reader;

        try {
            File f = new File("tiedostot/shaders/" + fileName);
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
        }
        return string.toString();
    }
}
