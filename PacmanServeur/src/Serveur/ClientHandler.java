package Serveur;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Command.AgentAction;
import Model.Maze;
import Model.PacmanGame;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;
	
	private PacmanGame pacmanGame;
	private Maze maze;

	public ClientHandler(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		try {
			this.clientSocket = clientSocket;
			/* Return l'input stream du socket */
			this.in = new DataInputStream(this.clientSocket.getInputStream());
			/* Return l'output stream du socket */
			this.out = new DataOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				/* Ecoute l'action effectue sur la commande */
				String action = this.in.readUTF();
				
				if(action.equals("INIT")) {
					System.out.println("INIT");
					
					/* Ecoute la map envoyer par l'utilisateur */
					String map = this.in.readUTF();
					
					try {
						this.pacmanGame = new PacmanGame(map);
						pacmanGame.setLabyrinth(new Maze(pacmanGame.getMapName()));
						pacmanGame.init();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(action.equals("START")) {
					System.out.println("START");
					this.pacmanGame.start();
					this.pacmanGame.launch();
				}else if(action.equals("STEP")) {
					System.out.println("STEP");
					this.pacmanGame.step();
				}else if(action.equals("PAUSE")) {
					System.out.println("PAUSE");
					this.pacmanGame.stop();
				}else if(action.equals("EXIT")) {
					Serveur.quitterServeur(clientSocket);
				}else if(action.equals("GAUCHE")) {
					this.pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.WEST));
					this.pacmanGame.step();
				}else if(action.equals("BAS")) {
					this.pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.SOUTH));
					this.pacmanGame.step();
				}else if(action.equals("DROITE")) {
					this.pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.EAST));
					this.pacmanGame.step();
				}else if(action.equals("HAUTE")) {
					this.pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.NORTH));
					this.pacmanGame.step();
				}

				ObjectOutputStream oos = new ObjectOutputStream(out);
				
				this.maze = this.pacmanGame.getLabyrinth();
				oos.writeObject(this.maze);
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}
