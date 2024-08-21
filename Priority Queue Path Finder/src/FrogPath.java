/**
 * @author Jay Prajapati - jprajap3 - 251350172
 * 
 * This class has been created to determine the path that Freddy The Frog must follow to get from the starting cell to the end cell. 
 */
public class FrogPath {

	private Pond pond;
	
	/**
	 * This constructor initializes the 'pond' with the given filename. This constructor will throw an Exception if the filename is not found.
	 * 
	 * @param filename - This parameter includes the name of the 'pond' that needs to be opened.
	 */
	public FrogPath (String filename) {
		
		try {
			// Initializing 'pond' using the given filename.
			pond = new Pond(filename);
			
		} catch (Exception exception) {
			System.out.println("File Not Found");
		}
	}
	
	/**
	 * This method method takes in the current Hexagon cell, 'currCell', and uses an ArrayUniquePriorityQueue (UPQ) object to store all possible cells that can be 
	 * reached from 'currCell' and each one must be given a priority which will determine where in the UPQ it should be placed (the item with the smallest priority 
	 * must go at the front and each subsequent item must have an equal or larger priority to the item before it)
	 * 
	 * @param currCell - This parameter is the current cell that Freddy the Frog is sitting on.
	 * @return - This method returns a type 'Hexagon' which is either the first element in the priority queue or 'null' if the queue is empty.
	 */
	public Hexagon findBest(Hexagon currCell) {
		
		ArrayUniquePriorityQueue<Hexagon> priorityQueue = new ArrayUniquePriorityQueue<Hexagon>();
		
		// With this 'for loop' I am checking all the adjacent neighboring cells to the current cell.
		for (int i = 0; i < 6; i++) {
			
			try {
				Hexagon neighbour = currCell.getNeighbour(i);
				
				// Here I am ensuring that I only consider the neighbor if it is not null, hasn't been marked, is not a mud cell, not an alligator cell, and no alligator is near
				if (neighbour != null && !neighbour.isMarked() && !neighbour.isMudCell() && !neighbour.isAlligator() && !isAlligatorNear(neighbour)) {
					
					// Calculating the priority for the neighboring cell
					double priority = calculatePriority(false, currCell.getNeighbour(i));
					// Adding the cell and its priority to the priority queue.
					priorityQueue.add(neighbour, priority);
				}
			} catch(InvalidNeighbourIndexException exception) {
				System.out.println(exception.getMessage());
			}
		}
		
	    if (currCell.isLilyPadCell()) {
	    	
	    	// With his 'for loop' I am looping through all of the neighboring cells
	        for (int i = 0; i < 6; i++) {
	        	
	            try {
	                Hexagon neighbour = currCell.getNeighbour(i);
	                if (neighbour != null && !neighbour.isMarked() && !isAlligatorNear(neighbour)) {
	                	
	                    for (int j = 0; j < 6; j++) {
	                    	
	                        try {
	                            Hexagon nextNeighbour = neighbour.getNeighbour(j);
	                            
	                            // Here I am ensuring that I only consider the neighbor if it is not null, hasn't been marked, is not a mud cell, not an alligator cell, and no alligator is near
	                            if (nextNeighbour != null && !nextNeighbour.isMarked() && !nextNeighbour.isMudCell() && !neighbour.isAlligator() && !isAlligatorNear(nextNeighbour)) {
	                            	
	                            	// Determining if the move is diagonal relative to the original cell
	                            	boolean isDiagonal = !(j == i || j == (i + 3) % 6);
	                            	
	                            	// Calculating the priority for the neighboring cell
	                                double priority = calculatePriority(isDiagonal, nextNeighbour);
	                                
	                                // Adding the cell and its priority to the priority queue.
	                                priorityQueue.add(nextNeighbour, priority);
	                            }
	                            
	                        } catch (InvalidNeighbourIndexException exception) {
	                            System.out.println(exception.getMessage());
	                        }
	                    }
	                }
	            } catch (InvalidNeighbourIndexException exception) {
	                System.out.println(exception.getMessage());
	            }
	        }
	    }
		
	    // If the priority queue is not empty, then I am returning the cell with the highest priority. If it is empty, then 'null' is being returned.
		if (!priorityQueue.isEmpty()) {
			return priorityQueue.peek();
		} else {
			return null;
		}
		
	}
	
	/**
	 * This method is a private helper method used by the 'findBest()' method. This method is implemented to calculate the priorities of the different cells. 
	 * Depending on the type of the cell, each of the cells will receive their calculated priority.
	 * 
	 * @param diagonal - This boolean parameter is 'true' if the cell is straight from the lilypad cell and is 'false' if the cell is not straight from the lilypad cell.
	 * @param cell - This parameter is the cell for which we are setting the priority. 
	 * @return - The integer value of the priority, 'priorityOfCells' is returned to the function 'findBest()'. 
	 */
	private double calculatePriority(boolean diagonal, Hexagon cell) {
		
		double priorityOfCell = 0.0;
		
		/* Here I am checking to see if there are flies on the cell. If there are flies, than the priority is calculated a little differently depending on the 
		* number of flies.
		*/
		if (cell instanceof FoodHexagon) {
			
			int numberOfFlies = ((FoodHexagon) cell).getNumFlies();
			
			if (numberOfFlies == 3) {
				priorityOfCell = 0.0;
			} else if (numberOfFlies == 2) {
				priorityOfCell = 1.0;
			} else if (numberOfFlies == 1) {
				priorityOfCell = 2.0;
			}
			
		} else if (cell.isEnd()) {
			
			priorityOfCell = 3.0;
			
		} else if (cell.isLilyPadCell()) {
			
			priorityOfCell = 4.0;
			
		} else if (cell.isReedsCell() && isAlligatorNear(cell)) { // Here I am calling 'isAlligatorNear()' to ensure if the near the reed cell or not since there is a different priority value
			
			priorityOfCell = 10.0;
			
		} else if (cell.isReedsCell()) {
			
			priorityOfCell = 5.0;
			
		} else if (cell.isWaterCell()) {
			
			priorityOfCell = 6.0;
			
		}
		
		// If the cell is diagonal, then 1.0 will be added to 'priorityOfCell', but if it is not diagonal, then 0.5 will be added to 'priorityOfCell'.
		if (diagonal) {
            priorityOfCell += 1.0;
        } else {
            priorityOfCell += 0.5;
        }

        return priorityOfCell;
        
	}
	
	/**
	 * This method is a private helper method that I created to help calculate the priority of the cells. This method checks to see whether or not there is a alligator
	 * in the neighboring cells and returns a boolean value accordingly.
	 * 
	 * @param cell - This parameter represents the cell that Freddy might possibly move to if there is no alligator and if the priority is the lowest.
	 * @return - This method returns a boolean value, either 'true' or 'false'. If there is an alligator and the 'if block' evaluates to true, then 'true' is returned, 
	 * but if the 'if block' evaluates to 'false' then 'false' is returned.
	 */
	private boolean isAlligatorNear(Hexagon cell) {

		/* This 'for loop' is iterating through all of the neighbors of the cell 'cell' and checking if there is an alligator or not. If there is an alligator, then
		* 'true' is returned, if not, then 'false' is returned.
		*/
	    for (int i = 0; i < 6; i++) {
	        try {
	            Hexagon neighbour = cell.getNeighbour(i);
	            if (neighbour != null && neighbour.isAlligator()) {
	                return true;
	            }
	        } catch (InvalidNeighbourIndexException exception) { // Here I am ensuring to catch the Exception if the neighbor index is not correct.
	            System.out.println(exception.getMessage());
	        }
	    }
	    return false;
	}

	
	
	
	/**
	 * This method is created to keep track of the cells that the frog has visited from the starting cell towards the end cell. Then this method goes on to return a
	 * string of Freddy's path. This includes the cell's ID of every cell that Freddy visit's in his path.
	 * @return
	 */
	public String findPath() {
        
		ArrayStack<Hexagon> S = new ArrayStack<Hexagon>();
		S.push(pond.getStart());
		S.peek().markInStack();
		int fliesEaten = 0;
		
		StringBuilder path = new StringBuilder();
		
		// While the stack is not empty, continue executing the 'while loop'.
		while (!S.isEmpty()) {
			
			// Here I am just adding to the string with whatever is required.
			Hexagon curr = S.peek();
			path.append(curr.getID() + " ");
			
			// If the current cell is the end cell, then the number of flies eaten will be returned and the program will end.
			if (curr.isEnd()) {
				path.append("ate " + fliesEaten + " flies");
	            return path.toString();
			}
			
			// If the current cell has flies, then 'fliesEaten' will be incremented accordingly.
			if (curr instanceof FoodHexagon) {
				fliesEaten += ((FoodHexagon) curr).getNumFlies();
				((FoodHexagon) curr).removeFlies();
			}
			
			Hexagon next = findBest(curr);
			
			// If there is no next cell, then we must backtrack by popping the stack. If there is then we can move to the next cell by pushing into it.
			if (next == null) {
				S.pop();
				curr.markOutStack();
			} else {
				S.push(next);
				next.markInStack();
			}
		}
		
		/* If the stack is empty, then the number of flies eaten is returned and the program ends. But if the stack is not empty AND there is no solution, then 
		*  "No solution" is returned. As can be seen, if everything else above is finished running (and the program still has no solution) then the return statement
		*  below gets executed.
		*/
		return "No solution";
		
    }

	
}
