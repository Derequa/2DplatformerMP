// This is a simple script that will run on every game loop iteration
// It will raise a script event to be recieved and/or handled by all the listening scripts
function run(){
	event_manager.raiseScriptEvent(timeline.getTime(), 0, s.getGUID());
}
