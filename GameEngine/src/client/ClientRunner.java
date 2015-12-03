package client;

import processing.core.PApplet;

/**
 * This class is used to run client runner directly instead of jumping through
 * the hoops of running as a PApplet. This way we can easily make the client into
 * a runnable JAR and more easily reference files we need.
 * @author Derek Batts
 */
public class ClientRunner {

	public static void main(String[] args) {
		PApplet.main("client.Client");
	}

}
