import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Serveur {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private ArrayList<ClientHandler> handlers;

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

				ClientHandler ch = new ClientHandler(this.clientSocket);
				
				Thread t = new Thread(ch);
				t.start();

				handlers.add(ch);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	public static synchronized void broadcast(Socket so, String msg, String nom) {
		
	}
}
