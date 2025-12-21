package keimo.seikkailupeli.kenttä;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.objektit.Pelaaja;

public class ErikoisTileMuutokset {
    private static Tekstuuri kassaVihkoauki = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/kassa_vihkoauki.png");
    private static Tekstuuri kassaVihkokiinni = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/kassa_vihkokiinni.png");
    private static Tekstuuri virheTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png");

    public static Renderöitävä annaSpesiaaliTekstuuri(Renderöitävä tekstuuri, String alkuperäinen, int sijX, int sijy) {
        // Erittäin hardkoodattua settiä
        switch (alkuperäinen) {
            case "kassa_vihkokiinni.png":
                if (Peli.huone.annaNimi().startsWith("Kauppa") &&
                    Peli.syötteenTila == SyötteenTila.DIALOGI &&
                    sijX == Pelaaja.sijX + 1
                ) return kassaVihkoauki;
                else return kassaVihkokiinni;
            case null:
                return virheTekstuuri;
            default:
                return tekstuuri;
        }
    }
}
