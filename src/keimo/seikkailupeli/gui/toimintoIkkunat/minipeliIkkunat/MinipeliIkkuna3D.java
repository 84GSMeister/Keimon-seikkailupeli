package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.kenttä.Maailma3D;
import keimo.seikkailupeli.objektit.Pelaaja;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MinipeliIkkuna3D {
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/minipeli_kehys.png");
    private static float siirräY = 600;

    public static void renderöiKehys(Ikkuna window) {
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
    
    public static void renderöiIkkuna(Ikkuna window, Kamera kamera) {
        if (siirräY > 0) siirräY -= 10;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Maailma3D.render(window);
    }

    public static void avaaToimintoIkkuna() {
        Peli.aktiivinenRuutu = Peli.Ruudut.MINIPELIRUUTU;
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_0;
        Maailma3D.luoMinipeliIkkuna();
    }

    public static void suljeToimintoIkkuna() {
        Peli.aktiivinenRuutu = Peli.Ruudut.PELIRUUTU;
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirräY = 600;
    }
}
