package keimo.HuoneEditori;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.*;

public class EditorinLatausIkkuna {
    
    private static JFrame ikkuna;
    private static JProgressBar edistymisPalkki;
    private static JLabel edistymisTeksti;
    protected static int edistyminen = 50;

    private static JFrame createGUI(){
        ikkuna = new JFrame("Ladataan editoria");
   
        edistymisPalkki = new JProgressBar();
        edistymisPalkki.setMinimum(0);
        edistymisPalkki.setMaximum(100);
   
        edistymisTeksti = new JLabel("Ladataan editoria");
   
        ikkuna.getContentPane().add(edistymisPalkki, BorderLayout.CENTER);
        ikkuna.getContentPane().add(edistymisTeksti, BorderLayout.SOUTH);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setPreferredSize(new Dimension(300, 80));
        ikkuna.setAlwaysOnTop(true);
        ikkuna.setLocationRelativeTo(null);
   
        return ikkuna;
    }
   
    public static void launch() throws InvocationTargetException, InterruptedException {
        JFrame frame = createGUI();
        frame.pack();
        frame.setVisible(true);
        EditorinLataajaSäie worker = new EditorinLataajaSäie();
        worker.execute();
    }

    static int huoneListanSuurin = 0;

    private static class EditorinLataajaSäie extends SwingWorker<String, Double> {
      
        @Override
        protected String doInBackground() throws Exception {
            try {
                publish((double)edistyminen);
                HuoneEditoriIkkuna.luoEditoriIkkuna();
                edistyminen = 100;
                publish((double)edistyminen);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Virheellinen huonelista.\n\nHäire sovelluksessa.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e) {
                System.out.println("Ohitetaan tyhjät indeksit");
            }
            return "Ladattu";
        }
      
        @Override
        protected void process(List<Double> aDoubles) {
            edistymisTeksti.setText("Ladataan editoria..." + aDoubles + "%");
            edistymisPalkki.setValue((int)(double)aDoubles.get(aDoubles.size() - 1));
        }
      
        @Override
        protected void done() {
            ikkuna.dispose();
            HuoneEditoriIkkuna.ikkuna.setVisible(true);
        }
    }
}
