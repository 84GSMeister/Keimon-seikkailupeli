package keimo.Ruudut.Lisäruudut;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.TavoiteLista;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogiPätkä;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogit;
import keimo.Ikkunat.AsetusIkkuna;
import keimo.Ruudut.PeliRuutu;
import keimo.Ruudut.PeliRuutu.LisäRuutuPanelinTyyppi;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Utility.SpringUtilities;

import java.awt.*;
import javax.swing.*;

public class ValintaDialogiRuutu {
    
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

    public static void luoValintaDialogiIkkuna(String tekstiTunniste) {
        try {
            vdp = VuoropuheDialogit.vuoropuheDialogiKartta.get(tekstiTunniste);
            if (vdp != null) {
                if (vdp.onkoValinta()) {
                    Peli.pauseDialogi = true;
                    Peli.valintaDialogi = true;
                    PeliRuutu.lisäRuutuPanelinTyyppi = LisäRuutuPanelinTyyppi.VALINTADIALOGI;
                    valintaInt = 0;
                    JPanel panel = luoValintaDialogiPaneliGUI(vdp.annaValinnanNimi());
                    PeliRuutu.lisäRuutuPaneli.add(panel, BorderLayout.CENTER);
                    PeliRuutu.lisäRuutuPaneli.setVisible(true);
                    PeliRuutu.lisäRuutuPaneli.remove(PeliRuutu.pauseLabel);
                }
            }
            else if (tekstiTunniste == "pause" || tekstiTunniste == "silta") {
                Peli.pauseDialogi = true;
                Peli.valintaDialogi = true;
                PeliRuutu.lisäRuutuPanelinTyyppi = LisäRuutuPanelinTyyppi.VALINTADIALOGI;
                valintaInt = 0;
                JPanel panel = luoValintaDialogiPaneliGUI(tekstiTunniste);
                PeliRuutu.lisäRuutuPaneli.add(panel, BorderLayout.CENTER);
                PeliRuutu.lisäRuutuPaneli.setVisible(true);
                PeliRuutu.lisäRuutuPaneli.remove(PeliRuutu.pauseLabel);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static VuoropuheDialogiPätkä vdp;
    
    protected static JPanel testPanel;
    private static JLabel otsikkoLabel;
    private static JPanel valintaPaneli;
    public static JLabel[] vasenOsoitin;
    private static JLabel[] valinnat;
    private static JLabel edistymisLabel;
    private static String valintaDialoginTunniste1;

    private static JPanel luoValintaDialogiPaneliGUI(String valintaDialoginTunniste) {
        valintaDialoginTunniste1 = valintaDialoginTunniste;
        //vdp = VuoropuheDialogit.vuoropuheDialogiKartta.get(valintaDialoginTunniste);
        testPanel = new JPanel();
        testPanel.removeAll();
        if (valintaDialoginTunniste == "pause" || valintaDialoginTunniste == "silta") {
            switch (valintaDialoginTunniste) {
                case "pause":
                    otsikkoLabel = new JLabel("Pause");
                    otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 24));
                    otsikkoLabel.setPreferredSize(new Dimension(300, 100));

                    valintojenMäärä = 4;
                    vasenOsoitin = new JLabel[valintojenMäärä];
                    for (int i = 0; i < vasenOsoitin.length; i++) {
                        vasenOsoitin[i] = new JLabel();
                    }

                    valinnat = new JLabel[valintojenMäärä];
                    valinnat[0] = new JLabel("Jatka");
                    valinnat[1] = new JLabel("Asetukset");
                    valinnat[2] = new JLabel("Uusi peli");
                    valinnat[3] = new JLabel("Lopeta");
                    for (int i = 0; i < valintojenMäärä; i++) {
                        valinnat[i].setFont(new Font("Courier10 BT", Font.PLAIN, 30));
                    }

                    valintaPaneli = new JPanel(new SpringLayout());
                    for (int i = 0; i < valintojenMäärä; i++) {
                        valintaPaneli.add(vasenOsoitin[i]);
                        valintaPaneli.add(valinnat[i]);
                    }
                    valintaPaneli.setPreferredSize(new Dimension(300, 200));
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
                case "silta":
                    otsikkoLabel = new JLabel("Hyppää?");
                    otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 24));
                    otsikkoLabel.setPreferredSize(new Dimension(300, 100));

                    valintojenMäärä = 2;
                    vasenOsoitin = new JLabel[valintojenMäärä];
                    vasenOsoitin[0] = new JLabel();
                    vasenOsoitin[1] = new JLabel();

                    valinnat = new JLabel[valintojenMäärä];
                    valinnat[0] = new JLabel("Kyllä");
                    valinnat[1] = new JLabel("Ei");
                    for (int i = 0; i < valintojenMäärä; i++) {
                        valinnat[i].setFont(new Font("Courier10 BT", Font.PLAIN, 30));
                    }

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
                    otsikkoLabel = new JLabel("<html><p>Polku pimeälle puolelle, onko?</p></html>");
                    otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 16));
                    otsikkoLabel.setPreferredSize(new Dimension(300, 100));

                    valintojenMäärä = 2;
                    vasenOsoitin = new JLabel[valintojenMäärä];
                    vasenOsoitin[0] = new JLabel();
                    vasenOsoitin[1] = new JLabel();

                    valinnat = new JLabel[valintojenMäärä];
                    valinnat[0] = new JLabel("Hrmm...no kyllä on!");
                    valinnat[1] = new JLabel("Ei");
                    for (int i = 0; i < valintojenMäärä; i++) {
                        valinnat[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
                    }

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
        }
        else {
            if (vdp.onkoValinta()) {
                otsikkoLabel = new JLabel("<html><p>" + vdp.annaValinnanOtsikko() + "</p></html>");
                otsikkoLabel.setFont(new Font("Courier10 BT", Font.PLAIN, 16));
                otsikkoLabel.setPreferredSize(new Dimension(300, 100));

                valintojenMäärä = vdp.annaValinnanVaihtoehdot().length;
                vasenOsoitin = new JLabel[valintojenMäärä];
                //vasenOsoitin[0] = new JLabel();
                //vasenOsoitin[1] = new JLabel();

                valinnat = new JLabel[valintojenMäärä];
                //valinnat[0] = new JLabel("Hrmm...no kyllä on!");
                //valinnat[1] = new JLabel("Ei");
                valintaPaneli = new JPanel(new SpringLayout());
                for (int i = 0; i < valintojenMäärä; i++) {
                    vasenOsoitin[i] = new JLabel();
                    valinnat[i] = new JLabel(vdp.annaValinnanVaihtoehdot()[i]);
                    valinnat[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
                    valintaPaneli.add(vasenOsoitin[i]);
                    valintaPaneli.add(valinnat[i]);
                }

                // valintaPaneli = new JPanel(new SpringLayout());
                // for (int i = 0; i < valintojenMäärä; i++) {
                //     valintaPaneli.add(vasenOsoitin[i]);
                //     valintaPaneli.add(valinnat[i]);
                // }
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
            }
            else {
                return null;
            }
        }
        
        return testPanel;
    }

    public static void hyväksyValinta() {
        if (valintaDialoginTunniste1 == "pause" || valintaDialoginTunniste1 == "silta") {
            switch (valintaDialoginTunniste1) {
                case "pause":
                    switch (valintaInt) {
                        case 0:
                            Peli.pausetaPeli(false);
                            Peli.pauseDialogi = false;
                            Peli.valintaDialogi = false;
                            PeliRuutu.lisäRuutuPaneli.removeAll();
                            PeliRuutu.lisäRuutuPaneli.revalidate();
                            PeliRuutu.lisäRuutuPaneli.repaint();
                            PeliRuutu.lisäRuutuPaneli.setVisible(false);
                        break;
                        case 1:
                            AsetusIkkuna.luoAsetusikkuna();
                        break;
                        case 2:
                            PääIkkuna.uusiIkkuna = true;
                        break;
                        case 3:
                            System.exit(0);
                        break;
                    }
                break;
                case "silta":
                    if (valintaInt == 0) {
                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_SILLALTA_ALAS;
                        Pelaaja.hp = 0;
                    }
                    PääIkkuna.edistäDialogia();
                    Peli.pauseDialogi = false;
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
        else {
            // if (valintaInt == 0) {
            //     TavoiteLista.suoritaTavoite("Avaa takahuone");
            //     PääIkkuna.avaaPitkäDialogiRuutu("goblin_kyllä", null);
            // }
            // else {
            //     PääIkkuna.avaaPitkäDialogiRuutu("goblin_ei", null);
            // }
            if (vdp.annaTriggerit() != null) {
                if (vdp.annaTriggerit()[valintaInt] != null) {
                    TavoiteLista.suoritaTavoite(vdp.annaTriggerit()[valintaInt]);
                }
            }
            PääIkkuna.avaaPitkäDialogiRuutu(vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt]);
            Peli.valintaDialogi = false;
            PeliRuutu.lisäRuutuPaneli.removeAll();
            PeliRuutu.lisäRuutuPaneli.revalidate();
            PeliRuutu.lisäRuutuPaneli.repaint();
            PeliRuutu.lisäRuutuPaneli.setVisible(false);
        }
        
    }
    public static void peruValinta() {
        switch (valintaDialoginTunniste1) {
            case "pause":
                Peli.pausetaPeli(false);
                Peli.pauseDialogi = false;
                Peli.valintaDialogi = false;
                PeliRuutu.lisäRuutuPaneli.removeAll();
                PeliRuutu.lisäRuutuPaneli.revalidate();
                PeliRuutu.lisäRuutuPaneli.repaint();
                PeliRuutu.lisäRuutuPaneli.setVisible(false);
            break;
        }
    }
}
