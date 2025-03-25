package keimo.kenttäkohteet.esine;

import java.awt.Image;

import javax.swing.ImageIcon;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Sieni extends Ruoka {

    @Override
    public String käytä() {
        super.käytä();
        super.poista = true;
        return "Sieniä, sieniä! (sait 3 elämäpistettä)";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Sieni";
            case "genetiivi":    return "Sienen";
            case "esiivi":       return "Sienenä";
            case "partitiivi":   return "Sientä";
            case "translatiivi": return "Sieneksi";
            case "inessiivi":    return "Sienessä";
            case "elatiivi":     return "Sienestä";
            case "illatiivi":    return "Sieneen";
            case "adessiivi":    return "Sienellä";
            case "ablatiivi":    return "Sieneltä";
            case "allatiivi":    return "Sienelle";
            default:             return "Sieni";
        }
    }

    public Sieni(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Sieni";
        super.tiedostonNimi = "sieni.png";
        super.kuvake = new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi).getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT));
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.käyttö = true;
        super.heal = 3;
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "Sieni";
        super.katsomisTeksti = "Kärpässienelläkin voi kuulemma saada aikaan jonkinmoisen efektin, vaikka se ei oikeaa taikasientä vastaakaan.";
        super.asetaTiedot();
    }
}
