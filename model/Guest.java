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
	private ArrayList<Reservation> rsvp; // list of reservation this user made

	/**
	 * Constructor for Guest
	 * 
	 * @param name
	 *            name of the guest
	 * @param id
	 *            id of the guest
	 */
	public Guest(String name, int id) {
		username = name;
		userId = id;
		rsvp = new ArrayList<>();
	}

	/**
	 * Getting all reservations made by the user
	 * 
	 * @return arrayList of reservations
	 */
	public ArrayList<Reservation> getAllRsvp() {
		return rsvp;
	}

	/**
	 * Getting the username of guest
	 * 
	 * @return username of the guest
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Getting the Guest Id
	 * 
	 * @return userId of the guest
	 */
	public int getId() {
		return userId;
	}

	/**
	 * Adding reservation to user Note: Only used HotelReservationModel, should
	 * not be called somewhere by itself Note: when calling Guest.add, also call
	 * Room[number].add
	 * 
	 * @param re
	 *            reservation to be added to guest
	 * @return true if you can add, false if already have
	 */
	public boolean addReservation(Reservation re) {
		if (rsvp.contains(re))
			return false;
		else {
			rsvp.add(re);
			return true;
		}
	}

	/**
	 * Deleting reservation to user Note: Only used HotelReservationModel,
	 * should not be called somewhere by itself Note: when calling Guest.delete,
	 * also call Room[number].delete
	 * 
	 * @param re
	 *            reservation to be deleted from guest
	 */
	public void deleteReservation(Reservation re) {
		rsvp.remove(re);
	}

	/**
	 * Getting the User's reservation
	 * 
	 * @return array of string containing user reservation info: room + price
	 */
	public String[] getUserReservation() {
		String[] list = new String[rsvp.size()];
		for (int i = 0; i < rsvp.size(); i++) {
			list[i] = rsvp.get(i).toString() + "Price: $" + rsvp.get(i).getPrice();
		}
		return list;
	}

	/**
	 * Getting total charge for all reservations in user's rsvp list
	 * 
	 * @return summation cost of all reservations
	 */
	public double getTotalCharge() {
		double sum = 0;
		for (int i = 0; i < rsvp.size(); i++) {
			sum += rsvp.get(i).getPrice();
		}
		return sum;
	}

	/**
	 * Override equals method User is considered equal if their name and id
	 * match only Purpose: easier to look up user in Sign In
	 */
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (this.getClass() != other.getClass())
			return false;

		Guest g = (Guest) other;
		return username.equals(g.username) && userId == g.userId;
	}

	/**
	 * Getting UserReservation in a compacted form
	 * 
	 * @return Array of Strings about the reservation Info in a compacted form
	 */
	public String[] getUserReservationCompactList() {
		String[] list = new String[rsvp.size()];
		for (int i = 0; i < rsvp.size(); i++) {
			list[i] = rsvp.get(i).shortToString();
		}
		return list;
	}

}
