package keimo.Ruudut;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Maastot.*;
import keimo.Säikeet.GrafiikanPäivitysSäie;
import keimo.Utility.KeimoFontit;
import keimo.Utility.KäännettäväKuvake;
import keimo.Utility.SkaalattavaKuvake;
import keimo.entityt.*;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.kenttäkohteet.kiintopiste.Kiintopiste;
import keimo.kenttäkohteet.warp.Warp;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.*;

public class PeliRuutu {

    public static int esineenKokoPx = 64;
    public static int pelaajanKokoPx = 64;
    public static boolean peliRuutuAktiivinen = false;
    
    public static JPanel peliKenttäJaHUD, peliRuutuPaneli, alueInfoPaneli;
    public static JPanel vasenYläPaneeli, vasenKeskiPaneeli, vasenAlaPaneeli, oikeaYläPaneeli, oikeaKeskiPaneeli, oikeaAlaPaneeli;
    public static JLabel vasenYläPaneelinTausta, vasenKeskiPaneelinTausta, vasenAlaPaneelinTausta, oikeaYläPaneelinTausta, oikeaKeskiPaneelinTausta, oikeaAlaPaneelinTausta;
    public static JLayeredPane peliKentänTaustaPaneli;
    public static JLayeredPane peliKenttä;
    public static JScrollPane scrollaavaPelikenttä;
    public static JPanel scrollaavanPelikentänPaneeli;
    public static JPanel vuoropuhePaneli, vuoropuhePaneliOikea, pausePaneli, lisäRuutuPaneli;
    public static JLabel vuoropuheKuvake, vuoropuheTeksti, vuoropuheNimi, pauseLabel;
    public static JLabel kokoruudunTakatausta, latausTausta;
    public static JPanel invaPanelinHud, yläPaneeli, alaPaneeli;
    public static JLabel hudTeksti;
    public static JPanel ostosPaneli, ostosOtsikkoPaneli, ostoksetYhteensäPaneli, ostosPanelinHud;
    public static JLabel ostosOtsikkoLabel, ostoksetYhteensäLabel;
    public static JLabel[] ostosEsineLabel, ostosEsineNimiLabel;
    public static JLabel ylätekstiAika, ylätekstiSij, ylätekstiSijRuutu, ylätekstiKohde, ylätekstiKohdeMaasto, ylätekstiKohdeNPC, ylätekstiViive, ylätekstiFPS, ylätekstiKuvat, ylätekstiTickrate, ylätekstiTicks;
    public static JPanel tekstiPaneli, kontrolliInfoPaneli, tavoiteInfoPaneli, aikaInfoPaneli, debugInfoPaneli, invaPaneli, tavaraPaneli, tavaraluettelonOtsikkoPaneli, valitunEsineenNimiPaneli, statsiPaneeli;
    public static JLabel[] esineLabel = new JLabel[Pelaaja.esineet.length];
    public static JLabel tavaraluettelonOtsikkoLabel, valitunEsineenNimiLabel;
    public static JLabel[] kontrolliInfoLabel = new JLabel[7];
    public static JLabel tavoiteOtsikkoLabel, tavoiteInfoLabel;
    public static JLabel peliTestiLabel, pelaajaLabel, pelaajanEsineLabel, pelaajanHyökkäysLabel, taustaLabel, alueInfoLabel, minimapLabel, pelaajaKartallaLabel;
    public static JLabel tölksKuvakeLabel, tölksMääräLabel, hpKuvakeLabel, hpMääräLabel, rahaKuvakeLabel, rahaMääräLabel;
    static JLabel[][] kenttäKohteenKuvake;
    static JLabel[][] maastoKohteenKuvake;
    static JLabel[] npcKuvake;
    public static JPanel peliAluePaneli;// = luoPeliRuudunGUI();
    static DecimalFormat df = new DecimalFormat("##.##");
    //protected static ImageIcon[][] kenttäkohteenSkaalattuKuvake = new ImageIcon[Peli.kentänKoko][Peli.kentänKoko];
    //protected static ImageIcon[][] viimeisinKuvake = new ImageIcon[Peli.kentänKoko][Peli.kentänKoko];
    public static Icon referenssiTaustaKuvake;
    
    public static JPanel luoPeliRuudunGUI() {
        
        GrafiikanPäivitysSäie.huoneenGrafiikanLatausKäynnissä = true;
        kenttäKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];

        taustaLabel = new JLabel(new ImageIcon());
        taustaLabel.setName("tausta");
        taustaLabel.setBounds(0, 0, 660, 660);

        vuoropuheKuvake = new JLabel("", SwingConstants.CENTER);
        vuoropuheKuvake.setPreferredSize(new Dimension(140, 110));
        vuoropuheKuvake.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        vuoropuheTeksti = new JLabel("teksti tähän", SwingConstants.LEFT);
        vuoropuheTeksti.setVerticalAlignment(SwingConstants.TOP);
        vuoropuheTeksti.setFont(KeimoFontit.fontti_keimo_12);
        vuoropuheTeksti.setPreferredSize(new Dimension(200, 80));

        vuoropuheNimi = new JLabel("nimi tähän", SwingConstants.LEFT);
        vuoropuheNimi.setFont(KeimoFontit.fontti_keimo_12);
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
        pausePaneli.setBounds(128, 128, 384, 384);
        pausePaneli.add(pauseLabel);
        pausePaneli.setVisible(false);

        lisäRuutuPaneli = new JPanel(new BorderLayout());
        lisäRuutuPaneli.setName("LisäRuutuPaneli");
        lisäRuutuPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        lisäRuutuPaneli.setBounds(128, 128, 384, 384);
        lisäRuutuPaneli.setVisible(false);
        
        peliKenttä = new JLayeredPane();
        peliKenttä.setLayout(null);
        peliKenttä.setPreferredSize(new Dimension(640, 640));
        peliKenttä.setComponentZOrder(vuoropuhePaneli, 0);
        peliKenttä.setBackground(new Color(200, 100, 100));
        peliKenttä.setOpaque(false);
        peliKenttä.revalidate();
        peliKenttä.repaint();

        scrollaavaPelikenttä = new JScrollPane(peliKenttä) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        scrollaavaPelikenttä.setBounds(0, 0, 640, 640);
        scrollaavaPelikenttä.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollaavaPelikenttä.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollaavaPelikenttä.setBorder(null);
        scrollaavaPelikenttä.setOpaque(false);
        scrollaavaPelikenttä.setBackground(new Color(0, 0, 0, 0));
        scrollaavaPelikenttä.getViewport().setOpaque(false);
        scrollaavaPelikenttä.getViewport().setBackground(new Color(128, 128, 128, 128));
        scrollaavaPelikenttä.setWheelScrollingEnabled(false);
        scrollaavanPelikentänPaneeli = new JPanel(null);
        scrollaavanPelikentänPaneeli.setBounds(10, 10, 640, 640);
        scrollaavanPelikentänPaneeli.add(scrollaavaPelikenttä);
        
        


        kokoruudunTakatausta = new JLabel(new ImageIcon("tiedostot/kuvat/taustat/kokoruuduntausta.png"));
        kokoruudunTakatausta.setBounds(0, 0, 10, 10);
        kokoruudunTakatausta.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        latausTausta = new JLabel(new ImageIcon("tiedostot/kuvat/taustat/lataustausta.png"));
        latausTausta.setBounds(0, 0, 10, 10);
        latausTausta.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        /**
         * Varsinainen pelikenttä
         * Näiden komponenttien sisältöä muokataan päivitäIkkuna() -metodissa
         */

        peliKenttäJaHUD = new JPanel();
        peliKenttäJaHUD.setLayout(new GridBagLayout());
        peliKenttäJaHUD.setBorder(BorderFactory.createLineBorder(Color.black, 1));


        JLabel aikaOtsikkoLabel = new JLabel("Aika: ");
        aikaOtsikkoLabel.setFont(KeimoFontit.fontti_keimo_12);
        ylätekstiAika = new JLabel("Aika");
        ylätekstiAika.setFont(KeimoFontit.fontti_keimo_12);
        ylätekstiAika.setBounds(10, 40, 180, 20);

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

        tavoiteOtsikkoLabel = new JLabel("Seuraava tavoite:");
        tavoiteOtsikkoLabel.setFont(KeimoFontit.fontti_keimo_10);
        tavoiteInfoLabel = new JLabel(TavoiteLista.pääTavoitteet.get(0));
        tavoiteInfoLabel.setFont(KeimoFontit.fontti_keimo_10);

        aikaInfoPaneli = new JPanel();
        aikaInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        aikaInfoPaneli.setBounds(10, 10, 180, 200);
        aikaInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        aikaInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        aikaInfoPaneli.add(aikaOtsikkoLabel);
        aikaInfoPaneli.add(ylätekstiAika);
        aikaInfoPaneli.add(tavoiteOtsikkoLabel);
        aikaInfoPaneli.add(tavoiteInfoLabel);
        aikaInfoPaneli.revalidate();
        aikaInfoPaneli.repaint();

        kontrolliInfoLabel[0] = new JLabel("Nuolet / WASD: Liiku");
        kontrolliInfoLabel[1] = new JLabel("Space: Käytä esinettä");
        kontrolliInfoLabel[2] = new JLabel("1-6: Vaihda tavarapaikkaa");
        kontrolliInfoLabel[3] = new JLabel("E: Poimi / Vuorovaikutus");
        kontrolliInfoLabel[4] = new JLabel("Q: Pudota");
        kontrolliInfoLabel[5] = new JLabel("Z: Yhdistä");
        kontrolliInfoLabel[6] = new JLabel("X: Katso esinettä");
        
        kontrolliInfoPaneli = new JPanel();
        kontrolliInfoPaneli.setLayout(new GridLayout(kontrolliInfoLabel.length, 1, 10, 10));
        kontrolliInfoPaneli.setBounds(10, 10, 180, 200);
        kontrolliInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        kontrolliInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            kontrolliInfoPaneli.add(kontrolliInfoLabel[i]);
        }
        kontrolliInfoPaneli.revalidate();
        kontrolliInfoPaneli.repaint();

        

        alueInfoLabel = new JLabel("Alue");
        alueInfoLabel.setPreferredSize(new Dimension(180, 20));
        alueInfoLabel.setFont(KeimoFontit.fontti_keimo_16);
        alueInfoLabel.setHorizontalAlignment(JLabel.CENTER);

        minimapLabel = new JLabel();
        minimapLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        minimapLabel.setBounds(0, 0, 180, 180);
        minimapLabel.setHorizontalAlignment(JLabel.CENTER);

        pelaajaKartallaLabel = new JLabel(new ImageIcon("tiedostot/kuvat/kartta/pelaaja_kartalla.png"));
        pelaajaKartallaLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        pelaajaKartallaLabel.setBounds(10, 10, 20, 20);
        pelaajaKartallaLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel minimapPaneli = new JPanel();
        minimapPaneli.setLayout(null);
        minimapPaneli.setPreferredSize(new Dimension(180, 180));
        minimapPaneli.setBounds(0, 0, 180, 180);
        
        minimapPaneli.add(pelaajaKartallaLabel);
        minimapPaneli.add(minimapLabel);

        alueInfoPaneli = new JPanel();
        //alueInfoPaneli.setLayout(new GridLayout(9, 1, 10, 10));
        alueInfoPaneli.setLayout(new BorderLayout());
        alueInfoPaneli.setBounds(10, 10, 180, 200);
        alueInfoPaneli.setBackground(new Color(210, 210, 210, 255));
        alueInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        //tavoiteInfoPaneli.add(tavoiteOtsikkoLabel);
        //tavoiteInfoPaneli.add(tavoiteInfoLabel);
        alueInfoPaneli.add(alueInfoLabel, BorderLayout.NORTH);
        alueInfoPaneli.add(minimapPaneli, BorderLayout.CENTER);
        alueInfoPaneli.revalidate();
        alueInfoPaneli.repaint();


        hpKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/hp_eitekstiä.png"));
        hpKuvakeLabel.setBounds(20, 0, 100, 55); 

        hpMääräLabel = new JLabel("10", SwingConstants.CENTER);
        hpMääräLabel.setBounds(20, 0, 100, 55);
        hpMääräLabel.setFont(KeimoFontit.fontti_keimo_16);
        hpMääräLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel hpMääräPaneli = new JPanel(null);
        hpMääräPaneli.setPreferredSize(new Dimension(100, 55));
        hpMääräPaneli.add(hpMääräLabel);
        hpMääräPaneli.add(hpKuvakeLabel);
        hpMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        rahaKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/rahet.png"));
        rahaKuvakeLabel.setBounds(20, 0, 100, 55);

        rahaMääräLabel = new JLabel("0.00€", SwingConstants.CENTER);
        rahaMääräLabel.setBounds(20, 0, 100, 55);
        rahaMääräLabel.setFont(KeimoFontit.fontti_keimo_16);
        rahaMääräLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel rahaMääräPaneli = new JPanel(null);
        rahaMääräPaneli.setPreferredSize(new Dimension(100, 55));
        rahaMääräPaneli.add(rahaMääräLabel);
        rahaMääräPaneli.add(rahaKuvakeLabel);
        rahaMääräPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

        tölksKuvakeLabel = new JLabel(new ImageIcon("tiedostot/kuvat/hud/tölks.png"));
        tölksKuvakeLabel.setBounds(20, 0, 100, 55);

        tölksMääräLabel = new JLabel("0", SwingConstants.CENTER);
        tölksMääräLabel.setBounds(20, 0, 100, 55);
        tölksMääräLabel.setFont(KeimoFontit.fontti_keimo_16);
        tölksMääräLabel.setHorizontalAlignment(JLabel.CENTER);

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



        ostosOtsikkoLabel = new JLabel("Ostoskori");

        ostosOtsikkoPaneli = new JPanel();
        ostosOtsikkoPaneli.setLayout(new GridBagLayout());
        ostosOtsikkoPaneli.setPreferredSize(new Dimension(180, 30));
        ostosOtsikkoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        ostosOtsikkoPaneli.add(ostosOtsikkoLabel);
        ostosOtsikkoPaneli.revalidate();
        ostosOtsikkoPaneli.repaint();

        ostosEsineLabel = new JLabel[1];
        for (int i = 0; i < ostosEsineLabel.length; i++) {
            ostosEsineLabel[i] = new JLabel("");
            ostosEsineLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        }
        ostosEsineNimiLabel = new JLabel[1];
        for (int i = 0; i < ostosEsineNimiLabel.length; i++) {
            ostosEsineNimiLabel[i] = new JLabel("");
        }

        ostosPaneli = new JPanel();
        ostosPaneli.setLayout(new GridLayout(ostosEsineLabel.length, 2));
        ostosPaneli.setPreferredSize(new Dimension(180, 170));
        ostosPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < ostosEsineLabel.length; i++) {
            ostosPaneli.add(ostosEsineLabel[i]);
            ostosPaneli.add(ostosEsineNimiLabel[i]);
        }
        ostosPaneli.revalidate();
        ostosPaneli.repaint();

        ostoksetYhteensäLabel = new JLabel("Ostokset yhteensä: " + 0 + "€");

        ostoksetYhteensäPaneli = new JPanel();
        ostoksetYhteensäPaneli.setLayout(new GridBagLayout());
        ostoksetYhteensäPaneli.setPreferredSize(new Dimension(180, 30));
        ostoksetYhteensäPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        ostoksetYhteensäPaneli.add(ostoksetYhteensäLabel);
        ostoksetYhteensäPaneli.revalidate();
        ostoksetYhteensäPaneli.repaint();

        ostosPanelinHud = new JPanel();
        ostosPanelinHud.setLayout(new BorderLayout());
        ostosPanelinHud.setBounds(10, 10, 180, 200);
        ostosPanelinHud.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        ostosPanelinHud.add(ostosOtsikkoPaneli, BorderLayout.NORTH);
        ostosPanelinHud.add(ostosPaneli, BorderLayout.CENTER);
        ostosPanelinHud.add(ostoksetYhteensäPaneli, BorderLayout.SOUTH);
        ostosPanelinHud.revalidate();
        ostosPanelinHud.repaint();




        tavaraluettelonOtsikkoLabel = new JLabel("Tavaraluettelo");
        tavaraluettelonOtsikkoLabel.setFont(KeimoFontit.fontti_keimo_12);

        tavaraluettelonOtsikkoPaneli = new JPanel();
        tavaraluettelonOtsikkoPaneli.setLayout(new GridBagLayout());
        tavaraluettelonOtsikkoPaneli.setPreferredSize(new Dimension(192, 30));
        tavaraluettelonOtsikkoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        tavaraluettelonOtsikkoPaneli.add(tavaraluettelonOtsikkoLabel);
        tavaraluettelonOtsikkoPaneli.revalidate();
        tavaraluettelonOtsikkoPaneli.repaint();

        for (int i = 0; i < esineLabel.length; i++) {
            esineLabel[i] = new JLabel("Esine 0");
            esineLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        }
        esineLabel[Peli.esineValInt].setBorder(BorderFactory.createLineBorder(Color.red, 3, true));

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
        valitunEsineenNimiLabel.setFont(KeimoFontit.fontti_keimo_12);

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
        invaPaneli.add(tavaraluettelonOtsikkoPaneli, BorderLayout.NORTH);
        invaPaneli.add(tavaraPaneli, BorderLayout.CENTER);
        invaPaneli.add(valitunEsineenNimiPaneli, BorderLayout.SOUTH);
        invaPaneli.revalidate();
        invaPaneli.repaint();
        
        invaPanelinHud = new JPanel();
        invaPanelinHud.setLayout(new BorderLayout());
        invaPanelinHud.setBounds(0, 20, 200, 188);
        invaPanelinHud.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        invaPanelinHud.add(invaPaneli, BorderLayout.SOUTH);
        invaPanelinHud.revalidate();
        invaPanelinHud.repaint();


        hudTeksti = new JLabel("Kokeile liikkua ja poimia esineitä!");
        alaPaneeli = new JPanel();
        alaPaneeli.setLayout(new BorderLayout());
        alaPaneeli.setPreferredSize(new Dimension(1060, 15));
        alaPaneeli.setBounds(0, 640, 660, 20);
        alaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        alaPaneeli.setVisible(PääIkkuna.tapahtumapalkkiNäkyvissä);
        alaPaneeli.add(hudTeksti, BorderLayout.CENTER);
        alaPaneeli.revalidate();
        alaPaneeli.repaint();

        vasenYläPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        vasenYläPaneelinTausta.setBounds(0, 0, 200, 220);
        vasenYläPaneeli = new JPanel();
        vasenYläPaneeli.setLayout(null);
        vasenYläPaneeli.setPreferredSize(new Dimension(200, 220));
        vasenYläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        vasenYläPaneeli.add(aikaInfoPaneli);
        vasenYläPaneeli.add(vasenYläPaneelinTausta);
        vasenYläPaneeli.revalidate();
        vasenYläPaneeli.repaint();

        vasenKeskiPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        vasenKeskiPaneelinTausta.setBounds(0, 0, 200, 220);
        vasenKeskiPaneeli = new JPanel();
        vasenKeskiPaneeli.setLayout(null);
        vasenKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
        vasenKeskiPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        vasenKeskiPaneeli.add(statsiPaneeli);
        vasenKeskiPaneeli.add(vasenKeskiPaneelinTausta);
        vasenKeskiPaneeli.revalidate();
        vasenKeskiPaneeli.repaint();

        vasenAlaPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        vasenAlaPaneelinTausta.setBounds(0, 0, 200, 220);
        vasenAlaPaneeli = new JPanel();
        vasenAlaPaneeli.setLayout(null);
        vasenAlaPaneeli.setPreferredSize(new Dimension(200, 220));
        vasenAlaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        vasenAlaPaneeli.add(invaPanelinHud);
        vasenAlaPaneeli.add(vasenAlaPaneelinTausta);
        vasenAlaPaneeli.revalidate();
        vasenAlaPaneeli.repaint();

        oikeaYläPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
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

        oikeaKeskiPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        oikeaKeskiPaneelinTausta.setBounds(0, 0, 200, 220);
        oikeaKeskiPaneeli = new JPanel();
        oikeaKeskiPaneeli.setLayout(null);
        oikeaKeskiPaneeli.setPreferredSize(new Dimension(200, 220));
        oikeaKeskiPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        oikeaKeskiPaneeli.add(alueInfoPaneli);
        oikeaKeskiPaneeli.add(oikeaKeskiPaneelinTausta);
        oikeaKeskiPaneeli.setComponentZOrder(oikeaKeskiPaneelinTausta, 1);
        oikeaKeskiPaneeli.setComponentZOrder(alueInfoPaneli, 0);
        oikeaKeskiPaneeli.revalidate();
        oikeaKeskiPaneeli.repaint();

        oikeaAlaPaneelinTausta = new JLabel(new ImageIcon("tiedostot/kuvat/hud/paneeli_tausta_kehys.png"));
        oikeaAlaPaneelinTausta.setBounds(0, 0, 200, 220);
        oikeaAlaPaneeli = new JPanel();
        oikeaAlaPaneeli.setLayout(null);
        oikeaAlaPaneeli.setPreferredSize(new Dimension(200, 220));
        oikeaAlaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        oikeaAlaPaneeli.add(ostosPanelinHud);
        oikeaAlaPaneeli.add(kontrolliInfoPaneli);
        oikeaAlaPaneeli.add(oikeaAlaPaneelinTausta);
        oikeaAlaPaneeli.setComponentZOrder(oikeaAlaPaneelinTausta, 2);
        oikeaAlaPaneeli.setComponentZOrder(ostosPanelinHud, 1);
        oikeaAlaPaneeli.setComponentZOrder(ostosPanelinHud, 0);
        oikeaAlaPaneeli.revalidate();
        oikeaAlaPaneeli.repaint();

        peliKentänTaustaPaneli = new JLayeredPane();
        peliKentänTaustaPaneli.setLayout(null);
        peliKentänTaustaPaneli.setOpaque(false);
        peliKentänTaustaPaneli.revalidate();
        peliKentänTaustaPaneli.repaint();

        GridBagLayout pelikentänLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        peliKenttäJaHUD.setLayout(pelikentänLayout);
        peliKenttäJaHUD.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        //peliKenttäJaHUD.add(yläPaneeli, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        peliKenttäJaHUD.add(vasenYläPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        peliKenttäJaHUD.add(vasenKeskiPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        peliKenttäJaHUD.add(vasenAlaPaneeli, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 3;
        peliKenttäJaHUD.add(peliKentänTaustaPaneli, gbc);
        gbc.gridheight = 1;
        gbc.gridx = 2;
        gbc.gridy = 1;
        peliKenttäJaHUD.add(oikeaYläPaneeli, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        peliKenttäJaHUD.add(oikeaKeskiPaneeli, gbc);
        gbc.gridx = 2;
        gbc.gridy = 3;
        peliKenttäJaHUD.add(oikeaAlaPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        //peliKenttäJaHUD.add(alaPaneeli, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        peliKenttäJaHUD.add(kokoruudunTakatausta, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        peliKenttäJaHUD.add(latausTausta, gbc);

        //peliKenttäJaHUD.setComponentZOrder(peliKenttä, 0);
        peliKenttäJaHUD.setComponentZOrder(peliKentänTaustaPaneli, 0);
        peliKenttäJaHUD.revalidate();
        peliKenttäJaHUD.repaint();
        
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new BorderLayout());
        tekstiPaneli.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys, 15));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        tekstiPaneli.revalidate();
        tekstiPaneli.repaint();

        peliRuutuPaneli = new JPanel();
        peliRuutuPaneli.setLayout(new BorderLayout());
        peliRuutuPaneli.setPreferredSize(new Dimension(PääIkkuna.ikkunanLeveys-10, PääIkkuna.ikkunanLeveys -10));
        peliRuutuPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        peliRuutuPaneli.setBackground(new Color(0, 0, 0, 255));
        peliRuutuPaneli.add(peliKenttäJaHUD, BorderLayout.CENTER);
        peliRuutuPaneli.revalidate();
        peliRuutuPaneli.repaint();

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
        peliAluePaneli.add(peliRuutuPaneli, BorderLayout.CENTER);

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

        kenttäKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JLabel[Peli.kentänKoko][Peli.kentänKoko];

        GrafiikanPäivitysSäie.huoneenGrafiikanLatausKäynnissä = true;
        //latausTausta.setVisible(true);
        peliKenttä.removeAll();
        peliKentänTaustaPaneli.removeAll();
        peliRuutuPaneli.setBackground(new Color(0, 0, 0));
        
        int kohteenSijX = 0;
        int kohteensijY = 0;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                kenttäKohteenKuvake[j][i] = new JLabel();
                kenttäKohteenKuvake[j][i].setName("kenttä_" + j + "_" + i);
                maastoKohteenKuvake[j][i] = new JLabel();
                maastoKohteenKuvake[j][i].setName("maasto_" + j + "_" + i);
                peliKenttä.setPreferredSize(new Dimension(Peli.kentänKoko * esineenKokoPx, Peli.kentänKoko * esineenKokoPx));
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
        pelaajanHyökkäysLabel = new JLabel();
        pelaajanHyökkäysLabel.setName("Pelaajan_hyökkäys");
        pelaajanHyökkäysLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 48, Pelaaja.sijY * pelaajanKokoPx, 16, 64);
        pelaajanHyökkäysLabel.setIcon(null);
        
        try {
            peliKenttä.add(pelaajaLabel, Integer.valueOf(4), 0);
            peliKenttä.add(pelaajanEsineLabel, Integer.valueOf(5), 0);
            peliKenttä.add(pelaajanHyökkäysLabel, Integer.valueOf(6), 0);
            scrollaavanPelikentänPaneeli.setBackground(new Color(0, 0, 0, 0));

            peliKentänTaustaPaneli.add(taustaLabel, Integer.valueOf(0), 0);
            peliKentänTaustaPaneli.add(scrollaavanPelikentänPaneeli, Integer.valueOf(1), 0);
            peliKentänTaustaPaneli.add(vuoropuhePaneli, Integer.valueOf(3), 0);
            peliKentänTaustaPaneli.add(alaPaneeli, Integer.valueOf(4), 0);
            peliKentänTaustaPaneli.add(lisäRuutuPaneli, Integer.valueOf(7), 0);
            peliKentänTaustaPaneli.add(pausePaneli, Integer.valueOf(8), 0);

            scrollaaPeliRuutua();
            
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (kenttäKohteenKuvake[j][i] != null) {
                        if (Peli.pelikenttä != null) {
                            if (Peli.pelikenttä.length > i && Peli.pelikenttä.length > j) {
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
                                peliKenttä.add(kenttäKohteenKuvake[j][i], Integer.valueOf(2), 0);
                                peliKenttä.add(maastoKohteenKuvake[j][i], Integer.valueOf(1), 0);
                            }
                        }
                        kohteenSijX += esineenKokoPx;
                    }
                }
                kohteenSijX = 0;
                kohteensijY += esineenKokoPx;
            }

            synchronized(Peli.entityLista) {
                if (Peli.entityLista != null) {
                    for (int i = 0; i < Peli.entityLista.size(); i++) {
                        Entity entity = Peli.entityLista.get(i);
                        entity.setName("entity" + i);
                        entity.setIcon(entity.annaKuvake());
                        entity.setBounds(entity.hitbox.getBounds());
                        peliKenttä.add(entity, Integer.valueOf(3), 0);
                    }
                }
            }
            latausTausta.setVisible(false);
            GrafiikanPäivitysSäie.huoneenGrafiikanLatausKäynnissä = false;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ongelma alkuikkunan luonnissa");
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {

        }
        catch (NullPointerException e) {
            System.out.println("ongelma alkuikkunan luonnissa");
            e.printStackTrace();
        }

        peliKenttä.revalidate();
        peliKenttä.repaint();
        peliKentänTaustaPaneli.revalidate();
        peliKentänTaustaPaneli.repaint();
    }

    public static void päivitäPeliRuutu() {
        try {
            //viimeisinKuvake = null;
            //ylätekstiHP.setText("HP: " + Pelaaja.hp);
            //ylätekstiKuparit.setText("Tölkkejä: " + Pelaaja.kuparit);
            hpMääräLabel.setText("" + Pelaaja.hp);
            rahaMääräLabel.setText("" + df.format(Pelaaja.raha) + "€");
            tölksMääräLabel.setText("" + Pelaaja.kuparit);
            if (Peli.pelikenttä != null && Peli.maastokenttä != null) {
                for (int i = 0; i < Peli.kentänKoko; i++) {
                    for (int j = 0; j < Peli.kentänKoko; j++) {
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
            pelaajaLabel.setBounds(Pelaaja.hitbox.getBounds());
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
                Image kuvake32 = kuvake64.getScaledInstance(32, 32, Image.SCALE_DEFAULT);
                pelaajanEsineenKuvake = new ImageIcon(kuvake32);
                pelaajanEsineLabel.setIcon(pelaajanEsineenKuvake);
            }
            else {
                pelaajanEsineLabel.setIcon(null);
            }
            switch (Pelaaja.keimonSuuntaVasenOikea) {
                case VASEN:
                    pelaajanHyökkäysLabel.setBounds((int)Pelaaja.hitbox.getMinX(), (int)Pelaaja.hitbox.getMinY(), 16, 64);
                    pelaajanHyökkäysLabel.setIcon(new ImageIcon("tiedostot/kuvat/entity/hyökkäys_sprite.png"));
                break;
                case OIKEA:
                    pelaajanHyökkäysLabel.setBounds((int)Pelaaja.hitbox.getMinX() + 48, (int)Pelaaja.hitbox.getMinY(), 16, 64);
                    pelaajanHyökkäysLabel.setIcon(new SkaalattavaKuvake("tiedostot/kuvat/entity/hyökkäys_sprite.png", SkaalattavaKuvake.Peilaus.PEILAA_X));
                break;
            }
            if (Pelaaja.hyökkäysAika > 0) pelaajanHyökkäysLabel.setVisible(true);
            else pelaajanHyökkäysLabel.setVisible(false);

            PääIkkuna.pelaajaSiirtyi = false;

            synchronized(Peli.entityLista) {
                if (Peli.entityLista != null) {
                    for (int i = 0; i < Peli.entityLista.size(); i++) {
                        Entity entity = Peli.entityLista.get(i);
                        entity.setBounds(entity.hitbox.getBounds());
                        entity.setIcon(entity.annaKuvake());
                    }
                }
            }
            peliKenttä.revalidate();
            peliKenttä.repaint();
            if (Peli.huone != null) {
                alueInfoLabel.setText(Peli.huone.annaAlue());
            }
        }
        catch (IndexOutOfBoundsException ioobe) {
            System.out.println("Ongelma ladatessa kuvaa objektille");
            ioobe.printStackTrace();
        }
        catch (NullPointerException npe) {
            System.out.println("Ongelma ruudunpäivityksessä");
            npe.printStackTrace();
        }
        catch (IllegalArgumentException iae) {
            System.out.println("Ongelma ruudunpäivityksessä");
            iae.printStackTrace();
        }

        peliKenttä.revalidate();
        peliKenttä.repaint();
        peliKentänTaustaPaneli.revalidate();
        peliKentänTaustaPaneli.repaint();
    }

    public static void scrollaaPeliRuutua() {
        //if (Peli.kentänKoko > 10 && Pelaaja.keimonState == KeimonState.JUOKSU) {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, peliKenttä);
            if (viewPort != null) {
                
                Rectangle view = viewPort.getViewRect();
                int peliRuudunX = (int)Pelaaja.hitbox.getX() - (scrollaavanPelikentänPaneeli.getBounds().width/2) + (esineenKokoPx/2);
                int peliRuudunY = (int)Pelaaja.hitbox.getY() - (scrollaavanPelikentänPaneeli.getBounds().height/2) + (esineenKokoPx/2);
                if (peliRuudunX > 0) {
                    view.x = peliRuudunX;
                }
                else {
                    view.x = 0;
                }
                if (peliRuudunY > 0) {
                    view.y = peliRuudunY;
                }
                else {
                    view.y = 0;
                }
                peliKenttä.scrollRectToVisible(view);
            }
        //}
    }

    static Random r = new Random();
    static int hajontaKerroin = (int)Pelaaja.känninVoimakkuusFloat;
    static int hajontaKerroinPienempi = hajontaKerroin/2;
    static int hajontaKerroinIsompi = (int)Pelaaja.känninVoimakkuusFloat+1;
    static int hajontaXKaikki = 0;
    static int hajontaYKaikki = 0;
    static int hajontaXPelaaja = 0;
    static int hajontaYPelaaja = 0;
    static int hajontaX = 0;
    static int hajontaY = 0;

    public static void luoKänniEfekti() {
        
        try {
            r = new Random();
            hajontaKerroin = (int)Pelaaja.känninVoimakkuusFloat;
            hajontaKerroinPienempi = hajontaKerroin/2;
            hajontaKerroinIsompi = (int)Pelaaja.känninVoimakkuusFloat+1;
            hajontaXKaikki = 0;
            hajontaYKaikki = 0;
            hajontaX = 0;
            hajontaY = 0;
            boolean tileLiikkuu = false;

            if (Pelaaja.känninVoimakkuusFloat > 0) {
                hajontaKerroinIsompi = (int)Pelaaja.känninVoimakkuusFloat+1;
                hajontaKerroin = (int)Pelaaja.känninVoimakkuusFloat;
                hajontaKerroinPienempi = hajontaKerroin/2;
                hajontaXKaikki = r.nextInt(-1 * hajontaKerroinIsompi + 1, hajontaKerroinIsompi);
                hajontaYKaikki = r.nextInt(-1 * hajontaKerroinIsompi + 1, hajontaKerroinIsompi);
                hajontaXPelaaja = r.nextInt(-1 * hajontaKerroinIsompi + 1, hajontaKerroinIsompi);
                hajontaYPelaaja = r.nextInt(-1 * hajontaKerroinIsompi + 1, hajontaKerroinIsompi);
                float tilenLiikeTodennäköisyys = r.nextFloat(0, 1);
                tileLiikkuu = tilenLiikeTodennäköisyys < (Pelaaja.känninVoimakkuusFloat/5) * (Pelaaja.känninVoimakkuusFloat/5);
            }
            
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (kenttäKohteenKuvake.length > i && kenttäKohteenKuvake.length > j) {
                        if (kenttäKohteenKuvake[j][i] != null) {
                            if (Peli.pelikenttä != null) {
                                if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                    if (hajontaKerroin > 0) {
                                        if (hajontaKerroinIsompi/2 > 0) {
                                            hajontaX = r.nextInt(-1 * hajontaKerroinIsompi/2 + 1, hajontaKerroinIsompi/2);
                                            hajontaY = r.nextInt(-1 * hajontaKerroinIsompi/2 + 1, hajontaKerroinIsompi/2);
                                        }
                                        if (tileLiikkuu) { 
                                            if ((int)kenttäKohteenKuvake[j][i].getBounds().getX() + hajontaX + hajontaXKaikki < (int)((j)*esineenKokoPx+Pelaaja.känninVoimakkuusFloat*6) && (int)kenttäKohteenKuvake[j][i].getBounds().getX() + hajontaX + hajontaXKaikki > (int)((j)*esineenKokoPx-Pelaaja.känninVoimakkuusFloat*6) && (int)kenttäKohteenKuvake[j][i].getBounds().getY() + hajontaY + hajontaYKaikki < (int)((i)*esineenKokoPx+Pelaaja.känninVoimakkuusFloat*6) && (int)kenttäKohteenKuvake[j][i].getBounds().getY() + hajontaY + hajontaYKaikki > (int)((i)*esineenKokoPx-Pelaaja.känninVoimakkuusFloat*6)) {
                                                kenttäKohteenKuvake[j][i].setBounds((int)kenttäKohteenKuvake[j][i].getBounds().getX() + hajontaX + hajontaXKaikki, (int)kenttäKohteenKuvake[j][i].getBounds().getY() + hajontaY + hajontaYKaikki, (int)kenttäKohteenKuvake[j][i].getBounds().getWidth(), (int)kenttäKohteenKuvake[j][i].getBounds().getHeight());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (maastoKohteenKuvake.length > i && maastoKohteenKuvake.length > j) {
                        if (maastoKohteenKuvake[j][i] != null) {
                            if (Peli.maastokenttä != null) {
                                if (Peli.maastokenttä[j][i] instanceof Maasto) {
                                    if (hajontaKerroin > 0) {
                                        if (hajontaKerroinPienempi > 0) {
                                            hajontaX = r.nextInt(-1 * hajontaKerroinPienempi + 1, hajontaKerroinPienempi);
                                            hajontaY = r.nextInt(-1 * hajontaKerroinPienempi + 1, hajontaKerroinPienempi);
                                        }
                                        if (tileLiikkuu) {
                                            maastoKohteenKuvake[j][i].setBounds((int)(j*esineenKokoPx) + hajontaX + hajontaXKaikki, (int)(i*esineenKokoPx) + hajontaY + hajontaYKaikki, (int)maastoKohteenKuvake[j][i].getBounds().getWidth(), (int)maastoKohteenKuvake[j][i].getBounds().getHeight());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Entity entity : Peli.entityLista) {
                if (hajontaKerroin > 0) {
                    if (hajontaKerroinIsompi/2 > 0) {
                        hajontaX = r.nextInt(-1 * hajontaKerroinIsompi*2 + 1, hajontaKerroinIsompi*2);
                        hajontaY = r.nextInt(-1 * hajontaKerroinIsompi*2 + 1, hajontaKerroinIsompi*2);
                    }
                    if (tileLiikkuu) {
                        entity.setBounds((int)entity.getBounds().getX() + hajontaX + hajontaXKaikki, (int)entity.getBounds().getY() + hajontaY + hajontaYKaikki, (int)entity.getBounds().getWidth(), (int)entity.getBounds().getHeight());
                    }
                }
            }
            if (tileLiikkuu) {
                pelaajaLabel.setBounds((int)pelaajaLabel.getBounds().getX() + hajontaXPelaaja + hajontaXKaikki, (int)pelaajaLabel.getBounds().getY() + hajontaYPelaaja + hajontaYKaikki, pelaajanKokoPx, pelaajanKokoPx);
            }
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Ongelma efektin luonnissa. Virheellinen kuvakelistan koko?");
            aioobe.printStackTrace();
            System.out.println(Peli.pelikenttä.length);
            System.out.println(kenttäKohteenKuvake.length);
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin vihollisten sijaintimuutos efektissä peruttiin (konkurrenssi-issue).");
            cme.printStackTrace();
        }
    }

    public static void nollaaKänniEfekti() {
        hajontaKerroin = (int)Pelaaja.känninVoimakkuusFloat;
        hajontaKerroinPienempi = hajontaKerroin/2;
        hajontaKerroinIsompi = (int)Pelaaja.känninVoimakkuusFloat+1;
        hajontaXKaikki = 0;
        hajontaYKaikki = 0;
        hajontaX = 0;
        hajontaY = 0;
        hajontaXPelaaja = 0;
        hajontaYPelaaja = 0;

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
        PääIkkuna.uudelleenpiirräKenttä = true;
    }

    public static void päivitäAmmusKenttä() {
        synchronized(Peli.entityLista) {
            try {
                if (Peli.entityLista != null) {
                    for (Component comp : peliKenttä.getComponents()) {
                        if (comp.getName() != null) {
                            if (comp.getName().contains("entity")) {
                                peliKenttä.remove(comp);
                            }
                        }
                    }
                    for (int i = 0; i < Peli.entityLista.size(); i++) {
                        Entity entity = Peli.entityLista.get(i);
                        entity.setName("entity" + i);
                        entity.setIcon(entity.annaKuvake());
                        entity.setBounds(entity.hitbox.getBounds());
                        peliKenttä.add(entity, Integer.valueOf(3), 0);
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {

            }
            catch (NullPointerException e) {
                System.out.println("ongelma alkuikkunan luonnissa");
                e.printStackTrace();
            }
        }
        peliKenttä.revalidate();
        peliKenttä.repaint();
    }

    public static void vaihdaTausta(ImageIcon tausta) {
        if (tausta == null) {
            taustaLabel.setVisible(false);
        }
        else {
            referenssiTaustaKuvake = tausta;
            taustaLabel.setIcon(tausta);
            taustaLabel.setVisible(true);
        }
    }

    public static void vaihdaTausta(KäännettäväKuvake tausta) {
        if (tausta == null) {
            taustaLabel.setVisible(false);
        }
        else {
            referenssiTaustaKuvake = tausta;
            taustaLabel.setIcon(tausta);
            taustaLabel.setVisible(true);
        }
    }

    public static void päivitäOstosPaneli() {
        if (ostosPaneli != null) {
            ostosEsineLabel = new JLabel[Pelaaja.ostosKori.size()];
            for (int i = 0; i < ostosEsineLabel.length; i++) {
                ostosEsineLabel[i] = new JLabel(Pelaaja.ostosKori.get(i).annaKuvake());
                ostosEsineLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
            }
            ostosEsineNimiLabel = new JLabel[Pelaaja.ostosKori.size()];
            for (int i = 0; i < ostosEsineNimiLabel.length; i++) {
                ostosEsineNimiLabel[i] = new JLabel(Pelaaja.ostosKori.get(i).annaNimi());
            }
            ostosPaneli.removeAll();
            ostosPaneli.setLayout(new GridLayout(ostosEsineLabel.length, 2));
            for (int i = 0; i < ostosEsineLabel.length; i++) {
                ostosPaneli.add(ostosEsineLabel[i]);
                ostosPaneli.add(ostosEsineNimiLabel[i]);
            }

            Pelaaja.ostostenHintaYhteensä = 0;
            for (Esine e : Pelaaja.ostosKori) {
                Pelaaja.ostostenHintaYhteensä += e.annaHinta();
            }
            ostoksetYhteensäLabel.setText("Ostokset yhteensä: " + df.format(Pelaaja.ostostenHintaYhteensä) + "€");

            ostosPaneli.revalidate();
            ostosPaneli.repaint();
        }
    }

    public static void keskitäPelikenttä(int kentänKokoTile, int panelinKokoPx) {
        int offset = 10;
        int koko = 640;
        switch (panelinKokoPx) {
            case 640:
                offset = (panelinKokoPx - kentänKokoTile * esineenKokoPx)/2 + 10;
                if (offset < 10) {
                    offset = 10;
                }
                koko = kentänKokoTile * esineenKokoPx;
                if (koko > 640) {
                    koko = 640;
                }
                scrollaavanPelikentänPaneeli.setBounds(offset, offset, koko, koko);
            break;
            case 960:
                offset = (panelinKokoPx - kentänKokoTile * esineenKokoPx)/2 + 15;
                if (offset < 15) {
                    offset = 15;
                }
                koko = kentänKokoTile * esineenKokoPx;
                if (koko > 960) {
                    koko = 960;
                }
            break;
            default:
                System.out.println("skaalauksen koko ei tuettu");
            break;
        }
        scrollaavanPelikentänPaneeli.setBounds(offset, offset, koko, koko);
    }
}
