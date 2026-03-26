/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wia2004_lab1;

public class Job implements Comparable<Job>{
   private String ProcessID;
   private int ArrivalTime;
   private int BurstTime;
   
   public Job(String ID, int arrival, int burst){
       this.ProcessID = ID;
       this.ArrivalTime = arrival;
       this.BurstTime = burst;
   }
   
   public int getArrival() {
       return this.ArrivalTime;
   }
   
   public int getBurst() {
       return this.BurstTime;
   }
   
   public String getID() {
       return this.ProcessID;
   }
   
   @Override
   public int compareTo(Job other) {
       return Integer.compare(this.ArrivalTime, other.ArrivalTime);
   }
   
}
