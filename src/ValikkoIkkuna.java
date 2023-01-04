import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class ValikkoIkkuna {
    
    static JPanel valikkoPaneli;
    static JPanel logoPaneli, välityhjäPaneli, nappiPaneli, nappiPaneliUlompi;
    static JLabel logo;
    //static JButton aloita, tekijät, huoneEditori, asetukset, lopeta;
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
            case 0:
                PääIkkuna.crd.next(PääIkkuna.kortit);
                //PääIkkuna.ikkuna.requestFocus();
                break;
            case 1:
                AsetusIkkuna.luoAsetusikkuna();
                break;
            case 2:
                CustomViestiIkkunat.ToteutusPuuttuu.showDialog();
                break;
            case 3:
                CustomViestiIkkunat.Credits.showDialog();
                break;
            case 4:
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
        
        //aloita = new JButton(new ImageIcon("tiedostot/kuvat/menu/main_aloita.png"));
        //aloita.setPreferredSize(new Dimension(440, 56));
        //aloita.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        hyväksy(0);
        //    }
        //});
        aloita = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_aloita.png"));
        aloita.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[0] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_osoitin.png"));
        //aloitaVasenOsoitin = new JLabel(new ImageIcon("tiedostot/kuvat/nuoli_oikea.png"));
        //aloitaOikeaOsoitin = new JLabel(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"));

        //asetukset = new JButton(new ImageIcon("tiedostot/kuvat/menu/main_asetukset.png"));
        //asetukset.setPreferredSize(new Dimension(440, 56));
        //asetukset.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        hyväksy(1);
        //    }
        //});
        asetukset = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_asetukset.png"));
        asetukset.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[1] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        //asetuksetVasenOsoitin = new JLabel();
        //asetuksetOikeaOsoitin = new JLabel();

        //huoneEditori = new JButton(new ImageIcon("tiedostot/kuvat/menu/main_editori.png"));
        //huoneEditori.setPreferredSize(new Dimension(440, 56));
        //huoneEditori.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        hyväksy(2);
        //    }
        //});
        huoneEditori = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_editori.png"));
        huoneEditori.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[2] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        //huoneEditoriVasenOsoitin = new JLabel();
        //huoneEditoriOikeaOsoitin = new JLabel();

        //tekijät = new JButton(new ImageIcon("tiedostot/kuvat/menu/main_kehittäjät.png"));
        //tekijät.setPreferredSize(new Dimension(440, 56));
        //tekijät.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        hyväksy(3);
        //    }
        //});
        tekijät = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_kehittäjät.png"));
        tekijät.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[3] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        //tekijätVasenOsoitin = new JLabel();
        //tekijätOikeaOsoitin = new JLabel();

        //lopeta = new JButton(new ImageIcon("tiedostot/kuvat/menu/main_lopeta.png"));
        //lopeta.setPreferredSize(new Dimension(440, 56));
        //lopeta.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        hyväksy(4);
        //    }
        //});
        lopeta = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_lopeta.png"));
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        vasenOsoitin[4] = new JLabel(new ImageIcon("tiedostot/kuvat/menu/main_tyhjä.png"));
        //lopetaVasenOsoitin = new JLabel();
        //lopetaOikeaOsoitin = new JLabel();
    
        nappiPaneli = new JPanel();
        //nappiPaneli.setLayout(new BoxLayout(nappiPaneli, BoxLayout.Y_AXIS));
        nappiPaneli.setLayout(new SpringLayout());
        nappiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        nappiPaneli.setBackground(Color.black);
        nappiPaneli.setPreferredSize(new Dimension(500, 400));
        //nappiPaneli.add(aloitaVasenOsoitin);
        nappiPaneli.add(vasenOsoitin[0]);
        nappiPaneli.add(aloita);
        //nappiPaneli.add(aloitaOikeaOsoitin);
        //nappiPaneli.add(asetuksetVasenOsoitin);
        nappiPaneli.add(vasenOsoitin[1]);
        nappiPaneli.add(asetukset);
        //nappiPaneli.add(asetuksetOikeaOsoitin);
        //nappiPaneli.add(huoneEditoriVasenOsoitin);
        nappiPaneli.add(vasenOsoitin[2]);
        nappiPaneli.add(huoneEditori);
        //nappiPaneli.add(huoneEditoriOikeaOsoitin);
        //nappiPaneli.add(tekijätVasenOsoitin);
        nappiPaneli.add(vasenOsoitin[3]);
        nappiPaneli.add(tekijät);
        //nappiPaneli.add(tekijätOikeaOsoitin);
        //nappiPaneli.add(lopetaVasenOsoitin);
        nappiPaneli.add(vasenOsoitin[4]);
        nappiPaneli.add(lopeta);
        //nappiPaneli.add(lopetaOikeaOsoitin);
        SpringUtilities.makeCompactGrid(nappiPaneli, 5, 2, 6, 6, 6, 6);

        nappiPaneliUlompi = new JPanel();
        nappiPaneliUlompi.setLayout(new CardLayout(70, 75));
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
