package patternStrategie;

import java.util.Random;

import Command.Agent;
import Command.AgentAction;
import Model.Maze;

public class Aleatoire implements Strategies {

	@Override
	public AgentAction getAction(Agent agent, Maze labyrinth) {
		// TODO Auto-generated method stub
		int[] typeAction = { 0, 1, 2, 3 };
		Random r = new Random();

		switch (typeAction[r.nextInt(4)]) {
		case 0:
			if (!labyrinth.isWall(agent.getPosition().getX(), agent.getPosition().getY() - 1)) {
				return new AgentAction(Maze.NORTH);
			}
			break;
		case 1:
			if (!labyrinth.isWall(agent.getPosition().getX(), agent.getPosition().getY() + 1)) {
				return new AgentAction(Maze.SOUTH);
			}
			break;
		case 2:
			if (!labyrinth.isWall(agent.getPosition().getX() + 1, agent.getPosition().getY())) {
				return new AgentAction(Maze.EAST);
			}
			break;
		case 3:
			if (!labyrinth.isWall(agent.getPosition().getX() - 1, agent.getPosition().getY())) {
				return new AgentAction(Maze.WEST);
			}
			break;
		}
		
		return getAction(agent, labyrinth);
	}
}