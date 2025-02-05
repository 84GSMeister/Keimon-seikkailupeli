package keimo.HuoneEditori.muokkausIkkunat;

import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.NäköalueenMuokkausIkkuna;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.entityt.npc.Vartija;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class VartijaMuokkaus {
    
    static final int ikkunanLeveys = 420;
    static int ikkunanKorkeus = 180;
    static JFrame muokkausIkkuna;
    static String[] tekstit;
    static int valintojenMäärä;
    static JPanel paneli;
    static JLabel[] tekstiLabelit;
    static JTextField näköalue1Tekstikenttä, näköalue2Tekstikenttä;
    static JButton näköalueMuokkausNappi;

    static void hyväksyMuutokset(int sijX, int sijY, Vartija muokattavaKohde) {
        String piste1 = näköalue1Tekstikenttä.getText();
        String piste2 = näköalue2Tekstikenttä.getText();
        try {
            int x1 = Integer.parseInt(piste1.substring(0, piste1.indexOf(",")));
            int y1 = Integer.parseInt(piste1.substring(piste1.indexOf(",")+1, piste1.length()));
            int x2 = Integer.parseInt(piste2.substring(0, piste2.indexOf(",")));
            int y2 = Integer.parseInt(piste2.substring(piste2.indexOf(",")+1, piste2.length()));
            if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
                JOptionPane.showMessageDialog(null, "NPC:n näköalueen koordinaatti ei voi olla negatiivinen.\nHuoneet eivät voi jatkua negatiiviselle lukualueelle.", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
            }
            else {
                muokattavaKohde.x1 = x1;
                muokattavaKohde.y1 = y1;
                muokattavaKohde.x2 = x2;
                muokattavaKohde.y2 = y2;
                muokkausIkkuna.dispose();
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virheellinen syöte. Koordinaatit tulee antaa muodossa x,y (pilkulla erotettuna ilman välilyöntejä).", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ei voitu tallentaa muutoksia, koska objektia ei löytynyt.\n\nTämä aiheutuu todennäköisesti siitä, että kohdeobjekti on eri huoneessa, kuin tällä hetkellä editorissa auki oleva huone.", "häire", JOptionPane.ERROR_MESSAGE);
        }
        catch (NumberFormatException e) {
            //System.out.println(piste1.substring(0, piste1.indexOf(",")));
            //System.out.println(piste1.substring(piste1.indexOf(","), piste1.length()-1));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virheellinen syöte. Koordinaatit tulee antaa muodossa x,y (pilkulla erotettuna ilman välilyöntejä).", "Virheellinen syöte", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void luoVartijaMuokkausIkkuna(Vartija muokattavaKohde) {
        
        int sijX = muokattavaKohde.annaSijX();
        int sijY = muokattavaKohde.annaSijY();

        valintojenMäärä = 3;
        tekstit = new String[valintojenMäärä];
        tekstit[0] = "Näköalueen raja (piste 1)";
        tekstit[1] = "Näköalueen raja (piste 2)";
        tekstit[2] = "Muokkaa nälöaluetta";

        paneli = new JPanel(new SpringLayout());
        tekstiLabelit = new JLabel[valintojenMäärä];

        tekstiLabelit[0] = new JLabel(tekstit[0]);
        paneli.add(tekstiLabelit[0]);
        näköalue1Tekstikenttä = new JTextField(muokattavaKohde.x1 + "," + muokattavaKohde.y1);
        paneli.add(näköalue1Tekstikenttä);

        tekstiLabelit[1] = new JLabel(tekstit[1]);
        paneli.add(tekstiLabelit[1]);
        näköalue2Tekstikenttä = new JTextField(muokattavaKohde.x2 + "," + muokattavaKohde.y2);
        paneli.add(näköalue2Tekstikenttä);

        tekstiLabelit[2] = new JLabel(tekstit[2]);
        paneli.add(tekstiLabelit[2]);
        näköalueMuokkausNappi = new JButton("Muokkaa näköaluetta");
        näköalueMuokkausNappi.addActionListener(e -> NäköalueenMuokkausIkkuna.luoNäköalueenMuokkausIkkuna(muokattavaKohde));
        paneli.add(näköalueMuokkausNappi);

        JButton okNappi = new JButton("Aseta");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    hyväksyMuutokset(sijX, sijY, muokattavaKohde);
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    muokkausIkkuna.dispose();
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);
        paneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä + 1, 2, 6, 6, 6, 6);
        ikkunanKorkeus = valintojenMäärä * 30 + 70;

        muokkausIkkuna = new JFrame();
        muokkausIkkuna.setTitle("Muokkaa objektia: " + muokattavaKohde.annaNimi());
        muokkausIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        muokkausIkkuna.setBounds(100, 50, ikkunanLeveys, ikkunanKorkeus);
        muokkausIkkuna.setLayout(new BorderLayout());
        muokkausIkkuna.setVisible(true);
        muokkausIkkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        muokkausIkkuna.add(paneli, BorderLayout.CENTER);
        muokkausIkkuna.revalidate();
        muokkausIkkuna.repaint();
    }

    public static void päivitäNäköalue(int x1, int y1, int x2, int y2) {
        näköalue1Tekstikenttä.setText("" + x1 + "," + y1);
        näköalue2Tekstikenttä.setText("" + x2 + "," + y2);
    }
}
