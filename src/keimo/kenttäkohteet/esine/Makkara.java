package keimo.kenttäkohteet.esine;

import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Makkara extends Ruoka {
    
    public boolean paistettu = false;
    public boolean käristetty = false;

    public Makkara(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Makkara";
        super.tiedostonNimi = "makkarat.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.heal = 1;
        super.hinta = 2.79;
        super.käyttö = true;
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        this.paistettu = false;
        this.käristetty = false;
        super.asetaTiedot();
    }

    @Override
    public String katso() {
        if (this.käristetty) {
            return "Makkara on läpeensä käristynyt. Ei näytä järin maittavalta.";
        }
        else if (this.paistettu) {
            return "Mmm... onpas herkullisen näköistä kyrsää.";
        }
        else {
            return "Makkaran voisi varmaan paistaa nuotiolla.";
        }
    }
    
    @Override
    public String käytä() {
        super.käytä();
        super.poista = true;
        if (käristetty) {
            super.heal = -1;
            TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.HIILTYNYT_MAKKARA;
            return "Ugh. Olipas pahaa hiiltynyttä makkaraa. Menetit 1 elämäpisteen.";
        }
        else if (paistettu) {
            super.heal = 5;
            return "Se oli erittäin makoisaa kyrsää. Sait " + super.heal + " elämäpistettä.";
        }
        else {
            super.heal = 1;
            return "Ei niin hyvää raakana. Sait " + super.heal + " elämäpisteen.";
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Makkara";
            case "genetiivi":    return "Makkaran";
            case "esiivi":       return "Makkarana";
            case "partitiivi":   return "Makkaraa";
            case "translatiivi": return "Makkaraksi";
            case "inessiivi":    return "Makkarassa";
            case "elatiivi":     return "Makkarasta";
            case "illatiivi":    return "Makkaraan";
            case "adessiivi":    return "Makkaralla";
            case "ablatiivi":    return "Makkaralta";
            case "allatiivi":    return "Makkaralle";
            default:             return "Makkara";
        }
    }

    public String paista() {
        if (this.käristetty) {
            return "Makkara on jo käristetty hiileksi";
        }
        else if (this.paistettu) {
            this.käristetty = true;
            super.nimi = "Käristetty makkara";
            super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/makkarat_käristetty.png");
            return "Paistoit liikaa! Nyt makkara on pikimusta.";
        }
        else {
            this.paistettu = true;
            super.nimi = "Paistettu makkara";
            super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/makkarat_paistettu.png");
            return "Mmm.. onpas hyvän näköistä kyrsää.";
        }
    }
}
