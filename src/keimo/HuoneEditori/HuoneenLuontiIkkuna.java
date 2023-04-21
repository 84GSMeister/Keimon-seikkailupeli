package keimo.HuoneEditori;

import keimo.*;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Kenttäkohteet.*;
import keimo.Kenttäkohteet.Warp.Suunta;
import keimo.Maastot.*;
import keimo.Utility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class HuoneenLuontiIkkuna {

    static final int ikkunanLeveys = 250;
    static final int ikkunanKorkeus = 180;
    static JFrame ikkuna;
    static String[] tekstit = {"Huoneen ID", "Huoneen nimi", "Kentän koko", "Tehtäväitemit"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];
    static KenttäKohde[][] huoneenSisältö = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
    static ArrayList<KenttäKohde> huoneenSisältöLista = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huoneenMaastoLista = new ArrayList<Maasto>();
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
                JOptionPane.showMessageDialog(null, "Negatiivinen ID ei kelpaa.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else if (huoneenNimi.contains("(") || huoneenNimi.contains(")")) {
                CustomViestiIkkunat.SulkumerkkiVaroitus.showDialog();
                return false;
            }
            else if (Peli.huoneKartta.containsKey(huoneenId)) {
                JOptionPane.showMessageDialog(null, "Huone ID:llä " + huoneenId + " löytyy jo.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                return false;
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
                return true;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    static void asetaArvot(int huoneenId, String huoneenNimi, int asetettuKentänKoko, int asetettuSuklaidenMäärä, int asetettuMakkaroidenMäärä, int asetettuVihujenMäärä, boolean tehtäväItemit) {
        TarkistettavatArvot.uusiKentänKoko = asetettuKentänKoko;

        if (tehtäväItemit) {
            huoneenSisältöLista.add(new Avain(false, 0, 0));
            huoneenSisältöLista.add(new Kaasupullo(false, 0, 0));
            huoneenSisältöLista.add(new Kaasusytytin(false, 0, 0, "tyhjä"));
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
        Peli.luoHuone(huoneenId, huoneenNimi, null, "Oma alue", huoneenSisältöLista, huoneenMaastoLista, null, false, "");
        huoneenSisältöLista.removeAll(huoneenSisältöLista);
        huoneenMaastoLista.removeAll(huoneenMaastoLista);
    }

    public static void luoHuoneenLuontiIkkuna(Suunta suunta) {
        
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
        paneli.add(new JLabel(tekstit[3]));
        tehtäväItemitCheckbox = new JCheckBox();
        paneli.add(tehtäväItemitCheckbox);
        //tekstiKentät[3].setText("Kyllä");
        //tekstiKentät[3].setEditable(false);
        //tekstiKentät[3].setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");
        tehtäväItemitCheckbox.setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");

        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    // switch (suunta) {
                    //     case VASEN:
                    //         asetettavaWarpVasen = true;
                    //         asetettavaWarpVasenHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
                    //     break;
                    //     case OIKEA:
                    //         asetettavaWarpOikea = true;
                    //         asetettavaWarpOikeaHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
                    //     break;
                    //     case ALAS:
                    //         asetettavaWarpAlas = true;
                    //         asetettavaWarpAlasHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
                    //     break;
                    //     case YLÖS:
                    //         asetettavaWarpYlös = true;
                    //         asetettavaWarpYlösHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
                    //     break;
                    //     default:
                    //     break;
                    // }
                    HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäReunawarppienTiedot(asetettavaWarpVasen, asetettavaWarpVasenHuoneId, asetettavaWarpOikea, asetettavaWarpOikeaHuoneId, asetettavaWarpAlas, asetettavaWarpAlasHuoneId, asetettavaWarpYlös, asetettavaWarpYlösHuoneId);
                    if (tarkistaArvot()) {
                        HuoneEditoriIkkuna.ikkuna.setFocusable(true);
                        HuoneEditoriIkkuna.ReunaWarppiIkkuna.luoReunaWarppiIkkuna(suunta, true, huoneenId);
                    }
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    //asetettavaWarp = false;
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
        ikkuna.setAlwaysOnTop(true);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }

    // public static boolean warpSuuntaan(Suunta suunta) {
    //     luoHuoneenLuontiIkkuna(suunta);
    //     return asetettavaWarpVasen;
    // }
    // public static int warpSuuntaanId(Suunta suunta) {
    //     //luoHuoneenLuontiIkkuna(suunta);
    //     return asetettavaWarpVasenHuoneId;
    // }
}
