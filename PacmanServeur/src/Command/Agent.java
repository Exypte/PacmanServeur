

package Command;

import Model.Maze;
import patternStrategie.Strategies;

public class Agent {

	private PositionAgent position;
	private PositionAgent oldPosition;
	private AgentType type;
	private Strategies strategie;
	
	public Agent(PositionAgent position, AgentType type) {
		this.position = position;
		this.type = type;
	}

	public PositionAgent getPosition() {
		return position;
	}

	public AgentType getType() {
		return type;
	}
	
	public Strategies getStrategie() {
		return strategie;
	}
	
	public PositionAgent getOldPosition() {
		return oldPosition;
	}
	
	public void setPosition(PositionAgent position) {
		this.position = position;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	public void setStrategie(Strategies strategie) {
		this.strategie = strategie;
	}

	public void setOldPosition(PositionAgent oldPosition) {
		this.oldPosition = oldPosition;
	}
}
