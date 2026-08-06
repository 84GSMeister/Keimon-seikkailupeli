package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.seikkailupeli.assets.Assets;

import java.util.ArrayList;

public final class KauppaHylly extends Säiliö {

    public KauppaHylly(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Kauppahylly";
        super.tiedostonNimi = "kauppahylly.png";
        super.tekstuuri = Assets.annaTekstuuri("kauppahylly");
        super.katsomisTeksti = "Tyhjä hylly";

        if (ominaisuusLista != null) {
            String esineenNimi = "";
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("sisältö=")) {
                    esineenNimi = ominaisuus.substring(8);
                    super.tekstuuri = Assets.annaTekstuuri("kauppahylly_" + esineenNimi);
                }
            }
            if (esineenNimi == "") {
                super.tekstuuri = Assets.annaTekstuuri("kauppahylly");
            }
        }
        super.asetaTiedot();
    }
}
