package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class TekstiVäriShader extends Shader {

    //private float aika = 0;
    private int väriShaderTyyppi = 0;
    //private float loopNopeus = 0.02f;

    public TekstiVäriShader(int tyyppi) {
        super("teksti_värishader");
        super.loopNopeus = 0.02f;
        this.väriShaderTyyppi = tyyppi;
        if (väriShaderTyyppi == 1) aika += Math.PI/2d;
    }

    @Override
    public void loop() {
        int location = glGetUniformLocation(program, "time");
        glUniform1f(location, aika);
        aika += loopNopeus;
    }

    @Override
    public void loop(float nopeus) {
        int location = glGetUniformLocation(program, "time");
        glUniform1f(location, aika);
        aika += nopeus;
    }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}
