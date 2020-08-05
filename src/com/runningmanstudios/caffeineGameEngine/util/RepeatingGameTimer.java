/**
 * com.runningmanstudios.caffeineGameEngine.utilities is a bunch of utility classes that make it easier to do certain operations
 */
package com.runningmanstudios.caffeineGameEngine.util;

/**
 * RepeatingGameTimer creates a repeating timer that will return a boolean value every time it is completed
 */
public class RepeatingGameTimer implements Runnable {
    private static int currentTimer = 0;
    boolean running = false;
    private final Thread timerThread;
    private final int milliseconds;
    private boolean done = false;
    private boolean play = false;
    private boolean restart = false;

    /**
     * creates the timer
     * @param milliseconds how long the timer lasts (after it reaches it will restart unlike GameTimer)
     */
    public RepeatingGameTimer(int milliseconds) {
        currentTimer++;
        this.milliseconds = milliseconds;
        this.running = true;
        this.timerThread = new Thread(this, "Repeating_Timer-"+currentTimer);
        this.timerThread.setDaemon(true);
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
                //every second
                if (System.currentTimeMillis() - timer > this.milliseconds) {
                    timer += this.milliseconds;
                    this.done = true;
                }
            } else if (this.restart) {
                this.restart = false;
                timer = System.currentTimeMillis();
                this.done = false;
                this.running = true;
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
     * if timer is done, done will be set to false;
     * @return whether the timer has completed or not
     */
    public boolean isDone() {
        boolean before = this.done;
        if (this.done) this.done = false;
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
