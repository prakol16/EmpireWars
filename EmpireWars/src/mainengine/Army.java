package mainengine;

import java.awt.Point;
import java.io.IOException;

public class Army {
	private int power = 0;
	private Empire empire = null;
	private int x = 0, y = 0;
	boolean isDead = false;
	Army(int power, int x, int y) {
		this.power = power;
		this.x = x;
		this.y = y;
	}
	void setEmpire(Empire e) {
		empire = e;
	}
	public Empire getEmpire() {
		return empire;
	}
	public int getStrength() {
		return power;
	}
	public Point getPoint() {
		return new Point(x, y);
	}
	void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void addPower(ArmyDeployer deployer, int number) {
		if (deployer.canDeploy(number)) {
			power += number;
			deployer.reduceArmiesLeft(number);
		}
	}
	public void move(Move move, int people) throws IOException {
		World world = World.world;
		int newX = world.wrapPosition(x + move.getXOffset()), newY = world.wrapPosition(y + move.getYOffset());
		people = Math.min(people, power);
		if (people < 0) return;
		Army[][] map = world.map;
		if (map[newY][newX].empire == empire) {
			World.html.logTransfer(this, map[newY][newX], people);
			map[newY][newX].power += people;
			power -= people;
		} else {
			Army other = map[newY][newX];
			int kills = (int) (people * 0.6);
			int oKills = (int) (other.power * 0.7);
			World.html.logFight(this, other);
			other.power -= kills;
			if (other.power < 0) {
				other.isDead = true;
				Army newArmy = new Army(Math.max(people - oKills, 0), newX, newY);
				empire.addArmies(newArmy);
				map[newY][newX] = newArmy;
				power -= people;
			} else {
				power -= Math.min(oKills, people);
			}
		}
	}
	public String toString() {
		return this.empire + String.format(" (strength: %d, x: %d, y: %d)", power, x, y);
	}
}
