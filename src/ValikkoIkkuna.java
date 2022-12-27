import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class ValikkoIkkuna {
    
    static JPanel valikkoPaneli;
    static JPanel logoPaneli;
    static JPanel nappiPaneli;
    static JLabel logo;
    static JButton aloita, tekijät, asetukset, lopeta;

    

    static JPanel luoValikkoPaneli() {
        
        logo = new JLabel(new ImageIcon("tiedostot/kuvat/menu/logo.png"));

        logoPaneli = new JPanel();
        logoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        logoPaneli.add(logo);

        
        aloita = new JButton("Aloita peli");
        aloita.setPreferredSize(new Dimension(600, 100));
        aloita.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.crd.next(PääIkkuna.kortit);
                PääIkkuna.ikkuna.requestFocus();
            }
        });
        aloita.setAlignmentX(Component.CENTER_ALIGNMENT);
        aloita.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));

        asetukset = new JButton("Asetukset");
        asetukset.setPreferredSize(new Dimension(600, 100));
        asetukset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AsetusIkkuna.luoAsetusikkuna();
            }
        });
        asetukset.setAlignmentX(Component.CENTER_ALIGNMENT);
        asetukset.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));

        tekijät = new JButton("Kehittäjät");
        tekijät.setPreferredSize(new Dimension(600, 100));
        tekijät.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomViestiIkkunat.Credits.showDialog();
            }
        });
        tekijät.setAlignmentX(Component.CENTER_ALIGNMENT);
        tekijät.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));

        lopeta = new JButton("Lopeta");
        lopeta.setPreferredSize(new Dimension(600, 100));
        lopeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PääIkkuna.ikkuna.dispose();
            }
        });
        lopeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lopeta.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
    
        nappiPaneli = new JPanel();
        nappiPaneli.setLayout(new BoxLayout(nappiPaneli, BoxLayout.Y_AXIS));
        nappiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        nappiPaneli.add(aloita);
        nappiPaneli.add(asetukset);
        nappiPaneli.add(tekijät);
        nappiPaneli.add(lopeta);


        valikkoPaneli = new JPanel();
        valikkoPaneli.setLayout(new BorderLayout());
        valikkoPaneli.add(logoPaneli, BorderLayout.NORTH);
        valikkoPaneli.add(nappiPaneli, BorderLayout.SOUTH);
        
        return valikkoPaneli;
    }
}
