package keimo.seikkailupeli.kenttä;

import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.objekti2d.Model;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class KenttäShaderEfektit {

    static Random random = new Random();
    static Tekstuuri mustaTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/tiili.png");
    
    static float punainen = 0f, vihreä = 0.5f, sininen = 1f;
    static boolean lisääPun = true, lisääVihr = true, lisääSin = false;
    static String lisättäväVäri = "punainen";
    static float lisäysAskelVäriBaari = 0.01f;
    static float lisäysAskelVäriBaariSala = 0.2f;
    static float lisäysAskelVäriKuu = 0.04f;
    protected static void renderöiKenttäVäriEfekti(Shader shader) {
        shader.bind();
        if (Peli.huone != null) {
            if (Peli.huone.annaNimi().equals("Metsä") || Peli.huone.annaNimi().equals("Metsä_kalja")) {
                shader.setUniform("himmennys", new Vector4f(0.5f, 0.5f, 0.5f, 0f));
            }
            else if (Peli.huone.annaNimi().equals("Metsä")) {
                shader.setUniform("himmennys", new Vector4f(0.7f, 0.7f, 0.7f, 0f));
            }
            // else if (Peli.huone.annaNimi().equals("Keimo-baari")) {
            //     shader.setUniform("himmennys", new Vector4f(punainen, vihreä, sininen, 0f));
            // }
            else if (Peli.huone.annaNimi().equals("Baari_salahuone")) {
                shader.setUniform("himmennys", new Vector4f(punainen, vihreä, sininen, 0f));
            }
            // else if (Peli.huone.annaNimi().equals("Kuu")) {
            //     shader.setUniform("himmennys", new Vector4f(punainen, vihreä, sininen, 0f));
            // }
            else shader.setUniform("himmennys", new Vector4f(0f, 0f, 0f, 0f));
        }
    }

    protected static void luoKenttäVäriEfekti() {
        // if (Peli.huone.annaNimi().equals("Keimo-baari")) {
        //     if (lisääPun) punainen += lisäysAskelVäriBaari;
        //     else punainen -= lisäysAskelVäriBaari;
        //     if (lisääVihr) vihreä += lisäysAskelVäriBaari;
        //     else vihreä -= lisäysAskelVäriBaari;
        //     if (lisääSin) sininen += lisäysAskelVäriBaari;
        //     else sininen -= lisäysAskelVäriBaari;
            
        //     if (punainen >= 1f) lisääPun = false;
        //     else if (punainen <= 0f) lisääPun = true;
        //     if (vihreä >= 1f) lisääVihr = false;
        //     else if (vihreä <= 0f) lisääVihr = true;
        //     if (sininen >= 1f) lisääSin = false;
        //     else if (sininen <= 0f) lisääSin = true;
        // }
        if (Peli.huone.annaNimi().equals("Baari_salahuone")) {
            if (lisättäväVäri.equals("punainen")) {
                if (lisääPun) punainen += lisäysAskelVäriBaariSala;
                else punainen -= lisäysAskelVäriBaariSala;
                if (punainen >= 1f) {
                    lisääPun = false;
                    lisättäväVäri = "vihreä";
                }
                else if (punainen <= 0f) {
                    lisääPun = true;
                }
            }
            else if (lisättäväVäri.equals("vihreä")) {
                if (lisääVihr) vihreä += lisäysAskelVäriBaariSala;
                else vihreä -= lisäysAskelVäriBaariSala;
                if (vihreä >= 1f) {
                    lisääVihr = false;
                    lisättäväVäri = "sininen";
                }
                else if (vihreä <= 0f) {
                    lisääVihr = true;
                }
            }
            else if (lisättäväVäri.equals("sininen")) {
                if (lisääSin) sininen += lisäysAskelVäriBaariSala;
                else sininen -= lisäysAskelVäriBaariSala;
                if (sininen >= 1f) {
                    lisääSin = false;
                    lisättäväVäri = "punainen";
                }
                else if (sininen <= 0f) {
                    lisääSin = true;
                }
            }
        }
        // else if (Peli.huone.annaNimi().equals("Kuu")) {
        //     if (lisääPun) punainen += lisäysAskelVäriKuu*0.731;
        //     else punainen -= lisäysAskelVäriKuu*0.641;
        //     if (lisääVihr) vihreä += lisäysAskelVäriKuu*0.985;
        //     else vihreä -= lisäysAskelVäriKuu*0.652;
        //     if (lisääSin) sininen += lisäysAskelVäriKuu*0.421;
        //     else sininen -= lisäysAskelVäriKuu*0.794;
            
        //     if (punainen >= 1f) lisääPun = false;
        //     else if (punainen <= 0f) lisääPun = true;
        //     if (vihreä >= 1f) lisääVihr = false;
        //     else if (vihreä <= 0f) lisääVihr = true;
        //     if (sininen >= 1f) lisääSin = false;
        //     else if (sininen <= 0f) lisääSin = true;
        // }
    }

    static float xHeilunnanNopeus = 0f;
    static float xHeilunnanPituus = 0f;
    static float yHeilunnanNopeus = 0f;
    static float yHeilunnanPituus = 0f;
    static float zHeilunnanNopeus = 0f;
    static float zHeilunnanPituus = 0f;
    public static float känniScaleX = 0;
    protected static Matrix4f känniEfekti(Matrix4f projection) {
        if (Pelaaja.känninVoimakkuusFloat > 0f) {
            float känniHajontaX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniHajontaY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniHajontaZ = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            xHeilunnanNopeus += Pelaaja.känninVoimakkuusFloat * 0.00574f;
            xHeilunnanPituus = Pelaaja.känninVoimakkuusFloat * 0.186f;
            yHeilunnanNopeus += Pelaaja.känninVoimakkuusFloat * 0.00361f;
            yHeilunnanPituus = Pelaaja.känninVoimakkuusFloat * 0.227f;
            zHeilunnanNopeus += Pelaaja.känninVoimakkuusFloat * 0.00231f;
            zHeilunnanPituus = Pelaaja.känninVoimakkuusFloat * 0.137f;
            projection.translate(xHeilunnanPituus * (float)Math.sin(xHeilunnanNopeus) + känniHajontaX/50f,
                                yHeilunnanPituus * (float)Math.sin(yHeilunnanNopeus) + känniHajontaY/50f,
                                zHeilunnanPituus * (float)Math.sin(zHeilunnanNopeus) + känniHajontaZ/50f);
            return projection;
        }
        else return projection;
    }

    static float rotZ = 0f;
    static float rotaationNopeus = 0f;
    protected static Matrix4f känniEfektiRotaatio(Matrix4f projection) {
        if (Pelaaja.känninVoimakkuusFloat > 6f) {
            rotaationNopeus = (Pelaaja.känninVoimakkuusFloat * Pelaaja.känninVoimakkuusFloat)/20f - (Pelaaja.känninVoimakkuusFloat * 6f)/20f;
            projection.rotate((float)Math.toRadians(rotZ), new Vector3f(0, 0, 1));
            rotZ += rotaationNopeus;
            return projection;
        }
        else return projection;
    }

    static float lisäysVäri = 0f;
    static boolean lisääVäri = true;
    static float lisäysAskelKimmellys = 0.01f;
    protected static void kimmellysEfekti(Shader shader) {
        shader.bind();
        if (lisääVäri) lisäysVäri += lisäysAskelKimmellys;
        else lisäysVäri -= lisäysAskelKimmellys;
        
        if (lisäysVäri >= 0.25f) lisääVäri = false;
        else if (lisäysVäri <= 0f) lisääVäri = true;
        
        shader.setUniform("addcolor", new Vector4f(lisäysVäri, lisäysVäri, lisäysVäri, 0));
    }

    protected static void renderöiErikoisEfektit(Shader shader, int x, int y, int z, Matrix4f cameraMatrix) {
        if (Peli.huone != null) {
            if (Peli.huone.annaNimi().equals("Baari_salahuone")) {
                renderöiMustatPalkitEfekti(shader, x, y, z, cameraMatrix);
            }
        }
    }

    static boolean mustatPalkitParillinen = true;
    static boolean mustatPakitParillinenFadeKasvata = true;
    static boolean mustatPakitParitonFadeKasvata = true;
    static float mustatPalkitParillinenFade = 0f;
    static float mustatPalkitParitonFade = 1f;
    static float mustatPalkitFadeAskel = 0.01f;
    protected static void renderöiMustatPalkitEfekti(Shader shader, int x, int y, int z, Matrix4f cameraMatrix) {
        shader.bind();
        Matrix4f tilenSijainti = new Matrix4f();
        if (y % 2 == 0) {
            tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
            shader.setUniform("subcolor", new Vector4f(1, 1, 1, (float)Math.pow(mustatPalkitParillinenFade, 2)));
        }
        if (y % 2 == -1) {
            tilenSijainti = new Matrix4f().translate(new Vector3f(x * 2, y * 2, z));
            shader.setUniform("subcolor", new Vector4f(1, 1, 1, (float)Math.pow(mustatPalkitParitonFade, 2)));
        }
        Matrix4f resultMatrix = new Matrix4f(cameraMatrix);
        resultMatrix.mul(tilenSijainti);
        
        shader.bind();
        shader.asetaSampler(0);
        shader.asetaSijainti(resultMatrix);
        
        mustaTekstuuri.bind(0);
        Model model = Assets.getModel();
        model.render();
    }

    protected static void luoErikoisEfektit() {
        if (Peli.huone != null) {
            if (Peli.huone.annaNimi().equals("Baari_salahuone")) {
                luoMustatPalkitEfekti();
            }
        }
    }

    protected static void luoMustatPalkitEfekti() {
        if (mustatPakitParillinenFadeKasvata) {
            mustatPalkitParillinenFade += mustatPalkitFadeAskel;
            mustatPalkitParitonFade -= mustatPalkitFadeAskel;
        }
        else {
            mustatPalkitParillinenFade -= mustatPalkitFadeAskel;
            mustatPalkitParitonFade += mustatPalkitFadeAskel;
        }
        if (mustatPalkitParillinenFade >= 1f) {
            mustatPakitParillinenFadeKasvata = false;
        }
        else if (mustatPalkitParillinenFade <= 0f) {
            mustatPakitParillinenFadeKasvata = true;
        }
    }
}
