package keimo.Säikeet;

import keimo.*;
import keimo.HuoneEditori.*;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Kenttäkohteet.KauppaRuutu;
import keimo.Kenttäkohteet.Pulloautomaatti;
import keimo.Kenttäkohteet.VisuaalinenObjekti;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.Lisäruudut.ValintaDialogiRuutu;
import keimo.Utility.*;
import keimo.Utility.SkaalattavaKuvake.Peilaus;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;

public class GrafiikanPäivitysSäie extends Thread {

    static double alkuAika = 0;
    static double loppuAika = 0;
    static double aikaErotusNs = 0;
    static double aikaErotusUs = 0;
    static double aikaErotusMs = 0;
    static double fpsFloat = 0f;
    static long fps = 0;
    static long kertymänAika = 0;
    static long kuviaKertymässä = 0;
    static long frameja = 0;
    public static boolean ongelmaGrafiikassa = false;
    static int odotusAikaUs = 10;
    public boolean säieKäynnissä = false;

    static ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
    static ArrayList<Long> päivitysAikaLista = new ArrayList<Long>();
    public static ImageIcon uusiTausta;

    static int viimeisinIkkunanLeveys = 0;
    static int viimeisinIkkunanKorkeus = 0;
    static boolean ikkunanPäivitys = false;

    PeliKentänPäivittäjä peliKentänPäivittäjä = new PeliKentänPäivittäjä();

    private static void päivitäPelaajanKuvake() {
        
        switch (Pelaaja.keimonState) {
            case IDLE: 
                switch (Pelaaja.keimonSuuntaVasenOikea) {
                    case VASEN:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_laiha.gif"), Peilaus.PEILAA_X); break;
                                    case NORMAALI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_normaali.gif"), Peilaus.PEILAA_X); break;
                                    case LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_lihava.gif"), Peilaus.PEILAA_X); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_erittäinlihava.gif"), Peilaus.PEILAA_X); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"), Peilaus.PEILAA_X); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_laiha.gif"), Peilaus.PEILAA_X); break;
                                    case NORMAALI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_normaali.gif"), Peilaus.PEILAA_X); break;
                                    case LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_lihava.gif"), Peilaus.PEILAA_X); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_erittäinlihava.gif"), Peilaus.PEILAA_X); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"), Peilaus.PEILAA_X); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_laiha.gif"), Peilaus.PEILAA_X); break;
                                    case NORMAALI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_normaali.gif"), Peilaus.PEILAA_X); break;
                                    case LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_lihava.gif"), Peilaus.PEILAA_X); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_erittäinlihava.gif"), Peilaus.PEILAA_X); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"), Peilaus.PEILAA_X); break;
                                }
                            break;
                            case ÜBER:
                            break;
                            default:
                            break;
                        }
                    break;
                    case OIKEA:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case ÜBER:
                            break;
                            default:
                            break;
                        }
                    break;
                }
            break;
            case JUOKSU:
                switch (Pelaaja.keimonSuunta) {
                    case VASEN:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case ÜBER:
                            break;
                            default:
                            break;
                            }
                    break;
                    case OIKEA:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
                            break;
                            case ÜBER:
                            break;
                            default:
                            break;
                            }
                    break;
                    case ALAS:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_alas_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case ÜBER:
                            break;
                            default:
                            break;
                            }
                    break;
                    case YLÖS:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_ylös_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case ÜBER:
                            break;
                            default:
                            break;
                            }
                    break;
                }
            break;
            case KUOLLUT:
                Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_kuollut.png");
            break;
        }
        if (Pelaaja.kuolemattomuusAika > 0) {
            ImageIcon vilkkuvaKuvake = Pelaaja.kuvake;
            if (Peli.globaaliTickit % 2 == 0) {
                Pelaaja.kuvake = vilkkuvaKuvake;
            }
            else {
                Pelaaja.kuvake = null;
            }
        }
    }

    private static void tarkistaPelaajanLiikesuunta() {
        if (Pelaaja.pelaajaLiikkuuVasen) {
            Pelaaja.keimonSuunta = Suunta.VASEN;
        }
        else if (Pelaaja.pelaajaLiikkuuOikea) {
            Pelaaja.keimonSuunta = Suunta.OIKEA;
        }
        if (Pelaaja.pelaajaLiikkuuYlös) {
            Pelaaja.keimonSuunta = Suunta.YLÖS;
        }
        else if (Pelaaja.pelaajaLiikkuuAlas) {
            Pelaaja.keimonSuunta = Suunta.ALAS;
        }
    }

    private static void päivitäHUD() {
        try {
            for (int i = 0; i < Pelaaja.esineet.length; i++) {
                if (Pelaaja.esineet[i] == null) {
                    PeliRuutu.esineLabel[i].setText(null);
                    PeliRuutu.esineLabel[i].setIcon(null);
                }
                else {
                    PeliRuutu.esineLabel[i].setIcon(Pelaaja.esineet[i].annaKuvake());
                }
            }
            if (Peli.valittuEsine == null) {
                PeliRuutu.valitunEsineenNimiLabel.setText("");
            }
            else {
                PeliRuutu.valitunEsineenNimiLabel.setText(Peli.valittuEsine.annaNimi());
            }

            if (Peli.huone.annaAlue().startsWith("Kauppa")) {
                PeliRuutu.ostosPanelinHud.setVisible(true);
                PeliRuutu.kontrolliInfoPaneli.setVisible(false);
            }
            else {
                PeliRuutu.ostosPanelinHud.setVisible(false);
                PeliRuutu.kontrolliInfoPaneli.setVisible(true);
            }
            switch (Peli.huone.annaAlue()) {
                case "Puisto": PeliRuutu.minimapLabel.setIcon(new ImageIcon("tiedostot/kuvat/kartta/kartta_puisto.png")); break;
                case "Asuintalot", "Pelto": PeliRuutu.minimapLabel.setIcon(new ImageIcon("tiedostot/kuvat/kartta/kartta_yokylä.png")); break;
                case "Metsä": PeliRuutu.minimapLabel.setIcon(new ImageIcon("tiedostot/kuvat/kartta/kartta_metsä.png")); break;
            }

            PeliRuutu.hudTeksti.setText(PääIkkuna.hudTeksti.getText());
            PeliRuutu.tavoiteInfoLabel.setText(TavoiteLista.nykyinenTavoite);
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    private static void skaalaaHUD() {
        if (PääIkkuna.ikkuna !=null && PeliRuutu.tavaraPaneli != null && PeliRuutu.kontrolliInfoPaneli != null) {
            if (tarkistaIkkunanKoonMuutos() || ikkunanPäivitys) {
                if (PääIkkuna.ikkuna.getHeight() > 1074 && PääIkkuna.ikkuna.getWidth() > 1800) {
                    PeliRuutu.esineenKokoPx = 96;
                    PeliRuutu.pelaajanKokoPx = 96;
                    PeliRuutu.vasenYläPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.vasenKeskiPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.vasenAlaPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.oikeaYläPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.oikeaKeskiPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.oikeaAlaPaneeli.setPreferredSize(new Dimension(320, 320));
                    ImageIcon taustaSkaalaattu = new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys_kokoruutu.png");
                    PeliRuutu.vasenYläPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.vasenKeskiPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.vasenAlaPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.oikeaYläPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.oikeaAlaPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.vasenYläPaneelinTausta.setBounds(0, 0, 320, 320);
                    PeliRuutu.vasenKeskiPaneelinTausta.setBounds(0, 0, 320, 320);
                    PeliRuutu.vasenAlaPaneelinTausta.setBounds(0, 0, 320, 320);
                    PeliRuutu.oikeaYläPaneelinTausta.setBounds(0, 0, 320, 320);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setBounds(0, 0, 320, 320);
                    PeliRuutu.oikeaAlaPaneelinTausta.setBounds(0, 0, 320, 320);
                    PeliRuutu.aikaInfoPaneli.setBounds(10, 10, 300, 300);
                    PeliRuutu.statsiPaneeli.setBounds(10, 10, 300, 300);
                    PeliRuutu.invaPanelinHud.setBounds(10, 10, 300, 300);
                    PeliRuutu.debugInfoPaneli.setBounds(10, 10, 300, 300);
                    PeliRuutu.alueInfoPaneli.setBounds(10, 10, 300, 300);
                    PeliRuutu.ostosPanelinHud.setBounds(10, 10, 300, 300);
                    PeliRuutu.kontrolliInfoPaneli.setBounds(10, 10, 300, 300);
                    PeliRuutu.peliKentänTaustaPaneli.setPreferredSize(new Dimension(980, 980));
                    PeliRuutu.vuoropuhePaneli.setBounds(0, 860, 980, 120);

                    PeliRuutu.skaalaaKuvakkeet = true;
                }
                else {
                    PeliRuutu.esineenKokoPx = 64;
                    PeliRuutu.pelaajanKokoPx = 64;
                    PeliRuutu.vasenYläPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.vasenKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.vasenAlaPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.oikeaYläPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.oikeaKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.oikeaAlaPaneeli.setPreferredSize(new Dimension(200, 220));
                    ImageIcon taustaSkaalaattu = new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png");
                    PeliRuutu.vasenYläPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.vasenKeskiPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.vasenAlaPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.oikeaYläPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.oikeaAlaPaneelinTausta.setIcon(taustaSkaalaattu);
                    PeliRuutu.vasenYläPaneelinTausta.setBounds(0, 0, 200, 220);
                    PeliRuutu.vasenKeskiPaneelinTausta.setBounds(0, 0, 200, 220);
                    PeliRuutu.vasenAlaPaneelinTausta.setBounds(0, 0, 200, 220);
                    PeliRuutu.oikeaYläPaneelinTausta.setBounds(0, 0, 200, 220);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setBounds(0, 0, 200, 220);
                    PeliRuutu.oikeaAlaPaneelinTausta.setBounds(0, 0, 200, 220);
                    PeliRuutu.aikaInfoPaneli.setBounds(10, 10, 180, 200);
                    PeliRuutu.statsiPaneeli.setBounds(10, 10, 180, 200);
                    PeliRuutu.invaPanelinHud.setBounds(10, 10, 180, 200);
                    PeliRuutu.debugInfoPaneli.setBounds(10, 10, 180, 200);
                    PeliRuutu.alueInfoPaneli.setBounds(10, 10, 180, 200);
                    PeliRuutu.ostosPanelinHud.setBounds(10, 10, 180, 200);
                    PeliRuutu.kontrolliInfoPaneli.setBounds(10, 10, 180, 200);
                    PeliRuutu.peliKentänTaustaPaneli.setPreferredSize(new Dimension(660, 660));
                    PeliRuutu.vuoropuhePaneli.setBounds(0, 540, 660, 120);
                    
                    PeliRuutu.skaalaaKuvakkeet = false;
                }
                PeliRuutu.taustaPaneli.setBounds(0, 0, Peli.kentänKoko * PeliRuutu.esineenKokoPx + 20, Peli.kentänKoko * PeliRuutu.esineenKokoPx + 20);
                PeliRuutu.taustaLabel.setBounds(0, 0, Peli.kentänKoko * PeliRuutu.esineenKokoPx + 20, Peli.kentänKoko * PeliRuutu.esineenKokoPx + 20);
                PeliRuutu.peliKenttä.setBounds(10, 10, 10 * PeliRuutu.esineenKokoPx, 10 * PeliRuutu.esineenKokoPx);
                PeliRuutu.lisäRuutuPaneli.setBounds(2 * PeliRuutu.esineenKokoPx, 2 * PeliRuutu.esineenKokoPx, 6 * PeliRuutu.esineenKokoPx, 6 * PeliRuutu.esineenKokoPx);
                PeliRuutu.pausePaneli.setBounds(2 * PeliRuutu.esineenKokoPx, 2 * PeliRuutu.esineenKokoPx, 6 * PeliRuutu.esineenKokoPx, 6 * PeliRuutu.esineenKokoPx);
                Pelaaja.hitbox.setSize(PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
                Pelaaja.nopeus = Math.round((8 - Pelaaja.ostosKori.size()) * PeliRuutu.pelaajanKokoPx / 64f);
                Pelaaja.teleport(Pelaaja.sijX, Pelaaja.sijY);


                // if (PääIkkuna.ikkuna.getHeight() < 750) {
                //     PeliRuutu.yläPaneeli.setVisible(false);
                //     PeliRuutu.alaPaneeli.setVisible(false);
                // }
                // else if (PääIkkuna.ikkuna.getHeight() < 768) {
                //     PeliRuutu.yläPaneeli.setVisible(false);
                //     PeliRuutu.alaPaneeli.setVisible(true);
                // }
                // else {
                //     PeliRuutu.yläPaneeli.setVisible(true);
                //     PeliRuutu.alaPaneeli.setVisible(true);
                // }
                if (PääIkkuna.ikkuna.getHeight() < 740 || PääIkkuna.ikkuna.getWidth() < 1280) {
                    PeliRuutu.kokoruudunTakatausta.setVisible(true);
                }
                else {
                    PeliRuutu.kokoruudunTakatausta.setVisible(false);
                }
                PeliKenttäMetodit.teleporttaaViholliset = true;
                PääIkkuna.ikkunanKokoMuutettuEnnenHuoneenLatuasta = true;
                ikkunanPäivitys = false;
                PeliRuutu.peliKenttäJaHUD.setBackground(Color.BLACK);
                PääIkkuna.uudelleenpiirräKenttä = true;
            }
        }
    }

    private static boolean tarkistaIkkunanKoonMuutos() {
        if (viimeisinIkkunanLeveys != PääIkkuna.ikkuna.getWidth() || viimeisinIkkunanKorkeus != PääIkkuna.ikkuna.getHeight()) {
            viimeisinIkkunanLeveys = PääIkkuna.ikkuna.getWidth();
            viimeisinIkkunanKorkeus = PääIkkuna.ikkuna.getHeight();
            return true;
        }
        else {
            return false;
        }
    }

    private static void tileMuutokset() {
        if (Peli.huone != null) {
            if (Peli.huone.annaAlue().startsWith("Kauppa")) {
                try {
                    if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof KauppaRuutu) {
                        if (Peli.pelikenttä[Pelaaja.sijX +1][Pelaaja.sijY] instanceof VisuaalinenObjekti) {
                            VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[Pelaaja.sijX +1][Pelaaja.sijY];
                            if (PääIkkuna.tekstiAuki) {
                                vo.tiedostonNimi = "kassa_vihkoauki.png";
                            }
                            else {
                                vo.tiedostonNimi = "kassa_vihkokiinni.png";
                            }
                            vo.päivitäKuvanAsento();
                        }
                    }
                    else if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Pulloautomaatti) {
                        Pulloautomaatti pulloautomaatti = (Pulloautomaatti)Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                        pulloautomaatti.valitseKuvake(pulloautomaatti.tila);
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void lisäPaneliMuutokset() {
        if (PeliRuutu.lisäRuutuPaneli != null) {
            if (PeliRuutu.lisäRuutuPaneli.isVisible() && ValintaDialogiRuutu.vasenOsoitin != null) {
                for (int i = 0; i < ValintaDialogiRuutu.vasenOsoitin.length; i++) {
                    if (ValintaDialogiRuutu.valintaInt == i) {
                        ValintaDialogiRuutu.vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/dialogi/osoitin32.png"));
                    }
                    else {
                        ValintaDialogiRuutu.vasenOsoitin[i].setIcon(null);
                    }
                }
            }
        }
    }

    private static void päivitäPelaajanKarttaIconinSijainti() {
        boolean sijaintiMääritetty = true;
        switch (PeliRuutu.alueInfoLabel.getText()) {
            case "Puisto":
                switch (Peli.huone.annaId()) {
                    case 0:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(85, 80, 20, 20);
                    break;
                    case 1:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(85, 20, 20, 20);
                    break;
                    case 2:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(85, 140, 20, 20);
                    break;
                    case 3:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(145, 80, 20, 20);
                    break;
                    case 4:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(20, 80, 20, 20);
                    break;
                    default:
                        sijaintiMääritetty = false;
                        PeliRuutu.pelaajaKartallaLabel.setBounds(10, 10, 20, 20);
                    break;
                }
            break;
            default:
                sijaintiMääritetty = false;
                PeliRuutu.pelaajaKartallaLabel.setBounds(10, 10, 20, 20);
            break;
        }
        if (sijaintiMääritetty) {
            PeliRuutu.pelaajaKartallaLabel.setIcon(new ImageIcon("tiedostot/kuvat/kartta/pelaaja_kartalla.png"));
        }
        else {
            PeliRuutu.pelaajaKartallaLabel.setIcon(new ImageIcon("tiedostot/kuvat/kartta/pelaaja_kartalla_virhe.png"));
        }
    }
    
    final JPanel panel = new JPanel();
    class PeliKentänPäivittäjä extends Thread {
        @Override
        public void run() {
            try {
                //Calculate how many ns each frame should take for our target game hertz.
                final double TIME_BETWEEN_UPDATES = 1000000000 / PelinAsetukset.RUUDUNPÄIVITYS;
                //If we are able to get as high as this FPS, don't render again.
                double TARGET_FPS = PelinAsetukset.tavoiteFPS;
                final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
                //At the very most we will update the game this many times before a new render.
                //If you're worried about visual hitches more than perfect timing, set this to 1.
                final int MAX_UPDATES_BEFORE_RENDER = 5;
                //We will need the last update time.
                double lastUpdateTime = System.nanoTime();
                //Store the last time we rendered.
                double lastRenderTime = System.nanoTime();
                while (true) {

                    alkuAika = System.nanoTime();
                    double now = System.nanoTime();
                    int updateCount = 0;

                    //Do as many game updates as we need to, potentially playing catchup.
                    while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                        //MyGame.this.scene.update();
                        lastUpdateTime += TIME_BETWEEN_UPDATES;
                        updateCount++;
                    }

                    //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                    //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                    if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                        lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                    }

                    //Render. To do so, we need to calculate interpolation for a smooth render.
                    float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
                    //MyGame.this.scene.render(interpolation);
                    alkuAika = System.nanoTime();
                    PääIkkuna.vaatiiPäivityksen = true;
                    //PääIkkuna.pääPaneeli.repaint();
                    if (PeliRuutu.peliRuutuAktiivinen) {
                        päivitäHUD();
                        skaalaaHUD();
                        tarkistaPelaajanLiikesuunta();
                        päivitäPelaajanKuvake();
                        PeliRuutu.päivitäPeliRuutu();
                        PeliRuutu.luoKänniEfekti();
                        tileMuutokset();
                        lisäPaneliMuutokset();
                        päivitäPelaajanKarttaIconinSijainti();
                    }

                    if (HuoneEditoriIkkuna.vaatiiPäivityksen) {
                        HuoneEditoriIkkuna.päivitäEditoriIkkuna();
                    }

                    kertymänAika += aikaErotusUs;

                    if (PääIkkuna.uudelleenpiirräKaikki) {
                        PääIkkuna.pääPaneeli.removeAll();
                        PääIkkuna.pääPaneeli.add(PeliRuutu.luoPeliRuudunGUI());
                        ikkunanPäivitys = true;
                        PeliRuutu.alustaPeliRuutu();
                        PeliRuutu.vaihdaTausta(uusiTausta);
                        PeliRuutu.päivitäOstosPaneli();
                        PääIkkuna.uudelleenpiirräKaikki = false;
                    }
                    if (PääIkkuna.uudelleenpiirräKenttä) {
                        PeliRuutu.alustaPeliRuutu();
                        PeliRuutu.vaihdaTausta(uusiTausta);
                        //PeliRuutu.skaalaaKuvakkeet = true;
                        PääIkkuna.uudelleenpiirräKenttä = false;
                    }
                    if (PääIkkuna.uudelleenpiirräObjektit) {
                        PeliRuutu.päivitäNPCKenttä();
                        PeliRuutu.vaihdaTausta(uusiTausta);
                        PääIkkuna.uudelleenpiirräObjektit = false;
                    }

                    lastRenderTime = now;

                    //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                    while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                        //allow the threading system to play threads that are waiting to run.
                        Thread.yield();

                        //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                        //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                        //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                        //On my OS it does not unpuase the game if i take this away
                        try {
                            if (PelinAsetukset.ajoitus == AjoitusMuoto.TARKKA) {}
                            else {
                                Thread.sleep(1);
                            }
                            //LockSupport.parkNanos(1_000_000);
                        }
                        catch (Exception e) {

                        }

                        now = System.nanoTime();
                    }
                    frameja++;
                    kuviaKertymässä++;
                    loppuAika = System.nanoTime();
                    aikaErotusNs = (loppuAika - alkuAika);
                    aikaErotusUs = aikaErotusNs/1000;
                    aikaErotusMs = aikaErotusUs/1000;
                }
            }
                
            catch (Exception e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();
                System.out.println(sStackTrace);
                String viesti = "Grafiikkasäie on kaatunut.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
                String otsikko = "Ongelma grafiikkasäikeessä";
                int virheenJälkeenValinta = CustomViestiIkkunat.GrafiikkäSäieVirhe.showDialog(viesti, otsikko);
                switch (virheenJälkeenValinta) {
                    case JOptionPane.YES_OPTION: run(); break;
                    case JOptionPane.NO_OPTION: System.exit(0); break;
                }
            }
        }
    }

    // public void päivitäAjastin(long aiemminKulunutAika) {
        
    //     long odotaKunnes = System.nanoTime() + (10 * 1_000_000) - aiemminKulunutAika;
    //     while(odotaKunnes > System.nanoTime()){
    //         ;
    //     }
    //     //LockSupport.parkNanos(odotaKunnes - System.nanoTime());
    // }

    @Override
    public void run() {

        frameja = 0;
        odotusAikaUs = 1_000_000 / PelinAsetukset.tavoiteFPS;
            
        if (!säieKäynnissä) {
            peliKentänPäivittäjä.setName("Pelikentän päivittäjä");
            peliKentänPäivittäjä.setPriority(3);
            peliKentänPäivittäjä.run();
        }
        säieKäynnissä = true;
    }
}