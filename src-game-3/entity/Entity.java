package entity;

import java.util.Hashtable;

import characteristic.Messageable;
import characteristic.Updateable;
import util.IdMaker;

public abstract class Entity implements Messageable{
	protected String id;
	
	public Entity(){
		this.id = IdMaker.next();
	}
	public String getId(){
		return id;
	}
	
	@Override
	public abstract void onMessageReceived(Hashtable<String, ? extends Object> message);
}
