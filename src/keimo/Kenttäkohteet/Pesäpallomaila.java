package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public final class Pesäpallomaila extends Ase {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Pesäpallomaila p = new Pesäpallomaila(false, 0, 0);
        //System.out.println("Haluutsä turpaan? Ja vielä " + p.annanimiSijamuodossa("adessiivi") + "!");
        // ^ Haluutsä turpaan? Ja vielä Pesäpallomailalla!
        switch (sijamuoto) {
            case "nominatiivi": return "Pesäpallomaila";
            case "genetiivi": return "Pesäpallomailan";
            case "esiivi": return "Pesäpallomailana";
            case "partitiivi": return "Pesäpallomailaa";
            case "translatiivi": return "Pesäpallomailaksi";
            case "inessiivi": return "Pesäpallomailassa";
            case "elatiivi": return "Pesäpallomailasta";
            case "illatiivi": return "Pesäpallomailaan";
            case "adessiivi": return "Pesäpallomailalla";
            case "ablatiivi": return "Pesäpallomailalta";
            case "allatiivi": return "Pesäpallomailalle";
            default: return "Pesäpallomaila";
        }
    }

    public Pesäpallomaila(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Pesäpallomaila";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Pikkuvihu");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pesäpallomaila.png");
        super.tiedostonNimi = "pesäpallomaila.png";
        super.katsomisTeksti = "Tulkaapas tänne viholliset jos uskallatte!";
        super.käyttöTeksti = "Löit vihollista turpaan";
        super.vahinko = 1;
        super.hyökkäysAika = 5;
        super.hyökkäysViive = 20;
        super.asetaTiedot();
    }
}
