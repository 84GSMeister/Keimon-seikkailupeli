import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MukautusIkkuna {
    
    static final int ikkunanLeveys = 250;
    static final int ikkunanKorkeus = 260;
    static JFrame ikkuna;
    static String[] tekstit = {"Huoneen ID", "Huoneen nimi", "Kentän koko", "Suklaiden määrä", "Makkaroiden määrä", "Vihollisten määrä", "Tehtäväitemit"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];
    static KenttäKohde[][] huoneenSisältö = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
    static ArrayList<KenttäKohde> huoneenSisältöLista = new ArrayList<KenttäKohde>();

    static void tarkistaArvot() {
        try {
            int huoneenId = Integer.parseInt(tekstiKentät[0].getText());
            String huoneenNimi = tekstiKentät[1].getText();
            int kentänKoko = Integer.parseInt(tekstiKentät[2].getText());
            int suklaidenMäärä = Integer.parseInt(tekstiKentät[3].getText());
            int makkaroidenMäärä = Integer.parseInt(tekstiKentät[4].getText());
            int vihujenMäärä = Integer.parseInt(tekstiKentät[5].getText());
            boolean tehtäväItemit = true;
            if (huoneenId < 0) {
                JOptionPane.showMessageDialog(null, "Negatiivinen ID ei kelpaa.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
            }
            else if (huoneenNimi.contains("(") || huoneenNimi.contains(")")) {
                CustomViestiIkkunat.SulkumerkkiVaroitus.showDialog();
            }
            else if (Main.huoneKartta.containsKey(huoneenId)) {
                JOptionPane.showMessageDialog(null, "Huone ID:llä " + huoneenId + " löytyy jo.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (kentänKoko > 15) {
                    int kentänKokoVaroitus = CustomViestiIkkunat.IsoKenttäVaroitus.showDialog();
                    if (kentänKokoVaroitus == JOptionPane.OK_OPTION) {
                        asetaArvot(huoneenId, huoneenNimi, kentänKoko, suklaidenMäärä, makkaroidenMäärä, vihujenMäärä, tehtäväItemit);
                    }
                }
                else {
                    asetaArvot(huoneenId, huoneenNimi, kentänKoko, suklaidenMäärä, makkaroidenMäärä, vihujenMäärä, tehtäväItemit);
                }
            }
            
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void asetaArvot(int huoneenId, String huoneenNimi, int asetettuKentänKoko, int suklaidenMäärä, int makkaroidenMäärä, int vihujenMäärä, boolean tehtäväItemit) {
        //PääIkkuna.uusiIkkuna = true;
        //ikkuna.dispose();
        Main.uusiKentänKoko = asetettuKentänKoko;
        PelinAsetukset.suklaidenMäärä = suklaidenMäärä;
        PelinAsetukset.makkaroidenMäärä = makkaroidenMäärä;
        PelinAsetukset.vihujenMäärä = vihujenMäärä;

        if (tehtäväItemit) {
            huoneenSisältöLista.add(new Avain());
            huoneenSisältöLista.add(new Kaasupullo());
            huoneenSisältöLista.add(new Kaasusytytin("tyhjä"));
            huoneenSisältöLista.add(new Hiili());
            huoneenSisältöLista.add(new Paperi());
            huoneenSisältöLista.add(new Vesiämpäri());
        }
        for (int i = 0; i < suklaidenMäärä; i++) {
            huoneenSisältöLista.add(new Suklaalevy());
        }
        for (int i = 0; i < makkaroidenMäärä; i++) {
            huoneenSisältöLista.add(new Makkara());
        }
        for (int i = 0; i < vihujenMäärä; i++) {
            huoneenSisältöLista.add(new PikkuVihu());
        }
        
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
        Peli.luoHuone(huoneenId, huoneenNimi, new ImageIcon().getImage(), huoneenSisältöLista);
        Main.huoneVaihdettava = true;
        Main.uusiHuone = huoneenId;
        huoneenSisältöLista.removeAll(huoneenSisältöLista);
    }

    static void luoMukautusikkuna() {
        
        JPanel paneli = new JPanel(new SpringLayout());
        for (int i = 0; i < valintojenMäärä; i++) {
            JLabel teksti = new JLabel(tekstit[i], JLabel.TRAILING);
            paneli.add(teksti);
            tekstiKentät[i] = new JTextField("" + Main.kentänKoko,10);
            teksti.setLabelFor(tekstiKentät[i]);
            paneli.add(tekstiKentät[i]);
        }
        tekstiKentät[0].setText("" + Main.huoneidenMäärä);
        tekstiKentät[0].setToolTipText("ID:tä käytetään warppaamiseen huoneiden välillä");
        tekstiKentät[1].setText("");
        tekstiKentät[1].setToolTipText("Ei pakollinen");
        tekstiKentät[2].setText("" + Main.kentänKoko);
        tekstiKentät[2].setToolTipText("Suurin sallittu koko: 10");
        tekstiKentät[3].setText("" + PelinAsetukset.suklaidenMäärä);
        tekstiKentät[3].setToolTipText("Näin monta suklaata spawnataan satunnaiseen ruutuun.");
        tekstiKentät[4].setText("" + PelinAsetukset.makkaroidenMäärä);
        tekstiKentät[4].setToolTipText("Näin monta makkaraa spawnataan satunnaiseen ruutuun.");
        tekstiKentät[5].setText("" + PelinAsetukset.vihujenMäärä);
        tekstiKentät[5].setToolTipText("Näin monta pikkuvihua spawnataan satunnaiseen ruutuun.");
        tekstiKentät[6].setText("Kyllä");
        tekstiKentät[6].setEditable(false);
        tekstiKentät[6].setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");

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

        SpringUtilities.makeCompactGrid(paneli, 8, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Luo huone");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }
}
