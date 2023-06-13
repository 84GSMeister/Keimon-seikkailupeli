package keimo.HuoneEditori;

import keimo.*;
import keimo.Ikkunat.*;
import keimo.Kenttäkohteet.*;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.Utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class ObjektinMuokkausIkkuna {

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
    static JComboBox<String> sisältöValinta;

    static void hyväksyMuutokset(int sijX, int sijY, String muokattavanKohteenNimi) {
        try {
            switch (muokattavanKohteenNimi) {
                case "Oviruutu":
                    int kohdeHuone = huoneValikko.getSelectedIndex();
                    int kohdeRuutuX = Integer.parseInt(tekstiKentät[1].getText());
                    int kohdeRuutuY = Integer.parseInt(tekstiKentät[2].getText());
                    if (kohdeHuone < 0) {
                        CustomViestiIkkunat.WarpinMuokkausVirhe.showDialog("Negatiivinen ID ei kelpaa", "Virheellinen ID");
                    }
                    else {
                        if (kohdeRuutuX < Peli.kentänAlaraja || kohdeRuutuY < Peli.kentänAlaraja || kohdeRuutuX > Peli.kentänYläraja || kohdeRuutuY > Peli.kentänYläraja) {
                            CustomViestiIkkunat.WarpinMuokkausVirhe.showDialog("Kohdehuoneen koordinaattien täytyy olla väliltä " + Peli.kentänAlaraja + "-" + Peli.kentänYläraja, "Virheelliset koordinaatit");
                        }
                        else {
                            Oviruutu oviruutu = (Oviruutu)HuoneEditoriIkkuna.objektiKenttä[sijX][sijY];
                            oviruutu.asetaKohdeHuone(kohdeHuone);
                            oviruutu.asetaKohdeRuudut(kohdeRuutuX, kohdeRuutuY);
                            oviruutu.asetaSuunta((Warp.Suunta)suuntaValinta.getSelectedItem());
                            oviruutu.päivitäLisäOminaisuudet();
                            HuoneEditoriIkkuna.objektiKenttä[sijX][sijY] = oviruutu;
                            muokkausIkkuna.dispose();
                        }
                    }
                break;
                
                case "Kirstu":
                    Kirstu kirstu = (Kirstu)HuoneEditoriIkkuna.objektiKenttä[sijX][sijY];
                    kirstu.asetaSisältö(sisältöValinta.getSelectedItem().toString());
                    kirstu.lisäOminaisuuksia = true;
                    kirstu.päivitäLisäOminaisuudet();
                    HuoneEditoriIkkuna.objektiKenttä[sijX][sijY] = kirstu;
                    muokkausIkkuna.dispose();
                break;

                default:
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

    static void luoObjektinMuokkausIkkuna(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        
        switch (muokattavaKohde.annaNimi()) {
            case "Oviruutu":
                valintojenMäärä = 4;
                tekstit = new String[valintojenMäärä];
                tekstit[0] = "Kohdehuone (ID)";
                tekstit[1] = "Kohderuudun X-sijainti";
                tekstit[2] = "Kohderuudun Y-sijainti";
                tekstit[3] = "Suunta";
                break;

            case "Kirstu":
                valintojenMäärä = 1;
                tekstit = new String[valintojenMäärä];
                tekstit[0] = "Valitse sisältö";
                break;

            default:
                break;
        }

        paneli = new JPanel(new SpringLayout());
        tekstiLabelit = new JLabel[valintojenMäärä];
        tekstiKentät = new JTextField[valintojenMäärä];

        // for (int i = 0; i < valintojenMäärä; i++) {
        //     tekstiLabelit[i] = new JLabel(tekstit[i]);
        //     paneli.add(tekstiLabelit[i]);
        // }

        
        switch (muokattavaKohde.annaNimi()) {
            case "Oviruutu":
                tekstiLabelit[0] = new JLabel(tekstit[0]);
                paneli.add(tekstiLabelit[0]);
                huoneValikko = luoHuoneenNimiLista();
                paneli.add(huoneValikko);
                for (int i = 1; i < 3; i++) {
                    tekstiLabelit[i] = new JLabel(tekstit[i]);
                    paneli.add(tekstiLabelit[i]);
                    tekstiKentät[i] = new JTextField();
                    paneli.add(tekstiKentät[i]);
                }
                Warp oviruutu = (Warp)muokattavaKohde;
                huoneValikko.setSelectedIndex(oviruutu.annaKohdeHuone());
                tekstiKentät[1].setText("" + oviruutu.annaKohdeRuutuX());
                tekstiKentät[2].setText("" + oviruutu.annaKohdeRuutuY());
                Suunta[] suuntaLista = {Suunta.VASEN, Suunta.OIKEA, Suunta.YLÖS, Suunta.ALAS};
                suuntaValinta = new JComboBox<Suunta>(suuntaLista);
                suuntaValinta.setSelectedItem(oviruutu.annaSuunta());

                tekstiLabelit[3] = new JLabel(tekstit[3]);
                paneli.add(tekstiLabelit[3]);
                paneli.add(suuntaValinta);
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
                    hyväksyMuutokset(sijX, sijY, muokattavaKohde.annaNimi());
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

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä + 1, 2, 6, 6, 6, 6);
        ikkunanKorkeus = valintojenMäärä * 30 + 70;

        muokkausIkkuna = new JFrame();
        muokkausIkkuna.setTitle("Muokkaa objektia: " + muokattavaKohde.annaNimi());
        muokkausIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        muokkausIkkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        muokkausIkkuna.setLayout(new BorderLayout());
        muokkausIkkuna.setVisible(true);
        muokkausIkkuna.setLocationRelativeTo(null);
        muokkausIkkuna.add(paneli, BorderLayout.CENTER);
        muokkausIkkuna.revalidate();
        muokkausIkkuna.repaint();
    }    
}
