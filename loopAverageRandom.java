package demo2;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class loopAverageRandom {

	static long timeSum = 0;
	static long timeAverage = 0;
	static long sum = 0;
	
	static Random random = new Random();
	
	static int max = 214783647;
	static int min = -214783647;
	static int count = 0;
	
	private static List<Integer> numList = new ArrayList<Integer>();
	
	
	public synchronized static void addRandInt(){
		numList.add(random.nextInt(max - min) + min);	
		sum += random.nextInt(max - min) + min;
		count++;
	}
	
	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please give an integer N");
		int N = scan.nextInt();
		for(int k=0; k<100; k++) {
		    Thread[] myThreads = new Thread[N];					
		    long tik = System.nanoTime();
		    for(int i=0; i<N; i++){
			    myThreads[i] = new Thread(new Runnable(){		//creating an array of size N of threads to add on numList
			    	public void run(){
					    while(count < 1000000){
						    addRandInt();
					    }
				    }
			    });
			
			    myThreads[i].start();
			
			    try {											// joining threads to halt unwanted premature execution of runnable
				    myThreads[i].join();						// which would lead to count not being correclty incremented and 
			    } catch (InterruptedException e){				// therefore the array list as well
				    // TODO Auto-generated catch block
				    e.printStackTrace();
			    }
		    }
		    long tok = System.nanoTime();
		
	    	long time = tok-tik;

		    timeSum += time;
		    
		    long average = sum/1000000;
		
		    System.out.println("Array of size " + numList.size());
		    System.out.println("Average value of random list is " + average);
		    System.out.println("Execution time of data entry is " + time + " nanoseconds, or " + time/1000000 + " milliseconds");
		    //System.out.println("Execution time of averaging is " + time2 + " nanoseconds, or " + time2/1000000 + " milliseconds");
		    count = 0;
		    sum = 0;
		    
		}
		timeSum = timeSum/1000000;
		timeAverage = timeSum/100;
	
		
		System.out.println("Array of size " + numList.size());
		System.out.println("Average time over 100 runs is " + timeAverage + " milliseconds");
	}
	
}