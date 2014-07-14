package empire;

import java.io.IOException;
import java.util.List;

import mainengine.Army;
import mainengine.ArmyDeployer;
import mainengine.Empire;
import mainengine.Move;
import mainengine.World;

public class TestEmpire1 extends Empire {
	@Override
	public void deploy(ArmyDeployer toDeploy) {
		List<Army> armies = getArmies();
		int perArmy = toDeploy.armiesLeft() / armies.size();
		for (Army a : armies) {
			a.addPower(toDeploy, perArmy);
		}
	}

	@Override
	public void move() throws IOException {
		World world = World.world;
		Army[][] map = getMap();
		for (Army a : getArmies()) {
			Move bestMove = null;
			int leastDefended = Integer.MAX_VALUE;
			for (Move move : Move.values()) {
				int newX = world.wrapPosition(move.getXOffset() + a.getPoint().x),
					newY = world.wrapPosition(move.getYOffset() + a.getPoint().y);
				if (map[newY][newX] != null && map[newY][newX].getStrength() <= leastDefended) {
					leastDefended = map[newY][newX].getStrength();
					bestMove = move;
				}
			}
			a.move(bestMove, a.getStrength() / 2);
		}
	}

}
