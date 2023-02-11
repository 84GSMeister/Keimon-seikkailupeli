package keimo;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

public class TarinaRuutu {
    
    static JPanel tarinaPaneli;
    static JPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JButton jatka;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String[] tarinaTeksti = new String[tarinanPituusRuutuina];
    static ImageIcon[] tarinanKuva = new ImageIcon[tarinanPituusRuutuina];
    

    static JPanel luoTarinaPaneli() {
        
        tarinanKuva[0] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_1.png");
        tarinanKuva[1] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_2.gif");
        tarinanKuva[2] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_3.png");
        tarinanKuva[3] = new ImageIcon("tiedostot/kuvat/tarina/alku/tarina_alku_4.png");

        tarinaTeksti[0] = "<html><p>" +
        "Keimo herää auringon säteiden aiheuttamaan kipuun. " +
        "Nämä normaalisti harmittomat luonnonilmiöt olivat päättäneet, että juuri Keimon tulisi kärsiä seuraamuksia eilispäivän nautinnosta. " +
        "Yksikään kuolevainen ei ole kokenut yhtä murhaavaa krapulaa, kuin sitä, joka yritti paraikaa pitää Keimon maassa. " +
        "Keimo ei kuitenkaan ollut tavallinen kuolevainen, ja hän kykeni yli-inhimillisellä tahdonvoimallaan avaamaan silmänsä ja katsomaan ympärilleen. " +
        "</p></html>";

        tarinaTeksti[1] = "<html><p>" +
        "Valonsäteet jatkoivat Keimon kiusaamista myös sen jälkeen, kun hän oli saanut silmänsä auki. " +
        "Säteet eivät tuoneet hänen verkkokalvoilleen kuvia, joita hän olisi tunnistanut. " +
        "Sen sijaan Keimon verkkokalvot täyttyivät kuvista, jotka olisivat tuhonneet heikomman mielen." +
        "</p></html>";

        tarinaTeksti[2] = "<html><p>" +
        "Pilvet leijailivat taivalla ylösalaisin. Linnut kävelivät maassa sen sijaan, että lentäisivät taivaalla.<br>" +
        "Ja puistossa miehet ulkoiluttivat lapsiaan kun heidän vaimonsa olivat kotona katsomassa jääkiekkoa.<br>" +
        "Lyhyesti sanottuna maailma oli mennyt vinksalleen." +
        "</p></html>";

        tarinaTeksti[3] = "<html><p>" +
        "Sitten radiosta kuului lyhyt ilmoitus, jonka suuruuden vain Keimo käsitti: <br>" +
        "''Alienit ovat hyökänneet Suomeen. Pyydämme kaikkia Suomen kansalaisia palaamaan koteihinsa ja aseistamaan itsensä pesäpallomailoilla itsepuolustusta varten.''" +
        "</p></html>";

        klikkaustenMäärä = 0;

        kuva = new JLabel(tarinanKuva[0]);
        kuva.setPreferredSize(new Dimension(640, 400));

        kuvaPaneli = new JPanel();
        kuvaPaneli.setSize(640, 400);
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);


        teksti = new JLabel(tarinaTeksti[0]);
        teksti.setMinimumSize(new Dimension(560, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

        jatka = new JButton("Seuraava");
        jatka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                klikkaustenMäärä++;
                if (klikkaustenMäärä >= tarinanPituusRuutuina -1) {
                    jatka.setText("Aloita peli");
                }
                if (klikkaustenMäärä >= tarinanPituusRuutuina) {
                    PääIkkuna.crd.next(PääIkkuna.kortit);
                    ValikkoRuutu.valikkoPaneli.requestFocus();
                }
                else {
                    teksti.setText(tarinaTeksti[klikkaustenMäärä]);
                    kuva.setIcon(tarinanKuva[klikkaustenMäärä]);
                }
            }
        });
        jatka.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        jatka.setPreferredSize(new Dimension(640, 80));
        jatka.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        tekstiPaneli = new JPanel();
        GridBagLayout tekstiPanelinLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        tekstiPaneli.setLayout(tekstiPanelinLayout);
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        tekstiPaneli.add(teksti, gbc);
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 1;
        tekstiPaneli.add(jatka, gbc);


        tarinaPaneli = new JPanel();
        tarinaPaneli.setLayout(new BorderLayout());
        tarinaPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        tarinaPaneli.add(tekstiPaneli, BorderLayout.CENTER);
        
        return tarinaPaneli;
    }
}
