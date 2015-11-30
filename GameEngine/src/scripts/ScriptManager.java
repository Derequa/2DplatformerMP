package scripts;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * This class manages scripts for the engine.
 * Code used from homework handout.
 * @author Dr David Roberts
 *
 */
public class ScriptManager {
	/* The javax.script JavaScript engine used by this class. */
	private static ScriptEngine js_engine = new ScriptEngineManager().getEngineByName("JavaScript");
	/* The Invocable reference to the engine. */
	private static Invocable js_invocable = (Invocable) js_engine;

	/**
	 * Used to bind the provided object to the name in the scope of the scripts
	 * being executed by this engine.
	 */
	public static void bindArgument(String name, Object obj) {
		js_engine.put(name,obj);
	}
	
	/**
	 * Will load the script source from the provided filename.
	 */
	public static void loadScript(String script_name) {
		try {
			js_engine.eval(new java.io.FileReader(script_name));
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(java.io.IOException iox) {
			iox.printStackTrace();
		}
	}
	
	/**
	 * Will load the script source from the provided filename.
	 * @throws ScriptException 
	 */
	public static void loadScript(FileReader f) throws ScriptException {
		js_engine.eval(f);
	}

	/**
	 * Will invoke the "update" function of the script loaded by this engine
	 * without any parameters.
	 */
	public static void executeScript() {
		try {
			js_invocable.invokeFunction("run");
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}

	/**
	 * Will invoke the "update" function of the script loaded by this engine
	 * with the provided list of parameters.
	 */
	public static void executeScript(Object... args) {
		try {
			js_invocable.invokeFunction("run",args);
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
}
