package mainengine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class HtmlManager {
	public static BufferedWriter bw;
	public HtmlManager() throws IOException {
		bw = new BufferedWriter(new FileWriter("record.html"));
	}
	void appendHead(Random r) throws IOException {
		World world = World.world;
		StringBuilder styles = new StringBuilder();
		StringBuilder bodyKey = new StringBuilder(createXML("div", "class", "bodyKey"));
		for (Class<?> competitor : world.competitors) {
			String color = randomColor(r);
			String name = competitor.getName().split("\\.")[1];
			styles.append(".").append(name)
			.append("{")
			.append("background-color:").append(color)
			.append("}\n");
			bodyKey.append(createXML("div", "class", name)).append(name).append("</div>");
		}
		bw.write("<html>"
				+ "<head>"
				+ "<title>Record</title>"
				+ createXML("meta", "charset", "UTF-8")
				+ createXML("link", "rel", "stylesheet", "href", "record.css")
				+ createXML("style", "type", "text/css")
				+ styles.toString()
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<button onclick='[].slice.call(document.querySelectorAll(\".info\")).forEach(function(b){b.classList.toggle(\"extraInfo\");});'>"
				+ "Toggle extra info"
				+ "</button>"
				+ bodyKey);
	}
	private static String randomColor(Random r) {
		return String.format("rgb(%d, %d, %d);", r.nextInt(150) + 100, r.nextInt(150) + 100, r.nextInt(150) + 100);
	}
	public static String createXML(String elemName, String... propertyMap) {
		StringBuilder xml = new StringBuilder();
		xml.append('<').append(elemName);
		for (int i = 0; i < propertyMap.length; i += 2) {
			xml.append(' ').append(propertyMap[i]).append("=\"").append(propertyMap[i + 1]).append('"');
		}
		return xml.append('>').toString();
	}
	void logTableXML() throws IOException {
		Army[][] map = World.world.map;
		bw.write("<table>");
		for (Army[] row : map) {
			bw.write("<tr>");
			for (Army a : row) {
				bw.write(createXML("td", "class", a.getEmpire().getClass().getName().replace('.', ' ')));
				bw.write(String.valueOf(a.getStrength()));
				bw.write("</td>");
			}
			bw.write("</tr>");
		}
		bw.write("</table>");
		bw.write('\n');
	}
	void logFight(Army attack, Army defend) throws IOException {
		bw.write(createXML("div", "class", "extraInfo info")
				+ "Army " + attack + " is attacking " + defend
				+ "</div>"
			);
	}
	void logTransfer(Army orig, Army newArm, int people) throws IOException {
		bw.write(createXML("div", "class", "extraInfo info")
				+ "Army " + orig + " is transfering " + people + " people to " + newArm
				+ "</div>"
			);
	}
	void end() throws IOException {
		bw.write("</body></html>");
		bw.flush();
	}
}
