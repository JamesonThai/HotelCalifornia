/**
 * Reservation.java: Represent a reservation made by a guest
 * Author: Kim Pham, Jameson Thai
 */

package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation implements Comparable<Reservation> {
	private LocalDate checkin;
	private LocalDate checkout;
	private TYPE roomType;
	private int roomNumber;
	private String username;

	/**
	 * Reservation Constructor
	 * 
	 * @param checkin
	 *            CheckInDate
	 * @param checkout
	 *            CheckOutDate
	 * @param roomType
	 *            roomType
	 * @param roomNumber
	 *            roomNumber
	 * @param username
	 *            name of the Guest trying to book
	 */
	public Reservation(LocalDate checkin, LocalDate checkout, TYPE roomType, int roomNumber, String username) {
		this.checkin = checkin;
		this.checkout = checkout;
		this.roomType = roomType;
		this.roomNumber = roomNumber;
		this.username = username;
	}

	/**
	 * Getting the length of stay for this reservation
	 * 
	 * @return length of stay
	 */
	public int getLengthOfStay() {
		return (int) ChronoUnit.DAYS.between(checkin, checkout);
	}

	/**
	 * Static method to compute length of stay for any 2 local dates
	 * 
	 * @param checkin
	 *            start date
	 * @param checkout
	 *            end date
	 * @return long value of length of stay
	 */
	public static long getLengthOfStay(LocalDate checkin, LocalDate checkout) {
		return ChronoUnit.DAYS.between(checkin, checkout);
	}

	/**
	 * Return price of this reservation
	 * 
	 * @return price of reservation
	 */
	public double getPrice() {
		return roomType.value() * this.getLengthOfStay();
	}

	/**
	 * Checks if reservation contains a specific date
	 * 
	 * @param date
	 *            date to be checked
	 * @return true if date is already reserved
	 */
	public boolean contains(LocalDate date) {
		return (checkin.isBefore(date) && checkout.isAfter(date));
	}

	/**
	 * Used to check if date range overlaps with reservations
	 * 
	 * @param date1
	 *            Checkindate
	 * @param date2
	 *            Checkoutdate
	 * @return true if overlap false if otherwise
	 */
	public boolean isInRange(LocalDate date1, LocalDate date2) {
		/// date1 & date2
		boolean overlap = false;
		if (checkin.equals(date1) || checkin.equals(date2) || checkout.equals(date1) || checkout.equals(date2))
			overlap = true;
		else if (checkin.isBefore(date1) && checkout.isAfter(date1))
			overlap = true;
		else if (checkin.isAfter(date1) && checkin.isBefore(date2))
			overlap = true;
		return overlap;
	}

	/**
	 * ToString method for getting reservation
	 */
	public String toString() {
		String line = "";
		line += roomType + " " + roomNumber + "\n";
		line += "Checkin " + checkin.toString() + "\n";
		line += "Checkout " + checkout.toString() + "\n";
		line += "Guest: " + username + "\n";
		return line;
	}

	/**
	 * Used for writing to file
	 * 
	 * @return a short toString version
	 */
	public String shortToString() {
		String line = roomType + "/" + roomNumber + "/" + checkin.toString() + "/" + checkout.toString() + "\n";
		return line;
	}

	/**
	 * Getting an info with RoomNumber omitted
	 * 
	 * @return a string without the roomNumber
	 */
	public String getInfoWithRoomNumberOmit() {
		String line = "";
		line += "Checkin " + checkin.toString() + "\n";
		line += "Checkout " + checkout.toString() + "\n";
		line += "Guest: " + username + "\n";
		return line;
	}

	@Override
	/**
	 * Used to sort reservation base on checkin Date
	 */
	public int compareTo(Reservation o) {
		return checkin.compareTo(o.checkin);
	}

	/**
	 * Used to check equals method
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (this.getClass() != other.getClass())
			return false;

		Reservation r = (Reservation) other;
		return (checkin.equals(r.checkin) && checkout.equals(r.checkout) && roomType == r.roomType
				&& roomNumber == r.roomNumber && username.equals(r.username));
	}

	/**
	 * returns checkIn date
	 * 
	 * @return checkInDate
	 */
	public LocalDate getCheckin() {
		return checkin;
	}

	/**
	 * setting the CheckInDate
	 * 
	 * @param checkin
	 *            date to be set as checkIn
	 */
	public void setCheckin(LocalDate checkin) {
		this.checkin = checkin;
	}

	/**
	 * 
	 * @return checkOutDate of reservation
	 */
	public LocalDate getCheckout() {
		return checkout;
	}

	/**
	 * Setting checkOutDate
	 * 
	 * @param checkout
	 *            date to be set as checkOut
	 */
	public void setCheckout(LocalDate checkout) {
		this.checkout = checkout;
	}

	/**
	 * 
	 * @return roomtype of reservation
	 */
	public TYPE getRoomtype() {
		return roomType;
	}

	/**
	 * Setting roomType
	 * 
	 * @param roomtype
	 *            variable to be set as roomType
	 */
	public void setRoomtype(TYPE roomtype) {
		this.roomType = roomtype;
	}

	/**
	 * getting the roomNumber
	 * 
	 * @return roomNumber
	 */
	public int getRoomNumber() {
		return roomNumber;
	}

	/**
	 * Setting the roomNumber
	 * 
	 * @param roomNumber
	 *            number to be assigned to roomNumber
	 */
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	/**
	 * Getting the userName
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setting the userName
	 * 
	 * @param username
	 *            variable to be set for userName
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
