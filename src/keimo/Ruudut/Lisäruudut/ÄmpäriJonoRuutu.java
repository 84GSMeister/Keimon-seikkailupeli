package keimo.Ruudut.Lisäruudut;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.Kenttäkohteet.Vesiämpäri;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.PeliRuutu.LisäRuutuPanelinTyyppi;
import keimo.Utility.KeimoFontit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class ÄmpäriJonoRuutu {

    public static void luoÄmpäriJonoIkkuna() {
        try {
            launch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static JPanel testPanel;
    private static JLabel otsikkoLabel;
    private static JProgressBar edistymisPalkki;
    private static JLabel jonoSijoitusTekstiLabel;
    private static JLabel jonoSijoitusNumeroLabel;
    private static JPanel edistymisPaneli;
    private static JLabel ohjeLabel;

    private static JPanel luoÄmpäriJonoPaneliGUI(){
        testPanel = new JPanel(new BorderLayout());
        testPanel.removeAll();

        otsikkoLabel = new JLabel("Jonotetaan ämpäriä");
        otsikkoLabel.setFont(KeimoFontit.fontti_keimo_20);
        otsikkoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        otsikkoLabel.setPreferredSize(new Dimension(300, 80));
   
        edistymisPalkki = new JProgressBar();
        edistymisPalkki.setMinimum(0);
        edistymisPalkki.setMaximum(100);
        edistymisPalkki.setPreferredSize(new Dimension(300, 80));
   
        jonoSijoitusTekstiLabel = new JLabel("Jonossa sijalla");
        jonoSijoitusTekstiLabel.setFont(KeimoFontit.fontti_keimo_20);
        jonoSijoitusTekstiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jonoSijoitusTekstiLabel.setPreferredSize(new Dimension(300, 30));

        jonoSijoitusNumeroLabel = new JLabel("sija");
        jonoSijoitusNumeroLabel.setFont(KeimoFontit.fontti_keimo_20);
        jonoSijoitusNumeroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jonoSijoitusNumeroLabel.setPreferredSize(new Dimension(300, 30));

        edistymisPaneli = new JPanel(new GridLayout(3, 1));
        edistymisPaneli.add(edistymisPalkki, BorderLayout.NORTH);
        edistymisPaneli.add(jonoSijoitusTekstiLabel, BorderLayout.CENTER);
        edistymisPaneli.add(jonoSijoitusNumeroLabel, BorderLayout.SOUTH);
        edistymisPaneli.setPreferredSize(new Dimension(300, 180));
        edistymisPaneli.setBorder(BorderFactory.createLineBorder(Color.black));

        ohjeLabel = new JLabel("<html>Space: Poistu jonosta</html>");
        ohjeLabel.setFont(KeimoFontit.fontti_keimo_20);
        ohjeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ohjeLabel.setPreferredSize(new Dimension(300, 80));
   
        testPanel.setPreferredSize(new Dimension(300, 400));
        testPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        testPanel.add(otsikkoLabel, BorderLayout.NORTH);
        testPanel.add(edistymisPaneli, BorderLayout.CENTER);
        testPanel.add(ohjeLabel, BorderLayout.SOUTH);
   
        return testPanel;
    }

    static MySwingWorker worker = new MySwingWorker(edistymisPalkki, jonoSijoitusNumeroLabel);
   
    public static void launch() throws InvocationTargetException, InterruptedException {
        Peli.valintaDialogi = true;
        ValintaDialogiRuutu.muutaPanelinKokoa(false, PääIkkuna.isoSkaalaus);
        PeliRuutu.lisäRuutuPanelinTyyppi = LisäRuutuPanelinTyyppi.ÄMPÄRIJONO;
        JPanel panel = luoÄmpäriJonoPaneliGUI();
        PeliRuutu.lisäRuutuPaneli.add(panel, BorderLayout.CENTER);
        PeliRuutu.lisäRuutuPaneli.setVisible(true);
        PeliRuutu.lisäRuutuPaneli.remove(PeliRuutu.pauseLabel);
        worker = new MySwingWorker(edistymisPalkki, jonoSijoitusNumeroLabel);
        worker.execute();
    }

    public static void poistuÄmpärijonosta() {
        worker.cancel(true);
        PeliRuutu.lisäRuutuPaneli.removeAll();
        PeliRuutu.lisäRuutuPaneli.revalidate();
        PeliRuutu.lisäRuutuPaneli.repaint();
        PeliRuutu.lisäRuutuPaneli.setVisible(false);
        Peli.pauseDialogi = false;
        Peli.valintaDialogi = false;
        PääIkkuna.avaaDialogi(new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png"), "Ei kiinosta  virhe", "Keimo");
    }

    static Random r = new Random();
    static int ämpäriJononPituus = r.nextInt(1000, 4000);

    private static class MySwingWorker extends SwingWorker<String, Double> {
        private final JProgressBar fProgressBar;
        private final JLabel fLabel;
        private MySwingWorker( JProgressBar aProgressBar, JLabel aLabel ) {
            fProgressBar = aProgressBar;
            fLabel = aLabel;
        }
      
        @Override
        protected String doInBackground() throws Exception {
            
            ämpäriJononPituus = r.nextInt(1000, 4000);
            String[] ämpäriJono = new String[ämpäriJononPituus];
            int toimivatMjonot = 0;
            edistymisPalkki.setMaximum(ämpäriJononPituus);
        
            try {
                for (int i = 0; i < ämpäriJononPituus; i++) {
                    Peli.pauseDialogi = true;
                    testPanel.revalidate();
                    testPanel.repaint();
                    publish((double)i);
                    Thread.sleep(1);
                    if (ämpäriJono[i] == null) {
                        //System.out.println("Jonossa sijalla: " + (ämpäriJononPituus-i));
                        continue;
                    }
                    else {
                        ämpäriJono[toimivatMjonot] = "";//Peli.huoneKartta.get(i).annaNimi() + " (" + Peli.huoneKartta.get(i).annaId() + ")";
                        //toimivatHuoneIndeksit.add(i);
                        toimivatMjonot++;
                        //System.out.println("Jonossa sijalla: " + (ämpäriJononPituus-i) + " @ ");
                    }
                }
                //huoneValikko = new JComboBox<String>(huoneidenNimet);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Virheellinen huonelista.\n\nHäire sovelluksessa.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e) {
                System.out.println("Ohitetaan tyhjät indeksit");
            }
            return "Ladattu";
        }
      
        @Override
        protected void process(List<Double> aDoubles) {
            //update the percentage of the progress bar that is done
            int amount = fProgressBar.getMaximum() - fProgressBar.getMinimum();
            fProgressBar.setValue( ( int ) (fProgressBar.getMinimum() + ( amount * aDoubles.get( aDoubles.size() - 1 ))) );
            //System.out.println(( int ) (fProgressBar.getMinimum() + ( amount * aDoubles.get( aDoubles.size() - 1 ))));
            //System.out.println(aDoubles.get( aDoubles.size() - 1 ));
            //OUTPUT_LABEL.setText("Jonossa sijalla..." + (int)(double)aDoubles.get( aDoubles.size() - 1 ) + " / " + ämpäriJononPituus);
            jonoSijoitusNumeroLabel.setText("" + (ämpäriJononPituus - (int)(double)aDoubles.get( aDoubles.size() - 1 )));
            //System.out.println("Jonossa sijalla..." + (ämpäriJononPituus - (int)(double)aDoubles.get( aDoubles.size() - 1 )));
            fProgressBar.setValue((int)(double)aDoubles.get( aDoubles.size() - 1 ));
            PeliRuutu.lisäRuutuPaneli.setVisible(true);
        }
      
        @Override
        protected void done() {
            try {
                fLabel.setText( get() );
                fProgressBar.setValue(0);
                worker.cancel(true);
                Peli.pauseDialogi = false;
                Peli.valintaDialogi = false;
                PeliRuutu.lisäRuutuPaneli.removeAll();
                PeliRuutu.lisäRuutuPaneli.revalidate();
                PeliRuutu.lisäRuutuPaneli.repaint();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
                if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                    Pelaaja.annaEsine(new Vesiämpäri(false, 0, 0));
                    PääIkkuna.avaaDialogi(new ImageIcon("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png"), "Sait uuden esineen: Vesiämpäri", "Uusi esine");
                }
                else {
                    PääIkkuna.avaaDialogi(new ImageIcon("tiedostot/kuvat/surunaama.png"), "Menit jonottamaan ämpäriä, vaikka sinulla ei ole tilaa tavaraluettelossa. Miltä nyt tuntuu?", "Jonotus epäonnistui");
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
            }
            catch (ExecutionException e) {
                e.printStackTrace();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
            }
            catch (CancellationException ce) {
                ce.printStackTrace();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
            }
        }
    }
}
