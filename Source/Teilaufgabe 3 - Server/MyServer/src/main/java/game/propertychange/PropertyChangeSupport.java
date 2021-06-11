package game.propertychange;

import java.util.ArrayList;
import java.util.List;

public class PropertyChangeSupport<T> {

	private final List<PropertyChangeListener<T>> listeners = new ArrayList<>();

	public void fire(T data) {
		listeners.stream().forEach(listener -> listener.handle(data));
	}

	public void fire() {
		for (var listener : listeners) {
			listener.handle(null);
		}

		// listeners.stream().forEach(listener -> listener.handle(null));
	}

	public void register(PropertyChangeListener<T> listener) {
		listeners.add(listener);
		// listener.handle(null);
	}

}
