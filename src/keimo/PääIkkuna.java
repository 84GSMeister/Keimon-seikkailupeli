package keimo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;

public class PääIkkuna {
    public static final int esineenKokoPx = 64;
    public static final int pelaajanKokoPx = 64;
    static int ikkunanLeveys = esineenKokoPx * Peli.kentänKoko;
    static int ikkunanKorkeus = esineenKokoPx * Peli.kentänKoko;
    static boolean uusiIkkuna = false;
    static JFrame ikkuna;
    static JMenuBar yläPalkki;
    static JMenu peli, tietoja, debug, työkalut;
    static JMenu huoneSubmenu;
    static JMenuItem huoneenVaihto, maastoGeneraattori, huoneEditori;
    static JMenuItem uusiPeli, mukauta, asetukset, ohjeet, tekijät;
    static JCheckBoxMenuItem näytäSijainti, näytäFPS, näytäReunat;
    static JMenuItem menuF2, menuF3, menuF4;
    static JPanel peliKenttä, peliKenttäUlompi, peliKenttäAlue, alueInfoPaneli;
    static JLabel kokoruudunTakatausta;
    static JPanel hud, yläPaneeli;
    public static JLabel hudTeksti;
    static JLabel ylätekstiAika;
    static JLabel ylätekstiSij;
    static JLabel ylätekstiKohde;
    static JLabel ylätekstiHP;
    public static JLabel ylätekstiKuparit;
    static JLabel ylätekstiViive;
    static JLabel ylätekstiFPS;
    static JLabel tavoiteTeksti1;
    static JLabel tavoiteTeksti2;
    static JLabel tavoiteTeksti3;
    static JPanel tekstiPaneli, infoPaneli, invaPaneli, tavaraPaneli, osoitinPaneli;
    static JLabel[] esineLabel = new JLabel[5];
    static JLabel[] osoitinLabel = new JLabel[5];
    static JLabel[] kontrolliInfoLabel = new JLabel[9];
    static JLabel peliTestiLabel, pelaajaLabel, pelaajanEsineLabel, taustaLabel, alueInfoLabel;
    static JLabel[][] kenttäKohteenKuvake;
    static JLabel[][] maastoKohteenKuvake;
    static JLabel[] npcKuvake;
    static boolean vaatiiPäivityksen = false;
    static boolean vaatiiKentänPäivityksen = false;
    static boolean uudelleenpiirräKaikki = false;
    static boolean pelaajaSiirtyi = false;
    static boolean fpsNäkyvissä = false;
    static boolean sijaintiNäkyvissä = false;
    static boolean reunatNäkyvissä = false;
    static JPanel peliAluePaneli, kortit;
    static CardLayout crd;

    // static class KenttäTakataustalla extends JPanel{
        
    //     //Image tausta = Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/kaboom_tausta.png");
    //     Image tausta = Toolkit.getDefaultToolkit().createImage("");
    //     @Override
    //     protected void paintComponent(Graphics g) {
    //         super.paintComponent(g);
    //         g.drawImage(tausta, 0, 0, null);
    //     }
    // }

    static void luoPääikkuna() {
        
        ikkunanLeveys = esineenKokoPx * Peli.kentänKoko + 50;
        ikkunanKorkeus = esineenKokoPx * Peli.kentänKoko + 330;
        kenttäKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];

        /**
         * Ikkunan ominaisuudet
         */
        
        ikkuna = new JFrame("Keimon Seikkailupeli v0.6.4 pre-alpha (7.4.2023)");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.revalidate();
        ikkuna.repaint();

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
                AsetusIkkuna.luoAsetusikkuna();
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
                CustomViestiIkkunat.Ohjeet.showDialog();
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
                näytäSijainti();
            }
        });

        näytäFPS = new JCheckBoxMenuItem("Näytä FPS");
        näytäFPS.setSelected(fpsNäkyvissä);
        näytäFPS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                näytäFPS();
            }
        });

        näytäReunat = new JCheckBoxMenuItem("Näytä reunat");
        näytäReunat.setSelected(reunatNäkyvissä);
        näytäReunat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                näytäReunat();
            }
        });

        menuF2 = new JMenuItem("F2 Uudelleenpiirrä kaikki");
        menuF2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.vaatiiPäivityksen = true;
                PääIkkuna.uudelleenpiirräKaikki = true;
                PääIkkuna.hudTeksti.setText("Ruudunpäivitys pakotettiin");
            }
        });

        menuF3 = new JMenuItem("F3 Päivitä kenttä");
        menuF3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.vaatiiKentänPäivityksen = true;
                PääIkkuna.hudTeksti.setText("Kentänpäivitys pakotettiin");
            }
        });

        menuF4 = new JMenuItem("F4 Näytä kohteen tiedot");
        menuF4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                näytäTiedot();
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
        debug.add(new JSeparator());
        debug.add(menuF2);
        debug.add(menuF3);
        debug.add(menuF4);
        debug.add(new JSeparator());
        debug.add(huoneSubmenu);

        maastoGeneraattori = new JMenuItem("Maastogeneraattori", new ImageIcon("tiedostot/kuvat/menu/gui/maastogeneraattori.png"));
        maastoGeneraattori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MukautusIkkuna.MaastoGeneraattoriIkkuna.luoMaastoGeneraattoriIkkuna();
            }
        });

        huoneEditori = new JMenuItem("Huone-editori", new ImageIcon("tiedostot/kuvat/menu/gui/huone-editori.png"));
        huoneEditori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //CustomViestiIkkunat.ToteutusPuuttuu.showDialog();
                HuoneEditoriIkkuna.luoEditoriIkkuna();
            }
        });

        työkalut = new JMenu("Työkalut");
        työkalut.add(maastoGeneraattori);
        työkalut.add(huoneEditori);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(peli);
        yläPalkki.add(tietoja);
        yläPalkki.add(debug);
        yläPalkki.add(työkalut);

        /**
         * Pelikentän yläosassa oleva tietopalkki (alue)
         */

        alueInfoLabel = new JLabel("Alue");
        alueInfoLabel.setPreferredSize(new Dimension(ikkunanLeveys-30, 30));
        alueInfoLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
        alueInfoLabel.setHorizontalAlignment(JLabel.CENTER);

        alueInfoPaneli = new JPanel();
        alueInfoPaneli.setLayout(new BorderLayout());
        alueInfoPaneli.setPreferredSize(new Dimension(ikkunanLeveys-30, 30));
        alueInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        alueInfoPaneli.add(alueInfoLabel, BorderLayout.CENTER);
        
        peliKenttä = new JPanel();
        peliKenttä.setLayout(null);
        peliKenttä.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        peliKenttä.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttä.revalidate();
        peliKenttä.repaint();

        kokoruudunTakatausta = new JLabel(new ImageIcon("tiedostot/kuvat/taustat/kokoruuduntausta.png"));
        kokoruudunTakatausta.setMaximumSize(new Dimension(300,300));
        kokoruudunTakatausta.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        /**
         * Varsinainen pelikenttä
         * Näiden komponenttien sisältöä muokataan päivitäIkkuna() -metodissa
         */

        peliKenttäUlompi = new JPanel();
        peliKenttäUlompi.setLayout(new GridBagLayout());
        peliKenttäUlompi.setPreferredSize(new Dimension(ikkunanLeveys-10, ikkunanLeveys -10));
        peliKenttäUlompi.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        //peliKenttäUlompi.add(kokoruudunTakatausta);
        peliKenttäUlompi.add(peliKenttä);
        //peliKenttäUlompi.setComponentZOrder(kokoruudunTakatausta, 1);
        peliKenttäUlompi.setComponentZOrder(peliKenttä, 0);
        peliKenttäUlompi.revalidate();
        peliKenttäUlompi.repaint();

        peliKenttäAlue = new JPanel();
        peliKenttäAlue.setLayout(new BorderLayout());
        peliKenttäAlue.setPreferredSize(new Dimension(ikkunanLeveys-10, ikkunanLeveys -10));
        peliKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttäAlue.add(alueInfoPaneli, BorderLayout.NORTH);
        peliKenttäAlue.add(peliKenttäUlompi, BorderLayout.CENTER);
        peliKenttäAlue.revalidate();
        peliKenttäAlue.repaint();

        int kohteenSijX = 10;
        int kohteensijY = 10;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                JLabel peliTestiLabel = new JLabel("PeliTestiLabel");
                peliTestiLabel.setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                peliKenttä.add(peliTestiLabel);
                kohteenSijX += esineenKokoPx;
            }
            kohteenSijX = 10;
            kohteensijY += esineenKokoPx;
        }

        /**
         * Alapalkissa oleva HUD muodostuu suurin piirtein näin:
         * 
         * ------------------------------------------------------------------
         *                           HUD-teksti                                <- tekstiPaneli----------------
         * ------------------------------------------------------------------                                  \
         *                     |osoitin |        |        |        |        |  <- osoitinPaneli                 -- hud
         *                     |        |        |        |        |        |                   \              /
         *                     |---------------------------------------------                    -- invaPaneli
         *     infoPaneli      |        |        |        |        |        |                   /
         *                     | esine1 | esine2 | esine3 | esine4 | esine5 |  <- tavaraPaneli
         *                     |        |        |        |        |        |
         * ------------------------------------------------------------------
         * 
         * Pelkät paneelit:
         * 
         * ------------------------------------------------------------------
         *                           tekstiPaneli                            
         * ------------------------------------------------------------------
         *                     |              osoitinPaneli
         *                     |
         *                     |---------------------------------------------
         *     infoPaneli      |
         *                     |              tavaraPaneli
         *                     |
         * ------------------------------------------------------------------
         */

        hudTeksti = new JLabel("Kokeile liikkua ja poimia esineitä!");
        
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new BorderLayout());
        tekstiPaneli.setPreferredSize(new Dimension(ikkunanLeveys -30, 20));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        tekstiPaneli.add(hudTeksti, BorderLayout.CENTER);
        tekstiPaneli.revalidate();
        tekstiPaneli.repaint();

        kontrolliInfoLabel[0] = new JLabel("Nuolet / WASD: Liiku");
        kontrolliInfoLabel[1] = new JLabel("Space: Käytä esinettä");
        kontrolliInfoLabel[2] = new JLabel("1-5: Vaihda tavarapaikkaa");
        kontrolliInfoLabel[3] = new JLabel("E: Poimi");
        kontrolliInfoLabel[4] = new JLabel("F: Vuorovaikutus");
        kontrolliInfoLabel[5] = new JLabel("Q: Pudota");
        kontrolliInfoLabel[6] = new JLabel("Z: Yhdistä");
        kontrolliInfoLabel[7] = new JLabel("X: Katso esinettä");
        kontrolliInfoLabel[8] = new JLabel("C: Katso kentän kohdetta");
        
        infoPaneli = new JPanel();
        infoPaneli.setLayout(new GridLayout(9, 1));
        infoPaneli.setPreferredSize(new Dimension(ikkunanLeveys-480, 120));
        infoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            infoPaneli.add(kontrolliInfoLabel[i]);
        }
        infoPaneli.revalidate();
        infoPaneli.repaint();

        for (int i = 0; i < osoitinLabel.length; i++) {
            osoitinLabel[i] = new JLabel("");
            osoitinLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }
        osoitinLabel[0] = new JLabel(new ImageIcon("tiedostot/kuvat/osoitin.png"));

        osoitinPaneli = new JPanel();
        osoitinPaneli.setLayout(new GridLayout(1, 5));
        osoitinPaneli.setPreferredSize(new Dimension(450, 30));
        osoitinPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < osoitinLabel.length; i++) {
            osoitinPaneli.add(osoitinLabel[i]);
        }
        osoitinPaneli.revalidate();
        osoitinPaneli.repaint();

        for (int i = 0; i < esineLabel.length; i++) {
            esineLabel[i] = new JLabel("Esine 0");
            esineLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }

        tavaraPaneli = new JPanel();
        tavaraPaneli.setLayout(new GridLayout(1, 5));
        tavaraPaneli.setPreferredSize(new Dimension(450, 90));
        tavaraPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < esineLabel.length; i++) {
            tavaraPaneli.add(esineLabel[i]);
        }
        tavaraPaneli.revalidate();
        tavaraPaneli.repaint();

        invaPaneli = new JPanel();
        invaPaneli.setLayout(new BorderLayout());
        invaPaneli.setPreferredSize(new Dimension(450, 120));
        invaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        invaPaneli.add(osoitinPaneli, BorderLayout.NORTH);
        invaPaneli.add(tavaraPaneli, BorderLayout.SOUTH);
        invaPaneli.revalidate();
        invaPaneli.repaint();
        
        hud = new JPanel();
        hud.setLayout(new BorderLayout());
        hud.setPreferredSize(new Dimension(ikkunanLeveys -30, 140));
        hud.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        hud.add(tekstiPaneli, BorderLayout.NORTH);
        hud.add(infoPaneli, BorderLayout.WEST);
        hud.add(invaPaneli, BorderLayout.EAST);
        hud.revalidate();
        hud.repaint();

        /**
         * Yläpaneelin infotekstit
         * Aika, sijainti, hp ym.
         */

        ylätekstiAika = new JLabel("Aika: ");
        ylätekstiAika.setVisible(true);
        ylätekstiAika.setBounds(210, 5, 220, 20);

        ylätekstiSij = new JLabel("Paina nuolinäppäimiä liikkuaksesi");
        ylätekstiSij.setVisible(sijaintiNäkyvissä);
        ylätekstiSij.setBounds(10,20, 400, 20);

        ylätekstiKohde = new JLabel("Kohteen sisältö näkyy tässä");
        ylätekstiKohde.setVisible(sijaintiNäkyvissä);
        ylätekstiKohde.setBounds(10,35, 400, 20);

        ylätekstiHP = new JLabel("HP: " + 10);
        ylätekstiHP.setVisible(true);
        ylätekstiHP.setBounds(10,5, 300, 20);

        ylätekstiKuparit = new JLabel("Tölkkejä: " + 0);
        ylätekstiKuparit.setVisible(true);
        ylätekstiKuparit.setBounds(430,5, 300, 20);

        ylätekstiViive = new JLabel("Päivitysaika");
        ylätekstiViive.setVisible(fpsNäkyvissä);
        ylätekstiViive.setBounds(210,20, 220, 20);

        ylätekstiFPS = new JLabel("FPS");
        ylätekstiFPS.setVisible(fpsNäkyvissä);
        ylätekstiFPS.setBounds(210,35, 220, 20);

        // tavoiteTeksti1 = new JLabel("Tavoite 1");
        // tavoiteTeksti1.setVisible(true);
        // tavoiteTeksti1.setBounds(430,5, 500, 20);

        // tavoiteTeksti2 = new JLabel("Tavoite 2");
        // tavoiteTeksti2.setVisible(true);
        // tavoiteTeksti2.setBounds(430,20, 500, 20);

        // tavoiteTeksti3 = new JLabel("Tavoite 3");
        // tavoiteTeksti3.setVisible(true);
        // tavoiteTeksti3.setBounds(430,35, 500, 20);

        yläPaneeli = new JPanel();
        yläPaneeli.setLayout(null);
        yläPaneeli.setPreferredSize(new Dimension(ikkunanLeveys -30, 60));
        yläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        yläPaneeli.add(ylätekstiAika);
        yläPaneeli.add(ylätekstiSij);
        yläPaneeli.add(ylätekstiKohde);
        yläPaneeli.add(ylätekstiHP);
        yläPaneeli.add(ylätekstiViive);
        yläPaneeli.add(ylätekstiFPS);
        yläPaneeli.add(ylätekstiKuparit);
        //yläPaneeli.add(tavoiteTeksti1);
        //yläPaneeli.add(tavoiteTeksti2);
        //yläPaneeli.add(tavoiteTeksti3);
        yläPaneeli.revalidate();
        yläPaneeli.repaint();

        peliAluePaneli = new JPanel();
        peliAluePaneli.setLayout(new BorderLayout());
        peliAluePaneli.add(yläPaneeli, BorderLayout.NORTH);
        peliAluePaneli.add(peliKenttäAlue, BorderLayout.CENTER);
        peliAluePaneli.add(hud, BorderLayout.SOUTH);

        crd = new CardLayout();
        kortit = new JPanel(crd);
        kortit.add(TarinaRuutu.luoTarinaPaneli());
        kortit.add(ValikkoRuutu.luoValikkoPaneli());
        kortit.add(OsionAlkuRuutu.kokeileLuodaOsionAlkuPaneli());
        kortit.add(peliAluePaneli);
        kortit.add(LoppuRuutu.luoLoppuRuutu());
        

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        //ikkuna.add(yläPaneeli);
        //ikkuna.add(peliKenttä);
        //ikkuna.add(hud);
        ikkuna.add(kortit, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

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

    static void näytäFPS() {
        if (!fpsNäkyvissä) {
            fpsNäkyvissä = true;
            ylätekstiViive.setVisible(true);
            ylätekstiFPS.setVisible(true);
        }
        else {
            fpsNäkyvissä = false;
            ylätekstiViive.setVisible(false);
            ylätekstiFPS.setVisible(false);
        }
    }

    static void näytäSijainti() {
        if (!sijaintiNäkyvissä) {
            sijaintiNäkyvissä = true;
            ylätekstiSij.setVisible(true);
            ylätekstiKohde.setVisible(true);
        }
        else {
            sijaintiNäkyvissä = false;
            ylätekstiSij.setVisible(false);
            ylätekstiKohde.setVisible(false);
        }
    }

    static void näytäReunat() {
        if (!reunatNäkyvissä) {
            reunatNäkyvissä = true;
        }
        else {
            reunatNäkyvissä = false;
        }
        vaatiiKentänPäivityksen = true;
    }

    static ImageIcon valitsePelaajanKuvake() {
        ImageIcon pelaajanKuvake = Pelaaja.kuvake;
        return pelaajanKuvake;
    }

    static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        peliKenttä.removeAll();
        peliKenttäAlue.setBackground(new Color(0, 0, 0));
        
        int kohteenSijX = 10;
        int kohteensijY = 10;
        
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                kenttäKohteenKuvake[j][i] = new JLabel();
                maastoKohteenKuvake[j][i] = new JLabel();
            }
        }
        pelaajaLabel = new JLabel(pelaajaKuvake);
        pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
        pelaajaLabel.setIcon(valitsePelaajanKuvake());
        pelaajanEsineLabel = new JLabel(pelaajaKuvake);
        pelaajanEsineLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx/2, pelaajanKokoPx/2);
        pelaajanEsineLabel.setIcon(valitsePelaajanKuvake());
        taustaLabel = new JLabel(new ImageIcon());
        taustaLabel.setBounds(0, 0, Peli.kentänKoko * esineenKokoPx + 20, Peli.kentänKoko * esineenKokoPx + 20);
        npcKuvake = new JLabel[Peli.annaNPCidenMäärä()];
        
        try {
            peliKenttä.add(pelaajaLabel);
            peliKenttä.add(pelaajanEsineLabel);
            peliKenttä.add(taustaLabel);
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (kenttäKohteenKuvake[j][i] == null) {

                    }
                    else {
                        if (j == sijX && i == sijY) {
                            if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                            }
                            if (Peli.maastokenttä[j][i] instanceof Maasto) {
                                maastoKohteenKuvake[j][i].setIcon(Peli.maastokenttä[j][i].annaKuvake());
                            }
                        }
                        if (reunatNäkyvissä) {
                            if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                                else if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                    kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                                    if (Peli.pelikenttä[j][i] instanceof Kiintopiste) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof Esine) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof NPC_KenttäKohde) {
                                        if (Peli.pelikenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                        }
                                        else {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        }
                                        
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof Warp) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                    }
                                }
                            }
                            else if (Peli.pelikenttä[j][i] == null) {
                                kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                            }
                        }
                        else {
                            kenttäKohteenKuvake[j][i].setBorder(null);
                        }
                        if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                        }
                        if (Peli.maastokenttä[j][i] instanceof Maasto) {
                            maastoKohteenKuvake[j][i].setIcon(Peli.maastokenttä[j][i].annaKuvake());
                        }
                    
                        kenttäKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        maastoKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        peliKenttä.add(kenttäKohteenKuvake[j][i]);
                        peliKenttä.setComponentZOrder(kenttäKohteenKuvake[j][i], 1);
                        peliKenttä.add(maastoKohteenKuvake[j][i]);
                        peliKenttä.setComponentZOrder(maastoKohteenKuvake[j][i], 2);
                        kohteenSijX += esineenKokoPx;
                    }
                }
                kohteenSijX = 10;
                kohteensijY += esineenKokoPx;
            }

            if (Peli.npcLista != null){
                npcKuvake = new JLabel[Peli.npcLista.size()];
                for (int i = 0; i < Peli.npcLista.size(); i++) {
                    NPC npc = Peli.npcLista.get(i);
                    npcKuvake[i] = new JLabel();
                    npcKuvake[i].setIcon(npc.kuvake);
                    npcKuvake[i].setBounds(npc.sijX_PX_vy, npc.sijY_PX_vy, pelaajanKokoPx, pelaajanKokoPx);
                    peliKenttä.add(npcKuvake[i]);
                    peliKenttä.setComponentZOrder(npcKuvake[i], 0);
                }
            }
            
            peliKenttä.setComponentZOrder(pelaajaLabel, 0);
            peliKenttä.setComponentZOrder(pelaajanEsineLabel, 0);
            //peliKenttä.setComponentZOrder(taustaLabel, 2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e) {

        }
        catch (NullPointerException e) {
            System.out.println("ongelma alkuikkunan luonnissa");
            e.printStackTrace();
        }

        peliKenttä.revalidate();
        peliKenttä.repaint();
    }

    static JPanel päivitäIkkuna() {
        
        if (vaatiiPäivityksen) {
            try {
                
                ylätekstiHP.setText("HP: " + Pelaaja.hp);
                ylätekstiKuparit.setText("Tölkkejä: " + Pelaaja.kuparit);
                
                for (int i = 0; i < Peli.kentänKoko; i++) {
                    for (int j = 0; j < Peli.kentänKoko; j++) {
                        if (kenttäKohteenKuvake[j][i] != null) {
                            if (Peli.pelikenttä[j][i] instanceof KenttäKohde && kenttäKohteenKuvake[j][i] != null) {
                                kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                            }
                            else if (kenttäKohteenKuvake[j][i] != null) {
                                kenttäKohteenKuvake[j][i].setIcon(null);
                            }
                            if (Peli.maastokenttä[j][i] instanceof Maasto && maastoKohteenKuvake[j][i] != null) {
                                maastoKohteenKuvake[j][i].setIcon(Peli.maastokenttä[j][i].annaKuvake());
                            }
                            else if (maastoKohteenKuvake[j][i] != null){
                                maastoKohteenKuvake[j][i].setIcon(null);
                            }

                            if (vaatiiKentänPäivityksen) {
                                if (reunatNäkyvissä) {
                                    if (Pelaaja.sijX== j && Pelaaja.sijY == i) {
                                        kenttäKohteenKuvake[j][i].setBorder(null);
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                        kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                                        if (Peli.pelikenttä[j][i] instanceof Kiintopiste) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                        }
                                        else if (Peli.pelikenttä[j][i] instanceof Esine) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                        }
                                        else if (Peli.pelikenttä[j][i] instanceof NPC_KenttäKohde) {
                                            if (Peli.pelikenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                                                kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                            }
                                            else {
                                                kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                            }
                                            
                                        }
                                        else if (Peli.pelikenttä[j][i] instanceof Warp) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                        }
                                    }
                                    else if (Peli.pelikenttä[j][i] == null) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                                    }
                                }
                                else {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                            }
                        }
                    }
                }

                //if (pelaajaSiirtyi || vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                    pelaajaLabel.setBounds((int)Pelaaja.hitbox.getMinX() + 10, (int)Pelaaja.hitbox.getMinY() + 10, pelaajanKokoPx, pelaajanKokoPx);
                    pelaajaLabel.setIcon(valitsePelaajanKuvake());
                    pelaajanEsineLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx/2, pelaajanKokoPx/2);
                    pelaajanEsineLabel.setBounds((int)Pelaaja.hitbox.getMinX() + 10, (int)Pelaaja.hitbox.getMinY() + 10, pelaajanKokoPx/2, pelaajanKokoPx/2);
                    if (Pelaaja.esineet[Peli.esineValInt] != null) {
                        ImageIcon pelaajanEsineenKuvake = (ImageIcon)Pelaaja.esineet[Peli.esineValInt].annaKuvake();
                        Image kuvake64 = pelaajanEsineenKuvake.getImage();
                        Image kuvake32 = kuvake64.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                        pelaajanEsineenKuvake = new ImageIcon(kuvake32);
                        pelaajanEsineLabel.setIcon(pelaajanEsineenKuvake);

                        //pelaajanEsineLabel.setIcon(new ImageIcon(((ImageIcon)Pelaaja.esineet[Peli.esineValInt].annaKuvake()).getImage()));
                    }
                    else {
                        pelaajanEsineLabel.setIcon(null);
                    }
                    pelaajaSiirtyi = false;
                //}
                if (Peli.npcLista != null) {
                    for (int i = 0; i < Peli.npcLista.size(); i++) {
                        NPC npc = Peli.npcLista.get(i);
                        if (Peli.npcLista.size() == npcKuvake.length) {
                            if (npcKuvake[i] != null && npc != null) {
                                npcKuvake[i].setIcon(npc.kuvake);
                                npcKuvake[i].setBounds(npc.sijX_PX_vy, npc.sijY_PX_vy, pelaajanKokoPx, pelaajanKokoPx);
                            }
                        }
                    }
                }
                if (Peli.huone != null) {
                    alueInfoLabel.setText(Peli.huone.annaAlue());
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Ongelma ladatessa kuvaa objektille");
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }

            peliKenttä.revalidate();
            peliKenttä.repaint();
            vaatiiPäivityksen = false;
            vaatiiKentänPäivityksen = false;
        }
        return peliKenttä;
    }
    static void vaihdaTausta(ImageIcon tausta) {
        if (tausta == null) {
            taustaLabel.setVisible(false);
        }
        else{
            taustaLabel.setIcon(tausta);
            taustaLabel.setVisible(true);
        }
    }

    
    class AjastimenPäivittäjä extends SwingWorker<Void, JLabel> {
        
        boolean ajastinKäynnissä = true;
        double kulunutAika = 0;
        int kulunutAikaMin = 0;
        double kulunutAikaSek = 0;
    
        DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

        @Override
        protected Void doInBackground() {
        
            while (!isCancelled()) {
                odotaMillisekunteja(10);
                //publish(PääIkkuna.päivitäIkkuna());
            }
            return null;
        }

        protected void process() {
            JLabel label = PääIkkuna.ylätekstiAika;
            label.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
        }

        public void odotaMillisekunteja(long millisekunnit) {
        
            long odotaKunnes = System.nanoTime() + (millisekunnit * 1_000_000);
            
            kulunutAika += 0.01;
            kulunutAikaSek = kulunutAika % 60;
            kulunutAikaMin = (int)kulunutAika / 60;
    
            //PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
            
            while(odotaKunnes > System.nanoTime()){
                ;
            }
        }
    }
}
