import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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
    static JMenuItem uusiPeli, mukauta, asetukset, ohjeet, tekijät, näytäFPS;
    static JMenuItem kaksoispistedeeValikkoItemi, kaksoispistedeeValikkoItemi2, kaksoispistedeeValikkoItemi3, kaksoispistedeeValikkoSubmenu2, kaksoispistedeeValikkoSubmenu3;
    static JPanel peliKenttä, hud, yläPaneeli;
    static JLabel hudTeksti, aikaTeksti, yläteksti1, yläteksti2, yläteksti3, yläteksti4, yläteksti5, tavoiteTeksti1, tavoiteTeksti2, tavoiteTeksti3;
    static JPanel tekstiPaneli, infoPaneli, invaPaneli, tavaraPaneli, osoitinPaneli;
    static JLabel[] esineLabel = new JLabel[5];
    static JLabel[] osoitinLabel = new JLabel[5];
    static JLabel[] kontrolliInfoLabel = new JLabel[9];
    static JLabel peliTestiLabel, pelaajaLabel;
    static boolean vaatiiPäivityksen = false;
    static boolean fpsNäkyvissä = false;

    static void luoPääikkuna() {
        
        ikkunanLeveys = EsineenKokoPx * Main.kentänKoko + 50;
        ikkunanKorkeus = EsineenKokoPx * Main.kentänKoko + 300;
        
        ikkuna = new JFrame("Keimon Seikkailupeli v0.2.2 alpha (7.12.2022)");
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

        näytäFPS = new JMenuItem("Näytä FPS");
        näytäFPS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFPS();
            }
        });
        
        debug = new JMenu("Debug");
        debug.add(näytäFPS);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(peli);
        yläPalkki.add(tietoja);
        yläPalkki.add(debug);
        yläPalkki.add(kaksoispistedeeValikko);
        
        peliKenttä = new JPanel();
        peliKenttä.setLayout(null);
        peliKenttä.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        peliKenttä.setBorder(BorderFactory.createLineBorder(Color.black, 1));
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

        aikaTeksti = new JLabel("Aika: ");
        aikaTeksti.setVisible(true);
        aikaTeksti.setBounds(210, 5, 200, 20);

        yläteksti1 = new JLabel("Paina nuolinäppäimiä liikkuaksesi");
        yläteksti1.setVisible(true);
        yläteksti1.setBounds(10,5, 500, 20);

        yläteksti2 = new JLabel("Kohteen sisältö näkyy tässä");
        yläteksti2.setVisible(true);
        yläteksti2.setBounds(10,20, 500, 20);

        yläteksti3 = new JLabel("HP: " + 10);
        yläteksti3.setVisible(true);
        yläteksti3.setBounds(10,35, 200, 20);

        yläteksti4 = new JLabel("Päivitysaika");
        yläteksti4.setVisible(false);
        yläteksti4.setBounds(210,20, 200, 20);

        yläteksti5 = new JLabel("FPS");
        yläteksti5.setVisible(false);
        yläteksti5.setBounds(210,35, 200, 20);

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
        yläPaneeli.add(aikaTeksti);
        yläPaneeli.add(yläteksti1);
        yläPaneeli.add(yläteksti2);
        yläPaneeli.add(yläteksti3);
        yläPaneeli.add(yläteksti4);
        yläPaneeli.add(yläteksti5);
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

    static void showFPS() {
        if (!fpsNäkyvissä) {
            fpsNäkyvissä = true;
            yläteksti4.setVisible(true);
            yläteksti5.setVisible(true);
        }
        else {
            fpsNäkyvissä = false;
            yläteksti4.setVisible(false);
            yläteksti5.setVisible(false);
        }
    }

    static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        peliKenttä.removeAll();
        int kohteenSijX = 10;
        int kohteensijY = 10;
        
        try {
            for (int i = 0; i < Main.kentänKoko; i++) {
                for (int j = 0; j < Main.kentänKoko; j++) {
                    if (j == sijX && i == sijY) {
                        if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                            peliTestiLabel = new JLabel(Main.pelikenttä[j][i].annaKuvake());
                        }
                        else {
                            peliTestiLabel = new JLabel("");
                        }
                        pelaajaLabel = new JLabel(pelaajaKuvake);
                    }
                    else if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                        peliTestiLabel = new JLabel(Main.pelikenttä[j][i].annaKuvake());
                        if (Main.pelikenttä[j][i] instanceof Kiintopiste) {
                            peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                        }
                        else if (Main.pelikenttä[j][i] instanceof Esine) {
                            peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                        }
                        else if (Main.pelikenttä[j][i] instanceof NPC) {
                            if (Main.pelikenttä[j][i] instanceof Vihollinen) {
                                peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                            }
                            else {
                                peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                            }
                            
                        }
                        pelaajaLabel = new JLabel("");
                    }
                    else {
                        peliTestiLabel = new JLabel("");
                        peliTestiLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                        pelaajaLabel = new JLabel("");
                    }
                    peliTestiLabel.setBounds(kohteenSijX, kohteensijY, EsineenKokoPx, EsineenKokoPx);
                    pelaajaLabel.setBounds(kohteenSijX, kohteensijY, PelaajanKokoPx, PelaajanKokoPx);
                    peliKenttä.add(peliTestiLabel);
                    peliKenttä.add(pelaajaLabel);
                    peliKenttä.setComponentZOrder(pelaajaLabel, 0);
                    peliKenttä.setComponentZOrder(peliTestiLabel, 1);
                    kohteenSijX += EsineenKokoPx;
                }
                kohteenSijX = 10;
                kohteensijY += EsineenKokoPx;
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e) {

        }

        peliKenttä.revalidate();
        peliKenttä.repaint();
    }

    static void päivitäIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {
        
        peliKenttä.removeAll();
        int kohteenSijX = 10;
        int kohteensijY = 10;
        
        if (vaatiiPäivityksen) {
            try {
                for (int i = 0; i < Main.kentänKoko; i++) {
                    for (int j = 0; j < Main.kentänKoko; j++) {
                        if (j == sijX && i == sijY) {
                            if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                                peliTestiLabel = new JLabel(Main.pelikenttä[j][i].annaKuvake());
                            }
                            else {
                                peliTestiLabel = new JLabel("");
                            }
                            pelaajaLabel = new JLabel(pelaajaKuvake);
                        }
                        else if (Main.pelikenttä[j][i] instanceof KenttäKohde) {
                            peliTestiLabel = new JLabel(Main.pelikenttä[j][i].annaKuvake());
                            if (Main.pelikenttä[j][i] instanceof Kiintopiste) {
                                peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                            }
                            else if (Main.pelikenttä[j][i] instanceof Esine) {
                                peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                            }
                            else if (Main.pelikenttä[j][i] instanceof NPC) {
                                if (Main.pelikenttä[j][i] instanceof Vihollinen) {
                                    peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                }
                                else {
                                    peliTestiLabel.setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                }
                                
                            }
                            pelaajaLabel = new JLabel("");
                        }
                        else {
                            peliTestiLabel = new JLabel("");
                            peliTestiLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                            pelaajaLabel = new JLabel("");
                        }
                        peliTestiLabel.setBounds(kohteenSijX, kohteensijY, EsineenKokoPx, EsineenKokoPx);
                        pelaajaLabel.setBounds(kohteenSijX, kohteensijY, PelaajanKokoPx, PelaajanKokoPx);
                        peliKenttä.add(peliTestiLabel);
                        peliKenttä.add(pelaajaLabel);
                        peliKenttä.setComponentZOrder(pelaajaLabel, 0);
                        peliKenttä.setComponentZOrder(peliTestiLabel, 1);
                        kohteenSijX += EsineenKokoPx;
                        
                    }
                    kohteenSijX = 10;
                    kohteensijY += EsineenKokoPx;
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
            }
            catch (IllegalArgumentException e) {
                //System.out.println("Ongelma ruudunpäivityksessä");
            }

            peliKenttä.revalidate();
            peliKenttä.repaint();
            vaatiiPäivityksen = false;
        }
    }
}
