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
    static JLabel otsikko, kuva;
    static JPanel kuvaPaneli;
    static JPanel alaPaneli;
    
    static JLabel häviöTeksti, tilastoTeksti;
    static JPanel tekstiPaneliVasen, tekstiPaneliOikea;

    static JLabel uusiPeli;
    static JLabel lopeta;
    static JPanel nappiPaneli;

    static int kelausViive = 0;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String loppuTeksti;
    static String loppuTekstiInfo;
    static ImageIcon loppuOtsikko;
    static ImageIcon loppuKuva;
    static int vaihtoehtojenMäärä = 2;
    static int valinta = 0;
    static JLabel[] vasenOsoitin;

    public static JPanel luoLoppuRuutu() {

        otsikko = new JLabel();
        otsikko.setPreferredSize(new Dimension(640, 100));

        kuva = new JLabel();
        kuva.setPreferredSize(new Dimension(640, 400));

        kuvaPaneli = new JPanel();
        kuvaPaneli.setLayout(new BorderLayout());
        kuvaPaneli.setSize(640, 360);
        kuvaPaneli.setBackground(Color.black);
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(otsikko, BorderLayout.NORTH);
        kuvaPaneli.add(kuva, BorderLayout.CENTER);


        häviöTeksti = new JLabel();
        häviöTeksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        häviöTeksti.setMinimumSize(new Dimension(320, 60));
        //häviöTeksti.setMaximumSize(new Dimension(320, 60));
        //häviöTeksti.setPreferredSize(new Dimension(320, 60));
        häviöTeksti.setAlignmentX(Component.CENTER_ALIGNMENT);
        häviöTeksti.setForeground(Color.white);

        tekstiPaneliVasen = new JPanel(new GridBagLayout());
        tekstiPaneliVasen.setBackground(Color.black);
        tekstiPaneliVasen.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
        tekstiPaneliVasen.setMinimumSize(new Dimension(320, 60));
        tekstiPaneliVasen.setMaximumSize(new Dimension(320, 60));
        tekstiPaneliVasen.setPreferredSize(new Dimension(320, 60));
        tekstiPaneliVasen.add(häviöTeksti);

        tilastoTeksti = new JLabel();
        tilastoTeksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        tilastoTeksti.setAlignmentX(Component.CENTER_ALIGNMENT);
        tilastoTeksti.setForeground(Color.white);

        tekstiPaneliOikea = new JPanel(new GridBagLayout());
        tekstiPaneliOikea.setBackground(Color.black);
        tekstiPaneliOikea.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
        tekstiPaneliOikea.setMinimumSize(new Dimension(320, 60));
        tekstiPaneliOikea.setMaximumSize(new Dimension(320, 60));
        tekstiPaneliOikea.setPreferredSize(new Dimension(320, 60));
        tekstiPaneliOikea.add(tilastoTeksti);

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
        nappiPaneli.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
        nappiPaneli.setMinimumSize(new Dimension(640, 130));
        nappiPaneli.setMaximumSize(new Dimension(640, 130));
        nappiPaneli.setPreferredSize(new Dimension(640, 130));
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
        //alaPaneli.setBorder(BorderFactory.createLineBorder(Color.orange, 1, true));
        gbc.gridx = 0;
        gbc.gridy = 0;
        alaPaneli.add(tekstiPaneliVasen, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        alaPaneli.add(tekstiPaneliOikea, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        alaPaneli.add(nappiPaneli, gbc);
        


        loppuruutuPaneli = new JPanel();
        loppuruutuPaneli.setLayout(new BorderLayout());
        loppuruutuPaneli.setBackground(Color.black);
        loppuruutuPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        loppuruutuPaneli.add(alaPaneli, BorderLayout.CENTER);
        loppuruutuPaneli.addKeyListener(new LoppuruudunKontrollit());

        JPanel loppuruutuPaneliUlompi = new JPanel();
        loppuruutuPaneliUlompi.setLayout(new GridBagLayout());
        loppuruutuPaneliUlompi.setBackground(Color.black);
        loppuruutuPaneliUlompi.add(loppuruutuPaneli);
        
        return loppuruutuPaneliUlompi;
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
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/voitto_otsikko.png");
            break;

            case KUOLEMA_TESTI:
                kelausViive = 20;
                loppuTeksti = "<html><p>" +
                "Hävisit pelin!" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_testi.jpg");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN:
                loppuTeksti = "<html><p>" +
                "Sait selkääsi!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pikkuvihu_passiivinen.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY:
                loppuTeksti = "<html><p>" +
                "Sait selkääsi!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pikkuvihu_lyöty.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY:
                loppuTeksti = "<html><p>" +
                "Sait selkääsi!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pikkuvihu_ämpäröity.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN:
                loppuTeksti = "<html><p>" + 
                "Sait selkääsi!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pahavihu_passiivinen.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY:
                loppuTeksti = "<html><p>" +
                "Sait selkääsi!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pahavihu_lyöty.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY:
                loppuTeksti = "<html><p>" +
                "Sait selkääsi!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_vihollinen_pahavihu_ämpäröity.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_JUHANI:
                kelausViive = 50;
                loppuTeksti = "<html><p>" +
                "Juhanille ei ryttyillä!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_juhani.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case KUOLEMA_SILLALTA_ALAS:
                kelausViive = 50;
                loppuTeksti = "<html><p>" +
                "Hyppäsit sillalta." + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_kuolema_silta.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case YLENSYÖNTI:
                loppuTeksti = "<html><p>" +
                "Söit liikaa ja sinulle tuli paha olo." + "<br>" + 
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case HIILTYNYT_MAKKARA:
                loppuTeksti = "<html><p>" +
                "Söit liikaa käristynyttä makkaraa ja tulit sairaaksi." + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case ALKOHOLIMYRKYTYS:
                loppuTeksti = "<html><p>" +
                "Joit liikaa ja sait alkoholimyrkytyksen." + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
            break;

            case VARTIJA:
                loppuTeksti = "<html><p>" +
                "Jäit kiinni näpistyksestä!" + "<br>" +
                "Hävisit pelin." + "<br>" +
                "</p></html>";
                loppuTekstiInfo = "<html><p>" +
                "Lyödyt viholliset: " + TarkistettavatArvot.lyödytVihut + "<br>" +
                "Ämpäröidyt viholliset: " + TarkistettavatArvot.ämpäröidytVihut + "<br>" +
                "</p></html>";
                loppuKuva = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_vartija.gif");
                loppuOtsikko = new ImageIcon("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
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

        häviöTeksti.setText(loppuTeksti);
        tilastoTeksti.setText(loppuTekstiInfo);
        otsikko.setIcon(loppuOtsikko);
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
