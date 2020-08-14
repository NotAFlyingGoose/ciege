/**
 * com.runningmanstudios.caffeineGameEngine.vector is a group of utilities related to lists
 * it contains the Vector(2-4) and Dimension(2-4) classes
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Vector2
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Dimension2
 */
package com.runningmanstudios.caffeineGameEngine.util.vector;

/**
 * Dimension4 Class, a list of 4 changeable type
 * @param <T1> the first type of the value
 * @param <T2> the second type of the value
 * @param <T3> the third type of the value
 * @param <T4> the fourth type of the value
 */
public class Dimension4<T1, T2, T3, T4> {
    public T1 v1;
    public T2 v2;
    public T3 v3;
    public T4 v4;

    public Dimension4(T1 v1, T2 v2, T3 v3, T4 v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public boolean equals(Dimension4 with) {
        return with.v1 == this.v1 && with.v2 == this.v2 && with.v3 == this.v3 && with.v4 == this.v4;
    }

    public Dimension3<T1, T2, T3> get3D() {
        return new Dimension3<T1, T2, T3>(this.v1, this.v2, this.v3);
    }
}
