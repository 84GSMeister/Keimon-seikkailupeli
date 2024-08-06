package keimo.HuoneEditori.Keimo3D;

import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.HuoneEditorinMetodit;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Kenttä {

    protected static void lataaGrafiikatKSTstä() {
        String luotavaObjekti = "";
        int luotavanObjektinX = 0;
        int luotavanObjektinY = 0;
        String[] luotavanObjektinOminaisuusLista = {""};

        String luotavaMaasto = "";
        int luotavanMaastonX = 0;
        int luotavanMaastonY = 0;
        String[] luotavanMaastonOminaisuusLista = {""};

        int rivejäTarkastettu = 0;
        try {
            File tiedosto = new File("tiedostot/pelitiedostot/huoneet/väliaikainen.ksh");
            Path polku = FileSystems.getDefault().getPath(tiedosto.getPath());
            Charset charset = Charset.forName("UTF-8");
            BufferedReader read = Files.newBufferedReader(polku, charset);
            String tarkastettavaRivi = null;
            while ((tarkastettavaRivi = read.readLine()) != null) {
                rivejäTarkastettu++;
                if (tarkastettavaRivi.startsWith("Huone")) {
                    //uusiHuoneenId = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length() -1));
                }
                // else if (tarkastettavaRivi.contains("#koko:")) {
                //     try {
                //         uusiHuoneenKoko = Integer.parseInt(tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1));
                //     }
                //     catch (NumberFormatException nfe) {
                //         System.out.println("Virheellinen koko. Asetetaan huoneen " + uusiHuoneenId + " kooksi 10.");
                //         uusiHuoneenKoko = 10;
                //     }
                // }
                // else if (tarkastettavaRivi.contains("#nimi:")) {
                //     uusiHuoneenNimi = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                // }
                // else if (tarkastettavaRivi.contains("#alue:")) {
                //     uusiHuoneenAlue = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                // }
                // else if (tarkastettavaRivi.contains("#tausta:")) {
                //     uusiHuoneenTaustanPolku = tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1);
                // }

                // else if (tarkastettavaRivi.contains("#warp_vasen:")) {
                //     try {
                //         uusiWarpVasenHuoneId = Integer.parseInt(tarkastettavaRivi.substring(17, tarkastettavaRivi.length() -1));
                //         uusiWarpVasen = true;
                //     }
                //     catch (NumberFormatException e) {
                //         uusiWarpVasen = false;
                //     }
                //     catch (StringIndexOutOfBoundsException e) {
                //         uusiWarpVasen = false;
                //     }
                // }
                // else if (tarkastettavaRivi.contains("#warp_oikea:")) {
                //     try {
                //         uusiWarpOikeaHuoneId = Integer.parseInt(tarkastettavaRivi.substring(17, tarkastettavaRivi.length() -1));
                //         uusiWarpOikea = true;
                //     }
                //     catch (NumberFormatException e) {
                //         uusiWarpOikea = false;
                //     }
                //     catch (StringIndexOutOfBoundsException e) {
                //         uusiWarpOikea = false;
                //     }
                // }
                // else if (tarkastettavaRivi.contains("#warp_alas:")) {
                //     try {
                //         uusiWarpAlasHuoneId = Integer.parseInt(tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1));
                //         uusiWarpAlas = true;
                //     }
                //     catch (NumberFormatException e) {
                //         uusiWarpAlas = false;
                //     }
                //     catch (StringIndexOutOfBoundsException e) {
                //         uusiWarpAlas = false;
                //     }
                // }
                // else if (tarkastettavaRivi.contains("#warp_ylös:")) {
                //     try {
                //         uusiWarpYlösHuoneId = Integer.parseInt(tarkastettavaRivi.substring(16, tarkastettavaRivi.length() -1));
                //         uusiWarpYlös = true;
                //     }
                //     catch (NumberFormatException e) {
                //         uusiWarpYlös = false;
                //     }
                //     catch (StringIndexOutOfBoundsException e) {
                //         uusiWarpYlös = false;
                //     }
                // }
                // else if (tarkastettavaRivi.contains("#tarina:")) {
                //     try {
                //         uusiHuoneenTarinanTunniste = tarkastettavaRivi.substring(13, tarkastettavaRivi.length() -1);
                //         if (uusiHuoneenTarinanTunniste == null || uusiHuoneenTarinanTunniste == "") {
                //             uusiHuoneenTarinanTunniste = null;
                //         }
                //     }
                //     catch (IndexOutOfBoundsException e) {
                //         uusiHuoneenTarinanTunniste = null;
                //     }
                // }
                // else if (tarkastettavaRivi.contains("#tavoite:")) {
                //     try {
                //         uusiHuoneenVaadittuTavoite = tarkastettavaRivi.substring(14, tarkastettavaRivi.length() -1);
                //         if (uusiHuoneenVaadittuTavoite == null || uusiHuoneenVaadittuTavoite == "") {
                //             uusiHuoneenVaadittuTavoite = null;
                //         }
                //     }
                //     catch (IndexOutOfBoundsException e) {
                //         uusiHuoneenVaadittuTavoite = null;
                //     }
                // }

                System.out.println(tarkastettavaRivi);
                if (tarkastettavaRivi.contains("#kenttä:")) {
                    if (tarkastettavaRivi.contains("{")) {
                        tarkastettavaRivi = read.readLine();
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
                                        //Keimo3D.kenttäObjektit.add(new KenttäObjektiVanha(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
                                        Objektit3D.kenttäObjektit.add(new KenttäObjekti(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));

                                    }
                                    else if (tarkastettavaRivi.contains("+")){
                                        luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                        //Keimo3D.kenttäObjektit.add(new KenttäObjektiVanha(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
                                        Objektit3D.kenttäObjektit.add(new KenttäObjekti(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
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
                                    //Keimo3D.kenttäObjektit.add(new KenttäObjektiVanha(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
                                    Objektit3D.kenttäObjektit.add(new KenttäObjekti(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
                                }
                                else if (tarkastettavaRivi.contains(",")) {
                                    luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf(","));
                                    //Keimo3D.kenttäObjektit.add(new KenttäObjektiVanha(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
                                    Objektit3D.kenttäObjektit.add(new KenttäObjekti(luotavaObjekti, luotavanObjektinOminaisuusLista, luotavanObjektinX, 0, luotavanObjektinY));
                                }
                                tarkastettavaRivi = read.readLine();
                            }
                            else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                                break;
                            }
                            else {
                                tarkastettavaRivi = read.readLine();
                                rivejäTarkastettu++;
                            }
                        }
                    }
                }
                else if (tarkastettavaRivi.contains("#maasto:")) {
                    if (tarkastettavaRivi.contains("{")) {
                        tarkastettavaRivi = read.readLine();
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
                                    // for (String st : luotavanMaastonOminaisuusLista) {
                                    //     System.out.println("ominaisuus: " + st);
                                    // }
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
                                        //AWTTest3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                        //Lattia.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                        Objektit3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                    }
                                    else if (tarkastettavaRivi.contains("+")){
                                        luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
                                        //AWTTest3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                        //Lattia.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                        Objektit3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
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
                                    //AWTTest3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                    //Lattia.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                    Objektit3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                }
                                else if (tarkastettavaRivi.contains(",")) {
                                    luotavaMaasto = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf(","));
                                    //AWTTest3D.lattiaTilet.add(new LattiaVanha(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                    //Lattia.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                    Objektit3D.lattiaTilet.add(new LattiaTile(luotavanMaastonOminaisuusLista, luotavanMaastonX, 0, luotavanMaastonY));
                                }
                                tarkastettavaRivi = read.readLine();
                            }
                            else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                                break;
                            }
                            else {
                                tarkastettavaRivi = read.readLine();
                                rivejäTarkastettu++;
                            }
                        }
                    }
                }
            //     else if (tarkastettavaRivi.contains("#npc:")) {
            //         if (tarkastettavaRivi.contains("{")) {
            //             tarkastettavaRivi = sc.nextLine();
            //             rivejäTarkastettu++;
            //             while (!tarkastettavaRivi.contains("}")) {
            //                 luotavaNPC = "";
            //                 luotavanNPCnX = 0;
            //                 luotavanNPCnY = 0;
            //                 if (tarkastettavaRivi.startsWith("        ")) {
            //                     if (tarkastettavaRivi.contains("+ominaisuudet:")) {
            //                         String ominaisuudetMerkkijonona = tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("[") +1, tarkastettavaRivi.indexOf("]"));
            //                         int ominaisuuksienMäärä = 0;
            //                         for (int i = 0; i < tarkastettavaRivi.length()-1; i++) {
            //                             if (tarkastettavaRivi.charAt(i) == '=') {
            //                                 ominaisuuksienMäärä++;
            //                             }
            //                             else if (tarkastettavaRivi.charAt(i) == ']') {
            //                                 break;
            //                             }
            //                         }
            //                         for (int i = 0; i < ominaisuuksienMäärä; i++) {
            //                             luotavanNPCnOminaisuusLista = ominaisuudetMerkkijonona.split(",");
            //                         }
            //                         //for (String st : luotavanObjektinOminaisuusLista) {
            //                         //    System.out.println("ominaisuus: " + st);
            //                         //}
            //                         if (tarkastettavaRivi.contains("_")) {
            //                             luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
            //                             luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
            //                             luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
            //                             uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
            //                         }
            //                         else if (tarkastettavaRivi.contains("+")){
            //                             luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("+"));
            //                             uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, true, luotavanNPCnOminaisuusLista));
            //                         }
            //                     }
            //                     else if (tarkastettavaRivi.contains("_")) {
            //                         luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
            //                         luotavanNPCnX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
            //                         luotavanNPCnY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
            //                         uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, true, luotavanNPCnX, luotavanNPCnY, false, null));
            //                     }
            //                     else if (tarkastettavaRivi.contains(",")) {
            //                         luotavaNPC = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf(","));
            //                         uusiNPCLista.add(luoNPCTiedoilla(luotavaNPC, false, luotavanNPCnX, luotavanNPCnY, false, null));
            //                     }
            //                 }
            //                 if (sc.hasNextLine()) {
            //                     tarkastettavaRivi = sc.nextLine();
            //                     rivejäTarkastettu++;
            //                 }
            //                 else {
            //                     break;
            //                 }
            //             }
            //         }
            //     }
            // }
            // Huone huone = new Huone(uusiHuoneenId, uusiHuoneenKoko, uusiHuoneenNimi, uusiHuoneenTaustanPolku, uusiHuoneenAlue, uusiObjektiLista, uusiMaastoLista, uusiNPCLista, uusiHuoneenTarinanTunniste, uusiHuoneenVaadittuTavoite);
            // huone.päivitäReunawarppienTiedot(uusiWarpVasen, uusiWarpVasenHuoneId, uusiWarpOikea, uusiWarpOikeaHuoneId, uusiWarpAlas, uusiWarpAlasHuoneId, uusiWarpYlös, uusiWarpYlösHuoneId);
            // //System.out.println("huone: " + huone.annaId() + ", vasen warp: " + huone.warpVasen + huone.warpVasenHuoneId + ", oikea warp: " + huone.warpOikea + huone.warpOikeaHuoneId + ", alas warp: " + huone.warpAlas + huone.warpAlasHuoneId + ", ylös warp: " + huone.warpYlös + huone.warpYlösHuoneId);
            // //uusiHuoneKartta.put(uusiHuoneenId, huone);
            // uusiObjektiLista.clear();
            // uusiMaastoLista.clear();
            // uusiNPCLista.clear();
            // uusiWarpVasen = false;
            // uusiWarpOikea = false;
            // uusiWarpAlas = false;
            // uusiWarpYlös = false;
            }
            read.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    protected static void tallennaVäliaikainenTiedosto() {
        try {
            File tiedosto = new File("tiedostot/pelitiedostot/huoneet/väliaikainen.ksh");
            Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedosto.getPath()), StandardCharsets.UTF_8);
            String kokoTiedostoMerkkijonona = HuoneEditorinMetodit.luoMerkkijonoHuoneesta(HuoneEditoriIkkuna.huoneKartta, HuoneEditoriIkkuna.muokattavaHuone);
            fstream.write(kokoTiedostoMerkkijonona);
            fstream.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected static void lataaObjMalli() {
        
    }
}
