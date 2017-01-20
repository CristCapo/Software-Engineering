package progetto2;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.Session;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet("/FirstServlet")
public class FirsteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FirsteServlet() {
		super();
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		int n=0;
		int m=0;
		int o=0;
		int k=0;
		int i=0;
		int p=0; 
		int q=0; 
		String[] elimina;
		String[] calcola;
		Enumeration<String> e = request.getParameterNames();
		Enumeration<String> e1 = request.getParameterNames();
		Enumeration<String> e2 = request.getParameterNames();
		String[] parametri_fissi = new String[5];
		LinkedList<String> parametri_nodi = new LinkedList<String>();
		LinkedList<String> parametri_archi = new LinkedList<String>();
		LinkedList<String> parametri_nodi_val = new LinkedList<String>();   
		LinkedList<String> parametri_archi_val = new LinkedList<String>();   



		while (e1.hasMoreElements()){
			e1.nextElement();
			i++;

		}




		if (i==1) {
			
			elimina = new String[i];
			elimina[0]=request.getParameter("inserisci");
			Session session = new Connessione().connettiti();

			if(new Neo4j().verificaID(elimina[0], session)){
				new Neo4j().deleteTree(elimina[0], session);
				StatementResult result = session.run("RETURN 1 as prova");
				Record rec = result.next();
				this.stampa(request, response, rec, 0, "EliminazioneOK", elimina[0]);
				
			}

			else{

				StatementResult result = session.run("RETURN 1 as prova");
				Record rec = result.next();
				this.stampa(request, response, rec, 0, "ErroreID", elimina[0]);
			}
			session.close();
		}

		if (i==3) {

			
			calcola = new String[i];
			while(e2.hasMoreElements())

			{
				String name = (String) e2.nextElement();
				String value = request.getParameter(name);
				calcola[k] = value;
				k++;
			}

			Session session = new Connessione().connettiti();

			if(new Neo4j().verificaID(calcola[0], session))

				if(new Neo4j().verificaVertex(calcola[0], Integer.parseInt(calcola[1]), Integer.parseInt(calcola[2]), session)){

					session.run("MATCH (N:Vertex {IDAlbero: '" + calcola[0] + "', ID: '0'}) set N.flag = N.flag + 1");
					long time_start = System.currentTimeMillis(); 
					Record rec = new Neo4j().res(session,Integer.parseInt(calcola[1]),Integer.parseInt(calcola[2]), calcola[0]);
					long time_finish = System.currentTimeMillis();
					long tempo = (time_finish - time_start)/1000;
					
					this.stampa(request, response, rec, tempo,"CalcoloOK",calcola[0]);
					session.run("MATCH (N:Vertex {IDAlbero: '" + calcola[0] + "', ID: '0'}) set N.flag = N.flag - 1");
				}

				else{
					StatementResult result = session.run("RETURN 1 as prova");
					Record rec = result.next();
					this.stampa(request, response, rec, 0, "ErroreVertex", calcola[0]);
				}

			else{

				StatementResult result = session.run("RETURN 1 as prova");
				Record rec = result.next();
				this.stampa(request, response, rec, 0, "ErroreID", calcola[0]);



			}
			
			session.close();
		}

		if (i >3) {

			

			Session session = new Connessione().connettiti();

			if (new Neo4j().verificaID(parametri_fissi[0], session)){

				StatementResult result = session.run("RETURN 1 as prova");
				Record rec = result.next();
				this.stampa(request, response, rec, 0, "ErroreCreazione", parametri_fissi[0]);

			}

			else{

				while (e.hasMoreElements()){

					String name = (String) e.nextElement();
					String value = request.getParameter(name);

					if (name.equals("parametroNodi"+n)){
						parametri_nodi.add(value);
						n++;

					}
					else{

						if (name.equals("parametroArchi"+m)){
							parametri_archi.add(value);
							m++;
						}

						else{

							if(name.equals("valoreNodi"+p)){
								parametri_nodi_val.add(value);
								p++;
							}

							else{
								if(name.equals("valoreArchi"+q)){
									parametri_archi_val.add(value);
									q++;
								}
								else {
									parametri_fissi[o]=value;
									o++;

								}

							}

						}


					}		


				}


				new Albero(Integer.parseInt(parametri_fissi[2]),Integer.parseInt(parametri_fissi[1]),parametri_fissi[0],parametri_nodi,parametri_archi,parametri_nodi_val,parametri_archi_val);
				StatementResult r = session.run("MATCH (N:Vertex {IDAlbero: '" + parametri_fissi[0] + "', ID: '0'}) SET N.flag = 0 return 1 as prova");
				Record rec = r.next();
				this.stampa(request, response, rec, 0, "CreazioneOK", parametri_fissi[0]);
			}

		}

	}

	public void stampa(HttpServletRequest request, HttpServletResponse response, Record record, long tempo,String comando, String idalbero) throws ServletException, IOException {

		String vertici = "<h3>" + "lista vertici" + " = " ;

		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");

		switch(comando){

		case "ErroreCreazione":
			out.println("<title>Errore</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+ "L'albero " + idalbero + " già esiste. Creazione fallita" + "</h1>");
			out.println("</body>");
			out.println("</html>");
			break;

		case "ErroreID":
			out.println("<title>Errore</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+ "L'albero " + idalbero + " non esiste." + "</h1>");
			out.println("</body>");
			out.println("</html>");
			break;

		case "ErroreVertex":
			out.println("<title>Errore</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+ "Almeno uno dei due vertici non esiste" + "</h1>");
			out.println("</body>");
			out.println("</html>");
			break;

		case "CreazioneOK":
			out.println("<title>Creazione avvenuta con successo</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+ "L'albero " + idalbero + " è stato creato con successo." + "</h1>");
			out.println("</body>");
			out.println("</html>");
			break;

		case "EliminazioneOK":
			out.println("<title>Cancellazione</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+ "L'albero " + idalbero + " è stato cancellato con successo." + "</h1>");
			out.println("</body>");
			out.println("</html>");
			break;

		case "CalcoloOK":
			out.println("<title>Risultati</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+ "Risultati operazioni" + "</h1>");
			for(String key : record.keys())
			{
				if(key == "Vertici")
				{
					vertici += record.get("Vertici") + " " ;
				}

				else 
				{
					out.println("<h3>"+ key + "= " + record.get(key) + "</h3>");
				}
			}

			vertici += "</h3>";

			out.println("<h3>"+ "tempo totale" + " = " + tempo +"</h3>");
			out.println("<a href=javascript:history.go(-1)>"+"Clicca qui per tornare alla pagina precedente "+"</a>");
			out.println("</body>");
			out.println("</html>");
			break;
		}

		out.close();
	}

}
/*Questa classe rappresenta l'interfaccia tra la parte web. Prende i dati dalle caselle input del sito web e le passa ad una lista. In base al numero degli elementi passati
viene effettuata una determinata operazione (con un solo elemento viene effettuata la delete, con tre la somma, con più di tre la creazione).
In base all'operazione, viene poi chiamato il metodo stampa che sceglie cosa stampare a schermo."*/
