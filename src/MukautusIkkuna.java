import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MukautusIkkuna {
    
    static final int ikkunanLeveys = 250;
    static final int ikkunanKorkeus = 210;
    static JFrame ikkuna;
    static String[] tekstit = {"Kentän koko", "Vaikeusaste", "Suklaiden määrä", "Makkaroiden määrä", "Vihollisten määrä"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];

    static void tarkistaArvot() {
        try {
            int kentänKoko = Integer.parseInt(tekstiKentät[0].getText());
            int vaikeusAste = Integer.parseInt(tekstiKentät[1].getText());
            int suklaidenMäärä = Integer.parseInt(tekstiKentät[2].getText());
            int makkaroidenMäärä = Integer.parseInt(tekstiKentät[3].getText());
            int vihujenMäärä = Integer.parseInt(tekstiKentät[4].getText());
            if (kentänKoko > 15) {
                int kentänKokoVaroitus = CustomViestiIkkunat.IsoKenttäVaroitus.showDialog();
                if (kentänKokoVaroitus == JOptionPane.OK_OPTION) {
                    asetaArvot(kentänKoko, vaikeusAste, suklaidenMäärä, makkaroidenMäärä, vihujenMäärä);
                }
            }
            else {
                asetaArvot(kentänKoko, vaikeusAste, suklaidenMäärä, makkaroidenMäärä, vihujenMäärä);
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void asetaArvot(int asetettuKentänKoko, int vaikeusAste, int suklaidenMäärä, int makkaroidenMäärä, int vihujenMäärä) {
        PääIkkuna.uusiIkkuna = true;
        ikkuna.dispose();
        Main.uusiKentänKoko = asetettuKentänKoko;
        PelinAsetukset.suklaidenMäärä = suklaidenMäärä;
        PelinAsetukset.makkaroidenMäärä = makkaroidenMäärä;
        PelinAsetukset.vihujenMäärä = vihujenMäärä;
        PelinAsetukset.vaikeusAste = vaikeusAste;
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
        tekstiKentät[1] = new JTextField("" + PelinAsetukset.vaikeusAste);
        tekstiKentät[2] = new JTextField("" + PelinAsetukset.suklaidenMäärä);
        tekstiKentät[3] = new JTextField("" + PelinAsetukset.makkaroidenMäärä);
        tekstiKentät[4] = new JTextField("" + PelinAsetukset.vihujenMäärä);

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

        SpringUtilities.makeCompactGrid(paneli, 6, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Mukauta");
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
