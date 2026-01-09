package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Musat;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MinipeliIkkunaPokeri {
    private static Shader peliShader = new Shader("shader");

    private static float ruudunLeveys = 1;
    private static float ruudunKorkeus = 1;

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/minipeli_kehys.png");
    private static Tekstuuri alkuruutuTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/alkuruutu.png");
    private static Tekstuuri valkoinenTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/valkoinen.png");
    private static float siirtymä = 0;
    private static boolean valikko = true;

    private static float nopeusHeitto = 20f;
    private static float nopeusJärjestäminen = 2f;
    private static float nopeusValinta = 1f;
    private static float nopeusKääntö = 4f;
    private static float[] siirräXKorttiPelaaja = {0, 0, 0, 0, 0};
    private static float[] siirräXKorttiVihollinen = {0, 0, 0, 0, 0};
    private static float[] siirräYKorttiPelaaja = {0, 0, 0, 0, 0};
    private static float[] siirräYKorttiVihollinen = {0, 0, 0, 0, 0};
    private static float[] kohdeXKorttiPelaajaJärjestetty = {0, 0, 0, 0, 0};
    private static float[] kohdeXKorttiVihollinenJärjestetty = {0, 0, 0, 0, 0};
    private static float[] käännäYKorttiVihollinen = {0, 0, 0, 0, 0};

    private static Random random = new Random();
    private static int korttienMäärä = 8;
    private static boolean peliKäynnissä = false;
    private static boolean pelaajaSiirtänyt = false;
    private static boolean kortitVaihdettu = false;
    private static boolean vihollisenKortitNäkyvissä = false;
    private static int pelaajanVoitot = 0;
    private static int vihollisenVoitot = 0;
    private static String voittaja = "";

    // private static Tekstuuri sandelsTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/sandels.png");
    // private static Tekstuuri olviTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/olvi.png");
    // private static Tekstuuri karhuTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/karhu.png");
    // private static Tekstuuri alecoqTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/ale_coq.png");
    // private static Tekstuuri lapinkultaTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/lapin_kulta.png");
    // private static Tekstuuri karjalaTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/karjala.png");
    // private static Tekstuuri olutolutTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/olut_olut.png");
    // private static Tekstuuri kupariTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kaljat/rainbow_lager.png");

    private static Tekstuuri sandelsTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_velho.png");
    private static Tekstuuri olviTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_juhani.png");
    private static Tekstuuri karhuTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_goblini.png");
    private static Tekstuuri alecoqTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_pasi.png");
    private static Tekstuuri lapinkultaTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_kauppias.png");
    private static Tekstuuri karjalaTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_keimo.png");
    private static Tekstuuri olutolutTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_pahavihu.png");
    private static Tekstuuri kupariTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_pikkuvihu.png");

    private static Kortti[] pelaajanKortit = new Kortti[5];
    private static Kortti[] vihollisenKortit = new Kortti[5];

    private static Tekstuuri pidäNappiTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/nappi_pidä.png");
    private static Tekstuuri vaihdaNappiTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/nappi_vaihda.png");
    private static Tekstuuri häviöTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/teksti_häviö.png");
    private static Tekstuuri voittoTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/teksti_voitto.png");
    private static Tekstuuri tasapeliTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/teksti_tasapeli.png");
    private static Tekstuuri korttiTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortti_selkä.png");
    private static Teksti ohjeTeksti = new Teksti("ohje", Color.green, 600, 48);
    private static Teksti voitotTekstiPelaaja = new Teksti("0", Color.green, 200, 48);
    private static Teksti voitotTekstiVihollinen = new Teksti("0", Color.green, 200, 48);

    private static class Kortti {
        public int arvo;
        public boolean valittu;

        public Kortti() {
            this.arvo = random.nextInt(korttienMäärä);
            this.valittu = false;
        }
    }

    private static enum LiikkeenTila {
        ALKU,
        HEITTO_PELAAJA,
        PALAUTUS_PELAAJA,
        HEITTO_VIHOLLINEN,
        PALAUTUS_VIHOLLINEN,
        KÄÄNTÖ_VIHOLLINEN,
        JÄRJESTÄMINEN,
        VALMIS;
    }
    private static LiikkeenTila liikkeenTila = LiikkeenTila.ALKU;

    public static void renderöiKehys(Ikkuna window) {
        ruudunLeveys = window.getWidth();
        ruudunKorkeus = window.getHeight();
        float scaleX = ruudunLeveys/3f;
        float scaleY = ruudunKorkeus/2.4f;
        float offsetY = ruudunKorkeus/12f;
        if (siirtymä < 1) siirtymä += 0.05;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, -offsetY, 0);
        matKehys.scale(scaleX * siirtymä, scaleY * siirtymä, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();
    }
    
    public static void renderöiIkkuna(Ikkuna window, Kamera kamera) {
        ruudunLeveys = window.getWidth();
        ruudunKorkeus = window.getHeight();
        if (siirtymä >= 1) {
            peliShader.bind();
            peliShader.setUniform("sampler", 0);
            peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

            if (valikko) {
                float scaleXValikkoKuvake = ruudunLeveys/4f;
                float scaleYValikkoKuvake = ruudunKorkeus/4f;
                Matrix4f matValikkoKuvake = new Matrix4f();
                window.getView().scale(1, matValikkoKuvake);
                matValikkoKuvake.scale(scaleXValikkoKuvake, scaleYValikkoKuvake, 0);
                peliShader.setUniform("projection", matValikkoKuvake);
                alkuruutuTekstuuri.bind(0);
                Assets.getModel().render();
            }
            else {
                float scaleXKortti = ruudunLeveys/32f;
                float scaleYKortti = ruudunKorkeus/24f;
                float offsetXKortti = scaleXKortti*2f;
                float offsetYKortti = ruudunKorkeus/6f;
                for (int i = 0; i < 5; i++) {
                    Matrix4f matKorttiPelaaja = new Matrix4f();
                    window.getView().scale(1, matKorttiPelaaja);
                    matKorttiPelaaja.translate((i-2)*offsetXKortti + siirräXKorttiPelaaja[i], -offsetYKortti + siirräYKorttiPelaaja[i], 0);
                    matKorttiPelaaja.scale(scaleXKortti, scaleYKortti, 0);
                    peliShader.setUniform("projection", matKorttiPelaaja);
                    valitseKortinTekstuuri(pelaajanKortit[i].arvo).bind(0);
                    Assets.getModel().render();

                    Matrix4f matKorttiVihollinen = new Matrix4f();
                    window.getView().scale(1, matKorttiVihollinen);
                    matKorttiVihollinen.translate((i-2)*offsetXKortti + siirräXKorttiVihollinen[i], offsetYKortti + siirräYKorttiVihollinen[i], 0);
                    matKorttiVihollinen.scale(scaleXKortti, scaleYKortti, 0);
                    matKorttiVihollinen.rotate((float)Math.toRadians(180 - käännäYKorttiVihollinen[i]), new Vector3f(0, 1, 0));
                    peliShader.setUniform("projection", matKorttiVihollinen);
                    if (vihollisenKortitNäkyvissä) valitseKortinTekstuuri(vihollisenKortit[i].arvo).bind(0);
                    else korttiTekstuuri.bind(0);
                    Assets.getModel().render();
                }

                float scaleXNappi = ruudunLeveys/6f;
                float scaleYNappi = ruudunKorkeus/24f;
                Matrix4f matNappi = new Matrix4f();
                window.getView().scale(1, matNappi);
                matNappi.scale(scaleXNappi, scaleYNappi, 0);
                peliShader.setUniform("projection", matNappi);
                if (liikkeenTila == LiikkeenTila.VALMIS) {
                    valitseLoppuTeksti(voittaja).bind(0);
                    Assets.getModel().render();
                }
                else if (liikkeenTila == LiikkeenTila.ALKU) {
                    if (valitutKortit() > 0) vaihdaNappiTekstuuri.bind(0);
                    else pidäNappiTekstuuri.bind(0);
                    Assets.getModel().render();
                }

                float scaleXVoitotTeksti = ruudunLeveys/6f;
                float scaleYVoitotTeksti = ruudunKorkeus/24f;
                float offsetXVoitotTeksti = ruudunLeveys/10f;
                float offsetYVoitotTeksti = ruudunKorkeus/6.5f;
                Matrix4f matVoitotTekstiPelaaja = new Matrix4f();
                window.getView().scale(1, matVoitotTekstiPelaaja);
                matVoitotTekstiPelaaja.translate(-offsetXVoitotTeksti, -offsetYVoitotTeksti, 0);
                matVoitotTekstiPelaaja.scale(scaleXVoitotTeksti, scaleYVoitotTeksti, 0);
                peliShader.setUniform("projection", matVoitotTekstiPelaaja);
                voitotTekstiPelaaja.päivitäTeksti("" + pelaajanVoitot);
                voitotTekstiPelaaja.bind(0);
                Assets.getModel().render();
                Matrix4f matVoitotTekstiVihollinen = new Matrix4f();
                window.getView().scale(1, matVoitotTekstiVihollinen);
                matVoitotTekstiVihollinen.translate(-offsetXVoitotTeksti, offsetYVoitotTeksti, 0);
                matVoitotTekstiVihollinen.scale(scaleXVoitotTeksti, scaleYVoitotTeksti, 0);
                peliShader.setUniform("projection", matVoitotTekstiVihollinen);
                voitotTekstiVihollinen.päivitäTeksti("" + vihollisenVoitot);
                voitotTekstiVihollinen.bind(0);
                Assets.getModel().render();
                
                float scaleXOhjeTeksti = ruudunLeveys/6f;
                float scaleYOhjeTeksti = ruudunKorkeus/24f;
                float offsetYOhjeTeksti = ruudunKorkeus/12f;
                Matrix4f matOhjeTeksti = new Matrix4f();
                window.getView().scale(1, matOhjeTeksti);
                matOhjeTeksti.translate(0, -offsetYOhjeTeksti, 0);
                matOhjeTeksti.scale(scaleXOhjeTeksti, scaleYOhjeTeksti, 0);
                peliShader.setUniform("projection", matOhjeTeksti);
                if (liikkeenTila == LiikkeenTila.VALMIS) ohjeTeksti.päivitäTeksti("Space: Uusi peli");
                else if (liikkeenTila == LiikkeenTila.ALKU) ohjeTeksti.päivitäTeksti("Space: Pelaa");
                else ohjeTeksti.päivitäTeksti("");
                ohjeTeksti.bind(0);
                Assets.getModel().render();
                Matrix4f matOhjeTeksti2 = new Matrix4f();
                window.getView().scale(1, matOhjeTeksti2);
                matOhjeTeksti2.translate(0, offsetYOhjeTeksti, 0);
                matOhjeTeksti2.scale(scaleXOhjeTeksti, scaleYOhjeTeksti, 0);
                peliShader.setUniform("projection", matOhjeTeksti2);
                if (liikkeenTila == LiikkeenTila.ALKU) ohjeTeksti.päivitäTeksti("1-5: Valitse");
                else ohjeTeksti.päivitäTeksti("");
                ohjeTeksti.bind(0);
                Assets.getModel().render();

                nopeusHeitto = ruudunLeveys/50f;
                nopeusJärjestäminen = ruudunLeveys/200f;
                nopeusValinta = ruudunLeveys/250f;
            }
        }
    }

    private static Tekstuuri valitseKortinTekstuuri(int arvo) {
        switch (arvo) {
            case 0: return kupariTekstuuri;
            case 1: return olutolutTekstuuri;
            case 2: return karjalaTekstuuri;
            case 3: return lapinkultaTekstuuri;
            case 4: return alecoqTekstuuri;
            case 5: return karhuTekstuuri;
            case 6: return olviTekstuuri;
            case 7: return sandelsTekstuuri;
            default: return valkoinenTekstuuri;
        }
    }

    private static int laskePisteet(Kortti[] käsi) {
        int santut = 0;
        int olvit = 0;
        int karhut = 0;
        int alecoqit = 0;
        int lapparit = 0;
        int karjalat = 0;
        int olutoluet = 0;
        int kuparit = 0;
        for (Kortti kortti : käsi) {
            if (kortti.arvo == 7) santut++;
            else if (kortti.arvo == 6) olvit++;
            else if (kortti.arvo == 5) karhut++;
            else if (kortti.arvo == 4) alecoqit++;
            else if (kortti.arvo == 3) lapparit++;
            else if (kortti.arvo == 2) karjalat++;
            else if (kortti.arvo == 1) olutoluet++;
            else if (kortti.arvo == 0) kuparit++;
        }

        int pisteet = 0;
        // Vitoset
        if (santut == 5) pisteet = 116;
        else if (olvit == 5) pisteet = 115;
        else if (karhut == 5) pisteet = 114;
        else if (alecoqit == 5) pisteet = 113;
        else if (lapparit == 5) pisteet = 112;
        else if (karjalat == 5) pisteet = 111;
        else if (olutoluet == 5) pisteet = 110;
        else if (kuparit == 5) pisteet = 109;

        // Neloset
        else if (santut == 4) pisteet = 108;
        else if (olvit == 4) pisteet = 107;
        else if (karhut == 4) pisteet = 106;
        else if (alecoqit == 4) pisteet = 105;
        else if (lapparit == 4) pisteet = 104;
        else if (karjalat == 4) pisteet = 103;
        else if (olutoluet == 4) pisteet = 102;
        else if (kuparit == 4) pisteet = 101;

        // Täyskäsi - Kolmoset
        else if (santut == 3) {
            if (olvit == 2) pisteet = 100;
            else if (karhut == 2) pisteet = 99;
            else if (alecoqit == 2) pisteet = 98;
            else if (lapparit == 2) pisteet = 97;
            else if (karjalat == 2) pisteet = 96;
            else if (olutoluet == 2) pisteet = 95;
            else if (kuparit == 2) pisteet = 94;
            else pisteet = 44;
        }
        else if (olvit == 3) {
            if (santut == 2) pisteet = 93;
            else if (karhut == 2) pisteet = 92;
            else if (alecoqit == 2) pisteet = 91;
            else if (lapparit == 2) pisteet = 90;
            else if (karjalat == 2) pisteet = 89;
            else if (olutoluet == 2) pisteet = 88;
            else if (kuparit == 2) pisteet = 87;
            else pisteet = 43;
        }
        else if (karhut == 3) {
            if (santut == 2) pisteet = 86;
            else if (olvit == 2) pisteet = 85;
            else if (alecoqit == 2) pisteet = 84;
            else if (lapparit == 2) pisteet = 83;
            else if (karjalat == 2) pisteet = 82;
            else if (olutoluet == 2) pisteet = 81;
            else if (kuparit == 2) pisteet = 80;
            else pisteet = 42;
        }
        else if (alecoqit == 3) {
            if (santut == 2) pisteet = 79;
            else if (olvit == 2) pisteet = 78;
            else if (karhut == 2) pisteet = 77;
            else if (lapparit == 2) pisteet = 76;
            else if (karjalat == 2) pisteet = 75;
            else if (olutoluet == 2) pisteet = 74;
            else if (kuparit == 2) pisteet = 73;
            else pisteet = 41;
        }
        else if (lapparit == 3) {
            if (santut == 2) pisteet = 72;
            else if (olvit == 2) pisteet = 71;
            else if (karhut == 2) pisteet = 70;
            else if (alecoqit == 2) pisteet = 69;
            else if (karjalat == 2) pisteet = 68;
            else if (olutoluet == 2) pisteet = 67;
            else if (kuparit == 2) pisteet = 66;
            else pisteet = 40;
        }
        else if (karjalat == 3) {
            if (santut == 2) pisteet = 65;
            else if (olvit == 2) pisteet = 64;
            else if (karhut == 2) pisteet = 63;
            else if (alecoqit == 2) pisteet = 62;
            else if (lapparit == 2) pisteet = 61;
            else if (olutoluet == 2) pisteet = 60;
            else if (kuparit == 2) pisteet = 59;
            else pisteet = 39;
        }
        else if (olutoluet == 3) {
            if (santut == 2) pisteet = 58;
            else if (olvit == 2) pisteet = 57;
            else if (karhut == 2) pisteet = 56;
            else if (alecoqit == 2) pisteet = 55;
            else if (lapparit == 2) pisteet = 54;
            else if (karjalat == 2) pisteet = 53;
            else if (kuparit == 2) pisteet = 52;
            else pisteet = 38;
        }
        else if (kuparit == 3) {
            if (santut == 2) pisteet = 51;
            else if (olvit == 2) pisteet = 50;
            else if (karhut == 2) pisteet = 49;
            else if (alecoqit == 2) pisteet = 48;
            else if (lapparit == 2) pisteet = 47;
            else if (karjalat == 2) pisteet = 46;
            else if (olutoluet == 2) pisteet = 45;
            else pisteet = 37;
        }

        // 2 Paria - 1 Pari
        else if (santut == 2) {
            if (olvit == 2) pisteet = 36;
            else if (karhut == 2) pisteet = 35;
            else if (alecoqit == 2) pisteet = 34;
            else if (lapparit == 2) pisteet = 33;
            else if (karjalat == 2) pisteet = 32;
            else if (olutoluet == 2) pisteet = 31;
            else if (kuparit == 2) pisteet = 30;
            else pisteet = 8;
        }
        else if (olvit == 2) {
            if (karhut == 2) pisteet = 29;
            else if (alecoqit == 2) pisteet = 28;
            else if (lapparit == 2) pisteet = 27;
            else if (karjalat == 2) pisteet = 26;
            else if (olutoluet == 2) pisteet = 25;
            else if (kuparit == 2) pisteet = 24;
            else pisteet = 7;
        }
        else if (karhut == 2) {
            if (alecoqit == 2) pisteet = 23;
            else if (lapparit == 2) pisteet = 22;
            else if (karjalat == 2) pisteet = 21;
            else if (olutoluet == 2) pisteet = 20;
            else if (kuparit == 2) pisteet = 19;
            else pisteet = 6;
        }
        else if (alecoqit == 2) {
            if (lapparit == 2) pisteet = 18;
            else if (karjalat == 2) pisteet = 17;
            else if (olutoluet == 2) pisteet = 16;
            else if (kuparit == 2) pisteet = 15;
            else pisteet = 5;
        }
        else if (lapparit == 2) {
            if (karjalat == 2) pisteet = 14;
            else if (olutoluet == 2) pisteet = 13;
            else if (kuparit == 2) pisteet = 12;
            else pisteet = 4;
        }
        else if (karjalat == 2) {
            if (olutoluet == 2) pisteet = 11;
            else if (kuparit == 2) pisteet = 10;
            else pisteet = 3;
        }
        else if (olutoluet == 2) {
            if (kuparit == 2) pisteet = 9;
            else pisteet = 2;
        }
        else if (kuparit == 2) pisteet = 1;
        else pisteet = 0;

        return pisteet;
    }

    private static String tarkistaVoittaja() {
        int pelaajanPisteet = laskePisteet(pelaajanKortit);
        int vihollisenPisteet = laskePisteet(vihollisenKortit);
        if (pelaajanPisteet > vihollisenPisteet) {
            pelaajanVoitot++;
            return "pelaaja";
        }
        else if (pelaajanPisteet < vihollisenPisteet){
            vihollisenVoitot++;
            return "vihollinen";
        }
        else return "tasapeli";
    }

    private static Tekstuuri valitseLoppuTeksti(String voittaja) {
        switch (voittaja) {
            case "pelaaja": return voittoTekstuuri;
            case "vihollinen": return häviöTekstuuri;
            default: return tasapeliTekstuuri;
        }
    }

    public static void pelaa() {
        if (!valikko) {
            if (!peliKäynnissä) {
                jaaKortit();
                peliKäynnissä = true;
            }
            switch (liikkeenTila) {
                case ALKU:
                    boolean odota = !siirräValittujaKortteja();
                    if (pelaajaSiirtänyt) {
                        if (!odota) liikkeenTila = LiikkeenTila.HEITTO_PELAAJA;
                    }
                break;
                case HEITTO_PELAAJA: heitäKortitPelaaja(); break;
                case PALAUTUS_PELAAJA: palutaKortitPelaaja(); break;
                case HEITTO_VIHOLLINEN: heitäKortitVihollinen(); break;
                case PALAUTUS_VIHOLLINEN: palutaKortitVihollinen(); break;
                case JÄRJESTÄMINEN: siirräJärjestykseen(); break;
                case KÄÄNTÖ_VIHOLLINEN: käännäVihollisenKortit(); break;
                case VALMIS: näytäLoppuruutu(); break;
            }
        }
    }

    private static void jaaKortit() {
        kortitVaihdettu = false;
        for (int i = 0; i < 5; i++) {
            pelaajanKortit[i] = new Kortti();
            vihollisenKortit[i] = new Kortti();
        }
    }

    public static void valitseKortti(int valinta) {
        if (!valikko && (!kortitVaihdettu && valinta >= 0 && valinta < 5)) {
            pelaajanKortit[valinta].valittu = !pelaajanKortit[valinta].valittu;
        }
    }

    private static boolean siirräValittujaKortteja() {
        boolean valmis = true;
        for (int i = 0; i < 5; i++) {
            if (pelaajanKortit[i].valittu && siirräYKorttiPelaaja[i] < ruudunKorkeus/24f) {
                siirräYKorttiPelaaja[i] += nopeusValinta;
                valmis = false;
            }
            else if (!pelaajanKortit[i].valittu && siirräYKorttiPelaaja[i] > 0) {
                siirräYKorttiPelaaja[i] -= nopeusValinta;
                valmis = false;
            }
            if (Math.abs(siirräYKorttiPelaaja[i]) < nopeusValinta) {
                siirräYKorttiPelaaja[i] = 0;
            }
        }
        return valmis;
    }

    public static void pelaaValitut() {
        if (!valikko) {
            switch (liikkeenTila) {
                default: pelaajaSiirtänyt = true; break;
                case VALMIS: uusiPeli(); break;
            }
        }
    }

    private static int valitutKortit() {
        int valitutKortit = 0;
        for (int i = 0; i < 5; i++) {
            if (pelaajanKortit[i].valittu) {
                valitutKortit++;
            }
        }
        return valitutKortit;
    }

    private static void heitäKortitPelaaja() {
        boolean valmis = true;
        for (int i = 0; i < 5; i++) {
            if (pelaajanKortit[i].valittu && siirräYKorttiPelaaja[i] < ruudunKorkeus + (ruudunKorkeus/6)*i) {
                siirräYKorttiPelaaja[i] += nopeusHeitto;
                valmis = false;
            }
        }
        if (valmis) {
            vaihdaValitutPelaaja();
            liikkeenTila = LiikkeenTila.PALAUTUS_PELAAJA;
        }
    }

    private static void vaihdaValitutPelaaja() {
        for (int i = 0; i < 5; i++) {
            if (pelaajanKortit[i].valittu) {
                pelaajanKortit[i] = new Kortti();
            }
        }
        kortitVaihdettu = true;
    }

    private static void palutaKortitPelaaja() {
        boolean valmis = true;
        for (int i = 0; i < 5; i++) {
            if (siirräYKorttiPelaaja[i] > 0) {
                siirräYKorttiPelaaja[i] -= nopeusHeitto;
                valmis = false;
            }
            if (Math.abs(siirräYKorttiPelaaja[i]) < nopeusHeitto) {
                siirräYKorttiPelaaja[i] = 0;
            }
        }
        if (valmis) {
            liikkeenTila = LiikkeenTila.HEITTO_VIHOLLINEN;
            valitseKortitVihollinen();
        }
    }

    private static void valitseKortitVihollinen() {
        for (Kortti k : vihollisenKortit) {
            if (random.nextBoolean()) k.valittu = true;
        }
        heitäKortitVihollinen();
    }

    private static void heitäKortitVihollinen() {
        boolean valmis = true;
        for (int i = 0; i < 5; i++) {
            if (vihollisenKortit[i].valittu && siirräYKorttiVihollinen[i] < ruudunKorkeus + (ruudunKorkeus/6)*i) {
                siirräYKorttiVihollinen[i] += nopeusHeitto;
                valmis = false;
            }
        }
        if (valmis) {
            vaihdaValitutVihollinen();
            liikkeenTila = LiikkeenTila.PALAUTUS_VIHOLLINEN;
        }
    }

    private static void vaihdaValitutVihollinen() {
        for (int i = 0; i < 5; i++) {
            if (vihollisenKortit[i].valittu) {
                vihollisenKortit[i] = new Kortti();
            }
        }
        kortitVaihdettu = true;
    }

    private static void palutaKortitVihollinen() {
        boolean valmis = true;
        for (int i = 0; i < 5; i++) {
            if (siirräYKorttiVihollinen[i] > 0) {
                siirräYKorttiVihollinen[i] -= nopeusHeitto;
                valmis = false;
            }
            if (Math.abs(siirräYKorttiVihollinen[i]) < nopeusHeitto) {
                siirräYKorttiVihollinen[i] = 0;
            }
        }
        if (valmis) {
            liikkeenTila = LiikkeenTila.JÄRJESTÄMINEN;
            järjestäKortit();
        }
    }

    private static void siirräJärjestykseen() {
        boolean valmis = true;
        for (int i = 0; i < siirräXKorttiPelaaja.length; i++) {
            if (siirräXKorttiPelaaja[i] < kohdeXKorttiPelaajaJärjestetty[i]) {
                siirräXKorttiPelaaja[i] += nopeusJärjestäminen;
                valmis = false;
            }
            else if (siirräXKorttiPelaaja[i] > kohdeXKorttiPelaajaJärjestetty[i]) {
                siirräXKorttiPelaaja[i] -= nopeusJärjestäminen;
                valmis = false;
            }
            if (Math.abs(siirräXKorttiPelaaja[i] - kohdeXKorttiPelaajaJärjestetty[i])/2f < nopeusJärjestäminen) {
                siirräXKorttiPelaaja[i] = kohdeXKorttiPelaajaJärjestetty[i];
            }
        }
        for (int i = 0; i < siirräXKorttiVihollinen.length; i++) {
            if (siirräXKorttiVihollinen[i] < kohdeXKorttiVihollinenJärjestetty[i]) {
                siirräXKorttiVihollinen[i] += nopeusJärjestäminen;
                valmis = false;
            }
            else if (siirräXKorttiVihollinen[i] > kohdeXKorttiVihollinenJärjestetty[i]) {
                siirräXKorttiVihollinen[i] -= nopeusJärjestäminen;
                valmis = false;
            }
            if (Math.abs(siirräXKorttiVihollinen[i] - kohdeXKorttiVihollinenJärjestetty[i])/2f < nopeusJärjestäminen) {
                siirräXKorttiVihollinen[i] = kohdeXKorttiVihollinenJärjestetty[i];
            }
        }
        if (valmis) {
            liikkeenTila = LiikkeenTila.KÄÄNTÖ_VIHOLLINEN;
            voittaja = tarkistaVoittaja();
        }
    }

    static Integer[] arvotVihollinen = new Integer[vihollisenKortit.length];
    static Integer[] arvotVihollinenJärjestetty;
    private static void järjestäKortit() {
        Integer[] arvotPelaaja = new Integer[pelaajanKortit.length];
        for (int i = 0; i < arvotPelaaja.length; i++) {
            arvotPelaaja[i] = pelaajanKortit[i].arvo;
        }
        Integer[] arvotPelaajaJärjestetty = Arrays.copyOf(arvotPelaaja, arvotPelaaja.length);
        arvotPelaajaJärjestetty = sortByCounting(arvotPelaajaJärjestetty);
        
        for (int i = 0; i < arvotVihollinen.length; i++) {
            arvotVihollinen[i] = vihollisenKortit[i].arvo;
        }
        arvotVihollinenJärjestetty = Arrays.copyOf(arvotVihollinen, arvotVihollinen.length);
        arvotVihollinenJärjestetty = sortByCounting(arvotVihollinenJärjestetty);

        int index0P = Arrays.asList(arvotPelaajaJärjestetty).indexOf(arvotPelaaja[0]);
        int index1P = Arrays.asList(arvotPelaajaJärjestetty).indexOf(arvotPelaaja[1]);
        int index2P = Arrays.asList(arvotPelaajaJärjestetty).indexOf(arvotPelaaja[2]);
        int index3P = Arrays.asList(arvotPelaajaJärjestetty).indexOf(arvotPelaaja[3]);
        int index4P = Arrays.asList(arvotPelaajaJärjestetty).indexOf(arvotPelaaja[4]);

        int index0V = Arrays.asList(arvotVihollinenJärjestetty).indexOf(arvotVihollinen[0]);
        int index1V = Arrays.asList(arvotVihollinenJärjestetty).indexOf(arvotVihollinen[1]);
        int index2V = Arrays.asList(arvotVihollinenJärjestetty).indexOf(arvotVihollinen[2]);
        int index3V = Arrays.asList(arvotVihollinenJärjestetty).indexOf(arvotVihollinen[3]);
        int index4V = Arrays.asList(arvotVihollinenJärjestetty).indexOf(arvotVihollinen[4]);

        while (index1P == index0P) index1P++;
        while (index2P == index1P || index2P == index0P) index2P++;
        while (index3P == index2P || index3P == index1P || index3P == index0P) index3P++;
        while (index4P == index3P || index4P == index2P || index4P == index1P || index4P == index0P) index4P++;

        while (index1V == index0V) index1V++;
        while (index2V == index1V || index2V == index0V) index2V++;
        while (index3V == index2V || index3V == index1V || index3V == index0V) index3V++;
        while (index4V == index3V || index4V == index2V || index4V == index1V || index4V == index0V) index4V++;

        int siirrettävä0P = index0P - 0;
        int siirrettävä1P = index1P - 1;
        int siirrettävä2P = index2P - 2;
        int siirrettävä3P = index3P - 3;
        int siirrettävä4P = index4P - 4;

        int siirrettävä0V = index0V - 0;
        int siirrettävä1V = index1V - 1;
        int siirrettävä2V = index2V - 2;
        int siirrettävä3V = index3V - 3;
        int siirrettävä4V = index4V - 4;

        kohdeXKorttiPelaajaJärjestetty[0] = ruudunLeveys/16f * siirrettävä0P;
        kohdeXKorttiPelaajaJärjestetty[1] = ruudunLeveys/16f * siirrettävä1P;
        kohdeXKorttiPelaajaJärjestetty[2] = ruudunLeveys/16f * siirrettävä2P;
        kohdeXKorttiPelaajaJärjestetty[3] = ruudunLeveys/16f * siirrettävä3P;
        kohdeXKorttiPelaajaJärjestetty[4] = ruudunLeveys/16f * siirrettävä4P;

        kohdeXKorttiVihollinenJärjestetty[0] = ruudunLeveys/16f * siirrettävä0V;
        kohdeXKorttiVihollinenJärjestetty[1] = ruudunLeveys/16f * siirrettävä1V;
        kohdeXKorttiVihollinenJärjestetty[2] = ruudunLeveys/16f * siirrettävä2V;
        kohdeXKorttiVihollinenJärjestetty[3] = ruudunLeveys/16f * siirrettävä3V;
        kohdeXKorttiVihollinenJärjestetty[4] = ruudunLeveys/16f * siirrettävä4V;
    }

    private static Integer[] sortByCounting(Integer[] arr) {
        Map<Integer, Long> countMap = Arrays.stream(arr)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldV, newV) -> oldV, LinkedHashMap::new));
        Integer[] newArr = new Integer[arr.length];
        int i = 0;
        for (Map.Entry<Integer, Long> entry : countMap.entrySet()) {
            Arrays.fill(newArr, i, i += entry.getValue().intValue(), entry.getKey());
        }
        Collections.reverse(Arrays.asList(newArr));
        return newArr;
    }

    private static void käännäVihollisenKortit() {
        boolean valmis = true;
        for (int i = 0; i < 5; i++) {
            if (käännäYKorttiVihollinen[i] < 180) {
                käännäYKorttiVihollinen[i] += nopeusKääntö;
                valmis = false;
            }
            if (käännäYKorttiVihollinen[i] > 90) {
                vihollisenKortitNäkyvissä = true;
            }
        }
        if (valmis) {
            liikkeenTila = LiikkeenTila.VALMIS;
        }
    }

    private static void näytäLoppuruutu() {

    }

    public static void uusiPeli() {
        peliKäynnissä = false;
        pelaajaSiirtänyt = false;
        vihollisenKortitNäkyvissä = false;
        liikkeenTila = LiikkeenTila.ALKU;
        for (int i = 0; i < 5; i++) {
            siirräXKorttiPelaaja[i] = 0;
            siirräXKorttiVihollinen[i] = 0;
            siirräYKorttiPelaaja[i] = 0;
            siirräYKorttiVihollinen[i] = 0;
            käännäYKorttiVihollinen[i] = 0;
        }
    }

    private static void nollaaVoitot() {
        pelaajanVoitot = 0;
        vihollisenVoitot = 0;
    }

    public static void ohitaValikko() {
        if (valikko) valikko = false;
    }

    public static void avaaToimintoIkkuna() {
        valikko = true;
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_POKERI;
        nollaaVoitot();
        uusiPeli();
        Musat.suljeMusa();
        Musat.toistaPeliMusa("minipeli_pokeri");
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirtymä = 0;
        Musat.suljeMusa();
    }
}
