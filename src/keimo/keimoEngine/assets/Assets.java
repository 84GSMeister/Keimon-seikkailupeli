package keimo.keimoEngine.assets;

import keimo.Utility.Käännettävä.Suunta;
import keimo.Utility.ModelLoader;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.grafiikat.objekti3d.Model3D;
import keimo.keimoEngine.grafiikat.objekti3d.TextureCache;

import java.util.ArrayList;
import java.util.HashMap;

public class Assets {

    private static Model model0, model0X, model0Y, model0XY;
    private static Model model90, model90X, model90Y, model90XY;
    private static Model model180, model180X, model180Y, model180XY;
    private static Model model270, model270X, model270Y, model270XY;
    public static TextureCache textureCache = new TextureCache();
    public static ArrayList<Model> lattiaTilet = new ArrayList<>();

    public static Model getModel() {return model0;}
    private static HashMap<String, Model3D> models3d = new HashMap<>();

    public static Model3D getModel3D(String objNimi) {
        return models3d.get(objNimi);
    }

    public static void lisää3DMalli(String nimi, float skaala, boolean käännäYZ) {
        models3d.put(nimi, ModelLoader.loadModel(nimi, "tiedostot/3d-objektit/" + nimi + "/" + nimi + ".obj", skaala, käännäYZ, textureCache));
    }

    public static Model getModel(int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        switch (kääntöAsteet) {
            default -> {
                if (xPeilaus && yPeilaus) return model0XY;
                else if (xPeilaus) return model0X;
                else if (yPeilaus) return model0Y;
                else return model0;
            }
            case 90 -> {
                if (xPeilaus && yPeilaus) return model90XY;
                else if (xPeilaus) return model90X;
                else if (yPeilaus) return model90Y;
                else return model90;
            }
            case 180 -> {
                if (xPeilaus && yPeilaus) return model180XY;
                else if (xPeilaus) return model180X;
                else if (yPeilaus) return model180Y;
                else return model180;
            }
            case 270 -> {
                if (xPeilaus && yPeilaus) return model270XY;
                else if (xPeilaus) return model270X;
                else if (yPeilaus) return model270Y;
                else return model270;
            }
        }
    }
    public static Model getModel(Suunta suunta) {
        switch (suunta) {
            case VASEN: return model0;
            case OIKEA: return model0X;
            case ALAS: return model270;
            case YLÖS: return model90;
            default: return model180;
        }
    }
    
    public static void createModels() {
        float[] vertices = new float[]{
            -1f, 1f, 0, // TOP LEFT 0
            1f, 1f, 0,  // TOP RIGHT 1
            1f, -1f, 0, // BOTTOM RIGHT 2
            -1f, -1f, 0,// BOTTOM LEFT 3
        };
        int[] indices = new int[]{0, 1, 2, 2, 3, 0};

        float[] texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
        model0 = new Model(vertices, texture, indices);
        texture = new float[]{1, 0, 0, 0, 0, 1, 1, 1,};
        model0X = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 1, 1, 1, 0, 0, 0,};
        model0Y = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 0, 1, 0, 0, 1, 0,};
        model0XY = new Model(vertices, texture, indices);

        texture = new float[]{0, 1, 0, 0, 1, 0, 1, 1,};
        model90 = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 0, 1, 1, 1, 1, 0,};
        model90X = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 1, 0, 0, 0, 0, 1,};
        model90Y = new Model(vertices, texture, indices);
        texture = new float[]{1, 0, 1, 1, 0, 1, 0, 0,};
        model90XY = new Model(vertices, texture, indices);

        texture = new float[]{1, 1, 0, 1, 0, 0, 1, 0,};
        model180 = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 1, 1, 1, 0, 0, 0,};
        model180X = new Model(vertices, texture, indices);
        texture = new float[]{1, 0, 0, 0, 0, 1, 1, 1,};
        model180Y = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
        model180XY = new Model(vertices, texture, indices);

        texture = new float[]{1, 0, 1, 1, 0, 1, 0, 0,};
        model270 = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 1, 0, 0, 0, 0, 1,};
        model270X = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 0, 1, 1, 1, 1, 0,};
        model270Y = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 0, 0, 1, 0, 1, 1,};
        model270XY = new Model(vertices, texture, indices);

        luo3DLattia();
    }

    private static void luo3DLattia() {
        for (int x = -20; x < 20; x++) {
            for (int z = -20; z < 20; z++) {
                float[] vertices = new float[]{
                    x, 0, z, // TOP LEFT 0
                    x+1, 0, z,  // TOP RIGHT 1
                    x+1, 0, z+1, // BOTTOM RIGHT 2
                    x, 0, z+1, // BOTTOM LEFT 3
                };
                int[] indices = new int[]{0, 1, 2, 2, 3, 0};
        
                float[] texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
                lattiaTilet.add(new Model(vertices, texture, indices));
            }
        }
    }

    public static void luo3DMallit() {
        lisää3DMalli("cube", 1f, false);
        lisää3DMalli("Tynnyri", 1.5f, true);
        lisää3DMalli("asunto_yokyla", 0.125f, false);
        lisää3DMalli("Sieni", 4f, true);
        lisää3DMalli("yokyla_40-54", 0.005f, true);
        lisää3DMalli("Wooden_Crate_2", 1f, true);
        lisää3DMalli("Testikuutio", 1f, true);
        lisää3DMalli("Coin", 3f, true);
        lisää3DMalli("Kolikko", 3f, true);
        lisää3DMalli("KeimoTeksti", 0.0125f, true);
    }
}
