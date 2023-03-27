package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public class Seteli extends Esine{

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Seteli";
            case "genetiivi":
                return "Setelin";
            case "esiivi":
                return "Setelinä";
            case "partitiivi":
                return "Seteliä";
            case "translatiivi":
                return "Seteliksi";
            case "inessiivi":
                return "Setelissä";
            case "elatiivi":
                return "Setelistä";
            case "illatiivi":
                return "Seteliin";
            case "adessiivi":
                return "Setelillä";
            case "ablatiivi":
                return "Seteliltä";
            case "allatiivi":
                return "Setelille";
            default:
                return "Seteli";
        }
    }
    
    public Seteli(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Seteli";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Juhani");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/seteli.png");
        super.katsomisTeksti = "2 kybää = 1 massi";
        super.asetaTiedot();
    }
}
