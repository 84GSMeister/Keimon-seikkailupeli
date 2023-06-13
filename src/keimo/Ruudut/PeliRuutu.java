package keimo.Ruudut;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.TavoiteLista;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.*;

public class PeliRuutu {

    public static final int esineenKokoPx = 64;
    public static final int pelaajanKokoPx = 64;
    public static boolean peliRuutuAktiivinen = false;
    
    public static JPanel peliKenttäUlompi, peliKenttäAlue, alueInfoPaneli;
    public static JPanel vasenYläPaneeli, vasenKeskiPaneeli, vasenAlaPaneeli, oikeaYläPaneeli, oikeaKeskiPaneeli, oikeaAlaPaneeli;
    public static JLayeredPane peliKenttä;
    public static JPanel vuoropuhePaneli, vuoropuhePaneliOikea, taustaPaneli, pausePaneli, lisäRuutuPaneli;
    public static JLabel vuoropuheKuvake, vuoropuheTeksti, vuoropuheNimi, pauseLabel;
    public static JLabel kokoruudunTakatausta;
    public static JPanel hud, yläPaneeli, alaPaneeli;
    public static JLabel hudTeksti;
    public static JLabel ylätekstiAika, ylätekstiSij, ylätekstiSijRuutu, ylätekstiKohde, ylätekstiKohdeMaasto, ylätekstiKohdeNPC, ylätekstiHP, ylätekstiKuparit, ylätekstiViive, ylätekstiFPS, ylätekstiKuvat, ylätekstiTickrate, ylätekstiTicks;

    public static JPanel tekstiPaneli, kontrolliInfoPaneli, tavoiteInfoPaneli, aikaInfoPaneli, debugInfoPaneli, invaPaneli, tavaraPaneli, osoitinPaneli, valitunEsineenNimiPaneli, statsiPaneeli;
    public static JLabel[] esineLabel = new JLabel[Pelaaja.esineet.length];
    public static JLabel osoitinLabel, valitunEsineenNimiLabel;
    public static JLabel[] kontrolliInfoLabel = new JLabel[10];
    public static JLabel tavoiteOtsikkoLabel, tavoiteInfoLabel;
    public static JLabel peliTestiLabel, pelaajaLabel, pelaajanEsineLabel, taustaLabel, alueInfoLabel;
    public static JLabel tölksKuvakeLabel, tölksMääräLabel, hpKuvakeLabel, hpMääräLabel, rahaKuvakeLabel, rahaMääräLabel;
    static JLabel[][] kenttäKohteenKuvake;
    static JLabel[][] maastoKohteenKuvake;
    static JLabel[] npcKuvake;
    public static JPanel peliAluePaneli = luoPeliRuudunGUI();
    static DecimalFormat df = new DecimalFormat("##.##");
    
    public static JPanel luoPeliRuudunGUI() {
        
        kenttäKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];

        /**
         * Pelikentän yläosassa oleva tietopalkki (alue)
         */

        alueInfoLabel = new JLabel("Alue");
        alueInfoLabel.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys, 15));
        alueInfoLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        alueInfoLabel.setHorizontalAlignment(JLabel.CENTER);

        alueInfoPaneli = new JPanel();
        alueInfoPaneli.setLayout(new BorderLayout());
        alueInfoPaneli.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys, 15));
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
        pausePaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        pausePaneli.setBounds(138, 138, 384, 384);
        pausePaneli.add(pauseLabel);
        pausePaneli.setVisible(false);

        lisäRuutuPaneli = new JPanel(new BorderLayout());
        lisäRuutuPaneli.setName("LisäRuutuPaneli");
        lisäRuutuPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        lisäRuutuPaneli.setBounds(138, 138, 384, 384);
        lisäRuutuPaneli.setVisible(false);
        
        peliKenttä = new JLayeredPane();
        peliKenttä.setLayout(null);
        peliKenttä.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys-30, PääIkkuna.ikkunanLeveys -30));
        peliKenttä.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttä.setComponentZOrder(vuoropuhePaneli, 0);
        peliKenttä.revalidate();
        peliKenttä.repaint();

        kokoruudunTakatausta = new JLabel(new ImageIcon("tiedostot/kuvat/taustat/kokoruuduntausta.png"));
        kokoruudunTakatausta.setBounds(0, 0, 10, 10);
        kokoruudunTakatausta.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        /**
         * Varsinainen pelikenttä
         * Näiden komponenttien sisältöä muokataan päivitäIkkuna() -metodissa
         */

        peliKenttäUlompi = new JPanel();
        peliKenttäUlompi.setLayout(new GridBagLayout());
        peliKenttäUlompi.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys-10, PääIkkuna.ikkunanLeveys -10));
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
        ylätekstiSij.setVisible(PääIkkuna.sijaintiNäkyvissä);
        ylätekstiSij.setBounds(10,10, 180, 20);

        ylätekstiSijRuutu = new JLabel("Paina nuolinäppäimiä liikkuaksesi");
        ylätekstiSijRuutu.setVisible(PääIkkuna.sijaintiNäkyvissä);
        ylätekstiSijRuutu.setBounds(10,30, 180, 20);

        ylätekstiKohde = new JLabel("Kohteen sisältö näkyy tässä");
        ylätekstiKohde.setVisible(PääIkkuna.sijaintiNäkyvissä);
        ylätekstiKohde.setBounds(10,50, 400, 20);

        ylätekstiKohdeMaasto = new JLabel("Maasto näkyy tässä");
        ylätekstiKohdeMaasto.setVisible(PääIkkuna.sijaintiNäkyvissä);
        ylätekstiKohdeMaasto.setBounds(10,70, 400, 20);

        ylätekstiKohdeNPC = new JLabel("NPC näkyy tässä");
        ylätekstiKohdeNPC.setVisible(PääIkkuna.sijaintiNäkyvissä);
        ylätekstiKohdeNPC.setBounds(10,90, 400, 20);

        ylätekstiViive = new JLabel("Päivitysaika");
        ylätekstiViive.setVisible(PääIkkuna.fpsNäkyvissä);
        ylätekstiViive.setBounds(10,110, 220, 20);

        ylätekstiFPS = new JLabel("FPS");
        ylätekstiFPS.setVisible(PääIkkuna.fpsNäkyvissä);
        ylätekstiFPS.setBounds(10,130, 220, 20);

        ylätekstiKuvat = new JLabel("Kuvia");
        ylätekstiKuvat.setVisible(PääIkkuna.fpsNäkyvissä);
        ylätekstiKuvat.setBounds(10,150, 220, 20);

        ylätekstiTickrate = new JLabel("Tickrate");
        ylätekstiTickrate.setVisible(PääIkkuna.fpsNäkyvissä);
        ylätekstiTickrate.setBounds(10,170, 220, 20);

        ylätekstiTicks = new JLabel("Tickejä");
        ylätekstiTicks.setVisible(PääIkkuna.fpsNäkyvissä);
        ylätekstiTicks.setBounds(10,190, 220, 20);

        debugInfoPaneli = new JPanel();
        debugInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        debugInfoPaneli.setBounds(10, 10, 180, 200);
        debugInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        debugInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        debugInfoPaneli.add(ylätekstiSij);
        debugInfoPaneli.add(ylätekstiSijRuutu);
        debugInfoPaneli.add(ylätekstiKohde);
        debugInfoPaneli.add(ylätekstiKohdeMaasto);
        debugInfoPaneli.add(ylätekstiKohdeNPC);
        debugInfoPaneli.add(ylätekstiViive);
        debugInfoPaneli.add(ylätekstiFPS);
        debugInfoPaneli.add(ylätekstiKuvat);
        debugInfoPaneli.add(ylätekstiTickrate);
        debugInfoPaneli.add(ylätekstiTicks);
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
        kontrolliInfoLabel[4] = new JLabel("Q: Pudota");
        kontrolliInfoLabel[5] = new JLabel("Z: Yhdistä");
        kontrolliInfoLabel[6] = new JLabel("X: Katso esinettä");
        kontrolliInfoLabel[7] = new JLabel("C: Katso kentän kohdetta");
        kontrolliInfoLabel[8] = new JLabel("F: Vuorovaikutus");
        kontrolliInfoLabel[9] = new JLabel("R: Järjestä tavaraluettelo");
        
        kontrolliInfoPaneli = new JPanel();
        kontrolliInfoPaneli.setLayout(new GridLayout(10, 1, 10, 10));
        kontrolliInfoPaneli.setBounds(10, 10, 180, 200);
        kontrolliInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        kontrolliInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            kontrolliInfoPaneli.add(kontrolliInfoLabel[i]);
        }
        kontrolliInfoPaneli.revalidate();
        kontrolliInfoPaneli.repaint();

        tavoiteOtsikkoLabel = new JLabel("Seuraava tavoite:");
        tavoiteInfoLabel = new JLabel(TavoiteLista.pääTavoitteet.get(0));

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
        hpKuvakeLabel.setBounds(20, 0, 100, 55); 

        hpMääräLabel = new JLabel("10", SwingConstants.CENTER);
        hpMääräLabel.setBounds(20, 0, 100, 55);
        hpMääräLabel.setFont(new Font("MS Gothic", Font.BOLD, 20));

        JPanel hpMääräPaneli = new JPanel(null);
        hpMääräPaneli.setPreferredSize(new Dimension(100, 55));
        hpMääräPaneli.add(hpMääräLabel);
        hpMääräPaneli.add(hpKuvakeLabel);
        hpMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        rahaKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/rahet.png"));
        rahaKuvakeLabel.setBounds(20, 0, 100, 55);

        rahaMääräLabel = new JLabel("0.00€", SwingConstants.CENTER);
        rahaMääräLabel.setBounds(20, 0, 100, 55);
        rahaMääräLabel.setFont(new Font("MS Gothic", Font.BOLD, 20));

        JPanel rahaMääräPaneli = new JPanel(null);
        rahaMääräPaneli.setPreferredSize(new Dimension(100, 55));
        rahaMääräPaneli.add(rahaMääräLabel);
        rahaMääräPaneli.add(rahaKuvakeLabel);
        rahaMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        tölksKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/tölks.png"));
        tölksKuvakeLabel.setBounds(20, 0, 100, 55);

        tölksMääräLabel = new JLabel("0", SwingConstants.CENTER);
        tölksMääräLabel.setBounds(20, 0, 100, 55);
        tölksMääräLabel.setFont(new Font("MS Gothic", Font.BOLD, 20));

        JPanel tölksMääräPaneli = new JPanel(null);
        tölksMääräPaneli.setPreferredSize(new Dimension(100, 55));
        tölksMääräPaneli.add(tölksMääräLabel);
        tölksMääräPaneli.add(tölksKuvakeLabel);
        tölksMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        JLabel[] plaseholderLabels = new JLabel[1];

        statsiPaneeli = new JPanel(new GridLayout(4, 1));
        statsiPaneeli.setBounds(10, 10, 180, 200);
        statsiPaneeli.add(hpMääräPaneli);
        statsiPaneeli.add(rahaMääräPaneli);
        statsiPaneeli.add(tölksMääräPaneli);
        for (JLabel jLabel : plaseholderLabels) {
            jLabel = new JLabel("");
            jLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
            statsiPaneeli.add(jLabel);
        }


        osoitinLabel = new JLabel("Tavaraluettelo");

        osoitinPaneli = new JPanel();
        osoitinPaneli.setLayout(new GridBagLayout());
        osoitinPaneli.setPreferredSize(new Dimension(192, 30));
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
        invaPaneli.setPreferredSize(new Dimension(192, 188));
        invaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        invaPaneli.add(osoitinPaneli, BorderLayout.NORTH);
        invaPaneli.add(tavaraPaneli, BorderLayout.CENTER);
        invaPaneli.add(valitunEsineenNimiPaneli, BorderLayout.SOUTH);
        invaPaneli.revalidate();
        invaPaneli.repaint();
        
        hud = new JPanel();
        hud.setLayout(new BorderLayout());
        hud.setBounds(0, 20, 200, 188);
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
        hudTeksti.setVisible(PääIkkuna.tapahtumapalkkiNäkyvissä);
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
        oikeaYläPaneeli.setComponentZOrder(ylätekstiKohdeMaasto, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiKohdeNPC, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiViive, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiFPS, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiKuvat, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiTickrate, 0);
        oikeaYläPaneeli.setComponentZOrder(ylätekstiTicks, 0);
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
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        peliKenttäUlompi.add(kokoruudunTakatausta, gbc);

        peliKenttäUlompi.setComponentZOrder(peliKenttä, 0);
        peliKenttäUlompi.revalidate();
        peliKenttäUlompi.repaint();
        
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new BorderLayout());
        tekstiPaneli.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys, 15));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        tekstiPaneli.revalidate();
        tekstiPaneli.repaint();

        peliKenttäAlue = new JPanel();
        peliKenttäAlue.setLayout(new BorderLayout());
        peliKenttäAlue.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys-10, PääIkkuna.ikkunanLeveys -10));
        peliKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliKenttäAlue.setBackground(new Color(0, 0, 0, 255));
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

        peliAluePaneli = new JPanel();
        peliAluePaneli.setLayout(new BorderLayout());
        peliAluePaneli.setBackground(new Color(0, 0, 0, 255));
        peliAluePaneli.add(peliKenttäAlue, BorderLayout.CENTER);

        alustaPeliRuutu();

        return peliAluePaneli;
    }

    public static void näytäFPS() {
        if (!PääIkkuna.fpsNäkyvissä) {
            PääIkkuna.fpsNäkyvissä = true;
            ylätekstiViive.setVisible(true);
            ylätekstiFPS.setVisible(true);
            ylätekstiKuvat.setVisible(true);
            ylätekstiTickrate.setVisible(true);
            ylätekstiTicks.setVisible(true);
        }
        else {
            PääIkkuna.fpsNäkyvissä = false;
            ylätekstiViive.setVisible(false);
            ylätekstiFPS.setVisible(false);
            ylätekstiKuvat.setVisible(false);
            ylätekstiTickrate.setVisible(false);
            ylätekstiTicks.setVisible(false);
        }
    }

    public static void näytäSijainti() {
        if (!PääIkkuna.sijaintiNäkyvissä) {
            PääIkkuna.sijaintiNäkyvissä = true;
            ylätekstiSij.setVisible(true);
            ylätekstiSijRuutu.setVisible(true);
            ylätekstiKohde.setVisible(true);
            ylätekstiKohdeMaasto.setVisible(true);
            ylätekstiKohdeNPC.setVisible(true);
        }
        else {
            PääIkkuna.sijaintiNäkyvissä = false;
            ylätekstiSij.setVisible(false);
            ylätekstiSijRuutu.setVisible(false);
            ylätekstiKohde.setVisible(false);
            ylätekstiKohdeMaasto.setVisible(false);
            ylätekstiKohdeNPC.setVisible(false);
        }
    }

    public static void alustaPeliRuutu() {

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
        pelaajaLabel = new JLabel();
        pelaajaLabel.setName("Pelaaja");
        pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
        pelaajaLabel.setIcon(PääIkkuna.valitsePelaajanKuvake());
        pelaajanEsineLabel = new JLabel();
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
            peliKenttä.add(lisäRuutuPaneli, new Integer(7), 0);
            peliKenttä.add(pausePaneli, new Integer(8), 0);
            peliKenttä.add(taustaPaneli, new Integer(0), 0);
            
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (kenttäKohteenKuvake[j][i] != null) {
                        if (PääIkkuna.reunatNäkyvissä) {
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
                        peliKenttä.add(kenttäKohteenKuvake[j][i], new Integer(2), 0);
                        peliKenttä.add(maastoKohteenKuvake[j][i], new Integer(1), 0);
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
                    if (PääIkkuna.reunatNäkyvissä) {
                        npcKuvake[i].setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
                    }
                    else {
                        npcKuvake[i].setBorder(null);
                    }
                    peliKenttä.add(npcKuvake[i], new Integer(3), 0);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
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

    public static JLayeredPane päivitäPeliRuutu() {

        //if (vaatiiPäivityksen) {
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

                            if (PääIkkuna.reunatNäkyvissä) {
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
                        }
                    }
                }

                pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                pelaajaLabel.setBounds((int)Pelaaja.hitbox.getMinX() + 10, (int)Pelaaja.hitbox.getMinY() + 10, pelaajanKokoPx, pelaajanKokoPx);
                pelaajaLabel.setIcon(PääIkkuna.valitsePelaajanKuvake());
                if (PääIkkuna.reunatNäkyvissä) {
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
                }
                else {
                    pelaajanEsineLabel.setIcon(null);
                }
                PääIkkuna.pelaajaSiirtyi = false;

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
        //    vaatiiPäivityksen = false;
        //}
        return peliKenttä;
    }

    static Random r = new Random();
    static int hajontaKerroin = Pelaaja.känninVoimakkuus;
    static int hajontaKerroinPienempi = hajontaKerroin/2;
    static int hajontaKerroinIsompi = Pelaaja.känninVoimakkuus+1;
    static int hajontaXKaikki = 0;
    static int hajontaYKaikki = 0;
    static int hajontaX = 0;
    static int hajontaY = 0;

    public static void luoKänniEfekti() {
        
        r = new Random();
        hajontaKerroin = Pelaaja.känninVoimakkuus;
        hajontaKerroinPienempi = hajontaKerroin/2;
        hajontaKerroinIsompi = Pelaaja.känninVoimakkuus+1;
        hajontaXKaikki = 0;
        hajontaYKaikki = 0;
        hajontaX = 0;
        hajontaY = 0;

        if (Pelaaja.känninVoimakkuus > 0) {
            hajontaKerroinIsompi = Pelaaja.känninVoimakkuus+1;
            hajontaKerroin = Pelaaja.känninVoimakkuus;
            hajontaKerroinPienempi = hajontaKerroin/2;
            hajontaXKaikki = r.nextInt(-1 * hajontaKerroinIsompi + 1, hajontaKerroinIsompi);
            hajontaYKaikki = r.nextInt(-1 * hajontaKerroinIsompi + 1, hajontaKerroinIsompi);
        }
        
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                if (kenttäKohteenKuvake[j][i] != null) {
                    if (Peli.pelikenttä[j][i] instanceof KenttäKohde && kenttäKohteenKuvake[j][i] != null) {
                        if (hajontaKerroin > 0) {
                            if (hajontaKerroinIsompi/2 > 0) {
                                hajontaX = r.nextInt(-1 * hajontaKerroinIsompi/2 + 1, hajontaKerroinIsompi/2);
                                hajontaY = r.nextInt(-1 * hajontaKerroinIsompi/2 + 1, hajontaKerroinIsompi/2);
                            }
                            if ((int)kenttäKohteenKuvake[j][i].getBounds().getX() + hajontaX + hajontaXKaikki < (int)(10+(j)*64+Pelaaja.känninVoimakkuus*6) && (int)kenttäKohteenKuvake[j][i].getBounds().getX() + hajontaX + hajontaXKaikki > (int)(10+(j)*64-Pelaaja.känninVoimakkuus*6) && (int)kenttäKohteenKuvake[j][i].getBounds().getY() + hajontaY + hajontaYKaikki < (int)(10+(i)*64+Pelaaja.känninVoimakkuus*6) && (int)kenttäKohteenKuvake[j][i].getBounds().getY() + hajontaY + hajontaYKaikki > (int)(10+(i)*64-Pelaaja.känninVoimakkuus*6)) {
                                kenttäKohteenKuvake[j][i].setBounds((int)kenttäKohteenKuvake[j][i].getBounds().getX() + hajontaX + hajontaXKaikki, (int)kenttäKohteenKuvake[j][i].getBounds().getY() + hajontaY + hajontaYKaikki, (int)kenttäKohteenKuvake[j][i].getBounds().getWidth(), (int)kenttäKohteenKuvake[j][i].getBounds().getHeight());
                            }
                        }
                    }
                    if (Peli.maastokenttä[j][i] instanceof Maasto && maastoKohteenKuvake[j][i] != null) {
                        if (hajontaKerroin > 0) {
                            if (hajontaKerroinPienempi > 0) {
                                hajontaX = r.nextInt(-1 * hajontaKerroinPienempi + 1, hajontaKerroinPienempi);
                                hajontaY = r.nextInt(-1 * hajontaKerroinPienempi + 1, hajontaKerroinPienempi);
                            }
                            maastoKohteenKuvake[j][i].setBounds((int)(10+j*64) + hajontaX + hajontaXKaikki, (int)(10+i*64) + hajontaY + hajontaYKaikki, (int)maastoKohteenKuvake[j][i].getBounds().getWidth(), (int)maastoKohteenKuvake[j][i].getBounds().getHeight());
                        }
                    }
                }
            }
        }
    }

    public static void nollaaKänniEfekti() {
        hajontaKerroin = Pelaaja.känninVoimakkuus;
        hajontaKerroinPienempi = hajontaKerroin/2;
        hajontaKerroinIsompi = Pelaaja.känninVoimakkuus+1;
        hajontaXKaikki = 0;
        hajontaYKaikki = 0;
        hajontaX = 0;
        hajontaY = 0;

        int kohteenSijX = 10;
        int kohteensijY = 10;

        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                if (kenttäKohteenKuvake[j][i] != null) {
                    kenttäKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                    maastoKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                    kohteenSijX += esineenKokoPx;
                }
            }
            kohteenSijX = 10;
            kohteensijY += esineenKokoPx;
        }
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
                    if (PääIkkuna.reunatNäkyvissä) {
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
}
