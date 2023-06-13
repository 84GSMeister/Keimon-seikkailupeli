package keimo.HuoneEditori;

import keimo.*;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.Utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class ReunaWarppiIkkuna {

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
    static JCheckBox warppiEnabloitu;

    static void hyväksyMuutokset(Suunta suunta, boolean warppaaTähänSuuntaan, int kohdeHuone) {
        switch (suunta) {
            case VASEN:
                HuoneEditoriIkkuna.warpVasen = warppaaTähänSuuntaan;
                HuoneEditoriIkkuna.warpVasenHuoneId = kohdeHuone;
            break;

            case OIKEA:
                HuoneEditoriIkkuna.warpOikea = warppaaTähänSuuntaan;
                HuoneEditoriIkkuna.warpOikeaHuoneId = kohdeHuone;
            break;

            case YLÖS:
                HuoneEditoriIkkuna.warpYlös = warppaaTähänSuuntaan;
                HuoneEditoriIkkuna.warpYlösHuoneId = kohdeHuone;
            break;

            case ALAS:
                HuoneEditoriIkkuna.warpAlas = warppaaTähänSuuntaan;
                HuoneEditoriIkkuna.warpAlasHuoneId = kohdeHuone;
            break;

            default:
            break;
        }
        HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäReunawarppienTiedot(HuoneEditoriIkkuna.warpVasen, HuoneEditoriIkkuna.warpVasenHuoneId, HuoneEditoriIkkuna.warpOikea, HuoneEditoriIkkuna.warpOikeaHuoneId, HuoneEditoriIkkuna.warpAlas, HuoneEditoriIkkuna.warpAlasHuoneId, HuoneEditoriIkkuna.warpYlös, HuoneEditoriIkkuna.warpYlösHuoneId);
        muokkausIkkuna.dispose();
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

    static void luoReunaWarppiIkkuna(boolean huoneOlemassa, Suunta suunta, boolean valitseViimeisinLuotuHuone, int viimeisimmänHuoneenNro) {
        

        valintojenMäärä = 2;
        tekstit = new String[valintojenMäärä];
        tekstiLabelit = new JLabel[valintojenMäärä];
        tekstiKentät = new JTextField[valintojenMäärä];
        paneli = new JPanel(new SpringLayout());

        tekstit[0] = "Warppi käytössä";
        tekstit[1] = "Valitse kohdehuone";

        tekstiLabelit[0] = new JLabel(tekstit[0]);
        paneli.add(tekstiLabelit[0]);
        warppiEnabloitu = new JCheckBox();
        paneli.add(warppiEnabloitu);

        tekstiLabelit[1] = new JLabel(tekstit[1]);
        paneli.add(tekstiLabelit[1]);
        huoneValikko = luoHuoneenNimiLista();
        paneli.add(huoneValikko);
        if (huoneOlemassa) {
            if (valitseViimeisinLuotuHuone) {
                int comboBoxinIndeksi = 0;
                for (int i = 0; i < huoneValikko.getItemCount(); i++) {
                    //System.out.println(huoneValikko.getItemAt(i).substring(huoneValikko.getItemAt(i).indexOf("(")+1, huoneValikko.getItemAt(i).indexOf(")")));
                    int haettavaHuoneenId = Integer.parseInt(huoneValikko.getItemAt(i).substring(huoneValikko.getItemAt(i).indexOf("(")+1, huoneValikko.getItemAt(i).indexOf(")")));
                    if (haettavaHuoneenId == viimeisimmänHuoneenNro) {
                        comboBoxinIndeksi = i;
                        break;
                    }
                }
                huoneValikko.setSelectedIndex(comboBoxinIndeksi);
                warppiEnabloitu.setSelected(valitseViimeisinLuotuHuone);
            }
            else {
                switch (suunta) {
                    case VASEN:
                        huoneValikko.setSelectedIndex(HuoneEditoriIkkuna.warpVasenHuoneId);
                        warppiEnabloitu.setSelected(HuoneEditoriIkkuna.warpVasen);
                    break;
                    case OIKEA:
                        huoneValikko.setSelectedIndex(HuoneEditoriIkkuna.warpOikeaHuoneId);
                        warppiEnabloitu.setSelected(HuoneEditoriIkkuna.warpOikea);
                    break;
                    case YLÖS:
                        huoneValikko.setSelectedIndex(HuoneEditoriIkkuna.warpYlösHuoneId);
                        warppiEnabloitu.setSelected(HuoneEditoriIkkuna.warpYlös);
                    break;
                    case ALAS:
                        huoneValikko.setSelectedIndex(HuoneEditoriIkkuna.warpAlasHuoneId);
                        warppiEnabloitu.setSelected(HuoneEditoriIkkuna.warpAlas);
                    break;
                }
            }
        }

        JButton okNappi = new JButton("Aseta");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    int asetettavaHuone = Integer.parseInt(huoneValikko.getSelectedItem().toString().substring(huoneValikko.getSelectedItem().toString().indexOf("(")+1, huoneValikko.getSelectedItem().toString().indexOf(")")));
                    hyväksyMuutokset(suunta, warppiEnabloitu.isSelected(), asetettavaHuone);
                    JOptionPane.showMessageDialog(null, "Huoneesta " + HuoneEditoriIkkuna.muokattavaHuone + " on nyt " + suunta + " warp huoneeseen " + asetettavaHuone + "\n\nTODO: Aseta automaattisesti warp myös takaisinpäin", "Warppi asetettu", JOptionPane.INFORMATION_MESSAGE);
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
        muokkausIkkuna.setTitle("Aseta reunawarppi: " + suunta);
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
