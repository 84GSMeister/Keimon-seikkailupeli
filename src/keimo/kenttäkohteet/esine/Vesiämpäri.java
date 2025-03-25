package keimo.kenttäkohteet.esine;

import javax.swing.ImageIcon;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Vesiämpäri extends Ase {

    @Override
    public String käytä() {
        super.poista = true;
        return "Vihollinen ämpäröitiin ja on nyt harmiton.";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Vesiämpäri";
            case "genetiivi": return "Vesiämpärin";
            case "esiivi": return "Vesiämpärinä";
            case "partitiivi": return "Vesiämpäriä";
            case "translatiivi": return "Vesiämpäriksi";
            case "inessiivi": return "Vesiämpärissä";
            case "elatiivi": return "Vesiämpäristä";
            case "illatiivi": return "Vesiämpäriin";
            case "adessiivi": return "Vesiämpärillä";
            case "ablatiivi": return "Vesiämpäriltä";
            case "allatiivi": return "Vesiämpärille";
            default: return "Vesiämpäri";
        }
    }

    public Vesiämpäri(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Vesiämpäri";
        super.tiedostonNimi = "vesiämpäri.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Näyttää juuri oikean kokoiselta vihollisen päähän.";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.vahinko = 2;
        super.hyökkäysAika = 10;
        super.hyökkäysViive = 30;
        super.asetaTiedot();
    }
}
