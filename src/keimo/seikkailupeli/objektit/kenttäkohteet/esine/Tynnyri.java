package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.seikkailupeli.assets.Assets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tynnyri extends Esine {

    public Tynnyri(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Tynnyri";
        super.tiedostonNimi = "tynnyri.png";
        super.tekstuuri = Assets.annaTekstuuri("tynnyri");
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "Tynnyri";
        super.katsomisTeksti = lueKoodiTiedosto();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Tynnyri";
            case "genetiivi":    return "Tynnyrin";
            case "esiivi":       return "Tynnyrinä";
            case "partitiivi":   return "Tynnyriä";
            case "translatiivi": return "Tynnyriksi";
            case "inessiivi":    return "Tynnyrissä";
            case "elatiivi":     return "Tynnyristä";
            case "illatiivi":    return "Tynnyriin";
            case "adessiivi":    return "Tynnyrillä";
            case "ablatiivi":    return "Tynnyriltä";
            case "allatiivi":    return "Tynnyrille";
            default:             return "Tynnyri";
        }
    }

    private String lueKoodiTiedosto() {
        String koodi = "";
        try {
            Scanner sc = new Scanner(new File("src/keimo/seikkailupeli/objektit/kenttäkohteet/esine/Tynnyri.java"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line != null && line != "" && line.length() > 0) {
                    koodi += line;
                }
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return koodi;
    }
}
