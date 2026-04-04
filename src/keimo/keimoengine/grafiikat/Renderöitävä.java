package keimo.keimoengine.grafiikat;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.*;

/**
 * Yläluokka kaikille objekteille, joita voidaan käyttää renderöitävänä tekstuurina.
 */

public abstract class Renderöitävä implements GLRenderöitävä {

    protected int id;
    protected int leveys;
    protected int korkeus;
    public static int tekstuureja = 0;

    protected void luoTekstuuri(int leveys, int korkeus, ByteBuffer buf, int filter) {
        if (Thread.currentThread().getName().equals("Keimo Engine -Renderöintisäie")) {
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            glGenerateMipmap(GL_TEXTURE_2D);
            tekstuureja++;
        }
        else {

        }
    }

    protected void poistaTekstuuri(int id) {
        glDeleteTextures(id);
        tekstuureja--;
    }

    protected void luoVakioTekstuuri() {
        leveys = 2;
        korkeus = 2;

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
        luoTekstuuri(leveys, korkeus, pixels, GL_NEAREST);
    }

    public class VääräSäieException extends Exception {
        
    }
}
