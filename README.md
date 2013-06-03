Distributed Scoring for Event Displays
======================================

Scoring software for small, two-alliance events to present scoring information
to an audience. This is a generic replacement for the often proprietary
scorekeeping and match management software used for competitions like the
FIRST Robotics Competition and VEX, but can be used for just about any type of
game.

The ideal use case is events such as robotics competitions (e.g., FTC, FRC, VEX,
etc), but all objectives and alliance layouts can be customized to fit a wide
variety of different games and formats.

Features
--------

* Distributed scoring. Multiple referees can enter scoring info via a mobile 
  interface.
* Team and match management
* Automatic schedule generation via a simulated annealing algorithm
	* Configurable alliance sizes and annealing parameters
* Configurable scoring objectives, including:
	* Categorized scoring groups to be summarized on the event display
	* Linear / exponential score multipliers
* A large score display suitable for being projected / shown to an audience
* Other configurable game parameters:
	* Configurable game periods (e.g., autonomous, teleop, endgame)
		* Configurable lengths and start times
		* Audio notifications on game period transitions  
	* Alliances: name, color
		* Lax alliance sizes - no-shows are allowed

Download
--------
The latest version is v1.0 and can be downloaded in binary form 
[from Bintray](http://dl.bintray.com/timothyb89/scoring-releases/scoring-1.0.zip?direct).

You can also get the latest version directly from git:

```
$ git clone https://github.com/timothyb89/scoring.git
$ cd scoring
$ mvn assembly:single
```

(note that maven is required to build)

Getting Started
---------------
To run, you just need a JRE 7 or higher. Then:

1. Copy `data/matches.dist.xml` to `data/matches.xml`
2. Copy `data/teams.dist.xml` to `data/teams.xml`
	* You can adjust the teams manually in the file now, or later via the web 
	  interface
3. Copy one `games/game.*.xml` to `data/game.xml`
	* `game.soccer.xml` is a good example to use if starting from scratch
4. Run scoring-[version].jar (or target/scoring-[version]-jar-with-dependencies.jar
   if using a git build)

The Match Frame should be opened and shown in fullscreen on the public-facing
display.

Once running, you can navigate to http://localhost:8182/ to view the web
interface. This can also be browsed to on any LAN-connected mobile devices to
assist in scoring, should firewalls permit.

From there you can:

* Manage teams
* Score / manage matches
* View / generate the schedule

Running Matches
---------------
_Note: the 'New Match' button on the computer GUI is deprecated and will not
work in many cases. Use the web interface instead._

Once teams have been configured and game parameters adjusted to your liking, you
can start a match via the web interface from any computer or mobile device.
Matches are managed entirely from the Scoring page in the web interface.

Match workflow:

1. Create ('queue') a match. The teams will be shown in the 'Next Match' field
   on-screen
2. Start the match
3. Keep score throughout the match
	* At any time, you may optionally pause the match or reset it. This will not
	  reset scores.
	* Cancelling the match will reset all scores, and give teams no points
4. Finish and save, or scrap the match
	* This will update the score display with new team rankings
5. Repeat until finished

Matches can be created with either the _Create Match_ button on the Scoring
page, or by selecting an entry from the Schedule page. 

API
---
The mobile clients use a JSON API that can also be accessed by automated scripts
and programs. In the past this has been used to create automated scoring
counters which read button presses via a device like an Arduino to automatically
credit teams for achieving scoring objectives.

Other notes
-----------
While this project is in a working condition and is used publicly in
competitions, ~~a lot~~ almost all of the code was tossed together in bursts of
two weeks and as such has some very messy pieces. Refactoring is a major bullet
point on the TODO list.

On audio: This uses the standard JDK audio libraries, which comes with some
caveats:
1. Supports only .wav formatted audio (though vorbis/ogg could be added fairly
   easily if requested)
2. On Linux, versions of Java before 1.7 frequently fail to detect the sound 
   device properly. This should be fixed with the new audio backend (Gervill)

Note that you probably need Java 1.7 to run the software anyway, due to the
server library (Jetty) dropping support for 1.6, so point #2 is probably a
non-issue anymore.

Also note that for licensing reasons various custom (free) audio files are
distributed with the application, rather than the familiar files from the FIRST
Field Management System. If you'd like you can copy those files into the
`res/sound/` directory and replace the provided files.

Authors
-------
Thanks go to the members of [FIRST Team #1977](http://robotics.lovelandhs.org)
for aiding in the development and testing of this software.
