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

Note that you probably need a Java 1.7 to run the software anyway, due to the
server library (Jetty) dropping support for 1.6, so point #2 is probably a
non-issue anymore.

Also note that for licensing reasons no audio files are currently included. For
FRC teams you can generally find a copy of the .wav files used by the FIRST FMS
inside its program directory. If these files can be confirmed as public domain,
we'll include them here, but until then, we'll be looking for alternatives.

Authors
-------
Thanks go to the members of [FIRST Team #1977](http://robotics.lovelandhs.org)
for aiding in the development and testing of this software.
