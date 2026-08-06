package keimo.seikkailupeli.ruudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.grafiikat.shaderit.TestiShader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.PelaajaModel;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.gui.hud.DebugTeksti;
import keimo.seikkailupeli.gui.hud.HUD;
import keimo.seikkailupeli.gui.hud.OstosLista;
import keimo.seikkailupeli.gui.hud.TavoitePopup;
import keimo.seikkailupeli.gui.toimintoIkkunat.DialogiValintaIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.HuijauskoodiValikko;
import keimo.seikkailupeli.gui.toimintoIkkunat.KarttaIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.OhjeIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.PullonPalautusIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.YhdistämisIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.ÄmpäriJonoIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaKeimoäly;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaOverflow;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPokeri;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPong;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaTetris;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.minipeli3d.MinipeliIkkuna3D;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.kenttä.Tausta;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.entityt.npc.NPC;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.seikkailupeli.äänet.Musat;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class PeliRuutu {

    static PelaajaModel player;
    static Ikkuna ikkuna;
    static Kamera kamera;
    static double hudAika = 0;
    static boolean häivytäPeliruutuUlos = false;
    static boolean häivytäPeliruutuSisään = true;
    static Shader vakioShader;
    static TestiShader shaderVäri = new TestiShader();

    public static void alusta(Ikkuna ikkuna1, Kamera kamera1, Shader shader1) {
        ikkuna = ikkuna1;
        kamera = kamera1;
        vakioShader = shader1;
        player = new PelaajaModel();
    }
    
    public static void renderöi(Shader shader, Kamera camera, Ikkuna window) {
        if (häivytäPeliruutuUlos) {
            häivytäPeliRuutuUlos(shader, window, camera);
        }
        else if (häivytäPeliruutuSisään) {
            häivytäPeliRuutuSisään(shader, window, camera);
        }
        else {
            renderöiPeliruudunOsat(shader, camera, window);
        }
    }

    private static void renderöiPeliruudunOsat(Shader shader, Kamera camera, Ikkuna window) {
        camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
		if (Kamera.päivitäZoom) camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX * Kamera.zoomKerroin, Kamera.zoomX * Kamera.zoomKerroin, -Kamera.zoomY * Kamera.zoomKerroin, Kamera.zoomY * Kamera.zoomKerroin));

        double alkuAika = System.nanoTime();
        KeimoEngine.kaatoTeksti.bind(0);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        Maailma.render(camera, window);
        double tileAika = System.nanoTime() - alkuAika;
        glDisable(GL_DEPTH_TEST);
        
        player.update((float)KeimoEngine.tavoitePäivitysAika, window, camera);
        player.render(camera, window);
        double pelaajaAika = System.nanoTime() - alkuAika - tileAika;
        
        renderöiHUD(shader, window, camera);
        
        if (PelinAsetukset.debugTiedot) {
            DebugTeksti.renderöiDebugTeksti(tileAika, pelaajaAika, hudAika, window);
        }
        DebugTeksti.renderöiLisäMoodiTekstit(window);
        hudAika = System.nanoTime() - alkuAika - tileAika - pelaajaAika;
    }

    private static void renderöiHUD(Shader shader, Ikkuna window, Kamera camera) {
		HUD.renderöiHUD(window);
		TavoitePopup.renderöiTavoitePopup(window);
		
		switch (Peli.syötteenTila) {
			case PELI -> {
                
            }
			case DIALOGI -> {
				HUD.renderöiDialogiLaatikko(shader, window);
                if (KeimoEngine.frames % 2 == 0) {
                    Dialogit.scrollaaDialogiTeksti();
                }
			}
			case TOIMINTO -> {
				switch (Peli.toimintoIkkuna) {
					case PULLONPALAUTUS -> {
						PullonPalautusIkkuna.simuloiPullonpalautus();
						PullonPalautusIkkuna.tarkistaTila();
						PullonPalautusIkkuna.renderöiIkkuna(shader, window);
					}
					case VALINTADIALOGI -> {
						DialogiValintaIkkuna.renderöi(shader, window);
					}
					case ÄMPÄRIJONO -> {
						ÄmpäriJonoIkkuna.simuloiÄmpärijono();
						ÄmpäriJonoIkkuna.tarkistaTila();
						ÄmpäriJonoIkkuna.renderöiIkkuna(shader, window);
					}
					case KARTTA -> {
						KarttaIkkuna.renderöiIkkuna(shader, window);
					}
					case OHJEET -> {
						OhjeIkkuna.renderöiIkkuna(shader, window);
					}
                    case YHDISTÄMINEN -> {
                        YhdistämisIkkuna.renderöi(shader, window);
                    }
					case HUIJAUSKOODIT -> {
						HuijauskoodiValikko.renderöi(shader, window);
					}
					case MINIPELI_3D -> {
                        MinipeliIkkuna3D.renderöi(window, camera, shader);
					}
					case MINIPELI_PONG -> {
						MinipeliIkkunaPong.renderöiKehys(window, shader);
						MinipeliIkkunaPong.renderöiIkkuna(window, shader);
					}
					case MINIPELI_POKERI -> {
						MinipeliIkkunaPokeri.renderöiKehys(window, shader);
						MinipeliIkkunaPokeri.renderöiIkkuna(window, shader);
					}
					case MINIPELI_TETRIS -> {
						MinipeliIkkunaTetris.renderöiKehys(window, shader);
						MinipeliIkkunaTetris.renderöiIkkuna(window, shader);
					}
					case MINIPELI_4 -> {
						MinipeliIkkunaOverflow.renderöiKehys(window, shader);
						MinipeliIkkunaOverflow.renderöiIkkuna(window, camera, shader);
					}
                    case MINIPELI_KEIMOÄLY -> {
                        MinipeliIkkunaKeimoäly.renderöiKehys(window, shader);
                        MinipeliIkkunaKeimoäly.renderöiIkkuna(window, shader);
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

    /**
     * "Siirry valittuun huoneeseen" eli
     * Lataa pelin nykyiseksi kentäksi huonekartasta (HashMapista) valittu Huone-objekti
     * @param huoneenId ladattavan huoneen ID huonekartassa
	 * @param pelaajanX X-koordinaatti, johon pelaaja siirretään (Tile)
	 * @param pelaajanY Y-koordinaatti, johon pelaaja siirretään (Tile)
     * @param debug estä tavoitteen tarkistus ja tarinan lataus
     */
	public static void lataaHuone(int huoneenId, int pelaajanX, int pelaajanY, boolean debug) {
        try {
            if (Peli.annaHuoneKartta().get(huoneenId) != null) {
                if ((!Peli.annaHuoneKartta().get(huoneenId).annaTavoiteVaatimus() || TavoiteLista.tavoiteLista.get(Peli.annaHuoneKartta().get(huoneenId).annaVaaditunTavoitteenTunniste())) || debug || Pelaaja.ohitaTavoitteet) {
                    if (Peli.huone != null) {
                        if (!Peli.huone.annaTaustanPolku().equals(Peli.annaHuoneKartta().get(huoneenId).annaTaustanPolku())) Tausta.häivytäTausta = true;
						else Tausta.häivytäTausta = false;
                        häivytäPeliRuutuUlos(vakioShader, ikkuna, kamera);
					}
					Peli.huone = Peli.annaHuoneKartta().get(huoneenId);
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
                    if (Peli.annaHuoneKartta().get(huoneenId).annaTarinaRuudunLataus() && !debug) {
                        if (Peli.peliTiedosto.annaTarinaKartta().containsKey(Peli.annaHuoneKartta().get(huoneenId).annaTarinaRuudunTunniste())) {
                            Peli.pause = true;
                            toistaMusaTarinanJälkeen = true;
                            Peli.siirryTarinaRuutuun(Peli.annaHuoneKartta().get(huoneenId).annaTarinaRuudunTunniste());
                            Peli.annaHuoneKartta().get(huoneenId).päivitäAlkudialogi(null);
                        }
                        else {
                            Dialogit.avaaDialogi("Tarinapätkää " + Peli.annaHuoneKartta().get(huoneenId).annaTarinaRuudunTunniste() + " ei löytynyt", "Virhe!");
                            Peli.nollaaPainallukset();
                        }
                    }
                    else {
                        häivytäPeliRuutuSisään(vakioShader, ikkuna, kamera);
                    }
                    if (!toistaMusaTarinanJälkeen) {
                        String musa = Peli.huone.annaHuoneenMusa();
                        Musat.toistaPeliMusa(musa);
                    }
                }
                else {
					Dialogit.haeTavoiteVinkkiTeksti(Peli.annaHuoneKartta().get(huoneenId).annaVaaditunTavoitteenTunniste());
                    Peli.nollaaPainallukset();
                }
            }
            else {
				if (!ikkuna.isFullscreen()) DialogiIkkunat.viestiIkkuna("Huonetta ei löytynyt.", "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "ok", "error", false);
				Peli.voiWarpataVasen = false;
				Peli.voiWarpataOikea = false;
				Peli.voiWarpataAlas = false;
				Peli.voiWarpataYlös = false;
            }
            
        }
        catch (NullPointerException e) {
			if (!ikkuna.isFullscreen()) DialogiIkkunat.viestiIkkuna("Huonetta ei löytynyt.", "Ongelma ladatessa huonetta " + huoneenId + ". Tämä voi johtua viallisesta default.kst-tiedostosta.", "ok", "error", false);
            e.printStackTrace();
			Peli.voiWarpataVasen = false;
			Peli.voiWarpataOikea = false;
			Peli.voiWarpataAlas = false;
			Peli.voiWarpataYlös = false;
        }
    }

    public static void häivytäPeliRuutuUlos(Shader shader, Ikkuna window, Kamera camera) {
        while (Maailma.fade < 1f) {
            Maailma.fade += 0.02f;
			try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                renderöiPeliruudunOsat(shader, camera, window);
                window.swapBuffers();
                KeimoEngine.frames++;
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        häivytäPeliruutuUlos = false;
	}

	public static void häivytäPeliRuutuSisään(Shader shader, Ikkuna window, Kamera camera) {
		camera.setPosition(new Vector3f(-Pelaaja.hitbox.x, Pelaaja.hitbox.y, 0));
		while (Maailma.fade > 0f) {
			Maailma.fade -= 0.02f;
			try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
                renderöiPeliruudunOsat(shader, camera, window);
                window.swapBuffers();
                KeimoEngine.frames++;
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        häivytäPeliruutuSisään = false;
	}
}
