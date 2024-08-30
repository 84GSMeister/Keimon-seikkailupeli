package keimo.HuoneEditori.TavoiteEditori;

import keimo.HuoneEditori.Keimo3D.Keimo3D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;

public class TavoitteenMuokkausIkkuna {
    
    public static JFrame ikkuna;
    static JLabel otsikkoLabel;
    static JTextPane tekstikenttä;
    static JPanel tekstikenttäPaneli;
    static JScrollPane scrollattavaPaneli;

    static JButton okNappi, cancelNappi;
    static JButton taikaNappi;
    static JPanel nappiPaneliVasen, nappiPaneliOikea;

    public static void luoTavoitteenMuokkausIkkuna(Object valittuTavoite) {

        ikkuna = new JFrame("Muokkaa tavoitetta");
        ikkuna.setBounds(0, 0, 640, 640);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        
        otsikkoLabel = new JLabel("TODO: Vois olla vähän parempi tekstieditori");

        tekstikenttä = new JTextPane();
        tekstikenttä.setContentType("text/html");
        tekstikenttä.setText("" + valittuTavoite);
        tekstikenttä.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        tekstikenttä.getStyledDocument().getText(0, tekstikenttä.getStyledDocument().getLength()-1);
                    }
                    catch (BadLocationException ble) {
                        ble.printStackTrace();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {

                }
                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });

        tekstikenttäPaneli = new JPanel(new BorderLayout());
        //tekstikenttäPaneli.add(otsikkoLabel, BorderLayout.NORTH);
        tekstikenttäPaneli.add(tekstikenttä, BorderLayout.CENTER);

        scrollattavaPaneli = new JScrollPane(tekstikenttäPaneli);
        //scrollattavaPaneli.setPreferredSize(new Dimension(622, 200));
        scrollattavaPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollattavaPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        okNappi = new JButton("OK");
        okNappi.addActionListener(e -> {
            // if (tekstikenttä.getText().startsWith("<html>")) {
            //     TarinaEditoriIkkuna.tarinanTekstit[tarinanSivu].setText(tekstikenttä.getText());
            // }
            // else {
            //     TarinaEditoriIkkuna.tarinanTekstit[tarinanSivu].setText("<html><p>" + tekstikenttä.getText() + "</p></html>");
            // }
            // TarinaEditoriIkkuna.päivitäTarinaKartta();
            ikkuna.dispose();
        });
        cancelNappi = new JButton("Peruuta");
        cancelNappi.addActionListener(e -> ikkuna.dispose());
        nappiPaneliVasen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nappiPaneliVasen.add(okNappi);
        nappiPaneliVasen.add(cancelNappi);

        taikaNappi = new JButton("?");
        taikaNappi.addActionListener(e -> {
            Keimo3D.käynnistäKeimo3D();
        });
        nappiPaneliOikea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nappiPaneliOikea.add(taikaNappi);

        JPanel tekstinMuokkausPaneli = new JPanel(new BorderLayout());
        tekstinMuokkausPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
        tekstinMuokkausPaneli.add(otsikkoLabel, BorderLayout.NORTH);
        tekstinMuokkausPaneli.add(scrollattavaPaneli, BorderLayout.CENTER);

        JPanel keskiPaneli = new JPanel(new BorderLayout());
        keskiPaneli.add(tekstinMuokkausPaneli, BorderLayout.CENTER);

        JPanel alaPaneli = new JPanel(new BorderLayout()); 
        alaPaneli.add(nappiPaneliVasen, BorderLayout.WEST);
        alaPaneli.add(nappiPaneliOikea, BorderLayout.EAST);

        ikkuna.setLayout(new BorderLayout());
        ikkuna.setLocationRelativeTo(TavoiteEditoriIkkuna.ikkuna);
        ikkuna.add(keskiPaneli, BorderLayout.CENTER);
        ikkuna.add(alaPaneli, BorderLayout.SOUTH);
        ikkuna.setVisible(true);
    }
}
