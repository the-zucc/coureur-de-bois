package entity;

import util.IdMaker;

public abstract class Entity {
	protected String id;
	
	public Entity(){
		this.id = IdMaker.next();
	}
	public String getId(){
		return id;
	}

}
