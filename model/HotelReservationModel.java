/**
 * HotelReservationModel.java: hotel model for the project, handling all reserving and cancelling
 * Author: Kim Pham
 */

package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Main model that handles all reservations This model keeps track of rooms,
 * guests and all reservation in them Acts as a system addReservation,
 * cancelReservation, sign in and sign up for current user
 * 
 * Note: no manager class is needed since this model just provide functions that
 * the manager use and just call it Member Field note: currentUser: used to keep
 * track of signed in user for each session all transaction is made to this user
 * only until signed out
 * 
 * receipts: keep track of all reservation made by current user this is needed
 * for the simple receipt where it prints only reservation made in one session
 */
public class HotelReservationModel {
	Room[] rooms;
	ArrayList<Guest> guests;
	Guest currentUser;
	ArrayList<Reservation> receipts;
	GetReceiptStrategy strategy;

	// listener to register view
	ArrayList<ChangeListener> listeners;

	// create 10 LUXURY and 10 ECONOMIC
	public HotelReservationModel() {
		rooms = new Room[20];
		for (int i = 0; i < 20; i++) {
			if (i < 10)
				rooms[i] = new Room(TYPE.LUXURY, i + 1);
			else
				rooms[i] = new Room(TYPE.ECO, i + 1);
		}
		guests = new ArrayList<>();
		receipts = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	// create a new guest with provided name, add to guest list
	// sign in guest after guest sign up
	// if sign in successfully, return the id (so that view can notify guest
	// their id)
	// id automatically assigned using order of sign up for now (1,2,3,4..)
	public int signUpGuest(String name) {

		Guest g = new Guest(name, guests.size() + 1);
		guests.add(g);

		boolean signedIn = signInGuest(g.getUsername(), g.getId());

		if (signedIn)
			return currentUser.getId();
		else
			return -1;
	}

	// look up guest in guest list using name and id provided
	// if found assign guest to current user, renew receipts
	public boolean signInGuest(String name, int id) {
		currentUser = null;
		Guest g = new Guest(name, id);
		int i = guests.indexOf(g);
		if (i == -1)
			return false;
		else {
			currentUser = guests.get(i);
			receipts = new ArrayList<>();
			return true;
		}
	}

	// sign out guest, set currentUser to null and renew receipt
	public void signOutGuest() {
		if (currentUser != null) {
			currentUser = null;
			receipts = new ArrayList<>();
		}
	}

	public boolean userExists(String name, int id) {
		Guest g = new Guest(name, id);
		if (guests.contains(g))
			return true;
		else
			return false;
	}

	// return username of currentUser
	public String currentUser() {
		return currentUser.getUsername();
	}

	// set the strategy for printing receipt(simple or comprehensive)
	public void setGetReceipt(GetReceiptStrategy s) {
		strategy = s;
	}

	// print the receipt based on the strategy set before hand
	public String printReceipt() {
		if(currentUser!=null){
			if (strategy == null)
				return "";
			else
				return strategy.getReceipt(currentUser, receipts);
		}else return "";
	}

	// return available rooms in form of strings
	// Ex: LUXURY5, ECO1...
	public String getAvailableRoom(LocalDate date1, LocalDate date2, boolean type) {
		String available = "";
		int start = 0;
		int end = 0;
		if (type) {
			start = 0;
			end = 10;
		} else {
			start = 10;
			end = 20;
		}
		for (int i = start; i < end; i++) {
			if (rooms[i].isAvailableForRange(date1, date2))
				available += rooms[i].getRoomNumber() + "\n";
		}
		return available;
	}

	// reserve room using roomNumber and checkin checkout date provided
	public boolean reserveRoom(String roomNumber, LocalDate date1, LocalDate date2) {
		boolean result = true;
		TYPE type = null;
		int number = 0;

		String roomType = roomNumber.substring(0, 1);

		// set room type LUXURY vs ECO

		if (roomType.equals("L")) {
			type = TYPE.LUXURY;
		} else
			type = TYPE.ECO;
		number = Integer.parseInt(roomNumber.substring(type.toString().length()));
		if(type == TYPE.LUXURY && (number < 0 || number >9)) return false;
		if(type == TYPE.ECO && (number < 10 || number >19)) return false;
		// create a reservation
		Reservation r = new Reservation(date1, date2, type, number, currentUser.getUsername());

		// add that reservation to currentUser and the reserved room
		result = currentUser.addReservation(r);
		if (!result)
			return result;

		// if (type == TYPE.LUXURY)
		// result = rooms[number - 1].addReservation(r);
		// else
		result = rooms[number - 1].addReservation(r);

		// add this reservation of this user for this session to receipt
		if (result)
			receipts.add(r);
		return result;
	}

	/**
	 * Canceling the reservation
	 * 
	 * @param type
	 *            Room Type
	 * @param number
	 *            Room Number
	 * @param date1
	 *            Checkin Date
	 * @param date2
	 *            Checkout Date
	 */
	public void cancelReservation(String type, int number, LocalDate date1, LocalDate date2) {
		TYPE t = null;
		// create the reservation to be deleted based on info
		if (type.equals("LUXURY"))
			t = TYPE.LUXURY;
		else
			t = TYPE.ECO;
		Reservation r = new Reservation(date1, date2, t, number, currentUser.getUsername());

		// delete reservation from room and current user
		if (type.equals("LUXURY")) // luxury room range from 0-9, eco from 10-19
			rooms[number - 1].deleteReservation(r);
		else
			rooms[number - 1].deleteReservation(r);

		currentUser.deleteReservation(r);
		receipts.remove(r); // remove from receipt

	}

	// return list of all guest(not needed for project, just for testing)
	public String guestList() {
		String line = "";
		for (int i = 0; i < guests.size(); i++) {
			line += guests.get(i).getUsername() + guests.get(i).getId() + "\n";
		}
		return line;
	}

	public String[] getRoomList() {
		String[] list = new String[rooms.length];
		for (int i = 0; i < rooms.length; i++) {
			list[i] = rooms[i].getRoomNumber();
		}
		return list;
	}

	// for MVC model
	public void attach(ChangeListener l) {
		listeners.add(l);
	}

	public void update() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).stateChanged(new ChangeEvent(this));
		}
	}

	/**
	 * Returns the arrayList of reserved rooms for the user
	 * 
	 * @return ArrayList of strings containing specific booked rooms by the user
	 *         in format of RoomType,num,checkin,checkout
	 */
	public ArrayList<String> returnListOfBookedRooms() {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < currentUser.getAllRsvp().size(); i++) {
			Reservation r = currentUser.getAllRsvp().get(i);
			String s = r.getRoomtype() + "," + r.getRoomNumber() + "," + r.getCheckin() + "," + r.getCheckout();
			s.trim();
			temp.add(s);
		}
		return temp;
	}

	/**
	 * Returns List of booked dates Regardless of User or type
	 * 
	 * @param startDate
	 *            the starting date of booking
	 * @param endDate
	 *            the Ending Date of the booking
	 * @return
	 */
	public String returnListOfBookedRoomsOnDate(LocalDate startDate, LocalDate endDate) {
		String available = "Nothing!";
		int count = 0;
		for (int i = 0; i < rooms.length; i++) {
			if (!rooms[i].isAvailableForRange(startDate, endDate)) {
				if (count == 0) {
					available = "";
					count++;
				}
				// follow hotel.getRoomInfo for user getting
				/**
				 * Edit Here
				 */
			available += rooms[i].getRoomNumber() + "\n" + rooms[i].getUserBookedRoom() + "\n";
			}
		}
		return available;
	}

	public String getRoomInfo(String roomNumber) {
		TYPE type = null;
		int number = 0;
		String roomType = roomNumber.substring(0, 1);
		if (roomType.equals("L")) {
			type = TYPE.LUXURY;
			System.out.println("lux");
		} else
			type = TYPE.ECO;
		number = Integer.parseInt(roomNumber.substring(type.toString().length()));
		return rooms[number - 1].getRoomInfo();
	}

	public void managerWrite(String filename) {
		try {
			File file = new File(filename);
			FileWriter writer = new FileWriter(file.getAbsoluteFile(), false);
			BufferedWriter buffer = new BufferedWriter(writer);
			for (int i = 0; i < guests.size(); i++) {
				buffer.write("User:" + guests.get(i).getUsername() + "/" + guests.get(i).getId() + "\n");
				String[] rsvp = guests.get(i).getUserReservationCompactList();
				for (int j = 0; j < rsvp.length; j++) {
					buffer.write(rsvp[j]);
				}
			}

			buffer.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file");
		}
	}

	public void read(String filename) {
		try {
			File file = new File(filename);
			if (file.exists()) {
				Scanner in = new Scanner(file);
				String line = "";
				TYPE type = null;
				while (in.hasNextLine()) {
					line = in.nextLine();
					if (line.startsWith("User:")) {
						System.out.println("Reading "+line);
						this.signOutGuest();
						String[] userLine = line.split(":");
						String[] userInfo = userLine[1].split("/");
						String guestname = userInfo[0];
						int guestId = Integer.parseInt(userInfo[1]);
						if (!this.userExists(guestname, guestId))
							System.out.println("sign up " + this.signUpGuest(guestname));
						else System.out.println("sign in " + this.signInGuest(guestname, guestId));
					} else if (!line.equals("") && currentUser != null) {
						System.out.println("Processing "+line);
						String[] rsvpInfo = line.split("/");// type, num,
															// checkin, checkout
						String[] checkInField = rsvpInfo[2].split("-");
						String[] checkOutField = rsvpInfo[3].split("-");
						LocalDate checkIn = LocalDate.of(Integer.parseInt(checkInField[0]),
								Integer.parseInt(checkInField[1]), Integer.parseInt(checkInField[2]));
						LocalDate checkOut = LocalDate.of(Integer.parseInt(checkOutField[0]),
								Integer.parseInt(checkOutField[1]), Integer.parseInt(checkOutField[2]));
						// check if expired
						String roomNumber = rsvpInfo[0] + rsvpInfo[1];
						LocalDate expiredDate = LocalDate.now();
						System.out.println(roomNumber);
						if (!checkIn.isBefore(expiredDate)) { // if not expired
																// then add it
							System.out.println("reserve this "+this.reserveRoom(roomNumber, checkIn, checkOut));
						}

					}

				}
			} else {
				System.out.println("This is the first run");
			}
		} catch (IOException ex) {
			System.out.println("Error opening file");
		}
	}

	// strategy for simpleReceipt
	public static class SimpleReceipt implements GetReceiptStrategy {
		@Override
		public String getReceipt(Guest user, ArrayList<Reservation> receipt) {
			receipt.sort((r1, r2) -> {
				return r1.compareTo(r2);
			});
			String line = "Username: " + user.getUsername() + "\n";
			line += "Id: " + user.getId() + "\n";
			double sum = 0;
			for (int i = 0; i < receipt.size(); i++) {
				Reservation r = receipt.get(i);
				line += "Room " + r.getRoomtype() + r.getRoomNumber();
				line += " " + r.getCheckin() + " - " + r.getCheckout() + "     $" + r.getPrice() + "\n";
				sum += r.getPrice();
			}
			line += "Total: $" + sum + "\n";

			return line;
		}

	}

	// strategy for comprehensive
	public static class ComprehensiveReceipt implements GetReceiptStrategy {
		@Override
		public String getReceipt(Guest user, ArrayList<Reservation> receipt) {
			String line = "Username: " + user.getUsername() + "\n";
			line += "Id: " + user.getId() + "\n";
			String[] list = user.getUserReservation();
			for (int i = 0; i < list.length; i++) {
				line += (i + 1) + ". " + list[i] + "\n\n";
			}
			line += "Total charge: $" + user.getTotalCharge();
			return line;
		}
	}

	// just for some debug
	public String debug() {
		return receipts.toString();
	}
}
