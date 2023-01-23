package keimo;

import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.Image;
import java.awt.Toolkit;

import keimo.Kenttäkohteet.Warp.Suunta;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;

public class HuoneEditorinMetodit {
    
    static HashMap<Integer, Huone> luoHuoneKarttaMerkkijonosta(String[] merkkijonot) {
        HashMap<Integer, Huone> uusiHuoneKartta = new HashMap<Integer, Huone>();
        int uusiHuoneenId = 0;
        String uusiHuoneenNimi = "";
        String uusiHuoneenAlue = "";
        Image uusiHuoneenTausta = null;

        String luotavaObjekti = "";
        int luotavanObjektinX = 0;
        int luotavanObjektinY = 0;
        String[] luotavanObjektinOminaisuusLista = {""};
        ArrayList<KenttäKohde> uusiObjektiLista = new ArrayList<KenttäKohde>();

        String luotavaMaasto = "";
        int luotavanMaastonX = 0;
        int luotavanMaastonY = 0;
        String[] luotavanMaastonOminaisuusLista = {""};
        ArrayList<Maasto> uusiMaastoLista = new ArrayList<Maasto>();

        try {
            for (String s : merkkijonot) {
                Scanner sc = new Scanner(s);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = sc.nextLine();
                    if (tarkastettavaRivi.startsWith("Huone")) {
                        uusiHuoneenId = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("#nimi:")) {
                        uusiHuoneenNimi = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#alue:")) {
                        uusiHuoneenAlue = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#tausta:")) {
                        uusiHuoneenTausta = Toolkit.getDefaultToolkit().createImage(tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("#kenttä:")) {
                        if (tarkastettavaRivi.contains("{")) {
                            tarkastettavaRivi = sc.nextLine();
                            while (!tarkastettavaRivi.contains("}")) {
                                luotavaObjekti = "";
                                luotavanObjektinX = 0;
                                luotavanObjektinY = 0;
                                if (tarkastettavaRivi.startsWith("        ")) {
                                    if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                        String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                        int ominaisuuksienMäärä = 0;
                                        for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                            if (tarkastettavaRivi.charAt(i) == ',') {
                                                ominaisuuksienMäärä++;
                                            }
                                            else if (tarkastettavaRivi.charAt(i) == ']') {
                                                break;
                                            }
                                        }
                                        for (int i = 0; i < ominaisuuksienMäärä; i++) {
                                            luotavanObjektinOminaisuusLista = ominaisuudetMerkkijonona.split(",");
                                        }
                                        //for (String st : luotavanObjektinOminaisuusLista) {
                                        //    System.out.println("ominaisuus: " + st);
                                        //}
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                            luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, false, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, false, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                        luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                        uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, true, luotavanObjektinX, luotavanObjektinY, false, null));
                                    }
                                    else if (tarkastettavaRivi.contains(",")) {
                                        luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf(","));
                                        uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, false, luotavanObjektinX, luotavanObjektinY, false, null));
                                    }
                                }
                                if (sc.hasNextLine()) {
                                    tarkastettavaRivi = sc.nextLine();
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    }
                    else if (tarkastettavaRivi.contains("#maasto:")) {
                        if (tarkastettavaRivi.contains("{")) {
                            tarkastettavaRivi = sc.nextLine();
                            while (!tarkastettavaRivi.contains("}")) {
                                luotavaMaasto = "";
                                luotavanMaastonX = 0;
                                luotavanMaastonY = 0;
                                if (tarkastettavaRivi.startsWith("        ")) {
                                    if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                        String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                        int ominaisuuksienMäärä = 0;
                                        for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                            if (tarkastettavaRivi.charAt(i) == ',') {
                                                ominaisuuksienMäärä++;
                                            }
                                            else if (tarkastettavaRivi.charAt(i) == ']') {
                                                break;
                                            }
                                        }
                                        for (int i = 0; i < ominaisuuksienMäärä; i++) {
                                            luotavanMaastonOminaisuusLista = ominaisuudetMerkkijonona.split(",");
                                        }
                                        //for (String st : luotavanObjektinOminaisuusLista) {
                                        //    System.out.println("ominaisuus: " + st);
                                        //}
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            luotavanMaastonX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                            luotavanMaastonY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        luotavanMaastonX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                        luotavanMaastonY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                        uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, true, luotavanMaastonX, luotavanMaastonY, false, null));
                                    }
                                    else if (tarkastettavaRivi.contains(",")) {
                                        luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf(","));
                                        uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, false, null));
                                    }
                                }
                                if (sc.hasNextLine()) {
                                    tarkastettavaRivi = sc.nextLine();
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    }
                }
                Huone huone = new Huone(uusiHuoneenId, 10, uusiHuoneenNimi, uusiHuoneenTausta, uusiHuoneenAlue, uusiObjektiLista, uusiMaastoLista, false, "");
                uusiHuoneKartta.put(uusiHuoneenId, huone);
                uusiObjektiLista.clear();
                uusiMaastoLista.clear();
                sc.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            String virheViesti = "";
            virheViesti += ("huone: " + uusiHuoneenId + "\n");
            virheViesti += ("nimi: " + uusiHuoneenNimi + "\n");
            virheViesti += ("alue: " + uusiHuoneenAlue + "\n");
            virheViesti += ("virheellinen objekti: " + luotavaObjekti + "\n");
            virheViesti += ("objektin X: " + luotavanObjektinX + "\n");
            virheViesti += ("objektin Y: " + luotavanObjektinY + "\n");
            System.out.println(virheViesti);
            JOptionPane.showMessageDialog(null, "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\n\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla.\n\n" + virheViesti, "Virhe ladatessa tiedostoa.", JOptionPane.ERROR_MESSAGE);
        }
        return uusiHuoneKartta;
    }

    static KenttäKohde luoObjektiTiedoilla(String objektinNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {

        KenttäKohde luotavaObjekti;

        /**
         * Jos on määritetty sijainti, käytä objektille konstruktoria Esine(int, int)
         * Muuten käytä parametritonta konstruktoria Esine()
         */

        if (!lisäOminaisuudet) {
            switch (objektinNimi) {

                case "Avain":
                    luotavaObjekti = määritettySijainti ? new Avain(sijX, sijY) : new Avain();
                    break;

                case "Hiili":
                    luotavaObjekti = määritettySijainti ? new Hiili(sijX, sijY) : new Hiili();
                    break;

                case "Kaasupullo":
                    luotavaObjekti = määritettySijainti ? new Kaasupullo(sijX, sijY) : new Kaasupullo();
                    break;

                case "Kaasusytytin":
                    luotavaObjekti = määritettySijainti ? new Kaasusytytin(sijX, sijY, "tyhjä") : new Kaasusytytin("tyhjä");
                    break;

                case "Kilpi":
                    luotavaObjekti = määritettySijainti ? new Kilpi(sijX, sijY) : new Kilpi();
                    break;

                case "Kirstu":
                    luotavaObjekti = määritettySijainti ? new Kirstu(sijX, sijY) : new Kirstu();
                    break;

                case "Makkara":
                    luotavaObjekti = määritettySijainti ? new Makkara(sijX, sijY) : new Makkara();
                    break;

                case "Nuotio":
                    luotavaObjekti = määritettySijainti ? new Nuotio(sijX, sijY) : new Nuotio();
                    break;

                case "Pahavihu":
                    luotavaObjekti = määritettySijainti ? new PahaVihu(sijX, sijY) : new PahaVihu();
                    break;

                case "Paperi":
                    luotavaObjekti = määritettySijainti ? new Paperi(sijX, sijY) : new Paperi();
                    break;

                case "Pikkuvihu":
                    luotavaObjekti = määritettySijainti ? new PikkuVihu(sijX, sijY) : new PikkuVihu();
                    break;

                case "Oviruutu":
                    luotavaObjekti = new ReunaWarppi(sijX, sijY, 0, 0, 0, Suunta.VASEN);
                    break;

                case "Suklaalevy":
                    luotavaObjekti = määritettySijainti ? new Suklaalevy(sijX, sijY) : new Suklaalevy();
                    break;

                case "Vesiämpäri":
                    luotavaObjekti = määritettySijainti ? new Vesiämpäri(sijX, sijY) : new Vesiämpäri();
                    break;

                case "Ämpärikone":
                    luotavaObjekti = määritettySijainti ? new Ämpärikone(sijX, sijY) : new Ämpärikone();
                    break;

                default:
                    luotavaObjekti = null;
                    break;
            }
        }
        else {
            switch (objektinNimi) {

                case "Kaasusytytin":
                    luotavaObjekti = määritettySijainti ? new Kaasusytytin(sijX, sijY, ominaisuusLista) : new Kaasusytytin(ominaisuusLista);
                    break;

                case "Oviruutu":
                    luotavaObjekti = new ReunaWarppi(sijX, sijY, ominaisuusLista);
                    break;

                default:
                    luotavaObjekti = null;
                    break;
                }
        }
        return luotavaObjekti;
    }

    static Maasto luoMaastoTiedoilla(String objektinNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {

        Maasto luotavaMaasto;

        /**
         * Jos on määritetty sijainti, käytä objektille konstruktoria Maasto(int, int)
         * Muuten käytä parametritonta konstruktoria Maasto()
         */
        if (lisäOminaisuudet) {
            switch (objektinNimi) {

                case "Tile":
                    luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                    break;

                default:
                    luotavaMaasto = null;
                    break;
            }
        }

        else {
            switch (objektinNimi) {

                case "Hiekka":
                    luotavaMaasto = määritettySijainti ? new Hiekka(sijX, sijY) : new Hiekka();
                    break;

                case "Seinä":
                    luotavaMaasto = määritettySijainti ? new Seinä(sijX, sijY) : new Seinä();
                    break;

                case "Vesi":
                    luotavaMaasto = määritettySijainti ? new Vesi(sijX, sijY) : new Vesi();
                    break;

                default:
                    luotavaMaasto = null;
                    break;
            }
        }

        return luotavaMaasto;
    }

    static String luoMerkkijonotHuonekartasta(HashMap<Integer, Huone> huoneKartta) {
        String kokoTiedostoMerkkijonona = "";
        kokoTiedostoMerkkijonona += "<KEIMO>\n\n";
        String[] huoneetMerkkijonoina = new String[huoneKartta.size()];
        for (Integer i : huoneKartta.keySet()) {
            huoneetMerkkijonoina[i] = "";
            huoneetMerkkijonoina[i] += "Huone " + huoneKartta.get(i).annaId() + ":" + "\n    ";
            huoneetMerkkijonoina[i] += "#nimi: " + huoneKartta.get(i).annaNimi() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#alue: " + huoneKartta.get(i).annaAlue() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#tausta: " + huoneKartta.get(i).annaTausta() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#kenttä: " + "{\n";
            for (KenttäKohde[] kk : huoneKartta.get(i).annaHuoneenKenttäSisältö()) {
                for (KenttäKohde k : kk) {
                    if (k != null) {
                        if (k.onkoMääritettySijainti()) {
                            huoneetMerkkijonoina[i] += "        " + k.annaNimi() + "_" + k.annaSijX() + "_" + k.annaSijY();
                        }
                        else {
                            huoneetMerkkijonoina[i] += "        " + k.annaNimi();
                        }
                        if (k.onkolisäOminaisuuksia()) {
                            huoneetMerkkijonoina[i] += "+ominaisuudet:[";
                            for (String s : k.annalisäOminaisuudet()) {
                                huoneetMerkkijonoina[i] += s + ",";
                            }
                            huoneetMerkkijonoina[i] = huoneetMerkkijonoina[i].substring(0, huoneetMerkkijonoina[i].length()-1);
                            huoneetMerkkijonoina[i] += "]";
                        }
                        huoneetMerkkijonoina[i] += ",\n";
                    }
                }
            }
            if (huoneetMerkkijonoina[i].charAt(huoneetMerkkijonoina[i].length()-2 ) != '{' && huoneetMerkkijonoina[i].charAt(huoneetMerkkijonoina[i].length()-1 ) != '{') {
                huoneetMerkkijonoina[i] = huoneetMerkkijonoina[i].substring(0, huoneetMerkkijonoina[i].length()-2);
                huoneetMerkkijonoina[i] +=";\n";
            }
            huoneetMerkkijonoina[i] += "    }\n    ";

            huoneetMerkkijonoina[i] += "#maasto: " + "{\n";
            for (Maasto[] mm : huoneKartta.get(i).annaHuoneenMaastoSisältö()) {
                for (Maasto m : mm) {
                    if (m != null) {
                        if (m.onkoMääritettySijainti()) {
                            huoneetMerkkijonoina[i] += "        " + m.annaNimi() + "_" + m.annaSijX() + "_" + m.annaSijY();
                        }
                        else {
                            huoneetMerkkijonoina[i] += "        " + m.annaNimi();
                        }
                        if (m.onkolisäOminaisuuksia()) {
                            huoneetMerkkijonoina[i] += "+ominaisuudet:[";
                            for (String s : m.annalisäOminaisuudet()) {
                                huoneetMerkkijonoina[i] += s + ",";
                            }
                            huoneetMerkkijonoina[i] = huoneetMerkkijonoina[i].substring(0, huoneetMerkkijonoina[i].length()-1);
                            huoneetMerkkijonoina[i] += "]";
                        }
                        huoneetMerkkijonoina[i] += ",\n";
                    }
                }
            }
            if (huoneetMerkkijonoina[i].charAt(huoneetMerkkijonoina[i].length()-2 ) != '{' && huoneetMerkkijonoina[i].charAt(huoneetMerkkijonoina[i].length()-1 ) != '{') {
                huoneetMerkkijonoina[i] = huoneetMerkkijonoina[i].substring(0, huoneetMerkkijonoina[i].length()-2);
                huoneetMerkkijonoina[i] +=";\n";
            }

            huoneetMerkkijonoina[i] += "    }\n";
            kokoTiedostoMerkkijonona += huoneetMerkkijonoina[i];
            kokoTiedostoMerkkijonona += "/Huone" + "\n";
        }
        kokoTiedostoMerkkijonona += "\n</KEIMO>";
        return kokoTiedostoMerkkijonona;
    }
}
