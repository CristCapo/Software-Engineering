package progetto2;
import java.io.File;
import java.io.IOException;


import org.neo4j.driver.v1.*;

public class Connessione {

	public Session connettiti() throws IOException{
		
		
		
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic("neo4j", "abc"), Config.build()
		        .withEncryptionLevel( Config.EncryptionLevel.REQUIRED )
		        .withTrustStrategy( Config.TrustStrategy.trustOnFirstUse( new File( "/path/to/neo4j_known_hosts" ) ) )
		        .toConfig() );
		
		return driver.session();
		
	}
	
	
}


/* Connessione al database mediante protocollo "bolt" (protocollo + veloce del protocollo HTML), credenziali e cifratura. Viene quindi restituita
la sessione relativa a questa connessione. N.B. OGNI SESSIONE CREATA DALLE VARIE ISTANZE RISULTA ESSERE INDIPENDENTE DALLE ALTRE. QUESTO ASSICURA
L'ESECUZIONE PARALLELA DA PARTE DI TANTI UTENTI SENZA PROBLEMI */