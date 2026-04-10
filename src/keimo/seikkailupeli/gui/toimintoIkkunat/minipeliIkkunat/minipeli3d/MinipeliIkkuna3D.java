package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.minipeli3d;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class MinipeliIkkuna3D {
    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("minipeli_kehys");
    private static float siirtymä = 0;

    public static void renderöi(Ikkuna window, Kamera kamera, Shader shader) {
        shader.nollaaShaderEfektit();
        shader.bind();
        if (siirtymä < 1) siirtymä += 0.05;
        Matrix4f matKehys = new Matrix4f();
        matKehys.translate(0, -1f/6f, 0);
        matKehys.scale(siirtymä * (2f/3f), siirtymä * (5f/6f), 0);
        shader.asetaSijainti(matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();
        
        glEnable(GL_STENCIL_TEST);

        // Fill stencil buffer with 0's
        glClearStencil(0);
        glClear(GL_STENCIL_BUFFER_BIT);

        // Write 1's into stencil buffer where the hole will be
        glColorMask(false, false, false, false);
        glDepthMask(false);
        glStencilFunc(GL_ALWAYS, 1, ~0);
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
        shader.setUniform("color", new Vector4f(0, 1, 0, 1));
        Matrix4f stencilAlue = new Matrix4f().scale(0.5f);
        shader.asetaSijainti(stencilAlue);
        Assets.getModel().render();

        // Draw rectangle, masking out fragments with 1's in the stencil buffer
        glColorMask(true, true, true, true);
        glDepthMask(true);
        glStencilFunc(GL_EQUAL, 1, ~0);
        glDepthFunc(GL_ALWAYS);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        if (siirtymä >= 1) Maailma3D.renderöi(window, shader);
        glDisable(GL_DEPTH_TEST);

        // Cleanup, if necessary
        glDisable(GL_STENCIL_TEST);
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_3D;
        Maailma3D.luoMinipeliIkkuna();
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirtymä = 0;
    }
}
