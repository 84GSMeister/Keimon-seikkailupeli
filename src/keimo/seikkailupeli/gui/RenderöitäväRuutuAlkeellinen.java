package keimo.seikkailupeli.gui;

import keimo.keimoengine.KeimoEngine;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class RenderöitäväRuutuAlkeellinen {

    static int drawCount;
    static int v_id, t_id, i_id;
    
    public static void init() {

        // Init GL
        glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glClearColor(1.0f, 0.0f, 1.0f, 0.0f); // Set the clear color

        // Init Shader
        int vs, fs, program;
        vs = glCreateShader(GL_VERTEX_SHADER);
        String vertexShaderCode =
        "uniform mat4 projection;" +
        "attribute vec4 vPosition;" +
        "attribute vec2 tPosition;" +
        "varying vec2 texCoords;" +
        "void main() {" +
        "    texCoords = tPosition;" +
        "    gl_Position = projection * vPosition;" +
        "}";
        glShaderSource(vs, vertexShaderCode);
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        String fragmentShaderCode =
        "uniform sampler2D sampler;" +
        "uniform vec4 vColor;" +
        "varying vec2 texCoords;" +
        "void main() {" +
        "    gl_FragColor = texture2D(sampler, texCoords);" +
        "}";
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

        glUseProgram(program);

        int location = glGetUniformLocation(program, "projection");
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.get(buffer);
        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }

        int location1 = glGetUniformLocation(program, "sampler");
        if (location1 != -1) {
            glUniform1i(location1, 0);
        }

        // Init Model
        float[] vertices = new float[]{
            -1f, 1f, 0, // TOP LEFT 0
            1f, 1f, 0,  // TOP RIGHT 1
            1f, -1f, 0, // BOTTOM RIGHT 2
            -1f, -1f, 0,// BOTTOM LEFT 3
        };
        int[] indices = new int[]{0, 1, 2, 2, 3, 0};

        float[] tex_coords = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};

        drawCount = indices.length;

        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW);
        
        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        buffer.flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Init texture
        luoVakioTekstuuri();
    }

    private static FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static void luoVakioTekstuuri() {
        int leveys = 2;
        int korkeus = 2;
        int id;

        int[] pixels_raw = new int[leveys * korkeus * 4];
        pixels_raw[0] = 0xFFFF00FF;
        pixels_raw[1] = 0xFF000000;
        pixels_raw[2] = 0xFF000000;
        pixels_raw[3] = 0xFFFF00FF;
        ByteBuffer pixels = BufferUtils.createByteBuffer(leveys * korkeus * 4);

        for (int i = 0; i < leveys; i++) {
            for (int j = 0; j < korkeus; j++) {
                try {
                    int pixel = pixels_raw[i * korkeus + j];
                    pixels.put((byte)((pixel >> 16) & 0xFF)); //RED
                    pixels.put((byte)((pixel >> 8) & 0xFF)); //GREEN
                    pixels.put((byte)((pixel >> 0) & 0xFF)); //BLUE
                    pixels.put((byte)((pixel >> 24) & 0xFF)); //ALPHA
                }
                catch (ArrayIndexOutOfBoundsException aioobe) {
                    System.out.println("Texture pixel index out of bounds: " + i + " " + j);
                    aioobe.printStackTrace();
                }
                
            }
        }

        pixels.flip();
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public static void loop() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Bind texture
        glActiveTexture(GL_TEXTURE0 + 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        // Draw Model
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        KeimoEngine.window.swapBuffers();

    }
}
