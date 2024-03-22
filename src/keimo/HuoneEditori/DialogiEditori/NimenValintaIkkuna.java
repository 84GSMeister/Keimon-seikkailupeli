package keimo.HuoneEditori.DialogiEditori;

import keimo.PääIkkuna;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;

public class NimenValintaIkkuna {

    static JFrame ikkuna;
    static JLabel otsikkoLabel;
    static JTextField nimiTekstikenttä;
    static JPanel tekstikenttäPaneli;

    static JButton okNappi, cancelNappi;
    static JPanel nappiPaneli;

    public static void luoNimenValintaIkkuna() {

        ikkuna = new JFrame("Muuta nimeä");
        ikkuna.setBounds(0, 0, 400, 110);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        
        otsikkoLabel = new JLabel("Dialogin nimi");
        nimiTekstikenttä = new JTextField();
        tekstikenttäPaneli = new JPanel(new BorderLayout());
        tekstikenttäPaneli.add(otsikkoLabel, BorderLayout.NORTH);
        tekstikenttäPaneli.add(nimiTekstikenttä, BorderLayout.CENTER);

        okNappi = new JButton("OK");
        okNappi.addActionListener(e -> {
            String syöteTeksti = nimiTekstikenttä.getText();
            if ((syöteTeksti == null) || (syöteTeksti == "") || (syöteTeksti.startsWith(" ")) || (syöteTeksti.length() < 1) || (syöteTeksti.contains("(")) || (syöteTeksti.contains(")"))) {
                JOptionPane.showMessageDialog(null, "Syöte ei voi olla tyhjä, alkaa välilyönnillä tai sisältää sulkumerkkejä.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
            }
            else if (DialogiEditoriIkkuna.editorinDialogiKartta.containsKey(syöteTeksti)) {
                JOptionPane.showMessageDialog(null, "Tarinapätkä " + PääIkkuna.lainausmerkki + syöteTeksti + PääIkkuna.lainausmerkki + " on jo olemassa.", "Tarinapätkä on jo olemassa", JOptionPane.ERROR_MESSAGE);
            }
            else {
                DialogiEditoriIkkuna.nimeäUudelleenDialogiPätkä(nimiTekstikenttä.getText());
                ikkuna.dispose();
            }
        });
        cancelNappi = new JButton("Peruuta");
        cancelNappi.addActionListener(e -> ikkuna.dispose());
        nappiPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nappiPaneli.add(okNappi);
        nappiPaneli.add(cancelNappi);

        ikkuna.setLayout(new BorderLayout());
        ikkuna.setLocationRelativeTo(DialogiEditoriIkkuna.ikkuna);
        ikkuna.add(tekstikenttäPaneli, BorderLayout.CENTER);
        ikkuna.add(nappiPaneli, BorderLayout.SOUTH);
        ikkuna.setVisible(true);
    }
}
