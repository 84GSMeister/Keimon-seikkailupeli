package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.Pelaaja.KeimonKylläisyys;
import keimo.Ruudut.PeliRuutu;

import javax.swing.ImageIcon;

public class Sänky extends Kiintopiste {
    
    @Override
    public String kokeileEsinettä(Esine e) {
        if (Pelaaja.känninVoimakkuus <= 0) {
            return "Vielä ei nukuta. Kokeile myöhemmin (tai kun olet kännissä)!";
        }
        else {
            PeliRuutu.nollaaKänniEfekti();
            Pelaaja.nollaaKylläisyys();
            Pelaaja.hp -= (Math.round(Pelaaja.känninVoimakkuusFloat*1.5)-2);
            if (Pelaaja.hp < 0) {
                Pelaaja.hp = 1;
            }
            Pelaaja.päivitäTerveys();
            Pelaaja.känninVoimakkuus = 0;
            Pelaaja.känninVoimakkuusFloat = 0;
            return "Känni on nollattu, mutta krapula saattaa vaikuttaa elinvoimaasi.";
        }
    }
    
    public Sänky (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Sänky";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/sänky.png");
        super.katsomisTeksti = "Nukuttaako?";
        super.asetaTiedot();
    }
}
