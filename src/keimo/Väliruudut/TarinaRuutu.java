package keimo.Väliruudut;

import keimo.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

public class TarinaRuutu {
    
    public static JPanel tarinaPaneli = luoTarinaPaneli("alku");
    static JPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JLabel jatka;

    static int klikkaustenMäärä;
    static int tarinanPituusRuutuina;
    static String[] tarinaTeksti;
    static ImageIcon[] tarinanKuva;
    static String tarinanTunniste;

    static GridBagLayout tekstiPanelinLayout;;
    static GridBagConstraints gbc;
    public static JPanel yhdistettyTarinaPaneli;
    

    public static JPanel luoTarinaPaneli(String tarina) {

        tarinanTunniste = tarina;
        switch (tarina) {
            case "alku":
                klikkaustenMäärä = 0;
                tarinanPituusRuutuina = 3;
                tarinaTeksti = new String[tarinanPituusRuutuina];
                tarinanKuva = new ImageIcon[tarinanPituusRuutuina];
            
                tarinanKuva[0] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_1.png");
                tarinanKuva[1] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_2.gif");
                tarinanKuva[2] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_3.png");
                //tarinanKuva[3] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_4.png");

                tarinaTeksti[0] = "<html><p>" +
                "Keimo herää auringon säteiden aiheuttamaan kipuun. " +
                "Nämä normaalisti harmittomat luonnonilmiöt olivat päättäneet, että juuri Keimon tulisi kärsiä seuraamuksia eilispäivän nautinnosta. " +
                "Yksikään kuolevainen ei ole kokenut yhtä murhaavaa krapulaa, kuin sitä, joka yritti paraikaa pitää Keimon maassa. " +
                "Keimo ei kuitenkaan ollut tavallinen kuolevainen, ja hän kykeni yli-inhimillisellä tahdonvoimallaan avaamaan silmänsä ja katsomaan ympärilleen. " +
                "</p></html>";

                tarinaTeksti[1] = "<html><p>" +
                "Valonsäteet jatkoivat Keimon kiusaamista myös sen jälkeen, kun hän oli saanut silmänsä auki. " +
                "Säteet eivät tuoneet hänen verkkokalvoilleen kuvia, joita hän olisi tunnistanut. " +
                "Sen sijaan Keimon verkkokalvot täyttyivät kuvista, jotka olisivat tuhonneet heikomman mielen." +
                "</p></html>";

                tarinaTeksti[2] = "<html><p>" +
                "Puisto on autioitunut tavallisista ihmisistä.<br>" +
                "Niiden sijaan puistot ja kadut ovat tulvillaan ilkeän näköisiä, aseistautuneita hyypiöitä.<br>" +
                "Ne ryntäilevät nopeasti pensaiden välillä, kuin etsien vankikarkuria.<br>" +
                "Tunnelma on uhkaava, ja huolestuttavat ajatukset vaivaavat sankariamme.<br>" +
                "<br>" +
                "Etsivätkö ne minua? Ovatko ne hävittäneet jo kaikki muut?<br>" +
                "Mitä hirveyksiä täällä tapahtuu? Minun on heti päästävä kotiin!" +
                "</p></html>";

                // tarinaTeksti[3] = "<html><p>" +
                // "Sitten radiosta kuului lyhyt ilmoitus, jonka suuruuden vain Keimo käsitti: <br>" +
                // "''Alienit ovat hyökänneet Suomeen. Pyydämme kaikkia Suomen kansalaisia palaamaan koteihinsa ja aseistamaan itsensä pesäpallomailoilla itsepuolustusta varten.''" +
                // "</p></html>";

                kuva = new JLabel(tarinanKuva[0]);
                //kuva.setPreferredSize(new Dimension(640, 400));

                kuvaPaneli = new JPanel();
                kuvaPaneli.setBounds(0, 0, 640, 400);
                kuvaPaneli.setBackground(Color.black);
                kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                kuvaPaneli.add(kuva);


                teksti = new JLabel(tarinaTeksti[0]);
                teksti.setMinimumSize(new Dimension(560, 180));
                teksti.setBounds(0, 0, 1000, 240);
                teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
                teksti.setForeground(Color.white);
                teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

                jatka = new JLabel("Space: Jatka");
                jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
                jatka.setForeground(Color.white);
                jatka.setPreferredSize(new Dimension(640, 80));
                jatka.setAlignmentX(Component.CENTER_ALIGNMENT);
            
                tekstiPaneli = new JPanel();
                tekstiPanelinLayout = new GridBagLayout();
                gbc = new GridBagConstraints();
                tekstiPaneli.setLayout(tekstiPanelinLayout);
                tekstiPaneli.setBounds(0, 0, 640, 320);
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
                tekstiPaneli.add(jatka, gbc);


                tarinaPaneli = new JPanel();
                tarinaPaneli.setLayout(new BorderLayout());
                tarinaPaneli.addKeyListener(new TarinaRuudunKontrollit());
                tarinaPaneli.add(kuvaPaneli, BorderLayout.NORTH);
                tarinaPaneli.add(tekstiPaneli, BorderLayout.CENTER);
                tarinaPaneli.requestFocus();

            break;
            case "koti":
                klikkaustenMäärä = 0;
                tarinanPituusRuutuina = 1;
                tarinaTeksti = new String[tarinanPituusRuutuina];
                tarinanKuva = new ImageIcon[tarinanPituusRuutuina];
            
                tarinanKuva[0] = new ImageIcon("tiedostot/kuvat/tarina/koti/tarina_koti.png");

                tarinaTeksti[0] = "<html><p>" +
                "Keimo saapuu kotiin ja huomaa kaikkien tavaroiden olevan levällään.<br>" +
                "Koti on täydellisessä kaaoksessa.<br>" +
                "Näyttää siltä kuin joku olisi murtautunut Keimon kotiin ja penkonut tavaroita.<br><br>" +
                "Kuka täällä on käynyt ja mitä hän haluaa?<br>" +
                "Parasta napata jotain kättä pidempää ja lähteä selvittämään asiaa.<br>" +
                "</p></html>";

                kuva = new JLabel(tarinanKuva[0]);
                //kuva.setPreferredSize(new Dimension(640, 400));

                kuvaPaneli = new JPanel();
                kuvaPaneli.setBounds(0, 0, 640, 400);
                kuvaPaneli.setBackground(Color.black);
                kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                kuvaPaneli.add(kuva);


                teksti = new JLabel(tarinaTeksti[0]);
                teksti.setMinimumSize(new Dimension(560, 180));
                teksti.setBounds(0, 0, 1000, 240);
                teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
                teksti.setForeground(Color.white);
                teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

                jatka = new JLabel("Space: Jatka");
                jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
                jatka.setForeground(Color.white);
                jatka.setPreferredSize(new Dimension(640, 80));
                jatka.setAlignmentX(Component.CENTER_ALIGNMENT);
            
                tekstiPaneli = new JPanel();
                tekstiPanelinLayout = new GridBagLayout();
                gbc = new GridBagConstraints();
                tekstiPaneli.setLayout(tekstiPanelinLayout);
                tekstiPaneli.setBounds(0, 0, 640, 320);
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
                tekstiPaneli.add(jatka, gbc);


                tarinaPaneli = new JPanel();
                tarinaPaneli.setLayout(new BorderLayout());
                tarinaPaneli.addKeyListener(new TarinaRuudunKontrollit());
                tarinaPaneli.add(kuvaPaneli, BorderLayout.NORTH);
                tarinaPaneli.add(tekstiPaneli, BorderLayout.CENTER);
                tarinaPaneli.requestFocus();

            break;
        }
        return tarinaPaneli;
    }

    static void jatka() {
        klikkaustenMäärä++;
        tarinaPaneli = new JPanel();
        //tarinaPaneli = luoTarinaPaneli("koti");
        if (klikkaustenMäärä >= tarinanPituusRuutuina -1 && tarinanTunniste == "alku") {
            jatka.setText("Space: Aloita peli");
        }
        if (klikkaustenMäärä >= tarinanPituusRuutuina) {
            if (tarinanTunniste == "alku") {
                PääIkkuna.crd.next(PääIkkuna.kortit);
                ValikkoRuutu.nappiPaneliAlkuvalikko.requestFocus();
            }
            else {
                PääIkkuna.crd.previous(PääIkkuna.kortit);
                PääIkkuna.crd.previous(PääIkkuna.kortit);
                PääIkkuna.ikkuna.requestFocus();
                PääIkkuna.kortit.remove(TarinaRuutu.tarinaPaneli);
                Pelaaja.pakotaPelaajanPysäytys();
                Peli.pause = false;
            }
        }
        else {
            teksti.setText(tarinaTeksti[klikkaustenMäärä]);
            kuva.setIcon(tarinanKuva[klikkaustenMäärä]);
        }
    }

    static class TarinaRuudunKontrollit implements KeyListener {

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
