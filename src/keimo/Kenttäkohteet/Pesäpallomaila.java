package keimo.Kenttäkohteet;

import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class Pesäpallomaila extends Esine {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Pesäpallomaila";
            case "genetiivi":
                return "Pesäpallomailan";
            case "esiivi":
                return "Pesäpallomailana";
            case "partitiivi":
                return "Pesäpallomailaa";
            case "translatiivi":
                return "Pesäpallomailaksi";
            case "inessiivi":
                return "Pesäpallomailassa";
            case "elatiivi":
                return "Pesäpallomailasta";
            case "illatiivi":
                return "Pesäpallomailaan";
            case "adessiivi":
                return "Pesäpallomailalla";
            case "ablatiivi":
                return "Pesäpallomailalta";
            case "allatiivi":
                return "Pesäpallomailalle";
            default:
                return "Pesäpallomaila";
        }
    }

    public Pesäpallomaila(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Pesäpallomaila";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Pikkuvihu");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pesäpallomaila.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.katsomisTeksti = "Tulkaapas tänne viholliset jos uskallatte!";
        super.käyttöTeksti = "Löit vihollista turpaan";
        super.asetaTiedot();
    }
}
