package keimo.Ruudut.Lisäruudut;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.TavoiteLista;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.PeliRuutu.LisäRuutuPanelinTyyppi;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Utility.SpringUtilities;

import java.awt.*;
import javax.swing.*;

public class ValintaDialogiIkkuna {
    
    public static int valintaInt = 0;
    private static int valintojenMäärä = 0;

    public static void pienennäValintaa() {
        if (valintaInt <= 0) {
            valintaInt = valintojenMäärä-1;
        }
        else {
            valintaInt--;
        }
    }
    public static void kasvataValintaa() {
        if (valintaInt >= valintojenMäärä-1) {
            valintaInt = 0;
        }
        else {
            valintaInt++;
        }
    }

    public static void luoValintaDialogiIkkuna(String valintaDialoginTunniste) {
        try {
            Peli.pause = true;
            Peli.valintaDialogi = true;
            PeliRuutu.lisäRuutuPanelinTyyppi = LisäRuutuPanelinTyyppi.VALINTADIALOGI;
            valintaInt = 0;
            JPanel panel = luoValintaDialogiPaneliGUI(valintaDialoginTunniste);
            PeliRuutu.lisäRuutuPaneli.add(panel, BorderLayout.CENTER);
            PeliRuutu.lisäRuutuPaneli.setVisible(true);
            PeliRuutu.lisäRuutuPaneli.remove(PeliRuutu.pauseLabel);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static JPanel testPanel;
    private static JLabel otsikkoLabel;
    private static JPanel valintaPaneli;
    public static JLabel[] vasenOsoitin;
    private static JLabel[] valinnat;
    private static JLabel edistymisLabel;
    private static String valintaDialoginTunniste1;

    private static JPanel luoValintaDialogiPaneliGUI(String valintaDialoginTunniste){
        valintaDialoginTunniste1 = valintaDialoginTunniste;
        switch (valintaDialoginTunniste) {
            case "silta":
                testPanel = new JPanel();
                testPanel.removeAll();

                otsikkoLabel = new JLabel("Hyppää?");
                otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 24));
                otsikkoLabel.setPreferredSize(new Dimension(300, 100));

                valintojenMäärä = 2;
                vasenOsoitin = new JLabel[2];
                vasenOsoitin[0] = new JLabel();
                vasenOsoitin[1] = new JLabel();

                valinnat = new JLabel[2];
                valinnat[0] = new JLabel("Kyllä");
                valinnat[0].setFont(new Font("Courier10 BT", Font.PLAIN, 30));
                valinnat[1] = new JLabel("Ei");
                valinnat[1].setFont(new Font("Courier10 BT", Font.PLAIN, 30));

                valintaPaneli = new JPanel(new SpringLayout());
                for (int i = 0; i < valintojenMäärä; i++) {
                    valintaPaneli.add(vasenOsoitin[i]);
                    valintaPaneli.add(valinnat[i]);
                }
                valintaPaneli.setPreferredSize(new Dimension(300, 100));
                SpringUtilities.makeCompactGrid(valintaPaneli, valintojenMäärä, 2, 6, 6, 6, 6);
        
                edistymisLabel = new JLabel("");
                edistymisLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 20));
                edistymisLabel.setPreferredSize(new Dimension(300, 100));
        
                testPanel.setPreferredSize(new Dimension(300, 300));
                testPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                testPanel.add(otsikkoLabel, BorderLayout.NORTH);
                testPanel.add(valintaPaneli, BorderLayout.CENTER);
                testPanel.add(edistymisLabel, BorderLayout.SOUTH);
            break;
            case "goblin":
                testPanel = new JPanel();
                testPanel.removeAll();

                otsikkoLabel = new JLabel("<html><p>Polku pimeälle puolelle, onko?</p></html>");
                otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 16));
                otsikkoLabel.setPreferredSize(new Dimension(300, 100));

                valintojenMäärä = 2;
                vasenOsoitin = new JLabel[2];
                vasenOsoitin[0] = new JLabel();
                vasenOsoitin[1] = new JLabel();

                valinnat = new JLabel[2];
                valinnat[0] = new JLabel("Hrmm...no kyllä on!");
                valinnat[0].setFont(new Font("Courier10 BT", Font.PLAIN, 30));
                valinnat[1] = new JLabel("Ei");
                valinnat[1].setFont(new Font("Courier10 BT", Font.PLAIN, 30));

                valintaPaneli = new JPanel(new SpringLayout());
                for (int i = 0; i < valintojenMäärä; i++) {
                    valintaPaneli.add(vasenOsoitin[i]);
                    valintaPaneli.add(valinnat[i]);
                }
                valintaPaneli.setPreferredSize(new Dimension(300, 100));
                SpringUtilities.makeCompactGrid(valintaPaneli, valintojenMäärä, 2, 6, 6, 6, 6);
        
                edistymisLabel = new JLabel("");
                edistymisLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 20));
                edistymisLabel.setPreferredSize(new Dimension(300, 100));
        
                testPanel.setPreferredSize(new Dimension(300, 300));
                testPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                testPanel.add(otsikkoLabel, BorderLayout.NORTH);
                testPanel.add(valintaPaneli, BorderLayout.CENTER);
                testPanel.add(edistymisLabel, BorderLayout.SOUTH);
            break;
        }
   
        return testPanel;
    }

    public static void hyväksyValinta() {
        switch (valintaDialoginTunniste1) {
            case "silta":
                if (valintaInt == 0) {
                    TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_SILLALTA_ALAS;
                    Pelaaja.hp = 0;
                }
                Peli.pause = false;
                Peli.valintaDialogi = false;
                PeliRuutu.lisäRuutuPaneli.removeAll();
                PeliRuutu.lisäRuutuPaneli.revalidate();
                PeliRuutu.lisäRuutuPaneli.repaint();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
            break;
            case "goblin":
                if (valintaInt == 0) {
                    TavoiteLista.suoritaTavoite("Avaa takahuone");
                    PääIkkuna.avaaPitkäDialogiRuutu("goblin_kyllä");
                }
                else {
                    PääIkkuna.avaaPitkäDialogiRuutu("goblin_ei");
                }
                Peli.valintaDialogi = false;
                PeliRuutu.lisäRuutuPaneli.removeAll();
                PeliRuutu.lisäRuutuPaneli.revalidate();
                PeliRuutu.lisäRuutuPaneli.repaint();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
            break;
        }
    }
}
