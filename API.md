# Web Service API

All communication between the server is done via JSON by default, although the client can request data in different formats (see the Data Formats section below). Following RESTful architecture, GET is used only to request data, POST creates resources, PUT replaces them, and DELETE removes them.


## Use Cases

This is a list of example requests that shows the features of the API. The meaning of the requests and their parameters are explained in later sections.

* A game developer registers a game:

    * POST /v1/game/ `{ "name": “My awesome game”, “description”: “A great game by me” }`

    * The developer holds onto adminKey for further use

* The game developer registers a version of the game

    * POST /v1/gameVersion/ `{ "game": "1234", "name": “1.0”, “description”: “First public version” }`

* A game wants to start tracking a new player:

   * POST /v1/player `{ "country": "France", gender: "MALE" }`

   * The server returns the player’s unique ID

* A game wants to track a player’s progress:

    * As the game starts:

        * POST /v1/event/ `{ "gameVersion": "123", “player”: "456", “type”: “start” }`

    * At the beginning of a level:

        * POST /v1/event/ `{ "gameVersion": "123", “player”: "456", “type”: “start”, “section”: [“level1”] }`

    * The player picks up a coin:

        * POST /v1/event/ `{ "gameVersion": "123", “player”: "456", “type”: “gain”, “section”: [“level1”], "customData": { “thing”: “coin”, “amount”: 1 } }`

    * The player wins the level

        * POST /v1/event/ `{ "gameVersion": "123", “player”: "456", “type”: “win”, “section”: [“level1”] }`

    * The player dies

        * POST /v1/event/ `{ "gameVersion": "123", “player”: "456", “type”: “fail”, “section”: [“level2”],  “coordinates”: [103, 99] }`

    * The game ends

        * POST /v1/event/ `{ "gameVersion": "123", “player”: "456", “type”: “end” }`

* A game wants to track its state:

    * As the game starts

        * POST /v1/snapshot `{ "gameVersion": "123", “player”: "456", customData": { “points”: 0, “lives”: 3, “mouseDown”: false } }`

    * After gaining some points

        * POST /v1/snapshot `{ "gameVersion": "123", “player”: "456", "customData": { “points”: 81, “lives”: 3, “mouseDown”: false } }`

* Group

    * A teacher creates a group 

        * POST /v1/group `{ "name": “My class” }`

    * The teacher closes the group

        * PUT /v1/group/5873 `{ "open": false }`

* A researcher wants to download the data:

    * Progress data for one game across all players, as JSON:

        * GET /v1/event?game=456

    * State data for a single player across all games, in CSV format:

        * GET /v1/snapshot.csv?playerId=123

    * New progress data in a given group

        * GET /v1/snapshot?groupId=789&after=2015-01-27T09:44:32.418Z


## Data Types

* Date - String containing date and time, following the ISO 8601 Extended format. It looks like `2015-01-27T09:44:32.418Z`. All times are in UTC, and include milliseconds.

* Id - String containing a server-generated unique identifier.

* AdminKey - String containing a password unique for each game, needed for game-specific requests. 

* PlayerMeta - Object with the following properties (all are optional, except id). Since RedMetrics data is open, is is vital that _no personally idenfiable information is stored about a player_.

    * id - Id

    * birthDate - Date. This date _must not_ be more exact than the nearest month and year.

    * region - String

    * country - String

    * gender - String (either "MALE", "FEMALE", or "OTHER")

    * externalId - String that can be set by developers in order to link the player with another database. This _must not_ be a personally identifiable marker such as an email address.

    * customData - String containing JSON data associated with the player. This _must not_ be contain personally identifiable markers such as name or exact address.

* GameMeta - Object with the following properties (all are optional, except id and name)

    * id - Id 

    * name - String

    * author - String containing the name of the person or organization who created the game

    * description - String

    * customData - String containing JSON data.

* GameVersionMeta - Object with the following properties (all are optional, except id and name)

    * id - Id 

    * name - String

    * description - String

    * customData - String containing JSON data.

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

* Coordinate - Array of 2 or 3 integers describing where the event occurred in "game space".

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

    * description - String describing the error.

* Status - Object with the following properties

    * apiVersion - Number describing the version, containing major and minor parts (such as 3.21)

    * build - String describing the build of the software

    * startedAt - Date that the server was last started


## Endpoints

Shared across all versions:

* /status

    * GET - Returns the Status of the server. 

Version 1:

* /v1/player/

    * GET - Lists PlayerMeta objects for all players who have ever used the service (see section on Paging below). 

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

* /v1/gameVersion/:versionId

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

Since a large number of values can be returned via GET requests for lists (such as a list of events, or snapshots), RedMetrics only returns a page of results at a time. The server returns a number of header fields that describe the what page has been requested, how many pages there are, and how many results per page: 

    * `X-Total-Count` - The number of results that fit the query.
    * `X-Page-Count` - The total number of pages.
    * `X-Per-Page-Count` - The number of results per page. 
    * `X-Page-Number` - The current page (page numbering starts at 1). 
    * `Link` - The link header provides URLs that allow you to access the first, next, previous, and last pages. The link header looks like this: `http://api.redwire.io/v1/game/?&page=0&perPage=3; rel=first, http://api.redwire.io/v1/game/?&page=1&perPage=3; rel=prev, http://api.redwire.io/v1/game/?&page=3&perPage=3; rel=next, http://api.redwire.io/v1/game/?&page=172&perPage=3; rel=last`

To request a given page of data, use the `page` and `perPage` parameters. The `page` is the (1-based) number of the page requested. The `perPage` parameter is number of results desired per page. 


## Data Formats

By default, all data is returned by the server in JSON. To request data in another format, the client can send the Accepts header along with the request, use the "format" query parameter with the format shortcode, or end their request with a dot followed the shortcode, such as `/v1/games.csv`. The following formats are provided:

* JSON

    * MIME type: application/json

    * Shortcode: json

* Comma-separated values (CSV) 

    * MIME type: text/csv

    * Shortcode: csv 

The server attempts to compress all transmissions when allowed by client.

It is not possible to _send_ data to the server in a format other than JSON, however.


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
