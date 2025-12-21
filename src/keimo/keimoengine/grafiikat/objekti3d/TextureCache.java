package keimo.keimoengine.grafiikat.objekti3d;

import keimo.keimoengine.grafiikat.Tekstuuri;

import java.util.*;

public class TextureCache {

    protected static final String DEFAULT_TEXTURE = "tiedostot/kuvat/muut/virhetekstuuri.png";
    protected static TextureCache textureCache = new TextureCache();
    private Map<String, Tekstuuri> textureMap;

    public TextureCache() {
        textureMap = new HashMap<>();
        textureMap.put(DEFAULT_TEXTURE, new Tekstuuri(DEFAULT_TEXTURE));
    }

    public static TextureCache getTextureCache() {
        return textureCache;
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
