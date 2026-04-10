package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.minipeli3d;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.objekti3d.Model3D;
import keimo.keimoengine.grafiikat.objekti3d.Transform3D;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.assets.huone.Huone3D;
import keimo.seikkailupeli.gui.hud.HUD;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.maastot.IsoLaatta;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.objektit.maastot.Tile;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maailma3D {

    public static ArrayList<Maasto> tilet = new ArrayList<>();
    public static ArrayList<KenttäKohde> kenttäObjektit = new ArrayList<>();
    public static ArrayList<Entity> entityt = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    //private static Shader shader = new Shader("shader");
    static Matrix4f world;
    static int scale = 32;
    private static Huone3D ladattuHuone;
    private static HashMap<Integer, Huone3D> huone3dKartta = new HashMap<>();
    private static int huoneenId = 0;
    public static boolean debugTiedotNäkyvissä = false;
    private static boolean voitto = false;

    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");
    private static Teksti voittoTeksti = new Teksti("VOITIT PELIN!", Color.yellow, 1400, 400, KeimoFontit.fontti_keimo_100, false);
    private static Teksti voittoTeksti2 = new Teksti("Salainen reitti on avattu", Color.yellow, 2800, 200, KeimoFontit.fontti_keimo_100, false);
    private static Teksti voittoTeksti3 = new Teksti("Paina Esc sulkeaksesi päätteen", Color.yellow, 3400, 100, KeimoFontit.fontti_keimo_100, false);
    private static Teksti vihjeTeksti = new Teksti("Tavoite 1. rivi", Color.cyan, 3300, 125, KeimoFontit.fontti_keimo_100, false);
    private static Teksti vihjeTeksti2 = new Teksti("Tavoite 2. rivi", Color.cyan, 3300, 125, KeimoFontit.fontti_keimo_100, false);

    public static void createWorld() {
        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
        for (Huone huone : Peli.huoneKartta.values()) {
            for (int y = 0; y < huone.annaKoko(); y++) {
                for (int x = 0; x < huone.annaKoko(); x++) {
                    if (huone.annaHuoneenMaastoSisältö()[x][y] != null) {
                        Maasto m = huone.annaHuoneenMaastoSisältö()[x][y];
                        String tiedostonNimi = huone.annaHuoneenMaastoSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            ArrayList<String> ominaisuusLista = new ArrayList<>();
                            ominaisuusLista.add("kuva=" + tiedostonNimi);
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            if (m instanceof Tile) tilet.add(Maasto.luoMaastoTiedoilla("Tile", x, y, ominaisuusLista));
                            else if (m instanceof IsoLaatta) tilet.add(Maasto.luoMaastoTiedoilla("IsoLaatta", x, y, ominaisuusLista));
                        }
                    }
                    if (huone.annaHuoneenKenttäSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenKenttäSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            String objektinNimi =  ("" + tiedostonNimi.charAt(0)).toUpperCase() + tiedostonNimi.substring(1);
                            ArrayList<String> ominaisuusLista = huone.annaHuoneenKenttäSisältö()[x][y].annaLisäOminaisuudet();
                            kenttäObjektit.add(KenttäKohde.luoObjektiTiedoilla(objektinNimi, x, y, ominaisuusLista));
                        }
                    }
                    if (huone.annaHuoneenNPCSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenNPCSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                            String entityNimi =  ("" + tiedostonNimi.charAt(0)).toUpperCase() + tiedostonNimi.substring(1);
                            ArrayList<String> ominaisuusLista = huone.annaHuoneenNPCSisältö()[x][y].annaLisäOminaisuudet();
                            entityt.add(Entity.luoEntityTiedoilla(entityNimi, x, y, ominaisuusLista));
                        }
                    }
                }
            }
            if (huone.annaTaustanPolku() != null && huone.annaTaustanPolku() != "") {
                taustakuvat.add(huone.annaTaustanPolku());
            }
        }

        // for (String s : Maailma.taustakuvat) {
        //     try {
        //         String taustanNimi = s.substring(0, s.length()-4);
        //         Tausta.taustaTekstuurit.put(taustanNimi, new Tekstuuri("tiedostot/kuvat/taustat/" + s));
        //     }
        //     catch (StringIndexOutOfBoundsException sioobe) {
        //         System.out.println("Virheellinen tausta: " + s);
        //         sioobe.printStackTrace();
        //     }
        // }
        createWorld3D();
    }

    private static void createWorld3D() {
        Model3D modelYokyläAsunto = Assets.getModel3D("asunto_yokyla");
        if (modelYokyläAsunto != null) {
            modelYokyläAsunto.getTransform().setPosition(new Vector3f(1, 31.25f, 1));
            modelYokyläAsunto.getTransform().setScale(new Vector3f(10f, 10f, 10f));
        }
        Model3D modelYokylä = Assets.getModel3D("yo-kyla");
        if (modelYokylä != null) {
            modelYokylä.getTransform().setScale(new Vector3f(40f, 40f, 40f));
            modelYokylä.getTransform().setPosition(new Vector3f(-2, 0, 0));
        }
        Model3D modelSieni = Assets.getModel3D("Sieni");
        if (modelSieni != null) {
            modelSieni.getTransform().setScale(new Vector3f(0.25f, 0.25f, 0.25f));
            modelSieni.getTransform().setPosition(new Vector3f(4, -2, 0));
        }
        Model3D modelTynnyri = Assets.getModel3D("Tynnyri");
        if (modelTynnyri != null) {
            modelTynnyri.getTransform().setScale(new Vector3f(0.25f, 0.25f, 0.25f));
            modelTynnyri.getTransform().setPosition(new Vector3f(4, -2, 0));
        }
        Model3D modelTölkki = Assets.getModel3D("tölkki");
        if (modelTölkki != null) {
            modelTölkki.getTransform().setScale(new Vector3f(0.25f, 0.25f, 0.25f));
            modelTölkki.getTransform().setPosition(new Vector3f(4, -2, 0));
        }

        huone3dKartta.put(0, new Huone3D(0, "Yo-kylä", modelYokylä, null));
        huone3dKartta.put(1, new Huone3D(1, "Asunto", modelYokyläAsunto, null));
        huone3dKartta.put(2, new Huone3D(2, "Sieni", modelSieni, null));
        huone3dKartta.put(3, new Huone3D(3, "Tynnyri", modelTynnyri, null));
        huone3dKartta.put(4, new Huone3D(4, "Tölkki", modelTölkki, null));
        vaihdaHuonetta(huoneenId);

        DebugTeksti.luoDebugTekstit();
    }

    public static void luoMinipeliIkkuna() {
        voitto = false;
        xSij = -72;
        ySij = 0;
        zSij = 15;
        upX = 0;
        upY = 1f;
        upZ = 0;
        hNopeus = 0.05f;
        vNopeus = 0.05f;
        yaw = 0;
        pitch = 0;
        roll = 0;
        moonJump = false;
    }

    public static int annaHuoneenId() {
        return huoneenId;
    }

    public static void vaihdaHuonetta(int huone) {
        try {
            if (huone >= 0 && huone < huone3dKartta.size()) {
                huoneenId = huone;
                ladattuHuone = huone3dKartta.get(huoneenId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renderöi(Ikkuna window, Shader shader) {
        if (voitto) {
            renderöiVoittoTeksti(window);
        }
        else {
            renderöi3DSkene(window, shader);
            //renderöiPyörivätObjektit();
            if (Peli.huone != null) renderöi3DMaailma(shader);
            renderöiVihjeTeksti(window);
            DebugTeksti.renderöiDebugTeksti(window);
            DebugTeksti.renderöiLisäMoodiTekstit(window);
        }
    }

    protected static void renderöi3DSkene(Ikkuna window, Shader shader) {
        if (ladattuHuone != null) {
            Model3D ladattuMalli = ladattuHuone.annaHuoneenModel();
            ArrayList<Model3D> ladatutObjektit = ladattuHuone.annaHuoneenObjektit();
            if (ladattuMalli != null) renderöi3DMalli(ladattuMalli, window, shader);
            if (ladatutObjektit != null) {
                for (Model3D malli3D : ladatutObjektit) {
                    renderöi3DMalli(malli3D, window, shader);
                }
            }
        }
    }

    protected static void renderöi3DMalli(Model3D malli, Ikkuna window, Shader shader) {
        if (malli != null) {
            Matrix4f modelMatrix = malli.getTransform().getTransformation();
            Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(70, window.getWidth()/window.getHeight(), 0.001f, 1000);
            Matrix4f lookAtMatrtix = new Matrix4f().setLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
            Matrix4f resultMatrix = perspectiveMatrix.mul(lookAtMatrtix).mul(modelMatrix);
            
            shader.bind();
            shader.asetaSijainti(resultMatrix);
            malli.draw();
        }
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
        teksti3dShader.asetaSijainti(mat3DTeksti);
        väriEfekti2(teksti3dShader);
        Assets.getModel3D("KeimoTeksti").draw();
    }

    public static void renderöi3DMaailma(Shader shader) {
        try {
            for (int y = 0; y < Peli.annaMaastoKenttä().length; y++) {
                for (int x = 0; x < Peli.annaMaastoKenttä().length; x++) {
                    Maasto m = Peli.annaMaastoKenttä()[x][y];
                    if (m instanceof Tile) {
                        Tile t = (Tile)m;
                        if (t != null) {
                            renderöiTile(t, x, Peli.annaMaastoKenttä().length/2 -y, world, shader);
                        }
                    }
                }
            }
            for (int y = 0; y < Peli.annaObjektiKenttä().length; y++) {
                for (int x = 0; x < Peli.annaObjektiKenttä().length; x++) {
                    KenttäKohde k = Peli.annaObjektiKenttä()[x][y];
                    if (k != null) {
                        if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, x, -y, 1, world, shader);
                        else renderöi3dKenttäObjekti(k, x, Peli.annaObjektiKenttä().length/2 -y, 0, world, shader);
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException aioobe) {
            System.out.println("koko muuttui");
            aioobe.printStackTrace();
        }
    }

    protected static void renderöiTile(Tile tile, int x, int y, Matrix4f world, Shader shader) {
        shader.bind();
        if (Assets.annaTileTekstuurit().containsKey(tile.annaTekstuurinNimi())) Assets.annaTileTekstuurit().get(tile.annaTekstuurinNimi()).bind(0);
		else virheTekstuuri.bind(0);

        Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, -25));
        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(70, 1, 0.001f, 1000);
        Matrix4f lookAtMatrtix = new Matrix4f().setLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
        Matrix4f resultMatrix = perspectiveMatrix.mul(lookAtMatrtix).mul(tilenSijainti);
        shader.asetaSijainti(resultMatrix);

        Model model = Assets.getModel(tile.annaKääntöAsteet(), tile.annaXPeilaus(), tile.annaYPeilaus());
        model.render();
	}

    protected static void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f world, Shader shader) {
        shader.bind();
		Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, -24));
        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(70, 1, 0.001f, 1000);
        Matrix4f lookAtMatrix = new Matrix4f().setLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
		
		Matrix4f resultMatrix = perspectiveMatrix.mul(lookAtMatrix).mul(objektinSijainti);
        shader.asetaSijainti(resultMatrix);
        objekti.annaTekstuuri().bind(0);
        Assets.getModel(objekti.annaKääntöAsteet(), objekti.annaXPeilaus(), objekti.annaYPeilaus()).render();
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

    public enum KameranLiike {
        VASEN,
        OIKEA,
        YLÖS,
        ALAS,
        PYÖRITÄ_VASEN,
        PYÖRITÄ_OIKEA;
    }

    public static double posStep = 0.01;
    public static float xSij = -72;
    public static float ySij = 0;
    public static float zSij = 15;
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
    private static boolean liikuEteenpäin = false;
    private static boolean liikuTaaksepäin = false;
    private static boolean liikuVasemmalle = false;
    private static boolean liikuOikealle = false;

    public static void liiku(Liike liike) {
        switch (liike) {
            case ETEENPÄIN:
                //xSij += hNopeus * (float)Math.cos(Math.toRadians(yaw));
                //zSij += hNopeus * (float)Math.sin(Math.toRadians(yaw));
                liikuEteenpäin = true;
            break;
            case TAAKSEPÄIN:
                //xSij -= hNopeus * (float)Math.cos(Math.toRadians(yaw));
                //zSij -= hNopeus * (float)Math.sin(Math.toRadians(yaw));
                liikuTaaksepäin = true;
            break;
            case VASEN:
                //xSij += hNopeus * (float)Math.sin(Math.toRadians(yaw));
                //zSij -= hNopeus * (float)Math.cos(Math.toRadians(yaw));
                liikuVasemmalle = true;
            break;
            case OIKEA:
                //xSij -= hNopeus * (float)Math.sin(Math.toRadians(yaw));
                //zSij += hNopeus * (float)Math.cos(Math.toRadians(yaw));
                liikuOikealle = true;
            break;
            case ALAS:
                ySij -= vNopeus;
            break;
            case YLÖS:
                ySij += vNopeus;
            break;
            case HYPPY:
                if (ySij <= 0 || moonJump) {
                    vNopeus = 0.1f;
                }
            case null, default:
            break;
        }
    }

    public static void lopetaLiike(Liike liike) {
        switch (liike) {
            case ETEENPÄIN:
                liikuEteenpäin = false;
            break;
            case TAAKSEPÄIN:
                liikuTaaksepäin = false;
            break;
            case VASEN:
                liikuVasemmalle = false;
            break;
            case OIKEA:
                liikuOikealle = false;
            break;
            case ALAS:
                ySij -= vNopeus;
            break;
            case YLÖS:
                ySij += vNopeus;
            break;
            case HYPPY:
                if (ySij <= 0 || moonJump) {
                    vNopeus = 0.1f;
                }
            case null, default:
                liikuEteenpäin = false;
                liikuTaaksepäin = false;
                liikuVasemmalle = false;
                liikuOikealle = false;
            break;
        }
    }

    public static void maailma3DLoop() {
        liikutaPelaajaa();
        päivitäSijainti();
        tarkistaVoitto();
    }

    private static void liikutaPelaajaa() {
        if (liikuEteenpäin) {
            xSij += hNopeus * (float)Math.cos(Math.toRadians(yaw));
            zSij += hNopeus * (float)Math.sin(Math.toRadians(yaw));
        }
        if (liikuTaaksepäin) {
            xSij -= hNopeus * (float)Math.cos(Math.toRadians(yaw));
            zSij -= hNopeus * (float)Math.sin(Math.toRadians(yaw));
        }
        if (liikuVasemmalle) {
            xSij += hNopeus * (float)Math.sin(Math.toRadians(yaw));
            zSij -= hNopeus * (float)Math.cos(Math.toRadians(yaw));
        }
        if (liikuOikealle) {
            xSij -= hNopeus * (float)Math.sin(Math.toRadians(yaw));
            zSij += hNopeus * (float)Math.cos(Math.toRadians(yaw));
        }

        ySij += vNopeus;
        if (ySij > 0) {
            if (vNopeus > -0.1f) vNopeus -= putoamisKiihtyvyys;
        }
        else if (ySij < 0) {
            vNopeus = 0;
            ySij = 0;
        }
    }

    public static void käännä(KameranLiike liike) {
        switch (liike) {
            case VASEN -> {
                yaw -= kääntöNopeus;
                if (yaw < 0) yaw += 360;
                yaw %= 360;
            }
            case OIKEA -> {
                yaw += kääntöNopeus;
                yaw %= 360;
            }
            case YLÖS -> {
                pitch += kääntöNopeus;
                // if (Maailma3D.pitch > 89.999) Maailma3D.pitch = 89.999f;
                // else Maailma3D.pitch = Math.round(Maailma3D.pitch);
                if (pitch < -90) pitch = -90;
            }
            case ALAS -> {
                pitch -= kääntöNopeus;
                // if (Maailma3D.pitch < -89.999) Maailma3D.pitch = -89.999f;
                // else Maailma3D.pitch = Math.round(Maailma3D.pitch);
                if (pitch < -90) pitch = -90;
            }
            case PYÖRITÄ_VASEN -> {
                roll += kääntöNopeus;
                roll %= 360;
            }
            case PYÖRITÄ_OIKEA -> {
                roll -= kääntöNopeus;
                if (roll < 0) roll += 360;
                roll %= 360;
            }
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
    }

    private static void tarkistaVoitto() {
        if (xSij > -1.5 && xSij < -1 && zSij > -3 && zSij < -2) {
            voitto = true;
            if (!TavoiteLista.tavoiteLista.get("Löydä salahuone")) TavoiteLista.suoritaTavoite("Löydä salahuone");
        }
    }

    private static void renderöiVoittoTeksti(Ikkuna window) {
        float skaalaX = 0.5f, skaalaY = 0.25f;
        Komponentti.renderöiKomponenttiJaSkaalaa(objekti3dShader, voittoTeksti, window, skaalaX, skaalaY, 1, 0, 0, 0);

        skaalaX = 0.5f; skaalaY = 1/16f;
        Komponentti.renderöiKomponenttiJaSkaalaa(objekti3dShader, voittoTeksti2, window, skaalaX, skaalaY, 1, 0, -0.125f, 0);

        skaalaX = 0.5f; skaalaY = 1/64f;
        Komponentti.renderöiKomponenttiJaSkaalaa(objekti3dShader, voittoTeksti3, window, skaalaX, skaalaY, 1, 0, -0.25f, 0);
    }

    private static void renderöiVihjeTeksti(Ikkuna window) {
        vihjeTeksti.päivitäTeksti("Tavoite: Etsi Keimon koti");
        HUD.renderöiTeksti(vihjeTeksti, (int)(window.getWidth()/2), 400, window);
        vihjeTeksti2.päivitäTeksti("Yo-kylä 46 A 24");
        HUD.renderöiTeksti(vihjeTeksti2, (int)(window.getWidth()/2), 420, window);
    }

    public class DebugTeksti {

        static Teksti[] debugInfoTekstit = new Teksti[13];
        static Teksti lisäMoodiTeksti = new Teksti("moodi", Color.orange, 1200, 48);
        static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
        static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

        public static void luoDebugTekstit() {
            for (int i = 0; i < debugInfoTekstit.length; i++) {
                debugInfoTekstit[i] = new Teksti("debug", Color.green, 1200, 48);
            }
        }

        public static void renderöiDebugTeksti(Ikkuna window) {
            if (debugTiedotNäkyvissä) {
                try {
                    int sijx = (int)(window.getWidth()/1.75f);
                    debugInfoTekstit[0].päivitäTeksti("Keimo3D Simulaattori v0.3");
                    HUD.renderöiTeksti(debugInfoTekstit[0], sijx, 40, window);
                    debugInfoTekstit[1].päivitäTeksti("ESC: Poistu simulaattorista");
                    HUD.renderöiTeksti(debugInfoTekstit[1], sijx, 60, window);
                    debugInfoTekstit[2].päivitäTeksti("F5: Huijauskoodit");
                    HUD.renderöiTeksti(debugInfoTekstit[2], sijx, 80, window);

                    //if (KeimoEngine.frameTime > 0) debugInfoTekstit[3].päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime / KeimoEngine.frames)));
                    //else debugInfoTekstit[3].päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime+0.00001 / KeimoEngine.frames)));
                    debugInfoTekstit[3].päivitäTeksti("fps: " + kaksiDesimaalia.format(1d /KeimoEngine.keskivertoFrameAika));
                    HUD.renderöiTeksti(debugInfoTekstit[3], sijx, 120, window);
                    if (ladattuHuone != null) debugInfoTekstit[4].päivitäTeksti("Kenttä: " + ladattuHuone.annaNimi() + " (" + ladattuHuone.annaId() + ")");
                    else debugInfoTekstit[4].päivitäTeksti("Kenttä: " + "Ei määritetty" + " (+ / - : vaihda)");
                    HUD.renderöiTeksti(debugInfoTekstit[4], sijx, 140, window);
                    debugInfoTekstit[5].päivitäTeksti("sij X: " + xSij);
                    HUD.renderöiTeksti(debugInfoTekstit[5], sijx, 180, window);
                    debugInfoTekstit[6].päivitäTeksti("sij Y: " + ySij);
                    HUD.renderöiTeksti(debugInfoTekstit[6], sijx, 200, window);
                    debugInfoTekstit[7].päivitäTeksti("sij Z: " + zSij);
                    HUD.renderöiTeksti(debugInfoTekstit[7], sijx, 220, window);
                    debugInfoTekstit[8].päivitäTeksti("H-nopeus: " + hNopeus);
                    HUD.renderöiTeksti(debugInfoTekstit[8], sijx, 240, window);
                    debugInfoTekstit[9].päivitäTeksti("V-nopeus: " + vNopeus);
                    HUD.renderöiTeksti(debugInfoTekstit[9], sijx, 260, window);
                    debugInfoTekstit[10].päivitäTeksti("Kulma Y (Yaw): " + yaw);
                    HUD.renderöiTeksti(debugInfoTekstit[10], sijx, 280, window);
                    debugInfoTekstit[11].päivitäTeksti("Kulma X (Pitch): " + pitch);
                    HUD.renderöiTeksti(debugInfoTekstit[11], sijx, 300, window);
                    debugInfoTekstit[12].päivitäTeksti("Kulma Z (Roll): " + roll);
                    HUD.renderöiTeksti(debugInfoTekstit[12], sijx, 320, window);
        
                }
                catch (NullPointerException npe) {
                    System.out.println("Debug-tekstin näyttämisessä virhe");
                    npe.printStackTrace();
                }
            }
        }
        
        public static void renderöiLisäMoodiTekstit(Ikkuna window) {
            int sijx = (int)(window.getWidth()/5.5);
            if (moonJump) {
                lisäMoodiTeksti.päivitäTeksti("Moonjump");
                HUD.renderöiTeksti(lisäMoodiTeksti, sijx, 500, window);
            }
        }
    }
}
