package keimo.HuoneEditori.muokkausIkkunat;

import keimo.*;
import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.DialogiEditori.*;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.Utility.Käännettävä.Suunta;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.avattavaEste.AvattavaEste;
import keimo.kenttäkohteet.avattavaEste.Portti;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class PorttiMuokkaus {

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
    protected static JTextField triggeriLista;
    static JCheckBox valintaLaatikko;

    static void hyväksyMuutokset(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        try {
            switch (muokattavaKohde.annaNimi()) {
                case "Portti":
                    Portti portti = (Portti)HuoneEditoriIkkuna.objektiKenttä[sijX][sijY];
                    String[] pisteet = triggeriLista.getText().split("; ");
                    portti.tyhjennäTriggerit();
                    for (String s : pisteet) {
                        System.out.println(s);
                        if (s.length() >= 3) {
                            try {
                                String[] xy = s.split(",");
                                int x = Integer.parseInt(xy[0]);
                                int y = Integer.parseInt(xy[1]);
                                portti.lisääTriggeri(x, y);
                            }
                            catch (NumberFormatException nfe) {
                                System.out.println("Virhe parsiessa numeroa");
                                nfe.printStackTrace();
                            }
                            catch (ArrayIndexOutOfBoundsException aioobe) {
                                System.out.println("Virhe parsiessa numeroa");
                                aioobe.printStackTrace();
                            }
                        }
                    }
                    portti.päivitäLisäOminaisuudet();
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

    public static void luoPorttiMuokkausIkkuna(int sijX, int sijY, KenttäKohde muokattavaKohde) {
        
        if (muokattavaKohde instanceof AvattavaEste) {
            valintojenMäärä = 2;
            tekstit = new String[valintojenMäärä];
            tekstit[0] = "Triggerit";
            tekstit[1] = "Lisää triggeri";
        }

        paneli = new JPanel(new SpringLayout());
        tekstiLabelit = new JLabel[valintojenMäärä];
        tekstiKentät = new JTextField[valintojenMäärä];

        if (muokattavaKohde instanceof AvattavaEste) {
            tekstiLabelit[0] = new JLabel(tekstit[0]);
            paneli.add(tekstiLabelit[0]);

            AvattavaEste ae = (AvattavaEste)muokattavaKohde;
            triggeriLista = new JTextField();
            triggeriLista.setEditable(false);
            String s = "";
            for (Point p : ae.annaVaaditutTriggerit()) {
                s += p.x + "," + p.y + "; ";
            }
            triggeriLista.setText(s);
            paneli.add(triggeriLista);

            tekstiLabelit[1] = new JLabel(tekstit[1]);
            paneli.add(tekstiLabelit[1]);

            JButton lisääTriggeri = new JButton("Valitse triggerit");
            lisääTriggeri.addActionListener(e -> TriggerinValintaIkkuna.luoTriggerinValintaIkkuna((AvattavaEste)muokattavaKohde));
            paneli.add(lisääTriggeri);
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

