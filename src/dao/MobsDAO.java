package dao;

public class MobsDAO {
	private static MobsDAO instance;
	
	private MobsDAO(){
		
	}
	public MobsDAO getInstance(){
		if(instance == null){
			instance = new MobsDAO(); 
		}
		return instance;
	}
}
