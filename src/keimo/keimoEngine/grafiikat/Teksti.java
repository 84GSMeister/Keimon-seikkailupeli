package keimo.keimoEngine.grafiikat;

import keimo.Utility.KeimoFontit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Teksti implements Kuva {
    
    private int id;
    private int leveys, korkeus;
    private Color väri;
    private Font fontti;
    private String edellinenTeksti = "";

    BufferedImage b;
    Graphics2D g;

    public Teksti(String teksti, int leveys, int korkeus) {
        this(teksti, Color.BLACK, leveys, korkeus);
    }

    public Teksti(String teksti, Color väri, int leveys, int korkeus) {
        this(teksti, väri, leveys, korkeus, KeimoFontit.fontti_keimo_12);
    }

    public Teksti(String teksti, Color väri, int leveys, int korkeus, Font fontti) {
        try {
            this.leveys = leveys;
            this.korkeus = korkeus;
            this.väri = väri;
            this.fontti = fontti;
            if (leveys > 0 && korkeus > 0) {
                this.b = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_4BYTE_ABGR);
                this.g = this.b.createGraphics();
                
                g.setBackground(new Color(0, 0, 0, 0));
                g.clearRect(0, 0, leveys, korkeus);
                g.setColor(this.väri);
                g.setFont(fontti);
                g.setClip(0, 0, leveys, korkeus);
                if (teksti.length() > 40) {
                    String tulostettava = "";
                    for (int i = 0; i < teksti.length(); i++) {
                        tulostettava += teksti.charAt(i);
                        if (tulostettava.length() > 40 && teksti.charAt(i) == ' ') {
                            g.drawString(tulostettava, 0, (int)(20 + i * 15));
                            teksti = teksti.substring(i);
                            tulostettava = "";
                        }
                    }
                }
                else g.drawString(teksti, 0, 10);

                int[] pixels_raw = new int[leveys * korkeus * 4];
                pixels_raw = b.getRGB(0, 0, leveys, korkeus, null, 0, leveys);
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
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
            }
        }
        catch (Exception e) {
            System.out.println("Tekstuurin luonti epäonnistui. Tiedosto: " + teksti);
            e.printStackTrace();
        }
    }

    public void päivitäTeksti(String teksti) {
        päivitäTeksti(teksti, false);
    }

    public void päivitäTeksti(String teksti, boolean korjaaLeveys) {
        päivitäTeksti(teksti, korjaaLeveys, 36);
    }

    public void päivitäTeksti(String teksti, boolean korjaaLeveys, int rivinvaihtoLeveys) {
        päivitäTeksti(teksti, korjaaLeveys, rivinvaihtoLeveys, this.väri);
    }

    public void päivitäTeksti(String teksti, boolean korjaaLeveys, int rivinvaihtoLeveys, Color color) {
        päivitäTeksti(teksti, korjaaLeveys, rivinvaihtoLeveys, color, 0, 0);
    }

    public void päivitäTeksti(String teksti, boolean korjaaLeveys, int rivinvaihtoLeveys, Color color, int offsetX, int offsetY) {
        if (!teksti.equals(edellinenTeksti)) {
            glDeleteTextures(id);

            if (korjaaLeveys) leveys = teksti.length() * 20;
            BufferedImage b = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = b.createGraphics();

            g.setBackground(new Color(0, 0, 0, 0));
            g.clearRect(0, 0, leveys, korkeus);
            g.setColor(color);
            g.setFont(this.fontti);
            g.setFont(fontti);
            g.setClip(0, 0, leveys, korkeus);
            if (teksti.length() > 36) {
                String tulostettava = "";
                int rivit = 0;
                for (int i = 0; i < teksti.length(); i++) {
                    tulostettava += teksti.charAt(i);
                    if ((tulostettava.length() > rivinvaihtoLeveys && teksti.charAt(i) == ' ') || tulostettava.contains("\\n") || tulostettava.contains("\n")) {
                        if (tulostettava.contains("\\n")) tulostettava = tulostettava.substring(0, tulostettava.length()-2);
                        g.drawString(tulostettava, offsetX, (int)(10 + rivit * 15) + offsetY);
                        tulostettava = "";
                        rivit++;
                    }
                    else if (i == teksti.length()-1) {
                        g.drawString(tulostettava, offsetX, (int)(10 + rivit * 15) + offsetY);
                    }
                }
            }
            else g.drawString(teksti, offsetX, 10 + offsetY);

            int[] pixels_raw = new int[leveys * korkeus * 4];
            pixels_raw = b.getRGB(0, 0, leveys, korkeus, null, 0, leveys);
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
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
            edellinenTeksti = teksti;
        }
    }

    public void päivitäTekstiDialogi(String teksti) {
        glDeleteTextures(id);

        
        leveys = teksti.length() * 20;
        if (leveys < 450) leveys = 450;
        else if (leveys > 540) leveys = 540;
        BufferedImage b = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = b.createGraphics();

        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, leveys, korkeus);
        g.setColor(this.väri);
        g.setFont(KeimoFontit.fontti_keimo_12);
        g.setClip(0, 0, leveys, korkeus);
        if (teksti.length() > 36) {
            String tulostettava = "";
            int rivit = 0;
            for (int i = 0; i < teksti.length(); i++) {
                tulostettava += teksti.charAt(i);
                if ((tulostettava.length() > 36 && teksti.charAt(i) == ' ') || tulostettava.contains("\n")) {
                    g.drawString(tulostettava, 0, (int)(20 + rivit * 15));
                    tulostettava = "";
                    rivit++;
                }
                else if (i == teksti.length()-1) {
                    g.drawString(tulostettava, 0, (int)(20 + rivit * 15));
                }
            }
        }
        else g.drawString(teksti, 0, 10);

        int[] pixels_raw = new int[leveys * korkeus * 4];
        pixels_raw = b.getRGB(0, 0, leveys, korkeus, null, 0, leveys);
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
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
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
