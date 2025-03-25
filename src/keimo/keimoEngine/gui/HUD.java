package keimo.keimoEngine.gui;

import java.text.DecimalFormat;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.toiminnot.Dialogit;

public class HUD {

    //private static Text text;
    //private static Model modeli = Assets.getModel();
    private static Shader peliShader = new Shader("shader");
    private static Shader guiShader = new Shader("staattinen");

    private static Tekstuuri aikaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/aika.png");
    private static Tekstuuri hpTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/hp.png");
    private static Tekstuuri rahaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/rahet.png");
    private static Tekstuuri tölksTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/tölks.png");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    private static Tekstuuri taustaOhjeTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/paneeli_tausta_ohjeet.png");
    private static Tekstuuri taustaStatsitTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/paneeli_tausta_statsit.png");
    private static Tekstuuri taustaTavaraluetteloTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/paneeli_tausta_tavaraluettelo.png");
    private static Tekstuuri taustaTavoitelistaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/tavoitelista_kehys.png");

    private static Teksti tavoiteInfoTeksti = new Teksti("Seuraava tavoite:", 300, 30);
    private static Teksti seuraavaTavoiteTeksti = new Teksti("Tavoite", 300, 30);

    private static Teksti aikaTeksti = new Teksti("aika", 120, 40);
    private static Teksti hpTeksti = new Teksti("" + Pelaaja.hp, 40, 30);
    private static Teksti rahaTeksti = new Teksti("" + Pelaaja.raha, 60, 30);
    private static Teksti tölksTeksti = new Teksti("" + Pelaaja.kuparit, 40, 30);
    private static Teksti tavaraluetteloTeksti = new Teksti("Tavaraluettelo", 200, 40);
    private static Teksti valittuEsineTeksti = new Teksti("", 150, 40);

    private static Tekstuuri valittuSlotTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/valittu_tavarapaikka.png");
    private static Tekstuuri yhdistettäväSlotTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/yhdistettävä_tavarapaikka.png");
    private static Tekstuuri tavarapaikka1Tekstuuri1 = new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_1.png");
    private static Tekstuuri tavarapaikka1Tekstuuri2 = new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_2.png");
    private static Tekstuuri tavarapaikka1Tekstuuri3 = new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_3.png");
    private static Tekstuuri tavarapaikka1Tekstuuri4 = new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_4.png");
    private static Tekstuuri tavarapaikka1Tekstuuri5 = new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_5.png");
    private static Tekstuuri tavarapaikka1Tekstuuri6 = new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_6.png");

    private static Tekstuuri dialogiKuvakeKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/dialogi_kuvake_kehys.png");
    private static Tekstuuri dialogiTekstiKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/dialogi_teksti_kehys.png");
    private static Tekstuuri dialogiNimiKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/dialogi_nimi_kehys.png");

    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

    public static void renderöiTeksti(String teksti, int sijX, int sijY, int leveys, int korkeus, Kamera camera, Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f tekstiAlue = new Matrix4f();
        camera.getUntransformedProjection().scale(1, tekstiAlue);
        tekstiAlue.translate(-window.getWidth()/2+sijX + leveys, window.getHeight()/2-sijY, 0);
        //float leveysKerroin = window.getWidth()/1000f;
        //float korkeusKerroin = window.getHeight()/1000f;
        //tekstiAlue.scale(leveys*leveysKerroin, korkeus*korkeusKerroin, 0);
        tekstiAlue.scale(leveys, korkeus, 0);
        peliShader.setUniform("projection", tekstiAlue);
        
        Teksti text = new Teksti(teksti, leveys, korkeus);

        text.bind(0);
        Assets.getModel().render();
    }

    public static void renderöiTeksti(Teksti teksti, int sijX, int sijY, Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        float scaleX = window.getWidth()/6;
        float scaleY = window.getHeight()/30;
        float offsetX = sijX;
        float offsetY = sijY;

        Matrix4f matAika = new Matrix4f();
        window.getView().scale(1, matAika);
        matAika.translate(-window.getWidth()/2+scaleX + offsetX, window.getHeight()/2 - offsetY, 0);
        matAika.scale(scaleX, scaleY, 0);

        teksti.bind(0);
        peliShader.setUniform("projection", matAika);
        Assets.getModel().render();
    }

    private static String päivitäAika() {
		double kulunutAika = (System.nanoTime() - Peli.aikaReferenssi)/1_000_000;
        String aika = "";
		if (!Peli.ajastinPysäytetty) {
			double kulunutAikaSek = (double)kulunutAika/1000d;
			int kulunutAikaMin = (int)kulunutAikaSek / 60;
			int kulunutAikaH = (int)kulunutAikaMin / 60;
			kulunutAikaSek = kulunutAikaSek % 60;
			aika = kulunutAikaH + ":" + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek);
		}
        return aika;
	}

    public static void renderöiHUD(Window window) {
        renderöiHUDPohjaVasen(window);
        renderöiInfoHUDKuvakkeet(window);
        renderöiInfoHUDNumerot(window);
        renderöiTavaraluetteloKuvakkeet(window);
        renderöiTavaraluetteloTekstit(window);
        renderöiTavoiteTeksti(window);
    }

    private static void renderöiInfoHUDKuvakkeet(Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        float scaleX = window.getWidth()/60;
        float scaleY = window.getHeight()/30;
        float keskitysX = window.getWidth()/29;
        float offsetX = window.getHeight()/24;
        float offsetY = window.getHeight()/25;

        Matrix4f matAika = new Matrix4f();
        window.getView().scale(1, matAika);
        matAika.translate(-window.getWidth()/2+scaleX + keskitysX - 0*offsetX, window.getHeight()/40 + 2*offsetY, 0);
        matAika.scale(scaleX, scaleY, 0);
        Matrix4f matHP = new Matrix4f();
        window.getView().scale(1, matHP);
        matHP.translate(-window.getWidth()/2+scaleX + keskitysX - 0*offsetX, window.getHeight()/40 + 0.25f*offsetY, 0);
        matHP.scale(scaleX, scaleY, 0);
        Matrix4f matRahet = new Matrix4f();
        window.getView().scale(1, matRahet);
        matRahet.translate(-window.getWidth()/2+scaleX + keskitysX - 0*offsetX, -window.getHeight()/40 - 0.25f*offsetY, 0);
        matRahet.scale(scaleX*1.8f, scaleY, 0);
        Matrix4f matTölks = new Matrix4f();
        window.getView().scale(1, matTölks);
        matTölks.translate(-window.getWidth()/2+scaleX + keskitysX - 0*offsetX, -window.getHeight()/40 - 2*offsetY, 0);
        matTölks.scale(scaleX*2, scaleY, 0);

        aikaTekstuuri.bind(0);
        peliShader.setUniform("projection", matAika);
        Assets.getModel().render();
        hpTekstuuri.bind(0);
        peliShader.setUniform("projection", matHP);
        Assets.getModel().render();
        rahaTekstuuri.bind(0);
        peliShader.setUniform("projection", matRahet);
        Assets.getModel().render();
        tölksTekstuuri.bind(0);
        peliShader.setUniform("projection", matTölks);
        Assets.getModel().render();
    }

    private static void renderöiInfoHUDNumerot(Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        float scaleX = window.getWidth()/30;
        float scaleY = window.getHeight()/30;
        float keskitysX = window.getWidth()/18;
        float offsetX = window.getHeight()/24;
        float offsetY = window.getHeight()/24;

        Matrix4f matAika = new Matrix4f();
        window.getView().scale(1, matAika);
        matAika.translate(-window.getWidth()/2+scaleX + keskitysX + offsetX, window.getHeight()/40 + 2*offsetY - 10, 0);
        matAika.scale(scaleX, scaleY, 0);
        Matrix4f matHP = new Matrix4f();
        window.getView().scale(1, matHP);
        matHP.translate(-window.getWidth()/2+scaleX + keskitysX + offsetX, window.getHeight()/40 + 0.25f*offsetY - 10, 0);
        matHP.scale(scaleX, scaleY, 0);
        Matrix4f matRahet = new Matrix4f();
        window.getView().scale(1, matRahet);
        matRahet.translate(-window.getWidth()/2+scaleX + keskitysX + offsetX, -window.getHeight()/40 - 0.25f*offsetY - 10, 0);
        matRahet.scale(scaleX, scaleY, 0);
        Matrix4f matTölks = new Matrix4f();
        window.getView().scale(1, matTölks);
        matTölks.translate(-window.getWidth()/2+scaleX + keskitysX + offsetX, -window.getHeight()/40 - 2*offsetY - 10, 0);
        matTölks.scale(scaleX, scaleY, 0);

        aikaTeksti.päivitäTeksti("" + päivitäAika());
        aikaTeksti.bind(0);
        peliShader.setUniform("projection", matAika);
        Assets.getModel().render();
        hpTeksti.päivitäTeksti("" + Pelaaja.hp);
        hpTeksti.bind(0);
        peliShader.setUniform("projection", matHP);
        Assets.getModel().render();
        rahaTeksti.päivitäTeksti("" + Pelaaja.raha);
        rahaTeksti.bind(0);
        peliShader.setUniform("projection", matRahet);
        Assets.getModel().render();
        tölksTeksti.päivitäTeksti("" + Pelaaja.kuparit);
        tölksTeksti.bind(0);
        peliShader.setUniform("projection", matTölks);
        Assets.getModel().render();
    }

    private static void renderöiTavaraluetteloKuvakkeet(Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        float scaleX = window.getWidth()/50;
        float scaleY = window.getHeight()/25;
        float keskitysX = window.getWidth()/15.75f;
        float keskitysY = window.getHeight()/7.85f;
        float offsetX = window.getWidth()/22.75f;
        float offsetY = window.getHeight()/25;

        Matrix4f matSlot1 = new Matrix4f();
        window.getView().scale(1, matSlot1);
        matSlot1.translate(-window.getWidth()/2+scaleX + keskitysX - offsetX, -window.getHeight()/2+scaleY + keskitysY + offsetY, 0);
        matSlot1.scale(scaleX, scaleY, 0);
        Matrix4f matSlot2 = new Matrix4f();
        window.getView().scale(1, matSlot2);
        matSlot2.translate(-window.getWidth()/2+scaleX + keskitysX, -window.getHeight()/2+scaleY + keskitysY + offsetY, 0);
        matSlot2.scale(scaleX, scaleY, 0);
        Matrix4f matSlot3 = new Matrix4f();
        window.getView().scale(1, matSlot3);
        matSlot3.translate(-window.getWidth()/2+scaleX + keskitysX + offsetX, -window.getHeight()/2+scaleY + keskitysY + offsetY, 0);
        matSlot3.scale(scaleX, scaleY, 0);
        Matrix4f matSlot4 = new Matrix4f();
        window.getView().scale(1, matSlot4);
        matSlot4.translate(-window.getWidth()/2+scaleX + keskitysX - offsetX, -window.getHeight()/2+scaleY + keskitysY - offsetY, 0);
        matSlot4.scale(scaleX, scaleY, 0);
        Matrix4f matSlot5 = new Matrix4f();
        window.getView().scale(1, matSlot5);
        matSlot5.translate(-window.getWidth()/2+scaleX + keskitysX, -window.getHeight()/2+scaleY + keskitysY - offsetY, 0);
        matSlot5.scale(scaleX, scaleY, 0);
        Matrix4f matSlot6 = new Matrix4f();
        window.getView().scale(1, matSlot6);
        matSlot6.translate(-window.getWidth()/2+scaleX + keskitysX + offsetX, -window.getHeight()/2+scaleY + keskitysY - offsetY, 0);
        matSlot6.scale(scaleX, scaleY, 0);

        Matrix4f matValittuSlot = new Matrix4f();
        switch (Peli.esineValInt) {
            case 0: matValittuSlot = matSlot1; break;
            case 1: matValittuSlot = matSlot2; break;
            case 2: matValittuSlot = matSlot3; break;
            case 3: matValittuSlot = matSlot4; break;
            case 4: matValittuSlot = matSlot5; break;
            case 5: matValittuSlot = matSlot6; break;
            default: matValittuSlot = null; break;
        }
        Matrix4f matYhdistettäväSlot = new Matrix4f();
        switch (Peli.yhdistettäväTavarapaikka) {
            case 0: matYhdistettäväSlot = matSlot1; break;
            case 1: matYhdistettäväSlot = matSlot2; break;
            case 2: matYhdistettäväSlot = matSlot3; break;
            case 3: matYhdistettäväSlot = matSlot4; break;
            case 4: matYhdistettäväSlot = matSlot5; break;
            case 5: matYhdistettäväSlot = matSlot6; break;
            default: matYhdistettäväSlot = null; break;
        }

        try {
            Pelaaja.esineet[0].annaTekstuuri().bind(0);
        }
        catch (NullPointerException npe) {
            tyhjäTekstuuri.bind(0);
        }
        peliShader.setUniform("projection", matSlot1);
        Assets.getModel().render();
        tavarapaikka1Tekstuuri1.bind(0);
        Assets.getModel().render();

        try {
            Pelaaja.esineet[1].annaTekstuuri().bind(0);
        }
        catch (NullPointerException npe) {
            tyhjäTekstuuri.bind(0);
        }
        peliShader.setUniform("projection", matSlot2);
        Assets.getModel().render();
        tavarapaikka1Tekstuuri2.bind(0);
        Assets.getModel().render();

        try {
            Pelaaja.esineet[2].annaTekstuuri().bind(0);
        }
        catch (NullPointerException npe) {
            tyhjäTekstuuri.bind(0);
        }
        peliShader.setUniform("projection", matSlot3);
        Assets.getModel().render();
        tavarapaikka1Tekstuuri3.bind(0);
        Assets.getModel().render();

        try {
            Pelaaja.esineet[3].annaTekstuuri().bind(0);
        }
        catch (NullPointerException npe) {
            tyhjäTekstuuri.bind(0);
        }
        peliShader.setUniform("projection", matSlot4);
        Assets.getModel().render();
        tavarapaikka1Tekstuuri4.bind(0);
        Assets.getModel().render();

        try {
            Pelaaja.esineet[4].annaTekstuuri().bind(0);
        }
        catch (NullPointerException npe) {
            tyhjäTekstuuri.bind(0);
        }
        peliShader.setUniform("projection", matSlot5);
        Assets.getModel().render();
        tavarapaikka1Tekstuuri5.bind(0);
        Assets.getModel().render();

        try {
            Pelaaja.esineet[5].annaTekstuuri().bind(0);
        }
        catch (NullPointerException npe) {
            tyhjäTekstuuri.bind(0);
        }
        peliShader.setUniform("projection", matSlot6);
        Assets.getModel().render();
        tavarapaikka1Tekstuuri6.bind(0);
        Assets.getModel().render();

        if (matValittuSlot != null) {
            valittuSlotTekstuuri.bind(0);
            peliShader.setUniform("projection", matValittuSlot);
            Assets.getModel().render();
        }

        if (matYhdistettäväSlot != null) {
            yhdistettäväSlotTekstuuri.bind(0);
            peliShader.setUniform("projection", matYhdistettäväSlot);
            Assets.getModel().render();
        }
    }

    private static void renderöiTavaraluetteloTekstit(Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        float scaleXOtsikko = window.getWidth()/16;
        float scaleXTavara = window.getWidth()/11;
        float scaleY = window.getHeight()/30;
        float keskitysX = window.getWidth()/18;
        float keskitysY = window.getHeight()/8;
        float offsetX = window.getWidth()/30;
        float offsetY = window.getHeight()/10;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(-window.getWidth()/2+scaleXOtsikko + keskitysX - offsetX, -window.getHeight()/2+scaleY + keskitysY + offsetY, 0);
        matOtsikko.scale(scaleXOtsikko, scaleY, 0);
        Matrix4f matTavara = new Matrix4f();
        window.getView().scale(1, matTavara);
        matTavara.translate(-window.getWidth()/2+scaleXTavara + keskitysX - offsetX, -window.getHeight()/2+scaleY + keskitysY - 1.2f*offsetY, 0);
        matTavara.scale(scaleXTavara, scaleY, 0);
        tavaraluetteloTeksti.bind(0);
        peliShader.setUniform("projection", matOtsikko);
        Assets.getModel().render();

        try {
            valittuEsineTeksti.päivitäTeksti(Peli.valittuEsine.annaNimi(), true);
            valittuEsineTeksti.bind(0);
        }
        catch (NullPointerException npe) {
            valittuEsineTeksti.päivitäTeksti("");
            valittuEsineTeksti.bind(0);
        }
        peliShader.setUniform("projection", matTavara);
        Assets.getModel().render();
    }

    private static void renderöiHUDPohjaVasen(Window window) {
        float scaleX = window.getWidth()/12;
        float scaleY = window.getHeight()/6;
        float scaleXSisempi = scaleX *(14f/15f);
        float scaleYSisempi = scaleY *(14f/15f);
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matVasenYlä = new Matrix4f();
        window.getView().scale(1, matVasenYlä);
        matVasenYlä.translate(-window.getWidth()/2+scaleX, window.getHeight()/2-scaleY, 0);
        matVasenYlä.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matVasenYlä);
        taustaOhjeTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matVasenKeski = new Matrix4f();
        window.getView().scale(1, matVasenKeski);
        matVasenKeski.translate(-window.getWidth()/2+scaleX, 0, 0);
        matVasenKeski.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matVasenKeski);
        taustaStatsitTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matVasenAla = new Matrix4f();
        window.getView().scale(1, matVasenAla);
        matVasenAla.translate(-window.getWidth()/2+scaleX, -window.getHeight()/2+scaleY, 0);
        matVasenAla.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matVasenAla);
        taustaTavaraluetteloTekstuuri.bind(0);
        Assets.getModel().render();

        guiShader.bind();
        float scaleXTavoitelista = window.getWidth()/3;
        float scaleYTavoitelista = window.getHeight()/24;

        Matrix4f matTavoitePohja = new Matrix4f();
        window.getView().scale(1, matTavoitePohja);
        matTavoitePohja.translate(window.getWidth()/2-scaleXTavoitelista, window.getHeight()/2-scaleYTavoitelista, 0);
        matTavoitePohja.scale(scaleXTavoitelista, scaleYTavoitelista, 0);
        guiShader.setUniform("projection", matTavoitePohja);
        guiShader.setUniform("color", new Vector4f(85, 79, 67, 0.7f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();

        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matTavoitelista = new Matrix4f();
        window.getView().scale(1, matTavoitelista);
        matTavoitelista.translate(window.getWidth()/2-scaleXTavoitelista, window.getHeight()/2-scaleYTavoitelista, 0);
        matTavoitelista.scale(scaleXTavoitelista, scaleYTavoitelista, 0);
        peliShader.setUniform("projection", matTavoitelista);
        taustaTavoitelistaTekstuuri.bind(0);
        Assets.getModel().render();
    }

    private static void renderöiHudPohjaOikea(Window window) {
        float scaleX = window.getWidth()/12;
        float scaleY = window.getHeight()/6;
        float scaleXSisempi = scaleX *(14f/15f);
        float scaleYSisempi = scaleY *(14f/15f);
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matOikeaYlä = new Matrix4f();
        window.getView().scale(1, matOikeaYlä);
        matOikeaYlä.translate(window.getWidth()/2-scaleX, window.getHeight()/2-scaleY, 0);
        matOikeaYlä.scale(scaleX, scaleY, 0);
        guiShader.setUniform("projection", matOikeaYlä);
        guiShader.setUniform("color", new Vector4f(85, 79, 67, 0.7f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();
        Matrix4f matOikeaYläSisempi = new Matrix4f();
        window.getView().scale(1, matOikeaYläSisempi);
        matOikeaYläSisempi.translate(window.getWidth()/2-scaleX, window.getHeight()/2-scaleY, 0);
        matOikeaYläSisempi.scale(scaleXSisempi, scaleYSisempi, 0);
        guiShader.setUniform("projection", matOikeaYläSisempi);
        guiShader.setUniform("color", new Vector4f(255, 255, 255, 0.6f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matOikeaKeski = new Matrix4f();
        window.getView().scale(1, matOikeaKeski);
        matOikeaKeski.translate(window.getWidth()/2-scaleX, 0, 0);
        matOikeaKeski.scale(scaleX, scaleY, 0);
        guiShader.setUniform("projection", matOikeaKeski);
        guiShader.setUniform("color", new Vector4f(85, 79, 67, 0.7f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();
        Matrix4f matOikeaKeskiSisempi = new Matrix4f();
        window.getView().scale(1, matOikeaKeskiSisempi);
        matOikeaKeskiSisempi.translate(window.getWidth()/2-scaleX, 0, 0);
        matOikeaKeskiSisempi.scale(scaleXSisempi, scaleYSisempi, 0);
        guiShader.setUniform("projection", matOikeaKeskiSisempi);
        guiShader.setUniform("color", new Vector4f(255, 255, 255, 0.6f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matOikeaAla = new Matrix4f();
        window.getView().scale(1, matOikeaAla);
        matOikeaAla.translate(window.getWidth()/2-scaleX, -window.getHeight()/2+scaleY, 0);
        matOikeaAla.scale(scaleX, scaleY, 0);
        guiShader.setUniform("projection", matOikeaAla);
        guiShader.setUniform("color", new Vector4f(85, 79, 67, 0.7f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();
        Matrix4f matOikeaAlaSisempi = new Matrix4f();
        window.getView().scale(1, matOikeaAlaSisempi);
        matOikeaAlaSisempi.translate(window.getWidth()/2-scaleX, -window.getHeight()/2+scaleY, 0);
        matOikeaAlaSisempi.scale(scaleXSisempi, scaleYSisempi, 0);
        guiShader.setUniform("projection", matOikeaAlaSisempi);
        guiShader.setUniform("color", new Vector4f(255, 255, 255, 0.6f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();
    }

    private static void renderöiTavoiteTeksti(Window window) {
        float scaleXTavoitelista = window.getWidth()/3;
        float scaleYTavoitelista = window.getHeight()/24;
        float offsetY = window.getHeight()/40;
        
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        
        Matrix4f matTavoiteInfo = new Matrix4f();
        window.getView().scale(1, matTavoiteInfo);
        matTavoiteInfo.translate(window.getWidth()/2-scaleXTavoitelista, window.getHeight()/2-scaleYTavoitelista - 0.5f*offsetY, 0);
        matTavoiteInfo.scale(scaleXTavoitelista, scaleYTavoitelista, 0);
        peliShader.setUniform("projection", matTavoiteInfo);
        tavoiteInfoTeksti.bind(0);
        Assets.getModel().render();

        Matrix4f matSeuraavaTavoite = new Matrix4f();
        window.getView().scale(1, matSeuraavaTavoite);
        matSeuraavaTavoite.translate(window.getWidth()/2-scaleXTavoitelista, window.getHeight()/2-scaleYTavoitelista - 1.5f*offsetY, 0);
        matSeuraavaTavoite.scale(scaleXTavoitelista, scaleYTavoitelista, 0);
        peliShader.setUniform("projection", matSeuraavaTavoite);
        seuraavaTavoiteTeksti.päivitäTeksti(TavoiteLista.nykyinenTavoite);
        seuraavaTavoiteTeksti.bind(0);
        Assets.getModel().render();
    }

    public static void renderöiDialogiLaatikko(Window window) {
        guiShader.bind();
        guiShader.setUniform("sampler", 0);

        float scaleXPohja = window.getWidth()/4 + window.getWidth()/12;
        float scaleYPohja = window.getHeight()/12;

        Matrix4f matDialogiPohja = new Matrix4f();
        window.getView().scale(1, matDialogiPohja);
        matDialogiPohja.translate(0, -window.getHeight()/2+scaleYPohja, 0);
        matDialogiPohja.scale(scaleXPohja, scaleYPohja, 0);
        guiShader.setUniform("projection", matDialogiPohja);
        guiShader.setUniform("color", new Vector4f(85, 79, 67, 0.7f));
        tyhjäTekstuuri.bind(0);
        Assets.getModel().render();

        peliShader.bind();
        peliShader.setUniform("sampler", 0);

        float scaleXKuvake = window.getWidth()/12;
        float scaleYKuvake = window.getHeight()/12;
        float offsetXKuvake = window.getWidth()/4;
        Matrix4f matDialogiKuvake = new Matrix4f();
        window.getView().scale(1, matDialogiKuvake);
        matDialogiKuvake.translate(-offsetXKuvake, -window.getHeight()/2+scaleYKuvake, 0);
        matDialogiKuvake.scale(scaleXKuvake, scaleYKuvake, 0);
        peliShader.setUniform("projection", matDialogiKuvake);
        dialogiKuvakeKehysTekstuuri.bind(0);
        Assets.getModel().render();
        float scaleXKuvakeSisempi = scaleXKuvake*(15f/16f);
        float scaleYKuvakeSisempi = scaleYKuvake*(15f/16f);
        Matrix4f matDialogiKuvakeSisempi = new Matrix4f();
        window.getView().scale(1, matDialogiKuvakeSisempi);
        matDialogiKuvakeSisempi.translate(-offsetXKuvake, -window.getHeight()/2+scaleYKuvake, 0);
        matDialogiKuvakeSisempi.scale(scaleXKuvakeSisempi, scaleYKuvakeSisempi, 0);
        peliShader.setUniform("projection", matDialogiKuvakeSisempi);
        Dialogit.dialogiKuvake.bind(0);
        Assets.getModel().render();

        float scaleXTeksti = scaleXPohja - window.getWidth()/12;
        float scaleYTeksti = scaleYPohja * (3f/4f);
        float offsetXTeksti = scaleXKuvake;
        float offsetYTeksti = 0;
        Matrix4f matDialogiTeksti = new Matrix4f();
        window.getView().scale(1, matDialogiTeksti);
        matDialogiTeksti.translate(offsetXTeksti, -window.getHeight()/2+scaleYTeksti - offsetYTeksti, 0);
        matDialogiTeksti.scale(scaleXTeksti, scaleYTeksti, 0);
        peliShader.setUniform("projection", matDialogiTeksti);
        dialogiTekstiKehysTekstuuri.bind(0);
        Assets.getModel().render();
        float scaleXTekstiSisempi = scaleXTeksti*(15F/16f);
        float scaleYTekstiSisempi = scaleYTeksti*(15F/16f);
        Matrix4f matDialogiTekstiSisempi = new Matrix4f();
        window.getView().scale(1, matDialogiTekstiSisempi);
        matDialogiTekstiSisempi.translate(offsetXTeksti, -window.getHeight()/2+scaleYTekstiSisempi - offsetYTeksti, 0);
        matDialogiTekstiSisempi.scale(scaleXTekstiSisempi, scaleYTekstiSisempi, 0);
        peliShader.setUniform("projection", matDialogiTekstiSisempi);
        Dialogit.dialogiTeksti.bind(0);
        Assets.getModel().render();

        float scaleXPuhuja = scaleXPohja - window.getWidth()/12;
        float scaleYPuhuja = scaleYPohja * (1f/4f);
        float offsetXPuhuja = scaleXKuvake;
        float offsetYPuhuja = scaleYPuhuja * 6;

        Matrix4f matDialogiPuhuja = new Matrix4f();
        window.getView().scale(1, matDialogiPuhuja);
        matDialogiPuhuja.translate(offsetXPuhuja, -window.getHeight()/2+scaleYPuhuja + offsetYPuhuja, 0);
        matDialogiPuhuja.scale(scaleXPuhuja, scaleYPuhuja, 0);
        peliShader.setUniform("projection", matDialogiPuhuja);
        dialogiNimiKehysTekstuuri.bind(0);
        Assets.getModel().render();
        Dialogit.dialogiNimi.bind(0);
        Assets.getModel().render();
    }
}
