// Write a script to do something at engine startup
// This script should det up the static rectangle to move and register the script event handler

function run(){
	print('Hello game engine!\n');
	guid = s.getGUID();
	print('GUID for green platform: ' + guid);
	s.setColor(10, 255, 50);
	s.setVisible(true);
	event_manager.registerScriptEvent("resources/scripts/static_mover.js");
}
