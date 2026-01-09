package keimo.keimoengine.grafiikat;

import keimo.keimoengine.fontit.KeimoFontit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.*;

/**
 * TODO: Optimoi tekstin uudelleenrenderöinti niin, että uutta tekstuuria ei luoda joka kerta kun teksti päivittyy.
 */

public class Teksti implements Renderöitävä {
    
    private int id;
    private int leveys, korkeus;
    private int alkuLeveys;
    private Color väri;
    private Font fontti;
    private int fonttiKoko;
    private String edellinenTeksti = "";

    private BufferedImage b;
    private Graphics2D g;

    public static final int LEIKKAA = 0;
    public static final int VENYTÄ = 1;
    public static final int RIVITÄ = 2;

    public Teksti(String teksti, int leveys, int korkeus) {
        this(teksti, Color.BLACK, leveys, korkeus);
    }

    public Teksti(String teksti, Color väri, int leveys, int korkeus) {
        this(teksti, väri, leveys, korkeus, KeimoFontit.fontti_keimo_36, false);
    }

    public Teksti(String teksti, Color väri, int leveys, int korkeus, Font fontti, boolean keskitäY) {
        try {
            this.leveys = leveys;
            this.korkeus = korkeus;
            this.alkuLeveys = leveys;
            this.väri = väri;
            this.fontti = fontti;
            this.fonttiKoko = fontti.getSize();
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
                            g.drawString(tulostettava, 0, (int)(i * fonttiKoko));
                            teksti = teksti.substring(i);
                            tulostettava = "";
                        }
                    }
                }
                else {
                    if (keskitäY) {
                        g.drawString(teksti, 0, fonttiKoko + (korkeus-(fonttiKoko*1.25f))/2);
                    }
                    else g.drawString(teksti, 0, fonttiKoko);
                }

                int[] pixels_raw = new int[leveys * korkeus * 4];
                pixels_raw = b.getRGB(0, 0, leveys, korkeus, null, 0, leveys);
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
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
            }
        }
        catch (Exception e) {
            System.out.println("Tekstin luonti epäonnistui. Ongelma fonttitiedostossa? Teksti: " + teksti + "; Luodaan vakiotekstuuri.");
            luoVakioTekstuuri();
            e.printStackTrace();
        }
    }

    public int annaLeveys() {
        return leveys;
    }

    public int annaKorkeus() {
        return korkeus;
    }

    public String annaTeksti() {
        return edellinenTeksti;
    }

    public void päivitäTeksti(String teksti) {
        päivitäTeksti(teksti, 0);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille määritellyn alueen.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi) {
        if (this.fonttiKoko > 0) päivitäTeksti(teksti, tekstiTyyppi, leveys/this.fonttiKoko +1);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille määritellyn alueen.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ
     * @param minimiLeveys Kuinka monta merkkiä vaaditaan ennen kuin tekstiä aletaan rivittämään/venyttämään. Ei vaikuta tyypillä 0 tai 3.
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi, int minimiLeveys) {
        päivitäTeksti(teksti, tekstiTyyppi, minimiLeveys, this.väri);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille määritellyn alueen.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ
     * @param minimiLeveys Kuinka monta merkkiä vaaditaan ennen kuin tekstiä aletaan rivittämään/venyttämään. Ei vaikuta tyypillä 0 tai 3.
     * @param color Tekstin väri - Käytä AWT:n mukaisia värejä.
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi, int minimiLeveys, Color color) {
        päivitäTeksti(teksti, tekstiTyyppi, minimiLeveys, color, 0, 0);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille määritellyn alueen.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ
     * @param minimiLeveys Kuinka monta merkkiä vaaditaan ennen kuin tekstiä aletaan rivittämään/venyttämään. Ei vaikuta tyypillä 0 tai 3.
     * @param color Tekstin väri - Käytä AWT:n mukaisia värejä.
     * @param offsetX Tyhjiä pikseleitä vasemmalla ennen tekstiä.
     * @param offsetY Tyhjiä pikseleitä ylhäällä ennen tekstiä.
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi, int minimiLeveys, Color color, int offsetX, int offsetY) {
        if (teksti != null && !teksti.equals(edellinenTeksti) && fontti != null) {
            glDeleteTextures(id);
            BufferedImage b = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = b.createGraphics();

            g.setBackground(new Color(0, 0, 0, 0));
            g.clearRect(0, 0, this.leveys, this.korkeus);
            g.setColor(color);
            g.setFont(this.fontti);
            g.setClip(0, 0, this.leveys, this.korkeus);

            switch (tekstiTyyppi) {
                case LEIKKAA -> {
                    String tulostettava = "";
                    int rivit = 0;
                    for (int i = 0; i < teksti.length(); i++) {
                        tulostettava += teksti.charAt(i);
                        if (tulostettava.contains("\n"))  {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (tulostettava.contains("\\n"))  {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-2);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (i == teksti.length()-1) {
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                        }
                    }
                }
                case VENYTÄ -> {
                    if (teksti.length() > minimiLeveys-1) {
                        leveys = teksti.length() * fonttiKoko;
                    }
                    else if (teksti.length() < minimiLeveys-1) {
                        leveys = alkuLeveys + fonttiKoko;
                    }
                    b = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_4BYTE_ABGR);
                    g = b.createGraphics();
                    g.setBackground(new Color(0, 0, 0, 0));
                    g.clearRect(0, 0, this.leveys, this.korkeus);
                    g.setColor(color);
                    g.setFont(this.fontti);
                    g.setClip(0, 0, this.leveys, this.korkeus);

                    String tulostettava = "";
                    int rivit = 0;
                    for (int i = 0; i < teksti.length(); i++) {
                        tulostettava += teksti.charAt(i);
                        if (tulostettava.contains("\n"))  {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (tulostettava.contains("\\n"))  {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-2);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (i == teksti.length()-1) {
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                        }
                    }
                }
                case RIVITÄ -> {
                    String tulostettava = "";
                    int merkkejäVälinJälkeen = 0;
                    String merkitVälinJälkeen = "";
                    int rivit = 0;
                    for (int i = 0; i < teksti.length(); i++) {
                        tulostettava += teksti.charAt(i);
                        if (teksti.charAt(i) == ' ') {
                            merkkejäVälinJälkeen = 0;
                            merkitVälinJälkeen = "";
                        }
                        else {
                            merkkejäVälinJälkeen++;
                            merkitVälinJälkeen += teksti.charAt(i);
                        }

                        if (tulostettava.contains("\n"))  {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (tulostettava.contains("\\n"))  {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-2);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if ((tulostettava.length() > minimiLeveys) && tekstiTyyppi == RIVITÄ) {
                            if (merkkejäVälinJälkeen > minimiLeveys) {
                                g.drawString(tulostettava.substring(0, minimiLeveys), offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            }
                            else {
                                g.drawString(tulostettava.substring(0, minimiLeveys - merkkejäVälinJälkeen), offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            }
                            tulostettava = merkitVälinJälkeen;
                            merkitVälinJälkeen = "";
                            rivit++;
                            if (i == teksti.length()-1) {
                                g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            }
                        }
                        else if (i == teksti.length()-1) {
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                        }
                    }
                }
            }

            int[] pixels_raw = new int[leveys * korkeus * 4];
            pixels_raw = b.getRGB(0, 0, leveys, korkeus, null, 0, leveys);
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
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, leveys, korkeus, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
            edellinenTeksti = teksti;
        }
    }

    private void luoVakioTekstuuri() {
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
