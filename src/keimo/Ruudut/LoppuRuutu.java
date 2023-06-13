package keimo.Ruudut;

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
    static JLabel kuva;
    static JPanel kuvaPaneli;
    static JPanel alaPaneli;
    
    static JLabel teksti;
    static JPanel tekstiPaneli;

    static JLabel uusiPeli;
    static JLabel lopeta;
    static JPanel nappiPaneli;

    static int kelausViive = 0;

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
        kuvaPaneli.setSize(640, 360);
        kuvaPaneli.setBackground(Color.black);
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);


        teksti = new JLabel();
        teksti.setMaximumSize(new Dimension(640, 200));
        teksti.setMinimumSize(new Dimension(500, 200));
        teksti.setPreferredSize(new Dimension(500, 200));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);
        teksti.setForeground(Color.white);

        tekstiPaneli = new JPanel(new GridBagLayout());
        tekstiPaneli.setBackground(Color.black);
        //tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, true));
        tekstiPaneli.setPreferredSize(new Dimension(500, 200));
        tekstiPaneli.setBounds(0, 0, 500, 300);
        tekstiPaneli.add(teksti);

        uusiPeli = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_uusipeli.png"));
        uusiPeli.setAlignmentX(Component.CENTER_ALIGNMENT);

        lopeta = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_lopeta.png"));
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        vasenOsoitin = new JLabel[vaihtoehtojenMäärä];
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            vasenOsoitin[i] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        }
        vasenOsoitin[valinta] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
    
        nappiPaneli = new JPanel();
        SpringLayout nappiPanelinlayout = new SpringLayout();
        nappiPanelinlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vasenOsoitin[0], 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneli);
        nappiPanelinlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, uusiPeli, 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneli);
        nappiPaneli.setLayout(nappiPanelinlayout);
        nappiPaneli.setBackground(Color.black);
        //nappiPaneli.setBorder(BorderFactory.createLineBorder(Color.blue, 1, true));
        nappiPaneli.setPreferredSize(new Dimension(500, 130));
        nappiPaneli.add(vasenOsoitin[0]);
        nappiPaneli.add(uusiPeli);
        nappiPaneli.add(vasenOsoitin[1]);
        nappiPaneli.add(lopeta);
        nappiPaneli.setVisible(false);
        SpringUtilities.makeCompactGrid(nappiPaneli, 2, 2, 6, 6, 6, 6);
    
        alaPaneli = new JPanel();
        GridBagLayout tekstiPanelinLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        alaPaneli.setLayout(tekstiPanelinLayout);
        alaPaneli.setBackground(Color.black);
        //alaPaneli.setBorder(BorderFactory.createLineBorder(Color.green, 1, true));
        gbc.gridx = 0;
        gbc.gridy = 0;
        alaPaneli.add(tekstiPaneli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        alaPaneli.add(nappiPaneli, gbc);
        


        loppuruutuPaneli = new JPanel();
        loppuruutuPaneli.setLayout(new BorderLayout());
        loppuruutuPaneli.setBackground(Color.black);
        loppuruutuPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        loppuruutuPaneli.add(alaPaneli, BorderLayout.CENTER);
        loppuruutuPaneli.addKeyListener(new LoppuruudunKontrollit());
        
        return loppuruutuPaneli;
    }

    public static void valitseLoppuRuutu(TarkistettavatArvot.PelinLopetukset pelinLopetus) {
        
        loppuruutuPaneli.requestFocus();
        kelausViive = 20;
        
        switch (pelinLopetus) {

            case NORMAALI_VOITTO:
                kelausViive = 20;
                loppuTeksti = "<html><p>" +
                "Voitit pelin!" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/voitto_normaali.jpg");
            break;

            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pikkuvihu_passiivinen.gif");
            break;

            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pikkuvihu_lyöty.gif");
            break;

            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pikkuvihu_ämpäröity.gif");
            break;

            case KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pahavihu_passiivinen.gif");
            break;

            case KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pahavihu_lyöty.gif");
            break;

            case KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY:
                loppuTeksti = "<html><p>" +
                "KEIM-Over!" + "<br><br>" + 
                "Sait selkääsi!" + "<br>" + 
                "Hävisit pelin.<br><br>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pahavihu_ämpäröity.gif");
            break;

            case KUOLEMA_JUHANI:
                kelausViive = 50;    
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
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
            break;

            case HIILTYNYT_MAKKARA:
                loppuTeksti = "<html><p>" +
                "Söit liikaa käristynyttä makkaraa ja tulit sairaaksi." + "<br>" +
                "Hävisit pelin." +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
            break;

            case ALKOHOLIMYRKYTYS:
                loppuTeksti = "<html><p>" +
                "Joit liikaa ja sait alkoholimyrkytyksen." + "<br>" + 
                "Hävisit pelin." +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
            break;

            default:
                loppuTeksti = "<html><p>" +
                "Vakioloppuruutu." +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/voitto.png");
            break;
        }

        kelausAjastin.start();

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

        if (kelausViive <= 0) {
            switch (valinta) {
                case 0: // Uusi peli
                    PääIkkuna.uusiIkkuna = true;
                    break;
                case 1: // Lopeta
                    System.exit(0);
                    break;
                default:
                    break;
            }
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

    static Timer kelausAjastin = new Timer(100, e -> {
        if (kelausViive > 0) {
            kelausViive--;
            nappiPaneli.setVisible(false);
        }
        else {
            nappiPaneli.setVisible(true);
        }
    });
}
