package keimo.Säikeet;

import keimo.*;
import keimo.HuoneEditori.*;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Ikkunat.DebugInfoIkkuna;
import keimo.Kenttäkohteet.KauppaRuutu;
import keimo.Kenttäkohteet.Pulloautomaatti;
import keimo.Kenttäkohteet.VisuaalinenObjekti;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Ruudut.OsionAlkuRuutu;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.TarinaRuutu;
import keimo.Ruudut.ValikkoRuutu;
import keimo.Ruudut.Lisäruudut.ValintaDialogiRuutu;
import keimo.Utility.*;
import keimo.Utility.Käännettävä.Suunta;
import keimo.Utility.SkaalattavaKuvake.Peilaus;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import javax.swing.*;

public class GrafiikanPäivitysSäie extends Thread {

    static double alkuAika = 0;
    static double loppuAika = 0;
    static double aikaErotusNs = 0;
    static double aikaErotusUs = 0;
    static double aikaErotusMs = 0;
    static double fpsFloat = 0f;
    static double päivitysAikaKertymä = 0;
    static long kertymänAika = 0;
    static long kuviaKertymässä = 0;
    static long frameja = 0;
    public static boolean ongelmaGrafiikassa = false;
    public static boolean säieKäynnissä = false;
    public static boolean säieKeskeytetty = false;
    public static boolean huoneenGrafiikanLatausKäynnissä = false;

    static ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
    static ArrayList<Long> päivitysAikaLista = new ArrayList<Long>();
    public static ImageIcon uusiTausta;
    public static KäännettäväKuvake uusiTaustaSkaalattu;

    static int viimeisinIkkunanLeveys = 0;
    static int viimeisinIkkunanKorkeus = 0;
    public static boolean ikkunanPäivitys = false;
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

    static double tavoiteFPS = PelinAsetukset.tavoiteFPS;
    static double tavoiteRuudunPäivitysAika = 1000000000 / tavoiteFPS;

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
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_laiha.gif"), Peilaus.PEILAA_X); break;
                                    case NORMAALI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_normaali.gif"), Peilaus.PEILAA_X); break;
                                    case LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_lihava.gif"), Peilaus.PEILAA_X); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_erittäinlihava.gif"), Peilaus.PEILAA_X); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new SkaalattavaKuvake(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"), Peilaus.PEILAA_X); break;
                                }
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
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_über_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif"); break;
                                }
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
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/idle/default.gif")); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif")); break;
                                }
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
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/idle/default.gif")); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif")); break;
                                }
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
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/idle/default.gif")); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif")); break;
                                }
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
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/idle/default.gif")); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/default.gif")); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon(("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.gif")); break;
                                }
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
            //Graphics2D g2d = (Graphics2D)Pelaaja.kuvake.getImage().getGraphics();
            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
            // SkaalattavaKuvake vilkkuvaKuvakeNäkyvä = new SkaalattavaKuvake(Pelaaja.kuvake, 1f);
            // Icon vilkkuvaKuvakeNäkymätön = new KäännettäväKuvake(Pelaaja.kuvake, 0, false, false, 1, 0.05f);
            ImageIcon vilkkuvaKuvake = Pelaaja.kuvake;
            if (Peli.globaaliTickit % 2 == 0) {
                Pelaaja.kuvake = vilkkuvaKuvake;
            }
            else {
                Pelaaja.kuvake = null;
            }
        }
        //Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/old/keimo/pelaaja.png");
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

            PeliRuutu.hudTeksti.setText(PääIkkuna.hudTeksti.getText());
            PeliRuutu.tavoiteInfoLabel.setText("<html>" + TavoiteLista.nykyinenTavoite);
        }
        catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    private static ImageIcon luoPaneliKuvake(Component panel) {
        try {
            Dimension size = panel.getSize();
            BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            panel.paint(g2);
            Image skaalattuKuvake = image.getScaledInstance(180, 180, Image.SCALE_DEFAULT);
            ImageIcon kuvake = new ImageIcon(skaalattuKuvake);
            return kuvake;
        }
        catch (Exception e) {
            //System.out.println("Minimapin luonti epäonnistui.");
            //e.printStackTrace();
            return null;
        }
    }

    private static void skaalaaHUD() {
        if (PääIkkuna.ikkuna !=null && PeliRuutu.tavaraPaneli != null && PeliRuutu.kontrolliInfoPaneli != null) {
            if (tarkistaIkkunanKoonMuutos() || ikkunanPäivitys) {
                if (PääIkkuna.ikkuna.getHeight() > 1044 && PääIkkuna.ikkuna.getWidth() > 1800) {
                    PääIkkuna.isoSkaalaus = true;
                    //PeliRuutu.esineenKokoPx = 64;
                    //PeliRuutu.pelaajanKokoPx = 64;
                    PeliRuutu.vasenYläPaneeli.setPreferredSize(new Dimension(300, 330));
                    PeliRuutu.vasenKeskiPaneeli.setPreferredSize(new Dimension(300, 330));
                    PeliRuutu.vasenAlaPaneeli.setPreferredSize(new Dimension(300, 330));
                    PeliRuutu.oikeaYläPaneeli.setPreferredSize(new Dimension(300, 330));
                    PeliRuutu.oikeaKeskiPaneeli.setPreferredSize(new Dimension(300, 330));
                    PeliRuutu.oikeaAlaPaneeli.setPreferredSize(new Dimension(300, 330));
                    //ImageIcon taustaSkaalaattu = new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys_kokoruutu.png");
                    KäännettäväKuvake hudPaneeliTaustaSkaalaattu = new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"), 0, false, false, (330d/220d), 1, -76, -84, false);
                    PeliRuutu.vasenYläPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.vasenKeskiPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.vasenAlaPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.oikeaYläPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.oikeaAlaPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.vasenYläPaneelinTausta.setBounds(0, 0, 300, 330);
                    PeliRuutu.vasenKeskiPaneelinTausta.setBounds(0, 0, 300, 330);
                    PeliRuutu.vasenAlaPaneelinTausta.setBounds(0, 0, 300, 330);
                    PeliRuutu.oikeaYläPaneelinTausta.setBounds(0, 0, 300, 330);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setBounds(0, 0, 300, 330);
                    PeliRuutu.oikeaAlaPaneelinTausta.setBounds(0, 0, 300, 330);
                    PeliRuutu.aikaInfoPaneli.setBounds(15, 15, 270, 300);
                    PeliRuutu.statsiPaneeli.setBounds(15, 15, 270, 300);
                    PeliRuutu.invaPanelinHud.setBounds(15, 15, 270, 300);
                    PeliRuutu.debugInfoPaneli.setBounds(15, 15, 270, 300);
                    PeliRuutu.alueInfoPaneli.setBounds(15, 15, 270, 300);
                    PeliRuutu.ostosPanelinHud.setBounds(15, 15, 270, 300);
                    PeliRuutu.pausePaneli.setBounds(192, 192, 576, 576);
                    PeliRuutu.minimapLabel.setBounds(0, 0, 270, 280);
                    PeliRuutu.kontrolliInfoPaneli.setBounds(15, 15, 270, 300);
                    PeliRuutu.peliKentänTaustaPaneli.setPreferredSize(new Dimension(990, 990));
                    PeliRuutu.taustaLabel.setBounds(0, 0, 990, 990);
                    KäännettäväKuvake taustaSkaalaattu = new KäännettäväKuvake(uusiTaustaSkaalattu, 0, false, false, 1, 1, 0, 0, false);
                    PeliRuutu.vaihdaTausta(taustaSkaalaattu);
                    PeliRuutu.vuoropuhePaneli.setBounds(0, 870, 990, 120);
                    PeliRuutu.scrollaavaPelikenttä.setBounds(0, 0, 960, 960);
                    PeliRuutu.scrollaavanPelikentänPaneeli.setBounds(15, 15, 960, 960);
                    PeliRuutu.keskitäPelikenttä(Peli.kentänKoko, 960);
                }
                else {
                    PääIkkuna.isoSkaalaus = false;
                    //PeliRuutu.esineenKokoPx = 64;
                    //PeliRuutu.pelaajanKokoPx = 64;
                    PeliRuutu.vasenYläPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.vasenKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.vasenAlaPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.oikeaYläPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.oikeaKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
                    PeliRuutu.oikeaAlaPaneeli.setPreferredSize(new Dimension(200, 220));
                    ImageIcon hudPaneeliTaustaSkaalaattu = new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png");
                    PeliRuutu.vasenYläPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.vasenKeskiPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.vasenAlaPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.oikeaYläPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.oikeaKeskiPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
                    PeliRuutu.oikeaAlaPaneelinTausta.setIcon(hudPaneeliTaustaSkaalaattu);
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
                    PeliRuutu.pausePaneli.setBounds(128, 128, 384, 384);
                    PeliRuutu.minimapLabel.setBounds(0, 0, 180, 180);
                    PeliRuutu.kontrolliInfoPaneli.setBounds(10, 10, 180, 200);
                    PeliRuutu.peliKentänTaustaPaneli.setPreferredSize(new Dimension(660, 660));
                    PeliRuutu.taustaLabel.setBounds(0, 0, 660, 660);
                    if (PeliRuutu.referenssiTaustaKuvake != null) {
                        KäännettäväKuvake taustaSkaalaattu = new KäännettäväKuvake(uusiTausta, 0, false, false, 1, 1, 0, 0, false);
                        PeliRuutu.taustaLabel.setIcon(taustaSkaalaattu);
                    }
                    PeliRuutu.vuoropuhePaneli.setBounds(0, 540, 660, 120);
                    PeliRuutu.scrollaavaPelikenttä.setBounds(0, 0, 640, 640);
                    PeliRuutu.scrollaavanPelikentänPaneeli.setBounds(10, 10, 640, 640);
                    PeliRuutu.keskitäPelikenttä(Peli.kentänKoko, 640);
                }
                ValintaDialogiRuutu.muutaPanelinKokoa(Peli.asetuksetAuki, PääIkkuna.isoSkaalaus);

                if (PääIkkuna.ikkuna.getHeight() < 740 || PääIkkuna.ikkuna.getWidth() < 1280) {
                    PeliRuutu.kokoruudunTakatausta.setVisible(true);
                }
                else {
                    PeliRuutu.kokoruudunTakatausta.setVisible(false);
                }
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
        int peliKentänLeveys = PeliRuutu.peliKenttä.getWidth();
        int peliKentänKorkeus = PeliRuutu.peliKenttä.getHeight();
        int pelaajanSijaintiXKartalla;
        int pelaajanSijaintiYKartalla;
        if (PääIkkuna.isoSkaalaus) {
            pelaajanSijaintiXKartalla = (int)(Pelaaja.hitbox.getCenterX() * (270f/(float)peliKentänLeveys)) -10;
            pelaajanSijaintiYKartalla = (int)(Pelaaja.hitbox.getCenterY() * (280f/(float)peliKentänKorkeus)) -10;
        }
        else {
            pelaajanSijaintiXKartalla = (int)(Pelaaja.hitbox.getCenterX() * (180f/(float)peliKentänLeveys)) -10;
            pelaajanSijaintiYKartalla = (int)(Pelaaja.hitbox.getCenterY() * (180f/(float)peliKentänKorkeus)) -10;
        }
        PeliRuutu.pelaajaKartallaLabel.setBounds(pelaajanSijaintiXKartalla, pelaajanSijaintiYKartalla, 20, 20);
    }
    
    final JPanel panel = new JPanel();
    void grafiikanPäivitysMetodi() {
        try {
            while (säieKäynnissä) {
                if (!säieKeskeytetty) {
                    alkuAika = System.nanoTime();
                    suoritaGrafiikanPäivitys();
                    while (System.nanoTime() - alkuAika < tavoiteRuudunPäivitysAika) {
                        Thread.yield();
                        try {
                            if (PelinAsetukset.ajoitus == AjoitusMuoto.TARKKA) {}
                            else {
                                Thread.sleep(1);
                            }
                            //LockSupport.parkNanos(1_000_000);
                        }
                        catch (Exception e) {

                        }

                    }
                    frameja++;
                    kuviaKertymässä++;
                    loppuAika = System.nanoTime();
                    aikaErotusNs = (loppuAika - alkuAika);
                    aikaErotusUs = aikaErotusNs/1000;
                    aikaErotusMs = aikaErotusUs/1000;

                    PeliRuutu.ylätekstiViive.setText("Päivitysaika: " + neljäDesimaalia.format(aikaErotusMs) + " ms");
                    PeliRuutu.ylätekstiKuvat.setText("Kuvia: " + frameja);

                    if (kuviaKertymässä >= 10) {
                        kuviaKertymässä = 0;
                        päivitysAikaKertymä = 0;
                    }
                    else {
                        päivitysAikaKertymä += aikaErotusMs;
                    }
                    if (kuviaKertymässä > 0) {
                        double keskivertoPäivitysAika10Framea = päivitysAikaKertymä/kuviaKertymässä;
                        //System.out.println("kertymä: " +  päivitysAikaKertymä + ", n: " + kuviaKertymässä + ", ka 10 framea: " + keskivertoPäivitysAika10Framea);
                        PeliRuutu.ylätekstiFPS.setText("FPS: " + neljäDesimaalia.format(1000/keskivertoPäivitysAika10Framea) + " (Tavoite: " + PelinAsetukset.tavoiteFPS + ")");
                    }
                    //System.out.println("grafiikkasäie: " + "operaatioaika: " + (operaatioAika - alkuAika)/1_000_000 + " ms, " + "odotusaika: " + (loppuAika - operaatioAika)/1_000_000 + " ms");
                }
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
            int virheenJälkeenValinta = CustomViestiIkkunat.GrafiikkäSäieVirhe.näytäDialogi(viesti, otsikko);
            switch (virheenJälkeenValinta) {
                case JOptionPane.YES_OPTION: run(); break;
                case JOptionPane.NO_OPTION: System.exit(0); break;
                case JOptionPane.CLOSED_OPTION: JOptionPane.showMessageDialog(null, "Säie yritetään uudelleenkäynnistää.", "Uudelleenkäynnistys", JOptionPane.INFORMATION_MESSAGE); break;
            }
        }
    }

    public static void suoritaGrafiikanPäivitys() {
        if (PelinAsetukset.tavoiteFPS > 0) {
            tavoiteRuudunPäivitysAika = 1_000_000_000 / PelinAsetukset.tavoiteFPS;
        }
        else {
            tavoiteRuudunPäivitysAika = 0;
        }
        synchronized(Peli.huoneenLatausLukko) {
            PääIkkuna.vaatiiPäivityksen = true;
            if (PeliRuutu.peliRuutuAktiivinen) {
                päivitäHUD();
                skaalaaHUD();
                tarkistaPelaajanLiikesuunta();
                päivitäPelaajanKuvake();
                PeliRuutu.scrollaaPeliRuutua();
                PeliRuutu.päivitäPeliRuutu();
                PeliRuutu.luoKänniEfekti();
                tileMuutokset();
                lisäPaneliMuutokset();
                päivitäPelaajanKarttaIconinSijainti();
                if (PääIkkuna.sijaintiNäkyvissä) {
                    Peli.päivitäPelaajanSijaintiTiedot();
                }
                if (DebugInfoIkkuna.ikkuna != null) {
                    if (DebugInfoIkkuna.ikkuna.isVisible()) {
                        DebugInfoIkkuna.päivitäTiedot();
                    }
                }
            }
        }
        kertymänAika += aikaErotusUs;
        synchronized (Peli.huoneenLatausLukko) {
            synchronized (Peli.grafiikanLatausLukko) {
                if (PääIkkuna.uudelleenpiirräKaikki) {
                    System.out.println("Lukko vapaa grafiikkasäikeelle");
                    switch (PääIkkuna.aktiivinenRuutu) {
                        case PELIRUUTU:
                        if (!Peli.huoneVaihdettava) {
                            PääIkkuna.pääPaneeli.removeAll();
                            PääIkkuna.pääPaneeli.add(PeliRuutu.luoPeliRuudunGUI());
                            PeliRuutu.kokoruudunTakatausta.setVisible(false);
                            if (Peli.pauseDialogi) {
                                PeliRuutu.vuoropuhePaneli.setVisible(true);
                                PeliRuutu.vuoropuheTeksti.setText(PääIkkuna.viimeisinDialogiTeksti);
                                PeliRuutu.vuoropuheNimi.setText(PääIkkuna.viimeisinDialogiPuhuja);
                                PeliRuutu.vuoropuheKuvake.setIcon(PääIkkuna.viimeisinDialogiKuvake);
                            }
                            ikkunanPäivitys = true;
                            if (PääIkkuna.isoSkaalaus) {
                                PeliRuutu.vaihdaTausta(uusiTaustaSkaalattu);
                            }
                            else {
                                PeliRuutu.vaihdaTausta(uusiTausta);
                            }
                            //PeliRuutu.minimapLabel.setIcon(luoPaneliKuvake(PeliRuutu.peliKenttä));
                            PeliRuutu.päivitäOstosPaneli();
                            //PeliSäie.pelinTilanPäivitysYliajo = true;
                            PääIkkuna.uudelleenpiirräKaikki = false;
                            PääIkkuna.uudelleenpiirräKenttä = true;
                        }
                        break;
                        case TARINARUUTU:
                            PääIkkuna.lataaRuutu("tarinaruutu");
                            TarinaRuutu.tarinaPaneli.requestFocus();
                            PääIkkuna.uudelleenpiirräKaikki = false;
                        break;
                        case VALIKKORUUTU:
                            PääIkkuna.lataaRuutu("valikkoruutu");
                            ValikkoRuutu.nappiPaneliAlkuvalikko.requestFocus();
                            PääIkkuna.uudelleenpiirräKaikki = false;
                        break;
                        case OSIONALKURUUTU:
                            PääIkkuna.lataaRuutu("osionalkuruutu");
                            OsionAlkuRuutu.osionAlkuPaneli.requestFocus();
                            PääIkkuna.uudelleenpiirräKaikki = false;
                        break;
                        default:
                        break;
                    }
                }
                if (PääIkkuna.uudelleenpiirräKenttä) {
                    PeliRuutu.peliKenttä.removeAll();
                    PeliRuutu.alustaPeliRuutu();
                    PääIkkuna.uudelleenpiirräKenttä = false;
                    PääIkkuna.uudelleenpiirräTaustat = true;
                }
                if (PääIkkuna.uudelleenpiirräTaustat) {
                    if (PääIkkuna.isoSkaalaus) {
                        PeliRuutu.vaihdaTausta(uusiTaustaSkaalattu);
                        KäännettäväKuvake minimapSkaalattu = new KäännettäväKuvake(luoPaneliKuvake(PeliRuutu.peliKenttä), 90, false, false, (280d/180d), 1, -69, -77, false);
                        PeliRuutu.minimapLabel.setIcon(minimapSkaalattu);
                    }
                    else {
                        PeliRuutu.vaihdaTausta(uusiTausta);
                        PeliRuutu.minimapLabel.setIcon(luoPaneliKuvake(PeliRuutu.peliKenttä));
                    }
                    PääIkkuna.uudelleenpiirräTaustat = false;
                    PääIkkuna.uudelleenpiirräObjektit = true;
                }
                if (PääIkkuna.uudelleenpiirräObjektit) {
                    PeliRuutu.päivitäNPCKenttä();
                    PeliRuutu.päivitäAmmusKenttä();
                    PääIkkuna.uudelleenpiirräObjektit = false;
                }

                double kulunutAika = (System.nanoTime() - Peli.aikaReferenssi)/1_000_000;
                if (!Peli.ajastinPysäytetty) {
                    double kulunutAikaSek = (double)kulunutAika/1000d;
                    int kulunutAikaMin = (int)kulunutAikaSek / 60;
                    int kulunutAikaH = (int)kulunutAikaMin / 60;
                    kulunutAikaSek = kulunutAikaSek % 60;
                    PeliRuutu.ylätekstiAika.setText(kulunutAikaH + ":" + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
                }
            }
        }

        synchronized(HuoneenLatausSäie.editorinHuoneenLatausLukko) {
            if (HuoneEditoriIkkuna.vaatiiPäivityksen) {
                HuoneEditoriIkkuna.päivitäEditoriIkkuna();
            }
        }
    }

    @Override
    public void run() {
        frameja = 0;

        säieKäynnissä = true;
        grafiikanPäivitysMetodi();
    }
}