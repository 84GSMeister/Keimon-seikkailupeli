package keimo.keimoEngine.kenttä;

import keimo.Huone;
import keimo.Peli;
import keimo.Maastot.Maasto;
import keimo.Maastot.Tile;
import keimo.entityt.Entity;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.grafiikat.objekti3d.Model3D;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.gui.HUD;
import keimo.keimoEngine.ikkuna.Window;
import keimo.kenttäkohteet.KenttäKohde;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maailma3D {

    private static int viewX;
    private static int viewY;
    public static ArrayList<Tile> tilet = new ArrayList<>();
    public static ArrayList<KenttäKohde> kenttäObjektit = new ArrayList<>();
    public static ArrayList<Entity> entityt = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    public static ArrayList<Model3D> objektit3d = new ArrayList<>();
    private static Shader shader = new Shader("shader");
    private static Tekstuuri testiTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/84gs.jpg");
    static Matrix4f world;
    static int scale = 32;
    static String kenttä = "Yo-kylä";

    private static HashMap<String, Tekstuuri> tileTextures = new HashMap<>();
    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
    private static Model3D modelYokyläAsunto, modelYokylä, modelSieni;

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
        createWorld3D();
    }

    private static void createWorld3D() {
        modelYokyläAsunto = Assets.getModel3D("asunto_yokyla");
        modelYokyläAsunto.getTransform().setScale(new Vector3f(10f, 10f, 10f));
        modelYokyläAsunto.getTransform().setPosition(new Vector3f(10, 10, 10));
        modelYokylä = Assets.getModel3D("yokyla_40-54");
        modelYokylä.getTransform().setScale(new Vector3f(40f, 40f, 40f));
        modelYokylä.getTransform().setPosition(new Vector3f(-2, 0, 0));
        modelSieni = Assets.getModel3D("Sieni");
        modelSieni.getTransform().setScale(new Vector3f(0.25f, 0.25f, 0.25f));
        modelSieni.getTransform().setPosition(new Vector3f(4, -2, 0));

        objektit3d.add(modelYokyläAsunto);
        objektit3d.add(modelYokylä);
        objektit3d.add(modelSieni);
    }

    public static void render(Window window) {
        liikutaPelaajaa();
        päivitäSijainti();
        renderöi3DSkene(window);
        renderöiPyörivätObjektit();
        renderöi3DMaailma();
        DebugTeksti.renderöiDebugTeksti(window);
        DebugTeksti.renderöiLisäMoodiTekstit(window);
    }

    protected static void renderöi3DSkene(Window window) {
        for (Model3D malli3D : objektit3d) {
            renderöi3DMalli(malli3D, window);
        }
    }

    protected static void renderöi3DMalli(Model3D malli, Window window) {
        Matrix4f modelMatrix = malli.getTransform().getTransformation();
        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(70, window.getWidth()/window.getHeight(), 0.001f, 1000);
        Matrix4f lookAtMatrtix = new Matrix4f().setLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);

        shader.bind();
        Matrix4f resultMatrix = perspectiveMatrix.mul(lookAtMatrtix).mul(modelMatrix);
        shader.setUniform("projection", resultMatrix);
        malli.draw();
    }

    static Shader teksti3dShader = new Shader("shader");
    public static Shader objekti3dShader = new Shader("shader");
    static Transform3D transform3DTeksti = new Transform3D();
    static Transform3D transform3DMalli = new Transform3D();
    protected static void renderöiPyörivätObjektit() {
        teksti3dShader.bind();

        transform3DTeksti.setPosition(new Vector3f(0, -5f, -5));
        transform3DTeksti.getRotation().rotateAxis((float)Math.toRadians(2 * 1f), 0, 1, 0);
        Matrix4f mat3DTeksti = new Matrix4f();
        mat3DTeksti.mul(transform3DTeksti.getTransformation());
        teksti3dShader.setUniform("projection", mat3DTeksti);
        väriEfekti2(teksti3dShader);
        Assets.getModel3D("KeimoTeksti").draw();
    }

    public static void renderöi3DMaailma() {
        try {
            for (int y = 0; y < Peli.annaMaastoKenttä().length; y++) {
                for (int x = 0; x < Peli.annaMaastoKenttä().length; x++) {
                    Maasto m = Peli.annaMaastoKenttä()[x][y];
                    if (m instanceof Tile) {
                        Tile t = (Tile)m;
                        if (t != null) {
                            renderöiTile(t, x, Peli.annaMaastoKenttä().length/2 -y, world);
                        }
                    }
                }
            }
            // for (Entity e : Peli.entityLista) {
            //     if (e != null) {
            //         renderöiEntity(e, (int)e.hitbox.getMinX(), (int)-e.hitbox.getMinY(), shader, world, camera);
            //     }
            // }
            for (int y = 0; y < Peli.annaObjektiKenttä().length; y++) {
                for (int x = 0; x < Peli.annaObjektiKenttä().length; x++) {
                    KenttäKohde k = Peli.annaObjektiKenttä()[x][y];
                    if (k != null) {
                        if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, x, -y, 1, world);
                        else renderöi3dKenttäObjekti(k, x, -y, 0, world);
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException aioobe) {
            System.out.println("koko muuttui");
            aioobe.printStackTrace();
        }
    }

    protected static void renderöiTile(Tile tile, int x, int y, Matrix4f world) {
        shader.bind();
        if (tileTextures.containsKey(tile.annaTekstuuri())) tileTextures.get(tile.annaTekstuuri()).bind(0);
		else virheTekstuuri.bind(0);

        Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 25));
        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(70, 1, 0.001f, 1000);
        Matrix4f lookAtMatrtix = new Matrix4f().setLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
        
        Matrix4f resultMatrix = perspectiveMatrix.mul(lookAtMatrtix).mul(tilenSijainti);
        shader.setUniform("projection", resultMatrix);
        Model model = Assets.getModel(tile.annaKuvanKääntö(), tile.annaKuvanPeilausX(), tile.annaKuvanPeilausY());
        model.render();
	}

    protected static void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f world) {
        shader.bind();
		Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2 -2f, y * 2 -0.5f, 0));
        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(70, 1, 0.001f, 1000);
        Matrix4f lookAtMatrtix = new Matrix4f().setLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
		
		Matrix4f resultMatrix = perspectiveMatrix.mul(lookAtMatrtix).mul(objektinSijainti);
        shader.setUniform("projection", resultMatrix);
        Assets.getModel3D("Tynnyri").draw();
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

    public enum Liike {
        ETEENPÄIN,
        TAAKSEPÄIN,
        VASEN,
        OIKEA,
        YLÖS,
        ALAS,
        HYPPY;
    }

    public static double posStep = 0.01;
    public static float xSij = 0;
    public static float ySij = 0;
    public static float zSij = 0;
    public static float kameranYSij = ySij + 0.5f;
    public static float xKohde = xSij;
    public static float yKohde = ySij;
    public static float zKohde = zSij -2;
    public static float upX = 0;
    public static float upY = 1f;
    public static float upZ = 0;
    public static float hNopeus = 0.05f;
    public static float vNopeus = 0.05f;
    public static float putoamisKiihtyvyys = 0.005f;
    public static double kääntöNopeus = 1;
    public static float yaw = 0;
    public static float pitch = 0;
    public static float roll = 0;
    public static boolean moonJump = false;

    public static void liiku(Liike liike) {
        switch (liike) {
            case ETEENPÄIN:
                xSij += hNopeus * (float)Math.cos(Math.toRadians(yaw));
                zSij += hNopeus * (float)Math.sin(Math.toRadians(yaw));
            break;
            case TAAKSEPÄIN:
                xSij -= hNopeus * (float)Math.cos(Math.toRadians(yaw));
                zSij -= hNopeus * (float)Math.sin(Math.toRadians(yaw));
            break;
            case VASEN:
                xSij += hNopeus * (float)Math.sin(Math.toRadians(yaw));
                zSij -= hNopeus * (float)Math.cos(Math.toRadians(yaw));
            break;
            case OIKEA:
                xSij -= hNopeus * (float)Math.sin(Math.toRadians(yaw));
                zSij += hNopeus * (float)Math.cos(Math.toRadians(yaw));
            break;
            case ALAS:
                ySij -= vNopeus;
            break;
            case YLÖS:
                ySij += vNopeus;
            break;
            case HYPPY:
                if (ySij <= 0 || moonJump){
                    vNopeus = 0.1f;
                }
            case null, default:
            break;
        }
    }

    private static void liikutaPelaajaa() {
        ySij += vNopeus;
        if (ySij > 0) {
            if (vNopeus > -0.1f) vNopeus -= putoamisKiihtyvyys;
        }
        else if (ySij < 0) {
            vNopeus = 0;
            ySij = 0;
        }
    }

    private static void päivitäSijainti() {
        kameranYSij = ySij + 0.5f;
        yKohde = kameranYSij + (float)Math.sin(Math.toRadians(pitch));
        xKohde = xSij + (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        zKohde = zSij + (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        upY = (float)Math.cos(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(pitch));
        upZ = (float)Math.sin(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        upX = (float)Math.sin(Math.toRadians(roll)) * (float)-Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //yKohde = ySij + 0.5f;
        xKohde = xSij + (float)Math.cos(Math.toRadians(yaw));
        zKohde = zSij + (float)Math.sin(Math.toRadians(yaw));
        //upY = 1;
        //upZ = 0;
        //upX = 0;
    }

    public class DebugTeksti {

        static Teksti debugInfoTeksti = new Teksti("debug", Color.green, 400, 30);
        static Teksti lisäMoodiTeksti = new Teksti("moodi", Color.orange, 400, 30);
        static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
        static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

        public static void renderöiDebugTeksti(Window window) {
            try {
                int sijx = (int)(window.getWidth()/5.5);
                debugInfoTeksti.päivitäTeksti("Keimo3D Simulaattori v0.3");
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 40, window);
                debugInfoTeksti.päivitäTeksti("ESC: Poistu simulaattorista");
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 60, window);
                debugInfoTeksti.päivitäTeksti("F5: Huijauskoodit");
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 80, window);

                if (KeimoEngine.frameTime > 0) debugInfoTeksti.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime / KeimoEngine.frames)));
                else debugInfoTeksti.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime+0.00001 / KeimoEngine.frames)));
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 120, window);
                debugInfoTeksti.päivitäTeksti("Kenttä: " + kenttä);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 140, window);
                debugInfoTeksti.päivitäTeksti("sij X: " + xSij);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 160, window);
                debugInfoTeksti.päivitäTeksti("sij Y: " + ySij);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 180, window);
                debugInfoTeksti.päivitäTeksti("sij Z: " + zSij);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 200, window);
                debugInfoTeksti.päivitäTeksti("H-nopeus: " + hNopeus);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 220, window);
                debugInfoTeksti.päivitäTeksti("V-nopeus: " + vNopeus);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 240, window);
                debugInfoTeksti.päivitäTeksti("Kulma Y (Yaw): " + yaw);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 260, window);
                debugInfoTeksti.päivitäTeksti("Kulma X (Pitch): " + pitch);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 280, window);
                debugInfoTeksti.päivitäTeksti("Kulma Z (Roll): " + roll);
                HUD.renderöiTeksti(debugInfoTeksti, sijx, 300, window);
    
            }
            catch (NullPointerException npe) {
                System.out.println("Debug-tekstin näyttämisessä virhe");
                npe.printStackTrace();
            }
        }
        
        public static void renderöiLisäMoodiTekstit(Window window) {
            int sijx = (int)(window.getWidth()/5.5);
            if (moonJump) {
                lisäMoodiTeksti.päivitäTeksti("Moonjump");
                HUD.renderöiTeksti(lisäMoodiTeksti, sijx, 500, window);
            }
        }
    }
}
