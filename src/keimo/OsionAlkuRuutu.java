package keimo;
import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

public class OsionAlkuRuutu {
    
    static JPanel osionAlkuPaneli;
    static JPanel kuvaPaneli;
    static JPanel tekstiPaneli;
    static JLabel kuva;

    static JLabel teksti;
    static JButton jatka;

    static int klikkaustenMäärä = 0;
    static int tarinanPituusRuutuina = 4;
    static String[] tarinaTeksti = new String[tarinanPituusRuutuina];
    static ImageIcon[] tarinanKuva = new ImageIcon[tarinanPituusRuutuina];
    

    static JPanel luoOsionAlkuPaneli() {

        kuva = new JLabel(new ImageIcon("tiedostot/kuvat/menu/kansikuva_puisto.png"));
        kuva.setPreferredSize(new Dimension(640, 400));

        kuvaPaneli = new JPanel();
        kuvaPaneli.setSize(640, 400);
        kuvaPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        kuvaPaneli.add(kuva);


        teksti = new JLabel("Tehtävä: Yritä löytää kotiin");
        teksti.setPreferredSize(new Dimension(640, 250));
        teksti.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        teksti.setAlignmentX(Component.CENTER_ALIGNMENT);

        jatka = new JButton("Seuraava");
        jatka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.crd.next(PääIkkuna.kortit);
                Peli.pause = false;
                PääIkkuna.ikkuna.requestFocus();
                Peli.peliAloitettu = true;
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


        osionAlkuPaneli = new JPanel();
        osionAlkuPaneli.setLayout(new BorderLayout());
        osionAlkuPaneli.add(kuvaPaneli, BorderLayout.NORTH);
        osionAlkuPaneli.add(tekstiPaneli, BorderLayout.CENTER);
        
        return osionAlkuPaneli;
    }
}
