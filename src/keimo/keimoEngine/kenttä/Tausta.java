package keimo.keimoEngine.kenttä;

import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Tausta {

    static HashMap<String, Tekstuuri> taustaTekstuurit = new HashMap<>();
    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
	private Shader taustaShader = new Shader("shader");
	public static boolean häivytäTausta = false;
    
    public void render(String tausta, Kamera cam, Window window) {
		taustaShader.bind();
		String taustanNimi = tausta.substring(0, tausta.length()-4);
		if (taustaTekstuurit.containsKey(taustanNimi)) taustaTekstuurit.get(taustanNimi).bind(0);
		else virheTekstuuri.bind(0);

        Matrix4f matTausta = new Matrix4f();
		taustaShader.setUniform("sampler", 0);
		taustaShader.setUniform("projection", matTausta);
		if (häivytäTausta) taustaShader.setUniform("subcolor", new Vector4f(Maailma.fade, Maailma.fade, Maailma.fade, 0f));
		else taustaShader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
		
		Model model = Assets.getModel();
		model.render();
	}
}
