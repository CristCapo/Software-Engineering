package progetto2;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

import org.neo4j.driver.v1.Session;

public class Albero {

	public Albero(int depth,int SplitSize, String idAlbero, LinkedList<String> attrNode, LinkedList<String> attrEdge,LinkedList<String> rangeNodi, LinkedList<String> rangeArchi) throws IOException{

		TreeMap<Integer,LinkedList<Integer>> nodi = new CreaCSV().csvNodi(idAlbero, SplitSize, depth, attrNode, rangeNodi); //Crea un dizionario di nodi del tipo: (figlio,<listadipadri>) dall'invocazione del metodo 
		new CreaCSV().csvArchi(idAlbero, nodi, attrEdge, rangeArchi);
		Session sess = new Connessione().connettiti();  //Inizio di una sessione in neo4j
		Neo4j create = new Neo4j();						//Istanza di Neo4j
		create.createNodi(sess, idAlbero);			//invocazione del metodo createNodi che porterà all'import dei nodi nel database dal file csv
		create.createArchi(sess, idAlbero);		//invocazione del metodo createArchi che porterà alla creazione delle relazioni nel database
		sess.close();									//chiusura della sessione corrente
	
	}
	
	
}
/*Questa classe creerà l'albero scrivendo sui file CSV (mediante le chiamate alla classe CreaCSV).*/