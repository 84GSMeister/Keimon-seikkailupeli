package keimo.HuoneEditori.muokkausIkkunat;

import keimo.*;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.DialogiEditori.*;
import keimo.Ikkunat.*;
import keimo.Maastot.Maasto;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.Utility.Käännettävä.Suunta;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.warp.Warp;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WarpMuokkaus {

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
    static JComboBox<Suunta> suuntaValinta;
    static JTextField kohdeRuudunObjekti;
    static JComboBox<String> sisältöValinta;
    static JTextField triggeriLista;
    static JCheckBox valintaLaatikko;

    static void hyväksyMuutokset(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        try {
            switch (muokattavaKohde.annaNimi()) {
                case "Oviruutu", "Kauppaovi", "Puuovi", "Baariovi":
                    int kohdeHuoneenId = huoneValikko.getSelectedIndex();
                    int kohdeRuutuX = Integer.parseInt(tekstiKentät[1].getText());
                    int kohdeRuutuY = Integer.parseInt(tekstiKentät[2].getText());
                    if (kohdeHuoneenId < 0) {
                        CustomViestiIkkunat.WarpinMuokkausVirhe.näytäDialogi("Negatiivinen ID ei kelpaa", "Virheellinen ID");
                    }
                    else {
                        Warp oviruutu = (Warp)HuoneEditoriIkkuna.objektiKenttä[sijX][sijY];
                        Huone kohdeHuone = HuoneEditoriIkkuna.huoneKartta.get(kohdeHuoneenId);
                        if (kohdeRuutuX < Peli.kentänAlaraja || kohdeRuutuY < Peli.kentänAlaraja || kohdeRuutuX > kohdeHuone.annaKoko()-1 || kohdeRuutuY > kohdeHuone.annaKoko()-1) {
                            CustomViestiIkkunat.WarpinMuokkausVirhe.näytäDialogi("Kohdehuoneen koordinaattien täytyy olla väliltä " + Peli.kentänAlaraja + "-" + (kohdeHuone.annaKoko()-1), "Virheelliset koordinaatit");
                        }
                        else {
                            oviruutu.asetaKohdeHuone(kohdeHuoneenId);
                            oviruutu.asetaKohdeRuudut(kohdeRuutuX, kohdeRuutuY);
                            oviruutu.asetaSuunta((Warp.Suunta)suuntaValinta.getSelectedItem());
                            oviruutu.päivitäLisäOminaisuudet();
                            HuoneEditoriIkkuna.objektiKenttä[sijX][sijY] = oviruutu;
                            muokkausIkkuna.dispose();
                        }
                    }
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
                    continue;
                }
                else {
                    huoneidenNimet[toimivatHuoneet] = HuoneEditoriIkkuna.huoneKartta.get(i).annaNimi() + " (" + HuoneEditoriIkkuna.huoneKartta.get(i).annaId() + ")";
                    toimivatHuoneet++;
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

    public static void luoWarpMuokkausIkkuna(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        
        if (muokattavaKohde instanceof Warp) {
            valintojenMäärä = 5;
            tekstit = new String[valintojenMäärä];
            tekstit[0] = "Kohdehuone (ID)";
            tekstit[1] = "Kohderuudun X-sijainti";
            tekstit[2] = "Kohderuudun Y-sijainti";
            tekstit[3] = "Kohteessa on";
            tekstit[4] = "Suunta";
        }

        paneli = new JPanel(new SpringLayout());
        tekstiLabelit = new JLabel[valintojenMäärä];
        tekstiKentät = new JTextField[valintojenMäärä];

        if (muokattavaKohde instanceof Warp) {
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
            warpKohdeHuone = oviruutu.annaKohdeHuone();
            warpKohdeX = oviruutu.annaKohdeRuutuX();
            warpKohdeY = oviruutu.annaKohdeRuutuY();
            huoneValikko.setSelectedIndex(oviruutu.annaKohdeHuone());
            huoneValikko.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    try {
                        String valittuKohde = "" + huoneValikko.getSelectedItem();
                        String huoneenNroString = valittuKohde.substring(valittuKohde.indexOf("(")+1, valittuKohde.indexOf(")"));
                        warpKohdeHuone = Integer.parseInt(huoneenNroString);

                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        Maasto m = huone.annaHuoneenMaastoSisältö()[warpKohdeX][warpKohdeY];
                        String kenttäTeksti, maastoTeksti;
                        if (k != null) kenttäTeksti = "K: " + k.annaNimi();
                        else {
                            kenttäTeksti = "K: " + "tyhjä";
                        }
                        if (m != null) maastoTeksti = "M: " + m.annaNimi();
                        else {
                            maastoTeksti = "M: " + "tyhjä";
                        }
                        kohdeRuudunObjekti.setText(kenttäTeksti + ", " + maastoTeksti);
                        kohdeRuudunObjekti.setForeground(Color.black);
                    }
                    catch (NumberFormatException nfe) {

                    }
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        kohdeRuudunObjekti.setText("Kentän ulkopuolella!");
                        kohdeRuudunObjekti.setForeground(Color.red);
                    }
                }
            });
            tekstiKentät[1].setText("" + oviruutu.annaKohdeRuutuX());
            tekstiKentät[1].getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        warpKohdeX = Integer.parseInt(tekstiKentät[1].getText());
                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        Maasto m = huone.annaHuoneenMaastoSisältö()[warpKohdeX][warpKohdeY];
                        String kenttäTeksti, maastoTeksti;
                        if (k != null) kenttäTeksti = "K: " + k.annaNimi();
                        else {
                            kenttäTeksti = "K: " + "tyhjä";
                        }
                        if (m != null) maastoTeksti = "M: " + m.annaNimi();
                        else {
                            maastoTeksti = "M: " + "tyhjä";
                        }
                        kohdeRuudunObjekti.setText(kenttäTeksti + ", " + maastoTeksti);
                        kohdeRuudunObjekti.setForeground(Color.black);
                    }
                    catch (NumberFormatException nfe) {

                    }
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        kohdeRuudunObjekti.setText("Kentän ulkopuolella!");
                        kohdeRuudunObjekti.setForeground(Color.red);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        warpKohdeX = Integer.parseInt(tekstiKentät[1].getText());
                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        Maasto m = huone.annaHuoneenMaastoSisältö()[warpKohdeX][warpKohdeY];
                        String kenttäTeksti, maastoTeksti;
                        if (k != null) kenttäTeksti = "K: " + k.annaNimi();
                        else {
                            kenttäTeksti = "K: " + "tyhjä";
                        }
                        if (m != null) maastoTeksti = "M: " + m.annaNimi();
                        else {
                            maastoTeksti = "M: " + "tyhjä";
                        }
                        kohdeRuudunObjekti.setText(kenttäTeksti + ", " + maastoTeksti);
                        kohdeRuudunObjekti.setForeground(Color.black);
                    }
                    catch (NumberFormatException nfe) {

                    }
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        kohdeRuudunObjekti.setText("Kentän ulkopuolella!");
                        kohdeRuudunObjekti.setForeground(Color.red);
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    try {
                        warpKohdeX = Integer.parseInt(tekstiKentät[1].getText());
                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        if (k != null) kohdeRuudunObjekti.setText(k.annaNimi());
                        else {
                            kohdeRuudunObjekti.setText("tyhjä");
                        }
                    }
                    catch (NumberFormatException nfe) {

                    }
                }
            });
            tekstiKentät[2].setText("" + oviruutu.annaKohdeRuutuY());
            tekstiKentät[2].getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        warpKohdeX = Integer.parseInt(tekstiKentät[1].getText());
                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        if (k != null) kohdeRuudunObjekti.setText(k.annaNimi());
                        else {
                            kohdeRuudunObjekti.setText("tyhjä");
                        }
                    }
                    catch (NumberFormatException nfe) {

                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        warpKohdeX = Integer.parseInt(tekstiKentät[1].getText());
                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        if (k != null) kohdeRuudunObjekti.setText(k.annaNimi());
                        else {
                            kohdeRuudunObjekti.setText("tyhjä");
                        }
                    }
                    catch (NumberFormatException nfe) {

                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    try {
                        warpKohdeX = Integer.parseInt(tekstiKentät[1].getText());
                        Huone huone = Peli.huoneKartta.get(warpKohdeHuone);
                        KenttäKohde k = huone.annaHuoneenKenttäSisältö()[warpKohdeX][warpKohdeY];
                        if (k != null) kohdeRuudunObjekti.setText(k.annaNimi());
                        else {
                            kohdeRuudunObjekti.setText("tyhjä");
                        }
                    }
                    catch (NumberFormatException nfe) {

                    }
                }
            });
            
            kohdeRuudunObjekti = new JTextField();
            kohdeRuudunObjekti.setEditable(false);
            Huone huone = Peli.huoneKartta.get(oviruutu.annaKohdeHuone());
            KenttäKohde k = huone.annaHuoneenKenttäSisältö()[oviruutu.annaKohdeRuutuX()][oviruutu.annaKohdeRuutuY()];
            if (k != null) kohdeRuudunObjekti.setText(k.annaNimi());
            else {
                kohdeRuudunObjekti.setText("tyhjä");
            }

            tekstiLabelit[3] = new JLabel(tekstit[3]);
            paneli.add(tekstiLabelit[3]);
            paneli.add(kohdeRuudunObjekti);

            Suunta[] suuntaLista = {Suunta.VASEN, Suunta.OIKEA, Suunta.YLÖS, Suunta.ALAS};
            suuntaValinta = new JComboBox<Suunta>(suuntaLista);
            suuntaValinta.setSelectedItem(oviruutu.annaSuunta());
            tekstiLabelit[4] = new JLabel(tekstit[4]);
            paneli.add(tekstiLabelit[4]);
            paneli.add(suuntaValinta);
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

    public static void päivitäDialogiValintaLaatikko() {
        if (sisältöValinta != null) {
            sisältöValinta.removeAllItems();
            for (String s : VuoropuheDialogit.vuoropuheDialogiKartta.keySet()) {
                sisältöValinta.addItem(s);
            }
        }
    }
}

