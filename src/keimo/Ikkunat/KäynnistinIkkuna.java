package keimo.Ikkunat;

import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.State;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PelinAsetukset;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.HuoneEditori.HuoneLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.TarinaRuutu;
import keimo.Säikeet.GrafiikanPäivitysSäie;
import keimo.Säikeet.PeliSäie;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.Utility.KeimoFontit;
import keimo.keimoEngine.KeimoEngine;
import keimo.kenttäkohteet.KenttäKohde;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;

public class KäynnistinIkkuna {

    public static boolean glKäytössä = false;

    static Frame ikkuna;
    static Panel yläPaneeli, alaPaneeli;
    static CheckboxGroup moottorinValinta;

    public static void luoKäynnistinIkkuna() {
        ikkuna = new Frame("Keimo Launcher");
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setSize(new Dimension(400, 180));
        ikkuna.setLocationRelativeTo(null);
        try {
            ikkuna.setIconImage(ImageIO.read(new File("tiedostot/kuvat/pelaaja_og.png")));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(ikkuna, "Aloitusikkunaa ei voitu ladata. Peli on todennäköisesti yritetty käynnistää väärillä java-argumenteilla.\nTämä virhe voi tulla, jos peli on yritetty käynnistää jar-tiedostosta suoraan.\n\nVersio 0.8.2:sta eteenpäin mukana tulee exe-tiedosto, joka käynnistää pelin automaattisesti oikeilla argumenteilla.", "Virhe ladatessa peliä", JOptionPane.ERROR_MESSAGE);
        }
        ikkuna.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                ikkuna.dispose();
             }
        });

        yläPaneeli = new Panel();
        alaPaneeli = new Panel();
        alaPaneeli.setLayout(new BorderLayout());

        moottorinValinta = new CheckboxGroup();

        Label valintaLabel = new Label("Valitse pelimoottori");
        Checkbox keimoEngineAlpa = new Checkbox("KeimoEngine Alpha (Suositeltu)", moottorinValinta, true);
        Checkbox vanhaEngine = new Checkbox("Vanha pelimoottori (Tullaan poistamaan tulevaisuudessa)", moottorinValinta, false);
        yläPaneeli.add(valintaLabel);
        yläPaneeli.add(keimoEngineAlpa);
        yläPaneeli.add(vanhaEngine);
        yläPaneeli.setLayout(new BoxLayout(yläPaneeli, BoxLayout.Y_AXIS));

        Button käynnistysNappi = new Button("Käynnistä peli");
        käynnistysNappi.setPreferredSize(new Dimension(300, 50));
        käynnistysNappi.addActionListener(e -> {
            käynnistäPeli(keimoEngineAlpa.getState());
            ikkuna.dispose();
        });
        alaPaneeli.add(käynnistysNappi, BorderLayout.CENTER);

        ikkuna.add(yläPaneeli, BorderLayout.NORTH);
        ikkuna.add(alaPaneeli, BorderLayout.SOUTH);
        ikkuna.setVisible(true);
    }

    private static void käynnistäPeli(boolean uusiEngine) {
        if (uusiEngine) {
            Thread latausThread = new Thread() {
                public void run() {
                    try {
                        Peli.kentänKoko = TarkistettavatArvot.uusiKentänKoko;
                        Peli.kentänYläraja = Peli.kentänAlaraja + Peli.kentänKoko - 1;
                        Peli.pause = true;
                        Peli.peliKäynnissä = false;
                        TarkistettavatArvot.nollaa();
                        //new Peli();
                        //Keimo3D.launchKeimo3D();
                        //throw new Exception();
                        luoPeli();
                    }
                    catch (Exception e) {
                        LatausIkkuna.suljeLatausIkkuna();
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String sStackTrace = sw.toString();
                        int virheValinta = CustomViestiIkkunat.SuljeVirheenJälkeen.näytäDialogi(sStackTrace);
                        if (virheValinta == 1) {
                            System.exit(0);
                        }
                        else {
                            käynnistäPeli(true);
                        }
                    }
                }
            };
            latausThread.start();
        }
        else {
            LatausIkkuna.palautaLatausIkkuna();
            Thread latausThread = new Thread() {
                public void run() {
                    try {
                        Peli.kentänKoko = TarkistettavatArvot.uusiKentänKoko;
                        Peli.kentänYläraja = Peli.kentänAlaraja + Peli.kentänKoko - 1;
                        Peli.pause = true;
                        Peli.peliKäynnissä = false;
                        TarkistettavatArvot.nollaa();
                        //new Peli();
                        //Keimo3D.launchKeimo3D();
                        //throw new Exception();
                        luoPeliVanha();
                    }
                    catch (Exception e) {
                        LatausIkkuna.suljeLatausIkkuna();
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String sStackTrace = sw.toString();
                        int virheValinta = CustomViestiIkkunat.SuljeVirheenJälkeen.näytäDialogi(sStackTrace);
                        if (virheValinta == 1) {
                            System.exit(0);
                        }
                        else {
                            LatausIkkuna.luoLatausIkkuna();
                            käynnistäPeli(false);
                        }
                    }
                }
            };
            latausThread.start();
        }
    }

    public static void luoPeli() {
        Peli.legacy = false;
        Peli.latausValmis = false;
        LatausIkkuna.päivitäLatausTeksti("Alustetaan peli...");
        PeliSäie.kulunutAika = 0;
        Peli.aikaReferenssi = System.nanoTime();
        Peli.peliAloitettu = false;
        Peli.peliLäpäisty = false;
        Peli.pauseDialogi = false;
        Peli.valintaDialogi = false;
        Peli.ajastinPysäytetty = false;
        Peli.uusiHuone = 0;
        Peli.huoneVaihdettava = true;

        LatausIkkuna.päivitäLatausTeksti("Ladataan KeimoEngine...");
        Peli.engine = new KeimoEngine();
        Peli.engine.start();
        while (!Peli.engine.glKäynnistetty) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        LatausIkkuna.päivitäLatausTeksti("Ladataan pelaajaa...");
        Peli.esineValInt = 0;
        Peli.valittuEsine = null;
        Peli.p = new Pelaaja();
        if (Pelaaja.sijX < Peli.kentänKoko && Pelaaja.sijY < Peli.kentänKoko) {
            Pelaaja.teleport(Pelaaja.sijX, Pelaaja.sijY);
        }
        else {
            Pelaaja.teleport(0, 0);
        }
        TarkistettavatArvot.pelinLoppuSyy = null;
    }

    public static void luoPeliVanha() {
        Peli.legacy = true;
        Peli.latausValmis = false;
        LatausIkkuna.päivitäLatausTeksti("Alustetaan peli...");
        PeliSäie.kulunutAika = 0;
        Peli.aikaReferenssi = System.nanoTime();
        Peli.peliAloitettu = false;
        Peli.peliLäpäisty = false;
        Peli.pauseDialogi = false;
        Peli.valintaDialogi = false;
        Peli.ajastinPysäytetty = false;
        Peli.uusiHuone = 0;
        Peli.huoneVaihdettava = true;

        LatausIkkuna.päivitäLatausTeksti("Ladataan dialogeja...");
        TavoiteLista.luoPääTavoiteLista();
        TavoiteLista.luoTavoiteLista();
        //VuoropuheDialogit.luoVakioVuoropuheDialogit();
        TarinaPätkä.nollaaTarinaId();
        KeimoFontit.rekisteröiFontit();

        LatausIkkuna.päivitäLatausTeksti("Ladataan huoneita...");
        KenttäKohde.nollaaObjektiId();
        HuoneLista.luoVakioHuoneKarttaTiedostosta();
        HuoneLista.lataaReferenssiHuonekartta();
        if (Peli.huoneKartta != null) {
            if (Peli.huoneKartta.get(0) != null) {
                Peli.muutaKentänKokoa(Peli.huoneKartta.get(0).annaKoko());
            }
        }
        
        LatausIkkuna.päivitäLatausTeksti("Ladataan pelaajaa...");
        Peli.esineValInt = 0;
        Peli.valittuEsine = null;
        Peli.p = new Pelaaja();
        if (Pelaaja.sijX < Peli.kentänKoko && Pelaaja.sijY < Peli.kentänKoko) {
            Pelaaja.teleport(Pelaaja.sijX, Pelaaja.sijY);
        }
        else {
            Pelaaja.teleport(0, 0);
        }
        TarkistettavatArvot.pelinLoppuSyy = null;

        LatausIkkuna.päivitäLatausTeksti("Ladataan grafiikkaa...");
        
        if (PääIkkuna.ikkuna != null && PääIkkuna.pääPaneeli != null) {
            PääIkkuna.ikkuna.remove(PääIkkuna.pääPaneeli);
        }
        PääIkkuna.luoPääikkuna();
        
        PääIkkuna.ikkuna.addKeyListener(Peli.nk);
        PääIkkuna.vaatiiPäivityksen = true;
        PeliRuutu.luoPeliRuudunGUI();

        LatausIkkuna.päivitäLatausTeksti("Ladataan ääniä...");
        ÄänentoistamisSäie.suljeMusa();
        ÄänentoistamisSäie.lataaÄänet();

        LatausIkkuna.päivitäLatausTeksti("Käynnistetään säikeitä...");
        if (Peli.grafiikkaSäie.getState() == State.NEW || Peli.grafiikkaSäie.getState() == State.TERMINATED) {
            Peli.grafiikkaSäie = new GrafiikanPäivitysSäie();
            Peli.grafiikkaSäie.setName("Grafiikkasäie");
            if (PelinAsetukset.ajoitus != AjoitusMuoto.ERITTÄIN_NOPEA) {
                Peli.grafiikkaSäie.start();
            }
        }
        if (Peli.peliSäie.getState() == State.NEW || Peli.peliSäie.getState() == State.TERMINATED) {
            Peli.peliSäie = new PeliSäie();
            Peli.peliSäie.setName("Pelisäie");
            Peli.peliSäie.start();
        }
        PääIkkuna.lataaRuutu("tarinaruutu");
        TarinaRuutu.tarinaPaneli.requestFocus();
        
        LatausIkkuna.päivitäLatausTeksti("Valmis.");
        try {
            Thread.sleep(25);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        LatausIkkuna.suljeLatausIkkuna();
    }
}
