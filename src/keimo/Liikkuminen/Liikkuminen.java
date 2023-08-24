package keimo.Liikkuminen;
public class Liikkuminen {
    
    /**
     * Täysin kirottua koodia
     */

    //static int[][][][][][][][][][][] asd = new int[10][][][][][][][][][][];

    protected static transient final int x = 0;
    private static transient volatile int y = 0;

    static void liikkuminen1() {

    }

    static Void liikkuminen2() {
        return null;
    }

    static Void liikkuminen3() {
        return (Void) new Object();
    }

    public void objecti() {
        Object object = new Object();
        if (!(object instanceof Void)) {
            object = (Void)new Object();
        }
    }

    Object object() {
        Boolean object = Boolean.valueOf(true);
        if (object || object instanceof Object) {
            System.out.println(object);
        }
        return object;
    }
}
