package unsynchronized;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;  

public class loopAverageRandom {
	
	private final static int milHold = 1000000;
	static long timeSum = 0;
	static long timeAverage = 0;
	int remainder = 0;
	static int maxIndiCount = 0;
	static int countListIndex = 0;
	private static int originalN = 0;
	
	static boolean flag = false;
    
	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please give an integer N for the number of threads");		//Receive integer input from the user for number of threads
		int N = scan.nextInt();
		originalN = N;
		while((milHold % N)!=0) {														//MAKES N EVENLY DIVISIBLE INTO 1 MILLION
			N++;
			flag = true;
		}
		if(flag==true) {																//bool flag to see if input from user has been altered to be evenly divisible
			System.out.println("Original number of threads has been changed from " + originalN +"to be evenly divisible into 1,000,000 for equal workload between threads");
			System.out.println("New number of threads is: " + N);
		}
		maxIndiCount = milHold/N;														//obtain max count for each thread so that they share workload equally
		
		System.out.println("Please give an integer for the number of loops to run random data entry for");
		int numLoops = scan.nextInt();
		//-------------------------------------------------------------------------
		// BEGINNING OF TIMING PORTION
		//-------------------------------------------------------------------------
		for(int k=0; k<numLoops; k++) {
			int numArr[] = new int[milHold];
			int sumArr[] = new int[N];
			int randHoldArr[] = new int[N];
			
			List<Integer> countList = new ArrayList<Integer>();		//Initialize countList so that each thread can maintain its own count to 
			int incrementVal = 0;
			for(int i=0; i<N; i++) {								//eliminate synchronization and extra function calls
				countList.add(incrementVal);
				incrementVal += maxIndiCount;
			}
			
			Thread[] myThreads = new Thread[N];					
			long tik = System.nanoTime();
			for(int i=0; i<N; i++){
				countListIndex = i;
				myThreads[i] = new Thread(new Runnable(){			//creating an array of size N of threads to enter random integers into numList
						public void run(){
							while(countList.get(countListIndex) < maxIndiCount){
								randHoldArr[countListIndex] = ThreadLocalRandom.current().nextInt(); //using thread local random and an array matching countList so each 
								numArr[countList.get(countListIndex)] = randHoldArr[countListIndex]; //each thread has its own memory location for entry of the random number
								sumArr[countListIndex] += randHoldArr[countListIndex];	
								countList.set(countListIndex, countList.get(countListIndex)+1);
							}
						}
				});
			
				myThreads[i].start();							
			
				try {												//joining threads to halt unwanted premature execution of runnable
					myThreads[i].join();							//which would lead to count not being correclty incremented and 
				} catch (InterruptedException e){					//therefore the array list as well
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			long tok = System.nanoTime();
            //-------------------------------------------------------------------------
            // END OF TIMING PORTION
            //-------------------------------------------------------------------------
		    
		    
			long time = tok-tik;
		    timeSum += time;
		    long average = 0;								//obtain average from sum
		    for(int j=0; j<N; j++) {
		    	average+=sumArr[j];
		    }
		    
		    average = average/milHold;
            
		    System.out.println("Array of size " + numArr.length);
		    System.out.println("Average value of random list is " + average);
		    System.out.println("Execution time of data entry is " + time + " nanoseconds, or " + time/milHold + " milliseconds" + '\n');
		}
		
		timeSum = timeSum/milHold;									//Get time sum in nanoseconds to milliseconds
		timeAverage = timeSum/numLoops;
		System.out.println('\n' + "Average time over 100 runs is " + timeAverage + " milliseconds");
		
		scan.close();
	}
}