package keimo.keimoEngine.kenttä;

import keimo.Huone;
import keimo.Peli;
import keimo.Maastot.Maasto;
import keimo.Maastot.Tile;
import keimo.entityt.Entity;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.collision.AABB;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.grafiikat.objekti3d.Model3D;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;
import keimo.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class Maailma3D {

    private static int viewX;
    private static int viewY;
    public static ArrayList<Tile> tilet = new ArrayList<>();
    public static ArrayList<KenttäKohde> kenttäObjektit = new ArrayList<>();
    public static ArrayList<Entity> entityt = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    public static ArrayList<Model3D> objektit3d = new ArrayList<>();
    public static Shader objekti3dShader = new Shader("shader");
    private static Shader tileShader = new Shader("shader");
    private static Tekstuuri testiTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/84gs.jpg");
    private static Transform3D transform = new Transform3D();
    static Matrix4f world;
    static int scale = 32;

    private static HashMap<String, Tekstuuri> tileTextures = new HashMap<>();
    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");

    public static void alustaShader(Kamera kamera, Window ikkuna) {
        Maailma.objekti3dShader.bind();
        Maailma.objekti3dShader.setCamera(kamera);

        Maailma.esineShader.bind();
        Maailma.esineShader.setCamera(kamera);
    }

    public static void createWorld() {
        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
        for (Huone huone : Peli.huoneKartta.values()) {
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
        //objektit3d.add(new Objekti3D("asunto_yokyla", new Point3D(8, 19, 0)));
        //objektit3d.add(new Objekti3D("yokyla_40-54", new Point3D(10, 19, 0)));
        //objektit3d.add(new Objekti3D(new Point3D(14, -3, 0)));
        //objektit3d.add(new Objekti3D("Wooden_Crate_2", new Point3D(14.5f, 20, -1)));
        //objektit3d.add(new Objekti3D("Testikuutio", new Point3D(14f, 20, 2)));
        //objektit3d.add(new Objekti3D("Coin", new Point3D(15, 14, 0)));
        //objektit3d.add(new Objekti3D("Kolikko", new Point3D(14, 14, 0)));
    }

    public static void render(Kamera camera) {
        try {
            // int posX = ((int)camera.getPosition().x / (scale * 2));
            // int posY = ((int)camera.getPosition().y / (scale * 2));
            // for (int y = 0; y < viewY; y++) {
            //     for (int x = 0; x < viewX; x++) {
            //         int renderX = x-posX-viewX/2;
            //         int renderY = y+posY-viewY/2;
            //         int maxX = Peli.maastokenttä.length;
            //         int maxY = Peli.maastokenttä.length;
            //         if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
            //             Maasto m = Peli.annaMaastoKenttä()[renderX][renderY];
            //             if (m instanceof Tile) {
            //                 Tile t = (Tile)m;
            //                 if (t != null) {
            //                     renderöiTile(t, renderX, -renderY, world, camera);
            //                 }
            //             }
            //         }
            //     }
            // }
            // for (Entity e : Peli.entityLista) {
            //     if (e != null) {
            //         renderöiEntity(e, (int)e.hitbox.getMinX(), (int)-e.hitbox.getMinY(), shader, world, camera);
            //     }
            // }
            // for (int y = 0; y < viewY; y++) {
            //     for (int x = 0; x < viewX; x++) {
            //         int renderX = x-posX-viewX/2;
            //         int renderY = y+posY-viewY/2;
            //         int maxX = Peli.maastokenttä.length;
            //         int maxY = Peli.maastokenttä.length;
            //         if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
            //             KenttäKohde k = Peli.annaObjektiKenttä()[renderX][renderY];
            //             if (k != null) {
            //                 if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, renderX, -renderY, 1, world, camera);
            //                 else renderöi3dKenttäObjekti(k, renderX, -renderY, 0, world, camera);
            //             }
            //         }
            //     }
            // }
            // for (Objekti3D objekti3d : objektit3d) {
            //     renderöi3dKenttäObjekti(objekti3d, objekti3d.annaSij().x, -objekti3d.annaSij().y, objekti3d.annaSij().z, world, camera);
            // }
            renderöi3DSkene();
        }
        catch (IndexOutOfBoundsException aioobe) {
            System.out.println("koko muuttui");
            aioobe.printStackTrace();
        }
    }

    public static void calculateView(Window window, Kamera camera) {
        viewX = (window.getWidth() / (scale * 2)) + (int)(0.005f / camera.getProjection().getScale(new Vector3f()).x) - (int)(5f * ((float)window.getWidth()/(float)window.getHeight()));
		viewY = (window.getHeight() / (scale * 2)) + (int)(0.01f / camera.getProjection().getScale(new Vector3f()).y) - 5;
	}

    protected static void renderöiTile(Tile tile, int x, int y, Matrix4f world, Kamera cam) {
        tileShader.bind();
		if (tileTextures.containsKey(tile.annaTekstuuri())) tileTextures.get(tile.annaTekstuuri()).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
		tilenSijainti.scale(1, 1, 0);
		Matrix4f target = new Matrix4f();
        //tilenSijainti = KenttäShaderEfektit.känniEfekti(tileShader, tilenSijainti);
		
		cam.getProjection().mul(world, target);
		target.mul(tilenSijainti);
		
		tileShader.setUniform("sampler", 0);
		tileShader.setUniform("projection", target);
        tileShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
        tileShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));
		
		Model model = Assets.getModel(tile.annaKuvanKääntö(), tile.annaKuvanPeilausX(), tile.annaKuvanPeilausY());
		model.render();
        //Assets.getModel3D("Tynnyri").draw();
	}

    protected static void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f world, Kamera cam) {

        objekti3dShader.bind();
		Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2 -2f, y * 2 -0.5f, 0));
		objektinSijainti.scale(1, 1, 0);
		Matrix4f target = new Matrix4f();
        //objektinSijainti = KenttäShaderEfektit.känniEfekti(objekti3dShader, objektinSijainti);
		
		cam.getProjection().mul(world, target);
		target.mul(objektinSijainti);
		
		objekti3dShader.setUniform("sampler", 0);
		objekti3dShader.setUniform("projection", target);
        objekti3dShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
        objekti3dShader.setUniform("addcolor", new Vector4f(0f, 0f, 0f, 0f));

        objekti.liikeY += objekti.annaLiikeNopeus();
        Transform3D transform = objekti.transform;
        transform.setPosition(new Vector3f(x, y + (float)(4f* Math.sin(Math.toRadians(objekti.annaLiikeY()))), z));
        transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        objekti3dShader.setTransform(transform);
        Assets.getModel3D("Tynnyri").draw();
        //Objekti3D.väriEfekti(objekti3dShader);

        // Mesh meshObj = Assets.getMeshObj(objekti.annaNimi());
        // if (meshObj != null && meshObj.tekstuuriLista.size() > 0) {
        //     //Tekstuuri tekstuuri = meshObj.tekstuuriLista.get(random.nextInt(meshObj.tekstuuriLista.size()));
        //     Tekstuuri tekstuuri = meshObj.tekstuuriLista.get(0);
        //     tekstuuri.bind(0);
        // }
        // else Objekti3D.testiTekstuuri.bind(0);
        
        // if (objekti instanceof Objekti3D) {
        //     Objekti3D objekti3d = (Objekti3D)objekti;
        //     //Objekti3D.shader.setCamera(cam);
        //     switch (objekti3d.annaTyyppi()) {
        //         case OBJ -> {
        //             Assets.getMeshObj(objekti.annaNimi()).draw(GL_TRIANGLES);
        //         }
        //         case KUUTIO -> {
        //             Assets.getMeshCube().draw(GL_QUADS);
        //         }
        //         case KOLMIO -> {
        //             Assets.getMesh().draw(GL_TRIANGLES);
        //         }
        //     }
        // }
        // else Assets.getMeshObj(objekti.anna3dMallinTunniste()).draw(GL_TRIANGLES);
    }

    static Kamera kamera = new Kamera(640, 480);
    static Shader teksti3dShader = new Shader("shader3dtest");
    static Transform3D transform3DTeksti = new Transform3D();
    static Transform3D transform3DMalli = new Transform3D();
    public static float camRotX = 0, camRotY = 0, camRotZ = 0;
    protected static void renderöi3DSkene() {
        teksti3dShader.bind();
        kamera.setProjection(kamera.getProjection().scale(2));
        teksti3dShader.setCamera(kamera);

        transform3DTeksti.setPosition(new Vector3f(0, 1.75f, -5));
        transform3DTeksti.getRotation().rotateAxis((float)Math.toRadians(2 * 1f), 0, 1, 0);
        Matrix4f mat3DTeksti = new Matrix4f();
        teksti3dShader.setUniform("projection", mat3DTeksti);
        teksti3dShader.setTransform(transform3DTeksti);
        väriEfekti2(teksti3dShader);
        Assets.getModel3D("KeimoTeksti").draw();

        transform3DMalli.setPosition(new Vector3f(0, 0f, -5));
        transform3DMalli.getRotation().rotateAxis((float)Math.toRadians(2 * 1f), 0, 1, 0);
        transform3DMalli.setScale(new Vector3f(0.25f, 0.25f, 0.25f));
        Matrix4f mat3DObjekti = new Matrix4f();
        teksti3dShader.setUniform("projection", mat3DObjekti);
        teksti3dShader.setTransform(transform3DMalli);
        väriEfekti2(teksti3dShader);
        Assets.getModel3D("Testikuutio").draw();


        päivitäSijainti();
        glEnable(GL11.GL_DEPTH_TEST);
        glLoadIdentity();
        glLoadMatrixf(new Matrix4f().scale(4f).setLookAt(xSij, ySij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ).get(new float[16]));

        objekti3dShader.bind();
        Matrix4f matrix = new Matrix4f();
        matrix.setLookAt(xSij, ySij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
        objekti3dShader.setUniform("projection", matrix);
        testiTekstuuri.bind(0);
        Assets.getModel().render();

        // float posX = 0, posY = 0, posZ = 0;
        // glBegin(GL_QUADS);
        //     glColor3f(1f, 0f, 0f);

        //     glTexCoord2f(0.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ + 0.5f);
        //     glTexCoord2f(1.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ + 0.5f);
        //     glTexCoord2f(1.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
        //     glTexCoord2f(0.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);

        //     //Back Face
        //     glTexCoord2f(1.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
        //     glTexCoord2f(1.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
        //     glTexCoord2f(0.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);
        //     glTexCoord2f(0.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ - 0.5f);

        //     //Top Face
        //     glTexCoord2f(0.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
        //     glTexCoord2f(0.0f, 0.0f); glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);
        //     glTexCoord2f(1.0f, 0.0f); glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
        //     glTexCoord2f(1.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);

        //     //Bottom Face
        //     glTexCoord2f(1.0f, 1.0f); glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
        //     glTexCoord2f(0.0f, 1.0f); glVertex3f(posX + 0.5f, posY, posZ - 0.5f);
        //     glTexCoord2f(0.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ + 0.5f);
        //     glTexCoord2f(1.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ + 0.5f);

        //     //Right face
        //     glTexCoord2f(1.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ - 0.5f);
        //     glTexCoord2f(1.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);
        //     glTexCoord2f(0.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
        //     glTexCoord2f(0.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ + 0.5f);

        //     //Left Face
        //     glTexCoord2f(0.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
        //     glTexCoord2f(1.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ + 0.5f);
        //     glTexCoord2f(1.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);
        //     glTexCoord2f(0.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
        
        // glEnd();
        for (Model model : Assets.lattiaTilet) {
            testiTekstuuri.bind(0);
            model.render();
        }

        // objekti3dShader.bind();
        // Matrix4f matrixId = new Matrix4f();
        // matrixId.rotateX((float)Math.toRadians(45));
        // tileShader.setUniform("projection", matrixId);
        // testiTekstuuri.bind(0);
        // Assets.lattiaTilet.get(0).render();
        // Assets.getModel().render();
    }

    public enum Liike {
        ETEENPÄIN,
        TAAKSEPÄIN,
        VASEN,
        OIKEA,
        YLÖS,
        ALAS;
    }

    public static double posStep = 0.01;
    public static float xSij = 0;
    public static float ySij = 1f;
    public static float zSij = 0;
    public static float kameranYSij = ySij + 0.5f;
    public static float xKohde = xSij;
    public static float yKohde = ySij;
    public static float zKohde = zSij -2;
    public static float upX = 0;
    public static float upY = 1f;
    public static float upZ = 0;
    public static float hSpeed = 0.05f;
    public static float vSpeed = 0.05f;
    public static double kääntöNopeus = 1;
    public static float yaw = 0;
    public static float pitch = 0;
    public static float roll = 0;

    public static void liiku(Liike liike) {
        switch (liike) {
            case ETEENPÄIN:
                xSij -= hSpeed * (float)Math.cos(Math.toRadians(yaw));
                zSij -= hSpeed * (float)Math.sin(Math.toRadians(yaw));
            break;
            case TAAKSEPÄIN:
                xSij += hSpeed * (float)Math.cos(Math.toRadians(yaw));
                zSij += hSpeed * (float)Math.sin(Math.toRadians(yaw));
            break;
            case VASEN:
                xSij += hSpeed * (float)Math.sin(Math.toRadians(yaw));
                zSij -= hSpeed * (float)Math.cos(Math.toRadians(yaw));
            break;
            case OIKEA:
                xSij -= hSpeed * (float)Math.sin(Math.toRadians(yaw));
                zSij += hSpeed * (float)Math.cos(Math.toRadians(yaw));
            break;
            case ALAS:
                ySij -= vSpeed;
            break;
            case YLÖS:
                ySij += vSpeed;
            break;
            case null, default:
            break;
        }
    }

    private static void päivitäSijainti() {
        //kameranYSij = ySij + 0.5f;
        //yKohde = kameranYSij + (float)Math.sin(Math.toRadians(pitch));
        //xKohde = xSij + (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //zKohde = zSij + (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //upY = (float)Math.cos(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(pitch));
        //upZ = (float)Math.sin(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //upX = (float)Math.sin(Math.toRadians(roll)) * (float)-Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        yKohde = ySij + 0.5f;
        xKohde = xSij + (float)Math.cos(Math.toRadians(yaw));
        zKohde = zSij + (float)Math.sin(Math.toRadians(yaw));
        upY = 1;
        upZ = 0;
        upX = 0;
    }

    static float punainen = 0f, vihreä = 0.5f, sininen = 1f;
    static boolean lisääPun = true, lisääVihr = true, lisääSin = false;
    protected static void väriEfekti2(Shader shader) {
        if (lisääPun) punainen += 0.01f;
        else punainen -= 0.01f;
        if (lisääVihr) vihreä += 0.01f;
        else vihreä -= 0.01f;
        if (lisääSin) sininen += 0.01f;
        else sininen -= 0.01f;
        
        if (punainen >= 1f) lisääPun = false;
        else if (punainen <= 0f) lisääPun = true;
        if (vihreä >= 1f) lisääVihr = false;
        else if (vihreä <= 0f) lisääVihr = true;
        if (sininen >= 1f) lisääSin = false;
        else if (sininen <= 0f) lisääSin = true;
        
        shader.setUniform("addcolor", new Vector4f(punainen, vihreä, sininen, 0f));
    }
}
