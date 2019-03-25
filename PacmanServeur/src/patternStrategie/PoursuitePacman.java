package patternStrategie;

import java.util.ArrayList;

import Command.Agent;
import Command.AgentAction;
import Command.PositionAgent;
import Model.Maze;

public class PoursuitePacman implements Strategies{

	
	public double calculDistanceEntreDeuxPoints(int xA, int yA, int xB, int yB) {

		return Math.sqrt(Math.pow((xB-xA),2)+Math.pow((yB-yA),2));
	}

	@Override
	public AgentAction getAction(Agent agent, Maze labyrinth) {
		
		// TODO Auto-generated method stub
		//recuperer les positions des pacmans
		ArrayList<PositionAgent> listePacmans=labyrinth.getPacman_start();
		ArrayList<Double> listeDistances=new ArrayList<Double>();
		
		for(PositionAgent pacman : listePacmans) {
			
			listeDistances.add(calculDistanceEntreDeuxPoints(agent.getPosition().getX(),agent.getPosition().getY(),pacman.getX(),pacman.getY()));
		}
		//on cherche le pacman le plus proche du fantome
		PositionAgent pacmanLePlusProche=listePacmans.get(0);
		double minimum=listeDistances.get(0);
		for (int i = 0; i < listeDistances.size(); i++) {
			if(minimum > listeDistances.get(i)) {
				minimum=listeDistances.get(i);
				pacmanLePlusProche=listePacmans.get(i);
			}
		}
		
		listeDistances.clear();
		//on cherche les chemins possibles
		ArrayList<PositionAgent> postitionPossibles=new ArrayList<PositionAgent>();
		
		if (!labyrinth.isWall(agent.getPosition().getX(), agent.getPosition().getY() - 1)) {
			//onfuit
			postitionPossibles.add(new PositionAgent(agent.getPosition().getX(), (agent.getPosition().getY()-1),Maze.NORTH));
		}

		if (!labyrinth.isWall(agent.getPosition().getX(), agent.getPosition().getY() + 1)) {

			postitionPossibles.add(new PositionAgent(agent.getPosition().getX(), (agent.getPosition().getY()+1),Maze.SOUTH));
		}

		if (!labyrinth.isWall(agent.getPosition().getX() + 1, agent.getPosition().getY())) {

			postitionPossibles.add(new PositionAgent((agent.getPosition().getX()+1), agent.getPosition().getY(),Maze.EAST));
		}

		if (!labyrinth.isWall(agent.getPosition().getX() - 1, agent.getPosition().getY())) {
			
			postitionPossibles.add(new PositionAgent((agent.getPosition().getX()-1), agent.getPosition().getY(),Maze.WEST));
		}
		
		//on prend la distance la plus courte vers pacman
		
		for (PositionAgent positionAgent : postitionPossibles) {
				
			listeDistances.add(calculDistanceEntreDeuxPoints(positionAgent.getX(), positionAgent.getY(), pacmanLePlusProche.getX(), pacmanLePlusProche.getY()));
		}
		
		PositionAgent positionLaPlusProche=postitionPossibles.get(0);
		
		minimum =listeDistances.get(0);
		
		for (int i = 0; i < listeDistances.size(); i++) {
			
			if(minimum > listeDistances.get(i)) {
				minimum=listeDistances.get(i);
				positionLaPlusProche=postitionPossibles.get(i);
			}
		}

		
		return new AgentAction(positionLaPlusProche.getDir());
	}
}
