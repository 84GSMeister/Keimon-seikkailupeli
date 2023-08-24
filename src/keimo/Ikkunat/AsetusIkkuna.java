package keimo.Ikkunat;

import keimo.*;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.Utility.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class AsetusIkkuna {
    
    public static JFrame ikkuna;
    static String[] tekstit = {"Vaikeusaste", "Musiikki päällä", "Valitse Musiikki", "Musiikin voimakkuus", "Ääniefektien voimakkuus", "Tavoite-FPS", "Tavoite-Tickrate", "Ajoitusmuoto"};
    static int valintojenMäärä = tekstit.length;
    static JCheckBox musiikkiPäälläCheckbox = new JCheckBox();
    static int musiikkiValinta;
    static JComboBox<Object> musiikkiValikko;
    static int vaikeusAste, tavoiteFPS;
    static JTextField vaikeusasteTekstikenttä, tavoiteFPSTekstikenttä, tavoiteTickrateTekstikenttä;
    static JSlider musaVolyymiSlider, ääniVolyymiSlider;
    static JComboBox<Object> ajoitusValikko = new JComboBox<Object>(PelinAsetukset.AjoitusMuoto.values());

    static final int ikkunanLeveys = 600;
    static final int ikkunanKorkeus = 295;

    public static String ikkunaTeksti = "Esatukset          ";

    public static boolean asetuksetAuki() {
        if (ikkuna == null) {
            return false;
        }
        else {
            if (ikkuna.isVisible()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static void asetaPäällimmäiseksi() {
        ikkuna.requestFocus();
    }

    public static void tarkistaArvot() {
        try {
            boolean musiikkiPäällä = musiikkiPäälläCheckbox.isSelected();
            PelinAsetukset.vaikeusAste = Integer.parseInt(vaikeusasteTekstikenttä.getText());
            PelinAsetukset.tavoiteFPS = Integer.parseInt(tavoiteFPSTekstikenttä.getText());
            PelinAsetukset.tavoiteTickrate = Integer.parseInt(tavoiteTickrateTekstikenttä.getText());
            if (PelinAsetukset.vaikeusAste < 0) {
                JOptionPane.showMessageDialog(null, "Vaikeusaste ei voi olla negatiivinen!\n\n0 = Vihollisille ei voi kuolla", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                PelinAsetukset.vaikeusAste = 0;
            }
            else if (PelinAsetukset.vaikeusAste > 30) {
                JOptionPane.showMessageDialog(null, "Maksimivaikeusaste on 30.\n\n0 = Vihollisille ei voi kuolla", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                PelinAsetukset.vaikeusAste = 30;
            }
            else if (PelinAsetukset.tavoiteFPS > 1000) {
                JOptionPane.showMessageDialog(null, "Maksimi-tavoite-FPS on 1000.", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                PelinAsetukset.tavoiteFPS = 1000;
            }
            else if (PelinAsetukset.tavoiteFPS < 1) {
                JOptionPane.showMessageDialog(null, "Tavoite-FPS täytyy olla positiivinen!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                PelinAsetukset.tavoiteFPS = 1;
            }
            else if (PelinAsetukset.tavoiteTickrate > 1000) {
                JOptionPane.showMessageDialog(null, "Maksimi-tavoite-tickrate on 1000.", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                PelinAsetukset.tavoiteTickrate = 1000;
            }
            else if (PelinAsetukset.tavoiteTickrate < 1) {
                JOptionPane.showMessageDialog(null, "Tavoite-tickrate taytyy olla positiivinen!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                PelinAsetukset.tavoiteTickrate = 1;
            }
            else {
                int hyväksyUudelleenkäynnistys = CustomViestiIkkunat.UudelleenkäynnistäAsetukset.showDialog();
                if (hyväksyUudelleenkäynnistys == JOptionPane.YES_OPTION) {
                    asetaArvot(musiikkiPäällä);
                }
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void asetaArvot(boolean musiikkiEnabloitu) {
        ikkuna.dispose();
        PelinAsetukset.musiikkiPäällä = musiikkiEnabloitu;
        PelinAsetukset.musiikkiValinta = musiikkiValikko.getSelectedIndex();
        PelinAsetukset.musaVolyymi = musaVolyymiSlider.getValue()/100d;
        PelinAsetukset.ääniVolyymi = ääniVolyymiSlider.getValue()/100d;
        PelinAsetukset.ajoitus = (AjoitusMuoto)ajoitusValikko.getSelectedItem();
        if (PelinAsetukset.ajoitus == AjoitusMuoto.ERITTÄIN_NOPEA) {
            PelinAsetukset.tavoiteFPS = 60;
            PelinAsetukset.tavoiteTickrate = 60;
        }
        Peli.ääniThread.run();
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
        teksti1.setToolTipText("Musiikki päällä.");
        paneli.add(teksti1);
        musiikkiPäälläCheckbox.setSelected(PelinAsetukset.musiikkiPäällä);
        musiikkiPäälläCheckbox.setToolTipText("Musiikki päällä.");
        paneli.add(musiikkiPäälläCheckbox);

        musiikkiValikko = new JComboBox<Object>(ÄänentoistamisSäie.musaLista.toArray());
        musiikkiValikko.setSelectedIndex(PelinAsetukset.musiikkiValinta);
        JLabel teksti2 = new JLabel(tekstit[2], JLabel.TRAILING);
        teksti2.setLabelFor(musiikkiValikko);
        paneli.add(teksti2);
        teksti2.setToolTipText("Valitse musiikki");
        musiikkiValikko.setToolTipText("Valitse musiikki");
        paneli.add(musiikkiValikko);

        musaVolyymiSlider = new JSlider();
        JLabel teksti3 = new JLabel(tekstit[3], JLabel.TRAILING);
        teksti3.setLabelFor(tavoiteFPSTekstikenttä);
        teksti3.setToolTipText("<html><p>Musiikin äänenvoimakkuus</p></html>");
        paneli.add(teksti3);
        musaVolyymiSlider.setValue((int)(PelinAsetukset.musaVolyymi*100));
        musaVolyymiSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ÄänentoistamisSäie.asetaMusanVolyymi(musaVolyymiSlider.getValue()/100d);
            }
        });
        musaVolyymiSlider.setToolTipText("<html><p>Musiikin äänenvoimakkuus</p></html>");
        paneli.add(musaVolyymiSlider);

        ääniVolyymiSlider = new JSlider();
        JLabel teksti4 = new JLabel(tekstit[4], JLabel.TRAILING);
        teksti4.setLabelFor(tavoiteFPSTekstikenttä);
        teksti4.setToolTipText("<html><p>Efektien (SFX) äänenvoimakkuus</p></html>");
        paneli.add(teksti4);
        ääniVolyymiSlider.setValue((int)(PelinAsetukset.ääniVolyymi*100));
        ääniVolyymiSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ÄänentoistamisSäie.ääniToistin.setVolume(ääniVolyymiSlider.getValue()/100d);
            }
        });
        ääniVolyymiSlider.setToolTipText("<html><p>Efektien (SFX) äänenvoimakkuus</p></html>");
        paneli.add(ääniVolyymiSlider);

        tavoiteFPSTekstikenttä = new JTextField();
        JLabel teksti5 = new JLabel(tekstit[5], JLabel.TRAILING);
        teksti5.setLabelFor(tavoiteFPSTekstikenttä);
        teksti5.setToolTipText("<html><p>Tavoite-FPS<br><br>Älä aseta liian suureksi tai<br>käyttöliittymä voi muuttua tökkiväksi<br><br>Oletus: 200</p></html><br><br>Vaatii uudelleenkäynnistyksen!</p></html>");
        paneli.add(teksti5);
        tavoiteFPSTekstikenttä.setText("" + (PelinAsetukset.tavoiteFPS));
        tavoiteFPSTekstikenttä.setToolTipText("<html><p>Tavoite-FPS<br><br>Älä aseta liian suureksi tai<br>käyttöliittymä voi muuttua tökkiväksi<br><br>Oletus: 200</p></html><br><br>Vaatii uudelleenkäynnistyksen!</p></html>");
        paneli.add(tavoiteFPSTekstikenttä);

        tavoiteTickrateTekstikenttä = new JTextField();
        JLabel teksti6 = new JLabel(tekstit[6], JLabel.TRAILING);
        teksti6.setLabelFor(tavoiteTickrateTekstikenttä);
        teksti6.setToolTipText("<html><p>Tavoite-Tickrate<br><br>Vaikuttaa mm.<br>-Pelaajan liikkeeseen<br>-Vihollisten liikkeeseen<br>-Collision-tarkistuksiin<br><br>Oletus: 60<br><br>Vaatii uudelleenkäynnistyksen!</p></html></p></html>");
        paneli.add(teksti6);
        tavoiteTickrateTekstikenttä.setText("" + (PelinAsetukset.tavoiteTickrate));
        tavoiteTickrateTekstikenttä.setToolTipText("<html><p>Tavoite-Tickrate<br><br>Vaikuttaa mm.<br>-Pelaajan liikkeeseen<br>-Vihollisten liikkeeseen<br>-Collision-tarkistuksiin<br><br>Oletus: 60<br><br>Vaatii uudelleenkäynnistyksen!</p></html></p></html>");
        paneli.add(tavoiteTickrateTekstikenttä);

        ajoitusValikko.setSelectedItem(PelinAsetukset.AjoitusMuoto.valueOf(PelinAsetukset.ajoitus.name()));
        JLabel teksti7 = new JLabel(tekstit[7], JLabel.TRAILING);
        paneli.add(teksti7);
        teksti7.setLabelFor(ajoitusValikko);
        teksti7.setToolTipText("<html><p>Tarkempi framerate- ja tickrate-ajoitus parantaa pelin sulavuutta, mutta kasvattaa CPU-käyttöä.<br>Tarkka: Suositellaan 4-ydin- ja paremmille prosessoreille.<br>Nopea: Suositellaan 2-ydin-4-säieprosessoreille.<br>Erittäin nopea: Suositellaan 2-ydinprosessoreille<br><br>Vaatii uudelleenkäynnistyksen!</p></html>");
        ajoitusValikko.setToolTipText("<html><p>Tarkempi framerate- ja tickrate-ajoitus parantaa pelin sulavuutta, mutta kasvattaa CPU-käyttöä.<br>Tarkka: Suositellaan 4-ydin- ja paremmille prosessoreille.<br>Nopea: Suositellaan 2-ydin-4-säieprosessoreille.<br>Erittäin nopea: Suositellaan 2-ydinprosessoreille<br><br>Vaatii uudelleenkäynnistyksen!</p></html>");
        paneli.add(ajoitusValikko);

        

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

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä+1, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Esatukset");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.setLocationRelativeTo(PääIkkuna.ikkuna);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }
}
