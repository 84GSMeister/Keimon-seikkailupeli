package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.objekti2d.Transform;
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
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.Nappi;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.Triggeri;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;

import java.awt.Color;

import org.joml.Vector4f;

public class NäppäinVinkkiTekstit {
    private static Tekstuuri näppäinETekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_e.png");
    private static Tekstuuri näppäinQTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_q.png");
    private static Tekstuuri näppäinZTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_z.png");
    private static Tekstuuri näppäinXTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_x.png");
    private static Tekstuuri näppäinCTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_c.png");
    private static Tekstuuri näppäinNuoliTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_nuoli.png");
    private static Tekstuuri näppäinSpaceTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_space.png");

    private static Tekstuuri ohjainXboxATekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_a.png");
    private static Tekstuuri ohjainXboxBTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_b.png");
    private static Tekstuuri ohjainXboxXTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_x.png");
    private static Tekstuuri ohjainXboxYTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_y.png");
    private static Tekstuuri ohjainXboxRtTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_rt.png");
    private static Tekstuuri ohjainNintendoATekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_a.png");
    private static Tekstuuri ohjainNintendoBTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_b.png");
    private static Tekstuuri ohjainNintendoXTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_x.png");
    private static Tekstuuri ohjainNintendoYTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_y.png");
    private static Tekstuuri ohjainNintendoRzTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_rz.png");
    private static Tekstuuri ohjainPlaystationXTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_x.png");
    private static Tekstuuri ohjainPlaystationYmpyräTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_ympyrä.png");
    private static Tekstuuri ohjainPlaystationKolmioTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_kolmio.png");
    private static Tekstuuri ohjainPlaystationNeliöTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_neliö.png");
    private static Tekstuuri ohjainPlaystationR2Tekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_r2.png");
    private static Tekstuuri ohjainRTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_r.png");
    
    private static Tekstuuri ohjainSelectTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_select.png");
    private static Tekstuuri ohjainNuoliTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nuoli.png");
    private static Tekstuuri ohjainAnalogTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_analog.png");

    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
    private static Teksti näppäinVihjeTeksti = new Teksti("näppäin", Color.white, 900, 48);
    private static int käytettävänEsineenVinkkiAjastin = 150;
    private static Esine viimeisinEsine;

    private static enum Näppäimet {
        NÄPPÄIN_E_OHJAIN_ALA,
        NÄPPÄIN_Q_OHJAIN_RT,
        NÄPPÄIN_SPACE_OHJAIN_OIKEA,
        NÄPPÄIN_C_OHJAIN_VASEN,
        NÄPPÄIN_Z_OHJAIN_SELECT,
        NÄPPÄIN_X_OHJAIN_R,
        NÄPPÄIN_NUOLI;
    }

    public static void renderöiNäppäinVinkki(KenttäKohde objektiKohdalla, Shader shader, Kamera camera, Transform transform) {
        if (objektiKohdalla instanceof Esine) {
            String teksti = "";
            Color väri;
            if (Pelaaja.annaEsineidenMäärä() >= Pelaaja.esineet.length) {
                teksti = "Tavaraluettelo täynnä!";
                väri = Color.red;
            }
            else {
                teksti = "Poimi";
                väri = Color.white;
            }
            renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, teksti, 0, väri, 0, 36, shader, camera, transform);
            renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
        }
        else if (objektiKohdalla instanceof Kiintopiste) {
            if (objektiKohdalla instanceof Nuotio) {
                if (Peli.valittuEsine != null) {
                    if (Peli.valittuEsine instanceof Makkara) {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Paista makkara", shader, camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
                    }
                    else if (Peli.valittuEsine instanceof Hiili || Peli.valittuEsine instanceof Paperi) {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Lisää nuotioon", shader, camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
                    }
                    else {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Kokeile esinettä", shader, camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
                    }
                }
                else {
                    renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", shader, camera, transform);
                }
            }
            else if (objektiKohdalla instanceof Kirstu) {
                if (Peli.valittuEsine != null) {
                    if (Peli.valittuEsine instanceof Avain) {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Avaa", shader, camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
                    }
                    else {
                        renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Kokeile esinettä", shader, camera, transform);
                        renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
                    }
                }
                else {
                    renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", shader, camera, transform);
                }
            }
            else if (objektiKohdalla instanceof Lepopaikka) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Nuku", shader, camera, transform);
            }
            else if (objektiKohdalla instanceof KauppaRuutu || objektiKohdalla instanceof BaariRuutu) {
                String teksti = "";
                Color väri;
                if (Pelaaja.annaEsineidenMäärä() >= 6) {
                    teksti = "Tavaraluettelo täynnä!";
                    väri = Color.red;
                }
                else {
                    teksti = "Asioi";
                    väri = Color.white;
                }
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, teksti, 0, väri, 0, 36, shader, camera, transform);
            }
            else if (objektiKohdalla instanceof KauppaHylly) {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Lisää koriin", shader, camera, transform);
                renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_Q_OHJAIN_RT, "Poista korista", shader, camera, transform);
            }
            else if (objektiKohdalla instanceof Pulloautomaatti) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Palauta pullot", shader, camera, transform);
            }
            else if (objektiKohdalla instanceof Ämpärikone) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Jonota", shader, camera, transform);
            }
            else if (objektiKohdalla instanceof Pelikone) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Pelaa", shader, camera, transform);
            }
            else if (objektiKohdalla instanceof Silta || objektiKohdalla instanceof KoristeOvi) {
                renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", shader, camera, transform);
            }
        }
        else if (objektiKohdalla instanceof NPC_KenttäKohde) {
            renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Juttele", shader, camera, transform);
            renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
        }
        else if (objektiKohdalla instanceof Triggeri) {
            if (objektiKohdalla instanceof Nappi) {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Paina", shader, camera, transform);
                renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
            }
            else {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Kokeile", shader, camera, transform);
                renderöiAlempiVinkki(Näppäimet.NÄPPÄIN_C_OHJAIN_VASEN, "Katso", shader, camera, transform);
            }
        }
        else if (objektiKohdalla instanceof VisuaalinenObjekti) {
            VisuaalinenObjekti vo = (VisuaalinenObjekti)objektiKohdalla;
            if (vo.onkoKatsottava()) {
                renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", shader, camera, transform);
            }
        }
        else if (objektiKohdalla instanceof AvattavaEste) {
            
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
            renderöiYlempiVinkki(Näppäimet.NÄPPÄIN_NUOLI, "", kääntöAsteet, Color.white, 0, 36, shader, camera, transform);
        }
        else {
            renderöiKeskiVinkki(Näppäimet.NÄPPÄIN_E_OHJAIN_ALA, "Katso", shader, camera, transform);
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

    private static void renderöiYlempiVinkki(Näppäimet näppäin, String teksti, Shader shader, Kamera camera, Transform transform) {
        renderöiYlempiVinkki(näppäin, teksti, 0, Color.white, 0, 36, shader, camera, transform);
    }

    private static void renderöiYlempiVinkki(Näppäimet näppäin, String teksti, int kääntöAsteet, Color väri, int tekstiTyyppi, int tekstinKoko, Shader shader, Kamera camera, Transform transform) {
        shader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateY = 3f;
        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
        valitseTekstuuri(näppäin).bind(0);
        Assets.getModel(kääntöAsteet, false, false).render();
        if (teksti != null && !teksti.equals("")) {
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            näppäinVihjeTeksti.päivitäTeksti(teksti, 0, 36, väri);
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }

    private static void renderöiKeskiVinkki(Näppäimet näppäin, String teksti, Shader shader, Kamera camera, Transform transform) {
        shader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateY = 2.5f;
        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
        valitseTekstuuri(näppäin).bind(0);
        Assets.getModel().render();
        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
        näppäinVihjeTeksti.päivitäTeksti(teksti);
        näppäinVihjeTeksti.bind(0);
        Assets.getModel().render();
    }

    private static void renderöiAlempiVinkki(Näppäimet näppäin, String teksti, Shader shader, Kamera camera, Transform transform) {
        shader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateY = 2f;
        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
        valitseTekstuuri(näppäin).bind(0);
        Assets.getModel().render();
        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
        näppäinVihjeTeksti.päivitäTeksti(teksti);
        näppäinVihjeTeksti.bind(0);
        Assets.getModel().render();
    }

    private static void renderöiKäyttöVinkki(float ySij, Näppäimet näppäin, String teksti, Shader shader, Kamera camera, Transform transform) {
        if (käytettävänEsineenVinkkiAjastin > 0) {
            shader.bind();
            float scaleXTeksti = 7f;
            float scaleYTeksti = 0.5f;
            float translateY = ySij;
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, translateY, 0).scale(0.5f, 0.5f, 1));
            if (käytettävänEsineenVinkkiAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1f - käytettävänEsineenVinkkiAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            valitseTekstuuri(näppäin).bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateY, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            if (käytettävänEsineenVinkkiAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1f - käytettävänEsineenVinkkiAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            näppäinVihjeTeksti.päivitäTeksti(teksti);
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }

    private static Tekstuuri valitseTekstuuri(Näppäimet näppäin) {
        if (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ) {
            switch (näppäin) {
                case NÄPPÄIN_E_OHJAIN_ALA: return näppäinETekstuuri;
                case NÄPPÄIN_Q_OHJAIN_RT: return näppäinQTekstuuri;
                case NÄPPÄIN_Z_OHJAIN_SELECT: return näppäinZTekstuuri;
                case NÄPPÄIN_X_OHJAIN_R: return näppäinXTekstuuri;
                case NÄPPÄIN_C_OHJAIN_VASEN: return näppäinCTekstuuri;
                case NÄPPÄIN_SPACE_OHJAIN_OIKEA: return näppäinSpaceTekstuuri;
                case NÄPPÄIN_NUOLI: return näppäinNuoliTekstuuri;
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
                        case NÄPPÄIN_NUOLI: return ohjainAnalogTekstuuri;
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
                        case NÄPPÄIN_NUOLI: return ohjainAnalogTekstuuri;
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
                        case NÄPPÄIN_NUOLI: return ohjainAnalogTekstuuri;
                        default: return virheTekstuuri;
                    }
                }
                default -> {return virheTekstuuri;}
            }
        }
        else return virheTekstuuri;
    }
}
