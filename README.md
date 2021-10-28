## Color, a "machine learning" attempt

### Introduction

The first thing that should be mentioned is that this program isn't really an artificial intelligence or machine learning program, I would rather call it lucky intelligence, as luck when the random variables in the nodes is created has a large impact in the "learning" part of the program.

The programs purpose is to make predictions if the user will like a specific color. The program does this by getting known colors and whether the user likes the specific color and then makes modifications to itself to  

### Todo:

* Make the /prediction.html interface
* Make the /information.html interface
* Create a data export function ( Training data and node values )
* Create a data import function ( Training data and node values )
* Automatic new generation
* Smarter random color generation ( Finds the color the furthest away from any other data point )
* Write the README.md document better also so it contains what the project actually is.

### How does it work?

#### User Interface:

The program creates a local webserver that serves the HTML/CSS/JS files to the user by using a web browser. The webserver also handles the API which connects to the network.

#### "Neural Network":

The network is build up of nodes, there are three different nodes: input nodes, hidden nodes and output nodes. Each node contains weights and a bias that gets adjusted in each generation. When an input is given to the network the input passes all weights and biases so a output is formed.

### Some random information:

* The favicon.ico file was found somewhere online with the information that it was free.
* There are some settnings that could improve the preformance, but be aware the network will improve at a slower pace.
    * networkAmount in networkInterface.java (lower gives better preformance)
    * automaticNewGenerationAmount in color.java (higher gives better preformance)
* Also this program probably does not work as intended.
