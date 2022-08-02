package Observers;

import Hex.GameObject;
import Observers.Events.Event;

public interface Observer {
    void onNotify(GameObject object, Event event);
}
