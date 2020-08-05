/**
 * com.runningmanstudios.caffeineGameEngine.vector is a group of utilities related to lists
 * it contains the Vector(2-4) and Dimension(2-4) classes
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Vector2
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Dimension2
 */
package com.runningmanstudios.caffeineGameEngine.util.vector;

/**
 * Dimension3 Class, a list of 3 changeable type
 * @param <T1> the first type of the value
 * @param <T2> the second type of the value
 * @param <T3> the third type of the value
 */
public class Dimension3<T1, T2, T3> {
    public T1 v1;
    public T2 v2;
    public T3 v3;

    public Dimension3(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public boolean equals(Dimension3 with) {
        return with.v1 == this.v1 && with.v2 == this.v2 && with.v3 == this.v3;
    }

    public Dimension2<T1, T2> get2D() {
        return new Dimension2<T1, T2>(this.v1, this.v2);
    }
}
