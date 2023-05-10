package keimo.Väliruudut;

import keimo.*;
import keimo.Ikkunat.*;
import keimo.HuoneEditori.*;
import keimo.Utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ValikkoRuutu {
    
    public static JPanel alkuValikkoPaneli = luoValikkoPaneli();
    static JPanel kortit;
    static CardLayout alkuvalikonKorttiLayout;
    static JPanel logoPaneli, välityhjäPaneli, nappiPaneliAlkuvalikko, nappiPaneliAsetukset, nappiPaneliUlompi;
    static JLabel logo;
    static JLabel aloita, tekijät, huoneEditori, asetukset, lopeta;
    static JLabel aloitaVasenOsoitin, aloitaOikeaOsoitin, tekijätVasenOsoitin, tekijätOikeaOsoitin, asetuksetVasenOsoitin, asetuksetOikeaOsoitin;
    static JLabel huoneEditoriVasenOsoitin, huoneEditoriOikeaOsoitin, lopetaVasenOsoitin, lopetaOikeaOsoitin;
    static final int vaihtoehtojenMäärä = 5;
    static int valinta = 0;
    static JLabel[] vasenOsoitin, vasenOsoitinAsetukset, oikeaOsoitinAsetukset;

    static JLabel vaikeusAste, musiikkiPäällä, valitseMusiikki, tavoiteFPS, takaisin;
    static final int asetustenMäärä = 5;
    static int valintaAsetuksissa = 0;
    
    public static void painaNäppäintä(String näppäin) {

        switch (näppäin) {
            case "ylös":
                valinta--;
                if (valinta < 0) {
                    valinta = 4;
                }
                päivitäOsoittimenSijainti(valinta);
                break;
            case "alas":
                valinta++;
                if (valinta > 4) {
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

    public static void painaNäppäintäAsetuksissa(String näppäin) {

        switch (näppäin) {
            case "ylös":
                valintaAsetuksissa--;
                if (valintaAsetuksissa < 0) {
                    valintaAsetuksissa = 4;
                }
                päivitäOsoittimenSijaintiAsetuksissa(valintaAsetuksissa);
                break;
            case "alas":
                valintaAsetuksissa++;
                if (valintaAsetuksissa > 4) {
                    valintaAsetuksissa = 0;
                }
                päivitäOsoittimenSijaintiAsetuksissa(valintaAsetuksissa);
                break;
            case "enter":
                hyväksyAsetuksissa(valintaAsetuksissa);
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

    static void päivitäOsoittimenSijaintiAsetuksissa(int valinta) {
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            if (i == valinta) {
                vasenOsoitinAsetukset[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
            }
            else {
                vasenOsoitinAsetukset[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            }
        }
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Aloita peli
                PääIkkuna.crd.next(PääIkkuna.kortit);
                OsionAlkuRuutu.osionAlkuPaneli.requestFocus();
                break;
            case 1: // Asetukset
                //AsetusIkkuna.luoAsetusikkuna();
                alkuvalikonKorttiLayout.next(kortit);
                nappiPaneliAsetukset.requestFocus();
                break;
            case 2: // Huone-editori
                HuoneEditoriIkkuna.luoEditoriIkkuna();
                break;
            case 3: // Kehittäjät
                CustomViestiIkkunat.Credits.showDialog();
                break;
            case 4: // Lopeta
                System.exit(0);
                break;
            default:
                break;
        }
    }

    static void hyväksyAsetuksissa(int valinta) {

        switch (valinta) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4: // Takaisin
                tarkistaArvot();
                alkuvalikonKorttiLayout.previous(kortit);
                nappiPaneliAlkuvalikko.requestFocus();
                break;
            default:
                break;
        }
    }
    static int val = 0;
    public static void säädäValikkoa(String näppäin) {
        switch (näppäin) {
            case "vasen":
                switch (valintaAsetuksissa) {
                    case 0:
                        PelinAsetukset.vaikeusAste--;
                        oikeaOsoitinAsetukset[0].setText("" + PelinAsetukset.vaikeusAste);
                    break;
                    case 1:
                        val--;
                        PelinAsetukset.musiikkiPäällä = val % 2 == 0;
                        oikeaOsoitinAsetukset[1].setText("" + (PelinAsetukset.musiikkiPäällä ? "kyllä" : "ei"));
                    break;
                    case 2:
                        PelinAsetukset.musiikkiValinta--;
                        oikeaOsoitinAsetukset[2].setText("" + PelinAsetukset.musiikkiValinta);
                    break;
                    case 3:
                        PelinAsetukset.tavoiteFPS--;
                        oikeaOsoitinAsetukset[3].setText("" + PelinAsetukset.tavoiteFPS);
                    break;
                    default:
                    break;
                }
            break;
            case "oikea":
                switch (valintaAsetuksissa) {
                    case 0:
                        PelinAsetukset.vaikeusAste++;
                        oikeaOsoitinAsetukset[0].setText("" + PelinAsetukset.vaikeusAste);
                    break;
                    case 1:
                        val++;
                        PelinAsetukset.musiikkiPäällä = val % 2 == 0;
                        oikeaOsoitinAsetukset[1].setText("" + (PelinAsetukset.musiikkiPäällä ? "kyllä" : "ei"));
                    break;
                    case 2:
                        PelinAsetukset.musiikkiValinta++;
                        oikeaOsoitinAsetukset[2].setText("" + PelinAsetukset.musiikkiValinta);
                    break;
                    case 3:
                        PelinAsetukset.tavoiteFPS++;
                        oikeaOsoitinAsetukset[3].setText("" + PelinAsetukset.tavoiteFPS);
                    break;
                    default:
                    break;
                }
            break;
        }
    }

    public static JPanel luoValikkoPaneli() {
        
        logo = new JLabel(new ImageIcon("tiedostot/kuvat/menu/KEIMON_logo.png"));

        logoPaneli = new JPanel();
        logoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        logoPaneli.setBackground(Color.black);
        logoPaneli.add(logo);
        
        aloita = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_aloita.png"));
        aloita.setAlignmentX(Component.CENTER_ALIGNMENT);

        asetukset = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_asetukset.png"));
        asetukset.setAlignmentX(Component.CENTER_ALIGNMENT);

        huoneEditori = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_editori.png"));
        huoneEditori.setAlignmentX(Component.CENTER_ALIGNMENT);

        tekijät = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_kehittäjät.png"));
        tekijät.setAlignmentX(Component.CENTER_ALIGNMENT);

        lopeta = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_lopeta.png"));
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        vasenOsoitin = new JLabel[vaihtoehtojenMäärä];
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            vasenOsoitin[i] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        }
        vasenOsoitin[valinta] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
    
        nappiPaneliAlkuvalikko = new JPanel();
        SpringLayout nappiPaneliAlkuvalikonlayout = new SpringLayout();
        nappiPaneliAlkuvalikonlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vasenOsoitin[0], 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneliAlkuvalikko);
        nappiPaneliAlkuvalikonlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, aloita, 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneliAlkuvalikko);
        nappiPaneliAlkuvalikko.setLayout(nappiPaneliAlkuvalikonlayout);
        nappiPaneliAlkuvalikko.setBackground(Color.black);
        nappiPaneliAlkuvalikko.setPreferredSize(new Dimension(700, 400));
        nappiPaneliAlkuvalikko.addKeyListener(new MenuKontrollit());
        nappiPaneliAlkuvalikko.add(vasenOsoitin[0]);
        nappiPaneliAlkuvalikko.add(aloita);
        nappiPaneliAlkuvalikko.add(vasenOsoitin[1]);
        nappiPaneliAlkuvalikko.add(asetukset);
        nappiPaneliAlkuvalikko.add(vasenOsoitin[2]);
        nappiPaneliAlkuvalikko.add(huoneEditori);
        nappiPaneliAlkuvalikko.add(vasenOsoitin[3]);
        nappiPaneliAlkuvalikko.add(tekijät);
        nappiPaneliAlkuvalikko.add(vasenOsoitin[4]);
        nappiPaneliAlkuvalikko.add(lopeta);
        SpringUtilities.makeCompactGrid(nappiPaneliAlkuvalikko, vaihtoehtojenMäärä, 2, 6, 6, 6, 6);

        vaikeusAste = new JLabel("VAIKEUSASTE");
        vaikeusAste.setForeground(Color.white);
        vaikeusAste.setFont(new Font("Courier10 BT", Font.PLAIN, 36));
        vaikeusAste.setAlignmentX(Component.CENTER_ALIGNMENT);

        musiikkiPäällä = new JLabel("MUSIIKKI PÄÄLLÄ");
        musiikkiPäällä.setForeground(Color.white);
        musiikkiPäällä.setFont(new Font("Courier10 BT", Font.PLAIN, 36));
        musiikkiPäällä.setAlignmentX(Component.CENTER_ALIGNMENT);

        valitseMusiikki = new JLabel("VALITSE MUSIIKKI");
        valitseMusiikki.setForeground(Color.white);
        valitseMusiikki.setFont(new Font("Courier10 BT", Font.PLAIN, 36));
        valitseMusiikki.setAlignmentX(Component.CENTER_ALIGNMENT);

        tavoiteFPS = new JLabel("TAVOITE-FPS");
        tavoiteFPS.setForeground(Color.white);
        tavoiteFPS.setFont(new Font("Courier10 BT", Font.PLAIN, 36));
        tavoiteFPS.setAlignmentX(Component.CENTER_ALIGNMENT);

        takaisin = new JLabel("TAKAISIN");
        takaisin.setForeground(Color.white);
        takaisin.setFont(new Font("Courier10 BT", Font.PLAIN, 36));
        takaisin.setAlignmentX(Component.CENTER_ALIGNMENT);

        vasenOsoitinAsetukset = new JLabel[asetustenMäärä];
        for (int i = 0; i < asetustenMäärä; i++) {
            vasenOsoitinAsetukset[i] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        }
        vasenOsoitinAsetukset[valinta] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));

        oikeaOsoitinAsetukset = new JLabel[asetustenMäärä];
        for (int i = 0; i < asetustenMäärä; i++) {
            oikeaOsoitinAsetukset[i] = new JLabel((""));
            oikeaOsoitinAsetukset[i].setForeground(Color.white);
            oikeaOsoitinAsetukset[i].setFont(new Font("Courier10 BT", Font.PLAIN, 36));
        }
        oikeaOsoitinAsetukset[0].setText("" + PelinAsetukset.vaikeusAste);
        oikeaOsoitinAsetukset[1].setText("" + "ei");
        oikeaOsoitinAsetukset[2].setText("" + PelinAsetukset.musiikkiValinta);
        oikeaOsoitinAsetukset[3].setText("" + PelinAsetukset.tavoiteFPS);
        oikeaOsoitinAsetukset[4] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        nappiPaneliAsetukset = new JPanel();
        SpringLayout nappiPaneliAsetustenlayout = new SpringLayout();
        nappiPaneliAsetustenlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vasenOsoitinAsetukset[0], 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneliAsetukset);
        nappiPaneliAsetustenlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vaikeusAste, 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneliAsetukset);
        nappiPaneliAsetukset.setLayout(nappiPaneliAsetustenlayout);
        nappiPaneliAsetukset.setBackground(Color.black);
        nappiPaneliAsetukset.setPreferredSize(new Dimension(500, 400));
        nappiPaneliAsetukset.addKeyListener(new AsetusKontrollit());
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[0]);
        nappiPaneliAsetukset.add(vaikeusAste);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[0]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[1]);
        nappiPaneliAsetukset.add(musiikkiPäällä);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[1]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[2]);
        nappiPaneliAsetukset.add(valitseMusiikki);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[2]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[3]);
        nappiPaneliAsetukset.add(tavoiteFPS);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[3]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[4]);
        nappiPaneliAsetukset.add(takaisin);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[4]);
        SpringUtilities.makeCompactGrid(nappiPaneliAsetukset, asetustenMäärä, 3, 6, 6, 6, 6);

        alkuvalikonKorttiLayout = new CardLayout();
        kortit = new JPanel(alkuvalikonKorttiLayout);
        kortit.add(nappiPaneliAlkuvalikko);
        kortit.add(nappiPaneliAsetukset);
        
        nappiPaneliUlompi = new JPanel();
        nappiPaneliUlompi.setLayout(new GridBagLayout());
        nappiPaneliUlompi.setBackground(Color.black);
        nappiPaneliUlompi.add(kortit);


        JPanel valikkoPaneli = new JPanel();
        valikkoPaneli.setLayout(new BorderLayout());
        valikkoPaneli.add(logoPaneli, BorderLayout.NORTH);
        valikkoPaneli.add(nappiPaneliUlompi, BorderLayout.CENTER);
        valikkoPaneli.setBackground(Color.black);
        //valikkoPaneli.addKeyListener(new MenuKontrollit());
        valikkoPaneli.requestFocus();
        
        alkuValikkoPaneli = valikkoPaneli;
        listaaFontit();
        return valikkoPaneli;
    }

    public static void tarkistaArvot() {
        try {
            if (PelinAsetukset.vaikeusAste < 0) {
                JOptionPane.showMessageDialog(null, "Vaikeusaste ei voi olla negatiivinen!\n\n0 = Vihollisille ei voi kuolla", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else if (PelinAsetukset.tavoiteFPS < 1) {
                JOptionPane.showMessageDialog(null, "Tavoite-FPS ei voi olla negatiivinen!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else {
                PääIkkuna.uusiIkkuna = true;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void listaaFontit() {
        String fonts[] = 
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    
        for (int i = 0; i < fonts.length; i++) {
            System.out.println(fonts[i]);
        }
    }
      

    static class MenuKontrollit implements KeyListener {

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
                        ValikkoRuutu.painaNäppäintä("alas");
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        ValikkoRuutu.painaNäppäintä("ylös");
                        break;
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER:
                        ValikkoRuutu.painaNäppäintä("enter");
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

    static class AsetusKontrollit implements KeyListener {

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
                        ValikkoRuutu.painaNäppäintäAsetuksissa("alas");
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        ValikkoRuutu.painaNäppäintäAsetuksissa("ylös");
                        break;
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER:
                        ValikkoRuutu.painaNäppäintäAsetuksissa("enter");
                        break;
                    case KeyEvent.VK_ESCAPE:
                        alkuvalikonKorttiLayout.previous(kortit);
                        nappiPaneliAlkuvalikko.requestFocus();
                        break;
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                        ValikkoRuutu.säädäValikkoa("vasen");
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        ValikkoRuutu.säädäValikkoa("oikea");
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
