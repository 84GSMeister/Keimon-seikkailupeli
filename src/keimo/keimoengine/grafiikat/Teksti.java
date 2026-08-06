package keimo.keimoengine.grafiikat;

import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.fontit.Väri;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 * TODO: Optimoi tekstin uudelleenrenderöinti niin, että uutta tekstuuria ei luoda joka kerta kun teksti päivittyy.
 */

public class Teksti extends Renderöitävä {
    
    private int alkuLeveys;
    private Väri väri;
    private Font fontti;
    private int fonttiKoko;
    private String edellinenTeksti = "";

    private BufferedImage b;
    private Graphics2D g;

    public static final int LEIKKAA = 0;
    public static final int VENYTÄ = 1;
    public static final int RIVITÄ = 2;
    public static final int RIVITÄ_JA_SCROLLAA = 3;

    public Teksti(String teksti, int leveys, int korkeus) {
        this(teksti, Väri.BLACK, leveys, korkeus);
    }

    public Teksti(String teksti, Väri väri, int leveys, int korkeus) {
        this(teksti, väri, leveys, korkeus, KeimoFontit.fontti_keimo_36, false);
    }

    public Teksti(String teksti, Väri väri, int leveys, int korkeus, Font fontti, boolean keskitäY) {
        try {
            this.leveys = leveys;
            this.korkeus = korkeus;
            this.alkuLeveys = leveys;
            this.väri = väri;
            this.fontti = fontti;
            this.fonttiKoko = fontti.getSize();
            this.edellinenTeksti = teksti;
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
                if (Thread.currentThread().getName().equals("Keimo Engine -Renderöintisäie")) {
                    luoTekstuuri(leveys, korkeus, pixels, GL_LINEAR);
                }
                else throw new VääräSäieException();
            }
        }
        catch (VääräSäieException e) {
            String säieNimi = Thread.currentThread().getName();
            System.out.println("Tekstin luonti epäonnistui. " + säieNimi + " ei voi luoda graafisia objekteja. Teksti: " + teksti);
            e.printStackTrace();
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
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille varatun alueen vaakasuunnassa.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ; 3 = RIVITÄ JA SCROLLAA
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi) {
        if (this.fonttiKoko > 0) päivitäTeksti(teksti, tekstiTyyppi, leveys/this.fonttiKoko +1);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille varatun alueen vaakasuunnassa.
     * Rivittäessä tekstin korkeus tulee olla oikein määritelty; Fonttikoolla 36: 1 rivi - korkeus 48, 2 riviä - korkeus 96 jne.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ; 3 = RIVITÄ JA SCROLLAA
     * @param minimiLeveys Kuinka monta merkkiä vaaditaan ennen kuin tekstiä aletaan rivittämään/venyttämään. Ei vaikuta tyypillä 0.
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi, int minimiLeveys) {
        päivitäTeksti(teksti, tekstiTyyppi, minimiLeveys, this.väri);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille varatun alueen vaakasuunnassa.
     * Rivittäessä tekstin korkeus tulee olla oikein määritelty; Fonttikoolla 36: 1 rivi - korkeus 48, 2 riviä - korkeus 96 jne.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ; 3 = RIVITÄ JA SCROLLAA
     * @param minimiLeveys Kuinka monta merkkiä vaaditaan ennen kuin tekstiä aletaan rivittämään/venyttämään. Ei vaikuta tyypillä 0.
     * @param color Tekstin väri - Käytä AWT:n mukaisia värejä.
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi, int minimiLeveys, Väri color) {
        päivitäTeksti(teksti, tekstiTyyppi, minimiLeveys, 6, color, 0, 0);
    }

    /**
     * Tekstin tyypillä määritetään, mitä tehdään, kun tekstin pituus ylittää sille varatun alueen vaakasuunnassa.
     * Rivittäessä tekstin korkeus tulee olla oikein määritelty; Fonttikoolla 36: 1 rivi - korkeus 48, 2 riviä - korkeus 96 jne.
     * @param teksti Renderöitävä teksti
     * @param tekstiTyyppi 0 = LEIKKAA; 1 = VENYTÄ; 2 = RIVITÄ; 3 = RIVITÄ JA SCROLLAA
     * @param minimiLeveys Kuinka monta merkkiä vaaditaan ennen kuin tekstiä aletaan rivittämään/venyttämään. Ei vaikuta tyypillä 0.
     * @param riviMäärä Kuinka monta riviä tekstiä näytetään kerrallaan ennen scrollausta. Vaikuttaa vain tyypillä 3.
     * @param color Tekstin väri - Käytä AWT:n mukaisia värejä.
     * @param offsetX Tyhjiä pikseleitä vasemmalla ennen tekstiä.
     * @param offsetY Tyhjiä pikseleitä ylhäällä ennen tekstiä.
     */

    public void päivitäTeksti(String teksti, int tekstiTyyppi, int minimiLeveys, int riviMäärä, Väri color, int offsetX, int offsetY) {
        if (teksti != null && !teksti.equals(edellinenTeksti) && fontti != null) {
            poistaTekstuuri(id);
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
                        if (tulostettava.contains("\n")) {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (tulostettava.contains("\\n")) {
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
                        if (tulostettava.contains("\n")) {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (tulostettava.contains("\\n")) {
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
                    int merkkejäEnnenVäliä = 0;
                    String merkitEnnenVäliä = "";
                    int rivit = 0;
                    for (int i = 0; i < teksti.length(); i++) {
                        tulostettava += teksti.charAt(i);
                        if (teksti.charAt(i) == ' ') {
                            merkkejäEnnenVäliä = 0;
                            merkitEnnenVäliä = "";
                        }
                        else {
                            merkkejäEnnenVäliä++;
                            merkitEnnenVäliä += teksti.charAt(i);
                        }

                        if (tulostettava.contains("\n")) {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if (tulostettava.contains("\\n")) {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-2);
                            g.drawString(tulostettava, offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            tulostettava = "";
                            rivit++;
                        }
                        else if ((tulostettava.length() > minimiLeveys)) {
                            if (merkkejäEnnenVäliä > minimiLeveys) {
                                g.drawString(tulostettava.substring(0, minimiLeveys), offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                                if (merkitEnnenVäliä.length() >= 1) {
                                    merkitEnnenVäliä = merkitEnnenVäliä.substring(merkitEnnenVäliä.length()-1, merkitEnnenVäliä.length());
                                }
                            }
                            else {
                                g.drawString(tulostettava.substring(0, minimiLeveys - merkkejäEnnenVäliä), offsetX, (int)((rivit+1) * fonttiKoko) + offsetY);
                            }
                            tulostettava = merkitEnnenVäliä;
                            merkitEnnenVäliä = "";
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
                case RIVITÄ_JA_SCROLLAA -> {
                    String tulostettava = "";
                    int merkkejäEnnenVäliä = 0;
                    String merkitEnnenVäliä = "";
                    int rivit = 0;
                    int scroll = 0;
                    for (int i = 0; i < teksti.length(); i++) {
                        tulostettava += teksti.charAt(i);
                        if (teksti.charAt(i) == ' ') {
                            merkkejäEnnenVäliä = 0;
                            merkitEnnenVäliä = "";
                        }
                        else {
                            merkkejäEnnenVäliä++;
                            merkitEnnenVäliä += teksti.charAt(i);
                        }

                        if (tulostettava.contains("\n")) {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-1);
                            rivit++;
                            if (rivit >= riviMäärä) scroll = rivit - riviMäärä +1;
                            else scroll = 0;
                            g.drawString(tulostettava, offsetX, (int)((rivit - scroll) * fonttiKoko) + offsetY);
                            tulostettava = "";
                        }
                        else if (tulostettava.contains("\\n")) {
                            tulostettava = tulostettava.substring(0, tulostettava.length()-2);
                            rivit++;
                            if (rivit >= riviMäärä) scroll = rivit - riviMäärä +1;
                            else scroll = 0;
                            g.drawString(tulostettava, offsetX, (int)((rivit - scroll) * fonttiKoko) + offsetY);
                            tulostettava = "";
                        }
                        else if ((tulostettava.length() > minimiLeveys)) {
                            
                            rivit++;
                            if (rivit >= riviMäärä) scroll = rivit - riviMäärä +1;
                            else scroll = 0;

                            if (merkkejäEnnenVäliä > minimiLeveys) {
                                g.drawString(tulostettava.substring(0, minimiLeveys), offsetX, (int)((rivit - scroll) * fonttiKoko) + offsetY);
                                if (merkitEnnenVäliä.length() >= 1) {
                                    merkitEnnenVäliä = merkitEnnenVäliä.substring(merkitEnnenVäliä.length()-1, merkitEnnenVäliä.length());
                                }
                            }
                            else {
                                g.drawString(tulostettava.substring(0, minimiLeveys - merkkejäEnnenVäliä), offsetX, (int)((rivit - scroll) * fonttiKoko) + offsetY);
                            }
                            tulostettava = merkitEnnenVäliä;
                            merkitEnnenVäliä = "";

                            if (i == teksti.length()-1) {
                                g.drawString(tulostettava, offsetX, (int)((rivit +1 - scroll) * fonttiKoko) + offsetY);
                            }
                        }

                        else if (i == teksti.length()-1) {
                            g.drawString(tulostettava, offsetX, (int)((rivit +1 - scroll) * fonttiKoko) + offsetY);
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
            luoTekstuuri(leveys, korkeus, pixels, GL_LINEAR);
            edellinenTeksti = teksti;
        }
    }

    @Override
    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }
}
