import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import javax.swing.*;
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

    static ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
    static ArrayList<Long> päivitysAikaLista = new ArrayList<Long>();
    static ImageIcon uusiTausta;

    AjastimenPäivittäjä ajastimenPäivittäjä = new AjastimenPäivittäjä();
    PeliKentänPäivittäjä peliKentänPäivittäjä = new PeliKentänPäivittäjä();

    static ImageIcon valitsePelaajanKuvake() {
        int pelaajanKuvakeInt = Main.pelaajanKylläisyys;
        switch (pelaajanKuvakeInt) {
            case 0:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
            case 1:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_1.png");
                break;
            case 2:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_2.png");
                break;
            case 3:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_3.png");
                break;
            case 4:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_4.png");
                break;
            default:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
        }
        return pelaajanKuvake;
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
    });

    final JPanel panel = new JPanel();
    class PeliKentänPäivittäjä extends SwingWorker<Void, JPanel> {
        @Override
        protected Void doInBackground() {
        
            try {
                while (!isCancelled()) {
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
                }
            }
                
            catch (Exception ignore) {
                JOptionPane.showMessageDialog(null, "Paskan möivät.\n\nKannattais varmaan koodata paremmin.", "vittu", JOptionPane.ERROR_MESSAGE);
            }

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
        double kulunutAika = 0;
        int kulunutAikaMin = 0;
        double kulunutAikaSek = 0;
    
        DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

        @Override
        protected Void doInBackground() {
        
            while (!isCancelled()) {
                odotaMillisekunteja(10);
                publish(PääIkkuna.ylätekstiAika);
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
        // TODO Auto-generated method stub
        //super.run();
        frameja = 0;
        odotusAikaUs = 1_000_000 / PelinAsetukset.tavoiteFPS;
        päivitysTiheys.start();
        sekuntiKello.start();

        

        //while (!ongelmaGrafiikassa) {
            //(new PeliKentänPäivittäjä()).execute();
            //(new AjastimenPäivittäjä()).execute();
            
            ajastimenPäivittäjä.execute();
            peliKentänPäivittäjä.execute();
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