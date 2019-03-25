package patternStrategie;

import Command.Agent;
import Command.AgentAction;
import Command.TypeAction;
import Model.Maze;

public interface Strategies {
	public AgentAction getAction(Agent agent, Maze labyrinthe);
}
