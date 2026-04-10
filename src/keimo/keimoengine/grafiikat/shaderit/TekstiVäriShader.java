package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class TekstiVäriShader extends Shader {

    private float aika = 0;
    private int väriShaderTyyppi = 0;

    public TekstiVäriShader(int tyyppi) {
        super("teksti_värishader");
        this.väriShaderTyyppi = tyyppi;
        if (väriShaderTyyppi == 1) aika += Math.PI/2d;
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
