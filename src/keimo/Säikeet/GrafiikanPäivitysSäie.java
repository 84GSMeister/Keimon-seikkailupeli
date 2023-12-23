package keimo.Säikeet;

import keimo.*;
import keimo.HuoneEditori.*;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Ikkunat.DebugInfoIkkuna;
import keimo.Kenttäkohteet.KauppaRuutu;
import keimo.Kenttäkohteet.Pulloautomaatti;
import keimo.Kenttäkohteet.VisuaalinenObjekti;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Ruudut.OsionAlkuRuutu;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.TarinaRuutu;
import keimo.Ruudut.ValikkoRuutu;
import keimo.Ruudut.Lisäruudut.ValintaDialogiRuutu;
import keimo.Utility.*;
import keimo.Utility.SkaalattavaKuvake.Peilaus;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

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
    static int odotusAikaUs = 10;
    public boolean säieKäynnissä = false;

    static ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
    static ArrayList<Long> päivitysAikaLista = new ArrayList<Long>();
    public static ImageIcon uusiTausta;

    static int viimeisinIkkunanLeveys = 0;
    static int viimeisinIkkunanKorkeus = 0;
    static boolean ikkunanPäivitys = false;
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");

    static double TAVOITE_FPS = PelinAsetukset.tavoiteFPS;
    static double TAVOITE_RUUDUNPÄIVITYSAIKA = 1000000000 / TAVOITE_FPS;

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
                default: PeliRuutu.minimapLabel.setIcon(new ImageIcon("tiedostot/kuvat/kartta/kartta_virhe.png")); break;
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
                if (PääIkkuna.ikkuna.getHeight() > 1044 && PääIkkuna.ikkuna.getWidth() > 1800) {
                    PeliRuutu.esineenKokoPx = 96;
                    PeliRuutu.pelaajanKokoPx = 96;
                    PeliRuutu.vasenYläPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.vasenKeskiPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.vasenAlaPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.oikeaYläPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.oikeaKeskiPaneeli.setPreferredSize(new Dimension(320, 320));
                    PeliRuutu.oikeaAlaPaneeli.setPreferredSize(new Dimension(320, 320));
                    //ImageIcon taustaSkaalaattu = new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys_kokoruutu.png");
                    KäännettäväKuvake taustaSkaalaattu = new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"), 0, false, false, 108, 1, -96, -64);
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
            case "Asuintalot", "Pelto":
                switch (Peli.huone.annaId()) {
                    case 5:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(20, 155, 20, 20);
                    break;
                    case 6:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(20, 120, 20, 20);
                    break;
                    case 7:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 120, 20, 20);
                    break;
                    case 10:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 90, 20, 20);
                    break;
                    case 11:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 65, 20, 20);
                    break;
                    case 12:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 40, 20, 20);
                    break;
                    case 24:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(140, 40, 20, 20);
                    break;
                    case 25:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(140, 10, 20, 20);
                    break;
                    case 26:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 10, 20, 20);
                    break;
                    default:
                        sijaintiMääritetty = false;
                        PeliRuutu.pelaajaKartallaLabel.setBounds(10, 10, 20, 20);
                    break;
                }
            break;
            case "Metsä":
                switch (Peli.huone.annaId()) {
                    case 27:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 125, 20, 20);
                    break;
                    case 28:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 65, 20, 20);
                    break;
                    case 29:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(140, 65, 20, 20);
                    break;
                    case 30:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(80, 15, 20, 20);
                    break;
                    case 31:
                        PeliRuutu.pelaajaKartallaLabel.setBounds(20, 65, 20, 20);
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
    void grafiikanPäivitysMetodi() {    
        try {
            TAVOITE_FPS = PelinAsetukset.tavoiteFPS;
            TAVOITE_RUUDUNPÄIVITYSAIKA = 1000000000 / TAVOITE_FPS;
            while (true) {
                alkuAika = System.nanoTime();
                suoritaGrafiikanPäivitys();
                double operaatioAika = System.nanoTime();
                while (System.nanoTime() - alkuAika < TAVOITE_RUUDUNPÄIVITYSAIKA) {
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
                    PeliRuutu.ylätekstiFPS.setText("FPS: " + neljäDesimaalia.format(1000/keskivertoPäivitysAika10Framea));
                }
                //System.out.println("grafiikkasäie: " + "operaatioaika: " + (operaatioAika - alkuAika)/1_000_000 + " ms, " + "odotusaika: " + (loppuAika - operaatioAika)/1_000_000 + " ms");
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

    public static void suoritaGrafiikanPäivitys() {
        PääIkkuna.vaatiiPäivityksen = true;
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
            if (PääIkkuna.sijaintiNäkyvissä) {
                Peli.päivitäPelaajanSijaintiTiedot();
            }
            if (DebugInfoIkkuna.ikkuna != null) {
                if (DebugInfoIkkuna.ikkuna.isVisible()) {
                    DebugInfoIkkuna.päivitäTiedot();
                }
            }
        }

        if (HuoneEditoriIkkuna.vaatiiPäivityksen) {
            HuoneEditoriIkkuna.päivitäEditoriIkkuna();
        }

        kertymänAika += aikaErotusUs;

        if (PääIkkuna.uudelleenpiirräKaikki) {
            switch (PääIkkuna.aktiivinenRuutu) {
                case PELIRUUTU:
                    PääIkkuna.pääPaneeli.removeAll();
                    PääIkkuna.pääPaneeli.add(PeliRuutu.luoPeliRuudunGUI());
                    PeliRuutu.kokoruudunTakatausta.setVisible(false);
                    PeliRuutu.latausTausta.setVisible(true);
                    ikkunanPäivitys = true;
                    PeliRuutu.alustaPeliRuutu();
                    PeliRuutu.vaihdaTausta(uusiTausta);
                    PeliRuutu.päivitäOstosPaneli();
                    PeliSäie.pelinTilanPäivitysYliajo = true;
                    PeliRuutu.latausTausta.setVisible(false);
                    PääIkkuna.uudelleenpiirräKaikki = false;
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
            PeliRuutu.alustaPeliRuutu();
            PeliRuutu.vaihdaTausta(uusiTausta);
            //PeliRuutu.skaalaaKuvakkeet = true;
            PääIkkuna.uudelleenpiirräKenttä = false;
        }
        if (PääIkkuna.uudelleenpiirräNPCt) {
            PeliRuutu.päivitäNPCKenttä();
            PeliRuutu.vaihdaTausta(uusiTausta);
            PääIkkuna.uudelleenpiirräNPCt = false;
        }

        if (PääIkkuna.uudelleenpiirräObjektit) {
            PeliRuutu.päivitäAmmusKenttä();
            PääIkkuna.uudelleenpiirräObjektit = false;
        }

        double kulunutAika = (System.nanoTime() - Peli.aikaReferenssi)/1_000_000;
        if (!Peli.ajastinPysäytetty) {
            double kulunutAikaSek = (double)kulunutAika/1000d;
            int kulunutAikaMin = (int)kulunutAikaSek / 60;
            kulunutAikaSek = kulunutAikaSek % 60;
            PeliRuutu.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
        }
    }

    @Override
    public void run() {

        frameja = 0;
        odotusAikaUs = 1_000_000 / PelinAsetukset.tavoiteFPS;

        grafiikanPäivitysMetodi();
        säieKäynnissä = true;
    }
}