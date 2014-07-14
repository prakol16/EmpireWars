package empire;

import mainengine.ArmyDeployer;
import mainengine.Empire;

public class Neutral extends Empire {
	@Override
	public void deploy(ArmyDeployer toDeploy) {
		// Neutral empires don't deploy armies
	}

	@Override
	public void move() {
		// Neutral empires don't move
		
	}
	
}
