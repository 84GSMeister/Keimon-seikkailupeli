package keimo.keimoEngine.kenttä;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.keimoEngine.grafiikat.Shader;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class KenttäShaderEfektit {

    static Random random = new Random();
    
    static float punainen = 0f, vihreä = 0.5f, sininen = 1f;
    static boolean lisääPun = true, lisääVihr = true, lisääSin = false;
    static float lisäysAskelVäriBaari = 0.002f;
    static float lisäysAskelVäriKuu = 0.008f;
    protected static void kenttäVäriEfekti(Shader shader) {
        shader.bind();
        if (Peli.huone.annaNimi().equals("Metsä") || Peli.huone.annaNimi().equals("Metsä_kalja")) {
            shader.setUniform("himmennys", new Vector4f(0.5f, 0.5f, 0.5f, 0f));
        }
        else if (Peli.huone.annaNimi().equals("Metsä_boss")) {
            shader.setUniform("himmennys", new Vector4f(0.7f, 0.7f, 0.7f, 0f));
        }
        else if (Peli.huone.annaNimi().equals("Keimo-baari")) {
            if (lisääPun) punainen += lisäysAskelVäriBaari;
            else punainen -= lisäysAskelVäriBaari;
            if (lisääVihr) vihreä += lisäysAskelVäriBaari;
            else vihreä -= lisäysAskelVäriBaari;
            if (lisääSin) sininen += lisäysAskelVäriBaari;
            else sininen -= lisäysAskelVäriBaari;
            
            if (punainen >= 1f) lisääPun = false;
            else if (punainen <= 0f) lisääPun = true;
            if (vihreä >= 1f) lisääVihr = false;
            else if (vihreä <= 0f) lisääVihr = true;
            if (sininen >= 1f) lisääSin = false;
            else if (sininen <= 0f) lisääSin = true;
            
            shader.setUniform("himmennys", new Vector4f(punainen, vihreä, sininen, 0f));
        }
        else if (Peli.huone.annaNimi().equals("Kuu")) {
            if (lisääPun) punainen += lisäysAskelVäriKuu;
            else punainen -= lisäysAskelVäriKuu*0.816;
            if (lisääVihr) vihreä += lisäysAskelVäriKuu;
            else vihreä -= lisäysAskelVäriKuu*0.739;
            if (lisääSin) sininen += lisäysAskelVäriKuu;
            else sininen -= lisäysAskelVäriKuu;
            
            if (punainen >= 1f) lisääPun = false;
            else if (punainen <= 0f) lisääPun = true;
            if (vihreä >= 1f) lisääVihr = false;
            else if (vihreä <= 0f) lisääVihr = true;
            if (sininen >= 1f) lisääSin = false;
            else if (sininen <= 0f) lisääSin = true;
            
            shader.setUniform("himmennys", new Vector4f(punainen, vihreä, sininen, 0f));
        }
        else shader.setUniform("himmennys", new Vector4f(0f, 0f, 0f, 0f));
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
            projection.rotate(rotaationNopeus * (float)Math.toRadians(rotZ), new Vector3f(0, 0, 1));
            rotZ++;
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
}
