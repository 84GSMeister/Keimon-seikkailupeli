package keimo.utility;

import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;
import keimo.seikkailupeli.objektit.entityt.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.*;
import keimo.seikkailupeli.objektit.maastot.*;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class KSTLoader {

    public static void lataaAsetuksetKST(String tiedostoPolku) {
        try {
            File tiedosto = new File(tiedostoPolku);
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
                else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                    break;
                }
                else {
                    tarkastettavaRivi = read.readLine();
                }
            }
            read.close();
            Peli.annaHuoneKartta().clear();
            lataaAsetuksetMerkkijonosta(asetuksetMerkkijonona);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, Huone> lataaKentätKST(String tiedostoPolku) {
        try {
            File tiedosto = new File(tiedostoPolku);
            String[] huoneetMerkkijonoina;
            int huoneidenMääräTiedostossa = 0;
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
            }
            huoneetMerkkijonoina = new String[huoneidenMääräTiedostossa];
            huoneidenMääräTiedostossa = 0;
            read.close();
            read = Files.newBufferedReader(path, charset);
            tarkastettavaRivi = read.readLine();
            while ((tarkastettavaRivi != null)) {
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
                else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                    break;
                }
                else {
                    tarkastettavaRivi = read.readLine();
                }
            }
            read.close();
            return KSTLoader.luoHuoneKarttaMerkkijonosta(huoneetMerkkijonoina);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, TarinaPätkä> lataaTarinatKST(String tiedostoPolku) {
        try {
            File tiedosto = new File(tiedostoPolku);
            String[] tarinaDialogitMerkkijonoina;
            int tarinaDialogienMääräTiedostossa = 0;
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
                if (tarkastettavaRivi.startsWith("Tarina ")) {
                    tarinaDialogienMääräTiedostossa++;
                }
            }
            tarinaDialogitMerkkijonoina = new String[tarinaDialogienMääräTiedostossa];
            tarinaDialogienMääräTiedostossa = 0;
            read.close();
            read = Files.newBufferedReader(path, charset);
            tarkastettavaRivi = read.readLine();
            while ((tarkastettavaRivi != null)) {
                if (tarkastettavaRivi.startsWith("Tarina ")) {
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
                else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                    break;
                }
                else {
                    tarkastettavaRivi = read.readLine();
                }
            }
            read.close();
            return KSTLoader.luoTarinaKarttaMerkkijonosta(tarinaDialogitMerkkijonoina);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, VuoropuheDialogiPätkä> lataaDialogitKST(String tiedostoPolku) {
        try {
            File tiedosto = new File(tiedostoPolku);
            String[] vuoropuheDialogitMerkkijonoina;
            int vuoropuheDialogienMääräTiedostossa = 0;
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
                if (tarkastettavaRivi.startsWith("Dialogi ")) {
                    vuoropuheDialogienMääräTiedostossa++;
                }
            }
            vuoropuheDialogitMerkkijonoina = new String[vuoropuheDialogienMääräTiedostossa];
            vuoropuheDialogienMääräTiedostossa = 0;
            read.close();
            read = Files.newBufferedReader(path, charset);
            tarkastettavaRivi = read.readLine();
            while ((tarkastettavaRivi != null)) {
                if (tarkastettavaRivi.startsWith("Dialogi ")) {
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
            return KSTLoader.luoDialogiKarttaMerkkijonosta(vuoropuheDialogitMerkkijonoina);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void lataaAsetuksetMerkkijonosta(String asetusMerkkijono) {
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
    
    public static HashMap<Integer, Huone> luoHuoneKarttaMerkkijonosta(String[] huoneMerkkijonot) {
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
        ArrayList<String> luotavanObjektinOminaisuusLista = new ArrayList<>();
        ArrayList<KenttäKohde> uusiObjektiLista = new ArrayList<>();

        String luotavaMaasto = "";
        int luotavanMaastonX = 0;
        int luotavanMaastonY = 0;
        ArrayList<String> luotavanMaastonOminaisuusLista = new ArrayList<>();
        ArrayList<Maasto> uusiMaastoLista = new ArrayList<>();

        String luotavaNPC = "";
        int luotavanNPCnX = 0;
        int luotavanNPCnY = 0;
        ArrayList<String> luotavanNPCnOminaisuusLista = new ArrayList<>();
        ArrayList<Entity> uusiNPCLista = new ArrayList<>();

        boolean uusiWarpVasen = false;
        int uusiWarpVasenHuoneId = 0;
        boolean uusiWarpOikea = false;
        int uusiWarpOikeaHuoneId = 0;
        boolean uusiWarpAlas = false;
        int uusiWarpAlasHuoneId = 0;
        boolean uusiWarpYlös = false;
        int uusiWarpYlösHuoneId = 0;

        int rivejäTarkastettu = 7;
        String idTarkistus = "";
        try {
            KenttäKohde.nollaaObjektiId();
            for (String s : huoneMerkkijonot) {
                Scanner sc = new Scanner(s);
                try {
                    while (sc.hasNextLine()) {
                        String tarkastettavaRivi = sc.nextLine();
                        rivejäTarkastettu++;
                        if (tarkastettavaRivi.startsWith("Huone")) {
                            idTarkistus = tarkastettavaRivi.substring(6, tarkastettavaRivi.length() -1);
                            uusiHuoneenId = Integer.parseInt(idTarkistus);
                        }
                        else if (tarkastettavaRivi.contains("#koko:")) {
                            try {
                                uusiHuoneenKoko = Integer.parseInt(tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1));
                            }
                            catch (NumberFormatException nfe) {
                                DialogiIkkunat.viestiIkkuna("Virheellinen koko", "Virhe parsiessa kst-tiedostoa!\n\nVirheellinen koko huoneessa " + uusiHuoneenId + " (rivi " + rivejäTarkastettu +")\n" + "Asetetaan huoneen kooksi 10.", "ok", "error", false);
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
                                        if (luotavanObjektinOminaisuusLista.size() > 0) luotavanObjektinOminaisuusLista.clear();
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
                                            if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                                String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                                for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                                    if (tarkastettavaRivi.charAt(i) == ']') {
                                                        break;
                                                    }
                                                }
                                                String[] ominaisuudetArray = ominaisuudetMerkkijonona.split(",");
                                                for (String ominaisuus : ominaisuudetArray) luotavanObjektinOminaisuusLista.add(ominaisuus);
                                                uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                            }
                                            else {
                                                uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, luotavanObjektinX, luotavanObjektinY, false, null));
                                            }
                                        }
                                        else {
                                            System.out.println("virheellinen objekti: " + tarkastettavaRivi);
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
                                        if (luotavanMaastonOminaisuusLista.size() > 0) luotavanMaastonOminaisuusLista.clear();
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
                                            if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                                String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                                for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                                    if (tarkastettavaRivi.charAt(i) == ']') {
                                                        break;
                                                    }
                                                }
                                                String[] ominaisuudetArray = ominaisuudetMerkkijonona.split(",");
                                                for (String ominaisuus : ominaisuudetArray) luotavanMaastonOminaisuusLista.add(ominaisuus);
                                                uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                            }
                                            else {
                                                uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, luotavanMaastonX, luotavanMaastonY, false, null));
                                            }
                                        }
                                        else {
                                            System.out.println("virheellinen tile: " + tarkastettavaRivi);
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
                                        if (luotavanNPCnOminaisuusLista.size() > 0) luotavanNPCnOminaisuusLista.clear();
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
                                            if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                                String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                                for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                                    if (tarkastettavaRivi.charAt(i) == ']') {
                                                        break;
                                                    }
                                                }
                                                String[] ominaisuudetArray = ominaisuudetMerkkijonona.split(",");
                                                for (String ominaisuus : ominaisuudetArray) luotavanNPCnOminaisuusLista.add(ominaisuus);
                                                uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                            }
                                            else {
                                                uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, luotavanNPCnX, luotavanNPCnY, false, null));
                                            }
                                        }
                                        else {
                                            System.out.println("virheellinen entity: " + tarkastettavaRivi);
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
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    DialogiIkkunat.viestiIkkuna("Virheellinen huoneen ID", "Virhe parsiessa kst-tiedostoa!\n\nVirheellinen huoneen ID: " + idTarkistus + " (rivi " + rivejäTarkastettu +")\nVain positiiviset kokonaisluvut kelpaavat huoneen ID:ksi.\nOhitetaan huoneen luonti.", "ok", "error", false);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
            DialogiIkkunat.viestiIkkuna("Virhe ladatessa tiedostoa.", "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla tai tiedosto on yhteensopimaton nykyisen pelin/editorin version kanssa.\n\n" + virheViesti, "ok", "error", false);
        }
        return uusiHuoneKartta;
    }

    public static Huone luoHuoneMerkkijonosta(String merkkijono, int id) {
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
        ArrayList<String> luotavanObjektinOminaisuusLista = new ArrayList<>();
        ArrayList<KenttäKohde> uusiObjektiLista = new ArrayList<>();

        String luotavaMaasto = "";
        int luotavanMaastonX = 0;
        int luotavanMaastonY = 0;
        ArrayList<String> luotavanMaastonOminaisuusLista = new ArrayList<>();
        ArrayList<Maasto> uusiMaastoLista = new ArrayList<>();

        String luotavaNPC = "";
        int luotavanNPCnX = 0;
        int luotavanNPCnY = 0;
        ArrayList<String> luotavanNPCnOminaisuusLista = new ArrayList<>();
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
                                    if (luotavanObjektinOminaisuusLista.size() > 0) luotavanObjektinOminaisuusLista.clear();
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
                                        if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                            String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                            for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                                if (tarkastettavaRivi.charAt(i) == ']') {
                                                    break;
                                                }
                                            }
                                            String[] ominaisuudetArray = ominaisuudetMerkkijonona.split(",");
                                            for (String ominaisuus : ominaisuudetArray) luotavanObjektinOminaisuusLista.add(ominaisuus);
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, luotavanObjektinX, luotavanObjektinY, true, luotavanObjektinOminaisuusLista));
                                        }
                                        else {
                                            uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, luotavanObjektinX, luotavanObjektinY, false, null));
                                        }
                                    }
                                    else {
                                        System.out.println("virheellinen objekti: " + tarkastettavaRivi);
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
                                    if (luotavanMaastonOminaisuusLista.size() > 0) luotavanMaastonOminaisuusLista.clear();
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
                                        if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                            String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                            for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                                if (tarkastettavaRivi.charAt(i) == ']') {
                                                    break;
                                                }
                                            }
                                            String[] ominaisuudetArray = ominaisuudetMerkkijonona.split(",");
                                            for (String ominaisuus : ominaisuudetArray) luotavanMaastonOminaisuusLista.add(ominaisuus);
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, luotavanMaastonX, luotavanMaastonY, true, luotavanMaastonOminaisuusLista));
                                        }
                                        else {
                                            uusiMaastoLista.add(luoMaastoTiedoilla(luotavaMaasto, luotavanMaastonX, luotavanMaastonY, false, null));
                                        }
                                    }
                                    else {
                                        System.out.println("virheellinen tile: " + tarkastettavaRivi);
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
                                    if (luotavanNPCnOminaisuusLista.size() > 0) luotavanNPCnOminaisuusLista.clear();
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
                                        if (tarkastettavaRivi.contains("+ominaisuudet:")) {
                                            String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
                                            for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
                                                if (tarkastettavaRivi.charAt(i) == ']') {
                                                    break;
                                                }
                                            }
                                            String[] ominaisuudetArray = ominaisuudetMerkkijonona.split(",");
                                            for (String ominaisuus : ominaisuudetArray) luotavanNPCnOminaisuusLista.add(ominaisuus);
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
                                        }
                                        else {
                                            uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, luotavanNPCnX, luotavanNPCnY, false, null));
                                        }
                                    }
                                    else {
                                        System.out.println("virheellinen entity: " + tarkastettavaRivi);
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
            DialogiIkkunat.viestiIkkuna("Virhe ladatessa tiedostoa.", "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla tai tiedosto on yhteensopimaton nykyisen pelin/editorin version kanssa.\n\n" + virheViesti, "ok", "error", false);
            return null;
        }
    }

    public static HashMap<String, TarinaPätkä> luoTarinaKarttaMerkkijonosta(String[] tarinaMerkkijonot) {
        
        HashMap<String, TarinaPätkä> uusiTarinaKartta = new HashMap<>();
        ArrayList<Integer> käytetytIdt = new ArrayList<>();

        int uusiTarinanId = 0;
        String uusiTarinanNimi = "";
        int uusiTarinanPituus = 0;
        String[] uudetTarinaPätkänKuvatiedostot = new String[uusiTarinanPituus];
        String[] uudetTarinaPätkänTekstit = new String[uusiTarinanPituus];

        try {
            for (String s : tarinaMerkkijonot) {
                Scanner sc = new Scanner(s);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = "";
                    tarkastettavaRivi = sc.nextLine();
                    if (tarkastettavaRivi.startsWith("Tarina")) {
                        uusiTarinanId = Integer.parseInt(tarkastettavaRivi.substring(7, tarkastettavaRivi.length() -1));
                        while (käytetytIdt.contains(uusiTarinanId)) uusiTarinanId++;
                        käytetytIdt.add(uusiTarinanId);
                    }
                    else if (tarkastettavaRivi.contains("#nimi:")) {
                        uusiTarinanNimi = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#pituus:")) {
                        uusiTarinanPituus = Integer.parseInt(tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("sivut:")) {
                        uudetTarinaPätkänKuvatiedostot = new String[uusiTarinanPituus];
                        uudetTarinaPätkänTekstit = new String[uusiTarinanPituus];
                        for (int i = 0; i < uusiTarinanPituus*2; i++) {
                            if (sc.hasNextLine()) {
                                tarkastettavaRivi = sc.nextLine();
                                if (tarkastettavaRivi.contains("kuva ")) {
                                    int alkuIndeksi = tarkastettavaRivi.indexOf("kuva ") +5;
                                    int loppuIndeksi = tarkastettavaRivi.indexOf(":");
                                    int kuvanNumero = Integer.parseInt(tarkastettavaRivi.substring(alkuIndeksi, loppuIndeksi));
                                    uudetTarinaPätkänKuvatiedostot[kuvanNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf(":") +2, tarkastettavaRivi.length() -1);
                                }
                                else if (tarkastettavaRivi.contains("teksti ")) {
                                    int alkuIndeksi = tarkastettavaRivi.indexOf("teksti ") +7;
                                    int loppuIndeksi = tarkastettavaRivi.indexOf(":");
                                    int tekstinNumero = Integer.parseInt(tarkastettavaRivi.substring(alkuIndeksi, loppuIndeksi));
                                    uudetTarinaPätkänTekstit[tekstinNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf(":") +2, tarkastettavaRivi.length() -1);
                                }
                            }
                        }
                    }
                }
                uusiTarinaKartta.put(uusiTarinanNimi, new TarinaPätkä(uusiTarinanId, uusiTarinanNimi, uusiTarinanPituus, uudetTarinaPätkänKuvatiedostot, uudetTarinaPätkänTekstit));
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

    public static HashMap<String, VuoropuheDialogiPätkä> luoDialogiKarttaMerkkijonosta(String[] dialogiMerkkijonot) {
        
        HashMap<String, VuoropuheDialogiPätkä> uusiDialogiKartta = new HashMap<>();
        ArrayList<Integer> käytetytIdt = new ArrayList<>();

        int uusiDialoginId = 0;
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
            for (String s : dialogiMerkkijonot) {
                Scanner sc = new Scanner(s);
                valinta = false;
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = "";
                    tarkastettavaRivi = sc.nextLine();
                    if (tarkastettavaRivi.startsWith("Dialogi")) {
                        uusiDialoginId = Integer.parseInt(tarkastettavaRivi.substring(8, tarkastettavaRivi.length() -1));
                        while (käytetytIdt.contains(uusiDialoginId)) uusiDialoginId++;
                        käytetytIdt.add(uusiDialoginId);
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
                            if (sc.hasNextLine()) {
                                tarkastettavaRivi = sc.nextLine();
                                if (tarkastettavaRivi.contains("kuva ")) {
                                    int alkuIndeksi = tarkastettavaRivi.indexOf("kuva ") +5;
                                    int loppuIndeksi = tarkastettavaRivi.indexOf(":");
                                    int kuvanNumero = Integer.parseInt(tarkastettavaRivi.substring(alkuIndeksi, loppuIndeksi));
                                    uudetDialogiPätkänKuvatiedostot[kuvanNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf(":") +2, tarkastettavaRivi.length() -1);
                                }
                                else if (tarkastettavaRivi.contains("teksti ")) {
                                    int alkuIndeksi = tarkastettavaRivi.indexOf("teksti ") +7;
                                    int loppuIndeksi = tarkastettavaRivi.indexOf(":");
                                    int tekstinNumero = Integer.parseInt(tarkastettavaRivi.substring(alkuIndeksi, loppuIndeksi));
                                    uudetDialogiPätkänTekstit[tekstinNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf(":") +2, tarkastettavaRivi.length() -1);
                                }
                                else if (tarkastettavaRivi.contains("puhuja ")) {
                                    int alkuIndeksi = tarkastettavaRivi.indexOf("puhuja ") +7;
                                    int loppuIndeksi = tarkastettavaRivi.indexOf(":");
                                    int puhujanNumero = Integer.parseInt(tarkastettavaRivi.substring(alkuIndeksi, loppuIndeksi));
                                    uudetDialogiPätkänPuhujat[puhujanNumero] = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf(":") +2, tarkastettavaRivi.length() -1);
                                }
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
                if (valinta) uusiDialogiKartta.put(uusiDialoginNimi, new VuoropuheDialogiPätkä(uusiDialoginId, uusiDialoginNimi, uusiDialoginPituus, uudetDialogiPätkänKuvatiedostot, uudetDialogiPätkänTekstit, uudetDialogiPätkänPuhujat, true, valinnanNimi, valinnanOtsikko, valinnanVaihtoehdot, vaihtoehtojenKohdedialogit, valinnanTriggerit));
                else uusiDialogiKartta.put(uusiDialoginNimi, new VuoropuheDialogiPätkä(uusiDialoginId, uusiDialoginNimi, uusiDialoginPituus, uudetDialogiPätkänKuvatiedostot, uudetDialogiPätkänTekstit, uudetDialogiPätkänPuhujat, false, null, null, null, null, null));
                sc.close();
            }
        }
        catch (NumberFormatException nfe) {
            System.out.println("Virheellinen indeksi");
            nfe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Virhe ladatessa dialogeja tiedostosta");
            e.printStackTrace();
        }
        return uusiDialogiKartta;
    }

    static KenttäKohde luoObjektiTiedoilla(String objektinNimi, int sijX, int sijY, boolean lisäOminaisuudet, ArrayList<String> ominaisuusLista) {
        return KenttäKohde.luoObjektiTiedoilla(objektinNimi, sijX, sijY, ominaisuusLista);
    }

    static Maasto luoMaastoTiedoilla(String maastonNimi, int sijX, int sijY, boolean lisäOminaisuudet, ArrayList<String> ominaisuusLista) {
        return Maasto.luoMaastoTiedoilla(maastonNimi, sijX, sijY, ominaisuusLista);
    }

    static Entity luoNPCTiedoilla(String npcnNimi, int sijX, int sijY, boolean lisäOminaisuudet, ArrayList<String> ominaisuusLista) {
        return Entity.luoEntityTiedoilla(npcnNimi, sijX, sijY, ominaisuusLista);
    }

    public static String luoMerkkijonotHuonekartasta(HashMap<Integer, Huone> huoneKartta, HashMap<String, TarinaPätkä> tarinaKartta, HashMap<String, VuoropuheDialogiPätkä> dialogiKartta) {
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
                            huoneetMerkkijonoina[i] += "        " + k.annaNimi() + "_" + k.annaSijX() + "_" + k.annaSijY();
                            //if (k.onkoLisäOminaisuuksia()) {
                            if (k.annaLisäOminaisuudet().size() > 0) {
                                huoneetMerkkijonoina[i] += "+ominaisuudet:[";
                                for (String s : k.annaLisäOminaisuudet()) {
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
                DialogiIkkunat.viestiIkkuna("Virhe tallentaessa objekteja", "Ei voitu tallentaa objekteja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
                huoneetMerkkijonoina[i] += "\n    }\n";
            }

            try {
                huoneetMerkkijonoina[i] += "#maasto: " + "{\n";
                for (Maasto[] mm : huoneKartta.get(i).annaHuoneenMaastoSisältö()) {
                    for (Maasto m : mm) {
                        if (m != null) {
                            huoneetMerkkijonoina[i] += "        " + m.annaNimi() + "_" + m.annaSijX() + "_" + m.annaSijY();
                            //if (m.onkoLisäOminaisuuksia()) {
                            if (m.annaLisäOminaisuudet().size() > 0) {
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
                DialogiIkkunat.viestiIkkuna("Virhe tallentaessa maastoa", "Ei voitu tallentaa maastoa.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
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
                            //if (n.onkoLisäOminaisuuksia()) {
                            if (n.annaLisäOminaisuudet().size() > 0) {
                                huoneetMerkkijonoina[i] += "+ominaisuudet:[";
                                for (String s : n.annaLisäOminaisuudet()) {
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
                DialogiIkkunat.viestiIkkuna("Virhe tallentaessa entityjä", "Ei voitu tallentaa entityjä.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
                huoneetMerkkijonoina[i] += "\n    }\n";
            }

            kokoTiedostoMerkkijonona += huoneetMerkkijonoina[i];
            kokoTiedostoMerkkijonona += "/Huone" + "\n";
        }
        if (tarinaKartta != null) {
            String[] tarinaDialogitMerkkijonoina = new String[Peli.peliTiedosto.annaTarinaKartta().size()];
            Object[] tarinanTunnisteet = tarinaKartta.keySet().toArray();
            for (int i = 0; i < Peli.peliTiedosto.annaTarinaKartta().size(); i++) {
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
                    DialogiIkkunat.viestiIkkuna("Virhe tallentaessa tarinaa", "Ei voitu tallentaa tarinan sivuja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
                    tarinaDialogitMerkkijonoina[i] += "\n    }\n";
                }
                kokoTiedostoMerkkijonona += tarinaDialogitMerkkijonoina[i];
                kokoTiedostoMerkkijonona += "/Tarina" + "\n";
            }
        }
        if (dialogiKartta != null) {
            String[] vuoropuheDialogitMerkkijonoina = new String[Peli.peliTiedosto.annaDialogiKartta().size()];
            Object[] dialoginTunnisteet = dialogiKartta.keySet().toArray();
            for (int i = 0; i < Peli.peliTiedosto.annaDialogiKartta().size(); i++) {
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
                        for (int j = 0; j < vdp.annaValinnanVaihtoehdot().length; j++) {
                            vuoropuheDialogitMerkkijonoina[i] += "        vaihtoehto " + j + ": " + vdp.annaValinnanVaihtoehdot()[j] + ";\n";
                            vuoropuheDialogitMerkkijonoina[i] += "        triggeri " + j + ": " + vdp.annaTriggerit()[j] + ";\n";
                            vuoropuheDialogitMerkkijonoina[i] += "        kohde " + j + ": " + vdp.annaValinnanVaihtoehtojenKohdeDialogit()[j] + ";\n";
                        }
                        vuoropuheDialogitMerkkijonoina[i] += "    }\n";
                    }
                }
                catch (NullPointerException e) {
                    DialogiIkkunat.viestiIkkuna("Virhe tallentaessa dialogeja", "Ei voitu tallentaa dialogeja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
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
                        huoneetMerkkijonoina += "        " + k.annaNimi() + "_" + k.annaSijX() + "_" + k.annaSijY();
                        //if (k.onkoLisäOminaisuuksia()) {
                        if (k.annaLisäOminaisuudet().size() > 0) {
                            huoneetMerkkijonoina += "+ominaisuudet:[";
                            for (String s : k.annaLisäOminaisuudet()) {
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
            DialogiIkkunat.viestiIkkuna("Virhe tallentaessa objekteja", "Ei voitu tallentaa objekteja.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
            huoneetMerkkijonoina += "\n    }\n";
        }

        try {
            huoneetMerkkijonoina += "#maasto: " + "{\n";
            for (Maasto[] mm : huoneKartta.get(id).annaHuoneenMaastoSisältö()) {
                for (Maasto m : mm) {
                    if (m != null) {
                        huoneetMerkkijonoina += "        " + m.annaNimi() + "_" + m.annaSijX() + "_" + m.annaSijY();
                        //if (m.onkoLisäOminaisuuksia()) {
                        if (m.annaLisäOminaisuudet().size() > 0) {
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
            DialogiIkkunat.viestiIkkuna("Virhe tallentaessa maastoa", "Ei voitu tallentaa maastoa.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
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
                        //if (n.onkoLisäOminaisuuksia()) {
                        if (n.annaLisäOminaisuudet().size() > 0) {
                            huoneetMerkkijonoina += "+ominaisuudet:[";
                            for (String s : n.annaLisäOminaisuudet()) {
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
            DialogiIkkunat.viestiIkkuna("Virhe tallentaessa entityjä", "Ei voitu tallentaa entityjä.\n\nNull pointer Exception\n\nTämä voi tapahtua, jos olet ladannut vanhentuneen kst-tiedoston editoriin.", "ok", "error", false);
            huoneetMerkkijonoina += "\n    }\n";
        }

        kokoTiedostoMerkkijonona += huoneetMerkkijonoina;
        kokoTiedostoMerkkijonona += "/Huone" + "\n";

        kokoTiedostoMerkkijonona += "\n</KEIMO>";
        return kokoTiedostoMerkkijonona;
    }
}
