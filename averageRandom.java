package demo2;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class averageRandom {
	
	static Random random = new Random();
	static int max = 214783647;
	static int min = -214783647;
	
	private static List<Integer> numList = new ArrayList<Integer>();
	static int count = 0;
	
	public synchronized static void addRandInt(){
		numList.add(random.nextInt(max - min) + min);	
		count++;
	}
	
	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please give an integer N");
		int N = scan.nextInt();
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
		
		
		long sum = 0;
		
		long tik2 = System.nanoTime();
        for(int i=0; i<1000000; i++) {						// getting sum
        	sum += numList.get(i);
        }
        long average = sum/1000000;
		long tok2 = System.nanoTime();
		
		
		long time = tok-tik;
		long time2 = tok2-tik2;
		
		
		System.out.println("Array of size " + numList.size());
		System.out.println("Average value of random list is " + average);
		System.out.println("Execution time of data entry is " + time + " nanoseconds, or " + time/1000000 + " milliseconds");
		System.out.println("Execution time of averaging is " + time2 + " nanoseconds, or " + time2/1000000 + " milliseconds");
	}
	
}