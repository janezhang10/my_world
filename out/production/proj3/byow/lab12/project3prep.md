# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: My original implementation involved creating hexagons from top to bottom and switching between 1, 2, 3 hexagons per row, but that was
        a lot more complicated than it needed to be. I learned from the staff solution that I should look at the problem from different angles, 
        and create hexagons by columns rather than by rows. 

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: The hexagon is the number of rooms and the tessalation is how the rooms and hallways are connected to each other. 

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:  I would first think about the construction of the world first and work my way backwards through helper methods. 
        I use a top down approach and use unwritten helper methods to get a better sense of how I want the overall program to composed and
        think about how to implement the smaller methods after the fact.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: A room is just a series of adjacent hallways with the touching walls broken down.
