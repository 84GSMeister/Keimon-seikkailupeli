package keimo.seikkailupeli.kenttä;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.seikkailupeli.assets.Assets;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Tausta {

    static HashMap<String, Tekstuuri> taustaTekstuurit = new HashMap<>();
    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/taustat/tausta_virhe.png");
	private Shader taustaShader = new Shader("shader");
	public static boolean häivytäTausta = false;

	public void render(String tausta, float x, float y, float z, Matrix4f cameraMatrix, float fade) {
		taustaShader.bind();
		String taustanNimi = "";
		if (tausta != null && tausta != "") {
			taustanNimi = tausta.substring(0, tausta.length()-4);
		}
		if (taustaTekstuurit.containsKey(taustanNimi)) taustaTekstuurit.get(taustanNimi).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f taustanSijainti = new Matrix4f();
		taustanSijainti.translate(x, y, z);
        Matrix4f resultMatrix = cameraMatrix.mul(taustanSijainti);
		
		taustaShader.setUniform("sampler", 0);
		taustaShader.setUniform("projection", resultMatrix);
		taustaShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel();
		model.render();
	}
}
