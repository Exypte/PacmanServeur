package Controler;

import javax.swing.JOptionPane;

import Model.Maze;
import Model.PacmanGame;
import View.ViewGame;

public class ControlerCommands implements InterfaceControler {

	private PacmanGame gameState;
	private ControlerGame controlerGame;
	private ViewGame viewGame;

	public ControlerCommands(PacmanGame gameState) {
		this.gameState = gameState;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		this.gameState.start();
		this.gameState.launch();
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub;
		
		if(viewGame != null) {
			this.viewGame.destroyWindow();
		}
		
		try {
			this.gameState.setLabyrinth(new Maze(this.gameState.getMapName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	    
		String[] joueurs = {"Un joueur", "Deux joueurs"};
	    
	    String joueur = (String)JOptionPane.showInputDialog(null, 
	      "Veuillez indiquer le nombre de joueur !",
	      "Parametre du jeu !",
	      JOptionPane.QUESTION_MESSAGE,
	      null,
	      joueurs,
	      joueurs[0]);
	    
	    if(joueur == "Un joueur") {
	    	this.gameState.setJ1(true);
	    	this.gameState.setJ2(false);
	    }else if(joueur == "Deux joueurs") {
	    	this.gameState.setJ1(true);
	    	this.gameState.setJ2(true);
	    }
		
		this.controlerGame = new ControlerGame(gameState);
		this.viewGame = new ViewGame(gameState, controlerGame);
		this.viewGame.showWindow();
		
		this.gameState.stop();
		this.gameState.init();
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		this.gameState.step();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		this.gameState.stop();
	}

}
