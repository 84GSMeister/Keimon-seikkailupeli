package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class VärinvaihtoShaderKuu extends Shader {

    public VärinvaihtoShaderKuu() {
        super("kuu");
        super.loopNopeus = 0.1f;
    }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}
