# Block Game - Recursive Board Simulation  

This project implements a **recursive block-based board game**, where players generate random boards, manipulate blocks through transformations, and calculate scores based on different game objectives.  

## Features  
- **Board Generation:** Create randomized game boards using a **quad-tree** structure.  
- **Block Manipulation:** Perform **reflections, rotations, and smashes** to modify the board layout.  
- **Block Selection:** Retrieve blocks at specific levels based on user interactions.  
- **Scoring System:** Compute scores for **perimeter-based** and **blob-based** goals using a **flattened** board representation.  

## How to Use  
1. **Initialize a Board** – Create a **Block** with a level and max depth.  
2. **Set Position & Size** – Use **updateSizeAndPosition()** to assign correct values.  
3. **Modify Blocks** – Use **reflect(), rotate(), smash()** to alter the board.  
4. **Render the Board** – Retrieve drawable elements with **getBlocksToDraw()**.  
5. **Compute Scores** – Use **PerimeterGoal.score()** and **BlobGoal.score()** to track progress.  

This game demonstrates **recursive structures and algorithmic transformations**, providing an interactive board simulation with dynamic gameplay mechanics.
