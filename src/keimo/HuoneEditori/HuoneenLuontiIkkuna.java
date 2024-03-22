package keimo.HuoneEditori;

import keimo.*;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Kenttäkohteet.*;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.Utility.Käännettävä.Suunta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HuoneenLuontiIkkuna {

    static final int ikkunanLeveys = 250;
    static final int ikkunanKorkeus = 180;
    static JFrame ikkuna;
    static String[] tekstit = {"Huoneen ID", "Huoneen nimi", "Kentän koko", "Tehtäväitemit"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];
    static KenttäKohde[][] huoneenSisältö = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
    static ArrayList<KenttäKohde> huoneenSisältöLista = new ArrayList<KenttäKohde>();
    static JCheckBox tehtäväItemitCheckbox;

    private static boolean asetettavaWarpVasen = false;
    private static int asetettavaWarpVasenHuoneId = 0;
    private static boolean asetettavaWarpOikea = false;
    private static int asetettavaWarpOikeaHuoneId = 0;
    private static boolean asetettavaWarpAlas = false;
    private static int asetettavaWarpAlasHuoneId = 0;
    private static boolean asetettavaWarpYlös = false;
    private static int asetettavaWarpYlösHuoneId = 0;

    private static int huoneenId;

    static boolean tarkistaArvot() {
        try {
            huoneenId = Integer.parseInt(tekstiKentät[0].getText());
            String huoneenNimi = tekstiKentät[1].getText();
            int kentänKoko = Integer.parseInt(tekstiKentät[2].getText());
            boolean tehtäväItemit = tehtäväItemitCheckbox.isSelected();
            if (huoneenId < 0) {
                JOptionPane.showMessageDialog(ikkuna, "Negatiivinen ID ei kelpaa.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else if (Peli.huoneKartta.containsKey(huoneenId)) {
                JOptionPane.showMessageDialog(ikkuna, "Huone ID:llä " + huoneenId + " löytyy jo.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else if (huoneenNimi.contains("(") || huoneenNimi.contains(")")) {
                CustomViestiIkkunat.SulkumerkkiVaroitus.näytäDialogi();
                return false;
            }
            else if (kentänKoko < 1) {
                JOptionPane.showMessageDialog(ikkuna, "Huoneen koon täytyy olla positiivinen.", "Virheellinen koko!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else {
                asetaArvot(huoneenId, huoneenNimi, kentänKoko, tehtäväItemit);
                return true;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(ikkuna, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    static void asetaArvot(int huoneenId, String huoneenNimi, int asetettuKentänKoko, boolean tehtäväItemit) {
        TarkistettavatArvot.uusiKentänKoko = asetettuKentänKoko;

        if (tehtäväItemit) {
            huoneenSisältöLista.add(new Avain(false, 0, 0));
            huoneenSisältöLista.add(new Kaasupullo(false, 0, 0));
            huoneenSisältöLista.add(new Kaasusytytin(false, 0, 0, null));
            huoneenSisältöLista.add(new Hiili(false, 0, 0));
            huoneenSisältöLista.add(new Paperi(false, 0, 0));
            huoneenSisältöLista.add(new Vesiämpäri(false, 0, 0));
        }

        ikkuna.dispose();
        String huoneenSisältöString = "";
        for (KenttäKohde k : huoneenSisältöLista) {
            huoneenSisältöString += k.annaNimi() + ", ";
        }
        System.out.println("Huoneeseen " + huoneenId + " asetetaan " + huoneenSisältöString);
        HuoneEditoriIkkuna.vaihdaHuonetta(HuoneEditoriIkkuna.muokattavaHuone, huoneenId, false);
        Peli.luoHuone(huoneenId, asetettuKentänKoko, huoneenNimi, null, "Oma alue", huoneenSisältöLista, null, null, null, null);
        huoneenSisältöLista.removeAll(huoneenSisältöLista);
    }

    public static void luoHuoneenLuontiIkkuna(Suunta suunta) {
        
        Huone h = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone);
        JPanel paneli = new JPanel(new SpringLayout());
        for (int i = 0; i < valintojenMäärä-1; i++) {
            JLabel teksti = new JLabel(tekstit[i], JLabel.TRAILING);
            paneli.add(teksti);
            tekstiKentät[i] = new JTextField("" + Peli.kentänKoko,10);
            teksti.setLabelFor(tekstiKentät[i]);
            paneli.add(tekstiKentät[i]);
        }
        if (Peli.huoneKartta != null) {
            tekstiKentät[0].setText("" + Peli.huoneKartta.size());
        }
        else {
            tekstiKentät[0].setText("" + 0);
        }
        tekstiKentät[0].setToolTipText("ID:tä käytetään warppaamiseen huoneiden välillä");
        tekstiKentät[1].setText("");
        tekstiKentät[1].setToolTipText("Ei pakollinen");
        tekstiKentät[2].setText("" + h.annaKoko());
        tekstiKentät[2].setToolTipText("Huoneen leveys ja korkeus tileinä: esim. 10 tarkoittaa 10x10-huonetta.");
        paneli.add(new JLabel(tekstit[3]));
        tehtäväItemitCheckbox = new JCheckBox();
        paneli.add(tehtäväItemitCheckbox);
        tehtäväItemitCheckbox.setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");

        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    if (HuoneEditoriIkkuna.huoneKartta != null) {
                        if (HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone) != null) {
                            HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäReunawarppienTiedot(asetettavaWarpVasen, asetettavaWarpVasenHuoneId, asetettavaWarpOikea, asetettavaWarpOikeaHuoneId, asetettavaWarpAlas, asetettavaWarpAlasHuoneId, asetettavaWarpYlös, asetettavaWarpYlösHuoneId);
                            if (tarkistaArvot()) {
                                HuoneEditoriIkkuna.ikkuna.setFocusable(true);
                                if (suunta != null) {
                                    ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, suunta, true, huoneenId);
                                }
                            }
                        }
                    }
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    ikkuna.dispose();
                    HuoneEditoriIkkuna.ikkuna.setFocusable(true);
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä+1, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Luo huone " + suunta + " huoneesta " + HuoneEditoriIkkuna.muokattavaHuone);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

    }
}
