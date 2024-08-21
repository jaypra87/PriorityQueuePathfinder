/**
 * @author Jay Prajapati - jprajap3 - 251350172
 * 
 * This class  implements the provided UniquePriorityQueueADT interface and supports the generic type T. This class stores the data and the corresponding priority
 * value that are provided.
 */
public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T> {
	
	private T[] queue;
	private double[] priority;
	private int count;
	
	/**
	 * This constructor initializes the both of the arrays with a capacity of 10 and initializes 'count' with the value of 0;
	 */
	public ArrayUniquePriorityQueue() {
		queue = (T[]) new Object[10];
		priority = new double [10];
		count = 0;
	}
	
	/**
	 * This method checks whether or not the given 'data' item is in the queue. If it is then the method ends immediately. If not then it checks whether or not the
	 * value of 'count' is equal to length of queue. If it is then it adds 5 additional spaced to the array. If not, then it adds the 'data' and its corresponding
	 * priority value to the array.
	 * 
	 * @param data - This is the 'T' value that is to be stored in the 'queue' array.
	 * @param prio - This is the double value of the priority that is to be stored in the 'priority' array.
	 */
	public void add (T data, double prio) {
		
		// Here I am checking to see if 'data' is already in the 'queue' array or not.
		if (contains(data)) {
			return;
		}
		
		if (count == queue.length) {
			T[] newQueue = (T[]) new Object[queue.length + 5];
			double[] newPriority = new double [priority.length + 5];
			
			// Copying the old array into a new array with the updated capacity.
			for (int i = 0; i < count; i++) {
				newQueue[i] = queue[i];
				newPriority[i] = priority[i];
			}
			// Copying the new array into the old array with the updated capacity. This way the old array will have a greater capacity than 10.
			queue = newQueue;
			priority = newPriority;
		}
		
		int i;
		
		// This for loop is created to loop through all of the values in the 'queue' array and 'priority' queue to ensure that they are placed in the correct spot. 
        for (i = count - 1; (i >= 0) && (priority[i] > prio); i--) {
            queue[i + 1] = queue[i];
            priority[i + 1] = priority[i];
        }
        queue[i + 1] = data;
        priority[i + 1] = prio;
        count++;
		
	}
	
	/**
	 * This method checks whether or not the given 'data' item is in the queue array. If it is then it returns 'true', however, if it is not then it returns 'false'.
	 * 
	 * @param data - This is the 'T' value that is to be checked whether or not it already exists in the 'queue' array or not.
	 * @return - This method returns a boolean value, 'true' if the 'data' item is in the 'queue' array and 'false' if the 'data' item is not in the 'queue' array.
	 */
	public boolean contains (T data) {
		
		// This for loop, iterates through every item in the 'queue' array to see if 'data' item already exists in the 'queue' array.
		for (int i = 0; i < count; i++) {
			if (data.equals(queue[i])) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * This method will call the 'isEmpty()' method to check whether or not the 'priority' queue is empty or not. If 'true' then it will throw an exception. If it is
	 * 'false' then it will return the first value in the 'queue' array. 
	 * 
	 * @return - This method returns the first value in the 'queue' array (the value with the lowest priority).
	 */
	public T peek () throws CollectionException {
		
		// Here I am checking to see if the priority queue is empty or not.
		if (isEmpty()) {
			throw new CollectionException("PQ is empty");
		}
		
		return queue[0];
	}
	
	/**
	 * This method is used to remove the item with the smallest priority. First this method checks if the 'priority' queue is empty by calling the 'isEmpty()' method.
	 * After that this method removes the items with the smallest priority and shifts all the other items to the left to fill the empty gap at the front.
	 * 
	 * @return - This method returns the value of the item with the smallest priority.
	 */
	public T removeMin () throws CollectionException {
		
		// Here I am checking to see if the priority queue is empty or not.
		if (isEmpty()) {
			throw new CollectionException("PQ is empty");
		}
		
		T returnValue = queue[0];
		
		// This 'for loop' moves all of the items of the two arrays one step to the left to fill in the gap after removing the item with the smallest priority.
		for (int i = 0; i < count; i++) {
			queue[i] = queue[i + 1];
			priority[i] = priority[i + 1];
		}
		
		count--;
		return returnValue;
		
	}
	
	/**
	 * This method has been implemented to update the priority of an already existing item in the queue. This method first checks if the 'data' item already exists
	 * in the queue. If it does not, then it will throw a CollectionException. If it does exist in the queue then the corresponding priority of the 'data' item is
	 * changed.
	 * 
	 * @param data - This parameter represents the value of the item in the queue array for which we have to update the priority.
	 * @param newPrio - This parameter represents the new priority of the corresponding item in the queue array.
	 */
	public void updatePriority (T data, double newPrio) throws CollectionException {
		
		// Here I am checking to see if 'data' is NOT in the 'queue' array.
		if (!contains(data)) {
			throw new CollectionException("Item not found in PQ");
		}
		
		/* This 'for loop' iterates through all of the values in the 'queue' array and 'priority' until the index is found where 'data' equals and already existing
		* item in the array. This 'for loop' then makes room for the priority change to occur by moving the existing items to the left.
		*/
		for (int i = 0; i < count; i++) {
			if (queue[i].equals(data)) {
				
				int index = i;
				
				for (int j = index; j < count - 1; j++) {
					queue[j] = queue[j + 1];
					priority[j] = priority[j + 1];
				}
				count--;
				add(data, newPrio);
			}
		}
	}
	
	/**
	 * This method is designed to check whether or not the 'priority' queue is empty. This method compares the value of count to 0. If 'true' than the 'if block' 
	 * executes.
	 * 
	 * @return - This method will return 'true' if the priority queue is empty and will return 'false' if the 'priority' queue is not empty.
	 */
	public boolean isEmpty() {
		if (count == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method is implemented to return the number of items stored in the 'priority' queue.
	 * 
	 * @return - This method will return the integer value of 'count' as this is the value that represents the number of values in the 'priority' queue.
	 */
	public int size() {
		return count;
	}
	
	
	/**
	 * This method is designed to return the length of the 'queue' array.
	 * 
	 * @return - This method returns the value of the length of the 'queue' array.
	 */
	public int getLength() {
		return queue.length;
	}
	
	
	/**
	 * This method returns a string that contains each item from the 'queue' array followed by the corresponding value in the 'priority' queue. First, this method 
	 * checks whether or not the 'priority' queue is empty. If it is empty then a message is returned that reads 'The PQ is empty'. If the 'priority' queue is not 
	 * empty then a StringBuilder object called 'string' is created. This object is appended into, and the items in the 'queue' array followed by the corresponding
	 * values in 'priority' queue are added to it.
	 * 
	 * @return - This method returns a string that contains each item from the 'queue' array followed by the corresponding value in the 'priority' queue.
	 */
	public String toString() {
		if (isEmpty()) {
			return "The PQ is empty";
		} 
		
		StringBuilder string = new StringBuilder();
		
		// This 'for loop' goes through all of the items in the 'queue' array and 'priority' queue and prints them out accordingly. 
		for (int i = 0; i < count; i++) {
			string.append(queue[i] + " ");
			string.append("[" + priority[i] + "]");
			// This 'if statement' is to ensure that a comma is not printed after the last item in the arrays.
			if (i < count - 1) {
                string.append(", ");
            }
		}
		
		return string.toString();
	}
	
	
}
