RedMetrics
=========


First :

 - Make sure you have a Java 8 SDK installed
 - Install postgresql
 - create a postgresql role
 - create a database named "redmetrics"

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