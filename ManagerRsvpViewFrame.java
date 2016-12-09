
/**
 * ManagerRsvpViewFrame.java: Frame where manager can view reservation by month view and room view
 * Author: Kim Pham, and Jameson T.
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.BorderFactory;
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

//Note: if in this frame any mutator method of hotel is called, the view shall be updated by calling hotel.update() 
//      and the specification for the update() will be specified in stateChange(ChangeEvent e) method
// 		to comply with MVC requirement
public class ManagerRsvpViewFrame extends JFrame implements ChangeListener {
	private HotelReservationModel hotel;
	CalendarModel cal;
	private LocalDate dateClicked;
	private JTextArea roomInfo;
	private JTextArea roomAvailability;
	private JTextArea roomReserved;
	private JButton[] daybuttons;
	private JLabel calendarLabel;
	private JLabel dayLabel;

	/**
	 * Constructor of ManagerView
	 * 
	 * @param hotel
	 *            hotel Model to be modified from manager view
	 * @param cal
	 *            Calendar to be modified
	 */
	public ManagerRsvpViewFrame(HotelReservationModel hotel, CalendarModel calendar) {
		this.hotel = hotel;
		calendar.attachListener(this);
		this.cal = calendar;

		// month view panel: to be added to North of this frame
		JPanel monthViewPanel = new JPanel();

		// navigation
		JButton pre = new JButton("<");
		JButton next = new JButton(">");
		calendarLabel = new JLabel();

		LocalDate now = cal.getDateNow();
		calendarLabel.setText(now.getMonth() + " " + now.getYear());
		dayLabel = new JLabel(cal.getDateNow().toString());

		JPanel navigation = new JPanel();
		navigation.add(pre);
		navigation.add(calendarLabel);
		navigation.add(next);
		navigation.add(dayLabel);

		pre.addActionListener(event -> {
			cal.previousMonth();
			cal.update();
		});

		next.addActionListener(event -> {
			cal.nextMonth();
			cal.update();
		});

		// set up button panel
		JPanel buttonPanel = new JPanel();
		JButton[] titleButtons = new JButton[7];

		// get month data from calendar model
		String[] row = cal.getRowData();

		// set gridbad layout for buttonPanel to display button
		buttonPanel.setLayout(new GridLayout(7, 7));
		// set up buttons with title
		String[] title = new String[] { "S", "M", "T", "W", "T", "F", "S" };
		for (int i = 0; i < titleButtons.length; i++) {
			titleButtons[i] = new JButton(title[i]);
			buttonPanel.add(titleButtons[i]);
		}

		// add day buttons
		daybuttons = new JButton[row.length];
		for (int i = 0; i < row.length; i++) {
			daybuttons[i] = new JButton(row[i]);

			daybuttons[i].addActionListener(event -> {
				JButton chosenButton = (JButton) event.getSource();
				if (!chosenButton.getText().equals("")) {
					if (chosenButton.getText().matches("^[0-9]*$")) {
						int dayValue = Integer.parseInt(chosenButton.getText());
						LocalDate currentMonthYear = cal.getRequestedDay();
						dateClicked = LocalDate.of(currentMonthYear.getYear(), currentMonthYear.getMonthValue(),
								dayValue);
						cal.update();
					}
				}
			});

			buttonPanel.add(daybuttons[i]);
		}

		roomAvailability = new JTextArea("RoomAvailability", 10, 10);
		roomReserved = new JTextArea("RoomsReserved", 10, 10);
		roomAvailability.setEditable(false);
		roomReserved.setEditable(false);
		JPanel textAreas = new JPanel();
		JPanel rmAvail = new JPanel();
		JPanel rmReser = new JPanel();

		rmAvail.setBorder(BorderFactory.createTitledBorder("Available Rooms"));
		rmReser.setBorder(BorderFactory.createTitledBorder("Reserved Rooms"));

		roomAvailability.setLineWrap(true);
		roomReserved.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(roomAvailability);
		JScrollPane scrollPane2 = new JScrollPane(roomReserved);

		rmAvail.add(scrollPane);
		rmReser.add(scrollPane2);
		textAreas.add(rmAvail);
		textAreas.add(rmReser);

		// set up month view panel
		JPanel calendarPanel = new JPanel();
		calendarPanel.setLayout(new BorderLayout());
		calendarPanel.add(navigation, BorderLayout.NORTH);
		calendarPanel.add(buttonPanel, BorderLayout.SOUTH);
		monthViewPanel.setLayout(new BorderLayout());
		// monthViewPanel.add(navigation, BorderLayout.NORTH);
		// monthViewPanel.add(buttonPanel);
		monthViewPanel.add(calendarPanel, BorderLayout.WEST);
		monthViewPanel.add(textAreas, BorderLayout.EAST);

		// buttonPanel.setBounds(10,10, 600, 600);
		// textAreas.setBounds(650,10,500,500);

		// room view panel: to be added to South of this frame
		JPanel roomViewPanel = new JPanel();
		roomViewPanel.setLayout(new BorderLayout());

		// panel to show button of room
		JPanel roomButtonPanel = new JPanel();
		roomButtonPanel.setLayout(new GridLayout(4, 5));

		String[] roomList = hotel.getRoomList();
		JButton[] roomButtons = new JButton[roomList.length];
		for (int i = 0; i < roomButtons.length; i++) {
			roomButtons[i] = new JButton(roomList[i]);
			roomButtonPanel.add(roomButtons[i]);
			roomButtons[i].addActionListener(event -> {
				JButton button = (JButton) event.getSource();
				String roomNumber = button.getText();
				String info = hotel.getRoomInfo(roomNumber);
				roomInfo.setText(info);
				repaint();
			});
		}

		// textArea to show Info of room
		JPanel roomInfoPanel = new JPanel();

		roomInfo = new JTextArea(20, 20);
		JScrollPane scrollPane3 = new JScrollPane(roomInfo);
		roomInfoPanel.add(scrollPane3);
		roomInfo.setEditable(false);
		roomButtonPanel.setBorder(BorderFactory.createTitledBorder("Room View"));
		roomInfoPanel.setBorder(BorderFactory.createTitledBorder("Room Info"));

		// Setting layout

		roomViewPanel.add(roomButtonPanel, BorderLayout.WEST);
		roomViewPanel.add(roomInfoPanel, BorderLayout.CENTER);
		this.add(monthViewPanel, BorderLayout.NORTH);
		this.add(roomViewPanel, BorderLayout.SOUTH);
		this.setSize(1200, 600);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	/**
	 * Overriding the stateChanged event from changeListeners
	 * 
	 * @param e
	 *            the event that affects the model
	 */
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		String[] row = cal.getRowData();
		for (int i = 0; i < daybuttons.length; i++) {
			daybuttons[i].setText(row[i]);
		}
		LocalDate current = cal.getRequestedDay();
		calendarLabel.setText(current.getMonth() + " " + current.getYear());
		if (dateClicked != null) {
			dayLabel.setText(dateClicked.toString());
			this.update(dateClicked);
		}
		repaint();
	}

	/**
	 * Updating the available rooms and ReservedRooms textField to show
	 * available/reserved rooms on that date
	 * 
	 * @param dateChosen
	 *            date to show room booking or availability
	 */
	public void update(LocalDate dateChosen) {
		// TODO Auto-generated method stub
		String s = "";
		roomAvailability.setText("");
		roomReserved.setText("");

		LocalDate dateAfter = LocalDate.of(dateClicked.getYear(), dateClicked.getMonthValue(),
				dateClicked.getDayOfMonth());
		s = hotel.getAvailableRoom(dateClicked, dateAfter, true) + "\n"
				+ hotel.getAvailableRoom(dateClicked, dateAfter, false);
		roomAvailability.setText(s);
		s = "Reserved Rooms" + "\n" + dateChosen.toString();
		s = hotel.returnListOfBookedRoomsOnDate(dateClicked, dateAfter);
		roomReserved.setText(s);
		dateClicked = dateChosen;
	}

}
