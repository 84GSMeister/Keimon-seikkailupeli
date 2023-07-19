package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.PääIkkuna;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class KauppaRuutu extends Kiintopiste {
    
    @Override
    public void näytäDialogi(Esine e) {
        boolean ponuAineksetOstettu = false;
        for (Esine pelaajanEsine : Pelaaja.ostosKori) {
            if (pelaajanEsine instanceof Ponuainekset) {
                ponuAineksetOstettu = true;
                break;
            }
        }
        if (ponuAineksetOstettu) {
            PääIkkuna.avaaPitkäDialogiRuutu("kyläkauppa");

            if (Pelaaja.raha > Pelaaja.ostostenHintaYhteensä) {
                int tyhjätPaikat = 0;
                for (Esine esine : Pelaaja.esineet) {
                    if (esine == null) {
                        tyhjätPaikat++;
                    }
                }
                if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                    PääIkkuna.avaaDialogi(null, "Ostokset ei mahdu tavaraluetteloon.", "");
                }
                else {
                    PääIkkuna.avaaPitkäDialogiRuutu("kauppa_normaali");
                    Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                    Pelaaja.ostostenHintaYhteensä = 0;
                    for (Esine ostos : Pelaaja.ostosKori) {
                        Pelaaja.annaEsine(ostos);
                    }
                    Pelaaja.tyhjennäOstoskori();
                }
            }
            else {
                Pelaaja.tyhjennäOstoskori();
            }
        }
        else if (Pelaaja.ostostenHintaYhteensä <= 0) {
            PääIkkuna.avaaDialogi(this.annaDialogiKuvake(), "Meinasitko ostaa jotain?", "ASS-Market kassa");
        }
        else if (Pelaaja.raha >= Pelaaja.ostostenHintaYhteensä) {
            int tyhjätPaikat = 0;
            for (Esine esine : Pelaaja.esineet) {
                if (esine == null) {
                    tyhjätPaikat++;
                }
            }
            if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                PääIkkuna.avaaDialogi(null, "Ostokset ei mahdu tavaraluetteloon.", "");
            }
            else {
                PääIkkuna.avaaPitkäDialogiRuutu("kauppa_normaali");
                Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                Pelaaja.ostostenHintaYhteensä = 0;
                for (Esine ostos : Pelaaja.ostosKori) {
                    Pelaaja.annaEsine(ostos);
                }
                Pelaaja.tyhjennäOstoskori();
            }
        }
        else {
            PääIkkuna.avaaPitkäDialogiRuutu("kauppa_eivaraa");
            Pelaaja.tyhjennäOstoskori();
        }
    }
    
    public KauppaRuutu (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kaupparuutu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaupparuutu.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.tiedostonNimi = "kaupparuutu.png";
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Kylien kauppias";
        super.asetaTiedot();
    }
}
