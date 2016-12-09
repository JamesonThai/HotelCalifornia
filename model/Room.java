package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represent 20 rooms: 10 luxury vs 10 economic enum of Luxury and Eco
 */
enum TYPE {
	LUXURY(200.00), ECO(80.00);
	private double price;

	private TYPE(double v) {
		price = v;
	}

	/**
	 * @return gets the value of the price
	 */
	public double value() {
		return price;
	}
}

/**
 * Represents a room in the Hotel
 * 
 * @author Kim Pham, Jameson Thai
 *
 */
public class Room {
	TYPE type;
	int number;
	ArrayList<Reservation> rsvp;

	/**
	 * Constructor of room Type
	 * 
	 * @param type
	 *            Eco or Lux
	 * @param number
	 *            roomNumber
	 */
	public Room(TYPE type, int number) {
		this.type = type;
		this.number = number;
		rsvp = new ArrayList<>();
	}

	/**
	 * @return roomName in format Type then number ex: LUXURY1
	 */
	public String getRoomNumber() {
		return type.name() + number;
	}

	/**
	 * @return roomInformation in format roomInformationWIthoutNumber + newLine
	 */
	public String getRoomInfo() {
		String line = this.getRoomNumber() + "\n";
		for (int i = 0; i < rsvp.size(); i++) {
			line += rsvp.get(i).getInfoWithRoomNumberOmit();
			line += "\n";
		}
		return line;
	}

	/**
	 * Gets the user who booked this room at that date
	 * 
	 * @return username of person who booked that rooms
	 */
	public String getUserBookedRoom() {
		int count = 0;
		String line = "";
		ArrayList<String> previousUsers = new ArrayList<String>();
		for (int i = 0; i < rsvp.size(); i++) {
			String s = rsvp.get(i).getUsername();
			for (String t : previousUsers) {
				if (s.equals(t))
					count++;
			}
			if (count == 0) {
				previousUsers.add(s);
				line += s + "\n";
			}
		}
		return line;
	}

	/**
	 * Adds a reservation to a room Note: Only to be used in
	 * HotelReservationModel with guest.add
	 * 
	 * @param re
	 *            Reservation to be added
	 * @return true if reservation already contains it otherwise false and add
	 *         it
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
	 * Removes a reservation
	 * 
	 * @param re
	 *            Reservation to be removed
	 */
	public void deleteReservation(Reservation re) {
		rsvp.remove(re);
	}

	/**
	 * Checks if room is available for a date range Utilizes
	 * Reservation.isInRange
	 * 
	 * @param date1
	 *            CheckInDate
	 * @param date2
	 *            CheckOutDate
	 * @return true if there is no conflict otherwise false
	 */
	public boolean isAvailableForRange(LocalDate date1, LocalDate date2) {
		boolean conflict = false;
		for (int i = 0; i < rsvp.size(); i++) {
			if (rsvp.get(i).isInRange(date1, date2)) {
				conflict = true;
				return !conflict;
			}
		}
		return !conflict;
	}

	/**
	 * Checks if room is available for this date Note: utilizes
	 * Reservation.contains
	 * 
	 * @param date
	 *            Date to be checked
	 * @return true if available otherwise false
	 */
	public boolean isAvailableForDate(LocalDate date) {
		boolean conflict = false;
		for (int i = 0; i < rsvp.size(); i++) {
			if (rsvp.get(i).contains(date)) {
				conflict = true;
				return !conflict;
			}
		}
		return !conflict;
	}

	/**
	 * Gets the reservation for this room as an array of strings
	 * 
	 * @return reservation for room
	 */
	public String[] getReservationList() {
		// sort the list by checkout date before returning
		rsvp.sort((r1, r2) -> {
			return r1.compareTo(r2);
		});

		String[] list = new String[rsvp.size()];
		for (int i = 0; i < rsvp.size(); i++) {
			list[i] = rsvp.get(i).toString();
		}
		return list;
	}

	/**
	 * @return total price for this room, 200 vs 80
	 */
	public double getPrice() {
		return type.value(); // LUXURY.value = 200, ECO.value = 80
	}
}
