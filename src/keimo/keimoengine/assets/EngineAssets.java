package keimo.keimoengine.assets;

import keimo.keimoengine.grafiikat.objekti2d.Model;

public class EngineAssets {

    private static Model model0, model0X, model0Y, model0XY;
    private static Model model90, model90X, model90Y, model90XY;
    private static Model model180, model180X, model180Y, model180XY;
    private static Model model270, model270X, model270Y, model270XY;
    public static Model getModel() {return model0;}
    
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
        texture = new float[]{1, 1, 1, 0, 0, 0, 0, 1,};
        model90X = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 0, 1, 1, 1, 1, 0,};
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
        texture = new float[]{0, 0, 0, 1, 1, 1, 1, 0,};
        model270X = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 1, 0, 0, 0, 0, 1,};
        model270Y = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 0, 0, 1, 0, 1, 1,};
        model270XY = new Model(vertices, texture, indices);
    }
}
