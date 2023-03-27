package keimo;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.*;
import java.text.DecimalFormat;

public class GrafiikanPäivitysSäie extends Thread {

    static long alkuAika = 0;
    static long loppuAika = 0;
    static long aikaErotusNs = 0;
    static long aikaErotusUs = 0;
    static long aikaErotusMs = 0;
    static double fpsFloat = 0f;
    static long fps = 0;
    static long kertymänAika = 0;
    static long kuviaKertymässä = 0;
    static long frameja = 0;
    static boolean ongelmaGrafiikassa = false;
    static int odotusAikaUs = 10;
    static boolean säieKäynnissä = false;

    static ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
    static ArrayList<Long> päivitysAikaLista = new ArrayList<Long>();
    static ImageIcon uusiTausta;

    AjastimenPäivittäjä ajastimenPäivittäjä = new AjastimenPäivittäjä();
    PeliKentänPäivittäjä peliKentänPäivittäjä = new PeliKentänPäivittäjä();

    public static void päivitäPelaajanKuvake() {
        
        switch (Pelaaja.keimonState) {
            case IDLE: 
                switch (Pelaaja.keimonSuuntaVasenOikea) {
                    case VASEN:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_vasena_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                        }
                    break;
                    case OIKEA:
                        switch (Pelaaja.keimonTerveys) {
                            case HYVÄ:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_hyvä_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_neutraali_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_idle_huono_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/idle/pelaaja_ylensyönti.png"); break;
                                }
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
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_vasen_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
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
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_hyvä_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case OK:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
                            break;
                            case HUONO:
                                switch (Pelaaja.keimonKylläisyys) {
                                    case LAIHA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_laiha.gif"); break;
                                    case NORMAALI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_normaali.gif"); break;
                                    case LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_lihava.gif"); break;
                                    case ERITTÄIN_LIHAVA: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/kävely/kävely_oikea_erittäinlihava.gif"); break;
                                    case YLENSYÖNTI: Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_ylensyönti.png"); break;
                                }
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
                            }
                    break;
                }
            break;
            case KUOLLUT:
                Pelaaja.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja/pelaaja_Kuollut.png");
            break;
        }
        if (Pelaaja.kuolemattomuusAika > 0) {
            ImageIcon vilkkuvaKuvake = Pelaaja.kuvake;
            if (Peli.globaaliAika % 2 == 0) {
                Pelaaja.kuvake = vilkkuvaKuvake;
            }
            else {
                Pelaaja.kuvake = null;
            }
        }
        //System.out.println("" + Pelaaja.keimonState + ", " + Pelaaja.keimonTerveys + ", " + Pelaaja.keimonKylläisyys + ", " + Pelaaja.kuvake);
    }

    public static void päivitäHUD() {
        for (int i = 0; i < 5; i++) {
            if (Pelaaja.esineet[i] == null) {
                PääIkkuna.esineLabel[i].setText(null);
                PääIkkuna.esineLabel[i].setIcon(null);
            }
            else {
                PääIkkuna.esineLabel[i].setIcon(Pelaaja.esineet[i].annaKuvake());
            }
        }
    }

    public static void skaalaaHUD() {
        if (PääIkkuna.ikkuna.getWidth() <= PääIkkuna.tavaraPaneli.getWidth() + PääIkkuna.infoPaneli.getWidth() + 25) {
            PääIkkuna.infoPaneli.setVisible(false);
        }
        else {
            PääIkkuna.infoPaneli.setVisible(true);
        }
        if (PääIkkuna.ikkuna.getHeight() <= 960) {
            PääIkkuna.yläPaneeli.setVisible(false);
            if (PääIkkuna.ikkuna.getHeight() <= 900) {
                PääIkkuna.alueInfoPaneli.setVisible(false);
            }
            else {
                PääIkkuna.alueInfoPaneli.setVisible(true);
            }
        }
        else {
            PääIkkuna.yläPaneeli.setVisible(true);
        }
        // ImageIcon takataustaKuvake = new ImageIcon();
        // Image kuvake64 = takataustaKuvake.getImage();
        // Image kuvake32 = kuvake64.getScaledInstance(PääIkkuna.ikkuna.getWidth(), PääIkkuna.ikkuna.getHeight(), Image.SCALE_SMOOTH);
        // takataustaKuvake = new ImageIcon(kuvake32);
        PääIkkuna.peliKenttäUlompi.setBackground(Color.BLACK);
    }

    public static void odotaMikrosekunteja(long mikrosekunnit){
        
        long odotaKunnes = System.nanoTime() + (mikrosekunnit * 1_000);
        
        //while(odotaKunnes > System.nanoTime()){
        //    ;
        //}
        LockSupport.parkNanos(odotaKunnes - System.nanoTime());
    }

    Timer päivitysTiheys = new Timer(16, e -> {
        PääIkkuna.ylätekstiViive.setText("Päivitysaika: " + aikaErotusMs + " ms, kuvia: " + frameja);
    });

    Timer sekuntiKello = new Timer(50, e -> {
        fpsFloat = kuviaKertymässä * 1_000_000f / kertymänAika;
        fps = (long)fpsFloat;
        PääIkkuna.ylätekstiFPS.setText("FPS: " + fps);
        //System.out.println("Kuvia kertymässä: " + kuviaKertymässä + ", kertymän aika: " + kertymänAika/1000 + " ms, fps: " + fps);
        kertymänAika = 0;
        kuviaKertymässä = 0;
        //Huone huone = Main.huoneKartta.get(Main);
        //PääIkkuna.alueInfoLabel.setText(Main.huoneKartta.get(Main.uusiHuone));
    });

    final JPanel panel = new JPanel();
    class PeliKentänPäivittäjä extends SwingWorker<Void, JPanel> {
        @Override
        protected Void doInBackground() {
        
            //if (!Main.pause) {
                try {
                    while (!isCancelled()) {
                        alkuAika = System.nanoTime();
                        odotaMikrosekunteja(odotusAikaUs);

                        PääIkkuna.vaatiiPäivityksen = true;
                        päivitäHUD();
                        skaalaaHUD();
                        päivitäPelaajanKuvake();
                        //System.out.println((int)Pelaaja.hitbox.getMinX() + " " + Pelaaja.sijX * PääIkkuna.pelaajanKokoPx + " " + (int)Pelaaja.hitbox.getMinY() + " " + Pelaaja.sijY * PääIkkuna.pelaajanKokoPx);
                        PääIkkuna.päivitäIkkuna();
                        if (HuoneEditoriIkkuna.vaatiiPäivityksen) {
                            HuoneEditoriIkkuna.päivitäEditoriIkkuna();
                        }

                        kertymänAika += aikaErotusUs;

                        if (PääIkkuna.uudelleenpiirräKaikki) {
                            PääIkkuna.luoAlkuIkkuna(Pelaaja.sijX, Pelaaja.sijY, new ImageIcon("tiedostot/kuvat/pelaaja.png"));
                            PääIkkuna.vaihdaTausta(uusiTausta);
                            PääIkkuna.uudelleenpiirräKaikki = false;
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
                    JOptionPane.showMessageDialog(null, "Paskan möivät.\n\nKannattais varmaan koodata paremmin.", "vittu", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            //}

            //publish(PääIkkuna.päivitäIkkuna());
            return null;
        }

        //@Override
        //protected void process(List<PääIkkuna.KenttäTakataustalla> panelit) {
            //PääIkkuna.peliKenttä = 
        //}
    }

    class AjastimenPäivittäjä extends SwingWorker<Void, JLabel> {
        
        boolean ajastinKäynnissä = true;
        static double kulunutAika = 0;
        int kulunutAikaMin = 0;
        double kulunutAikaSek = 0;
    
        DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

        @Override
        protected Void doInBackground() {
        
            while (!isCancelled()) {
                if (!Peli.pause && Peli.peliAloitettu) {
                    odotaMillisekunteja(10);
                    publish(PääIkkuna.ylätekstiAika);
                }
            }
            return null;
        }

        protected void process(JLabel label) {
            label = PääIkkuna.ylätekstiAika;
            //PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
        }

        public void odotaMillisekunteja(long millisekunnit) {
        
            long odotaKunnes = System.nanoTime() + (millisekunnit * 1_000_000);
            
            kulunutAika += 0.01;
            kulunutAikaSek = kulunutAika % 60;
            kulunutAikaMin = (int)kulunutAika / 60;
    
            PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
            
            while(odotaKunnes > System.nanoTime()){
                ;
            }
            //LockSupport.parkNanos(odotaKunnes - System.nanoTime());
        }
    }

    @Override
    public void run() {

        frameja = 0;
        odotusAikaUs = 1_000_000 / PelinAsetukset.tavoiteFPS;
        päivitysTiheys.start();
        sekuntiKello.start();

        

        //while (!ongelmaGrafiikassa) {
            //(new PeliKentänPäivittäjä()).execute();
            //(new AjastimenPäivittäjä()).execute();
            
        if (!säieKäynnissä) {
            ajastimenPäivittäjä.execute();
            peliKentänPäivittäjä.execute();
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