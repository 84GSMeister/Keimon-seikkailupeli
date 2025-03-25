package keimo.keimoEngine.grafiikat;

import keimo.Ikkunat.KäynnistinIkkuna;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.awt.image.BufferedImage;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Tekstuuri implements Kuva {
    private int id;
    private int leveys;
    private int korkeus;

    public Tekstuuri(String tiedostoNimi) {
        if (KäynnistinIkkuna.glKäytössä) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
    
                ByteBuffer buf = stbi_load(tiedostoNimi, w, h, channels, 4);
                if (buf == null) {
                    throw new RuntimeException("Image file [" + tiedostoNimi + "] not loaded: " + stbi_failure_reason());
                }
    
                leveys = w.get();
                korkeus = h.get();
    
                generateTexture(leveys, korkeus, buf);
    
                stbi_image_free(buf);
            }
            catch (Exception e) {
                System.out.println("Tekstuurin luonti epäonnistui: " + tiedostoNimi);
                e.printStackTrace();
            }
        }
    }

    private void generateTexture(int width, int height, ByteBuffer buf) {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);
    }
    
    public void cleanup() {
        glDeleteTextures(id);
    }

    public Tekstuuri(BufferedImage kuva) {
        if (KäynnistinIkkuna.glKäytössä) {
            leveys = kuva.getWidth();
            korkeus = kuva.getHeight();

            int[] pixels_raw = new int[leveys * korkeus * 4];
            pixels_raw = kuva.getRGB(0, 0, leveys, korkeus, null, 0, leveys);
            ByteBuffer pixels = BufferUtils.createByteBuffer(leveys * korkeus * 4);

            for (int i = 0; i < leveys; i++) {
                for (int j = 0; j < korkeus; j++) {
                    try {
                        int pixel = pixels_raw[i * korkeus + j];
                        pixels.put((byte)((pixel >> 16) & 0xFF)); //RED
                        pixels.put((byte)((pixel >> 8) & 0xFF)); //GREEN
                        pixels.put((byte)(pixel & 0xFF)); //BLUE
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
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        }
    }

    // @Override
	// protected void finalize() throws Throwable {
	// 	glDeleteTextures(id);
	// 	super.finalize();
	// }

    @Override
    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }
}
