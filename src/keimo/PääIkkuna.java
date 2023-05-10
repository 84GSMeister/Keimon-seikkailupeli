package keimo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.HuoneEditori.*;
import keimo.Ikkunat.*;
import keimo.Väliruudut.*;

public class PääIkkuna {
    public static final int esineenKokoPx = 64;
    public static final int pelaajanKokoPx = 64;
    static int ikkunanLeveys = esineenKokoPx * Peli.kentänKoko;
    static int ikkunanKorkeus = esineenKokoPx * Peli.kentänKoko;
    protected static boolean fullscreen = false;
    public static boolean uusiIkkuna = false;
    public static JFrame ikkuna;
    static JMenuBar yläPalkki;
    static JMenu peli, tietoja, debug, työkalut;
    static JMenu huoneSubmenu;
    static JMenuItem huoneenVaihto, maastoGeneraattori, huoneEditori;
    static JMenuItem uusiPeli, mukauta, asetukset, ohjeet, tekijät;
    static JCheckBoxMenuItem näytäSijainti, näytäFPS, näytäReunat, näytäTapahtumapalkki;
    static JMenuItem menuF2, menuF3, menuF4;
    public static JPanel peliKenttäUlompi, peliKenttäAlue, alueInfoPaneli;
    public static JPanel vasenYläPaneeli, vasenKeskiPaneeli, vasenAlaPaneeli, oikeaYläPaneeli, oikeaKeskiPaneeli, oikeaAlaPaneeli;
    public static JLayeredPane peliKenttä;
    public static JPanel vuoropuhePaneli, vuoropuhePaneliOikea, taustaPaneli, pausePaneli;
    public static JLabel vuoropuheKuvake, vuoropuheTeksti, vuoropuheNimi, pauseLabel;
    public static JLabel kokoruudunTakatausta;
    public static JPanel hud, yläPaneeli, alaPaneeli;
    public static JLabel hudTeksti;
    public static JLabel ylätekstiAika, ylätekstiSij, ylätekstiSijRuutu, ylätekstiKohde, ylätekstiHP, ylätekstiKuparit, ylätekstiViive, ylätekstiFPS;
    static JLabel tavoiteTeksti1;
    static JLabel tavoiteTeksti2;
    static JLabel tavoiteTeksti3;
    public static JPanel tekstiPaneli, kontrolliInfoPaneli, tavoiteInfoPaneli, aikaInfoPaneli, debugInfoPaneli, invaPaneli, tavaraPaneli, osoitinPaneli, valitunEsineenNimiPaneli, statsiPaneeli;
    public static JLabel[] esineLabel = new JLabel[Pelaaja.esineet.length];
    public static JLabel osoitinLabel, valitunEsineenNimiLabel;
    public static JLabel[] kontrolliInfoLabel = new JLabel[9];
    public static JLabel tavoiteOtsikkoLabel, tavoiteInfoLabel;
    public static JLabel peliTestiLabel, pelaajaLabel, pelaajanEsineLabel, taustaLabel, alueInfoLabel;
    public static JLabel tölksKuvakeLabel, tölksMääräLabel, hpKuvakeLabel, hpMääräLabel, rahaKuvakeLabel, rahaMääräLabel;
    static JLabel[][] kenttäKohteenKuvake;
    static JLabel[][] maastoKohteenKuvake;
    static JLabel[] npcKuvake;
    public static boolean vaatiiPäivityksen = false;
    //static boolean vaatiiKentänPäivityksen = false;
    public static boolean uudelleenpiirräKaikki = false;
    public static boolean uudelleenpiirräKenttä = true;
    static boolean pelaajaSiirtyi = false;
    static boolean fpsNäkyvissä = false;
    static boolean sijaintiNäkyvissä = false;
    static boolean reunatNäkyvissä = false;
    static boolean tapahtumapalkkiNäkyvissä = false;
    public static JPanel peliAluePaneli, kortit;
    public static CardLayout crd;
    static DecimalFormat df = new DecimalFormat("##.##");
    static GraphicsDevice näytöt = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

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
        
        ikkuna = new JFrame("Keimon Seikkailupeli v.0.7.1 pre-alpha (10.5.2023)");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.setBounds(0, 0, 1366, 768);
        ikkuna.setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        näytäTapahtumapalkki = new JCheckBoxMenuItem("Näytä tapahtumapalkin teksti");
        näytäTapahtumapalkki.setSelected(tapahtumapalkkiNäkyvissä);
        näytäTapahtumapalkki.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                näytäTapahtumapalkki();
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
                PääIkkuna.uudelleenpiirräKenttä = true;
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
        debug.add(näytäTapahtumapalkki);
        debug.add(new JSeparator());
        debug.add(menuF2);
        debug.add(menuF3);
        debug.add(menuF4);
        debug.add(new JSeparator());
        debug.add(huoneSubmenu);

        maastoGeneraattori = new JMenuItem("Maastogeneraattori", new ImageIcon("tiedostot/kuvat/menu/gui/maastogeneraattori.png"));
        maastoGeneraattori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!MaastoGeneraattoriIkkuna.ikkunaAuki()) {
                    MaastoGeneraattoriIkkuna.luoMaastoGeneraattoriIkkuna();
                }
                else {
                    MaastoGeneraattoriIkkuna.asetaPäällimmäiseksi();
                }
            }
        });

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
        alueInfoLabel.setPreferredSize(new Dimension(ikkunanLeveys, 15));
        alueInfoLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        alueInfoLabel.setHorizontalAlignment(JLabel.CENTER);

        alueInfoPaneli = new JPanel();
        alueInfoPaneli.setLayout(new BorderLayout());
        alueInfoPaneli.setPreferredSize(new Dimension(ikkunanLeveys, 15));
        alueInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        alueInfoPaneli.add(alueInfoLabel, BorderLayout.CENTER);

        taustaLabel = new JLabel(new ImageIcon());
        taustaLabel.setBounds(0, 0, Peli.kentänKoko * esineenKokoPx + 20, Peli.kentänKoko * esineenKokoPx + 20);

        taustaPaneli = new JPanel();
        taustaPaneli.setName("Tausta_paneli");
        taustaPaneli.setBounds(0, 0, Peli.kentänKoko * esineenKokoPx + 20, Peli.kentänKoko * esineenKokoPx + 20);
        taustaPaneli.add(taustaLabel);

        vuoropuheKuvake = new JLabel("", SwingConstants.CENTER);
        vuoropuheKuvake.setPreferredSize(new Dimension(140, 110));
        vuoropuheKuvake.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        vuoropuheTeksti = new JLabel("teksti tähän", SwingConstants.LEFT);
        vuoropuheTeksti.setVerticalAlignment(SwingConstants.TOP);
        vuoropuheTeksti.setPreferredSize(new Dimension(200, 80));
        //vuoropuheTeksti.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        vuoropuheNimi = new JLabel("nimi tähän", SwingConstants.LEFT);
        vuoropuheNimi.setPreferredSize(new Dimension(200, 30));
        vuoropuheNimi.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        vuoropuhePaneliOikea = new JPanel(new BorderLayout());
        vuoropuhePaneliOikea.setPreferredSize(new Dimension(400, 110));
        vuoropuhePaneliOikea.add(vuoropuheNimi, BorderLayout.NORTH);
        vuoropuhePaneliOikea.add(vuoropuheTeksti, BorderLayout.SOUTH);

        vuoropuhePaneli = new JPanel(new BorderLayout());
        vuoropuhePaneli.setName("Vuoropuhepaneli");
        vuoropuhePaneli.setBounds(0, 540, 660, 120);
        vuoropuhePaneli.add(vuoropuheKuvake, BorderLayout.WEST);
        vuoropuhePaneli.add(vuoropuhePaneliOikea, BorderLayout.CENTER);
        vuoropuhePaneli.setVisible(false);

        pauseLabel = new JLabel("Pause", SwingConstants.CENTER);
        pauseLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
        pauseLabel.setPreferredSize(new Dimension(200, 110));
        pauseLabel.setAlignmentX(SwingConstants.CENTER);
        pauseLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        pausePaneli = new JPanel(new GridBagLayout());
        pausePaneli.setName("Pausepaneli");
        pausePaneli.setBounds(138, 138, 384, 384);
        pausePaneli.add(pauseLabel);
        pausePaneli.setVisible(false);
        
        peliKenttä = new JLayeredPane();
        peliKenttä.setLayout(null);
        peliKenttä.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        peliKenttä.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttä.setComponentZOrder(vuoropuhePaneli, 0);
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


        ylätekstiHP = new JLabel("HP: " + 10);
        ylätekstiHP.setVisible(true);
        ylätekstiHP.setBounds(10,10, 180, 20);

        ylätekstiAika = new JLabel("Aika: ");
        ylätekstiAika.setVisible(true);
        ylätekstiAika.setBounds(10, 40, 180, 20);

        ylätekstiKuparit = new JLabel("Tölkkejä: " + 0);
        ylätekstiKuparit.setVisible(true);
        ylätekstiKuparit.setBounds(10,70, 180, 20);

        ylätekstiSij = new JLabel("Paina nuolinäppäimiä liikkuaksesi");
        ylätekstiSij.setVisible(sijaintiNäkyvissä);
        ylätekstiSij.setBounds(10,10, 180, 20);

        ylätekstiSijRuutu = new JLabel("Paina nuolinäppäimiä liikkuaksesi");
        ylätekstiSijRuutu.setVisible(sijaintiNäkyvissä);
        ylätekstiSijRuutu.setBounds(10,40, 180, 20);

        ylätekstiKohde = new JLabel("Kohteen sisältö näkyy tässä");
        ylätekstiKohde.setVisible(sijaintiNäkyvissä);
        ylätekstiKohde.setBounds(10,70, 400, 20);

        ylätekstiViive = new JLabel("Päivitysaika");
        ylätekstiViive.setVisible(fpsNäkyvissä);
        ylätekstiViive.setBounds(10,110, 220, 20);

        ylätekstiFPS = new JLabel("FPS");
        ylätekstiFPS.setVisible(fpsNäkyvissä);
        ylätekstiFPS.setBounds(10,140, 220, 20);

        debugInfoPaneli = new JPanel();
        debugInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        debugInfoPaneli.setBounds(10, 10, 180, 200);
        debugInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        debugInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        debugInfoPaneli.add(ylätekstiSij);
        debugInfoPaneli.add(ylätekstiSijRuutu);
        debugInfoPaneli.add(ylätekstiKohde);
        debugInfoPaneli.add(ylätekstiViive);
        debugInfoPaneli.add(ylätekstiFPS);
        debugInfoPaneli.revalidate();
        debugInfoPaneli.repaint();

        aikaInfoPaneli = new JPanel();
        aikaInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        aikaInfoPaneli.setBounds(10, 10, 180, 200);
        aikaInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        aikaInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        aikaInfoPaneli.add(ylätekstiHP);
        aikaInfoPaneli.add(ylätekstiAika);
        aikaInfoPaneli.add(ylätekstiKuparit);
        aikaInfoPaneli.revalidate();
        aikaInfoPaneli.repaint();

        kontrolliInfoLabel[0] = new JLabel("Nuolet / WASD: Liiku");
        kontrolliInfoLabel[1] = new JLabel("Space: Käytä esinettä");
        kontrolliInfoLabel[2] = new JLabel("1-6: Vaihda tavarapaikkaa");
        kontrolliInfoLabel[3] = new JLabel("E: Poimi / Vuorovaikutus");
        kontrolliInfoLabel[4] = new JLabel("F: Vuorovaikutus");
        kontrolliInfoLabel[5] = new JLabel("Q: Pudota");
        kontrolliInfoLabel[6] = new JLabel("Z: Yhdistä");
        kontrolliInfoLabel[7] = new JLabel("X: Katso esinettä");
        kontrolliInfoLabel[8] = new JLabel("C: Katso kentän kohdetta");
        
        kontrolliInfoPaneli = new JPanel();
        kontrolliInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        kontrolliInfoPaneli.setBounds(10, 10, 180, 200);
        kontrolliInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        kontrolliInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            kontrolliInfoPaneli.add(kontrolliInfoLabel[i]);
        }
        kontrolliInfoPaneli.revalidate();
        kontrolliInfoPaneli.repaint();

        tavoiteOtsikkoLabel = new JLabel("Seuraava tavoite:");
        tavoiteInfoLabel = new JLabel("");

        tavoiteInfoPaneli = new JPanel();
        tavoiteInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        tavoiteInfoPaneli.setBounds(10, 10, 180, 200);
        tavoiteInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        tavoiteInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        tavoiteInfoPaneli.add(tavoiteOtsikkoLabel);
        tavoiteInfoPaneli.add(tavoiteInfoLabel);
        tavoiteInfoPaneli.revalidate();
        tavoiteInfoPaneli.repaint();


        hpKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/hp_eitekstiä.png"));
        //hpKuvakeLabel.setPreferredSize(new Dimension(64, 64));
        hpKuvakeLabel.setBounds(20, 0, 100, 55);
        //hpKuvakeLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));   

        hpMääräLabel = new JLabel("10", SwingConstants.CENTER);
        //hpMääräLabel.setPreferredSize(new Dimension(64, 64));
        hpMääräLabel.setBounds(20, 0, 100, 55);
        hpMääräLabel.setFont(new Font("MS Gothic", Font.BOLD, 20));
        //hpMääräLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        JPanel hpMääräPaneli = new JPanel(null);
        hpMääräPaneli.setPreferredSize(new Dimension(100, 55));
        hpMääräPaneli.add(hpMääräLabel);
        hpMääräPaneli.add(hpKuvakeLabel);
        hpMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        rahaKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/rahet.png"));
        //rahaKuvakeLabel.setPreferredSize(new Dimension(64, 64));
        rahaKuvakeLabel.setBounds(20, 0, 100, 55);
        //rahaKuvakeLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        rahaMääräLabel = new JLabel("0.00€", SwingConstants.CENTER);
        //rahaMääräLabel.setPreferredSize(new Dimension(64, 64));
        rahaMääräLabel.setBounds(20, 0, 100, 55);
        rahaMääräLabel.setFont(new Font("MS Gothic", Font.BOLD, 20));
        //rahaMääräLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        JPanel rahaMääräPaneli = new JPanel(null);
        rahaMääräPaneli.setPreferredSize(new Dimension(100, 55));
        rahaMääräPaneli.add(rahaMääräLabel);
        rahaMääräPaneli.add(rahaKuvakeLabel);
        rahaMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        tölksKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/tölks.png"));
        //tölksKuvakeLabel.setPreferredSize(new Dimension(64, 64));
        tölksKuvakeLabel.setBounds(20, 0, 100, 55);
        //tölksKuvakeLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        tölksMääräLabel = new JLabel("0", SwingConstants.CENTER);
        //tölksMääräLabel.setPreferredSize(new Dimension(64, 64));
        tölksMääräLabel.setBounds(20, 0, 100, 55);
        tölksMääräLabel.setFont(new Font("MS Gothic", Font.BOLD, 20));
        //tölksMääräLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        JPanel tölksMääräPaneli = new JPanel(null);
        tölksMääräPaneli.setPreferredSize(new Dimension(100, 55));
        tölksMääräPaneli.add(tölksMääräLabel);
        tölksMääräPaneli.add(tölksKuvakeLabel);
        tölksMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        JLabel[] plaseholderLabels = new JLabel[1];

        statsiPaneeli = new JPanel(new GridLayout(4, 1));
        statsiPaneeli.setBounds(0, 0, 200, 220);
        //statsiPaneeli.setBackground(new Color(0, 0, 0, 0));
        statsiPaneeli.add(hpMääräPaneli);
        //statsiPaneeli.add(hpKuvakeLabel);
        //statsiPaneeli.add(hpMääräLabel);
        statsiPaneeli.add(rahaMääräPaneli);
        //statsiPaneeli.add(rahaKuvakeLabel);
        //statsiPaneeli.add(rahaMääräLabel);
        statsiPaneeli.add(tölksMääräPaneli);
        //statsiPaneeli.add(tölksKuvakeLabel);
        //statsiPaneeli.add(tölksMääräLabel);
        for (JLabel jLabel : plaseholderLabels) {
            jLabel = new JLabel("");
            jLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
            statsiPaneeli.add(jLabel);
        }


        osoitinLabel = new JLabel("Tavaraluettelo");

        osoitinPaneli = new JPanel();
        osoitinPaneli.setLayout(new GridBagLayout());
        osoitinPaneli.setPreferredSize(new Dimension(192, 20));
        osoitinPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        osoitinPaneli.add(osoitinLabel);
        osoitinPaneli.revalidate();
        osoitinPaneli.repaint();

        for (int i = 0; i < esineLabel.length; i++) {
            esineLabel[i] = new JLabel("Esine 0");
            esineLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        }
        esineLabel[0].setBorder(BorderFactory.createLineBorder(Color.red, 3, true));

        tavaraPaneli = new JPanel();
        tavaraPaneli.setLayout(new GridLayout(2, 3));
        tavaraPaneli.setPreferredSize(new Dimension(192, 128));
        tavaraPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < esineLabel.length; i++) {
            tavaraPaneli.add(esineLabel[i]);
        }
        tavaraPaneli.revalidate();
        tavaraPaneli.repaint();

        valitunEsineenNimiLabel = new JLabel("Valittu esine");

        valitunEsineenNimiPaneli = new JPanel();
        valitunEsineenNimiPaneli.setLayout(new GridBagLayout());
        valitunEsineenNimiPaneli.setPreferredSize(new Dimension(192, 30));
        valitunEsineenNimiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        valitunEsineenNimiPaneli.add(valitunEsineenNimiLabel);
        valitunEsineenNimiPaneli.revalidate();
        valitunEsineenNimiPaneli.repaint();

        invaPaneli = new JPanel();
        invaPaneli.setLayout(new BorderLayout());
        invaPaneli.setPreferredSize(new Dimension(192, 178));
        invaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        invaPaneli.add(osoitinPaneli, BorderLayout.NORTH);
        invaPaneli.add(tavaraPaneli, BorderLayout.CENTER);
        invaPaneli.add(valitunEsineenNimiPaneli, BorderLayout.SOUTH);
        invaPaneli.revalidate();
        invaPaneli.repaint();
        
        hud = new JPanel();
        hud.setLayout(new BorderLayout());
        hud.setBounds(0, 20, 200, 178);
        hud.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        hud.add(invaPaneli, BorderLayout.SOUTH);
        hud.revalidate();
        hud.repaint();


        yläPaneeli = new JPanel();
        yläPaneeli.setLayout(new BorderLayout());
        yläPaneeli.setPreferredSize(new Dimension(1060, 15));
        yläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        yläPaneeli.add(alueInfoLabel, BorderLayout.CENTER);
        yläPaneeli.revalidate();
        yläPaneeli.repaint();

        hudTeksti = new JLabel("Kokeile liikkua ja poimia esineitä!");
        hudTeksti.setVisible(tapahtumapalkkiNäkyvissä);
        alaPaneeli = new JPanel();
        alaPaneeli.setLayout(new BorderLayout());
        alaPaneeli.setPreferredSize(new Dimension(1060, 15));
        alaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        alaPaneeli.add(hudTeksti, BorderLayout.CENTER);
        alaPaneeli.revalidate();
        alaPaneeli.repaint();

        JLabel vasenYläPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        vasenYläPaneelinTausta.setBounds(0, 0, 200, 220);
        vasenYläPaneeli = new JPanel();
        vasenYläPaneeli.setLayout(null);
        vasenYläPaneeli.setPreferredSize(new Dimension(200, 220));
        vasenYläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        vasenYläPaneeli.add(aikaInfoPaneli);
        vasenYläPaneeli.add(vasenYläPaneelinTausta);
        //vasenYläPaneeli.setComponentZOrder(vasenYläPaneelinTausta, 1);
        //vasenYläPaneeli.setComponentZOrder(aikaInfoPaneli, 0);
        vasenYläPaneeli.revalidate();
        vasenYläPaneeli.repaint();

        JLabel vasenKeskiPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        vasenKeskiPaneelinTausta.setBounds(0, 0, 200, 220);
        vasenKeskiPaneeli = new JPanel();
        vasenKeskiPaneeli.setLayout(null);
        vasenKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
        vasenKeskiPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        vasenKeskiPaneeli.add(statsiPaneeli);
        vasenKeskiPaneeli.add(vasenKeskiPaneelinTausta);
        vasenKeskiPaneeli.setComponentZOrder(vasenKeskiPaneelinTausta, 1);
        vasenKeskiPaneeli.setComponentZOrder(statsiPaneeli, 0);
        vasenKeskiPaneeli.revalidate();
        vasenKeskiPaneeli.repaint();

        JLabel vasenAlaPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        vasenAlaPaneelinTausta.setBounds(0, 0, 200, 220);
        vasenAlaPaneeli = new JPanel();
        vasenAlaPaneeli.setLayout(null);
        vasenAlaPaneeli.setPreferredSize(new Dimension(200, 220));
        vasenAlaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        vasenAlaPaneeli.add(hud);
        vasenAlaPaneeli.add(vasenAlaPaneelinTausta);
        vasenAlaPaneeli.setComponentZOrder(vasenKeskiPaneelinTausta, 1);
        vasenAlaPaneeli.setComponentZOrder(hud, 0);
        vasenAlaPaneeli.revalidate();
        vasenAlaPaneeli.repaint();

        JLabel oikeaYläPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        oikeaYläPaneelinTausta.setBounds(0, 0, 200, 220);
        oikeaYläPaneeli = new JPanel();
        oikeaYläPaneeli.setLayout(null);
        oikeaYläPaneeli.setPreferredSize(new Dimension(200, 220));
        oikeaYläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        oikeaYläPaneeli.add(debugInfoPaneli);
        oikeaYläPaneeli.add(oikeaYläPaneelinTausta);
        oikeaYläPaneeli.setComponentZOrder(oikeaYläPaneelinTausta, 1);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiSij, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiSijRuutu, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiKohde, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiViive, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiFPS, 0);
        oikeaYläPaneeli.revalidate();
        oikeaYläPaneeli.repaint();

        JLabel oikeaKeskiPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        oikeaKeskiPaneelinTausta.setBounds(0, 0, 200, 220);
        oikeaKeskiPaneeli = new JPanel();
        oikeaKeskiPaneeli.setLayout(null);
        oikeaKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
        oikeaKeskiPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        oikeaKeskiPaneeli.add(kontrolliInfoPaneli);
        oikeaKeskiPaneeli.add(oikeaKeskiPaneelinTausta);
        oikeaKeskiPaneeli.setComponentZOrder(oikeaKeskiPaneelinTausta, 1);
        oikeaKeskiPaneeli.setComponentZOrder(kontrolliInfoPaneli, 0);
        oikeaKeskiPaneeli.revalidate();
        oikeaKeskiPaneeli.repaint();

        JLabel oikeaAlaPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        oikeaAlaPaneelinTausta.setBounds(0, 0, 200, 220);
        oikeaAlaPaneeli = new JPanel();
        oikeaAlaPaneeli.setLayout(null);
        oikeaAlaPaneeli.setPreferredSize(new Dimension(200, 220));
        oikeaAlaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        oikeaAlaPaneeli.add(tavoiteInfoPaneli);
        oikeaAlaPaneeli.add(oikeaAlaPaneelinTausta);
        oikeaAlaPaneeli.setComponentZOrder(oikeaAlaPaneelinTausta, 1);
        oikeaAlaPaneeli.setComponentZOrder(tavoiteInfoPaneli, 0);
        oikeaAlaPaneeli.revalidate();
        oikeaAlaPaneeli.repaint();

        GridBagLayout pelikentänLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        peliKenttäUlompi.setLayout(pelikentänLayout);
        peliKenttäUlompi.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        peliKenttäUlompi.add(yläPaneeli, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        peliKenttäUlompi.add(vasenYläPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        peliKenttäUlompi.add(vasenKeskiPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        peliKenttäUlompi.add(vasenAlaPaneeli, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 3;
        peliKenttäUlompi.add(peliKenttä, gbc);
        gbc.gridheight = 1;
        gbc.gridx = 2;
        gbc.gridy = 1;
        peliKenttäUlompi.add(oikeaYläPaneeli, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        peliKenttäUlompi.add(oikeaKeskiPaneeli, gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        peliKenttäUlompi.add(oikeaAlaPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        peliKenttäUlompi.add(alaPaneeli, gbc);

        peliKenttäUlompi.setComponentZOrder(peliKenttä, 0);
        peliKenttäUlompi.revalidate();
        peliKenttäUlompi.repaint();

        //hudTeksti = new JLabel("Kokeile liikkua ja poimia esineitä!");
        
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new BorderLayout());
        tekstiPaneli.setPreferredSize(new Dimension(ikkunanLeveys, 15));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        //tekstiPaneli.add(hudTeksti, BorderLayout.CENTER);
        tekstiPaneli.revalidate();
        tekstiPaneli.repaint();

        peliKenttäAlue = new JPanel();
        peliKenttäAlue.setLayout(new BorderLayout());
        peliKenttäAlue.setPreferredSize(new Dimension(ikkunanLeveys-10, ikkunanLeveys -10));
        peliKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttäAlue.setBackground(new Color(0, 0, 0, 255));
        //peliKenttäAlue.add(alueInfoPaneli, BorderLayout.NORTH);
        peliKenttäAlue.add(peliKenttäUlompi, BorderLayout.CENTER);
        //peliKenttäAlue.add(tekstiPaneli, BorderLayout.SOUTH);
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

        // hudTeksti = new JLabel("Kokeile liikkua ja poimia esineitä!");
        
        // tekstiPaneli = new JPanel();
        // tekstiPaneli.setLayout(new BorderLayout());
        // tekstiPaneli.setPreferredSize(new Dimension(ikkunanLeveys -30, 20));
        // tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        // tekstiPaneli.add(hudTeksti, BorderLayout.CENTER);
        // tekstiPaneli.revalidate();
        // tekstiPaneli.repaint();

        peliAluePaneli = new JPanel();
        peliAluePaneli.setLayout(new BorderLayout());
        peliAluePaneli.setBackground(new Color(0, 0, 0, 255));
        peliAluePaneli.add(peliKenttäAlue, BorderLayout.CENTER);

        crd = new CardLayout();
        kortit = new JPanel(crd);
        kortit.add(TarinaRuutu.luoTarinaPaneli("alku"));
        kortit.add(ValikkoRuutu.alkuValikkoPaneli);
        kortit.add(OsionAlkuRuutu.kokeileLuodaOsionAlkuPaneli());
        kortit.add(peliAluePaneli);
        kortit.add(LoppuRuutu.luoLoppuRuutu());
        

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        ikkuna.add(kortit, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

    }

    private static int tekstiäJäljellä;
    private static boolean tekstiAuki = false;
    private static Timer tekstiAjastin;

    private static String dialogiPaneelinTeksti;
    private static boolean hudTekstinPäivittäjäKäynnissä = false;
    private static Thread hudTekstinPäivittäjä = new Thread() {
        public void run() {
            while (tekstiäJäljellä > 0) {
                if (tekstiAuki) {
                    String tulostettavaTeksti = dialogiPaneelinTeksti.substring(0, dialogiPaneelinTeksti.length()-tekstiäJäljellä +1);
                    vuoropuheTeksti.setText(tulostettavaTeksti);
                    System.out.println(tulostettavaTeksti + ", " + tekstiäJäljellä);
                    LockSupport.parkNanos(16_666_000);
                    tekstiäJäljellä--;
                }
            }
            hudTekstinPäivittäjä.suspend();
        }
    };

    static String kelattuTeksti = "";
    private static void luoVuoropuheRuutu(Icon kuvake, String teksti, String nimi) {
        kelattuTeksti = teksti;
        vuoropuheKuvake.setIcon(kuvake);
        vuoropuheNimi.setText(nimi);
        tekstiäJäljellä = teksti.length();
        tekstiAjastin = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tekstiäJäljellä > 0 && tekstiAuki) {
                    String tulostettavaTeksti = teksti.substring(0, teksti.length()-tekstiäJäljellä +1);
                    vuoropuheTeksti.setText(tulostettavaTeksti);
                    System.out.println(tulostettavaTeksti + " ... " + tekstiäJäljellä);
                    tekstiäJäljellä--;
                }
                else {
                    tekstiAjastin.stop();
                }
            }
        });
        tekstiAjastin.start();

        dialogiPaneelinTeksti = teksti;
        
        // if (!hudTekstinPäivittäjä.isAlive()) {
        //     hudTekstinPäivittäjä.start();
        //     hudTekstinPäivittäjäKäynnissä = true;
        // }
        // else if (!hudTekstinPäivittäjäKäynnissä) {
        //     hudTekstinPäivittäjä.resume();
        //     hudTekstinPäivittäjäKäynnissä = true;
        // }
    }

    public static void avaaDialogi(Icon kuvake, String teksti, String nimi) {
        if (Peli.dialoginAvausViive <= 0) {
            Peli.pause = true;
            tekstiAuki = true;
            luoVuoropuheRuutu(kuvake, teksti, nimi);
            vuoropuheTeksti.setText("");
            vuoropuhePaneli.setVisible(true);
            Peli.dialoginAvausViive = 5;
        }
    }

    public static void kelaaDialogi() {
        if (tekstiäJäljellä <= 1 && !tekstiAjastin.isRunning()) {
            suljeDialogi();
        }
        else {
            tekstiäJäljellä = 1;
            tekstiAjastin.stop();
            vuoropuheTeksti.setText(kelattuTeksti);
        }
    }

    public static void suljeDialogi() {
        Peli.pause = false;
        tekstiAuki = false;
        vuoropuhePaneli.setVisible(false);
        //hudTekstinPäivittäjäKäynnissä = false;
        //hudTekstinPäivittäjä.suspend();
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
            ylätekstiSijRuutu.setVisible(true);
            ylätekstiKohde.setVisible(true);
        }
        else {
            sijaintiNäkyvissä = false;
            ylätekstiSij.setVisible(false);
            ylätekstiSijRuutu.setVisible(false);
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
        uudelleenpiirräKenttä = true;
    }

    static void näytäTapahtumapalkki() {
        if (!tapahtumapalkkiNäkyvissä) {
            tapahtumapalkkiNäkyvissä = true;
            hudTeksti.setVisible(true);
        }
        else {
            tapahtumapalkkiNäkyvissä = false;
            hudTeksti.setVisible(false);
        }
    }

    static ImageIcon valitsePelaajanKuvake() {
        ImageIcon pelaajanKuvake = Pelaaja.kuvake;
        return pelaajanKuvake;
    }

    public static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        peliKenttä.removeAll();
        peliKenttäAlue.setBackground(new Color(0, 0, 0));
        
        int kohteenSijX = 10;
        int kohteensijY = 10;
        
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                kenttäKohteenKuvake[j][i] = new JLabel();
                kenttäKohteenKuvake[j][i].setName("kenttä_" + j + "_" + i);
                maastoKohteenKuvake[j][i] = new JLabel();
                maastoKohteenKuvake[j][i].setName("maasto_" + j + "_" + i);
            }
        }
        pelaajaLabel = new JLabel(pelaajaKuvake);
        pelaajaLabel.setName("Pelaaja");
        pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
        pelaajaLabel.setIcon(valitsePelaajanKuvake());
        pelaajanEsineLabel = new JLabel(pelaajaKuvake);
        pelaajanEsineLabel.setName("Pelaajan_esine");
        pelaajanEsineLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx/2, pelaajanKokoPx/2);
        pelaajanEsineLabel.setIcon(null);
        // taustaLabel = new JLabel(new ImageIcon());
        // taustaLabel.setBounds(0, 0, Peli.kentänKoko * esineenKokoPx + 20, Peli.kentänKoko * esineenKokoPx + 20);
        npcKuvake = new JLabel[Peli.annaNPCidenMäärä()];
        
        try {
            peliKenttä.add(pelaajaLabel, new Integer(4), 0);
            peliKenttä.add(pelaajanEsineLabel, new Integer(5), 0);
            peliKenttä.add(vuoropuhePaneli, new Integer(6), 0);
            peliKenttä.add(pausePaneli, new Integer(7), 0);
            peliKenttä.add(taustaPaneli, new Integer(0), 0);
            
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
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        
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
                        //kenttäKohteenKuvake[j][i].setComponentZOrder(peliKenttä, 1);
                        //maastoKohteenKuvake[j][i].setComponentZOrder(peliKenttä, 2);
                        peliKenttä.add(kenttäKohteenKuvake[j][i], new Integer(2), 0);
                        //peliKenttä.setComponentZOrder(kenttäKohteenKuvake[j][i], 1);
                        peliKenttä.add(maastoKohteenKuvake[j][i], new Integer(1), 0);
                        //peliKenttä.setComponentZOrder(maastoKohteenKuvake[j][i], 2);
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
                    npcKuvake[i].setName("npc" + i);
                    npcKuvake[i].setIcon(npc.kuvake);
                    npcKuvake[i].setBounds((int)npc.hitbox.getMinX(), (int)npc.hitbox.getMinY(), pelaajanKokoPx, pelaajanKokoPx);
                    if (reunatNäkyvissä) {
                        npcKuvake[i].setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
                    }
                    else {
                        npcKuvake[i].setBorder(null);
                    }
                    peliKenttä.add(npcKuvake[i], new Integer(3), 0);
                    //peliKenttä.setComponentZOrder(npcKuvake[i], 0);
                }
            }

            //vuoropuhePaneli.setComponentZOrder(peliKenttä, 1);
            //taustaPaneli.setComponentZOrder(peliKenttä, 0);
            
            //peliKenttä.setComponentZOrder(pelaajaLabel, 0);
            //peliKenttä.setComponentZOrder(pelaajanEsineLabel, 0);
            //peliKenttä.setComponentZOrder(taustaPaneli, 3);
            //peliKenttä.setComponentZOrder(vuoropuhePaneli, 0);
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

    public static JLayeredPane päivitäIkkuna() {
        
        if (vaatiiPäivityksen) {
            try {
                
                ylätekstiHP.setText("HP: " + Pelaaja.hp);
                ylätekstiKuparit.setText("Tölkkejä: " + Pelaaja.kuparit);
                hpMääräLabel.setText("" + Pelaaja.hp);
                rahaMääräLabel.setText("" + df.format(Pelaaja.raha) + "€");
                tölksMääräLabel.setText("" + Pelaaja.kuparit);
                
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

                            // if (vaatiiKentänPäivityksen) {
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
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                            
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
                            // }
                        }
                    }
                }

                //if (pelaajaSiirtyi || vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                    pelaajaLabel.setBounds((int)Pelaaja.hitbox.getMinX() + 10, (int)Pelaaja.hitbox.getMinY() + 10, pelaajanKokoPx, pelaajanKokoPx);
                    pelaajaLabel.setIcon(valitsePelaajanKuvake());
                    if (reunatNäkyvissä) {
                        pelaajaLabel.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
                    }
                    else {
                        pelaajaLabel.setBorder(null);
                    }
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
                                npcKuvake[i].setBounds((int)npc.hitbox.getMinX()+9, (int)npc.hitbox.getMinY()+9, pelaajanKokoPx, pelaajanKokoPx);
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
        }
        return peliKenttä;
    }

    public static void päivitäNPCKenttä() {

        npcKuvake = new JLabel[Peli.annaNPCidenMäärä()];
        
        try {

            if (Peli.npcLista != null){
                for (Component comp : peliKenttä.getComponents()) {
                    if (comp.getName() != null) {
                        if (comp.getName().contains("npc")) {
                            peliKenttä.remove(comp);
                        }
                    }
                    else {
                        peliKenttä.remove(comp);
                    }
                }
                npcKuvake = new JLabel[Peli.npcLista.size()];
                for (int i = 0; i < Peli.npcLista.size(); i++) {
                    NPC npc = Peli.npcLista.get(i);
                    npcKuvake[i] = new JLabel();
                    npcKuvake[i].setIcon(npc.kuvake);
                    npcKuvake[i].setBounds((int)npc.hitbox.getMinX(), (int)npc.hitbox.getMinY(), pelaajanKokoPx, pelaajanKokoPx);
                    if (reunatNäkyvissä) {
                        npcKuvake[i].setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
                    }
                    else {
                        npcKuvake[i].setBorder(null);
                    }
                    peliKenttä.add(npcKuvake[i], new Integer(3), 0);
                }
            }
            else {
                for (int i = 0; i < npcKuvake.length; i++) {
                    npcKuvake[i] = new JLabel(new ImageIcon());
                    peliKenttä.add(npcKuvake[i], new Integer(3), 0);
                }
            }

            //vuoropuhePaneli.setComponentZOrder(peliKenttä, 1);
            //taustaPaneli.setComponentZOrder(peliKenttä, 0);
            
            //peliKenttä.setComponentZOrder(pelaajaLabel, 0);
            //peliKenttä.setComponentZOrder(pelaajanEsineLabel, 0);
            //peliKenttä.setComponentZOrder(taustaPaneli, 3);
            //peliKenttä.setComponentZOrder(vuoropuhePaneli, 0);
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

    public static void vaihdaTausta(ImageIcon tausta) {
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
