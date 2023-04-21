package keimo.HuoneEditori;

import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import keimo.*;
import keimo.Kenttäkohteet.Warp.Suunta;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;

public class HuoneEditorinMetodit {
    
    static HashMap<Integer, Huone> luoHuoneKarttaMerkkijonosta(String[] merkkijonot) {
        HashMap<Integer, Huone> uusiHuoneKartta = new HashMap<Integer, Huone>();
        int uusiHuoneenId = 0;
        String uusiHuoneenNimi = "";
        String uusiHuoneenAlue = "";
        String uusiHuoneenTaustanPolku = "";

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

        String luotavaNPC = "";
        int luotavanNPCnX = 0;
        int luotavanNPCnY = 0;
        String[] luotavanNPCnOminaisuusLista = {""};
        ArrayList<NPC> uusiNPCLista = new ArrayList<NPC>();

        boolean uusiWarpVasen = false;
        int uusiWarpVasenHuoneId = 0;
        boolean uusiWarpOikea = false;
        int uusiWarpOikeaHuoneId = 0;
        boolean uusiWarpAlas = false;
        int uusiWarpAlasHuoneId = 0;
        boolean uusiWarpYlös = false;
        int uusiWarpYlösHuoneId = 0;

        int rivejäTarkastettu = 0;
        try {
            for (String s : merkkijonot) {
                Scanner sc = new Scanner(s);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = sc.nextLine();
                    rivejäTarkastettu++;
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
                        uusiHuoneenTaustanPolku = tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1);
                    }

                    else if (tarkastettavaRivi.contains("#warp_vasen:")) {
                        try {
                            uusiWarpVasenHuoneId = Integer.parseInt(tarkastettavaRivi.substring(17, tarkastettavaRivi.length() -1));
                            uusiWarpVasen = true;
                        }
                        catch (NumberFormatException e) {
                            uusiWarpVasen = false;
                        }
                        catch (StringIndexOutOfBoundsException e) {
                            uusiWarpVasen = false;
                        }
                    }
                    else if (tarkastettavaRivi.contains("#warp_oikea:")) {
                        try {
                            uusiWarpOikeaHuoneId = Integer.parseInt(tarkastettavaRivi.substring(17, tarkastettavaRivi.length() -1));
                            uusiWarpOikea = true;
                        }
                        catch (NumberFormatException e) {
                            uusiWarpOikea = false;
                        }
                        catch (StringIndexOutOfBoundsException e) {
                            uusiWarpOikea = false;
                        }
                    }
                    else if (tarkastettavaRivi.contains("#warp_alas:")) {
                        try {
                            uusiWarpAlasHuoneId = Integer.parseInt(tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1));
                            uusiWarpAlas = true;
                        }
                        catch (NumberFormatException e) {
                            uusiWarpAlas = false;
                        }
                        catch (StringIndexOutOfBoundsException e) {
                            uusiWarpAlas = false;
                        }
                    }
                    else if (tarkastettavaRivi.contains("#warp_ylös:")) {
                        try {
                            uusiWarpYlösHuoneId = Integer.parseInt(tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1));
                            uusiWarpYlös = true;
                        }
                        catch (NumberFormatException e) {
                            uusiWarpYlös = false;
                        }
                        catch (StringIndexOutOfBoundsException e) {
                            uusiWarpYlös = false;
                        }
                    }

                    if (tarkastettavaRivi.contains("#kenttä:")) {
                        if (tarkastettavaRivi.contains("{")) {
                            tarkastettavaRivi = sc.nextLine();
                            rivejäTarkastettu++;
                            while (!tarkastettavaRivi.contains("}")) {
                                luotavaObjekti = "";
                                luotavanObjektinX = 0;
                                luotavanObjektinY = 0;
                                if (tarkastettavaRivi.startsWith("        ")) {
                                    if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                        String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                        int ominaisuuksienMäärä = 0;
                                        for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                            if (tarkastettavaRivi.charAt(i) == '=') {
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
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, true, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
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
                                    rivejäTarkastettu++;
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
                            rivejäTarkastettu++;
                            while (!tarkastettavaRivi.contains("}")) {
                                luotavaMaasto = "";
                                luotavanMaastonX = 0;
                                luotavanMaastonY = 0;
                                if (tarkastettavaRivi.startsWith("        ")) {
                                    if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                        String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                        int ominaisuuksienMäärä = 0;
                                        for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                            if (tarkastettavaRivi.charAt(i) == '=') {
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
                                    rivejäTarkastettu++;
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    }
                    else if (tarkastettavaRivi.contains("#npc:")) {
                        if (tarkastettavaRivi.contains("{")) {
                            tarkastettavaRivi = sc.nextLine();
                            rivejäTarkastettu++;
                            while (!tarkastettavaRivi.contains("}")) {
                                luotavaNPC = "";
                                luotavanNPCnX = 0;
                                luotavanNPCnY = 0;
                                if (tarkastettavaRivi.startsWith("        ")) {
                                    if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                        String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                        int ominaisuuksienMäärä = 0;
                                        for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                            if (tarkastettavaRivi.charAt(i) == '=') {
                                                ominaisuuksienMäärä++;
                                            }
                                            else if (tarkastettavaRivi.charAt(i) == ']') {
                                                break;
                                            }
                                        }
                                        for (int i = 0; i < ominaisuuksienMäärä; i++) {
                                            luotavanNPCnOminaisuusLista = ominaisuudetMerkkijonona.split(",");
                                        }
                                        //for (String st : luotavanObjektinOminaisuusLista) {
                                        //    System.out.println("ominaisuus: " + st);
                                        //}
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                            luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                        luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                        uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, true, luotavanNPCnX, luotavanNPCnY, false, null));
                                    }
                                    else if (tarkastettavaRivi.contains(",")) {
                                        luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf(","));
                                        uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, false, null));
                                    }
                                }
                                if (sc.hasNextLine()) {
                                    tarkastettavaRivi = sc.nextLine();
                                    rivejäTarkastettu++;
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    }
                }
                Huone huone = new Huone(uusiHuoneenId, 10, uusiHuoneenNimi, uusiHuoneenTaustanPolku, uusiHuoneenAlue, uusiObjektiLista, uusiMaastoLista, uusiNPCLista, false, "");
                huone.päivitäReunawarppienTiedot(uusiWarpVasen, uusiWarpVasenHuoneId, uusiWarpOikea, uusiWarpOikeaHuoneId, uusiWarpAlas, uusiWarpAlasHuoneId, uusiWarpYlös, uusiWarpYlösHuoneId);
                //System.out.println("huone: " + huone.annaId() + ", vasen warp: " + huone.warpVasen + huone.warpVasenHuoneId + ", oikea warp: " + huone.warpOikea + huone.warpOikeaHuoneId + ", alas warp: " + huone.warpAlas + huone.warpAlasHuoneId + ", ylös warp: " + huone.warpYlös + huone.warpYlösHuoneId);
                uusiHuoneKartta.put(uusiHuoneenId, huone);
                uusiObjektiLista.clear();
                uusiMaastoLista.clear();
                uusiNPCLista.clear();
                uusiWarpVasen = false;
                uusiWarpOikea = false;
                uusiWarpAlas = false;
                uusiWarpYlös = false;
                sc.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            rivejäTarkastettu += 2;
            String virheViesti = "";
            virheViesti += ("Virheellinen rivi: " + rivejäTarkastettu + "\n\n");
            virheViesti += ("huone: " + uusiHuoneenId + "\n");
            virheViesti += ("nimi: " + uusiHuoneenNimi + "\n");
            virheViesti += ("alue: " + uusiHuoneenAlue + "\n\n");
            virheViesti += ("Virhe on todennäköisesti jossakin näistä: " + "\n");
            virheViesti += ("viimeisin ladattu objekti: " + luotavaObjekti + "\n");
            virheViesti += ("objektin X: " + luotavanObjektinX + "\n");
            virheViesti += ("objektin Y: " + luotavanObjektinY + "\n");
            virheViesti += ("viimeisin ladattu maasto: " + luotavaMaasto + "\n");
            virheViesti += ("maaston X: " + luotavanMaastonX + "\n");
            virheViesti += ("maaston Y: " + luotavanMaastonY + "\n");
            virheViesti += ("viimeisin ladattu npc: " + luotavaNPC + "\n");
            virheViesti += ("npc:n X: " + luotavanNPCnX + "\n");
            virheViesti += ("npc:n Y: " + luotavanNPCnY + "\n\n");
            virheViesti += ("^^Tyhjä, 0, 0 -> todennäköinen virhe");
            System.out.println(virheViesti);
            JOptionPane.showMessageDialog(null, "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla.\n\n" + virheViesti, "Virhe ladatessa tiedostoa.", JOptionPane.ERROR_MESSAGE);
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
                    luotavaObjekti = new Avain(määritettySijainti, sijX, sijY);
                    break;

                case "Hiili":
                    luotavaObjekti = new Hiili(määritettySijainti, sijX, sijY);
                    break;

                case "Huume":
                    luotavaObjekti = new Huume(määritettySijainti, sijX, sijY);
                    break;

                case "Juhani":
                    luotavaObjekti = new Juhani(määritettySijainti, sijX, sijY);
                    break;

                case "Kaasupullo":
                    luotavaObjekti = new Kaasupullo(määritettySijainti, sijX, sijY);
                    break;

                case "Kaasusytytin":
                    luotavaObjekti = new Kaasusytytin(määritettySijainti, sijX, sijY, "tyhjä");
                    break;

                case "Kauppa":
                    luotavaObjekti = new Kauppa(määritettySijainti, sijX, sijY);
                    break;

                case "Kilpi":
                    luotavaObjekti = new Kilpi(määritettySijainti, sijX, sijY);
                    break;

                case "Kirstu":
                    luotavaObjekti = new Kirstu(määritettySijainti, sijX, sijY, null);
                    break;

                case "Kuparilager":
                    luotavaObjekti = new Kuparilager(määritettySijainti, sijX, sijY);
                    break;

                case "Makkara":
                    luotavaObjekti = new Makkara(määritettySijainti, sijX, sijY);
                    break;

                case "Nuotio":
                    luotavaObjekti = new Nuotio(määritettySijainti, sijX, sijY);
                    break;

                case "Paperi":
                    luotavaObjekti = new Paperi(määritettySijainti, sijX, sijY);
                    break;

                case "Pesäpallomaila":
                    luotavaObjekti = new Pesäpallomaila(määritettySijainti, sijX, sijY);
                    break;

                case "Oviruutu":
                    luotavaObjekti = new Oviruutu(sijX, sijY, 0, 0, 0, Suunta.VASEN);
                    break;

                case "Seteli":
                    luotavaObjekti = new Seteli(määritettySijainti, sijX, sijY);
                    break;

                case "Suklaalevy":
                    luotavaObjekti = new Suklaalevy(määritettySijainti, sijX, sijY);
                    break;

                case "Vesiämpäri":
                    luotavaObjekti = new Vesiämpäri(määritettySijainti, sijX, sijY);
                    break;

                case "Ämpärikone":
                    luotavaObjekti = new Ämpärikone(määritettySijainti, sijX, sijY);
                    break;

                default:
                    luotavaObjekti = null;
                    break;
            }
        }
        else {
            switch (objektinNimi) {

                case "Kaasusytytin":
                    luotavaObjekti = määritettySijainti ? new Kaasusytytin(true, sijX, sijY, ominaisuusLista) : new Kaasusytytin(false, sijX, sijY, ominaisuusLista);
                    break;

                case "Oviruutu":
                    luotavaObjekti = new Oviruutu(sijX, sijY, ominaisuusLista);
                    break;

                case "Kirstu":
                    luotavaObjekti = new Kirstu(määritettySijainti, sijX, sijY, ominaisuusLista);
                    break;

                default:
                    luotavaObjekti = null;
                    break;
                }
        }
        return luotavaObjekti;
    }

    static Maasto luoMaastoTiedoilla(String maastonNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {

        Maasto luotavaMaasto;

        /**
         * Jos on määritetty sijainti, käytä objektille konstruktoria Maasto(int, int)
         * Muuten käytä parametritonta konstruktoria Maasto()
         */
        if (lisäOminaisuudet) {
            switch (maastonNimi) {

                case "Tile":
                    luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                    break;

                case "EsteTile":
                    luotavaMaasto = new EsteTile(sijX, sijY, ominaisuusLista);
                    break;

                default:
                    luotavaMaasto = null;
                    break;
            }
        }

        else {
            switch (maastonNimi) {

                default:
                    luotavaMaasto = null;
                    break;
            }
        }

        return luotavaMaasto;
    }

    static NPC luoNPCTiedoilla(String npcnNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {

        NPC luotavaNPC;

        /**
         * Jos on määritetty sijainti, käytä objektille konstruktoria Maasto(int, int)
         * Muuten käytä parametritonta konstruktoria Maasto()
         */
        if (lisäOminaisuudet) {
            switch (npcnNimi) {

                case "Pikkuvihu":
                    luotavaNPC = new Pikkuvihu(sijX, sijY, ominaisuusLista);
                    break;

                case "Pahavihu":
                    luotavaNPC = new Pahavihu(sijX, sijY, ominaisuusLista);
                    break;

                default:
                    luotavaNPC = null;
                    break;
            }
        }

        else {
            switch (npcnNimi) {

                case "Pikkuvihu":
                    luotavaNPC = new Pikkuvihu(sijX, sijY, null);
                    break;

                case "Pahavihu":
                    luotavaNPC = new Pahavihu(sijX, sijY, null);
                    break;

                default:
                    luotavaNPC = null;
                    break;
            }
        }
        if (luotavaNPC == null) {
            JOptionPane.showMessageDialog(null, "Ei voi ladata npc:tä", "luotavaNPC = null", JOptionPane.ERROR_MESSAGE);
        }
        return luotavaNPC;
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
            huoneetMerkkijonoina[i] += "#tausta: " + huoneKartta.get(i).annaTaustanPolku() + ";" + "\n    ";

            if (huoneKartta.get(i).annaReunaWarppiTiedot(Suunta.VASEN)) {
                huoneetMerkkijonoina[i] += "#warp_vasen: " + huoneKartta.get(i).annaReunaWarpinKohdeId(Suunta.VASEN) + ";" + "\n    ";
            }
            else {
                huoneetMerkkijonoina[i] += "#warp_vasen: " + ";" + "\n    ";
            }
            if (huoneKartta.get(i).annaReunaWarppiTiedot(Suunta.OIKEA)) {
                huoneetMerkkijonoina[i] += "#warp_oikea: " + huoneKartta.get(i).annaReunaWarpinKohdeId(Suunta.OIKEA) + ";" + "\n    ";
            }
            else {
                huoneetMerkkijonoina[i] += "#warp_oikea: " + ";" + "\n    ";
            }
            if (huoneKartta.get(i).annaReunaWarppiTiedot(Suunta.ALAS)) {
                huoneetMerkkijonoina[i] += "#warp_alas: " + huoneKartta.get(i).annaReunaWarpinKohdeId(Suunta.ALAS) + ";" + "\n    ";
            }
            else {
                huoneetMerkkijonoina[i] += "#warp_alas: " + ";" + "\n    ";
            }
            if (huoneKartta.get(i).annaReunaWarppiTiedot(Suunta.YLÖS)) {
                huoneetMerkkijonoina[i] += "#warp_ylös: " + huoneKartta.get(i).annaReunaWarpinKohdeId(Suunta.YLÖS) + ";" + "\n    ";
            }
            else {
                huoneetMerkkijonoina[i] += "#warp_ylös: " + ";" + "\n    ";
            }

            try {
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
            }
            catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Ei voitu tallentaa objekteja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa objekteja", JOptionPane.ERROR_MESSAGE);
                huoneetMerkkijonoina[i] += "\n    }\n";
            }

            try {
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
                huoneetMerkkijonoina[i] += "    }\n    ";
            }
            catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Ei voitu tallentaa maastoa.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa maastoa", JOptionPane.ERROR_MESSAGE);
                huoneetMerkkijonoina[i] += "\n    }\n";
            }

            try {
                huoneetMerkkijonoina[i] += "#npc: " + "{\n";
                for (NPC[] nn : huoneKartta.get(i).annaHuoneenNPCSisältö()) {
                    for (NPC n : nn) {
                        if (n != null) {
                            if (n.onkoMääritettySijainti()) {
                                huoneetMerkkijonoina[i] += "        " + n.annaNimi() + "_" + n.annaSijX() + "_" + n.annaSijY();
                            }
                            else {
                                huoneetMerkkijonoina[i] += "        " + n.annaNimi();
                            }
                            if (n.onkolisäOminaisuuksia()) {
                                huoneetMerkkijonoina[i] += "+ominaisuudet:[";
                                for (String s : n.annalisäOminaisuudet()) {
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
            }
            catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Ei voitu tallentaa npc:itä.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa NPC:itä", JOptionPane.ERROR_MESSAGE);
                huoneetMerkkijonoina[i] += "\n    }\n";
            }

            kokoTiedostoMerkkijonona += huoneetMerkkijonoina[i];
            kokoTiedostoMerkkijonona += "/Huone" + "\n";
        }
        kokoTiedostoMerkkijonona += "\n</KEIMO>";
        return kokoTiedostoMerkkijonona;
    }
}
