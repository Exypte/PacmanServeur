package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Command.Agent;
import Command.PositionAgent;
import Controler.InterfaceControler;
import Model.Maze;
import Model.PacmanGame;
import patternStrategie.Joueur1;

public class ViewGame implements Observateur {

	private JFrame frame;
	private JPanel score;
	private JLabel turn, life, scoreNumber;
	private PacmanGame gameState;
	private PanelPacmanGame graphicPanel;
	private InterfaceControler controler;

	public ViewGame(PacmanGame gameState, InterfaceControler controler) {
		this.gameState = gameState;
		this.gameState.enregistrerObservateur(this);
		this.controler = controler;

		windowGenerator();
	}

	public void windowGenerator() {
		this.frame = new JFrame();
		this.frame.setTitle("Pacman");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(new Dimension(800, 800));
		this.frame.addKeyListener((KeyListener) gameState.getJoueur1());
		this.frame.addKeyListener((KeyListener) gameState.getJoueur2());

		GridLayout gridScore = new GridLayout(0, 3);
		this.score = new JPanel(gridScore);
		this.turn = new JLabel();
		this.life = new JLabel();
		this.scoreNumber = new JLabel();
		this.turn.setHorizontalAlignment(JLabel.CENTER);
		this.life.setHorizontalAlignment(JLabel.CENTER);
		this.scoreNumber.setHorizontalAlignment(JLabel.CENTER);
		this.score.add(turn);
		this.score.add(life);
		this.score.add(scoreNumber);
		
		// Cr�ation du panel affichant la map
		this.graphicPanel = new PanelPacmanGame(gameState.getLabyrinth());
		// La taille du panel est regl�e a 96% de la taille de la fenetre
		this.graphicPanel.setPreferredSize(new Dimension(this.frame.getWidth(), (int) (this.frame.getHeight() * 0.96)));
		// Ajout des panels au frame principale
		this.frame.add(this.score, BorderLayout.NORTH);
		this.frame.add(this.graphicPanel);
	}

	@Override
	public void actualiser(String text) {
		// System.out.println(text);

		/*ArrayList<PositionAgent> positionPacmans = new ArrayList<>();
		ArrayList<PositionAgent> positionGhosts = new ArrayList<>();
		for (Agent p : gameState.getPacmans()) {
			positionPacmans.add(p.getPosition());
		}
		for (Agent f : gameState.getFantomes()) {
			positionGhosts.add(f.getPosition());
		}*/
		this.turn.setText("Number of turns : " + this.gameState.getLapCount());
		this.life.setText("Life : " + this.gameState.getVies());
		this.scoreNumber.setText("Score number : " + this.gameState.getScores());
		
		this.graphicPanel.setM(this.gameState.getLabyrinth());
		
		if (this.gameState.isGhostScarred()) {
			this.graphicPanel.setGhostsScarred(true);
		} else {
			this.graphicPanel.setGhostsScarred(false);
		}

		/*graphicPanel.setPacmans_pos(positionPacmans);
		graphicPanel.setGhosts_pos(positionGhosts);*/

		this.graphicPanel.repaint();
	}

	public void showWindow() {
		this.frame.setVisible(true);
	}

	public void hideWindow() {
		this.frame.setVisible(false);
	}

	public void destroyWindow() {
		this.frame.dispose();
	}

	public JLabel getTurn() {
		return turn;
	}

	public JLabel getLife() {
		return life;
	}

	public JLabel getScoreNumber() {
		return scoreNumber;
	}

	public void setTurn(JLabel turn) {
		this.turn = turn;
	}

	public void setLife(JLabel life) {
		this.life = life;
	}

	public void setScoreNumber(JLabel scoreNumber) {
		this.scoreNumber = scoreNumber;
	}
}