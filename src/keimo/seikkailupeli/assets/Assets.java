package keimo.seikkailupeli.assets;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.grafiikat.Animaatio;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.objekti3d.Model3D;
import keimo.keimoengine.grafiikat.objekti3d.TextureCache;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.grafiikat.shaderit.TestiShader;
import keimo.keimoengine.grafiikat.shaderit.TrippiShader;
import keimo.keimoengine.grafiikat.shaderit.VäriliukuShader;
import keimo.keimoengine.grafiikat.shaderit.VärinvaihtoShader;
import keimo.keimoengine.grafiikat.shaderit.VärinvaihtoShaderKuu;
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
    private static HashMap<String, Shader> shaderit = new HashMap<>();
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
            assert false : "Tekstuuria ei löytynyt: " + nimi;
            return tekstuurit.get("vakio");
        }
    }

    public static Shader annaShader(String nimi) {
        if (shaderit.containsKey(nimi)) {
            return shaderit.get(nimi);
        }
        else {
            assert false : "Shader-ohjelmaa ei löytynyt: " + nimi;
            return shaderit.get("vakio");
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
                assert false : "Ääntä ei löytynyt: " + ääniTiedosto;
                return ääniTiedostot.get("default");
            }
        }
    }

    public static List<File> annaÄäniLista() {
        return List.of((File[])ääniTiedostot.values().toArray());
    }

    public static File annaMusa(String musaTiedosto) {
        if (musaTiedostot.containsKey(musaTiedosto)) {
            return musaTiedostot.get(musaTiedosto);
        }
        else {
            assert false : "Musaa ei löytynyt: " + musaTiedosto;
            return null;
        }
    }

    public static List<File> annaMusaLista() {
        return List.of((File[])musaTiedostot.values().toArray());
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
        tekstuurit.put("pelikone_keimoäly", new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pelikone_keimoäly.png"));
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

        // Entityiden tekstuurit
        tekstuurit.put("asevihu", new Animaatio("tiedostot/kuvat/npc/asevihu.gif"));
        tekstuurit.put("asevihu_lyöty", new Tekstuuri("tiedostot/kuvat/npc/asevihu_lyöty.png"));
        tekstuurit.put("asevihu_ämpäröity", new Tekstuuri("tiedostot/kuvat/npc/asevihu_ämpäröity.png"));
        tekstuurit.put("pahavihu", new Animaatio("tiedostot/kuvat/npc/pahavihu.gif"));
        tekstuurit.put("pahavihu_lyöty", new Tekstuuri("tiedostot/kuvat/npc/pahavihu_lyöty.png"));
        tekstuurit.put("pikkuvihu", new Animaatio("tiedostot/kuvat/npc/pikkuvihu.gif"));
        tekstuurit.put("pikkuvihu_lyöty", new Tekstuuri("tiedostot/kuvat/npc/pikkuvihu_lyöty.png"));
        tekstuurit.put("pikkuvihu_ämpäröity", new Tekstuuri("tiedostot/kuvat/npc/pikkuvihu_suutari.png"));
        tekstuurit.put("vartija_off", new Tekstuuri("tiedostot/kuvat/npc/vartija_off.png"));
        tekstuurit.put("vartija_on", new Animaatio("tiedostot/kuvat/npc/vartija_on.gif"));
        tekstuurit.put("boss_aggr", new Animaatio("tiedostot/kuvat/npc/boss/boss_aggr.gif"));
        tekstuurit.put("boss_idle", new Animaatio("tiedostot/kuvat/npc/boss/boss_idle.gif"));
        tekstuurit.put("boss_kuollut", new Tekstuuri("tiedostot/kuvat/npc/boss/boss_kuollut.png"));
        tekstuurit.put("boss_spin", new Animaatio("tiedostot/kuvat/npc/boss/boss_spin.gif"));
        tekstuurit.put("ammus", new Tekstuuri("tiedostot/kuvat/entity/ammus.png"));
        tekstuurit.put("laatikko", new Tekstuuri("tiedostot/kuvat/entity/työnnettävä_laatikko.png"));
        tekstuurit.put("laatikko_iso", new Tekstuuri("tiedostot/kuvat/entity/iso_laatikko.png"));
        tekstuurit.put("testi_entity", new Tekstuuri("tiedostot/kuvat/entity/apu_pesukone.png"));

        // Valikoiden tekstuurit
        tekstuurit.put("menu_logo", new Tekstuuri("tiedostot/kuvat/menu/KEIMON_logo.png"));
        tekstuurit.put("menu_main_aloita", new Tekstuuri("tiedostot/kuvat/menu/main_aloita.png"));
        tekstuurit.put("menu_main_asetukset", new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png"));
        tekstuurit.put("menu_main_editori", new Tekstuuri("tiedostot/kuvat/menu/main_editori.png"));
        tekstuurit.put("menu_main_kehittäjät", new Tekstuuri("tiedostot/kuvat/menu/main_kehittäjät.png"));
        tekstuurit.put("menu_main_lopeta", new Tekstuuri("tiedostot/kuvat/menu/main_lopeta.png"));
        tekstuurit.put("menu_asetukset_hyväksy", new Tekstuuri("tiedostot/kuvat/menu/asetukset_hyväksy.png"));
        tekstuurit.put("menu_asetukset_takaisin", new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png"));
        tekstuurit.put("menu_loppu_uusipeli", new Tekstuuri("tiedostot/kuvat/menu/loppu_uusipeli.png"));
        tekstuurit.put("menu_osoitin", new Tekstuuri("tiedostot/kuvat/menu/osoitin.png"));
        tekstuurit.put("menu_osoitin2", new Tekstuuri("tiedostot/kuvat/menu/osoitin2.png"));
        tekstuurit.put("menu_osoitin3", new Tekstuuri("tiedostot/kuvat/menu/osoitin3.png"));
        tekstuurit.put("menu_osoitin_vanha", new Animaatio("tiedostot/kuvat/menu/osoitin_vanha.gif"));
        tekstuurit.put("menu_tyhjä", new Tekstuuri("tiedostot/kuvat/menu/tyhjä.png"));

        // HUD
        tekstuurit.put("hud_paneeli_tyhjä", new Tekstuuri("tiedostot/kuvat/hud/paneeli_tausta_tyhjä.png"));
        tekstuurit.put("hud_paneeli_tavaraluettelo", new Tekstuuri("tiedostot/kuvat/hud/paneeli_tausta_tavaraluettelo.png"));
        tekstuurit.put("hud_hp", new Tekstuuri("tiedostot/kuvat/hud/hp_eitekstiä.png"));
        tekstuurit.put("hud_juomat", new Tekstuuri("tiedostot/kuvat/hud/juomat.png"));
        tekstuurit.put("hud_ruoka", new Tekstuuri("tiedostot/kuvat/hud/ruoka.png"));
        tekstuurit.put("hud_pelaaja0", new Tekstuuri("tiedostot/kuvat/hud/pelaaja_kuvake/pelaaja.png"));
        tekstuurit.put("hud_pelaaja1", new Tekstuuri("tiedostot/kuvat/hud/pelaaja_kuvake/pelaaja_1.png"));
        tekstuurit.put("hud_pelaaja2", new Tekstuuri("tiedostot/kuvat/hud/pelaaja_kuvake/pelaaja_2.png"));
        tekstuurit.put("hud_pelaaja3", new Tekstuuri("tiedostot/kuvat/hud/pelaaja_kuvake/pelaaja_3.png"));
        tekstuurit.put("hud_pelaaja_ylensyönti", new Tekstuuri("tiedostot/kuvat/hud/pelaaja_kuvake/pelaaja_ylensyönti.png"));
        tekstuurit.put("hud_aika", new Tekstuuri("tiedostot/kuvat/hud/aika.png"));
        tekstuurit.put("hud_rahet", new Tekstuuri("tiedostot/kuvat/hud/rahet.png"));
        tekstuurit.put("hud_tölks", new Tekstuuri("tiedostot/kuvat/hud/tölks.png"));
        tekstuurit.put("hud_tavarapaikka_valittu", new Tekstuuri("tiedostot/kuvat/hud/valittu_tavarapaikka.png"));
        tekstuurit.put("hud_tavarapaikka_yhdistettävä", new Tekstuuri("tiedostot/kuvat/hud/yhdistettävä_tavarapaikka.png"));
        tekstuurit.put("hud_tavarapaikka_1", new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_1.png"));
        tekstuurit.put("hud_tavarapaikka_2", new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_2.png"));
        tekstuurit.put("hud_tavarapaikka_3", new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_3.png"));
        tekstuurit.put("hud_tavarapaikka_4", new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_4.png"));
        tekstuurit.put("hud_tavarapaikka_5", new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_5.png"));
        tekstuurit.put("hud_tavarapaikka_6", new Tekstuuri("tiedostot/kuvat/hud/tavarapaikka_6.png"));
        tekstuurit.put("kartta_pelaajakuvake", new Tekstuuri("tiedostot/kuvat/pelaaja_og.png"));
        tekstuurit.put("kartta_asuintalot", new Tekstuuri("tiedostot/kuvat/hud/kartat/asuintalot.png"));
        tekstuurit.put("kartta_baari", new Tekstuuri("tiedostot/kuvat/hud/kartat/baari.png"));
        tekstuurit.put("kartta_baari_salahuone", new Tekstuuri("tiedostot/kuvat/hud/kartat/baari_salahuone.png"));
        tekstuurit.put("kartta_kauppa", new Tekstuuri("tiedostot/kuvat/hud/kartat/kauppa.png"));
        tekstuurit.put("kartta_koti", new Tekstuuri("tiedostot/kuvat/hud/kartat/koti.png"));
        tekstuurit.put("kartta_kuu", new Tekstuuri("tiedostot/kuvat/hud/kartat/kuu.png"));
        tekstuurit.put("kartta_metsä", new Tekstuuri("tiedostot/kuvat/hud/kartat/metsä.png"));
        tekstuurit.put("kartta_metsä_boss", new Tekstuuri("tiedostot/kuvat/hud/kartat/metsä_boss.png"));
        tekstuurit.put("kartta_pelto", new Tekstuuri("tiedostot/kuvat/hud/kartat/pelto.png"));
        tekstuurit.put("kartta_puisto", new Tekstuuri("tiedostot/kuvat/hud/kartat/puisto.png"));
        tekstuurit.put("kartta_temppeli", new Tekstuuri("tiedostot/kuvat/hud/kartat/temppeli.png"));
        tekstuurit.put("kartta_temppeli_boss", new Tekstuuri("tiedostot/kuvat/hud/kartat/temppeli_boss.png"));
        tekstuurit.put("kartta_yo-kylä", new Tekstuuri("tiedostot/kuvat/hud/kartat/yo-kylä.png"));
        tekstuurit.put("kartta_eikarttaa", new Tekstuuri("tiedostot/kuvat/hud/kartat/ei_karttaa.png"));
        tekstuurit.put("hud_seuraava_tavoite", new Tekstuuri("tiedostot/kuvat/hud/seuraavatavoite.png"));
        tekstuurit.put("dialogi_kuvake_kehys", new Tekstuuri("tiedostot/kuvat/hud/dialogi_kuvake_kehys.png"));
        tekstuurit.put("dialogi_teksti_kehys", new Tekstuuri("tiedostot/kuvat/hud/dialogi_teksti_kehys.png"));
        tekstuurit.put("dialogi_nimi_kehys", new Tekstuuri("tiedostot/kuvat/hud/dialogi_nimi_kehys.png"));

        // Toimintoikkunat ja minipelit
        tekstuurit.put("ikkuna_kehys", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/toimintoikkuna_kehys.png"));
        tekstuurit.put("ikkuna_kehys_musta", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/toimintoikkuna_kehys_valikko.png"));
        tekstuurit.put("toimintoikkuna_ämpärijono", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/ämpärijono.png"));
        tekstuurit.put("toimintoikkuna_pullonpalautus", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/pullonpalautus.png"));
        tekstuurit.put("minipeli_kehys", new Tekstuuri("tiedostot/kuvat/gui/minipelit/minipeli_kehys.png"));
        tekstuurit.put("minipeli_pong_alkuruutu", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pong/alkuruutu.png"));
        tekstuurit.put("minipeli_pong_valkoinen", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pong/valkoinen.png"));
        tekstuurit.put("minipeli_pokeri_alkuruutu", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/alkuruutu.png"));
        tekstuurit.put("minipeli_pokeri_teksti_voitto", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/teksti_voitto.png"));
        tekstuurit.put("minipeli_pokeri_teksti_häviö", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/teksti_häviö.png"));
        tekstuurit.put("minipeli_pokeri_teksti_tasapeli", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/teksti_tasapeli.png"));
        tekstuurit.put("minipeli_pokeri_nappi_pidä", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/nappi_pidä.png"));
        tekstuurit.put("minipeli_pokeri_nappi_vaihda", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/nappi_vaihda.png"));
        tekstuurit.put("minipeli_pokeri_kortti_selkä", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortti_selkä.png"));
        tekstuurit.put("minipeli_pokeri_kortti_velho", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_velho.png"));
        tekstuurit.put("minipeli_pokeri_kortti_juhani", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_juhani.png"));
        tekstuurit.put("minipeli_pokeri_kortti_goblini", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_goblini.png"));
        tekstuurit.put("minipeli_pokeri_kortti_pasi", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_pasi.png"));
        tekstuurit.put("minipeli_pokeri_kortti_kauppias", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_kauppias.png"));
        tekstuurit.put("minipeli_pokeri_kortti_keimo", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_keimo.png"));
        tekstuurit.put("minipeli_pokeri_kortti_pahavihu", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_pahavihu.png"));
        tekstuurit.put("minipeli_pokeri_kortti_pikkuvihu", new Tekstuuri("tiedostot/kuvat/gui/minipelit/pokeri/kortit/kortti_pikkuvihu.png"));
        tekstuurit.put("minipeli_tetris_alkuruutu", new Tekstuuri("tiedostot/kuvat/gui/minipelit/tetris/alkuruutu.png"));
        tekstuurit.put("minipeli_tetris_palikka", new Tekstuuri("tiedostot/kuvat/gui/minipelit/tetris/palikka.png"));
        tekstuurit.put("minipeli_tetris_hud", new Tekstuuri("tiedostot/kuvat/gui/minipelit/tetris/tetris_hud.png"));
        tekstuurit.put("minipeli_keimoäly_tausta", new Tekstuuri("tiedostot/kuvat/gui/minipelit/keimoäly/chat_tausta.png"));
        tekstuurit.put("minipeli_keimoäly_puhekupla_vastaus", new Tekstuuri("tiedostot/kuvat/gui/minipelit/keimoäly/puhekupla_vastaus.png"));
        tekstuurit.put("minipeli_keimoäly_puhekupla_kysymys", new Tekstuuri("tiedostot/kuvat/gui/minipelit/keimoäly/puhekupla_kysymys.png"));
        tekstuurit.put("isokartta", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/kartta/kartta.png"));
        tekstuurit.put("isokartta_tyhjä", new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/kartta/kartta_pohja_kädet.png"));

        // Valikot ja asetukset
        tekstuurit.put("asetukset_ohjaimet_xbox", new Tekstuuri("tiedostot/kuvat/gui/asetukset/ohjainkuvakkeet_xbox.png"));
        tekstuurit.put("asetukset_ohjaimet_nintendo", new Tekstuuri("tiedostot/kuvat/gui/asetukset/ohjainkuvakkeet_nintendo.png"));
        tekstuurit.put("asetukset_ohjaimet_playstation", new Tekstuuri("tiedostot/kuvat/gui/asetukset/ohjainkuvakkeet_playstation.png"));

        // Näppäin- ja Ohjainkuvakkeet
        tekstuurit.put("näppäin_wasd", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/wasd_näppäimet.png"));
        tekstuurit.put("näppäin_e", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_e.png"));
        tekstuurit.put("näppäin_nuoli", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_nuoli.png"));
        tekstuurit.put("näppäin_space", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_space.png"));
        tekstuurit.put("näppäin_q", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_q.png"));
        tekstuurit.put("näppäin_z", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_z.png"));
        tekstuurit.put("näppäin_x", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_x.png"));
        tekstuurit.put("näppäin_c", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/näppäin_c.png"));
        tekstuurit.put("ohjain_analog",new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_analog.png"));
        tekstuurit.put("ohjain_nuoli", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nuoli.png"));
        tekstuurit.put("ohjain_select", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_select.png"));
        tekstuurit.put("ohjain_r", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_r.png"));
        tekstuurit.put("ohjain_xbox_a",new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_a.png"));
        tekstuurit.put("ohjain_xbox_b", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_b.png"));
        tekstuurit.put("ohjain_xbox_x", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_x.png"));
        tekstuurit.put("ohjain_xbox_y", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_y.png"));
        tekstuurit.put("ohjain_xbox_rt", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_xbox_rt.png"));
        tekstuurit.put("ohjain_nintendo_a",new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_a.png"));
        tekstuurit.put("ohjain_nintendo_b", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_b.png"));
        tekstuurit.put("ohjain_nintendo_x", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_x.png"));
        tekstuurit.put("ohjain_nintendo_y", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_y.png"));
        tekstuurit.put("ohjain_nintendo_rz", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_nintendo_rz.png"));
        tekstuurit.put("ohjain_playstation_x",new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_x.png"));
        tekstuurit.put("ohjain_playstation_ympyrä", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_ympyrä.png"));
        tekstuurit.put("ohjain_playstation_kolmio", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_kolmio.png"));
        tekstuurit.put("ohjain_playstation_neliö", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_neliö.png"));
        tekstuurit.put("ohjain_playstation_r2", new Tekstuuri("tiedostot/kuvat/gui/näppäinkuvakkeet/ohjain_playstation_r2.png"));

        // Editori
        tekstuurit.put("editori_popup_pohja", new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png"));
        tekstuurit.put("editori_valittu", new Tekstuuri("tiedostot/kuvat/editori/valittu.png"));
        tekstuurit.put("editori_yläpalkki_pohja_vasen", new Tekstuuri("tiedostot/kuvat/editori/yläpalkki_pohja_vasen.png"));
        tekstuurit.put("editori_yläpalkki_pohja_oikea", new Tekstuuri("tiedostot/kuvat/editori/yläpalkki_pohja_oikea.png"));
        tekstuurit.put("editori_kopioi_ominaisuudet", new Tekstuuri("tiedostot/kuvat/editori/kopioi_ominaisuudet.png"));
        tekstuurit.put("editori_välilehti_tiedosto", new Tekstuuri("tiedostot/kuvat/editori/välilehti_tiedosto.png"));
        tekstuurit.put("editori_välilehti_huone", new Tekstuuri("tiedostot/kuvat/editori/välilehti_huone.png"));
        tekstuurit.put("editori_välilehti_kenttä", new Tekstuuri("tiedostot/kuvat/editori/välilehti_kenttä.png"));
        tekstuurit.put("editori_välilehti_näytä", new Tekstuuri("tiedostot/kuvat/editori/välilehti_näytä.png"));
        tekstuurit.put("editori_välilehti_lisäosat", new Tekstuuri("tiedostot/kuvat/editori/välilehti_lisäosat.png"));
        tekstuurit.put("editori_huone_vaihda", new Tekstuuri("tiedostot/kuvat/editori/huone_vaihda_nuoli.png"));
        tekstuurit.put("editori_huone_uusi", new Tekstuuri("tiedostot/kuvat/editori/huone_uusi.png"));
        tekstuurit.put("editori_huone_poista", new Tekstuuri("tiedostot/kuvat/editori/huone_poista.png"));
        tekstuurit.put("editori_objekti_valittu", new Tekstuuri("tiedostot/kuvat/editori/valittu_objekti.png"));
        tekstuurit.put("editori_objekti_kääntö", new Tekstuuri("tiedostot/kuvat/editori/objekti_kääntö.png"));
        tekstuurit.put("editori_objekti_peilaus_x", new Tekstuuri("tiedostot/kuvat/editori/objekti_peilaus_x.png"));
        tekstuurit.put("editori_objekti_peilaus_y", new Tekstuuri("tiedostot/kuvat/editori/objekti_peilaus_y.png"));
        tekstuurit.put("editori_tiedosto_uusi", new Tekstuuri("tiedostot/kuvat/editori/tiedosto_uusi.png"));
        tekstuurit.put("editori_tiedosto_avaa", new Tekstuuri("tiedostot/kuvat/editori/tiedosto_avaa.png"));
        tekstuurit.put("editori_tiedosto_tallenna", new Tekstuuri("tiedostot/kuvat/editori/tiedosto_tallenna.png"));
        tekstuurit.put("editori_näytä_maasto", new Tekstuuri("tiedostot/kuvat/editori/näytä_maasto.png"));
        tekstuurit.put("editori_näytä_objektit", new Tekstuuri("tiedostot/kuvat/editori/näytä_objektit.png"));
        tekstuurit.put("editori_näytä_entityt", new Tekstuuri("tiedostot/kuvat/editori/näytä_entityt.png"));
        tekstuurit.put("editori_näytä_osoitin", new Tekstuuri("tiedostot/kuvat/editori/osoitin.png"));
        tekstuurit.put("editori_näytä_debug", new Tekstuuri("tiedostot/kuvat/editori/näytä_debug.png"));
        tekstuurit.put("editori_lisäosat_dialogieditori", new Tekstuuri("tiedostot/kuvat/editori/lisäosat_dialogieditori.png"));
        tekstuurit.put("editori_lisäosat_tarinaeditori", new Tekstuuri("tiedostot/kuvat/editori/lisäosat_tarinaeditori.png"));
        tekstuurit.put("editori_lisäosat_tavoite-editori", new Tekstuuri("tiedostot/kuvat/editori/lisäosat_tavoite-editori.png"));
    }

    public static void lataaShaderit() {
        shaderit.put("vakio", new Shader("shader"));
        shaderit.put("staattinen", new Shader("staattinen"));
        shaderit.put("värinvaihto", new VärinvaihtoShader(0));
        shaderit.put("värinvaihto2", new VärinvaihtoShader(1));
        shaderit.put("väriliuku", new VäriliukuShader());
        shaderit.put("kuu", new VärinvaihtoShaderKuu());
        shaderit.put("kiintopiste", new Shader("kiintopiste_kiiluva"));
        shaderit.put("trippi", new TrippiShader());
        shaderit.put("testi", new TestiShader());
        shaderit.put("testi2", new Shader("testi2"));
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
        lisääÄäni("Käytä", new File("tiedostot/äänet/käytä.wav"));
        lisääÄäni("Valinta", new File("tiedostot/äänet/selekt.wav"));
        lisääÄäni("Hyväksy", new File("tiedostot/äänet/akkept.wav"));
        lisääÄäni("Kartta", new File("tiedostot/äänet/kartta.ogg"));
        lisääÄäni("Juoman_kaato", new File("tiedostot/äänet/juoman_kaato.ogg"));
        lisääÄäni("Kalja_kilinä", new File("tiedostot/äänet/kalja_kilinä.ogg"));
        lisääÄäni("Tavoite_suoritettu", new File("tiedostot/äänet/tavoite_suoritettu.wav"));
        lisääÄäni("Raha2", new File("tiedostot/äänet/raha2.wav"));
        lisääÄäni("Ping", new File("tiedostot/äänet/ping.wav"));
        lisääÄäni("dialogi1", new File("tiedostot/äänet/dialogi1.ogg"));
        lisääÄäni("dialogi2", new File("tiedostot/äänet/dialogi2.ogg"));
        lisääÄäni("dialogi3", new File("tiedostot/äänet/dialogi3.wav"));
        lisääÄäni("dialogi_juhani", new File("tiedostot/äänet/dialogi_juhani.ogg"));
        lisääÄäni("dialogi_kauppias", new File("tiedostot/äänet/dialogi_kauppias.ogg"));
        lisääÄäni("dialogi_yoda", new File("tiedostot/äänet/dialogi_yoda.ogg"));
        lisääÄäni("dialogi_velho", new File("tiedostot/äänet/dialogi_velho.ogg"));
        lisääÄäni("dialogi_pasi", new File("tiedostot/äänet/dialogi_pasi.ogg"));

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
        lisääMusa("minipeli_keimoäly", new File("tiedostot/musat/minipeli_keimoäly.ogg"));
    }
}
