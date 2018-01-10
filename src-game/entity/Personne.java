package entity;

public class Personne {
	private int age;
	private String nom;
	private String autoPr�f�r�e;
	
	public Personne(int age1, String nom1, String autoPr�f�r�e1) {
		age = age1;
		nom = nom1;
		autoPr�f�r�e = autoPr�f�r�e1;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getAutoPr�f�r�e() {
		return autoPr�f�r�e;
	}
	public void setAutoPr�f�r�e(String autoPr�f�r�e) {
		this.autoPr�f�r�e = autoPr�f�r�e;
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
