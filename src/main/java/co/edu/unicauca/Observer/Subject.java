package co.edu.unicauca.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para el patrón Observer
 */
public class Subject {
    private List<Observer> observers;

    public Subject() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    public void notifyAllObserves() {
        // Notificar sin parámetro
        if (observers != null) {
            for (Observer observer : observers) {
                observer.update(null);
            }
        }
    }

    public void notifyAllObserves(Object data) {
        // Notificar con parámetro
        if (observers != null) {
            for (Observer observer : observers) {
                observer.update(data);
            }
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }
}
