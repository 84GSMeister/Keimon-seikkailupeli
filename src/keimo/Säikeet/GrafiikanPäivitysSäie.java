package keimo.Säikeet;

import keimo.*;
import keimo.HuoneEditori.*;
import keimo.Utility.*;
import keimo.Utility.SkaalattavaKuvake.Peilaus;

import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;
import javax.swing.Timer;
import javax.swing.text.StyledEditorKit;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import javax.swing.*;
import java.awt.Color;
import java.awt.Image;
import java.text.DecimalFormat;

public class GrafiikanPäivitysSäie extends Thread {

    //public static long aikaReferenssi = System.nanoTime();
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
    static boolean säieKäynnissä = false;

    static ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
    static ArrayList<Long> päivitysAikaLista = new ArrayList<Long>();
    public static ImageIcon uusiTausta;

    //AjastimenPäivittäjä ajastimenPäivittäjä = new AjastimenPäivittäjä();
    PeliKentänPäivittäjä peliKentänPäivittäjä = new PeliKentänPäivittäjä();

    public static void päivitäPelaajanKuvake() {
        
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
        //System.out.println("" + Pelaaja.keimonState + ", " + Pelaaja.keimonTerveys + ", " + Pelaaja.keimonKylläisyys + ", " + Pelaaja.kuvake);
    }

    public static void päivitäHUD() {
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            if (Pelaaja.esineet[i] == null) {
                PääIkkuna.esineLabel[i].setText(null);
                PääIkkuna.esineLabel[i].setIcon(null);
            }
            else {
                PääIkkuna.esineLabel[i].setIcon(Pelaaja.esineet[i].annaKuvake());
            }
        }
        if (Peli.valittuEsine == null) {
            PääIkkuna.valitunEsineenNimiLabel.setText("");
        }
        else {
            PääIkkuna.valitunEsineenNimiLabel.setText(Peli.valittuEsine.annaNimi());
        }
    }

    public static void skaalaaHUD() {
        if (PääIkkuna.ikkuna !=null && PääIkkuna.tavaraPaneli != null && PääIkkuna.kontrolliInfoPaneli != null) {
            if (PääIkkuna.ikkuna.getHeight() < 750) {
                PääIkkuna.yläPaneeli.setVisible(false);
                PääIkkuna.alaPaneeli.setVisible(false);
            }
            else if (PääIkkuna.ikkuna.getHeight() < 768) {
                PääIkkuna.yläPaneeli.setVisible(false);
                PääIkkuna.alaPaneeli.setVisible(true);
            }
            else {
                PääIkkuna.yläPaneeli.setVisible(true);
                PääIkkuna.alaPaneeli.setVisible(true);
            }
            if (PääIkkuna.ikkuna.getHeight() < 740 || PääIkkuna.ikkuna.getWidth() < 1280) {
                PääIkkuna.kokoruudunTakatausta.setVisible(true);
            }
            else {
                PääIkkuna.kokoruudunTakatausta.setVisible(false);
            }
            PääIkkuna.peliKenttäUlompi.setBackground(Color.BLACK);
        }
    }

    public static void odotaMikrosekunteja(long mikrosekunnit){
        
        long odotaKunnes = (System.nanoTime() + (mikrosekunnit/2 * 1_000));
        
        //while(odotaKunnes > System.nanoTime()){
        //    ;
        //}
        LockSupport.parkNanos(odotaKunnes - System.nanoTime());
    }

    

    final JPanel panel = new JPanel();
    class PeliKentänPäivittäjä extends Thread {
        @Override
        public void run() {
            //if (!Main.pause) {
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
                        //long hudAika = System.nanoTime();
                        päivitäHUD();
                        skaalaaHUD();
                        päivitäPelaajanKuvake();
                        //long ikkunaAika = System.nanoTime();
                        PääIkkuna.päivitäIkkuna();
                        //long päivitysAika = System.nanoTime();
                        
                        //System.out.println("HUD: " + (ikkunaAika-hudAika)/1_000_000 + " ms, " + "Ikkuna: " + (päivitysAika-ikkunaAika)/1_000_000 + " ms, " + "Ajanmittaus: " + (ajanMittausAika-päivitysAika)/1_000_000 + " ms");
                        //SwingUtilities.invokeLater(new PääIkkunanPäivittäjä());
                        

                        if (HuoneEditoriIkkuna.vaatiiPäivityksen) {
                            HuoneEditoriIkkuna.päivitäEditoriIkkuna();
                        }

                        kertymänAika += aikaErotusUs;

                        if (PääIkkuna.uudelleenpiirräKaikki) {
                            PääIkkuna.luoAlkuIkkuna(Pelaaja.sijX, Pelaaja.sijY, new ImageIcon("tiedostot/kuvat/pelaaja.png"));
                            PääIkkuna.vaihdaTausta(uusiTausta);
                            PääIkkuna.uudelleenpiirräKaikki = false;
                        }
                        if (PääIkkuna.uudelleenpiirräKenttä) {
                            PääIkkuna.päivitäNPCKenttä();
                            PääIkkuna.vaihdaTausta(uusiTausta);
                            PääIkkuna.uudelleenpiirräKenttä = false;
                        }

                        //long ajanMittausAika = System.nanoTime();
                        //odotaMikrosekunteja(odotusAikaUs - (ajanMittausAika-hudAika)/1000);

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
                                Thread.sleep(1);
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
                        //double loppuAika = System.nanoTime();
                        //double aikaErotusNs = loppuAika - alkuAika;
                        //double aikaErotusMs = (aikaErotusNs/1_000_100f);
                        //System.out.println(1000/((System.nanoTime()- alkuAika)/1_000_000));
                        //System.out.println("frame ajassa " + aikaErotusMs + ", framerate: " + (1000/aikaErotusMs) + ", framet: " + frameja);
                    }
                }
                    
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Paskan möivät.\n\nKannattais varmaan koodata paremmin.", "vittu", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            //}

            //publish(PääIkkuna.päivitäIkkuna());
        }

        //@Override
        //protected void process(List<PääIkkuna.KenttäTakataustalla> panelit) {
            //PääIkkuna.peliKenttä = 
        //}
    }

    // boolean ajastinKäynnissä = true;
    // public static double kulunutAika = 0;
    // int kulunutAikaMin = 0;
    // double kulunutAikaSek = 0;
    // DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    public void päivitäAjastin(long aiemminKulunutAika) {
        
        long odotaKunnes = System.nanoTime() + (10 * 1_000_000) - aiemminKulunutAika;
        
        // kulunutAika += 0.01;
        // kulunutAikaSek = kulunutAika % 60;
        // kulunutAikaMin = (int)kulunutAika / 60;

        //PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
        
        while(odotaKunnes > System.nanoTime()){
            ;
        }
        //LockSupport.parkNanos(odotaKunnes - System.nanoTime());
    }

    // public static class PääIkkunanPäivittäjä implements Runnable {
    //     public void run() {
    //         päivitäHUD();
    //         skaalaaHUD();
    //         päivitäPelaajanKuvake();
    //         PääIkkuna.päivitäIkkuna();
    //     }
    // }

    // public class AjastimenPäivittäjä extends SwingWorker<Void, JLabel> {
        
    //     boolean ajastinKäynnissä = true;
    //     public static double kulunutAika = 0;
    //     int kulunutAikaMin = 0;
    //     double kulunutAikaSek = 0;
    
    //     DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    //     @Override
    //     protected Void doInBackground() {
        
    //         while (!isCancelled()) {
    //             if (!Peli.pause && Peli.peliAloitettu) {
    //                 //odotaMillisekunteja(10);
    //                 //publish(PääIkkuna.ylätekstiAika);
    //             }
    //         }
    //         return null;
    //     }

    //     protected void process(JLabel label) {
    //         label = PääIkkuna.ylätekstiAika;
    //         //PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
    //     }

    //     public void odotaMillisekunteja(long millisekunnit) {
        
    //         long odotaKunnes = System.nanoTime() + (millisekunnit * 1_000_000);
            
    //         kulunutAika += 0.01;
    //         kulunutAikaSek = kulunutAika % 60;
    //         kulunutAikaMin = (int)kulunutAika / 60;
    
    //         PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
            
    //         while(odotaKunnes > System.nanoTime()){
    //             ;
    //         }
    //         //LockSupport.parkNanos(odotaKunnes - System.nanoTime());
    //     }
    // }

    @Override
    public void run() {

        frameja = 0;
        odotusAikaUs = 1_000_000 / PelinAsetukset.tavoiteFPS;

        

        //while (!ongelmaGrafiikassa) {
            //(new PeliKentänPäivittäjä()).execute();
            //(new AjastimenPäivittäjä()).execute();
            
        if (!säieKäynnissä) {
            //ajastimenPäivittäjä.execute();
            peliKentänPäivittäjä.setName("Pelikentän päivittäjä");
            peliKentänPäivittäjä.run();
        }

        säieKäynnissä = true;

            /**
            alkuAika = System.nanoTime();
            odotaMikrosekunteja(odotusAikaUs);

            PääIkkuna.vaatiiPäivityksen = true;
            valitsePelaajanKuvake();
            päivitäHUD();
            PääIkkuna.päivitäIkkuna();

            kertymänAika += aikaErotusUs;

            if (PääIkkuna.uudelleenpiirräKaikki) {
                PääIkkuna.luoAlkuIkkuna(Main.pelaajanSijX, Main.pelaajanSijY, new ImageIcon("tiedostot/kuvat/pelaaja.png"));
                PääIkkuna.vaihdaTausta(uusiTausta);
                valitsePelaajanKuvake();
                PääIkkuna.uudelleenpiirräKaikki = false;
            }
            frameja++;
            kuviaKertymässä++;
            loppuAika = System.nanoTime();
            aikaErotusNs = (loppuAika - alkuAika);
            aikaErotusUs = aikaErotusNs/1000;
            aikaErotusMs = aikaErotusUs/1000;
            */
        //}
    }
}