package keimo.HuoneEditori;

import keimo.PääIkkuna;
import keimo.Kenttäkohteet.AvattavaEste;
import keimo.Kenttäkohteet.KenttäKohde;
import keimo.Kenttäkohteet.Triggeri;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TriggerinValintaIkkuna {
    
    static JFrame ikkuna;
    static ArrayList<Point> valitutTriggerit = new ArrayList<>();
    static JLabel valitutTriggeritLabel;
    static AvattavaEste muokattavaEste;

    public static void luoTriggerinValintaIkkuna(AvattavaEste muokattavaKohde) {

        valitutTriggeritLabel = new JLabel("valitut triggerit");
        valitutTriggerit.clear();
        // for (Point p : muokattavaKohde.annaVaaditutTriggerit()) {
        //     valitutTriggerit.add(p);
        // }
        String[] pisteet = ObjektinMuokkausIkkuna.triggeriLista.getText().split("; ");
        for (String s : pisteet) {
            System.out.println(s);
            if (s.length() >= 3) {
                try {
                    int x = Integer.parseInt("" + s.charAt(0));
                    int y = Integer.parseInt("" + s.charAt(2));
                    valitutTriggerit.add(new Point(x, y));
                }
                catch (NumberFormatException nfe) {
                    System.out.println("Virhe parsiessa numeroa");
                    nfe.printStackTrace();
                }
            }
        }

        JPanel valintaPaneli = new JPanel();

        for (KenttäKohde[] kk : HuoneEditoriIkkuna.objektiKenttä) {
            for (KenttäKohde k : kk) {
                if (k instanceof Triggeri) {
                    Triggeri trg = (Triggeri)k;
                    JButton triggeriKuvake = new JButton((trg.annaKuvake()));
                    triggeriKuvake.setText("" + trg.annaSijX() + "," + trg.annaSijY());
                    triggeriKuvake.addActionListener(e -> {
                        Point p = new Point(trg.annaSijX(), trg.annaSijY());
                        if (valitutTriggerit.contains(p)) {
                            valitutTriggerit.remove(p);
                        }
                        else {
                            valitutTriggerit.add(p);
                        }
                        päivitäTriggeriLista();
                    });
                    valintaPaneli.add(triggeriKuvake);
                }
            }
        }

        JButton okNappi = new JButton("OK");
        okNappi.addActionListener(e -> {
            muokattavaEste = muokattavaKohde;
            String s = "";
            for (Point p : valitutTriggerit) {
                // muokattavaEste.tyhjennäTriggerit();
                // if (!muokattavaEste.annaVaaditutTriggerit().contains(p)) {
                //     muokattavaEste.lisääTriggeri(p.x, p.y);
                // }

                s += p.x + "," + p.y + "; ";
            }
            ObjektinMuokkausIkkuna.triggeriLista.setText(s);
            ikkuna.dispose();
            HuoneEditoriIkkuna.ikkuna.setFocusable(true);
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addActionListener(e -> {
            ikkuna.dispose();
            HuoneEditoriIkkuna.ikkuna.setFocusable(true);
        });

        JPanel okCancelPaneli = new JPanel();
        okCancelPaneli.add(okNappi);
        okCancelPaneli.add(cancelNappi);

        ikkuna = new JFrame();
        ikkuna.setTitle("Valitse triggerit");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, 400, 300);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
        ikkuna.add(valitutTriggeritLabel, BorderLayout.NORTH);
        ikkuna.add(valintaPaneli, BorderLayout.CENTER);
        ikkuna.add(okCancelPaneli, BorderLayout.SOUTH);
        ikkuna.revalidate();
        ikkuna.repaint();

        päivitäTriggeriLista();
    }

    private static void päivitäTriggeriLista() {
        String s = "";
        for (Point p : valitutTriggerit) {
            s += p.x + "," + p.y + "; ";
        }
        valitutTriggeritLabel.setText(s);
    }
}
