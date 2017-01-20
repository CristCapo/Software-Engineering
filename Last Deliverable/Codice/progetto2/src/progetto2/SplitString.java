package progetto2;

public class SplitString {
	
	public boolean intdec(String s){
		
		String regex = "\\d+\\.\\d+\\-\\d+\\.\\d+";
		
		if (s.matches(regex))
			return true;
		
		return false;
		
	}
	

	public int parteSinistraI(String s){
		
		int indicetratt = s.indexOf("-");
		
		String parteSinistra = s.substring(0, indicetratt);
		
		return Integer.parseInt(parteSinistra);
		
	}
	
	public int parteDestraI(String s){
		
		int indicetratt = s.indexOf("-");
		
		String parteDestra = s.substring(indicetratt+1, s.length());
		
		return Integer.parseInt(parteDestra);
		
	}
	

	
	public float parteSinistraF(String s){
		
		int indicetratt = s.indexOf("-");
		
		String parteSinistra = s.substring(0, indicetratt);
		
		return Float.parseFloat(parteSinistra);
		
	}
	
	public float parteDestraF(String s){
		
		int indicetratt = s.indexOf("-");
		
		String parteDestra = s.substring(indicetratt + 1, s.length());
		
		return Float.parseFloat(parteDestra);
		
	}

}

/*Questa classe serve per prendere i numeri dalle stringhe passate in input. Infatti, i dati presi dalle caselle di testo sono sempre stringhe, quindi bisogna convertirle.
 * Mediante l'utilizzo di una espressione regolare, il primo metodo restituisce true se la stringa contiene numeri decimali. Gli altri 4 metodi servono a prendere la parte
 * sinistra del trattino e la parte destra del trattino (le stringhe sono del tipo x-y, z.k-a.b)*/
