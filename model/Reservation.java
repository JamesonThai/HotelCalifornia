/**
 * Reservation.java: Represent a reservation made by a guest
 * Author: Kim Pham
 */


package model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



public class Reservation implements Comparable<Reservation>{
	private LocalDate checkin;
	private LocalDate checkout;
	private TYPE roomType;
	private int roomNumber;
	private String username;
	
	public Reservation(LocalDate checkin, LocalDate checkout, TYPE roomType, int roomNumber, String username){
		this.checkin = checkin;
		this.checkout = checkout;
		this.roomType = roomType;
		this.roomNumber = roomNumber;
		this.username = username;
	}
	
	//get length of stay for this reservation
	public int getLengthOfStay(){
		return (int)ChronoUnit.DAYS.between(checkin, checkout);
	}
	
	//static method to computer length of stay for any 2 local date
	public static long getLengthOfStay(LocalDate checkin, LocalDate checkout){
		return  ChronoUnit.DAYS.between(checkin, checkout);
	}
	
	//return the price for this reservation
	public double getPrice(){
		return roomType.value()*this.getLengthOfStay();
	}
	
	//check if reservation contains a specific date
	//if yes then it means on that date is already reserved
	//used for checking if a room is available that date
	public boolean contains(LocalDate date){
		return (checkin.isBefore(date) && checkout.isAfter(date));
	}
	
	//check if this date range overlap a reservation
	//used to filter out rooms available in this date range(no reservation overlaps)
	public boolean isInRange(LocalDate date1, LocalDate date2){
		///    date1 & date2
		boolean overlap = false;
		if(checkin.equals(date1) || checkin.equals(date2) || checkout.equals(date1) || checkout.equals(date2))
			overlap = true;
		else if(checkin.isBefore(date1) && checkout.isAfter(date1))
			overlap = true;
		else if(checkin.isAfter(date1) && checkin.isBefore(date2))
			overlap = true;
		return overlap;
	}
	
	
	public String toString(){
		String line = "";
		line+= roomType +" "+roomNumber+"\n";
		line+= "Checkin "+checkin.toString()+"\n";
		line+= "Checkout "+checkout.toString()+"\n";
		line+= "Guest: "+username+"\n";
		return line;
	}
	
	//this short toString version is meant to use later when writing to file
	public String shortToString(){
		String line = roomType+"/"+roomNumber+"/"+checkin.toString()+"/"+checkout.toString()+"\n";
		return line;
	}
	
	@Override //used to sort reservation base on checkin date
	public int compareTo(Reservation o) {
		return checkin.compareTo(o.checkin);
	}
	
	public boolean equals(Object other){
		if(this == other) return true;
		if(other==null) return false;
		if(this.getClass() != other.getClass()) return false;
		
		Reservation r = (Reservation) other;
		return (checkin.equals(r.checkin) && checkout.equals(r.checkout) 
				&& roomType == r.roomType && roomNumber==r.roomNumber 
				&& username.equals(r.username));
	}
	
	//all getters and setters, since LocalDate is immutable, it's ok to return
	public LocalDate getCheckin() {
		return checkin;
	}
	public void setCheckin(LocalDate checkin) {
		this.checkin = checkin;
	}
	public LocalDate getCheckout() {
		return checkout;
	}
	public void setCheckout(LocalDate checkout) {
		this.checkout = checkout;
	}
	public TYPE getRoomtype() {
		return roomType;
	}
	public void setRoomtype(TYPE roomtype) {
		this.roomType = roomtype;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	
}
