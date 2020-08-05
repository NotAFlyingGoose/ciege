/**
 * com.runningmanstudios.caffeineGameEngine.utilities.noise has classess for creating SimplexNoise
 * @see com.runningmanstudios.caffeineGameEngine.util.noise.SimplexNoise
 */
package com.runningmanstudios.caffeineGameEngine.util.noise;

import java.io.Serializable;
import java.util.Random;

/**
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 *
 */

public class SimplexNoise implements Serializable {

    SimplexNoise_octave[] octaves;
    double[] frequencys;
    double[] amplitudes;

    int largestFeature;
    double persistence;
    long seed;

    /**
     * create simplex noise
     * @param largestFeature size of noise
     * @param persistence smoothness of the noise
     * @param rawSeed String that is converted into a long seed
     */
    public SimplexNoise(int largestFeature, double persistence, String rawSeed){
        this(largestFeature, persistence, rawSeed.hashCode());
    }

    /**
     * create simplex noise
     * @param largestFeature size of noise
     * @param persistence smoothness of the noise
     * @param seed seed of the noise
     */
    public SimplexNoise(int largestFeature,double persistence, long seed){

        this.largestFeature=largestFeature;
        this.persistence=persistence;
        this.seed=seed;

        //recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));

        this.octaves =new SimplexNoise_octave[numberOfOctaves];
        this.frequencys =new double[numberOfOctaves];
        this.amplitudes =new double[numberOfOctaves];

        Random rnd=new Random(seed);

        for(int i=0;i<numberOfOctaves;i++){
            this.octaves[i]=new SimplexNoise_octave(rnd.nextInt());

            this.frequencys[i] = Math.pow(2,i);
            this.amplitudes[i] = Math.pow(persistence, this.octaves.length-i);
        }

    }

    /**
     * get noise value for x and y position
     * @param x x position of noise
     * @param y y position of noise
     * @return noise value between -1 and +1
     */
    public double getNoise(long x, long y){

        double result=0;

        for(int i = 0; i< this.octaves.length; i++){
            //double frequency = Math.pow(2,i);
            //double amplitude = Math.pow(persistence,octaves.length-i);

            result=result+ this.octaves[i].noise(x/ this.frequencys[i], y/ this.frequencys[i])* this.amplitudes[i];
        }


        return result;

    }

    /**
     * get noise value for x, y and z position
     * @param x x position of noise
     * @param y y position of noise
     * @param z z position of noise
     * @return noise value between -1 and +1
     */
    public double getNoise(int x,int y, int z){

        double result=0;

        for(int i = 0; i< this.octaves.length; i++){
            double frequency = Math.pow(2,i);
            double amplitude = Math.pow(this.persistence, this.octaves.length-i);

            result=result+ this.octaves[i].noise(x/frequency, y/frequency,z/frequency)* amplitude;
        }


        return result;

    }
}
