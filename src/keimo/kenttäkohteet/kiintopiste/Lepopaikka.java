package keimo.kenttäkohteet.kiintopiste;

import java.util.ArrayList;

import keimo.Pelaaja;
import keimo.kenttäkohteet.esine.Esine;

public abstract class Lepopaikka extends Kiintopiste {

    public long hpVähennys;
    public boolean vältäKuolema = false;

    public Lepopaikka(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
    }

    public String vuorovaikuta(Esine e) {
        if (Pelaaja.känninVoimakkuusFloat <= 0) {
            return "Vielä ei nukuta. Kokeile myöhemmin (tai kun olet kännissä)!";
        }
        else {
            vältäKuolema = false;
            Pelaaja.nollaaKylläisyys();
            System.out.println(hpVähennys);
            if (hpVähennys > 0) {
                Pelaaja.hp -= hpVähennys;
            }
            if (Pelaaja.hp < 1) {
                Pelaaja.hp = 1;
                vältäKuolema = true;
            }
            Pelaaja.päivitäTerveys();
            Pelaaja.känninVoimakkuusFloat = 0;
            if (hpVähennys <= 0) {
                return "Nukuit kännisenä yön yli. Sinulle ei tullut krapulaa.";
            }
            else if (hpVähennys == 1) {
                if (vältäKuolema) return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpisteen). Selvisit kuitenkin hengissä 1 elämäpisteen kanssa (krapulaan ei voi kuolla).";
                else return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpisteen).";
            }
            else {
                if (vältäKuolema) return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpistettä). Selvisit kuitenkin hengissä 1 elämäpisteen kanssa (krapulaan ei voi kuolla).";
                else return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpistettä).";
            }
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "känni":
                if (hpVähennys <= 0) {
                    return "Nukuit kännisenä yön yli. Sinulle ei tullut krapulaa.";
                }
                else if (hpVähennys == 1) {
                    if (vältäKuolema) return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpisteen). Selvisit kuitenkin hengissä 1 elämäpisteen kanssa (krapulaan ei voi kuolla).";
                    else return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpisteen).";
                }
                else {
                    if (vältäKuolema) return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpistettä). Selvisit kuitenkin hengissä 1 elämäpisteen kanssa (krapulaan ei voi kuolla).";
                    else return "Nukuit kännisenä yön yli. Nyt sinulla on krapula (menetit " + hpVähennys + " elämäpistettä).";
                }
            case "selvä": return "Vielä ei nukuta. Kokeile myöhemmin (tai kun olet kännissä)!";
            case null, default: return katso();
        }
    }
}
