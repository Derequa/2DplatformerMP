package server;

import processing.core.PApplet;

/**
 * This class is used to run server runner directly instead of jumping through
 * the hoops of running as a PApplet. This way we can easily make the server into
 * a runnable JAR and more easily reference files we need.
 * @author Derek Batts
 */
public class ServerRunner {

	public static void main(String[] args) {
		PApplet.main("server.GameManager");
	}

}
