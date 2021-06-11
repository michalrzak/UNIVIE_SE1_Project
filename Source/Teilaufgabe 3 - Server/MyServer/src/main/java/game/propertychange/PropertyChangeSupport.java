package game.propertychange;

import java.util.ArrayList;
import java.util.List;

public class PropertyChangeSupport<T> {

	private final List<PropertyChangeListener<T>> listners = new ArrayList<>();

	public void fire(T data) {
		listners.forEach(listner -> listner.handle(data));
	}

	public void fire() {
		listners.forEach(listner -> listner.handle(null));
	}

	public void register(PropertyChangeListener<T> listener) {
		listners.add(listener);
	}

}
