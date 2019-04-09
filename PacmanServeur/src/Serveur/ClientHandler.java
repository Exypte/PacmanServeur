package Serveur;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Command.AgentAction;
import Model.Maze;
import Model.PacmanGame;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private DataInputStream in;
	private DataOutputStream out;

	private PacmanGame pacmanGame;
	private Maze maze;
	private String pseudo;
	private boolean connectionOk;
	private Connection connexion = null;
	private Statement statement = null;
	private ResultSet resultat = null;

	int maxConnexion=5;
	public ClientHandler(Socket clientSocket, String pseudo) {
		// TODO Auto-generated constructor stub
		try {
			this.clientSocket = clientSocket;
			/* Return l'input stream du socket */
			in = new DataInputStream(clientSocket.getInputStream());
			/* Return l'output stream du socket */
			out = new DataOutputStream(clientSocket.getOutputStream());

			pacmanGame = new PacmanGame();
			this.pseudo=pseudo;
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

				connectionOk=false;
				int cptToMax=0;

				while(!connectionOk && cptToMax < maxConnexion) {

					try {
						out.writeUTF("Login : ");
						String login = in.readUTF();
						System.out.println(login);
						out.writeUTF("Mot de passe : ");
						String mdp = in.readUTF();
						System.out.println(mdp);



						/* Connexion Ã  la base de donnÃ©es */
						String url = "jdbc:mysql://localhost:3306/jee_pacman?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
						String utilisateur = "root";
						String motDePasse = "root";
						String requete = "SELECT * FROM joueurs WHERE pseudo='"+login+"' AND password='"+mdp+"';";

						connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
						statement = connexion.createStatement();
						resultat = statement.executeQuery(requete);

						if(resultat.next()) {
							connectionOk = true;
							pseudo=login;
							pacmanGame.setPseudo(pseudo);
						}else {
							connectionOk = false;
						}

						out.writeBoolean(connectionOk);

					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch ( SQLException e ) {
						/* GÃ©rer les Ã©ventuelles erreurs ici */
						System.out.println(e.getMessage());
					}
					cptToMax++;
				}
				// TODO Auto-generated method stub
				if(!connectionOk)
					try {
						out.writeUTF("5 tentatives échouées... Vous etes déconnecté du serveur.");
						clientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("Connexion fermée");
					}
				else
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

									maze = pacmanGame.getLabyrinth();

									Gson gson = new Gson();
									String s = gson.toJson(maze);

									out.writeUTF(s);	
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
								//	pacmanGame.step();
							}else if(action.equals("BAS")) {
								pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.SOUTH));
								//pacmanGame.step();
							}else if(action.equals("DROITE")) {
								pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.EAST));
								//pacmanGame.step();
							}else if(action.equals("HAUT")) {
								pacmanGame.getJoueur1().setAgentAction(new AgentAction(Maze.NORTH));
								//pacmanGame.step();
							}
						}catch(SocketException e) {
							System.out.println("Client dï¿½connectï¿½.");
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

							//ObjectOutputStream oos = new ObjectOutputStream(out);
							maze = pacmanGame.getLabyrinth();

							if(pacmanGame.isGhostScarred()) {
								maze.setScarred(true);
							}else{
								maze.setScarred(false);
							}


							Gson gson = new Gson();
							String s = gson.toJson(maze);

							out.writeUTF(s);
						}
					}catch(SocketException e) {
						System.out.println("Client dï¿½connectï¿½.");
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


