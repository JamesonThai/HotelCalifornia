/**
 * HotelReservationSystem.java: Main method for the program
 * Author: Kim Pham
 */

import java.time.LocalDate;

import model.HotelReservationModel;

public class HotelReservationSystem {

	/**
	 * Main Method to test
	 * @param args not used
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HotelReservationModel hotel = new HotelReservationModel();
		hotel.read("rsvp.txt");
		MainScene scene = new MainScene(hotel);
	}

}
