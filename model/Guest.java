/**
 * Guest.java: Represent a guest reserving a room
 * Author: Kim Pham
 */

package model;

import java.util.ArrayList;
/*
 * Guest model
 */
public class Guest {
	private String username;
	private int userId;
	private ArrayList<Reservation> rsvp; //list of reservation this user made

	public Guest(String name, int id){
		username = name;
		userId = id;
		rsvp = new ArrayList<>();
	}
	
	public ArrayList<Reservation> getAllRsvp(){
		return rsvp;
	}
	
	//return username
	public String getUsername(){
		return username;
	}
	
	//return userId
	public int getId(){
		return userId;
	}
	
	//add a reservation to user
	//Note: only used in HotelReservationModel, should not be call somewhere by itself
	//Note: when call Guest.add, also call Room[number].add
	public boolean addReservation(Reservation re){
		if(rsvp.contains(re))
			return false;
		else{
			rsvp.add(re);
			return true;
		}
	}
	
	//same for add
	public void deleteReservation(Reservation re){
		rsvp.remove(re);
	}
	
	//return user's reservations as array of String containing info
	public String[] getUserReservation(){
		String[] list = new String[rsvp.size()];
		for(int i = 0; i < rsvp.size(); i++){
			list[i] = rsvp.get(i).toString()+"Price: $"+rsvp.get(i).getPrice();
		}
		return list;
	}
	
	//get total charge for all reservations in user's rsvp list
	public double getTotalCharge(){
		double sum = 0;
		for(int i = 0; i < rsvp.size(); i++){
			sum+= rsvp.get(i).getPrice();
		}
		return sum;
	}
	
	//override equal method
	//user is considered equal when their name and id match only (no rsvp)
	//it's made this way to make looking up for user in Sign in possible
	public boolean equals(Object other){
		if(this==other) return true;
		if(other==null) return false;
		if(this.getClass()!=other.getClass()) return false;
		
		Guest g = (Guest) other;
		return username.equals(g.username) && userId == g.userId;
	}
	
	public String[] getUserReservationCompactList(){
		String[] list = new String[rsvp.size()];
		for(int i = 0; i < rsvp.size(); i++){
			list[i] = rsvp.get(i).shortToString();
		}
		return list;
	}
	
}
