# RedMetrics

RedMetrics offers open source game analytics. It is made up of a RESTful web service and a [web app](https://github.com/CyberCRI/RedMetrics-Web) that allows teachers and researchers to track game metrics, then download the raw data for offline analysis. Limited support for groups is also available.

Check it out at [redmetrics.crigamelab.org](http://redmetrics.crigamelab.org). 

To use it for your game, check out the [RESTful web service API](API.md). If your game works in Unity, check out [RedMetrics-Unity](https://github.com/CyberCRI/RedMetrics-Unity). If your game runs in a browser, you can use [RedMetrics.js](https://github.com/CyberCRI/RedMetrics.js) to connect it.

We take privacy seriously! You may not use RedMetrics to store personally identifiable information about users, such as email address or full names. Please see the [RedMetrics Privacy Policy](PRIVACY.MD) for more information.

## Features

* Game metrics
    * Time played, levels, progression
    * Replay (drill-down)
    * Export raw metrics data for download
* Player information
    * Stores information about players
    * Associates players with anonymous IDs 
    * Does not handle login or registration - each game is responsable for those functions

## Development

RedMetrics runs on Linux and Mac OSX.

First :

 - Make sure you have a Java 8 SDK installed
 - Install postgresql
 - create a postgresql role
 - create a database 
 - run "CREATE EXTENSION ltree;" as a superuser on the database
   OR
   psql yourdb < ltree.sql

### Getting started...

####...with IntelliJ

Checkout project using GitHub tool in IntelliJ

Set language level to Java 8.0

    File > Project Structure
    Project language level > select "8.0 - Lambdas..."

Install Lombok plugin

    File > Settings > Plugins > Browse Repositories
    Type "lombok" in search bar
    Install and restart IDE

#### ...with NetBeans

Install NetBeans IDE 8.0

Checkout project using Git tool in NetBeans :

    Team > git > clone
    Use this URL : https://github.com/CyberCRI/RedMetrics.git
    Enter name and password (check remember password if you want)
    choose the branche to clone

Set language level to Java 8.0

    File > Project Properties > sources
    Set source/binary format to 1.8

### Deploying 

To deploy, simply create a packaged JAR file that contains all the dependencies, and upload it onto your server.

In the command line, use `mvn package` (or `mvn package -Dmaven.test.skip=true` to skip the unit tests). The packaged JAR is in the `target` directory, called `redmetrics-0.1-jar-with-dependencies.jar`. 

### Configuration

You need to create the RedMetrics config file redmetrics.conf in /etc/redmetrics.conf or in ./redmetrics.conf.
You can use the example given with the project : [ExampleFile](https://github.com/CyberCRI/RedMetrics/blob/master/src/main/java/redmetricsExample.conf)

#### Database related config
 * **databaseURL** is the psql database URL
 * **dbusername** is the username you choose for the psql database
 * **dbassword** is the password you choose for the psql database

#### RedMetrics related config
 * **listenPort** will be the port used by RedMetrics for listening

### Starting the server

To start RedMetrics, launch it with Java. We recommend using a daemon on Linux to start, stop, restart and check the status of RedMetrics.
