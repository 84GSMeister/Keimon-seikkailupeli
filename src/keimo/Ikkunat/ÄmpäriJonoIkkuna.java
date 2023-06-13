package keimo.Ikkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.Kenttäkohteet.Vesiämpäri;
import keimo.Ruudut.PeliRuutu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class ÄmpäriJonoIkkuna {

    public static void luoÄmpäriJonoIkkuna() {
        try {
            launch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static JPanel testPanel;
    private static JProgressBar edistymisPalkki;
    private static JLabel otsikkoLabel;
    private static JLabel edistymisLabel;

    private static JPanel luoÄmpäriJonoPaneliGUI(){
        testPanel = new JPanel();
        testPanel.removeAll();
   
        edistymisPalkki = new JProgressBar(  );
        edistymisPalkki.setMinimum( 0 );
        edistymisPalkki.setMaximum( 100 );
        edistymisPalkki.setPreferredSize(new Dimension(300, 100));

        otsikkoLabel = new JLabel( "Jonotetaan ämpäriä" );
        otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 24));
        otsikkoLabel.setPreferredSize(new Dimension(300, 100));
   
        edistymisLabel = new JLabel( "Jonotetaan ämpäriä" );
        edistymisLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 20));
        edistymisLabel.setPreferredSize(new Dimension(300, 100));
   
        testPanel.setPreferredSize(new Dimension(300, 300));
        testPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        testPanel.add(otsikkoLabel, BorderLayout.NORTH);
        testPanel.add(edistymisPalkki, BorderLayout.CENTER);
        testPanel.add(edistymisLabel, BorderLayout.SOUTH);
   
        return testPanel;
    }

    static MySwingWorker worker = new MySwingWorker(edistymisPalkki, edistymisLabel);
   
    public static void launch( ) throws InvocationTargetException, InterruptedException {
        Peli.valintaDialogi = true;
        JPanel panel = luoÄmpäriJonoPaneliGUI();
        PeliRuutu.lisäRuutuPaneli.add(panel, BorderLayout.CENTER);
        PeliRuutu.lisäRuutuPaneli.setVisible(true);
        PeliRuutu.lisäRuutuPaneli.remove(PeliRuutu.pauseLabel);
        worker = new MySwingWorker(edistymisPalkki, edistymisLabel);
        worker.execute();
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
                    Peli.pause = true;
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
            edistymisLabel.setText("Jonossa sijalla..." + (ämpäriJononPituus - (int)(double)aDoubles.get( aDoubles.size() - 1 )));
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
                Peli.pause = false;
                Peli.valintaDialogi = false;
                PeliRuutu.lisäRuutuPaneli.removeAll();
                PeliRuutu.lisäRuutuPaneli.revalidate();
                PeliRuutu.lisäRuutuPaneli.repaint();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
                if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                    Pelaaja.annaEsine(new Vesiämpäri(false, 0, 0));
                    PääIkkuna.avaaDialogi(new ImageIcon("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png"), "Sait uuden esineen: Vesiämpäri", "");
                }
                else {
                    PääIkkuna.avaaDialogi(null, "Menit jonottamaan ämpäriä, vaikka sinulla ei ole tilaa tavaraluettelossa. Miltä nyt tuntuu?", "");
                }
            }
            catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            catch ( ExecutionException e ) {
                e.printStackTrace();
            }
        }
    }
}
