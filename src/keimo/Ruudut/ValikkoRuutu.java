package keimo.Ruudut;

import keimo.*;
import keimo.Ikkunat.*;
import keimo.HuoneEditori.*;
import keimo.Utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ValikkoRuutu {
    
    public static JPanel alkuValikkoPaneli = luoValikkoPaneli();
    public static JPanel kortit;
    public static CardLayout alkuvalikonKorttiLayout;
    static JPanel logoPaneli, välityhjäPaneli;
    public static JPanel nappiPaneliAlkuvalikko;
    static JPanel nappiPaneliAsetukset;
    static JPanel nappiPaneliUlompi;
    static JLabel logo;
    static JLabel aloita, tekijät, huoneEditori, asetukset, lopeta;
    //static JLabel aloitaVasenOsoitin, aloitaOikeaOsoitin, tekijätVasenOsoitin, tekijätOikeaOsoitin, asetuksetVasenOsoitin, asetuksetOikeaOsoitin;
    static JLabel huoneEditoriVasenOsoitin, huoneEditoriOikeaOsoitin, lopetaVasenOsoitin, lopetaOikeaOsoitin;
    static final int vaihtoehtojenMäärä = 5;
    static int valinta = 0;
    static JLabel[] vasenOsoitin, vasenOsoitinAsetukset, oikeaOsoitinAsetukset;

    static JLabel vaikeusAste, musiikkiPäällä, valitseMusiikki, tavoiteFPS, tavoiteTickrate, takaisin;
    static final int asetustenMäärä = 6;
    static int valintaAsetuksissa = 0;
    static JPanel infoPaneliAsetukset;
    static JLabel infoLabelAsetukset;
    static String[] infoTekstiAsetukset;
    
    public static void painaNäppäintä(String näppäin) {
        PääIkkuna.pääPaneeli.revalidate();
        PääIkkuna.pääPaneeli.repaint();
        //alkuValikkoPaneli.revalidate();
        //alkuValikkoPaneli.repaint();
        switch (näppäin) {
            case "ylös":
                valinta--;
                if (valinta < 0) {
                    valinta = vaihtoehtojenMäärä-1;
                }
                päivitäOsoittimenSijainti(valinta);
                break;
            case "alas":
                valinta++;
                if (valinta > vaihtoehtojenMäärä-1) {
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
        for (int i = 0; i < asetustenMäärä; i++) {
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
                //PääIkkuna.crd.next(PääIkkuna.kortit);
                PääIkkuna.lataaRuutu("osionalkuruutu");
                OsionAlkuRuutu.osionAlkuPaneli.requestFocus();
                break;
            case 1: // Asetukset
                //AsetusIkkuna.luoAsetusikkuna();
                alkuvalikonKorttiLayout.next(kortit);
                päivitäMuutokset();
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
            case 4:
                break;
            case 5: // Takaisin
                tarkistaArvot();
                alkuvalikonKorttiLayout.previous(kortit);
                nappiPaneliAlkuvalikko.requestFocus();
                break;
            default:
                break;
        }
    }
    static int vaVal = 1;
    static String[] vaVaihtoehdot = {"Passiivinen", "Normaali", "Vaikea", "Järjetön"};
    static String vaValittu = vaVaihtoehdot[1];
    static int musVal = 0;
    public static void säädäValikkoa(String näppäin) {
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
                        if (PelinAsetukset.musiikkiValinta > 0) {
                            PelinAsetukset.musiikkiValinta--;
                        }
                    break;
                    case 3:
                        if (PelinAsetukset.tavoiteFPS > 1) {
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
                        if (PelinAsetukset.musiikkiValinta < PelinAsetukset.musalistanPituus-1) {
                            PelinAsetukset.musiikkiValinta++;
                        }
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
        päivitäMuutokset();
    }

    public static void päivitäMuutokset() {
        oikeaOsoitinAsetukset[0].setText("" + vaValittu);
        oikeaOsoitinAsetukset[1].setText("" + (PelinAsetukset.musiikkiPäällä ? "PÄÄLLÄ" : "POIS"));
        oikeaOsoitinAsetukset[2].setText("" + PelinAsetukset.musiikkiValinta);
        oikeaOsoitinAsetukset[3].setText("" + PelinAsetukset.tavoiteFPS);
        oikeaOsoitinAsetukset[4].setText("" + PelinAsetukset.tavoiteTickrate);
    }

    public static JPanel luoValikkoPaneli() {
        
        logo = new JLabel(new ImageIcon("tiedostot/kuvat/menu/KEIMON_logo.png"));

        logoPaneli = new JPanel();
        logoPaneli.setPreferredSize(new Dimension(640, 360));
        //logoPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, true));
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
        //nappiPaneliAlkuvalikko.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        nappiPaneliAlkuvalikko.setPreferredSize(new Dimension(700, 320));
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

        JPanel nappiPaneliAlkuvalikkoKeskitetty = new JPanel(new GridBagLayout());
        nappiPaneliAlkuvalikkoKeskitetty.setPreferredSize(new Dimension(1000, 320));
        //nappiPaneliAlkuvalikkoKeskitetty.setBorder(BorderFactory.createLineBorder(Color.green, 1, false));
        nappiPaneliAlkuvalikkoKeskitetty.setBackground(Color.black);
        nappiPaneliAlkuvalikkoKeskitetty.add(nappiPaneliAlkuvalikko);



        vaikeusAste = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_vaikeusaste.png"));
        vaikeusAste.setForeground(Color.white);
        vaikeusAste.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        vaikeusAste.setAlignmentX(Component.CENTER_ALIGNMENT);

        musiikkiPäällä = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_musiikki.png"));
        musiikkiPäällä.setForeground(Color.white);
        musiikkiPäällä.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        musiikkiPäällä.setAlignmentX(Component.CENTER_ALIGNMENT);

        valitseMusiikki = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_valitsemusiikki.png"));
        valitseMusiikki.setForeground(Color.white);
        valitseMusiikki.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        valitseMusiikki.setAlignmentX(Component.CENTER_ALIGNMENT);

        tavoiteFPS = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_tavoitefps.png"));
        tavoiteFPS.setForeground(Color.white);
        tavoiteFPS.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        tavoiteFPS.setAlignmentX(Component.CENTER_ALIGNMENT);

        tavoiteTickrate = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_tavoitetickrate.png"));
        tavoiteTickrate.setForeground(Color.white);
        tavoiteTickrate.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        tavoiteTickrate.setAlignmentX(Component.CENTER_ALIGNMENT);

        takaisin = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_hyväksy.png"));
        takaisin.setForeground(Color.white);
        takaisin.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        takaisin.setAlignmentX(Component.CENTER_ALIGNMENT);

        vasenOsoitinAsetukset = new JLabel[asetustenMäärä];
        for (int i = 0; i < asetustenMäärä; i++) {
            vasenOsoitinAsetukset[i] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            vasenOsoitinAsetukset[i].setPreferredSize(new Dimension(50, 30));
        }
        vasenOsoitinAsetukset[valintaAsetuksissa] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));

        oikeaOsoitinAsetukset = new JLabel[asetustenMäärä];
        for (int i = 0; i < asetustenMäärä; i++) {
            oikeaOsoitinAsetukset[i] = new JLabel((""));
            oikeaOsoitinAsetukset[i].setForeground(Color.white);
            oikeaOsoitinAsetukset[i].setFont(new Font("Courier10 BT", Font.PLAIN, 22));
        }
        oikeaOsoitinAsetukset[0].setText("" + PelinAsetukset.vaikeusAste);
        oikeaOsoitinAsetukset[1].setText("" + (PelinAsetukset.musiikkiPäällä ? "PÄÄLLÄ" : "POIS"));
        oikeaOsoitinAsetukset[2].setText("" + PelinAsetukset.musiikkiValinta);
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
        nappiPaneliAsetukset.add(valitseMusiikki);
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
        infoPaneliAsetukset = new JPanel();
        infoPaneliAsetukset.setBackground(Color.black);
        //infoPaneliAsetukset.setBorder(BorderFactory.createLineBorder(Color.green, 1, false));
        infoPaneliAsetukset.setPreferredSize(new Dimension(500, 300));
        infoPaneliAsetukset.add(infoLabelAsetukset);

        JPanel nappiJaInfoPaneliAsetukset = new JPanel(new BorderLayout());
        nappiJaInfoPaneliAsetukset.setPreferredSize(new Dimension(1200, 300));
        //nappiJaInfoPaneliAsetukset.setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
        nappiJaInfoPaneliAsetukset.add(nappiPaneliAsetukset, BorderLayout.WEST);
        nappiJaInfoPaneliAsetukset.add(infoPaneliAsetukset, BorderLayout.EAST);

        alkuvalikonKorttiLayout = new CardLayout();
        kortit = new JPanel(alkuvalikonKorttiLayout);
        kortit.add(nappiPaneliAlkuvalikkoKeskitetty);
        kortit.add(nappiJaInfoPaneliAsetukset);
        
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
        //listaaFontit();
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
