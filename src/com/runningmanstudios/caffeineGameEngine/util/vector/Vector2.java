/**
 * com.runningmanstudios.caffeineGameEngine.vector is a group of utilities related to lists
 * it contains the Vector(2-4) and Dimension(2-4) classes
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Vector2
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Dimension2
 */
package com.runningmanstudios.caffeineGameEngine.util.vector;

/**
 * Vector2 Class, a list of 2 coordinate positions
 * contains x, y
 */
public class Vector2 {
    public Dimension2<Float, Float> vector;

    public Vector2(float x, float y) {
        this.vector = new Dimension2<Float, Float>(x, y);
    }

    public float getX(){
        return this.vector.v1;
    }

    public float getY(){
        return this.vector.v2;
    }

    public boolean equals(Vector2 with) {
        return this.vector.equals(with.vector);
    }

}
