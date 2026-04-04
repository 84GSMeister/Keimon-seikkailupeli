package keimo.seikkailupeli.kenttä;

import keimo.keimoengine.collision.AABB;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.shaderit.EfektiShader;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.grafiikat.shaderit.TestiShader;
import keimo.keimoengine.grafiikat.shaderit.TrippiShader;
import keimo.keimoengine.grafiikat.shaderit.VäriliukuShader;
import keimo.keimoengine.grafiikat.shaderit.VärinvaihtoShader;
import keimo.keimoengine.grafiikat.shaderit.VärinvaihtoShaderKuu;
import keimo.keimoengine.ikkuna.*;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Käännettävä.Suunta;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.entityt.npc.Boss;
import keimo.seikkailupeli.objektit.entityt.npc.NPC;
import keimo.seikkailupeli.objektit.entityt.npc.Vihollinen;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.VisuaalinenObjekti;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä.Kerättävä;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Kiintopiste;
import keimo.seikkailupeli.objektit.maastot.IsoLaatta;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.objektit.maastot.Tile;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maailma {
    private static int viewX;
	private static int viewY;
    public static ArrayList<Maasto> tilet = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    public static AABB[][] boundingBoxes;
    //private static Shader objektiShader = new Shader("shader");
    //public static Shader objekti3dShader = new Shader("shader");
    //public static Shader esineShader = new Shader("shader");
    private static Shader kiintopisteShader = Assets.annaShader("kiintopiste");
    //private static Shader tileShader = new Shader("shader");
    //private static Shader entityShader = new Shader("shader");
    //private static Shader erikoisEfektiShader = new Shader("shader");

    private static Shader vakioShader = Assets.annaShader("vakio");
    private static Shader värinvaihtoShaderBaari = Assets.annaShader("värinvaihto");
    private static Shader värinvaihtoShaderBaariSala = Assets.annaShader("värinvaihto2");
    private static Shader trippiShader = Assets.annaShader("trippi");
    private static Shader väriliukuShader = Assets.annaShader("väriliuku");
    private static Shader kuuShader = Assets.annaShader("kuu");
    private static Shader testiShader = Assets.annaShader("testi");

    static Tausta tausta;
    private static int scale = 32;
    public static int tileMäärä, objektiMäärä, entityMäärä;
    public static float rotZ = 0;

	private static HashMap<String, Tekstuuri> tileTextures = new HashMap<>();
	private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
    private static Tekstuuri entityHpPalkkiPunainenTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/palkki_punainen.png");
    private static Tekstuuri entityHpPalkkiVihreäTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/palkki_vihreä.png");
	public static float fade = 0f;

    public static void createWorld() {
        for (Huone huone : Peli.huoneKartta.values()) {
            boundingBoxes = new AABB[huone.annaKoko()][huone.annaKoko()];
            for (int y = 0; y < huone.annaKoko(); y++) {
                for (int x = 0; x < huone.annaKoko(); x++) {
                    if (huone.annaHuoneenMaastoSisältö()[x][y] != null) {
                        Maasto m = huone.annaHuoneenMaastoSisältö()[x][y];
                        String tiedostonNimi = m.annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            ArrayList<String> ominaisuusLista = new ArrayList<>();
                            ominaisuusLista.add("kuva=" + tiedostonNimi);
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            if (m instanceof Tile) tilet.add(Maasto.luoMaastoTiedoilla("Tile", x, y, ominaisuusLista));
                            else if (m instanceof IsoLaatta) tilet.add(Maasto.luoMaastoTiedoilla("IsoLaatta", x, y, ominaisuusLista));
                        }
                    }
                }
            }
            if (huone.annaTaustanPolku() != null && huone.annaTaustanPolku() != "") {
                taustakuvat.add(huone.annaTaustanPolku());
            }
        }

        for (int i = 0; i < Maailma.tilet.size(); i++) {
			if (Maailma.tilet.get(i) != null) {
				Maasto m = Maailma.tilet.get(i);
                if (!tileTextures.containsKey(m.annaTekstuurinNimi())) {
					String tex = Maailma.tilet.get(i).annaTekstuurinNimi();
					if (m instanceof Tile) tileTextures.put(tex, new Tekstuuri("tiedostot/kuvat/maasto/" + tex + ".png"));
                    else if (m instanceof IsoLaatta) tileTextures.put(tex, new Tekstuuri("tiedostot/kuvat/maasto/isot_laatat/" + tex + ".png"));
				}
			}
		}
		for (String s : Maailma.taustakuvat) {
			try {
				String taustanNimi = s.substring(0, s.length()-4);
				Tausta.taustaTekstuurit.put(taustanNimi, new Tekstuuri("tiedostot/kuvat/taustat/" + s));
			}
			catch (StringIndexOutOfBoundsException sioobe) {
				System.out.println("Virheellinen tausta: " + s);
				sioobe.printStackTrace();
			}
		}
        tausta = new Tausta();
    }

    public void cleanup() {
        tileTextures.values().forEach(Tekstuuri::cleanup);
    }

    public static void render(Kamera camera, Ikkuna window) {
        try {
            tileMäärä = 0; objektiMäärä = 0; entityMäärä = 0;
            int posX = ((int)camera.getPosition().x / (scale * 2));
            int posY = ((int)camera.getPosition().y / (scale * 2));
            Matrix4f cameraMatrix = camera.getPerspectiveView(window, PelinAsetukset.zoom);
            cameraMatrix = KenttäShaderEfektit.känniEfektiRotaatio(cameraMatrix);
            if (PelinAsetukset.vapaaKamera) cameraMatrix = asetaKameranSijaintiVapaa(cameraMatrix, window);
            else cameraMatrix = asetaKameranSijainti(cameraMatrix, window);
            cameraMatrix = KenttäShaderEfektit.känniEfekti(cameraMatrix);

            renderöiTausta(0, 0, 1, new Matrix4f(), fade);
            
            if (Peli.huone != null) {
                // Shaderin vois ehkä valita muuten kuin huoneen nimen perusteella.
                Shader shader = valitseShader(Peli.huone.annaNimi());
                Shader kokoRuutuEfektiShader = valitseKokoRuutuEfektiShader(Peli.huone.annaNimi());
                shader.bind();
                shader.loop();
                int etäisyys = laskeIsonLaatanNäköetäisyys();
                for (int y = 0; y < etäisyys; y++) {
                    for (int x = 0; x < etäisyys; x++) {
                        int renderX = x-posX-etäisyys/2 +1;
                        int renderY = y+posY-etäisyys/2 +1;
                        int maxX = Peli.annaObjektiKenttä().length;
                        int maxY = Peli.annaObjektiKenttä().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            Maasto m = Peli.annaMaastoKenttä()[renderX][renderY];
                            if (m instanceof IsoLaatta) {
                                IsoLaatta l = (IsoLaatta)m;
                                if (l != null) {
                                    renderöiIsoLaatta(l, renderX, -renderY, 0, cameraMatrix, shader);
                                    tileMäärä++;
                                }
                            }
                        }
                    }
                }
                for (int y = 0; y < viewY; y++) {
                    for (int x = 0; x < viewX; x++) {
                        int renderX = x-posX-viewX/2 +1;
                        int renderY = y+posY-viewY/2 +1;
                        int maxX = Peli.annaMaastoKenttä().length;
                        int maxY = Peli.annaMaastoKenttä().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            Maasto m = Peli.annaMaastoKenttä()[renderX][renderY];
                            if (m instanceof Tile) {
                                Tile t = (Tile)m;
                                if (t != null) {
                                    renderöiTile(t, renderX, -renderY, 0, cameraMatrix, shader);
                                    tileMäärä++;
                                }
                            }
                        }
                    }
                }
                synchronized (Peli.entityLista) {
                    for (Entity e : Peli.entityLista) {
                        if (e != null) {
                            renderöiEntity(e, (int)e.hitbox.getMinX(), (int)-e.hitbox.getMinY(), 0, cameraMatrix, shader);
                            entityMäärä++;
                        }
                    }
                }
                for (int y = 0; y < viewY; y++) {
                    for (int x = 0; x < viewX; x++) {
                        int renderX = x-posX-viewX/2 +1;
                        int renderY = y+posY-viewY/2 +1;
                        int maxX = Peli.annaObjektiKenttä().length;
                        int maxY = Peli.annaObjektiKenttä().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            KenttäKohde k = Peli.annaObjektiKenttä()[renderX][renderY];
                            if (k != null) {
                                if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, renderX, -renderY, 1, cameraMatrix, shader);
                                else renderöiKenttäObjekti(k, renderX, -renderY, 1, cameraMatrix, shader);
                                objektiMäärä++;
                            }
                        }
                    }
                }
                KenttäShaderEfektit.luoErikoisEfektit();
                KenttäShaderEfektit.luoKenttäVäriEfekti();
                KenttäShaderEfektit.renderöiKenttäVäriEfekti(vakioShader);
                //KenttäShaderEfektit.renderöiKenttäVäriEfekti(objekti3dShader);
                //KenttäShaderEfektit.renderöiKenttäVäriEfekti(esineShader);
                //KenttäShaderEfektit.renderöiKenttäVäriEfekti(tileShader);
                //KenttäShaderEfektit.renderöiKenttäVäriEfekti(entityShader);
                KenttäShaderEfektit.renderöiKenttäVäriEfekti(kiintopisteShader);
                KenttäShaderEfektit.kimmellysEfekti(kiintopisteShader);

                // Shaderien testaukseen
                //renderöiShaderEfekti();

                for (int y = 0; y < viewY; y++) {
                    for (int x = 0; x < viewX; x++) {
                        int renderX = x-posX-viewX/2 +1;
                        int renderY = y+posY-viewY/2 +1;
                        int maxX = Peli.annaMaastoKenttä().length;
                        int maxY = Peli.annaMaastoKenttä().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            KenttäShaderEfektit.renderöiErikoisEfektit(vakioShader, renderX, -renderY, 1, cameraMatrix);
                        }
                    }
                }
                if (kokoRuutuEfektiShader instanceof EfektiShader) {
                    EfektiShader efektiShader = (EfektiShader)kokoRuutuEfektiShader;
                    efektiShader.bind();
                    efektiShader.asetaFade(fade);
                    efektiShader.loop();
                    efektiShader.asetaSijainti(new Matrix4f());
                    shaderPohjaTekstuuri.bind(0);
                    Assets.getModel().render();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    static Tekstuuri shaderPohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/shader_kerros.png");

    public static void laskeNäköetäisyys(Ikkuna window) {
        viewX = (int)(window.getWidth()/64f * PelinAsetukset.zoom) +4;
		viewY = (int)(window.getHeight()/64f * PelinAsetukset.zoom) +6;
	}

    private static int laskeIsonLaatanNäköetäisyys() {
        return Peli.annaMaastoKenttä().length;
    }

    private static Matrix4f asetaKameranSijainti(Matrix4f cameraMatrix, Ikkuna window) {
        Matrix4f kameranSijainti = new Matrix4f(cameraMatrix);
        int offsetX = window.getWidth()/2 - (int)Pelaaja.hitbox.getWidth();
        int offsetY = window.getHeight()/2 - (int)Pelaaja.hitbox.getHeight();
        int kameranSijaintiX = 0;
        int kameranSijaintiY = 0;
        if (Pelaaja.hitbox.getMinX() < offsetX) {
            kameranSijaintiX = offsetX;
        }
        else if (Peli.huone.annaKoko() * 64 - Pelaaja.hitbox.getMaxX() < offsetX) {
            kameranSijaintiX = Peli.huone.annaKoko() * 64 - offsetX - 64;
        }
        else {
            kameranSijaintiX = (int)Pelaaja.hitbox.getMinX();
        }
        if (Pelaaja.hitbox.getMinY() < offsetY) {
            kameranSijaintiY = offsetY;
        }
        else if (Peli.huone.annaKoko() * 64 - Pelaaja.hitbox.getMaxY() < offsetY) {
            kameranSijaintiY = Peli.huone.annaKoko() * 64 - offsetY - 64;
        }
        else {
            kameranSijaintiY = (int)Pelaaja.hitbox.getMinY();
        }
        
        kameranSijainti.translate((float)(-2*kameranSijaintiX/64d), (float)(2*kameranSijaintiY/64d), 0);
        return kameranSijainti;
    }

    private static Matrix4f asetaKameranSijaintiVapaa(Matrix4f cameraMatrix, Ikkuna window) {
        Matrix4f kameranSijainti = new Matrix4f(cameraMatrix);
        kameranSijainti.translate((float)(-2*Pelaaja.hitbox.getMinX()/64d), (float)(2*Pelaaja.hitbox.getMinY()/64d), 0);
        return kameranSijainti;
    }

    private static void renderöiTausta(int x, int y, int z, Matrix4f cameraMatrix, float fade) {
		if (Peli.huone != null && Peli.huone.annaTaustanPolku() != null) {
            tausta.render(Peli.huone.annaTaustanPolku(), x, y, z, cameraMatrix, fade);
		}
	}

    protected static void renderöiTile(Tile tile, int x, int y, int z, Matrix4f cameraMatrix, Shader shader) {
		if (tileTextures.containsKey(tile.annaTekstuurinNimi())) tileTextures.get(tile.annaTekstuurinNimi()).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        shader.bind();
		shader.asetaSampler(0);
        //shader.asetaKamera(cameraMatrix);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(tile.annaKääntöAsteet(), tile.annaXPeilaus(), tile.annaYPeilaus());
		model.render();
	}

    protected static void renderöiIsoLaatta(IsoLaatta laatta, int x, int y, int z, Matrix4f cameraMatrix, Shader shader) {
        if (tileTextures.containsKey(laatta.annaTekstuurinNimi())) tileTextures.get(laatta.annaTekstuurinNimi()).bind(0);
		else virheTekstuuri.bind(0);

        int l = laatta.annaLeveys(), k = laatta.annaKorkeus();
        Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z)).scale(l, k, 1).translate(1f - 1f/l, -1f + 1f/k, 0);
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        shader.bind();
		shader.asetaSampler(0);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(laatta.annaKääntöAsteet(), laatta.annaXPeilaus(), laatta.annaYPeilaus());
		model.render();
	}

    protected static void renderöiEntity(Entity entity, int x, int y, int z, Matrix4f cameraMatrix, Shader shader) {
		if (entity.annaTekstuuri() != null) entity.annaTekstuuri().bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f entitynSijainti = new Matrix4f().translate(new Vector3f(x * 2f / 64f, y * 2f / 64f, z));
        entitynSijainti.scale(entity.leveys/64f, entity.korkeus/64f, 0);
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(entitynSijainti);
		
        float hurtEfekti = 0f;
        if (entity instanceof Vihollinen) {
            Vihollinen v = (Vihollinen)entity;
            if (v.annaHurtAika() > 0) hurtEfekti = 255f;
            else hurtEfekti = 0f;
        }

        shader.bind();
		shader.asetaSampler(0);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        shader.setUniform("addcolor", new Vector4f(hurtEfekti, hurtEfekti, hurtEfekti, 0));
		
		Model model;
		if (entity instanceof Boss) model = Assets.getModel(entity.suuntaVasenOikea);
        else model = Assets.getModel(entity.annaSuunta());
		model.render();

        if (entity instanceof NPC) {
            NPC npc = (NPC)entity;
            if (npc.maxHp > 0) {
                resultMatrix.translate(0, -1f, 0);
                resultMatrix.scale(1, 0.0625f, 1);
                shader.asetaSijainti(resultMatrix);
                entityHpPalkkiPunainenTekstuuri.bind(0);
                Assets.getModel().render();

                float offsetX = (float)npc.hp/(float)npc.maxHp;
                resultMatrix.scale(offsetX, 1, 1);
                resultMatrix.translate(-(1f - offsetX)*2, 0, 0);
                shader.asetaSijainti(resultMatrix);
                entityHpPalkkiVihreäTekstuuri.bind(0);
                Assets.getModel().render();
            }
        }
	}

	protected static void renderöiKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix, Shader shader) {
		if (objekti.onkoKolmiUlotteinen()) {
            renderöi3dKenttäObjekti(objekti, x, y, z, cameraMatrix, shader);
        }
        else {
            if (objekti instanceof Esine || objekti instanceof Kerättävä) {
                renderöiEsinePyörivä(objekti, x, y, z, cameraMatrix, shader);
            }
            else if (objekti instanceof Kiintopiste || objekti instanceof NPC_KenttäKohde) {
                renderöiKiintopisteKiiluva(objekti, x, y, 0, cameraMatrix, kiintopisteShader);
            }
            else {
                renderöiKenttäkohdeStaattinen(objekti, x, y, 0, cameraMatrix, shader);
            }
            Model model = Assets.getModel(objekti.annaKääntöAsteet(), objekti.annaXPeilaus(), objekti.annaYPeilaus());
		    model.render();
        }
	}

    protected static void renderöiEsinePyörivä(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix, Shader shader) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objekti.liikeY += objekti.annaLiikeNopeus();
        objekti.transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        objekti.transform.getPosition().set(0, -2f - (float)(4*Math.sin(Math.toRadians(objekti.annaLiikeY()))), 0);
        resultMatrix.mul(objekti.transform.getTransformation());

        shader.bind();
		shader.asetaSampler(0);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected static void renderöiKiintopisteKiiluva(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix, Shader shader) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        shader.bind();
		shader.asetaSampler(0);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected static void renderöiKenttäkohdeStaattinen(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix, Shader shader) {
        if (objekti instanceof VisuaalinenObjekti) ErikoisTileMuutokset.annaSpesiaaliTekstuuri(objekti.annaTekstuuri(), objekti.annaKuvanTiedostoNimi(), (int)x, (int)y).bind(0);
        else if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        shader.bind();
		shader.asetaSampler( 0);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected static void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix, Shader shader) {
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objekti.liikeY += objekti.annaLiikeNopeus();
        objekti.transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        resultMatrix.mul(objekti.transform.getTransformation());

        shader.bind();
		shader.asetaSampler(0);
		shader.asetaSijainti(resultMatrix);
        shader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        Assets.getModel3D(objekti.anna3dMallinTunniste()).draw();
    }

    protected static void renderöiShaderEfekti() {
        Shader shader = valitseShader(Peli.huone.annaNimi());
        shader.bind();
        shader.loop();
        shader.asetaSijainti(new Matrix4f());
        Assets.annaTekstuuri("menu_osoitin").bind(0);
        Assets.getModel().render();
    }

    private static Shader valitseShader(String kenttä) {
        switch (kenttä) {
            default -> {
                return vakioShader;
            }
            case "Keimo-baari" -> {
                return värinvaihtoShaderBaari;
            }
            case "Baari_salahuone" -> {
                return värinvaihtoShaderBaariSala;
            }
            case "Kuu" -> {
                return kuuShader;
            }
            case "Metsä_kalja" -> {
                return trippiShader;
            }
        }
    }

    private static Shader valitseKokoRuutuEfektiShader(String kenttä) {
        switch (kenttä) {
            default -> {
                return vakioShader;
            }
            case "Baari_salahuone" -> {
                return testiShader;
            }
            case "Metsä_boss" -> {
                return väriliukuShader;
            }
        }
    }

    public enum Liike {
        VASEN,
        OIKEA,
        YLÖS,
        ALAS,
    }

    public static boolean liikuYlös = false;
    public static boolean liikuAlas = false;
    public static boolean liikuVasemmalle = false;
    public static boolean liikuOikealle = false;

    public static void liiku(Liike liike) {
        switch (liike) {
            case YLÖS:
                liikuYlös = true;
            break;
            case ALAS:
                liikuAlas = true;
            break;
            case VASEN:
                liikuVasemmalle = true;
            break;
            case OIKEA:
                liikuOikealle = true;
            break;
            case null, default:
            break;
        }
    }

    public static void lopetaLiike(Liike liike) {
        switch (liike) {
            case YLÖS:
                liikuYlös = false;
            break;
            case ALAS:
                liikuAlas = false;
            break;
            case VASEN:
                liikuVasemmalle = false;
            break;
            case OIKEA:
                liikuOikealle = false;
            break;
            case null, default:
                liikuYlös = false;
                liikuAlas = false;
                liikuVasemmalle = false;
                liikuOikealle = false;
            break;
        }
    }

    public static void liikutaPelaajaa() {
        if (liikuYlös & liikuVasemmalle) {
            Pelaaja.kokeileLiikkumista(Suunta.YLÄVASEN);
        }
        else if (liikuAlas & liikuVasemmalle) {
            Pelaaja.kokeileLiikkumista(Suunta.ALAVASEN);
        }
        else if (liikuYlös & liikuOikealle) {
            Pelaaja.kokeileLiikkumista(Suunta.YLÄOIKEA);
        }
        else if (liikuAlas & liikuOikealle) {
            Pelaaja.kokeileLiikkumista(Suunta.ALAOIKEA);
        }
        else {
            if (liikuYlös) {
                Pelaaja.kokeileLiikkumista(Suunta.YLÖS);
            }
            if (liikuAlas) {
                Pelaaja.kokeileLiikkumista(Suunta.ALAS);
            }
            if (liikuVasemmalle) {
                Pelaaja.kokeileLiikkumista(Suunta.VASEN);
            }
            if (liikuOikealle) {
                Pelaaja.kokeileLiikkumista(Suunta.OIKEA);
            }
        }
    }
}
