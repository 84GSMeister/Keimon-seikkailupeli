package keimo.keimoEngine.kenttä;

import keimo.Huone;
import keimo.Peli;
import keimo.Maastot.Maasto;
import keimo.Maastot.Tile;
import keimo.entityt.Entity;
import keimo.entityt.npc.Boss;
import keimo.entityt.npc.Vihollinen;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.collision.AABB;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.*;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.VisuaalinenObjekti;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
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

	private HashMap<String, Tekstuuri> tileTextures = new HashMap<>();
	private Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
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

    public void render(Kamera camera) {
        try {
            tileMäärä = 0; objektiMäärä = 0; entityMäärä = 0;
            int posX = ((int)camera.getPosition().x / (scale * 2));
            int posY = ((int)camera.getPosition().y / (scale * 2));
            for (int y = 0; y < viewY; y++) {
                for (int x = 0; x < viewX; x++) {
                    int renderX = x-posX-viewX/2;
                    int renderY = y+posY-viewY/2;
                    int maxX = Peli.maastokenttä.length;
                    int maxY = Peli.maastokenttä.length;
                    if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                        Maasto m = Peli.annaMaastoKenttä()[renderX][renderY];
                        if (m instanceof Tile) {
                            Tile t = (Tile)m;
                            if (t != null) {
                                renderöiTile(t, renderX, -renderY, world, camera);
                                tileMäärä++;
                            }
                        }
                    }
                }
            }
            synchronized (Peli.entityLista) {
                for (Entity e : Peli.entityLista) {
                    if (e != null) {
                        renderöiEntity(e, (int)e.hitbox.getMinX(), (int)-e.hitbox.getMinY(), world, camera);
                        entityMäärä++;
                    }
                }
            }
            for (int y = 0; y < viewY; y++) {
                for (int x = 0; x < viewX; x++) {
                    int renderX = x-posX-viewX/2;
                    int renderY = y+posY-viewY/2;
                    int maxX = Peli.maastokenttä.length;
                    int maxY = Peli.maastokenttä.length;
                    if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                        KenttäKohde k = Peli.annaObjektiKenttä()[renderX][renderY];
                        if (k != null) {
                            if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, renderX, -renderY, 0, world, camera);
                            else renderöiKenttäObjekti(k, renderX, -renderY, 0, world, camera);
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

            //world = KenttäShaderEfektit.känniEfekti(tileShader, world);
        }
        catch (IndexOutOfBoundsException aioobe) {
            System.out.println("koko muuttui");
            aioobe.printStackTrace();
        }
    }

    public void calculateView(Window window, Kamera camera) {
        viewX = (window.getWidth() / (scale * 2)) + (int)(0.03f / camera.getProjection().getScale(new Vector3f()).x) - (int)(5f * ((float)window.getWidth()/(float)window.getHeight()));
		viewY = (window.getHeight() / (scale * 2)) + (int)(0.03f / camera.getProjection().getScale(new Vector3f()).y) - 5;
	}
	
	public Matrix4f getWorldMatrix() {
		return world;
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

    public int getScale() {
        return scale;
    }

    public AABB getTileBoundingBox(int x, int y) {
		try {
            if (boundingBoxes != null) {
                return boundingBoxes[x][y];
            }
            else return null;
		}
		catch (ArrayIndexOutOfBoundsException aioobe) {
			return null;
		}
	}
    protected void renderöiTile(Tile tile, int x, int y, Matrix4f world, Kamera cam) {
		tileShader.bind();
		if (tileTextures.containsKey(tile.annaTekstuuri())) tileTextures.get(tile.annaTekstuuri()).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
		tilenSijainti.scale(1, 1, 0);
		Matrix4f target = new Matrix4f();
        tilenSijainti = KenttäShaderEfektit.känniEfekti(tileShader, tilenSijainti);
		
        cam.getProjection().mul(world, target);
		target.mul(tilenSijainti);
		
		tileShader.setUniform("sampler", 0);
		tileShader.setUniform("projection", target);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(tile.annaKuvanKääntö(), tile.annaKuvanPeilausX(), tile.annaKuvanPeilausY());
		model.render();
	}

	protected void renderöiKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f world, Kamera cam) {
		if (objekti instanceof Esine) {
            esineShader.bind();
            if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
            else virheTekstuuri.bind(0);
            
            Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z * 2));
            objektinSijainti.scale(1, 1, 0);
            Matrix4f target = new Matrix4f();
            objektinSijainti = KenttäShaderEfektit.esineenKeinumisEfekti(objekti, esineShader, objektinSijainti);
            objektinSijainti = KenttäShaderEfektit.känniEfekti(esineShader, objektinSijainti);
            
            cam.getProjection().mul(world, target);
            target.mul(objektinSijainti);
            
            esineShader.setUniform("sampler", 0);
            
            esineShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
            objekti.liikeY += objekti.annaLiikeNopeus();
            Transform3D transform = objekti.transform;
            transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
            target.mul(transform.getTransformation());
            esineShader.setUniform("projection", target);
            //esineShader.setTransform(transform);
        }
        else if (objekti instanceof Kiintopiste || objekti instanceof NPC_KenttäKohde) {
            kiintopisteShader.bind();
            if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
            else virheTekstuuri.bind(0);
            
            Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z * 2));
            objektinSijainti.scale(1, 1, 0);
            Matrix4f target = new Matrix4f();
            objektinSijainti = KenttäShaderEfektit.känniEfekti(kiintopisteShader, objektinSijainti);
            
            cam.getProjection().mul(world, target);
            target.mul(objektinSijainti);
            
            kiintopisteShader.setUniform("sampler", 0);
            kiintopisteShader.setUniform("projection", target);
            kiintopisteShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        }
        else {
            objektiShader.bind();
            if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
            else if (objekti instanceof VisuaalinenObjekti) ErikoisTileMuutokset.annaSpesiaaliTekstuuri(objekti.annaTekstuuri(), objekti.annaKuvanTiedostoNimi()).bind(0);
            else virheTekstuuri.bind(0);
            
            Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
            objektinSijainti.scale(1, 1, 0);
            Matrix4f target = new Matrix4f();
            objektinSijainti = KenttäShaderEfektit.känniEfekti(objektiShader, objektinSijainti);
            
            cam.getProjection().mul(world, target);
            target.mul(objektinSijainti);
            
            objektiShader.setUniform("sampler", 0);
            objektiShader.setUniform("projection", target);
            objektiShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        }
		
		Model model = Assets.getModel(objekti.annaKääntöAsteet(), objekti.annaXPeilaus(), objekti.annaYPeilaus());
		model.render();
	}

	protected void renderöiEntity(Entity entity, int x, int y, Matrix4f world, Kamera cam) {
		entityShader.bind();
		if (entity.annaTekstuuri() != null) entity.annaTekstuuri().bind(0);
		else virheTekstuuri.bind(0);
		
		Matrix4f entitynSijainti = new Matrix4f().translate(new Vector3f(x * 2f / 64f, y * 2f / 64f, 0));
		entitynSijainti.scale(entity.leveys/64f, entity.korkeus/64f, 0);
		Matrix4f target = new Matrix4f();
        entitynSijainti = KenttäShaderEfektit.känniEfekti(entityShader, entitynSijainti);
		
		cam.getProjection().mul(world, target);
		target.mul(entitynSijainti);
		
		entityShader.setUniform("sampler", 0);
		entityShader.setUniform("projection", target);

        float hurtEfekti = 0f;
        if (entity instanceof Vihollinen) {
            Vihollinen v = (Vihollinen)entity;
            if (v.annaHurtAika() > 0) hurtEfekti = 255f;
            else hurtEfekti = 0f;
        }
        entityShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        entityShader.setUniform("addcolor", new Vector4f(hurtEfekti, hurtEfekti, hurtEfekti, 0));
		
        Model model;
		if (entity instanceof Boss) model = Assets.getModel();
        else model = Assets.getModel(entity.suunta);
		model.render();
	}

    protected void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f world, Kamera cam) {
        objekti3dShader.bind();
		Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
		objektinSijainti.scale(1, 1, 0);
		Matrix4f target = new Matrix4f();
        objektinSijainti = KenttäShaderEfektit.känniEfekti(objekti3dShader, objektinSijainti);
		
		cam.getProjection().mul(world, target);
        target.mul(objektinSijainti);
		
		objekti3dShader.setUniform("sampler", 0);
        objekti3dShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));

        objekti.liikeY += objekti.annaLiikeNopeus();
        Transform3D transform = objekti.transform;
        transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        target.mul(transform.getTransformation());
        objekti3dShader.setUniform("projection", target);

        Assets.getModel3D(objekti.anna3dMallinTunniste()).draw();
    }
}
