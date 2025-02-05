package keimo.keimoEngine.kenttä;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.kenttäkohteet.KenttäKohde;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class KenttäShaderEfektit {

    static Random random = new Random();
    
    static float punainen = 0f, vihreä = 0.5f, sininen = 1f;
    static boolean lisääPun = true, lisääVihr = true, lisääSin = false;
    static float lisäysAskelVäri = 0.002f;
    protected static void kenttäVäriEfekti(Shader shader) {
        shader.bind();
        if (Peli.huone.annaNimi().equals("Metsä") || Peli.huone.annaNimi().equals("Metsä_kalja")) {
            shader.setUniform("himmennys", new Vector4f(0.5f, 0.5f, 0.5f, 0f));
        }
        else if (Peli.huone.annaNimi().equals("Metsä_boss")) {
            shader.setUniform("himmennys", new Vector4f(0.7f, 0.7f, 0.7f, 0f));
        }
        else if (Peli.huone.annaNimi().equals("Keimo-baari")) {
            if (lisääPun) punainen += lisäysAskelVäri;
            else punainen -= lisäysAskelVäri;
            if (lisääVihr) vihreä += lisäysAskelVäri;
            else vihreä -= lisäysAskelVäri;
            if (lisääSin) sininen += lisäysAskelVäri;
            else sininen -= lisäysAskelVäri;
            
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

    protected static Matrix4f esineenKeinumisEfekti(KenttäKohde objekti, Shader shader, Matrix4f projection) {
        projection.translate(new Vector3f(0, 0.5f - (float)(0.5*Math.sin(Math.toRadians(objekti.annaLiikeY()))), 0));
        return projection;
    }

    static float xHeilunnanNopeus = 0f;
    static float xHeilunnanPituus = 0f;
    protected static Matrix4f känniEfekti(Shader shader, Matrix4f projection) {
        shader.bind();
        if (Pelaaja.känninVoimakkuusFloat > 0f) {
            float känniHajontaX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniHajontaY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniHajontaZ = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniScaleX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniScaleY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
            float känniScaleZ = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);

            xHeilunnanNopeus += Pelaaja.känninVoimakkuusFloat * 0.000005f * projection.getTranslation(new Vector3f()).x;
            xHeilunnanPituus = Pelaaja.känninVoimakkuusFloat * 0.2f;
            projection.translate(xHeilunnanPituus * (float)Math.sin(xHeilunnanNopeus) + känniHajontaX/50f, känniHajontaY/50f, känniHajontaZ/50f);
            projection.scale(1 + känniScaleX/50f, 1+ känniScaleY/50f, 1+ känniScaleZ/50f);
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
