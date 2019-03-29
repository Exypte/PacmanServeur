package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JOptionPane;

import Command.Agent;
import Command.AgentAction;
import Command.AgentType;
import Command.PositionAgent;
import patternStrategie.Aleatoire;
import patternStrategie.FuiteFantome;
import patternStrategie.Joueur1;
import patternStrategie.Joueur2;
import patternStrategie.PoursuitePacman;
import patternStrategie.Strategies;

public class PacmanGame extends Game {

	private Maze labyrinth;

	private Joueur1 joueur1;
	private Joueur2 joueur2;
	private boolean j1 = true;
	private boolean j2 = false;

	private List<Agent> fantomes;
	private List<Agent> pacmans;
	private List<PositionAgent> positionPacmanSave;

	private int scores;
	private int vies;
	private boolean pacmanWin = true;
	private boolean ghostScarred = false;
	private int turnScarred = 0;
	
	private String mapName;

	public PacmanGame() {
		this.fantomes = new ArrayList<Agent>();
		this.pacmans = new ArrayList<Agent>();
		this.positionPacmanSave = new ArrayList<>();

		this.joueur1 = new Joueur1();
		this.joueur2 = new Joueur2();
	}
	
	public PacmanGame(String mapName) throws Exception {

		this.mapName = mapName;
		//this.labyrinth = new Maze(mapName);

		this.fantomes = new ArrayList<Agent>();
		this.pacmans = new ArrayList<Agent>();
		this.positionPacmanSave = new ArrayList<>();

		this.joueur1 = new Joueur1();
		this.joueur2 = new Joueur2();
	}
	
	private void jouerBruitage(String filename) {
		try {
	        AudioInputStream audioIn;
			audioIn = AudioSystem.getAudioInputStream(new File(filename));
	        Clip clip = AudioSystem.getClip();
	        clip.stop();
	        clip.setFramePosition(0);
	        clip.open(audioIn);
	        clip.start();
		} catch (Exception e) {
			
		}
	}

	@Override
	public void initializeGame() {
		// TODO Auto-generated method stub

		this.fantomes.clear();
		this.pacmans.clear();
		this.positionPacmanSave.clear();
		this.scores = 0;
		this.vies = 3;
		/*
		 * Initialisation des fantomes et des pacmans crees par maze lors du chargement
		 * d'une map
		 */

		for (PositionAgent position : labyrinth.getGhosts_start()) {
			Agent ghost = new Agent(position, AgentType.Fantome);
			ghost.setOldPosition(position);
			ghost.setStrategie(new PoursuitePacman());
			this.fantomes.add(ghost);
		}

		for (PositionAgent position : labyrinth.getPacman_start()) {
			Agent pacman = new Agent(position, AgentType.Pacman);
			pacman.setOldPosition(position);
			pacman.setStrategie(new Aleatoire());
			this.positionPacmanSave.add(new PositionAgent(position.getX(), position.getY(), position.getDir()));
			this.pacmans.add(pacman);
		}
		
		if(this.j1) {
			this.pacmans.get(0).setStrategie(joueur1);
		}
		
		if(this.j2) {
			this.fantomes.get(0).setStrategie(joueur2);
		}
		
		jouerBruitage("resource/sons/pacman_beginning.wav");
	}

	@Override
	public void takeTurn() {
		// TODO Auto-generated method stub

		ArrayList<Agent> pacmansTmp = new ArrayList<>();
		ArrayList<Agent> fantomesTmp = new ArrayList<>();
		ArrayList<PositionAgent> newPosPacman = new ArrayList<>();
		ArrayList<PositionAgent> newPosFantome = new ArrayList<>();

		for (Agent p : this.pacmans) {
			moveAgent(p, p.getStrategie().getAction(p, labyrinth));
			mangerPacgomme(p);
			mangerCapsule(p);
		}

		for (Agent f : this.fantomes) {
			if(this.ghostScarred && this.turnScarred == this.getLapCount() + 20) {
				f.setStrategie(new FuiteFantome());
			}else if(this.ghostScarred && this.turnScarred == this.getLapCount()) {
				f.setStrategie(new PoursuitePacman());
			}
			moveAgent(f, f.getStrategie().getAction(f, labyrinth));
			
		}

		if (this.ghostScarred) {
			for (Agent p : this.pacmans) {
				for (Agent f : this.fantomes) {
					fantomesTmp.add(mangerGhost(p, f));
				}
			}

			for (Agent tmp : fantomesTmp) {
				this.fantomes.remove(tmp);
			}
		} else {
			for (Agent f : this.fantomes) {
				for (Agent p : this.pacmans) {
					pacmansTmp.add(mangerPacman(f, p));
				}
			}

			for (int i = 0; i < pacmansTmp.size(); ++i) {
				if(pacmansTmp.get(i) != null && this.vies == 0) {
					this.pacmans.remove(pacmansTmp.get(i));
				}else if(pacmansTmp.get(i) != null) {
					this.vies -= 1;
					for(int j = 0; j < this.pacmans.size(); j++) {
						if(this.pacmans.get(j).equals(pacmansTmp.get(i))) {
							this.pacmans.get(j).setPosition(new PositionAgent(positionPacmanSave.get(j).getX(), positionPacmanSave.get(j).getY(), positionPacmanSave.get(j).getDir()));
						}
					}
				}
			}
		}
		
		for(Agent p : this.pacmans) {
			newPosPacman.add(p.getPosition());
		}
		
		for(Agent f : this.fantomes) {
			newPosFantome.add(f.getPosition());
		}
		
		this.labyrinth.setPacman_start(newPosPacman);
		this.labyrinth.setGhosts_start(newPosFantome);
		
		if(this.turnScarred == this.getLapCount()) {
			this.ghostScarred = false;
		}
		
		if(this.pacmans.size() <= 0) {
			this.gameOver();
			notifier("Fantome win");
			JOptionPane.showMessageDialog(null, "Les fantomes ont gagnés la partie !", "PacmanGame", JOptionPane.INFORMATION_MESSAGE);
		}
		
		this.pacmanWin = true;
		for(int i = 0; i < this.labyrinth.getSizeX(); i++) {
			for(int j = 0; j < this.labyrinth.getSizeY(); j++) {
				if (labyrinth.isFood(i, j)) {
					this.pacmanWin = false;
				}
			}
		}
		
		if(this.pacmanWin) {
			this.gameOver();
			notifier("Pacman win");
			JOptionPane.showMessageDialog(null, "Le pacman a gagné la partie !", "PacmanGame", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub
		this.setRunning(false);
	}

	public boolean isLegalMove(Agent agent, AgentAction agentAction) {
		if (this.labyrinth.isWall(agent.getPosition().getX() + agentAction.getVx(),
				agent.getPosition().getY() + agentAction.getVy())) {
			return true;
		} else {
			return false;
		}
	}

	public void moveAgent(Agent agent, AgentAction agentAction) {
		if (!isLegalMove(agent, agentAction)) {
			agent.setOldPosition(new PositionAgent(agent.getPosition().getX(), agent.getPosition().getY(),
					agentAction.getDirection()));
			agent.getPosition().setX(agent.getPosition().getX() + agentAction.getVx());
			agent.getPosition().setY(agent.getPosition().getY() + agentAction.getVy());
			agent.getPosition().setDir(agentAction.getDirection());
		} else {
			//System.out.println("moveAgent impossible");
		}
	}

	public void mangerPacgomme(Agent agent) {
		if (labyrinth.isFood(agent.getPosition().getX(), agent.getPosition().getY())) {
			jouerBruitage("resource/sons/pacman_eatfruit.wav");
			labyrinth.setFood(agent.getPosition().getX(), agent.getPosition().getY(), false);
			this.scores += 10;
		}
	}

	public void mangerCapsule(Agent agent) {
		if (labyrinth.isCapsule(agent.getPosition().getX(), agent.getPosition().getY())) {
			jouerBruitage("resource/sons/pacman_chomp.wav");
			labyrinth.setCapsule(agent.getPosition().getX(), agent.getPosition().getY(), false);
			this.turnScarred = getLapCount() + 20;
			this.ghostScarred = true;
		}
	}

	public Agent mangerPacman(Agent ghost, Agent pacman) {
		if (ghost.getPosition().getX() == pacman.getPosition().getX()
				&& ghost.getPosition().getY() == pacman.getPosition().getY()) {
			System.out.println("on mange un pacman premier cas");
			jouerBruitage("resource/sons/pacman_death.wav");
			return pacman;
		} else if ((ghost.getOldPosition().getX() + 1) == pacman.getOldPosition().getX()
				&& ghost.getOldPosition().getY() == pacman.getOldPosition().getY()
				&& (ghost.getPosition().getX() - 1) == pacman.getPosition().getX()
				&& ghost.getPosition().getY() == pacman.getPosition().getY()) {
			jouerBruitage("resource/sons/pacman_death.wav");
			System.out.println("on mange un pacman deuxieme cas");
			return pacman;
		} else if ((ghost.getOldPosition().getX() - 1) == pacman.getOldPosition().getX()
				&& ghost.getOldPosition().getY() == pacman.getOldPosition().getY()
				&& (ghost.getPosition().getX() + 1) == pacman.getPosition().getX()
				&& ghost.getPosition().getY() == pacman.getPosition().getY()) {
			jouerBruitage("resource/sons/pacman_death.wav");
			System.out.println("on mange un pacman troisieme cas");
			return pacman;
		} else if (ghost.getOldPosition().getX() == pacman.getOldPosition().getX()
				&& (ghost.getOldPosition().getY() + 1) == pacman.getOldPosition().getY()
				&& ghost.getPosition().getX() == pacman.getPosition().getX()
				&& (ghost.getPosition().getY() - 1) == pacman.getPosition().getY()) {
			jouerBruitage("resource/sons/pacman_death.wav");
			System.out.println("on mange un pacman quatrieme cas");
			return pacman;
		} else if (ghost.getOldPosition().getX() == pacman.getOldPosition().getX()
				&& (ghost.getOldPosition().getY() - 1) == pacman.getOldPosition().getY()
				&& ghost.getPosition().getX() == pacman.getPosition().getX()
				&& (ghost.getPosition().getY() + 1) == pacman.getPosition().getY()) {
			jouerBruitage("resource/sons/pacman_death.wav");
			System.out.println("on mange un pacman cinquieme cas");
			return pacman;
		} else {
			return null;
		}
	}

	public Agent mangerGhost(Agent pacman, Agent ghost) {
		if (pacman.getPosition().getX() == ghost.getPosition().getX()
				&& pacman.getPosition().getY() == ghost.getPosition().getY()) {

			jouerBruitage("resource/sons/pacman_eatghost.wav");
			System.out.println("on mange un fantome premier cas");
			return ghost;
		} else if ((pacman.getOldPosition().getX() + 1) == ghost.getOldPosition().getX()
				&& pacman.getOldPosition().getY() == ghost.getOldPosition().getY()
				&& (pacman.getPosition().getX() - 1) == ghost.getPosition().getX()
				&& pacman.getPosition().getY() == ghost.getPosition().getY()) {

			jouerBruitage("resource/sons/pacman_eatghost.wav");
			System.out.println("on mange un fantome deuxieme cas");
			return ghost;
		} else if ((pacman.getOldPosition().getX() - 1) == ghost.getOldPosition().getX()
				&& pacman.getOldPosition().getY() == ghost.getOldPosition().getY()
				&& (pacman.getPosition().getX() + 1) == ghost.getPosition().getX()
				&& pacman.getPosition().getY() == ghost.getPosition().getY()) {

			jouerBruitage("resource/sons/pacman_eatghost.wav");
			System.out.println("on mange un fantome troisieme cas");
			return ghost;
		} else if (pacman.getOldPosition().getX() == ghost.getOldPosition().getX()
				&& (pacman.getOldPosition().getY() + 1) == ghost.getOldPosition().getY()
				&& pacman.getPosition().getX() == ghost.getPosition().getX()
				&& (pacman.getPosition().getY() - 1) == ghost.getPosition().getY()) {

			jouerBruitage("resource/sons/pacman_eatghost.wav");
			System.out.println("on mange un fantome quatrieme cas");
			return ghost;
		} else if (pacman.getOldPosition().getX() == ghost.getOldPosition().getX()
				&& (pacman.getOldPosition().getY() - 1) == ghost.getOldPosition().getY()
				&& pacman.getPosition().getX() == ghost.getPosition().getX()
				&& (pacman.getPosition().getY() + 1) == ghost.getPosition().getY()) {

			jouerBruitage("resource/sons/pacman_eatghost.wav");
			System.out.println("on mange un fantome cinquieme cas");
			return ghost;
		} else {
			return null;
		}
	}

	public Maze getLabyrinth() {
		return labyrinth;
	}

	public void setLabyrinth(Maze labyrinth) {
		this.labyrinth = labyrinth;
	}

	public List<Agent> getFantomes() {
		return fantomes;
	}

	public void setFantomes(List<Agent> fantomes) {
		this.fantomes = fantomes;
	}

	public List<Agent> getPacmans() {
		return pacmans;
	}

	public void setPacmans(List<Agent> pacmans) {
		this.pacmans = pacmans;
	}

	public boolean isGhostScarred() {
		return ghostScarred;
	}

	public void setGhostScarred(boolean ghostScarred) {
		this.ghostScarred = ghostScarred;
	}

	public Joueur1 getJoueur1() {
		return joueur1;
	}

	public Joueur2 getJoueur2() {
		return joueur2;
	}

	public void setJoueur1(Joueur1 joueur1) {
		this.joueur1 = joueur1;
	}

	public void setJoueur2(Joueur2 joueur2) {
		this.joueur2 = joueur2;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public boolean isJ1() {
		return j1;
	}

	public boolean isJ2() {
		return j2;
	}

	public void setJ1(boolean j1) {
		this.j1 = j1;
	}

	public void setJ2(boolean j2) {
		this.j2 = j2;
	}

	public int getScores() {
		return scores;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}

	public int getVies() {
		return vies;
	}

	public void setVies(int vies) {
		this.vies = vies;
	}
}