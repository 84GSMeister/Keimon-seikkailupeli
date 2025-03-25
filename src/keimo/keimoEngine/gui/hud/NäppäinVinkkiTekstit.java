package keimo.keimoEngine.gui.hud;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.grafiikat.objekti2d.Transform;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.VisuaalinenObjekti;
import keimo.kenttäkohteet.avattavaEste.AvattavaEste;
import keimo.kenttäkohteet.esine.*;
import keimo.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.kenttäkohteet.kiintopiste.*;
import keimo.kenttäkohteet.triggeri.Nappi;
import keimo.kenttäkohteet.triggeri.Triggeri;
import keimo.kenttäkohteet.warp.Warp;

import java.awt.Color;

public class NäppäinVinkkiTekstit {
    private static Tekstuuri näppäinETekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_e.png");
    private static Tekstuuri näppäinCTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_c.png");
    private static Tekstuuri näppäinNuoliTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/nuoli.png");
    private static Teksti näppäinVihjeTeksti = new Teksti("näppäin", Color.white, 300, 20);

    public static void renderöiNäppäinVinkki(Shader shader, Kamera camera, Transform transform) {
        shader.bind();
        float scaleXTeksti = 7f;
        float translateYTeksti1 = 2.625f;
        float translateYTeksti2 = 1.625f;
        KenttäKohde objektiKohdalla = Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];

        if (objektiKohdalla instanceof Esine) {
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
            näppäinETekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
            if (Pelaaja.annaEsineidenMäärä() >= 6) näppäinVihjeTeksti.päivitäTeksti("Tavaraluettelo täynnä!" , false, 36, Color.red);
            else näppäinVihjeTeksti.päivitäTeksti("Poimi", false, 36, Color.white);
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
            näppäinCTekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
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
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Paista makkara");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                    else if (Peli.valittuEsine instanceof Hiili || Peli.valittuEsine instanceof Paperi) {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Lisää nuotioon");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                    else {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Kokeile esinettä");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
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
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2*1.25f, 0).scale(scaleXTeksti, 1, 1));
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
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Avaa");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                    else {
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                        näppäinETekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Kokeile esinettä");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                        näppäinCTekstuuri.bind(0);
                        Assets.getModel().render();
                        shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
                        näppäinVihjeTeksti.päivitäTeksti("Katso");
                        näppäinVihjeTeksti.bind(0);
                        Assets.getModel().render();
                    }
                }
                else {
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 2f, 0).scale(0.5f, 0.5f, 1));
                    näppäinCTekstuuri.bind(0);
                    Assets.getModel().render();
                    shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti2, 0).scale(scaleXTeksti, 1, 1));
                    näppäinVihjeTeksti.päivitäTeksti("Katso");
                    näppäinVihjeTeksti.bind(0);
                    Assets.getModel().render();
                }
            }
            else if (objektiKohdalla instanceof Sänky) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Nuku");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof KauppaRuutu || objektiKohdalla instanceof BaariRuutu) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Asioi");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof KauppaHylly) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Lisää koriin");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Pulloautomaatti) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Palauta pullot");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Ämpärikone) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Jonota");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Pelikone) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Pelaa");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else if (objektiKohdalla instanceof Silta) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Katso");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
        }
        else if (objektiKohdalla instanceof NPC_KenttäKohde) {
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
            näppäinETekstuuri.bind(0);
            Assets.getModel().render();
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
            näppäinVihjeTeksti.päivitäTeksti("Juttele");
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
        else if (objektiKohdalla instanceof Triggeri) {
            if (objektiKohdalla instanceof Nappi) {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
                näppäinVihjeTeksti.päivitäTeksti("Paina");
                näppäinVihjeTeksti.bind(0);
                Assets.getModel().render();
            }
            else {
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(-1f, 3f, 0).scale(0.5f, 0.5f, 1));
                näppäinETekstuuri.bind(0);
                Assets.getModel().render();
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
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
                shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
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
            shader.setUniform("projection", transform.getProjection(camera.getProjection()).translate(scaleXTeksti, translateYTeksti1, 0).scale(scaleXTeksti, 1, 1));
            näppäinVihjeTeksti.päivitäTeksti("Katso");
            näppäinVihjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }
}
