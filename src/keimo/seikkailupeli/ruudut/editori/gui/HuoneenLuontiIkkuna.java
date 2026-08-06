package keimo.seikkailupeli.ruudut.editori.gui;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.huone.Huone;
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

public class HuoneenLuontiIkkuna {
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_popup_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.5f, 0.5f, 0, 0, pohjaTekstuuri);

    private static Teksti otsikkoTeksti = new Teksti("Muokkaa objektia", Väri.white, 400, 48);
    private static Teksti tiedotTeksti = new Teksti("Tulossa myöhemmin", Väri.gray, 1600, 400);
    private static LabelKomponentti otsikko = new LabelKomponentti(0.4f, 0.1f, 0, 0.4f, otsikkoTeksti);
    private static LabelKomponentti tiedot = new LabelKomponentti(0.4f, 0.4f, 0, -0.2f, tiedotTeksti);

    private static Nappi okNappi = new Nappi(0.166f, 0.05f, 0, -0.4f, Assets.annaTekstuuri("editori_nappi_ok"));

    private static Frame ikkuna;
    private static TextField huoneenKokoTeksti;
    private static TextField huoneenNimiTeksti;
    private static TextField huoneenAlueTeksti;
    private static TextField huoneenTaustaTeksti;
    private static Choice huoneenMusaValinta;
    private static TextField huoneenTarinaDialogiValinta;
    private static TextField huoneenTavoiteValinta;

    public static void luoIkkuna(int luontiId) {
        if (ikkuna == null || (ikkuna != null && !ikkuna.isVisible())) {
            ikkuna = new Frame();

            Label huomLabel = new Label("Väliaikainen huoneenluonti-ikkuna");
            huomLabel.setForeground(Color.red);

            Panel yläPaneeli = new Panel();
            yläPaneeli.add(huomLabel);

            Label huoneenIdLabel = new Label("Huoneen ID");
            Label huoneenKokoLabel = new Label("Huoneen koko");
            Label huoneenNimiLabel = new Label("Huoneen nimi");
            Label huoneenAlueLabel = new Label("Huoneen alue");
            Label huoneenTaustaLabel = new Label("Huoneen tausta");
            Label huoneenMusaLabel = new Label("Huoneen musiikki");
            Label huoneenTarinaDialogiLabel = new Label("Huoneen tarinadialogi");
            Label huoneenTavoiteLabel = new Label("Huoneen tavoite");
            
            Panel lomakeVasenPaneeli = new Panel();
            lomakeVasenPaneeli.setLayout(new GridLayout(8, 1));
            lomakeVasenPaneeli.add(huoneenIdLabel);
            lomakeVasenPaneeli.add(huoneenKokoLabel);
            lomakeVasenPaneeli.add(huoneenNimiLabel);
            lomakeVasenPaneeli.add(huoneenAlueLabel);
            lomakeVasenPaneeli.add(huoneenTaustaLabel);
            lomakeVasenPaneeli.add(huoneenMusaLabel);
            lomakeVasenPaneeli.add(huoneenTarinaDialogiLabel);
            lomakeVasenPaneeli.add(huoneenTavoiteLabel);

            TextField huoneenIdTeksti = new TextField("" + luontiId);
            huoneenIdTeksti.setPreferredSize(new Dimension(200, 50));
            huoneenIdTeksti.setEditable(false);
            huoneenKokoTeksti = new TextField("" + 10);
            huoneenNimiTeksti = new TextField("Uusi huone");
            huoneenAlueTeksti = new TextField("Uusi alue");
            huoneenTaustaTeksti = new TextField("tausta_puisto.png");
            huoneenMusaValinta = new Choice();
            huoneenMusaValinta.add("puisto");
            huoneenMusaValinta.add("overworld");
            huoneenMusaValinta.add("metsä");
            huoneenMusaValinta.add("koti");
            huoneenMusaValinta.add("kauppa");
            huoneenMusaValinta.add("baari");
            huoneenMusaValinta.add("temppeli");
            huoneenMusaValinta.add("boss");
            huoneenMusaValinta.add("kuu");
            huoneenMusaValinta.add("tarina");
            huoneenMusaValinta.add("valikko");
            huoneenTarinaDialogiValinta = new TextField();
            huoneenTavoiteValinta = new TextField();

            Panel lomakeOikeaPaneeli = new Panel();
            lomakeOikeaPaneeli.setLayout(new GridLayout(8, 1));
            lomakeOikeaPaneeli.add(huoneenIdTeksti);
            lomakeOikeaPaneeli.add(huoneenKokoTeksti);
            lomakeOikeaPaneeli.add(huoneenNimiTeksti);
            lomakeOikeaPaneeli.add(huoneenAlueTeksti);
            lomakeOikeaPaneeli.add(huoneenTaustaTeksti);
            lomakeOikeaPaneeli.add(huoneenMusaValinta);
            lomakeOikeaPaneeli.add(huoneenTarinaDialogiValinta);
            lomakeOikeaPaneeli.add(huoneenTavoiteValinta);

            Button okNappi = new Button("OK");
            okNappi.addActionListener(e -> {
                luoUusiHuone(luontiId);
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
            ikkuna.setTitle("Luo huone");
            ikkuna.setSize(new Dimension(400, 300));
            ikkuna.setLocationRelativeTo(null);
            ikkuna.setResizable(false);
            ikkuna.setAlwaysOnTop(true);
            ikkuna.setVisible(true);
            ikkuna.requestFocus();
        }
    }

    public static void sulje() {
        if (ikkuna != null) {
            ikkuna.dispose();
        }
    }

    private static void luoUusiHuone(int luontiId) {
        try {
            int luontiKoko = Integer.parseInt(huoneenKokoTeksti.getText());
            if (luontiKoko < 1) throw new NumberFormatException();
            String luontiNimi = huoneenNimiTeksti.getText();
            String luontiAlue = huoneenAlueTeksti.getText();
            String luontiTausta = huoneenTaustaTeksti.getText();
            String luontiMusa = huoneenMusaValinta.getSelectedItem();
            String luontiDialogi = huoneenTarinaDialogiValinta.getText();
            String luontiTavoite = huoneenTavoiteValinta.getText();
            EditoriRuutu.editorinHuoneKartta.put(luontiId, new Huone(luontiId, luontiKoko, luontiNimi, luontiTausta, luontiAlue, null, null, null, luontiMusa, luontiDialogi, luontiTavoite));
            if (ikkuna != null) ikkuna.dispose();
            EditoriRuutu.lataaHuone(luontiId);
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
            EditoriRuutu.avaaHuoneenLuontiIkkuna(false);
            EditoriRuutu.estäVahinkoPainallukset = true;
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        otsikkoTeksti.päivitäTeksti("Luo huone");
        tiedotTeksti.päivitäTeksti("Huoneen luonti ja poistaminen\ntulossa myöhemmin.\nKäytä vanhaa editoria\ntoistaiseksi.");
        pohja.renderöi(shader, window);
        otsikko.renderöi(shader, window);
        tiedot.renderöi(shader, window);
        okNappi.renderöi(shader, window);
    }
}
