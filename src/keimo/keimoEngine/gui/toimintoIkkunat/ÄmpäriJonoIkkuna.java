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
import keimo.kenttäkohteet.esine.Vesiämpäri;
import keimo.kenttäkohteet.kiintopiste.Ämpärikone;

import java.awt.Color;
import java.util.Random;
import javax.swing.JOptionPane;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ÄmpäriJonoIkkuna {

    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/toimintoikkunat/toimintoikkuna_kehys_valikko.png");
    private static Teksti ämpäriJonoTeksti = new Teksti("Palautetaan", Color.green, 300, 200);
    private static Teksti ohjeTeksti = new Teksti("Space: Poistu", Color.green, 300, 200);
    private static Tekstuuri surunaamaTekstuuri = new Tekstuuri("tiedostot/kuvat/surunaama.png");
    private static Tekstuuri keimoTekstuuri = new Tekstuuri("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");

    private static Random r = new Random();
    private static String statusTeksti = "Jonotetaan";
    private static int ämpäriJononPituus;
    public static boolean keskeytetty = false;
    private static Thread simulointiSäie;
    private static boolean valmis = false;
    public static Ämpärikone ämpärikone;
    private static float siirräY = 600;
    
    public static void renderöiIkkuna(Window window) {
        float scaleX = window.getWidth()/4;
        float scaleY = window.getHeight()/4;
        float scaleXSisempi = scaleX *(2f/3f);
        float scaleYSisempi = scaleY *(2f/3f);
        float offsetY = window.getHeight()/10;
        if (siirräY > 0) siirräY -= 20;
        //if (offsetY <= 0 && scaleX < window.getWidth()/4) scaleX += window.getWidth()/100f;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, siirräY, 0);
        matKehys.scale(scaleX, scaleY - siirräY, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matStatusTeksti = new Matrix4f();
        window.getView().scale(1, matStatusTeksti);
        matStatusTeksti.translate(0, siirräY, 0);
        matStatusTeksti.scale(scaleXSisempi, scaleYSisempi - siirräY, 0);
        peliShader.setUniform("projection", matStatusTeksti);
        ämpäriJonoTeksti.päivitäTeksti(statusTeksti);
        ämpäriJonoTeksti.bind(0);
        Assets.getModel().render();

        Matrix4f matOhjeTeksti = new Matrix4f();
        window.getView().scale(1, matOhjeTeksti);
        matOhjeTeksti.translate(0, -offsetY + siirräY, 0);
        matOhjeTeksti.scale(scaleXSisempi, scaleYSisempi - siirräY, 0);
        peliShader.setUniform("projection", matOhjeTeksti);
        ohjeTeksti.bind(0);
        Assets.getModel().render();
    }

    public static void tarkistaTila() {
        if (valmis) {
            valmis();
        }
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.ÄMPÄRIJONO;
        if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Ämpärikone) {
            ämpärikone = (Ämpärikone)Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            simulointiSäie = new Thread() {
                public void run() {
                    simuloiÄmpärijono();
                }
            };
            simulointiSäie.start();
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
    
    private static void simuloiÄmpärijono() {
        valmis = false;
        ämpäriJononPituus = r.nextInt(1000, 4000);
        keskeytetty = false;
        try {
            for (int i = 0; i < ämpäriJononPituus; i++) {
                if (!keskeytetty) {
                    Peli.pauseDialogi = true;
                    statusTeksti = "Jonossa sijalla: " + (ämpäriJononPituus - i);
                    Thread.sleep(3);
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
            Peli.pauseDialogi = false;
            Peli.valintaDialogi = false;
            suljeToimintoIkkuna();
            if (keskeytetty) {
                Dialogit.avaaDialogi(keimoTekstuuri, "Ei kiinosta  virhe", "Keimo");
            }
            else if (Pelaaja.annaEsineidenMäärä() < 6) {
                Vesiämpäri ämpäri = new Vesiämpäri(true, 0, 0);
                Pelaaja.annaEsine(ämpäri);
                Dialogit.avaaDialogi(ämpäri.annaDialogiTekstuuri(), "Sait uuden " + ämpäri.annaNimiSijamuodossa("genetiivi"), "Ämpärijono");
            }
            else {
                Dialogit.avaaDialogi(surunaamaTekstuuri, "Menit jonottamaan ämpäriä, vaikka sinun tavaraluettelosi on täynnä. Miltä nyt tuntuu?", "Ämpärijono");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
