package visual;

import java.util.Hashtable;

import javafx.scene.Group;

public class Component extends Group{
	private Hashtable<String, Component> subComponents;
	public Component(String id){
		setId(id);
		subComponents = new Hashtable<String, Component>();
	}
	public void addChildComponent(Component child) {
		if(child.getId() != null){
			getChildren().add(child);
			subComponents.put(child.getId(),child);
		}
	}
}
