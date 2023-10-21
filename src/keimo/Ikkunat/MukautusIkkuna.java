package keimo.Ikkunat;

import keimo.*;
import keimo.Kenttäkohteet.*;
import keimo.Utility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MukautusIkkuna {
    
    static final int ikkunanLeveys = 250;
    static final int ikkunanKorkeus = 180;
    static JFrame ikkuna;
    static String[] tekstit = {"Huoneen ID", "Huoneen nimi", "Kentän koko", "Tehtäväitemit"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];
    static KenttäKohde[][] huoneenSisältö = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
    static ArrayList<KenttäKohde> huoneenSisältöLista = new ArrayList<KenttäKohde>();
    static JCheckBox tehtäväItemitCheckbox;

    // private static int suklaidenMäärä = 12;
    // private static int makkaroidenMäärä = 6;
    // private static int vihujenMäärä = 5;

    static void tarkistaArvot() {
        try {
            int huoneenId = Integer.parseInt(tekstiKentät[0].getText());
            String huoneenNimi = tekstiKentät[1].getText();
            int kentänKoko = Integer.parseInt(tekstiKentät[2].getText());
            //int suklaidenMäärä = Integer.parseInt(tekstiKentät[3].getText());
            //int makkaroidenMäärä = Integer.parseInt(tekstiKentät[4].getText());
            //int vihujenMäärä = Integer.parseInt(tekstiKentät[5].getText());
            boolean tehtäväItemit = tehtäväItemitCheckbox.isSelected();
            if (huoneenId < 0) {
                JOptionPane.showMessageDialog(null, "Negatiivinen ID ei kelpaa.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
            }
            else if (huoneenNimi.contains("(") || huoneenNimi.contains(")")) {
                CustomViestiIkkunat.SulkumerkkiVaroitus.showDialog();
            }
            else if (Peli.huoneKartta.containsKey(huoneenId)) {
                JOptionPane.showMessageDialog(null, "Huone ID:llä " + huoneenId + " löytyy jo.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (kentänKoko > 10) {
                    int kentänKokoVaroitus = CustomViestiIkkunat.IsoKenttäVaroitus.showDialog();
                    if (kentänKokoVaroitus == JOptionPane.OK_OPTION) {
                        asetaArvot(huoneenId, huoneenNimi, kentänKoko, 0, 0, 0, tehtäväItemit);
                    }
                }
                else {
                    asetaArvot(huoneenId, huoneenNimi, kentänKoko, 0, 0, 0, tehtäväItemit);
                }
            }
            
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void asetaArvot(int huoneenId, String huoneenNimi, int asetettuKentänKoko, int asetettuSuklaidenMäärä, int asetettuMakkaroidenMäärä, int asetettuVihujenMäärä, boolean tehtäväItemit) {
        //PääIkkuna.uusiIkkuna = true;
        //ikkuna.dispose();
        TarkistettavatArvot.uusiKentänKoko = asetettuKentänKoko;
        // suklaidenMäärä = asetettuSuklaidenMäärä;
        // makkaroidenMäärä = asetettuMakkaroidenMäärä;
        // vihujenMäärä = asetettuVihujenMäärä;

        if (tehtäväItemit) {
            huoneenSisältöLista.add(new Avain(false, 0, 0));
            huoneenSisältöLista.add(new Kaasupullo(false, 0, 0));
            huoneenSisältöLista.add(new Kaasusytytin(false, 0, 0, null));
            huoneenSisältöLista.add(new Hiili(false, 0, 0));
            huoneenSisältöLista.add(new Paperi(false, 0, 0));
            huoneenSisältöLista.add(new Vesiämpäri(false, 0, 0));
        }
        // for (int i = 0; i < suklaidenMäärä; i++) {
        //     huoneenSisältöLista.add(new Suklaalevy(false, 0, 0));
        // }
        // for (int i = 0; i < makkaroidenMäärä; i++) {
        //     huoneenSisältöLista.add(new Makkara(false, 0, 0));
        // }
        // for (int i = 0; i < vihujenMäärä; i++) {
        //     huoneenSisältöLista.add(new PikkuVihu_KenttäKohde(false, 0, 0));
        // }
        
        //for (KenttäKohde[] k : huoneenSisältö){
        //    for (KenttäKohde kk : k){
        //        huoneenSisältöLista.add(kk);
        //    }
        //}
        ikkuna.dispose();
        String huoneenSisältöString = "";
        for (KenttäKohde k : huoneenSisältöLista) {
            huoneenSisältöString += k.annaNimi() + ", ";
        }
        System.out.println("Huoneeseen asetetaan " + huoneenSisältöString);
        Peli.luoHuone(huoneenId, huoneenNimi, null, "Oma alue", huoneenSisältöLista, null, null, null, null);
        Peli.huoneVaihdettava = true;
        Peli.uusiHuone = huoneenId;
        huoneenSisältöLista.removeAll(huoneenSisältöLista);
    }

    public static void luoMukautusikkuna() {
        
        JPanel paneli = new JPanel(new SpringLayout());
        for (int i = 0; i < valintojenMäärä-1; i++) {
            JLabel teksti = new JLabel(tekstit[i], JLabel.TRAILING);
            paneli.add(teksti);
            tekstiKentät[i] = new JTextField("" + Peli.kentänKoko,10);
            teksti.setLabelFor(tekstiKentät[i]);
            paneli.add(tekstiKentät[i]);
        }
        tekstiKentät[0].setText("" + Peli.huoneKartta.size());
        tekstiKentät[0].setToolTipText("ID:tä käytetään warppaamiseen huoneiden välillä");
        tekstiKentät[1].setText("");
        tekstiKentät[1].setToolTipText("Ei pakollinen");
        tekstiKentät[2].setText("" + Peli.kentänKoko);
        tekstiKentät[2].setToolTipText("Suurin sallittu koko: 10");
        // tekstiKentät[3].setText("" + suklaidenMäärä);
        // tekstiKentät[3].setToolTipText("Näin monta suklaata spawnataan satunnaiseen ruutuun.");
        // tekstiKentät[4].setText("" + makkaroidenMäärä);
        // tekstiKentät[4].setToolTipText("Näin monta makkaraa spawnataan satunnaiseen ruutuun.");
        // tekstiKentät[5].setText("" + vihujenMäärä);
        // tekstiKentät[5].setToolTipText("Näin monta pikkuvihua spawnataan satunnaiseen ruutuun.");
        paneli.add(new JLabel(tekstit[3]));
        tehtäväItemitCheckbox = new JCheckBox();
        tehtäväItemitCheckbox.setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");
        paneli.add(tehtäväItemitCheckbox);
        // tekstiKentät[3].setText("Kyllä");
        // tekstiKentät[3].setEditable(false);
        // tekstiKentät[3].setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");

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

        ikkuna = new JFrame("Luo huone");
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
