package keimo.HuoneEditori;

import keimo.Huone;
import keimo.PääIkkuna;
import keimo.Kenttäkohteet.Käännettävä.Suunta;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class KarttaIkkuna {

    static final int ikkunanLeveys = 1600;
    static final int ikkunanKorkeus = 900;
    static JFrame ikkuna;
    static JLabel[][] kartta;
    static JPanel[][] maastoKartta;
    static int kartanKoko;
    static int kartanAloitusX;
    static int kartanAloitusY;
    static int kartanLeveys;
    static int kartanKorkeus;

    static Huone tarkastettavaHuone;
    static JPanel paneli;
    static int tarkastettavaX;
    static int tarkastettavaY;
    static boolean[][] tarkastettavat;
    static Huone[][] muodostettuHuoneVisualisaatio;
    static boolean tarkastettaviaJäljellä = false;
    static KarttaPaneli kp;
    static JScrollPane scrollaavaPaneli;

    static int zoom = 1;
    static JLabel zoomintasoLabel;
    static JLabel statusLabel;

    static MouseAdapter ma;

    public static class KarttaPaneli extends JPanel {

        public KarttaPaneli() {
            setLayout(new BorderLayout());
            try {
                scrollaavaPaneli = new JScrollPane(paneli);
                scrollaavaPaneli.getHorizontalScrollBar().setValue(scrollaavaPaneli.getHorizontalScrollBar().getMaximum());
                scrollaavaPaneli.getHorizontalScrollBar().setValue(2000);
                scrollaavaPaneli.getVerticalScrollBar().setValue(scrollaavaPaneli.getVerticalScrollBar().getMaximum()/2);
                add(scrollaavaPaneli);

                ma = new MouseAdapter() {

                    private Point origin;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        origin = new Point(e.getPoint());
                        if (SwingUtilities.isRightMouseButton(e)) {
                            JPopupMenu ominaisuusMenu = new JPopupMenu();
                            Huone h = muodostettuHuoneVisualisaatio[origin.x/(640/zoom)][origin.y/(640/zoom)];
                            if (h != null) {
                                JMenuItem lataaHuoneEditoriin = new JMenuItem("Lataa huone " + h.annaId() + " (" + h.annaNimi() + ") editoriin");
                                lataaHuoneEditoriin.addActionListener(al -> {
                                    HuoneEditoriIkkuna.vaihdaHuonetta(HuoneEditoriIkkuna.muokattavaHuone, h.annaId(), true);
                                    HuoneEditoriIkkuna.muokattavaHuone = h.annaId();
                                });
                                ominaisuusMenu.add(lataaHuoneEditoriin);
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (origin != null) {
                                JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, paneli);
                                if (viewPort != null) {
                                    int deltaX = origin.x - e.getX();
                                    int deltaY = origin.y - e.getY();

                                    Rectangle view = viewPort.getViewRect();
                                    view.x += deltaX;
                                    view.y += deltaY;

                                    paneli.scrollRectToVisible(view);
                                }
                            }
                        }
                    }

                };
                paneli.addMouseListener(ma);
                paneli.addMouseMotionListener(ma);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

    }
    
    public static void luoKarttaikkuna() {
        
        kartanKoko = HuoneEditoriIkkuna.huoneKartta.size()/2;
        kartanAloitusX = kartanKoko/2;
        kartanAloitusY = kartanKoko/2;
        tarkastettavat = new boolean[kartanKoko][kartanKoko];
        
        paneli = new JPanel();
        paneli = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        paneli.setLayout(new GridLayout(kartanKoko, kartanKoko, 0, 0));
        paneli.setBounds(0, 0, 660, 660);

        muodostettuHuoneVisualisaatio = new Huone[kartanKoko][kartanKoko];
        kartta = new JLabel[kartanKoko][kartanKoko];
        maastoKartta = new JPanel[kartanKoko][kartanKoko];

        for (int i = 0; i < kartanKoko-1; i++) {
            for (int j = 0; j < kartanKoko-1; j++) {
                kartta[j][i] = new JLabel("");
                maastoKartta[j][i] = new JPanel();
                maastoKartta[j][i].setName("Huonepaneli " + j + ", " + i + " (tyhjä)");
                paneli.add(maastoKartta[j][i]);
            }
        }

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
        paneli.removeAll();
        paneli.setLayout(new GridLayout(kartanKorkeus, kartanLeveys, 0, 0));
        for (int i = 0; i < kartanKorkeus; i++) {
            for (int j = 0; j < kartanLeveys; j++) {
                //kartta[j][i] = new JLabel("");
                //maastoKartta[j][i] = new JPanel();
                //maastoKartta[j][i].setName("Huonepaneli " + j + ", " + i + " (tyhjä)");
                paneli.add(maastoKartta[j][i]);
            }
        }

        JButton miinusNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/zoom_miinus.png"));
        miinusNappi.setToolTipText("Zoomaa ulos");
        miinusNappi.addActionListener(e -> {
            if (zoom < 4) {
                zoom *= 2;
                muutaKartanKokoa();
            }
        });
        JButton plusNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/zoom_plus.png"));
        plusNappi.setToolTipText("Zoomaa sisään");
        plusNappi.addActionListener(e -> {
            if (zoom > 1) {
                zoom /= 2;
                muutaKartanKokoa();
            }
        });

        zoomintasoLabel = new JLabel("zoom: " + 1f/zoom + "x");

        JButton päivitäNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/peru.png"));
        päivitäNappi.setToolTipText("Päivitä");
        päivitäNappi.addActionListener(e -> muutaKartanKokoa());

        JPanel yläPaneeli = new JPanel();
        yläPaneeli.add(miinusNappi);
        yläPaneeli.add(plusNappi);
        yläPaneeli.add(zoomintasoLabel);
        yläPaneeli.add(päivitäNappi);

        statusLabel = new JLabel("Valmis.");

        JPanel alaPaneeli = new JPanel();
        alaPaneeli.setLayout(new FlowLayout(FlowLayout.LEFT));
        alaPaneeli.add(statusLabel);

        kp = new KarttaPaneli();
        kp.setName("karttapaneli");


        ikkuna = new JFrame("Kartta");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        ikkuna.add(yläPaneeli, BorderLayout.NORTH);
        ikkuna.add(kp, BorderLayout.CENTER);
        ikkuna.add(alaPaneeli, BorderLayout.SOUTH);
        ikkuna.revalidate();
        ikkuna.repaint();

    }

    private static void muutaKartanKokoa() {
        
        Thread t = new Thread() {
            @Override
            public void run(){
                try {
                    statusLabel.setText("Muutetaan kokoa...");
                    Thread.sleep(1000);
                    zoomintasoLabel.setText(("zoom: " + 1f/zoom + "x"));
                    statusLabel.setText("Valmis.");
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                scrollaavaPaneli.getHorizontalScrollBar().setValue(scrollaavaPaneli.getHorizontalScrollBar().getMaximum()/2);
                scrollaavaPaneli.getVerticalScrollBar().setValue(scrollaavaPaneli.getVerticalScrollBar().getMaximum()/2);
            }
        };
        t.start();

        Thread t2 = new Thread() {
            @Override
            public void run(){
                for (Component c : paneli.getComponents()) {
                    if (c.getName() != null) {
                        if (c.getName().startsWith("Huonepaneli")) {
                            paneli.remove(c);
                        }
                    }
                    else {
                        paneli.remove(c);
                    }
                }
                for (int i = 0; i < kartanKorkeus; i++) {
                    for (int j = 0; j < kartanLeveys; j++) {
                        try {
                            if (maastoKartta[j][i] != null && muodostettuHuoneVisualisaatio[j][i] != null) {
                                maastoKartta[j][i] = muodostettuHuoneVisualisaatio[j][i].annaHuoneenMaastoGrafiikka(zoom);
                                maastoKartta[j][i].setName("Huonepaneli " + j + ", " + i);
                            }
                            else {
                                maastoKartta[j][i] = new JPanel();
                                maastoKartta[j][i].setName("Huonepaneli " + j + ", " + i + " (tyhjä)");
                            }
                            System.out.println(maastoKartta[j][i].getName());
                            paneli.add(maastoKartta[j][i]);
                        }
                        catch (ArrayIndexOutOfBoundsException aioobe) {
                            System.out.println("ohitetaan indeksit");
                        }
                    }
                }
                paneli.setBounds(0, 0, 640/zoom*kartanLeveys, 640/zoom*kartanKorkeus);
                if (paneli.getBounds().width+20 < ikkuna.getBounds().width) {
                    ikkuna.setBounds(ikkuna.getBounds().x, ikkuna.getBounds().y, paneli.getBounds().width+20, ikkuna.getBounds().height);
                }
                if (paneli.getBounds().height+100 < ikkuna.getBounds().height) {
                    ikkuna.setBounds(ikkuna.getBounds().x, ikkuna.getBounds().y, ikkuna.getBounds().width, paneli.getBounds().height+100);
                }
                zoomintasoLabel.setText(("zoom: " + 1f/zoom + "x"));
                statusLabel.setText("Odotetaan pääsäikeen ikkunaoperaatioita...");
                paneli.revalidate();
                paneli.repaint();

            }
        };
        t2.start();
    }
    
    static void muodostaVisuaalinenKartta() {
        //int zoom = 1;
        kartta[kartanAloitusX][kartanAloitusY].setText("Huone 0");
        tarkastettavat[kartanAloitusX][kartanAloitusY] = true;
        tarkastettavaHuone = HuoneEditoriIkkuna.huoneKartta.get(0);
        muodostettuHuoneVisualisaatio[kartanAloitusX][kartanAloitusY] = tarkastettavaHuone;
        maastoKartta[kartanAloitusX][kartanAloitusY].add(tarkastettavaHuone.annaHuoneenMaastoGrafiikka(zoom));
        maastoKartta[kartanAloitusX][kartanAloitusY].setName("Huonepaneli " + kartanAloitusX + ", " + kartanAloitusY + ": " + tarkastettavaHuone.annaNimi());
        tarkastettaviaJäljellä = true;
        tarkastettavaX = kartanAloitusX;
        tarkastettavaY = kartanAloitusY;

        boolean vasemmallaHuone = false;
        boolean oikeallaHuone = false;
        boolean alhaallaHuone = false;
        boolean ylhäälläHuone = false;
        int tarkistusKerrrat = 0;
        kartanLeveys = 0;
        kartanKorkeus = 0;

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
                                    maastoKartta[tarkastettavaX-1][tarkastettavaY].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.VASEN)).annaHuoneenMaastoGrafiikka(zoom));
                                    maastoKartta[tarkastettavaX-1][tarkastettavaY].setName("Huonepaneli " + (tarkastettavaX-1) + ", " + tarkastettavaY + ": " + HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.VASEN)).annaNimi());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX-1][tarkastettavaY] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.VASEN));
                                    vasemmallaHuone = true;
                                    kartanLeveys++;
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
                                    maastoKartta[tarkastettavaX+1][tarkastettavaY].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.OIKEA)).annaHuoneenMaastoGrafiikka(zoom));
                                    maastoKartta[tarkastettavaX+1][tarkastettavaY].setName("Huonepaneli " + (tarkastettavaX+1) + ", " + tarkastettavaY + ": " + HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.OIKEA)).annaNimi());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX+1][tarkastettavaY] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.OIKEA));
                                    oikeallaHuone = true;
                                    kartanLeveys++;
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
                                    maastoKartta[tarkastettavaX][tarkastettavaY+1].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.ALAS)).annaHuoneenMaastoGrafiikka(zoom));
                                    maastoKartta[tarkastettavaX][tarkastettavaY+1].setName("Huonepaneli " + tarkastettavaX + ", " + (tarkastettavaY+1) + ": " + HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.ALAS)).annaNimi());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX][tarkastettavaY+1] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.ALAS));
                                    alhaallaHuone = true;
                                    kartanKorkeus++;
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
                                    maastoKartta[tarkastettavaX][tarkastettavaY-1].add(HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.YLÖS)).annaHuoneenMaastoGrafiikka(zoom));
                                    maastoKartta[tarkastettavaX][tarkastettavaY-1].setName("Huonepaneli " + tarkastettavaX + ", " + (tarkastettavaY-1) + ": " + HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.YLÖS)).annaNimi());
                                    muodostettuHuoneVisualisaatio[tarkastettavaX][tarkastettavaY-1] = HuoneEditoriIkkuna.huoneKartta.get(tarkastettavaHuone.annaReunaWarpinKohdeId(Suunta.YLÖS));
                                    ylhäälläHuone = true;
                                    kartanKorkeus++;
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
        ArrayList<Integer> xArvoLista = new ArrayList<>();
        ArrayList<Integer> yArvoLista = new ArrayList<>();
        for (int i = 0; i < muodostettuHuoneVisualisaatio.length; i++) {
            for (int j = 0; j < muodostettuHuoneVisualisaatio.length; j++) {
                if (muodostettuHuoneVisualisaatio[j][i] != null) {
                    xArvoLista.add(j);
                    yArvoLista.add(i);
                }
            }
        }
        kartanLeveys = Collections.max(xArvoLista) - Collections.min(xArvoLista) +1;
        kartanKorkeus = Collections.max(yArvoLista) - Collections.min(yArvoLista) +1;
        System.out.println("leveys: " + kartanLeveys);
        System.out.println("korkeus: " + kartanKorkeus);

        ArrayList<JPanel> muodostetutPanelit = new ArrayList<>();
        ArrayList<Integer> xIndeksit = new ArrayList<>();
        ArrayList<Integer> yIndeksit = new ArrayList<>();
        for (int i = 0; i < tarkastettavat.length; i++) {
            for (int j = 0; j < tarkastettavat.length; j++) {
                if (maastoKartta[j][i] != null) {
                    if (maastoKartta[j][i].getName().startsWith("Huonepaneli") && !maastoKartta[j][i].getName().contains("(tyhjä)")) {
                        System.out.println("kyllä: " + maastoKartta[j][i].getName());
                        muodostetutPanelit.add(maastoKartta[j][i]);
                        try {
                            int x = Integer.parseInt(maastoKartta[j][i].getName().substring(12, 13));
                            int y = Integer.parseInt(maastoKartta[j][i].getName().substring(15, 16));
                            xIndeksit.add(x);
                            yIndeksit.add(y);
                        }
                        catch (NumberFormatException nfe) {
                            System.out.println("virheellinen indeksi");
                            nfe.printStackTrace();
                        }
                    }
                    else {
                        //System.out.println("ei: " + maastoKartta[j][i].getName());
                    }
                }
            }
        }

        int leveysErotus = Collections.max(xIndeksit) - kartanLeveys;
        int korkeusErotus = Collections.max(yIndeksit) - kartanKorkeus;

        maastoKartta = new JPanel[kartanLeveys][kartanKorkeus];
        for (int i = 0; i < kartanKorkeus; i++) {
            for (int j = 0; j < kartanLeveys; j++) {
                maastoKartta[j][i] = new JPanel();
            }
        }
        for (JPanel jp : muodostetutPanelit) {
            //System.out.println(jp.getName());
            try {
                int asetettavaX = Integer.parseInt(jp.getName().substring(12, 13)) - leveysErotus -1;
                int asetettavaY = Integer.parseInt(jp.getName().substring(15, 16)) - korkeusErotus -1;
                maastoKartta[asetettavaX][asetettavaY] = jp;
                Huone h = muodostettuHuoneVisualisaatio[asetettavaX][asetettavaY];
                if (h != null) {
                    maastoKartta[asetettavaX][asetettavaY].setToolTipText(h.annaNimi() + "(" + h.annaId() + ")");
                }
            }
            catch (NumberFormatException nfe) {
                System.out.println("virheellinen indeksi");
            }
            
            //System.out.println(jp.getName() + ": " + asetettavaX + ", " + asetettavaY);
        }
        for (int i = 0; i < kartanKoko; i++) {
            for (int j = 0; j < kartanKoko; j++) {
                if (muodostettuHuoneVisualisaatio[j][i] != null) {
                    try {
                        muodostettuHuoneVisualisaatio[j - leveysErotus -1][i - korkeusErotus -1] = muodostettuHuoneVisualisaatio[j][i];
                        System.out.println(j + " " + i + " asetetaan kohtaan " + (j - leveysErotus -1) + " " + (i - korkeusErotus -1));
                        muodostettuHuoneVisualisaatio[j][i] = null;
                    }
                    catch (ArrayIndexOutOfBoundsException aioobe) {
                        System.out.println("ohitetaan huone");
                    }
                }
                else {
                    //System.out.println(j + " " + i + " " + "null");
                }
            }
        }
    }
}
