package keimo.HuoneEditori;

import keimo.*;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogiPätkä;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogit;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.Ikkunat.LatausIkkuna;
import keimo.Maastot.*;
import keimo.Utility.Käännettävä.Suunta;
import keimo.entityt.*;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class HuoneEditorinMetodit {

    static void lataaAsetuksetMerkkijonosta(String asetusMerkkijono) {
        try {
            Scanner sc = new Scanner(asetusMerkkijono);
            while (sc.hasNextLine()) {
                String tarkastettavaRivi = sc.nextLine();
                if (tarkastettavaRivi.contains("#alkuhuone: ")) {
                    Pelaaja.alkuHuone = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("#alkuhuone: ") + 12, tarkastettavaRivi.length()-1));
                    Peli.uusiHuone = Pelaaja.alkuHuone;
                }
                else if (tarkastettavaRivi.contains("#alkuX: ")) {
                    Pelaaja.alkuSijX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("#alkuX: ") + 8, tarkastettavaRivi.length()-1));
                }
                else if (tarkastettavaRivi.contains("#alkuY: ")) {
                    Pelaaja.alkuSijY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("#alkuY: ") + 8, tarkastettavaRivi.length()-1));
                }
            }
            sc.close();
        }
        catch (NumberFormatException nfe) {
            System.out.println("virhe lukiessa merkkijonoa");
            nfe.printStackTrace();
        }
    }
    
    static HashMap<Integer, Huone> luoHuoneKarttaMerkkijonosta(String[] huoneMerkkijonot) {
        HashMap<Integer, Huone> uusiHuoneKartta = new HashMap<Integer, Huone>();
        int uusiHuoneenId = 0;
        int uusiHuoneenKoko = 10;
        String uusiHuoneenNimi = "";
        String uusiHuoneenAlue = "";
        String uusiHuoneenTaustanPolku = "";
        String uusiHuoneenTarinanTunniste = null;
        String uusiHuoneenVaadittuTavoite = null;
        String uusiMusa = null;

        String luotavaObjekti = "";
        int luotavanObjektinX = 0;
        int luotavanObjektinY = 0;
        String[] luotavanObjektinOminaisuusLista = {""};
        ArrayList<KenttäKohde> uusiObjektiLista = new ArrayList<>();

        String luotavaMaasto = "";
        int luotavanMaastonX = 0;
        int luotavanMaastonY = 0;
        String[] luotavanMaastonOminaisuusLista = {""};
        ArrayList<Maasto> uusiMaastoLista = new ArrayList<>();

        String luotavaNPC = "";
        int luotavanNPCnX = 0;
        int luotavanNPCnY = 0;
        String[] luotavanNPCnOminaisuusLista = {""};
        ArrayList<Entity> uusiNPCLista = new ArrayList<>();

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
            KenttäKohde.nollaaObjektiId();
            TarinaPätkä.nollaaTarinaId();
            for (String s : huoneMerkkijonot) {
                Scanner sc = new Scanner(s);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = sc.nextLine();
                    rivejäTarkastettu++;
                    if (tarkastettavaRivi.startsWith("Huone")) {
                        uusiHuoneenId = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("#koko:")) {
                        try {
                            uusiHuoneenKoko = Integer.parseInt(tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1));
                        }
                        catch (NumberFormatException nfe) {
                            System.out.println("Virheellinen koko. Asetetaan huoneen " + uusiHuoneenId + " kooksi 10.");
                            uusiHuoneenKoko = 10;
                        }
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
                    else if (tarkastettavaRivi.contains("#musa:")) {
                        uusiMusa = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
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
                    else if (tarkastettavaRivi.contains("#tarina:")) {
                        try {
                            uusiHuoneenTarinanTunniste = tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1);
                            if (uusiHuoneenTarinanTunniste == null || uusiHuoneenTarinanTunniste == "") {
                                uusiHuoneenTarinanTunniste = null;
                            }
                        }
                        catch (IndexOutOfBoundsException e) {
                            uusiHuoneenTarinanTunniste = null;
                        }
                    }
                    else if (tarkastettavaRivi.contains("#tavoite:")) {
                        try {
                            uusiHuoneenVaadittuTavoite = tarkastettavaRivi.substring(14, tarkastettavaRivi.length() -1);
                            if (uusiHuoneenVaadittuTavoite == null || uusiHuoneenVaadittuTavoite == "") {
                                uusiHuoneenVaadittuTavoite = null;
                            }
                        }
                        catch (IndexOutOfBoundsException e) {
                            uusiHuoneenVaadittuTavoite = null;
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
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                            int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                            luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                            int yAlkuIndeksi = xLoppuIndeksi +1;
                                            int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                                if (yLoppuIndeksi == -1) {
                                                    yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                                }
                                            }
                                            luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, true, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, false, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                        int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                        luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                        int yAlkuIndeksi = xLoppuIndeksi +1;
                                        int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                        if (yLoppuIndeksi == -1) {
                                            yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                            }
                                        }
                                        luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
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
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                            int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                            luotavanMaastonX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                            int yAlkuIndeksi = xLoppuIndeksi +1;
                                            int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                                if (yLoppuIndeksi == -1) {
                                                    yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                                }
                                            }
                                            luotavanMaastonY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                        int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                        luotavanMaastonX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                        int yAlkuIndeksi = xLoppuIndeksi +1;
                                        int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                        if (yLoppuIndeksi == -1) {
                                            yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                            }
                                        }
                                            luotavanMaastonY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
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
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                            int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                            luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                            int yAlkuIndeksi = xLoppuIndeksi +1;
                                            int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                                if (yLoppuIndeksi == -1) {
                                                    yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                                }
                                            }
                                            luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                        int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                        luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                        int yAlkuIndeksi = xLoppuIndeksi +1;
                                        int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                        if (yLoppuIndeksi == -1) {
                                            yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                            }
                                        }
                                        luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
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
                Huone huone = new Huone(uusiHuoneenId, uusiHuoneenKoko, uusiHuoneenNimi, uusiHuoneenTaustanPolku, uusiHuoneenAlue, uusiObjektiLista, uusiMaastoLista, uusiNPCLista, uusiMusa, uusiHuoneenTarinanTunniste, uusiHuoneenVaadittuTavoite);
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
            JOptionPane.showMessageDialog(LatausIkkuna.ikkuna(), "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla tai tiedosto on yhteensopimaton nykyisen pelin/editorin version kanssa.\n\n" + virheViesti, "Virhe ladatessa tiedostoa.", JOptionPane.ERROR_MESSAGE);
        }
        return uusiHuoneKartta;
    }

    static Huone luoHuoneMerkkijonosta(String merkkijono, int id) {
        int uusiHuoneenId = id;
        int uusiHuoneenKoko = 10;
        String uusiHuoneenNimi = "";
        String uusiHuoneenAlue = "";
        String uusiHuoneenTaustanPolku = "";
        String uusiHuoneenTarinanTunniste = null;
        String uusiHuoneenVaadittuTavoite = null;
        String uusiMusa = null;

        String luotavaObjekti = "";
        int luotavanObjektinX = 0;
        int luotavanObjektinY = 0;
        String[] luotavanObjektinOminaisuusLista = {""};
        ArrayList<KenttäKohde> uusiObjektiLista = new ArrayList<>();

        String luotavaMaasto = "";
        int luotavanMaastonX = 0;
        int luotavanMaastonY = 0;
        String[] luotavanMaastonOminaisuusLista = {""};
        ArrayList<Maasto> uusiMaastoLista = new ArrayList<>();

        String luotavaNPC = "";
        int luotavanNPCnX = 0;
        int luotavanNPCnY = 0;
        String[] luotavanNPCnOminaisuusLista = {""};
        ArrayList<Entity> uusiNPCLista = new ArrayList<>();

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
            KenttäKohde.nollaaObjektiId();
            TarinaPätkä.nollaaTarinaId();
            //for (String s : huoneMerkkijonot) {
                Scanner sc = new Scanner(merkkijono);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = sc.nextLine();
                    rivejäTarkastettu++;
                    if (tarkastettavaRivi.startsWith("Huone")) {
                        //uusiHuoneenId = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("#koko:")) {
                        try {
                            uusiHuoneenKoko = Integer.parseInt(tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1));
                        }
                        catch (NumberFormatException nfe) {
                            System.out.println("Virheellinen koko. Asetetaan huoneen " + uusiHuoneenId + " kooksi 10.");
                            uusiHuoneenKoko = 10;
                        }
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
                    else if (tarkastettavaRivi.contains("#musa:")) {
                        uusiMusa = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
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
                    else if (tarkastettavaRivi.contains("#tarina:")) {
                        try {
                            uusiHuoneenTarinanTunniste = tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1);
                            if (uusiHuoneenTarinanTunniste == null || uusiHuoneenTarinanTunniste == "") {
                                uusiHuoneenTarinanTunniste = null;
                            }
                        }
                        catch (IndexOutOfBoundsException e) {
                            uusiHuoneenTarinanTunniste = null;
                        }
                    }
                    else if (tarkastettavaRivi.contains("#tavoite:")) {
                        try {
                            uusiHuoneenVaadittuTavoite = tarkastettavaRivi.substring(14, tarkastettavaRivi.length() -1);
                            if (uusiHuoneenVaadittuTavoite == null || uusiHuoneenVaadittuTavoite == "") {
                                uusiHuoneenVaadittuTavoite = null;
                            }
                        }
                        catch (IndexOutOfBoundsException e) {
                            uusiHuoneenVaadittuTavoite = null;
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
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                            int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                            luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                            int yAlkuIndeksi = xLoppuIndeksi +1;
                                            int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                                if (yLoppuIndeksi == -1) {
                                                    yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                                }
                                            }
                                            luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, true, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, false, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                        int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                        luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                        int yAlkuIndeksi = xLoppuIndeksi +1;
                                        int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                        if (yLoppuIndeksi == -1) {
                                            yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                            }
                                        }
                                        luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
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
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                            int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                            luotavanMaastonX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                            int yAlkuIndeksi = xLoppuIndeksi +1;
                                            int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                                if (yLoppuIndeksi == -1) {
                                                    yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                                }
                                            }
                                            luotavanMaastonY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, false, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                        int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                        luotavanMaastonX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                        int yAlkuIndeksi = xLoppuIndeksi +1;
                                        int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                        if (yLoppuIndeksi == -1) {
                                            yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                            }
                                        }
                                        luotavanMaastonY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
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
                                        if (tarkastettavaRivi.contains("_")) {
                                            luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                            int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                            int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                            luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                            int yAlkuIndeksi = xLoppuIndeksi +1;
                                            int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                                if (yLoppuIndeksi == -1) {
                                                    yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                                }
                                            }
                                            luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                        else if (tarkastettavaRivi.contains("+")){
                                            luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                    }
                                    else if (tarkastettavaRivi.contains("_")) {
                                        luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                        int xAlkuIndeksi = tarkastettavaRivi.indexOf("_") +1;
                                        int xLoppuIndeksi = tarkastettavaRivi.indexOf("_", xAlkuIndeksi);
                                        luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(xAlkuIndeksi, xLoppuIndeksi));
                                        int yAlkuIndeksi = xLoppuIndeksi +1;
                                        int yLoppuIndeksi = tarkastettavaRivi.indexOf("+", yAlkuIndeksi);
                                        if (yLoppuIndeksi == -1) {
                                            yLoppuIndeksi = tarkastettavaRivi.indexOf(",", yAlkuIndeksi);
                                            if (yLoppuIndeksi == -1) {
                                                yLoppuIndeksi = tarkastettavaRivi.indexOf(";", yAlkuIndeksi);
                                            }
                                        }
                                        luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(yAlkuIndeksi, yLoppuIndeksi));
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
                Huone huone = new Huone(uusiHuoneenId, uusiHuoneenKoko, uusiHuoneenNimi, uusiHuoneenTaustanPolku, uusiHuoneenAlue, uusiObjektiLista, uusiMaastoLista, uusiNPCLista, uusiMusa, uusiHuoneenTarinanTunniste, uusiHuoneenVaadittuTavoite);
                huone.päivitäReunawarppienTiedot(uusiWarpVasen, uusiWarpVasenHuoneId, uusiWarpOikea, uusiWarpOikeaHuoneId, uusiWarpAlas, uusiWarpAlasHuoneId, uusiWarpYlös, uusiWarpYlösHuoneId);
                //System.out.println("huone: " + huone.annaId() + ", vasen warp: " + huone.warpVasen + huone.warpVasenHuoneId + ", oikea warp: " + huone.warpOikea + huone.warpOikeaHuoneId + ", alas warp: " + huone.warpAlas + huone.warpAlasHuoneId + ", ylös warp: " + huone.warpYlös + huone.warpYlösHuoneId);
                //uusiHuoneKartta.put(uusiHuoneenId, huone);
                uusiObjektiLista.clear();
                uusiMaastoLista.clear();
                uusiNPCLista.clear();
                uusiWarpVasen = false;
                uusiWarpOikea = false;
                uusiWarpAlas = false;
                uusiWarpYlös = false;
                sc.close();
                return huone;
            //}
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
            JOptionPane.showMessageDialog(null, "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla tai tiedosto on yhteensopimaton nykyisen pelin/editorin version kanssa.\n\n" + virheViesti, "Virhe ladatessa tiedostoa.", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    static HashMap<String, TarinaPätkä> luoTarinaKarttaMerkkijonosta(String[] tarinaMerkkijonot) {
        
        HashMap<String, TarinaPätkä> uusiTarinaKartta = new HashMap<>();

        TarinaDialogiLista.tarinaKartta.clear();
        String uusiTarinanNimi = "";
        int uusiTarinanPituus = 0;
        ImageIcon[] uudetTarinaPätkänKuvat = new ImageIcon[uusiTarinanPituus];
        String[] uudetTarinaPätkänKuvatiedostot = new String[uusiTarinanPituus];
        String[] uudetTarinaPätkänTekstit = new String[uusiTarinanPituus];

        try {
            for (String s : tarinaMerkkijonot) {
                Scanner sc = new Scanner(s);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = "";
                    tarkastettavaRivi = sc.nextLine();
                    if (tarkastettavaRivi.startsWith("Tarina")) {

                    }
                    else if (tarkastettavaRivi.contains("#nimi:")) {
                        uusiTarinanNimi = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#pituus:")) {
                        uusiTarinanPituus = Integer.parseInt(tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("sivut:")) {
                        uudetTarinaPätkänKuvat = new ImageIcon[uusiTarinanPituus];
                        uudetTarinaPätkänKuvatiedostot = new String[uusiTarinanPituus];
                        uudetTarinaPätkänTekstit = new String[uusiTarinanPituus];
                        for (int i = 0; i < uusiTarinanPituus*2; i++) {
                            tarkastettavaRivi = sc.nextLine();
                            if (tarkastettavaRivi.contains("kuva ")) {
                                int kuvanNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("kuva ") +5, tarkastettavaRivi.indexOf("kuva ") +6));
                                uudetTarinaPätkänKuvat[kuvanNumero] = new ImageIcon(tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1));
                                uudetTarinaPätkänKuvatiedostot[kuvanNumero] = tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1);
                            }
                            else if (tarkastettavaRivi.contains("teksti ")) {
                                int tekstinNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("teksti ") +7, tarkastettavaRivi.indexOf("teksti ") +8));
                                uudetTarinaPätkänTekstit[tekstinNumero] = tarkastettavaRivi.substring(18, tarkastettavaRivi.length() -1);
                            }
                        }
                    }
                }
                uusiTarinaKartta.put(uusiTarinanNimi, new TarinaPätkä(uusiTarinanNimi, uusiTarinanPituus, uudetTarinaPätkänKuvatiedostot, uudetTarinaPätkänKuvat, uudetTarinaPätkänTekstit));
                sc.close();
            }
        }
        catch (NumberFormatException nfe) {
            System.out.println("Virheellinen indeksi");
            nfe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Virhe ladatessa tarinaa tiedostosta");
            e.printStackTrace();
        }
        return uusiTarinaKartta;
    }

    static HashMap<String, VuoropuheDialogiPätkä> luoDialogiKarttaMerkkijonosta(String[] tarinaMerkkijonot) {
        
        HashMap<String, VuoropuheDialogiPätkä> uusiDialogiKartta = new HashMap<>();

        VuoropuheDialogit.vuoropuheDialogiKartta.clear();
        String uusiDialoginNimi = "";
        int uusiDialoginPituus = 0;
        String[] uudetDialogiPätkänKuvatiedostot = new String[uusiDialoginPituus];
        String[] uudetDialogiPätkänTekstit = new String[uusiDialoginPituus];
        String[] uudetDialogiPätkänPuhujat = new String[uusiDialoginPituus];
        boolean valinta = false;
        String valinnanNimi = "";
        String valinnanOtsikko = "";
        String[] valinnanVaihtoehdot = new String[uusiDialoginPituus];
        String[] vaihtoehtojenKohdedialogit = new String[uusiDialoginPituus];
        String[] valinnanTriggerit = new String[uusiDialoginPituus];

        try {
            for (String s : tarinaMerkkijonot) {
                Scanner sc = new Scanner(s);
                valinta = false;
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = "";
                    tarkastettavaRivi = sc.nextLine();
                    if (tarkastettavaRivi.startsWith("Dialogi")) {

                    }
                    else if (tarkastettavaRivi.contains("#nimi:")) {
                        uusiDialoginNimi = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#pituus:")) {
                        uusiDialoginPituus = Integer.parseInt(tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("dialogit:")) {
                        uudetDialogiPätkänKuvatiedostot = new String[uusiDialoginPituus];
                        uudetDialogiPätkänTekstit = new String[uusiDialoginPituus];
                        uudetDialogiPätkänPuhujat = new String[uusiDialoginPituus];
                        for (int i = 0; i < uusiDialoginPituus*3; i++) {
                            tarkastettavaRivi = sc.nextLine();
                            if (tarkastettavaRivi.contains("kuva ")) {
                                int kuvanNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("kuva ") +5, tarkastettavaRivi.indexOf("kuva ") +6));
                                uudetDialogiPätkänKuvatiedostot[kuvanNumero] = tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1);
                            }
                            else if (tarkastettavaRivi.contains("teksti ")) {
                                int tekstinNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("teksti ") +7, tarkastettavaRivi.indexOf("teksti ") +8));
                                uudetDialogiPätkänTekstit[tekstinNumero] = tarkastettavaRivi.substring(18, tarkastettavaRivi.length() -1);
                            }
                            else if (tarkastettavaRivi.contains("puhuja ")) {
                                int puhujanNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("puhuja ") +7, tarkastettavaRivi.indexOf("puhuja ") +8));
                                uudetDialogiPätkänPuhujat[puhujanNumero] = tarkastettavaRivi.substring(18, tarkastettavaRivi.length() -1);
                            }   
                        }
                    }
                    else if (tarkastettavaRivi.contains("valinta:")) {
                        valinta = true;
                        int vaihtoehdot = 0;
                        boolean vaihtoehdotLuotu = false;
                        while (!tarkastettavaRivi.contains("}")) {
                            tarkastettavaRivi = sc.nextLine();
                            if (tarkastettavaRivi.contains("valinnan_nimi")) {
                                valinnanNimi = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("valinnan_nimi") +15, tarkastettavaRivi.length()-1);
                            }
                            else if (tarkastettavaRivi.contains("valinnan_otsikko")) {
                                valinnanOtsikko = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("valinnan_otsikko") +18, tarkastettavaRivi.length()-1);
                            }
                            else if (tarkastettavaRivi.contains("vaihtoehdot")) {
                                vaihtoehdot = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("vaihtoehdot") +13, tarkastettavaRivi.length()-1));
                            }
                            if (vaihtoehdot > 0 && ! vaihtoehdotLuotu) {
                                valinnanVaihtoehdot = new String[vaihtoehdot];
                                vaihtoehtojenKohdedialogit = new String[vaihtoehdot];
                                valinnanTriggerit = new String[vaihtoehdot];
                                while (sc.hasNextLine() && !tarkastettavaRivi.contains("}")) {
                                    tarkastettavaRivi = sc.nextLine();
                                    if (tarkastettavaRivi.contains("vaihtoehto ")) {
                                        int vaihtoehdonNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("vaihtoehto ") +11, tarkastettavaRivi.indexOf("vaihtoehto ") +12));
                                        valinnanVaihtoehdot[vaihtoehdonNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("vaihtoehto ") +14, tarkastettavaRivi.length() -1);
                                    }
                                    else if (tarkastettavaRivi.contains("triggeri ")) {
                                        int triggerinNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("triggeri ") +9, tarkastettavaRivi.indexOf("triggeri ") +10));
                                        valinnanTriggerit[triggerinNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("triggeri ") +12, tarkastettavaRivi.length() -1);
                                    }
                                    else if (tarkastettavaRivi.contains("kohde ")) {
                                        int kohteenNumero = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("kohde ") +6, tarkastettavaRivi.indexOf("kohde ") +7));
                                        vaihtoehtojenKohdedialogit[kohteenNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("kohde ") +9, tarkastettavaRivi.length() -1);
                                    }
                                }
                                vaihtoehdotLuotu = true;
                            }
                        }
                    }
                }
                if (valinta) uusiDialogiKartta.put(uusiDialoginNimi, new VuoropuheDialogiPätkä(uusiDialoginNimi, uusiDialoginPituus, uudetDialogiPätkänKuvatiedostot, uudetDialogiPätkänTekstit, uudetDialogiPätkänPuhujat, true, valinnanNimi, valinnanOtsikko, valinnanVaihtoehdot, vaihtoehtojenKohdedialogit, valinnanTriggerit));
                else uusiDialogiKartta.put(uusiDialoginNimi, new VuoropuheDialogiPätkä(uusiDialoginNimi, uusiDialoginPituus, uudetDialogiPätkänKuvatiedostot, uudetDialogiPätkänTekstit, uudetDialogiPätkänPuhujat, false, null, null, null, null, null));
                sc.close();
            }
        }
        catch (NumberFormatException nfe) {
            System.out.println("Virheellinen indeksi");
            nfe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Virhe ladatessa tarinaa tiedostosta");
            e.printStackTrace();
        }
        return uusiDialogiKartta;
    }

    static KenttäKohde luoObjektiTiedoilla(String objektinNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {
        return KenttäKohde.luoObjektiTiedoilla(objektinNimi, määritettySijainti, sijX, sijY, ominaisuusLista);
    }

    static Maasto luoMaastoTiedoilla(String maastonNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {
        return Maasto.luoMaastoTiedoilla(maastonNimi, määritettySijainti, sijX, sijY, ominaisuusLista);
    }

    static Entity luoNPCTiedoilla(String npcnNimi, boolean määritettySijainti, int sijX, int sijY, boolean lisäOminaisuudet, String[] ominaisuusLista) {
        return Entity.luoEntityTiedoilla(npcnNimi, määritettySijainti, sijX, sijY, ominaisuusLista);
    }

    static String luoMerkkijonotHuonekartasta(HashMap<Integer, Huone> huoneKartta, HashMap<String, TarinaPätkä> tarinaKartta, HashMap<String, VuoropuheDialogiPätkä> dialogiKartta) {
        String kokoTiedostoMerkkijonona = "";
        kokoTiedostoMerkkijonona += "<KEIMO>\n\n";
        kokoTiedostoMerkkijonona += "Asetukset:" + "\n    ";
        kokoTiedostoMerkkijonona += "#alkuhuone: " + Pelaaja.alkuHuone + ";" + "\n    ";
        kokoTiedostoMerkkijonona += "#alkuX: " + Pelaaja.alkuSijX + ";" + "\n    ";
        kokoTiedostoMerkkijonona += "#alkuY: " + Pelaaja.alkuSijY + ";" + "\n";
        kokoTiedostoMerkkijonona += "/Asetukset" + "\n";

        String[] huoneetMerkkijonoina = new String[huoneKartta.size()];
        for (Integer i : huoneKartta.keySet()) {
            huoneetMerkkijonoina[i] = "";
            huoneetMerkkijonoina[i] += "Huone " + huoneKartta.get(i).annaId() + ":" + "\n    ";
            huoneetMerkkijonoina[i] += "#koko: " + huoneKartta.get(i).annaKoko() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#nimi: " + huoneKartta.get(i).annaNimi() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#alue: " + huoneKartta.get(i).annaAlue() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#tausta: " + huoneKartta.get(i).annaTaustanPolku() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#musa: " + huoneKartta.get(i).annaHuoneenMusa() + ";" + "\n    ";

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

            if (huoneKartta.get(i).annaTarinaRuudunLataus()) {
                huoneetMerkkijonoina[i] += "#tarina: " + huoneKartta.get(i).annaTarinaRuudunTunniste() + ";" + "\n    ";
            }
            else {
                huoneetMerkkijonoina[i] += "#tarina: " + ";" + "\n    ";
            }
            if (huoneKartta.get(i).annaTavoiteVaatimus()) {
                huoneetMerkkijonoina[i] += "#tavoite: " + huoneKartta.get(i).annaVaaditunTavoitteenTunniste() + ";" + "\n    ";
            }
            else {
                huoneetMerkkijonoina[i] += "#tavoite: " + ";" + "\n    ";
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
                                for (String s : m.annaLisäOminaisuudet()) {
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
                for (Entity[] nn : huoneKartta.get(i).annaHuoneenNPCSisältö()) {
                    for (Entity n : nn) {
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
        if (tarinaKartta != null) {
            String[] tarinaDialogitMerkkijonoina = new String[TarinaDialogiLista.tarinaKartta.size()];
            Object[] tarinanTunnisteet = tarinaKartta.keySet().toArray();
            for (int i = 0; i < TarinaDialogiLista.tarinaKartta.size(); i++) {
                tarinaDialogitMerkkijonoina[i] = "";
                tarinaDialogitMerkkijonoina[i] += "Tarina " + tarinaKartta.get(tarinanTunnisteet[i]).annaId() + ":" + "\n    ";
                tarinaDialogitMerkkijonoina[i] += "#nimi: " + tarinaKartta.get(tarinanTunnisteet[i]).annaNimi() + ";" + "\n    ";
                tarinaDialogitMerkkijonoina[i] += "#pituus: " + tarinaKartta.get(tarinanTunnisteet[i]).annaPituus() + ";" + "\n    ";
                try {
                    tarinaDialogitMerkkijonoina[i] += "#sivut: " + "{\n";
                    TarinaPätkä tp = tarinaKartta.get(tarinanTunnisteet[i]);
                    for (int j = 0; j < tp.annaPituus(); j++) {
                        tarinaDialogitMerkkijonoina[i] += "        kuva " + j + ": " + tp.annaKuvatiedostot()[j] + ";\n";
                        tarinaDialogitMerkkijonoina[i] += "        teksti " + j + ": " + tp.annaTekstit()[j] + ";\n";
                    }
                    if (tarinaDialogitMerkkijonoina[i].charAt(tarinaDialogitMerkkijonoina[i].length()-2 ) != '{' && tarinaDialogitMerkkijonoina[i].charAt(tarinaDialogitMerkkijonoina[i].length()-1 ) != '{') {
                        tarinaDialogitMerkkijonoina[i] = tarinaDialogitMerkkijonoina[i].substring(0, tarinaDialogitMerkkijonoina[i].length()-2);
                        tarinaDialogitMerkkijonoina[i] +=";\n";
                    }
                    tarinaDialogitMerkkijonoina[i] += "    }\n";
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Ei voitu tallentaa tarinan sivuja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa tarinaa", JOptionPane.ERROR_MESSAGE);
                    tarinaDialogitMerkkijonoina[i] += "\n    }\n";
                }
                kokoTiedostoMerkkijonona += tarinaDialogitMerkkijonoina[i];
                kokoTiedostoMerkkijonona += "/Tarina" + "\n";
            }
        }
        if (dialogiKartta != null) {
            String[] vuoropuheDialogitMerkkijonoina = new String[Dialogit.PitkätDialogit.vuoropuheDialogiKartta.size()];
            Object[] dialoginTunnisteet = dialogiKartta.keySet().toArray();
            for (int i = 0; i < Dialogit.PitkätDialogit.vuoropuheDialogiKartta.size(); i++) {
                vuoropuheDialogitMerkkijonoina[i] = "";
                vuoropuheDialogitMerkkijonoina[i] += "Dialogi " + dialogiKartta.get(dialoginTunnisteet[i]).annaId() + ":" + "\n    ";
                vuoropuheDialogitMerkkijonoina[i] += "#nimi: " + dialogiKartta.get(dialoginTunnisteet[i]).annaTunniste() + ";" + "\n    ";
                vuoropuheDialogitMerkkijonoina[i] += "#pituus: " + dialogiKartta.get(dialoginTunnisteet[i]).annaPituus() + ";" + "\n    ";
                try {
                    vuoropuheDialogitMerkkijonoina[i] += "#dialogit: " + "{\n";
                    VuoropuheDialogiPätkä vdp = dialogiKartta.get(dialoginTunnisteet[i]);
                    for (int j = 0; j < vdp.annaPituus(); j++) {
                        vuoropuheDialogitMerkkijonoina[i] += "        kuva " + j + ": " + vdp.annaKuvienTiedostoNimet()[j] + ";\n";
                        vuoropuheDialogitMerkkijonoina[i] += "        teksti " + j + ": " + vdp.annaTekstit()[j] + ";\n";
                        vuoropuheDialogitMerkkijonoina[i] += "        puhuja " + j + ": " + vdp.annaPuhujat()[j] + ";\n";
                    }
                    if (vuoropuheDialogitMerkkijonoina[i].charAt(vuoropuheDialogitMerkkijonoina[i].length()-2 ) != '{' && vuoropuheDialogitMerkkijonoina[i].charAt(vuoropuheDialogitMerkkijonoina[i].length()-1 ) != '{') {
                        vuoropuheDialogitMerkkijonoina[i] = vuoropuheDialogitMerkkijonoina[i].substring(0, vuoropuheDialogitMerkkijonoina[i].length()-2);
                        vuoropuheDialogitMerkkijonoina[i] +=";\n";
                    }
                    vuoropuheDialogitMerkkijonoina[i] += "    }\n";
                    if (vdp.onkoValinta()) {
                        vuoropuheDialogitMerkkijonoina[i] += "    #valinta: " + "{\n";
                        vuoropuheDialogitMerkkijonoina[i] += "        valinnan_nimi: " + vdp.annaValinnanNimi() + ";\n";
                        vuoropuheDialogitMerkkijonoina[i] += "        valinnan_otsikko: " + vdp.annaValinnanOtsikko() + ";\n";
                        vuoropuheDialogitMerkkijonoina[i] += "        vaihtoehdot: " + vdp.annaValinnanVaihtoehdot().length + ";\n";
                        for (int j = 0; j < vdp.annaPituus(); j++) {
                            vuoropuheDialogitMerkkijonoina[i] += "        vaihtoehto " + j + ": " + vdp.annaValinnanVaihtoehdot()[j] + ";\n";
                            vuoropuheDialogitMerkkijonoina[i] += "        triggeri " + j + ": " + vdp.annaTriggerit()[j] + ";\n";
                            vuoropuheDialogitMerkkijonoina[i] += "        kohde " + j + ": " + vdp.annaValinnanVaihtoehtojenKohdeDialogit()[j] + ";\n";
                        }
                        vuoropuheDialogitMerkkijonoina[i] += "    }\n";
                    }
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Ei voitu tallentaa dialogeja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa dialogeja", JOptionPane.ERROR_MESSAGE);
                    vuoropuheDialogitMerkkijonoina[i] += "\n    }\n";
                }
                kokoTiedostoMerkkijonona += vuoropuheDialogitMerkkijonoina[i];
                kokoTiedostoMerkkijonona += "/Dialogi" + "\n";
            }
        }

        kokoTiedostoMerkkijonona += "\n</KEIMO>";
        return kokoTiedostoMerkkijonona;
    }

    public static String luoMerkkijonoHuoneesta(HashMap<Integer, Huone> huoneKartta, int id) {
        String kokoTiedostoMerkkijonona = "";
        kokoTiedostoMerkkijonona += "<KEIMO>\n\n";
        String huoneetMerkkijonoina = "";
        huoneetMerkkijonoina = "";
        huoneetMerkkijonoina += "Huone " + huoneKartta.get(id).annaId() + ":" + "\n    ";
        huoneetMerkkijonoina += "#koko: " + huoneKartta.get(id).annaKoko() + ";" + "\n    ";
        huoneetMerkkijonoina += "#nimi: " + huoneKartta.get(id).annaNimi() + ";" + "\n    ";
        huoneetMerkkijonoina += "#alue: " + huoneKartta.get(id).annaAlue() + ";" + "\n    ";
        huoneetMerkkijonoina += "#tausta: " + huoneKartta.get(id).annaTaustanPolku() + ";" + "\n    ";
        huoneetMerkkijonoina += "#musa: " + huoneKartta.get(id).annaHuoneenMusa() + ";" + "\n    ";

        if (huoneKartta.get(id).annaReunaWarppiTiedot(Suunta.VASEN)) {
            huoneetMerkkijonoina += "#warp_vasen: " + huoneKartta.get(id).annaReunaWarpinKohdeId(Suunta.VASEN) + ";" + "\n    ";
        }
        else {
            huoneetMerkkijonoina += "#warp_vasen: " + ";" + "\n    ";
        }
        if (huoneKartta.get(id).annaReunaWarppiTiedot(Suunta.OIKEA)) {
            huoneetMerkkijonoina += "#warp_oikea: " + huoneKartta.get(id).annaReunaWarpinKohdeId(Suunta.OIKEA) + ";" + "\n    ";
        }
        else {
            huoneetMerkkijonoina += "#warp_oikea: " + ";" + "\n    ";
        }
        if (huoneKartta.get(id).annaReunaWarppiTiedot(Suunta.ALAS)) {
            huoneetMerkkijonoina += "#warp_alas: " + huoneKartta.get(id).annaReunaWarpinKohdeId(Suunta.ALAS) + ";" + "\n    ";
        }
        else {
            huoneetMerkkijonoina += "#warp_alas: " + ";" + "\n    ";
        }
        if (huoneKartta.get(id).annaReunaWarppiTiedot(Suunta.YLÖS)) {
            huoneetMerkkijonoina += "#warp_ylös: " + huoneKartta.get(id).annaReunaWarpinKohdeId(Suunta.YLÖS) + ";" + "\n    ";
        }
        else {
            huoneetMerkkijonoina += "#warp_ylös: " + ";" + "\n    ";
        }

        if (huoneKartta.get(id).annaTarinaRuudunLataus()) {
            huoneetMerkkijonoina += "#tarina: " + huoneKartta.get(id).annaTarinaRuudunTunniste() + ";" + "\n    ";
        }
        else {
            huoneetMerkkijonoina += "#tarina: " + ";" + "\n    ";
        }
        if (huoneKartta.get(id).annaTavoiteVaatimus()) {
            huoneetMerkkijonoina += "#tavoite: " + huoneKartta.get(id).annaVaaditunTavoitteenTunniste() + ";" + "\n    ";
        }
        else {
            huoneetMerkkijonoina += "#tavoite: " + ";" + "\n    ";
        }

        try {
            huoneetMerkkijonoina += "#kenttä: " + "{\n";
            for (KenttäKohde[] kk : huoneKartta.get(id).annaHuoneenKenttäSisältö()) {
                for (KenttäKohde k : kk) {
                    if (k != null) {
                        if (k.onkoMääritettySijainti()) {
                            huoneetMerkkijonoina += "        " + k.annaNimi() + "_" + k.annaSijX() + "_" + k.annaSijY();
                        }
                        else {
                            huoneetMerkkijonoina += "        " + k.annaNimi();
                        }
                        if (k.onkolisäOminaisuuksia()) {
                            huoneetMerkkijonoina += "+ominaisuudet:[";
                            for (String s : k.annalisäOminaisuudet()) {
                                huoneetMerkkijonoina += s + ",";
                            }
                            huoneetMerkkijonoina = huoneetMerkkijonoina.substring(0, huoneetMerkkijonoina.length()-1);
                            huoneetMerkkijonoina += "]";
                        }
                        huoneetMerkkijonoina += ",\n";
                    }
                }
            }
            if (huoneetMerkkijonoina.charAt(huoneetMerkkijonoina.length()-2 ) != '{' && huoneetMerkkijonoina.charAt(huoneetMerkkijonoina.length()-1 ) != '{') {
                huoneetMerkkijonoina = huoneetMerkkijonoina.substring(0, huoneetMerkkijonoina.length()-2);
                huoneetMerkkijonoina +=";\n";
            }
            huoneetMerkkijonoina += "    }\n    ";
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Ei voitu tallentaa objekteja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa objekteja", JOptionPane.ERROR_MESSAGE);
            huoneetMerkkijonoina += "\n    }\n";
        }

        try {
            huoneetMerkkijonoina += "#maasto: " + "{\n";
            for (Maasto[] mm : huoneKartta.get(id).annaHuoneenMaastoSisältö()) {
                for (Maasto m : mm) {
                    if (m != null) {
                        if (m.onkoMääritettySijainti()) {
                            huoneetMerkkijonoina += "        " + m.annaNimi() + "_" + m.annaSijX() + "_" + m.annaSijY();
                        }
                        else {
                            huoneetMerkkijonoina += "        " + m.annaNimi();
                        }
                        if (m.onkolisäOminaisuuksia()) {
                            huoneetMerkkijonoina += "+ominaisuudet:[";
                            for (String s : m.annaLisäOminaisuudet()) {
                                huoneetMerkkijonoina += s + ",";
                            }
                            huoneetMerkkijonoina = huoneetMerkkijonoina.substring(0, huoneetMerkkijonoina.length()-1);
                            huoneetMerkkijonoina += "]";
                        }
                        huoneetMerkkijonoina += ",\n";
                    }
                }
            }
            if (huoneetMerkkijonoina.charAt(huoneetMerkkijonoina.length()-2 ) != '{' && huoneetMerkkijonoina.charAt(huoneetMerkkijonoina.length()-1 ) != '{') {
                huoneetMerkkijonoina = huoneetMerkkijonoina.substring(0, huoneetMerkkijonoina.length()-2);
                huoneetMerkkijonoina +=";\n";
            }
            huoneetMerkkijonoina += "    }\n    ";
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Ei voitu tallentaa maastoa.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa maastoa", JOptionPane.ERROR_MESSAGE);
            huoneetMerkkijonoina += "\n    }\n";
        }

        try {
            huoneetMerkkijonoina += "#npc: " + "{\n";
            for (Entity[] nn : huoneKartta.get(id).annaHuoneenNPCSisältö()) {
                for (Entity n : nn) {
                    if (n != null) {
                        if (n.onkoMääritettySijainti()) {
                            huoneetMerkkijonoina += "        " + n.annaNimi() + "_" + n.annaSijX() + "_" + n.annaSijY();
                        }
                        else {
                            huoneetMerkkijonoina += "        " + n.annaNimi();
                        }
                        if (n.onkolisäOminaisuuksia()) {
                            huoneetMerkkijonoina += "+ominaisuudet:[";
                            for (String s : n.annalisäOminaisuudet()) {
                                huoneetMerkkijonoina += s + ",";
                            }
                            huoneetMerkkijonoina = huoneetMerkkijonoina.substring(0, huoneetMerkkijonoina.length()-1);
                            huoneetMerkkijonoina += "]";
                        }
                        huoneetMerkkijonoina += ",\n";
                    }
                }
            }
            if (huoneetMerkkijonoina.charAt(huoneetMerkkijonoina.length()-2 ) != '{' && huoneetMerkkijonoina.charAt(huoneetMerkkijonoina.length()-1 ) != '{') {
                huoneetMerkkijonoina = huoneetMerkkijonoina.substring(0, huoneetMerkkijonoina.length()-2);
                huoneetMerkkijonoina +=";\n";
            }

            huoneetMerkkijonoina += "    }\n";
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Ei voitu tallentaa npc:itä.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "Virhe tallentaessa NPC:itä", JOptionPane.ERROR_MESSAGE);
            huoneetMerkkijonoina += "\n    }\n";
        }

        kokoTiedostoMerkkijonona += huoneetMerkkijonoina;
        kokoTiedostoMerkkijonona += "/Huone" + "\n";

        kokoTiedostoMerkkijonona += "\n</KEIMO>";
        return kokoTiedostoMerkkijonona;
    }
}
