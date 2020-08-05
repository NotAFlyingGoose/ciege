/**
 * com.runningmanstudios.caffeineGameEngine.vector is a group of utilities related to lists
 * it contains the Vector(2-4) and Dimension(2-4) classes
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Vector2
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Dimension2
 */
package com.runningmanstudios.caffeineGameEngine.util.vector;

/**
 * Dimension2 Class, a list of 2 changeable type
 * @param <T1> the first type of the value
 * @param <T2> the second type of the value
 */
public class Dimension2<T1, T2> {
    public T1 v1;
    public T2 v2;

    public Dimension2(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public boolean equals(Dimension2 with) {
        return with.v1 == this.v1 && with.v2 == this.v2;
    }
}
