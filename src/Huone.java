import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import java.awt.Image;

public class Huone {
    
    private int id;
    private String nimi;
    private int huoneenKoko;
    private KenttäKohde[][] huoneenKenttäSisältö;
    private Maasto[][] huoneenMaastoSisältö;
    private Image tausta;
    int esineitäKentällä = 0;
    int maastoaKentällä = 0;
    boolean näytäAlkuDialogi = false;
    String alkuDialogi;

    static Random r = new Random();

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }

    public int annaKoko() {
        return huoneenKoko;
    }

    public Image annaTausta() {
        return tausta;
    }
    
    public KenttäKohde[][] annaHuoneenKenttäSisältö() {
        return huoneenKenttäSisältö;
    }

    public Maasto[][] annaHuoneenMaastoSisältö() {
        return huoneenMaastoSisältö;
    }

    void sijoitaSatunnaiseenRuutuun(KenttäKohde k){
        int randX = r.nextInt(Main.kentänKoko);
        int randY = r.nextInt(Main.kentänKoko);
        if (huoneenKenttäSisältö[randX][randY] == null) {
            huoneenKenttäSisältö[randX][randY] = k;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Main.kentänKoko * Main.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(k);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void sijoitaSatunnaiseenRuutuun(Maasto m){
        int randX = r.nextInt(Main.kentänKoko);
        int randY = r.nextInt(Main.kentänKoko);
        if (huoneenMaastoSisältö[randX][randY] == null) {
            huoneenMaastoSisältö[randX][randY] = m;
            maastoaKentällä++;
        }
        else {
            if (esineitäKentällä < Main.kentänKoko * Main.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(m);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, KenttäKohde k){
        if (huoneenKenttäSisältö[sijX][sijY] == null) {
            huoneenKenttäSisältö[sijX][sijY] = k;
            esineitäKentällä++;
        }
        else {
            if (maastoaKentällä < Main.kentänKoko * Main.kentänKoko) {
                //sijoitaMäärättyynRuutuun(sijX, sijY, t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ei voi sijoittaa ruutuun, jossa on jo jotakin.", "Virheellinen sijainti.", JOptionPane.ERROR_MESSAGE);
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, Maasto m){
        if (huoneenMaastoSisältö[sijX][sijY] == null) {
            huoneenMaastoSisältö[sijX][sijY] = m;
            maastoaKentällä++;
        }
        else {
            if (maastoaKentällä < Main.kentänKoko * Main.kentänKoko) {
                //sijoitaMäärättyynRuutuun(sijX, sijY, t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ei voi sijoittaa ruutuun, jossa on jo jotakin.", "Virheellinen sijainti.", JOptionPane.ERROR_MESSAGE);
        }
    }

    Huone(int luontiId, int luontiKoko, String luontiNimi, Image luontiTausta, ArrayList<KenttäKohde> luontiKenttäSisältö, ArrayList<Maasto> luontiMaastoSisältö, boolean näytäAlkuDialogi, String alkuDialogi) {
        this.id = luontiId;
        this.nimi = luontiNimi;
        this.huoneenKoko = luontiKoko;
        this.huoneenKenttäSisältö = HuoneLista.luoVakioKenttä(luontiId);
        this.huoneenMaastoSisältö = HuoneLista.luoVakioMaasto(luontiId);
        this.tausta = luontiTausta;
        this.näytäAlkuDialogi = näytäAlkuDialogi;
        this.alkuDialogi = alkuDialogi;

        for (int i = 0; i < huoneenKenttäSisältö.length; i++) {
            for (int j = 0; j < huoneenKenttäSisältö.length; j++) {
                this.huoneenKenttäSisältö[j][i] = null;
                this.huoneenMaastoSisältö[j][i] = null;
            }
        }
        for (KenttäKohde k : luontiKenttäSisältö) {
            if (k.määritettySijainti) {
                sijoitaMäärättyynRuutuun(k.sijX, k.sijY, k);
            }
            else {
                sijoitaSatunnaiseenRuutuun(k);
            }
        }
        for (Maasto m : luontiMaastoSisältö) {
            if (m.määritettySijainti) {
                sijoitaMäärättyynRuutuun(m.sijX, m.sijY, m);
            }
            else {
                sijoitaSatunnaiseenRuutuun(m);
            }
        }
        Main.huoneidenMäärä++;
    }

}
