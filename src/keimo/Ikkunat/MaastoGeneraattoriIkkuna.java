package keimo.Ikkunat;

import keimo.*;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.Utility.SpringUtilities;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class MaastoGeneraattoriIkkuna {

    static final int ikkunanLeveys = 420;
    static final int ikkunanKorkeus = 165;
    static JFrame maastoIkkuna;
    static String[] tekstit = {"Luo objektikenttä kuvan avulla", "Luo maasto kuvan avulla", "Tarkastele maastoa"};
    static int valintojenMäärä = tekstit.length;
    static JButton valitseKuva, valitseKuva2;
    static JLabel maastonTarkastelu;

    static int[][] objektiKuvanVäriArvot;
    static int[][] objektiKuvanVäriArvotPunainen;
    static int[][] objektiKuvanVäriArvotVihreä;
    static int[][] objektiKuvanVäriArvotSininen;

    // static int[][] maastoKuvanVäriArvot;
    // static int[][] maastoKuvanVäriArvotPunainen;
    // static int[][] maastoKuvanVäriArvotVihreä;
    // static int[][] maastoKuvanVäriArvotSininen;

    static Maasto[][] kuvaMaasto;
    static KenttäKohde[][] kuvaKenttä;

    static boolean objektitValittu = false;
    static boolean maastoValittu = false;

    static void tarkistaArvot() {
        kuvaKenttä = new KenttäKohde[10][10];
        kuvaMaasto = new Maasto[10][10];
        try {
            if (objektitValittu) {
                for (int i = 0; i < objektiKuvanVäriArvot.length; i++) {
                    for (int j = 0; j < objektiKuvanVäriArvot.length; j++) {
                        System.out.println("" + j + ", " + i + " - Väri: " + objektiKuvanVäriArvot[j][i]);
                        if (objektiKuvanVäriArvot[j][i] == 0x00D0C030) {
                            kuvaKenttä[j][i] = new Avain(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x001E1107) {
                            kuvaKenttä[j][i] = new Hiili(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x001B2644) {
                            kuvaKenttä[j][i] = new Kaasupullo(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x0032416B) {
                            kuvaKenttä[j][i] = new Kaasusytytin(true, j, i, null);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00666666) {
                            kuvaKenttä[j][i] = new Kilpi(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x005B3318) {
                            kuvaKenttä[j][i] = new Kirstu(true, j, i, null);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00BA5E6B) {
                            kuvaKenttä[j][i] = new Makkara(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00232330) {
                            kuvaKenttä[j][i] = new Nuotio(true, j, i, null);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00CE631C) {
                            kuvaKenttä[j][i] = new Oviruutu(j, i, null);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00AD1616) {
                            //kuvaKenttä[j][i] = new PahaVihu(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00E5DD72) {
                            kuvaKenttä[j][i] = new Paperi(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x0000FF00) {
                            kuvaKenttä[j][i] = null;
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00AA4949) {
                            //kuvaKenttä[j][i] = new PikkuVihu_KenttäKohde(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x007F4A00) {
                            kuvaKenttä[j][i] = new Suklaalevy(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x0029722E) {
                            kuvaKenttä[j][i] = new Vesiämpäri(true, j, i);
                        }
                        else if (objektiKuvanVäriArvot[j][i] == 0x00006806) {
                            kuvaKenttä[j][i] = new Ämpärikone(true, j, i, null);
                        }
                        else {
                            kuvaKenttä[j][i] = null;
                        }
                    }
                }
            }
            // if (maastoValittu) {
            //     for (int i = 0; i < maastoKuvanVäriArvot.length; i++) {
            //         for (int j = 0; j < maastoKuvanVäriArvot.length; j++) {
            //             if (maastoKuvanVäriArvotPunainen[j][i] < 20 && maastoKuvanVäriArvotVihreä[j][i] < 150 && maastoKuvanVäriArvotSininen[j][i] > 200) {
            //                 kuvaMaasto[j][i] = new Vesi();
            //             }
            //             else if (maastoKuvanVäriArvotPunainen[j][i] > 200 && maastoKuvanVäriArvotVihreä[j][i] > 200 && maastoKuvanVäriArvotSininen[j][i] < 20) {
            //                 kuvaMaasto[j][i] = new Hiekka();
            //             }
            //             else {
            //                 kuvaMaasto[j][i] = null;
            //             }
            //         }
            //     }
            // }
            asetaArvot();
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Nyt näyttää siltä, että jotain on mennyt särki!\n\nIlmoitathan kehittäjille, mitä teit, että sait tämän tapahtumaan.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Ei valittua tiedostoa.", "Ei voi ladata!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    static void asetaArvot() {
        //Peli.maastokenttä = kuvaMaasto;
        //Peli.pelikenttä = kuvaKenttä;
    }

    public static void luoMaastoGeneraattoriIkkuna() {
        
        JPanel paneli = new JPanel(new SpringLayout());

        valitseKuva = new JButton("Valitse kuva objekteja varten");
        valitseKuva.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    JOptionPane.showMessageDialog(null, "Tämän työkalun avulla voit luoda maaston png-kuvan värien perusteella.\nKuvan täytyy olla 10 x 10 -kokoinen.", "Luo maasto kuvan perusteella", JOptionPane.INFORMATION_MESSAGE);
                    JFileChooser tiedostoSelain = new JFileChooser(".\\");
                    FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png)", "png");
                    tiedostoSelain.setFileFilter(tiedostoSuodatin);
                    int valinta = tiedostoSelain.showOpenDialog(maastoIkkuna);
                    if (valinta == JFileChooser.APPROVE_OPTION) {
                        String väriArvot = "";
                        File tiedosto = tiedostoSelain.getSelectedFile();
                        try {
                            BufferedImage kuva = ImageIO.read(tiedosto);
                            objektiKuvanVäriArvot = new int[kuva.getWidth()][kuva.getHeight()];
                            objektiKuvanVäriArvotPunainen = new int[kuva.getWidth()][kuva.getHeight()];
                            objektiKuvanVäriArvotVihreä = new int[kuva.getWidth()][kuva.getHeight()];
                            objektiKuvanVäriArvotSininen = new int[kuva.getWidth()][kuva.getHeight()];
                            if (kuva.getWidth() == 10 && kuva.getHeight() == 10) {
                                for (int i = 0; i < kuva.getHeight(); i++) {
                                    for (int j = 0; j < kuva.getWidth(); j++) {
                                        objektiKuvanVäriArvot[j][i] = kuva.getRGB(j, i);
                                        objektiKuvanVäriArvotPunainen[j][i] = (objektiKuvanVäriArvot[j][i] & 0x00ff0000) >> 16;
                                        objektiKuvanVäriArvotVihreä[j][i] = (objektiKuvanVäriArvot[j][i] & 0x0000ff00) >> 8;
                                        objektiKuvanVäriArvotSininen[j][i] = (objektiKuvanVäriArvot[j][i] & 0x000000ff);
                                        objektiKuvanVäriArvot[j][i] = objektiKuvanVäriArvotSininen[j][i] + 256 * objektiKuvanVäriArvotVihreä[j][i] + 65536 * objektiKuvanVäriArvotPunainen[j][i];
                                        väriArvot += "Pikseli " + j + "_" + i + " - Värit: " + objektiKuvanVäriArvot[j][i] + " - Punainen: " + objektiKuvanVäriArvotPunainen[j][i] + ", Vihreä: " + objektiKuvanVäriArvotVihreä[j][i] + ", Sininen: " + objektiKuvanVäriArvotSininen[j][i] + ";\n";
                                    }
                                }
                                objektitValittu = true;
                                System.out.println(väriArvot);
                                JOptionPane.showMessageDialog(null, "<html><p>" + väriArvot + "</p></html>", "Väriarvot", JOptionPane.INFORMATION_MESSAGE);
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Vain 10 x 10 -kuvat kelpaavat.", "Virheellinen kuvatiedosto", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        catch (IOException ioe) {
                            JOptionPane.showMessageDialog(null, "Virhe tiedoston käsittelyssä.", "Virhe", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        // valitseKuva2 = new JButton("Valitse kuva maastoa varten");
        // valitseKuva2.addMouseListener(new MouseAdapter() {
        //     public void mousePressed (MouseEvent e) {
        //         if (!SwingUtilities.isRightMouseButton(e)) {
        //             JOptionPane.showMessageDialog(null, "Tämän työkalun avulla voit luoda maaston png-kuvan värien perusteella.\nKuvan täytyy olla 10 x 10 -kokoinen.", "Luo maasto kuvan perusteella", JOptionPane.INFORMATION_MESSAGE);
        //             JFileChooser tiedostoSelain = new JFileChooser(".\\");
        //             FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png)", "png");
        //             tiedostoSelain.setFileFilter(tiedostoSuodatin);
        //             int valinta = tiedostoSelain.showOpenDialog(maastoIkkuna);
        //             if (valinta == JFileChooser.APPROVE_OPTION) {
        //                 String väriArvot = "";
        //                 File tiedosto = tiedostoSelain.getSelectedFile();
        //                 try {
        //                     BufferedImage kuva = ImageIO.read(tiedosto);
        //                     maastoKuvanVäriArvot = new int[kuva.getWidth()][kuva.getHeight()];
        //                     maastoKuvanVäriArvotPunainen = new int[kuva.getWidth()][kuva.getHeight()];
        //                     maastoKuvanVäriArvotVihreä = new int[kuva.getWidth()][kuva.getHeight()];
        //                     maastoKuvanVäriArvotSininen = new int[kuva.getWidth()][kuva.getHeight()];
        //                     if (kuva.getWidth() == 10 && kuva.getHeight() == 10) {
        //                         for (int i = 0; i < kuva.getHeight(); i++) {
        //                             for (int j = 0; j < kuva.getWidth(); j++) {
        //                                 maastoKuvanVäriArvot[j][i] = kuva.getRGB(j, i);
        //                                 maastoKuvanVäriArvotPunainen[j][i] = (maastoKuvanVäriArvot[j][i] & 0x00ff0000) >> 16;
        //                                 maastoKuvanVäriArvotVihreä[j][i] = (maastoKuvanVäriArvot[j][i] & 0x0000ff00) >> 8;
        //                                 maastoKuvanVäriArvotSininen[j][i] = (maastoKuvanVäriArvot[j][i] & 0x000000ff);
        //                                 väriArvot += "Pikseli " + j + "_" + i + " - Punainen: " + maastoKuvanVäriArvotPunainen[j][i] + ", Vihreä: " + maastoKuvanVäriArvotVihreä[j][i] + ", Sininen: " + maastoKuvanVäriArvotSininen[j][i] + ";\n";
        //                             }
        //                         }
        //                         maastoValittu = true;
        //                         System.out.println(väriArvot);
        //                         JOptionPane.showMessageDialog(null, "<html><p>" + väriArvot + "</p></html>", "Väriarvot", JOptionPane.INFORMATION_MESSAGE);
        //                     }
        //                     else {
        //                         JOptionPane.showMessageDialog(null, "Vain 10 x 10 -kuvat kelpaavat.", "Virheellinen kuvatiedosto", JOptionPane.ERROR_MESSAGE);
        //                     }
        //                 }
        //                 catch (IOException ioe) {
        //                     JOptionPane.showMessageDialog(null, "Virhe tiedoston käsittelyssä.", "Virhe", JOptionPane.ERROR_MESSAGE);
        //                 }
        //             }
        //         }
        //     }
        // });
        JLabel teksti0 = new JLabel(tekstit[0], JLabel.TRAILING);
        teksti0.setLabelFor(valitseKuva);
        paneli.add(teksti0);
        paneli.add(valitseKuva);

        // JLabel teksti1 = new JLabel(tekstit[1], JLabel.TRAILING);
        // teksti1.setLabelFor(valitseKuva2);
        // paneli.add(teksti1);
        // paneli.add(valitseKuva2);

        maastonTarkastelu = new JLabel();
        JLabel teksti2 = new JLabel(tekstit[2], JLabel.TRAILING);
        teksti2.setLabelFor(maastonTarkastelu);
        paneli.add(teksti2);
        paneli.add(maastonTarkastelu);
        

        JButton okNappi = new JButton("Aseta nykyiseksi kentäksi");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    tarkistaArvot();
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    maastoIkkuna.dispose();
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);

        SpringUtilities.makeCompactGrid(paneli, 3, 2, 6, 6, 6, 6);

        maastoIkkuna = new JFrame();
        maastoIkkuna.setTitle("Maastogeneraattori");
        maastoIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
        maastoIkkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        maastoIkkuna.setLayout(new BorderLayout());
        maastoIkkuna.setVisible(true);
        maastoIkkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maastoIkkuna.add(paneli, BorderLayout.CENTER);
        maastoIkkuna.revalidate();
        maastoIkkuna.repaint();
    }

    public static boolean ikkunaAuki() {
        if (maastoIkkuna == null) {
            return false;
        }
        else {
            if (maastoIkkuna.isVisible()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static void asetaPäällimmäiseksi() {
        maastoIkkuna.requestFocus();
    }
}
