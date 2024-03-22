package keimo.Ikkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.NPCt.NPC;
import keimo.NPCt.Vihollinen;
import keimo.Utility.Downloaded.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ConcurrentModificationException;
import java.text.DecimalFormat;
import javax.swing.*;

public class DebugInfoIkkuna {
    
    static int tietojenMääräPeli = 3;
    static int tietojenMääräPelaaja = 14;
    static int tietojenMääräVihollinen = 8;
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    public static JFrame ikkuna;
    static JLabel dInfoHuone, dInfoSijX, dInfoSijY, dInfoSijXPx, dInfoSijYPx, dInfoNopeus;
    static JLabel dInfoKuolemattomuusAika, dInfoReaktioAika, dInfoKänninVoimakkuus;
    static JLabel dInfoKeimonState, dInfoKeimonKylläisyys, dInfoKeimonTerveys, dInfoKeimonSuunta, dInfoKeimonSuuntaX;
    static JLabel dInfoWarpViive, dInfoAmmusGeneraattori, dInfoKentänTyhjennys;
    static JPanel dInfoVihollinen, dInfoVihTyyppi, dInfoVihSijX, dInfoVihSijY, dInfoVihNopeus, dInfoVihLiikJäljellä, dInfoVihSuunta, dInfoVihSuuntaSeuraava;

    public static void luoDebugInfoIkkuna() {

        JLabel warpViiveTeksti = new JLabel("Warp-viive: ");
        dInfoWarpViive = new JLabel("");

        JLabel ammusGeneraattoriTeksti = new JLabel("Ammusgeneraattori: ");
        dInfoAmmusGeneraattori = new JLabel("");

        JLabel kentänTyhjennysTeksti = new JLabel("Kentäntyhjentäjä: ");
        dInfoKentänTyhjennys = new JLabel("");

        JPanel peliInfoPaneli = new JPanel(new SpringLayout());
        peliInfoPaneli.add(warpViiveTeksti); peliInfoPaneli.add(dInfoWarpViive);
        peliInfoPaneli.add(ammusGeneraattoriTeksti); peliInfoPaneli.add(dInfoAmmusGeneraattori);
        peliInfoPaneli.add(kentänTyhjennysTeksti); peliInfoPaneli.add(dInfoKentänTyhjennys);
        SpringUtilities.makeCompactGrid(peliInfoPaneli, tietojenMääräPeli, 2, 6, 6, 6, 6);


        JLabel huoneTeksti = new JLabel("Huone: ");
        dInfoHuone = new JLabel("");

        JLabel sijXTeksti = new JLabel("Sij X (Tile): ");
        dInfoSijX = new JLabel("");

        JLabel sijYTeksti = new JLabel("Sij Y (Tile): ");
        dInfoSijY = new JLabel("");

        JLabel sijXPxTeksti = new JLabel("Sij X (Px): ");
        dInfoSijXPx = new JLabel("");

        JLabel sijYPxTeksti = new JLabel("Sij Y (Px): ");
        dInfoSijYPx = new JLabel("");

        JLabel nopeusTeksti = new JLabel("Nopeus: ");
        dInfoNopeus = new JLabel("");

        JLabel kuolemattomuusAikaTeksti = new JLabel("Kuolemattomuusaika: ");
        dInfoKuolemattomuusAika = new JLabel("");

        JLabel reaktioAikaTeksti = new JLabel("Reaktioaika: ");
        dInfoReaktioAika = new JLabel("");

        JLabel känninVoimakkuusTeksti = new JLabel("Kännin voimakkuus: ");
        dInfoKänninVoimakkuus = new JLabel("");

        JLabel keimonStateTeksti = new JLabel("Keimon State: ");
        dInfoKeimonState = new JLabel("");

        JLabel keimonKylläisyysTeksti = new JLabel("Keimon Kylläisyys: ");
        dInfoKeimonKylläisyys = new JLabel("");

        JLabel keimonTerveysTeksti = new JLabel("Keimon Terveys: ");
        dInfoKeimonTerveys = new JLabel("");

        JLabel keimonSuuntaTeksti = new JLabel("Keimon Suunta: ");
        dInfoKeimonSuunta = new JLabel("");

        JLabel keimonSuuntaXTeksti = new JLabel("Keimon X-Suunta: ");
        dInfoKeimonSuuntaX = new JLabel("");

        JPanel pelaajaInfoPaneli = new JPanel(new SpringLayout());
        pelaajaInfoPaneli.add(huoneTeksti); pelaajaInfoPaneli.add(dInfoHuone);
        pelaajaInfoPaneli.add(sijXTeksti); pelaajaInfoPaneli.add(dInfoSijX);
        pelaajaInfoPaneli.add(sijYTeksti); pelaajaInfoPaneli.add(dInfoSijY);
        pelaajaInfoPaneli.add(sijXPxTeksti); pelaajaInfoPaneli.add(dInfoSijXPx);
        pelaajaInfoPaneli.add(sijYPxTeksti); pelaajaInfoPaneli.add(dInfoSijYPx);
        pelaajaInfoPaneli.add(nopeusTeksti); pelaajaInfoPaneli.add(dInfoNopeus);
        pelaajaInfoPaneli.add(kuolemattomuusAikaTeksti); pelaajaInfoPaneli.add(dInfoKuolemattomuusAika);
        pelaajaInfoPaneli.add(reaktioAikaTeksti); pelaajaInfoPaneli.add(dInfoReaktioAika);
        pelaajaInfoPaneli.add(känninVoimakkuusTeksti); pelaajaInfoPaneli.add(dInfoKänninVoimakkuus);
        pelaajaInfoPaneli.add(keimonStateTeksti); pelaajaInfoPaneli.add(dInfoKeimonState);
        pelaajaInfoPaneli.add(keimonKylläisyysTeksti); pelaajaInfoPaneli.add(dInfoKeimonKylläisyys);
        pelaajaInfoPaneli.add(keimonTerveysTeksti); pelaajaInfoPaneli.add(dInfoKeimonTerveys);
        pelaajaInfoPaneli.add(keimonSuuntaTeksti); pelaajaInfoPaneli.add(dInfoKeimonSuunta);
        pelaajaInfoPaneli.add(keimonSuuntaXTeksti); pelaajaInfoPaneli.add(dInfoKeimonSuuntaX);
        SpringUtilities.makeCompactGrid(pelaajaInfoPaneli, tietojenMääräPelaaja, 2, 6, 6, 6, 6);


        JLabel vihollinenTeksti = new JLabel("Vihollinen: ");
        dInfoVihollinen = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JLabel vihTyyppiTeksti = new JLabel("Tyyppi: ");
        dInfoVihTyyppi = new JPanel();
        dInfoVihTyyppi.setPreferredSize(new Dimension(300, 20));

        JLabel vihSijXTeksti = new JLabel("Sij X: ");
        dInfoVihSijX = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JLabel vihSijYTeksti = new JLabel("Sij Y: ");
        dInfoVihSijY = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JLabel vihNopeusTeksti = new JLabel("Nopeus: ");
        dInfoVihNopeus = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JLabel vihLiikJäljelläTeksti = new JLabel("Liikettä jäljellä: ");
        dInfoVihLiikJäljellä = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JLabel vihSuuntaTeksti = new JLabel("Suunta: ");
        dInfoVihSuunta = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JLabel vihSuuntaSeuraavaTeksti = new JLabel("Seuraava suunta: ");
        dInfoVihSuuntaSeuraava = new JPanel();
        dInfoVihollinen.setPreferredSize(new Dimension(300, 20));

        JPanel vihollisInfoPaneli = new JPanel(new SpringLayout());
        vihollisInfoPaneli.add(vihollinenTeksti); vihollisInfoPaneli.add(dInfoVihollinen);
        vihollisInfoPaneli.add(vihTyyppiTeksti); vihollisInfoPaneli.add(dInfoVihTyyppi);
        vihollisInfoPaneli.add(vihSijXTeksti); vihollisInfoPaneli.add(dInfoVihSijX);
        vihollisInfoPaneli.add(vihSijYTeksti); vihollisInfoPaneli.add(dInfoVihSijY);
        vihollisInfoPaneli.add(vihNopeusTeksti); vihollisInfoPaneli.add(dInfoVihNopeus);
        vihollisInfoPaneli.add(vihLiikJäljelläTeksti); vihollisInfoPaneli.add(dInfoVihLiikJäljellä);
        vihollisInfoPaneli.add(vihSuuntaTeksti); vihollisInfoPaneli.add(dInfoVihSuunta);
        vihollisInfoPaneli.add(vihSuuntaSeuraavaTeksti); vihollisInfoPaneli.add(dInfoVihSuuntaSeuraava);
        SpringUtilities.makeCompactGrid(vihollisInfoPaneli, tietojenMääräVihollinen, 2, 6, 6, 6, 6);

        JTabbedPane välilehdet = new JTabbedPane();
        välilehdet.addTab("Peli", peliInfoPaneli);
        välilehdet.addTab("Pelaaja", pelaajaInfoPaneli);
        välilehdet.addTab("Viholliset", vihollisInfoPaneli);
        
        ikkuna = new JFrame("Debug Info");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(0, 0 , 400, 700);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(PääIkkuna.ikkuna);
        ikkuna.add(välilehdet);
        ikkuna.revalidate();
        ikkuna.repaint();
    }

    public static void päivitäTiedot() {
        dInfoWarpViive.setText("" + Peli.warppiViive);
        dInfoAmmusGeneraattori.setText("" + (99 - (Peli.globaaliTickit % 100)));
        dInfoKentänTyhjennys.setText("" + (1999 - (Peli.globaaliTickit % 2000)));

        dInfoHuone.setText("" + Peli.huone.annaId());
        dInfoSijX.setText("" + Pelaaja.sijX);
        dInfoSijY.setText("" + Pelaaja.sijY);
        dInfoSijXPx.setText("" + Pelaaja.hitbox.x);
        dInfoSijYPx.setText("" + Pelaaja.hitbox.y);
        if (Pelaaja.pelaajaLiikkuuVasen || Pelaaja.pelaajaLiikkuuOikea || Pelaaja.pelaajaLiikkuuYlös || Pelaaja.pelaajaLiikkuuAlas) {
            dInfoNopeus.setText("" + Pelaaja.nopeus);
        }
        else {
            dInfoNopeus.setText("" + 0);
        }
        dInfoKuolemattomuusAika.setText("" + Pelaaja.kuolemattomuusAika);
        dInfoReaktioAika.setText("" + Pelaaja.reaktioAika);
        dInfoKänninVoimakkuus.setText("" + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat) + " (" + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + " ‰)");
        dInfoKeimonState.setText(Pelaaja.keimonState.toString());
        dInfoKeimonKylläisyys.setText(Pelaaja.keimonKylläisyys.toString());
        dInfoKeimonTerveys.setText(Pelaaja.keimonTerveys.toString());
        dInfoKeimonSuunta.setText(Pelaaja.keimonSuunta.toString());
        dInfoKeimonSuuntaX.setText(Pelaaja.keimonSuuntaVasenOikea.toString());

        try {
            dInfoVihollinen.removeAll();
            dInfoVihTyyppi.removeAll();
            dInfoVihSijX.removeAll();
            dInfoVihSijY.removeAll();
            dInfoVihNopeus.removeAll();
            dInfoVihLiikJäljellä.removeAll();
            dInfoVihSuunta.removeAll();
            dInfoVihSuuntaSeuraava.removeAll();
            for (NPC npc : Peli.npcLista) {
                if (npc instanceof Vihollinen) {
                    Vihollinen v = (Vihollinen)npc;
                
                    JLabel infoViholliset = new JLabel("Vihollinen " + v.id);
                    dInfoVihollinen.add(infoViholliset);

                    JLabel infoVihTyyppi = new JLabel("" + v.annaNimi());
                    dInfoVihTyyppi.add(infoVihTyyppi);

                    JLabel infoVihSijX = new JLabel("" + v.hitbox.x);
                    dInfoVihSijX.add(infoVihSijX);

                    JLabel infoVihSijY = new JLabel("" + v.hitbox.y);
                    dInfoVihSijY.add(infoVihSijY);

                    JLabel infoVihNopeus = new JLabel("" + v.nopeus);
                    dInfoVihNopeus.add(infoVihNopeus);

                    JLabel infoVihLiikJäljellä = new JLabel("" + v.liikuVielä);
                    dInfoVihLiikJäljellä.add(infoVihLiikJäljellä);

                    // dInfoVihSijX.setText("" + v.hitbox.getCenterX());
                    // dInfoVihSijY.setText("" + v.hitbox.getCenterY());
                    // dInfoVihNopeus.setText("" + v.nopeus);
                    // dInfoVihLiikJäljellä.setText("" + v.liikuVielä);

                    String suunta = "";
                    String suuntaSeuraava = "";

                    switch (v.liikeTapa) {
                        case LOOP_NELIÖ_MYÖTÄPÄIVÄÄN, NELIÖ_MYÖTÄPÄIVÄÄN_ESTEESEEN_ASTI:
                            suunta = "" + v.liikeSuuntaLoopNeliöMyötäpäivään[v.liikeLoopinVaihe % v.liikeSuuntaLoopNeliöMyötäpäivään.length];
                            suuntaSeuraava = "" + v.liikeSuuntaLoopNeliöMyötäpäivään[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopNeliöMyötäpäivään.length];
                        break;
                        case LOOP_NELIÖ_VASTAPÄIVÄÄN, NELIÖ_VASTAPÄIVÄÄN_ESTEESEEN_ASTI:
                            suunta = "" + v.liikeSuuntaLoopNeliöVastapäivään[v.liikeLoopinVaihe % v.liikeSuuntaLoopNeliöVastapäivään.length];
                            suuntaSeuraava = "" + v.liikeSuuntaLoopNeliöVastapäivään[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopNeliöVastapäivään.length];
                        break;
                        case LOOP_VASEN_OIKEA, VASEN_OIKEA_ESTEESEEN_ASTI:
                            suunta = "" + v.liikeSuuntaLoopVasenOikea[v.liikeLoopinVaihe % v.liikeSuuntaLoopVasenOikea.length];
                            suuntaSeuraava = "" + v.liikeSuuntaLoopVasenOikea[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopVasenOikea.length];
                        break;
                        case LOOP_YLÖS_ALAS, YLÖS_ALAS_ESTEESEEN_ASTI:
                            suunta = "" + v.liikeSuuntaLoopYlösAlas[v.liikeLoopinVaihe % v.liikeSuuntaLoopYlösAlas.length];
                            suuntaSeuraava = "" + v.liikeSuuntaLoopYlösAlas[(v.liikeLoopinVaihe +1) % v.liikeSuuntaLoopYlösAlas.length];
                        break;
                        default:
                        break;
                    }

                    JLabel infoVihSuunta = new JLabel(suunta);
                    dInfoVihSuunta.add(infoVihSuunta);

                    JLabel infoVihSuuntaSeuraava = new JLabel(suuntaSeuraava);
                    dInfoVihSuuntaSeuraava.add(infoVihSuuntaSeuraava);
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Konkurrenssi-issue");
            cme.printStackTrace();
        }
    }
}
