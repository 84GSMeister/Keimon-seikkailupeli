package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

public class TestiShader extends Shader {

    //private float aika = 0;
    //private float loopNopeus = 0.02f;

    public TestiShader() {
        super("testi2");
        super.loopNopeus = 0.02f;
    }

    // @Override
    // public void loop() {
    //     int location = glGetUniformLocation(program, "time");
    //     glUniform1f(location, aika);
    //     aika += loopNopeus;
    // }

    // @Override
    // public void loop(float nopeus) {
    //     int location = glGetUniformLocation(program, "time");
    //     glUniform1f(location, aika);
    //     aika += nopeus;
    // }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}