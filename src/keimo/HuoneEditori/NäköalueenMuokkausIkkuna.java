package keimo.HuoneEditori;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import keimo.HuoneEditori.muokkausIkkunat.VartijaMuokkaus;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.entityt.npc.NPC;
import keimo.entityt.npc.Vartija;

public class NäköalueenMuokkausIkkuna {
    static JFrame ikkuna;
    static String[] tekstit;
    static JPanel paneli;
    static JLabel[] tekstiLabelit;
    static JTextField[] tekstiKentät;

    public static void luoNäköalueenMuokkausIkkuna(NPC npc) {
        if (npc instanceof Vartija) {
            Vartija vartija = (Vartija)npc;
            int sijX = vartija.annaSijX();
            int sijY = vartija.annaSijY();

            int valintojenMäärä = 4;
            tekstit = new String[valintojenMäärä];
            tekstit[0] = "X1: ";
            tekstit[1] = "Y1: ";
            tekstit[2] = "X2: ";
            tekstit[3] = "Y2: ";
            tekstiLabelit = new JLabel[valintojenMäärä];
            tekstiKentät = new JTextField[valintojenMäärä];

            paneli = new JPanel(new SpringLayout());
            tekstiLabelit = new JLabel[valintojenMäärä];

            tekstiLabelit[0] = new JLabel(tekstit[0]);
            paneli.add(tekstiLabelit[0]);
            tekstiKentät[0] = new JTextField("" + vartija.x1);
            paneli.add(tekstiKentät[0]);

            tekstiLabelit[1] = new JLabel(tekstit[1]);
            paneli.add(tekstiLabelit[1]);
            tekstiKentät[1] = new JTextField("" + vartija.y1);
            paneli.add(tekstiKentät[1]);

            tekstiLabelit[2] = new JLabel(tekstit[2]);
            paneli.add(tekstiLabelit[2]);
            tekstiKentät[2] = new JTextField("" + vartija.x2);
            paneli.add(tekstiKentät[2]);

            tekstiLabelit[3] = new JLabel(tekstit[3]);
            paneli.add(tekstiLabelit[3]);
            tekstiKentät[3] = new JTextField("" + vartija.y2);
            paneli.add(tekstiKentät[3]);

            SpringUtilities.makeCompactGrid(paneli, valintojenMäärä, 2, 6, 6, 6, 6);

            JButton okNappi = new JButton("OK");
            okNappi.addActionListener(e -> {
                if (tarkistaArvot()) {
                    ikkuna.dispose();
                    HuoneEditoriIkkuna.ikkuna.setFocusable(true);
                }
            });

            JButton cancelNappi = new JButton("Peruuta");
            cancelNappi.addActionListener(e -> {
                ikkuna.dispose();
                HuoneEditoriIkkuna.ikkuna.setFocusable(true);
            });

            JPanel okCancelPaneli = new JPanel();
            okCancelPaneli.add(okNappi);
            okCancelPaneli.add(cancelNappi);

            ikkuna = new JFrame();
            ikkuna.setTitle("Valitse triggerit");
            ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
            ikkuna.setBounds(0, 0, 440, 180);
            ikkuna.setLayout(new BorderLayout());
            ikkuna.setVisible(true);
            ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
            ikkuna.add(paneli, BorderLayout.CENTER);
            ikkuna.add(okCancelPaneli, BorderLayout.SOUTH);
            ikkuna.revalidate();
            ikkuna.repaint();
        }
        else JOptionPane.showMessageDialog(null, "Näköalueen muokkausta ei voitu määrittää " + npc.annaNimiSijamuodossa("allatiivi"), "Virheellinen npc:n tyyppi", JOptionPane.ERROR_MESSAGE);
    }

    private static boolean tarkistaArvot() {
        
        try {
            int x1 = Integer.parseInt(tekstiKentät[0].getText());
            int y1 = Integer.parseInt(tekstiKentät[1].getText());
            int x2 = Integer.parseInt(tekstiKentät[2].getText());
            int y2 = Integer.parseInt(tekstiKentät[3].getText());
            if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
                JOptionPane.showMessageDialog(null, "NPC:n näköalueen koordinaatti ei voi olla negatiivinen.\nHuoneet eivät voi jatkua negatiiviselle lukualueelle.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else {
                VartijaMuokkaus.päivitäNäköalue(x1, y1, x2, y2);
                return true;
            }
        }
        catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
