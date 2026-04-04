package keimo.seikkailupeli.äänet;

import keimo.keimoengine.collision.Piste;
import keimo.keimoengine.äänet.PeliääniToistin;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.io.File;
import java.lang.Thread.State;
import java.util.Random;

public class Äänet {
    private static double defaultVolume = 1;
    private static double defaultPan = 0;
    private static Random random = new Random();

    private static Object äänisäikeenLukko = new Object();
    private static Thread äänisäie;
    private static String äänisäieÄäni = "";
    private static double äänisäieVolyymi = 0;
    private static double äänisäiePan = 0;
    private static float äänisäieSampleRate = 44100;
    private static boolean äänisäieLoop = false;
    private static boolean äänisäieKäytäTiedostoa = false;
    private static File äänisäieTiedosto;
    private static boolean ääniSäieTakaperin = false;

    public static void toistaSFX(String ääni) {
        toistaSFX(ääni, defaultVolume, defaultPan);
    }

    public static void toistaSFX(String ääni, boolean muuttuvaTaajuus) {
        toistaSFX(ääni, defaultVolume, defaultPan, muuttuvaTaajuus);
    }

    public static void toistaSFX(String ääni, boolean muuttuvaTaajuus, float minimiMuutosPuoliaskel, float maksimiMuutosPuoliaskel) {
        toistaSFX(ääni, defaultVolume, defaultPan, muuttuvaTaajuus, minimiMuutosPuoliaskel, maksimiMuutosPuoliaskel, false);
    }

    public static void toistaSFX(String ääni, boolean muuttuvaTaajuus, float minimiMuutosPuoliaskel, float maksimiMuutosPuoliaskel, boolean takaperin) {
        toistaSFX(ääni, defaultVolume, defaultPan, muuttuvaTaajuus, minimiMuutosPuoliaskel, maksimiMuutosPuoliaskel, takaperin);
    }

    public static void toistaSFX(String ääni, Piste sijaintiKentällä) {
        double xEtäisyys = Pelaaja.hitbox.getCenterX() - sijaintiKentällä.annaX();
        double yEtäisyys = Pelaaja.hitbox.getCenterY() - sijaintiKentällä.annaY();
        double pan = -xEtäisyys/512;
        if (pan < -1) pan = -1;
        else if (pan > 1) pan = 1;
        double etäisyysKerroinV = Math.min(xEtäisyys, yEtäisyys);
        double volume = ((100 * etäisyysKerroinV) / 1024 + 100)/100;
        if (volume > 0.75) volume = 0.75;
        else if (volume < 0) volume = 0;
        toistaSFX(ääni, volume, pan);
    }

    public static void toistaSFX(String ääni, double volume, double pan) {
        toistaSFX(ääni, volume, pan, false);
    }

    public static void toistaSFX(String ääni, double volume, double pan, boolean muuttuvaTaajuus) {
        toistaSFX(ääni, volume, pan, muuttuvaTaajuus, -1, 1);
    }

    public static void toistaSFX(String ääni, double volume, double pan, boolean muuttuvaTaajuus, float minimiMuutosPuoliaskel, float maksimiMuutosPuoliaskel) {
        toistaSFX(ääni, volume, pan, muuttuvaTaajuus, minimiMuutosPuoliaskel, maksimiMuutosPuoliaskel, false);
    }
    
    /**
     * Toista valittu ääni
     * @param ääni äänitiedoston nimi
     * @param volume voimakkuus (0 - 1)
     * @param pan stereo-panorointi: -1 = Täysin vasemmalla; 0 = Keskellä; 1 = Täysin oikealla
     * @param muuttuvaTaajuus Aseta äänelle taajuusvaihtelua
     * @param minimiMuutosPuoliaskel Montako puoliaskelta matalammalta ääni voidaan toistaa (Oletus = -1)
     * @param maksimiMuutosPuoliaskel Montako puoliaskelta korkeammalta ääni voidaan toistaa (Oletus = 1)
     * @param takaperin Toista takaperin
     */
    public static void toistaSFX(String ääni, double volume, double pan, boolean muuttuvaTaajuus, float minimiMuutosPuoliaskel, float maksimiMuutosPuoliaskel, boolean takaperin) {
        try {
            double sfxVolyymi = volume * PelinAsetukset.ääniVolyymi;
            if (muuttuvaTaajuus) {
                float minTaajuus = (float)(44100 * Math.pow(2d, ((minimiMuutosPuoliaskel/12d))));
                float maxTaajuus = (float)(44100 * Math.pow(2d, ((maksimiMuutosPuoliaskel/12d))));
                float sampleRate = random.nextFloat(minTaajuus, maxTaajuus);
                toistaÄäni(ääni, sfxVolyymi, pan, sampleRate, false, takaperin);
            }
            else toistaÄäni(ääni, sfxVolyymi, pan, 44100, false, takaperin);
        }
        catch (Exception e) {
            System.out.println("Äänitiedostoa \"" + ääni + "\" ei löytynyt");
            e.printStackTrace();
        }
    }

    public static void toistaSFXMuunnetullaTaajuudella(String ääni, float muutosPuoliaskel) {
        toistaSFXMuunnetullaTaajuudella(ääni, defaultVolume, defaultPan, muutosPuoliaskel);
    }

    public static void toistaSFXMuunnetullaTaajuudella(String ääni, double volume, double pan, float muutosPuoliaskel) {
        try {
            double sfxVolyymi = volume * PelinAsetukset.ääniVolyymi;
            float sampleRate = (float)(44100 * Math.pow(2d, ((muutosPuoliaskel/12d))));
            toistaÄäni(ääni, sfxVolyymi, pan, sampleRate, false, false);
        }
        catch (Exception e) {
            System.out.println("Äänitiedostoa \"" + ääni + "\" ei löytynyt");
            e.printStackTrace();
        }
    }

    public static Thread luoÄänisäie() {
        äänisäie = new Thread() {
            @Override
            public void run() {
                synchronized(äänisäikeenLukko) {
                    try {
                        if (PelinAsetukset.äänetPäällä) {
                            double toistoVolyymi = äänisäieVolyymi;
                            if (äänisäieKäytäTiedostoa) {
                                PeliääniToistin.toistaResamplattavaÄäni(äänisäieSampleRate, äänisäieTiedosto, toistoVolyymi, äänisäiePan, äänisäieLoop, ääniSäieTakaperin);
                            }
                            else {
                                File ääniTiedosto = Assets.annaÄäni(äänisäieÄäni);
                                PeliääniToistin.toistaResamplattavaÄäni(äänisäieSampleRate, ääniTiedosto, toistoVolyymi, äänisäiePan, äänisäieLoop, ääniSäieTakaperin);
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Ääntä ei voitu toistaa");
                        e.printStackTrace();
                    }
                }
            }
        };
        äänisäie.setName("Äänten ajoittajasäie");
        return äänisäie;
    }

    private static void asetaArvotSäikeelle(String ääni, double volume, double pan, float sampleRate, boolean loop, boolean käytäTiedostoa, File tiedosto, boolean takaperin) {
        äänisäieÄäni = ääni;
        äänisäieVolyymi = volume;
        äänisäiePan = pan;
        äänisäieSampleRate = sampleRate;
        äänisäieLoop = loop;
        äänisäieKäytäTiedostoa = käytäTiedostoa;
        äänisäieTiedosto = tiedosto;
        ääniSäieTakaperin = takaperin;
    }

    public static void toistaÄäni(File ääniTiedosto, double volume, double pan, boolean muutaTaajuutta, float sampleRate, boolean loop, boolean takaperin) {
        try {
            if (äänisäie == null || äänisäie.getState() == State.TERMINATED) {
                äänisäie = luoÄänisäie();
            }
            if (äänisäie != null) {
                if (äänisäie.getState() != State.TERMINATED && äänisäie.getState() != State.RUNNABLE) {
                    asetaArvotSäikeelle("", volume, pan, sampleRate, loop, true, ääniTiedosto, takaperin);
                    äänisäie.start();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Thread state: " + äänisäie.getState());
            e.printStackTrace();
        }
    }

    public static void toistaÄäni(String ääni, double volume, double pan, float sampleRate, boolean loop, boolean takaperin) {
        try {
            if (äänisäie == null || äänisäie.getState() == State.TERMINATED) {
                äänisäie = luoÄänisäie();
            }
            if (äänisäie != null) {
                // while (äänisäie.getState() == State.RUNNABLE) {
                //     Thread.sleep(50);
                // }
                äänisäie = luoÄänisäie();
                asetaArvotSäikeelle(ääni, volume, pan, sampleRate, loop, false, null, takaperin);
                äänisäie.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("state: " + äänisäie.getState());
        }
    }

    public static void suljeÄänet() {
        new Thread() {
            @Override
            public void run() {
                PeliääniToistin.suljeÄänet();
            }
        }.start();
    }
}
