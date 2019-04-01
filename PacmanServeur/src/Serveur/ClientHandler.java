package Serveur;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import Command.AgentAction;
import Model.Maze;
import Model.PacmanGame;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;

	private PacmanGame pacmanGame;
	private Maze maze;

	public ClientHandler(Socket clientSocket, String pseudo) {
		// TODO Auto-generated constructor stub
		try {
			this.clientSocket = clientSocket;
			/* Return l'input stream du socket */
			this.in = new DataInputStream(this.clientSocket.getInputStream());
			/* Return l'output stream du socket */
			this.out = new DataOutputStream(this.clientSocket.getOutputStream());

			this.pacmanGame = new PacmanGame();
			this.pacmanGame.setPseudo(pseudo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Thread ecoute = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while(true) {
					try {
						/* Ecoute l'action effectue sur la commande */
						String action = in.readUTF();

						if(action.equals("RESTART")) {
							System.out.println("RESTART");

							/* Ecoute la map envoyer par l'utilisateur */
							String map = in.readUTF();

							try {
								//pacmanGame = new PacmanGame("layouts/" + map);
								pacmanGame.setMapName("layouts/" + map);
								pacmanGame.setLabyrinth(new Maze(pacmanGame.getMapName()));
								pacmanGame.init();

								ObjectOutputStream oos = new ObjectOutputStream(out);
								maze = pacmanGame.getLabyrinth();
								oos.writeObject(maze);	
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if(action.equals("START")) {
							System.out.println("START");
							pacmanGame.start();
							pacmanGame.launch();
						}else if(action.equals("STEP")) {
							System.out.println("STEP");
							pacmanGame.step();
							ObjectOutputStream oos = new ObjectOutputStream(out);
							maze = pacmanGame.getLabyrinth();
							oos.writeObject(maze);	
						}else if(action.equals("PAUSE")) {
							System.out.println("PAUSE");
							pacmanGame.stop();
						}else if(action.equals("EXIT")) {
							Serveur.quitterServeur();
							break;
						}else if(action.equals("GAUCHE")) {
							pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.WEST));
							//	this.pacmanGame.step();
						}else if(action.equals("BAS")) {
							pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.SOUTH));
							//this.pacmanGame.step();
						}else if(action.equals("DROITE")) {
							pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.EAST));
							//pacmanGame.step();
						}else if(action.equals("HAUT")) {
							pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.NORTH));
							//this.pacmanGame.step();
						}
					}catch(SocketException e) {
						System.out.println("Client d�connect�.");
						Serveur.quitterServeur();
						break;
					}catch (EOFException e) {
						// TODO: handle exception
						break;
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					} 
				}

			}});



		Thread envoi = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						synchronized (pacmanGame.getLock()) {
							pacmanGame.getLock().wait();

							ObjectOutputStream oos = new ObjectOutputStream(out);
							maze = pacmanGame.getLabyrinth();

							if(pacmanGame.isGhostScarred()) {
								maze.setScarred(true);
							}else{
								maze.setScarred(false);
							}

							oos.writeObject(maze);
						}
					}catch(SocketException e) {
						System.out.println("Client d�connect�.");
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}

			}});

		ecoute.start();
		envoi.start();
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}


