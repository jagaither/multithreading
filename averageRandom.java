package unsynchronized;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class averageRandom {
	
	static Random random = new Random();
	
	private static int numArr[] = new int[1000000];
	//private static List<Integer> countList = new ArrayList<Integer>();	//Either keep countList up here and concatenate or assign memory after taking 
	static long sum = 0;													//in integer and finding optimal value for N
	static int randNum = 0;
    static int remainder = 0;
	static int maxIndiCount = 0;
    static int countListIndex = 0;
	
	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please give an integer N for the number of threads");		//Receive integer input from the user for number of threads
		int N = scan.nextInt();		
		while((1000000 % N)!=0) {								//MAKES N EVENLY DIVISIBLE INTO 1 MILLION
			N++;
		}
		maxIndiCount = 1000000/N;								//obtain max count for each thread so that they share workload equally
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
						//System.out.println(countListIndex);
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
        long average = sum/1000000;								//obtain average from sum
		
        scan.close();
		
		System.out.println("Array of size " + numArr.length);
		System.out.println("Average value of random list is " + average);
		System.out.println("Execution time of data entry is " + time + " nanoseconds, or " + time/1000000 + " milliseconds");
		
	}
}