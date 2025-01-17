package Main;

import java.lang.*;
import java.io.*;
import java.util.*;

public class TicketBooker {
	
	//63 berths(upper ,lower , middle)  + ( 18 RAC passengers) 
    //10 waiting list tickets ->21 L, 21 M, 21U , 18RAC, 10WL
    static int availableLowerBerths = 2;//normally 21
    static int availableMiddleBerths = 2;//normally 21
    static int availableUpperBerths = 2;//normally 21
    static int availableRacTickets = 2;//normally 18
    static int availableWaitingList = 2;//normally 10
    
    // ************************NOTE QUEUE SYNTAX*********************

    static Queue<Integer> waitingList = new LinkedList<>();//queue of WL passengers
    static Queue<Integer> racList =  new LinkedList<>();//queu of RAC passengers
    static List<Integer> bookedTicketList =  new ArrayList<>();//list of bookedticket passengers

    static List<Integer> lowerBerthsPositions = new ArrayList<>(Arrays.asList(1,2));//normally 1,2,...21
    static List<Integer> middleBerthsPositions = new ArrayList<>(Arrays.asList(1,2));//normally 1,2,...21
    static List<Integer> upperBerthsPositions = new ArrayList<>(Arrays.asList(1,2));//normally 1,2,...21
    static List<Integer> racPositions = new ArrayList<>(Arrays.asList(1,2));//normally 1,2,...18
    static List<Integer> waitingListPositions = new ArrayList<>(Arrays.asList(1));//normally 1,2,...10

    // ************************NOTE MAP00000000000000000000000000000 SYNTAX*********************
    
    static Map<Integer, Passenger> passengers = new HashMap<>();//map of passenger ids to passengers

    public void bookTicket(Passenger p, int berthInfo,String allotedBerth)
    {   	
    	p.number = berthInfo;
        p.alloted = allotedBerth;
        // add passenger to the map
        passengers.put(p.passengerId,p);
        //add passenger id to the list of booked tickets
        bookedTicketList.add(p.passengerId);        
        System.out.println("--------------------------Booked Successfully   Your Id is "+p.passengerId);
		// TODO Auto-generated method stub
		
	}
    
    public void addToRAC(Passenger p,int racInfo,String allotedRAC)
    {
        //assign seat number and type(RAC)
        p.number = racInfo;
        p.alloted = allotedRAC;
        // add passenger to the map
        passengers.put(p.passengerId,p);
        //add passenger id to the queue of RAC tickets
        racList.add(p.passengerId);
        //decrease available RAC tickets by 1    
        availableRacTickets--;
        //remove the position that was alloted to the passenger
        racPositions.remove(0);

        System.out.println("--------------------------added to RAC Successfully");
    }
    
  //adding to WL
    public void addToWaitingList(Passenger p,int waitingListInfo,String allotedWL)
    {
        //assign seat number and type(WL)
        p.number = waitingListInfo; 
        p.alloted = allotedWL;
        // add passenger to the map
        passengers.put(p.passengerId,p);
        //add passenger id to the queue of WL tickets
        waitingList.add(p.passengerId);
        //decrease available WL tickets by 1    
        availableWaitingList--;
        //remove the position that was alloted to the passenger
        waitingListPositions.remove(0);

        System.out.println("-------------------------- added to Waiting List Successfully");
    }
    
    public void cancelTicket(int passengerId)
    {
        //remove the passenger from the map before that we have to store
        Passenger p = passengers.get(passengerId);       
        passengers.remove(Integer.valueOf(passengerId));
        
        bookedTicketList.remove(Integer.valueOf(passengerId));
        // i want to delete index along with objecct in hashmap and so i am using Integer.valueOf(id));
        
        int positionBooked = p.number;

        System.out.println("---------------cancelled Successfully");

        //both  p.alloted and L are string and so using equals
        if(p.alloted.equals("L"))
        { 
          availableLowerBerths++;
          lowerBerthsPositions.add(positionBooked);
        }
        else if(p.alloted.equals("M"))
        { 
          availableMiddleBerths++;
          middleBerthsPositions.add(positionBooked);
        }
        else if(p.alloted.equals("U"))
        { 
          availableUpperBerths++;
          upperBerthsPositions.add(positionBooked);
        }

        //check if any RAC is there
        if(racList.size() > 0)
        {
            //to take first in from queue we have to use poll
            Passenger passengerFromRAC = passengers.get(racList.poll());
            
            //************************************************
            int positionRac = passengerFromRAC.number;
            racPositions.add(positionRac);
            racList.remove(Integer.valueOf(passengerFromRAC.passengerId));
            availableRacTickets++;      

            //check if any WL is there
            if(waitingList.size()>0) {
                //take the passenger from WL and add them to RAC , increase the free space in waiting list and 
                //increase available WL and decrease available RAC by 1
                Passenger passengerFromWaitingList = passengers.get(waitingList.poll());
                int positionWL = passengerFromWaitingList.number;
                waitingListPositions.add(positionWL);
                waitingList.remove(Integer.valueOf(passengerFromWaitingList.passengerId));

                passengerFromWaitingList.number = racPositions.get(0);
                passengerFromWaitingList.alloted = "RAC";
                racPositions.remove(0);
                racList.add(passengerFromWaitingList.passengerId);
                
                availableWaitingList++;
                availableRacTickets--;
            }
            // now we have a passenger from RAc to whom we can book a ticket, 
            //so book the cancelled ticket to the RAC passenger
            Main.bookTicket(passengerFromRAC);
            // to avoid code repetion
        }
    
    }
    
    public void printAvailable()
    {
        System.out.println("Available Lower Berths "  + availableLowerBerths);
        System.out.println("Available Middle Berths "  + availableMiddleBerths);
        System.out.println("Available Upper Berths "  + availableUpperBerths);
        System.out.println("Availabel RACs " + availableRacTickets);
        System.out.println("Available Waiting List " + availableWaitingList);
        System.out.println("--------------------------");
    }
    
    public void printPassengers()
    {
        if(passengers.size() == 0)
        {
            System.out.println("No details of passengers");
            return;
        }
        for(Passenger p : passengers.values())
        {
            System.out.println("PASSENGER ID " + p.passengerId );
            System.out.println(" Name " + p.name );
            System.out.println(" Age " + p.age );
            System.out.println(" Status " + p.number + p.alloted);
            System.out.println("--------------------------");
        }
    }

	public TicketBooker() {
		// TODO Auto-generated constructor stub
	}


	

}
