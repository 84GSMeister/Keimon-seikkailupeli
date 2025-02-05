package keimo.keimoEngine.ikkuna;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import keimo.keimoEngine.KeimoEngine;

public class Kamera {
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

    public void setProjection(Matrix4f projection) {
        this.projection = projection;
    }

    public void resetZoom(Window window) {
        zoomX = window.getWidth()/2;
        zoomY = window.getHeight()/2;
        zoomZ = 0;
    }
}
