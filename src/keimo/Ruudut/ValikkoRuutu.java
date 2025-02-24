package keimo.Ruudut;

import keimo.*;
import keimo.Ikkunat.*;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.HuoneEditori.*;
import keimo.Utility.KeimoFontit;
import keimo.Utility.Downloaded.SpringUtilities;

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
    public static JPanel nappiPaneliAsetukset;
    static JPanel nappiPaneliUlompi;
    static JLabel logo;
    static JLabel aloita, tekijät, huoneEditori, asetukset, lopeta;
    static JLabel huoneEditoriVasenOsoitin, huoneEditoriOikeaOsoitin, lopetaVasenOsoitin, lopetaOikeaOsoitin;
    static final int vaihtoehtojenMäärä = 5;
    static int valinta = 0;
    static JLabel[] vasenOsoitin, vasenOsoitinAsetukset, oikeaOsoitinAsetukset;

    static JLabel vaikeusAste, musiikkiPäällä, äänetPäällä, tavoiteFPS, tavoiteTickrate, takaisin;
    static final int asetustenMäärä = 6;
    static int valintaAsetuksissa = 0;
    static JPanel infoPaneliAsetukset;
    static JLabel infoLabelAsetukset;
    static String[] infoTekstiAsetukset;
    
    public static void painaNäppäintä(String näppäin) {
        PääIkkuna.pääPaneeli.revalidate();
        PääIkkuna.pääPaneeli.repaint();
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
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    public static void painaNäppäintäAsetuksissa(String näppäin) {

        switch (näppäin) {
            case "ylös":
                valintaAsetuksissa--;
                if (valintaAsetuksissa < 0) {
                    valintaAsetuksissa = asetustenMäärä-1;
                }
                infoLabelAsetukset.setText(infoTekstiAsetukset[valintaAsetuksissa]);
                if (valintaAsetuksissa == 0) {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 10));
                    infoLabelAsetukset.setFont(KeimoFontit.fontti_keimo_10);
                }
                else {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 15));
                    infoLabelAsetukset.setFont(KeimoFontit.fontti_keimo_16);
                }
                päivitäOsoittimenSijaintiAsetuksissa(valintaAsetuksissa);
                break;
            case "alas":
                valintaAsetuksissa++;
                if (valintaAsetuksissa > asetustenMäärä-1) {
                    valintaAsetuksissa = 0;
                }
                infoLabelAsetukset.setText(infoTekstiAsetukset[valintaAsetuksissa]);
                if (valintaAsetuksissa == 0) {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 10));
                    infoLabelAsetukset.setFont(KeimoFontit.fontti_keimo_10);
                }
                else {
                    infoLabelAsetukset.setFont(new Font("Courier10 BT", Font.PLAIN, 15));
                    infoLabelAsetukset.setFont(KeimoFontit.fontti_keimo_16);
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

    static void päivitäOsoittimenSijainti(int valinta) {
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            if (i == valinta) {
                vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.gif"));
            }
            else {
                vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            }
        }
    }

    static void päivitäOsoittimenSijaintiAsetuksissa(int valinta) {
        for (int i = 0; i < asetustenMäärä; i++) {
            if (i == valinta) {
                vasenOsoitinAsetukset[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.gif"));
            }
            else {
                vasenOsoitinAsetukset[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            }
        }
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Aloita peli
                PääIkkuna.lataaRuutu("osionalkuruutu");
                OsionAlkuRuutu.osionAlkuPaneli.requestFocus();
                ÄänentoistamisSäie.toistaSFX("Hyväksy");
                break;
            case 1: // Asetukset
                alkuvalikonKorttiLayout.next(kortit);
                päivitäMuutokset();
                nappiPaneliAsetukset.requestFocus();
                break;
            case 2: // Huone-editori
                HuoneEditoriIkkuna.käynnistäEditori();
                break;
            case 3: // Kehittäjät
                CustomViestiIkkunat.Credits.näytäDialogi();
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
    static int musVal = 1;
    static int ääniVal = 1;
    public static void säädäValikkoa(String näppäin) {
        switch (näppäin) {
            case "vasen":
                switch (valintaAsetuksissa) {
                    case 0:
                        if (vaVal > 0) {
                            vaVal--;
                            vaValittu = vaVaihtoehdot[vaVal % 4];
                            ÄänentoistamisSäie.toistaSFX("Valinta");
                        }
                        PelinAsetukset.valitseVaikeusaste(vaValittu);
                    break;
                    case 1:
                        musVal--;
                        PelinAsetukset.musiikkiPäällä = musVal % 2 != 0;
                        ÄänentoistamisSäie.toistaSFX("Valinta");
                    break;
                    case 2:
                        ääniVal--;
                        PelinAsetukset.äänetPäällä = ääniVal % 2 != 0;
                        ÄänentoistamisSäie.toistaSFX("Valinta");
                    break;
                    case 3:
                        if (PelinAsetukset.tavoiteFPS > 0) {
                            PelinAsetukset.tavoiteFPS--;
                            ÄänentoistamisSäie.toistaSFX("Valinta");
                        }
                    break;
                    case 4:
                        if (PelinAsetukset.tavoiteTickrate > 1) {
                            PelinAsetukset.tavoiteTickrate--;
                            ÄänentoistamisSäie.toistaSFX("Valinta");
                        }
                    break;
                    default:
                    break;
                }
            break;
            case "oikea":
                switch (valintaAsetuksissa) {
                    case 0:
                        if (vaVal < vaVaihtoehdot.length -1) {
                            vaVal++;
                            vaValittu = vaVaihtoehdot[vaVal % 4];
                            ÄänentoistamisSäie.toistaSFX("Valinta");
                        }
                        PelinAsetukset.valitseVaikeusaste(vaValittu);
                    break;
                    case 1:
                        musVal++;
                        PelinAsetukset.musiikkiPäällä = musVal % 2 != 0;
                        ÄänentoistamisSäie.toistaSFX("Valinta");
                    break;
                    case 2:
                        ääniVal++;
                        PelinAsetukset.äänetPäällä = ääniVal % 2 != 0;
                        ÄänentoistamisSäie.toistaSFX("Valinta");
                    break;
                    case 3:
                        if (PelinAsetukset.tavoiteFPS < 1000) {
                            PelinAsetukset.tavoiteFPS++;
                            ÄänentoistamisSäie.toistaSFX("Valinta");
                        }
                    break;
                    case 4:
                        if (PelinAsetukset.tavoiteTickrate < 1000) {
                            PelinAsetukset.tavoiteTickrate++;
                            ÄänentoistamisSäie.toistaSFX("Valinta");
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
        oikeaOsoitinAsetukset[2].setText("" + (PelinAsetukset.äänetPäällä ? "PÄÄLLÄ" : "POIS"));
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
        vasenOsoitin[valinta] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.gif"));
    
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
        //vaikeusAste.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        vaikeusAste.setAlignmentX(Component.CENTER_ALIGNMENT);

        musiikkiPäällä = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_musiikki.png"));
        musiikkiPäällä.setForeground(Color.white);
        //musiikkiPäällä.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        musiikkiPäällä.setAlignmentX(Component.CENTER_ALIGNMENT);

        äänetPäällä = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_äänet.png"));
        äänetPäällä.setForeground(Color.white);
        //valitseMusiikki.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        äänetPäällä.setAlignmentX(Component.CENTER_ALIGNMENT);

        tavoiteFPS = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_tavoitefps.png"));
        tavoiteFPS.setForeground(Color.white);
        //tavoiteFPS.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        tavoiteFPS.setAlignmentX(Component.CENTER_ALIGNMENT);

        tavoiteTickrate = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_tavoitetickrate.png"));
        tavoiteTickrate.setForeground(Color.white);
        //tavoiteTickrate.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
        tavoiteTickrate.setAlignmentX(Component.CENTER_ALIGNMENT);

        takaisin = new JLabel(new ImageIcon("tiedostot/kuvat/menu/asetukset_hyväksy.png"));
        takaisin.setForeground(Color.white);
        //takaisin.setFont(new Font("Courier10 BT", Font.PLAIN, 30));
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
            oikeaOsoitinAsetukset[i].setFont(KeimoFontit.fontti_keimo_14);
            oikeaOsoitinAsetukset[i].setPreferredSize(new Dimension(60, 30));
        }
        oikeaOsoitinAsetukset[0].setText("" + PelinAsetukset.vaikeusAste);
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
        "Passiivinen: viholliset eivät vahingoita" + "<br>" + 
        "Normaali: viholliset tekevät normaalia vahinkoa" + "<br>" +
        "Vaikea: viholliset tekevät kaksinkertaista vahinkoa" + "<br>" +
        "Järjetön: viholliset tekevät 10-kertaista vahinkoa" +
        "</p></html>";

        infoTekstiAsetukset[1] = "<html><p>" +
        "Musiikki päällä" +
        "</p></html>";

        infoTekstiAsetukset[2] = "<html><p>" +
        "äänet päällä" +
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
        infoLabelAsetukset.setPreferredSize(new Dimension(500, 300));
        infoLabelAsetukset.setFont(KeimoFontit.fontti_keimo_12);
        infoPaneliAsetukset = new JPanel();
        infoPaneliAsetukset.setBackground(Color.black);
        infoPaneliAsetukset.setPreferredSize(new Dimension(500, 300));
        infoPaneliAsetukset.add(infoLabelAsetukset);

        JPanel nappiJaInfoPaneliAsetukset = new JPanel(new BorderLayout());
        nappiJaInfoPaneliAsetukset.setPreferredSize(new Dimension(1200, 300));
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
        valikkoPaneli.requestFocus();
        
        alkuValikkoPaneli = valikkoPaneli;
        päivitäMuutokset();
        ÄänentoistamisSäie.toistaPeliMusa("valikko");
        return valikkoPaneli;
    }

    public static void tarkistaArvot() {
        try {
            if (PelinAsetukset.vaikeusAste < 0) {
                JOptionPane.showMessageDialog(null, "Vaikeusaste ei voi olla negatiivinen!\n\n0 = Vihollisille ei voi kuolla", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            else if (PelinAsetukset.tavoiteFPS < 0) {
                JOptionPane.showMessageDialog(null, "Tavoite-FPS ei voi olla negatiivinen!\n\n0 = Max FPS", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
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
                            alkuvalikonKorttiLayout.previous(kortit);
                            nappiPaneliAlkuvalikko.requestFocus();
                        }
                        break;
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                        ValikkoRuutu.säädäValikkoa("vasen");
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        ValikkoRuutu.säädäValikkoa("oikea");
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
