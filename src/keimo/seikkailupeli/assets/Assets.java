package keimo.seikkailupeli.assets;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.objekti3d.Model3D;
import keimo.keimoengine.grafiikat.objekti3d.TextureCache;
import keimo.seikkailupeli.objektit.Käännettävä.Suunta;
import keimo.seikkailupeli.objektit.Käännettävä.SuuntaVasenOikea;
import keimo.seikkailupeli.äänet.Musat;
import keimo.utility.ModelLoader;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_messageBox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Assets {

    private static HashMap<String, Model3D> models3d = new HashMap<>();
    private static HashMap<String, File> ääniTiedostot = new HashMap<>();
    private static List<File> tölkkiÄäniLista = new ArrayList<>();
    private static HashMap<String, File> musaTiedostot = new HashMap<>();
    private static Random random = new Random();

    public static Model3D getModel3D(String objNimi) {
        return models3d.get(objNimi);
    }

    private static void lisää3DMalli(String nimi, float skaala, boolean käännäYZ) {
        models3d.put(nimi, ModelLoader.loadModel(nimi, "tiedostot/3d-objektit/" + nimi + "/" + nimi + ".obj", skaala, käännäYZ, TextureCache.getTextureCache()));
    }

    public static Model getModel() {
        return EngineAssets.getModel();
    }

    public static Model getModel(int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        return EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus);
    }

    public static Model getModel(Suunta suunta) {
        switch (suunta) {
            case VASEN: return EngineAssets.getModel();
            case OIKEA: return EngineAssets.getModel(0, true, false);
            case ALAS: return EngineAssets.getModel(270, false, false);
            case YLÖS: return EngineAssets.getModel(90, false, false);
            case null, default: return EngineAssets.getModel(180, false, false);
        }
    }

    public static Model getModel(SuuntaVasenOikea suunta) {
        switch (suunta) {
            case VASEN: return EngineAssets.getModel();
            case OIKEA: return EngineAssets.getModel(0, true, false);
            case null, default: return EngineAssets.getModel(180, false, false); 
        }
    }

    private static void lisääÄäni(String ääni, File ääniTiedosto) {
        if (ääniTiedosto != null && ääniTiedosto.isFile()) {
            ääniTiedostot.put(ääni, ääniTiedosto);
        }
        else {
            tinyfd_messageBox("Virhe ladatessa äänitiedostoa", "Tiedostoa " + ääniTiedosto + " ei voitu ladata", "ok", "error", false);
        }
    }

    private static void lisääMusa(String musa, File ääniTiedosto) {
        if (ääniTiedosto != null && ääniTiedosto.isFile()) {
            musaTiedostot.put(musa, ääniTiedosto);
        }
        else {
            tinyfd_messageBox("Virhe ladatessa äänitiedostoa", "Tiedostoa " + ääniTiedosto + " ei voitu ladata", "ok", "error", false);
        }
    }

    public static File annaÄäni(String ääniTiedosto) {
        if (ääniTiedosto.startsWith("tölkki")) {
            return tölkkiÄäniLista.get(random.nextInt(tölkkiÄäniLista.size()));
        }
        else {
            if (ääniTiedostot.containsKey(ääniTiedosto)) {
                return ääniTiedostot.get(ääniTiedosto);
            }
            else {
                assert false : "Ääntä ei löytynyt" + ääniTiedosto;
                return ääniTiedostot.get("default");
            }
        }
    }

    public static File annaMusa(String musaTiedosto) {
        if (musaTiedostot.containsKey(musaTiedosto)) {
            return musaTiedostot.get(musaTiedosto);
        }
        else {
            assert false : "Musaa ei löytynyt" + musaTiedosto;
            return null;
        }
    }

    public static void lataa3DMallit() {
        lisää3DMalli("Tynnyri",         1.5f,    true);
        lisää3DMalli("asunto_yokyla",   0.125f,  false);
        lisää3DMalli("Sieni",           4f,      true);
        lisää3DMalli("yo-kyla",         0.005f,  true);
        lisää3DMalli("Kolikko",         3f,      true);
        lisää3DMalli("KeimoTeksti",     0.0125f, true);
        lisää3DMalli("tölkki",          0.075f,  true);
    }

    public static void lataaÄänet() {
        lisääÄäni("pelaaja_damage", new File("tiedostot/äänet/pelaaja_damage.mp3"));
        lisääÄäni("Hyökkäys", new File("tiedostot/äänet/hyökkäys.wav"));
        lisääÄäni("woof", new File("tiedostot/äänet/woof/woof.wav"));
        lisääÄäni("oven_avaus", new File("tiedostot/äänet/risitas.wav"));
        lisääÄäni("oven_sulkeminen", new File("tiedostot/äänet/ovi_kiinni.wav"));
        lisääÄäni("ammus", new File("tiedostot/äänet/ammus.wav"));
        lisääÄäni("frans_cs", new File("tiedostot/äänet/frans_cs.mp3"));
        lisääÄäni("nappi", new File("tiedostot/äänet/nappi.wav"));
        lisääÄäni("portti", new File("tiedostot/äänet/portti.wav"));
        lisääÄäni("pullo", new File("tiedostot/äänet/pullo.mp3"));
        lisääÄäni("Vesiämpäri", new File("tiedostot/äänet/vihollinen_ämpäröinti.mp3"));
        lisääÄäni("Pesäpallomaila", new File("tiedostot/äänet/vihollinen_mukilointi.mp3"));
        lisääÄäni("Pikkuvihu_damage", new File("tiedostot/äänet/Pikkuvihu_damage.wav"));
        lisääÄäni("Pahavihu_damage", new File("tiedostot/äänet/Pahavihu_damage.wav"));
        lisääÄäni("Asevihu_damage", new File("tiedostot/äänet/Asevihu_damage.wav"));
        lisääÄäni("Pomo_damage", new File("tiedostot/äänet/Boss_damage.wav"));
        lisääÄäni("Boss_death", new File("tiedostot/äänet/Boss_death.wav"));
        lisääÄäni("Kolikko", new File("tiedostot/äänet/koin.wav"));
        lisääÄäni("Kerää", new File("tiedostot/äänet/kollekt.wav"));
        lisääÄäni("Pudota", new File("tiedostot/äänet/pudota.wav"));
        lisääÄäni("Käytä", new File("tiedostot/äänet/käytä.wav"));
        lisääÄäni("Valinta", new File("tiedostot/äänet/selekt.wav"));
        lisääÄäni("Hyväksy", new File("tiedostot/äänet/akkept.wav"));
        lisääÄäni("Kartta", new File("tiedostot/äänet/kartta.ogg"));
        lisääÄäni("Juoman_kaato", new File("tiedostot/äänet/juoman_kaato.ogg"));
        lisääÄäni("Kalja_kilinä", new File("tiedostot/äänet/kalja_kilinä.ogg"));
        lisääÄäni("Tavoite_suoritettu", new File("tiedostot/äänet/tavoite_suoritettu.wav"));
        lisääÄäni("Raha2", new File("tiedostot/äänet/raha2.wav"));
        lisääÄäni("Ping", new File("tiedostot/äänet/ping.wav"));
        lisääÄäni("Pong", new File("tiedostot/äänet/pong.wav"));

        try {
            tölkkiÄäniLista = Stream.of(new File("tiedostot/äänet/tölkki").listFiles())
                .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3")))
                .collect(Collectors.toList());
                
            for (File tölkkiääni : tölkkiÄäniLista) {
                String nimi = tölkkiääni.getName();
                lisääÄäni(nimi.substring(0, nimi.length()-4), tölkkiääni);
            }
        }
        catch (Exception e) {
            System.out.println("Ei voitu ladata tölkkiääniä");
            e.printStackTrace();
        }
    }

    public static void lataaMusat() {
        Musat.luoMusasäie();
        lisääMusa("overworld", new File("tiedostot/musat/keimo_overworld.ogg"));
        lisääMusa("puisto", new File("tiedostot/musat/keimo_puisto.ogg"));
        lisääMusa("tarina", new File("tiedostot/musat/keimo_sad_tarina.ogg"));
        lisääMusa("boss", new File("tiedostot/musat/keimo_taistelu_boss.ogg"));
        lisääMusa("valikko", new File("tiedostot/musat/keimo_valikko.mp3"));
        lisääMusa("metsä", new File("tiedostot/musat/keimo_metsä.ogg"));
        lisääMusa("koti", new File("tiedostot/musat/keimo_koti.ogg"));
        lisääMusa("baari", new File("tiedostot/musat/keimo_baari.ogg"));
        lisääMusa("kauppa", new File("tiedostot/musat/keimo_kauppa.ogg"));
        lisääMusa("temppeli", new File("tiedostot/musat/keimo_temppeli.ogg"));
        lisääMusa("kuu", new File("tiedostot/musat/keimo_kuu.ogg"));
        lisääMusa("välitarina", new File("tiedostot/musat/keimo_välitarina.ogg"));
        lisääMusa("baari_sala", new File("tiedostot/musat/keimo_baari_sala.ogg"));
        lisääMusa("minipeli_kasino", new File("tiedostot/musat/minipeli_kasino.mid"));
        lisääMusa("minipeli_pokeri", new File("tiedostot/musat/minipeli_pokeri.ogg"));
        lisääMusa("minipeli_tetris", new File("tiedostot/musat/minipeli_tetris.ogg"));
        lisääMusa("minipeli_pong", new File("tiedostot/musat/minipeli_pong.ogg"));
    }
}
