package keimo.Ikkunat;

import keimo.PääIkkuna;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

public class SelainIkkuna {
    static JFrame ikkuna;
    static JLabel otsikkoLabel;
    static String webOsoite84gs = "http://www.84gs.xyz";
    static JTextField tekstikenttä;
    static JButton selaaNappi;
    static JPanel tekstikenttäPaneli;

    static JButton okNappi, cancelNappi;
    static JPanel nappiPaneliVasen;
    static JFXPanel tekstiFXPaneli;

    public static void luoSelainIkkuna() {

        ikkuna = new JFrame("Selain");
        ikkuna.setBounds(0, 0, 640, 640);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());

        tekstikenttä = new JTextField(webOsoite84gs);
        tekstikenttä.setPreferredSize(new Dimension(500, 25));
        tekstikenttä.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    
                }
            });
        selaaNappi = new JButton("Selaa");
        selaaNappi.addActionListener(e -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String webOsoite = "";
                    webOsoite += tekstikenttä.getText();
                    htmlAlue.getEngine().load(webOsoite);
                    tekstikenttä.setText(htmlAlue.getEngine().getLocation());
                }
            });
        });

        tekstikenttäPaneli = new JPanel();
        tekstikenttäPaneli.add(tekstikenttä);
        tekstikenttäPaneli.add(selaaNappi);

        okNappi = new JButton("OK");
        okNappi.addActionListener(e -> ikkuna.dispose());
        cancelNappi = new JButton("Peruuta");
        cancelNappi.addActionListener(e -> ikkuna.dispose());
        nappiPaneliVasen = new JPanel();
        nappiPaneliVasen.add(okNappi);
        nappiPaneliVasen.add(cancelNappi);

        JPanel tekstinMuokkausPaneli = new JPanel(new BorderLayout());
        tekstinMuokkausPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
        tekstinMuokkausPaneli.add(tekstikenttäPaneli, BorderLayout.CENTER);

        tekstiFXPaneli = new JFXPanel();
        tekstiFXPaneli.setAlignmentX(0.5f);
        initFX();
        JPanel tekstinTarkasteluPaneli = new JPanel(new BorderLayout());
        tekstinTarkasteluPaneli.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        tekstinTarkasteluPaneli.add(tekstiFXPaneli);

        JPanel yläPaneli = new JPanel(new BorderLayout());
        yläPaneli.add(tekstinMuokkausPaneli, BorderLayout.CENTER);

        JPanel keskiPaneli = new JPanel(new BorderLayout());
        keskiPaneli.add(tekstinTarkasteluPaneli, BorderLayout.CENTER);

        JPanel alaPaneli = new JPanel(new BorderLayout()); 
        alaPaneli.add(nappiPaneliVasen, BorderLayout.WEST);

        ikkuna.setLayout(new BorderLayout());
        ikkuna.setLocationRelativeTo(PääIkkuna.ikkuna);
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
                htmlAlue.getEngine().load(webOsoite84gs);
                htmlAlue.getEngine().setOnError(new EventHandler<WebErrorEvent>() {
                    @Override
                    public void handle(WebErrorEvent e) {
                        System.out.println(e.getMessage());
                    }
                });
                htmlAlue.getEngine().load(webOsoite84gs);
                try {
                    Thread.sleep(500);
                } 
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                htmlAlue.getEngine().load(webOsoite84gs);
            }
        });
    }

    static WebView htmlAlue;
    private static Scene luoJFXPaneli() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 620, 520, javafx.scene.paint.Color.ALICEBLUE);
        
        htmlAlue = new WebView();
        htmlAlue.setPrefSize(620, 520);

        StackPane paneeli = new StackPane();
        paneeli.setMinSize(620, 520);
        paneeli.setPrefSize(1700, 520);
        StackPane.setAlignment(htmlAlue, Pos.CENTER_RIGHT);
        paneeli.getChildren().add(htmlAlue);

        root.getChildren().add(paneeli);

        return scene;
    }
}
