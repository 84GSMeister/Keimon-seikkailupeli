import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

public class TarinaIkkuna {
    
    static JPanel tarinaPaneli;
    static JPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JButton jatka;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String[] tarinaTeksti = new String[tarinanPituusRuutuina];
    

    static JPanel luoTarinaPaneli() {
        
        tarinaTeksti[0] = "<html><p>" +
        "Keimo herää auringon säteiden aiheuttamaan kipuun. " +
        "Nämä normaalisti harmittomat luonnonilmiöt olivat päättäneet, " + 
        "että juuri Keimon tulisi kärsiä seuraamuksia eilispäivän nautinnosta. " +
        "Yksikään kuolevainen ei ole kokenut yhtä murhaavaa krapulaa, kuin sitä, " +
        "joka yritti paraikaa pitää Keimon maassa. " +
        "Keimo ei kuitenkaan ollut tavallinen kuolevainen, " +
        "ja hän kykeni yli-inhimillisellä tahdonvoimallaan avaamaan silmänsä " +
        "ja katsomaan ympärilleen. " +
        "</p></html>";

        tarinaTeksti[1] = "<html><p>" +
        "Valonsäteet jatkoivat Keimon kiusaamista myös sen jälkeen, kun hän oli saanut silmänsä auki. " +
        "Säteet eivät tuoneet hänen verkkokalvoilleen kuvia, joita hän olisi tunnistanut. " +
        "Sen sijaan Keimon verkkokalvot täyttyivät kuvista, jotka olisivat tuhonneet heikomman mielen." +
        "</p></html>";

        tarinaTeksti[2] = "<html><p>" +
        "Teksti 3" +
        "</p></html>";

        tarinaTeksti[3] = "<html><p>" +
        "Teksti 4" +
        "</p></html>";

        klikkaustenMäärä = 0;

        kuva = new JLabel(new ImageIcon("tiedostot/kuvat/menu/tarina_alku_1.png"));
        kuva.setPreferredSize(new Dimension(640, 400));

        kuvaPaneli = new JPanel();
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);


        teksti = new JLabel("<html><p>Keimo herää auringon säteiden aiheuttamaan kipuun. " +
        "Nämä normaalisti harmittomat luonnonilmiöt olivat päättäneet, " + 
        "että juuri Keimon tulisi kärsiä seuraamuksia eilispäivän nautinnosta. " +
        "Yksikään kuolevainen ei ole kokenut yhtä murhaavaa krapulaa, kuin sitä, " +
        "joka yritti paraikaa pitää Keimon maassa. " +
        "Keimo ei kuitenkaan ollut tavallinen kuolevainen, " +
        "ja hän kykeni yli-inhimillisellä tahdonvoimallaan avaamaan silmänsä " +
        "ja katsomaan ympärilleen.</p></html>");
        teksti.setPreferredSize(new Dimension(640, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

        jatka = new JButton("Seuraava");
        jatka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                klikkaustenMäärä++;
                if (klikkaustenMäärä >= 4) {
                    PääIkkuna.crd.next(PääIkkuna.kortit);
                    Main.pause = false;
                    PääIkkuna.ikkuna.requestFocus();
                }
                else {
                    teksti.setText(tarinaTeksti[klikkaustenMäärä]);
                }
            }
        });
        jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        jatka.setPreferredSize(new Dimension(640, 80));
        jatka.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new FlowLayout(FlowLayout.TRAILING));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        tekstiPaneli.add(teksti);
        tekstiPaneli.add(jatka);


        tarinaPaneli = new JPanel();
        tarinaPaneli.setLayout(new BoxLayout(tarinaPaneli, BoxLayout.Y_AXIS));
        tarinaPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        tarinaPaneli.add(tekstiPaneli, BorderLayout.SOUTH);
        
        return tarinaPaneli;
    }
}
