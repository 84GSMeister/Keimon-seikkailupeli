package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.objekti2d.Transform;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.seikkailupeli.Peli;
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

public class NäppäinVinkkiTekstit {
    private static Tekstuuri näppäinETekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_e.png");
    private static Tekstuuri näppäinCTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_c.png");
    private static Tekstuuri näppäinQTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_q.png");
    private static Tekstuuri näppäinNuoliTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/nuoli.png");
    private static Teksti näppäinVihjeTeksti = new Teksti("näppäin", Color.white, 900, 48);

    public static void renderöiNäppäinVinkki(Shader shader, Kamera camera, Transform transform) {
        shader.bind();
        float scaleXTeksti = 7f;
        float scaleYTeksti = 0.5f;
        float translateYTeksti1 = 3f;
        float translateYTeksti2 = 2f;
        KenttäKohde objektiKohdalla = Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];

        if (objektiKohdalla instanceof Esine) {
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
            näppäinETekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            if (Pelaaja.annaEsineidenMäärä() >= 6) näppäinVihjeTeksti.päivitäTeksti("Tavaraluettelo täynnä!" , 0, 36, Color.red);
            else näppäinVihjeTeksti.päivitäTeksti("Poimi", 0, 36, Color.white);
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
            näppäinCTekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            näppäinVihjeTeksti.päivitäTeksti("Katso");
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
        else if (objektiKohdalla instanceof Kiintopiste) {
            if (objektiKohdalla instanceof Nuotio) {
                if (Peli.valittuEsine != null) {
                    if (Peli.valittuEsine instanceof Makkara) {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Paista makkara");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                    else if (Peli.valittuEsine instanceof Hiili || Peli.valittuEsine instanceof Paperi) {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Lisää nuotioon");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                    else {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Kokeile esinettä");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                }
                else {
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                    näppäinETekstuuri.bind(0);
                    Assets.getModel().render();
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                    näppäinCTekstuuri.bind(0);
                    Assets.getModel().render();
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2*1.25f, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                    näppäinVihjeTeksti.päivitäTeksti("Katso");
                    näppäinVihjeTeksti.bind(0);
                    Assets.getModel().render();
                }
            }
            else if (objektiKohdalla instanceof Kirstu) {
                if (Peli.valittuEsine != null) {
                    if (Peli.valittuEsine instanceof Avain) {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Avaa");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                    else {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Kokeile esinettä");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                }
                else {
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                    näppäinCTekstuuri.bind(0);
                    Assets.getModel().render();
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                    näppäinVihjeTeksti.päivitäTeksti("Katso");
                    näppäinVihjeTeksti.bind(0);
                    Assets.getModel().render();
                }
            }
            else if (objektiKohdalla instanceof Lepopaikka) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Nuku");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof KauppaRuutu || objektiKohdalla instanceof BaariRuutu) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                if (objektiKohdalla instanceof BaariRuutu && Pelaaja.annaEsineidenMäärä() >= 6) näppäinVihjeTeksti.päivitäTeksti("Tavaraluettelo täynnä!" , 0, 36, Color.red);
                else näppäinVihjeTeksti.päivitäTeksti("Asioi", 0, 36, Color.white);
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof KauppaHylly) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Lisää koriin");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                näppäinQTekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Poista korista");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Pulloautomaatti) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Palauta pullot");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Ämpärikone) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Jonota");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Pelikone) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Pelaa");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Silta || objektiKohdalla instanceof KoristeOvi) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Katso");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
        }
        else if (objektiKohdalla instanceof NPC_KenttäKohde) {
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
            näppäinETekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            näppäinVihjeTeksti.päivitäTeksti("Juttele");
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
            näppäinCTekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            näppäinVihjeTeksti.päivitäTeksti("Katso");
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
        else if (objektiKohdalla instanceof Triggeri) {
            if (objektiKohdalla instanceof Nappi) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Paina");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Katso");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
        }
        else if (objektiKohdalla instanceof VisuaalinenObjekti) {
            VisuaalinenObjekti vo = (VisuaalinenObjekti)objektiKohdalla;
            if (vo.onkoKatsottava()) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
                näppäinVihjeTeksti.päivitäTeksti("Katso");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
        }
        else if (objektiKohdalla instanceof AvattavaEste) {
            
        }
        else if (objektiKohdalla instanceof Warp) {
            Warp warp = (Warp)objektiKohdalla;
            näppäinNuoliTekstuuri.bind(0);
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
            switch (warp.annaSuunta()) {
                case VASEN: Assets.getModel(270, false, false).render(); break;
                case OIKEA: Assets.getModel(90, false, false).render(); break;
                case ALAS: Assets.getModel(180, false, false).render(); break;
                case YLÖS: Assets.getModel(0, false, false).render(); break;
                default: break;
            }
        }
        else {
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
            näppäinETekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, scaleYTeksti, 1));
            näppäinVihjeTeksti.päivitäTeksti("Katso");
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }
}
