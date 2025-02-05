package keimo.keimoEngine.grafiikat.objekti3d;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform3D {
    
    private Vector3f position;
    private Quaternionf rotation;
    private Vector3f scale;

    public Transform3D() {
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = new Vector3f(1f);
    }

    public Matrix4f getTransformation() {
        Matrix4f matrix = new Matrix4f();
        matrix.translate(new Vector3f(position.x/8f, position.y/-8f, position.z/8f));
        matrix.rotate(rotation);
        matrix.scale(scale);
        return matrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
