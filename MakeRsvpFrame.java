/**
 * MakeRsvpFrame.java: Frame where guest can make new reservations
 * Author: Kim Pham
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.CalendarModel;
import model.HotelReservationModel;
import model.Reservation;
import model.Room;

/**
 * MakeRsvpFrame has: 1/ inputPanel: checkinPanel, checkoutPanel, roomTypePanel
 * 2/ reservationPanel: roomInfoPanel,
 * userChoicePanel(roomChoicePanel+navigationPanel)
 *
 */
public class MakeRsvpFrame extends JFrame implements ChangeListener {
	private HotelReservationModel hotel;
	private CalendarModel calendar;
	private CalendarDialog calendarChoser;

	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private boolean isLuxury;

	private JTextField checkInField;
	private JTextField checkOutField;
	private JTextArea availableRoomArea;
	private JTextField roomChoiceField;

	/**
	 * Constructor for MakeRsvp scene
	 * 
	 * @param hotel
	 *            Hotel Model
	 * @param calendar
	 *            Calendar Model
	 */
	public MakeRsvpFrame(HotelReservationModel hotel, CalendarModel calendar) {
		this.hotel = hotel;
		hotel.attach(this);
		this.calendar = calendar;

		// setting up inputPanel
		JPanel inputPanel = new JPanel();
		JLabel checkin = new JLabel("Check in");
		JLabel checkout = new JLabel("Check out");

		JButton checkInButton = new JButton(new ImageIcon("calendar_icon.png"));
		JButton checkOutButton = new JButton(new ImageIcon("calendar_icon.png"));

		checkInButton.addActionListener(getDateFromCalendar(true));
		checkOutButton.addActionListener(getDateFromCalendar(false));

		checkInField = new JTextField(10);
		checkInField.setEditable(false);
		checkOutField = new JTextField(10);
		checkOutField.setEditable(false);

		JLabel roomTypeLabel = new JLabel("Room Type");
		JButton luxuryButton = new JButton("$200");
		JButton ecoButton = new JButton("$80");

		luxuryButton.addActionListener(roomTypeSelected(true));
		ecoButton.addActionListener(roomTypeSelected(false));

		// check in panel
		JPanel checkinPanel = new JPanel();
		checkinPanel.add(checkin);
		checkinPanel.add(checkInButton);
		checkinPanel.add(checkInField);

		// check out panel
		JPanel checkoutPanel = new JPanel();
		checkoutPanel.add(checkout);
		checkoutPanel.add(checkOutButton);
		checkoutPanel.add(checkOutField);

		// room type panel
		JPanel roomTypePanel = new JPanel();
		roomTypePanel.add(roomTypeLabel);
		roomTypePanel.add(luxuryButton);
		roomTypePanel.add(ecoButton);

		// add check in panel, check out panel and room type panel to input
		// panel
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(checkinPanel, BorderLayout.NORTH);
		inputPanel.add(checkoutPanel, BorderLayout.CENTER);
		inputPanel.add(roomTypePanel, BorderLayout.SOUTH);

		// add input panel to frame
		this.add(inputPanel, BorderLayout.NORTH);

		// Reservation Panel
		JPanel reservationPanel = new JPanel();
		reservationPanel.setLayout(new BorderLayout());

		JLabel roomInfoLabel = new JLabel("Available rooms");
		availableRoomArea = new JTextArea(20, 20);
		availableRoomArea.setLineWrap(true);
		availableRoomArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(availableRoomArea);

		JLabel roomChoiceLabel = new JLabel("Enter room number to reserve");
		roomChoiceField = new JTextField(10);
		roomChoiceField.setEditable(false);

		// confirm button
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(event -> {
			if (checkInField.getText() == null || checkOutField.getText() == null
					|| !roomChoiceField.getText().matches("^[0-9]*$") || roomChoiceField.getText().equals("")) {
				JOptionPane.showMessageDialog(MakeRsvpFrame.this, "Please enter valid input");
			} else {
				String roomChoice = "";
				if (isLuxury) {
					roomChoice = "LUXURY" + roomChoiceField.getText();
				} else {
					roomChoice = "ECO" + roomChoiceField.getText();
				}
				boolean result = hotel.reserveRoom(roomChoice, checkInDate, checkOutDate);
				if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate))
					JOptionPane.showMessageDialog(this, "Please enter valid date");
				else {
					if (result) {
						JOptionPane.showMessageDialog(this,
								"Room " + roomChoiceField.getText().toUpperCase() + " is reserved successfully.");
						hotel.update(); // update model and let changeListener
										// modify the view
					} else {
						JOptionPane.showMessageDialog(this, "Room already reserved by you or unavailable");
					}
				}
			}
		});

		// more reservation Button
		JButton moreReservation = new JButton("More Reservation?");
		moreReservation.addActionListener(event -> {
			resetAllField();
		});

		// done button
		JButton done = new JButton("Done");
		done.addActionListener(event -> {
			Object[] options = { "Comprehensive", "Simple" };
			int n = JOptionPane.showOptionDialog(this, "Simple or Comprehensive", "Print Receipt",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			// simple choice
			if (n == 1) {
				hotel.setGetReceipt(new HotelReservationModel.SimpleReceipt());
			} else {
				hotel.setGetReceipt(new HotelReservationModel.ComprehensiveReceipt());
			}

			String receipt = hotel.printReceipt();
			JOptionPane.showMessageDialog(this, receipt);
			resetAllField();
			this.setVisible(false);
		});

		JPanel showRoomPanel = new JPanel();
		showRoomPanel.setLayout(new BorderLayout());
		showRoomPanel.add(roomInfoLabel, BorderLayout.NORTH);
		showRoomPanel.add(scrollPane, BorderLayout.SOUTH);

		reservationPanel.add(showRoomPanel, BorderLayout.WEST);

		// located east of reservation panel to store roomChoicePanel and
		// navigationPanel
		JPanel userChoicePanel = new JPanel();
		userChoicePanel.setLayout(new BorderLayout());
		JPanel roomChoicePanel = new JPanel();
		roomChoicePanel.add(roomChoiceLabel);
		roomChoicePanel.add(roomChoiceField);
		userChoicePanel.add(roomChoicePanel, BorderLayout.NORTH);

		JPanel navigationPanel = new JPanel();
		navigationPanel.add(confirm);
		navigationPanel.add(moreReservation);
		navigationPanel.add(done);
		userChoicePanel.add(navigationPanel, BorderLayout.SOUTH);

		reservationPanel.add(userChoicePanel, BorderLayout.EAST);
		this.add(reservationPanel, BorderLayout.SOUTH);
		this.setResizable(false);
		this.setVisible(true);
		this.pack();
	}

	/**
	 * Updating the textAreas and fields
	 * 
	 * @param dateChosen
	 *            Date to be checked
	 * @param firstField
	 *            boolean variable to determine if Luxury or not
	 */
	public void update(LocalDate dateChosen, boolean firstField) {
		if (firstField) {
			checkInField.setText(dateChosen.toString());
			checkInDate = dateChosen;
		} else {
			checkOutField.setText(dateChosen.toString());
			checkOutDate = dateChosen;
		}

		if (firstField && checkInDate.isBefore(calendar.getDateNow())) {
			JOptionPane.showMessageDialog(this, "Please pick a future or current date");
			calendarChoser.setVisible(true);
		} else if (!firstField && Reservation.getLengthOfStay(checkInDate, dateChosen) >= 60) {
			JOptionPane.showMessageDialog(this, "The maximum lenght of stay is 60");
			calendarChoser.setVisible(true);
		}

		this.repaint();
	}

	/**
	 * Action Listener
	 * 
	 * @param firstField
	 *            boolean to set to FirstField in CalendarChooser
	 * @return Action Listener
	 */
	public ActionListener getDateFromCalendar(boolean firstField) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (calendarChoser == null) {
					calendarChoser = new CalendarDialog(MakeRsvpFrame.this, calendar);
					calendar.attachListener(calendarChoser);
				}
				calendarChoser.setField(firstField);
				calendarChoser.setVisible(true);
			}
		};
	}

	/**
	 * 
	 * @param isluxury
	 *            Checking if Luxury or Eco
	 * @return Action Listener
	 */
	public ActionListener roomTypeSelected(boolean isluxury) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String list = hotel.getAvailableRoom(checkInDate, checkOutDate, isluxury);
				isLuxury = isluxury;
				availableRoomArea.setText(list);
				roomChoiceField.setEditable(true);
			}

		};
	}

	/**
	 * Makes all TextFields and textAreas blank
	 */
	public void resetAllField() {
		checkInField.setText("");
		checkOutField.setText("");
		availableRoomArea.setText("");
		roomChoiceField.setText("");
	}

	@Override
	/**
	 * Override method that changes the textArea of availableRoomArea and
	 * updates the model
	 */
	public void stateChanged(ChangeEvent e) {
		availableRoomArea.setText(hotel.getAvailableRoom(checkInDate, checkOutDate, isLuxury));
	}

}
