package keimo.keimoEngine.kenttä;

import keimo.Huone;
import keimo.Pelaaja;
import keimo.Peli;
import keimo.PelinAsetukset;
import keimo.Maastot.IsoLaatta;
import keimo.Maastot.Maasto;
import keimo.Maastot.Tile;
import keimo.entityt.Entity;
import keimo.entityt.npc.Boss;
import keimo.entityt.npc.NPC;
import keimo.entityt.npc.Vihollinen;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.collision.AABB;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.ikkuna.*;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.VisuaalinenObjekti;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.kenttäkohteet.kerättävä.Kerättävä;
import keimo.kenttäkohteet.kiintopiste.Kiintopiste;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maailma {
    private int viewX;
	private int viewY;
    public static ArrayList<Maasto> tilet = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    public static AABB[][] boundingBoxes;
    private static Shader objektiShader = new Shader("shader");
    public static Shader objekti3dShader = new Shader("shader");
    public static Shader esineShader = new Shader("shader");
    private static Shader kiintopisteShader = new Shader("shader");
    private static Shader tileShader = new Shader("shader");
    private static Shader entityShader = new Shader("shader");
    private int scale = 32;
    public static int tileMäärä, objektiMäärä, entityMäärä;
    public static float rotZ = 0;

	private HashMap<String, Tekstuuri> tileTextures = new HashMap<>();
	private Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
    private Tekstuuri entityHpPalkkiPunainenTekstuuri = new Tekstuuri("tiedostot/kuvat/entity/hp_palkki_punainen.png");
    private Tekstuuri entityHpPalkkiVihreäTekstuuri = new Tekstuuri("tiedostot/kuvat/entity/hp_palkki_vihreä.png");
	public static float fade = 0f;

    public Maailma() {
        createWorld();
    }

    public void createWorld() {
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
                if (!tileTextures.containsKey(m.annaTekstuuri())) {
					String tex = Maailma.tilet.get(i).annaTekstuuri();
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
    }

    public void cleanup() {
        tileTextures.values().forEach(Tekstuuri::cleanup);
    }

    public void render(Kamera camera, Window window) {
        try {
            tileMäärä = 0; objektiMäärä = 0; entityMäärä = 0;
            int posX = ((int)camera.getPosition().x / (scale * 2));
            int posY = ((int)camera.getPosition().y / (scale * 2));
            Matrix4f perspectiveMatrix = new Matrix4f().setPerspective((float)Math.toRadians(45), window.getWidth()/window.getHeight(), 0.001f, 1000);
            perspectiveMatrix.scale(800f/window.getWidth(), 800f/window.getHeight(), 1);
            Matrix4f lookAtMatrix = new Matrix4f().setLookAt(0, 0, 32 * PelinAsetukset.zoom, 0, 0, 0, 0, 1, 0);
            lookAtMatrix = KenttäShaderEfektit.känniEfektiRotaatio(lookAtMatrix);
            //lookAtMatrtix.rotate((float)Math.toRadians(rotZ), new Vector3f(0, 0, 1));
            //lookAtMatrix = asetaKameranSijainti(lookAtMatrix, window);
            lookAtMatrix = asetaKameranSijaintiVanha(lookAtMatrix, window);
            Matrix4f cameraMatrix = perspectiveMatrix.mul(lookAtMatrix);
            cameraMatrix = KenttäShaderEfektit.känniEfekti(cameraMatrix);
            asetaKameranSijainti(cameraMatrix, window);

            int etäisyys = laskeIsonLaatanNäköetäisyys();
            for (int y = 0; y < etäisyys; y++) {
                for (int x = 0; x < etäisyys; x++) {
                    int renderX = x-posX-etäisyys/2 +1;
                    int renderY = y+posY-etäisyys/2 +1;
                    int maxX = Peli.maastokenttä.length;
                    int maxY = Peli.maastokenttä.length;
                    if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                        Maasto m = Peli.annaMaastoKenttä()[renderX][renderY];
                        if (m instanceof IsoLaatta) {
                            IsoLaatta l = (IsoLaatta)m;
                            if (l != null) {
                                renderöiIsoLaatta(l, renderX, -renderY, 0, cameraMatrix);
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
                    int maxX = Peli.maastokenttä.length;
                    int maxY = Peli.maastokenttä.length;
                    if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                        Maasto m = Peli.annaMaastoKenttä()[renderX][renderY];
                        if (m instanceof Tile) {
                            Tile t = (Tile)m;
                            if (t != null) {
                                renderöiTile(t, renderX, -renderY, 0, cameraMatrix);
                                tileMäärä++;
                            }
                        }
                    }
                }
            }
            synchronized (Peli.entityLista) {
                for (Entity e : Peli.entityLista) {
                    if (e != null) {
                        renderöiEntity(e, (int)e.hitbox.getMinX(), (int)-e.hitbox.getMinY(), 0, cameraMatrix);
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
                            if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, renderX, -renderY, 1, cameraMatrix);
                            else renderöiKenttäObjekti(k, renderX, -renderY, 1, cameraMatrix);
                            objektiMäärä++;
                        }
                    }
                }
            }
            KenttäShaderEfektit.kenttäVäriEfekti(objektiShader);
            KenttäShaderEfektit.kenttäVäriEfekti(objekti3dShader);
            KenttäShaderEfektit.kenttäVäriEfekti(kiintopisteShader);
            KenttäShaderEfektit.kenttäVäriEfekti(esineShader);
            KenttäShaderEfektit.kenttäVäriEfekti(tileShader);
            KenttäShaderEfektit.kenttäVäriEfekti(entityShader);
            KenttäShaderEfektit.kimmellysEfekti(kiintopisteShader);
        }
        catch (IndexOutOfBoundsException aioobe) {
            System.out.println("koko muuttui");
            aioobe.printStackTrace();
        }
    }

    public void laskeNäköetäisyys(Window window) {
        viewX = (int)(window.getWidth()/64f * PelinAsetukset.zoom) +4;
		viewY = (int)(window.getHeight()/64f * PelinAsetukset.zoom) +6;
	}

    private int laskeIsonLaatanNäköetäisyys() {
        return Peli.annaMaastoKenttä().length;
    }

    private Matrix4f asetaKameranSijainti(Matrix4f cameraMatrix, Window window) {
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

    private Matrix4f asetaKameranSijaintiVanha(Matrix4f cameraMatrix, Window window) {
        Matrix4f kameranSijainti = new Matrix4f(cameraMatrix);
        kameranSijainti.translate((float)(-2*Pelaaja.hitbox.getMinX()/64d), (float)(2*Pelaaja.hitbox.getMinY()/64d), 0);
        return kameranSijainti;
    }

    protected void renderöiTile(Tile tile, int x, int y, int z, Matrix4f cameraMatrix) {
		if (tileTextures.containsKey(tile.annaTekstuuri())) tileTextures.get(tile.annaTekstuuri()).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        tileShader.bind();
		tileShader.setUniform("sampler", 0);
		tileShader.setUniform("projection", resultMatrix);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(tile.annaKuvanKääntö(), tile.annaKuvanPeilausX(), tile.annaKuvanPeilausY());
		model.render();
	}

    protected void renderöiIsoLaatta(IsoLaatta laatta, int x, int y, int z, Matrix4f cameraMatrix) {
        if (tileTextures.containsKey(laatta.annaTekstuuri())) tileTextures.get(laatta.annaTekstuuri()).bind(0);
		else virheTekstuuri.bind(0);

        int l = laatta.annaLeveys(), k = laatta.annaKorkeus();
        Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z)).scale(l, k, 1).translate(1f - 1f/l, -1f + 1f/k, 0);
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        tileShader.bind();
		tileShader.setUniform("sampler", 0);
		tileShader.setUniform("projection", resultMatrix);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(laatta.annaKuvanKääntö(), laatta.annaKuvanPeilausX(), laatta.annaKuvanPeilausY());
		model.render();
	}

    protected void renderöiEntity(Entity entity, int x, int y, int z, Matrix4f cameraMatrix) {
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

        entityShader.bind();
		entityShader.setUniform("sampler", 0);
		entityShader.setUniform("projection", resultMatrix);
        entityShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        entityShader.setUniform("addcolor", new Vector4f(hurtEfekti, hurtEfekti, hurtEfekti, 0));
		
		Model model;
		if (entity instanceof Boss) model = Assets.getModel(entity.suuntaVasenOikea);
        else model = Assets.getModel(entity.suunta);
		model.render();

        if (entity instanceof NPC) {
            NPC npc = (NPC)entity;
            if (npc.maxHp > 0) {
                resultMatrix.translate(0, -1f, 0);
                resultMatrix.scale(1, 0.0625f, 1);
                entityShader.setUniform("projection", resultMatrix);
                entityHpPalkkiPunainenTekstuuri.bind(0);
                Assets.getModel().render();

                float offsetX = (float)npc.hp/(float)npc.maxHp;
                resultMatrix.scale(offsetX, 1, 1);
                resultMatrix.translate(-(1f - offsetX)*2, 0, 0);
                entityShader.setUniform("projection", resultMatrix);
                entityHpPalkkiVihreäTekstuuri.bind(0);
                Assets.getModel().render();
            }
        }
	}

	protected void renderöiKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
		if (objekti.onkoKolmiUlotteinen()) {
            renderöi3dKenttäObjekti(objekti, x, y, z, cameraMatrix);
        }
        else {
            if (objekti instanceof Esine || objekti instanceof Kerättävä) {
                renderöiEsinePyörivä(objekti, x, y, z, cameraMatrix);
            }
            else if (objekti instanceof Kiintopiste || objekti instanceof NPC_KenttäKohde) {
                renderöiKiintopisteKiiluva(objekti, x, y, 0, cameraMatrix);
            }
            else {
                renderöiKenttäkohdeStaattinen(objekti, x, y, 0, cameraMatrix);
            }
            Model model = Assets.getModel(objekti.annaKääntöAsteet(), objekti.annaXPeilaus(), objekti.annaYPeilaus());
		    model.render();
        }
	}

    protected void renderöiEsinePyörivä(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objekti.liikeY += objekti.annaLiikeNopeus();
        objekti.transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        objekti.transform.getPosition().set(0, -2f - (float)(4*Math.sin(Math.toRadians(objekti.annaLiikeY()))), 0);
        resultMatrix.mul(objekti.transform.getTransformation());

        esineShader.bind();
        esineShader.setUniform("projection", resultMatrix);
        esineShader.setUniform("sampler", 0);
        esineShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected void renderöiKiintopisteKiiluva(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        kiintopisteShader.bind();
        kiintopisteShader.setUniform("projection", resultMatrix);
        kiintopisteShader.setUniform("sampler", 0);
        kiintopisteShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected void renderöiKenttäkohdeStaattinen(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else if (objekti instanceof VisuaalinenObjekti) ErikoisTileMuutokset.annaSpesiaaliTekstuuri(objekti.annaTekstuuri(), objekti.annaKuvanTiedostoNimi()).bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objektiShader.bind();
        objektiShader.setUniform("projection", resultMatrix);
        objektiShader.setUniform("sampler", 0);
        objektiShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objekti.liikeY += objekti.annaLiikeNopeus();
        objekti.transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        resultMatrix.mul(objekti.transform.getTransformation());

        objekti3dShader.bind();
		objekti3dShader.setUniform("projection", resultMatrix);
        objekti3dShader.setUniform("sampler", 0);
        objekti3dShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        Assets.getModel3D(objekti.anna3dMallinTunniste()).draw();
    }
}
