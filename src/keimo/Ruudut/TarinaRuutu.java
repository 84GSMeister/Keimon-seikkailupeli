package keimo.Ruudut;

import keimo.*;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;

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
            return tarinaPaneli;
        }
        catch (NullPointerException npe) {

            JOptionPane.showMessageDialog(null, "Tarinaa ei voitu ladata. Tämä voi johtua vanhentuneesta default.kst -tiedostosta.", "Tarinaa ei löytynyt", JOptionPane.ERROR_MESSAGE);
            
            kuva = new JLabel("Virhe");

            kuvaPaneli = new JPanel();
            kuvaPaneli.setBounds(0, 0, 640, 400);
            kuvaPaneli.setBackground(Color.black);
            kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
            kuvaPaneli.add(kuva);


            teksti = new JLabel("Virhe");
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
            return tarinaPaneli;
        }
    }

    static void jatka() {
        klikkaustenMäärä++;
        tarinaPaneli = new JPanel();
        //tarinaPaneli = luoTarinaPaneli("koti");
        if (klikkaustenMäärä >= tarinanPituusRuutuina -1 && tarinanTunniste == "alku") {
            jatka.setText("Space: Aloita peli");
        }
        if (klikkaustenMäärä >= tarinanPituusRuutuina) {
            if (!Peli.peliAloitettu) {
                //PääIkkuna.crd.next(PääIkkuna.kortit);
                PääIkkuna.lataaRuutu("valikkoruutu");
                ValikkoRuutu.nappiPaneliAlkuvalikko.requestFocus();
            }
            else {
                //PääIkkuna.crd.previous(PääIkkuna.kortit);
                //PääIkkuna.crd.previous(PääIkkuna.kortit);
                PääIkkuna.lataaRuutu("peliruutu");
                PääIkkuna.ikkuna.requestFocus();
                //PääIkkuna.kortit.remove(TarinaRuutu.tarinaPaneli);
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
                        System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
                        break;
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
            }
    }
}
