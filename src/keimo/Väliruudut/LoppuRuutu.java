package keimo.Väliruudut;

import keimo.*;
import keimo.Utility.SpringUtilities;

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
    
    public static JPanel loppuruutuPaneli;
    static JPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JButton uusiPeli, lopeta;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String loppuTeksti;
    static ImageIcon loppuKuva;
    static int vaihtoehtojenMäärä = 2;
    static int valinta = 0;
    static JLabel[] vasenOsoitin;

    public static JPanel luoLoppuRuutu() {

        kuva = new JLabel();
        kuva.setPreferredSize(new Dimension(640, 400));

        kuvaPaneli = new JPanel();
        kuvaPaneli.setSize(640, 400);
        kuvaPaneli.setBackground(Color.black);
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);


        teksti = new JLabel();
        teksti.setMinimumSize(new Dimension(560, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);
        teksti.setForeground(Color.white);

        // uusiPeli = new JButton("Uusi peli");
        // uusiPeli.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         PääIkkuna.ikkuna.dispose();
        //         Peli.uusiPeli();
        //     }
        // });
        // uusiPeli.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        // uusiPeli.setPreferredSize(new Dimension(320, 80));
        // uusiPeli.setAlignmentX(Component.CENTER_ALIGNMENT);

        // lopeta = new JButton("Lopeta");
        // lopeta.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         System.exit(0);
        //     }
        // });
        // lopeta.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        // lopeta.setPreferredSize(new Dimension(320, 80));
        // lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel uusiPeli = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_uusipeli.png"));
        uusiPeli.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lopeta = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_lopeta.png"));
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        vasenOsoitin = new JLabel[vaihtoehtojenMäärä];
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            vasenOsoitin[i] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        }
        vasenOsoitin[valinta] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
    
        JPanel nappiPaneli = new JPanel();
        SpringLayout nappiPanelinlayout = new SpringLayout();
        nappiPanelinlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vasenOsoitin[0], 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneli);
        nappiPanelinlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, uusiPeli, 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneli);
        nappiPaneli.setLayout(nappiPanelinlayout);
        nappiPaneli.setBackground(Color.black);
        nappiPaneli.setPreferredSize(new Dimension(500, 400));
        nappiPaneli.add(vasenOsoitin[0]);
        nappiPaneli.add(uusiPeli);
        nappiPaneli.add(vasenOsoitin[1]);
        nappiPaneli.add(lopeta);
        SpringUtilities.makeCompactGrid(nappiPaneli, 2, 2, 6, 6, 6, 6);
    
        tekstiPaneli = new JPanel();
        GridBagLayout tekstiPanelinLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        tekstiPaneli.setLayout(tekstiPanelinLayout);
        tekstiPaneli.setBackground(Color.black);
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
        tekstiPaneli.add(nappiPaneli, gbc);
        // gbc.ipadx = 10;
        // gbc.ipady = 10;
        // gbc.gridx = 1;
        // gbc.gridy = 1;
        // tekstiPaneli.add(lopeta, gbc);

        


        loppuruutuPaneli = new JPanel();
        loppuruutuPaneli.setLayout(new BorderLayout());
        loppuruutuPaneli.setBackground(Color.black);
        loppuruutuPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        loppuruutuPaneli.add(tekstiPaneli, BorderLayout.CENTER);
        loppuruutuPaneli.addKeyListener(new LoppuruudunKontrollit());
        
        return loppuruutuPaneli;
    }

    public static void valitseLoppuRuutu(TarkistettavatArvot.PelinLopetukset pelinLopetus) {
        
        loppuruutuPaneli.requestFocus();
        
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

    public static void painaNäppäintä(String näppäin) {

        switch (näppäin) {
            case "ylös":
                valinta--;
                if (valinta < 0) {
                    valinta = 1;
                }
                päivitäOsoittimenSijainti(valinta);
                break;
            case "alas":
                valinta++;
                if (valinta > 1) {
                    valinta = 0;
                }
                päivitäOsoittimenSijainti(valinta);
                break;
            case "enter":
                hyväksy(valinta);
                break;
            default:
                break;
        }
    }

    static void päivitäOsoittimenSijainti(int valinta) {
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            if (i == valinta) {
                vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
            }
            else {
                vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            }
        }
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Uusi peli
                PääIkkuna.ikkuna.dispose();
                Peli.uusiPeli();
                break;
            case 1: // Lopeta
                System.exit(0);
                break;
            default:
                break;
        }
    }

    static class LoppuruudunKontrollit implements KeyListener {

        /*
         * Määritellään mitä eri näppäinkomennot tekee ja mitä metodeja kutsutaan
         */

        @Override
            public void keyTyped(KeyEvent e) {
                
            }
    
        @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                        painaNäppäintä("alas");
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        painaNäppäintä("ylös");
                        break;
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER:
                        painaNäppäintä("enter");
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
