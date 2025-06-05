package keimo.keimoEngine.assets;

import keimo.Utility.Käännettävä.Suunta;
import keimo.Utility.Käännettävä.SuuntaVasenOikea;
import keimo.Utility.ModelLoader;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.grafiikat.objekti3d.Model3D;
import keimo.keimoEngine.grafiikat.objekti3d.TextureCache;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Assets {

    private static Model model0, model0X, model0Y, model0XY;
    private static Model model90, model90X, model90Y, model90XY;
    private static Model model180, model180X, model180Y, model180XY;
    private static Model model270, model270X, model270Y, model270XY;
    public static TextureCache textureCache = new TextureCache();
    public static ArrayList<Model> lattiaTilet = new ArrayList<>();
    public static Model getModel() {return model0;}

    private static HashMap<String, Model3D> models3d = new HashMap<>();
    private static HashMap<String, File> ääniTiedostot = new HashMap<>();
    private static List<File> tölkkiÄäniLista = new ArrayList<>();
    private static HashMap<String, File> musaTiedostot = new HashMap<>();
    private static Random random = new Random();

    public static Model3D getModel3D(String objNimi) {
        return models3d.get(objNimi);
    }

    public static void lisää3DMalli(String nimi, float skaala, boolean käännäYZ) {
        models3d.put(nimi, ModelLoader.loadModel(nimi, "tiedostot/3d-objektit/" + nimi + "/" + nimi + ".obj", skaala, käännäYZ, textureCache));
    }

    public static Model getModel(int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        switch (kääntöAsteet) {
            default -> {
                if (xPeilaus && yPeilaus) return model0XY;
                else if (xPeilaus) return model0X;
                else if (yPeilaus) return model0Y;
                else return model0;
            }
            case 90 -> {
                if (xPeilaus && yPeilaus) return model90XY;
                else if (xPeilaus) return model90X;
                else if (yPeilaus) return model90Y;
                else return model90;
            }
            case 180 -> {
                if (xPeilaus && yPeilaus) return model180XY;
                else if (xPeilaus) return model180X;
                else if (yPeilaus) return model180Y;
                else return model180;
            }
            case 270 -> {
                if (xPeilaus && yPeilaus) return model270XY;
                else if (xPeilaus) return model270X;
                else if (yPeilaus) return model270Y;
                else return model270;
            }
        }
    }
    public static Model getModel(Suunta suunta) {
        switch (suunta) {
            case VASEN: return model0;
            case OIKEA: return model0X;
            case ALAS: return model270;
            case YLÖS: return model90;
            case null, default: return model180;
        }
    }

    public static Model getModel(SuuntaVasenOikea suunta) {
        switch (suunta) {
            case VASEN: return model0;
            case OIKEA: return model0X;
            case null, default: return model180;
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

    public static void createModels() {
        float[] vertices = new float[]{
            -1f, 1f, 0, // TOP LEFT 0
            1f, 1f, 0,  // TOP RIGHT 1
            1f, -1f, 0, // BOTTOM RIGHT 2
            -1f, -1f, 0,// BOTTOM LEFT 3
        };
        int[] indices = new int[]{0, 1, 2, 2, 3, 0};

        float[] texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
        model0 = new Model(vertices, texture, indices);
        texture = new float[]{1, 0, 0, 0, 0, 1, 1, 1,};
        model0X = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 1, 1, 1, 0, 0, 0,};
        model0Y = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 0, 1, 0, 0, 1, 0,};
        model0XY = new Model(vertices, texture, indices);

        texture = new float[]{0, 1, 0, 0, 1, 0, 1, 1,};
        model90 = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 1, 0, 0, 0, 0, 1,};
        model90X = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 0, 1, 1, 1, 1, 0,};
        model90Y = new Model(vertices, texture, indices);
        texture = new float[]{1, 0, 1, 1, 0, 1, 0, 0,};
        model90XY = new Model(vertices, texture, indices);

        texture = new float[]{1, 1, 0, 1, 0, 0, 1, 0,};
        model180 = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 1, 1, 1, 0, 0, 0,};
        model180X = new Model(vertices, texture, indices);
        texture = new float[]{1, 0, 0, 0, 0, 1, 1, 1,};
        model180Y = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
        model180XY = new Model(vertices, texture, indices);

        texture = new float[]{1, 0, 1, 1, 0, 1, 0, 0,};
        model270 = new Model(vertices, texture, indices);
        texture = new float[]{0, 0, 0, 1, 1, 1, 1, 0,};
        model270X = new Model(vertices, texture, indices);
        texture = new float[]{1, 1, 1, 0, 0, 0, 0, 1,};
        model270Y = new Model(vertices, texture, indices);
        texture = new float[]{0, 1, 0, 0, 1, 0, 1, 1,};
        model270XY = new Model(vertices, texture, indices);

        luo3DLattia();
    }

    private static void luo3DLattia() {
        for (int x = -20; x < 20; x++) {
            for (int z = -20; z < 20; z++) {
                float[] vertices = new float[]{
                    x, 0, z, // TOP LEFT 0
                    x+1, 0, z,  // TOP RIGHT 1
                    x+1, 0, z+1, // BOTTOM RIGHT 2
                    x, 0, z+1, // BOTTOM LEFT 3
                };
                int[] indices = new int[]{0, 1, 2, 2, 3, 0};
        
                float[] texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
                lattiaTilet.add(new Model(vertices, texture, indices));
            }
        }
    }

    public static void luo3DMallit() {
        lisää3DMalli("Tynnyri",         1.5f,    true);
        lisää3DMalli("asunto_yokyla",   0.125f,  false);
        lisää3DMalli("Sieni",           4f,      true);
        lisää3DMalli("yokyla_40-54",    0.005f,  true);
        lisää3DMalli("Kolikko",         3f,      true);
        lisää3DMalli("KeimoTeksti",     0.0125f, true);
        lisää3DMalli("tölkki",          0.075f,  true);
    }

    public static void lataaÄänet() {
        ääniTiedostot.put("pelaaja_damage", new File("tiedostot/äänet/pelaaja_damage.mp3"));
        ääniTiedostot.put("pikkuvihu_damage", new File("tiedostot/äänet/pikkuvihu_damage.mp3"));
        ääniTiedostot.put("Hyökkäys", new File("tiedostot/äänet/hyökkäys.wav"));
        ääniTiedostot.put("woof", new File("tiedostot/äänet/woof/woof.wav"));
        ääniTiedostot.put("oven_avaus", new File("tiedostot/äänet/risitas.wav"));
        ääniTiedostot.put("oven_sulkeminen", new File("tiedostot/äänet/ovi_kiinni.wav"));
        ääniTiedostot.put("ammus", new File("tiedostot/äänet/ammus.wav"));
        ääniTiedostot.put("frans_cs", new File("tiedostot/äänet/frans_cs.mp3"));
        ääniTiedostot.put("nappi", new File("tiedostot/äänet/nappi.wav"));
        ääniTiedostot.put("portti", new File("tiedostot/äänet/portti.wav"));
        ääniTiedostot.put("pullo", new File("tiedostot/äänet/pullo.mp3"));
        ääniTiedostot.put("Vesiämpäri", new File("tiedostot/äänet/vihollinen_ämpäröinti.mp3"));
        ääniTiedostot.put("Pesäpallomaila", new File("tiedostot/äänet/vihollinen_mukilointi.mp3"));
        ääniTiedostot.put("Pikkuvihu_damage", new File("tiedostot/äänet/Pikkuvihu_damage.wav"));
        ääniTiedostot.put("Pahavihu_damage", new File("tiedostot/äänet/Pahavihu_damage.wav"));
        ääniTiedostot.put("Asevihu_damage", new File("tiedostot/äänet/Asevihu_damage.wav"));
        ääniTiedostot.put("Pomo_damage", new File("tiedostot/äänet/Boss_damage.wav"));
        ääniTiedostot.put("Boss_death", new File("tiedostot/äänet/Boss_death.wav"));
        ääniTiedostot.put("Kolikko", new File("tiedostot/äänet/koin.wav"));
        ääniTiedostot.put("Kerää", new File("tiedostot/äänet/kollekt.wav"));
        ääniTiedostot.put("Pudota", new File("tiedostot/äänet/pudota.wav"));
        ääniTiedostot.put("Käytä", new File("tiedostot/äänet/käytä.wav"));
        ääniTiedostot.put("Valinta", new File("tiedostot/äänet/selekt.wav"));
        ääniTiedostot.put("Hyväksy", new File("tiedostot/äänet/akkept.wav"));
        ääniTiedostot.put("Kartta", new File("tiedostot/äänet/kartta.ogg"));
        ääniTiedostot.put("Juoman_kaato", new File("tiedostot/äänet/juoman_kaato.ogg"));
        ääniTiedostot.put("Kalja_kilinä", new File("tiedostot/äänet/kalja_kilinä.ogg"));
        ääniTiedostot.put("Tavoite_suoritettu", new File("tiedostot/äänet/tavoite_suoritettu.wav"));
        ääniTiedostot.put("Raha2", new File("tiedostot/äänet/raha2.wav"));

        tölkkiÄäniLista = Stream.of(new File("tiedostot/äänet/tölkki").listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3")))
            .collect(Collectors.toList());
            
        for (File tölkkiääni : tölkkiÄäniLista) {
            String nimi = tölkkiääni.getName();
            ääniTiedostot.put(nimi.substring(0, nimi.length()-4), tölkkiääni);
        }
    }

    public static void lataaMusat() {
        musaTiedostot.put("overworld", new File("tiedostot/musat/keimo_overworld.ogg"));
        musaTiedostot.put("puisto", new File("tiedostot/musat/keimo_puisto.ogg"));
        musaTiedostot.put("tarina", new File("tiedostot/musat/keimo_sad_tarina.ogg"));
        musaTiedostot.put("boss", new File("tiedostot/musat/keimo_taistelu_boss_v2.ogg"));
        musaTiedostot.put("valikko", new File("tiedostot/musat/keimo_valikko.mp3"));
        musaTiedostot.put("metsä", new File("tiedostot/musat/keimo_metsä.ogg"));
        musaTiedostot.put("koti", new File("tiedostot/musat/keimo_koti.ogg"));
        musaTiedostot.put("baari", new File("tiedostot/musat/keimo_baari.ogg"));
        musaTiedostot.put("kauppa", new File("tiedostot/musat/keimo_kauppa.ogg"));
        musaTiedostot.put("temppeli", new File("tiedostot/musat/keimo_temppeli.ogg"));
        musaTiedostot.put("kuu", new File("tiedostot/musat/keimo_kuu.ogg"));
    }
}
