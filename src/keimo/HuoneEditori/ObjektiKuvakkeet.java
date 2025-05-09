package keimo.HuoneEditori;

import keimo.Huone;
import keimo.Peli;
import keimo.Utility.KäännettäväKuvake;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

public class ObjektiKuvakkeet {

    public static HashMap<String, ObjektiKuvake> objektiKuvakkeet = new HashMap<>();
    public static HashMap<String, ObjektiKuvake> visuaalisetObjektiKuvakkeet;
    public static HashMap<String, ObjektiKuvake> tileKuvakkeet;// = new HashMap<>();
    public static HashMap<String, ObjektiKuvake> entityKuvakkeet = new HashMap<>();

    public static void luoKuvakkeet() {
        luoObjektiKuvakkeet();
        luoVisuaalisetObjektiKuvakkeet();
        luoMaastoKuvakkeet();
        luoEntityKuvakkeet();
    }

    private static void luoObjektiKuvakkeet() {
        objektiKuvakkeet.put("Avain", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/avain.png"));
        objektiKuvakkeet.put("Baariovi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/baariovi.png"));
        objektiKuvakkeet.put("Baariruutu", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/baariruutu.png"));
        objektiKuvakkeet.put("Hiili", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/hiili.png"));
        objektiKuvakkeet.put("Huume", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/huume.png"));
        objektiKuvakkeet.put("Jallupullo", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/jallupullo.png"));
        objektiKuvakkeet.put("Juhani", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/juhani.gif"));
        objektiKuvakkeet.put("Jumal Velho", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/velho.png"));
        objektiKuvakkeet.put("Jumal Yoda", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/yoda.png"));
        objektiKuvakkeet.put("Kaasupullo", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kaasupullo.png"));
        objektiKuvakkeet.put("Kaasusytytin", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/tyhjäkaasusytytin.png"));
        objektiKuvakkeet.put("Kalja-automaatti", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kalja-automaatti.png"));
        objektiKuvakkeet.put("Kartta", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kartta.png"));
        objektiKuvakkeet.put("Kauppahylly", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kauppahylly.png"));
        objektiKuvakkeet.put("Kauppaovi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kauppaovi.png"));
        objektiKuvakkeet.put("Kaupparuutu", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kaupparuutu.png"));
        objektiKuvakkeet.put("Kauppias", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kauppias.png"));
        objektiKuvakkeet.put("Kilpi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kilpi.png"));
        objektiKuvakkeet.put("Kirstu", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kirstu.png"));
        objektiKuvakkeet.put("Kolikko", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kolikko.png"));
        objektiKuvakkeet.put("Koriste-esine", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/koriste-esine_eikuvaa.png"));
        objektiKuvakkeet.put("Kuparilager", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kuparilager.png"));
        objektiKuvakkeet.put("Kuuhahmo1", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kuuhahmo_1.png"));
        objektiKuvakkeet.put("Kuuhahmo2", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kuuhahmo_2.png"));
        objektiKuvakkeet.put("Kuuhahmo3", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/kuuhahmo_3.png"));
        objektiKuvakkeet.put("KuuOlutlasi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/olutlasi_kuu.png"));
        objektiKuvakkeet.put("Makkara", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/makkarat.png"));
        objektiKuvakkeet.put("Nappi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/nappi.png"));
        objektiKuvakkeet.put("Nuotio", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/nuotio.png"));
        objektiKuvakkeet.put("Olutlasi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/olutlasi.png"));
        objektiKuvakkeet.put("Painelaatta", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/painelaatta.png"));
        objektiKuvakkeet.put("Paperi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/paperi.png"));
        objektiKuvakkeet.put("Pasi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/pasi.png"));
        objektiKuvakkeet.put("Paskanmarjabooli", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/paskanmarjabooli.png"));
        objektiKuvakkeet.put("Paskanmarjat", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/paskanmarjat.png"));
        objektiKuvakkeet.put("Pelikone", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/pelikone.png"));
        objektiKuvakkeet.put("Pelikone2", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/pelikone2.png"));
        objektiKuvakkeet.put("Penkki", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/puistonpenkki.png"));
        objektiKuvakkeet.put("Pesäpallomaila", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/pesäpallomaila.png"));
        objektiKuvakkeet.put("Pontikka-ainekset", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/ponuainekset.png"));
        objektiKuvakkeet.put("Portti", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/portti.png"));
        objektiKuvakkeet.put("Pulloautomaatti", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png"));
        objektiKuvakkeet.put("Puuovi", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/puuovi.png"));
        objektiKuvakkeet.put("Oviruutu", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/reunawarppi.png"));
        objektiKuvakkeet.put("Seteli", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/seteli.png"));
        objektiKuvakkeet.put("Sieni", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/sieni.png"));
        objektiKuvakkeet.put("Silta", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/asfaltti_silta.png"));
        objektiKuvakkeet.put("Suklaalevy", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/suklaalevy.png"));
        objektiKuvakkeet.put("Sänky", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/sänky.png"));
        objektiKuvakkeet.put("Tynnyri", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/tynnyri.png"));
        objektiKuvakkeet.put("Vesiämpäri", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png"));
        objektiKuvakkeet.put("Ämpärikone", new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/ämpärikone.png"));
    }

    private static void luoVisuaalisetObjektiKuvakkeet() {
        Map<String, ObjektiKuvake> kuvaKartta = Stream.of(new File("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit").listFiles())
                    .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
                    .collect(Collectors.toMap(File::getName, file -> new ObjektiKuvake(file.getPath())));
        visuaalisetObjektiKuvakkeet = new HashMap<>(kuvaKartta);
    }

    private static void luoMaastoKuvakkeet() {
        Map<String, ObjektiKuvake> kuvaKartta = Stream.of(new File("tiedostot/kuvat/maasto").listFiles())
                    .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
                    .collect(Collectors.toMap(File::getName, file -> new ObjektiKuvake(file.getPath())));
        tileKuvakkeet = new HashMap<>(kuvaKartta);
    }

    private static void luoEntityKuvakkeet() {
        // for (Huone huone : Peli.huoneKartta.values()) {
        //     for (int y = 0; y < huone.annaKoko(); y++) {
        //         for (int x = 0; x < huone.annaKoko(); x++) {
        //             if (huone.annaHuoneenNPCSisältö()[x][y] != null) {
        //                 String tiedostonNimi = huone.annaHuoneenNPCSisältö()[x][y].annaKuvanTiedostoNimi();
        //                 if (tiedostonNimi != null) {
        //                     if (!entityKuvakkeet.containsKey(tiedostonNimi)) {
        //                         entityKuvakkeet.put(tiedostonNimi, new ObjektiKuvake("tiedostot/kuvat/entity/" + tiedostonNimi));
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }
        entityKuvakkeet.put("Asevihu", new ObjektiKuvake("tiedostot/kuvat/npc/asevihu.png"));
        entityKuvakkeet.put("IsoKyltti", new ObjektiKuvake("tiedostot/kuvat/entity/kyltti_kuubileet.png"));
        entityKuvakkeet.put("IsoLaatikko", new ObjektiKuvake("tiedostot/kuvat/entity/iso_laatikko.png"));
        entityKuvakkeet.put("Laatikko", new ObjektiKuvake("tiedostot/kuvat/entity/työnnettävä_laatikko.png"));
        entityKuvakkeet.put("Pahavihu", new ObjektiKuvake("tiedostot/kuvat/npc/pahavihu.png"));
        entityKuvakkeet.put("Pikkuvihu", new ObjektiKuvake("tiedostot/kuvat/npc/pikkuvihu.png"));
        entityKuvakkeet.put("Pomo", new ObjektiKuvake("tiedostot/kuvat/npc/boss.png"));
        entityKuvakkeet.put("TestiEntity", new ObjektiKuvake("tiedostot/kuvat/entity/apu_pesukone.png"));
        entityKuvakkeet.put("Vartija", new ObjektiKuvake("tiedostot/kuvat/npc/vartija_off.png"));
    }

    public static class ObjektiKuvake extends ImageIcon {
        public ObjektiKuvake(String tiedostonNimi) {
            super(tiedostonNimi);
        }
    }
}
