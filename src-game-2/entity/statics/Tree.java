package entity.statics;

import javafx.geometry.Point3D;
import visual.Component;
import visual.TreeComponent;

public class Tree extends StaticEntity {
	
	public Tree(Point3D position) {
		super(position);
	}

	@Override
	public Component buildComponent() {
		return new TreeComponent(this);
	}
}
