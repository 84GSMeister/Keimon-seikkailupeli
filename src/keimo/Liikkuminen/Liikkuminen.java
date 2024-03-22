package keimo.Liikkuminen;
public class Liikkuminen {
    
    /**
     * Täysin kirottua koodia.
     * 
     * Mietittiin tossa vaan Lennin kanssa että mitäs jos tekis liikkumisesta olion (Object) :D
     * Aina kun pelaaja liikkuu niin luodaan uusi Liikkuminen-olio :DDD Olijo-ohjelmoinnin parhauksia :D
     * mutta naurattaahan se koska tää toimii :DDDddD
     */

    static void liikkuminen1() {

    }

    static Void liikkuminen2() {
        return null;
    }

    static Void liikkuminen3() {
        return (Void) new Object();
    }

    //static int[][][][][][][][][][][] asd = new int[10][][][][][][][][][][];

    protected static transient final int x = 0;
    private static transient volatile int y = 0;

    public void objecti() {
        Object object = new Object();
        if (!(object instanceof Void)) {
            object = (Void)new Object();
        }
    }

    Object object() {
        Boolean object = Boolean.valueOf(true);
        if (object || object instanceof Object || y == 0) {
            System.out.println(object);
        }
        return object;
    }
}
