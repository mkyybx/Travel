# Travel Planning System
A simple travel planning system
***
## The requirement of the project:

> There are three ways of transportation between cities (cars, trains and airplanes). Sometime a passenger makes a travel request to the system, and the system needs to design a travel route according to the needs of the passenger. In addition, one can use the system to inquire the current location of the passenger and his status (the city where he stays or the transportation way which he uses).

The travel system can give the travel plans based on three strategies:
* Lowest price
* Shortest time
* Lowest price given the limitation of time
***
We designed this system using the modified Dijkstra algorithm(for the first two strategies) and A-star algorithm(for the last strategy), but they are not efficient enough because when the number of city exceeds 13, I cannot get the planning result within one minute, though the algorithms have been adapted to parallel computing(multi-thread). So I am willing to hear any creative ideas that can solve this problem efficiently.
