package keimo.HuoneEditori.TarinaEditori;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import keimo.Ikkunat.SelainIkkuna;

public class TarinanTekstinMuokkausIkkuna {
    
    static JFrame ikkuna;
    static JLabel otsikkoLabel;
    static JTextPane tekstikenttä;
    static JPanel tekstikenttäPaneli;
    static JScrollPane scrollattavaPaneli;

    static JButton okNappi, cancelNappi;
    static JButton taikaNappi;
    static JPanel nappiPaneliVasen, nappiPaneliOikea;
    static JFXPanel tekstiFXPaneli;

    public static void luoTarinanLisäysIkkuna(int tarinanSivu) {

        ikkuna = new JFrame("Muokkaa tekstiä");
        ikkuna.setBounds(0, 0, 640, 640);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        
        otsikkoLabel = new JLabel("TODO: Vois olla vähän parempi tekstieditori");

        tekstikenttä = new JTextPane();
        tekstikenttä.setContentType("text/html");
        tekstikenttä.setText(TarinaEditoriIkkuna.tarinanTekstit[tarinanSivu].getText());
        tekstikenttä.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        tekstikenttä.getStyledDocument().getText(0, tekstikenttä.getStyledDocument().getLength()-1);
                    }
                    catch (BadLocationException ble) {
                        ble.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            päivitäTeksti(tekstikenttä.getText());
                        }
                    });
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            päivitäTeksti(tekstikenttä.getText());
                        }
                    });
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            päivitäTeksti(tekstikenttä.getText());
                        }
                    });
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
            if (tekstikenttä.getText().startsWith("<html>")) {
                TarinaEditoriIkkuna.tarinanTekstit[tarinanSivu].setText(tekstikenttä.getText());
            }
            else {
                TarinaEditoriIkkuna.tarinanTekstit[tarinanSivu].setText("<html><p>" + tekstikenttä.getText() + "</p></html>");
            }
            TarinaEditoriIkkuna.päivitäTarinaKartta();
            ikkuna.dispose();
        });
        cancelNappi = new JButton("Peruuta");
        cancelNappi.addActionListener(e -> ikkuna.dispose());
        nappiPaneliVasen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nappiPaneliVasen.add(okNappi);
        nappiPaneliVasen.add(cancelNappi);

        taikaNappi = new JButton("?");
        taikaNappi.addActionListener(e -> {
            SelainIkkuna.luoSelainIkkuna();
        });
        nappiPaneliOikea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nappiPaneliOikea.add(taikaNappi);

        JPanel tekstinMuokkausPaneli = new JPanel(new BorderLayout());
        tekstinMuokkausPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
        tekstinMuokkausPaneli.add(otsikkoLabel, BorderLayout.NORTH);
        tekstinMuokkausPaneli.add(scrollattavaPaneli, BorderLayout.CENTER);

        tekstiFXPaneli = new JFXPanel();
        tekstiFXPaneli.setAlignmentX(0.5f);
        initFX();
        JPanel tekstinTarkasteluPaneli = new JPanel(new BorderLayout());
        tekstinTarkasteluPaneli.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        tekstinTarkasteluPaneli.add(tekstiFXPaneli);

        JPanel yläPaneli = new JPanel(new BorderLayout());
        yläPaneli.add(tekstinTarkasteluPaneli, BorderLayout.CENTER);

        JPanel keskiPaneli = new JPanel(new BorderLayout());
        keskiPaneli.add(tekstinMuokkausPaneli, BorderLayout.CENTER);

        JPanel alaPaneli = new JPanel(new BorderLayout()); 
        alaPaneli.add(nappiPaneliVasen, BorderLayout.WEST);
        alaPaneli.add(nappiPaneliOikea, BorderLayout.EAST);

        ikkuna.setLayout(new BorderLayout());
        ikkuna.setLocationRelativeTo(TarinaEditoriIkkuna.ikkuna);
        ikkuna.add(yläPaneli, BorderLayout.NORTH);
        ikkuna.add(keskiPaneli, BorderLayout.CENTER);
        ikkuna.add(alaPaneli, BorderLayout.SOUTH);
        ikkuna.setVisible(true);
    }

    private static void initFX() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = luoJFXPaneli();
                tekstiFXPaneli.setScene(scene);
            }
        });
    }

    static WebView htmlTekstikenttä;
    private static Scene luoJFXPaneli() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 622, 360, javafx.scene.paint.Color.ALICEBLUE);
        
        htmlTekstikenttä = new WebView();
        htmlTekstikenttä.setPrefSize(622, 360);
        htmlTekstikenttä.getEngine().loadContent(tekstikenttä.getText());

        StackPane paneeli = new StackPane();
        paneeli.setMinSize(622, 360);
        paneeli.setPrefSize(1700, 360);
        StackPane.setAlignment(htmlTekstikenttä, Pos.CENTER_RIGHT);
        paneeli.getChildren().add(htmlTekstikenttä);
        paneeli.setBackground(Background.fill(javafx.scene.paint.Color.GOLD));

        root.getChildren().add(paneeli);

        return scene;
    }

    private static void päivitäTeksti(String teksti) {
        htmlTekstikenttä.getEngine().loadContent(teksti);
    }
}
