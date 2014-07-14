package mainengine;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Empire {
	private List<Army> armies = new ArrayList<Army>();
	private List<Army> bufferArmies = new ArrayList<Army>();
	private final int armyGrowth = 50;
	public final void addArmies(Army... armies) {
		for (Army army : armies) {
			bufferArmies.add(army);
			army.setEmpire(this);
		}
	}
	public final Army[][] getMap() {
		World world = World.world;
		Army[][] fogged = new Army[world.size][world.size];
		Army[][] map = world.map;
		for (Army a : armies) {
			for (Move m : Move.values()) {
				Point p = a.getPoint();
				int x = world.wrapPosition(p.x + m.getXOffset()),
						y = world.wrapPosition(p.y + m.getYOffset());
				fogged[y][x] = map[y][x];
			}
		}
		return fogged;
	}
	final void flushBufferArmies() {
		armies.addAll(bufferArmies);
		bufferArmies.clear();
	}
	public final int getArmiesPerTurn() {
		return World.world.empireBaseStrength + armyGrowth * armies.size();
	}
	public abstract void deploy(ArmyDeployer toDeploy);
	public abstract void move() throws IOException;
	public final List<Army> getArmies() {
		return armies;
	}
	public String toString() {
		return this.getClass().getName();
	}
	public final boolean cleanUp() {
		for (Iterator<Army> i = armies.iterator(); i.hasNext();) {
			Army a = i.next();
			if (a.isDead) {
				i.remove();
			}
		}
		return armies.size() > 0;
	}
}
