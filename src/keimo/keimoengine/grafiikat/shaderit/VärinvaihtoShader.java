package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class VärinvaihtoShader extends Shader {

    private float aika = 0;
    private int väriShaderTyyppi = 0;

    public VärinvaihtoShader(int tyyppi) {
        super("värinvaihto");
        this.väriShaderTyyppi = tyyppi;
    }

    @Override
    public void loop() {
        int location = glGetUniformLocation(program, "time");
        glUniform1f(location, aika);
        if (väriShaderTyyppi == 0) aika += 0.02f;
        else if (väriShaderTyyppi == 1) aika += 0.25f;
    }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}
