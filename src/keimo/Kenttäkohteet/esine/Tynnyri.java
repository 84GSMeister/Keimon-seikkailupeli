package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public class Tynnyri extends Esine {

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

    public Tynnyri(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Tynnyri";
        super.tiedostonNimi = "tynnyri.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "Tynnyri";
        super.katsomisTeksti = "package keimo.kenttäkohteet.esine;\r\n" + //
                        "import keimo.keimoEngine.grafiikat.Tekstuuri;\r\n" + //
                        "import javax.swing.ImageIcon;\r\n" + //
                        "public class Tynnyri extends Esine {\r\n" + //
                        "    public Tynnyri(boolean määritettySijainti, int sijX, int sijY) {\r\n" + //
                        "        super(määritettySijainti, sijX, sijY);\r\n" + //
                        "        super.nimi = \"Tynnyri\";\r\n" + //
                        "        super.tiedostonNimi = \"tynnyri.png\";\r\n" + //
                        "        super.kuvake = new ImageIcon(\"tiedostot/kuvat/kenttäkohteet/\" + tiedostonNimi);\r\n" + //
                        "        super.tekstuuri = new Tekstuuri(\"tiedostot/kuvat/kenttäkohteet/\" + tiedostonNimi);\r\n" + //
                        "        super.kolmiUlotteinen = true;\r\n" + //
                        "        super.obj3dMallinTunniste = \"Tynnyri\";\r\n" + //
                        "        super.liikeNopeus = 10f;\r\n" + //
                        "        super.pyörimisNopeus = 2f;\r\n" + //
                        "        super.katsomisTeksti = \"\";\r\n" + //
                        "        super.asetaTiedot();\r\n" + //
                        "    }\r\n" + //
                        "}";
        super.asetaTiedot();
    }
}
