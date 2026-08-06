package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.objekti2d.Transform;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.VisuaalinenObjekti;
import keimo.seikkailupeli.objektit.kenttäkohteet.avattavaEste.AvattavaEste;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä.Kerättävä;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.Nappi;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.Triggeri;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;

import org.joml.Vector4f;

public class NäppäinVinkkiTekstit {
    private static Renderöitävä näppäinETekstuuri = Assets.annaTekstuuri("näppäin_e");
    private static Renderöitävä näppäinQTekstuuri = Assets.annaTekstuuri("näppäin_q");
    private static Renderöitävä näppäinZTekstuuri = Assets.annaTekstuuri("näppäin_z");
    private static Renderöitävä näppäinXTekstuuri = Assets.annaTekstuuri("näppäin_x");
    private static Renderöitävä näppäinCTekstuuri = Assets.annaTekstuuri("näppäin_c");
    private static Renderöitävä näppäinNuoliTekstuuri = Assets.annaTekstuuri("näppäin_nuoli");
    private static Renderöitävä näppäinSpaceTekstuuri = Assets.annaTekstuuri("näppäin_space");

    private static Renderöitävä ohjainXboxATekstuuri = Assets.annaTekstuuri("ohjain_xbox_a");
    private static Renderöitävä ohjainXboxBTekstuuri = Assets.annaTekstuuri("ohjain_xbox_b");
    private static Renderöitävä ohjainXboxXTekstuuri = Assets.annaTekstuuri("ohjain_xbox_x");
    private static Renderöitävä ohjainXboxYTekstuuri = Assets.annaTekstuuri("ohjain_xbox_y");
    private static Renderöitävä ohjainXboxRtTekstuuri = Assets.annaTekstuuri("ohjain_xbox_rt");
    private static Renderöitävä ohjainNintendoATekstuuri = Assets.annaTekstuuri("ohjain_nintendo_a");
    private static Renderöitävä ohjainNintendoBTekstuuri = Assets.annaTekstuuri("ohjain_nintendo_b");
    private static Renderöitävä ohjainNintendoXTekstuuri = Assets.annaTekstuuri("ohjain_nintendo_x");
    private static Renderöitävä ohjainNintendoYTekstuuri = Assets.annaTekstuuri("ohjain_nintendo_y");
    private static Renderöitävä ohjainNintendoRzTekstuuri = Assets.annaTekstuuri("ohjain_nintendo_rz");
    private static Renderöitävä ohjainPlaystationXTekstuuri = Assets.annaTekstuuri("ohjain_playstation_x");
    private static Renderöitävä ohjainPlaystationYmpyräTekstuuri = Assets.annaTekstuuri("ohjain_playstation_ympyrä");
    private static Renderöitävä ohjainPlaystationKolmioTekstuuri = Assets.annaTekstuuri("ohjain_playstation_kolmio");
    private static Renderöitävä ohjainPlaystationNeliöTekstuuri = Assets.annaTekstuuri("ohjain_playstation_neliö");
    private static Renderöitävä ohjainPlaystationR2Tekstuuri = Assets.annaTekstuuri("ohjain_playstation_r2");

    private static Renderöitävä ohjainRTekstuuri = Assets.annaTekstuuri("ohjain_r");
    private static Renderöitävä ohjainSelectTekstuuri = Assets.annaTekstuuri("ohjain_select");
    private static Renderöitävä ohjainNuoliTekstuuri = Assets.annaTekstuuri("ohjain_nuoli");
    private static Renderöitävä ohjainAnalogTekstuuri = Assets.annaTekstuuri("ohjain_analog");

    private static Shader staattinenShader = Assets.annaShader("staattinen");
    private static Shader tekstiShaderYlempi = Assets.annaShader("teksti");
    private static Shader tekstiShaderAlempi = Assets.annaShader("teksti2");
    private static Renderöitävä virheTekstuuri = Assets.annaTekstuuri("vakio");
    private static Teksti näppäinVihjeTeksti = new Teksti("näppäin", Väri.white, 900, 48);
    private static Renderöitävä vinkkiPohjaTekstuuri = Assets.annaTekstuuri("vinkki_teksti_pohja");
    private static int käytettävänEsineenVinkkiAjastin = 150;
    private static Esine viimeisinEsine;

    private static enum Näppäimet {
        NÄPPÄIN_E_OHJAIN_ALA,
        NÄPPÄIN_Q_OHJAIN_RT,
        NÄPPÄIN_SPACE_OHJAIN_OIKEA,
        NÄPPÄIN_C_OHJAIN_VASEN,
        NÄPPÄIN_Z_OHJAIN_SELECT,
        NÄPPÄIN_X_OHJAIN_R,
        NÄPPÄIN_NUOLI_OHJAIN_ANALOG,
        OHJAIN_NUOLI,
        OHJAIN_YLÄ;
    }

    public static void renderöiNäppäinVinkki(KenttäKohde objektiKohdalla, Kamera camera, Transform transform) {
        
        if (objektiKohdalla instanceof Esine) {
            String teksti = "";
            Väri väri;
            if (Pelaaja.annaEsineidenMäärä() >= Pelaaja.esineet.length) {
                teksti = "Tavaraluettelo täynnä!";
                väri = Väri.red;
            }
            else {
                teksti = "Poimi";
                väri = Väri.white;
            }
            renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, teksti, 0, väri, 0, 36, camera, transform);
            renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
        }
        else if (objektiKohdalla instanceof Kiintopiste) {
            if (objektiKohdalla instanceof Nuotio) {
                if (Peli.valittuEsine != null) {
                    if (Peli.valittuEsine instanceof Makkara) {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Paista makkara", camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
                    }
                    else if (Peli.valittuEsine instanceof Hiili || Peli.valittuEsine instanceof Paperi) {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Lisää nuotioon", camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
                    }
                    else {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Kokeile esinettä", camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
                    }
                }
                else {
                    renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", camera, transform);
                }
            }
            else if (objektiKohdalla instanceof Kirstu) {
                if (Peli.valittuEsine != null) {
                    if (Peli.valittuEsine instanceof Avain) {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Avaa", camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
                    }
                    else {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Kokeile esinettä", camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
                    }
                }
                else {
                    renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", camera, transform);
                }
            }
            else if (objektiKohdalla instanceof Lepopaikka) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Nuku", camera, transform);
            }
            else if (objektiKohdalla instanceof KauppaRuutu || objektiKohdalla instanceof BaariRuutu) {
                String teksti = "";
                Väri väri;
                if (Pelaaja.annaEsineidenMäärä() >= 6) {
                    teksti = "Tavaraluettelo täynnä!";
                    väri = Väri.red;
                }
                else {
                    teksti = "Asioi";
                    väri = Väri.white;
                }
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, teksti, 0, väri, 0, 36, camera, transform);
            }
            else if (objektiKohdalla instanceof KauppaHylly) {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Lisää koriin", camera, transform);
                renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_Q_OHJAIN_RT, "Poista korista", camera, transform);
            }
            else if (objektiKohdalla instanceof Pulloautomaatti) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Palauta pullot", camera, transform);
            }
            else if (objektiKohdalla instanceof Ämpärikone) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Jonota", camera, transform);
            }
            else if (objektiKohdalla instanceof Pelikone) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Pelaa", camera, transform);
            }
            else if (objektiKohdalla instanceof Silta || objektiKohdalla instanceof KoristeOvi) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", camera, transform);
            }
        }
        else if (objektiKohdalla instanceof NPC_KenttäKohde) {
            renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Juttele", camera, transform);
            renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
        }
        else if (objektiKohdalla instanceof Triggeri) {
            if (objektiKohdalla instanceof Nappi) {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Paina", camera, transform);
                renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
            }
            else {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Kokeile", camera, transform);
                renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", camera, transform);
            }
        }
        else if (objektiKohdalla instanceof VisuaalinenObjekti) {
            VisuaalinenObjekti vo = (VisuaalinenObjekti)objektiKohdalla;
            if (vo.onkoKatsottava()) {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", camera, transform);
            }
        }
        else if (objektiKohdalla instanceof AvattavaEste) {
            
        }
        else if (objektiKohdalla instanceof Kerättävä) {
            
        }
        else if (objektiKohdalla instanceof Warp) {
            Warp warp = (Warp)objektiKohdalla;
            int kääntöAsteet = 0;
            switch (warp.annaSuunta()) {
                case VASEN: kääntöAsteet = 270; break;
                case OIKEA: kääntöAsteet = 90; break;
                case ALAS: kääntöAsteet = 180; break;
                case YLÖS: kääntöAsteet = 0; break;
                default: break;
            }
            renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_NUOLI_OHJAIN_ANALOG, "", kääntöAsteet, Väri.white, 0, 36, camera, transform);
        }
        else {
            renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", camera, transform);
        }
    }

    public static void renderöiKäyttöesineenVinkki(Esine esineKädessä, Shader shader, Kamera camera, Transform transform) {
        if (esineKädessä == null || !esineKädessä.equals(viimeisinEsine)) {
            käytettävänEsineenVinkkiAjastin = 150;
        }
        else {
            käytettävänEsineenVinkkiAjastin--;
            renderöiKäyttöVinkki(-2f, Näppäimet.NÄPPÄIN_X_OHJAIN_R, "Katso", shader, camera, transform);
            if (esineKädessä.onkoKäyttö()) {
                if (esineKädessä instanceof Juoma) {
                    renderöiKäyttöVinkki(-3f, Näppäimet.NÄPPÄIN_SPACE_OHJAIN_OIKEA, "Juo", shader, camera, transform);
                }
                else if (esineKädessä instanceof Ruoka) {
                    renderöiKäyttöVinkki(-3f, Näppäimet.NÄPPÄIN_SPACE_OHJAIN_OIKEA, "Syö", shader, camera, transform);
                }
                else {
                    renderöiKäyttöVinkki(-3f, Näppäimet.NÄPPÄIN_SPACE_OHJAIN_OIKEA, "Käytä", shader, camera, transform);
                }
            }
            else if (esineKädessä.onkoKenttäkäyttöön()) {
                if (esineKädessä instanceof Ase) {
                    renderöiKäyttöVinkki(-3f, Näppäimet.NÄPPÄIN_SPACE_OHJAIN_OIKEA, "Käytä asetta", shader, camera, transform);
                }
            }
            if (esineKädessä.onkoYhdistettävä()) {
                renderöiKäyttöVinkki(-4f, Näppäimet.NÄPPÄIN_Z_OHJAIN_SELECT, "Yhdistä", shader, camera, transform);
            }
        }
        viimeisinEsine = esineKädessä;
    }

    private static void renderöiYlempiVinkki(Näppäimet näppäin, String teksti, Kamera camera, Transform transform) {
        renderöiYlempiVinkki(näppäin, teksti, 0, Väri.white, 0, 36, camera, transform);
    }

    private static void renderöiYlempiVinkki(Näppäimet näppäin, String teksti, int kääntöAsteet, Väri väri, int tekstiTyyppi, int tekstinKoko, Kamera camera, Transform transform) {
        staattinenShader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateY = 3f;
        staattinenShader.asetaSijainti(transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
        valitseTekstuuri(näppäin).bind(0);
        Assets.getModel(kääntöAsteet, false, false).render();

        if (teksti != null && !teksti.equals("")) {
            staattinenShader.asetaSijainti(transform.getProjection(camera.getProjection()).translate((1f/3f)*teksti.length(), translateY, 0).scale((1f/3f)*teksti.length(), scaleYTeksti, 1));
            vinkkiPohjaTekstuuri.bind(0);
            Assets.getModel().render();

            tekstiShaderYlempi.bind();
            tekstiShaderYlempi.asetaSijainti(transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            näppäinVihjeTeksti.päivitäTeksti(teksti, 0, 36, väri);
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
        tekstiShaderYlempi.loop();
    }

    private static void renderöiKeskiVinkki(Näppäimet näppäin, String teksti, Kamera camera, Transform transform) {
        staattinenShader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateY = 2.5f;
        staattinenShader.asetaSijainti(transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
        valitseTekstuuri(näppäin).bind(0);
        Assets.getModel().render();

        staattinenShader.asetaSijainti(transform.getProjection(camera.getProjection()).translate((1f/3f)*teksti.length(), translateY, 0).scale((1f/3f)*teksti.length(), scaleYTeksti, 1));
        vinkkiPohjaTekstuuri.bind(0);
        Assets.getModel().render();

        tekstiShaderYlempi.bind();
        tekstiShaderYlempi.asetaSijainti(transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
        näppäinVihjeTeksti.päivitäTeksti(teksti);
        näppäinVihjeTeksti.bind(0);
        Assets.getModel().render();
        tekstiShaderYlempi.loop();
    }

    private static void renderöiAlempiVinkki(Näppäimet näppäin, String teksti, Kamera camera, Transform transform) {
        staattinenShader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateY = 2f;
        staattinenShader.asetaSijainti(transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
        valitseTekstuuri(näppäin).bind(0);
        Assets.getModel().render();

        staattinenShader.asetaSijainti(transform.getProjection(camera.getProjection()).translate((1f/3f)*teksti.length(), translateY, 0).scale((1f/3f)*teksti.length(), scaleYTeksti, 1));
        vinkkiPohjaTekstuuri.bind(0);
        Assets.getModel().render();

        tekstiShaderAlempi.bind();
        tekstiShaderAlempi.asetaSijainti(transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
        näppäinVihjeTeksti.päivitäTeksti(teksti);
        näppäinVihjeTeksti.bind(0);
        Assets.getModel().render();
        tekstiShaderAlempi.loop();
    }

    private static void renderöiKäyttöVinkki(float ySij, Näppäimet näppäin, String teksti, Shader shader, Kamera camera, Transform transform) {
        if (käytettävänEsineenVinkkiAjastin > 0) {
            shader.bind();
            float scaleXTeksti = 7f;
            float scaleYTeksti = 0.5f;
            float translateY = ySij;
            shader.asetaSijainti(transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
            if (käytettävänEsineenVinkkiAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1f - käytettävänEsineenVinkkiAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            valitseTekstuuri(näppäin).bind(0);
            Assets.getModel().render();
            shader.asetaSijainti(transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            if (käytettävänEsineenVinkkiAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1f - käytettävänEsineenVinkkiAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            näppäinVihjeTeksti.päivitäTeksti(teksti);
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }

    private static Renderöitävä valitseTekstuuri(Näppäimet näppäin) {
        if (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ) {
            switch (näppäin) {
                case NÄPPÄIN_E_OHJAIN_ALA: return näppäinETekstuuri;
                case NÄPPÄIN_Q_OHJAIN_RT: return näppäinQTekstuuri;
                case NÄPPÄIN_Z_OHJAIN_SELECT: return näppäinZTekstuuri;
                case NÄPPÄIN_X_OHJAIN_R: return näppäinXTekstuuri;
                case NÄPPÄIN_C_OHJAIN_VASEN: return näppäinCTekstuuri;
                case NÄPPÄIN_SPACE_OHJAIN_OIKEA: return näppäinSpaceTekstuuri;
                case NÄPPÄIN_NUOLI_OHJAIN_ANALOG: return näppäinNuoliTekstuuri;
                default: return virheTekstuuri;
            }
        }
        else if (Peli.viimeisinSyöteLaite == SyöteLaitteet.PELIOHJAIN) {
            switch (PelinAsetukset.ohjainKuvakkeet) {
                case XBOX -> {
                    switch (näppäin) {
                        case NÄPPÄIN_E_OHJAIN_ALA: return ohjainXboxATekstuuri;
                        case NÄPPÄIN_Q_OHJAIN_RT: return ohjainXboxRtTekstuuri;
                        case NÄPPÄIN_C_OHJAIN_VASEN: return ohjainXboxXTekstuuri;
                        case NÄPPÄIN_SPACE_OHJAIN_OIKEA: return ohjainXboxBTekstuuri;
                        case NÄPPÄIN_X_OHJAIN_R: return ohjainRTekstuuri;
                        case NÄPPÄIN_Z_OHJAIN_SELECT: return ohjainSelectTekstuuri;
                        case NÄPPÄIN_NUOLI_OHJAIN_ANALOG: return ohjainAnalogTekstuuri;
                        case OHJAIN_NUOLI: return ohjainNuoliTekstuuri;
                        case OHJAIN_YLÄ: return ohjainXboxYTekstuuri;
                        default: return virheTekstuuri;
                    }
                }
                case NINTENDO -> {
                    switch (näppäin) {
                        case NÄPPÄIN_E_OHJAIN_ALA: return ohjainNintendoBTekstuuri;
                        case NÄPPÄIN_Q_OHJAIN_RT: return ohjainNintendoRzTekstuuri;
                        case NÄPPÄIN_C_OHJAIN_VASEN: return ohjainNintendoYTekstuuri;
                        case NÄPPÄIN_SPACE_OHJAIN_OIKEA: return ohjainNintendoATekstuuri;
                        case NÄPPÄIN_X_OHJAIN_R: return ohjainRTekstuuri;
                        case NÄPPÄIN_Z_OHJAIN_SELECT: return ohjainSelectTekstuuri;
                        case NÄPPÄIN_NUOLI_OHJAIN_ANALOG: return ohjainAnalogTekstuuri;
                        case OHJAIN_NUOLI: return ohjainNuoliTekstuuri;
                        case OHJAIN_YLÄ: return ohjainNintendoXTekstuuri;
                        default: return virheTekstuuri;
                    }
                }
                case PLAYSTATION -> {
                    switch (näppäin) {
                        case NÄPPÄIN_E_OHJAIN_ALA: return ohjainPlaystationXTekstuuri;
                        case NÄPPÄIN_Q_OHJAIN_RT: return ohjainPlaystationR2Tekstuuri;
                        case NÄPPÄIN_C_OHJAIN_VASEN: return ohjainPlaystationNeliöTekstuuri;
                        case NÄPPÄIN_SPACE_OHJAIN_OIKEA: return ohjainPlaystationYmpyräTekstuuri;
                        case NÄPPÄIN_X_OHJAIN_R: return ohjainRTekstuuri;
                        case NÄPPÄIN_Z_OHJAIN_SELECT: return ohjainSelectTekstuuri;
                        case NÄPPÄIN_NUOLI_OHJAIN_ANALOG: return ohjainAnalogTekstuuri;
                        case OHJAIN_NUOLI: return ohjainNuoliTekstuuri;
                        case OHJAIN_YLÄ: return ohjainPlaystationKolmioTekstuuri;
                        default: return virheTekstuuri;
                    }
                }
                default -> {return virheTekstuuri;}
            }
        }
        else return virheTekstuuri;
    }
}
