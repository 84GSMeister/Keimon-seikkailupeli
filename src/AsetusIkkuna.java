import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AsetusIkkuna {
    
    static final int ikkunanLeveys = 400;
    static final int ikkunanKorkeus = 135;
    static JFrame ikkuna;
    static String[] tekstit = {"Musiikki päällä", "Valitse Musiikki"};
    static int valintojenMäärä = tekstit.length;
    static JCheckBox musiikkiPäälläCheckbox = new JCheckBox();
    static String[] musiikkiVaihtoehdot = {"Udo haukkuu: Mario 2", "Udo haukkuu: Running in the 90s", "Udo haukkuu: Nyan Cat", "Udo haukkuu: Mario World Athletic", "Udo haukkuu: Never Gonna Give You Up", "Udo haukkuu: Disco Band", "Udo haukkuu: Wide President theme"};
    static int musiikkiValinta;
    static JComboBox<String> musiikkiValikko = new JComboBox<String>(musiikkiVaihtoehdot);

    static void tarkistaArvot() {
        try {
            boolean musiikkiPäällä = musiikkiPäälläCheckbox.isSelected();
            asetaArvot(musiikkiPäällä);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void asetaArvot(boolean musiikkiEnabloitu) {
        PääIkkuna.uusiIkkuna = true;
        ikkuna.dispose();
        PelinAsetukset.musiikkiPäällä = musiikkiEnabloitu;
        PelinAsetukset.musiikkiValinta = musiikkiValikko.getSelectedIndex();
    }

    static void luoAsetusikkuna() {
        
        JPanel paneli = new JPanel(new SpringLayout());

        musiikkiPäälläCheckbox = new JCheckBox();
        JLabel teksti0 = new JLabel(tekstit[0], JLabel.TRAILING);
        teksti0.setLabelFor(musiikkiPäälläCheckbox);
        paneli.add(teksti0);
        musiikkiPäälläCheckbox.setSelected(PelinAsetukset.musiikkiPäällä);
        paneli.add(musiikkiPäälläCheckbox);

        musiikkiValikko.setSelectedIndex(PelinAsetukset.musiikkiValinta);
        JLabel teksti1 = new JLabel(tekstit[1], JLabel.TRAILING);
        paneli.add(teksti1);
        teksti1.setLabelFor(musiikkiValikko);
        paneli.add(musiikkiValikko);

        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    tarkistaArvot();
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    ikkuna.dispose();
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);

        SpringUtilities.makeCompactGrid(paneli, 3, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Esatukset");
        ikkuna.setIconImage(new ImageIcon("kuvat/pelaaja.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }
}
