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

				ClientHandler ch = new ClientHandler(this.clientSocket, "");

				Thread t = new Thread(ch);
				t.start();

				handlers.add(ch);


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
