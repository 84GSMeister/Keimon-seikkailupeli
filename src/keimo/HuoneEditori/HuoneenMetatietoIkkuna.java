package keimo.HuoneEditori;

import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaEditoriIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteEditoriIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Utility.Downloaded.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HuoneenMetatietoIkkuna {

    static final int ikkunanLeveys = 500;
    static final int ikkunanKorkeus = 295;
    static String[] musat = {"baari", "boss", "kauppa", "koti", "kuu", "metsä", "overworld", "puisto", "tarina", "temppeli", "valikko"};

    static JPanel pääPaneeli, yläPaneeli, alaPaneeli, yläVasenPaneeli, yläOikeaPaneeli;
    static JLabel huoneenIdLabel, huoneenKokoLabel, huoneenNimiTekstiKenttäLabel, huoneenAlueTekstiKenttäLabel, huoneenKuvaTekstiKenttäLabel, huoneenMusaTekstiKenttäLabel, huoneenDialogiTekstiKenttäLabel, huoneenTavoiteTekstiKenttäLabel;
    static JTextField huoneenIdTekstiKenttä, huoneenKokoTekstiKenttä, huoneenNimiTekstiKenttä, huoneenAlueTekstiKenttä, valitunKuvanPolku, huoneenDialogiValintaTekstiKenttä, huoneenTavoiteTekstiKenttä;
    static JButton huoneenKuvaValintaNappi, tarinanMuokkausNappi, tavoitteidenMuokkausNappi;
    static JComboBox<Object> huoneenMusaValintaLaatikko, huoneenTarinaValintaLaatikko, huoneenTavoiteValintaLaatikko;
    static JFrame ikkuna;
    
    public static void luoIkkuna() {
        huoneenIdLabel = new JLabel("Huoneen ID: ");
        huoneenIdLabel.setToolTipText("Huoneen ID, ei muokattavissa");
        huoneenIdTekstiKenttä = new JTextField();
        huoneenIdTekstiKenttä.setPreferredSize(new Dimension(200, 20));
        huoneenIdTekstiKenttä.setToolTipText("Huoneen ID, ei muokattavissa");
        huoneenIdTekstiKenttä.setOpaque(false);
        huoneenIdTekstiKenttä.setEditable(false);
        huoneenKokoLabel = new JLabel("Huoneen koko: ");
        huoneenKokoLabel.setToolTipText("Vakiokoko on 10. Sitä suuremmat huoneet ovat tällä hetkellä kokeellisia. Kokoa voi muuttaa huoneen luontivaiheessa.");
        huoneenKokoTekstiKenttä = new JTextField();
        huoneenKokoTekstiKenttä.setPreferredSize(new Dimension(200, 20));
        huoneenKokoTekstiKenttä.setToolTipText("Vakiokoko on 10. Sitä suuremmat huoneet ovat tällä hetkellä kokeellisia. Kokoa voi muuttaa huoneen luontivaiheessa.");
        huoneenKokoTekstiKenttä.setOpaque(false);
        huoneenKokoTekstiKenttä.setEditable(false);
        huoneenNimiTekstiKenttäLabel = new JLabel("Huoneen nimi: ");
        huoneenNimiTekstiKenttäLabel.setToolTipText("Nimi ei ole pakollinen, mutta selkeyttävä seikka");
        huoneenNimiTekstiKenttä = new JTextField();
        huoneenNimiTekstiKenttä.setPreferredSize(new Dimension(200, 20));
        huoneenNimiTekstiKenttä.setToolTipText("Nimi ei ole pakollinen, mutta selkeyttävä seikka");
        huoneenAlueTekstiKenttäLabel = new JLabel("Huoneen alue: ");
        huoneenAlueTekstiKenttäLabel.setToolTipText("Tätä käytetään identifioimaan oikea kartta alueelle");
        huoneenAlueTekstiKenttä = new JTextField();
        huoneenAlueTekstiKenttä.setPreferredSize(new Dimension(200, 20));
        huoneenAlueTekstiKenttä.setToolTipText("Tätä käytetään identifioimaan oikea kartta alueelle");
        huoneenKuvaTekstiKenttäLabel = new JLabel("Huoneen tausta: ");
        huoneenKuvaTekstiKenttäLabel.setToolTipText("Huoneen taustaksi suositellaan 660x660 pikselin kuvia. Muunkokoiset kuvat voivat näyttää väärältä tai eivät näy kokonaan.");

        huoneenMusaTekstiKenttäLabel = new JLabel("Huoneen musiikki: ");
        huoneenMusaTekstiKenttäLabel.setToolTipText("Valitse huoneessa soiva musiikki. Jos vierekkäisten huoneiden musiikki on sama, se ei ala alusta huonetta ladatessa.");
        huoneenMusaValintaLaatikko = new JComboBox<Object>();
        huoneenMusaValintaLaatikko.addItem(" ");
        for (String s : musat) {
            huoneenMusaValintaLaatikko.addItem(s);
        }
        huoneenMusaValintaLaatikko.setToolTipText("Tämä tarinapätkä tulee, kun huone ladataan ensimmäisen kerran.");

        huoneenDialogiTekstiKenttäLabel = new JLabel("Huoneen alkudialogi: ");
        huoneenDialogiTekstiKenttäLabel.setToolTipText("Tämä tarinapätkä tulee, kun huone ladataan ensimmäisen kerran.");
        //huoneenDialogiValintaTekstiKenttä = new JTextField();
        huoneenTarinaValintaLaatikko = new JComboBox<Object>();
        huoneenTarinaValintaLaatikko.addItem(" ");
        for (String s : TarinaDialogiLista.tarinaKartta.keySet()) {
            huoneenTarinaValintaLaatikko.addItem(s);
        }
        huoneenTarinaValintaLaatikko.setToolTipText("Tämä tarinapätkä tulee, kun huone ladataan ensimmäisen kerran.");

        huoneenTavoiteTekstiKenttäLabel = new JLabel("Huoneen tavoite: ");
        huoneenTavoiteTekstiKenttäLabel.setToolTipText("Tämä tavoite täytyy olla suoritettu, jotta huoneeseen pääsee sisälle.");
        //huoneenTavoiteTekstiKenttä = new JTextField();
        huoneenTavoiteValintaLaatikko = new JComboBox<Object>();
        huoneenTavoiteValintaLaatikko.addItem(" ");
        for (String s : TavoiteLista.tavoiteLista.keySet()) {
            huoneenTavoiteValintaLaatikko.addItem(s);
        }
        huoneenTavoiteTekstiKenttäLabel.setToolTipText("Tämä tavoite täytyy olla suoritettu, jotta huoneeseen pääsee sisälle.");

        JLabel placeholder1 = new JLabel(" ");
        placeholder1.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel placeholder2 = new JLabel(" ");
        placeholder2.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel placeholder3 = new JLabel(" ");
        placeholder3.setFont(new Font("Arial", Font.PLAIN, 18));
        valitunKuvanPolku = new JTextField("Ei kuvaa");
        JLabel placeholder4 = new JLabel(" ");
        placeholder4.setFont(new Font("Arial", Font.PLAIN, 18));
        JLabel placeholder5 = new JLabel(" ");
        placeholder4.setFont(new Font("Arial", Font.PLAIN, 18));

        yläVasenPaneeli = new JPanel();
        yläVasenPaneeli.setLayout(new SpringLayout());
        yläVasenPaneeli.setMaximumSize(new Dimension(300, 60));
        yläVasenPaneeli.add(huoneenIdLabel);
        yläVasenPaneeli.add(huoneenIdTekstiKenttä);
        yläVasenPaneeli.add(huoneenKokoLabel);
        yläVasenPaneeli.add(huoneenKokoTekstiKenttä);
        yläVasenPaneeli.add(huoneenNimiTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenNimiTekstiKenttä);
        yläVasenPaneeli.add(huoneenAlueTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenAlueTekstiKenttä);
        yläVasenPaneeli.add(huoneenKuvaTekstiKenttäLabel);
        yläVasenPaneeli.add(valitunKuvanPolku);
        yläVasenPaneeli.add(huoneenMusaTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenMusaValintaLaatikko);
        yläVasenPaneeli.add(huoneenDialogiTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenTarinaValintaLaatikko);
        yläVasenPaneeli.add(huoneenTavoiteTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenTavoiteValintaLaatikko);
        SpringUtilities.makeCompactGrid(yläVasenPaneeli, 8, 2, 6, 6, 6, 6);

        huoneenKuvaValintaNappi = new JButton("Valitse kuva");
        huoneenKuvaValintaNappi.setToolTipText("Huoneen taustaksi suositellaan 660x660 pikselin kuvia. Muunkokoiset kuvat voivat näyttää väärältä tai eivät näy kokonaan.");
        huoneenKuvaValintaNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser tiedostoSelain = new JFileChooser(".\\");
                FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png .jpg .gif)", "png", "jpg", "gif");
                tiedostoSelain.setFileFilter(tiedostoSuodatin);
                int valinta = tiedostoSelain.showOpenDialog(ikkuna);
                if (valinta == JFileChooser.APPROVE_OPTION) {
                    File tiedosto = tiedostoSelain.getSelectedFile();
                    HuoneEditoriIkkuna.huoneenTaustakuvaPolku = tiedosto.getName();
                    HuoneEditoriIkkuna.huoneenTaustakuva = new ImageIcon("tiedostot/kuvat/taustat/" + HuoneEditoriIkkuna.huoneenTaustakuvaPolku);
                    HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäTausta(HuoneEditoriIkkuna.huoneenTaustakuvaPolku);
                    valitunKuvanPolku.setText(tiedosto.getName());
                }
            }
        });

        tarinanMuokkausNappi = new JButton("Muokkaa tarinadialogeja");
        tarinanMuokkausNappi.addActionListener(e -> TarinaEditoriIkkuna.luoTarinaEditoriIkkuna());

        tavoitteidenMuokkausNappi = new JButton("Muokkaa tavoitteita");
        tavoitteidenMuokkausNappi.addActionListener(e -> TavoiteEditoriIkkuna.luoTavoiteEditoriIkkuna());

        yläOikeaPaneeli = new JPanel();
        yläOikeaPaneeli.setLayout(new SpringLayout());
        yläOikeaPaneeli.add(placeholder1);
        yläOikeaPaneeli.add(placeholder2);
        yläOikeaPaneeli.add(placeholder3);
        yläOikeaPaneeli.add(placeholder4);
        yläOikeaPaneeli.add(huoneenKuvaValintaNappi);
        yläOikeaPaneeli.add(placeholder5);
        yläOikeaPaneeli.add(tarinanMuokkausNappi);
        yläOikeaPaneeli.add(tavoitteidenMuokkausNappi);
        SpringUtilities.makeCompactGrid(yläOikeaPaneeli, 8, 1, 6, 6, 6, 6);

        yläPaneeli = new JPanel();
        yläPaneeli.setLayout(new BorderLayout());
        yläPaneeli.setMaximumSize(new Dimension(500, 60));
        yläPaneeli.add(yläVasenPaneeli, BorderLayout.WEST);
        yläPaneeli.add(yläOikeaPaneeli, BorderLayout.EAST);

        JButton okNappi = new JButton("OK");
        okNappi.setBounds(0, 0, 20, 20);
        okNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (huoneenNimiTekstiKenttä.getText().contains("(") || huoneenNimiTekstiKenttä.getText().contains(")")) {
                    CustomViestiIkkunat.SulkumerkkiVaroitus.näytäDialogi();
                }
                else {
                    HuoneEditoriIkkuna.huoneenNimi = huoneenNimiTekstiKenttä.getText();
                    HuoneEditoriIkkuna.huoneenAlue = huoneenAlueTekstiKenttä.getText();
                    HuoneEditoriIkkuna.huoneenMusa = "" + huoneenMusaValintaLaatikko.getSelectedItem();
                    HuoneEditoriIkkuna.huoneenAlkuDialoginTunniste = (String)huoneenTarinaValintaLaatikko.getSelectedItem();
                    HuoneEditoriIkkuna.huoneenVaaditunTavoitteenTunniste = (String)huoneenTavoiteValintaLaatikko.getSelectedItem();
                    HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäNimiJaAlue(HuoneEditoriIkkuna.huoneenNimi, HuoneEditoriIkkuna.huoneenAlue);
                    HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäMusa(HuoneEditoriIkkuna.huoneenMusa);
                    HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäAlkudialogi(HuoneEditoriIkkuna.huoneenAlkuDialoginTunniste);
                    //HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäHuoneenKenttäSisältö(HuoneEditoriIkkuna.objektiKenttä);
                    //HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäHuoneenMaastoSisältö(HuoneEditoriIkkuna.maastoKenttä);
                    //HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäHuoneenNPCSisältö(HuoneEditoriIkkuna.npcKenttä);
                    HuoneEditoriIkkuna.huoneenNimiLabel.setText(HuoneEditoriIkkuna.huoneenNimi + " (" + HuoneEditoriIkkuna.huoneenAlue + ")");
                    ikkuna.dispose();
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.setBounds(60, 0, 20, 20);
        cancelNappi.addActionListener(e -> ikkuna.dispose());

        alaPaneeli = new JPanel();
        alaPaneeli.setLayout(new FlowLayout(FlowLayout.CENTER));
        alaPaneeli.setMaximumSize(new Dimension(500, 20));
        alaPaneeli.setBounds(0, 0, 500, 30);
        alaPaneeli.add(okNappi);
        alaPaneeli.add(cancelNappi);

        pääPaneeli = new JPanel();
        pääPaneeli.setLayout(new BorderLayout());
        pääPaneeli.add(yläPaneeli, BorderLayout.NORTH);
        pääPaneeli.add(alaPaneeli, BorderLayout.SOUTH);
        pääPaneeli.setPreferredSize(new Dimension(ikkunanLeveys -30, 90));
        pääPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        pääPaneeli.revalidate();
        pääPaneeli.repaint();

        huoneenIdTekstiKenttä.setText("" + HuoneEditoriIkkuna.muokattavaHuone);
        huoneenKokoTekstiKenttä.setText("" + HuoneEditoriIkkuna.huoneenKoko);
        huoneenNimiTekstiKenttä.setText(HuoneEditoriIkkuna.huoneenNimi);
        huoneenAlueTekstiKenttä.setText(HuoneEditoriIkkuna.huoneenAlue);
        //huoneenDialogiValintaTekstiKenttä.setText(HuoneEditoriIkkuna.huoneenAlkuDialoginTunniste);
        valitunKuvanPolku.setText(HuoneEditoriIkkuna.huoneenTaustakuvaPolku);
        huoneenMusaValintaLaatikko.setSelectedItem(HuoneEditoriIkkuna.huoneenMusa);
        huoneenTarinaValintaLaatikko.setSelectedItem(HuoneEditoriIkkuna.huoneenAlkuDialoginTunniste);
        huoneenTavoiteValintaLaatikko.setSelectedItem(HuoneEditoriIkkuna.huoneenVaaditunTavoitteenTunniste);

        ikkuna = new JFrame("Muokkaa huoneen metatietoja");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        ikkuna.setResizable(false);
        ikkuna.setVisible(true);
        ikkuna.add(pääPaneeli);
        ikkuna.revalidate();
        ikkuna.repaint();
    }

    public static void päivitäTarinaValintaLaatikko() {
        if (huoneenTarinaValintaLaatikko != null) {
            huoneenTarinaValintaLaatikko.removeAllItems();
            huoneenTarinaValintaLaatikko.addItem(" ");
            for (String s : TarinaDialogiLista.tarinaKartta.keySet()) {
                huoneenTarinaValintaLaatikko.addItem(s);
            }
        }
    }
}
