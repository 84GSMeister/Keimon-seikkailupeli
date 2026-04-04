package keimo.seikkailupeli.objektit.maastot;

import keimo.seikkailupeli.objektit.PeliObjekti;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Maasto extends PeliObjekti {
    
    protected String tekstuurinNimi;
    protected boolean estääLiikkumisen = false;
    protected boolean estääLiikkumisenVasen = false;
    protected boolean estääLiikkumisenOikea = false;
    protected boolean estääLiikkumisenAlas = false;
    protected boolean estääLiikkumisenYlös = false;

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            super.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kuva="));
            this.lisäOminaisuudet.add("kuva="+ tiedostonNimi);
            
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
            if (kääntöAsteet != 0) this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
            if (xPeilaus) this.lisäOminaisuudet.add("x-peilaus=" + (xPeilaus ? "kyllä" : "ei"));
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
            if (yPeilaus)this.lisäOminaisuudet.add("y-peilaus=" + (yPeilaus ? "kyllä" : "ei"));
        }
    }

    public boolean estääköLiikkumisen(Suunta suunta) {
        if (this.estääLiikkumisen) {
            return true;
        }
        else {
            switch (suunta) {
                case VASEN: return estääLiikkumisenVasen;
                case OIKEA: return estääLiikkumisenOikea;
                case ALAS: return estääLiikkumisenAlas;
                case YLÖS: return estääLiikkumisenYlös;
                case null, default: return false;
            }
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }

    public String annaTekstuurinNimi() {
        return tekstuurinNimi;
    }

    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    public static Maasto luoMaastoTiedoilla(String maastonNimi, int sijX, int sijY, ArrayList<String> ominaisuusLista) {

        Maasto luotavaMaasto;

        switch (maastonNimi) {

            case "Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            case "IsoLaatta":
                luotavaMaasto = new IsoLaatta(sijX, sijY, ominaisuusLista);
                break;

            case "Laatta":
                luotavaMaasto = new Laatta(sijX, sijY, ominaisuusLista);
                break;

            case "EsteTile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;
            
            case "Yksisuuntainen Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaMaasto = null;
                break;
        }

        return luotavaMaasto;
    }

    public static Maasto luoRandomMaasto(int sijX, int sijY) {
        Random r = new Random();
        Object[] lista = listaaKuvat("tiedostot/kuvat/maasto").toArray();
        Object kuvaTiedosto = lista[r.nextInt(lista.length)];
        String[] ominaisuusListaArray = {"kuva=" + kuvaTiedosto,"kääntö=0","x-peilaus=ei","y-peilaus=ei"};
        ArrayList<String> ominaisuusLista = new ArrayList<>();
        for (String s : ominaisuusListaArray) {
            ominaisuusLista.add(s);
        }
        return luoMaastoTiedoilla("Tile", sijX, sijY, ominaisuusLista);
    }

    /**
     * Lataa kuvatiedostot ja luo niistä lista.
     * Käytä tätä funktiota maastolle ja koriste-esineille.
     * @param dir tiedostopolku
     * @return kuvalista merkkijonona
     */
    public static List<String> listaaKuvat(String dir) {
        List<String> kuvaLista = Stream.of(new File(dir).listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
            .map(File::getName).sorted()
            .collect(Collectors.toList());
        Collections.sort(kuvaLista, new Comparator<String>() {
            public int compare(String e1, String e2) {
                return e1.compareTo(e2);
            }
        });
        return kuvaLista;
    }

    public static List<MaastoKuva> listaaMaastoKuvat(String dir) {
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

    public static class MaastoKuva {
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

    String tiedot = "";
    void asetaTiedot() {
        tiedot = "";
        tiedot += "Nimi: " + this.annaNimi() + "\n";

        List<Suunta> esteSuunnat = new ArrayList<>();
        if (this.estääköLiikkumisen(Suunta.VASEN)) esteSuunnat.add(Suunta.VASEN);
        if (this.estääköLiikkumisen(Suunta.OIKEA)) esteSuunnat.add(Suunta.OIKEA);
        if (this.estääköLiikkumisen(Suunta.YLÖS)) esteSuunnat.add(Suunta.YLÖS);
        if (this.estääköLiikkumisen(Suunta.ALAS)) esteSuunnat.add(Suunta.ALAS);
        tiedot += "Estää liikkumisen: " + esteSuunnat.toString() + "\n";

        tiedot += "Kuva: " + this.tiedostonNimi;
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    protected void luoSkaalattuKuvake() {
         
    }
}
