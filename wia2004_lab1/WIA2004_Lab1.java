package wia2004_lab1;

import java.util.Arrays;
import java.util.Scanner;

public class WIA2004_Lab1 {
    public static void main(String[] args) {
    
    //Input for the amount of jobs needed 
    Scanner input = new Scanner(System.in);
    System.out.print("Number of Jobs: ");
    int num_jobs = input.nextInt();
    
    //Declaring the array for jobs to be completed by the CPU 
    Job [] task_list = new Job[num_jobs];
    
    //Loop for inputting the job details such as ID, burst time and arrival time
    input.nextLine();
    System.out.println("Job ID, Arrival Time, Burst Time (seperate by space)");
    for (int i = 0; i < num_jobs; i++){
        String line_input = input.nextLine();
        String [] array_input = line_input.split(" ");
        task_list[i] = new Job(array_input[0], Integer.parseInt(array_input[1]), Integer.parseInt(array_input[2]));
    }
    
    //Sorting the list according to the arrival time
    Arrays.sort(task_list);
    
    //Declaring the current time, total waiting time and total turnaround for the FCFS algorithm
    int current_time = 0;
    int total_waiting = 0;
    int total_turnaround = 0;
    
    //For loop to represent the number of tasks to be completed
    for(int i = 0; i < task_list.length; i++) {
        Job current = task_list[i];
        
        //Checking if the CPU is idle and declaring the initial current time
        if (current_time < current.getArrival()) {
            current_time = current.getArrival();
        }
        
        //Calculating the completion, waiting and turnaround times
        int start_time = current_time;
        int completion_time = start_time + current.getBurst();
        int waiting_time = start_time - current.getArrival();
        int turnaround_time = completion_time - current.getArrival();
        
        //Adding the overall waiting and turnaround times
        total_waiting += waiting_time;
        total_turnaround += turnaround_time;
        
        //Setting the current time to the job completion time
        current_time = completion_time;
        
        //Displaying the job details (ID, Arrival, Burst, Start, Completion, Waiting and Turnarond)
        System.out.println("======================================");
        System.out.println("Job: " + current.getID());
        System.out.println("Arrival Time: " + current.getArrival());
        System.out.println("Burst Time: " + current.getBurst());
        System.out.println("Start Time: " + start_time);
        System.out.println("Completion Time: " + completion_time);
        System.out.println("Waiting Time: " + waiting_time);
        System.out.println("Turnaround Time: " + turnaround_time);
        System.out.println("=====================================\n");
    }
    
    //Displaying the total completion time of all jobs, average waiting time and average turnaround time
    System.out.println("Total Completion Time: " + current_time);
    System.out.println("Average Waiting Time: " + (double) total_waiting / task_list.length);
    System.out.println("Average Turnaround Time: " + (double) total_turnaround / task_list.length);
  }
    
}
