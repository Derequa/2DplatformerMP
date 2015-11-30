// Write a script to run each gameloop iteration
// This script should change the color of something every few seconds
// This script should also create a script event in the event manager

function run(){
	event_manager.raiseScriptEvent(timeline.getTime(), 0, s.getGUID());
}
