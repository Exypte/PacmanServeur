package Serveur;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Serveur {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	public static ArrayList<ClientHandler> handlers;
	boolean connectionOk;
	Connection connexion = null;
	Statement statement = null;
	ResultSet resultat = null;

	public Serveur() {
		try {
			/* Creer un serveur socket avec le port specifie */
			this.serverSocket = new ServerSocket(6000);
			System.out.println("Serveur mis en place.");
			this.handlers = new ArrayList<>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void jeu() {
		System.out.println("Attente de clients pour le jeu.");

		while(true) {
			try {
				this.clientSocket = this.serverSocket.accept();
				System.out.println("Connection accept !");

				/* Return l'input stream du socket */
				this.in = new DataInputStream(this.clientSocket.getInputStream());
				/* Return l'output stream du socket */
				this.out = new DataOutputStream(this.clientSocket.getOutputStream());

				this.out.writeUTF("Login : ");
				String login = this.in.readUTF();
				System.out.println(login);
				this.out.writeUTF("Mot de passe : ");
				String mdp = this.in.readUTF();
				System.out.println(mdp);

				/* Connexion à la base de données */
				String url = "jdbc:mysql://localhost:3306/jee_pacman?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
				String utilisateur = "root";
				String motDePasse = "root";
				String requete = "SELECT * FROM joueurs WHERE pseudo='"+login+"' AND password='"+mdp+"';";
				try {
					connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
					statement = connexion.createStatement();
					resultat = statement.executeQuery(requete);
					if(resultat.next()) {
						connectionOk = true;
					}else {
						connectionOk = false;
					}
				} catch ( SQLException e ) {
					/* Gérer les éventuelles erreurs ici */
					System.out.println(e.getMessage());
				} finally {
					if ( resultat != null ) {
						try {
							/* On commence par fermer le ResultSet */
							resultat.close();
						} catch ( SQLException ignore ) {
						}
					}
					if ( statement != null ) {
						try {
							/* Puis on ferme le Statement */
							statement.close();
						} catch ( SQLException ignore ) {
						}
					}
					if ( connexion != null ) {
						try {
							/* Et enfin on ferme la connexion */
							connexion.close();
						} catch ( SQLException ignore ) {
						}
					}
				}

				if(connectionOk) {
					this.out.writeBoolean(connectionOk);

					ClientHandler ch = new ClientHandler(this.clientSocket, login);

					Thread t = new Thread(ch);
					t.start();

					handlers.add(ch);
				}else {
					this.out.writeBoolean(connectionOk);
					this.clientSocket.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	public static void quitterServeur() {
		for(ClientHandler ch : handlers) {
			if(ch.getClientSocket() == null) {
				handlers.remove(ch);
			}
		}
	}
}
