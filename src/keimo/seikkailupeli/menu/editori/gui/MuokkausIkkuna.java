package keimo.seikkailupeli.menu.editori.gui;

import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;

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

import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_messageBox;

public class MuokkausIkkuna {

    private static Tekstuuri pohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png");
    private static StaattinenKomponentti pohja = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, pohjaTekstuuri);

    private static Teksti otsikkoTeksti = new Teksti("Muokkaa objektia", Color.white, 300, 48);
    private static Teksti tiedotTeksti = new Teksti("Tulossa myöhemmin", Color.gray, 800, 200);
    private static StaattinenKomponentti otsikko = new StaattinenKomponentti(0.4f, 0.1f, 0, 0.4f, otsikkoTeksti);
    private static StaattinenKomponentti tiedot = new StaattinenKomponentti(0.4f, 0.4f, 0, -0.2f, tiedotTeksti);

    private static Nappi okNappi = new Nappi(0.166f, 0.05f, 0, -0.4f, new Tekstuuri("tiedostot/kuvat/editori/ok_nappi.png"));

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

            Label huomLabel = new Label("Väliaikainen muokkausikkuna");
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

            Huone h = EditoriRuutu.editorinHuoneKartta.get(luontiId);
            TextField huoneenIdTeksti = new TextField("" + luontiId);
            huoneenIdTeksti.setPreferredSize(new Dimension(200, 50));
            huoneenIdTeksti.setEditable(false);
            huoneenKokoTeksti = new TextField("" + h.annaKoko());
            huoneenKokoTeksti.setPreferredSize(new Dimension(200, 50));
            huoneenKokoTeksti.setEditable(false);
            huoneenNimiTeksti = new TextField("" + h.annaNimi());
            huoneenAlueTeksti = new TextField("" + h.annaAlue());
            huoneenTaustaTeksti = new TextField("" + h.annaTaustanPolku());
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
            huoneenMusaValinta.select("" + h.annaHuoneenMusa());
            huoneenTarinaDialogiValinta = new TextField(h.annaTarinaRuudunTunniste());
            huoneenTavoiteValinta = new TextField(h.annaVaaditunTavoitteenTunniste());

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
                tallennaMuutokset(luontiId);
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
            ikkuna.setTitle("Muokkaa huonetta");
            ikkuna.setSize(new Dimension(400, 300));
            ikkuna.setLocationRelativeTo(null);
            ikkuna.setResizable(false);
            ikkuna.setAlwaysOnTop(true);
            ikkuna.setVisible(true);
            ikkuna.requestFocus();
        }
    }

    private static void tallennaMuutokset(int luontiId) {
        try {
            //int luontiKoko = Integer.parseInt(huoneenKokoTeksti.getText());
            //if (luontiKoko < 1) throw new NumberFormatException();
            String luontiNimi = huoneenNimiTeksti.getText();
            String luontiAlue = huoneenAlueTeksti.getText();
            String luontiTausta = huoneenTaustaTeksti.getText();
            String luontiMusa =huoneenMusaValinta.getSelectedItem();
            String luontiDialogi = huoneenTarinaDialogiValinta.getText();
            String luontiTavoite = huoneenTavoiteValinta.getText();
            Huone h = EditoriRuutu.editorinHuoneKartta.get(luontiId);
            h.päivitäNimiJaAlue(luontiNimi, luontiAlue);
            h.päivitäTausta(luontiTausta);
            h.päivitäMusa(luontiMusa);
            h.päivitäAlkudialogi(luontiDialogi);
            if (ikkuna != null) ikkuna.dispose();
            EditoriRuutu.lataaHuone(luontiId);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            tinyfd_messageBox("Virhe huoneen luonnissa", "Virhe huoneen luonnissa. Tarkista, että koko on positiivinen kokonaisluku.", "ok", "error", false);
        }
        catch (Exception e) {
            e.printStackTrace();
            tinyfd_messageBox("Virhe huoneen luonnissa", "Virhe huoneen luonnissa. Tarkista syötearvot.", "ok", "error", false);
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
