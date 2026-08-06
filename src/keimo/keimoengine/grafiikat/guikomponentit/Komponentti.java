package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;

import org.joml.Matrix4f;

public abstract class Komponentti {
    
    protected float scaleX, scaleY;
    protected float offsetX, offsetY;
    protected float rotX, rotY, rotZ;
    protected Renderöitävä tekstuuri;
    protected Matrix4f sijaintiMatriisi = new Matrix4f();
    protected Matrix4f rotaatioMatriisi = new Matrix4f();

    public Komponentti(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Komponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
    }

    public Renderöitävä annaSisältö() {
        return tekstuuri;
    }

    public void päivitäSisältö(Renderöitävä tekstuuri) {
        this.tekstuuri = tekstuuri;
    }

    public void muutaKokoa(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void muutaOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void muutaOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
