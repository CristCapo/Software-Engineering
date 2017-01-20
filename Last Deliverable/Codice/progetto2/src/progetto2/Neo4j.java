package progetto2;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.neo4j.driver.v1.*;

public class Neo4j {

	public void createNodi(Session session,String IDAlbero) throws FileNotFoundException, IOException{
		
		String file="C:\\Users\\Cristian\\Documents\\Neo4j\\default.graphdb\\Import\\nodiCsv" +IDAlbero +".csv";
		
		FileReader reader = new FileReader(file);
		
		BufferedReader br = new BufferedReader(reader);
		
		String split =",";
		
		String line = br.readLine();

		String[] res = line.split(split);
		
		List<String> x = Arrays.asList(res);
		
		Iterator<String> it = x.iterator();
		
		String a = it.next();
		
		String b = "CREATE (:Vertex {" + a +":" + " toString(line." + a +")";
		
		while(it.hasNext()){
			String c = it.next();
			
			if (c.equals("ID"))
				b+="," + c + ":" + " line." + c ;
			else
				b+="," + c + ":" + " toFloat(line." + c + ")";
		}
		
		b+="}" + ")";
		
		
		
		StatementResult result = session.run( "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM 'file:///nodiCsv" + IDAlbero + ".csv' as line "
				+ b + " RETURN 1 as res");
		
		
		Record p = result.next();
		Value k = p.get("res");
		while (k.asInt() != 1){
			
		}
		br.close();
		
		
		
		
	}
	
	public void createArchi(Session session,String IDAlbero) throws IOException{
		
		String file="C:\\Users\\Cristian\\Documents\\Neo4j\\default.graphdb\\Import\\archiCsv" + IDAlbero+".csv";
		
		FileReader reader = new FileReader(file);
		
		BufferedReader br = new BufferedReader(reader);
		
		String split =",";
		
		String line = br.readLine();

		String[] res = line.split(split);
		
		List<String> x = Arrays.asList(res);
		
		
		
		
		
		String a = x.get(0);
		
		String b = " CREATE (N) - [:Padre {" + a +": " + "toString(line." + a +")";
		
		
		
		String d = "";
		
		for(int i=1 ; i<x.size();i++){
			
			String c = x.get(i);
			
			if (c.equals("StartVertex")){
				
				i++;
				c=x.get(i);
			}
			
			
			if(c.equals("EndVertex")){
				
				i++;
				d=c;
				c=x.get(i);
			}
			
			if(c.equals("ID"))
				b+=", " + c +":" + " line." + c;
			else
				b+=", " + c +":" + " toFloat(line." + c + ")" ;
			
		}
		
		b += "}]";
		
		
		String z = "USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM 'file:///archiCsv" + IDAlbero + ".csv' as line "
				+ "MATCH (N:Vertex {IDAlbero: toString(line.IDAlbero), ID: line.StartVertex}), (M:Vertex {IDAlbero: toString(line.IDAlbero) , ID: line.EndVertex}) "
				+ b + " ->" + " (M) RETURN 1 AS res";
		
		
		
		StatementResult result = session.run(z);
		Record p = result.next();
		Value k = p.get("res");
		while (k.asInt() != 1){
			
		}
		br.close();
		
	}
	
	public Record res(Session session,int x,int y,String IDAlbero){
		
		
		
		
		
		String attrnode = this.takeattrnode(session,IDAlbero);
		String attredge = this.takeattredge(session,IDAlbero);
		
		StatementResult resnode = session.run("MATCH path = (N:Vertex {IDAlbero: '" + IDAlbero + "', ID: '" + x +"' }) - [*] -> (M:Vertex {IDAlbero: '" + IDAlbero + "' ,ID: '" + y +"'}) RETURN " 
				+ attredge   
				+ attrnode + " ,EXTRACT (p in NODES(path) | p.ID) as Vertici");
		
		Record record = resnode.next();
		
		
		return record;
	}
	
	public String takeattrnode(Session session,String IDAlbero){
		
		StatementResult resnode = session.run("MATCH (N:Vertex {IDAlbero: '" + IDAlbero + "', ID: '0'}) RETURN PROPERTIES(N)");
		Record record = resnode.next();
		String attrnode = "";
		
		for(String a : record.get("PROPERTIES(N)").keys()){
			
			if( a.equals("ID") || a.equals("IDAlbero") || a.equals("flag")){
				
				continue;
				
			}
			
			attrnode +=" ,reduce(total = 0, N in nodes(path) | total + ";
			attrnode += "N." + a + ") as Node_" + a;
			
		}

		
		return attrnode;
		
	}
	
	public String takeattredge(Session session,String IDAlbero){
		
		StatementResult resedge = session.run("MATCH () - [R:Padre {IDAlbero: '" + IDAlbero + "', ID: '1'}] -> () RETURN PROPERTIES(R)");
		Record record = resedge.next();
		String attredge = "";
		boolean primo = true;
		for(String a : record.get("PROPERTIES(R)").keys()){
			
			if( a.equals("ID") || a.equals("IDAlbero")){
				
				continue;
				
			}
			if (primo)
				primo = false;
				
			
			
			else
				attredge += ",";
			
			
			attredge += " reduce( total = 0, R in relationships(path) | total + ";
			attredge += " R." + a + " ) as Edge_" + a;

		}
		
		
		return attredge;
		
	}
	
	public void deleteTree(String idAlbero, Session session){
		
		
		
		StatementResult wait = session.run("MATCH (N:Vertex {IDAlbero: '" + idAlbero + "', ID: '0' }) WHERE (N.flag = 0) RETURN N.flag AS bool");
		
		while(!wait.hasNext()){

			wait = session.run("MATCH (N:Vertex {IDAlbero: '" + idAlbero + "', ID: '0' }) WHERE (N.flag = 0) RETURN N.flag AS bool");
			
		}
		
		
		StatementResult del = session.run("MATCH (N:Vertex {IDAlbero: '" + idAlbero + "'}) WITH N LIMIT 1000 DETACH DELETE N RETURN 1 as res");
		if(del.hasNext()){
		
		Record p = del.next();
		Value k = p.get("res");
		while (k.asInt() == 1){
			del = session.run("MATCH (N:Vertex {IDAlbero: '" + idAlbero + "'}) WITH N LIMIT 1000 DETACH DELETE N RETURN 1 as res");
			if (!del.hasNext())
				break;
			k = p.get("res");
		}
	}
		
	}
	
	
	public boolean verificaID(String IDAlbero, Session session){
		
		StatementResult bool = session.run("MATCH (N:Vertex {IDAlbero: '" + IDAlbero + "'}) RETURN 1 LIMIT 1");
		
		if(bool.hasNext())
			
			return true;
		
		else
			
			return false;
		
		
	}
	
	public boolean verificaVertex(String IDAlbero, int x, int y, Session session){
		
		StatementResult bool = session.run("MATCH (N:Vertex {IDAlbero: '" + IDAlbero + "', ID: '" +  x + "'}) RETURN 1 ");
		StatementResult bool_2 = session.run("MATCH (N:Vertex {IDAlbero: '" + IDAlbero + "', ID: '" +  y + "'}) RETURN 1 ");
		
		if( !bool.hasNext() || !bool_2.hasNext())
			
			return false;
		
		else
			
			return true;
		
		
	}
	

}
/*Questa classe si occupa di tutte le operazioni da effettuare sul database. I primi due metodi utilizzano delle query CYPHER (create mediante concatenazione di stringhe)
* per importare i dati presi dai file csv sul database. Il metodo res utilizza una query cypher per calcolare la somma del percorso. Per creare la query, richiama due i 
* metodi takeattrnode e takeattredge, che restituiscono stringhe relative agli attributi dei nodi e degli archi. Una volta concatenate le stringhe, viene calcolato il 
* percorso nonchè la somma. Il metodo deleteTree utilizza una query cypher per cancellare un albero dato il suo id. Poiché la cancellazione deve essere messa in attesa,
* il metodo verifica il valore dell'attributo flag del nodo avente id zero (ovviamente dell'albero da eliminare). Finché questo valore è diverso da zero, la cancellazione
* viene messa in attesa mediante un ciclo infinito. Non appena la flag torna a zero, l'albero viene cancellato. L'albero viene eliminando cancellando 1000 nodi 
* (e i relativi archi) per volta in modo da non influire negativamente sulle prestazioni. Gli ultimi due metodi verificano l'esistenza dell'albero e dei vertici 
* (sempre mediante query cypher)*/


