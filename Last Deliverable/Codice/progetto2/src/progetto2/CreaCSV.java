package progetto2;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;




public class CreaCSV {
	
	
	
	public TreeMap<Integer,LinkedList<Integer>> csvNodi(String IDAlbero, int splitsize, int depth, LinkedList<String> attrNode,LinkedList<String> rangeNodi) throws IOException{
		
		String file="C:\\Users\\Cristian\\Documents\\Neo4j\\default.graphdb\\Import\\nodiCsv" + IDAlbero +".csv";
		
		FileWriter writer = new FileWriter(file);	
		
		String a="IDAlbero" + "," + "ID,Vertex";
		
		TreeMap<String,String> attributi = new TreeMap<String,String>();
		
		int temporaneo = 0;
		
		for(String nome : attrNode){
			attributi.put(nome, rangeNodi.get(temporaneo));
			temporaneo++;
		}
		
		
		for(String b: attrNode){			
			
			a+= "," + b;					
			
		}
		
		a+='\n';
		
		writer.write(a);
		
		TreeMap<Integer,LinkedList<Integer>> nodLiv = new TreeMap<Integer,LinkedList<Integer>>();
		
		TreeMap<Integer,LinkedList<Integer>> nodPad = new TreeMap<Integer,LinkedList<Integer>>();
		
		LinkedList<Integer> nodi = new LinkedList<Integer>();
		
		a=IDAlbero + "," + 0 + ",Vertex0";
		
		
		for (String b: attributi.keySet()){
			
			boolean decimale = new SplitString().intdec(attributi.get(b));  //true se decimale
			
			if(decimale){
				
				float sx = new SplitString().parteSinistraF(attributi.get(b));
				float dx = new SplitString().parteDestraF(attributi.get(b));
				
				a+="," + new RandomGeneration().floatRandomGeneration(sx, dx);
				
			}
			
			else{
				
				int sx = new SplitString().parteSinistraI(attributi.get(b));
				int dx = new SplitString().parteDestraI(attributi.get(b));
				
				a+="," + new RandomGeneration().intRandomGeneration(sx, dx);
				
			}
			
		}
			

		a+="\n";
		writer.write(a);
		nodi.add(0);
		
		nodLiv.put(0, nodi);
		
		for(int i=1;i<depth;i++){
			
			LinkedList<Integer> tmp = new LinkedList<Integer>();
			
			
			
			for(int x: nodLiv.get(i-1)){
				
				LinkedList<Integer> padri = new LinkedList<Integer>();
				
				for(int j=1;j<=splitsize;j++){
					
					a="";
					int padre=(x*splitsize)+j;
					
					a+=IDAlbero + "," + padre+",Vertex"+padre;
					
					
					for (String b: attributi.keySet()){
						
						boolean decimale = new SplitString().intdec(attributi.get(b));  //true se decimale
						
						if(decimale){
							
							float sx = new SplitString().parteSinistraF(attributi.get(b));
							float dx = new SplitString().parteDestraF(attributi.get(b));
							
							a+="," + new RandomGeneration().floatRandomGeneration(sx, dx);
							
						}
						
						else{
							
							int sx = new SplitString().parteSinistraI(attributi.get(b));
							int dx = new SplitString().parteDestraI(attributi.get(b));
							
							a+="," + new RandomGeneration().intRandomGeneration(sx, dx);
							
						}
						
					}
					
					
					
					
				
					
					a+='\n';
					
					writer.write(a);
					
					padri.add(padre);
					
					tmp.add(padre);
					
				}
				nodPad.put(x, padri);
				
			}
			
			nodLiv.put(i,tmp);
			
		
		
		}
		
		
		
		writer.flush();
		writer.close();
		
		return nodPad;
	}
	
	public void csvArchi(String IdAlbero, TreeMap<Integer,LinkedList<Integer>> nodi, LinkedList<String> attrEdge, LinkedList<String> rangeArchi) throws IOException{

		String dir="C:\\Users\\Cristian\\Documents\\Neo4j\\default.graphdb\\Import\\archiCsv" + IdAlbero +".csv";
		
		FileWriter writer=new FileWriter(dir);
		
		//LinkedList<String> c=attrEdge;
		
		String a="IDAlbero" + "," + "ID,StartVertex,EndVertex";
		
		for(String b: attrEdge){
			
			a+= "," + b;
			
		}
		
		a+= "\n";
		writer.write(a);
		
		
		for(int figlio: nodi.keySet()){
			
			
			
			for(int padre: nodi.get(figlio)){
				a="";
				a=IdAlbero + "," + padre + "," + padre + "," + figlio;
				
				
				int temporaneo = 0;
				
				for(String b: attrEdge){
					
					String range = rangeArchi.get(temporaneo);
					
					boolean decimale = new SplitString().intdec(range);
					
					if(decimale){
						
						float sx = new SplitString().parteSinistraF(range);
						float dx = new SplitString().parteDestraF(range);
						
						a+= "," + new RandomGeneration().floatRandomGeneration(sx, dx);
						
					}
					
					else{
						
						int sx = new SplitString().parteSinistraI(range);
						int dx = new SplitString().parteDestraI(range);
						
						a+= "," + new RandomGeneration().intRandomGeneration(sx, dx);
						
					}
					
					temporaneo++;
					
				}
				
				a+="\n";
				writer.write(a);
				
			}
			
			
			
		}
			
		writer.flush();
		writer.close();
		return;
	}
		
}

/*Il metodo csvNodi si occuperà della creazione del file nodicsvidalbero.csv relativo ai nodi. Per semplicità si utilizzano dei dizionari per distinguere
 * i nodi ad un certo livello, e per capire quali sono i padri di un certo nodo*/

/*Il metodo csvArchi si occuperà della creazione del file archicsvidalbero.csv relativo agli archi. Mediante il dizionario creato dal primo metodo, 
 * si crea un file csv nel quale la prima riga è occupata dagli headers (idalbero,id,startvertex,endvertex,attributi) mentre le altre righe saranno occupate
 * dai valori corrispondenti*/
