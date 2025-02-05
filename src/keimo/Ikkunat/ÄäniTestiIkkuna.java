package keimo.Ikkunat;

import keimo.PääIkkuna;
import keimo.Utility.Downloaded.SpringUtilities;

import java.awt.BorderLayout;
import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ÄäniTestiIkkuna {

    static final int ikkunanLeveys = 600;
    static final int ikkunanKorkeus = 115;
    public static String ikkunaTeksti = "Äänitesti";
    static MediaPlayer musaToistin, ääniToistin;
    
    static JFrame ikkuna;
    static JLabel valitseMusiikkiLabel = new JLabel("Valitse musiikki");
    static JLabel valitseÄäniLabel = new JLabel("Valitse ääni");
    static JComboBox<Object> musiikkiValikko;
    static JComboBox<Object> ääniValikko;
    static JButton toistaMusiikkiNappi;
    static JButton toistaÄäniNappi;
    static JButton pysäytäMusiikkiNappi;
    static JButton pysäytäÄäniNappi;

    public static void luoÄäniTestiIkkuna() {

        JPanel paneli = new JPanel(new SpringLayout());

        musiikkiValikko = new JComboBox<>(listaaMusat());
        musiikkiValikko.setMaximumRowCount(20);
        toistaMusiikkiNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/toista_nappi.png"));
        toistaMusiikkiNappi.addActionListener(e -> toistaMusa("" + musiikkiValikko.getSelectedItem()));
        pysäytäMusiikkiNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/pysäytä_nappi.png"));
        pysäytäMusiikkiNappi.addActionListener(e -> pysäytäMusaToistin());
        paneli.add(valitseMusiikkiLabel);
        paneli.add(musiikkiValikko);
        paneli.add(toistaMusiikkiNappi);
        paneli.add(pysäytäMusiikkiNappi);

        ääniValikko = new JComboBox<>(listaaÄänet());
        ääniValikko.setMaximumRowCount(20);
        toistaÄäniNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/toista_nappi.png"));
        toistaÄäniNappi.addActionListener(e -> toistaÄäni("" + ääniValikko.getSelectedItem()));
        pysäytäÄäniNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/pysäytä_nappi.png"));
        pysäytäÄäniNappi.addActionListener(e -> pysäytäÄäniToistin());
        paneli.add(valitseÄäniLabel);
        paneli.add(ääniValikko);
        paneli.add(toistaÄäniNappi);
        paneli.add(pysäytäÄäniNappi);

        SpringUtilities.makeCompactGrid(paneli, 2, 4, 6, 6, 6, 6);

        ikkuna = new JFrame(ikkunaTeksti);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(AsetusIkkuna.ikkuna);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

    }

    private static Object[] listaaMusat() {
        return Stream.of(new File("tiedostot/musat").listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")))
            .map(File::getName).sorted()
            .collect(Collectors.toList()).toArray();
    }

    private static Object[] listaaÄänet() {
        return Stream.of(new File("tiedostot/äänet").listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")))
            .map(File::getName).sorted()
            .collect(Collectors.toList()).toArray();
    }

    private static void toistaMusa(String toistettava) {
        try {
            pysäytäMusaToistin();
            musaToistin = new MediaPlayer(new Media(new File("tiedostot/musat/" + toistettava).toURI().toString()));
            musaToistin.setCycleCount(MediaPlayer.INDEFINITE);
            musaToistin.play();
        }
        catch (NullPointerException e) {
            System.out.println("Musiikkia ei voitu toistaa");
            e.printStackTrace();
        }
    }

    private static void toistaÄäni(String toistettava) {
        try {
            ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/" + toistettava).toURI().toString()));
            ääniToistin.play();
        }
        catch (NullPointerException e) {
            System.out.println("Musiikkia ei voitu toistaa");
            e.printStackTrace();
        }
    }

    private static void pysäytäMusaToistin() {
        if (musaToistin != null) {
            musaToistin.stop();
        }
    }

    private static void pysäytäÄäniToistin() {
        if (ääniToistin != null) {
            ääniToistin.stop();
        }
    }
}
