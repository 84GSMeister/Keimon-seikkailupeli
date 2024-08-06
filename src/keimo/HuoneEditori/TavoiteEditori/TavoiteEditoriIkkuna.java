package keimo.HuoneEditori.TavoiteEditori;

import java.awt.BorderLayout;
import javax.swing.*;

import keimo.HuoneEditori.HuoneEditoriIkkuna;

public class TavoiteEditoriIkkuna {

    static JFrame ikkuna;
    static JComboBox<Object> pääTavoiteLista;
    static JComboBox<Object> sivuTavoiteLista;
    static final int ikkunanLeveys = 400;
    static final int ikkunanKorkeus = 240;
    
    public static void luoTavoiteEditoriIkkuna() {

        ikkuna = new JFrame("Tavoite-editori v0.1");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(0, 0, ikkunanLeveys, ikkunanKorkeus);

        JLabel pääTavoiteOtsikko = new JLabel("Päätavoitteet");
        pääTavoiteLista = new JComboBox<>(TavoiteLista.pääTavoitteet.values().toArray());
        JButton muokkaaPääTavoitteita = new JButton("Muokkaa tavoitetta");
        muokkaaPääTavoitteita.addActionListener(e -> {
            TavoitteenMuokkausIkkuna.luoTavoitteenMuokkausIkkuna(pääTavoiteLista.getSelectedItem());
        });
        JPanel pääTavoitePaneeli = new JPanel();
        pääTavoitePaneeli.add(pääTavoiteOtsikko);
        pääTavoitePaneeli.add(pääTavoiteLista);
        pääTavoitePaneeli.add(muokkaaPääTavoitteita);

        JLabel sivuTavoiteOtsikko = new JLabel("Sivutavoitteet");
        sivuTavoiteLista = new JComboBox<>(TavoiteLista.tavoiteLista.keySet().toArray());
        JButton muokkaaSivuTavoitteita = new JButton("Muokkaa tavoitetta");
        muokkaaSivuTavoitteita.addActionListener(e -> {
            TavoitteenMuokkausIkkuna.luoTavoitteenMuokkausIkkuna(sivuTavoiteLista.getSelectedItem());
        });
        JPanel sivutavoitePaneeli = new JPanel();
        sivutavoitePaneeli.add(sivuTavoiteOtsikko);
        sivutavoitePaneeli.add(sivuTavoiteLista);
        sivutavoitePaneeli.add(muokkaaSivuTavoitteita);

        JButton okNappi = new JButton("OK");
        okNappi.setBounds(0, 0, 20, 20);
        okNappi.addActionListener(e -> {
            ikkuna.dispose();
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.setBounds(60, 0, 20, 20);
        cancelNappi.addActionListener(e -> {
            ikkuna.dispose();
        });

        JPanel alaPaneeli = new JPanel();
        alaPaneeli.setName("alaPaneeli");
        alaPaneeli.add(okNappi);
        alaPaneeli.add(cancelNappi);

        ikkuna.setLayout(new BorderLayout());
        ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        ikkuna.setVisible(true);
        ikkuna.add(pääTavoitePaneeli, BorderLayout.NORTH);
        ikkuna.add(sivutavoitePaneeli, BorderLayout.CENTER);
        ikkuna.add(alaPaneeli, BorderLayout.SOUTH);
    }
}
