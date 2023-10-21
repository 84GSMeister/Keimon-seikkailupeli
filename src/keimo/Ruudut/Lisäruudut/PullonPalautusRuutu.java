package keimo.Ruudut.Lisäruudut;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.Kenttäkohteet.Pulloautomaatti;
import keimo.Kenttäkohteet.Pulloautomaatti.PulloautomaatinKuvake;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.PeliRuutu.LisäRuutuPanelinTyyppi;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class PullonPalautusRuutu {
    
    private static Random r = new Random();
    public static boolean jatkoSyöteAnnettu = false;
    public static Pulloautomaatti pulloautomaatti = null;
    public static VirheenTyyppi virheenTyyppi = VirheenTyyppi.PAKKAUS;

    public static void luoPullonPalautusIkkuna() {
        try {
            if (Pelaaja.kuparit > 0) {
                launch();
            }
            else {
                PääIkkuna.avaaDialogi(null, "Sinulla ei ole yhtään tölkkiä.", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum VirheenTyyppi {
        PAKKAUS,
        MUOTO,
        KÄSI,
        MERKKI;
    }
    
    static JPanel testPanel;
    private static JProgressBar edistymisPalkki;
    private static JLabel otsikkoLabel;
    private static JLabel edistymisLabel;

    private static JPanel luoPullonPalautusPaneliGUI(){
        testPanel = new JPanel();
        testPanel.removeAll();
   
        edistymisPalkki = new JProgressBar();
        edistymisPalkki.setMinimum(0);
        edistymisPalkki.setMaximum(100);
        edistymisPalkki.setPreferredSize(new Dimension(300, 100));

        otsikkoLabel = new JLabel("Palautetaan tölkkejä");
        otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 24));
        otsikkoLabel.setPreferredSize(new Dimension(300, 100));
   
        edistymisLabel = new JLabel("Palautetaan tölkkejä");
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
   
    public static void launch() throws InvocationTargetException, InterruptedException {
        
        Peli.valintaDialogi = true;
        PeliRuutu.lisäRuutuPanelinTyyppi = LisäRuutuPanelinTyyppi.PULLONPALAUTUS;
        if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Pulloautomaatti) {
            pulloautomaatti = (Pulloautomaatti)Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            pulloautomaatti.valitseKuvake(PulloautomaatinKuvake.AKTIIVINEN);
        }
        
        JPanel panel = luoPullonPalautusPaneliGUI();
        PeliRuutu.lisäRuutuPaneli.add(panel, BorderLayout.CENTER);
        PeliRuutu.lisäRuutuPaneli.setVisible(true);
        PeliRuutu.lisäRuutuPaneli.remove(PeliRuutu.pauseLabel);
        worker = new MySwingWorker(edistymisPalkki, edistymisLabel);
        worker.execute();
    }
    static int pullonPalautuksenPituus = Pelaaja.kuparit;

    private static class MySwingWorker extends SwingWorker<String, Double> {
        private final JProgressBar fProgressBar;
        private final JLabel fLabel;
        private MySwingWorker( JProgressBar aProgressBar, JLabel aLabel ) {
            fProgressBar = aProgressBar;
            fLabel = aLabel;
        }
      
        @Override
        protected String doInBackground() throws Exception {
            
            pullonPalautuksenPituus = Pelaaja.kuparit;
            String[] pullonPalautusJono = new String[pullonPalautuksenPituus];
            int toimivatMjonot = 0;
            edistymisPalkki.setMaximum(pullonPalautuksenPituus);
            try {
                for (int i = 0; i < pullonPalautuksenPituus; i++) {
                    Peli.pauseDialogi = true;
                    int virhe = r.nextInt(0, 10);
                    jatkoSyöteAnnettu = false;
                    switch (virhe) {
                        case 0:
                            pulloautomaatti.tila = PulloautomaatinKuvake.VIRHE;
                            virheenTyyppi = VirheenTyyppi.PAKKAUS;
                            edistymisLabel.setText("<html><p>Poista pakkaus ja yritä uudelleen tai hävitä se muuten.<br>(Paina Space)</p></html>");
                            while (!jatkoSyöteAnnettu) {
                                Thread.sleep(10);
                                Peli.pauseDialogi = true;
                            }
                            pulloautomaatti.tila = PulloautomaatinKuvake.AKTIIVINEN;
                        break;
                        case 1:
                            pulloautomaatti.tila = PulloautomaatinKuvake.VIRHE;
                            virheenTyyppi = VirheenTyyppi.MUOTO;
                            edistymisLabel.setText("<html><p>Palauta pakkaus alkuperäiseen muotoon ja yritä uudelleen.<br>(Paina X)</p></html>");
                            while (!jatkoSyöteAnnettu) {
                                Thread.sleep(10);
                                Peli.pauseDialogi = true;
                            }
                            pulloautomaatti.tila = PulloautomaatinKuvake.AKTIIVINEN;
                        break;
                        case 2:
                            pulloautomaatti.tila = PulloautomaatinKuvake.VIRHE;
                            virheenTyyppi = VirheenTyyppi.KÄSI;
                            edistymisLabel.setText("<html><p>Älä työnnä kättä automaattiin!<br>(Paina C)</p></html>");
                            while (!jatkoSyöteAnnettu) {
                                Thread.sleep(10);
                                Peli.pauseDialogi = true;
                            }
                            pulloautomaatti.tila = PulloautomaatinKuvake.AKTIIVINEN;
                        break;
                        case 3:
                            pulloautomaatti.tila = PulloautomaatinKuvake.VIRHE;
                            virheenTyyppi = VirheenTyyppi.MERKKI;
                            edistymisLabel.setText("<html><p>Kauppa ei hyväksy tätä merkkiä!<br>(Paina Z)</p></html>");
                            while (!jatkoSyöteAnnettu) {
                                Thread.sleep(10);
                                Peli.pauseDialogi = true;
                            }
                            pulloautomaatti.tila = PulloautomaatinKuvake.AKTIIVINEN;
                        break;
                        default:
                        break;
                    }
                    testPanel.revalidate();
                    testPanel.repaint();
                    publish((double)i);
                    Thread.sleep(400);
                    if (pullonPalautusJono[i] == null) {
                        continue;
                    }
                    else {
                        pullonPalautusJono[toimivatMjonot] = "";
                        toimivatMjonot++;
                    }
                }
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
            edistymisLabel.setText("Tölkkejä jäljellä..." + (pullonPalautuksenPituus - (int)(double)aDoubles.get( aDoubles.size() - 1 )));
            fProgressBar.setValue((int)(double)aDoubles.get( aDoubles.size() - 1 ));
            PeliRuutu.lisäRuutuPaneli.setVisible(true);
        }
      
        @Override
        protected void done() {
            try {
                fLabel.setText( get() );
                fProgressBar.setValue(0);
                worker.cancel(true);
                pulloautomaatti.tila = PulloautomaatinKuvake.IDLE;
                Peli.pauseDialogi = false;
                Peli.valintaDialogi = false;
                PeliRuutu.lisäRuutuPaneli.removeAll();
                PeliRuutu.lisäRuutuPaneli.revalidate();
                PeliRuutu.lisäRuutuPaneli.repaint();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
                
                float saatavaRaha = 0.15f * Pelaaja.kuparit;
                if (saatavaRaha > 0) {
                    Pelaaja.raha += saatavaRaha;
                    Pelaaja.kuparit = 0;
                    DecimalFormat df = new DecimalFormat("##.##");
                    String saatavaRahaFormatoitu = df.format(saatavaRaha);
                    PääIkkuna.avaaDialogi(null, "Palautit tölkit kauppaan ja sait " + saatavaRahaFormatoitu + "€.", saatavaRahaFormatoitu);
                }
                else {
                    PääIkkuna.avaaDialogi(null, "Sinulla ei ole yhtään tölkkiä.", "");
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
