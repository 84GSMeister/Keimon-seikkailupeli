package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
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
    private static String[] ohjeTekstit = {
        "Nuolet/WASD: Liiku",
        "E: Poimi/Vuorovaikuta",
        "1-6: Vaihda tavarapaikkaa",
        "Space: Käytä esinettä",
        "Q: Pudota",
        "Z: Yhdistä",
        "X: Katso esinettä",
        "C: Katso kohdetta",
    };
    private static float siirtymäY = -600;
    
    public static void renderöiIkkuna(Window window) {
        if (siirtymäY < 0) siirtymäY += 20;
        float scaleXPohja = window.getWidth()/4;
        float scaleXTekstit = window.getWidth()/5;
        float scaleYPohja = window.getHeight()/2;
        float scaleYTekstit = window.getHeight()/28;
        float keskitysY = window.getHeight()/7.5f;
        float offsetY = window.getHeight()/24;
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

        for (int i = 0; i < ohjeTekstit.length; i++) {
            Matrix4f matOhjeTeksti = new Matrix4f();
            window.getView().scale(1, matOhjeTeksti);
            matOhjeTeksti.translate(0, keskitysY -offsetY * i + siirtymäY, 0);
            matOhjeTeksti.scale(scaleXTekstit, scaleYTekstit, 0);
            peliShader.setUniform("projection", matOhjeTeksti);
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
