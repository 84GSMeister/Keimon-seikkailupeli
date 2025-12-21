package keimo.keimoengine.collision;

public class Neliö {

    public int x = 0;
    public int y = 0;
    public int leveys;
    public int korkeus;

    public Neliö(int leveys, int korkeus) {
        this.leveys = leveys;
        this.korkeus = korkeus;
    }

    public Neliö(int sijX, int sijY, int leveys, int korkeus) {
        this.x = sijX;
        this.y = sijY;
        this.leveys = leveys;
        this.korkeus = korkeus;
    }

    /**
     * Returns the width of the bounding {@code Rectangle} in
     * {@code double} precision.
     * @return the width of the bounding {@code Rectangle}.
     */
    public double getWidth() {
        return leveys;
    }

    /**
     * Returns the height of the bounding {@code Rectangle} in
     * {@code double} precision.
     * @return the height of the bounding {@code Rectangle}.
     */
    public double getHeight() {
        return korkeus;
    }

    /**
     * Returns the X coordinate of the bounding {@code Rectangle} in
     * {@code double} precision.
     * @return the X coordinate of the bounding {@code Rectangle}.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the bounding {@code Rectangle} in
     * {@code double} precision.
     * @return the Y coordinate of the bounding {@code Rectangle}.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the smallest X coordinate of the framing
     * rectangle of the {@code Shape} in {@code double}
     * precision.
     * @return the smallest X coordinate of the framing
     *          rectangle of the {@code Shape}.
     * @since 1.2
     */
    public double getMinX() {
        return getX();
    }

    /**
     * Returns the smallest Y coordinate of the framing
     * rectangle of the {@code Shape} in {@code double}
     * precision.
     * @return the smallest Y coordinate of the framing
     *          rectangle of the {@code Shape}.
     * @since 1.2
     */
    public double getMinY() {
        return getY();
    }

    /**
     * Returns the largest X coordinate of the framing
     * rectangle of the {@code Shape} in {@code double}
     * precision.
     * @return the largest X coordinate of the framing
     *          rectangle of the {@code Shape}.
     * @since 1.2
     */
    public double getMaxX() {
        return getX() + getWidth();
    }

    /**
     * Returns the largest Y coordinate of the framing
     * rectangle of the {@code Shape} in {@code double}
     * precision.
     * @return the largest Y coordinate of the framing
     *          rectangle of the {@code Shape}.
     * @since 1.2
     */
    public double getMaxY() {
        return getY() + getHeight();
    }

    /**
     * Returns the X coordinate of the center of the framing
     * rectangle of the {@code Shape} in {@code double}
     * precision.
     * @return the X coordinate of the center of the framing rectangle
     *          of the {@code Shape}.
     * @since 1.2
     */
    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    /**
     * Returns the Y coordinate of the center of the framing
     * rectangle of the {@code Shape} in {@code double}
     * precision.
     * @return the Y coordinate of the center of the framing rectangle
     *          of the {@code Shape}.
     * @since 1.2
     */
    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    /**
     * Returns the location of this {@code Rectangle}.
     * <p>
     * This method is included for completeness, to parallel the
     * {@code getLocation} method of {@code Component}.
     * @return the {@code Point} that is the upper-left corner of
     *                  this {@code Rectangle}.
     * @see       java.awt.Component#getLocation
     * @see       #setLocation(Point)
     * @see       #setLocation(int, int)
     * @since     1.1
     */
    public Piste getLocation() {
        return new Piste(x, y);
    }
    
    /**
     * Determines whether or not this {@code Rectangle} and the specified
     * {@code Rectangle} intersect. Two rectangles intersect if
     * their intersection is nonempty.
     *
     * @param r the specified {@code Rectangle}
     * @return    {@code true} if the specified {@code Rectangle}
     *            and this {@code Rectangle} intersect;
     *            {@code false} otherwise.
     */
    public boolean intersects(Neliö n) {
        int tw = this.leveys;
        int th = this.korkeus;
        int rw = n.leveys;
        int rh = n.korkeus;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = this.x;
        int ty = this.y;
        int rx = n.x;
        int ry = n.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }

    /**
     * Moves this {@code Rectangle} to the specified location.
     * <p>
     * This method is included for completeness, to parallel the
     * {@code setLocation} method of {@code Component}.
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     * @see       #getLocation
     * @see       java.awt.Component#setLocation(int, int)
     * @since     1.1
     */
    public void setLocation(int x, int y) {
        move(x, y);
    }

    /**
     * Moves this {@code Rectangle} to the specified location.
     *
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     * @deprecated As of JDK version 1.1,
     * replaced by {@code setLocation(int, int)}.
     */
    @Deprecated
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
