package keimo;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class ValikkoRuutu {
    
    static JPanel valikkoPaneli;
    static JPanel logoPaneli, välityhjäPaneli, nappiPaneli, nappiPaneliUlompi;
    static JLabel logo;
    static JLabel aloita, tekijät, huoneEditori, asetukset, lopeta;
    static JLabel aloitaVasenOsoitin, aloitaOikeaOsoitin, tekijätVasenOsoitin, tekijätOikeaOsoitin, asetuksetVasenOsoitin, asetuksetOikeaOsoitin;
    static JLabel huoneEditoriVasenOsoitin, huoneEditoriOikeaOsoitin, lopetaVasenOsoitin, lopetaOikeaOsoitin;
    static final int vaihtoehtojenMäärä = 5;
    static int valinta = 0;
    static JLabel[] vasenOsoitin;

    
    static void painaNäppäintä(String näppäin) {

        switch (näppäin) {
            case "ylös":
                valinta--;
                if (valinta < 0) {
                    valinta = 4;
                }
                päivitäOsoittimenSijainti(valinta);
                break;
            case "alas":
                valinta++;
                if (valinta > 4) {
                    valinta = 0;
                }
                päivitäOsoittimenSijainti(valinta);
                break;
            case "enter":
                hyväksy(valinta);
                break;
            default:
                break;
        }
    }

    static void päivitäOsoittimenSijainti(int valinta) {
        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            if (i == valinta) {
                vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
            }
            else {
                vasenOsoitin[i].setIcon(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
            }
        }
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Aloita peli
                PääIkkuna.crd.next(PääIkkuna.kortit);
                //PääIkkuna.ikkuna.requestFocus();
                break;
            case 1: // Asetukset
                AsetusIkkuna.luoAsetusikkuna();
                break;
            case 2: // Huone-editori
                HuoneEditoriIkkuna.luoEditoriIkkuna();
                break;
            case 3: // Kehittäjät
                CustomViestiIkkunat.Credits.showDialog();
                break;
            case 4: // Lopeta
                System.exit(0);
                break;
            default:
                break;
        }
    }

    static JPanel luoValikkoPaneli() {
        
        logo = new JLabel(new ImageIcon("tiedostot/kuvat/menu/KEIMON_logo.png"));

        logoPaneli = new JPanel();
        logoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        logoPaneli.setBackground(Color.black);
        logoPaneli.add(logo);

        vasenOsoitin = new JLabel[vaihtoehtojenMäärä];
        
        aloita = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_aloita.png"));
        aloita.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[0] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        asetukset = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_asetukset.png"));
        asetukset.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[1] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        huoneEditori = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_editori.png"));
        huoneEditori.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[2] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        tekijät = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_kehittäjät.png"));
        tekijät.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[3] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        lopeta = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_lopeta.png"));
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[4] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));

        vasenOsoitin[valinta] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
    
        nappiPaneli = new JPanel();
        SpringLayout nappiPanelinlayout = new SpringLayout();
        nappiPanelinlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, vasenOsoitin[0], 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneli);
        nappiPanelinlayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, aloita, 0, SpringLayout.HORIZONTAL_CENTER, nappiPaneli);
        nappiPaneli.setLayout(nappiPanelinlayout);
        nappiPaneli.setBackground(Color.black);
        nappiPaneli.setPreferredSize(new Dimension(500, 400));
        nappiPaneli.add(vasenOsoitin[0]);
        nappiPaneli.add(aloita);
        nappiPaneli.add(vasenOsoitin[1]);
        nappiPaneli.add(asetukset);
        nappiPaneli.add(vasenOsoitin[2]);
        nappiPaneli.add(huoneEditori);
        nappiPaneli.add(vasenOsoitin[3]);
        nappiPaneli.add(tekijät);
        nappiPaneli.add(vasenOsoitin[4]);
        nappiPaneli.add(lopeta);
        SpringUtilities.makeCompactGrid(nappiPaneli, 5, 2, 6, 6, 6, 6);

        nappiPaneliUlompi = new JPanel();
        nappiPaneliUlompi.setLayout(new GridBagLayout());
        nappiPaneliUlompi.setBackground(Color.black);
        nappiPaneliUlompi.add(nappiPaneli);


        valikkoPaneli = new JPanel();
        valikkoPaneli.setLayout(new BorderLayout());
        valikkoPaneli.add(logoPaneli, BorderLayout.NORTH);
        valikkoPaneli.add(nappiPaneliUlompi, BorderLayout.CENTER);
        valikkoPaneli.setBackground(Color.black);
        valikkoPaneli.requestFocus();
        
        return valikkoPaneli;
    }
}
