package com.runningmanstudios.caffeineGameEngine.checks.exceptions;

public class GameBuilderException extends Exception{

    public GameBuilderException(String message) {
        super(message);
    }

    public GameBuilderException(Throwable cause) {
        super(cause);
    }

    public GameBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

}
