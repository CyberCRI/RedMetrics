RedMetrics
=========


First :

 - Make sure you have a Java 8 SDK installed
 - Install postgresql
 - create a postgresql role
 - create a database named "redmetrics"
 - run "CREATE EXTENSION ltree;" as a superuser
   OR
   psql yourdb < ltree.sql

#####Once you check out the project with Git, you will need to create the following file :

    /src/main/java/org/cri/redmetrics/db/DbUser.java

You can copy the content of DbUserExample.java file in the same package. Then just fill in the username and password of the postgresql role you created previously.


Getting started...
--------------

...with IntelliJ
----

checkout project using github tool in intelliJ

Set language level to Java 8.0

    File > Project Structure
    Project language level > select "8.0 - Lambdas..."

Install Lombok plugin

    File > Settings > Plugins > Browse Repositories
    Type "lombok" in search bar
    Install and restart IDE

...with NetBeans
----

Install NetBeans IDE 8.0

checkout project using git tool in Netbeans :

    Team > git > clone
    Use this URL : https://github.com/CyberCRI/RedMetrics.git
    Enter name and password (check remember password if you want)
    choose the branche to clone

Set language level to Java 8.0

    File > Project Properties > sources
    Set source/binary format to 1.8
   

## Using

By default, RedMetrics listens on port [4567](http://localhost:4567). This can be changed by editing the constructor of [src/main/java/org/cri/redmetrics/Server.java](https://github.com/CyberCRI/RedMetrics/blob/master/src/main/java/org/cri/redmetrics/Server.java). 

## Deploying 

To deploy, simply create a packaged JAR file that contains all the dependencies, upload it onto your server, and launch it.



RedMetrics Specifications
=========

RedMetrics is composed of a RESTful web service and a basic website that allows teachers and researchers to track game metrics, then download the raw data for offline analysis. Limited support for groups is also available.

# Features

* Game metrics

 * Time played, levels, progression

 * Replay (drill-down)

* Export raw metrics data for download

* Teacher- or researcher-created created groups for players to join

* Player information

 * Stores information about players

 * Associates players with anonymous IDs 

 * Does not handle login or registration - each game is responsable for those functions

# Structure

RedMetrics is made up of two parts: a RESTful web service that handles all data storage and retrieval, and a basic web site for interaction with the researcher or teacher.

# Web Service API

All communication between the server is done via JSON by default, although the client can request data in different formats (see the Data Formats section below). Following RESTful architecture, GET is used only to request data, POST creates resources, PUT replaces them, and DELETE removes them.

The database should be protected against SQL Injection

## Use Cases

This is a list of example requests that shows the features of the API. The meaning of the requests and their parameters are explained in later sections.

* A game developer registers a game:

 * POST /v1/game/
{ "name": “My awesome game”, “description”: “A great game by me” }

 * The developer holds onto adminKey for further use

* The game developer registers a version of the game

 * POST /v1/game/1/version/
{ "name": “1.0”, “description”: “First public version” }

* A game wants to start tracking a given player identified by mr@science.com:

 * They look if the player is already registered

  * GET /v1/player/email/mr@science.com

  * If the server returns 404, the player must be new. 

 * The game registers the player.

  * POST /v1/player

{ "email": “[bob@bob.com](mailto:bob@bob.com)” }

  * The server returns the player’s unique ID

* A game wants to track a player’s progress:

 * As the game starts:

  * POST /v1/event/
{ "game": 123, “player”: 456, “type”: “start” }

 * At the beginning of a level:

  * POST /v1/event/
{ "game": 123, “player”: 456, “type”: “start”, “section”: [“level1”] }

 * The player picks up a coin:

  * POST /v1/event/
{ "game": 123, “player”: 456, “type”: “gain”, “section”: [“level1”],

"customData": { “thing”: “coin”, “amount”: 1 }

}

 * The player wins the level

  * POST /v1/event/
{ "game": 123, “player”: 456, “type”: “win”, “section”: [“level1”] }

 * The player dies

  * POST /v1/event/
{ "game": 123, “player”: 456, “type”: “fail”, “section”: [“level2”],  “coordinates”: [103, 99] }

 * The game ends

  * POST /v1/event/
{ "game": 123, “player”: 456, “type”: “end” }

* A game wants to track its state:

 * As the game starts

  * POST /v1/snapshot

{ "game": 123, “player”: 456,

"customData": { “points”: 0, “lives”: 3, “mouseDown”: false }

}

 * After gaining some points

  * POST /v1/snapshot

{ "game": 123, “player”: 456,

"customData": { “points”: 81, “lives”: 3, “mouseDown”: false }

}

* Group

 * A teacher creates a group 

  * POST /v1/group
{ "name": “My class” }

 * The teacher closes the group

  * PUT /v1/group/5873
{ "open": false }

* A researcher wants to download the data:

 * Progress data for one game across all players, as JSON:

  * GET /v1/event?gameId=456

 * State data for a single player across all games, in CSV format:

  * GET /v1/snapshot?playerId=123&format=csv

 * New progress data in a given group

  * GET /v1/snapshot?groupId=789&after=Tue,+22+Oct+2013+18:46:15+UTC

## Data Types

* Date - String containing date and time, following the HTTP format. All times are in UTC.

* Email - String that defines an email address. Hopefully this can be used across login providers, like Google, Facebook, and others.

* Id - String containing a server-generated unique identifier.

* AdminKey - String containing a password unique for each game, needed for game-specific requests. 

* PlayerMeta - Object with the following properties (all are optional, except id)

 * id - Id

 * email - Email. Only exposed when registering a new player and searching for players by their email address.

 * gender - String (either "male" or “female”)

 * city - String

 * country - String

 * birthday - Date

* GameMeta - Object with the following properties (all are optional, except id and name)

 * id - Id 

 * name - String

 * author - String containing the name of the person or organization who created the game

 * description - String

 * allowedDomains - Array of strings or null. If null, pages from any domain are allowed to post data about the game. Otherwise, the given domains are added to CORS headers that restrict which web pages can post data to the game.

* GameVersionMeta - Object with the following properties (all are optional, except id)

 * id - Id 

 * name - String

 * description - String

* GroupMeta - Object with the following properties (all are optional, except id)

 * id - Id

 * name - String

 * description - String

 * creator - String containing the name of the person creating the group

 * open - Boolean indicating if the group accepts more data

* EventType - String naming the type of progress event that occurred. Standard event types are as follows:

 * "start"

 * "end"

 * "win"

 * "fail"

 * "restart"

 * "gain"

 * "lose"

* Coordinate - Array of 2 or 3 numbers describing where the event occurred in "game space".

* Section - String describing what "level" the player was in when the event occured. Adding more elements separated by points specifiy the section within a hierarchy. For example, “level1.section2.subsection3”

* Event - Object with the following properties

 * userTime - Date sent by the game (optional)

 * serverTime - Date generated by the server

 * type - String

 * customData - Any data structure. For "gain" and “lose” events, specifies the number of things are gained or lost.

 * section - Section (optional)

 * coordinates - Coordinate where the event occurred (optional)

* Snapshot - Object with the following properties

 * userTime - Date sent by the game (optional)

 * serverTime - Date generated by the server

 * customData - Object describing the state of the game at the given time. The format of this information is arbitrary and depends on the game.

* Error - Object with the following properties

 * code - Number describing the error.

 * message - String describing the error.

* Status - Object with the following properties

 * apiVersion - Number describing the version, containing major and minor parts (such as 3.21)

 * build - String providing a SHA-1 hash corresponding to the deployed software

 * since - Date that the server was last started

## Endpoints

Shared across all versions:

* /status

 * GET - Returns the Status of the server. 

Version 1:

* /v1/player/

 * GET - Lists PlayerMeta objects for all players who have ever used the service (see section on Paging below). Can be filtered by the following query parameters:

  * email - Email. This is the way to search for registered players by their email address.

 * POST - Creates a new anonymous player and returns the server-generated ID.

* /v1/player/:playerId

 * GET - Retrieves the PlayerMeta for the identified player

 * PUT - Updates the PlayerMeta. Will attempt to merge the sent information with the stored information.

* /v1/game

 * GET - Lists the games using the service as GameMeta objects (see section on Paging below)

 * POST - Creates a new game. A GameMeta object should be sent in the body. The Location response header will contain the URL for the new game. The response body contains the unique AdminKey.

* /v1/game/:gameId

 * GET - Retrieves information about the game with that Id as a GameMeta object

 * PUT - Updates game information with the provided GameMeta. The AdminKey must be sent with the adminKey parameter.

* /v1/game/:gameId/version

 * GET - Lists versions of the the game with that Id as GameVersionMeta objects (see section on Paging below)

 * POST - Creates a new version of the game. A GameVersionMeta object should be sent in the body. The AdminKey must be sent with the adminKey parameter. The Location response header will contain the URL for the new game. 

* /v1/game/:gameId/version/:versionId

 * GET - Retrieves information about the game version as a GameVersionMeta object

 * PUT - Updates game information with the provided GameVersionMeta. The AdminKey must be sent with the adminKey parameter. 

* /v1/group

 * GET - Lists the group as GroupMeta objects (see section on Paging below)

 * POST - Creates a new group. A GroupMeta object should be sent in the body. The Location response header will contain the URL for the new group.

* /v1/group/:groupId

 * GET - Retrieves a GroupMeta associated with that ID.

 * PUT - Updates group information with the provided GroupMeta. 

* /v1/event

 * GET - Lists progress objects (see section on Paging below). Can be filtered by the following query parameters:

  * game - Id (can be included multiple times to form a list)

  * version - Id (can be included multiple times to form a list)

  * player - Id (can be included multiple times to form a list)

  * type - EventType (can be included multiple times to form a list)

  * section - Section (level.section.* finds level.section.subsection)

  * group - Id (can be included multiple times to form a list)

  * after - Date

  * before - Date

  * afterUserTime - Date

  * beforeUserTime - Date

 * POST - Adds more progress information sent with the Progress object, or array or Progress objects. The gameId, playerId, and adminKey query parameters are required. The groupId parameter is optional. Since the progress object cannot be addressed by itself, no Location header will be returned.

* /v1/snapshot

 * GET - Lists Snapshot objects (see section on Paging below). Can be filtered by the following query parameters:

  * game - Id (can be included multiple times to form a list)

  * version - Id (can be included multiple times to form a list)

  * player - Id (can be included multiple times to form a list)

  * group - Id (can be included multiple times to form a list)

  * after - Date

  * before - Date

  * afterUserTime - Date

  * beforeUserTime - Date

 * POST - Adds more state information sent with the Snapshot object, or array of Shapshot objects. The gameId, playerId, and adminKey query parameters are required. The groupId parameter is optional. Since the snapshot object cannot be addressed by itself, no Location header will be returned.

## Paging

Since a large number of values can be returned via GET requests for lists, a paging mechanism is put in place. The list is included in a JSON object that also specifies the index of the first item returned (start), the number of items returned in the response (count), and the total number of items recorded (total). For example, the third page of 50 items from a total of 213 items would look like this:

{ 

  start: 100,

  count: 50,

  total: 213,

  data: [ … ]

} 

To retrieve the next page of data, the client simply sends the start query parameter set to the next page (150 in this example). The count query can also be sent, but the server may not allow the count to exceed a certain maximum value.

## Data Formats

By default, all data is sent by the server in JSON. To request data in another format, the client can send the Accepts header along with the request, or use the "format" query parameter with the format shortcode. The following formats are provided:

* Comma-separated values (CSV) 

 * MIME type: text/csv

 * Shortcode: csv 

* Tab-separated values (TSV) 

 * MIME type: text/tab-separated-values

 * Shortcode: tsv

* JSON

 * MIME type: application/json

 * Shortcode: json

* XML

 * MIME type: application/xml

 * Shortcode: xml

Whenever data is sent in a format other than JSON, the paging features do not apply.

The server attempts to compress all transmissions when allowed by client.

## HTTP status codes

HTTP status codes are treated in the following way:

* 200s - Success

 * 200 OK - A successful request that returned data

 * 201 Created - A successful request that created a resource. The Location header should contain the URL of the new resource.

 * 204 No Content - A successful request that does not return data.

* 400s - Client error

 * 400 Bad Request - The request was formatted incorrectly (e.g. the data contained in the request was not valid JSON).

 * 401 Not Authorized - The access credentials were not sent or are incorrect. 

 * 404 Not Found - The URL does not correspond to a valid resource.

 * 409 Conflict - The resource modification would put the system into an invalid state.

* 500s - Server error

 * 500 Internal Server Error - The server has a bug. Impossible, of course!

## Cross-Origin Request (CORS)

To ease integration with browser-based games, the server will send back the appropriate CORS headers allowing contacting it from any site (or perhaps only those whitelisted).

## Error Messages 

All responses with HTTP error codes should have an Error object in their body.


