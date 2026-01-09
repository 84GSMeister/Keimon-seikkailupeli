package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.awt.Color;

public class HUD_Kartta {
    private static Teksti alueTeksti = new Teksti("Alue", Color.black, 192, 48, KeimoFontit.fontti_keimo_36, true);
    //private static Tekstuuri karttaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/kartta_pohja.png");
    private static Tekstuuri karttaTekstuuri;
    private static Teksti huoneTeksti = new Teksti("Huone", Color.black, 192, 48, KeimoFontit.fontti_keimo_36, true);
    private static Tekstuuri pelaajaKartallaKuvake = new Tekstuuri("tiedostot/kuvat/pelaaja_og.png");

    
    private static Tekstuuri karttaAsuintalotTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/asuintalot.png");
    private static Tekstuuri karttaBaariTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/baari.png");
    private static Tekstuuri karttaBaariSalahuoneTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/baari_salahuone.png");
    private static Tekstuuri karttaKauppaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/kauppa.png");
    private static Tekstuuri karttaKotiTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/koti.png");
    private static Tekstuuri karttaKuuTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/kuu.png");
    private static Tekstuuri karttaMetsäTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/metsä.png");
    private static Tekstuuri karttaMetsäBossTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/metsä_boss.png");
    private static Tekstuuri karttaPeltoTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/pelto.png");
    private static Tekstuuri karttaPuistoTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/puisto.png");
    private static Tekstuuri karttaTemppeliTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/temppeli.png");
    private static Tekstuuri karttaTemppeliBossTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/temppeli_boss.png");
    private static Tekstuuri karttaYokyläTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/yo-kylä.png");
    private static Tekstuuri eiKarttaaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/ei_karttaa.png");

    private static StaattinenKomponentti alueLabel = new StaattinenKomponentti(1f/7.5f, 1f/18f, 5f/6f, 2f/3f + 2f/9f, alueTeksti);
    private static StaattinenKomponentti karttaLabel = new StaattinenKomponentti(1f/7.5f, 1f/6f, 5f/6f, 2f/3f, karttaTekstuuri);
    private static StaattinenKomponentti huoneLabel = new StaattinenKomponentti(1f/7.5f, 1f/18f, 5f/6f, 2f/3f - 2f/9f, huoneTeksti);
    private static StaattinenKomponentti pelaajanKuvakeLabel = new StaattinenKomponentti(1f/32f, 1f/32f, 5f/6f, 2f/3f, pelaajaKartallaKuvake);

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        shader.setUniform("sampler", 0);

        alueTeksti.päivitäTeksti(Peli.huone.annaAlue(), 1, 1);
        alueLabel.renderöi(shader, window);

        switch (Peli.huone.annaNimi()) {
            case "Asuintalot": karttaTekstuuri = karttaAsuintalotTekstuuri; break;
            case "Baari_salahuone": karttaTekstuuri = karttaBaariSalahuoneTekstuuri; break;
            case "Jatkuva_puisto": karttaTekstuuri = karttaPuistoTekstuuri; break;
            case "Kauppa": karttaTekstuuri = karttaKauppaTekstuuri; break;
            case "Keimo-baari": karttaTekstuuri = karttaBaariTekstuuri; break;
            case "Koti": karttaTekstuuri = karttaKotiTekstuuri; break;
            case "Kuu": karttaTekstuuri = karttaKuuTekstuuri; break;
            case "Metsä": karttaTekstuuri = karttaMetsäTekstuuri; break;
            case "Metsä_boss": karttaTekstuuri = karttaMetsäBossTekstuuri; break;
            case "Pelto": karttaTekstuuri = karttaPeltoTekstuuri; break;
            case "Temppeli": karttaTekstuuri = karttaTemppeliTekstuuri; break;
            case "Temppeli_boss": karttaTekstuuri = karttaTemppeliBossTekstuuri; break;
            case "Yo-kylä_Itä": karttaTekstuuri = karttaYokyläTekstuuri; break;
            default: karttaTekstuuri = eiKarttaaTekstuuri; break;
        }
        karttaLabel.päivitäSisältö(karttaTekstuuri);
        karttaLabel.renderöi(shader, window);

        huoneTeksti.päivitäTeksti(Peli.huone.annaNimi(), 1, 1);
        huoneLabel.renderöi(shader, window);

        float pelaajanSijXRelatiivinen = (float)(Pelaaja.hitbox.getCenterX() - Peli.huone.annaKoko() * 32f);
        float pelaajanSijYRelatiivinen = (float)(Pelaaja.hitbox.getCenterY() - Peli.huone.annaKoko() * 32f);
        pelaajanKuvakeLabel.muutaOffsetX(5f/6f + pelaajanSijXRelatiivinen/(float)Peli.huone.annaKoko()/64f /4f);
        pelaajanKuvakeLabel.muutaOffsetY(2f/3f - pelaajanSijYRelatiivinen/(float)Peli.huone.annaKoko()/64f /3f);
        pelaajanKuvakeLabel.renderöi(shader, window);
    }
}
