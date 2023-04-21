package keimo.Väliruudut;

import keimo.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

public class LoppuRuutu {
    
    static JPanel tarinaPaneli;
    static JPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JButton uusiPeli, lopeta;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String loppuTeksti;
    static ImageIcon loppuKuva;

    public static JPanel luoLoppuRuutu() {

        kuva = new JLabel();
        kuva.setPreferredSize(new Dimension(640, 400));

        kuvaPaneli = new JPanel();
        kuvaPaneli.setSize(640, 400);
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);


        teksti = new JLabel();
        teksti.setMinimumSize(new Dimension(560, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

        uusiPeli = new JButton("Uusi peli");
        uusiPeli.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.ikkuna.dispose();
                Peli.uusiPeli();
            }
        });
        uusiPeli.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        uusiPeli.setPreferredSize(new Dimension(320, 80));
        uusiPeli.setAlignmentX(Component.CENTER_ALIGNMENT);

        lopeta = new JButton("Lopeta");
        lopeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        lopeta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        lopeta.setPreferredSize(new Dimension(320, 80));
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        tekstiPaneli = new JPanel();
        GridBagLayout tekstiPanelinLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        tekstiPaneli.setLayout(tekstiPanelinLayout);
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2;
        tekstiPaneli.add(teksti, gbc);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 1;
        tekstiPaneli.add(uusiPeli, gbc);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 1;
        tekstiPaneli.add(lopeta, gbc);


        tarinaPaneli = new JPanel();
        tarinaPaneli.setLayout(new BorderLayout());
        tarinaPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        tarinaPaneli.add(tekstiPaneli, BorderLayout.CENTER);
        
        return tarinaPaneli;
    }

    public static void valitseLoppuRuutu(TarkistettavatArvot.PelinLopetukset pelinLopetus) {
        switch (pelinLopetus) {

            case NORMAALI_VOITTO:
                loppuTeksti = "<html><p>" +
                "Voitit pelin!" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/voitto_normaali.png");
            break;

            case KUOLEMA_VIHOLLINEN_NEUTRAALI:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/haviö_kuolema_vihollinen_neutraali.gif");
            break;

            case KUOLEMA_VIHOLLINEN_PIESTY:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi! (piesty)" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/haviö_kuolema_vihollinen_piesty.gif");
            break;

            case KUOLEMA_VIHOLLINEN_ÄMPÄRÖITY:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi! (ämpäröity)" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/haviö_kuolema_vihollinen_ämpäröity.gif");
            break;

            case KUOLEMA_JUHANI:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" +
                "Juhanille ei ryttyillä!" + "<br>" +
                "Hävisit pelin." +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_juhani.gif");
            break;

            case YLENSYÖNTI:
                loppuTeksti = "<html><p>" +
                "Söit liikaa ja sinulle tuli paha olo." + "<br>" + 
                "Hävisit pelin." +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/haviö_ylensyönti.gif");
            break;

            default:
                loppuTeksti = "<html><p>" +
                "Vakioloppuruutu." +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/voitto.png");
            break;
        }

        //Uudelleenkäynnistä gif-animaatio
        Image img = loppuKuva.getImage();
        img.flush();
        loppuKuva = new ImageIcon(img);

        teksti.setText(loppuTeksti);
        kuva.setIcon(loppuKuva);
    }
}
