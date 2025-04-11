package keimo.keimoEngine.toiminnot;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.gui.toimintoIkkunat.*;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.VisuaalinenObjekti;
import keimo.kenttäkohteet.avattavaEste.AvattavaEste;
import keimo.kenttäkohteet.esine.*;
import keimo.kenttäkohteet.kenttäNPC.*;
import keimo.kenttäkohteet.kiintopiste.*;
import keimo.kenttäkohteet.triggeri.*;

public class Vuorovaikutukset {
    
    public static void vuorovaikuta(KenttäKohde k, Esine e) {

        if (k instanceof Kiintopiste) {
            Kiintopiste kp = (Kiintopiste)k;
            if (kp instanceof Nuotio) {
                Nuotio n = (Nuotio)kp;
                if (TavoiteLista.nykyinenTavoite.startsWith("Löydä takaisin kotiin")) {
                    Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), "Sinun on löydettävä kotiin ensin. Voit palata myöhemmin nuotion äärelle.", n.annaNimi());
                }
                else if (TavoiteLista.nykyinenTavoite.startsWith("Etsi pesäpallomaila")) {
                    Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), "Sinun kannattaisi etsiä jokin ase. Voit palata myöhemmin nuotion äärelle.", n.annaNimi());
                }
                else {
                    if (TavoiteLista.nykyinenTavoite.startsWith("Etsi nuotiopaikka")) TavoiteLista.suoritaPääTavoite(2);
                    if (!n.onSytytetty()) {
                        if (e instanceof Paperi) {
                            n.kokeileEsinettä(e);
                            e = null;
                            Peli.valittuEsine = e;
                            Pelaaja.esineet[Peli.esineValInt] = e;
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("lisää sytyke"), n.annaNimi());
                        }
                        else if (e instanceof Hiili) {
                            n.kokeileEsinettä(e);
                            e = null;
                            Peli.valittuEsine = e;
                            Pelaaja.esineet[Peli.esineValInt] = e;
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("lisää polttoaine"), n.annaNimi());
                        }
                        else if (e instanceof Kaasusytytin) {
                            Kaasusytytin ks = (Kaasusytytin)e;
                            if (!ks.toimiva) {
                                Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("ei_toimiva"), n.annaNimi());
                            }
                            else if (ks.toimiva && n.onkoPolttoaine() && n.onkoSytyke()) {
                                n.sytytä(true);
                                TavoiteLista.suoritaPääTavoite(3);
                                Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("sytytä"), n.annaNimi());
                            }
                            else Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.katso(), n.annaNimi());
                        }
                        else if (e instanceof Makkara) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("makkara_ei_sytytetty"), n.annaNimi());
                        }
                        else if (e != null) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + n.annaNimiSijamuodossa("illatiivi"), n.annaNimi());
                        }
                        else Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.katso(), n.annaNimi());
                    }
                    else {
                        if (e instanceof Makkara) {
                            Makkara m = (Makkara)e;
                            Dialogit.avaaDialogi(m.annaDialogiTekstuuri(), m.paista(), m.annaNimi());
                        }
                        else if (e instanceof Vesiämpäri) {
                            n.sytytä(false);
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("sammuta"), n.annaNimi());
                        }
                        else Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.katso(), n.annaNimi());
                    }
                }
            }
            else if (kp instanceof Kirstu) {
                Kirstu kirstu = (Kirstu)kp;
                if (e instanceof Avain) {
                    if (!kirstu.onkoAvattu()) {
                        kirstu.avaa();
                        e = null;
                        Peli.valittuEsine = e;
                        Pelaaja.esineet[Peli.esineValInt] = e;
                        Pelaaja.annaEsine(kirstu.annaSisältöEsine());
                        Dialogit.avaaDialogi(kirstu.annaTekstuuri(), kirstu.haeDialogiTeksti("avaa"), kirstu.annaNimi());
                    }
                }
                else if (e != null) {
                    Dialogit.avaaDialogi(kirstu.annaTekstuuri(), e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + kirstu.annaNimiSijamuodossa("illatiivi"), kirstu.annaNimi());
                }
                else Dialogit.avaaDialogi(kirstu.annaTekstuuri(), kirstu.katso(), kirstu.annaNimi());
            }
            else if (kp instanceof Lepopaikka) {
                Lepopaikka l = (Lepopaikka)kp;
                Dialogit.avaaDialogi(l.annaTekstuuri(), l.vuorovaikuta(e), l.annaNimi());
            }
            else if (kp instanceof KauppaHylly) {
                KauppaHylly hylly = (KauppaHylly)kp;
                int tyhjätPaikat = 0;
                for (Esine esine : Pelaaja.esineet) {
                    if (esine == null) {
                        tyhjätPaikat++;
                    }
                }
                if (Pelaaja.ostosKori.size() >= tyhjätPaikat) {
                    Dialogit.avaaDialogi(hylly.annaDialogiTekstuuri(), "Ostoskoriin ei voi lisätä enempää tavaraa kuin tavaraluettelossa on tyhjiä paikkoja!", hylly.annaNimi());
                }
                else {
                    if (hylly.annaSisältöEsine() instanceof Kuparilager) ÄänentoistamisSäie.toistaSFX("Kalja_kilinä");
                    else ÄänentoistamisSäie.toistaSFX("Kerää");
                    Dialogit.avaaDialogi(hylly.annaDialogiTekstuuri(), "Ostoskoriin lisättiin " + hylly.annaSisältö(), hylly.annaNimi());
                }
                Pelaaja.lisääOstosKoriin(hylly.annaSisältöEsine());
                Pelaaja.ostostenHintaYhteensä = 0;
                for (Esine ostettavaEsine : Pelaaja.ostosKori) {
                    Pelaaja.ostostenHintaYhteensä += ostettavaEsine.annaHinta();
                }
            }
            else if (kp instanceof KauppaRuutu) {
                KauppaRuutu ruutu = (KauppaRuutu)kp;
                boolean ponuAineksetOstettu = false;
                for (Esine pelaajanEsine : Pelaaja.ostosKori) {
                    if (pelaajanEsine instanceof Ponuainekset) {
                        ponuAineksetOstettu = true;
                        break;
                    }
                }
                if (ponuAineksetOstettu) {
                    Dialogit.avaaPitkäDialogiRuutu("kyläkauppa");

                    if (Pelaaja.raha > Pelaaja.ostostenHintaYhteensä) {
                        int tyhjätPaikat = 0;
                        for (Esine esine : Pelaaja.esineet) {
                            if (esine == null) {
                                tyhjätPaikat++;
                            }
                        }
                        if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                            Dialogit.avaaDialogi("", "Ostokset ei mahdu tavaraluetteloon.", "");
                        }
                        else {
                            //PääIkkuna.avaaPitkäDialogiRuutu("kauppa_normaali");
                            Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                            Pelaaja.ostostenHintaYhteensä = 0;
                            for (Esine ostos : Pelaaja.ostosKori) {
                                Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(ostos.annaNimi(), true, 0, 0, null));
                            }
                            Pelaaja.tyhjennäOstoskori();
                        }
                    }
                    else {
                        Pelaaja.tyhjennäOstoskori();
                    }
                }
                else if (Pelaaja.ostostenHintaYhteensä <= 0) {
                    Dialogit.avaaDialogi(ruutu.annaDialogiTekstuuri(), "Meinasitko ostaa jotain?", "ASS-Market kassa");
                }
                else if (Pelaaja.raha >= Pelaaja.ostostenHintaYhteensä) {
                    int tyhjätPaikat = 0;
                    for (Esine esine : Pelaaja.esineet) {
                        if (esine == null) {
                            tyhjätPaikat++;
                        }
                    }
                    if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                        Dialogit.avaaDialogi(ruutu.annaDialogiTekstuuri(), "Ostokset ei mahdu tavaraluetteloon.", "");
                    }
                    else {
                        Dialogit.avaaPitkäDialogiRuutu("kauppa_normaali");
                        Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                        Pelaaja.ostostenHintaYhteensä = 0;
                        for (Esine ostos : Pelaaja.ostosKori) {
                            Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(ostos.annaNimi(), true, 0, 0, null));
                        }
                        Pelaaja.tyhjennäOstoskori();
                    }
                }
                else {
                    Dialogit.avaaPitkäDialogiRuutu("kauppa_eivaraa");
                    Pelaaja.tyhjennäOstoskori();
                }
            }
            else if (kp instanceof BaariRuutu) {
                BaariRuutu ruutu = (BaariRuutu)kp;
                Olutlasi olutlasi = new Olutlasi(true, 0, 0);
                if (Pelaaja.raha >= olutlasi.annaHinta()) {
                    int tyhjätPaikat = 0;
                    for (Esine esine : Pelaaja.esineet) {
                        if (esine == null) {
                            tyhjätPaikat++;
                        }
                    }
                    if (tyhjätPaikat <= 0) {
                        Dialogit.avaaDialogi(ruutu.annaTekstuuri(), "Tavaraluettelo on täynnä. Ei voi lisätä olutta.", ruutu.annaNimi());
                    }
                    else {
                        Dialogit.avaaPitkäDialogiRuutu("baari_normaali");
                        Pelaaja.annaEsine(olutlasi);
                        Pelaaja.raha -= olutlasi.annaHinta();
                    }
                }
                else Dialogit.avaaPitkäDialogiRuutu("baari_eivaraa");
            }
            else if (kp instanceof Ämpärikone) {
                ÄmpäriJonoIkkuna.avaaToimintoIkkuna();
            }
            else if (kp instanceof Pelikone) {
                MinipeliIkkuna.avaaToimintoIkkuna();
            }
            else if (kp instanceof Pulloautomaatti) {
                PullonPalautusIkkuna.avaaToimintoIkkuna();
            }
            else if (kp instanceof Silta) {
                DialogiValintaIkkuna.avaaToimintoIkkuna("silta");
            }
        }
        else if (k instanceof NPC_KenttäKohde) {
            NPC_KenttäKohde kenttäNPC = (NPC_KenttäKohde)k;
            if (kenttäNPC.onkoCustomDialogi()) Dialogit.avaaPitkäDialogiRuutu(kenttäNPC.annaDialogi());
            else {
                if (kenttäNPC instanceof Juhani) {
                    Juhani juhani = (Juhani)kenttäNPC;
                    if (Pelaaja.raha >= 20) {
                        if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                            juhani.annaHuume();
                            Dialogit.avaaDialogi(juhani.annaDialogiTekstuuri(), juhani.haeDialogiTeksti("huume"), juhani.annaNimi());
                        }
                        else Dialogit.avaaDialogi(juhani.annaDialogiTekstuuri(), juhani.haeDialogiTeksti("invatäynnä"), juhani.annaNimi());
                    }
                    else Dialogit.avaaDialogi(juhani.annaDialogiTekstuuri(), juhani.katso(), juhani.annaNimi());
                }
                else if (kenttäNPC instanceof Kauppias) {
                    Kauppias kauppias = (Kauppias)kenttäNPC;
                    Dialogit.avaaDialogi(kauppias.annaDialogiTekstuuri(), kauppias.haeDialogiTeksti("juttele"), kauppias.annaNimi());
                }
                else if (kenttäNPC instanceof JumalVelho) {
                    JumalVelho jv = (JumalVelho)kenttäNPC;
                    if (!jv.löydetty()) {
                        jv.löydäJumalVelho();
                        Dialogit.avaaDialogi(jv.annaDialogiTekstuuri(), jv.haeDialogiTeksti("löydä"), jv.annaNimi());
                    }
                    else {
                        boolean ponuLöytyy = false;
                        boolean jalluLöytyy = false;
                        for (Esine pelaajanEsine : Pelaaja.esineet) {
                            if (pelaajanEsine instanceof Ponuainekset) ponuLöytyy = true;
                            else if (pelaajanEsine instanceof Jallupullo) jalluLöytyy = true; 
                        }
                        if (ponuLöytyy && jalluLöytyy) {
                            for (int i = 0; i < Pelaaja.esineet.length; i++) {
                                if (Pelaaja.esineet[i] instanceof Ponuainekset) {
                                    Pelaaja.esineet[i] = new Paskanmarjat(false, 0, 0);
                                    break;
                                }
                            }
                            Dialogit.avaaDialogi(jv.annaDialogiTekstuuri(), jv.haeDialogiTeksti("anna_paskanmarjat"), jv.annaNimi());
                        }
                        else {
                            Dialogit.avaaDialogi(jv.annaDialogiTekstuuri(), jv.haeDialogiTeksti("booli_vinkki"), jv.annaNimi());
                        }
                    }
                }
                else if (kenttäNPC instanceof JumalYoda) {
                    JumalYoda jy = (JumalYoda)kenttäNPC;
                    if (TavoiteLista.tavoiteLista.get("Avaa takahuone")){
                        Dialogit.avaaDialogi(jy.annaDialogiTekstuuri(), "Hrmm...", "Jumal Yoda");
                    }
                    else {
                        if (TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda")) {
                            jy.löydä(true);
                            Dialogit.avaaPitkäDialogiRuutu("goblin_alku");
                        }
                        else Dialogit.avaaDialogi(jy.annaDialogiTekstuuri(), "Hrmm...", "Goblin");
                    }
                }
                else if (kenttäNPC instanceof Pasi) {
                    if (TavoiteLista.nykyinenTavoite.startsWith("Etsi Pasi")) TavoiteLista.suoritaPääTavoite(5);
                    Dialogit.avaaPitkäDialogiRuutu("pasi");
                }
            }
        }
        else if (k instanceof Triggeri) {
            Triggeri trg = (Triggeri)k;
            System.out.println(trg.annaNimi());
            if (trg.annaVaadittuVihollinen() != null) {
                Dialogit.avaaDialogi(trg.annaDialogiTekstuuri(), "Tähän tarvitaan jokin vihollinen.", trg.annaNimi());
            }
            else if (trg.annaVaadittuEsine() != null) {
                Dialogit.avaaDialogi(trg.annaDialogiTekstuuri(), "Tähän tarvitaan jokin esine.", trg.annaNimi());
            }
            else trg.triggeröi();
        }
        else if (k instanceof VisuaalinenObjekti) {
            VisuaalinenObjekti vo = (VisuaalinenObjekti)k;
            if (vo.onkoKatsottava()) Dialogit.avaaPitkäDialogiRuutu(vo.annaKatsomisDialogi());
        }
        else if (k instanceof AvattavaEste) {

        }
        else if (k != null) {
            Dialogit.avaaDialogi(k.annaDialogiTekstuuri(), k.katso(), k.annaNimi());
        }
    }

    public static void käytäEsinettä(KenttäKohde k, Esine e) {
        if (e != null) {
            if (e.onkoKäyttö()) {
                if (Pelaaja.käyttöViive <= 0) {
                    if (e instanceof Ruoka) {
                        Ruoka ruoka = (Ruoka)e;
                        Dialogit.avaaDialogi(ruoka.annaTekstuuri(), ruoka.käytä(), ruoka.annaNimi());
                        Pelaaja.syöRuoka(ruoka.annaParannusMäärä());
                    }
                    else if (e instanceof Juoma ) {
                        e.käytä();
                    }
                    else if (e instanceof Kartta) {
                        e.käytä();
                    }
                    if (e.poistoon()){
                        Pelaaja.esineet[Peli.esineValInt] = null;
                        e = null;
                    }
                }
            }
            else if (e.onkoKenttäkäyttöön()) {
                if (e instanceof Ase) {
                    Ase ase = (Ase)e;
                    if (Pelaaja.käyttöViive <= 0) {
                        Pelaaja.käytettyAse = ase;
                        Pelaaja.hyökkäysAika += ase.annaHyökkäysAika();
                        Pelaaja.käyttöViive += ase.annaHyökkäysViive();
                        ÄänentoistamisSäie.toistaSFX("Hyökkäys");
                        if (k instanceof Juhani) {
                            Juhani juhani = (Juhani)k;
                            juhani.kuolemaJuhani();
                        }
                    }
                }
            }
        }
    }
}
