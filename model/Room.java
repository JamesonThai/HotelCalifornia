/**
 * Room.java: Represent a room in the hotel
 * Author: Kim Pham
 */


package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represent 20 rooms: 10 luxury vs 10 economic
 */
//enum of LUXURY and ECONOMIC
enum TYPE{
	LUXURY(200.00), ECO(80.00);
	private double price;
	
	private TYPE(double v){
		price = v;
	}
	
	public double value(){
		return price;
	}
}

public class Room {
	TYPE type;  
	int number;
	ArrayList<Reservation> rsvp;
	
	public Room(TYPE type, int number){
		this.type = type;
		this.number = number;
		rsvp = new ArrayList<>();
	}
	
	//return room name in format TypeNumber, eg LUXURY1
	public String getRoomNumber(){
		return type.name()+number;
	}
	
	//add to room a reservation, only used it in HotelReservationModel alongside
	// with guest.add
	public boolean addReservation(Reservation re){
		if(rsvp.contains(re))
			return false;
		else {
			rsvp.add(re);
			return true;
		}
	}
	
	//same to add
	public void deleteReservation(Reservation re){
		rsvp.remove(re);
	}
	
	//check if this room is available for a date range
	//note: this method utilize Reservation.isInRange
	public boolean isAvailableForRange(LocalDate date1, LocalDate date2){
		boolean conflict = false;
		for(int i = 0; i < rsvp.size(); i++){
			if(rsvp.get(i).isInRange(date1, date2)){
				conflict = true;
				return !conflict;
			}
		}
		return !conflict;
	}
	
	//check if this room is available for this date
	//note: utilize Reservation.contains
	public boolean isAvailableForDate(LocalDate date){
		boolean conflict = false;
		for(int i = 0; i < rsvp.size(); i++){
			if(rsvp.get(i).contains(date)){
				conflict = true;
				return !conflict;
			}
		}
		return !conflict;
	}
	
	//get reservations for this rooms as array of String
	public String[] getReservationList(){
		//sort the list by checkout date before returning
		rsvp.sort((r1,r2)->{
			return r1.compareTo(r2);
		});
		
		String[] list = new String[rsvp.size()];
		for(int i = 0; i < rsvp.size(); i++){
			list[i] = rsvp.get(i).toString();
		}
		return list;
	}
	
	//get total price for this room 200 vs 80
	public double getPrice(){
		return type.value();   //LUXURY.value = 200, ECO.value = 80
	}
}
