package keimo.keimoEngine.gui.toimintoIkkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Peli.SyötteenTila;
import keimo.Peli.ToimintoIkkunanTyyppi;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class OhjeIkkuna {
    public static boolean näytäOhjeet = true;
    private static Shader peliShader = new Shader("shader");
    private static Tekstuuri pohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/kartta/kartta_pohja_kädet.png");
    private static Teksti otsikkoTeksti = new Teksti("Näppäimet", Color.black, 200, 20);
    private static Teksti ohjeTeksti = new Teksti("ohje", Color.black, 340, 20);
    private static String[] ohjeTekstit = {
        "Nuolet/WASD: Liiku",
        "E: Poimi/Vuorovaikuta",
        "1-6: Vaihda tavarapaikkaa",
        "Space: Käytä esinettä",
        "Q: Pudota",
        "Z: Yhdistä",
        "X: Katso esinettä",
    };
    
    public static void renderöiIkkuna(Window window) {
        float scaleXPohja = window.getWidth()/4;
        float scaleXTekstit = window.getWidth()/5;
        float scaleYPohja = window.getHeight()/2;
        float scaleYTekstit = window.getHeight()/24;
        float keskitysY = window.getHeight()/8;
        float offsetY = window.getHeight()/20;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, 0, 0);
        matKehys.scale(scaleXPohja, scaleYPohja, 0);
        peliShader.setUniform("projection", matKehys);
        pohjaTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, keskitysY + offsetY, 0);
        matOtsikko.scale(scaleXTekstit, scaleYTekstit, 0);
        peliShader.setUniform("projection", matOtsikko);
        otsikkoTeksti.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < ohjeTekstit.length; i++) {
            Matrix4f matOhjeTeksti = new Matrix4f();
            window.getView().scale(1, matOhjeTeksti);
            matOhjeTeksti.translate(0, keskitysY -offsetY * i, 0);
            matOhjeTeksti.scale(scaleXTekstit, scaleYTekstit, 0);
            peliShader.setUniform("projection", matOhjeTeksti);
            ohjeTeksti.päivitäTeksti(ohjeTekstit[i]);
            ohjeTeksti.bind(0);
            Assets.getModel().render();
        }
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.OHJEET;
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        näytäOhjeet = false;
    }
}
