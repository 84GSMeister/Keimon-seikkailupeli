package keimo.Ikkunat;

import keimo.*;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.Utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AsetusIkkuna {
    
    static final int ikkunanLeveys = 400;
    static final int ikkunanKorkeus = 190;
    static JFrame ikkuna;
    static String[] tekstit = {"Vaikeusaste", "Musiikki päällä", "Valitse Musiikki", "Tavoite-FPS"};
    static int valintojenMäärä = tekstit.length;
    static JCheckBox musiikkiPäälläCheckbox = new JCheckBox();
    static String[] musiikkiVaihtoehdot = {"Udo haukkuu: Mario 2", "Udo haukkuu: Running in the 90s", "Udo haukkuu: Nyan Cat", "Udo haukkuu: Mario World Athletic", "Udo haukkuu: Never Gonna Give You Up", "Udo haukkuu: Disco Band", "Udo haukkuu: Wide President theme"};
    static int musiikkiValinta;
    static JComboBox<Object> musiikkiValikko = new JComboBox<Object>(ÄänentoistamisSäie.musaLista.toArray());
    static int vaikeusAste, tavoiteFPS;
    static JTextField vaikeusasteTekstikenttä, tavoiteFPSTekstikenttä;

    public static void tarkistaArvot() {
        try {
            boolean musiikkiPäällä = musiikkiPäälläCheckbox.isSelected();
            PelinAsetukset.vaikeusAste = Integer.parseInt(vaikeusasteTekstikenttä.getText());
            PelinAsetukset.tavoiteFPS = Integer.parseInt(tavoiteFPSTekstikenttä.getText()) + 1;
            if (PelinAsetukset.vaikeusAste < 0) {
                JOptionPane.showMessageDialog(null, "Vaikeusaste ei voi olla negatiivinen!\n\n0 = Vihollisille ei voi kuolla", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else if (PelinAsetukset.tavoiteFPS < 1) {
                JOptionPane.showMessageDialog(null, "Tavoite-FPS ei voi olla negatiivinen!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else {
                asetaArvot(musiikkiPäällä);
            }
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

    public static void luoAsetusikkuna() {
        
        JPanel paneli = new JPanel(new SpringLayout());

        vaikeusasteTekstikenttä = new JTextField();
        JLabel teksti0 = new JLabel(tekstit[0], JLabel.TRAILING);
        teksti0.setLabelFor(vaikeusasteTekstikenttä);
        teksti0.setToolTipText("Vaikuttaa vihollisten vahinkoon.");
        paneli.add(teksti0);
        vaikeusasteTekstikenttä.setText("" + PelinAsetukset.vaikeusAste);
        vaikeusasteTekstikenttä.setToolTipText("Vaikuttaa vihollisten vahinkoon.");
        paneli.add(vaikeusasteTekstikenttä);

        musiikkiPäälläCheckbox = new JCheckBox();
        JLabel teksti1 = new JLabel(tekstit[1], JLabel.TRAILING);
        teksti1.setLabelFor(musiikkiPäälläCheckbox);
        paneli.add(teksti1);
        musiikkiPäälläCheckbox.setSelected(PelinAsetukset.musiikkiPäällä);
        paneli.add(musiikkiPäälläCheckbox);

        musiikkiValikko.setSelectedIndex(PelinAsetukset.musiikkiValinta);
        JLabel teksti2 = new JLabel(tekstit[2], JLabel.TRAILING);
        paneli.add(teksti2);
        teksti2.setLabelFor(musiikkiValikko);
        paneli.add(musiikkiValikko);

        tavoiteFPSTekstikenttä = new JTextField();
        JLabel teksti3 = new JLabel(tekstit[3], JLabel.TRAILING);
        teksti3.setLabelFor(tavoiteFPSTekstikenttä);
        teksti3.setToolTipText("Vaikuttaa siihen, kuinka kauan grafiikkaa piirtävä säie odottaa. Älä aseta liian suureksi.");
        paneli.add(teksti3);
        tavoiteFPSTekstikenttä.setText("" + (PelinAsetukset.tavoiteFPS -1));
        tavoiteFPSTekstikenttä.setToolTipText("Vaikuttaa siihen, kuinka kauan grafiikkaa piirtävä säie odottaa. Älä aseta liian suureksi.");
        paneli.add(tavoiteFPSTekstikenttä);

        

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

        SpringUtilities.makeCompactGrid(paneli, 5, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Esatukset");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }
}
