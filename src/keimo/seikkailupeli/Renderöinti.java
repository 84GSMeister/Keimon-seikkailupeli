package keimo.seikkailupeli;

import org.joml.Vector3f;
import org.joml.Vector4f;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.seikkailupeli.assets.PelaajaModel;
import keimo.seikkailupeli.gui.hud.DebugTeksti;
import keimo.seikkailupeli.gui.hud.HUD;
import keimo.seikkailupeli.gui.hud.OstosLista;
import keimo.seikkailupeli.gui.hud.TavoitePopup;
import keimo.seikkailupeli.gui.toimintoIkkunat.DialogiValintaIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.HuijauskoodiValikko;
import keimo.seikkailupeli.gui.toimintoIkkunat.KarttaIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.OhjeIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.PullonPalautusIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.ÄmpäriJonoIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaOverflow;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPokeri;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPong;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaTetris;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.kenttä.Maailma3D;
import keimo.seikkailupeli.menu.KehittäjäRuutu;
import keimo.seikkailupeli.menu.LoppuRuutu;
import keimo.seikkailupeli.menu.TarinaRuutu;
import keimo.seikkailupeli.menu.ValikkoRuutu;
import keimo.seikkailupeli.menu.VirheRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.GrafiikkaAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.OhjainAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.PeliAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.ÄäniAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiMidi;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiValikko;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiWoof;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.menu.editori.EditoriRuutuVarmistus;
import keimo.seikkailupeli.menu.editori.gui.EditorinValikko;
import keimo.seikkailupeli.objektit.Pelaaja;

import static org.lwjgl.opengl.GL11.*;

public class Renderöinti {

    static double hudAika = 0;
    static Shader valikkoShader;
    static Shader peliShader;

    public static void luoShaderit() {
        valikkoShader = new Shader("staattinen");
		valikkoShader.setUniform("sampler", 0);
		valikkoShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
		valikkoShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
		valikkoShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));

        peliShader = new Shader("shader");
		peliShader.setUniform("sampler", 0);
		peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
		peliShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
		peliShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));
    }

    public static void renderöiRuutu(Ikkuna window, Kamera camera, PelaajaModel player) {
        switch (Peli.aktiivinenRuutu) {
			case PELIRUUTU -> {
				double alkuAika = System.nanoTime();
				KeimoEngine.kaatoTeksti.bind(0);

				Maailma.render(camera, window);
				double tileAika = System.nanoTime() - alkuAika;
				
				player.update((float)KeimoEngine.targetUpdate, window, camera);
				player.render(camera, window);
				double pelaajaAika = System.nanoTime() - alkuAika - tileAika;
				
				//world.correctCamera(camera, window);
				renderöiHUD(valikkoShader, window, camera);
				
				if (PelinAsetukset.debugTiedot) {
					DebugTeksti.renderöiDebugTeksti(tileAika, pelaajaAika, hudAika, window);
				}
				DebugTeksti.renderöiLisäMoodiTekstit(window);
				hudAika = System.nanoTime() - alkuAika - tileAika - pelaajaAika;
			}
			case TARINARUUTU -> {
				TarinaRuutu.render(valikkoShader, window);
			}
			case VALIKKORUUTU -> {
				ValikkoRuutu.render(valikkoShader, window);
			}
			case ASETUSRUUTU -> {
				AsetusRuutu.render(valikkoShader, window);
			}
			case ASETUSRUUTU_GRAFIIKKA -> {
				GrafiikkaAsetusRuutu.render(valikkoShader, window);
			}
			case ASETUSRUUTU_ÄÄNI -> {
				ÄäniAsetusRuutu.render(valikkoShader, window);
			}
			case ASETUSRUUTU_PELI -> {
				PeliAsetusRuutu.render(valikkoShader, window);
			}
            case ASETUSRUUTU_OHJAIMET -> {
				OhjainAsetusRuutu.render(valikkoShader, window);
			}
			case ASETUSRUUTU_ÄÄNITESTI_VALIKKO -> {
				ÄäniTestiValikko.render(valikkoShader, window);
			}
			case ASETUSRUUTU_ÄÄNITESTI_PELIÄÄNET -> {
				ÄäniTestiRuutu.render(valikkoShader, window);
			}
			case ASETUSRUUTU_ÄÄNITESTI_MIDI -> {
				ÄäniTestiMidi.render(valikkoShader, window);
			}
			case ASETUSRUUTU_ÄÄNITESTI_WOOF -> {
				ÄäniTestiWoof.render(valikkoShader, window);
			}
			case KEHITTÄJÄRUUTU -> {
				KehittäjäRuutu.render(window);
			}
			case LOPPURUUTU -> {
				LoppuRuutu.render(window);
			}
			case VIRHERUUTU -> {
				VirheRuutu.render(valikkoShader, window);
			}
			case MINIPELIRUUTU -> {
				Maailma3D.render(window);
			}
			case EDITORIRUUTU -> {
				EditoriRuutu.render(camera, window);
				switch (Peli.syötteenTila) { 
					case TOIMINTO -> {
						EditorinValikko.renderöi(valikkoShader, window);
					}
					default -> {}
				}
			}
			case EDITORIRUUTU_VARMISTUS -> {
				EditoriRuutuVarmistus.render(valikkoShader, window);
			}
			case null, default -> {

			}
		}
    }

    private static void renderöiHUD(Shader valikkoShader, Ikkuna window, Kamera camera) {

		HUD.renderöiHUD(window);
		TavoitePopup.renderöiTavoitePopup(window);
		
		switch (Peli.syötteenTila) {
			case PELI -> {}
			case DIALOGI -> {
				HUD.renderöiDialogiLaatikko(window);
			}
			case TOIMINTO -> {
				switch (Peli.toimintoIkkuna) {
					case PULLONPALAUTUS -> {
						PullonPalautusIkkuna.simuloiPullonpalautus();
						PullonPalautusIkkuna.tarkistaTila();
						PullonPalautusIkkuna.renderöiIkkuna(window);
					}
					case VALINTADIALOGI -> {
						DialogiValintaIkkuna.renderöi(valikkoShader, window);
					}
					case ÄMPÄRIJONO -> {
						ÄmpäriJonoIkkuna.simuloiÄmpärijono();
						ÄmpäriJonoIkkuna.tarkistaTila();
						ÄmpäriJonoIkkuna.renderöiIkkuna(window);
					}
					case KARTTA -> {
						KarttaIkkuna.renderöiIkkuna(window);
					}
					case OHJEET -> {
						OhjeIkkuna.renderöiIkkuna(window);
					}
					case HUIJAUSKOODIT -> {
						HuijauskoodiValikko.renderöi(valikkoShader, window);
					}
					case MINIPELI_0 -> {

					}
					case MINIPELI_PONG -> {
						MinipeliIkkunaPong.pelaa();
						MinipeliIkkunaPong.renderöiKehys(window);
						MinipeliIkkunaPong.renderöiIkkuna(window, camera);
					}
					case MINIPELI_POKERI -> {
						MinipeliIkkunaPokeri.pelaa();
						MinipeliIkkunaPokeri.renderöiKehys(window);
						MinipeliIkkunaPokeri.renderöiIkkuna(window, camera);
					}
					case MINIPELI_TETRIS -> {
						MinipeliIkkunaTetris.pelaa();
						MinipeliIkkunaTetris.renderöiKehys(window);
						MinipeliIkkunaTetris.renderöiIkkuna(window, camera);
					}
					case MINIPELI_4 -> {
						MinipeliIkkunaOverflow.pelaa();
						MinipeliIkkunaOverflow.renderöiKehys(window);
						MinipeliIkkunaOverflow.renderöiIkkuna(window, camera);
					}
				}
			}
		}

		if (Peli.huone != null) {
			if (Peli.huone.annaNimi().startsWith("Kauppa")) {
				OstosLista.render(valikkoShader, window);
			}
		}
	}

    public static void häivytäPeliRuutuUlos(Ikkuna window, Kamera camera, PelaajaModel player) {
        while (Maailma.fade < 1f) {
            Maailma.fade += 0.02f;
			try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                renderöiRuutu(window, camera, player);
                window.swapBuffers();
                KeimoEngine.frames++;
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void häivytäPeliRuutuSisään(Ikkuna window, Kamera camera, PelaajaModel player) {
		camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
		while (Maailma.fade > 0f) {
			Maailma.fade -= 0.02f;
			try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                renderöiRuutu(window, camera, player);
                window.swapBuffers();
                KeimoEngine.frames++;
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
