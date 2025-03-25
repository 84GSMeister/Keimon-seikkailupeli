package keimo.keimoEngine.grafiikat.objekti3d;

import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class Model3D {
    private final String id;
    private List<Material> materialList;
    private Transform3D transform = new Transform3D();

    public Model3D(String id, List<Material> materialList) {
        this.id = id;
        this.materialList = materialList;
    }

    public void cleanup() {
        materialList.forEach(Material::cleanup);
    }

    public String getId() {
        return id;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public Transform3D getTransform() {
        return transform;
    }

    public void draw() {
        for (Material material : getMaterialList()) {
            Tekstuuri texture = Assets.textureCache.getTexture(material.getTexturePath());
            glActiveTexture(GL_TEXTURE0);
            texture.bind(0);
            for (Mesh3D mesh : material.getMeshList()) {
                glBindVertexArray(mesh.getVaoId());
                glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
                glBindVertexArray(0);
            }
        }
    }
}
