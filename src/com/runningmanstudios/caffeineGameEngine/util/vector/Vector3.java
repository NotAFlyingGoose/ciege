/**
 * com.runningmanstudios.caffeineGameEngine.vector is a group of utilities related to lists
 * it contains the Vector(2-4) and Dimension(2-4) classes
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Vector2
 * @see com.runningmanstudios.caffeineGameEngine.util.vector.Dimension2
 */
package com.runningmanstudios.caffeineGameEngine.util.vector;

/**
 * Vector3 Class, a list of 3 coordinate positions
 * contains x, y, z
 */
public class Vector3 {
    public Dimension3<Float, Float, Float> vector;

    public Vector3(float x, float y, float z){
        this.vector = new Dimension3<Float, Float, Float>(x, y, z);
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

    public boolean equals(Vector3 with) {
        return this.vector.equals(with.vector);
    }
}
