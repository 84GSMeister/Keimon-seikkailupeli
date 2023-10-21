package keimo.Ikkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PeliKenttäMetodit.PathFindingExample;
import keimo.PääIkkuna;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.NPCt.NPC;
import keimo.NPCt.Vartija;
import keimo.NPCt.Vihollinen;
import keimo.Utility.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ConcurrentModificationException;

import javax.swing.*;

public class DebugInfoIkkuna {
    
    static int tietojenMäärä = 7;
    public static JFrame ikkuna;
    static JLabel dInfoHuone, dInfoSijX, dInfoSijY, dInfoNopeus, dInfoKuolemattomuusAika, dInfoReaktioAika, dInfoWarpViive;
    static JLabel dInfoVihollinen, dInfoVihSijX, dInfoVihSijY, dInfoVihNopeus, dInfoVihLiikJäljellä, dInfoVihSuunta, dInfoVihSuuntaSeuraava;

    public static void luoDebugInfoIkkuna() {

        JLabel huoneTeksti = new JLabel("Huone: ");
        dInfoHuone = new JLabel("");

        JLabel sijXTeksti = new JLabel("Sij X: ");
        dInfoSijX = new JLabel("");

        JLabel sijYTeksti = new JLabel("Sij Y: ");
        dInfoSijY = new JLabel("");

        JLabel nopeusTeksti = new JLabel("Nopeus: ");
        dInfoNopeus = new JLabel("");

        JLabel kuolemattomuusAikaTeksti = new JLabel("Kuolemattomuusaika: ");
        dInfoKuolemattomuusAika = new JLabel("");

        JLabel reaktioAikaTeksti = new JLabel("Reaktioaika: ");
        dInfoReaktioAika = new JLabel("");

        JLabel warpViiveTeksti = new JLabel("Warp-viive: ");
        dInfoWarpViive = new JLabel("");

        JPanel pääInfoPaneli = new JPanel(new SpringLayout());
        pääInfoPaneli.add(huoneTeksti); pääInfoPaneli.add(dInfoHuone);
        pääInfoPaneli.add(sijXTeksti); pääInfoPaneli.add(dInfoSijX);
        pääInfoPaneli.add(sijYTeksti); pääInfoPaneli.add(dInfoSijY);
        pääInfoPaneli.add(nopeusTeksti); pääInfoPaneli.add(dInfoNopeus);
        pääInfoPaneli.add(kuolemattomuusAikaTeksti); pääInfoPaneli.add(dInfoKuolemattomuusAika);
        pääInfoPaneli.add(reaktioAikaTeksti); pääInfoPaneli.add(dInfoReaktioAika);
        pääInfoPaneli.add(warpViiveTeksti); pääInfoPaneli.add(dInfoWarpViive);
        SpringUtilities.makeCompactGrid(pääInfoPaneli, tietojenMäärä, 2, 6, 6, 6, 6);


        JLabel vihollinenTeksti = new JLabel("Vihollinen: ");
        dInfoVihollinen = new JLabel("");

        JLabel vihSijXTeksti = new JLabel("Sij X: ");
        dInfoVihSijX = new JLabel("");

        JLabel vihSijYTeksti = new JLabel("Sij Y: ");
        dInfoVihSijY = new JLabel("");

        JLabel vihNopeusTeksti = new JLabel("Nopeus: ");
        dInfoVihNopeus = new JLabel("");

        JLabel vihLiikJäljelläTeksti = new JLabel("Liikettä jäljellä: ");
        dInfoVihLiikJäljellä = new JLabel("");

        JLabel vihSuuntaTeksti = new JLabel("Suunta: ");
        dInfoVihSuunta = new JLabel("");

        JLabel vihSuuntaSeuraavaTeksti = new JLabel("Seuraava suunta: ");
        dInfoVihSuuntaSeuraava = new JLabel("");

        JPanel vihollisInfoPaneli = new JPanel(new SpringLayout());
        vihollisInfoPaneli.add(vihollinenTeksti); vihollisInfoPaneli.add(dInfoVihollinen);
        vihollisInfoPaneli.add(vihSijXTeksti); vihollisInfoPaneli.add(dInfoVihSijX);
        vihollisInfoPaneli.add(vihSijYTeksti); vihollisInfoPaneli.add(dInfoVihSijY);
        vihollisInfoPaneli.add(vihNopeusTeksti); vihollisInfoPaneli.add(dInfoVihNopeus);
        vihollisInfoPaneli.add(vihLiikJäljelläTeksti); vihollisInfoPaneli.add(dInfoVihLiikJäljellä);
        vihollisInfoPaneli.add(vihSuuntaTeksti); vihollisInfoPaneli.add(dInfoVihSuunta);
        vihollisInfoPaneli.add(vihSuuntaSeuraavaTeksti); vihollisInfoPaneli.add(dInfoVihSuuntaSeuraava);
        SpringUtilities.makeCompactGrid(vihollisInfoPaneli, 7, 2, 6, 6, 6, 6);
        
        ikkuna = new JFrame("Debug Info");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, 400, 300);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(PääIkkuna.ikkuna);
        ikkuna.add(pääInfoPaneli, BorderLayout.WEST);
        ikkuna.add(vihollisInfoPaneli, BorderLayout.EAST);
        ikkuna.revalidate();
        ikkuna.repaint();
    }

    public static void päivitäTiedot() {
        dInfoHuone.setText("" + Peli.huone.annaId());
        dInfoSijX.setText("" + Pelaaja.sijX);
        dInfoSijY.setText("" + Pelaaja.sijY);
        if (Pelaaja.pelaajaLiikkuuVasen || Pelaaja.pelaajaLiikkuuOikea || Pelaaja.pelaajaLiikkuuYlös || Pelaaja.pelaajaLiikkuuAlas) {
            dInfoNopeus.setText("" + Pelaaja.nopeus);
        }
        else {
            dInfoNopeus.setText("" + 0);
        }
        dInfoKuolemattomuusAika.setText("" + Pelaaja.kuolemattomuusAika);
        dInfoReaktioAika.setText("" + Pelaaja.reaktioAika);
        dInfoWarpViive.setText("" + Peli.warppiViive);

        try {
            for (NPC npc : Peli.npcLista) {
                Vihollinen v = (Vihollinen)npc;
                dInfoVihollinen.setText("Vihollinen " + v.id);
                dInfoVihSijX.setText("" + v.hitbox.getCenterX());
                dInfoVihSijY.setText("" + v.hitbox.getCenterY());
                dInfoVihNopeus.setText("" + v.nopeus);
                dInfoVihLiikJäljellä.setText("" + v.liikuVielä);

                switch (v.liikeTapa) {
                    case LOOP_NELIÖ_MYÖTÄPÄIVÄÄN, NELIÖ_MYÖTÄPÄIVÄÄN_ESTEESEEN_ASTI:
                        dInfoVihSuunta.setText("" + v.liikeSuuntaLoopNeliöMyötäpäivään[v.liikeLoopinVaihe % v.liikeSuuntaLoopNeliöMyötäpäivään.length]);
                        dInfoVihSuuntaSeuraava.setText("" + v.liikeSuuntaLoopNeliöMyötäpäivään[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopNeliöMyötäpäivään.length]);
                    break;
                    case LOOP_NELIÖ_VASTAPÄIVÄÄN, NELIÖ_VASTAPÄIVÄÄN_ESTEESEEN_ASTI:
                        dInfoVihSuunta.setText("" + v.liikeSuuntaLoopNeliöVastapäivään[v.liikeLoopinVaihe % v.liikeSuuntaLoopNeliöVastapäivään.length]);
                        dInfoVihSuuntaSeuraava.setText("" + v.liikeSuuntaLoopNeliöVastapäivään[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopNeliöVastapäivään.length]);
                    break;
                    case LOOP_VASEN_OIKEA, VASEN_OIKEA_ESTEESEEN_ASTI:
                        dInfoVihSuunta.setText("" + v.liikeSuuntaLoopVasenOikea[v.liikeLoopinVaihe % v.liikeSuuntaLoopVasenOikea.length]);
                        dInfoVihSuuntaSeuraava.setText("" + v.liikeSuuntaLoopVasenOikea[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopVasenOikea.length]);
                    break;
                    case LOOP_YLÖS_ALAS, YLÖS_ALAS_ESTEESEEN_ASTI:
                        dInfoVihSuunta.setText("" + v.liikeSuuntaLoopYlösAlas[v.liikeLoopinVaihe % v.liikeSuuntaLoopYlösAlas.length]);
                        dInfoVihSuuntaSeuraava.setText("" + v.liikeSuuntaLoopYlösAlas[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopYlösAlas.length]);
                    break;
                    default:
                    break;
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Konkurrenssi-issue");
            cme.printStackTrace();
        }
    }
}
