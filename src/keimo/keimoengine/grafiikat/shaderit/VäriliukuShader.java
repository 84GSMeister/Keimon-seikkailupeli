package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class VäriliukuShader extends EfektiShader {

    public VäriliukuShader() {
        super("väriliuku");
        super.loopNopeus = 0.02f;
    }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}
