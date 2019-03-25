package patternStrategie;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Command.Agent;
import Command.AgentAction;
import Model.Maze;

public class Joueur1 implements Strategies, KeyListener {

	AgentAction agentAction = new AgentAction(Maze.STOP);
	
	@Override
	public AgentAction getAction(Agent agent, Maze labyrinthe) {
		// TODO Auto-generated method stub
		return agentAction;
	}

	
	public AgentAction getAgentAction() {
		return agentAction;
	}


	public void setAgentAction(AgentAction agentAction) {
		this.agentAction = agentAction;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case 37:
			this.agentAction = new AgentAction(Maze.WEST);
			break;
		case 38:
			this.agentAction = new AgentAction(Maze.NORTH);
			break;
		case 39:
			this.agentAction = new AgentAction(Maze.EAST);
			break;
		case 40:
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
