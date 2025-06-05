package keimo.keimoEngine.grafiikat.objekti3d;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.*;

public class TextureCache {

    public static final String DEFAULT_TEXTURE = "tiedostot/kuvat/muut/virhetekstuuri.png";

    private Map<String, Tekstuuri> textureMap;

    public TextureCache() {
        textureMap = new HashMap<>();
        textureMap.put(DEFAULT_TEXTURE, new Tekstuuri(DEFAULT_TEXTURE));
    }

    public void cleanup() {
        textureMap.values().forEach(Tekstuuri::cleanup);
    }

    public Tekstuuri createTexture(String texturePath) {
        return textureMap.computeIfAbsent(texturePath, Tekstuuri::new);
    }

    public Tekstuuri getTexture(String texturePath) {
        Tekstuuri texture = null;
        if (texturePath != null) {
            texture = textureMap.get(texturePath);
        }
        if (texture == null) {
            texture = textureMap.get(DEFAULT_TEXTURE);
        }
        return texture;
    }
}
