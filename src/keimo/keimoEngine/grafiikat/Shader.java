package keimo.keimoEngine.grafiikat;

import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.Kamera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int vs, fs, program;
    private int uniformMatProjection, uniformMatTransformWorld, uniformMatTransformObject;

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

        uniformMatProjection = glGetUniformLocation(program, "projection");
        uniformMatTransformWorld = glGetUniformLocation(program, "transformWorld");
        uniformMatTransformObject = glGetUniformLocation(program, "transformObject");
    }

	protected void destroy() throws Throwable {
		glDetachShader(program, vs);
		glDetachShader(program, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);
		glDeleteProgram(program);
	}

    public void setCamera(Kamera kamera) {
        if (uniformMatProjection != 1) {
            float[] matrix = new float[16];
            kamera.getProjection().get(matrix);
            glUniformMatrix4fv(uniformMatProjection, false, matrix);
        }
        if (uniformMatTransformWorld != 1) {
            float[] matrix = new float[16];
            kamera.getTransformation().get(matrix);
            glUniformMatrix4fv(uniformMatTransformWorld, false, matrix);
        }
    }

    public void setTransform(Transform3D transform) {
        if (uniformMatTransformObject != 1) {
            float[] matrix = new float[16];
            transform.getTransformation().get(matrix);
            glUniformMatrix4fv(uniformMatTransformObject, false, matrix);
        }
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
