package keimo.seikkailupeli.gui;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Latauspalkki;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Window;

import java.awt.Color;
import java.awt.Font;

public class LatausRuutu {

    private static Shader shader = new Shader("staattinen");
    private static Tekstuuri latausRuudunTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/lataus/latausruutu.png");
    private static Teksti latausTeksti = new Teksti("Ladataan", Color.black, 1000, 50, new Font("Calibri", Font.PLAIN, 36), false);
    private static StaattinenKomponentti taustakuvaLabel = new StaattinenKomponentti(1, 1, 0, 0, latausRuudunTekstuuri);
    private static MenuKomponentti latausTekstiLabel = new MenuKomponentti(1, 1f/16f, 0, -13f/16f, latausTeksti);
    private static Latauspalkki latauspalkki = new Latauspalkki(1, 1f/16f, 0, -15f/16f);
    
    public static void päivitäLatausTeksti(String teksti, int latausProsentti) {
        latausTeksti.päivitäTeksti(teksti + "... " + latausProsentti + "%");
        latauspalkki.päivitäLatausProsentti(latausProsentti);
    }
    
    public static void renderöiLatausRuutu(Window window, int latausProsentti) {
        shader.bind();

        taustakuvaLabel.renderöi(shader, window);
        latausTekstiLabel.renderöi(shader, window);
        latauspalkki.renderöi(shader, window);
    }
}
