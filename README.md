# Java Visual Calltree


## How It Works
The agent creates a method calltree for all methods in the defined <package> and stores it a
output diretory.

The server used this directory to create a view of the method call tree.

## Usage

java -javaagent:/<path>/java-vis-agent-0.1.jar="<package>" -jar <your-application>.jar

// start the server
// access the server with localhost:8080



