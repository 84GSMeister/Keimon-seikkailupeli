package keimo.HuoneEditori;

import keimo.*;
import keimo.entityt.*;
import keimo.entityt.npc.Pikkuvihu;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.Maastot.Maasto;
import keimo.Maastot.Tile;
import keimo.Utility.Käännettävä.Suunta;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.esine.*;
import keimo.kenttäkohteet.kiintopiste.*;
import keimo.kenttäkohteet.warp.*;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class HuoneLista {

    static HashMap<Integer, Huone> huoneKarttaReferenssi = new HashMap<>();

        public static void lataaPelitiedosto() {
            lataaHuoneetJaDialogitKST();
            lataaReferenssiHuonekartta();
            //luoTestiHuone();
            //debugSpawn();
        }
        
        /**
         * Legacy alkaa
         * Vakiokentän luonti koodissa (ei lataamalla default.kst)
         */
        private static HashMap<Integer, Huone> lataaHuonelistaLegacy() {
            HashMap<Integer, Huone> huoneKartta = new HashMap<>();

            ArrayList<KenttäKohde> huone0Kenttä = luoVakioKenttäLegacy(0);
            ArrayList<Maasto> huone0Maasto = luoVakioMaastoLegacy(0);
            ArrayList<Entity> huone0Entityt = luoVakioNPCListaLegacy(0);
            huoneKartta.put(0, new Huone(0, 10, "Puisto_keski", "tausta_puisto.png", null, huone0Kenttä, huone0Maasto, huone0Entityt, "puisto", null, null));
            ArrayList<KenttäKohde> huone1Kenttä = luoVakioKenttäLegacy(1);
            ArrayList<Maasto> huone1Maasto = luoVakioMaastoLegacy(1);
            ArrayList<Entity> huone1Entityt = luoVakioNPCListaLegacy(1);
            huoneKartta.put(1, new Huone(1, 10, "Puisto_ylä", "tausta_puisto.png", null, huone1Kenttä, huone1Maasto, huone1Entityt, "puisto", null, null));
            ArrayList<KenttäKohde> huone2Kenttä = luoVakioKenttäLegacy(2);
            ArrayList<Maasto> huone2Maasto = luoVakioMaastoLegacy(2);
            ArrayList<Entity> huone2Entityt = luoVakioNPCListaLegacy(2);
            huoneKartta.put(2, new Huone(2, 10, "Puisto_ala", "tausta_puisto.png", null, huone2Kenttä, huone2Maasto, huone2Entityt, "puisto", null, null));
            ArrayList<KenttäKohde> huone3Kenttä = luoVakioKenttäLegacy(3);
            ArrayList<Maasto> huone3Maasto = luoVakioMaastoLegacy(3);
            huoneKartta.put(3, new Huone(3, 10, "Puisto_oikea", "tausta_puisto.png", null, huone3Kenttä, huone3Maasto, null, "puisto", null, null));
            ArrayList<KenttäKohde> huone4Kenttä = luoVakioKenttäLegacy(4);
            ArrayList<Maasto> huone4Maasto = luoVakioMaastoLegacy(4);
            huoneKartta.put(4, new Huone(4, 10, "Puisto_vasen", "tausta_puisto.png", null, huone4Kenttä, huone4Maasto, null, "puisto", null, null));
            ArrayList<KenttäKohde> huone5Kenttä = luoVakioKenttäLegacy(5);
            ArrayList<Maasto> huone5Maasto = luoVakioMaastoLegacy(5);
            huoneKartta.put(5, new Huone(5, 10, "Asuintalot", "tausta_jatkuu.png", null, huone5Kenttä, huone5Maasto, null, "overworld", null, null));
            Pelaaja.alkuHuone = 0;
            Pelaaja.alkuSijX = 5;
            Pelaaja.alkuSijY = 5;

            return huoneKartta;
        }
    
        private static ArrayList<KenttäKohde> luoVakioKenttäLegacy(int huoneenId) {

            ArrayList<KenttäKohde> kenttäkohteet = new ArrayList<>();
    
            switch (huoneenId) {
                default:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet = {"kohdehuone=1", "kohderuutuX=3", "kohderuutuY=9", "suunta=Ylös"};
                    kenttäkohteet.add(new Oviruutu(3, 0, ominaisuudet));
                    String[] ominaisuudet2 = {"kohdehuone=2", "kohderuutuX=5", "kohderuutuY=0", "suunta=Alas"};
                    kenttäkohteet.add(new Oviruutu(5, 9, ominaisuudet2));
                    String[] ominaisuudet3 = {"kohdehuone=3", "kohderuutuX=0", "kohderuutuY=3", "suunta=Oikea"};
                    kenttäkohteet.add(new Oviruutu(9, 3, ominaisuudet3));
                    String[] ominaisuudet4 = {"kohdehuone=4", "kohderuutuX=9", "kohderuutuY=7", "suunta=Vasen"};
                    kenttäkohteet.add(new Oviruutu(0, 7, ominaisuudet4));
                    kenttäkohteet.add(new Suklaalevy(false, 0, 0));
                    kenttäkohteet.add(new Vesiämpäri(false, 0, 0));
                    break;
                case 1:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet5 = {"kohdehuone=0", "kohderuutuX=3", "kohderuutuY=0", "suunta=Alas"};
                    kenttäkohteet.add(new Oviruutu(3, 9, ominaisuudet5));
                    kenttäkohteet.add(new Hiili(false, 0, 0));
                    kenttäkohteet.add(new Kaasusytytin(false, 0, 0, new String[]{"toimivuus=tyhjä"}));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    kenttäkohteet.add(new Suklaalevy(false, 0, 0));
                    kenttäkohteet.add(new Kilpi(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    break;
                case 2:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet6 = {"kohdehuone=0", "kohderuutuX=5", "kohderuutuY=9", "suunta=Ylös"};
                    kenttäkohteet.add(new Oviruutu(5, 0, ominaisuudet6));
                    kenttäkohteet.add(new Suklaalevy(false, 0, 0));
                    kenttäkohteet.add(new Kaasupullo(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    break;
                case 3:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet7 = {"kohdehuone=0", "kohderuutuX=9", "kohderuutuY=3", "suunta=Vasen"};
                    kenttäkohteet.add(new Oviruutu(0, 3, ominaisuudet7));
                    kenttäkohteet.add(new Vesiämpäri(false, 0, 0));
                    kenttäkohteet.add(new Avain(false, 0, 0));
                    kenttäkohteet.add(new Paperi(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    kenttäkohteet.add(new Makkara(false, 0, 0));
                    break;
                case 4:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet8 = {"kohdehuone=0", "kohderuutuX=0", "kohderuutuY=7", "suunta=Oikea"};
                    kenttäkohteet.add(new Oviruutu(9, 7, ominaisuudet8));
                    String[] ominaisuudet9 = {"kohdehuone=5", "kohderuutuX=9", "kohderuutuY=2", "suunta=Vasen"};
                    kenttäkohteet.add(new Oviruutu(0, 2, ominaisuudet9));
                    kenttäkohteet.add(new Vesiämpäri(false, 0, 0));
                    break;
                case 5:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet10 = {"kohdehuone=4", "kohderuutuX=0", "kohderuutuY=2", "suunta=Oikea"};
                    kenttäkohteet.add(new Oviruutu(9, 2, ominaisuudet10));
                    kenttäkohteet.add(new Nuotio(true, 2, 2, null));
                    kenttäkohteet.add(new Kirstu(true, 4, 2, null));
                    kenttäkohteet.add(new Ämpärikone(false, 0, 0, null));
                    break;
            }
            return kenttäkohteet;
        }
    
        private static ArrayList<Maasto> luoVakioMaastoLegacy(int huoneenId) {

            ArrayList<Maasto> maastot = new ArrayList<>();

            switch (huoneenId) {
                default:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(1, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(8, 8, new String[]{"kuva=kauppa_seinä_e.png"}));
                    maastot.add(new Tile(8, 9, new String[]{"kuva=kauppa_seinä_e.png"}));
                    maastot.add(new Tile(9, 8, new String[]{"kuva=kauppa_seinä_e.png"}));
                    maastot.add(new Tile(9, 9, new String[]{"kuva=kauppa_seinä_e.png"}));
                    maastot.add(new Tile(9, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 4, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 5, new String[]{"kuva=hiekka.png"}));
                    break;
                case 1:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(2, 1, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(1, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 4, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 5, new String[]{"kuva=hiekka.png"}));
                    break;
                case 2:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(1, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(2, 1, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(9, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 4, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 5, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(8, 3, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 4, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 5, new String[]{"kuva=vesi_e.png"}));
                    break;
                case 3:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(7, 1, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(7, 2, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(7, 3, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(7, 4, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 1, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 2, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 3, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 4, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(8, 5, new String[]{"kuva=vesi_e.png"}));
                    maastot.add(new Tile(9, 3, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 4, new String[]{"kuva=hiekka.png"}));
                    maastot.add(new Tile(9, 5, new String[]{"kuva=hiekka.png"}));
                    break;
            }
            return maastot;
        }
    
        private static ArrayList<Entity> luoVakioNPCListaLegacy(int huoneenId) {
            ArrayList<Entity> npcLista = new ArrayList<Entity>();
            npcLista.clear();
            switch (huoneenId) {
                default:
                    npcLista.add(new Pikkuvihu(5, 6, new String[]{"liiketapa=LOOP_NELIÖ_VASTAPÄIVÄÄN"}));
                    npcLista.add(new Pikkuvihu(2, 2, new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}));
                    break;
                case 1:
                    npcLista.add(new Pikkuvihu(3, 3, new String[]{"liiketapa=LOOP_NELIÖ_VASTAPÄIVÄÄN"}));
                    npcLista.add(new Pikkuvihu(4, 2, new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}));
                    npcLista.add(new Pikkuvihu(1, 4, new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}));
                    break;
                case 2:
                    npcLista.add(new Pikkuvihu(1, 5, new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}));
                    npcLista.add(new Pikkuvihu(5, 3, new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}));
                    npcLista.add(new Pikkuvihu(2, 4, new String[]{"liiketapa=LOOP_NELIÖ_VASTAPÄIVÄÄN"}));
                    break;
            }
            return npcLista;
        }
        /**
         * Legacy päättyy
         */
    
    
    /**
     * Lataa pelin alussa luotava kenttä default.kst -tiedostosta
    */

    private static void lataaHuoneetJaDialogitKST() {
        try {
            File tiedosto = new File("tiedostot/pelitiedostot/default.kst");
            String[] huoneetMerkkijonoina;
            int huoneidenMääräTiedostossa = 0;
            String[] tarinaDialogitMerkkijonoina;
            int tarinaDialogienMääräTiedostossa = 0;
            String[] vuoropuheDialogitMerkkijonoina;
            int vuoropuheDialogienMääräTiedostossa = 0;
            String asetuksetMerkkijonona = "";
            Path path = FileSystems.getDefault().getPath(tiedosto.getPath());
            Charset charset = Charset.forName("UTF-8");
            BufferedReader read = Files.newBufferedReader(path, charset);
            String tarkastettavaRivi = null;
            if ((tarkastettavaRivi = read.readLine()) != null) {
                tarkastettavaRivi = read.readLine();
                if (!tarkastettavaRivi.startsWith("<KEIMO>")) {
                    System.out.println(tarkastettavaRivi);
                }
            }
            while ((tarkastettavaRivi = read.readLine()) != null) {
                if (tarkastettavaRivi.startsWith("Huone ")) {
                    huoneidenMääräTiedostossa++;
                }
                else if (tarkastettavaRivi.startsWith("Tarina ")) {
                    tarinaDialogienMääräTiedostossa++;
                }
                else if (tarkastettavaRivi.startsWith("Dialogi ")) {
                    vuoropuheDialogienMääräTiedostossa++;
                }
            }
            huoneetMerkkijonoina = new String[huoneidenMääräTiedostossa];
            huoneidenMääräTiedostossa = 0;
            tarinaDialogitMerkkijonoina = new String[tarinaDialogienMääräTiedostossa];
            tarinaDialogienMääräTiedostossa = 0;
            vuoropuheDialogitMerkkijonoina = new String[vuoropuheDialogienMääräTiedostossa];
            vuoropuheDialogienMääräTiedostossa = 0;
            read.close();
            read = Files.newBufferedReader(path, charset);
            tarkastettavaRivi = read.readLine();
            while ((tarkastettavaRivi != null)) {
                if (tarkastettavaRivi.startsWith("Asetukset")) {
                    while (tarkastettavaRivi != null) {
                        asetuksetMerkkijonona += tarkastettavaRivi + "\n";
                        if (tarkastettavaRivi.startsWith("/Asetukset")) {
                            break;
                        }
                        tarkastettavaRivi = read.readLine();
                    }
                }
                if (tarkastettavaRivi.startsWith("Huone ")) {
                    huoneidenMääräTiedostossa++;
                    huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] = "";
                    while (tarkastettavaRivi != null) {
                        huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                        if (tarkastettavaRivi.startsWith("/Huone")) {
                            break;
                        }
                        tarkastettavaRivi = read.readLine();
                    }
                }
                else if (tarkastettavaRivi.startsWith("Tarina ")) {
                    tarinaDialogienMääräTiedostossa++;
                    tarinaDialogitMerkkijonoina[tarinaDialogienMääräTiedostossa-1] = "";
                    while (tarkastettavaRivi != null) {
                        tarinaDialogitMerkkijonoina[tarinaDialogienMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                        if (tarkastettavaRivi.startsWith("/Tarina")) {
                            break;
                        }
                        tarkastettavaRivi = read.readLine();
                    }
                }
                else if (tarkastettavaRivi.startsWith("Dialogi ")) {
                    vuoropuheDialogienMääräTiedostossa++;
                    vuoropuheDialogitMerkkijonoina[vuoropuheDialogienMääräTiedostossa-1] = "";
                    while (tarkastettavaRivi != null) {
                        vuoropuheDialogitMerkkijonoina[vuoropuheDialogienMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                        if (tarkastettavaRivi.startsWith("/Dialogi")) {
                            break;
                        }
                        tarkastettavaRivi = read.readLine();
                    }
                }
                else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                    break;
                }
                else {
                    tarkastettavaRivi = read.readLine();
                }
            }
            read.close();
            Peli.huoneKartta.clear();
            HuoneEditorinMetodit.lataaAsetuksetMerkkijonosta(asetuksetMerkkijonona);
            huoneKarttaReferenssi = HuoneEditorinMetodit.luoHuoneKarttaMerkkijonosta(huoneetMerkkijonoina);
            //huoneKarttaReferenssi = lataaHuonelistaLegacy();
            TarinaDialogiLista.tarinaKartta = HuoneEditorinMetodit.luoTarinaKarttaMerkkijonosta(tarinaDialogitMerkkijonoina);
            Dialogit.PitkätDialogit.vuoropuheDialogiKartta = HuoneEditorinMetodit.luoDialogiKarttaMerkkijonosta(vuoropuheDialogitMerkkijonoina);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ei voitu ladata tiedostoa default.kst", "Virhe kentän luonnissa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Testihuone
     */
    private static void luoTestiHuone() {
        int testiHuoneenId = Peli.huoneKartta.size();
        System.out.println("Testihuoneen id: " + testiHuoneenId);
        int testihuoneenKoko = 30;
        ArrayList<KenttäKohde> kenttäKohdeLista = new ArrayList<>();
        for (int i = 0; i < testihuoneenKoko; i++) {
            for (int j = 0; j < testihuoneenKoko; j++) {
                kenttäKohdeLista.add(KenttäKohde.luoRandomKenttäKohde(j, i));
            }
        }
        ArrayList<Maasto> maastoLista = new ArrayList<>();
        for (int i = 0; i < testihuoneenKoko; i++) {
            for (int j = 0; j < testihuoneenKoko; j++) {
                //maastoLista.add(Maasto.luoRandomMaasto(j, i));
            }
        }
        Huone testiHuone = new Huone(testiHuoneenId, testihuoneenKoko, "testihuone", null, "testialue", kenttäKohdeLista, maastoLista, null, null, null, null);
        Peli.huoneKartta.put(testiHuoneenId, testiHuone);
    }

    /**
     * Debug spawn
     */
    private static void debugSpawn() {
        Peli.entityLista.add(new TyönnettäväLaatikko(12, 12));
    }

    public static void lataaReferenssiHuonekartta() {
        Peli.huoneKartta.clear();
        for (int i = 0; i < huoneKarttaReferenssi.size(); i++) {
            Huone h = huoneKarttaReferenssi.get(i);
            ArrayList<KenttäKohde> kenttäKohteet = new ArrayList<>();
            for (KenttäKohde[] kk : h.annaHuoneenKenttäSisältö()) {
                for (KenttäKohde k : kk) {
                    if (k != null) kenttäKohteet.add(KenttäKohde.luoObjektiTiedoilla(k.annaNimi(), k.onkoMääritettySijainti(), k.annaSijX(), k.annaSijY(), k.annalisäOminaisuudet()));
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
                    if (e != null) entityt.add(Entity.luoEntityTiedoilla(e.annaNimi(), e.onkoMääritettySijainti(), e.annaSijX(), e.annaAlkuSijY(), e.annalisäOminaisuudet()));
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
            Peli.huoneKartta.put(i, uusiHuone);
        }
    }
}
