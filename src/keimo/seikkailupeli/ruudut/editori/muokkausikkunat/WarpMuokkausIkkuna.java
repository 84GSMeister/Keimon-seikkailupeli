package keimo.seikkailupeli.ruudut.editori.muokkausikkunat;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Suunnallinen;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class WarpMuokkausIkkuna {
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_popup_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.5f, 0.5f, 0, 0, pohjaTekstuuri);

    private static Teksti otsikkoTeksti = new Teksti("Muokkaa objektia", Väri.white, 300, 48);
    private static Teksti tiedotTeksti = new Teksti("Tulossa myöhemmin", Väri.gray, 800, 200);
    private static LabelKomponentti otsikko = new LabelKomponentti(0.4f, 0.1f, 0, 0.4f, otsikkoTeksti);
    private static LabelKomponentti tiedot = new LabelKomponentti(0.4f, 0.4f, 0, -0.2f, tiedotTeksti);

    private static Nappi okNappi = new Nappi(0.166f, 0.05f, 0, -0.4f, Assets.annaTekstuuri("editori_nappi_ok"));

    private static Frame ikkuna;
    private static TextField kohdehuoneTeksti;
    private static TextField kohdeXTeksti;
    private static TextField kohdeYTeksti;
    private static Choice suuntaValinta;

    public static void luoIkkuna(Warp warp) {
        if (ikkuna == null || (ikkuna != null && !ikkuna.isVisible())) {
            ikkuna = new Frame();

            Label huomLabel = new Label("Väliaikainen warp-muokkausikkuna");
            huomLabel.setForeground(Color.red);

            Panel yläPaneeli = new Panel();
            yläPaneeli.add(huomLabel);

            Label kohdehuoneLabel = new Label("Kohdehuone");
            Label kohdeXLabel = new Label("Kohteen X-ruutu");
            Label kohdeYLabel = new Label("Kohteen Y-ruutu");
            Label suuntaLabel = new Label("Suunta");
            
            Panel lomakeVasenPaneeli = new Panel();
            lomakeVasenPaneeli.setLayout(new GridLayout(4, 1));
            lomakeVasenPaneeli.add(kohdehuoneLabel);
            lomakeVasenPaneeli.add(kohdeXLabel);
            lomakeVasenPaneeli.add(kohdeYLabel);
            lomakeVasenPaneeli.add(suuntaLabel);

            kohdehuoneTeksti = new TextField("" + warp.annaKohdeHuone());
            kohdehuoneTeksti.setPreferredSize(new Dimension(200, 50));
            kohdeXTeksti = new TextField("" + warp.annaKohdeRuutuX());
            kohdeXTeksti.setPreferredSize(new Dimension(200, 50));
            kohdeYTeksti = new TextField("" + warp.annaKohdeRuutuY());
            kohdeYTeksti.setPreferredSize(new Dimension(200, 50));
            suuntaValinta = new Choice();
            suuntaValinta.add("Vasen");
            suuntaValinta.add("Oikea");
            suuntaValinta.add("Alas");
            suuntaValinta.add("Ylös");
            suuntaValinta.select("" + warp.annaSuunta());
            suuntaValinta.setPreferredSize(new Dimension(200, 50));

            Panel lomakeOikeaPaneeli = new Panel();
            lomakeOikeaPaneeli.setLayout(new GridLayout(4, 1));
            lomakeOikeaPaneeli.add(kohdehuoneTeksti);
            lomakeOikeaPaneeli.add(kohdeXTeksti);
            lomakeOikeaPaneeli.add(kohdeYTeksti);
            lomakeOikeaPaneeli.add(suuntaValinta);

            Button okNappi = new Button("OK");
            okNappi.addActionListener(e -> {
                tallennaMuutokset(warp);
            });
            Button peruutaNappi = new Button("Peruuta");
            peruutaNappi.addActionListener(e -> {
                ikkuna.dispose();
            });

            Panel nappiPaneeli = new Panel();
            nappiPaneeli.setLayout(new FlowLayout());
            nappiPaneeli.add(okNappi);
            nappiPaneeli.add(peruutaNappi);

            ikkuna.setLayout(new BorderLayout());
            ikkuna.add(yläPaneeli, BorderLayout.NORTH);
            ikkuna.add(lomakeVasenPaneeli, BorderLayout.WEST);
            ikkuna.add(lomakeOikeaPaneeli, BorderLayout.EAST);
            ikkuna.add(nappiPaneeli, BorderLayout.SOUTH);
            ikkuna.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        ikkuna.dispose();
                    }
                }
            );
            ikkuna.setTitle("Muokkaa objektia");
            ikkuna.setSize(new Dimension(400, 200));
            ikkuna.setLocationRelativeTo(null);
            ikkuna.setResizable(false);
            ikkuna.setAlwaysOnTop(true);
            ikkuna.setVisible(true);
            ikkuna.requestFocus();
        }
    }

    private static void tallennaMuutokset(Warp warp) {
        try {
            int luontiKohdeHuone = Integer.parseInt(kohdehuoneTeksti.getText());
            int luontiKohdeX = Integer.parseInt(kohdeXTeksti.getText());
            int luontiKohdeY = Integer.parseInt(kohdeYTeksti.getText());
            Suunta suunta = Suunnallinen.haeSuunta(suuntaValinta.getSelectedItem());
            warp.asetaKohdeHuone(luontiKohdeHuone);
            warp.asetaKohdeRuudut(luontiKohdeX, luontiKohdeY);
            warp.asetaSuunta(suunta);
            ArrayList<String> lisäOminaisuudet = new ArrayList<>();
            lisäOminaisuudet.add("kohdehuone=" + luontiKohdeHuone);
            lisäOminaisuudet.add("kohderuutuX=" + luontiKohdeX);
            lisäOminaisuudet.add("kohderuutuY=" + luontiKohdeY);
            lisäOminaisuudet.add("suunta=" + suunta);
            warp.päivitäLisäOminaisuudet();
            if (ikkuna != null) ikkuna.dispose();
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            DialogiIkkunat.viestiIkkuna("Virhe huoneen luonnissa", "Virhe huoneen luonnissa. Tarkista, että koko on positiivinen kokonaisluku.", "ok", "error", false);
        }
        catch (Exception e) {
            e.printStackTrace();
            DialogiIkkunat.viestiIkkuna("Virhe huoneen luonnissa", "Virhe huoneen luonnissa. Tarkista syötearvot.", "ok", "error", false);
        }
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        okNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaMuokkausIkkuna(false);
            EditoriRuutu.estäVahinkoPainallukset = true;
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        otsikkoTeksti.päivitäTeksti("Muokkaa");
        tiedotTeksti.päivitäTeksti("Tulossa myöhemmin");
        pohja.renderöi(shader, window);
        otsikko.renderöi(shader, window);
        tiedot.renderöi(shader, window);
        okNappi.renderöi(shader, window);
    }
}
