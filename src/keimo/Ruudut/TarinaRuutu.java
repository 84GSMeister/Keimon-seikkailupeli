package keimo.Ruudut;

import keimo.*;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.Ikkunat.LatausIkkuna;
import keimo.Utility.KeimoFontit;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
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

    static GridBagLayout tekstiPanelinLayout;
    static GridBagConstraints gbc;

    static GridBagLayout tarinaPanelinLayout;
    static GridBagConstraints tarinaPanelinGBC;
    //public static JPanel yhdistettyTarinaPaneli;

    public static JPanel luoTarinaPaneli(String tarina) {

        try {
            tarinanTunniste = tarina;
            //TarinaPätkä tarinaPätkä = TarinaDialogiLista.luoTarina(tarina);
            TarinaPätkä tarinaPätkä = TarinaDialogiLista.tarinaKartta.get(tarina);
            klikkaustenMäärä = 0;
            tarinanPituusRuutuina = tarinaPätkä.annaPituus();
            tarinaTeksti = tarinaPätkä.annaTekstit();
            tarinanKuva = tarinaPätkä.annaKuvat();

            Image img = tarinanKuva[0].getImage();
            img.flush();
            kuva = new JLabel(new ImageIcon(img));
            //kuva.setBorder(BorderFactory.createLineBorder(Color.white, 1));

            kuvaPaneli = new JPanel();
            kuvaPaneli.setBounds(0, 0, 640, 400);
            kuvaPaneli.setBackground(Color.black);
            kuvaPaneli.add(kuva);


            teksti = new JLabel(tarinaTeksti[0]);
            teksti.setMinimumSize(new Dimension(640, 225));
            //teksti.setBorder(BorderFactory.createLineBorder(Color.white));
            //teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
            teksti.setFont(KeimoFontit.fontti_keimo_14);
            teksti.setForeground(Color.white);
            teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

            

            jatka = new JLabel("Space: Jatka");
            //jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
            jatka.setFont(KeimoFontit.fontti_keimo_30);
            jatka.setForeground(Color.white);
            jatka.setBounds(0, 0, 640, 40);
            jatka.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel tekstiPaneli = new JPanel(null);
            tekstiPanelinLayout = new GridBagLayout();
            gbc = new GridBagConstraints();
            tekstiPaneli.setLayout(tekstiPanelinLayout);
            tekstiPaneli.setPreferredSize(new Dimension(640, 280));
            tekstiPaneli.setBackground(Color.black);
            //tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.white, 1, false));
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.ABOVE_BASELINE_TRAILING;
            tekstiPaneli.add(teksti, gbc);
            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.BELOW_BASELINE;
            tekstiPaneli.add(jatka, gbc);


            JPanel tarinaPaneliSisempi = new JPanel();
            tarinaPaneliSisempi.setBackground(Color.black);
            tarinaPaneliSisempi.setLayout(new BorderLayout());
            tarinaPaneliSisempi.add(kuvaPaneli, BorderLayout.NORTH);
            tarinaPaneliSisempi.add(tekstiPaneli, BorderLayout.CENTER);

            tarinaPaneli = new JPanel();
            tarinaPaneli.addKeyListener(new TarinaRuudunKontrollit());
            tarinaPaneli.setBackground(Color.black);
            tarinaPaneli.setLayout(new GridBagLayout());
            tarinaPaneli.add(tarinaPaneliSisempi);

            return tarinaPaneli;
        }
        catch (NullPointerException npe) {

            npe.printStackTrace();
            JOptionPane.showMessageDialog(LatausIkkuna.ikkuna(), "Tarinaa ei voitu ladata. Tämä voi johtua vanhentuneesta default.kst -tiedostosta.", "Tarinaa ei löytynyt", JOptionPane.ERROR_MESSAGE);
            
            kuva = new JLabel("Virhe");

            kuvaPaneli = new JPanel();
            kuvaPaneli.setBounds(0, 0, 640, 400);
            kuvaPaneli.setBackground(Color.black);
            kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
            kuvaPaneli.add(kuva);


            teksti = new JLabel("Virhe");
            teksti.setMinimumSize(new Dimension(640, 225));
            teksti.setBorder(BorderFactory.createLineBorder(Color.white));
            //teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
            teksti.setFont(KeimoFontit.fontti_keimo_14);
            teksti.setForeground(Color.white);
            teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

            jatka = new JLabel("Space: Jatka");
            //jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
            jatka.setFont(KeimoFontit.fontti_keimo_30);
            jatka.setForeground(Color.white);
            jatka.setPreferredSize(new Dimension(640, 40));
            jatka.setAlignmentX(Component.CENTER_ALIGNMENT);

        
            tekstiPaneli = new JPanel();
            tekstiPanelinLayout = new GridBagLayout();
            gbc = new GridBagConstraints();
            tekstiPaneli.setLayout(tekstiPanelinLayout);
            tekstiPaneli.setBounds(0, 0, 640, 320);
            tekstiPaneli.setBackground(Color.black);
            tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
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



            JPanel tarinaPaneliSisempi = new JPanel();
            tarinaPaneliSisempi.setBackground(Color.black);
            tarinaPaneliSisempi.setLayout(new BorderLayout());
            tarinaPaneliSisempi.add(kuvaPaneli, BorderLayout.NORTH);
            tarinaPaneliSisempi.add(tekstiPaneli, BorderLayout.CENTER);

            tarinaPaneli = new JPanel();
            tarinaPaneli.addKeyListener(new TarinaRuudunKontrollit());
            tarinaPaneli.setLayout(new GridBagLayout());
            tarinaPaneli.add(tarinaPaneliSisempi);
            tarinaPaneli.requestFocus();

            return tarinaPaneli;
        }
    }

    static void jatka() {
        klikkaustenMäärä++;
        tarinaPaneli = new JPanel();
        //tarinaPaneli = luoTarinaPaneli("alku");
        if (klikkaustenMäärä >= tarinanPituusRuutuina -1 && tarinanTunniste == "alku") {
            jatka.setText("Space: Aloita peli");
        }
        if (klikkaustenMäärä >= tarinanPituusRuutuina) {
            if (!Peli.peliAloitettu) {
                PääIkkuna.lataaRuutu("valikkoruutu");
                ValikkoRuutu.nappiPaneliAlkuvalikko.requestFocus();
            }
            else {
                PääIkkuna.lataaRuutu("peliruutu");
                PääIkkuna.ikkuna.requestFocus();
                Pelaaja.pakotaPelaajanPysäytys();
                Peli.pause = false;
            }
        }
        else {
            teksti.setText(tarinaTeksti[klikkaustenMäärä]);
            Image img = tarinanKuva[klikkaustenMäärä].getImage();
            img.flush();
            kuva.setIcon(new ImageIcon(img));
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
                        break;
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
            }
    }
}
