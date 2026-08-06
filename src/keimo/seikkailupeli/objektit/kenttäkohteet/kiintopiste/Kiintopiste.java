package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;

public abstract class Kiintopiste extends KenttäKohde {

    ArrayList<String> käyvätEsineet = new ArrayList<String>();

    public Kiintopiste(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Tämä kohde";
            case "genetiivi":    return "Tämän kohteem";
            case "essiivi":      return "Tänä kohteena";
            case "partitiivi":   return "Tätä kohdetta";
            case "translatiivi": return "Täksi kohteeksi";
            case "inessiivi":    return "Tässä kohteessa";
            case "elatiivi":     return "Tästä kohteesta";
            case "illatiivi":    return "Tähän kohteeseen";
            case "adessiivi":    return "Tällä kohteella";
            case "ablatiivi":    return "Tältä kohteelta";
            case "allatiivi":    return "Tälle kohteelle";
            default:             return "Tämä kohde";
        }
    }
}
