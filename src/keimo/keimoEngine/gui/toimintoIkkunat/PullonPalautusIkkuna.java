package keimo.keimoEngine.gui.toimintoIkkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Peli.SyötteenTila;
import keimo.Peli.ToimintoIkkunanTyyppi;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.kiintopiste.Pulloautomaatti;
import keimo.kenttäkohteet.kiintopiste.Pulloautomaatti.PulloautomaatinKuvake;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.JOptionPane;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class PullonPalautusIkkuna {
    
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/toimintoikkuna_kehys_valikko.png");
    private static Teksti pullonPalautusTeksti = new Teksti("Palautetaan", Color.green, 300, 200);

    private static Random r = new Random();
    private static String statusTeksti = "Palautetaan";
    private static int pullonPalautuksenPituus;
    public static boolean jatkoSyöteAnnettu = false;
    private static Thread simulointiSäie;
    private static boolean valmis = false;
    public static Pulloautomaatti pulloautomaatti;
    public static VirheenTyyppi virheenTyyppi = VirheenTyyppi.PAKKAUS;
    private static float siirräY = 600;
    
    public static void renderöiIkkuna(Window window) {
        float scaleX = window.getWidth()/4;
        float scaleY = window.getHeight()/4;
        float scaleXSisempi = scaleX *(2f/3f);
        float scaleYSisempi = scaleY *(2f/3f);
        if (siirräY > 0) siirräY -= 20;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.scale(scaleX, scaleY, 0);
        matKehys.translate(0, siirräY/100f, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matTeksti = new Matrix4f();
        window.getView().scale(1, matTeksti);
        matTeksti.scale(scaleXSisempi, scaleYSisempi, 0);
        matTeksti.translate(0, siirräY/100f, 0);
        peliShader.setUniform("projection", matTeksti);
        pullonPalautusTeksti.päivitäTeksti(statusTeksti);
        pullonPalautusTeksti.bind(0);
        Assets.getModel().render();
    }

    public static void tarkistaTila() {
        if (valmis) {
            valmis();
        }
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.PULLONPALAUTUS;
        if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Pulloautomaatti) {
            pulloautomaatti = (Pulloautomaatti)Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (Pelaaja.kuparit > 0) {
                pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);
                simulointiSäie = new Thread() {
                    public void run() {
                        simuloiPullonpalautus();
                    }
                };
                simulointiSäie.start();
            }
            else {
                suljeToimintoIkkuna();
                Dialogit.avaaDialogi(pulloautomaatti.annaDialogiTekstuuri(), "Sinulla ei ole yhtään tölkkiä.", "Pulloautomaatti");
            }
        }
        else suljeToimintoIkkuna();
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        siirräY = 600;
    }

    public enum VirheenTyyppi {
        PAKKAUS,
        MUOTO,
        KÄSI,
        MERKKI;
    }
    
    private static void simuloiPullonpalautus() {
        valmis = false;
        pullonPalautuksenPituus = Pelaaja.kuparit;
        String[] pullonPalautusJono = new String[pullonPalautuksenPituus];
        int toimivatMjonot = 0;
        try {
            for (int i = 0; i < pullonPalautuksenPituus; i++) {
                Peli.pauseDialogi = true;
                int virhe = r.nextInt(0, 10);
                jatkoSyöteAnnettu = false;
                statusTeksti = "Tölkkejä jäljellä :" + (pullonPalautuksenPituus - i);
                switch (virhe) {
                    case 0:
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                        virheenTyyppi = VirheenTyyppi.PAKKAUS;
                        statusTeksti = ("<html><p>Poista pakkaus ja yritä uudelleen tai hävitä se muuten.<br>(Paina Space)</p></html>");
                        while (!jatkoSyöteAnnettu) {Thread.sleep(10);}
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);
                    break;
                    case 1:
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                        virheenTyyppi = VirheenTyyppi.MUOTO;
                        statusTeksti =("<html><p>Palauta pakkaus alkuperäiseen muotoon ja yritä uudelleen.<br>(Paina X)</p></html>");
                        while (!jatkoSyöteAnnettu) {Thread.sleep(10);}
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);
                    break;
                    case 2:
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                        virheenTyyppi = VirheenTyyppi.KÄSI;
                        statusTeksti =("<html><p>Älä työnnä kättä automaattiin!<br>(Paina C)</p></html>");
                        while (!jatkoSyöteAnnettu) {Thread.sleep(10);}
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);
                    break;
                    case 3:
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                        virheenTyyppi = VirheenTyyppi.MERKKI;
                        statusTeksti =("<html><p>Kauppa ei hyväksy tätä merkkiä!<br>(Paina Z)</p></html>");
                        while (!jatkoSyöteAnnettu) {Thread.sleep(10);}
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);
                    break;
                    default:
                    break;
                }
                Thread.sleep(400);
                if (pullonPalautusJono[i] == null) {
                    continue;
                }
                else {
                    pullonPalautusJono[toimivatMjonot] = "";
                    toimivatMjonot++;
                }
            }
            valmis = true;
        }
        catch (InterruptedException ie) {
            System.out.println("Kuka kehtaa keskeyttää nukkumiseni!? T. Vihainen Pelisäie");
            ie.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virheellinen lista.\n\nHäire sovelluksessa.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException e) {
            System.out.println("Ohitetaan tyhjät indeksit");
        }
    }

    private static void valmis() {
        try {
            valmis = false;
            pulloautomaatti.valitseTila(PulloautomaatinKuvake.IDLE);
            Peli.pauseDialogi = false;
            Peli.valintaDialogi = false;
            suljeToimintoIkkuna();
            float saatavaRaha = 0.15f * Pelaaja.kuparit;
            if (saatavaRaha > 0) {
                Pelaaja.raha += saatavaRaha;
                Pelaaja.kuparit = 0;
                DecimalFormat df = new DecimalFormat("##.##");
                String saatavaRahaFormatoitu = df.format(saatavaRaha);
                Dialogit.avaaDialogi(pulloautomaatti.annaDialogiTekstuuri(), "Palautit tölkit kauppaan ja sait " + saatavaRahaFormatoitu + "€.", "Pulloautomaatti");
            }
            else {
                Dialogit.avaaDialogi(pulloautomaatti.annaDialogiTekstuuri(), "Sinulla ei ole yhtään tölkkiä.", "Pulloautomaatti");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
