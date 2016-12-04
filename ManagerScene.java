
/**
 * ManagerScene.java: Manager view with load, view and quit choice
 * Author: Kim Pham, and ...
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.CalendarModel;
import model.Guest;
import model.HotelReservationModel;

public class ManagerScene<Reservation> extends JFrame {
	private HotelReservationModel hotel;
	private ManagerRsvpViewFrame viewFrame;
	static GregorianCalendar cal = new GregorianCalendar();
	private CalendarModel calendarModel;
	
	public ManagerScene(HotelReservationModel hotel) {
		this.setTitle("Manager System");
		this.hotel = hotel;
		this.setLayout(new BorderLayout());
		JButton load = new JButton("Load Reservations");
		JButton view = new JButton("View Reservations");
		JButton quit = new JButton("Quit");
		
		calendarModel = new CalendarModel();
		load.addActionListener(event -> {
			hotel.read("rsvp.txt");
		});

		quit.addActionListener(event -> {
			hotel.managerWrite("rsvp.txt");
		});

		view.addActionListener(event -> {
			if (viewFrame == null)
				viewFrame = new ManagerRsvpViewFrame(hotel, calendarModel);
			else
				viewFrame.setVisible(true);
		});
		BoxLayout layout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.setLayout(layout);
		this.add(load);
		this.add(view);
		this.add(quit);

		this.setSize(500, 500);
		this.setVisible(true);

	}
}
