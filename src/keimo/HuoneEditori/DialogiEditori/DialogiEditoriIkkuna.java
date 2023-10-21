package keimo.HuoneEditori.DialogiEditori;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class DialogiEditoriIkkuna {

    static TextArea tekstiMuokkausKenttä;
    
    public static void luoDialogiEditoriIkkuna() {

        Frame ikkuna = new Frame("Dialogieditori");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(0, 0, 800, 600);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.setVisible(true);

        MenuItem tuoTekstitiedostosta = new MenuItem("Tuo tekstitiedostosta");
        tuoTekstitiedostosta.addActionListener(e -> {
            FileDialog tiedostoSelain = new FileDialog(ikkuna, "Avaa", FileDialog.LOAD);
            tiedostoSelain.setVisible(true);
            String tiedostoPolku = tiedostoSelain.getFile();
            File tiedosto = new File(tiedostoPolku);
            try {
                Scanner sc = new Scanner(tiedosto);
                String tiedostonTeksti = "";
                while (sc.hasNextLine()) {
                    tiedostonTeksti += sc.nextLine() + "\n";
                }
                tekstiMuokkausKenttä.setText(tiedostonTeksti);
                sc.close();
            }
            catch (FileNotFoundException fnfe) {
                System.out.println("Tiedostoa ei löytynyt.");
            }
            System.out.println(tiedosto);
        });

        Menu tiedosto = new Menu("Tiedosto");
        tiedosto.add(tuoTekstitiedostosta);

        MenuBar yläPalkki = new MenuBar();
        yläPalkki.add(tiedosto);

        Label valitseDialogiLabel = new Label("Valitse dialogi:");
        Choice dialogiValintaLaatikko = new Choice();
        for (String dialogiNimi : VuoropuheDialogit.vuoropuheDialogiKartta.keySet()) {
            dialogiValintaLaatikko.add(dialogiNimi);
        }
        dialogiValintaLaatikko.setPreferredSize(new Dimension(80, 20));
        dialogiValintaLaatikko.addItemListener(e -> {
            String dialogiTunniste = dialogiValintaLaatikko.getSelectedItem();
            String tarinaTeksti = "";
            for (String s : VuoropuheDialogit.vuoropuheDialogiKartta.get(dialogiTunniste).annaTekstit()) {
                tarinaTeksti += s;
                tarinaTeksti += "\n\n";
            }
            tekstiMuokkausKenttä.setText(tarinaTeksti);
        });

        Panel yläPaneeli = new Panel();
        yläPaneeli.setName("yläpaneeli");
        yläPaneeli.add(valitseDialogiLabel);
        yläPaneeli.add(dialogiValintaLaatikko);


        tekstiMuokkausKenttä = new TextArea();
        tekstiMuokkausKenttä.setSize(800, 300);
        String dialogiTunniste = dialogiValintaLaatikko.getSelectedItem();
        String tarinaTeksti = "";
        for (String s : VuoropuheDialogit.vuoropuheDialogiKartta.get(dialogiTunniste).annaTekstit()) {
            tarinaTeksti += s;
            tarinaTeksti += "\n\n";
        }
        tekstiMuokkausKenttä.setText(tarinaTeksti);

        Panel tekstiPaneeli = new Panel(new BorderLayout());
        tekstiPaneeli.setName("tekstiPaneeli");
        tekstiPaneeli.add(tekstiMuokkausKenttä);


        Button okNappi = new Button("OK");
        okNappi.setBounds(0, 0, 20, 20);
        okNappi.addActionListener(e -> ikkuna.dispose());

        Button cancelNappi = new Button("Peruuta");
        cancelNappi.setBounds(60, 0, 20, 20);
        cancelNappi.addActionListener(e -> ikkuna.dispose());

        Panel alaPaneeli = new Panel();
        alaPaneeli.setName("alaPaneeli");
        alaPaneeli.add(okNappi);
        alaPaneeli.add(cancelNappi);


        ikkuna.setMenuBar(yläPalkki);
        ikkuna.add(yläPaneeli, BorderLayout.NORTH);
        ikkuna.add(tekstiPaneeli, BorderLayout.CENTER);
        ikkuna.add(alaPaneeli, BorderLayout.SOUTH);
    }
}
