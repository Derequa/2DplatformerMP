package server;

import java.io.Serializable;
import java.util.Hashtable;
import model.*;
@Deprecated
public class GameStatePacket implements Serializable {

	private static final long serialVersionUID = 5651937106928201982L;
	
	public Hashtable<Integer, GameObject> objects;
	public Spawn playerSpawn;
	public Spawn playerDeath;
	public int deathGUID;
	public int yourPlayer;

	public GameStatePacket(Hashtable<Integer, GameObject> objects, Spawn spawn, Spawn death, int deathGUID, int yourPlayer) {
		this.objects = objects;
		this.playerSpawn = spawn;
		this.playerDeath = death;
		this.deathGUID = deathGUID;
		this.yourPlayer = yourPlayer;
	}

}
