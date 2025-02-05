package keimo.keimoEngine.kenttä;

public class TestiObjekti {
    public static float[] vertices = new float[] {
        0f -0.5f, 0f +0.5f, 0, //0
        0f +0.5f, 0f +0.5f, 0, //1
        0f +0.5f, 0f -0.5f, 0, //2
        0f -0.5f, 0f -0.5f, 0 //3
    };
    public static float[] texture = new float[] {
        0,0,
        1,0,
        1,1,
        0,1
    };
    public static int[] indices = new int[] {
        0,1,2,
        2,3,0
    };
}
