package game.propertychange;

public interface IRegisterForEvent<T> {
	public void register(PropertyChangeListener<T> listener);
}
