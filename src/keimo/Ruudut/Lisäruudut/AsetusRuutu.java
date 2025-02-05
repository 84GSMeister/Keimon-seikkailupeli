package keimo.Ruudut.Lisäruudut;

import keimo.Peli;
import keimo.PelinAsetukset;
import keimo.PääIkkuna;
import keimo.Ruudut.PeliRuutu;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.Utility.Downloaded.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class AsetusRuutu {

    private static int asetustenMäärä = 6;
    private static int valintaAsetuksissa = 0;

    private static JLabel[] vasenOsoitinAsetukset;
    private static JLabel[] oikeaOsoitinAsetukset;
    private static String[] infoTekstiAsetukset;
    private static JLabel infoLabelAsetukset;
    public static JPanel nappiPaneliAsetukset;

    public static JPanel luoKompaktiValikkoRuutu() {

        JLabel vaikeusAste = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_vaikeusaste.png"));
        vaikeusAste.setForeground(Color.white);
        vaikeusAste.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        vaikeusAste.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel musiikkiPäällä = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_musiikki.png"));
        musiikkiPäällä.setForeground(Color.white);
        musiikkiPäällä.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        musiikkiPäällä.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel äänetPäällä = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_äänet.png"));
        äänetPäällä.setForeground(Color.white);
        äänetPäällä.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        äänetPäällä.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tavoiteFPS = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_tavoitefps.png"));
        tavoiteFPS.setForeground(Color.white);
        tavoiteFPS.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        tavoiteFPS.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tavoiteTickrate = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_tavoitetickrate.png"));
        tavoiteTickrate.setForeground(Color.white);
        tavoiteTickrate.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        tavoiteTickrate.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel takaisin = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_hyväksy.png"));
        takaisin.setForeground(Color.white);
        takaisin.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        takaisin.setAlignmentX(Component.CENTER_ALIGNMENT);

        vasenOsoitinAsetukset = new JLabel[asetustenMäärä];
        for (int i = 0; i < asetustenMäärä; i++) {
            vasenOsoitinAsetukset[i] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            vasenOsoitinAsetukset[i].setPreferredSize(new Dimension(50, 30));
        }
        vasenOsoitinAsetukset[valintaAsetuksissa] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.gif"));

        oikeaOsoitinAsetukset = new JLabel[asetustenMäärä];
        for (int i = 0; i < asetustenMäärä; i++) {
            oikeaOsoitinAsetukset[i] = new JLabel((""));
            oikeaOsoitinAsetukset[i].setForeground(Color.white);
            oikeaOsoitinAsetukset[i].setFont(new Font("Courier10 BT", Font.PLAIN, 22));
        }
        oikeaOsoitinAsetukset[0].setText("" + vaValittu);
        oikeaOsoitinAsetukset[1].setText("" + (PelinAsetukset.musiikkiPäällä ? "PÄÄLLÄ" : "POIS"));
        oikeaOsoitinAsetukset[2].setText("" + (PelinAsetukset.äänetPäällä ? "PÄÄLLÄ" : "POIS"));
        oikeaOsoitinAsetukset[3].setText("" + PelinAsetukset.tavoiteFPS);
        oikeaOsoitinAsetukset[4].setText("" + PelinAsetukset.tavoiteTickrate);
        oikeaOsoitinAsetukset[5] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        nappiPaneliAsetukset = new JPanel();
        SpringLayout nappiPaneliAsetustenlayout = new SpringLayout();
        nappiPaneliAsetustenlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vasenOsoitinAsetukset[0], 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneliAsetukset);
        nappiPaneliAsetustenlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vaikeusAste, 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneliAsetukset);
        nappiPaneliAsetukset.setLayout(nappiPaneliAsetustenlayout);
        nappiPaneliAsetukset.setBackground(Color.black);
        //nappiPaneliAsetukset.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        nappiPaneliAsetukset.setPreferredSize(new Dimension(700, 300));
        nappiPaneliAsetukset.addKeyListener(new AsetusKontrollit());
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[0]);
        nappiPaneliAsetukset.add(vaikeusAste);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[0]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[1]);
        nappiPaneliAsetukset.add(musiikkiPäällä);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[1]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[2]);
        nappiPaneliAsetukset.add(äänetPäällä);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[2]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[3]);
        nappiPaneliAsetukset.add(tavoiteFPS);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[3]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[4]);
        nappiPaneliAsetukset.add(tavoiteTickrate);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[4]);
        nappiPaneliAsetukset.add(vasenOsoitinAsetukset[5]);
        nappiPaneliAsetukset.add(takaisin);
        nappiPaneliAsetukset.add(oikeaOsoitinAsetukset[5]);
        SpringUtilities.makeCompactGrid(nappiPaneliAsetukset, asetustenMäärä, 3, 0, 0, 0, 0);



        infoTekstiAsetukset = new String[asetustenMäärä];

        infoTekstiAsetukset[0] = "<html><p>" + 
        "Pelin vaikeusaste" + "<br><br>" +
        "Pasiivinen: viholliset eivät vahingoita" + "<br>" + 
        "Normaali: viholliset tekevät normaalia vahinkoa" + "<br>" +
        "Vaikea: viholliset tekevät kaksinkertaista vahinkoa" + "<br>" +
        "Järjetön: viholliset tekevät 10-kertaista vahinkoa" +
        "</p></html>";

        infoTekstiAsetukset[1] = "Musiikki päällä";

        infoTekstiAsetukset[2] = "<html><p>" +
        "0 = Udo haukkuu: Running in the 90s (midi)" + "<br>" +
        "1 = Udo haukkuu: Disco Band (midi)" + "<br>" +
        "2 = Udo haukkuu: Kylie (BeepBox)" + "<br>" +
        "3 = Udo haukkuu: Mario 2 theme (midi)" + "<br>" +
        "4 = Udo haukkuu: Nyan Cat (midi)" + "<br>" +
        "5 = Udo haukkuu: Never Gonna Give You Up (midi)" + "<br>" +
        "6 = Udo haukkuu: Super Mario World (midi)" + "<br>" +
        "7 = Udo haukkuu: Wide President theme (midi)" + "<br>" +
        "8 = Udo haukkuu: Alice Deejay - Better off Alone" + "<br>" +
        "9 = Udo haukkuu: Eiffel 65 - Blue" + "<br>" +
        "10 = Udo haukkuu: Gigi D'Agostino - L'Amour Toujours (Seppo on bi)" + "<br>" +
        "11 = Udo haukkuu: Gigi D'Agostino - The Riddle" + "<br>" +
        "12 = Udo haukkuu: Hixxy - Phaze 2 Phaze" + "<br>" +
        "13 = Udo haukkuu: Knife Party - Bonfire" + "<br>" +
        "14 = Udo haukkuu: Knife Party - Rage Valley" + "<br>" +
        "15 = Udo haukkuu: Martin Garrix- Animals" + "<br>" +
        "16 = Udo haukkuu: Paradisio - Bailando" + "<br>" +
        "17 = Udo haukkuu: Rollergirl - Geisha Dreams" + "<br>" +
        "18 = Udo haukkuu: Running in the 90s" + "<br>" +
        "19 = Udo haukkuu: Sandy Marton - Camel by Camel" + "<br>" +
        "20 = Udo haukkuu: Sash - Ecuador" + "<br>" +
        "21 = Udo haukkuu: Scooter - How Much is the Fish" + "<br>" +
        "22 = Udo haukkuu: TheFatRat - Windfall" + "<br>" +
        "</p></html>";

        infoTekstiAsetukset[3] = "<html><p>" +
        "Tavoite-FPS" + "<br><br>" +
        "Älä aseta liian suureksi tai" + "<br>" +
        "käyttöliittymä voi muuttua tökkiväksi" + "<br><br>" +
        "Oletus: 200" +
        "</p></html>";

        infoTekstiAsetukset[4] = "<html><p>" +
        "Tavoite-Tickrate" + "<br><br>" +
        "Vaikuttaa mm." + "<br>" +
        "-Pelaajan liikkeeseen" + "<br>" +
        "-Vihollisten liikkeeseen" + "<br>" +
        "-Collision-tarkistuksiin" + "<br><br>" +
        "Oletus: 60" +
        "</p></html>";

        infoTekstiAsetukset[5] = "<html><p>" +
        "Hyväksy muutokset ja" + "<br>" +
        "käynnistä uudelleen" + "<br><br>" +
        "(Esc = Peruuta)" +
        "</p></html>";

        infoLabelAsetukset = new JLabel(infoTekstiAsetukset[0]);
        infoLabelAsetukset.setForeground(Color.white);
        infoLabelAsetukset.setPreferredSize(new Dimension(480, 300));
        infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 15));
        JPanel infoPaneliAsetukset = new JPanel();
        infoPaneliAsetukset.setBackground(Color.black);
        infoPaneliAsetukset.setPreferredSize(new Dimension(500, 300));
        infoPaneliAsetukset.add(infoLabelAsetukset);

        JPanel nappiJaInfoPaneliAsetukset = new JPanel(new BorderLayout());
        nappiJaInfoPaneliAsetukset.setPreferredSize(new Dimension(1200, 300));
        nappiJaInfoPaneliAsetukset.add(nappiPaneliAsetukset, BorderLayout.WEST);
        nappiJaInfoPaneliAsetukset.add(infoPaneliAsetukset, BorderLayout.EAST);
        return nappiPaneliAsetukset;
    }

    private static void painaNäppäintäAsetuksissa(String näppäin) {

        switch (näppäin) {
            case "ylös":
                valintaAsetuksissa--;
                if (valintaAsetuksissa < 0) {
                    valintaAsetuksissa = asetustenMäärä-1;
                }
                infoLabelAsetukset.setText(infoTekstiAsetukset[valintaAsetuksissa]);
                if (valintaAsetuksissa == 2) {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 10));
                }
                else {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 15));
                }
                päivitäOsoittimenSijaintiAsetuksissa(valintaAsetuksissa);
                break;
            case "alas":
                valintaAsetuksissa++;
                if (valintaAsetuksissa > asetustenMäärä-1) {
                    valintaAsetuksissa = 0;
                }
                infoLabelAsetukset.setText(infoTekstiAsetukset[valintaAsetuksissa]);
                if (valintaAsetuksissa == 2) {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 10));
                }
                else {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 15));
                }
                päivitäOsoittimenSijaintiAsetuksissa(valintaAsetuksissa);
                break;
            case "enter":
                hyväksyAsetuksissa(valintaAsetuksissa);
                break;
            default:
                break;
        }
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    private static void päivitäOsoittimenSijaintiAsetuksissa(int valinta) {
        for (int i = 0; i < asetustenMäärä; i++) {
            if (i == valinta) {
                vasenOsoitinAsetukset[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.gif"));
            }
            else {
                vasenOsoitinAsetukset[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            }
        }
    }

    private static void hyväksyAsetuksissa(int valinta) {

        switch (valinta) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5: // Takaisin
                tarkistaArvot();
                break;
            default:
                break;
        }
    }

    private static void tarkistaArvot() {
        try {
            if (PelinAsetukset.vaikeusAste < 0) {
                JOptionPane.showMessageDialog(null, "Vaikeusaste ei voi olla negatiivinen!\n\n0 = Vihollisille ei voi kuolla", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else if (PelinAsetukset.tavoiteFPS < 0) {
                JOptionPane.showMessageDialog(null, "Tavoite-FPS ei voi olla negatiivinen!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (Peli.peliKäynnissä) {
                    Peli.pausetaPeli(false);
                    Peli.pauseDialogi = false;
                    Peli.valintaDialogi = false;
                    PeliRuutu.lisäRuutuPaneli.removeAll();
                    PeliRuutu.lisäRuutuPaneli.revalidate();
                    PeliRuutu.lisäRuutuPaneli.repaint();
                    PeliRuutu.lisäRuutuPaneli.setVisible(false);
                    PääIkkuna.ikkuna.requestFocus();
                }
                else {
                    PääIkkuna.uusiIkkuna = true;
                }
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int vaVal = 1;
    private static String[] vaVaihtoehdot = {"Passiivinen", "Normaali", "Vaikea", "Järjetön"};
    private static String vaValittu = vaVaihtoehdot[1];
    private static int musVal = 1;
    private static int ääniVal = 1;
    private static void säädäValikkoa(String näppäin) {
        switch (näppäin) {
            case "vasen":
                switch (valintaAsetuksissa) {
                    case 0:
                        // if (PelinAsetukset.vaikeusAste > 0) {
                        //     PelinAsetukset.vaikeusAste--;
                        // }
                        if (vaVal > 0) {
                            vaVal--;
                            vaValittu = vaVaihtoehdot[vaVal % 4];
                        }
                        PelinAsetukset.valitseVaikeusaste(vaValittu);
                    break;
                    case 1:
                        musVal--;
                        PelinAsetukset.musiikkiPäällä = musVal % 2 != 0;
                    break;
                    case 2:
                        ääniVal--;
                        PelinAsetukset.äänetPäällä = ääniVal % 2 != 0;
                    break;
                    case 3:
                        if (PelinAsetukset.tavoiteFPS > 0) {
                            PelinAsetukset.tavoiteFPS--;
                            //PelinAsetukset.RUUDUNPÄIVITYS = PelinAsetukset.tavoiteFPS;
                        }
                    break;
                    case 4:
                        if (PelinAsetukset.tavoiteTickrate > 1) {
                            PelinAsetukset.tavoiteTickrate--;
                        }
                    break;
                    default:
                    break;
                }
            break;
            case "oikea":
                switch (valintaAsetuksissa) {
                    case 0:
                        // if (PelinAsetukset.vaikeusAste < 30) {
                        //     PelinAsetukset.vaikeusAste++;
                        // }
                        if (vaVal < vaVaihtoehdot.length -1) {
                            vaVal++;
                            vaValittu = vaVaihtoehdot[vaVal % 4];
                        }
                        PelinAsetukset.valitseVaikeusaste(vaValittu);
                    break;
                    case 1:
                        musVal++;
                        PelinAsetukset.musiikkiPäällä = musVal % 2 != 0;
                    break;
                    case 2:
                        ääniVal++;
                        PelinAsetukset.äänetPäällä = ääniVal % 2 != 0;
                    break;
                    case 3:
                        if (PelinAsetukset.tavoiteFPS < 1000) {
                            PelinAsetukset.tavoiteFPS++;
                            //PelinAsetukset.RUUDUNPÄIVITYS = PelinAsetukset.tavoiteFPS;
                        }
                    break;
                    case 4:
                        if (PelinAsetukset.tavoiteTickrate < 1000) {
                            PelinAsetukset.tavoiteTickrate++;
                        }
                    break;
                    default:
                    break;
                }
            break;
        }
        ÄänentoistamisSäie.toistaSFX("Valinta");
        päivitäMuutokset();
    }

    private static void päivitäMuutokset() {
        oikeaOsoitinAsetukset[0].setText("" + vaValittu);
        oikeaOsoitinAsetukset[1].setText("" + (PelinAsetukset.musiikkiPäällä ? "PÄÄLLÄ" : "POIS"));
        oikeaOsoitinAsetukset[2].setText("" + (PelinAsetukset.äänetPäällä ? "PÄÄLLÄ" : "POIS"));
        oikeaOsoitinAsetukset[3].setText("" + PelinAsetukset.tavoiteFPS);
        oikeaOsoitinAsetukset[4].setText("" + PelinAsetukset.tavoiteTickrate);
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
                        painaNäppäintäAsetuksissa("alas");
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        painaNäppäintäAsetuksissa("ylös");
                        break;
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER:
                        painaNäppäintäAsetuksissa("enter");
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (Peli.peliKäynnissä) {
                            Peli.pausetaPeli(false);
                            Peli.pauseDialogi = false;
                            Peli.valintaDialogi = false;
                            PeliRuutu.lisäRuutuPaneli.removeAll();
                            PeliRuutu.lisäRuutuPaneli.revalidate();
                            PeliRuutu.lisäRuutuPaneli.repaint();
                            PeliRuutu.lisäRuutuPaneli.setVisible(false);
                            PääIkkuna.ikkuna.requestFocus();
                        }
                        break;
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                        säädäValikkoa("vasen");
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        säädäValikkoa("oikea");
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
