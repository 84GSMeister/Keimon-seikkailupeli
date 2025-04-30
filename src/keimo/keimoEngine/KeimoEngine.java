package keimo.keimoEngine;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PeliKenttäMetodit;
import keimo.PelinAsetukset;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.HuoneLista;
import keimo.HuoneEditori.ObjektiKuvakkeet;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Utility.KeimoFontit;
import keimo.entityt.Entity;
import keimo.entityt.npc.NPC;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.assets.PelaajaModel;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.gui.*;
import keimo.keimoEngine.gui.hud.*;
import keimo.keimoEngine.gui.toimintoIkkunat.*;
import keimo.keimoEngine.ikkuna.*;
import keimo.keimoEngine.io.NäppäinKomennot;
import keimo.keimoEngine.kenttä.*;
import keimo.keimoEngine.menu.*;
import keimo.keimoEngine.menu.asetusRuudut.*;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.keimoEngine.äänet.Musa;
import keimo.keimoEngine.äänet.Ääni;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.kiintopiste.Pulloautomaatti;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class KeimoEngine extends Thread {

    String ikkunaTeksti = "Keimon Seikkailupeli";
    int ikkunanLeveys = 800;
    int ikkunanKorkeus = 600;
	boolean kokoNäyttö = false;
	public static Window window;
	public static boolean glKäynnistetty = false;

	Shader shader;
	static Kamera camera;
	public static Teksti kaatoTeksti;
	double startTime, passedTime, endTime;
	double targetUpdate = 1f/6e0f;
	public static double frameTime;
	public static int frames;
	double unprocessed = 0;
	public static Maailma world;
	Tausta tausta;
	PelaajaModel player;
	public static boolean siirryEditoriin = false;

	static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
	static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

	private long audioContext;
    private long audioDevice;
	public static Musa musa;
	
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
		Window.setCallbacks();
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		try {
			if (!glfwInit()) {
				throw new IllegalStateException("Unable to initialize GLFW");
			}

			String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
			audioDevice = alcOpenDevice(defaultDeviceName);
			int[] audioAttributes = {0};
			audioContext = alcCreateContext(audioDevice, audioAttributes);
			alcMakeContextCurrent(audioContext);
			ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
			ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
			if (!alCapabilities.OpenAL10) {
				assert false : "audio not supported";
			}

			window = new Window(ikkunaTeksti, kokoNäyttö, ikkunanLeveys, ikkunanKorkeus);

			final Image_parser ikkunanKuvake = Image_parser.load_image("tiedostot/kuvat/pelaaja_og.png");
			GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
			image.set(ikkunanKuvake.get_width(), ikkunanKuvake.get_height(), ikkunanKuvake.get_image());
			imagebf.put(0, image);
			glfwSetWindowIcon(window.getWindow(), imagebf);
			
			// This line is critical for LWJGL's interoperation with GLFW's
			// OpenGL context, or any context that is managed externally.
			// LWJGL detects the context that is current in the current thread,
			// creates the GLCapabilities instance and makes the OpenGL
			// bindings available for use.
			GL.createCapabilities();
			glKäynnistetty = true;
			Assets.createModels();
			Assets.luo3DMallit();
			Assets.luoÄänet();
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_TEXTURE_2D);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			glDepthMask(true);
        	glEnable(GL_DEPTH_TEST);
        	glDepthFunc(GL_LEQUAL);
       		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
			renderöiLatausRuutu();
			window.swapBuffers();
			
			TavoiteLista.luoPääTavoiteLista();
			TavoiteLista.luoTavoiteLista();
			TarinaPätkä.nollaaTarinaId();
			KeimoFontit.rekisteröiFontit();

			KenttäKohde.nollaaObjektiId();
			HuoneLista.lataaPelitiedosto();
			
			if (Peli.huoneKartta != null) {
				if (Peli.huoneKartta.get(0) != null) {
					Peli.muutaKentänKokoa(Peli.huoneKartta.get(0).annaKoko());
				}
			}

			luoObjektiTekstuurit();

			camera = new Kamera(window.getWidth(), window.getHeight());
			camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
			camera.setOrthographic(window.getWidth(), window.getHeight());
			camera.setRotation(new Quaternionf(new AxisAngle4f((float)Math.toRadians(30), new Vector3f(1, 0, 0))));

			world = new Maailma();
			Maailma3D.createWorld();
			tausta = new Tausta();
			kaatoTeksti = new Teksti("null", Color.white, 1, 1);
			ÄänentoistamisSäie.lataaÄänet();
			
			player = new PelaajaModel();
			Pelaaja.teleporttaaSpawniin();
			shader = new Shader("shader");
			shader.bind();
			shader.setUniform("sampler", 0);
			shader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
			shader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
        	shader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));

			startTime = Timer.getTime();
			unprocessed = 0;

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set the clear color

			editoriInit();
			lataaTarinaRuutu("alku");
		}
		catch (IllegalStateException ise) {
			JOptionPane.showMessageDialog(null, "Grafiikkaikkunan luonti epäonnistui.\n\nVarmista, että sinulla on OpenGL 3.2 -yhteensopiva näytönohjain ja ajurit ajan tasalla.\n\nNvidia: vähintään Geforce 8000 -sarja\nAMD/ATI: vähintään Radeon 3000 -sarja\nIntel: vähintään HD Graphics 4000 -sarja(Ivy Bridge)\nMuut: ei varmaan toimi", "Virhe OpenGL-kirjaston luonnissa.", JOptionPane.ERROR_MESSAGE);
			ise.printStackTrace();
		}
	}

	protected void loop() {
		while (!window.shouldClose()) {
			try {
				targetUpdate = 1f/PelinAsetukset.pelinNopeus;
				boolean canRender = false;
				endTime = Timer.getTime();
				passedTime = endTime - startTime;
				unprocessed += passedTime;
				frameTime += passedTime;
				startTime = endTime;
				while (unprocessed >= targetUpdate) {
					if (unprocessed > 0.05) unprocessed = 0.05;
					unprocessed -= targetUpdate;
					canRender = true;
					NäppäinKomennot.tarkistaSyöte(window, camera, world);
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
		alcDestroyContext(audioContext);
		alcCloseDevice(audioDevice);
		GL.destroy();
		glfwTerminate();
		ÄänentoistamisSäie.suljeMusa();
		glKäynnistetty = false;
		if (siirryEditoriin) editoriLoop();
		//System.exit(0);
	}

	private static void luoObjektiTekstuurit() {
		Pulloautomaatti.luoTekstuurit();
	}

	public static void lataaTarinaRuutu(String tarina) {
		valitseAktiivinenRuutu("tarinaruutu");
		TarinaRuutu.lataaTarinaPätkä(tarina);
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
				ÄänentoistamisSäie.toistaPeliMusa("tarina");
				camera.setPosition(new Vector3f(0,0.5f, 0));
			}
			case "valikkoruutu" -> {
				Peli.aktiivinenRuutu = Peli.Ruudut.VALIKKORUUTU;
				Peli.pause = true;
				ÄänentoistamisSäie.toistaPeliMusa("valikko");
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
				ÄänentoistamisSäie.toistaPeliMusa("tarina");
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
				renderöiTausta();
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
				TarinaRuutu.render(window);
			}
			case VALIKKORUUTU -> {
				ValikkoRuutu.render(window);
			}
			case ASETUSRUUTU -> {
				AsetusRuutu.render(window);
			}
			case ASETUSRUUTU_GRAFIIKKA -> {
				GrafiikkaAsetusRuutu.render(window);
			}
			case ASETUSRUUTU_ÄÄNI -> {
				ÄäniAsetusRuutu.render(window);
			}
			case ASETUSRUUTU_PELI -> {
				PeliAsetusRuutu.render(window);
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
				VirheRuutu.render(window);
			}
			case MINIPELIRUUTU -> {
				Maailma3D.render(window);
			}
			case null, default -> {

			}
		}
		window.swapBuffers();
		frames++;
    }

	private void renderöiTausta() {
		if (Peli.huone != null && Peli.huone.annaTaustanPolku() != null) {
			tausta.render(Peli.huone.annaTaustanPolku(), camera, window);
		}
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
						PullonPalautusIkkuna.tarkistaTila();
						PullonPalautusIkkuna.renderöiIkkuna(window);
					}
					case VALINTADIALOGI -> {
						DialogiValintaIkkuna.renderöiKehys(window);
						DialogiValintaIkkuna.renderöiValinnat(window);
					}
					case ÄMPÄRIJONO -> {
						ÄmpäriJonoIkkuna.tarkistaTila();
						ÄmpäriJonoIkkuna.renderöiIkkuna(window);
					}
					case KARTTA -> {
						KarttaIkkuna.renderöiIkkuna(window);
					}
					case OHJEET -> {
						OhjeIkkuna.renderöiIkkuna(window);
					}
					case MINIPELI -> {
						//MinipeliIkkuna.renderöiKehys(window);
						//MinipeliIkkuna.renderöiIkkuna(window, camera);
						//Maailma3D.render(window);
					}
				}
			}
		}

		if (Peli.huone != null) {
			if (Peli.huone.annaNimi().startsWith("Kauppa")) {
				OstosLista.render(shader, window);
			}
		}
	}

	private void renderöiLatausRuutu() {
		LatausRuutu.renderöiLatausRuutu(window);
	}

	public void peliLoop() {
		if (Peli.peliKäynnissä) {
			if (!Peli.pause && !Peli.pauseDialogi) {
				Peli.pelaajanLiike();
				Peli.pelinKulku();
				Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
				camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
				if (Kamera.päivitäZoom) camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX * Kamera.zoomKerroin, Kamera.zoomX * Kamera.zoomKerroin, -Kamera.zoomY * Kamera.zoomKerroin, Kamera.zoomY * Kamera.zoomKerroin));
				PeliKenttäMetodit.suoritaPelikenttäMetoditJokaTick();
				if (Peli.globaaliTickit % 2 == 0) {
					PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2Tick();
				}
				if (Peli.globaaliTickit % 10 == 0) {
					PeliKenttäMetodit.suoritaPelikenttäMetoditJoka10Tick();
				}
				if (Peli.globaaliTickit % 60 == 0) {
					PeliKenttäMetodit.suoritaPelikenttäMetoditJoka60Tick();
				}
				if (Peli.globaaliTickit % 2000 == 0) {
					PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2000Tick();
				}
				if (Peli.globaaliTickit % 100 == 0) {
					PeliKenttäMetodit.suoritaPelikenttäMetoditJoka100Tick();
				}
				if (Peli.globaaliTickit % 600 == 0) {
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJoka600Tick();
                }
			}
			if (Peli.globaaliTickit % 2 == 0) {
				Dialogit.scrollaaDialogiTeksti();
			}
			if (Peli.globaaliTickit % 5 == 0) {
				// String muuttuvaMerkki = "" + AsetusIkkuna.ikkunaTeksti.charAt(0);
				// AsetusIkkuna.ikkunaTeksti = AsetusIkkuna.ikkunaTeksti.substring(1) + muuttuvaMerkki;
				// if (AsetusIkkuna.ikkuna != null) {
				//     AsetusIkkuna.ikkuna.setTitle(AsetusIkkuna.ikkunaTeksti);
				// }
			}
		}
        if (!Peli.pause) {
            Peli.globaaliTickit++;
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
                    Peli.pelikenttä = Peli.huone.annaHuoneenKenttäSisältö();
                    Peli.maastokenttä = Peli.huone.annaHuoneenMaastoSisältö();
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
                            Peli.huoneKartta.get(huoneenId).tarinaRuudunTunniste = null;
                        }
                        else {
                            Dialogit.avaaDialogi("", "Tarinapätkää " + Peli.huoneKartta.get(huoneenId).annaTarinaRuudunTunniste() + " ei löytynyt", "Virhe!");
                        }
                    }
                    if (!toistaMusaTarinanJälkeen) {
                        String musa = Peli.huone.annaHuoneenMusa();
                        ÄänentoistamisSäie.toistaPeliMusa(musa);
                    }
                    else {
                        ÄänentoistamisSäie.toistaPeliMusa(null);
                    }
					häivytäPeliRuutuSisään();
                }
                else {
					Dialogit.haeTavoiteVinkkiTeksti(Peli.huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste());
                }
            }
            else {
				if (!window.isFullscreen()) JOptionPane.showMessageDialog(null, "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
				Peli.voiWarpataVasen = false;
				Peli.voiWarpataOikea = false;
				Peli.voiWarpataAlas = false;
				Peli.voiWarpataYlös = false;
            }
            
        }
        catch (NullPointerException e) {
			if (!window.isFullscreen()) JOptionPane.showMessageDialog(null, "Ongelma ladatessa huonetta " + huoneenId + ". Tämä voi johtua viallisesta default.kst-tiedostosta.", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
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

	private static void editoriInit() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			ObjektiKuvakkeet.luoObjektiKuvakkeet();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void editoriLoop() {
		HuoneEditoriIkkuna.käynnistäEditori();
		while (true) {
			try {
				HuoneEditoriIkkuna.päivitäEditoriIkkuna();
				Thread.sleep(50);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}
