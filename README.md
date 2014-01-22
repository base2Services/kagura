Kagura: A simple developer managed reporting framework

Kagura, allows you to build your own front end, and use SQL queries with a Freemarker based preprocessor, making it a
fast tool to deploy new code and make changes. It also uses a read only simple file based storage system, making it easy
to backup, deploy, and even to scale.

Kagura comprises of 2 components,

1. A REST backend that uses Karaf and Camel

2. A webserver front end, which the user is responsible for developing, an example one is provided with a usable Javascript library in the "example" directory.


#How to start:

##There are several ways of starting the example:

1. Run it all via Maven

2. Run the components in their appropriate containers

3. A mixed approach

##Setup for any approach:

1. Compile the whole project, from the root directory run:

        mvn clean package install

Then start two shells; and each one change them to one of both the directories below:

####Shell 1:

    cd example/javascript

####Shell 2:

    cd services/

##For the maven approach:
####Shell 1:

    mvn clean package org.mortbay.jetty:jetty-maven-plugin:8.1.12.v20130726:run-exploded

####Shell 2:

    cd kagura-camel
    mvn org.apache.camel:camel-maven-plugin:2.9.0:run

##For running via their respective containers:

####Shell 1:

    Deploy WAR file to Tomcat, Jboss or any other java applications server.

####Shell 2:

    cd kagura-assembly/target
    tar xzf kagura-assembly-1.4-SNAPSHOT.tar.gz
    cd kagura-assembly-1.4-SNAPSHOT/bin
    ./karaf debug

##Imaginary Questions

###I like what you have here and wish to deploy it and use it for our own means what do I do?

1. Copy one of the "TestReports" directories, modify the users.yaml and groups.yaml file as desired, then modify the reports as desired

2. Start the assembly project, then shut it down. This creates the com.base2.kagura.services.camel.cfg file in the kagura-assmebly-1.4-SNAPSHOT/etc/

3. Modify the created "com.base2.kagura.services.camel.cfg" file, put the full path the new directory

4. Start the container again

5. Test user

That's it. All further changes to files are live.
