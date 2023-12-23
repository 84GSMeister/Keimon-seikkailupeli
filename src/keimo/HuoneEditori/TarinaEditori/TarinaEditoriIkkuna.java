package keimo.HuoneEditori.TarinaEditori;

import keimo.PääIkkuna;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.HuoneenMetatietoIkkuna;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TarinaEditoriIkkuna {
    
    static JFrame ikkuna;
    static final int ikkunanLeveys = 1200;
    static final int ikkunanKorkeus = 500; 

    static JComboBox<Object> tarinaValikkoLaatikko;
    static String laatikonValittuKohde;
    static JButton tarinanLisäysNappi;
    static JPanel tarinanValintaPaneli;

    static JButton lisäysNappi, poistamisNappi;
    static JPanel sivunValintaPaneli;

    static JPanel nappiPaneli;

    static JPanel tarinaPaneli;
    static JPanel[] tarinanSivut;
    static JButton[] tarinanKuvakkeet;
    static JButton[] tarinanTekstit;
    static ImageIcon[] tarinanKuvakkeetAlkupKoossa;
    static String[] tarinanKuvatiedostot;

    static JPanel alaPaneli;
    static JButton okNappi, cancelNappi;

    static int tarinanPituus = 4;

    public static void luoTarinaEditoriIkkuna() {

        ikkuna = new JFrame("Tarinaeditori v0.2");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(0, 0, ikkunanLeveys, ikkunanKorkeus);

        tarinaValikkoLaatikko = new JComboBox<>();
        for (String s : TarinaDialogiLista.tarinaKartta.keySet()) {
            tarinaValikkoLaatikko.addItem(s);
        }
        tarinaValikkoLaatikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                laatikonValittuKohde = (String)tarinaValikkoLaatikko.getSelectedItem();
                vaihdaTarinaDialogia(laatikonValittuKohde);
                päivitäTarinanPituus();
            }
        });

        tarinanLisäysNappi = new JButton("Uusi tarinapätkä");
        tarinanLisäysNappi.addActionListener(e -> TarinanLisäysIkkuna.luoTarinanLisäysIkkuna());

        tarinanValintaPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tarinanValintaPaneli.add(tarinaValikkoLaatikko);
        tarinanValintaPaneli.add(tarinanLisäysNappi);


        lisäysNappi = new JButton("Lisää sivu");
        lisäysNappi.addActionListener(e -> {
            tarinanPituus++;
            päivitäTarinanPituus();
            päivitäTarinaKartta();
        });
        poistamisNappi = new JButton("Poista sivu");
        poistamisNappi.addActionListener(e -> {
            if (tarinanPituus > 0) {
                tarinanPituus--;
                päivitäTarinanPituus();
            }
            päivitäTarinaKartta();
        });

        sivunValintaPaneli = new JPanel();
        sivunValintaPaneli.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        sivunValintaPaneli.add(lisäysNappi);
        sivunValintaPaneli.add(poistamisNappi);

        nappiPaneli = new JPanel(new BorderLayout());
        nappiPaneli.add(tarinanValintaPaneli, BorderLayout.NORTH);
        nappiPaneli.add(sivunValintaPaneli, BorderLayout.SOUTH);


        tarinanSivut = new JPanel[tarinanPituus];
        tarinanKuvakkeet = new JButton[tarinanPituus];
        tarinanTekstit = new JButton[tarinanPituus];
        tarinanKuvakkeetAlkupKoossa = new ImageIcon[tarinanPituus];

        for (int i = 0; i < tarinanPituus; i++) {
            int valittuSivunId = i;
            tarinanKuvakkeetAlkupKoossa[i] = new ImageIcon("");
            tarinanKuvakkeet[i] = new JButton("tarinan kuva " + i);
            tarinanKuvakkeet[i].setPreferredSize(new Dimension(ikkunanLeveys/tarinanPituus, ikkuna.getHeight()/2 - 100));
            tarinanKuvakkeet[i].addActionListener(e -> {valitseTarinanKuvake(valittuSivunId);});
            tarinanTekstit[i] = new JButton("<html><p>tarinan teksti " + i + "</p></html>");
            tarinanTekstit[i].setPreferredSize(new Dimension(ikkunanLeveys/tarinanPituus, ikkuna.getHeight()/2 - 50));
            tarinanTekstit[i].addActionListener(e -> TarinanTekstinMuokkausIkkuna.luoTarinanLisäysIkkuna(valittuSivunId));
            tarinanSivut[i] = new JPanel(new BorderLayout());
            tarinanSivut[i].setBorder(BorderFactory.createLineBorder(Color.green, 1, false));
            tarinanSivut[i].add(tarinanKuvakkeet[i], BorderLayout.NORTH);
            tarinanSivut[i].add(tarinanTekstit[i], BorderLayout.SOUTH);
        }

        tarinaPaneli = new JPanel(new GridLayout(1, tarinanPituus, 10, 10));
        tarinaPaneli.setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
        for (JPanel jp : tarinanSivut) {
            tarinaPaneli.add(jp);
        }

        String luotavanTarinanTunniste = (String)tarinaValikkoLaatikko.getSelectedItem();
        ImageIcon[] luotavanTarinanKuvakkeet = new ImageIcon[tarinanPituus];
        for (int i = 0; i < luotavanTarinanKuvakkeet.length; i++) {
            luotavanTarinanKuvakkeet[i] = (ImageIcon)tarinanKuvakkeet[i].getIcon();
        }
        String[] luotavanTarinanTekstit = new String[tarinanPituus];
        for (int i = 0; i < luotavanTarinanTekstit.length; i++) {
            luotavanTarinanTekstit[i] = tarinanTekstit[i].getText();
        }

        okNappi = new JButton("OK");
        okNappi.addActionListener(e -> {
            HuoneenMetatietoIkkuna.päivitäTarinaValintaLaatikko();
            ikkuna.dispose();
        });
        cancelNappi = new JButton("Peruuta");
        cancelNappi.addActionListener(e -> ikkuna.dispose());

        alaPaneli = new JPanel();
        alaPaneli.setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        alaPaneli.add(okNappi);

        ikkuna.setLayout(new BorderLayout());
        ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        ikkuna.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                päivitäTarinanPituus();
            }
        });
        ikkuna.setVisible(true);
        ikkuna.add(nappiPaneli, BorderLayout.NORTH);
        ikkuna.add(tarinaPaneli, BorderLayout.CENTER);
        ikkuna.add(alaPaneli, BorderLayout.SOUTH);
    }

    private static void päivitäTarinanPituus() {

        tarinaPaneli.removeAll();
        String luotavanTarinanTunniste = laatikonValittuKohde;

        tarinanSivut = new JPanel[tarinanPituus];
        tarinanKuvakkeet = new JButton[tarinanPituus];
        tarinanTekstit = new JButton[tarinanPituus];
        tarinanKuvakkeetAlkupKoossa = new ImageIcon[tarinanPituus];
        tarinanKuvatiedostot = new String[tarinanPituus];

        for (int i = 0; i < tarinanPituus; i++) {
            int valittuSivunId = i;
            tarinanKuvakkeetAlkupKoossa[i] = new ImageIcon("tiedostot/kuvat/tarina/tarina_placeholder.png");
            tarinanKuvakkeet[i] = new JButton(new ImageIcon("tiedostot/kuvat/tarina/tarina_placeholder.png"));
            tarinanKuvakkeet[i].setPreferredSize(new Dimension(ikkunanLeveys/tarinanPituus, ikkuna.getHeight()/2 - 100));
            tarinanKuvakkeet[i].setToolTipText("Klikkaa tästä valitaksesi kuvatiedoston");
            tarinanKuvakkeet[i].addActionListener(e -> {valitseTarinanKuvake(valittuSivunId);});
            tarinanTekstit[i] = new JButton("<html><p>tarinan teksti " + i + "</p></html>");
            tarinanTekstit[i].setPreferredSize(new Dimension(ikkunanLeveys/tarinanPituus, ikkuna.getHeight()/2 - 50));
            tarinanTekstit[i].setToolTipText("Klikkaa tästä muokataksesi tekstiä");
            tarinanTekstit[i].addActionListener(e -> TarinanTekstinMuokkausIkkuna.luoTarinanLisäysIkkuna(valittuSivunId));

            TarinaPätkä tp = TarinaDialogiLista.tarinaKartta.get(luotavanTarinanTunniste);
            if (tp != null) {
                if (tp.annaKuvatiedostot() != null) {
                    if (tp.annaKuvatiedostot().length > i) {
                        if (tp.annaKuvatiedostot()[i] != null) {
                            tarinanKuvatiedostot[i] = tp.annaKuvatiedostot()[i];
                            Image img = new ImageIcon(tp.annaKuvatiedostot()[i]).getImage();
                            tarinanKuvakkeetAlkupKoossa[i] = new ImageIcon(tp.annaKuvatiedostot()[i]);
                            Image sovitettuKuvake = img.getScaledInstance(ikkunanLeveys/tarinanPituus, ikkuna.getHeight()/2 - 100, Image.SCALE_DEFAULT);
                            tarinanKuvakkeet[i].setIcon(new ImageIcon(sovitettuKuvake));
                            tarinanKuvakkeet[i].setText("");
                        }
                    }
                }
                if (tp.annaTekstit() != null) {
                    if (tp.annaTekstit().length > i) {
                        if (tp.annaTekstit()[i] != null) {
                            tarinanTekstit[i].setText(tp.annaTekstit()[i]);
                            //System.out.println("teksti " + i + ": " + tp.annaTekstit()[i]);
                        }
                    }
                }
            }

            tarinanSivut[i] = new JPanel(new BorderLayout());
            tarinanSivut[i].setBorder(BorderFactory.createLineBorder(Color.green, 1, false));
            tarinanSivut[i].add(tarinanKuvakkeet[i], BorderLayout.NORTH);
            tarinanSivut[i].add(tarinanTekstit[i], BorderLayout.SOUTH);
        }

        tarinaValikkoLaatikko.removeAllItems();
        for (String s : TarinaDialogiLista.tarinaKartta.keySet()) {
            tarinaValikkoLaatikko.addItem(s);
        }
        tarinaValikkoLaatikko.setSelectedItem(laatikonValittuKohde);

        for (JPanel jp : tarinanSivut) {
            tarinaPaneli.add(jp);
        }
        tarinaPaneli.revalidate();
        tarinaPaneli.repaint();

    }

    protected static void päivitäTarinaKartta() {
        String luotavanTarinanTunniste = laatikonValittuKohde;
        ImageIcon[] luotavanTarinanKuvakkeet = new ImageIcon[tarinanPituus];
        String[] luotavanTarinanKuvatiedostot = new String[tarinanPituus];
        for (int i = 0; i < luotavanTarinanKuvakkeet.length; i++) {
            if (tarinanKuvakkeet.length > i) {
                luotavanTarinanKuvakkeet[i] = (ImageIcon)tarinanKuvakkeetAlkupKoossa[i];
                luotavanTarinanKuvatiedostot[i] = tarinanKuvatiedostot[i];
            }
        }
        String[] luotavanTarinanTekstit = new String[tarinanPituus];
        for (int i = 0; i < luotavanTarinanTekstit.length; i++) {
            if (tarinanTekstit.length > i) {
                String str = tarinanTekstit[i].getText().replaceAll("[\r\n]", "");
                luotavanTarinanTekstit[i] = str;
                System.out.println("teksti " + i + ": " + str);
            }
        }
        TarinaDialogiLista.tarinaKartta.put(luotavanTarinanTunniste, new TarinaPätkä(luotavanTarinanTunniste, tarinanPituus, luotavanTarinanKuvatiedostot, luotavanTarinanTekstit));
    }

    private static void vaihdaTarinaDialogia(String tarinanTunniste) {
        TarinaPätkä tp = TarinaDialogiLista.tarinaKartta.get(tarinanTunniste);
        if (tp != null) {
            tarinanPituus = tp.annaPituus();
            if (tp.annaKuvatiedostot() != null) {
                for (int i = 0; i < tarinanKuvakkeet.length; i++) {
                    if (tp.annaKuvatiedostot().length > i) {
                        tarinanKuvakkeet[i].setIcon(new ImageIcon(tp.annaKuvatiedostot()[i]));
                    }
                }
            }
            if (tp.annaTekstit() != null) {
                for (int i = 0; i < tarinanTekstit.length; i++) {
                    if (tp.annaTekstit().length > i) {
                        tarinanTekstit[i].setText((tp.annaTekstit()[i]));
                    }
                }
            }
        }
    }

    private static void valitseTarinanKuvake(int sivunId) {
        JFileChooser tiedostoSelain = new JFileChooser("tiedostot/kuvat/tarina");
        FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png .jpg .gif)", "png", "jpg", "gif");
        tiedostoSelain.setFileFilter(tiedostoSuodatin);
        int valinta = tiedostoSelain.showOpenDialog(ikkuna);
        if (valinta == JFileChooser.APPROVE_OPTION) {
            try {
                File tiedosto = tiedostoSelain.getSelectedFile();
                ImageIcon valittuKuva = new ImageIcon(tiedosto.getPath());
                tarinanKuvakkeetAlkupKoossa[sivunId] = new ImageIcon(valittuKuva.getImage());
                tarinanKuvakkeet[sivunId].setIcon(valittuKuva);
                String polku = tiedosto.getPath();
                tarinanKuvatiedostot[sivunId] = polku.substring(polku.indexOf("tiedostot\\kuvat\\tarina"), polku.length());
                System.out.println(tarinanKuvatiedostot[sivunId]);
                tarinanKuvakkeet[sivunId].setText("");
                päivitäTarinaKartta();
                päivitäTarinanPituus();
            }
            catch (IndexOutOfBoundsException ioobe) {
                JOptionPane.showMessageDialog(null, "Kuvatiedoston täytyy olla kansiossa \"tiedostot/kuvat/tarina\". Muuten se ei välttämättä tallennu oikein.", "Virheellinen polku", JOptionPane.WARNING_MESSAGE);
                ioobe.printStackTrace();
            }
        }
    }
}
