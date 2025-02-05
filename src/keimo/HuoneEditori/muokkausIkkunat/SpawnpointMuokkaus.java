package keimo.HuoneEditori.muokkausIkkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.Utility.Downloaded.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.Collections;

import javax.swing.*;

public class SpawnpointMuokkaus {
    
    static final int ikkunanLeveys = 420;
    static int ikkunanKorkeus = 180;
    static JFrame muokkausIkkuna;
    static int valintojenMäärä;
    static JPanel paneli;
    static JLabel spawnHuoneLabel, spawnXLabel, spawnYLabel;
    static JComboBox<String> spawnHuoneValikko;
    static JTextField spawnXTekstikenttä, spawnYTekstikenttä;

    private static void hyväksyMuutokset(int sijX, int sijY) {
        try {
            int huone = spawnHuoneValikko.getSelectedIndex();
            int x = Integer.parseInt(spawnXTekstikenttä.getText());
            int y = Integer.parseInt(spawnYTekstikenttä.getText());
            if (x < 0 || y < 0) {
                JOptionPane.showMessageDialog(null, "Pelaajan spawn-pisteen koordinaatti ei voi olla negatiivinen.\nHuoneet eivät voi jatkua negatiiviselle lukualueelle.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
            }
            else if (x >= Peli.huoneKartta.get(0).annaKoko() || y >= Peli.huoneKartta.get(0).annaKoko()) {
                JOptionPane.showMessageDialog(null, "Virheellinen syöte. Kentän ulkopuolella.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Pelaaja.alkuHuone = huone;
                Pelaaja.alkuSijX = x;
                Pelaaja.alkuSijY = y;
                muokkausIkkuna.dispose();
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virheellinen syöte. Kentän ulkopuolella.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ei voitu tallentaa muutoksia, koska huonetta ei löytynyt.\n\nHuonekartan latauksessa ongelma. Onkohan kst-tiedostossa vikaa?", "häire", JOptionPane.ERROR_MESSAGE);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virheellinen syöte. Vain kokonaisluvut kelpaavat.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void luoSpawnpointMuokkausIkkuna() {
        
        int alkuHuone = Pelaaja.alkuHuone;
        int sijX = Pelaaja.alkuSijX;
        int sijY = Pelaaja.alkuSijY;
        valintojenMäärä = 3;

        spawnHuoneLabel = new JLabel("Spawn-huone");
        spawnHuoneValikko = luoHuoneenNimiLista();
        spawnHuoneValikko.setSelectedIndex(alkuHuone);
        spawnXLabel = new JLabel("Spawn X");
        spawnXTekstikenttä = new JTextField("" + sijX);
        spawnYLabel = new JLabel("Spawn Y");
        spawnYTekstikenttä = new JTextField("" + sijY);
        paneli = new JPanel(new SpringLayout());
        paneli.add(spawnHuoneLabel);
        paneli.add(spawnHuoneValikko);
        paneli.add(spawnXLabel);
        paneli.add(spawnXTekstikenttä);
        paneli.add(spawnYLabel);
        paneli.add(spawnYTekstikenttä);

        JButton okNappi = new JButton("Aseta");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    hyväksyMuutokset(sijX, sijY);
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    muokkausIkkuna.dispose();
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);
        paneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä + 1, 2, 6, 6, 6, 6);
        ikkunanKorkeus = valintojenMäärä * 30 + 70;

        muokkausIkkuna = new JFrame();
        muokkausIkkuna.setTitle("Muokkaa pelaajan spawn-pistettä");
        muokkausIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        muokkausIkkuna.setBounds(100, 50, ikkunanLeveys, ikkunanKorkeus);
        muokkausIkkuna.setLayout(new BorderLayout());
        muokkausIkkuna.setVisible(true);
        muokkausIkkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        muokkausIkkuna.add(paneli, BorderLayout.CENTER);
        muokkausIkkuna.revalidate();
        muokkausIkkuna.repaint();
    }

    static JComboBox<String> luoHuoneenNimiLista() {
        int huoneListanKoko = HuoneEditoriIkkuna.huoneKartta.size();
        int huoneListanSuurin = Collections.max(HuoneEditoriIkkuna.huoneKartta.keySet());
        String[] huoneidenNimet = new String[huoneListanKoko];
        int toimivatHuoneet = 0;
        JComboBox<String> huoneValikko = new JComboBox<>();
        
        try {
            for (int i = 0; i < huoneListanSuurin + 1; i++) {
                if (Peli.huoneKartta.get(i) == null) {
                    continue;
                }
                else {
                    huoneidenNimet[toimivatHuoneet] = HuoneEditoriIkkuna.huoneKartta.get(i).annaNimi() + " (" + HuoneEditoriIkkuna.huoneKartta.get(i).annaId() + ")";
                    toimivatHuoneet++;
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
        return huoneValikko;
    }
}
