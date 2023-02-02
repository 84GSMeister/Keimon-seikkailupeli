package keimo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class HuoneenVaihtoIkkuna {
    
    static final int ikkunanLeveys = 300;
    static final int ikkunanKorkeus = 140;
    static JFrame ikkuna;
    static String[] tekstit = {"Warppaa ID:llä", "Valitse huone"};
    static String[] huoneidenNimet;
    static JComboBox<String> huoneValikko;
    static int valintojenMäärä = tekstit.length;
    static int warppiTapa = 0;
    static JTextField kohdeHuoneTekstikenttä;
    static JRadioButton warppaaIdllä, warppaaListalla;
	static ButtonGroup warppiTapaValinta = new ButtonGroup();
    static ArrayList<Integer> toimivatHuoneIndeksit = new ArrayList<Integer>();

    static void luoHuoneenNimiLista() {
        int huoneListanKoko = Peli.huoneKartta.size();
        int huoneListanSuurin = Collections.max(Peli.huoneKartta.keySet());
        huoneidenNimet = new String[huoneListanKoko];
        int toimivatHuoneet = 0;
        
        try {
            for (int i = 0; i < huoneListanSuurin + 1; i++) {
                if (Peli.huoneKartta.get(i) == null) {
                    System.out.println("Huonetta " + i + " ei löytynyt.");
                    continue;
                }
                else {
                    huoneidenNimet[toimivatHuoneet] = Peli.huoneKartta.get(i).annaNimi() + " (" + Peli.huoneKartta.get(i).annaId() + ")";
                    toimivatHuoneIndeksit.add(i);
                    toimivatHuoneet++;
                    System.out.println("Huone " + i + ": " + Peli.huoneKartta.get(i).annaNimi() + ", ID: " + Peli.huoneKartta.get(i).annaId());
                }
            }
            huoneValikko = new JComboBox<String>(huoneidenNimet);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen huonelista.\n\nHäire sovelluksessa.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            System.out.println("Ohitetaan tyhjät indeksit");
        }
    }
    
    static void tarkistaArvot(int valinta) {
        switch (valinta) {
            default:
                try {
                    int huoneenId = Integer.parseInt(kohdeHuoneTekstikenttä.getText());
                    if (Peli.huoneKartta.containsKey(huoneenId)) {
                        asetaArvot(huoneenId);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Huonetta ID:llä " + huoneenId + " ei löytynyt.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 1:
                try {
                    //int huoneenId = toimivatHuoneIndeksit.get(huoneValikko.getSelectedIndex());
                    //huoneenId = Integer.parseInt("" + huoneidenNimet[huoneValikko.getSelectedIndex()].charAt(huoneidenNimet[huoneValikko.getSelectedIndex()].indexOf("(")));
                    String huoneString = huoneidenNimet[huoneValikko.getSelectedIndex()].substring(huoneidenNimet[huoneValikko.getSelectedIndex()].indexOf("("));
                    String huoneStringPelkkäNumero = huoneString.substring(1, huoneString.length()-1);
                    int huoneenId = Integer.parseInt(huoneStringPelkkäNumero);
                    System.out.println(huoneStringPelkkäNumero);
                    if (Peli.huoneKartta.containsKey(huoneenId)) {
                        asetaArvot(huoneenId);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Huonetta ID:llä " + huoneenId + " ei löytynyt.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    static void asetaArvot(int huoneenId) {
        Peli.uusiHuone = huoneenId;
        Peli.huoneVaihdettava = true;
        ikkuna.dispose();
    }

    static void luoHuoneenVaihtoikkuna() {
        
        luoHuoneenNimiLista();

        JPanel paneli = new JPanel(new SpringLayout());

        warppaaIdllä = new JRadioButton("");
        warppaaIdllä.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warppiTapa = 0;
			}
		});
        kohdeHuoneTekstikenttä = new JTextField();
        JLabel teksti0 = new JLabel(tekstit[0], JLabel.TRAILING);
        teksti0.setLabelFor(kohdeHuoneTekstikenttä);
        warppiTapaValinta.add(warppaaIdllä);
        paneli.add(warppaaIdllä);
        paneli.add(teksti0);
        paneli.add(kohdeHuoneTekstikenttä);

        warppaaListalla = new JRadioButton("");
        //warppaaListalla.setSelected(true);
        warppaaListalla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				warppiTapa = 1;
			}
		});
        huoneValikko = new JComboBox<String>(huoneidenNimet);
        JLabel teksti1 = new JLabel(tekstit[1], JLabel.TRAILING);
        teksti1.setLabelFor(huoneValikko);
        warppiTapaValinta.add(warppaaListalla);
        paneli.add(warppaaListalla);
        paneli.add(teksti1);
        paneli.add(huoneValikko);


        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    tarkistaArvot(warppiTapa);
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

        JButton kolmasNappi = new JButton("");
        kolmasNappi.setVisible(false);
        kolmasNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    ikkuna.dispose();
                }
            }
        });

        paneli.add(kolmasNappi);
        paneli.add(okNappi);
        paneli.add(cancelNappi);

        SpringUtilities.makeCompactGrid(paneli, 3, 3, 6, 6, 6, 6);

        ikkuna = new JFrame("Warppaa huoneeseen");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }
}
