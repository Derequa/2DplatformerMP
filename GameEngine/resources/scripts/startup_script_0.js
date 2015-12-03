// This script runs at startup, prints a message, and modifys one of the game objects
function run(){
	print('Hello game engine!\n');
	guid = s.getGUID();
	print('GUID for green platform: ' + guid);
	s.setColor(10, 255, 50);
	s.setVisible(true);
	event_manager.registerScriptEvent("resources/scripts/static_mover.js");
}
