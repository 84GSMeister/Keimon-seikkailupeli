package keimo.HuoneEditori;

import keimo.*;
import keimo.entityt.*;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogit;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.Ikkunat.LatausIkkuna;
import keimo.Maastot.Maasto;
import keimo.Utility.Käännettävä.Suunta;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.KenttäKohde;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

public class HuoneLista {

    static HashMap<Integer, Huone> huoneKarttaReferenssi = new HashMap<>();
        
        /**
         * Legacy alkaa
         * Vakiokentän luonti koodissa (ei lataamalla default.kst)
         */
    
        // static ArrayList<KenttäKohde> huone0Kenttä = new ArrayList<KenttäKohde>();
        // static ArrayList<Maasto> huone0Maasto = new ArrayList<Maasto>();
        // static ArrayList<KenttäKohde> huone1Kenttä = new ArrayList<KenttäKohde>();
        // static ArrayList<Maasto> huone1Maasto = new ArrayList<Maasto>();
        // static ArrayList<KenttäKohde> huone2Kenttä = new ArrayList<KenttäKohde>();
        // static ArrayList<Maasto> huone2Maasto = new ArrayList<Maasto>();
        // static ArrayList<KenttäKohde> huone3Kenttä = new ArrayList<KenttäKohde>();
        // static ArrayList<Maasto> huone3Maasto = new ArrayList<Maasto>();
        // static ArrayList<KenttäKohde> huone4Kenttä = new ArrayList<KenttäKohde>();
        // static ArrayList<Maasto> huone4Maasto = new ArrayList<Maasto>();
        // static ArrayList<KenttäKohde> huone5Kenttä = new ArrayList<KenttäKohde>();
        // static ArrayList<Maasto> huone5Maasto = new ArrayList<Maasto>();
    
        // static KenttäKohde[][] luoVakioKenttä(int huoneenId) {
    
        //     pelikenttä = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
        //     switch (huoneenId) {
        //         default:
        //             huone0Kenttä.removeAll(huone0Kenttä);
        //             huone0Kenttä.add(new ReunaWarppi(3, 0, 1, 3, 9, ReunaWarppi.Suunta.YLÖS));
        //             huone0Kenttä.add(new ReunaWarppi(5 , 9, 2, 5, 0, ReunaWarppi.Suunta.ALAS));
        //             huone0Kenttä.add(new ReunaWarppi(9, 3, 3, 0, 3, ReunaWarppi.Suunta.OIKEA));
        //             huone0Kenttä.add(new ReunaWarppi(0, 7, 4, 9, 7, ReunaWarppi.Suunta.VASEN));
        //             huone0Kenttä.add(new Suklaalevy());
        //             huone0Kenttä.add(new Vesiämpäri());
        //             break;
        //         case 1:
        //             huone1Kenttä.removeAll(huone1Kenttä);
        //             huone1Kenttä.add(new ReunaWarppi(3, 9, 0, 3, 0, ReunaWarppi.Suunta.ALAS));
        //             huone1Kenttä.add(new Hiili());
        //             huone1Kenttä.add(new Kaasusytytin("tyhjä"));
        //             huone1Kenttä.add(new Makkara());
        //             huone1Kenttä.add(new Suklaalevy());
        //             huone1Kenttä.add(new Kilpi());
        //             huone1Kenttä.add(new Makkara());
        //             huone1Kenttä.add(new Makkara());
        //             huone1Kenttä.add(new Makkara());
        //             break;
        //         case 2:
        //             huone2Kenttä.removeAll(huone2Kenttä);
        //             huone2Kenttä.add(new ReunaWarppi(5, 0, 0, 5, 9, ReunaWarppi.Suunta.YLÖS));
        //             huone2Kenttä.add(new Suklaalevy());
        //             huone2Kenttä.add(new Kaasupullo());
        //             huone2Kenttä.add(new Makkara());
        //             huone2Kenttä.add(new Makkara());
        //             huone2Kenttä.add(new PikkuVihu_KenttäKohde());
        //             huone2Kenttä.add(new PikkuVihu_KenttäKohde());
        //             huone2Kenttä.add(new PikkuVihu_KenttäKohde());
        //             huone2Kenttä.add(new PikkuVihu_KenttäKohde());
        //             huone2Kenttä.add(new PikkuVihu_KenttäKohde());
        //             break;
        //         case 3:
        //             huone3Kenttä.removeAll(huone3Kenttä);
        //             huone3Kenttä.add(new ReunaWarppi(0, 3, 0, 9, 3, ReunaWarppi.Suunta.VASEN));
        //             huone3Kenttä.add(new Vesiämpäri());
        //             huone3Kenttä.add(new Avain());
        //             huone3Kenttä.add(new Paperi());
        //             huone3Kenttä.add(new Makkara());
        //             huone3Kenttä.add(new Makkara());
        //             huone3Kenttä.add(new Makkara());
        //             huone3Kenttä.add(new PikkuVihu_KenttäKohde());
        //             huone3Kenttä.add(new PikkuVihu_KenttäKohde());
        //             huone3Kenttä.add(new PikkuVihu_KenttäKohde());
        //             break;
        //         case 4:
        //             huone4Kenttä.removeAll(huone4Kenttä);
        //             huone4Kenttä.add(new ReunaWarppi(9, 7, 0, 0, 7, ReunaWarppi.Suunta.OIKEA));
        //             huone4Kenttä.add(new ReunaWarppi(0, 2, 5, 9, 2, ReunaWarppi.Suunta.VASEN));
        //             huone4Kenttä.add(new Vesiämpäri());
        //             huone4Kenttä.add(new PikkuVihu_KenttäKohde(1,1));
        //             huone4Kenttä.add(new PikkuVihu_KenttäKohde(3,4));
        //             huone4Kenttä.add(new PikkuVihu_KenttäKohde(5,7));
        //             break;
        //         case 5:
        //             huone5Kenttä.removeAll(huone5Kenttä);
        //             huone5Kenttä.add(new ReunaWarppi(9, 2, 4, 0, 2, ReunaWarppi.Suunta.OIKEA));
        //             huone5Kenttä.add(new Nuotio(2,2));
        //             huone5Kenttä.add(new Kirstu(4,2));
        //             huone5Kenttä.add(new Ämpärikone());
        //             huone5Kenttä.add(new PikkuVihu_KenttäKohde());
        //             break;
        //     }
        //     return pelikenttä;
        // }
    
        // static Maasto[][] luoVakioMaasto(int huoneenId) {
    
        //     maastokenttä = new Maasto[Peli.kentänKoko][Peli.kentänKoko];
        //     switch (huoneenId) {
        //         default:
        //             huone0Maasto.removeAll(huone0Maasto);
        //             huone0Maasto.add(new Hiekka(1,3));
        //             huone0Maasto.add(new Seinä(8,8));
        //             huone0Maasto.add(new Seinä(8,9));
        //             huone0Maasto.add(new Seinä(9,8));
        //             huone0Maasto.add(new Seinä(9,9));
        //             huone0Maasto.add(new Hiekka());
        //             huone0Maasto.add(new Hiekka());
        //             huone0Maasto.add(new Hiekka());
        //             break;
        //         case 1:
        //             huone1Maasto.removeAll(huone1Maasto);
        //             huone1Maasto.add(new Vesi(2,1));
        //             huone1Maasto.add(new Hiekka(1,3));
        //             huone1Maasto.add(new Hiekka());
        //             huone1Maasto.add(new Hiekka());
        //             huone1Maasto.add(new Hiekka());
        //             break;
        //         case 2:
        //             huone2Maasto.removeAll(huone2Maasto);
        //             huone2Maasto.add(new Hiekka(1,3));
        //             huone2Maasto.add(new Vesi(2,1));
        //             huone2Maasto.add(new Hiekka());
        //             huone2Maasto.add(new Hiekka());
        //             huone2Maasto.add(new Hiekka());
        //             huone2Maasto.add(new Vesi());
        //             huone2Maasto.add(new Vesi());
        //             huone2Maasto.add(new Vesi());
        //             break;
        //         case 3:
        //             huone3Maasto.removeAll(huone3Maasto);
        //             huone3Maasto.add(new Vesi(7,1));
        //             huone3Maasto.add(new Vesi(7,2));
        //             huone3Maasto.add(new Vesi(7,3));
        //             huone3Maasto.add(new Vesi(7,4));
        //             huone3Maasto.add(new Vesi(8,1));
        //             huone3Maasto.add(new Vesi(8,2));
        //             huone3Maasto.add(new Vesi(8,3));
        //             huone3Maasto.add(new Vesi(8,4));
        //             huone3Maasto.add(new Vesi(8,5));
        //             huone3Maasto.add(new Hiekka());
        //             huone3Maasto.add(new Hiekka());
        //             huone3Maasto.add(new Hiekka());
        //             break;
        //     }
        //     return maastokenttä;
        // }
    
        // static ArrayList<NPC> luoVakioNPCLista(int huoneenId) {
        //     ArrayList<NPC> npcLista = new ArrayList<NPC>();
        //     npcLista.clear();
        //     switch (huoneenId) {
        //         default:
        //             npcLista.add(new Pikkuvihu(560, 640, LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN));
        //             npcLista.add(new Pikkuvihu(256, 256, LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN));
        //             break;
        //         case 1:
        //             npcLista.add(new Pikkuvihu(300, 300, LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN));
        //             npcLista.add(new Pikkuvihu(450, 200, LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN));
        //             npcLista.add(new Pikkuvihu(100, 400, LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN));
        //             break;
        //         case 2:
        //             npcLista.add(new Pikkuvihu(100, 500, LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN));
        //             npcLista.add(new Pikkuvihu(540, 300, LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN));
        //             npcLista.add(new Pikkuvihu(250, 400, LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN));
        //             break;
        //     }
        //     return npcLista;
        // }
        /**
         * Legacy päättyy
         */
    
    
        /**
         * Lataa pelin alussa luotava kenttä default.kst -tiedostosta
        */

    public static void luoVakioHuoneKarttaTiedostosta() {
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
            TarinaDialogiLista.tarinaKartta = HuoneEditorinMetodit.luoTarinaKarttaMerkkijonosta(tarinaDialogitMerkkijonoina);
            Dialogit.PitkätDialogit.vuoropuheDialogiKartta = HuoneEditorinMetodit.luoDialogiKarttaMerkkijonosta(vuoropuheDialogitMerkkijonoina);
            if (Peli.legacy) VuoropuheDialogit.vuoropuheDialogiKartta = HuoneEditorinMetodit.luoDialogiKarttaMerkkijonostaVanha(vuoropuheDialogitMerkkijonoina);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(LatausIkkuna.ikkuna(), "Ei voitu ladata tiedostoa default.kst", "Virhe kentän luonnissa", JOptionPane.ERROR_MESSAGE);
        }

        /**
         * Testihuone
         */
        // int testiHuoneenId = huoneKartta.size();
        // System.out.println("Testihuoneen id: " + testiHuoneenId);
        // int testihuoneenKoko = 30;
        // ArrayList<KenttäKohde> kenttäKohdeLista = new ArrayList<>();
        // for (int i = 0; i < testihuoneenKoko; i++) {
        //     for (int j = 0; j < testihuoneenKoko; j++) {
        //         kenttäKohdeLista.add(KenttäKohde.luoRandomKenttäKohde(j, i));
        //     }
        // }
        // ArrayList<Maasto> maastoLista = new ArrayList<>();
        // for (int i = 0; i < testihuoneenKoko; i++) {
        //     for (int j = 0; j < testihuoneenKoko; j++) {
        //         //maastoLista.add(Maasto.luoRandomMaasto(j, i));
        //     }
        // }
        // Huone testiHuone = new Huone(testiHuoneenId, testihuoneenKoko, "testihuone", null, "testialue", kenttäKohdeLista, maastoLista, null, null, null);
        // huoneKartta.put(testiHuoneenId, testiHuone);

        /**
         * Debug spawn
         */
        //Peli.ammusLista.add(new TyönnettäväLaatikko(12, 12));
    }

    public static void lataaReferenssiHuonekartta() {
        Peli.huoneKartta.clear();
        for (int i = 0; i < huoneKarttaReferenssi.size(); i++) {
            Huone h = huoneKarttaReferenssi.get(i);
            ArrayList<KenttäKohde> kenttäKohteet = new ArrayList<>();
            for (KenttäKohde[] kk : h.annaHuoneenKenttäSisältö()) {
                for (KenttäKohde k : kk) {
                    kenttäKohteet.add(k);
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
                    entityt.add(e);
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
