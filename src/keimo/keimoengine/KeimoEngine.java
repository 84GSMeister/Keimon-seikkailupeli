package keimo.keimoengine;

import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.ikkuna.*;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PeliKenttäMetodit;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.HuoneLista;
import keimo.seikkailupeli.assets.PelaajaModel;
import keimo.seikkailupeli.assets.TarinaDialogiLista;
import keimo.seikkailupeli.assets.TarinaPätkä;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.gui.*;
import keimo.seikkailupeli.gui.hud.*;
import keimo.seikkailupeli.gui.toimintoIkkunat.*;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.*;
import keimo.seikkailupeli.kenttä.*;
import keimo.seikkailupeli.menu.*;
import keimo.seikkailupeli.menu.asetusRuudut.*;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.menu.editori.EditoriRuutuVarmistus;
import keimo.seikkailupeli.menu.editori.gui.EditorinValikko;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.entityt.npc.NPC;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Pulloautomaatti;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.seikkailupeli.toiminnot.NäppäinKomennot;
import keimo.seikkailupeli.äänet.Musat;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

import javax.swing.JOptionPane;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
	public static Window window;
	public static boolean glKäynnistetty = false;

	Shader valikkoShader;
	static Kamera camera;
	public static Teksti kaatoTeksti;
	double startTime, passedTime, endTime;
	double targetUpdate = 1f/6e0f;
	public static double frameTime;
	public static int frames;
	double unprocessed = 0;
	public static Maailma world;
	
	PelaajaModel player;
	public static boolean siirryEditoriin = false;

	static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
	static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");
	
	@Override
	public void run() {
		this.setName("Keimo Engine -säie");
		try {
			init();
			loop();
		} catch (Exception excp) {
			excp.printStackTrace();
		}
	}
		
	protected void init() {
		glKäynnistetty = false;
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW_Window.setCallbacks();
		try {
			//throw new IllegalStateException();
			initGLFW();
		}
		catch (IllegalStateException ise) {
			//JOptionPane.showMessageDialog(null, "Grafiikkaikkunan luonti epäonnistui.\n\nVarmista, että sinulla on OpenGL 3.2 -yhteensopiva näytönohjain ja ajurit ajan tasalla.\n\nNvidia: vähintään Geforce 8000 -sarja\nAMD/ATI: vähintään Radeon 3000 -sarja\nIntel: vähintään HD Graphics 4000 -sarja(Ivy Bridge)\nMuut: ei varmaan toimi", "Virhe OpenGL-kirjaston luonnissa.", JOptionPane.ERROR_MESSAGE);
			ise.printStackTrace();

			String viesti = "Grafiikkaikkunan luonti epäonnistui.\n\n" + //
								"Varmista, että sinulla on OpenGL 3.2 -yhteensopiva näytönohjain ja ajurit ajan tasalla.\n\n" + //
								"Nvidia: vähintään Geforce 8000 -sarja\n" + //
								"AMD/ATI: vähintään Radeon 3000 -sarja\n" + //
								"Intel: vähintään HD Graphics 4000 -sarja(Ivy Bridge)\n" + //
								"Muut: ei varmaan toimi\n\n" +
								"Virhe voi myös johtua GLFW-ikkunakirjaston toimimattomuudesta joillakin näytönohjainten ja käyttöjärjestelmien yhdistelmillä.\n" +
								"Jos sinulla on vanha Intelin näytönohjain ja uudempi Windows-versio kuin 7, tätä ongelmaa ei varmaan pystytä korjaamaan.\n\n" +
								"Haluatko kokeilla käynnistää pelin vanhalla ikkunointijärjestelmällä (kaikki ominaisuudet eivät ehkä toimi)?";
			String otsikko = "Virhe OpenGL-kirjaston luonnissa.";
			int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
			int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
			String[] vaihtoehdot = {"Käynnistä Legacy-tilassa", "Sulje sovellus"};

			switch (JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[1])) {
				case JOptionPane.OK_OPTION -> {
					try {
						System.out.println("käynnisteään legacy-tilassa");
						initLegacy();
					}
					catch (Exception e) {
						viesti = "Käsittämätön poikkeus sovelluksessa. \n\nSanokaa sille jontulle että vetää käteen ja korjaa paskan softansa.";
						otsikko = "Fataali häire!";
						valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
						viestiTyyppi = JOptionPane.ERROR_MESSAGE;
						JOptionPane.showMessageDialog(null, viesti, otsikko, JOptionPane.ERROR_MESSAGE);
					}
				}
				case JOptionPane.CANCEL_OPTION, JOptionPane.CLOSED_OPTION -> {
					ise.printStackTrace();
				}
			}
		}
	}

	private void initGLFW() {
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		window = new GLFW_Window(ikkunaTeksti, kokoNäyttö, ikkunanLeveys, ikkunanKorkeus);
		final Image_parser ikkunanKuvake = Image_parser.load_image("tiedostot/kuvat/pelaaja_og.png");
		GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
		if (ikkunanKuvake.get_image() != null) {
			image.set(ikkunanKuvake.get_width(), ikkunanKuvake.get_height(), ikkunanKuvake.get_image());
			imagebf.put(0, image);
			glfwSetWindowIcon(window.getWindow(), imagebf);
		}
		window.setInput(new NäppäinKomennot(window.getWindow()));
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		//((GLFW_Window)(window)).createNuklearContext();
		glKäynnistetty = true;
		EngineAssets.createModels();

		renderöiLatausRuutu("Alustetaan pelimoottoria", 10);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		valikkoShader = new Shader("staattinen");
		valikkoShader.bind();
		valikkoShader.setUniform("sampler", 0);
		valikkoShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
		valikkoShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
		valikkoShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));
		
		renderöiLatausRuutu("Ladataan grafiikkaa", 20);
		Assets.lataa3DMallit();
		luoObjektiTekstuurit();
		KeimoFontit.rekisteröiFontit();
		player = new PelaajaModel();

		renderöiLatausRuutu("Ladataan ääniä", 25);
		Assets.lataaÄänet();
		Assets.lataaMusat();

		renderöiLatausRuutu("Ladataan asetuksia", 30);
		TavoiteLista.luoPääTavoiteLista();
		TavoiteLista.luoTavoiteLista();
		TarinaPätkä.nollaaTarinaId();
		KenttäKohde.nollaaObjektiId();

		//renderöiLatausRuutu("Ladataan kenttiä", 40);
		HuoneLista.lataaPelitiedosto();
		
		renderöiLatausRuutu("Mukautetaan kenttiä", 70);
		if (Peli.huoneKartta != null) {
			if (Peli.huoneKartta.get(0) != null) {
				Peli.muutaKentänKokoa(Peli.huoneKartta.get(0).annaKoko());
			}
		}
		Pelaaja.teleporttaaSpawniin();

		camera = new Kamera(window.getWidth(), window.getHeight());
		camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
		camera.setOrthographic(window.getWidth(), window.getHeight());
		camera.setRotation(new Quaternionf(new AxisAngle4f((float)Math.toRadians(30), new Vector3f(1, 0, 0))));

		renderöiLatausRuutu("Luodaan maailmaa", 80);
		world = new Maailma();
		Maailma3D.createWorld();

		renderöiLatausRuutu("Ladataan", 90);
		kaatoTeksti = new Teksti("null", Color.white, 1, 1);
		startTime = Kello.annaAika();
		unprocessed = 0;
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set the clear color

		renderöiLatausRuutu("Käynnistetään peliä", 95);
		lataaTarinaRuutu("alku");
		renderöiLatausRuutu("Valmis", 100);
	}

	private void initLegacy() {
		ikkunanLeveys = 400;
		ikkunanKorkeus = 150;
		ikkunaTeksti = "Testi";
		window = new Legacy_Window(ikkunaTeksti, kokoNäyttö, ikkunanLeveys, ikkunanKorkeus);
	}

	protected void loop() {
		while (!window.shouldClose()) {
			try {
				targetUpdate = 1f/PelinAsetukset.pelinNopeus;
				boolean canRender = false;
				endTime = Kello.annaAika();
				passedTime = endTime - startTime;
				unprocessed += passedTime;
				frameTime += passedTime;
				startTime = endTime;
				while (unprocessed >= targetUpdate) {
					if (unprocessed > 0.05) unprocessed = 0.05;
					unprocessed -= targetUpdate;
					canRender = true;
					window.getInput().tarkistaSyöte();
					peliLoop();
					if (window.hasResized()) {
						world.laskeNäköetäisyys(window);
						camera.setOrthographic(window.getWidth(), window.getHeight());
						camera.resetZoom(window);
						window.setView(window.getWidth(), window.getHeight());
						glViewport(0, 0, window.getWidth(), window.getHeight());
					}
					window.update();
					if (frameTime >= 1f) {
						frameTime = 0;
						frames = 0;
					}
				}
				if (canRender) {
					renderöiRuutu();
				}
				else {
					LockSupport.parkNanos(1_000_000);
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
				valitseAktiivinenRuutu("virheruutu");
				VirheRuutu.siirryVirheruutuun(viesti);
			}
		}
		GL.destroy();
		glfwTerminate();
		Musat.suljeMusa();
		glKäynnistetty = false;
		System.exit(0);
	}

	private static void luoObjektiTekstuurit() {
		Pulloautomaatti.luoTekstuurit();
	}

	public static void lataaTarinaRuutu(String tarina) {
		valitseAktiivinenRuutu("tarinaruutu");
		TarinaRuutu.lataaTarinaPätkä(tarina);
		if (tarina.equals("alku")) Musat.toistaPeliMusa("tarina");
		else Musat.toistaPeliMusa("välitarina");
	}

	public static void lataaLoppuRuutu(PelinLopetukset pelinLoppuSyy) {
		LoppuRuutu.lataaLopetus(pelinLoppuSyy);
		valitseAktiivinenRuutu("loppuruutu");
	}

	public static void valitseAktiivinenRuutu(String ruutu) {
		switch (ruutu) {
			case "peliruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.PELIRUUTU;
				Peli.peliAloitettu = true;
				Peli.peliKäynnissä = true;
				Peli.pause = false;
				world.laskeNäköetäisyys(window);
				camera.resetZoom(window);
				if (OhjeIkkuna.näytäOhjeet) OhjeIkkuna.avaaToimintoIkkuna();
			}
			case "tarinaruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.TARINARUUTU;
				Peli.pause = true;
				camera.setPosition(new Vector3f(0,0.5f, 0));
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
			case "asetusruutu_grafiikka" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.ASETUSRUUTU_GRAFIIKKA;
			}
			case "asetusruutu_ääni" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.ASETUSRUUTU_ÄÄNI;
			}
			case "asetusruutu_peli" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.ASETUSRUUTU_PELI;
			}
			case "asetusruutu_äänitesti" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.ASETUSRUUTU_ÄÄNITESTI;
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
					Peli.pause = true;
					EditoriRuutu.luoEditoriRuutu((GLFW_Window)window);
					EditoriRuutu.lataaHuone(0);
					EditorinValikko.suljeValikko();
				}
				catch (Exception e) {
					tinyfd_messageBox("Editoriin siirtyminen epäonnistui.", "Editoria ei voi käynnistää.\nEditori ei toimi, jos peli on käynnistetty Legacy-ikkunassa.", "ok", "error", false);
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
	double hudAika = 0;
	private void renderöiRuutu() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		switch (Peli.aktiivinenRuutu) {
			case PELIRUUTU -> {
				double alkuAika = System.nanoTime();
				kaatoTeksti.bind(0);

				world.render(camera, window);
				double tileAika = System.nanoTime() - alkuAika;
				
				player.update((float)targetUpdate, window, camera, world);
				player.render(camera, world, window);
				double pelaajaAika = System.nanoTime() - alkuAika - tileAika;
				
				//world.correctCamera(camera, window);
				renderöiHUD();
				
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
			case ASETUSRUUTU_ÄÄNITESTI -> {
				ÄäniTestiRuutu.render(window);
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
		window.swapBuffers();
		frames++;
    }
	
	private void renderöiHUD() {
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
					case MINIPELI_1 -> {
						MinipeliIkkunaPong.pelaa();
						MinipeliIkkunaPong.renderöiKehys(window);
						MinipeliIkkunaPong.renderöiIkkuna(window, camera);
					}
					case MINIPELI_2 -> {
						MinipeliIkkunaPokeri.pelaa();
						MinipeliIkkunaPokeri.renderöiKehys(window);
						MinipeliIkkunaPokeri.renderöiIkkuna(window, camera);
					}
					case MINIPELI_3 -> {
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

	private void renderöiLatausRuutu(String latausTeksti, int latausProsentti) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		LatausRuutu.päivitäLatausTeksti(latausTeksti, latausProsentti);
		LatausRuutu.renderöiLatausRuutu(window, latausProsentti);
		window.swapBuffers();
	}

	public void peliLoop() {
		if (Peli.peliKäynnissä) {
			switch (Peli.aktiivinenRuutu) {
				case MINIPELIRUUTU -> {
					Maailma3D.maailma3DLoop();
				}
				default -> {
					if (!Peli.pause && !Peli.pauseDialogi) {
						Peli.pelaajanLiike();
						Peli.pelinKulku();
						Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
						camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
						if (Kamera.päivitäZoom) camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX * Kamera.zoomKerroin, Kamera.zoomX * Kamera.zoomKerroin, -Kamera.zoomY * Kamera.zoomKerroin, Kamera.zoomY * Kamera.zoomKerroin));
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
					if (Kello.globaaliTickit() % 2 == 0) {
						Dialogit.scrollaaDialogiTeksti();
					}
				}
			}
		}
        if (!Peli.pause) {
            Kello.tick();
        }
    }

	/**
     * "Siirry valittuun huoneeseen" eli
     * Lataa pelin nykyiseksi kentäksi huonekartasta (HashMapista) valittu Huone-objekti
     * @param huoneenId ladattavan huoneen ID huonekartassa
	 * @param pelaajanX X-koordinaatti, johon pelaaja siirretään (Tile)
	 * @param pelaajanY Y-koordinaatti, johon pelaaja siirretään (Tile)
     * @param debug estä tavoitteen tarkistus ja tarinan lataus
     */
	public void lataaHuone(int huoneenId, int pelaajanX, int pelaajanY, boolean debug) {
        try{
            if (Peli.huoneKartta.get(huoneenId) != null) {
                if ((!Peli.huoneKartta.get(huoneenId).annaTavoiteVaatimus() || TavoiteLista.tavoiteLista.get(Peli.huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste())) || debug || Pelaaja.ohitaTavoitteet) {
                    if (Peli.huone != null) {
						if (!Peli.huone.annaTaustanPolku().equals(Peli.huoneKartta.get(huoneenId).annaTaustanPolku())) Tausta.häivytäTausta = true;
						else Tausta.häivytäTausta = false;
						häivytäPeliRuutuUlos();
					}
					Peli.huone = Peli.huoneKartta.get(huoneenId);
					Peli.muutaKentänKokoa(Peli.huone.annaKoko());
                    synchronized (Peli.entityLista) {
						Peli.entityLista.clear();
						for (Entity[] nn : Peli.huone.annaHuoneenNPCSisältö()) {
							for (Entity entity : nn) {
								if (entity != null) {
									Peli.entityLista.add(entity);
									if (entity instanceof NPC) {
										NPC npc = (NPC)entity;
										if (!npc.onLadattuPelissä) {
											npc.teleport(npc.annaAlkuSijX(), npc.annaAlkuSijY());
											npc.onLadattuPelissä = true;
										}
									}
								}
							}
						}
					}
                    Peli.voiWarpataVasen = false;
                    Peli.voiWarpataOikea = false;
                    Peli.voiWarpataAlas = false;
                    Peli.voiWarpataYlös = false;
                    Peli.warppiViive = 20;
                    boolean toistaMusaTarinanJälkeen = false;
					Pelaaja.teleport(pelaajanX, pelaajanY);
                    if (Peli.huoneKartta.get(huoneenId).annaTarinaRuudunLataus() && !debug) {
                        if (TarinaDialogiLista.tarinaKartta.containsKey(Peli.huoneKartta.get(huoneenId).annaTarinaRuudunTunniste())) {
                            Peli.pause = true;
                            toistaMusaTarinanJälkeen = true;
                            Peli.siirryTarinaRuutuun(Peli.huoneKartta.get(huoneenId).annaTarinaRuudunTunniste());
                            Peli.huoneKartta.get(huoneenId).päivitäAlkudialogi(null);
                        }
                        else {
                            Dialogit.avaaDialogi("", "Tarinapätkää " + Peli.huoneKartta.get(huoneenId).annaTarinaRuudunTunniste() + " ei löytynyt", "Virhe!");
                        }
                    }
                    if (!toistaMusaTarinanJälkeen) {
                        String musa = Peli.huone.annaHuoneenMusa();
                        Musat.toistaPeliMusa(musa);
                    }
                    else {
                        //Musat.toistaPeliMusa(null);
                    }
					häivytäPeliRuutuSisään();
                }
                else {
					Dialogit.haeTavoiteVinkkiTeksti(Peli.huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste());
                }
            }
            else {
				if (!window.isFullscreen()) tinyfd_messageBox("Huonetta ei löytynyt.", "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "ok", "error", false);
				Peli.voiWarpataVasen = false;
				Peli.voiWarpataOikea = false;
				Peli.voiWarpataAlas = false;
				Peli.voiWarpataYlös = false;
            }
            
        }
        catch (NullPointerException e) {
			if (!window.isFullscreen()) tinyfd_messageBox("Huonetta ei löytynyt.", "Ongelma ladatessa huonetta " + huoneenId + ". Tämä voi johtua viallisesta default.kst-tiedostosta.", "ok", "error", false);
            e.printStackTrace();
			Peli.voiWarpataVasen = false;
			Peli.voiWarpataOikea = false;
			Peli.voiWarpataAlas = false;
			Peli.voiWarpataYlös = false;
        }
    }

	public void häivytäPeliRuutuUlos() {
		while (Maailma.fade < 1f) {
			Maailma.fade += 0.02f;
			try {
				renderöiRuutu();
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void häivytäPeliRuutuSisään() {
		camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
		while (Maailma.fade > 0f) {
			Maailma.fade -= 0.02f;
			try {
				renderöiRuutu();
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void suljePeliIkkuna() {
		glfwSetWindowShouldClose(window.getWindow(), true);
	}
}
