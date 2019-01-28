package characteristic;

public interface ComponentUpdateable extends Updateable, ComponentOwner{
	public boolean shouldUpdateComponent();
	public void updateComponent();
}
