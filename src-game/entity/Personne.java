package entity;

public class Personne {
	private int age;
	private String nom;
	private String autoPréférée;
	
	public Personne(int age1, String nom1, String autoPréférée1) {
		age = age1;
		nom = nom1;
		autoPréférée = autoPréférée1;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getAutoPréférée() {
		return autoPréférée;
	}
	public void setAutoPréférée(String autoPréférée) {
		this.autoPréférée = autoPréférée;
	}
	
	public int getAge() {
		return age;
	}
	
	public void cestLaFeteDeLaPersonne() {
		age+=1;
	}
	
	//fonction main
//	public static void main(String[] args) {
//		Personne p1 = new Personne(18, "laurier", "rx8");
//		Personne p = new Personne(18, "nicolas", "corolla");
//		
//		System.out.println(p.getNom()+" "+p.getAge());
//		System.out.println(p1.getNom()+" "+p1.getAge());
//		
//		p.cestLaFeteDeLaPersonne();
//		
//		System.out.println(p.getNom()+" "+p.getAge());
//		System.out.println(p1.getNom()+" "+p1.getAge());
//	}
	
}
