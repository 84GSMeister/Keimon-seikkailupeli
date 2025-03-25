package keimo.keimoEngine.gui.toimintoIkkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Peli.SyötteenTila;
import keimo.Peli.ToimintoIkkunanTyyppi;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.kenttä.Maailma3D;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MinipeliIkkuna {
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/toimintoikkunat/toimintoikkuna_kehys_valikko.png");
    private static float scaleX = 30;
    private static float siirräY = 600;

    public static void renderöiKehys(Window window) {
        float scaleX = window.getWidth()/4;
        float scaleY = window.getHeight()/4;
        if (siirräY > 0) siirräY -= 20;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, siirräY, 0);
        matKehys.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();
    }
    
    public static void renderöiIkkuna(Window window, Kamera kamera) {
        float scaleY = window.getHeight()/2;
        if (siirräY > 0) siirräY -= 10;
        if (siirräY <= 0 && scaleX < window.getWidth()/4) scaleX += window.getWidth()/100f;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        // Matrix4f matKehys = new Matrix4f();
        // window.getView().scale(1, matKehys);
        // matKehys.translate(0, -offsetY, 0);
        // matKehys.scale(scaleX, scaleY, 0);
        // peliShader.setUniform("projection", matKehys);
        // if (offsetY > 0) {
        //     pohjaTekstuuri.bind(0);
        // }
        // else {
        //     karttaTekstuuri.bind(0);
        // }
        // Assets.getModel().render();

        Maailma3D.render(window);
    }

    public static void avaaToimintoIkkuna() {
        Peli.aktiivinenRuutu = Peli.Ruudut.MINIPELIRUUTU;
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI;
    }

    public static void suljeToimintoIkkuna() {
        Peli.aktiivinenRuutu = Peli.Ruudut.PELIRUUTU;
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        scaleX = 30;
        siirräY = 600;
    }
}
