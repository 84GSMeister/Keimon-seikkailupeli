import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.DecimalFormat;

public class PääIkkuna {
    static final int EsineenKokoPx = 64;
    static final int PelaajanKokoPx = 64;
    static int ikkunanLeveys = EsineenKokoPx * Main.kentänKoko + 50;
    static int ikkunanKorkeus = EsineenKokoPx * Main.kentänKoko + 310;
    static boolean uusiIkkuna = false;
    static JFrame ikkuna;
    static JMenuBar yläPalkki;
    static JMenu peli, tietoja, debug;
    static JMenu kaksoispistedeeValikko, kaksoispistedeeValikkoSubmenu;
    static JMenu huoneSubmenu;
    static JMenuItem huoneenVaihto, testiHuoneOletus, testiHuone1, testiHuone2, testiHuone3;
    static JMenuItem uusiPeli, mukauta, asetukset, ohjeet, tekijät;
    static JCheckBoxMenuItem näytäSijainti, näytäFPS, näytäReunat;
    static JMenuItem kaksoispistedeeValikkoItemi, kaksoispistedeeValikkoItemi2, kaksoispistedeeValikkoItemi3, kaksoispistedeeValikkoSubmenu2, kaksoispistedeeValikkoSubmenu3;
    static JPanel peliKenttäAlue;
    static KenttäTakataustalla peliKenttä;
    static JPanel hud, yläPaneeli;
    static JLabel hudTeksti, ylätekstiAika, ylätekstiSij, ylätekstiKohde, ylätekstiHP, ylätekstiViive, ylätekstiFPS, tavoiteTeksti1, tavoiteTeksti2, tavoiteTeksti3;
    static JPanel tekstiPaneli, infoPaneli, invaPaneli, tavaraPaneli, osoitinPaneli;
    static JLabel[] esineLabel = new JLabel[5];
    static JLabel[] osoitinLabel = new JLabel[5];
    static JLabel[] kontrolliInfoLabel = new JLabel[9];
    static JLabel peliTestiLabel, pelaajaLabel, taustaLabel;
    static JLabel[][] kenttäKohteenKuvake;
    static boolean vaatiiPäivityksen = false;
    static boolean vaatiiTäysPäivityksen = false;
    static boolean uudelleenpiirräKaikki = false;
    static boolean pelaajaSiirtyi = false;
    static boolean fpsNäkyvissä = false;
    static boolean sijaintiNäkyvissä = false;
    static boolean reunatNäkyvissä = true;

    static class KenttäTakataustalla extends JPanel{
        
        //Image tausta = Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/kaboom_tausta.png");
        Image tausta = Toolkit.getDefaultToolkit().createImage("");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(tausta, 0, 0, null);
        }
    }

    static void luoPääikkuna() {
        
        ikkunanLeveys = EsineenKokoPx * Main.kentänKoko + 50;
        ikkunanKorkeus = EsineenKokoPx * Main.kentänKoko + 300;
        kenttäKohteenKuvake = new JLabel[Main.kentänKoko][Main.kentänKoko];
        
        ikkuna = new JFrame("Keimon Seikkailupeli v0.3.1 alpha (18.12.2022)");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.revalidate();
        ikkuna.repaint();

        uusiPeli = new JMenuItem("Uusi peli", new ImageIcon("./kuvat/pelaaja32.png"));
        uusiPeli.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uusiIkkuna = true;
            }
        });

        mukauta = new JMenuItem("Mukauta", new ImageIcon("./kuvat/pelaaja32.png"));
        mukauta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //CustomViestiIkkunat.ToteutusPuuttuu.showDialog();
                MukautusIkkuna.luoMukautusikkuna();
            }
        });

        asetukset = new JMenuItem("Asetukset", new ImageIcon("./kuvat/pelaaja32.png"));
        asetukset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //CustomViestiIkkunat.ToteutusPuuttuu.showDialog();
                AsetusIkkuna.luoAsetusikkuna();
            }
        });

        peli = new JMenu("Peli");
        peli.add(uusiPeli);;
        peli.add(mukauta);
        peli.add(new JSeparator());
        peli.add(asetukset);

        ohjeet = new JMenuItem("Ohjeet");
        ohjeet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Ohjeet.showDialog();
            }
        });

        tekijät = new JMenuItem("Tekijät");
        tekijät.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Credits.showDialog();
            }
        });

        tietoja = new JMenu("Tietoja");
        tietoja.add(ohjeet);
        tietoja.add(new JSeparator());
        tietoja.add(tekijät);

        kaksoispistedeeValikkoItemi = new JMenuItem(":D");
        kaksoispistedeeValikkoItemi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Kaksoispistedee.showDialog();
            }
        });

        kaksoispistedeeValikkoItemi2 = new JMenuItem(":D");
        kaksoispistedeeValikkoItemi2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Kaksoispistedee.showDialog();
            }
        });

        kaksoispistedeeValikkoItemi3 = new JMenuItem(":D");
        kaksoispistedeeValikkoItemi3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Kaksoispistedee.showDialog();
            }
        });

        kaksoispistedeeValikkoSubmenu = new JMenu(":D");
        kaksoispistedeeValikkoSubmenu.add(kaksoispistedeeValikkoItemi);
        kaksoispistedeeValikkoSubmenu.add(kaksoispistedeeValikkoItemi2);
        kaksoispistedeeValikkoSubmenu.add(kaksoispistedeeValikkoItemi3);

        kaksoispistedeeValikkoSubmenu2 = new JMenuItem(":D");
        kaksoispistedeeValikkoSubmenu2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Kaksoispistedee.showDialog();
            }
        });

        kaksoispistedeeValikkoSubmenu3 = new JMenuItem(":D");
        kaksoispistedeeValikkoSubmenu3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Kaksoispistedee.showDialog();
            }
        });

        kaksoispistedeeValikko = new JMenu(":D");
        kaksoispistedeeValikko.add(kaksoispistedeeValikkoSubmenu3);
        kaksoispistedeeValikko.add(kaksoispistedeeValikkoSubmenu2);
        kaksoispistedeeValikko.add(kaksoispistedeeValikkoSubmenu);

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

        huoneenVaihto = new JMenuItem("Warppaa huoneeseen");
        huoneenVaihto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HuoneenVaihtoIkkuna.luoHuoneenVaihtoikkuna();
            }
        });

        testiHuoneOletus = new JMenuItem("Oletushuone");
        testiHuoneOletus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.uusiHuone = 0;
                Main.huoneVaihdettava = true;
            }
        });

        testiHuone1 = new JMenuItem("Testihuone 1");
        testiHuone1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.uusiHuone = 1;
                Main.huoneVaihdettava = true;
            }
        });

        testiHuone2 = new JMenuItem("Testihuone 2");
        testiHuone2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.uusiHuone = 2;
                Main.huoneVaihdettava = true;
            }
        });

        testiHuone3 = new JMenuItem("Testihuone 3");
        testiHuone3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.uusiHuone = 3;
                Main.huoneVaihdettava = true;
            }
        });
        
        huoneSubmenu = new JMenu("Huone");
        huoneSubmenu.add(huoneenVaihto);
        huoneSubmenu.add(new JSeparator());
        huoneSubmenu.add(testiHuoneOletus);
        huoneSubmenu.add(testiHuone1);
        huoneSubmenu.add(testiHuone2);
        huoneSubmenu.add(testiHuone3);
        
        debug = new JMenu("Debug");
        debug.add(näytäSijainti);
        debug.add(näytäFPS);
        debug.add(näytäReunat);
        debug.add(new JSeparator());
        debug.add(huoneSubmenu);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(peli);
        yläPalkki.add(tietoja);
        yläPalkki.add(debug);
        yläPalkki.add(kaksoispistedeeValikko);
        
        peliKenttäAlue = new JPanel();
        peliKenttäAlue.setLayout(null);
        peliKenttäAlue.setBounds(10, 10, 10, 10);
        peliKenttäAlue.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        peliKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        
        peliKenttä = new KenttäTakataustalla();
        peliKenttä.setLayout(null);
        peliKenttä.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        peliKenttä.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttä.add(peliKenttäAlue);
        peliKenttä.revalidate();
        peliKenttä.repaint();

        int kohteenSijX = 10;
        int kohteensijY = 10;
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                JLabel peliTestiLabel = new JLabel("PeliTestiLabel");
                peliTestiLabel.setBounds(kohteenSijX, kohteensijY, EsineenKokoPx, EsineenKokoPx);
                peliKenttä.add(peliTestiLabel);
                kohteenSijX += EsineenKokoPx;
            }
            kohteenSijX = 10;
            kohteensijY += EsineenKokoPx;
        }

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
        kontrolliInfoLabel[4] = new JLabel("Q: Pudota");
        kontrolliInfoLabel[5] = new JLabel("Z: Yhdistä");
        kontrolliInfoLabel[6] = new JLabel("X: Katso esinettä");
        kontrolliInfoLabel[7] = new JLabel("C: Katso kentän kohdetta");
        kontrolliInfoLabel[8] = new JLabel("F: Erikoiskäyttö");
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            //kontrolliInfoLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }
        
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

        ylätekstiAika = new JLabel("Aika: ");
        ylätekstiAika.setVisible(true);
        ylätekstiAika.setBounds(210, 5, 220, 20);

        ylätekstiSij = new JLabel("Paina nuolinäppäimiä liikkuaksesi");
        ylätekstiSij.setVisible(sijaintiNäkyvissä);
        ylätekstiSij.setBounds(10,20, 220, 20);

        ylätekstiKohde = new JLabel("Kohteen sisältö näkyy tässä");
        ylätekstiKohde.setVisible(sijaintiNäkyvissä);
        ylätekstiKohde.setBounds(10,35, 220, 20);

        ylätekstiHP = new JLabel("HP: " + 10);
        ylätekstiHP.setVisible(true);
        ylätekstiHP.setBounds(10,5, 220, 20);

        ylätekstiViive = new JLabel("Päivitysaika");
        ylätekstiViive.setVisible(fpsNäkyvissä);
        ylätekstiViive.setBounds(210,20, 220, 20);

        ylätekstiFPS = new JLabel("FPS");
        ylätekstiFPS.setVisible(fpsNäkyvissä);
        ylätekstiFPS.setBounds(210,35, 220, 20);

        tavoiteTeksti1 = new JLabel("Tavoite 1");
        tavoiteTeksti1.setVisible(true);
        tavoiteTeksti1.setBounds(430,5, 500, 20);

        tavoiteTeksti2 = new JLabel("Tavoite 2");
        tavoiteTeksti2.setVisible(true);
        tavoiteTeksti2.setBounds(430,20, 500, 20);

        tavoiteTeksti3 = new JLabel("Tavoite 3");
        tavoiteTeksti3.setVisible(true);
        tavoiteTeksti3.setBounds(430,35, 500, 20);

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
        yläPaneeli.add(tavoiteTeksti1);
        yläPaneeli.add(tavoiteTeksti2);
        yläPaneeli.add(tavoiteTeksti3);
        yläPaneeli.revalidate();
        yläPaneeli.repaint();

        ikkuna.add(yläPalkki);
        ikkuna.add(yläPaneeli);
        ikkuna.add(peliKenttä);
        ikkuna.add(hud);
        ikkuna.revalidate();
        ikkuna.repaint();

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
        vaatiiTäysPäivityksen = true;
    }

    static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        peliKenttä.removeAll();
        peliKenttäAlue.setBackground(new Color(0, 0, 0));
        
        int kohteenSijX = 10;
        int kohteensijY = 10;
        
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                kenttäKohteenKuvake[j][i] = new JLabel();
            }
        }
        pelaajaLabel = new JLabel(pelaajaKuvake);
        pelaajaLabel.setBounds(Main.pelaajanSijX * PelaajanKokoPx + 10, Main.pelaajanSijY * PelaajanKokoPx + 10, PelaajanKokoPx, PelaajanKokoPx);
        taustaLabel = new JLabel(new ImageIcon());
        taustaLabel.setBounds(0, 0, Main.kentänKoko * EsineenKokoPx + 20, Main.kentänKoko * EsineenKokoPx + 20);
        
        try {
            peliKenttä.add(pelaajaLabel);
            peliKenttä.add(taustaLabel);
            for (int i = 0; i < Main.kentänKoko; i++) {
                for (int j = 0; j < Main.kentänKoko; j++) {
                    if (j == sijX && i == sijY) {
                        if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(Main.pelikenttä[j][i].annaKuvake());
                        }
                    }
                    //if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                        if (reunatNäkyvissä) {
                            if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                                if (Main.pelaajanSijX == j && Main.pelaajanSijY == i) {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                                else if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                                    kenttäKohteenKuvake[j][i].setIcon(Main.pelikenttä[j][i].annaKuvake());
                                    if (Main.pelikenttä[j][i] instanceof Kiintopiste) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                    }
                                    else if (Main.pelikenttä[j][i] instanceof Esine) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                    }
                                    else if (Main.pelikenttä[j][i] instanceof NPC) {
                                        if (Main.pelikenttä[j][i] instanceof Vihollinen) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                        }
                                        else {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        }
                                        
                                    }
                                    else if (Main.pelikenttä[j][i] instanceof Warp) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                    }
                                }
                            }
                            else if (Main.pelikenttä[j][i] == null) {
                                kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                            }
                        }
                        else {
                            kenttäKohteenKuvake[j][i].setBorder(null);
                        }
                        if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(Main.pelikenttä[j][i].annaKuvake());
                        }
                        
                    //}
                    //else {
                    //    kenttäKohteenKuvake[j][i].setIcon(null);
                    //    kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                    //}
                    kenttäKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, EsineenKokoPx, EsineenKokoPx);
                    peliKenttä.add(kenttäKohteenKuvake[j][i]);
                    peliKenttä.setComponentZOrder(kenttäKohteenKuvake[j][i], 1);
                    kohteenSijX += EsineenKokoPx;
                }
                kohteenSijX = 10;
                kohteensijY += EsineenKokoPx;
            }
            
            peliKenttä.setComponentZOrder(pelaajaLabel, 0);
            //peliKenttä.setComponentZOrder(taustaLabel, 2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e) {

        }

        peliKenttä.revalidate();
        peliKenttä.repaint();
    }

    //static void päivitäIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {
    static JPanel päivitäIkkuna() {
        
        //peliKenttä.removeAll();
        //int kohteenSijX = 10;
        //int kohteenSijY = 10;
        
        if (vaatiiPäivityksen) {
            try {
                for (int i = 0; i < Main.kentänKoko; i++) {
                    for (int j = 0; j < Main.kentänKoko; j++) {
                        if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(Main.pelikenttä[j][i].annaKuvake());
                        }
                        else {
                            kenttäKohteenKuvake[j][i].setIcon(null);
                        }

                        if (vaatiiTäysPäivityksen) {
                            if (reunatNäkyvissä) {
                                if (Main.pelaajanSijX == j && Main.pelaajanSijY == i) {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                                else if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                                    kenttäKohteenKuvake[j][i].setIcon(Main.pelikenttä[j][i].annaKuvake());
                                    if (Main.pelikenttä[j][i] instanceof Kiintopiste) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                    }
                                    else if (Main.pelikenttä[j][i] instanceof Esine) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                    }
                                    else if (Main.pelikenttä[j][i] instanceof NPC) {
                                        if (Main.pelikenttä[j][i] instanceof Vihollinen) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                        }
                                        else {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        }
                                        
                                    }
                                    else if (Main.pelikenttä[j][i] instanceof Warp) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                    }
                                }
                                else if (Main.pelikenttä[j][i] == null) {
                                    kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                                }
                            }
                            else {
                                kenttäKohteenKuvake[j][i].setBorder(null);
                            }
                        }
                    }
                }

                if (pelaajaSiirtyi || vaatiiTäysPäivityksen) {
                    pelaajaLabel.setBounds(Main.pelaajanSijX * PelaajanKokoPx + 10, Main.pelaajanSijY * PelaajanKokoPx + 10, PelaajanKokoPx, PelaajanKokoPx);;
                    pelaajaSiirtyi = false;
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Jokin meni pieleen.", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                GrafiikanPäivitysSäie.ongelmaGrafiikassa = true;
                uusiIkkuna = true;
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
            }

            peliKenttä.revalidate();
            peliKenttä.repaint();
            vaatiiPäivityksen = false;
            vaatiiTäysPäivityksen = false;
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
