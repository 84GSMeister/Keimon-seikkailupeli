package keimo.keimoengine.ikkuna;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;
import org.lwjgl.PointerBuffer;

public class DialogiIkkunat {

    /**
     * Yksinkertainen viestidialogi. Wrapper TinyFileDialogs -luokan ympärille.
     * @param otsikko Dialogin otsikko ikkunan yläpalkissa
     * @param viesti Ruudussa näkyvä viesti
     * @param valintaTyyppi "ok", "okcancel", "yesno" tai "yesnocancel"
     * @param viestiTyyppi "info", "warning", "error" tai "question"
     * @param vakioValinta Oletuksena valittu painike. false = ei/peruuta; true = kyllä/ok
     * @return kyllä/ok valittu
     */
    public static boolean viestiIkkuna(String otsikko, String viesti, String valintaTyyppi, String viestiTyyppi, boolean vakioValinta) {
        return tinyfd_messageBox(otsikko, viesti, valintaTyyppi, viestiTyyppi, vakioValinta);
    }

    /**
     * Tekstisyöteikkuna. Wrapper TinyFileDialogs -luokan ympärille.
     * @param otsikko Dialogin otsikko ikkunan yläpalkissa
     * @param viesti Tekstikentän yläpuolella näkyvä viesti
     * @param oletusSyöte Tekstikentän sisältö ikkunan avautuessa
     * @return Teksti, joka kenttään syötettiin
     */

    public static String syöteIkkuna(String otsikko, String viesti, String oletusSyöte) {
        return tinyfd_inputBox(otsikko, viesti, oletusSyöte);
    }

    /**
     * Tiedostoselainikkuna tiedoston avaamiselle. Wrapper TinyFileDialogs -luokan ympärille.
     * @param otsikko Dialogin otsikko ikkunan yläpalkissa
     * @param tiedostoPolku Polku, joka tiedostoselaimeen aukeaa vakiona
     * @param suodattimet Mitä tiedostotyyppejä selaimessa näytetään
     * @param tiedostoTyypit Tiedostotyyppien selitys, joka näkyy tiedostoselaimen valikossa
     * @param salliUseitaTiedostoja Salli useiden tiedostojen valitseminen selaimessa
     * @return Valitun tiedoston polku
     */
    public static String tiedostoSelainAvaa(String otsikko, String tiedostoPolku, PointerBuffer suodattimet, String tiedostoTyypit, boolean salliUseitaTiedostoja) {
        return tinyfd_openFileDialog(otsikko, tiedostoPolku, suodattimet, tiedostoTyypit, salliUseitaTiedostoja);
    }

    /**
     * Tiedostoselainikkuna tiedoston tallentamiselle. Wrapper TinyFileDialogs -luokan ympärille.
     * @param otsikko Dialogin otsikko ikkunan yläpalkissa
     * @param tiedostoPolku Polku, joka tiedostoselaimeen aukeaa vakiona
     * @param suodattimet Mitä tiedostotyyppejä selaimessa näytetään
     * @param tiedostoTyypit Tiedostotyyppien selitys, joka näkyy tiedostoselaimen valikossa
     * @return Tallennetun tiedoston polku
     */
    public static String tiedostoSelainTallenna(String otsikko, String tiedostoPolku, PointerBuffer suodattimet, String tiedostoTyypit) {
        return tinyfd_saveFileDialog(otsikko, tiedostoPolku, suodattimet, tiedostoTyypit);
    }
}
