package keimo.keimoEngine.assets;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Utility.Käännettävä.SuuntaVasenOikea;
import keimo.keimoEngine.collision.AABB;
import keimo.keimoEngine.collision.Collision;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti2d.Transform;
import keimo.keimoEngine.gui.hud.NäppäinVinkkiTekstit;
import keimo.keimoEngine.gui.hud.TavoitePopup;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.kenttä.Maailma;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Random;

public class PelaajaModel {
    
    private Transform transform;
    private AABB boundingBox;

    private HashMap<String, Animaatio> animaatiot = new HashMap<>();
    private Tekstuuri hyökkäysTekstuuri = new Tekstuuri("tiedostot/kuvat/entity/hyökkäys_sprite.png");
    private Kuva esineTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private Kuva hitboxKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/entity/hitbox_kehys.png");
    private Shader pelaajaShader = new Shader("shader");
    private Shader pelaajaShaderStaattinen = new Shader("staattinen");
    private boolean peilaaX = false;
    private boolean peilaaY = false;
    private int scale = 32;
    private Random random = new Random();

    public PelaajaModel() {
        
        this.animaatiot.put("default", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/pelaaja_default.gif"));
        this.animaatiot.put("ylensyönti", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/pelaaja_ylensyönti.gif"));

        this.animaatiot.put("idle_hyvä_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_laiha.gif"));
        this.animaatiot.put("idle_hyvä_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_normaali.gif"));
        this.animaatiot.put("idle_hyvä_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_lihava.gif"));
        this.animaatiot.put("idle_hyvä_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_erittäinlihava.gif"));
        this.animaatiot.put("idle_neutraali_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_laiha.gif"));
        this.animaatiot.put("idle_neutraali_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_normaali.gif"));
        this.animaatiot.put("idle_neutraali_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_lihava.gif"));
        this.animaatiot.put("idle_neutraali_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_erittäinlihava.gif"));
        this.animaatiot.put("idle_huono_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_laiha.gif"));
        this.animaatiot.put("idle_huono_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_normaali.gif"));
        this.animaatiot.put("idle_huono_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_lihava.gif"));
        this.animaatiot.put("idle_huono_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_erittäinlihava.gif"));
        
        this.animaatiot.put("kävely_sivulle_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_sivulle_laiha.gif"));
        this.animaatiot.put("kävely_ylös_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_laiha.gif"));
        this.animaatiot.put("kävely_alas_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_laiha.gif"));
        this.animaatiot.put("kävely_yläsivu_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_yläsivu_laiha.gif"));
        this.animaatiot.put("kävely_alasivu_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alasivu_laiha.gif"));
        this.animaatiot.put("kävely_sivulle_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_sivulle_normaali.gif"));
        this.animaatiot.put("kävely_ylös_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_normaali.gif"));
        this.animaatiot.put("kävely_alas_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_normaali.gif"));
        this.animaatiot.put("kävely_yläsivu_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_yläsivu_normaali.gif"));
        this.animaatiot.put("kävely_alasivu_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alasivu_normaali.gif"));
        this.animaatiot.put("kävely_sivulle_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_sivulle_lihava.gif"));
        this.animaatiot.put("kävely_ylös_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_lihava.gif"));
        this.animaatiot.put("kävely_alas_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_lihava.gif"));
        this.animaatiot.put("kävely_yläsivu_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_yläsivu_lihava.gif"));
        this.animaatiot.put("kävely_alasivu_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alasivu_lihava.gif"));
        this.animaatiot.put("kävely_sivulle_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_sivulle_erittäinlihava.gif"));
        this.animaatiot.put("kävely_ylös_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_erittäinlihava.gif"));
        this.animaatiot.put("kävely_alas_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_erittäinlihava.gif"));
        this.animaatiot.put("kävely_yläsivu_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_yläsivu_erittäinlihava.gif"));
        this.animaatiot.put("kävely_alasivu_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alasivu_erittäinlihava.gif"));
        this.transform = new Transform();
        this.transform.scale = new Vector3f(scale, scale,1);
        this.boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
    }

    public void update(float delta, Window window, Kamera camera, Maailma world) {
        transform.pos.set(new Vector3f((float)Pelaaja.hitbox.getCenterX()/scale -1, (float)-Pelaaja.hitbox.getCenterY()/scale +1, 0));
        boundingBox.getCenter().set(transform.pos.x, transform.pos.y);

        int collisionCheckRadius = 5;
        AABB[][] boxes = new AABB[5][5];
        for (int y = 0; y < collisionCheckRadius; y++) {
            for (int x = 0; x < collisionCheckRadius; x++) {
                boxes[x][y] = world.getTileBoundingBox(
                    (int)(((transform.pos.x / 2) + 0.5f) - (collisionCheckRadius / 2) + x),
                    (int)(((-transform.pos.y / 2) + 0.5f) - (collisionCheckRadius / 2) + y)
                );
            }
        }
        AABB box = null;
        for (int y = 0; y < boxes.length; y++) {
            for (int x = 0; x < boxes.length; x++) {
                if (boxes[x][y] != null) {
                    if (box == null) {
                        box = boxes[x][y];
                    }
                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[x][y].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[x][y];
                    }
                }
            }
        }

        if (box != null) {
            Collision data = boundingBox.getCollision(box);
            if (data.isIntersecting) {
                boundingBox.correctPosition(box, data);
                transform.pos.set(boundingBox.getCenter(), 0);
            }
        }

        //camera.setPosition(transform.pos.mul(-world.getScale()/2, new Vector3f()));
    }

    

    public void render(Kamera camera, Maailma world) {
        pelaajaShader.bind();
        pelaajaShader.setUniform("sampler", 0);

        float känniHajontaX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
		float känniHajontaY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
		float känniScaleX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
		float känniScaleY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
    
        float scaleX = 64;
        float scaleY = 64;

        pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection().translate(känniHajontaX, känniHajontaY, 0)));
        valitsePelaajanKuvake();
        if (Pelaaja.känniKuolemattomuus > 0) {
            if (Peli.globaaliTickit % 4 == 0 || Peli.globaaliTickit % 4 == 1) {
                pelaajaShader.setUniform("addcolor", new Vector4f(1, 0, 0, 0));
            }
            else {
                pelaajaShader.setUniform("addcolor", new Vector4f(0, 0, 1, 0));
            }
        }
        else pelaajaShader.setUniform("addcolor", new Vector4f(0, 0, 0, 0));
        if (Pelaaja.kuolemattomuusAika <= 0 || Peli.globaaliTickit % 2 == 0) {
            Assets.getModel(0, peilaaX, peilaaY).render();
        }

        if (Pelaaja.hyökkäysAika > 0) {
            boolean hyökkäysKuvakkeenPeilaus = Pelaaja.keimonSuuntaVasenOikea == SuuntaVasenOikea.VASEN;
            pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection().translate(känniHajontaX, känniHajontaY, 0).translate(hyökkäysKuvakkeenPeilaus ? -32 : 32, 0, 0)));
            hyökkäysTekstuuri.bind(0);
            Assets.getModel(0, !hyökkäysKuvakkeenPeilaus, false).render();
        }
        if (Peli.valittuEsine != null) {
            pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection().scale(0.5f, 0.5f, 0).translate((float)Pelaaja.hitbox.getX() + känniHajontaX, (float)-Pelaaja.hitbox.getY() + känniHajontaY, 0)));
            esineTekstuuri = Peli.valittuEsine.annaTekstuuri();
            esineTekstuuri.bind(0);
            Assets.getModel().render();
        }
        if (Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] != null) {
            NäppäinVinkkiTekstit.renderöiNäppäinVinkki(pelaajaShaderStaattinen, camera, transform);
        }
    }

    private void valitsePelaajanKuvake() {
        try {
            peilaaX = false;
            peilaaY = false;
            switch (Pelaaja.keimonState) {
                case IDLE -> {
                    switch (Pelaaja.keimonTerveys) {
                        case ÜBER -> animaatiot.get("default").bind();
                        case HYVÄ -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_laiha").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_laiha").bind(); break;
                                    }
                                }
                                case NORMAALI -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_normaali").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_normaali").bind(); break;
                                    }
                                }
                                case LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_lihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_lihava").bind(); break;
                                    }
                                }
                                case ERITTÄIN_LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_erittäinlihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_erittäinlihava").bind(); break;
                                    }
                                }
                                case YLENSYÖNTI -> animaatiot.get("ylensyönti").bind();
                            }
                        }
                        case OK -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_laiha").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_laiha").bind(); break;
                                    }
                                }
                                case NORMAALI -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_normaali").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_normaali").bind(); break;
                                    }
                                }
                                case LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_lihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_lihava").bind(); break;
                                    }
                                }
                                case ERITTÄIN_LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_erittäinlihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_erittäinlihava").bind(); break;
                                    }
                                }
                                case YLENSYÖNTI -> animaatiot.get("ylensyönti").bind();
                            }
                        }
                        case HUONO -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_laiha").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_laiha").bind(); break;
                                    }
                                }
                                case NORMAALI -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_normaali").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_normaali").bind(); break;
                                    }
                                }
                                case LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_lihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_lihava").bind(); break;
                                    }
                                }
                                case ERITTÄIN_LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_erittäinlihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_erittäinlihava").bind(); break;
                                    }
                                }
                                case YLENSYÖNTI -> animaatiot.get("ylensyönti").bind();
                            }
                        }
                    }
                }
                case JUOKSU -> {
                    switch (Pelaaja.keimonSuunta) {
                        case VASEN -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_sivulle_laiha").bind(); peilaaX = true; break;
                                case NORMAALI: animaatiot.get("kävely_sivulle_normaali").bind(); peilaaX = true; break;
                                case LIHAVA: animaatiot.get("kävely_sivulle_lihava").bind(); peilaaX = true; break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_sivulle_erittäinlihava").bind(); peilaaX = true; break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); peilaaX = true; break;
                            }
                        }
                        case OIKEA -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_sivulle_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_sivulle_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_sivulle_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_sivulle_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case YLÖS -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_ylös_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_ylös_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_ylös_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_ylös_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case ALAS -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_alas_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_alas_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_alas_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_alas_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case YLÄOIKEA -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_yläsivu_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_yläsivu_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_yläsivu_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_yläsivu_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case YLÄVASEN -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_yläsivu_laiha").bind(); peilaaX = true; break;
                                case NORMAALI: animaatiot.get("kävely_yläsivu_normaali").bind(); peilaaX = true; break;
                                case LIHAVA: animaatiot.get("kävely_yläsivu_lihava").bind(); peilaaX = true; break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_yläsivu_erittäinlihava").bind(); peilaaX = true; break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case ALAOIKEA -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_alasivu_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_alasivu_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_alasivu_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_alasivu_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case ALAVASEN -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_alasivu_laiha").bind(); peilaaX = true; break;
                                case NORMAALI: animaatiot.get("kävely_alasivu_normaali").bind(); peilaaX = true; break;
                                case LIHAVA: animaatiot.get("kävely_alasivu_lihava").bind(); peilaaX = true; break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_alasivu_erittäinlihava").bind(); peilaaX = true; break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        
                    }
                }
                case null, default -> {

                }
            }
        }
        catch (NullPointerException npe) {
            animaatiot.get("default").bind();
        }
    }

    public void renderöiHitboxKehys(Rectangle hitbox, Kamera camera) {
        pelaajaShaderStaattinen.bind();
        pelaajaShaderStaattinen.setUniform("sampler", 0);

        pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection()));
        hitboxKehysTekstuuri.bind(0);
        Assets.getModel(0, peilaaX, peilaaY).render();
    }
}
