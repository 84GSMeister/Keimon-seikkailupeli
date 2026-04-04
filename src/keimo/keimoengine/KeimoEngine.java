package keimo.keimoengine;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.assets.GUITekstuurit;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.ikkuna.*;
import keimo.keimoengine.ruudut.LatausRuutu;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PeliKenttäMetodit;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.Renderöinti;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.assets.huone.HuoneLista;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaOverflow;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPokeri;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPong;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaTetris;
import keimo.seikkailupeli.io.SyöteYhdistetty;
import keimo.seikkailupeli.kenttä.*;
import keimo.seikkailupeli.menu.*;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.seikkailupeli.äänet.Musat;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class KeimoEngine extends Thread {

    String ikkunaTeksti = "Keimon Seikkailupeli";
    int ikkunanLeveys = 800;
    int ikkunanKorkeus = 600;
	boolean kokoNäyttö = false;
	public static Ikkuna window;
	public static boolean glKäynnistetty = false;
	public static long glfwWindowHandle = 0;

	public static Teksti kaatoTeksti;
	public static double tavoitePäivitysAika = 1f/6e0f;
	public static double tavoiteFrameAika = 60f;
	public static int frames;
	public static double keskivertoFrameAika = 0;
	boolean pääsäikeenAlustusValmis = false;
	boolean latausValmis = false;
	boolean pääsäikeenVirhe = false;
	
	public static boolean siirryEditoriin = false;

	static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
	static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");
	
	@Override
	public void run() {
		this.setName("Keimo Engine -säie");
		try {
			init();
		}
		catch (Exception excp) {
			excp.printStackTrace();
		}
	}
		
	protected void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW_Ikkuna.setCallbacks();
		try {
			//throw new IllegalStateException();
			initGLFW();
		}
		catch (IllegalStateException ise) {
			ise.printStackTrace();

			String viesti = "Grafiikkaikkunan luonti epäonnistui.\n\n" + //
								"Varmista, että sinulla on OpenGL 3.2 -yhteensopiva näytönohjain ja ajurit ajan tasalla.\n\n" + //
								"Nvidia: vähintään Geforce 8000 -sarja\n" + //
								"AMD/ATI: vähintään Radeon HD 2000 -sarja\n" + //
								"Intel: vähintään HD Graphics 4000 -sarja(Ivy Bridge)\n" + //
								"Muut: ei varmaan toimi\n\n" +
								"Sovellus sulkeutuu.";
			String otsikko = "Virhe OpenGL-kirjaston luonnissa.";
			tinyfd_messageBox(otsikko, viesti, "ok", "error", false);
		}
	}

	private void initGLFW() {
		Thread peliSäie = new Thread() {
			@Override
			public void run() {
				window = new GLFW_Ikkuna(ikkunaTeksti, kokoNäyttö, ikkunanLeveys, ikkunanKorkeus);
				final Image_parser ikkunanKuvake = Image_parser.load_image("tiedostot/kuvat/pelaaja_og.png");
				GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
				if (ikkunanKuvake.get_image() != null) {
					image.set(ikkunanKuvake.get_width(), ikkunanKuvake.get_height(), ikkunanKuvake.get_image());
					imagebf.put(0, image);
					glfwSetWindowIcon(window.getWindow(), imagebf);
				}
				window.asetaSyöte(new SyöteYhdistetty(window.getWindow()));
				glfwMakeContextCurrent(0);
				pääsäikeenAlustusValmis = true;

				while (!latausValmis) {
					try {
						// Odota renderöintisäiettä, jotta pelisäie ei ehdi alustaa GL-elementtejä
						Thread.sleep(50);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pelisäieLoop();
			}
		};
		peliSäie.setName("Keimo Engine -Pääsäie");
		peliSäie.start();


		Thread renderöintiSäie = new Thread() {
			@Override
			public void run() {

				while (!pääsäikeenAlustusValmis) {
					try {
						// Odota pelisäiettä, jotta GL-konteksti saadaan renderöintisäikeelle
						Thread.sleep(50);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				glfwWindowHandle = window.getWindow();
				glfwMakeContextCurrent(glfwWindowHandle);

				// Tehdään kaikki pelin sisällön lataus renderöintisäikeessä.

				GL.createCapabilities();
				glKäynnistetty = true;
				EngineAssets.createModels();
				GUITekstuurit.lataaTekstuurit();

				renderöiLatausRuutu("Alustetaan pelimoottoria", 10);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glEnable(GL_TEXTURE_2D);
				glDepthMask(true);
				glEnable(GL_DEPTH_TEST);
				glDepthFunc(GL_LEQUAL);
				glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
				Renderöinti.alusta(window);
				
				renderöiLatausRuutu("Ladataan grafiikkaa", 20);
				Assets.lataaTekstuurit();
				Assets.lataa3DMallit();
				KeimoFontit.rekisteröiFontit();

				renderöiLatausRuutu("Ladataan shadereita", 25);
				Assets.lataaShaderit();
				
				renderöiLatausRuutu("Ladataan ääniä", 30);
				Assets.lataaÄänet();
				Assets.lataaMusat();

				renderöiLatausRuutu("Ladataan asetuksia", 35);
				TavoiteLista.luoPääTavoiteLista();
				TavoiteLista.luoTavoiteLista();
				TarinaPätkä.nollaaTarinaId();
				KenttäKohde.nollaaObjektiId();
				Dialogit.luoTekstuurit();
				HuoneLista.lataaPelitiedosto();
				
				renderöiLatausRuutu("Mukautetaan kenttiä", 75);
				if (Peli.huoneKartta != null) {
					if (Peli.huoneKartta.get(0) != null) {
						Peli.muutaKentänKokoa(Peli.huoneKartta.get(0).annaKoko());
					}
				}
				Pelaaja.teleporttaaSpawniin();

				renderöiLatausRuutu("Luodaan maailmaa", 80);
				Maailma.createWorld();
				Maailma3D.createWorld();

				renderöiLatausRuutu("Ladataan", 90);
				kaatoTeksti = new Teksti("null", Color.white, 1, 1);
				glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Aseta tyhjennysväri mustaksi

				renderöiLatausRuutu("Käynnistetään peliä", 95);
				Renderöinti.siirrySeuraavaanRuutuun("tarinaruutu", "alku");
				latausValmis = true;
				renderöiLatausRuutu("Valmis", 100);

				renderöintisäieLoop();
			}
		};
		renderöintiSäie.setName("Keimo Engine -Renderöintisäie");
		renderöintiSäie.start();
	}

	protected void pelisäieLoop() {
		double startTime = Kello.annaAika(), passedTime, endTime, unprocessed = 0;
		while (!window.shouldClose()) {
			try {
				tavoitePäivitysAika = 1f/PelinAsetukset.pelinNopeus;
				endTime = Kello.annaAika();
				passedTime = endTime - startTime;
				unprocessed += passedTime;
				startTime = endTime;
				while (unprocessed >= tavoitePäivitysAika) {
					if (unprocessed > 0.05) unprocessed = 0.05;
					unprocessed -= tavoitePäivitysAika;
					pääsäikeenVirhe = false;
					// Pelilogiikan voisi laittaa omaan säikeeseen.
					peliLogiikkaLoop(); 
					// Tapahtumankäsittely pääsäikeessä, jotta muut säikeet (renderöinti) eivät jumitu ikkunaa siirrettäessä yms.
					window.annaSyöte().tarkistaSyöte();
					window.update();
					((GLFW_Ikkuna)window).pollEvents();
				}
				LockSupport.parkNanos(1_000_000);
			}
			catch (Exception e) {
				e.printStackTrace();
				pääsäikeenVirhe = true;
			}
		}
		glKäynnistetty = false;
		glfwTerminate();
		Musat.suljeMusa();
		System.exit(0);
	}

	double[] avgFrameTimes = new double[10];
	protected void renderöintisäieLoop() {
		double startTime = Kello.annaAika(), passedTime, endTime, unprocessed = 0;
		while (glKäynnistetty) {
			try {
				if (Peli.vaatiiUudelleenkäynnistyksen) {
					Peli.nollaaPeli();
					KeimoEngine.kaatoTeksti = new Teksti("", 0, 0);
					Renderöinti.siirrySeuraavaanRuutuun("tarinaruutu", "alku");
					Peli.vaatiiUudelleenkäynnistyksen = false;
				}
				//if (PelinAsetukset.tavoiteFPS == 0) tavoiteFrameAika = 0.001f;
				//else tavoiteFrameAika = 1f/PelinAsetukset.tavoiteFPS;
				tavoiteFrameAika = 1f/PelinAsetukset.pelinNopeus;
				boolean canRender = false;
				endTime = Kello.annaAika();
				passedTime = endTime - startTime;
				unprocessed += passedTime;
				startTime = endTime;
				while (unprocessed >= tavoiteFrameAika) {
					if (unprocessed > 0.05) unprocessed = 0.05;
					unprocessed -= tavoiteFrameAika;
					canRender = true;
					pelinPäivitysLoop(); // Pelin tilan päivityksen kautta kutsutaan useita GL-funktioita, joten helpointa pitää se renderöintisäikeessä.
					if (window.hasResized()) {
						Maailma.laskeNäköetäisyys(window);
						Renderöinti.päivitäKameranNäköalue(window);
						window.muutaKokoa(window.getWidth(), window.getHeight());
					}
				}
				if (canRender) {
					renderLoop();
				}
				else {
					LockSupport.parkNanos(1_000_000);
				}

				double frameTime = Kello.annaAika() - startTime;
				avgFrameTimes[frames % avgFrameTimes.length] = frameTime;
				keskivertoFrameAika = 0;
				for (int i = 0; i < avgFrameTimes.length; i++) {
					if (avgFrameTimes[i] > 0) keskivertoFrameAika += avgFrameTimes[i];
				}
				keskivertoFrameAika /= avgFrameTimes.length;
				frameTime = 0;

				if (pääsäikeenVirhe) {
					throw new Exception();
				}
			}
			catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				String sStackTrace = sw.toString();
				System.out.println(sStackTrace);
				String viesti = "Käsittelemätön virhe sovelluksessa. Ilmoitathan kehittäjille.\n\nVirhekoodi: \n";
				if (kaatoTeksti == null) viesti += "\nPakotettu kaatuminen\n\n";
				viesti += sStackTrace;
				viesti += "\n\nSovellus sulkeutuu.";
				glDisable(GL_STENCIL_TEST);
				glDisable(GL_DEPTH_TEST);
				Renderöinti.siirrySeuraavaanRuutuun("virheruutu");
				VirheRuutu.siirryVirheruutuun(viesti);
			}
		}
		GL.destroy();
	}

	private void renderLoop() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Tyhjennä framebuffer
		Renderöinti.renderöiRuutu(window);
		window.swapBuffers();
		frames++;
    }
	
	private void renderöiLatausRuutu(String latausTeksti, int latausProsentti) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		LatausRuutu.päivitäLatausTeksti(latausTeksti, latausProsentti);
		LatausRuutu.renderöiLatausRuutu(window, latausProsentti);
		window.swapBuffers();
	}

	public void pelinPäivitysLoop() {
		if (Peli.peliKäynnissä) {
			switch (Peli.aktiivinenRuutu) {
				default -> {
					Peli.pelaajanLiike();
					Peli.pelinKulku();
					Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
					switch (Peli.syötteenTila) {
						default -> {
							
						}
						case TOIMINTO -> {
							switch (Peli.toimintoIkkuna) {
								case MINIPELI_3D -> {
									Maailma3D.maailma3DLoop();
								}
								case MINIPELI_PONG -> {
									MinipeliIkkunaPong.pelaa();
								}
								case MINIPELI_POKERI -> {
									MinipeliIkkunaPokeri.pelaa();
								}
								case MINIPELI_TETRIS -> {
									MinipeliIkkunaTetris.pelaa();
								}
								case MINIPELI_4 -> {
									MinipeliIkkunaOverflow.pelaa();
								}
								default -> {}
							}
						}
					}
				}
			}
		}
    }

	public void peliLogiikkaLoop() {
		if (Peli.peliKäynnissä) {
			switch (Peli.aktiivinenRuutu) {
				default -> {
					switch (Peli.syötteenTila) {
						default -> {
							if (!Peli.pause && !Peli.pauseDialogi) {
								if (Peli.annaObjektiKenttä() != null && Peli.annaMaastoKenttä() != null) {
									PeliKenttäMetodit.suoritaPelikenttäMetoditJokaTick();
									if (Kello.globaaliTickit() % 2 == 0) {
										PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2Tick();
									}
									if (Kello.globaaliTickit() % 10 == 0) {
										PeliKenttäMetodit.suoritaPelikenttäMetoditJoka10Tick();
									}
									if (Kello.globaaliTickit() % 60 == 0) {
										PeliKenttäMetodit.suoritaPelikenttäMetoditJoka60Tick();
									}
									if (Kello.globaaliTickit() % 2000 == 0) {
										PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2000Tick();
									}
									if (Kello.globaaliTickit() % 100 == 0) {
										PeliKenttäMetodit.suoritaPelikenttäMetoditJoka100Tick();
									}
									if (Kello.globaaliTickit() % 600 == 0) {
										PeliKenttäMetodit.suoritaPelikenttäMetoditJoka600Tick();
									}
								}
							}
						}
					}
				}
			}
		}
		if (!Peli.pause) {
            Kello.tick();
        }
	}

	public static void suljePeliIkkuna() {
		glfwSetWindowShouldClose(window.getWindow(), true);
	}
}
