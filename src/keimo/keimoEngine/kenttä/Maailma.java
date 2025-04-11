package keimo.keimoEngine.kenttä;

import keimo.Huone;
import keimo.Pelaaja;
import keimo.Peli;
import keimo.PelinAsetukset;
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
    public static ArrayList<Tile> tilet = new ArrayList<>();
    public static ArrayList<KenttäKohde> kenttäObjektit = new ArrayList<>();
    public static ArrayList<Entity> entityt = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    public static AABB[][] boundingBoxes;
    private static Shader objektiShader = new Shader("shader");
    public static Shader objekti3dShader = new Shader("shader");
    public static Shader esineShader = new Shader("shader");
    private static Shader kiintopisteShader = new Shader("shader");
    private static Shader tileShader = new Shader("shader");
    private static Shader entityShader = new Shader("shader");
    private Matrix4f world;
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
        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }

    public void createWorld() {
        for (Huone huone : Peli.huoneKartta.values()) {
            boundingBoxes = new AABB[huone.annaKoko()][huone.annaKoko()];
            for (int y = 0; y < huone.annaKoko(); y++) {
                for (int x = 0; x < huone.annaKoko(); x++) {
                    if (huone.annaHuoneenMaastoSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenMaastoSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            String[] ominaisuusLista = {"kuva=" + tiedostonNimi};
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            tilet.add(new Tile(x, y, ominaisuusLista));
                        }
                    }
                    if (huone.annaHuoneenKenttäSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenKenttäSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            String objektinNimi =  ("" + tiedostonNimi.charAt(0)).toUpperCase() + tiedostonNimi.substring(1);
                            String[] ominaisuusLista = huone.annaHuoneenKenttäSisältö()[x][y].annalisäOminaisuudet();
                            kenttäObjektit.add(KenttäKohde.luoObjektiTiedoilla(objektinNimi, true, x, y, ominaisuusLista));
                        }
                    }
                    if (huone.annaHuoneenNPCSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenNPCSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            String entityNimi =  ("" + tiedostonNimi.charAt(0)).toUpperCase() + tiedostonNimi.substring(1);
                            String[] ominaisuusLista = huone.annaHuoneenNPCSisältö()[x][y].annalisäOminaisuudet();
                            entityt.add(Entity.luoEntityTiedoilla(entityNimi, true, x, y, ominaisuusLista));
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
				if (!tileTextures.containsKey(Maailma.tilet.get(i).annaTekstuuri())) {
					String tex = Maailma.tilet.get(i).annaTekstuuri();
					tileTextures.put(tex, new Tekstuuri("tiedostot/kuvat/maasto/" + tex + ".png"));
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
            lookAtMatrix.translate((float)(-2*Pelaaja.hitbox.getMinX()/64d), (float)(2*Pelaaja.hitbox.getMinY()/64d), 0);
            Matrix4f cameraMatrix = perspectiveMatrix.mul(lookAtMatrix);
            cameraMatrix = KenttäShaderEfektit.känniEfekti(cameraMatrix);

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
        viewX = (int)(window.getWidth()/64f * PelinAsetukset.zoom) +2;
		viewY = (int)(window.getHeight()/64f * PelinAsetukset.zoom) +4;
	}

    public void correctCamera(Kamera camera, Window window) {
        if (Peli.huone != null) {
            Vector3f pos = camera.getPosition();
            int w = Peli.huone.annaKoko() * scale * 2;
            int h = Peli.huone.annaKoko() * scale * 2;
            if (pos.x < -(window.getWidth()/2)-scale*Peli.huone.annaKoko()) {
                pos.x = -(window.getWidth()/2)-scale*Peli.huone.annaKoko();
            }
            if (pos.x > -(window.getWidth()/2)+scale + 64) {
                pos.x = -(window.getWidth()/2)+scale + 64;
            }
            if (pos.y < (window.getHeight()/2)+scale + 64) {
                pos.y = (window.getHeight()/2)+scale + 64;
            }
            if (pos.y > (window.getHeight()/2)+scale*Peli.huone.annaKoko()) {
                pos.y = (window.getHeight()/2)+scale*Peli.huone.annaKoko();
            }
        }
    }

    protected void renderöiTile(Tile tile, int x, int y, int z, Matrix4f cameraMatrix) {
		if (tileTextures.containsKey(tile.annaTekstuuri())) tileTextures.get(tile.annaTekstuuri()).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);

        //tile.transform.getRotation().rotateAxis((float)Math.toRadians(1), 0, 1, 0);
        //resultMatrix.mul(tile.transform.getTransformation());
        
        tileShader.bind();
		tileShader.setUniform("sampler", 0);
		tileShader.setUniform("projection", resultMatrix);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(tile.annaKuvanKääntö(), tile.annaKuvanPeilausX(), tile.annaKuvanPeilausY());
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
        //objektinSijainti.translate(KenttäShaderEfektit.känniOffsetX, KenttäShaderEfektit.känniOffsetY, KenttäShaderEfektit.känniOffsetZ);
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
