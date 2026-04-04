package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class VärinvaihtoShaderKuu extends Shader {

    private float aika = 0;

    public VärinvaihtoShaderKuu() {
        super("kuu");
    }

    @Override
    public void loop() {
        int location = glGetUniformLocation(program, "time");
        glUniform1f(location, aika);
        aika += 0.02f;
    }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}
