package mainengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import empire.*;

public class World {
	final Class<?>[] competitors = {
			TestEmpire1.class,
			TestEmpire2.class
	};
	// The size of the world
	public final int size = (int) (Math.sqrt(competitors.length) * 4);
	// A map of all of the world
	Army[][] map = new Army[size][size];
	private static Random r;
	// A list of the current empires
	List<Empire> empires = new ArrayList<Empire>(competitors.length + 2);
	public final int neutralArmyStrength = 200;
	public final int empireBaseStrength = 500;
	private final int numberOfTurns = 12;
	
	static HtmlManager html;
	
	// Keep a static reference to the world
	public static World world;
	public static void main(String[] args) throws IOException {
		r = new Random();
		// Create a new world
		world = new World();
		html = new HtmlManager();
		html.appendHead(r);
		try {
			// Create an instance of each empire
			world.init();
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println("Something went wrong with instanciating the empires.");
			e.printStackTrace();
		}
		for (int i = 0; i < world.numberOfTurns; ++i) {
			html.logTableXML();
			world.nextTurn();
		}
		html.logTableXML();
		world.show();
		world.printStats();
		html.end();
	}
	private void printStats() {
		for (Empire e : empires) {
			System.out.println(e + ": " + e.getArmies().size());
		}
	}
	private void init() throws InstantiationException, IllegalAccessException {
		// We randomly assign a starting value for each empire, without duplicates
		int[] starting = new int[competitors.length];
		do {
			for (int i = 0; i < starting.length; ++i) {
				starting[i] = r.nextInt(size * size);
			}
			Arrays.sort(starting);
		} while (!checkStarting(starting));
		// This is the position of the empire we are adding
		int pos = 0;
		// We create a neutral empire to occupy all spots that's not a regular empire
		Empire neutralEmpire = new Neutral();
		empires.add(neutralEmpire);
		for (int row = 0; row < map.length; ++row) {
			for (int column = 0; column < map.length; ++column) {
				// Is this a real empire or a neutral army?
				boolean isRealEmpire = (pos < starting.length && starting[pos] == row * size + column);
				Army army = new Army(isRealEmpire ? empireBaseStrength : neutralArmyStrength, column, row);
				Empire e = (Empire) (isRealEmpire ? competitors[pos].newInstance() : neutralEmpire);
				e.addArmies(army);
				map[row][column] = army;
				
				if (isRealEmpire) {
					pos++;
					empires.add(e);
				}
			}
		}
		flushAllBufferArmies();
	}
	private void flushAllBufferArmies() {
		for (Empire e : empires) {
			e.flushBufferArmies();
		}
	}
	private void show() {
		for (int row = 0; row < map.length; ++row) {
			for (int column = 0; column < map.length; ++column) {
				System.out.print(map[row][column].toString() + " ");
			}
			System.out.println();
		}
	}
	private void nextTurn() throws IOException {
		Collections.shuffle(empires);
		for (Empire e : empires) {
			e.deploy(new ArmyDeployer(e.getArmiesPerTurn()));
		}
		for (Empire e : empires) {
			e.move();
		}
		flushAllBufferArmies();
		for (Iterator<Empire> i = empires.iterator(); i.hasNext();) {
			Empire e = i.next();
			if (!e.cleanUp()) {
				i.remove();
			};
		}
	}
	private boolean checkStarting(int[] starting) {
		for (int i = 0; i < starting.length - 1; ++i) {
			if (starting[i] == starting[i + 1]) return false;
		}
		return true;
	}
	public int wrapPosition(int x) {
		return (x + size) % size;
	}
}
