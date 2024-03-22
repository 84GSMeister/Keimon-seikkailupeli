package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.Ruudut.PeliRuutu;

public abstract class Lepopaikka extends Kiintopiste {

    long hpVähennys;

    @Override
    public String vuorovaikuta(Esine e) {
        if (Pelaaja.känninVoimakkuusFloat <= 0) {
            return "Vielä ei nukuta. Kokeile myöhemmin (tai kun olet kännissä)!";
        }
        else {
            PeliRuutu.nollaaKänniEfekti();
            Pelaaja.nollaaKylläisyys();
            System.out.println(hpVähennys);
            if (hpVähennys > 0) {
                Pelaaja.hp -= hpVähennys;
            }
            if (Pelaaja.hp < 1) {
                Pelaaja.hp = 1;
            }
            System.out.println("hp: " + Pelaaja.hp);
            Pelaaja.päivitäTerveys();
            Pelaaja.känninVoimakkuusFloat = 0;
            return "Känni on nollattu, mutta krapula saattaa vaikuttaa elinvoimaasi.";
        }
    }
    
    public Lepopaikka(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
    }
}
