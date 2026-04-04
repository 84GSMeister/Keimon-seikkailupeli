package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class OhjeIkkuna {
    public static boolean näytäOhjeet = true;
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("isokartta_tyhjä");
    private static Teksti otsikkoTeksti = new Teksti("Näppäimet", Color.black, 600, 48);
    private static Teksti ohjeTeksti = new Teksti("ohje", Color.black, 1000, 48);

    private static Renderöitävä[] näppäinTekstuurit = {
        Assets.annaTekstuuri("näppäin_wasd"),
        Assets.annaTekstuuri("näppäin_e"),
        Assets.annaTekstuuri("näppäin_nuoli"),
        Assets.annaTekstuuri("näppäin_space"),
        Assets.annaTekstuuri("näppäin_q"),
        Assets.annaTekstuuri("näppäin_z"),
        Assets.annaTekstuuri("näppäin_x"),
        Assets.annaTekstuuri("näppäin_c"),
    };

    private static Renderöitävä[] ohjainTekstuuritXbox = {
        Assets.annaTekstuuri("ohjain_analog"),
        Assets.annaTekstuuri("ohjain_xbox_a"),
        Assets.annaTekstuuri("ohjain_nuoli"),
        Assets.annaTekstuuri("ohjain_xbox_b"),
        Assets.annaTekstuuri("ohjain_xbox_rt"),
        Assets.annaTekstuuri("ohjain_select"),
        Assets.annaTekstuuri("ohjain_r"),
        Assets.annaTekstuuri("ohjain_xbox_x"),
    };

    private static Renderöitävä[] ohjainTekstuuritNintendo = {
        Assets.annaTekstuuri("ohjain_analog"),
        Assets.annaTekstuuri("ohjain_nintendo_b"),
        Assets.annaTekstuuri("ohjain_nuoli"),
        Assets.annaTekstuuri("ohjain_nintendo_a"),
        Assets.annaTekstuuri("ohjain_nintendo_rz"),
        Assets.annaTekstuuri("ohjain_select"),
        Assets.annaTekstuuri("ohjain_r"),
        Assets.annaTekstuuri("ohjain_nintendo_y"),
    };

    private static Renderöitävä[] ohjainTekstuuritPlaystation = {
        Assets.annaTekstuuri("ohjain_analog"),
        Assets.annaTekstuuri("ohjain_playstation_x"),
        Assets.annaTekstuuri("ohjain_nuoli"),
        Assets.annaTekstuuri("ohjain_playstation_ympyrä"),
        Assets.annaTekstuuri("ohjain_playstation_r2"),
        Assets.annaTekstuuri("ohjain_select"),
        Assets.annaTekstuuri("ohjain_r"),
        Assets.annaTekstuuri("ohjain_playstation_neliö"),
    };

    private static String[] ohjeTekstit = {
        "Liiku",
        "Poimi/Vuorovaikuta",
        "Vaihda tavarapaikkaa",
        "Käytä esinettä",
        "Pudota",
        "Yhdistä",
        "Katso esinettä",
        "Katso kohdetta",
    };
    private static float siirtymäY = -600;
    
    public static void renderöiIkkuna(Shader peliShader, Ikkuna window) {
        if (siirtymäY < 0) siirtymäY += 20;
        float scaleXPohja = window.getWidth()/4f;
        float scaleYPohja = window.getHeight()/2f;
        float scaleXKuvakkeet = window.getWidth()/64f;
        float scaleYKuvakkeet = window.getHeight()/64f;
        float scaleXTekstit = window.getWidth()/5f;
        float scaleYTekstit = window.getHeight()/28f;
        float keskitysY = window.getHeight()/7.5f;
        float offsetY = window.getHeight()/24f;
        float offsetX = window.getWidth()/5.75f;

        peliShader.bind();
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, siirtymäY, 0);
        matKehys.scale(scaleXPohja, scaleYPohja, 0);
        peliShader.asetaSijainti(matKehys);
        pohjaTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, keskitysY + offsetY + siirtymäY, 0);
        matOtsikko.scale(scaleXTekstit, scaleYTekstit, 0);
        peliShader.asetaSijainti(matOtsikko);
        otsikkoTeksti.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < näppäinTekstuurit.length; i++) {
            Matrix4f matNäppäinTekstuuri = new Matrix4f();
            window.getView().scale(1, matNäppäinTekstuuri);
            matNäppäinTekstuuri.translate(-offsetX -scaleXKuvakkeet, keskitysY -offsetY * i + siirtymäY, 0);
            matNäppäinTekstuuri.scale(scaleXKuvakkeet, scaleYKuvakkeet, 0);
            peliShader.asetaSijainti(matNäppäinTekstuuri);
            if (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ) näppäinTekstuurit[i].bind(0);
            else if (Peli.viimeisinSyöteLaite == SyöteLaitteet.PELIOHJAIN) {
                switch (PelinAsetukset.ohjainKuvakkeet) {
                    case XBOX: ohjainTekstuuritXbox[i].bind(0); break;
                    case NINTENDO: ohjainTekstuuritNintendo[i].bind(0); break;
                    case PLAYSTATION: ohjainTekstuuritPlaystation[i].bind(0); break;
                }
                
            }
            Assets.getModel().render();
        }

        for (int i = 0; i < ohjeTekstit.length; i++) {
            Matrix4f matNäppäinTeksti = new Matrix4f();
            window.getView().scale(1, matNäppäinTeksti);
            matNäppäinTeksti.translate(scaleXKuvakkeet*3f, keskitysY -offsetY * i + siirtymäY, 0);
            matNäppäinTeksti.scale(scaleXTekstit, scaleYTekstit, 0);
            peliShader.asetaSijainti(matNäppäinTeksti);
            ohjeTeksti.päivitäTeksti(ohjeTekstit[i]);
            ohjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }

    public static void avaaToimintoIkkuna() {
        siirtymäY = -600;
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.OHJEET;
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Peli.pausetaPeli(false);
        Pelaaja.käyttöViive = 50;
        näytäOhjeet = false;
    }
}
