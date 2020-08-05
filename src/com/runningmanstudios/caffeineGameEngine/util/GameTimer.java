/**
 * com.runningmanstudios.caffeineGameEngine.utilities is a bunch of utility classes that make it easier to do certain operations
 */
package com.runningmanstudios.caffeineGameEngine.util;

/**
 * GameTimer creates a timer that will return a boolean value when it is completed
 */
public class GameTimer implements Runnable {
    private static int currentTimer = 0;
    boolean running;
    private final Thread timerThread;
    private final int milliseconds;
    private boolean done = false;
    private boolean onceAndDone = true;
    private boolean play = false;
    private boolean restart = false;

    /**
     * creates the timer
     * @param milliseconds how long the timer last
     */
    public GameTimer(int milliseconds) {
        currentTimer++;
        this.milliseconds = milliseconds;
        this.running = true;
        this.timerThread = new Thread(this, "Timer-"+currentTimer);
        this.timerThread.setDaemon(true);
        this.timerThread.start();
    }

    /**
     * creates the timer
     * @param milliseconds how long the timer last
     * @param onceAndDone set done to false after you check if the timer is done
     *      true: if the done == true and isDone() has been called then done will be set to false
     *      false: if done == true then it will be left like that (not recommended)
     */
    public GameTimer(int milliseconds, boolean onceAndDone) {
        currentTimer++;
        this.onceAndDone = onceAndDone;
        this.milliseconds = milliseconds;
        this.running = true;
        this.timerThread = new Thread(this, "Timer-"+currentTimer);
        this.timerThread.start();
    }

    /**
     * Main timer thread
     */
    @Override
    public void run() {
        long timer = System.currentTimeMillis();

        while (this.running) {
            if (this.play) {
                if (System.currentTimeMillis() - timer > this.milliseconds) {
                    this.done = true;
                    this.running = false;
                }
            }
            if (this.restart) {
                this.restart = false;
                timer = System.currentTimeMillis();
                this.done = false;
            }
        }

    }

    /**
     * pauses the timer, stopping it <em>in place.
     */
    public void pause() {
        this.play = false;
    }

    /**
     * resumes the timer, starting it where it left off.
     */
    public void resume() {
        this.play = true;
    }

    /**
     * pauses and restarts the timer. you will have to <code>resume()</code> to start it again
     */
    public void stop() {
        this.play = false;
        this.restart = true;
    }

    /**
     * check if the timer is done
     * if onceAndDone == true then done will be set to false
     * @return whether the timer has completed or not
     */
    public boolean isDone() {
        boolean before = this.done;
        if (this.onceAndDone && this.done) {
            this.done = false;
        }
        return before;
    }

    /**
     * kills the timer's thread.
     */
    public void kill() {
        this.running = false;
        this.timerThread.interrupt();
    }

    /**
     * if the timer is alive
     */
    public boolean isAlive() {
        return this.running;
    }
}
