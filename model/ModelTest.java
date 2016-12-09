package model;

import java.time.LocalDate;

public class ModelTest {

	public static void main(String[] args) {
		
		HotelReservationModel system = new HotelReservationModel();
		system.read("rsvp.txt");
		
		system.managerWrite("rsvp.txt");
		system.read("rsvp.txt");
		system.read("rsvp.txt");
		system.managerWrite("rsvp.txt");
		system.managerWrite("rsvp.txt");
		system.signInGuest("blah", 3);
		system.reserveRoom("ECO12", LocalDate.of(2017, 1, 13), LocalDate.of(2017, 1, 13));
		system.managerWrite("rsvp.txt");
		system.signUpGuest("Boo2");
		system.managerWrite("rsvp.txt");
		system.read("rsvp.txt");
		
		System.out.println(system.guestList());
		system.setGetReceipt(new HotelReservationModel.ComprehensiveReceipt());
		system.signInGuest("Kim", 1);
		System.out.println(system.printReceipt());
		system.signInGuest("My", 2);
		System.out.println(system.printReceipt());
		system.signInGuest("blah", 3);
		System.out.println(system.printReceipt());
		system.signInGuest("Boo2", 4);
		System.out.println(system.printReceipt());
	}

}
