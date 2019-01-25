package com.passive.api.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Lazy<T> extends ObjectProperty<T> {

    private Observable observable;
    private InvalidationListener listener;
    private T value;
    private Callable<T> callable;
    private List<InvalidationListener> invalidationListeners;
    private List<ChangeListener<? super T>> changeListeners;

    public Lazy(Callable<T> callable) {
        this.callable = callable;
    }

    private void valueChanged(T oldValue, T newValue) {
        if (oldValue != newValue) {
            if (changeListeners != null) {
                changeListeners.forEach(c -> c.changed(this, oldValue, newValue));
            }
        }
        if (invalidationListeners != null) {
            invalidationListeners.forEach(i -> i.invalidated(this));
        }
    }

    public T recalculate() {
        try {
            value = callable.call();
            valueChanged(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public T get() {
        if (value == null) {
            try {
                value = callable.call();
                valueChanged(null, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    @Override
    public void set(T newValue) {
        T oldValue = value;
        value = newValue;
        valueChanged(oldValue, newValue);
    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void bind(ObservableValue<? extends T> observable) {
        if (listener == null) {
            listener = (obs) -> set(observable.getValue());
        }
        observable.addListener(listener);
        this.observable = observable;
    }

    @Override
    public void unbind() {
        this.observable.removeListener(listener);
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        if (changeListeners == null) {
            changeListeners = new ArrayList<>();
        }
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        if (changeListeners != null) {
            changeListeners.remove(listener);
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (invalidationListeners == null) {
            invalidationListeners = new ArrayList<>();
        }
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        if (invalidationListeners != null) {
            invalidationListeners.remove(listener);
        }
    }
}
