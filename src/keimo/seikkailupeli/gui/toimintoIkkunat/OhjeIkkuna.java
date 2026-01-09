package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
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
    private static Shader peliShader = new Shader("shader");
    private static Tekstuuri pohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/ohje/kartta_pohja_kädet.png");
    private static Teksti otsikkoTeksti = new Teksti("Näppäimet", Color.black, 600, 48);
    private static Teksti ohjeTeksti = new Teksti("ohje", Color.black, 1000, 48);

    private static Tekstuuri[] näppäinTekstuurit = {
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/wasd_näppäimet.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_e.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_nuoli.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_space.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_q.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_z.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_x.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_c.png"),
    };

    private static Tekstuuri[] ohjainTekstuuritXbox = {
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_analog.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_a.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nuoli.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_b.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_rt.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_select.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_r.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_x.png"),
    };

    private static Tekstuuri[] ohjainTekstuuritNintendo = {
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_analog.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_b.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nuoli.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_a.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_rz.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_select.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_r.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_y.png"),
    };

    private static Tekstuuri[] ohjainTekstuuritPlaystation = {
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_analog.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_x.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nuoli.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_ympyrä.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_r2.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_select.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_r.png"),
        new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_neliö.png"),
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
    
    public static void renderöiIkkuna(Ikkuna window) {
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
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, siirtymäY, 0);
        matKehys.scale(scaleXPohja, scaleYPohja, 0);
        peliShader.setUniform("projection", matKehys);
        pohjaTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, keskitysY + offsetY + siirtymäY, 0);
        matOtsikko.scale(scaleXTekstit, scaleYTekstit, 0);
        peliShader.setUniform("projection", matOtsikko);
        otsikkoTeksti.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < näppäinTekstuurit.length; i++) {
            Matrix4f matNäppäinTekstuuri = new Matrix4f();
            window.getView().scale(1, matNäppäinTekstuuri);
            matNäppäinTekstuuri.translate(-offsetX -scaleXKuvakkeet, keskitysY -offsetY * i + siirtymäY, 0);
            matNäppäinTekstuuri.scale(scaleXKuvakkeet, scaleYKuvakkeet, 0);
            peliShader.setUniform("projection", matNäppäinTekstuuri);
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
            peliShader.setUniform("projection", matNäppäinTeksti);
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
