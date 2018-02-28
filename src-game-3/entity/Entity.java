package entity;

import characteristic.Updateable;
import util.IdMaker;

public class Entity{
	protected String id;
	
	public Entity(){
		this.id = IdMaker.next();
	}
	public String getId(){
		return id;
	}
}
