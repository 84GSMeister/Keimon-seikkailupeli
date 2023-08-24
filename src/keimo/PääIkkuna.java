package keimo;

import keimo.Ruudut.*;
import keimo.Ruudut.Lisäruudut.ValintaDialogiRuutu;
import keimo.Säikeet.PeliSäie;
import keimo.Dialogi.VuoropuheDialogit;
import keimo.HuoneEditori.*;
import keimo.Ikkunat.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

public final class PääIkkuna {

    public static int ikkunanLeveys = PeliRuutu.esineenKokoPx * Peli.kentänKoko;
    static int ikkunanKorkeus = PeliRuutu.esineenKokoPx * Peli.kentänKoko;
    protected static boolean fullscreen = false;
    public static boolean uusiIkkuna = false;
    public static JFrame ikkuna;
    static JMenuBar yläPalkki;
    static JMenu peli, tietoja, debug, työkalut;
    static JMenu huoneSubmenu;
    static JMenuItem huoneenVaihto, mukauta, huoneEditori;
    static JMenuItem uusiPeli, asetukset, ohjeet, tekijät;
    static JCheckBoxMenuItem näytäSijainti, näytäFPS, näytäReunat, näytäTapahtumapalkki;
    static JMenuItem menuF2, menuF3, menuF4;
    public static JLabel hudTeksti;
    static JLabel tavoiteTeksti1;
    static JLabel tavoiteTeksti2;
    static JLabel tavoiteTeksti3;
    public static boolean vaatiiPäivityksen = false;
    public static boolean uudelleenpiirräKaikki = false;
    public static boolean uudelleenpiirräKenttä = false;
    public static boolean uudelleenpiirräObjektit = false;
    public static boolean pelaajaSiirtyi = false;
    public static boolean fpsNäkyvissä = false;
    public static boolean sijaintiNäkyvissä = false;
    public static boolean reunatNäkyvissä = false;
    public static boolean tapahtumapalkkiNäkyvissä = false;
    public static JPanel pääPaneeli;
    public static boolean ikkunanKokoMuutettuEnnenHuoneenLatuasta = false;
    static GraphicsDevice näytöt = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    public static String lainausmerkki = "";

    static void luoPääikkuna() {
        
        ikkunanLeveys = PeliRuutu.esineenKokoPx * Peli.kentänKoko + 50;
        ikkunanKorkeus = PeliRuutu.esineenKokoPx * Peli.kentänKoko + 330;

        hudTeksti = new JLabel();

        /**
         * Ikkunan ominaisuudet
         */
        
        if (ikkuna == null) {
            ikkuna = new JFrame("Keimon Seikkailupeli v.0.8.4 pre-alpha (24.8.2023)");
            ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
            ikkuna.setLayout(new BorderLayout());
            ikkuna.setBackground(Color.black);
            ikkuna.setVisible(true);
            ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ikkuna.setBounds(0, 0, 1366, 768);
            //ikkuna.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ikkuna.setLocationRelativeTo(null);
            ikkuna.revalidate();
            ikkuna.repaint();
        }

        /**
         * Yläpalkin Menu-komponentit ja niiden ominaisuudet
         */

        uusiPeli = new JMenuItem("Uusi peli", new ImageIcon("tiedostot/kuvat/menu/gui/uusipeli.png"));
        uusiPeli.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uusiIkkuna = true;
            }
        });
        
        asetukset = new JMenuItem("Asetukset", new ImageIcon("tiedostot/kuvat/menu/gui/asetukset.png"));
        asetukset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!AsetusIkkuna.asetuksetAuki()) {
                    AsetusIkkuna.luoAsetusikkuna();
                }
                else {
                    AsetusIkkuna.asetaPäällimmäiseksi();
                }
            }
        });

        peli = new JMenu("Peli");
        peli.add(uusiPeli);;
        peli.add(new JSeparator());
        peli.add(asetukset);

        ohjeet = new JMenuItem("Ohjeet", new ImageIcon("tiedostot/kuvat/menu/gui/ohjeet.png"));
        ohjeet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Peli.pause = true;
                CustomViestiIkkunat.Ohjeet.showDialog(PeliRuutu.tavoiteInfoLabel.getText());
                Peli.pause = false;
            }
        });

        tekijät = new JMenuItem("Tekijät", new ImageIcon("tiedostot/kuvat/menu/gui/tekijät.png"));
        tekijät.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Peli.pause = true;
                CustomViestiIkkunat.Credits.showDialog();
                Peli.pause = false;
            }
        });

        tietoja = new JMenu("Tietoja");
        tietoja.add(ohjeet);
        tietoja.add(new JSeparator());
        tietoja.add(tekijät);

        näytäSijainti = new JCheckBoxMenuItem("Näytä sijainti");
        näytäSijainti.setSelected(sijaintiNäkyvissä);
        näytäSijainti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PeliRuutu.näytäSijainti();
            }
        });

        näytäFPS = new JCheckBoxMenuItem("Näytä FPS");
        näytäFPS.setSelected(fpsNäkyvissä);
        näytäFPS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PeliRuutu.näytäFPS();
            }
        });

        näytäReunat = new JCheckBoxMenuItem("Näytä reunat");
        näytäReunat.setSelected(reunatNäkyvissä);
        näytäReunat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                näytäReunat();
            }
        });

        näytäTapahtumapalkki = new JCheckBoxMenuItem("Näytä tapahtumapalkki");
        näytäTapahtumapalkki.setSelected(tapahtumapalkkiNäkyvissä);
        näytäTapahtumapalkki.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                näytäTapahtumapalkki();
            }
        });

        menuF2 = new JMenuItem("F2 Uudelleenpiirrä kaikki");
        menuF2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (PeliRuutu.peliRuutuPaneli != null) {
                    if (PeliRuutu.peliRuutuPaneli.isVisible()) {
                        PääIkkuna.vaatiiPäivityksen = true;
                        PääIkkuna.uudelleenpiirräKaikki = true;
                        PeliRuutu.hudTeksti.setText("Ruudunpäivitys pakotettiin");
                    }
                }
            }
        });

        menuF3 = new JMenuItem("F3 Uudelleenpiirrä kenttä");
        menuF3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.uudelleenpiirräKenttä = true;
                PeliRuutu.hudTeksti.setText("Kentänpäivitys pakotettiin");
            }
        });

        menuF4 = new JMenuItem("F4 Uudelleenpiirrä objektit");
        menuF4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.uudelleenpiirräObjektit = true;
                PeliRuutu.hudTeksti.setText("Objektien päivitys pakotettiin");
            }
        });

        huoneenVaihto = new JMenuItem("Warppaa huoneeseen", new ImageIcon("tiedostot/kuvat/menu/gui/warppaa.png"));
        huoneenVaihto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HuoneenVaihtoIkkuna.luoHuoneenVaihtoikkuna();
            }
        });

        mukauta = new JMenuItem("Luo huone", new ImageIcon("tiedostot/kuvat/menu/gui/mukauta.png"));
        mukauta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MukautusIkkuna.luoMukautusikkuna();
            }
        });

        huoneSubmenu = new JMenu("Huone");
        huoneSubmenu.setIcon(new ImageIcon("tiedostot/kuvat/menu/gui/huone.png"));
        huoneSubmenu.add(huoneenVaihto);
        huoneSubmenu.add(mukauta);
        
        debug = new JMenu("Debug");
        debug.add(näytäSijainti);
        debug.add(näytäFPS);
        debug.add(näytäReunat);
        debug.add(näytäTapahtumapalkki);
        debug.add(new JSeparator());
        debug.add(menuF2);
        debug.add(menuF3);
        debug.add(menuF4);
        debug.add(new JSeparator());
        debug.add(huoneSubmenu);

        huoneEditori = new JMenuItem("Huone-editori", new ImageIcon("tiedostot/kuvat/menu/gui/huone-editori.png"));
        huoneEditori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!HuoneEditoriIkkuna.editoriAuki()) {
                    HuoneEditoriIkkuna.luoEditoriIkkuna();
                }
                else {
                    HuoneEditoriIkkuna.asetaPäällimmäiseksi();
                }
            }
        });

        työkalut = new JMenu("Työkalut");
        työkalut.add(huoneEditori);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(peli);
        yläPalkki.add(tietoja);
        yläPalkki.add(debug);
        yläPalkki.add(työkalut);

        pääPaneeli = new JPanel(new BorderLayout());
        

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        ikkuna.add(pääPaneeli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        lataaLainausmerkki();
    }
    /**
     * Valitse pääpaneelin "tila" eli mikä osa peliä on näkyvissä.
     * Jokaisen paneelin sisältö määritellään omassa luokassaan Ruudut-kansiossa.
     * Esim. PeliRuutu.java sisältää kaikki pelikentän ja hudin ym. komponentit.
     * @param ruutu ruudun tunniste
     */

    public static void lataaRuutu(String ruutu) {
        pääPaneeli.removeAll();
        PeliRuutu.peliRuutuAktiivinen = false;
        switch (ruutu) {
            case "tarinaruutu":
                //try {
                    TarinaRuutu.luoTarinaPaneli("alku");
                    pääPaneeli.add(TarinaRuutu.tarinaPaneli, BorderLayout.CENTER);
                //}
                //catch (NullPointerException npe) {
                //    JOptionPane.showMessageDialog(null, "Tarinaa ei voitu ladata. Tämä voi johtua vanhentuneesta default.kst -tiedostosta.", "Tarinaa ei löytynyt", JOptionPane.ERROR_MESSAGE);
                //    ValikkoRuutu.luoValikkoPaneli();
                //    pääPaneeli.add(ValikkoRuutu.alkuValikkoPaneli, BorderLayout.CENTER);
                //}
            break;
            case "valikkoruutu":
                ValikkoRuutu.luoValikkoPaneli();
                pääPaneeli.add(ValikkoRuutu.alkuValikkoPaneli, BorderLayout.CENTER);
            break;
            case "peliruutu":
                pääPaneeli.add(PeliRuutu.peliAluePaneli, BorderLayout.CENTER);
                PeliRuutu.peliRuutuAktiivinen = true;
            break;
            case "osionalkuruutu":
                pääPaneeli.add(OsionAlkuRuutu.kokeileLuodaOsionAlkuPaneli(), BorderLayout.CENTER);
            break;
            case "loppuruutu":
                pääPaneeli.add(LoppuRuutu.luoLoppuRuutu(), BorderLayout.CENTER);
            break;
        }
        pääPaneeli.revalidate();
        pääPaneeli.repaint();
    }

    public static void lataaTarinaRuutu(String tarinaRuudunTunniste) {
        pääPaneeli.removeAll();
        pääPaneeli.add(TarinaRuutu.luoTarinaPaneli(tarinaRuudunTunniste), BorderLayout.CENTER);
        pääPaneeli.revalidate();
        pääPaneeli.repaint();
    }

    public static int tekstiäJäljellä;
    public static boolean tekstiAuki = false;

    public static String dialogiTeksti = "";
    static String kelattuTeksti = "";
    private static void luoVuoropuheRuutu(Icon kuvake, String teksti, String nimi) {
        kelattuTeksti = teksti;
        //TekstiAjastinSäie.dialogiTeksti = teksti;
        dialogiTeksti = teksti;
        PeliRuutu.vuoropuheKuvake.setIcon(kuvake);
        PeliRuutu.vuoropuheNimi.setText(nimi);
        tekstiäJäljellä = teksti.length();
    }

    public static int dialogiaJäljellä = 0;
    public static boolean useitaRuutuja = false;

    

    public static void avaaPitkäDialogiRuutu(String vuoropuheRuudunTunniste) {
        VuoropuheDialogit.luoYksityiskohtainenVuoropuheRuutu(vuoropuheRuudunTunniste);
        avaaDialogi(VuoropuheDialogit.dialogiKuvat[0], VuoropuheDialogit.dialogiTekstit[0], VuoropuheDialogit.dialogiPuhujat[0]);
    }

    public static void avaaPitkäDialogiRuutu(String vuoropuheRuudunTunniste, String valinnanTunniste) {
        VuoropuheDialogit.luoYksityiskohtainenVuoropuheRuutu(vuoropuheRuudunTunniste);
        avaaDialogi(VuoropuheDialogit.dialogiKuvat[0], VuoropuheDialogit.dialogiTekstit[0], VuoropuheDialogit.dialogiPuhujat[0], true, valinnanTunniste);
    }

    public static boolean äläSuljeNuolilla = false;
    public static String valintaTulossa = null;

    public static void avaaDialogi(Icon kuvake, String teksti, String nimi) {
        if (Peli.dialoginAvausViive <= 0 || useitaRuutuja) {
            Peli.pause = true;
            äläSuljeNuolilla = false;
            tekstiAuki = true;
            luoVuoropuheRuutu(kuvake, teksti, nimi);
            PeliRuutu.vuoropuheTeksti.setText("");
            PeliRuutu.vuoropuhePaneli.setVisible(true);
            Peli.dialoginAvausViive = 5;
        }
    }

    public static void avaaDialogi(Icon kuvake, String teksti, String nimi, boolean estäNuolet) {
        if (Peli.dialoginAvausViive <= 0 || useitaRuutuja) {
            Peli.pause = true;
            äläSuljeNuolilla = estäNuolet;
            tekstiAuki = true;
            luoVuoropuheRuutu(kuvake, teksti, nimi);
            PeliRuutu.vuoropuheTeksti.setText("");
            PeliRuutu.vuoropuhePaneli.setVisible(true);
            Peli.dialoginAvausViive = 5;
        }
    }

    public static void avaaDialogi(Icon kuvake, String teksti, String nimi, boolean estäNuolet, String valinnanTunniste) {
        if (Peli.dialoginAvausViive <= 0 || useitaRuutuja) {
            Peli.pause = true;
            äläSuljeNuolilla = estäNuolet;
            tekstiAuki = true;
            luoVuoropuheRuutu(kuvake, teksti, nimi);
            PeliRuutu.vuoropuheTeksti.setText("");
            PeliRuutu.vuoropuhePaneli.setVisible(true);
            Peli.dialoginAvausViive = 5;
            valintaTulossa = valinnanTunniste;
        }
    }

    public static void kelaaDialogi() {
        if (tekstiäJäljellä <= 1) {
            suljeDialogi();
        }
        else {
            tekstiäJäljellä = 1;
            PeliRuutu.vuoropuheTeksti.setText(kelattuTeksti);
        }
    }

    public static void suljeDialogi() {
        if (dialogiaJäljellä > 1) {
            VuoropuheDialogit.siirrySeuraavaanDialogiRuutuun(VuoropuheDialogit.dialoginPituus - dialogiaJäljellä + 1);
        }
        else if (valintaTulossa != null) {
            ValintaDialogiRuutu.luoValintaDialogiIkkuna(valintaTulossa);
            valintaTulossa = null;
        }
        else {
            Peli.pause = false;
            tekstiAuki = false;
            useitaRuutuja = false;
            PeliRuutu.vuoropuhePaneli.setVisible(false);
        }
    }

    static void näytäTiedot() {
        String tiedot = "";
        if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
            tiedot += "Kohteen tiedot: \n";
            tiedot += "Kohteessa ei ole mitään.";
        }
        else {
            tiedot += "Kohteen tiedot: \n";
            tiedot += Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY].annaTiedot();
        }
        tiedot += "\n\n";
        if (Peli.maastokenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
            tiedot += "Maaston tiedot: \n";
            tiedot += "Normaali maasto.";
        }
        else {
            tiedot += "Maaston tiedot: \n";
            tiedot += Peli.maastokenttä[Pelaaja.sijX][Pelaaja.sijY].annaTiedot();
        }
        JOptionPane.showMessageDialog(null, tiedot, "Kohteen tiedot", JOptionPane.INFORMATION_MESSAGE);
    }

    static void näytäReunat() {
        if (!reunatNäkyvissä) {
            reunatNäkyvissä = true;
        }
        else {
            reunatNäkyvissä = false;
        }
        uudelleenpiirräKenttä = true;
    }

    static void näytäTapahtumapalkki() {
        if (!tapahtumapalkkiNäkyvissä) {
            tapahtumapalkkiNäkyvissä = true;
            PeliRuutu.alaPaneeli.setVisible(true);
        }
        else {
            tapahtumapalkkiNäkyvissä = false;
            PeliRuutu.alaPaneeli.setVisible(false);
        }
    }

    public static ImageIcon valitsePelaajanKuvake() {
        ImageIcon pelaajanKuvake = Pelaaja.kuvake;
        return pelaajanKuvake;
    }

    public static void lataaLainausmerkki() {
        try {
            Scanner sc = new Scanner(new File("tiedostot/tekstit/lainausmerkki.txt"));
            while (sc.hasNextLine()) {
                String rivi = sc.nextLine();
                lainausmerkki = rivi;
                System.out.println(lainausmerkki);
            }
        }
        catch (FileNotFoundException fne) {
            fne.printStackTrace();
        }
    }
}
