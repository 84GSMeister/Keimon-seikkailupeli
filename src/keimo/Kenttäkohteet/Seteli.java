package keimo.Kenttäkohteet;

import keimo.Pelaaja;

import javax.swing.ImageIcon;

public final class Seteli extends Esine {

    @Override
    public String käytä() {
        Pelaaja.raha += 20;
        super.poista = true;
        return "Kaks kybää";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Seteli";
            case "genetiivi": return "Setelin";
            case "esiivi": return "Setelinä";
            case "partitiivi": return "Seteliä";
            case "translatiivi": return "Seteliksi";
            case "inessiivi": return "Setelissä";
            case "elatiivi": return "Setelistä";
            case "illatiivi": return "Seteliin";
            case "adessiivi": return "Setelillä";
            case "ablatiivi": return "Seteliltä";
            case "allatiivi": return "Setelille";
            default: return "Seteli";
        }
    }

    public Seteli(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Seteli";
        super.käyttö = true;
        super.sopiiKäytettäväksi.add("Juhani");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/seteli.png");
        super.tiedostonNimi = "seteli.png";
        super.katsomisTeksti = "2 kybää = 1 massi";
        super.asetaTiedot();
    }
}
