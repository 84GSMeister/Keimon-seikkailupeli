package keimo.keimoengine.ruudut;

import keimo.keimoengine.assets.GUITekstuurit;
import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Latauspalkki;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;

import java.awt.Font;

public class LatausRuutu {

    private static Shader shader = new Shader("staattinen");
    private static Renderöitävä latausRuudunTekstuuri = GUITekstuurit.annaTekstuuri("latausruutu");
    private static Teksti latausTeksti = new Teksti("Ladataan", Väri.black, 1000, 50, new Font("Calibri", Font.PLAIN, 36), false);
    private static LabelKomponentti taustakuvaLabel = new LabelKomponentti(1, 1, 0, 0, latausRuudunTekstuuri);
    private static MenuKomponentti latausTekstiLabel = new MenuKomponentti(1, 1f/16f, 0, -13f/16f, latausTeksti);
    private static Latauspalkki latauspalkki = new Latauspalkki(1, 1f/16f, 0, -15f/16f);
    
    public static void päivitäLatausTeksti(String teksti, int latausProsentti) {
        latausTeksti.päivitäTeksti(teksti + "... " + latausProsentti + "%");
        latauspalkki.päivitäLatausProsentti(latausProsentti);
    }
    
    public static void renderöiLatausRuutu(Ikkuna window, int latausProsentti) {
        shader.bind();
        taustakuvaLabel.renderöi(shader, window);
        latausTekstiLabel.renderöi(shader, window);
        latauspalkki.renderöi(shader, window);
    }
}
