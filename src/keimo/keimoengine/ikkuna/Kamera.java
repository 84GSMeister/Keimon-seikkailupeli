package keimo.keimoengine.ikkuna;

import keimo.keimoengine.KeimoEngine;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Kamera {

    /**
     * Kamera-luokka vaatii aika paljon parannuksia ja korjauksia.
     * Lisää perspektiivikameran määrittely tässä ja poista se maailma-luokasta.
     */

    private Vector3f position;
    private Quaternionf rotation;
    private Matrix4f projection;

    public static boolean päivitäZoom = false;
    public static float zoomKerroin = 1f;
    public static float zoomX = 0;
    public static float zoomY = 0;
    public static float zoomZ = 0;

    public Kamera(int width, int height) {
        position = new Vector3f(0, 0, 0);
        rotation = new Quaternionf();
        projection = new Matrix4f();
    }

    public Matrix4f getTransformation() {
        Matrix4f matrix = new Matrix4f();
        matrix.rotate(rotation.conjugate(new Quaternionf()));
        float translateX = 2f / KeimoEngine.window.getWidth();
        float translateY = 2f / KeimoEngine.window.getHeight();
        float translateZ = 0.0025f;
        matrix.translate(position.x * translateX, position.y * translateY, position.z * translateZ);
        return matrix;
    }

    // public Matrix4f getPerspectiveCamera() {

    // }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Matrix4f getUntransformedProjection() {
		return projection;
	}
	
	public Matrix4f getProjection() {
		return projection.translate(position, new Matrix4f());
	}

    public void setOrthographic(float left, float right, float top, float bottom) {
        projection.setOrtho2D(left, right, bottom, top);
    }

    public void setOrthographic(int width, int height) {
        projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
    }

    public void setPerspective(float fov, float aspectRatio, float zNear, float zFar) {
        projection.setPerspective(fov, aspectRatio, zNear, zFar);
    }

    public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        projection.setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public Matrix4f getPerspectiveView(Ikkuna window, float zoom) {
        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective((float)Math.toRadians(90), window.getHeight() > 0 ? window.getWidth()/window.getHeight() : 1, 0.001f, 1000);
        perspectiveMatrix.scale(2048f/window.getWidth(), 2048f/window.getHeight(), 1);
        Matrix4f lookAtMatrix = new Matrix4f().setLookAt(0, 0, 32 * zoom, 0, 0, 0, 0, 1, 0);
        Matrix4f cameraMatrix = perspectiveMatrix.mul(lookAtMatrix);
        return cameraMatrix;
    }

    public void setProjection(Matrix4f projection) {
        this.projection = projection;
    }

    public void resetZoom(Ikkuna window) {
        zoomX = window.getWidth()/2;
        zoomY = window.getHeight()/2;
        zoomZ = 0;
    }
}
