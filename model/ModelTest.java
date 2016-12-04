package model;

import java.time.LocalDate;

public class ModelTest {

	public static void main(String[] args) {
		
		HotelReservationModel system = new HotelReservationModel();
		system.read("rsvp.txt");
	/*	LocalDate date1 = LocalDate.of(2016, 12, 7);
		LocalDate date2 = LocalDate.of(2016, 12, 9);
		
		system.signUpGuest("Kim");
		system.reserveRoom("LUXURY5", date1, date2);
		system.signUpGuest("My");
		system.reserveRoom("ECO12", date1, date2);
		system.reserveRoom("ECO13", date1, date2);
		system.signUpGuest("Boo");
		system.reserveRoom("ECO15", date1, date2);
		system.reserveRoom("LUXURY5", date1, date2);
		system.signInGuest("Kim", 1);
		system.reserveRoom("LUXURY1", date1, date2);
		system.reserveRoom("LUXURY2", date1, date2);
		system.reserveRoom("LUXURY3", date1, date2);
		system.reserveRoom("ECO11", date1, date2);
		system.reserveRoom("ECO18", date1, date2);
		
		system.setGetReceipt(new HotelReservationModel.ComprehensiveReceipt());
			*/
		system.signInGuest("Boo", 3);
		system.setGetReceipt(new HotelReservationModel.ComprehensiveReceipt());
		String boo = system.printReceipt();
		System.out.println(boo);
		//system.managerWrite("rsvp.txt");
	
	}

}
