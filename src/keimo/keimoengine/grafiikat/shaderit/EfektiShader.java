package keimo.keimoengine.grafiikat.shaderit;

import static org.lwjgl.opengl.GL20.*;

/**
 * Koko ruudun efektejä varten shaderit
 */

public abstract class EfektiShader extends Shader {
    
    public EfektiShader(String shaderNimi) {
        super(shaderNimi);
    }

    public void asetaFade(float fade) {
        int location = glGetUniformLocation(program, "fade");
        glUniform1f(location, fade);
    }
}
