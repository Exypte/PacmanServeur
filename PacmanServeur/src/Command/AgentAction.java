package Command;

import Model.Maze;

public class AgentAction {

	// Vecteur de déplacement qui sera utile pour réaliser l'action dans le jeu
	private int vx;
	private int vy;

	// Direction
	private int direction;

	public AgentAction(int direction){

        this.direction = direction;

        /*
         * Calcul le vecteur de deplacement associe
         */
        if (this.direction == Maze.NORTH){
            this.vx = 0;
            this.vy = -1;
        } else if(this.direction == Maze.SOUTH){
			this.vx = 0;
			this.vy = 1;
        } else if(this.direction == Maze.EAST){
			this.vx = 1;
			this.vy = 0;
        } else if(this.direction == Maze.WEST){
			this.vx = -1;
			this.vy = 0;
        } else if(this.direction == Maze.STOP){
			this.vx = 0;
			this.vy = 0;
			this.direction = 0;
        } else {
			System.out.println("Direction impossible");
        }
	}
	
	public int getVx() {
		return vx;
	}

	public int getVy() {
		return vy;
	}

	public int getDirection() {
		return direction;
	}

	public void setVx(int vx) {
		this.vx = vx;
	}

	public void setVy(int vy) {
		this.vy = vy;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}



	/**
	 * @param agent
	 * @param typeAction
	 * 
	 */
	public void doAction(Agent agent, TypeAction typeAction) {

		switch (typeAction) {
		case Est:
			moveToEst(agent);
			break;
		case Nord:
			moveToNord(agent);
			break;
		case Ouest:
			moveToOuest(agent);
			break;
		case Sud:
			moveToSud(agent);
			break;
		default:

			break;
		}

	}

	public void moveToEst(Agent agent) {
		agent.setPosition(new PositionAgent(agent.getPosition().getX() + 1, agent.getPosition().getY(), Maze.EAST));
	}

	public void moveToNord(Agent agent) {
		agent.setPosition(new PositionAgent(agent.getPosition().getX(), agent.getPosition().getY() + 1, Maze.SOUTH));

	}

	public void moveToSud(Agent agent) {
		agent.setPosition(new PositionAgent(agent.getPosition().getX(), agent.getPosition().getY() - 1, Maze.NORTH));
	}

	public void moveToOuest(Agent agent) {
		agent.setPosition(new PositionAgent(agent.getPosition().getX() - 1, agent.getPosition().getY(), Maze.WEST));
	}

}
