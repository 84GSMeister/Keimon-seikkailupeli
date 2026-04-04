package keimo.keimoengine.grafiikat;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.awt.image.BufferedImage;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

/**
 * Tekstuuri-luokka hoitaa tekstuurien lataamisen ja käsittelyn OpenGL:lle.
 * Jos tekstuurin lataaminen epäonnistuu, luodaan vakiotekstuuri, joka on magentan ja mustan ruudukkokuvio.
 */

public class Tekstuuri extends Renderöitävä {

    public Tekstuuri(String tiedostoNimi) {
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

            if (Thread.currentThread().getName().equals("Keimo Engine -Renderöintisäie")) {
                luoTekstuuri(leveys, korkeus, buf, GL_NEAREST);
            }
            else throw new VääräSäieException();
            stbi_image_free(buf);
        }
        catch (VääräSäieException e) {
            String säieNimi = Thread.currentThread().getName();
            System.out.println("Tekstuurin luonti epäonnistui. " + säieNimi + " ei voi luoda graafisia objekteja. Tekstuuri: " + tiedostoNimi);
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
            luoVakioTekstuuri();
        }
    }
    
    public void cleanup() {
        glDeleteTextures(id);
    }

    public Tekstuuri(BufferedImage kuva) {
        try {
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
            
            if (Thread.currentThread().getName().equals("Keimo Engine -Renderöintisäie")) {
                luoTekstuuri(leveys, korkeus, pixels, GL_NEAREST);
            }
            else throw new VääräSäieException();
        }
        catch (VääräSäieException e) {
            String säieNimi = Thread.currentThread().getName();
            System.out.println("Tekstuurin luonti epäonnistui. " + säieNimi + " ei voi luoda graafisia objekteja. Tekstuuri on todennäköisesti luotu osana animaatio-objektia.");
            e.printStackTrace();
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
