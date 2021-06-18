package unsynchronized;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class loop {
	
	static Random random = new Random();
	
	//private static int numArr[] = new int[1000000];				//changed numList to numArr so that elements can be changed and accessed faster with a larger dataset
	static long sum = 0;	
	static long timeSum = 0;
	static long timeAverage = 0;
	static int randNum = 0;
    static int remainder = 0;
	static int maxIndiCount = 0;
    static int countListIndex = 0;
	static int originalN = 0;
	
	static boolean flag = false;
    
	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please give an integer N for the number of threads");		//Receive integer input from the user for number of threads
		int N = scan.nextInt();
		originalN = N;
		while((1000000 % N)!=0) {								//MAKES N EVENLY DIVISIBLE INTO 1 MILLION
			N++;
			flag = true;
		}
		if(flag==true) {										//bool flag to see if input from user has been altered to be evenly divisible
			System.out.println("Original number of threads has been changed to be evenly divisible into 1,000,000 for equal workload between threads");
			System.out.println("New number of threads is: " + N);
		}
		maxIndiCount = 1000000/N;								//obtain max count for each thread so that they share workload equally
		
		//List<Integer> countList = new ArrayList<Integer>();		//Initialize countList so that each thread can maintain its own count to 
		//int incrementVal = 0;
		//for(int i=0; i<N; i++) {								//eliminate synchronization and extra function calls
		//	countList.add(incrementVal);
		//	incrementVal += maxIndiCount;
		//}
		
		for(int k=0; k<100; k++) {
			int numArr[] = new int[1000000];
		  
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
						    randNum = random.nextInt();
						    numArr[countList.get(countListIndex)] = randNum;
						    sum+=randNum;
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
		    long time = tok-tik;
		    timeSum += time;
            long average = sum/1000000;								//obtain average from sum
		
            
		    System.out.println("Array of size " + numArr.length);
		    System.out.println("Average value of random list is " + average);
		    System.out.println("Execution time of data entry is " + time + " nanoseconds, or " + time/1000000 + " milliseconds");
		}
		timeSum = timeSum/1000000;									//Get time sum in nanoseconds to milliseconds
		timeAverage = timeSum/100;
		System.out.println("Average time over 100 runs is " + timeAverage + " milliseconds");
	}
}