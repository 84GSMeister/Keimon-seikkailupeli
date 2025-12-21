package keimo.seikkailupeli.menu;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Äänet;

public class ValikkoRuutu {
    
    private static int valinta = 0;
    private static int vaihtoehtojenMäärä = 5;
    private static Tekstuuri otsikkoKuva = new Tekstuuri("tiedostot/kuvat/menu/KEIMON_logo.png");
    private static Tekstuuri valintaAloitaTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_aloita.png");
    private static Tekstuuri valintaAsetuksetTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Tekstuuri valintaEditoriTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_editori.png");
    private static Tekstuuri valintaKehittäjätTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_kehittäjät.png");
    private static Tekstuuri valintaLopetaTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_lopeta.png");
    private static Animaatio osoitinKuvake = new Animaatio("tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static MenuKomponentti logo = new MenuKomponentti(1, 0.5f, 0, 0.5f, otsikkoKuva);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -1f/10f -1f/2f, 0);
    private static MenuKomponentti valintaLabel = new MenuKomponentti(1f/2f, 1f/10f, 0, 0);

    public static void painaNäppäintä(String näppäin) {
        switch (näppäin) {
            case "ylös" -> {
                valinta--;
                if (valinta < 0) {
                    valinta = vaihtoehtojenMäärä-1;
                }
            }
            case "alas" -> {
                valinta++;
                if (valinta > vaihtoehtojenMäärä-1) {
                    valinta = 0;
                }
            }
            case "enter" -> {
                hyväksy(valinta);
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Aloita peli
                jatka();
                break;
            case 1: // Asetukset
                AsetusRuutu.pelissä = false;
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
                break;
            case 2: // Huone-editori
                KeimoEngine.valitseAktiivinenRuutu("editoriruutu_varmistus");
                break;
            case 3: // Kehittäjät
                KeimoEngine.valitseAktiivinenRuutu("kehittäjäruutu");
                break;
            case 4: // Lopeta
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public static void jatka() {
        if (!Peli.peliAloitettu) {
            KeimoEngine.valitseAktiivinenRuutu("peliruutu");
        }
        else {
            KeimoEngine.valitseAktiivinenRuutu("peliruutu");
            Pelaaja.pakotaPelaajanPysäytys();
            Peli.pause = false;
        }
        Äänet.toistaSFX("Valinta");
    }

    public static void render(Shader shader, Window window) {
        try {
            shader.bind();
            shader.nollaaShaderEfektit();
            logo.renderöi(shader, window);
            for (int i = 2; i >= -2; i--) {
                osoitinLabel.muutaOffsetY(-1f/2f +i*(1f/5f));
                osoitinLabel.päivitäSisältö(annaOsoitinKuvake(2-i));
                osoitinLabel.renderöi(shader, window);
            }

            for (int i = 2; i >= -2; i--) {
                valintaLabel.muutaOffsetY(-1f/2f +i*(1f/5f));
                valintaLabel.päivitäSisältö(annaValikkoTeksti(2-i));
                valintaLabel.renderöi(shader, window);
            }
            
        }
        catch (Exception e) {
            System.out.println("Valikkoruudun renderöinti epäonnistui.");
            e.printStackTrace();
        }
    }

    private static Tekstuuri annaValikkoTeksti(int valikkoElementti) {
        switch (valikkoElementti) {
            case 0: return valintaAloitaTekstuuri;
            case 1: return valintaAsetuksetTekstuuri;
            case 2: return valintaEditoriTekstuuri;
            case 3: return valintaKehittäjätTekstuuri;
            case 4: return valintaLopetaTekstuuri;
            default: return valintaAloitaTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }
}
