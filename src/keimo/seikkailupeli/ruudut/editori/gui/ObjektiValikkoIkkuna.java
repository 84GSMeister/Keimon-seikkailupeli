package keimo.seikkailupeli.ruudut.editori.gui;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.ObjektiListaNappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.gui.yläpalkki.Yläpalkki;
import keimo.seikkailupeli.objektit.PeliObjekti;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ObjektiValikkoIkkuna {

    private static float scaleX = 0.75f;
    private static float scaleY = 0.5f;
    private static int objektienMäärä = KenttäKohde.kenttäkohdeLista.length;
    private static int sarakkeet = 4;
    private static int rivit = (objektienMäärä+sarakkeet-1) / sarakkeet;
    private static float scaleXObjekti = scaleX / sarakkeet;
    private static float scaleYObjekti = scaleY / rivit;
    private static float scaleYPohja = 0.8f;

    private static Renderöitävä objektiValikkoPohjaTekstuuri = Assets.annaTekstuuri("editori_objektivalikko_pohja");
    private static Renderöitävä objektiValikkoVälilehtiTekstuuri = Assets.annaTekstuuri("editori_objektivalikko_välilehti");
    private static LabelKomponentti välilehtiObjektitLabel = new LabelKomponentti(0.25f, 0.1f, -0.5f, 0.6f, objektiValikkoVälilehtiTekstuuri);
    private static LabelKomponentti välilehtiMaastoLabel = new LabelKomponentti(0.25f, 0.1f, 0, 0.6f, objektiValikkoVälilehtiTekstuuri);
    private static LabelKomponentti välilehtiEntitytLabel = new LabelKomponentti(0.25f, 0.1f, 0.5f, 0.6f, objektiValikkoVälilehtiTekstuuri);

    private static Teksti välilehtiObjektitTeksti = new Teksti("Objektit", Väri.white, 400, 48);
    private static Teksti välilehtiMaastoTeksti = new Teksti("Maasto", Väri.white, 320, 48);
    private static Teksti välilehtiEntitytTeksti = new Teksti("Entityt", Väri.white, 350, 48);
    private static Nappi välilehtiObjektitNappi = new Nappi(0.25f, 0.1f, -0.475f, 0.6f, välilehtiObjektitTeksti);
    private static Nappi välilehtiMaastoNappi = new Nappi(0.25f, 0.1f, 0.025f, 0.6f, välilehtiMaastoTeksti);
    private static Nappi välilehtiEntitytNappi = new Nappi(0.25f, 0.1f, 0.525f, 0.6f, välilehtiEntitytTeksti);

    private static HashMap<Integer, ObjektiListaNappi> esineValikko = new HashMap<>();
    private static HashMap<Integer, ObjektiListaNappi> maastoValikko = new HashMap<>();
    private static HashMap<Integer, ObjektiListaNappi> entityValikko = new HashMap<>();

    private static HashMap<String, MaastoKuva> maastoKuvat = new HashMap<>();
    private static List<MaastoKuva> kuvaLista = new ArrayList<>();

    private static enum Välilehdet {
        OBJEKTIT,
        MAASTO,
        ENTITYT;
    }
    private static Välilehdet välilehti = Välilehdet.OBJEKTIT;

    public static void luoObjektiValikko() {
        for (int i = 0; i < KenttäKohde.kenttäkohdeLista.length; i++) {
            String objNimi = KenttäKohde.kenttäkohdeLista[i];
            int objSijX = i % 4;
            int objSijY = (i) / 4;
            if (objSijY < 0) objSijY = 0;
            float offsetX = scaleXObjekti * (objSijX - sarakkeet/2) * 1.5f;
            float offsetY = scaleYObjekti * (-objSijY + rivit/2) * 2;
            Teksti objektinNimiTeksti = new Teksti(objNimi, Väri.white, 800, 48);
            Renderöitävä objektinKuvake = KenttäKohde.luoObjektiTiedoilla(objNimi, 0, 0, null).annaTekstuuri();
            esineValikko.put(i, new ObjektiListaNappi(i, objNimi, scaleXObjekti, scaleYObjekti, offsetX, offsetY, objektinNimiTeksti, objektinKuvake));
        }

        List<MaastoKuva> tileKuvaLista = listaaMaastoKuvat("tiedostot/kuvat/maasto");
        List<MaastoKuva> isolaattaKuvaLista = listaaMaastoKuvat("tiedostot/kuvat/maasto/isot_laatat");
        kuvaLista = Stream.concat(tileKuvaLista.stream(), isolaattaKuvaLista.stream()).collect(Collectors.toList());
        for (int i = 0; i < kuvaLista.size(); i++) {
            String objNimi = kuvaLista.get(i).annaNimi();
            int objSijX = i % 4;
            int objSijY = (i) / 4;
            if (objSijY < 0) objSijY = 0;
            float offsetX = scaleXObjekti * (objSijX - sarakkeet/2) * 1.5f;
            float offsetY = scaleYObjekti * (-objSijY + rivit/2) * 2;
            String objNimiIlmanTiedostopäätettä = objNimi.substring(0, objNimi.length()-4);
            Teksti objektinNimiTeksti = new Teksti(objNimiIlmanTiedostopäätettä, Väri.white, 800, 48);
            Renderöitävä objektinKuvake = Assets.annaTileTekstuurit().get(objNimiIlmanTiedostopäätettä);
            maastoValikko.put(i, new ObjektiListaNappi(i, objNimi, scaleXObjekti, scaleYObjekti, offsetX, offsetY, objektinNimiTeksti, objektinKuvake));
            maastoKuvat.put(kuvaLista.get(i).annaNimi(), kuvaLista.get(i));
        }

        for (int i = 0; i < Entity.entityLista.length; i++) {
            String objNimi = Entity.entityLista[i];
            int objSijX = i % 4;
            int objSijY = (i) / 4;
            if (objSijY < 0) objSijY = 0;
            float offsetX = scaleXObjekti * (objSijX - sarakkeet/2) * 1.5f;
            float offsetY = scaleYObjekti * (-objSijY + rivit/2) * 2;
            Teksti objektinNimiTeksti = new Teksti(objNimi, Väri.white, 800, 48);
            Renderöitävä objektinKuvake = Entity.luoEntityTiedoilla(objNimi, 0, 0, null).annaTekstuuri();
            entityValikko.put(i, new ObjektiListaNappi(i, objNimi, scaleXObjekti, scaleYObjekti, offsetX, offsetY, objektinNimiTeksti, objektinKuvake));
        }
    }
    
    public static void tarkistaListaHover(int hiiriX, int hiiriY) {
        välilehtiObjektitNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiMaastoNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiEntitytNappi.hiiriSisällä(hiiriX, hiiriY);
        switch (välilehti) {
            case OBJEKTIT -> {
                for (Nappi nappi : esineValikko.values()) {
                    nappi.hiiriSisällä(hiiriX, hiiriY);
                }
            }
            case MAASTO -> {
                for (Nappi nappi : maastoValikko.values()) {
                    nappi.hiiriSisällä(hiiriX, hiiriY);
                }
            }
            case ENTITYT -> {
                for (Nappi nappi : entityValikko.values()) {
                    nappi.hiiriSisällä(hiiriX, hiiriY);
                }
            }
        }
    }

    public static void tarkistaKlikkaus(int hiiriX, int hiiriY) {
        if (välilehtiObjektitNappi.hiiriSisällä(hiiriX, hiiriY)) {
            välilehti = Välilehdet.OBJEKTIT;
        }
        else if (välilehtiMaastoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            välilehti = Välilehdet.MAASTO;
        }
        else if (välilehtiEntitytNappi.hiiriSisällä(hiiriX, hiiriY)) {
            välilehti = Välilehdet.ENTITYT;
        }
        switch (välilehti) {
            case OBJEKTIT -> {
                String valittuObjekti = valitseObjekti(hiiriX, hiiriY);
                if (valittuObjekti != null && valittuObjekti != "") {
                    EditoriRuutu.valitunEsineenNimi = valittuObjekti;
                    PeliObjekti valittuPeliObjekti = KenttäKohde.luoObjektiTiedoilla(EditoriRuutu.valitunEsineenNimi, 0, 0, null);
                    EditoriRuutu.valittuEsine = valittuPeliObjekti;
                    EditoriRuutu.kopioidunEsineenOminaisuudet = valittuPeliObjekti.annaLisäOminaisuudet();
                    EditoriRuutu.kopioitu = false;
                    Yläpalkki.asetaValittuObjekti(valittuPeliObjekti);
                    EditoriRuutu.avaaObjektiValikko(false);
                }
            }
            case MAASTO -> {
                String valittuObjekti = valitseObjekti(hiiriX, hiiriY);
                MaastoKuva valittuKuva = maastoKuvat.get(valittuObjekti);
                if (valittuObjekti != null && valittuObjekti != "") {
                    EditoriRuutu.valitunMaastonKuva = valittuObjekti;
                    String[] ominaisuudet = {"kuva=" + valittuObjekti, "leveys=" + valittuKuva.annaLeveys(), "korkeus=" + valittuKuva.annaKorkeus()};
                    List<String> ominaisuusLista1 = List.of(ominaisuudet);
                    ArrayList<String> ominaisuusLista = new ArrayList<>(ominaisuusLista1);
                    PeliObjekti valittuPeliObjekti;
                    if (valittuKuva.annaLeveys() == 1 && valittuKuva.annaKorkeus() == 1) {
                        EditoriRuutu.valitunEsineenNimi = "Tile";
                    }
                    else {
                        EditoriRuutu.valitunEsineenNimi = "Laatta";
                    }
                    valittuPeliObjekti = Maasto.luoMaastoTiedoilla(EditoriRuutu.valitunEsineenNimi, 0, 0, ominaisuusLista);
                    EditoriRuutu.valittuEsine = valittuPeliObjekti;
                    EditoriRuutu.kopioidunEsineenOminaisuudet = valittuPeliObjekti.annaLisäOminaisuudet();
                    EditoriRuutu.kopioitu = false;
                    Yläpalkki.asetaValittuObjekti(valittuPeliObjekti);
                    EditoriRuutu.avaaObjektiValikko(false);
                }
            }
            case ENTITYT -> {
                String valittuObjekti = valitseObjekti(hiiriX, hiiriY);
                if (valittuObjekti != null && valittuObjekti != "") {
                    EditoriRuutu.valitunEsineenNimi = valittuObjekti;
                    PeliObjekti valittuPeliObjekti = Entity.luoEntityTiedoilla(valittuObjekti, hiiriX, hiiriY, null);
                    EditoriRuutu.valittuEsine = valittuPeliObjekti;
                    EditoriRuutu.kopioidunEsineenOminaisuudet = valittuPeliObjekti.annaLisäOminaisuudet();
                    EditoriRuutu.kopioitu = false;
                    Yläpalkki.asetaValittuObjekti(valittuPeliObjekti);
                    EditoriRuutu.avaaObjektiValikko(false);
                }
            }
        }
        EditoriRuutu.estäVahinkoPainallukset = true;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        // Pohja
        Matrix4f objektiValikonSijainti = new Matrix4f();
        objektiValikonSijainti.scale(scaleX, scaleYPohja, 1);
        shader.bind();
		shader.asetaSijainti(objektiValikonSijainti);
        shader.setUniform("subcolor", new Vector4f(1, 1, 1, 0.25f));
        objektiValikkoPohjaTekstuuri.bind(0);
        Assets.getModel().render();

        // Välilehdet
        välilehtiObjektitLabel.renderöi(shader, window);
        välilehtiMaastoLabel.renderöi(shader, window);
        välilehtiEntitytLabel.renderöi(shader, window);
        välilehtiObjektitNappi.renderöi(shader, window);
        välilehtiMaastoNappi.renderöi(shader, window);
        välilehtiEntitytNappi.renderöi(shader, window);

        // Valikon sisältö
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        switch (välilehti) {
            case OBJEKTIT -> {
                for (Nappi nappi : esineValikko.values()) {
                    nappi.renderöi(shader, window);
                }
                välilehtiObjektitTeksti.päivitäTeksti("Objektit °", 0, 350, Väri.orange);
                välilehtiObjektitNappi.päivitäSisältö(välilehtiObjektitTeksti);
                välilehtiMaastoTeksti.päivitäTeksti("Maasto", 0, 350, Väri.white);
                välilehtiMaastoNappi.päivitäSisältö(välilehtiMaastoTeksti);
                välilehtiEntitytTeksti.päivitäTeksti("Entityt", 0, 350, Väri.white);
                välilehtiEntitytNappi.päivitäSisältö(välilehtiEntitytTeksti);
            }
            case MAASTO -> {
                for (Nappi nappi : maastoValikko.values()) {
                    nappi.renderöi(shader, window);
                }
                välilehtiObjektitTeksti.päivitäTeksti("Objektit", 0, 350, Väri.white);
                välilehtiObjektitNappi.päivitäSisältö(välilehtiObjektitTeksti);
                välilehtiMaastoTeksti.päivitäTeksti("Maasto °", 0, 350, Väri.orange);
                välilehtiMaastoNappi.päivitäSisältö(välilehtiMaastoTeksti);
                välilehtiEntitytTeksti.päivitäTeksti("Entityt", 0, 350, Väri.white);
                välilehtiEntitytNappi.päivitäSisältö(välilehtiEntitytTeksti);
            }
            case ENTITYT -> {
                for (Nappi nappi : entityValikko.values()) {
                    nappi.renderöi(shader, window);
                }
                välilehtiObjektitTeksti.päivitäTeksti("Objektit", 0, 350, Väri.white);
                välilehtiObjektitNappi.päivitäSisältö(välilehtiObjektitTeksti);
                välilehtiMaastoTeksti.päivitäTeksti("Maasto", 0, 350, Väri.white);
                välilehtiMaastoNappi.päivitäSisältö(välilehtiMaastoTeksti);
                välilehtiEntitytTeksti.päivitäTeksti("Entityt °", 0, 350, Väri.orange);
                välilehtiEntitytNappi.päivitäSisältö(välilehtiEntitytTeksti);
            }
        }
    }

    private static String valitseObjekti(int hiiriX, int hiiriY) {
        String objektinNimi = "";
        switch (välilehti) {
            case OBJEKTIT -> {
                for (int i = 0; i < esineValikko.size(); i++) {
                    if (esineValikko.get(i).hiiriSisällä(hiiriX, hiiriY)) {
                        objektinNimi = esineValikko.get(i).annaNimi();
                    }
                }
            }
            case MAASTO -> {
                for (int i = 0; i < maastoValikko.size(); i++) {
                    if (maastoValikko.get(i).hiiriSisällä(hiiriX, hiiriY)) {
                        objektinNimi = maastoValikko.get(i).annaNimi();
                    }
                }
            }
            case ENTITYT -> {
                for (int i = 0; i < entityValikko.size(); i++) {
                    if (entityValikko.get(i).hiiriSisällä(hiiriX, hiiriY)) {
                        objektinNimi = entityValikko.get(i).annaNimi();
                    }
                }
            }
        }
        return objektinNimi;
    }

    private static List<MaastoKuva> listaaMaastoKuvat(String dir) {
        List<MaastoKuva> kuvaLista = new ArrayList<>();
        File[] tiedostot = new File(dir).listFiles();
        for (File file : tiedostot) {
            if (!file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif"))) {
                try {
                    BufferedImage kuva = ImageIO.read(file);
                    if (kuva.getWidth() % 64 == 0 && kuva.getHeight() % 64 == 0) {
                        String nimi = file.getName();
                        int leveys = kuva.getWidth() / 64;
                        int korkeus = kuva.getHeight() / 64;
                        kuvaLista.add(new MaastoKuva(nimi, leveys, korkeus));
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }   
        return kuvaLista;
    }

    private static class MaastoKuva {
        private String nimi;
        private int leveys;
        private int korkeus;

        public MaastoKuva(String nimi, int leveys, int korkeus) {
            this.nimi = nimi;
            this.leveys = leveys;
            this.korkeus = korkeus;
        }

        public String annaNimi() {
            return nimi;
        }
        public int annaLeveys() {
            return leveys;
        }
        public int annaKorkeus() {
            return korkeus;
        }
    }
}
