package keimo.HuoneEditori;

import keimo.Huone;
import keimo.PääIkkuna;
import keimo.Kenttäkohteet.Käännettävä.Suunta;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class KarttaIkkuna {

    static final int ikkunanLeveys = 1600;
    static final int ikkunanKorkeus = 900;
    static JFrame ikkuna;
    static JLabel[][] kartta;
    static JPanel[][] maastoKartta;
    static JScrollPane karttaPaneli;
    static int kartanKoko;
    static int kartanAloitusX;
    static int kartanAloitusY;

    static Huone tarkastettavaHuone;
    static int tarkastettavaX;
    static int tarkastettavaY;
    static boolean[][] tarkastettavat;
    static Huone[][] muodostettuHuoneVisualisaatio;
    static boolean tarkastettaviaJäljellä = false;
    
    public static void luoKarttaikkuna() {
        
        kartanKoko = HuoneEditoriIkkuna.huoneKartta.size()/2;
        kartanAloitusX = kartanKoko/2;
        kartanAloitusY = kartanKoko/2;
        tarkastettavat = new boolean[kartanKoko][kartanKoko];
        
        JPanel paneli = new JPanel();
        paneli = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        paneli.setLayout(new GridLayout(kartanKoko, kartanKoko, 10, 10));
        paneli.setBounds(0, 0, 660, 660);

        muodostettuHuoneVisualisaatio = new Huone[kartanKoko][kartanKoko];
        kartta = new JLabel[kartanKoko][kartanKoko];
        maastoKartta = new JPanel[kartanKoko][kartanKoko];
        for (int i = 0; i < kartanKoko-1; i++) {
            for (int j = 0; j < kartanKoko-1; j++) {
                kartta[j][i] = new JLabel("");
                maastoKartta[j][i] = new JPanel();
                paneli.add(maastoKartta[j][i]);
            }
        }

        karttaPaneli = new JScrollPane(paneli);
        karttaPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        karttaPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        karttaPaneli.setBounds(0, 0, ikkunanLeveys-20, ikkunanKorkeus-35);

        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    ikkuna.dispose();
                }
            }
        });

        muodostaVisuaalinenKartta();
        karttaPaneli.getHorizontalScrollBar().setValue(karttaPaneli.getHorizontalScrollBar().getMaximum());
        karttaPaneli.getHorizontalScrollBar().setValue(4000);
        karttaPaneli.getVerticalScrollBar().setValue(karttaPaneli.getVerticalScrollBar().getMaximum()/2);
        //ScrollUtil.scroll(karttaPaneli, ScrollUtil.HCENTER, ScrollUtil.VCENTER);

        ikkuna = new JFrame("Kartta");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.add(karttaPaneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

    }

    
    static void muodostaVisuaalinenKartta() {
        kartta[kartanAloitusX][kartanAloitusY].setText("Huone 0");
        tarkastettavat[kartanAloitusX][kartanAloitusY] = true;
        tarkastettavaHuone = HuoneEditoriIkkuna.huoneKartta.get(0);
        muodostettuHuoneVisualisaatio[kartanAloitusX][kartanAloitusY] = tarkastettavaHuone;
        maastoKartta[kartanAloitusX][kartanAloitusY].add(tarkastettavaHuone.annaHuoneenMaastoGrafiikka());
        tarkastettaviaJäljellä = true;
        tarkastettavaX = kartanAloitusX;
        tarkastettavaY = kartanAloitusY;

        boolean vasemmallaHuone = false;
        boolean oikeallaHuone = false;
        boolean alhaallaHuone = false;
        boolean ylhäälläHuone = false;
        int tarkistusKerrrat = 0;

        while (tarkastettaviaJäljellä) {
            if (tarkistusKerrrat > kartanKoko) {
                break;
            }
            for (int i = 0; i < tarkastettavat.length; i++) {
                for (int j = 0; j < tarkastettavat.length; j++) {
                    if (tarkastettavat[j][i]) {
                        tarkastettavaX = j;
                        tarkastettavaY = i;
                        tarkastettavaHuone = muodostettuHuoneVisualisaatio[j][i];
                        if (tarkastettavaHuone.annaReunaWarppiTiedot(Suunta.VASEN)) {
                            if (tarkastettavaX > 0) {
                                if (muodostettuHuoneVisualisaatio[tarkastettavaX-1][tarkastettavaY] == null) {
                                    tarkastettavat[tarkastettavaX-1][tarkastettavaY] = true;
                                    kartta[tarkastettavaX-1][tarkastettavaY].setText("Huone " + tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.VASEN));
                                    maastoKartta[tarkastettavaX-1][tarkastettavaY].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.VASEN)).annaHuoneenMaastoGrafiikka());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX-1][tarkastettavaY] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.VASEN));
                                    vasemmallaHuone = true;
                                }
                            }
                        }
                        else {
                            vasemmallaHuone = false;
                        }
                        if (tarkastettavaHuone.annaReunaWarppiTiedot(Suunta.OIKEA)) {
                            if (tarkastettavaX < kartanKoko) {
                                if (muodostettuHuoneVisualisaatio[tarkastettavaX+1][tarkastettavaY] == null) {
                                    tarkastettavat[tarkastettavaX+1][tarkastettavaY] = true;
                                    kartta[tarkastettavaX+1][tarkastettavaY].setText("Huone " + tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.OIKEA));
                                    maastoKartta[tarkastettavaX+1][tarkastettavaY].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.OIKEA)).annaHuoneenMaastoGrafiikka());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX+1][tarkastettavaY] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.OIKEA));
                                    oikeallaHuone = true;
                                }
                            }
                        }
                        else {
                            oikeallaHuone = false;
                        }
                        if (tarkastettavaHuone.annaReunaWarppiTiedot(Suunta.ALAS)) {
                            if (tarkastettavaY < kartanKoko) {
                                if (muodostettuHuoneVisualisaatio[tarkastettavaX][tarkastettavaY+1] == null) {
                                    tarkastettavat[tarkastettavaX][tarkastettavaY+1] = true;
                                    kartta[tarkastettavaX][tarkastettavaY+1].setText("Huone " + tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.ALAS));
                                    maastoKartta[tarkastettavaX][tarkastettavaY+1].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.ALAS)).annaHuoneenMaastoGrafiikka());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX][tarkastettavaY+1] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.ALAS));
                                    alhaallaHuone = true;
                                }
                            }
                        }
                        else {
                            alhaallaHuone = false;
                        }
                        if (tarkastettavaHuone.annaReunaWarppiTiedot(Suunta.YLÖS)) {
                            if (tarkastettavaY > 0) {
                                if (muodostettuHuoneVisualisaatio[tarkastettavaX][tarkastettavaY-1] == null) {
                                    tarkastettavat[tarkastettavaX][tarkastettavaY-1] = true;
                                    kartta[tarkastettavaX][tarkastettavaY-1].setText("Huone " + tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.YLÖS));
                                    maastoKartta[tarkastettavaX][tarkastettavaY-1].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.YLÖS)).annaHuoneenMaastoGrafiikka());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX][tarkastettavaY-1] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.YLÖS));
                                    ylhäälläHuone = true;
                                }
                            }
                        }
                        else {
                            ylhäälläHuone = false;
                        }
                        tarkastettaviaJäljellä = true;
                    }
                }
            }
            if (!vasemmallaHuone && ! oikeallaHuone && !alhaallaHuone && !ylhäälläHuone) {
                //tarkastettaviaJäljellä = false;
            }
            tarkistusKerrrat++;
        }
    }
}
