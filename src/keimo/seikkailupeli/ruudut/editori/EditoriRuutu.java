package keimo.seikkailupeli.ruudut.editori;

import keimo.keimoengine.assets.GUITekstuurit;
import keimo.keimoengine.collision.AABB;
import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.*;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.gui.hud.HUD;
import keimo.seikkailupeli.kenttä.Tausta;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.entityt.npc.Boss;
import keimo.seikkailupeli.objektit.entityt.npc.NPC;
import keimo.seikkailupeli.objektit.entityt.npc.Vihollinen;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä.Kerättävä;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Kiintopiste;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.objektit.maastot.Tile;
import keimo.seikkailupeli.ruudut.editori.dialogieditori.DialogiEditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.gui.EditorinValikko;
import keimo.seikkailupeli.ruudut.editori.gui.HuoneenLuontiIkkuna;
import keimo.seikkailupeli.ruudut.editori.gui.LatausIkkuna;
import keimo.seikkailupeli.ruudut.editori.gui.MuokkausIkkuna;
import keimo.seikkailupeli.ruudut.editori.gui.ObjektiValikkoIkkuna;
import keimo.seikkailupeli.ruudut.editori.gui.PopupValikko;
import keimo.seikkailupeli.ruudut.editori.gui.TietoIkkuna;
import keimo.seikkailupeli.ruudut.editori.gui.TileTooltip;
import keimo.seikkailupeli.ruudut.editori.gui.yläpalkki.Yläpalkki;
import keimo.seikkailupeli.ruudut.editori.gui.yläpalkki.Yläpalkki.Välilehdet;
import keimo.seikkailupeli.ruudut.editori.tarinaeditori.TarinaEditoriIkkuna;
import keimo.seikkailupeli.äänet.Musat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class EditoriRuutu {
    private static int viewX;
	private static int viewY;
    public static ArrayList<Maasto> tilet = new ArrayList<>();
    public static ArrayList<String> taustakuvat = new ArrayList<>();
    public static AABB[][] boundingBoxes;
    private static Shader objektiShader;
    public static Shader objekti3dShader;
    public static Shader esineShader;
    private static Shader kiintopisteShader;
    private static Shader tileShader;
    private static Shader entityShader;
    private static Shader valikkoShader;
    public static int tileMäärä, objektiMäärä, entityMäärä;
    public static float rotZ = 0;
    public static boolean debugTiedotNäkyvissä = false;
    public static boolean estäVahinkoPainallukset = false;
    private static boolean grafiikatAlustettu = false;

	private static Renderöitävä virheTekstuuri = Assets.annaTekstuuri("vakio");
    private static Renderöitävä tyhjäTileTekstuuri = Assets.annaTekstuuri("editori_tyhjä_tile");
    private static Renderöitävä entityHpPalkkiPunainenTekstuuri = GUITekstuurit.annaTekstuuri("palkki_punainen");
    private static Renderöitävä entityHpPalkkiVihreäTekstuuri = GUITekstuurit.annaTekstuuri("palkki_vihreä");
	public static float fade = 0f;
    public static HashMap<Integer, Huone> editorinHuoneKartta = new HashMap<>();

    public static boolean kenttäNäkyvissä = true;
    public static boolean maastoNäkyvissä = true;
    public static boolean entitytNäkyvissä = true;
    public static boolean taustaNäkyvissä = true;
    public static float zoom = 1;
    public static Huone ladattuHuone;
    public static int ladatunHuoneenId;
    public static PeliObjekti valittuEsine;
    public static String valitunEsineenNimi = "";
    public static String valitunMaastonKuva = "";
    public static ArrayList<String> kopioidunEsineenOminaisuudet;
    public static boolean kopioitu = false;

    public static int hoverX = 0;
    public static int hoverY = 0;
    public static int kameranSijX = 0;
    public static int kameranSijY = 0;
    public static int tileX = 0;
    public static int tileY = 0;
    public static int scroll = 0;

    public static int kääntöAsteet = 0;
    public static boolean peilausX = false;
    public static boolean peilausY = false;

    public static boolean popupNäkyvissä = false;
    public static boolean objektiValikkoAuki = false;
    public static boolean tietoIkkunaAuki = false;
    public static boolean muokkausIkkunaAuki = false;
    public static boolean huoneenLuontiIkkunaAuki = false;
    public static boolean dialogiEditoriAuki = false;
    public static boolean tarinaEditoriAuki = false;
    private static Renderöitävä hoverTileTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    public static PeliObjekti tarkistettavaEsine;
    public static boolean kopiointi = false;
    public static boolean käytäKopioitujaOminaisuuksia = false;
    private static TileTooltip tileTooltip;
    public static boolean näytäTileTooltip = true;
    
    public static enum EditorinTilat {
        MAAILMA,
        YLÄPALKKI,
        POPUP,
        OBJEKTIVALIKKO,
        TIETOIKKUNA,
        MUOKKAUSIKKUNA,
        HUONEENLUONTIIKKUNA,
        VALIKKO,
        LATAUS,
        DIALOGIEDITORI,
        TARINAEDITORI;
    }
    public static EditorinTilat aktiivinenKomponentti = EditorinTilat.MAAILMA;

    public static void luoEditoriRuutu() {
        kopioiHuonekarttaEditoriin();
        createWorld();
        ObjektiValikkoIkkuna.luoObjektiValikko();
        TarinaEditoriIkkuna.kopioiTarinakarttaEditoriin();
        DialogiEditoriRuutu.kopioiDialogikarttaEditoriin();
        valittuEsine = KenttäKohde.luoObjektiTiedoilla("Avain", 0, 0, null);
        Yläpalkki.asetaValittuObjekti(valittuEsine);
    }

    private static void alustaGrafiikat() {
        if (!grafiikatAlustettu) {
            luoEditoriRuutu();
            lataaHuone(0);
            EditorinValikko.suljeValikko();
            tileTooltip = new TileTooltip("X, Y");
            objektiShader = new Shader("shader");
            objekti3dShader = new Shader("shader");
            esineShader = new Shader("shader");
            kiintopisteShader = new Shader("shader");
            tileShader = new Shader("shader");
            entityShader = new Shader("shader");
            valikkoShader = new Shader("shader");
            Yläpalkki.alustaGrafiikat();
            grafiikatAlustettu = true;
        }
    }

    public static void kopioiHuonekarttaEditoriin() {
        editorinHuoneKartta.clear();
        for (int i = 0; i < Peli.annaHuoneKartta().size(); i++) {
            Huone h = Peli.annaHuoneKartta().get(i);
            if (h != null) {
                ArrayList<KenttäKohde> kenttäKohteet = new ArrayList<>();
                for (KenttäKohde[] kk : h.annaHuoneenKenttäSisältö()) {
                    for (KenttäKohde k : kk) {
                        if (k != null) kenttäKohteet.add(KenttäKohde.luoObjektiTiedoilla(k.annaNimi(), k.annaSijX(), k.annaSijY(), k.annaLisäOminaisuudet()));
                    }
                }
                ArrayList<Maasto> maastot = new ArrayList<>();
                for (Maasto[] mm : h.annaHuoneenMaastoSisältö()) {
                    for (Maasto m : mm) {
                        maastot.add(m);
                    }
                }
                ArrayList<Entity> entityt = new ArrayList<>();
                for (Entity[] ee : h.annaHuoneenNPCSisältö()) {
                    for (Entity e : ee) {
                        if (e != null) entityt.add(Entity.luoEntityTiedoilla(e.annaNimi(), e.annaSijX(), e.annaAlkuSijY(), e.annaLisäOminaisuudet()));
                    }
                }
                Huone uusiHuone = new Huone(h.annaId(),
                                            h.annaKoko(),
                                            h.annaNimi(),
                                            h.annaTaustanPolku(),
                                            h.annaAlue(),
                                            kenttäKohteet,
                                            maastot,
                                            entityt,
                                            h.annaHuoneenMusa(),
                                            h.annaTarinaRuudunTunniste(),
                                            h.annaVaaditunTavoitteenTunniste());
                uusiHuone.päivitäReunawarppienTiedot(h.annaReunaWarppiTiedot(Suunta.VASEN),
                                                    h.annaReunaWarpinKohdeId(Suunta.VASEN),
                                                    h.annaReunaWarppiTiedot(Suunta.OIKEA),
                                                    h.annaReunaWarpinKohdeId(Suunta.OIKEA),
                                                    h.annaReunaWarppiTiedot(Suunta.ALAS),
                                                    h.annaReunaWarpinKohdeId(Suunta.ALAS),
                                                    h.annaReunaWarppiTiedot(Suunta.YLÖS),
                                                    h.annaReunaWarpinKohdeId(Suunta.YLÖS));
                editorinHuoneKartta.put(i, uusiHuone);
            }
        }
    }

    public static void kopioiEditorinHuonekarttaPeliin() {
        Peli.annaHuoneKartta().clear();
        for (int i = 0; i < editorinHuoneKartta.size(); i++) {
            Huone h = editorinHuoneKartta.get(i);
            if (h != null) {
                ArrayList<KenttäKohde> kenttäKohteet = new ArrayList<>();
                for (KenttäKohde[] kk : h.annaHuoneenKenttäSisältö()) {
                    for (KenttäKohde k : kk) {
                        if (k != null) kenttäKohteet.add(KenttäKohde.luoObjektiTiedoilla(k.annaNimi(), k.annaSijX(), k.annaSijY(), k.annaLisäOminaisuudet()));
                    }
                }
                ArrayList<Maasto> maastot = new ArrayList<>();
                for (Maasto[] mm : h.annaHuoneenMaastoSisältö()) {
                    for (Maasto m : mm) {
                        maastot.add(m);
                    }
                }
                ArrayList<Entity> entityt = new ArrayList<>();
                for (Entity[] ee : h.annaHuoneenNPCSisältö()) {
                    for (Entity e : ee) {
                        if (e != null) entityt.add(Entity.luoEntityTiedoilla(e.annaNimi(), e.annaSijX(), e.annaAlkuSijY(), e.annaLisäOminaisuudet()));
                    }
                }
                Huone uusiHuone = new Huone(h.annaId(),
                                            h.annaKoko(),
                                            h.annaNimi(),
                                            h.annaTaustanPolku(),
                                            h.annaAlue(),
                                            kenttäKohteet,
                                            maastot,
                                            entityt,
                                            h.annaHuoneenMusa(),
                                            h.annaTarinaRuudunTunniste(),
                                            h.annaVaaditunTavoitteenTunniste());
                uusiHuone.päivitäReunawarppienTiedot(h.annaReunaWarppiTiedot(Suunta.VASEN),
                                                    h.annaReunaWarpinKohdeId(Suunta.VASEN),
                                                    h.annaReunaWarppiTiedot(Suunta.OIKEA),
                                                    h.annaReunaWarpinKohdeId(Suunta.OIKEA),
                                                    h.annaReunaWarppiTiedot(Suunta.ALAS),
                                                    h.annaReunaWarpinKohdeId(Suunta.ALAS),
                                                    h.annaReunaWarppiTiedot(Suunta.YLÖS),
                                                    h.annaReunaWarpinKohdeId(Suunta.YLÖS));
                Peli.annaHuoneKartta().put(i, uusiHuone);
            }
        }
    }

    private static void createWorld() {
        for (Huone huone : editorinHuoneKartta.values()) {
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
                        }
                    }
                }
            }
            if (huone.annaTaustanPolku() != null && huone.annaTaustanPolku() != "") {
                taustakuvat.add(huone.annaTaustanPolku());
            }
        }

        DebugTeksti.luoDebugTekstit();
    }

    public static void lataaHuone(int huoneenId) {
        lataaHuone(huoneenId, false);
    }

    public static void lataaHuone(int huoneenId, boolean haeTaaksepäin) {
        if (editorinHuoneKartta.containsKey(huoneenId)) {
            ladatunHuoneenId = huoneenId;
            if (editorinHuoneKartta.get(huoneenId) != null) {
                ladattuHuone = editorinHuoneKartta.get(huoneenId);
                Musat.toistaPeliMusa(ladattuHuone.annaHuoneenMusa());
            }
        }
        else {
            if (haeTaaksepäin) {
                for (int i = huoneenId; i >= 0; i--) {
                    if (editorinHuoneKartta.containsKey(i)) {
                        ladatunHuoneenId = i;
                        if (editorinHuoneKartta.get(i) != null) {
                            ladattuHuone = editorinHuoneKartta.get(i);
                            Musat.toistaPeliMusa(ladattuHuone.annaHuoneenMusa());
                            break;
                        }
                    }
                }
            }
            else {
                for (int i = huoneenId; i < editorinHuoneKartta.size(); i++) {
                    if (editorinHuoneKartta.containsKey(i)) {
                        ladatunHuoneenId = i;
                        if (editorinHuoneKartta.get(i) != null) {
                            ladattuHuone = editorinHuoneKartta.get(i);
                            Musat.toistaPeliMusa(ladattuHuone.annaHuoneenMusa());
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void renderöi(Ikkuna ikkuna, Kamera kamera) {
        alustaGrafiikat();
        try {
            tileMäärä = 0; objektiMäärä = 0; entityMäärä = 0;
            int posX = kameranSijX / 2;// / (scale * 2);
            int posY = kameranSijY / 2;// / (scale * 2);
            Matrix4f cameraMatrix = kamera.getPerspectiveView(ikkuna, zoom);
            cameraMatrix = asetaKameranSijaintiVanha(cameraMatrix, ikkuna);

            laskeNäköetäisyys(ikkuna);
            if (taustaNäkyvissä) renderöiTausta(0, 0, 1, new Matrix4f(), fade);

            if (maastoNäkyvissä) {
                int etäisyys = laskeIsonLaatanNäköetäisyys();
                for (int y = 0; y < etäisyys; y++) {
                    for (int x = 0; x < etäisyys; x++) {
                        int renderX = x-posX-etäisyys/2 +1;
                        int renderY = y+posY-etäisyys/2 +1;
                        int maxX = ladattuHuone.annaHuoneenMaastoSisältö().length;
                        int maxY = ladattuHuone.annaHuoneenMaastoSisältö().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            Maasto m = ladattuHuone.annaHuoneenMaastoSisältö()[renderX][renderY];
                            if (m instanceof Tile) {
                                Tile t = (Tile)m;
                                if (t != null) {
                                    if (t.annaLeveys() > 1 || t.annaKorkeus() > 1) {
                                        renderöiIsoLaatta(t, renderX, -renderY, 0, cameraMatrix);
                                        tileMäärä++;
                                    }
                                }
                            }
                        }
                    }
                }
                for (int y = 0; y < viewY; y++) {
                    for (int x = 0; x < viewX; x++) {
                        int renderX = x-posX-viewX/2 +1;
                        int renderY = y+posY-viewY/2 +1;
                        int maxX = ladattuHuone.annaHuoneenMaastoSisältö().length;
                        int maxY = ladattuHuone.annaHuoneenMaastoSisältö().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            Maasto m = ladattuHuone.annaHuoneenMaastoSisältö()[renderX][renderY];
                            if (m instanceof Tile) {
                                Tile t = (Tile)m;
                                if (t != null) {
                                    if (t.annaLeveys() == 1 && t.annaKorkeus() == 1) {
                                        renderöiTile(t, renderX, -renderY, 0, cameraMatrix);
                                        tileMäärä++;
                                    }
                                }
                            }
                            else if (m == null) {
                                renderöiTyhjäTile(renderX, -renderY, 0, cameraMatrix);
                            }
                        }
                    }
                }
            }
            if (entitytNäkyvissä) {
                synchronized (Peli.entityLista) {
                    for (Entity[] ee : ladattuHuone.annaHuoneenNPCSisältö()) {
                        for (Entity e : ee) {
                            if (e != null) {
                                renderöiEntity(e, (int)e.hitbox.getMinX(), (int)-e.hitbox.getMinY(), 0, cameraMatrix);
                                entityMäärä++;
                            }
                        }
                    }
                }
            }
            if (kenttäNäkyvissä) {
                for (int y = 0; y < viewY; y++) {
                    for (int x = 0; x < viewX; x++) {
                        int renderX = x-posX-viewX/2 +1;
                        int renderY = y+posY-viewY/2 +1;
                        int maxX = ladattuHuone.annaHuoneenKenttäSisältö().length;
                        int maxY = ladattuHuone.annaHuoneenKenttäSisältö().length;
                        if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                            KenttäKohde k = ladattuHuone.annaHuoneenKenttäSisältö()[renderX][renderY];
                            if (k != null) {
                                if (k.onkoKolmiUlotteinen()) renderöi3dKenttäObjekti(k, renderX, -renderY, 1, cameraMatrix);
                                else renderöiKenttäObjekti(k, renderX, -renderY, 1, cameraMatrix);
                                objektiMäärä++;
                            }
                        }
                    }
                }
            }
            //KenttäShaderEfektit.luoErikoisEfektit();
            //KenttäShaderEfektit.luoKenttäVäriEfekti();

            //KenttäShaderEfektit.renderöiKenttäVäriEfekti(objektiShader);
            //KenttäShaderEfektit.renderöiKenttäVäriEfekti(objekti3dShader);
            //KenttäShaderEfektit.renderöiKenttäVäriEfekti(kiintopisteShader);
            //KenttäShaderEfektit.renderöiKenttäVäriEfekti(esineShader);
            //KenttäShaderEfektit.renderöiKenttäVäriEfekti(tileShader);
            //KenttäShaderEfektit.renderöiKenttäVäriEfekti(entityShader);
            //KenttäShaderEfektit.kimmellysEfekti(kiintopisteShader);

            for (int y = 0; y < viewY; y++) {
                for (int x = 0; x < viewX; x++) {
                    int renderX = x-posX-viewX/2 +1;
                    int renderY = y+posY-viewY/2 +1;
                    int maxX = ladattuHuone.annaHuoneenMaastoSisältö().length;
                    int maxY = ladattuHuone.annaHuoneenMaastoSisältö().length;
                    if (renderX >= 0 && renderY >= 0 && renderX < maxX && renderY < maxY) {
                        //KenttäShaderEfektit.renderöiErikoisEfektit(erikoisEfektiShader, renderX, -renderY, 1, cameraMatrix);
                    }
                }
            }
            renderöiGUI(hoverX, hoverY, cameraMatrix, kameranSijX, kameranSijY, valikkoShader, ikkuna);
            DebugTeksti.renderöiDebugTeksti(ikkuna);
        }
        catch (IndexOutOfBoundsException aioobe) {
            System.out.println("koko muuttui");
            aioobe.printStackTrace();
        }
    }

    public static void laskeNäköetäisyys(Ikkuna window) {
        viewX = (int)(window.getWidth()/64f * zoom) +4;
		viewY = (int)(window.getHeight()/64f * zoom) +6;
	}

    private static int laskeIsonLaatanNäköetäisyys() {
        return ladattuHuone.annaHuoneenMaastoSisältö().length;
    }

    private static Matrix4f asetaKameranSijaintiVanha(Matrix4f cameraMatrix, Ikkuna window) {
        Matrix4f kameranSijainti = new Matrix4f(cameraMatrix);
        kameranSijainti.translate(kameranSijX, kameranSijY, 0);
        return kameranSijainti;
    }

    private static void renderöiTausta(int x, int y, int z, Matrix4f cameraMatrix, float fade) {
		if (ladattuHuone != null && ladattuHuone.annaTaustanPolku() != null) {
            Tausta.render(ladattuHuone.annaTaustanPolku(), x, y, z, cameraMatrix, fade);
		}
	}

    protected static void renderöiTile(Tile tile, int x, int y, int z, Matrix4f cameraMatrix) {
		if (Assets.annaTileTekstuurit().containsKey(tile.annaTekstuurinNimi())) Assets.annaTileTekstuurit().get(tile.annaTekstuurinNimi()).bind(0);
		else virheTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        tileShader.bind();
		tileShader.asetaSampler(0);
		tileShader.asetaSijainti(resultMatrix);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(tile.annaKääntöAsteet(), tile.annaXPeilaus(), tile.annaYPeilaus());
		model.render();
	}

    protected static void renderöiIsoLaatta(Tile tile, int x, int y, int z, Matrix4f cameraMatrix) {
        if (Assets.annaTileTekstuurit().containsKey(tile.annaTekstuurinNimi())) Assets.annaTileTekstuurit().get(tile.annaTekstuurinNimi()).bind(0);
		else virheTekstuuri.bind(0);

        int l = tile.annaLeveys();
        int k = tile.annaKorkeus();
        
        Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z)).scale(l, k, 1).translate(1f - 1f/l, -1f + 1f/k, 0);
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        tileShader.bind();
		tileShader.asetaSampler(0);
		tileShader.asetaSijainti(resultMatrix);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel(tile.annaKääntöAsteet(), tile.annaXPeilaus(), tile.annaYPeilaus());
		model.render();
	}

    protected static void renderöiTyhjäTile(int x, int y, int z, Matrix4f cameraMatrix) {
        tyhjäTileTekstuuri.bind(0);

		Matrix4f tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        tileShader.bind();
		tileShader.asetaSampler(0);
		tileShader.asetaSijainti(resultMatrix);
        tileShader.setUniform("subcolor", new Vector4f(fade, fade, fade, 0f));
		
		Model model = Assets.getModel();
		model.render();
	}

    protected static void renderöiEntity(Entity entity, int x, int y, int z, Matrix4f cameraMatrix) {
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
		entityShader.asetaSampler(0);
		entityShader.asetaSijainti(resultMatrix);
        entityShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
        entityShader.setUniform("addcolor", new Vector4f(hurtEfekti, hurtEfekti, hurtEfekti, 0));
		
		Model model;
		if (entity instanceof Boss) model = Assets.getModel(entity.suuntaVasenOikea);
        else model = Assets.getModel(entity.annaSuunta());
		model.render();

        if (entity instanceof NPC) {
            NPC npc = (NPC)entity;
            if (npc.maxHp > 0) {
                resultMatrix.translate(0, -1f, 0);
                resultMatrix.scale(1, 0.0625f, 1);
                entityShader.asetaSijainti(resultMatrix);
                entityHpPalkkiPunainenTekstuuri.bind(0);
                Assets.getModel().render();

                float offsetX = (float)npc.hp/(float)npc.maxHp;
                resultMatrix.scale(offsetX, 1, 1);
                resultMatrix.translate(-(1f - offsetX)*2, 0, 0);
                entityShader.asetaSijainti(resultMatrix);
                entityHpPalkkiVihreäTekstuuri.bind(0);
                Assets.getModel().render();
            }
        }
	}

	protected static void renderöiKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
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

    protected static void renderöiEsinePyörivä(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
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
        esineShader.asetaSijainti(resultMatrix);
        esineShader.asetaSampler(0);
        if (valittuEsine instanceof KenttäKohde && tileX == (int)x && tileY == (int)-y) esineShader.setUniform("subcolor", new Vector4f(0.5f, 0.5f, 0.5f, 0f));
        else esineShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
    }

    protected static void renderöiKiintopisteKiiluva(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        kiintopisteShader.bind();
        kiintopisteShader.asetaSijainti(resultMatrix);
        kiintopisteShader.asetaSampler(0);
        if (valittuEsine instanceof KenttäKohde && tileX == (int)x && tileY == (int)-y) kiintopisteShader.setUniform("subcolor", new Vector4f(0.5f, 0.5f, 0.5f, 0f));
        else kiintopisteShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
    }

    protected static void renderöiKenttäkohdeStaattinen(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        if (objekti.annaTekstuuri() != null) objekti.annaTekstuuri().bind(0);
        //else if (objekti instanceof VisuaalinenObjekti) ErikoisTileMuutokset.annaSpesiaaliTekstuuri(objekti.annaTekstuuri(), objekti.annaKuvanTiedostoNimi()).bind(0);
        else virheTekstuuri.bind(0);
        
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objektiShader.bind();
        objektiShader.asetaSijainti(resultMatrix);
        objektiShader.asetaSampler(0);
        objektiShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, fade));
    }

    protected static void renderöi3dKenttäObjekti(KenttäKohde objekti, float x, float y, float z, Matrix4f cameraMatrix) {
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        objekti.liikeY += objekti.annaLiikeNopeus();
        objekti.transform.getRotation().rotateAxis((float)Math.toRadians(objekti.annaPyörimisNopeus()), 0, 1, 0);
        resultMatrix.mul(objekti.transform.getTransformation());

        objekti3dShader.bind();
		objekti3dShader.asetaSijainti(resultMatrix);
        objekti3dShader.asetaSampler(0);
        if (valittuEsine instanceof KenttäKohde && tileX == (int)x && tileY == (int)-y) objekti3dShader.setUniform("subcolor", new Vector4f(0.5f, 0.5f, 0.5f, 0f));
        else objekti3dShader.setUniform("subcolor", new Vector4f(0f, 0f, 0f, 0f));
        Assets.getModel3D(objekti.anna3dMallinTunniste()).draw();
    }

    public static void painaEsc() {
        switch (aktiivinenKomponentti) {
            case MAAILMA, YLÄPALKKI -> {
                EditorinValikko.avaaValikko();
            }
            case POPUP -> {
                avaaPopup(false);
            }
            case OBJEKTIVALIKKO -> {
                avaaObjektiValikko(false);
            }
            case TIETOIKKUNA -> {
                avaaTietoIkkuna(false);
            }
            case MUOKKAUSIKKUNA -> {
                avaaMuokkausIkkuna(false);
            }
            case HUONEENLUONTIIKKUNA -> {
                HuoneenLuontiIkkuna.sulje();
            }
            case VALIKKO -> {

            }
            case LATAUS -> {

            }
            case DIALOGIEDITORI -> {
                DialogiEditoriRuutu.painaEsc();
            }
            case TARINAEDITORI -> {
                TarinaEditoriIkkuna.painaEsc();
            }
        }
    }

    public static void tarkistaHoverNapit(int hiiriX, int hiiriY) {
        switch (aktiivinenKomponentti) {
            case OBJEKTIVALIKKO -> {
                ObjektiValikkoIkkuna.tarkistaListaHover(hiiriX, hiiriY);
            }
            case TIETOIKKUNA -> {
                TietoIkkuna.tarkistaHover(hiiriX, hiiriY);
            }
            case MUOKKAUSIKKUNA -> {
                MuokkausIkkuna.tarkistaHover(hiiriX, hiiriY);
            }
            case HUONEENLUONTIIKKUNA -> {
                HuoneenLuontiIkkuna.tarkistaHover(hiiriX, hiiriY);
            }
            case POPUP -> {
                PopupValikko.tarkistaPopupHover(hiiriX, hiiriY);
            }
            case YLÄPALKKI -> {
                Yläpalkki.tarkistaYläpalkkiHover(hiiriX, hiiriY);
            }
            case MAAILMA -> {
                
            }
            case VALIKKO -> {

            }
            case LATAUS -> {

            }
            case DIALOGIEDITORI -> {
                DialogiEditoriRuutu.tarkistaHover(hiiriX, hiiriY);
            }
            case TARINAEDITORI -> {
                TarinaEditoriIkkuna.tarkistaHover(hiiriX, hiiriY);
            }
        }
    }

    public static void hiirenVasenToiminto(int hiiriX, int hiiriY) {
        switch (aktiivinenKomponentti) {
            case OBJEKTIVALIKKO -> {
                ObjektiValikkoIkkuna.tarkistaKlikkaus(hiiriX, hiiriY);
            }
            case TIETOIKKUNA -> {
                TietoIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
            case MUOKKAUSIKKUNA -> {
                MuokkausIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
            case HUONEENLUONTIIKKUNA -> {
                HuoneenLuontiIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
            case POPUP -> {
                PopupValikko.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case YLÄPALKKI -> {
                Yläpalkki.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case MAAILMA -> {
                if (kopiointi) {
                    kopioiObjekti();
                }
            }
            case VALIKKO -> {

            }
            case LATAUS -> {

            }
            case DIALOGIEDITORI -> {
                DialogiEditoriRuutu.tarkistaNapit(hiiriX, hiiriY);
            }
            case TARINAEDITORI -> {
                TarinaEditoriIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
        }
    }

    public static void hiirenOikeaToiminto(int hiiriX, int hiiriY) {
        switch (aktiivinenKomponentti) {
            case OBJEKTIVALIKKO -> {
                EditoriRuutu.avaaObjektiValikko(false);
            }
            case TIETOIKKUNA -> {
                avaaTietoIkkuna(false);
            }
            case MUOKKAUSIKKUNA -> {
                avaaMuokkausIkkuna(false);
            }
            case HUONEENLUONTIIKKUNA -> {
                avaaHuoneenLuontiIkkuna(false);
            }
            case POPUP -> {
                avaaPopup(false);
            }
            case YLÄPALKKI -> {
                
            }
            case MAAILMA -> {
                if (tileX > 0 && tileX < ladattuHuone.annaKoko() && tileY > 0 && tileY < ladattuHuone.annaKoko()) {
                    tarkistettavaEsine = ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY];
                    PopupValikko.päivitäHiirenSijainti(hiiriX, hiiriY);
                    EditoriRuutu.avaaPopup(true);
                }
            }
            case VALIKKO -> {

            }
            case null, default -> {}
        }
    }

    public static void hiirenKeskiToiminto(int hiiriX, int hiiriY) {
        
    }

    public static void hiirenVasenToimintoHold(int hiiriX, int hiiriY) {
        if (!estäVahinkoPainallukset) {
            switch (aktiivinenKomponentti) {
                case MAAILMA -> {
                    if (valitunEsineenNimi.equals("Random")) asetaRandomObjekti();
                    else asetaValittuObjekti();
                }
                case DIALOGIEDITORI -> {
                    DialogiEditoriRuutu.tarkistaHiirenRaahaus(hiiriX, hiiriY);
                }
                default -> {

                }
            }
        }
    }

    public static void hiirenOikeaToimintoHold(int hiiriX, int hiiriY) {

    }

    public static void hiirenKeskiToimintoHold(int hiiriX, int hiiriY) {
        switch (aktiivinenKomponentti) {
            case MAAILMA -> {
                poistaObjekti();
            }
            default -> {

            }
        }
    }

    public static void hiirenVasenToimintoRelease(int hiiriX, int hiiriY) {
        switch (aktiivinenKomponentti) {
            case MAAILMA -> {
                estäVahinkoPainallukset = false;
            }
            default -> {
                
            }
        }
    }

    public static void scrollToiminto(int scrollX, int scrollY) {
        switch (aktiivinenKomponentti) {
            case MAAILMA -> {
                päivitäZoom(scrollY);
            }
            case DIALOGIEDITORI -> {
                DialogiEditoriRuutu.scroll(scrollY);
            }
            case TARINAEDITORI -> {
                TarinaEditoriIkkuna.scroll(scrollY);
            }
            case null, default -> {

            }
        }
    }

    private static void asetaRandomObjekti() {
        Yläpalkki.nykyinenVälilehti = Välilehdet.KENTTÄ;
        if (tileX >= 0 && tileY >= 0) {
            if (tileX < ladattuHuone.annaKoko() && tileY < ladattuHuone.annaKoko()) {
                ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY] = KenttäKohde.luoRandomKenttäKohde(tileX, tileY);
            }
        }
    }

    private static void asetaValittuObjekti() {
        try {
            Yläpalkki.nykyinenVälilehti = Välilehdet.KENTTÄ;
            if (tileX >= 0 && tileY >= 0) {
                if (tileX < ladattuHuone.annaKoko() && tileY < ladattuHuone.annaKoko()) {
                    if (valittuEsine instanceof KenttäKohde) {
                        if (kopioidunEsineenOminaisuudet == null) {
                            kopioidunEsineenOminaisuudet = new ArrayList<>();
                            kopioidunEsineenOminaisuudet.add("kääntö=" + kääntöAsteet);
                            kopioidunEsineenOminaisuudet.add("x-peilaus=" + (peilausX ? "kyllä" : "ei"));
                            kopioidunEsineenOminaisuudet.add("y-peilaus=" + (peilausY ? "kyllä" : "ei"));
                        }
                        else {
                            kopioidunEsineenOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
                            if (kääntöAsteet != 0) kopioidunEsineenOminaisuudet.add("kääntö=" + kääntöAsteet);
                            kopioidunEsineenOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
                            if (peilausX) kopioidunEsineenOminaisuudet.add("x-peilaus=" + (peilausX ? "kyllä" : "ei"));
                            kopioidunEsineenOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
                            if (peilausY) kopioidunEsineenOminaisuudet.add("y-peilaus=" + (peilausY ? "kyllä" : "ei"));
                        }
                        ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY] = KenttäKohde.luoObjektiTiedoilla(valitunEsineenNimi, tileX, tileY, kopioidunEsineenOminaisuudet);
                    }
                    else if (valittuEsine instanceof Maasto) {
                        if (valittuEsine instanceof Tile) {
                            Tile t = (Tile)valittuEsine;
                            kopioidunEsineenOminaisuudet.clear();
                            kopioidunEsineenOminaisuudet.add("kuva=" + valitunMaastonKuva);
                            kopioidunEsineenOminaisuudet.add("kääntö=" + kääntöAsteet);
                            kopioidunEsineenOminaisuudet.add("x-peilaus=" + (peilausX ? "kyllä" : "ei"));
                            kopioidunEsineenOminaisuudet.add("y-peilaus=" + (peilausY ? "kyllä" : "ei"));
                            if (t.annaLeveys() > 1 && t.annaKorkeus() > 1) {
                                kopioidunEsineenOminaisuudet.add("leveys=" + t.annaLeveys());
                                kopioidunEsineenOminaisuudet.add("korkeus=" + t.annaKorkeus());
                            }
                            ladattuHuone.annaHuoneenMaastoSisältö()[tileX][tileY] = Maasto.luoMaastoTiedoilla(valitunEsineenNimi, tileX, tileY, kopioidunEsineenOminaisuudet);
                        }
                    }
                    else if (valittuEsine instanceof Entity) {
                        ladattuHuone.annaHuoneenNPCSisältö()[tileX][tileY] = Entity.luoEntityTiedoilla(valitunEsineenNimi, tileX, tileY, kopioidunEsineenOminaisuudet);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void poistaObjekti() {
        Yläpalkki.nykyinenVälilehti = Välilehdet.KENTTÄ;
        if (tileX >= 0 && tileY >= 0) {
            if (tileX < ladattuHuone.annaKoko() && tileY < ladattuHuone.annaKoko()) {
                if (valittuEsine instanceof KenttäKohde) {
                    ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY] = null;
                }
                else if (valittuEsine instanceof Maasto) {
                    ladattuHuone.annaHuoneenMaastoSisältö()[tileX][tileY] = null;
                }
                else if (valittuEsine instanceof Entity) {
                    ladattuHuone.annaHuoneenNPCSisältö()[tileX][tileY] = null;
                }
            }
        }
    }

    private static void kopioiObjekti() {
        if (valittuEsine instanceof KenttäKohde) {
            if (ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY] != null) {
                valittuEsine = ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY];
                valitunEsineenNimi = valittuEsine.annaNimi();
                kopioidunEsineenOminaisuudet = valittuEsine.annaLisäOminaisuudet();
                kääntöAsteet = valittuEsine.annaKääntöAsteet();
                peilausX = valittuEsine.annaXPeilaus();
                peilausY = valittuEsine.annaYPeilaus();
                kopioitu = true;
            }
            else valitunEsineenNimi = "Ei valittua esinettä";
        }
        else if (valittuEsine instanceof Maasto) {
            if (ladattuHuone.annaHuoneenMaastoSisältö()[tileX][tileY] != null) {
                valittuEsine = ladattuHuone.annaHuoneenMaastoSisältö()[tileX][tileY];
                valitunEsineenNimi = valittuEsine.annaNimi();
                kopioidunEsineenOminaisuudet = valittuEsine.annaLisäOminaisuudet();
                valitunMaastonKuva = ((Maasto)valittuEsine).annaKuvanTiedostoNimi();
                kääntöAsteet = valittuEsine.annaKääntöAsteet();
                peilausX = valittuEsine.annaXPeilaus();
                peilausY = valittuEsine.annaYPeilaus();
                kopioitu = true;
            }
            else valitunEsineenNimi = "Ei valittua esinettä";
        }
        else if (valittuEsine instanceof Entity) {
            if (ladattuHuone.annaHuoneenNPCSisältö()[tileX][tileY] != null) {
                valittuEsine = ladattuHuone.annaHuoneenNPCSisältö()[tileX][tileY];
                valitunEsineenNimi = valittuEsine.annaNimi();
                kopioidunEsineenOminaisuudet = valittuEsine.annaLisäOminaisuudet();
                kääntöAsteet = valittuEsine.annaKääntöAsteet();
                peilausX = valittuEsine.annaXPeilaus();
                peilausY = valittuEsine.annaYPeilaus();
                kopioitu = true;
            }
            else valitunEsineenNimi = "Ei valittua esinettä";
        }
        Yläpalkki.asetaValittuObjekti(valittuEsine);
    }

    public static void avaaPopup(boolean avaus) {
        if (avaus) {
            if (ladattuHuone.annaHuoneenKenttäSisältö()[tileX][tileY] != null) {
                popupNäkyvissä = avaus;
                PopupValikko.avaaPopup();
                PopupValikko.popupKameraX = kameranSijX /2;
                PopupValikko.popupKameraY = kameranSijY /2;
            }
        }
        else popupNäkyvissä = avaus;
    }

    public static void avaaObjektiValikko(boolean avaus) {
        objektiValikkoAuki = avaus;
    }

    public static void avaaTietoIkkuna(boolean avaus) {
        tietoIkkunaAuki = avaus;
    }

    public static void avaaMuokkausIkkuna(boolean avaus) {
        muokkausIkkunaAuki = avaus;
    }

    public static void avaaHuoneenLuontiIkkuna(boolean avaus) {
        huoneenLuontiIkkunaAuki = avaus;
    }

    public static void päivitäFokus(int x, int y) {
        hoverX = x;
        hoverY = y;
        if (objektiValikkoAuki) {
            aktiivinenKomponentti = EditorinTilat.OBJEKTIVALIKKO;
        }
        else if (tietoIkkunaAuki) {
            aktiivinenKomponentti = EditorinTilat.TIETOIKKUNA;
        }
        else if (muokkausIkkunaAuki) {
            aktiivinenKomponentti = EditorinTilat.MUOKKAUSIKKUNA;
        }
        else if (huoneenLuontiIkkunaAuki) {
            aktiivinenKomponentti = EditorinTilat.HUONEENLUONTIIKKUNA;
        }
        else if (dialogiEditoriAuki) {
            aktiivinenKomponentti = EditorinTilat.DIALOGIEDITORI;
        }
        else if (tarinaEditoriAuki) {
            aktiivinenKomponentti = EditorinTilat.TARINAEDITORI;
        }
        else if (popupNäkyvissä) {
            aktiivinenKomponentti = EditorinTilat.POPUP;
        }
        else if (Peli.syötteenTila == Peli.SyötteenTila.TOIMINTO) {
            aktiivinenKomponentti = EditorinTilat.VALIKKO;
        }
        else if (y < 150) {
            aktiivinenKomponentti = EditorinTilat.YLÄPALKKI;
        }
        else {
            aktiivinenKomponentti = EditorinTilat.MAAILMA;
            PopupValikko.popupHiiriX = x;
            PopupValikko.popupHiiriY = y;
        }
    }

    public static void päivitäZoom(int scrollY) {
        scroll = scrollY;
        if ((zoom > 0.5 && scrollY > 0) || (zoom < 9.9 && scrollY < 0)) {
            zoom += scroll * -0.1f;
        }
    }

    private static void päivitäHoverTile(float hiiriX, float hiiriY, Matrix4f cameraMatrix, int kameranSijX, int kameranSijY, Ikkuna window) {
        int sijX = Math.round((window.getWidth()/2-hiiriX) / (-64f/zoom) - kameranSijX/2f);
        int sijY = Math.round((window.getHeight()/2-hiiriY) / (64f/zoom) - kameranSijY/2f);
        tileX = sijX;
        tileY = -sijY;
        Matrix4f objektinSijainti = new Matrix4f().translate(new Vector3f(sijX * 2, sijY * 2, 0));
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(objektinSijainti);

        valikkoShader.bind();
		valikkoShader.asetaSijainti(resultMatrix);
        valikkoShader.asetaSampler(0);
        valikkoShader.setUniform("subcolor", new Vector4f(1, 1, 1, 0.5f));
        hoverTileTekstuuri.bind(0);
        Assets.getModel().render();
    }

    private static void päivitäTileTooltip(Shader shader, Ikkuna window) {
        tileTooltip.päivitäSijainti(hoverX, hoverY);
        tileTooltip.päivitäTeksti("" + tileX + ", " + tileY);
        if (tileX < 0 || tileY < 0 || tileX >= ladattuHuone.annaKoko() || tileY >= ladattuHuone.annaKoko()) {
            tileTooltip.päivitäVäri(Väri.red);
        }
        else tileTooltip.päivitäVäri(Väri.white);
        tileTooltip.renderöi(shader, window);
    }

    private static void renderöiGUI(float x, float y, Matrix4f cameraMatrix, int kameranSijX, int kameranSijY, Shader shader, Ikkuna window) {
        switch (aktiivinenKomponentti) {
            case MAAILMA -> {
                päivitäHoverTile(x, y, cameraMatrix, kameranSijX, kameranSijY, window);
                if (näytäTileTooltip) päivitäTileTooltip(shader, window);
            }
            case YLÄPALKKI -> {

            }
            case POPUP -> {
                PopupValikko.renderöi(shader, window);
            }
            case OBJEKTIVALIKKO -> {
                ObjektiValikkoIkkuna.renderöi(shader, window);
            }
            case TIETOIKKUNA -> {
                TietoIkkuna.renderöi(shader, window);
            }
            case MUOKKAUSIKKUNA -> {
                MuokkausIkkuna.renderöi(shader, window);
            }
            case HUONEENLUONTIIKKUNA -> {
                HuoneenLuontiIkkuna.renderöi(shader, window);
            }
            case VALIKKO -> {
                
            }
            case LATAUS -> {
                LatausIkkuna.renderöi(shader, window);
            }
            case DIALOGIEDITORI -> {
                DialogiEditoriRuutu.renderöi(shader, window);
            }
            case TARINAEDITORI -> {
                TarinaEditoriIkkuna.renderöi(shader, window);
            }
        }
        Yläpalkki.renderöi(shader, window);
    }

    public class DebugTeksti {

        static Teksti[] debugInfoTekstit = new Teksti[10];
        static Teksti lisäMoodiTeksti = new Teksti("moodi", Väri.orange, 1200, 48);
        static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
        static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

        public static void luoDebugTekstit() {
            for (int i = 0; i < debugInfoTekstit.length; i++) {
                debugInfoTekstit[i] = new Teksti("debug", Väri.cyan, 1200, 48);
            }
        }

        public static void renderöiDebugTeksti(Ikkuna window) {
            if (debugTiedotNäkyvissä) {
                try {
                    int sijx = (int)(window.getWidth()/64);
                    debugInfoTekstit[0].päivitäTeksti("Hiiri x: " + hoverX);
                    HUD.renderöiTeksti(debugInfoTekstit[0], sijx, 200, window);
                    debugInfoTekstit[1].päivitäTeksti("Hiiri y: " + hoverY);
                    HUD.renderöiTeksti(debugInfoTekstit[1], sijx, 220, window);
                    debugInfoTekstit[2].päivitäTeksti("Kamera x: " + -kameranSijX);
                    HUD.renderöiTeksti(debugInfoTekstit[2], sijx, 240, window);
                    debugInfoTekstit[3].päivitäTeksti("Kamera y: " + -kameranSijY);
                    HUD.renderöiTeksti(debugInfoTekstit[3], sijx, 260, window);
                    debugInfoTekstit[4].päivitäTeksti("Tile x: " + tileX);
                    HUD.renderöiTeksti(debugInfoTekstit[4], sijx, 280, window);
                    debugInfoTekstit[5].päivitäTeksti("Tile y: " + tileY);
                    HUD.renderöiTeksti(debugInfoTekstit[5], sijx, 300, window);
                    debugInfoTekstit[6].päivitäTeksti("Zoom +/-: " + kaksiDesimaalia.format(zoom));
                    HUD.renderöiTeksti(debugInfoTekstit[6], sijx, 320, window);
                    debugInfoTekstit[7].päivitäTeksti("Fokus: " + aktiivinenKomponentti);
                    HUD.renderöiTeksti(debugInfoTekstit[7], sijx, 340, window);
                    debugInfoTekstit[8].päivitäTeksti("(F3: Piilota)");
                    HUD.renderöiTeksti(debugInfoTekstit[8], sijx, 360, window);
                    
                }
                catch (NullPointerException npe) {
                    System.out.println("Debug-tekstin näyttämisessä virhe");
                    npe.printStackTrace();
                }
            }
        }
    }
}

