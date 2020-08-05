package com.runningmanstudios.caffeineGameEngine.checks.event;

import com.runningmanstudios.caffeineGameEngine.checks.annotations.EventBusSubscriber;
import com.runningmanstudios.caffeineGameEngine.checks.exceptions.EventException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The EventBus manages and emits Events which are given to all Classes that have methods with the annotation @EventBusSubscriber.
 * To add a classes methods to the EventBus just call the method <code>eventBusInstance.jump(myClass.class);</code>.
 * @see com.runningmanstudios.caffeineGameEngine.checks.event.Event
 */
public class EventBus implements Serializable {
    private final Map<Class, List<EventBusListener>> listeners = new HashMap<>();

    /**
     * sends an Event to all methods that have that Event as a parameter.
     * Events must extend the Event class
     * @param event event to emit
     * @return if while being emited a listener cancelled the event then this will return true
     * cancelling doesn't actually do anything other than give you a boolean value.
     * you can decide whether or not to actually stop the process or not.
     * @see com.runningmanstudios.caffeineGameEngine.checks.event.Event
     */
    public boolean emit(Event event) {
        synchronized (this) {
            //create a boolean to check if the event has been cancelled or not
            boolean cancelled = false;
            //get class from event instance
            Class eventClass = event.getClass();
            //loop through every method that associate themselves with that class
            //Now we are going to backtrack and go through every listener that listens to a super of that event, including the event itself.
            //and then we are going to invoke the listener's method

            //create a boolean to see if we have found the base event class
            boolean foundEvent = false;
            //the super class we are checking
            Class superClass = eventClass;
            //a loop that goes through the super events
            while (!foundEvent) {
                //get all the methods that associate themselves with that super class
                List<EventBusListener> superEventSubscribers = this.listeners.get(superClass);
                //don't do the following if there are no methods that associate themselves with that super class
                if (superEventSubscribers != null) {
                    //loop through every method that associate themselves with that super class
                    for (EventBusListener superEventSubscriber : superEventSubscribers) {
                        //get the method from the listener object
                        Method superListenerMethod = superEventSubscriber.getListener();
                        //get the object from the listener object
                        //so we can invoke the non-static method above without throwing an error
                        Object superUpper = superEventSubscriber.getUpperObject();

                        //invoke the method, passing along the super object and the event
                        try {
                            superListenerMethod.invoke(superUpper, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        if (event.isCancelled()) {
                            cancelled = true;
                        }
                    }
                }
                //make the super class a super of itself
                superClass = superClass.getSuperclass();
                //check if super class is the event class; if true stop the loop
                if (superClass == Event.class) {
                    foundEvent = true;
                }
            }
            return cancelled;
        }
    }

    /**
     * adds a classes' methods to the listeners, if the method has a single Event as a parameter and the method has the annotation @EventBusSubscriber.
     * if a class does not have any methods with the annotation @EventBusSubscriber, this will throw a EventException.
     * if a method has the annotation @EventBusSubscriber but the method does not have a single Event as a parameter, this will throw a EventException.
     * the parameter determines the Event this method will be associated with, e.g. if a method has "MoveEvent" as a parameter then every time MoveEvent is emitted the method will be invoked.
     * @param listener class to add methods
     * @see EventException
     * @see EventBusSubscriber
     */
    public void subscribe(Object listener) {
        Class listenerClass = listener.getClass();
        List<Method> subscribers = getMethodsAnnotatedWith(listenerClass);

        if (subscribers.isEmpty()){
            try {
                throw new EventException("Class  ->  \"" + listenerClass.getSimpleName() + "\"\nClass does not have @EventBusSubscriber method");
            } catch (EventException e) {
                e.printStackTrace();
            }
        }

        for (Method eventSubscriber : subscribers) {
            if (eventSubscriber.getParameterTypes().length!=1) {
                try {
                    throw new EventException("Method  ->  \"" + eventSubscriber.getName() + "\"\nMethod does not have a single parameter");
                } catch (EventException e) {
                    e.printStackTrace();
                }
            }
            Class subscribingTo = eventSubscriber.getParameterTypes()[0];
            if (Event.class.isAssignableFrom(subscribingTo)) {
                // we have the event he wants

                if (!this.listeners.containsKey(subscribingTo)) {
                    this.listeners.put(subscribingTo, new LinkedList<>());
                }
                this.listeners.get(subscribingTo).add(new EventBusListener(listener, eventSubscriber));

                // you went to far down idiot
            } else {
                new EventException("Method  ->  \"" + eventSubscriber.getName() + "\"\nMethod does not have proper Event parameter").printStackTrace();
            }
        }
    }

    /**
     * gets the methods in a class that have the EventBusSubscriber annotation
     * @param type class to check
     * @return a list of all methods that have the EventBusSubscriber annotation
     */
    private static List<Method> getMethodsAnnotatedWith(final Class<?> type) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventBusSubscriber.class)) {
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }

    private class EventBusListener {
        private final Object upper;
        private final Method listener;

        public EventBusListener(Object upper, Method listener) {
            this.upper = upper;
            this.listener = listener;
        }
        public Object getUpperObject() {
            return this.upper;
        }
        public Method getListener() {
            return this.listener;
        }

        @Override
        public String toString() {
            return this.listener.getDeclaringClass().getSimpleName() + "@" + this.listener.getName();
        }
    }

}
