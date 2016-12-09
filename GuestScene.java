
/**
 * GuestScene.java: Guest View with makeReservation and View/CancelReservation choice
 * Author: Kim Pham
 */
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.CalendarModel;
import model.HotelReservationModel;

public class GuestScene extends JFrame {
	private HotelReservationModel hotel;
	private SignInDialog signInDialog;
	private String chosenCheckin;
	private CalendarModel calendarModel;
	private MakeRsvpFrame make;
	private ViewCancelRsvp viewCancel;

	/**
	 * Constructor for Guest Scene
	 * 
	 * @param model
	 *            Hotel Model
	 */
	public GuestScene(HotelReservationModel model) {
		this.hotel = model;
		if (signInDialog == null) {
			signInDialog = new SignInDialog(GuestScene.this, model);
			signInDialog.setVisible(true);
		}
		calendarModel = new CalendarModel();

		JButton makeReservation = new JButton("Make Reservation");
		JButton viewReservation = new JButton("View/Cancel Reservation");

		JButton done = new JButton("All Done");

		// panel for makeRsvp and viewRsvp
		JPanel panel = new JPanel();
		panel.add(makeReservation);
		panel.add(viewReservation);
		this.add(panel, BorderLayout.CENTER);

		// panel for done
		JPanel panel2 = new JPanel();
		panel2.add(done);
		this.add(panel2, BorderLayout.SOUTH);
		this.setSize(500, 500);

		makeReservation.addActionListener(event -> {
			if (make == null) {
				make = new MakeRsvpFrame(model, calendarModel);
			} else
				make.setVisible(true);
		});

		viewReservation.addActionListener(event -> {
			if (viewCancel == null)
				viewCancel = new ViewCancelRsvp(model);
			else
				viewCancel.setVisible(true);
		});

		done.addActionListener(event -> {
			hotel.managerWrite("rsvp.txt");
			this.setVisible(false);
		});

	}
}
