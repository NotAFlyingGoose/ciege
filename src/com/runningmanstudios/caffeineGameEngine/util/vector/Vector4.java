/**
 * com.runningmanstudios.caffeineGameEngine.vector is a group of utilities related to lists
 * it contains the Vector(2-4) and Dimension(2-4) classes
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Vector2
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Dimension2
 */
package com.runningmanstudios.caffeineGameEngine.util.vector;

/**
 * Vector4 Class, a list of 4 coordinate positions
 * contains x, y, z, w
 */
public class Vector4 {
    public Dimension4<Float, Float, Float, Float> vector;

    public Vector4(float x, float y, float z, float w){
        this.vector = new Dimension4<Float, Float, Float, Float>(x, y, z, w);
    }

    public float getX(){
        return this.vector.v1;
    }

    public float getY(){
        return this.vector.v2;
    }

    public float getZ(){
        return this.vector.v3;
    }

    public float getW(){
        return this.vector.v4;
    }

    public boolean equals(Vector4 with) {
        return this.vector.equals(with.vector);
    }
}
