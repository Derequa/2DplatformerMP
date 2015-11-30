// Write a script that handles an event for a given static rectangle
// This script should be called by the event manager to handle a script event for a
// certain game object
function run(){
	//print('A script event has been handled');
	// Calculate new position
	newY = s.posGetY() + s.vMagY;
	// Check if we move past our bounds and change direction if we do
	if((newY > 320) || (newY < 40))
		s.vMagY *= -1;
	s.posSetY(newY);
}
