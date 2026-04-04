package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class KarttaIkkuna {
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("isokartta_tyhjä");
    private static Renderöitävä karttaTekstuuri = Assets.annaTekstuuri("isokartta");
    private static float offsetY = 600;
    private static float scaleX = 30;
    
    public static void renderöiIkkuna(Shader peliShader, Ikkuna window) {
        float scaleY = window.getHeight()/2;
        if (offsetY > 0) offsetY -= 10;
        if (offsetY <= 0 && scaleX < window.getWidth()/4) scaleX += window.getWidth()/100f;
        peliShader.bind();
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, -offsetY, 0);
        matKehys.scale(scaleX, scaleY, 0);
        peliShader.asetaSijainti(matKehys);
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
