package keimo.keimoEngine.grafiikat.objekti3d;

import java.util.*;

import org.joml.Vector4f;

public class Material {

    public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    private Vector4f diffuseColor;
    private List<Mesh3D> meshList;
    private String texturePath;

    public Material() {
        diffuseColor = DEFAULT_COLOR;
        meshList = new ArrayList<>();
    }

    public void cleanup() {
        meshList.forEach(Mesh3D::cleanup);
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public List<Mesh3D> getMeshList() {
        return meshList;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
}
