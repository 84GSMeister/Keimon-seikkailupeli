package keimo.seikkailupeli.assets;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.grafiikat.Animaatio;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.objekti3d.Model3D;
import keimo.keimoengine.grafiikat.objekti3d.TextureCache;
import keimo.seikkailupeli.objektit.Käännettävä.Suunta;
import keimo.seikkailupeli.objektit.Käännettävä.SuuntaVasenOikea;
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
    private static HashMap<String, Renderöitävä> tekstuurit = new HashMap<>();
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

    public static Renderöitävä annaTekstuuri(String nimi) {
        if (tekstuurit.containsKey(nimi)) {
            return tekstuurit.get(nimi);
        }
        else {
            assert false : "Ääntä ei löytynyt" + nimi;
            return tekstuurit.get("vakio");
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

    public static void lataaTekstuurit() {
        // tekstuurit.put("vakio", new Tekstuuri("vakio"));

        // Objekteihin liittyvät tekstuurit
        tekstuurit.put("avain", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/avain.png"));
        tekstuurit.put("baariovi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/oviruutu_baari.png"));
        tekstuurit.put("baariruutu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/baariruutu.png"));
        tekstuurit.put("hiili", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/hiili.png"));
        tekstuurit.put("huume", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/huume.png"));
        tekstuurit.put("portti", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/portti.png"));
        tekstuurit.put("portti_auki", new Animaatio(30, "tiedostot/kuvat/kenttäkohteet/portti_auki.gif", 1));
        tekstuurit.put("jallupullo", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/jallupullo.png"));
        tekstuurit.put("juhani", new Animaatio("tiedostot/kuvat/kenttäkohteet/juhani.gif"));
        tekstuurit.put("juhani_dialogi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/juhani_dialogi.png"));
        tekstuurit.put("jumalvelho", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/velho.png"));
        tekstuurit.put("jumalvelho_dialogi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/velho_dialogi.png"));
        tekstuurit.put("jumalyoda", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/yoda.png"));
        tekstuurit.put("jumalyoda_dialogi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png"));
        tekstuurit.put("jumalyoda_goblin", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/goblin.png"));
        tekstuurit.put("jumalyoda_goblin_dialogi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/goblin_dialogi.png"));
        tekstuurit.put("juomalasi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/juomalasi.png"));
        tekstuurit.put("juomalasi_olut", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/juomalasi_olut.png"));
        tekstuurit.put("juomalasi_lonkero", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/juomalasi_lonkero.png"));
        tekstuurit.put("juomalasi_siideri", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/juomalasi_siideri.png"));
        tekstuurit.put("juomalasi_kuuolut", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/juomalasi_kuuolut.png"));
        tekstuurit.put("kaasupullo", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kaasupullo.png"));
        tekstuurit.put("kaasusytytin", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kaasusytytin.png"));
        tekstuurit.put("kaasusytytin_tyhjä", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kaasusytytin_tyhjä.png"));
        tekstuurit.put("kalja-automaatti", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kalja-automaatti.png"));
        tekstuurit.put("kartta", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kartta.png"));
        tekstuurit.put("kauppahylly", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly.png"));
        tekstuurit.put("kauppahylly_Kaasusytytin", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_Kaasusytytin.png"));
        tekstuurit.put("kauppahylly_Kuparilager", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_Kuparilager.png"));
        tekstuurit.put("kauppahylly_kuvavirhe", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_kuvavirhe.png"));
        tekstuurit.put("kauppahylly_Makkara", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_Makkara.png"));
        tekstuurit.put("kauppahylly_Pontikka-ainekset", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_Pontikka-ainekset.png"));
        tekstuurit.put("kauppahylly_Suklaalevy", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_Suklaalevy.png"));
        tekstuurit.put("kauppaovi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/oviruutu_kauppa.png"));
        tekstuurit.put("kaupparuutu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kaupparuutu.png"));
        tekstuurit.put("kauppias", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppias.png"));
        tekstuurit.put("kauppias_dialogi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png"));
        tekstuurit.put("kirstu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kirstu.png"));
        tekstuurit.put("kirstu_avattu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kirstu_avattu.png"));
        tekstuurit.put("kolikko", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kolikko.png"));
        tekstuurit.put("koristeovi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/koristeovi.png"));
        tekstuurit.put("kuparilager", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kuparilager.png"));
        tekstuurit.put("kuuhahmo1", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kuuhahmo_1.png"));
        tekstuurit.put("kuuhahmo2", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kuuhahmo_2.png"));
        tekstuurit.put("kuuhahmo3", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kuuhahmo_3.png"));
        tekstuurit.put("makkarat", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/makkarat.png"));
        tekstuurit.put("makkarat_käristetty", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/makkarat_käristetty.png"));
        tekstuurit.put("makkarat_paistettu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/makkarat_paistettu.png"));
        tekstuurit.put("nappi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nappi.png"));
        tekstuurit.put("nappi_painettu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nappi_painettu.png"));
        tekstuurit.put("nuotio", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nuotio.png"));
        tekstuurit.put("nuotio_sammunut", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nuotio_sammunut.png"));
        tekstuurit.put("oviruutu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/oviruutu_normaali.png"));
        tekstuurit.put("painelaatta", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta.png"));
        tekstuurit.put("painelaatta_pahavihu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_pahavihu.png"));
        tekstuurit.put("painelaatta_pahavihu_painettu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_pahavihu_painettu.png"));
        tekstuurit.put("painelaatta_pikkuvihu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu.png"));
        tekstuurit.put("painelaatta_pikkuvihu_painettu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu_painettu.png"));
        tekstuurit.put("paperi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/paperi.png"));
        tekstuurit.put("pasi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pasi.png"));
        tekstuurit.put("pasi_dialogi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/pasi_dialogi.png"));
        tekstuurit.put("paskanmarjabooli", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/paskanmarjabooli.png"));
        tekstuurit.put("paskanmarjat", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/paskanmarjat.png"));
        tekstuurit.put("pelikone", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pelikone.png"));
        tekstuurit.put("pesäpallomaila", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pesäpallomaila.png"));
        tekstuurit.put("ponuainekset", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/ponuainekset.png"));
        tekstuurit.put("puistonpenkki", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puistonpenkki.png"));
        tekstuurit.put("pullonpalautus_idle", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png"));
        tekstuurit.put("pullonpalautus_aktiivinen", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_aktiivinen.png"));
        tekstuurit.put("pullonpalautus_virhe", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_virhe.png"));
        tekstuurit.put("puuovi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puuovi.png"));
        tekstuurit.put("puuovi_avattu", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puuovi_avattu.png"));
        tekstuurit.put("salaovi", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/oviruutu_sala.png"));
        tekstuurit.put("seteli", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/seteli.png"));
        tekstuurit.put("sieni", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/sieni.png"));
        tekstuurit.put("silta", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/asfaltti_silta.png"));
        tekstuurit.put("suklaalevy", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/suklaalevy.png"));
        tekstuurit.put("sänky", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/sänky.png"));
        tekstuurit.put("tynnyri", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/tynnyri.png"));
        tekstuurit.put("vesiämpäri", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png"));
        tekstuurit.put("ämpärikone", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/ämpärikone.png"));

        // Valikoiden tekstuurit
        tekstuurit.put("menu_main_aloita", new Tekstuuri("tiedostot/kuvat/menu/main_aloita.png"));
        tekstuurit.put("menu_main_asetukset", new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png"));
        tekstuurit.put("menu_main_editori", new Tekstuuri("tiedostot/kuvat/menu/main_editori.png"));
        tekstuurit.put("menu_main_kehittäjät", new Tekstuuri("tiedostot/kuvat/menu/main_kehittäjät.png"));
        tekstuurit.put("menu_main_lopeta", new Tekstuuri("tiedostot/kuvat/menu/main_lopeta.png"));
        tekstuurit.put("menu_asetukset_hyväksy", new Tekstuuri("tiedostot/kuvat/menu/asetukset_hyväksy.png"));
        tekstuurit.put("menu_asetukset_takaisin", new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png"));
        tekstuurit.put("menu_loppu_uusipeli", new Tekstuuri("tiedostot/kuvat/menu/loppu_uusipeli.png"));
        tekstuurit.put("menu_osoitin", new Animaatio("tiedostot/kuvat/menu/osoitin.gif"));
        tekstuurit.put("menu_tyhjä", new Tekstuuri("tiedostot/kuvat/menu/tyhjä.png"));

        tekstuurit.put("ikkuna_kehys", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/toimintoikkuna_kehys.png"));
        tekstuurit.put("ikkuna_kehys_musta", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/toimintoikkuna_kehys_valikko.png"));
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
        //Musat.luoMusasäie();
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
        lisääMusa("minipeli_pokeri", new File("tiedostot/musat/minipeli_pokeri.ogg"));
        lisääMusa("minipeli_tetris", new File("tiedostot/musat/minipeli_tetris.ogg"));
        lisääMusa("minipeli_pong", new File("tiedostot/musat/minipeli_pong.ogg"));
    }
}
