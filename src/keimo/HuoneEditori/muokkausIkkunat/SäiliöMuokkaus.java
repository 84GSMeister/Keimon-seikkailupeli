package keimo.HuoneEditori.muokkausIkkunat;

import keimo.*;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.Utility.Käännettävä.Suunta;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.kiintopiste.KauppaHylly;
import keimo.kenttäkohteet.kiintopiste.Kirstu;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class SäiliöMuokkaus {

    static final int ikkunanLeveys = 420;
    static int ikkunanKorkeus = 180;
    static JFrame muokkausIkkuna;
    static String[] tekstit;
    static int valintojenMäärä;
    static JPanel paneli;
    static JLabel[] tekstiLabelit;
    static JTextField[] tekstiKentät;
    static JComboBox<String> huoneValikko;
    static String[] huoneidenNimet;
    static ArrayList<Integer> toimivatHuoneIndeksit = new ArrayList<Integer>();
    static JComboBox<Suunta> suuntaValinta;
    static JTextField kohdeRuudunObjekti;
    static JComboBox<String> sisältöValinta;
    static JTextField triggeriLista;
    static JCheckBox valintaLaatikko;

    static void hyväksyMuutokset(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        try {
            switch (muokattavaKohde.annaNimi()) {

                case "Kauppahylly":
                    KauppaHylly kauppaHylly = (KauppaHylly)HuoneEditoriIkkuna.objektiKenttä[sijX][sijY];
                    if (sisältöValinta.getSelectedItem().toString().startsWith("Kaasusytytin")) {
                        ArrayList<String> ominaisuusLista = new ArrayList<>();
                        ominaisuusLista.add("toimivuus=toimiva");
                        kauppaHylly.asetaSisältö(sisältöValinta.getSelectedItem().toString(), ominaisuusLista);
                    }
                    else {
                        kauppaHylly.asetaSisältö(sisältöValinta.getSelectedItem().toString(), null);
                    }
                    kauppaHylly.lisäOminaisuuksia = true;
                    kauppaHylly.päivitäLisäOminaisuudet();
                    kauppaHylly.päivitäTiedot();
                    //kauppaHylly.päivitäKuvanAsento();
                    HuoneEditoriIkkuna.objektiKenttä[sijX][sijY] = kauppaHylly;
                    muokkausIkkuna.dispose();
                break;
                
                case "Kirstu":
                    Kirstu kirstu = (Kirstu)HuoneEditoriIkkuna.objektiKenttä[sijX][sijY];
                    if (sisältöValinta.getSelectedItem().toString().startsWith("Kaasusytytin")) {
                        ArrayList<String> ominaisuusLista = new ArrayList<>();
                        ominaisuusLista.add("toimivuus=toimiva");
                        kirstu.asetaSisältö(sisältöValinta.getSelectedItem().toString(), ominaisuusLista);
                    }
                    else {
                        kirstu.asetaSisältö(sisältöValinta.getSelectedItem().toString(), null);
                    }
                    kirstu.lisäOminaisuuksia = true;
                    kirstu.päivitäLisäOminaisuudet();
                    HuoneEditoriIkkuna.objektiKenttä[sijX][sijY] = kirstu;
                    muokkausIkkuna.dispose();
                break;
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "", "häire", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ei voitu tallentaa muutoksia, koska objektia ei löytynyt.\n\nTämä aiheutuu todennäköisesti siitä, että kohdeobjekti on eri huoneessa, kuin tällä hetkellä editorissa auki oleva huone.", "häire", JOptionPane.ERROR_MESSAGE);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virheellinen syöte. Vain kokonaisluvut kelpaavat.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
        }
    }

    static JComboBox<String> luoHuoneenNimiLista() {
        int huoneListanKoko = HuoneEditoriIkkuna.huoneKartta.size();
        int huoneListanSuurin = Collections.max(HuoneEditoriIkkuna.huoneKartta.keySet());
        huoneidenNimet = new String[huoneListanKoko];
        int toimivatHuoneet = 0;
        
        try {
            for (int i = 0; i < huoneListanSuurin + 1; i++) {
                if (Peli.huoneKartta.get(i) == null) {
                    //System.out.println("Huonetta " + i + " ei löytynyt.");
                    continue;
                }
                else {
                    huoneidenNimet[toimivatHuoneet] = HuoneEditoriIkkuna.huoneKartta.get(i).annaNimi() + " (" + HuoneEditoriIkkuna.huoneKartta.get(i).annaId() + ")";
                    toimivatHuoneIndeksit.add(i);
                    toimivatHuoneet++;
                    //System.out.println("Huone " + i + ": " + huoneKartta.get(i).annaNimi() + ", ID: " + huoneKartta.get(i).annaId());
                }
            }
            huoneValikko = new JComboBox<String>(huoneidenNimet);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen huonelista.\n\nHäire sovelluksessa.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            System.out.println("Ohitetaan tyhjät indeksit");
        }
        return huoneValikko;
    }

    static int warpKohdeHuone;
    static int warpKohdeX;
    static int warpKohdeY;

    public static void luoSäiliönMuokkausIkkuna(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        
        if (muokattavaKohde instanceof Kirstu || muokattavaKohde instanceof KauppaHylly) {
            valintojenMäärä = 1;
            tekstit = new String[valintojenMäärä];
            tekstit[0] = "Valitse sisältö";
        }

        paneli = new JPanel(new SpringLayout());
        tekstiLabelit = new JLabel[valintojenMäärä];
        tekstiKentät = new JTextField[valintojenMäärä];

        switch (muokattavaKohde.annaNimi()) {

            case "Kauppahylly":
                tekstiLabelit[0] = new JLabel(tekstit[0]);
                paneli.add(tekstiLabelit[0]);

                KauppaHylly kauppaHylly = (KauppaHylly)muokattavaKohde;
                sisältöValinta = new JComboBox<String>(HuoneEditoriIkkuna.esineLista);
                sisältöValinta.setSelectedItem(kauppaHylly.annaSisältö());
                paneli.add(sisältöValinta);
            break;

            case "Kirstu":
                tekstiLabelit[0] = new JLabel(tekstit[0]);
                paneli.add(tekstiLabelit[0]);

                Kirstu kirstu = (Kirstu)muokattavaKohde;
                sisältöValinta = new JComboBox<String>(HuoneEditoriIkkuna.esineLista);
                sisältöValinta.setSelectedItem(kirstu.annaSisältö());
                paneli.add(sisältöValinta);
            break;

            default:
            break;
        }
        JButton okNappi = new JButton("Aseta");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    hyväksyMuutokset(sijX, sijY, muokattavaKohde);
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    muokkausIkkuna.dispose();
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);
        paneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä + 1, 2, 6, 6, 6, 6);
        ikkunanKorkeus = valintojenMäärä * 30 + 70;

        muokkausIkkuna = new JFrame();
        muokkausIkkuna.setTitle("Muokkaa objektia: " + muokattavaKohde.annaNimi());
        muokkausIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        muokkausIkkuna.setBounds(100, 50, ikkunanLeveys, ikkunanKorkeus);
        muokkausIkkuna.setLayout(new BorderLayout());
        muokkausIkkuna.setVisible(true);
        muokkausIkkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        muokkausIkkuna.add(paneli, BorderLayout.CENTER);
        muokkausIkkuna.revalidate();
        muokkausIkkuna.repaint();
    }
}

