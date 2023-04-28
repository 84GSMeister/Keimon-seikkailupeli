package keimo.Väliruudut;

import keimo.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.io.File;

import javax.swing.*;
import javafx.scene.*;
import javafx.scene.media.*;
import javafx.scene.text.*;
import javafx.application.*;
import javafx.embed.swing.*;

public class OsionAlkuRuutu {
    
    public static JPanel osionAlkuPaneli;
    static JFXPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JLabel jatka;
    static JButton toista;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String[] tarinaTeksti = new String[tarinanPituusRuutuina];
    static ImageIcon[] tarinanKuva = new ImageIcon[tarinanPituusRuutuina];

    static MediaPlayer mediaPlayer;
    static MediaView video;

    static JFXPanel fxPanel;

    public static JPanel kokeileLuodaOsionAlkuPaneli() {
        JPanel paneli = null;
        try {
            /**
             * Käytä metodia luoOsionAlkuPaneliIlmanJFXää() mikäli JavaFX:n kirjastojen kanssa on ongelmaa.
             */
            //paneli = luoOsionAlkuPaneli();
            paneli = luoOsionAlkuPaneliIlmanJFXää();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ei voitu ladata JavaFX:n ajonaikaisia grafiikkakirjastoja.\nMediaelementit eivät välttämättä toimi.", "Virhe ladatessa kirjastoja", JOptionPane.WARNING_MESSAGE);
            paneli = luoOsionAlkuPaneliIlmanJFXää();
        }
        return paneli;
    }

    private static void luoJFXPaneli() {
        // This method is invoked on the EDT thread
        fxPanel = new JFXPanel();
        kuvaPaneli.add(fxPanel);
        kuvaPaneli.setSize(300, 200);
        kuvaPaneli.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
       });
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 640, 360, javafx.scene.paint.Color.ALICEBLUE);
        Text text = new Text();

        Media media = new Media(new File("tiedostot/videot/keimo_plaseholder_video.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        video = new MediaView(mediaPlayer);

        text.setX(40);
        text.setY(100);
        text.setFont(new javafx.scene.text.Font(25));
        text.setText("Welcome JavaFX!");

        video.setX(0);
        video.setY(0);
        video.setFitWidth(640);
        video.setFitHeight(360);

        //root.getChildren().add(text);
        root.getChildren().add(video);

        return (scene);
    }

    static JPanel luoOsionAlkuPaneli() {

        kuvaPaneli = new JFXPanel();
        kuvaPaneli.setSize(640, 400);
        kuvaPaneli.setLayout(new BorderLayout());
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));


        teksti = new JLabel("Tehtävä: Yritä löytää kotiin");
        teksti.setPreferredSize(new Dimension(640, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

        toista = new JButton("Toista video");
        toista.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.play();
                //System.out.println(mediaPlayer.getStatus());
                //System.out.println(mediaPlayer.getTotalDuration());
                //System.out.println(mediaPlayer.getError());
            }
        });
        toista.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        toista.setPreferredSize(new Dimension(640, 80));
        toista.setAlignmentX(Component.CENTER_ALIGNMENT);

        jatka = new JLabel("Seuraava");
        // jatka.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         PääIkkuna.crd.next(PääIkkuna.kortit);
        //         Peli.pause = false;
        //         PääIkkuna.ikkuna.requestFocus();
        //         Peli.peliAloitettu = true;
        //         mediaPlayer.stop();
        //     }
        // });
        jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        jatka.setPreferredSize(new Dimension(640, 80));
        jatka.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new FlowLayout(FlowLayout.TRAILING));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        tekstiPaneli.add(toista);
        tekstiPaneli.add(teksti);
        tekstiPaneli.add(jatka);

        luoJFXPaneli();

        osionAlkuPaneli = new JPanel();
        osionAlkuPaneli.setLayout(new BorderLayout());
        osionAlkuPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        osionAlkuPaneli.add(tekstiPaneli, BorderLayout.CENTER);
        
        return osionAlkuPaneli;
    }

    static JPanel luoOsionAlkuPaneliIlmanJFXää() {

        kuva = new JLabel(new ImageIcon("tiedostot/kuvat/menu/kansikuva_puisto.png"));
        kuva.setPreferredSize(new Dimension(640, 400));

        JPanel kuvaPaneli = new JPanel();
        kuvaPaneli.setSize(640, 400);
        kuvaPaneli.setLayout(new BorderLayout());
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);

        teksti = new JLabel("Tehtävä: Yritä löytää kotiin");
        teksti.setPreferredSize(new Dimension(640, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

        jatka = new JLabel("Space: Seuraava");
        // jatka.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         PääIkkuna.crd.next(PääIkkuna.kortit);
        //         Peli.pause = false;
        //         PääIkkuna.ikkuna.requestFocus();
        //         Peli.peliAloitettu = true;
        //         mediaPlayer.stop();
        //     }
        // });
        jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        jatka.setPreferredSize(new Dimension(640, 80));
        jatka.setAlignmentX(Component.CENTER_ALIGNMENT);
        jatka.requestFocus();
    
        tekstiPaneli = new JPanel();
        GridBagLayout tekstiPanelinLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        tekstiPaneli.setLayout(tekstiPanelinLayout);
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        tekstiPaneli.add(teksti, gbc);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 1;
        tekstiPaneli.add(jatka, gbc);

        osionAlkuPaneli = new JPanel();
        osionAlkuPaneli.setLayout(new BorderLayout());
        osionAlkuPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        osionAlkuPaneli.add(tekstiPaneli, BorderLayout.CENTER);
        osionAlkuPaneli.addKeyListener(new OsionAlkuruudunKontrollit());
        
        return osionAlkuPaneli;
    }

    static void jatka() {
        PääIkkuna.crd.next(PääIkkuna.kortit);
        Peli.pause = false;
        PääIkkuna.ikkuna.requestFocus();
        Peli.peliAloitettu = true;
    }

    static class OsionAlkuruudunKontrollit implements KeyListener {

        /*
         * Määritellään mitä eri näppäinkomennot tekee ja mitä metodeja kutsutaan
         */

        @Override
            public void keyTyped(KeyEvent e) {
                
            }
    
        @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER:
                        jatka();
                        break;
                    default:
                        System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
                        break;
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
            }
    }
}
