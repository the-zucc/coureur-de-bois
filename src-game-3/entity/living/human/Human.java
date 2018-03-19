package entity.living.human;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import entity.living.LivingEntity;
import game.Map;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public abstract class Human extends LivingEntity implements AttachableReceiver {
	int level;

	public Human(Point3D position, Map map, int level) {
		super(position, map);
		this.level = level;
	}
	@Override
	public boolean shouldUpdateComponent() {
		return true;
	}
	
	@Override
	public void attach(Attachable a) {
		getComponent().addChildComponent(a.getComponent());
		a.onAttach(this);
		onAttachActions(a);
	}

	@Override
	public void detach(Attachable a) {
		if(getAttachables().contains(a)){
			getAttachables().remove(a);
			getComponent().removeChildComponent(a.getComponent());
			a.onDetach(this);
		}
	}

	@Override
	public void onAttachActions(Attachable a) {

	}

	@Override
	public void onDetachActions(Attachable a) {

	}

	@Override
	public ArrayList<Attachable> getAttachables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttachables() {

	}
}
