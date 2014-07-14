package mainengine;

public class ArmyDeployer {
	private int deployed = 0, toDeploy;
	ArmyDeployer(int number) {
		toDeploy = number;
	}
	public void reduceArmiesLeft(int number) {
		deployed += number;
	}
	public boolean canDeploy(int number) {
		return (deployed + number) <= toDeploy;
	}
	public int armiesLeft() {
		return toDeploy - deployed;
	}
}
