package keimo.seikkailupeli.assets;

import keimo.keimoengine.Kello;
import keimo.keimoengine.grafiikat.Animaatio;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;

/**
 * KuvaObjekti-luokka tarjoaa entryn renderöitävien objektien usean instanssin renderöintiin.
 * Sen kautta voidaan antaa useita pointereita samaan Animaatio-objektiin, mikä mahdollistaa
 * saman animaation (kuvalista) toistamisen eri kohdista eri objekteissa.
 */

public class KuvaObjekti {
    
    private Renderöitävä renderöitävä;
    private int pointer;
    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps = 1.0/30;
    private int toistot;
    private int toistettu;

    public KuvaObjekti(String kuvanTunniste) {
        this.renderöitävä = Assets.annaTekstuuri(kuvanTunniste);
        if (renderöitävä instanceof Animaatio) asetaAnimaationMetatiedot((Animaatio)renderöitävä);
    }

    public KuvaObjekti(Renderöitävä renderöitävä) {
        this.renderöitävä = renderöitävä;
        if (renderöitävä instanceof Animaatio) asetaAnimaationMetatiedot((Animaatio)renderöitävä);
    }

    public Renderöitävä annaRenderöitävä() {
        return renderöitävä;
    }

    public void asetaRenderöitävä(Renderöitävä uusiRenderöitävä) {
        this.renderöitävä = uusiRenderöitävä;
        if (renderöitävä instanceof Animaatio) asetaAnimaationMetatiedot((Animaatio)renderöitävä);
    }

    private void asetaAnimaationMetatiedot(Animaatio anim) {
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.toistot = anim.annaToistot();
        this.toistettu = 0;
        this.lastTime = Kello.annaAika();
        this.fps = anim.annaFps();
    }

    public void bind(int sampler) {
        if (renderöitävä instanceof Animaatio) {
            Animaatio anim = (Animaatio)renderöitävä;
            this.currentTime = Kello.annaAika();
            this.elapsedTime += currentTime - lastTime;

            if (elapsedTime >= fps) {
                if (elapsedTime >= 0.2) elapsedTime = 0.2;
                elapsedTime -= fps;
                pointer++;
            }
            if (pointer >= anim.annaFramet().size()) {
                if (toistettu < toistot-1) {
                    pointer = 0;
                    toistettu++;
                }
                else if (toistot == 0) {
                    pointer = 0;
                }
                else {
                    pointer = anim.annaFramet().size()-1;
                }
            }

            this.lastTime = currentTime;
            if (pointer < anim.annaFramet().size()) anim.annaFramet().get(pointer).bind(sampler);
        }
        else if (renderöitävä instanceof Tekstuuri || renderöitävä instanceof Teksti) {
            renderöitävä.bind(sampler);
        }
    }
}