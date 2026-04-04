package keimo.seikkailupeli.gui.hud;

import java.awt.Color;
import java.text.DecimalFormat;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import keimo.keimoengine.Kello;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.gui.toimintoIkkunat.PullonPalautusIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.ÄmpäriJonoIkkuna;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.toiminnot.Dialogit;

public class HUD {

    private static Shader peliShader = new Shader("shader");
    private static Shader guiShader = new Shader("staattinen");
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");
    public static boolean hudGrafiikatAlustettu = false;

    // Pohja
    private static Renderöitävä taustaOhjeTekstuuri = Assets.annaTekstuuri("hud_paneeli_tyhjä");
    private static Renderöitävä taustaTavaraluetteloTekstuuri = Assets.annaTekstuuri("hud_paneeli_tavaraluettelo");
    private static StaattinenKomponentti hudPohjaVasenYläLabel = new StaattinenKomponentti(1f/6f, 1f/3f, -5f/6f, 2f/3f, taustaOhjeTekstuuri);
    private static StaattinenKomponentti hudPohjaVasenKeskiLabel = new StaattinenKomponentti(1f/6f, 1f/3f, -5f/6f, 0, taustaOhjeTekstuuri);
    private static StaattinenKomponentti hudPohjaVasenAlaLabel = new StaattinenKomponentti(1f/6f, 1f/3f, -5f/6f, -2f/3f, taustaTavaraluetteloTekstuuri);
    private static StaattinenKomponentti hudPohjaOikeaYläLabel = new StaattinenKomponentti(1f/6f, 1f/3f, 5f/6f, 2f/3f, taustaOhjeTekstuuri);
    private static StaattinenKomponentti hudPohjaOikeaKeskiLabel = new StaattinenKomponentti(1f/6f, 1f/3f, 5f/6f, 0, taustaOhjeTekstuuri);
    private static StaattinenKomponentti hudPohjaOikeaAlaLabel = new StaattinenKomponentti(1f/6f, 1f/3f, 5f/6f, -2f/3f, taustaOhjeTekstuuri);

    // HP & Juomat
    private static Renderöitävä hpTekstuuri = Assets.annaTekstuuri("hud_hp");
    private static Renderöitävä juomatTekstuuri = Assets.annaTekstuuri("hud_juomat");
    private static Teksti hpTeksti = new Teksti("HP", Color.black, 100, 48, KeimoFontit.fontti_keimo_36, true);
    private static Teksti juomatTeksti = new Teksti("Juomat", Color.black, 200, 48, KeimoFontit.fontti_keimo_36, true);
    private static Renderöitävä ruokaTekstuuri = Assets.annaTekstuuri("hud_ruoka");
    private static Renderöitävä pelaajaStatus0Tekstuuri = Assets.annaTekstuuri("hud_pelaaja0");
    private static Renderöitävä pelaajaStatus1Tekstuuri = Assets.annaTekstuuri("hud_pelaaja1");
    private static Renderöitävä pelaajaStatus2Tekstuuri = Assets.annaTekstuuri("hud_pelaaja2");
    private static Renderöitävä pelaajaStatus3Tekstuuri = Assets.annaTekstuuri("hud_pelaaja3");
    private static Renderöitävä pelaajaStatus4Tekstuuri = Assets.annaTekstuuri("hud_pelaaja_ylensyönti");
    private static Teksti syödytRuoatTeksti = new Teksti("" + Pelaaja.syödytRuoat, Color.black, 100, 48, KeimoFontit.fontti_keimo_36, true);
    private static StaattinenKomponentti hpKuvakeLabel = new StaattinenKomponentti(1f/24f, 1f/15f, -22f/24f, 5f/6f, hpTekstuuri);
    private static StaattinenKomponentti hpTekstiLabel = new StaattinenKomponentti(1f/12f, 1f/15f, -19f/24f, 5f/6f, hpTeksti);
    private static StaattinenKomponentti juomatKuvakeLabel = new StaattinenKomponentti(1f/24f, 1f/15f, -22f/24f, 4f/6f, juomatTekstuuri);
    private static StaattinenKomponentti juomatTekstiLabel = new StaattinenKomponentti(1f/12f, 1f/15f, -19f/24f, 4f/6f, juomatTeksti);
    private static StaattinenKomponentti pelaajaKuvakeLabel = new StaattinenKomponentti(1f/24f, 1f/15f, -22f/24f, 3f/6f, pelaajaStatus0Tekstuuri);
    private static StaattinenKomponentti ruokaKuvakeLabel = new StaattinenKomponentti(1f/24f, 1f/15f, -22f/24f, 3f/6f, ruokaTekstuuri);
    private static StaattinenKomponentti ruokaTekstiLabel = new StaattinenKomponentti(1f/12f, 1f/15f, -19f/24f, 3f/6f, syödytRuoatTeksti);

    // Statsit
    private static Renderöitävä aikaTekstuuri = Assets.annaTekstuuri("hud_aika");
    private static Renderöitävä rahaTekstuuri = Assets.annaTekstuuri("hud_rahet");
    private static Renderöitävä tölksTekstuuri = Assets.annaTekstuuri("hud_tölks");
    private static Teksti aikaTeksti = new Teksti("aika", Color.black, 500, 100, KeimoFontit.fontti_keimo_36, true);
    private static Teksti rahaTeksti = new Teksti("" + Pelaaja.raha, Color.black, 180, 100, KeimoFontit.fontti_keimo_36, true);
    private static Teksti tölksTeksti = new Teksti("" + Pelaaja.kuparit, Color.black, 120, 100, KeimoFontit.fontti_keimo_36, true);
    private static StaattinenKomponentti aikaKuvakeLabel = new StaattinenKomponentti(1f/24f, 1f/15f, -22f/24f, 1f/6f, aikaTekstuuri);
    private static StaattinenKomponentti aikaTekstiLabel = new StaattinenKomponentti(1f/12f, 1f/15f, -19f/24f, 1f/6f, aikaTeksti);
    private static StaattinenKomponentti rahetKuvakeLabel = new StaattinenKomponentti(1f/15f, 1f/15f, -27f/30f, 0, rahaTekstuuri);
    private static StaattinenKomponentti rahetTekstiLabel = new StaattinenKomponentti(1f/15f, 1f/15f, -23f/30f, 0, rahaTeksti);
    private static StaattinenKomponentti tölksKuvakeLabel = new StaattinenKomponentti(1f/15f, 1f/15f, -27f/30f, -1f/6f, tölksTekstuuri);
    private static StaattinenKomponentti tölksTekstiLabel = new StaattinenKomponentti(1f/15f, 1f/15f, -23f/30f, -1f/6f, tölksTeksti);

    // Tavaraluettelo
    private static Teksti tavaraluetteloTeksti = new Teksti("Tavaraluettelo", 550, 48);
    private static Teksti valittuEsineTeksti = new Teksti("", 150, 48);
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä valittuSlotTekstuuri = Assets.annaTekstuuri("hud_tavarapaikka_valittu");
    private static Renderöitävä yhdistettäväSlotTekstuuri = Assets.annaTekstuuri("hud_tavarapaikka_yhdistettävä");
    private static Renderöitävä tavarapaikka1Tekstuuri1 = Assets.annaTekstuuri("hud_tavarapaikka_1");
    private static Renderöitävä tavarapaikka1Tekstuuri2 = Assets.annaTekstuuri("hud_tavarapaikka_2");
    private static Renderöitävä tavarapaikka1Tekstuuri3 = Assets.annaTekstuuri("hud_tavarapaikka_3");
    private static Renderöitävä tavarapaikka1Tekstuuri4 = Assets.annaTekstuuri("hud_tavarapaikka_4");
    private static Renderöitävä tavarapaikka1Tekstuuri5 = Assets.annaTekstuuri("hud_tavarapaikka_5");
    private static Renderöitävä tavarapaikka1Tekstuuri6 = Assets.annaTekstuuri("hud_tavarapaikka_6");
    private static StaattinenKomponentti tavaraluetteloOtsikkoLabel = new StaattinenKomponentti(1f/7.5f, 1f/18f, -5f/6f, -4f/9f, tavaraluetteloTeksti);
    private static StaattinenKomponentti valittuEsineTekstiLabel = new StaattinenKomponentti(1f/7.5f, 1f/18f, -5f/6f, -8f/9f, valittuEsineTeksti);
    private static StaattinenKomponentti tavarapaikka1Label = new StaattinenKomponentti(1f/24f, 1f/15f, -11f/12f, -9f/15f, tavarapaikka1Tekstuuri1);
    private static StaattinenKomponentti tavarapaikka2Label = new StaattinenKomponentti(1f/24f, 1f/15f, -5f/6f, -9f/15f, tavarapaikka1Tekstuuri2);
    private static StaattinenKomponentti tavarapaikka3Label = new StaattinenKomponentti(1f/24f, 1f/15f, -3f/4f, -9f/15f, tavarapaikka1Tekstuuri3);
    private static StaattinenKomponentti tavarapaikka4Label = new StaattinenKomponentti(1f/24f, 1f/15f, -11f/12f, -11f/15f, tavarapaikka1Tekstuuri4);
    private static StaattinenKomponentti tavarapaikka5Label = new StaattinenKomponentti(1f/24f, 1f/15f, -5f/6f, -11f/15f, tavarapaikka1Tekstuuri5);
    private static StaattinenKomponentti tavarapaikka6Label = new StaattinenKomponentti(1f/24f, 1f/15f, -3f/4f, -11f/15f, tavarapaikka1Tekstuuri6);
    private static StaattinenKomponentti valittuTavarapaikkaLabel = new StaattinenKomponentti(1f/24f, 1f/12f, 0, 0, valittuSlotTekstuuri);
    private static StaattinenKomponentti yhdistettäväTavarapaikkaLabel = new StaattinenKomponentti(1f/24f, 1f/12f, 0, 0, yhdistettäväSlotTekstuuri);

    // Kartta
    private static Teksti alueTeksti = new Teksti("Alue", Color.black, 192, 48, KeimoFontit.fontti_keimo_36, true);
    private static Teksti huoneTeksti = new Teksti("Huone", Color.black, 192, 48, KeimoFontit.fontti_keimo_36, true);
    private static Renderöitävä karttaTekstuuri;
    private static Renderöitävä pelaajaKartallaKuvake = Assets.annaTekstuuri("kartta_pelaajakuvake");
    private static Renderöitävä karttaAsuintalotTekstuuri = Assets.annaTekstuuri("kartta_asuintalot");
    private static Renderöitävä karttaBaariTekstuuri = Assets.annaTekstuuri("kartta_baari");
    private static Renderöitävä karttaBaariSalahuoneTekstuuri = Assets.annaTekstuuri("kartta_baari_salahuone");
    private static Renderöitävä karttaKauppaTekstuuri = Assets.annaTekstuuri("kartta_kauppa");
    private static Renderöitävä karttaKotiTekstuuri = Assets.annaTekstuuri("kartta_koti");
    private static Renderöitävä karttaKuuTekstuuri = Assets.annaTekstuuri("kartta_kuu");
    private static Renderöitävä karttaMetsäTekstuuri = Assets.annaTekstuuri("kartta_metsä");
    private static Renderöitävä karttaMetsäBossTekstuuri = Assets.annaTekstuuri("kartta_metsä_boss");
    private static Renderöitävä karttaPeltoTekstuuri = Assets.annaTekstuuri("kartta_pelto");
    private static Renderöitävä karttaPuistoTekstuuri = Assets.annaTekstuuri("kartta_puisto");
    private static Renderöitävä karttaTemppeliTekstuuri = Assets.annaTekstuuri("kartta_temppeli");
    private static Renderöitävä karttaTemppeliBossTekstuuri = Assets.annaTekstuuri("kartta_temppeli_boss");
    private static Renderöitävä karttaYokyläTekstuuri = Assets.annaTekstuuri("kartta_yo-kylä");
    private static Renderöitävä eiKarttaaTekstuuri = Assets.annaTekstuuri("kartta_eikarttaa");
    private static StaattinenKomponentti alueLabel = new StaattinenKomponentti(1f/7.5f, 1f/18f, 5f/6f, 8f/9f, alueTeksti);
    private static StaattinenKomponentti karttaLabel = new StaattinenKomponentti(1f/7.5f, 1f/6f, 5f/6f, 2f/3f, karttaTekstuuri);
    private static StaattinenKomponentti huoneLabel = new StaattinenKomponentti(1f/7.5f, 1f/18f, 5f/6f, 4f/9f, huoneTeksti);
    private static StaattinenKomponentti pelaajanKuvakeLabel = new StaattinenKomponentti(1f/32f, 1f/32f, 5f/6f, 2f/3f, pelaajaKartallaKuvake);

    // Tavoitelaatikko
    private static Renderöitävä taustaTavoitelistaTekstuuri = Assets.annaTekstuuri("hud_seuraava_tavoite");
    private static Teksti seuraavaTavoiteTeksti = new Teksti("Tavoite", Color.black, 1000, 48);
    private static StaattinenKomponentti tavoitelaatikkoKehysLabel = new StaattinenKomponentti(2f/3f, 1f/12f, 0, 11f/12f, taustaTavoitelistaTekstuuri);
    private static StaattinenKomponentti seuraavaTavoiteLabel = new StaattinenKomponentti(1.75f/3f, 1f/27.5f, 0, 8f/9f, seuraavaTavoiteTeksti);

    // Dialogilaatikko
    private static Renderöitävä dialogiKuvakeKehysTekstuuri = Assets.annaTekstuuri("dialogi_kuvake_kehys");
    private static Renderöitävä dialogiTekstiKehysTekstuuri = Assets.annaTekstuuri("dialogi_teksti_kehys");
    private static Renderöitävä dialogiNimiKehysTekstuuri = Assets.annaTekstuuri("dialogi_nimi_kehys");
    private static StaattinenKomponentti dialogiPohjaLabel = new StaattinenKomponentti(2f/3f, 1f/6f, 0, -5f/6f, tyhjäTekstuuri);
    private static StaattinenKomponentti dialogiKuvakeKehysLabel = new StaattinenKomponentti(1f/6f, 1f/6f, -1f/2f, -5f/6f, dialogiKuvakeKehysTekstuuri);
    private static StaattinenKomponentti dialogiKuvakeLabel = new StaattinenKomponentti(3f/20f, 3f/20f, -1f/2f, -5f/6f);
    private static StaattinenKomponentti dialogiTekstiKehysLabel = new StaattinenKomponentti(1f/2f, 1f/8f, 1f/6f, -7f/8f, dialogiTekstiKehysTekstuuri);
    private static StaattinenKomponentti dialogiTekstiLabel = new StaattinenKomponentti(29f/60f, 9f/80f, 1f/6f, -7f/8f);
    private static StaattinenKomponentti dialogiPuhujaKehysLabel = new StaattinenKomponentti(1f/2f, 1f/24f, 1f/6f, -17/24f, dialogiNimiKehysTekstuuri);
    private static StaattinenKomponentti dialogiPuhujaLabel = new StaattinenKomponentti(29f/60f, 3f/80f, 1f/6f, -17f/24f);

    private static void alustaHUDGrafiikat() {
        if (!hudGrafiikatAlustettu) {
            ÄmpäriJonoIkkuna.alustaGrafiikat();
            PullonPalautusIkkuna.alustaGrafiikat();
            hudGrafiikatAlustettu = true;
        }
    }

    public static void renderöiTeksti(String teksti, int sijX, int sijY, int leveys, int korkeus, Kamera camera, Ikkuna window) {
        peliShader.bind();
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f tekstiAlue = new Matrix4f();
        camera.getUntransformedProjection().scale(1, tekstiAlue);
        tekstiAlue.translate(-window.getWidth()/2+sijX + leveys, window.getHeight()/2-sijY, 0);
        tekstiAlue.scale(leveys, korkeus, 0);
        peliShader.asetaSijainti(tekstiAlue);
        
        Teksti text = new Teksti(teksti, leveys, korkeus);

        text.bind(0);
        Assets.getModel().render();
    }

    public static void renderöiTeksti(Teksti teksti, int sijX, int sijY, Ikkuna window) {
        peliShader.bind();
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        
        peliShader.bind();
        float scaleX = window.getWidth()/6;
        float scaleY = window.getHeight()/60;
        float offsetX = sijX;
        float offsetY = sijY;

        Matrix4f matAika = new Matrix4f();
        window.getView().scale(1, matAika);
        matAika.translate(-window.getWidth()/2+scaleX + offsetX, window.getHeight()/2 - offsetY, 0);
        matAika.scale(scaleX, scaleY, 0);

        teksti.bind(0);
        peliShader.asetaSijainti(matAika);
        Assets.getModel().render();
    }

    public static void renderöiHUD(Ikkuna window) {
        alustaHUDGrafiikat();
        guiShader.bind();
        guiShader.setUniform("color", new Vector4f(0f, 0f, 0f, 0f));
        renderöiPohja(guiShader, window);
        renderöiHp(guiShader, window);
        renderöiStatsit(guiShader, window);
        renderöiTavaraluettelo(guiShader, window);
        renderöiKartta(guiShader, window);
        renderöiTavoiteLaatikko(guiShader, window);
    }

    private static void renderöiPohja(Shader shader, Ikkuna window) {
        hudPohjaVasenYläLabel.renderöi(shader, window);
        hudPohjaVasenKeskiLabel.renderöi(shader, window);
        hudPohjaVasenAlaLabel.renderöi(shader, window);
        hudPohjaOikeaYläLabel.renderöi(shader, window);
        hudPohjaOikeaKeskiLabel.renderöi(shader, window);
        hudPohjaOikeaAlaLabel.renderöi(shader, window);
    }

    private static void renderöiHp(Shader shader, Ikkuna window) {
        hpKuvakeLabel.renderöi(shader, window);
        hpTeksti.päivitäTeksti("" + Pelaaja.hp, 0, 50, Color.black);
        hpTekstiLabel.renderöi(shader, window);

        juomatKuvakeLabel.renderöi(shader, window);
        juomatTeksti.päivitäTeksti(kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + "‰", 0, 50, Color.black);
        juomatTekstiLabel.renderöi(shader, window);

        ruokaKuvakeLabel.renderöi(shader, window);
        switch (Pelaaja.syödytRuoat) {
            case 0: pelaajaKuvakeLabel.päivitäSisältö(pelaajaStatus0Tekstuuri); break;
            case 1: pelaajaKuvakeLabel.päivitäSisältö(pelaajaStatus1Tekstuuri); break;
            case 2: pelaajaKuvakeLabel.päivitäSisältö(pelaajaStatus2Tekstuuri); break;
            case 3: pelaajaKuvakeLabel.päivitäSisältö(pelaajaStatus3Tekstuuri); break;
            case 4: pelaajaKuvakeLabel.päivitäSisältö(pelaajaStatus4Tekstuuri); break;
        }
        pelaajaKuvakeLabel.renderöi(shader, window);
        syödytRuoatTeksti.päivitäTeksti("" + Pelaaja.syödytRuoat + "/3");
        ruokaTekstiLabel.renderöi(shader, window);
    }

    private static void renderöiStatsit(Shader shader, Ikkuna window) {
        aikaKuvakeLabel.renderöi(shader, window);
        aikaTeksti.päivitäTeksti(Kello.päivitäAika());
        aikaTekstiLabel.renderöi(shader, window);

        rahetKuvakeLabel.renderöi(shader, window);
        rahaTeksti.päivitäTeksti("" + Pelaaja.raha);
        rahetTekstiLabel.renderöi(shader, window);

        tölksKuvakeLabel.renderöi(shader, window);
        tölksTeksti.päivitäTeksti("" + Pelaaja.kuparit);
        tölksTekstiLabel.renderöi(shader, window);
    }

    private static void renderöiTavaraluettelo(Shader shader, Ikkuna window) {
        tavaraluetteloOtsikkoLabel.renderöi(shader, window);
        try {
            valittuEsineTeksti.päivitäTeksti(Peli.valittuEsine.annaNimi(), 1, 1);
            valittuEsineTeksti.bind(0);
        }
        catch (NullPointerException npe) {
            valittuEsineTeksti.päivitäTeksti("");
            valittuEsineTeksti.bind(0);
        }
        valittuEsineTekstiLabel.renderöi(shader, window);

        tavarapaikka1Label.päivitäSisältö(Pelaaja.esineet[0] != null ? Pelaaja.esineet[0].annaTekstuuri() : tyhjäTekstuuri);
        tavarapaikka1Label.renderöi(shader, window);
        tavarapaikka1Label.päivitäSisältö(tavarapaikka1Tekstuuri1);
        tavarapaikka1Label.renderöi(shader, window);
        tavarapaikka2Label.päivitäSisältö(Pelaaja.esineet[1] != null ? Pelaaja.esineet[1].annaTekstuuri() : tyhjäTekstuuri);
        tavarapaikka2Label.renderöi(shader, window);
        tavarapaikka2Label.päivitäSisältö(tavarapaikka1Tekstuuri2);
        tavarapaikka2Label.renderöi(shader, window);
        tavarapaikka3Label.päivitäSisältö(Pelaaja.esineet[2] != null ? Pelaaja.esineet[2].annaTekstuuri() : tyhjäTekstuuri);
        tavarapaikka3Label.renderöi(shader, window);
        tavarapaikka3Label.päivitäSisältö(tavarapaikka1Tekstuuri3);
        tavarapaikka3Label.renderöi(shader, window);
        tavarapaikka4Label.päivitäSisältö(Pelaaja.esineet[3] != null ? Pelaaja.esineet[3].annaTekstuuri() : tyhjäTekstuuri);
        tavarapaikka4Label.renderöi(shader, window);
        tavarapaikka4Label.päivitäSisältö(tavarapaikka1Tekstuuri4);
        tavarapaikka4Label.renderöi(shader, window);
        tavarapaikka5Label.päivitäSisältö(Pelaaja.esineet[4] != null ? Pelaaja.esineet[4].annaTekstuuri() : tyhjäTekstuuri);
        tavarapaikka5Label.renderöi(shader, window);
        tavarapaikka5Label.päivitäSisältö(tavarapaikka1Tekstuuri5);
        tavarapaikka5Label.renderöi(shader, window);
        tavarapaikka6Label.päivitäSisältö(Pelaaja.esineet[5] != null ? Pelaaja.esineet[5].annaTekstuuri() : tyhjäTekstuuri);
        tavarapaikka6Label.renderöi(shader, window);
        tavarapaikka6Label.päivitäSisältö(tavarapaikka1Tekstuuri6);
        tavarapaikka6Label.renderöi(shader, window);

        if (Peli.esineValInt % 3 == 0) valittuTavarapaikkaLabel.muutaOffsetX(-11f/12f);
        else if (Peli.esineValInt % 3 == 1) valittuTavarapaikkaLabel.muutaOffsetX(-5f/6f);
        else if (Peli.esineValInt % 3 == 2) valittuTavarapaikkaLabel.muutaOffsetX(-3f/4f);
        if (Peli.esineValInt / 3 == 0) valittuTavarapaikkaLabel.muutaOffsetY(-7f/12f);
        else if (Peli.esineValInt / 3 == 1) valittuTavarapaikkaLabel.muutaOffsetY(-3f/4f);
        
        if (Peli.yhdistettäväTavarapaikka % 3 == 0) yhdistettäväTavarapaikkaLabel.muutaOffsetX(-11f/12f);
        else if (Peli.yhdistettäväTavarapaikka % 3 == 1) yhdistettäväTavarapaikkaLabel.muutaOffsetX(-5f/6f);
        else if (Peli.yhdistettäväTavarapaikka % 3 == 2) yhdistettäväTavarapaikkaLabel.muutaOffsetX(-3f/4f);
        if (Peli.yhdistettäväTavarapaikka / 3 == 0) yhdistettäväTavarapaikkaLabel.muutaOffsetY(-7f/12f);
        else if (Peli.yhdistettäväTavarapaikka / 3 == 1) yhdistettäväTavarapaikkaLabel.muutaOffsetY(-3f/4f);

        valittuTavarapaikkaLabel.renderöi(shader, window);
        if (Peli.yhdistäminenKäynnissä) yhdistettäväTavarapaikkaLabel.renderöi(shader, window);
    }

    private static void renderöiKartta(Shader shader, Ikkuna window) {
        shader.bind();

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

    private static void renderöiTavoiteLaatikko(Shader shader, Ikkuna window) {
        tavoitelaatikkoKehysLabel.renderöi(shader, window);
        seuraavaTavoiteTeksti.päivitäTeksti(TavoiteLista.nykyinenTavoite);
        seuraavaTavoiteLabel.renderöi(shader, window);
    }

    public static void renderöiDialogiLaatikko(Shader shader, Ikkuna window) {
        shader.bind();
        shader.setUniform("addcolor", new Vector4f(0.85f, 0.85f, 0.85f, 0.7f));
        dialogiPohjaLabel.renderöi(shader, window);
        shader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));
        
        Dialogit.renderöiDialogiTeksti();

        dialogiKuvakeKehysLabel.renderöi(shader, window);
        dialogiKuvakeLabel.päivitäSisältö(Dialogit.dialogiKuvake);
        dialogiKuvakeLabel.renderöi(shader, window);

        dialogiTekstiKehysLabel.renderöi(shader, window);
        dialogiTekstiLabel.päivitäSisältö(Dialogit.dialogiTeksti);
        dialogiTekstiLabel.renderöi(shader, window);

        dialogiPuhujaKehysLabel.renderöi(shader, window);
        dialogiPuhujaLabel.päivitäSisältö(Dialogit.dialogiNimi);
        dialogiPuhujaLabel.renderöi(shader, window);
    }
}
