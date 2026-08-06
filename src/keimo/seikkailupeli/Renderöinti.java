package keimo.seikkailupeli;

import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ruudut.VirheRuutu;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.gui.toimintoIkkunat.OhjeIkkuna;
import keimo.seikkailupeli.io.SyöteYhdistetty;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.ruudut.KehittäjäRuutu;
import keimo.seikkailupeli.ruudut.LoppuRuutu;
import keimo.seikkailupeli.ruudut.PeliRuutu;
import keimo.seikkailupeli.ruudut.PeliRuutuLataus;
import keimo.seikkailupeli.ruudut.TarinaRuutu;
import keimo.seikkailupeli.ruudut.ValikkoRuutu;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutuVarmistus;
import keimo.seikkailupeli.ruudut.editori.gui.EditorinValikko;
import keimo.seikkailupeli.äänet.Musat;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Renderöinti {

    static Shader valikkoShader;
    static Shader peliShader;
	public static Kamera kamera;
	public static Ikkuna ikkuna;
	private static String seuraavaRuutu = "";
	private static String seuraavaTarina = "";
	private static PelinLopetukset pelinLoppuSyy = null;

	public static Object glKontekstiLukko = new Object();
	public static boolean glKontekstiToisellaSäikeellä = false;

	public static void alusta(Ikkuna ikkuna1) {
		luoShaderit();
		ikkuna = ikkuna1;
		kamera = new Kamera(ikkuna.getWidth(), ikkuna.getHeight());
		kamera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
		kamera.setOrthographic(ikkuna.getWidth(), ikkuna.getHeight());
		kamera.setRotation(new Quaternionf(new AxisAngle4f((float)Math.toRadians(30), new Vector3f(1, 0, 0))));
		PeliRuutu.alusta(ikkuna, kamera, peliShader);
	}

    private static void luoShaderit() {
        valikkoShader = new Shader("staattinen");
		valikkoShader.asetaSampler(0);
		valikkoShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
		valikkoShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
		valikkoShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));

        peliShader = new Shader("shader");
		peliShader.asetaSampler(0);
		peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
		peliShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
		peliShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));
    }

    public static void renderöiRuutu(Ikkuna ikkuna) {
		switch (Peli.aktiivinenRuutu) {
			case PELIRUUTU -> {
				if (Peli.huone != null) {
					PeliRuutu.renderöi(peliShader, kamera, ikkuna);
				}
				else PeliRuutuLataus.renderöi(valikkoShader, ikkuna);
			}
			case TARINARUUTU -> {
				TarinaRuutu.renderöi(valikkoShader, ikkuna);
			}
			case VALIKKORUUTU -> {
				ValikkoRuutu.renderöi(valikkoShader, ikkuna);
			}
			case ASETUSRUUTU -> {
				AsetusRuutu.renderöi(valikkoShader, ikkuna);
			}
			case KEHITTÄJÄRUUTU -> {
				KehittäjäRuutu.renderöi(ikkuna);
			}
			case LOPPURUUTU -> {
				LoppuRuutu.renderöi(valikkoShader, ikkuna);
			}
			case VIRHERUUTU -> {
				VirheRuutu.renderöi(valikkoShader, ikkuna);
			}
			case EDITORIRUUTU -> {
				EditoriRuutu.renderöi(ikkuna, kamera);
				switch (Peli.syötteenTila) { 
					case TOIMINTO -> {
						EditorinValikko.renderöi(valikkoShader, ikkuna);
					}
					default -> {}
				}
			}
			case EDITORIRUUTU_VARMISTUS -> {
				EditoriRuutuVarmistus.renderöi(valikkoShader, ikkuna);
			}
			case null, default -> {

			}
		}
    }
	public static void siirrySeuraavaanRuutuun(String ruutu) {
		seuraavaRuutu = ruutu;
		switch (seuraavaRuutu) {
			case "peliruutu" -> {
				valitseAktiivinenRuutu(seuraavaRuutu);
				PeliRuutu.häivytäPeliRuutuSisään(peliShader, ikkuna, kamera);
			}
			case "tarinaruutu" -> {
				lataaTarinaRuutu(seuraavaTarina);
			}
			case "loppuruutu" -> {
				lataaLoppuRuutu(pelinLoppuSyy);
			}
			default -> {
				valitseAktiivinenRuutu(seuraavaRuutu);
			}
		}
	}

	public static void siirrySeuraavaanRuutuun(String ruutu, String tarina) {
		seuraavaTarina = tarina;
		siirrySeuraavaanRuutuun(ruutu);
	}

	public static void siirrySeuraavaanRuutuun(String ruutu, PelinLopetukset loppuSyy) {
		pelinLoppuSyy = loppuSyy;
		siirrySeuraavaanRuutuun(ruutu);
	}

	private static void lataaTarinaRuutu(String tarina) {
		valitseAktiivinenRuutu("tarinaruutu");
		TarinaRuutu.lataaTarinaPätkä(tarina);
	}

	private static void lataaLoppuRuutu(PelinLopetukset pelinLoppuSyy) {
		LoppuRuutu.lataaLopetus(pelinLoppuSyy);
		valitseAktiivinenRuutu("loppuruutu");
	}

	private static void valitseAktiivinenRuutu(String ruutu) {
		SyöteYhdistetty.nollaaPainallukset();
		switch (ruutu) {
			case "peliruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.PELIRUUTU;
				Peli.peliKäynnissä = true;
				Peli.pause = false;
				Maailma.laskeNäköetäisyys(ikkuna);
				kamera.resetZoom(ikkuna);
				if (OhjeIkkuna.näytäOhjeet) OhjeIkkuna.avaaToimintoIkkuna();
			}
			case "tarinaruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.TARINARUUTU;
				Peli.pause = true;
			}
			case "valikkoruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.VALIKKORUUTU;
				Peli.pause = true;
				Musat.toistaPeliMusa("valikko");
			}
			case "asetusruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.ASETUSRUUTU;
				Peli.pause = true;
			}
			case "kehittäjäruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.KEHITTÄJÄRUUTU;
			}
			case "loppuruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.LOPPURUUTU;
				Peli.pause = true;
				Musat.toistaPeliMusa("tarina");
			}
			case "editoriruutu" -> {
				try {
					Peli.aktiivinenRuutu = Peli.Ruudut.EDITORIRUUTU;
					Peli.syötteenTila = Peli.SyötteenTila.PELI;
					Peli.pause = true;
				}
				catch (Exception e) {
					DialogiIkkunat.viestiIkkuna("Editoriin siirtyminen epäonnistui.", "Editoria ei voi käynnistää.\nEditori ei toimi, jos peli on käynnistetty Legacy-ikkunassa.", "ok", "error", false);
				}
			}
			case "editoriruutu_varmistus" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.EDITORIRUUTU_VARMISTUS;
			}
			case "virheruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.VIRHERUUTU;
				Peli.pause = true;
			}
		}
	}

	public static void päivitäKameranNäköalue(Ikkuna ikkuna) {
		kamera.setOrthographic(ikkuna.getWidth(), ikkuna.getHeight());
		kamera.resetZoom(ikkuna);
	}
}
