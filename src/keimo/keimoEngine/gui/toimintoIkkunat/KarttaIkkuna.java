package keimo.keimoEngine.gui.toimintoIkkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Peli.SyötteenTila;
import keimo.Peli.ToimintoIkkunanTyyppi;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class KarttaIkkuna {
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri pohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/kartta/kartta_pohja_kädet.png");
    private static Tekstuuri karttaTekstuuri = new Tekstuuri("tiedostot/kuvat/kartta/kartta.png");
    private static float offsetY = 600;
    private static float scaleX = 30;
    
    public static void renderöiIkkuna(Window window) {
        float scaleY = window.getHeight()/2;
        if (offsetY > 0) offsetY -= 10;
        if (offsetY <= 0 && scaleX < window.getWidth()/4) scaleX += window.getWidth()/100f;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, -offsetY, 0);
        matKehys.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matKehys);
        if (offsetY > 0) {
            pohjaTekstuuri.bind(0);
        }
        else {
            karttaTekstuuri.bind(0);
        }
        Assets.getModel().render();
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.KARTTA;
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        offsetY = 600;
        scaleX = 30;
    }
}
