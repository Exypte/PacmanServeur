package patternStrategie;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Command.Agent;
import Command.AgentAction;
import Model.Maze;

public class Joueur2 implements Strategies, KeyListener {

	AgentAction agentAction = new AgentAction(Maze.STOP);
	
	@Override
	public AgentAction getAction(Agent agent, Maze labyrinthe) {
		// TODO Auto-generated method stub
		return agentAction;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case 81:
			this.agentAction = new AgentAction(Maze.WEST);
			break;
		case 90:
			this.agentAction = new AgentAction(Maze.NORTH);
			break;
		case 68:
			this.agentAction = new AgentAction(Maze.EAST);
			break;
		case 83:
			this.agentAction = new AgentAction(Maze.SOUTH);
			break;
		default:
			//System.out.println("KeyCode : " + e.getKeyCode() + " non traité");
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
