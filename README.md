This is a small-scale game engine for a 2D platformer with multiplayer functions.
This was designed and built over a semester for a class of mine on game engines and I plan on updating it and re-tooling it to be more robust and flexible in the coming weeks and months. I used the processing framework to build this game. It currently is bundled with processing 2, but will be updated with processing 3 very soon. Currently the game it plays is very very simple. You control a box that can jump around and respawn and stuff. Once you run the server side of the game, any number of clients can connect and play (on localhost currently). Each client is equipped with the ability to record and play back replays of gameplay at varying speeds while still playing on the server. This project was a wonderful challenge and learing expierence with many different aspects of design and I look for ward to improving it more.


BUILDING: This is essentailly an eclipse project, you should be able to clone it directly. If not, you should be able to easily import an archive of this repository into an eclipse project.


To run the server side YOU MUST RUN THE SERVER RUNNER CLASS because that way java can find the resource files it needs.
Same deal with the client side RUN THE CLIENT RUNNER CLASS.


Controls:

W - move up / tap to jump

A - move left

D = move right
