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
	private boolean isLuxury;
	private LocalDate dateAfter;
	
	private JButton[] daybuttons;
	private JLabel calendarLabel;
	private boolean firstField;
	
	/**
	 * Constructor of ManagerView
	 * @param hotel hotel Model to be modified from manager view
	 * @param cal Calendar to be modified
	 */
	public ManagerRsvpViewFrame(HotelReservationModel hotel, CalendarModel calendar) {
		this.hotel = hotel;
		this.cal = calendar;
		
		// month view panel: to be added to North of this frame
		JPanel monthViewPanel = new JPanel();
		// monthViewPanel's contents go here
		
		JButton CalendarPlaceHolder = new JButton("placeHolder");

		//navigation
		JButton pre = new JButton("<");
		JButton next = new JButton(">");
		calendarLabel = new JLabel();

		LocalDate now = cal.getDateNow();
		calendarLabel.setText(now.getMonth()+" "+now.getYear());
		
		JPanel navigation = new JPanel();
		navigation.add(pre);
		navigation.add(calendarLabel);
		navigation.add(next);
		monthViewPanel.add(navigation, BorderLayout.NORTH);
		
		pre.addActionListener(event->{
			cal.previousMonth();
			updateCalendar();
//			cal.update();
		});
		
		next.addActionListener(event->{
			cal.nextMonth();
			updateCalendar();
//			cal.update();
		});
		
		// New check
		
		//set up button panel
		JPanel buttonPanel = new JPanel();
		JButton[] titleButtons = new JButton[7];
		
		//get month data from calendar model
		String[] row = cal.getRowData();
		
		//set gridbad layout for buttonPanel to display button
		buttonPanel.setLayout(new GridLayout(6, 8)); // was 6,7 before
		
		//set up buttons with title
		String[] title = new String[]{"S","M","T","W","T","F","S"};
		for(int i = 0; i < titleButtons.length; i++){
			titleButtons[i] = new JButton(title[i]);
			buttonPanel.add(titleButtons[i]);
		}
		
		//add day buttons
		daybuttons = new JButton[row.length];
		for(int i = 0; i < row.length; i++){
			daybuttons[i] = new JButton(row[i]);
			
			daybuttons[i].addActionListener(event->{
				JButton chosenButton = (JButton) event.getSource();
				if(!chosenButton.getText().equals("")){
					int dayValue = Integer.parseInt(chosenButton.getText());
					LocalDate currentMonthYear = cal.getRequestedDay();
					dateClicked = LocalDate.of(currentMonthYear.getYear(), currentMonthYear.getMonthValue(), dayValue);
					update(dateClicked);
				}
			});
			
			buttonPanel.add(daybuttons[i]);
		}

		roomAvailability = new JTextArea("RoomAvailability",10,10);
		roomReserved = new JTextArea("RoomsReserved",10,10);
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
			
		monthViewPanel.add(buttonPanel);
		monthViewPanel.add(textAreas);
		
		buttonPanel.setBounds(10,10, 600, 600);
		textAreas.setBounds(650,10,500,500);
		
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
			// add the button's action listener here
			roomButtonPanel.add(roomButtons[i]);
		}
		
		// textArea to show Info of room
		JPanel roomInfoPanel = new JPanel();

		roomInfo = new JTextArea(20, 20);
		JScrollPane scrollPane3 = new JScrollPane(roomInfo);
		roomInfoPanel.add(scrollPane3);
		roomInfo.setEditable(false);
		
		// Setting layout
		roomViewPanel.add(roomButtonPanel, BorderLayout.WEST);
		roomViewPanel.add(roomInfoPanel, BorderLayout.CENTER);
		this.add(monthViewPanel, BorderLayout.NORTH);
		this.add(roomViewPanel, BorderLayout.SOUTH);
		this.setSize(1200, 600);
		// COMENTED OUT PACK so it doesn't resize and cut out textAreas
//		this.pack();
		this.setVisible(true);
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		// Getting available room from checkInDate to CheckOut Date or are we just only checking for the current day
		System.out.println("goT hERE");
		String[] row = cal.getRowData();
		for(int i = 0; i < daybuttons.length; i++){
			daybuttons[i].setText(row[i]);
		}
		LocalDate current = cal.getRequestedDay();
		calendarLabel.setText(current.getMonth() +" "+current.getYear());
		repaint();
	}

	/**
	 * Updating the available rooms and ReservedRooms textField to show available/reserved rooms on that date
	 * @param dateChosen date to show room booking or availability
	 */
	public void update(LocalDate dateChosen) {
		// TODO Auto-generated method stub
		String s = "";
		roomAvailability.setText("");
		roomReserved.setText("");
		LocalDate dateAfter = LocalDate.of(dateClicked.getYear(), dateClicked.getMonthValue(),dateClicked.getDayOfMonth());
		s = dateChosen.toString() + "\n"+ hotel.getAvailableRoom(dateClicked, dateAfter, true)+ "\n"+hotel.getAvailableRoom(dateClicked, dateAfter, false);
		roomAvailability.setText(s);
		s = "Reserved Rooms" + "\n" + dateChosen.toString();
		s = dateChosen.toString() + "\n"+ hotel.returnListOfBookedRoomsOnDate(dateClicked, dateAfter);
		roomReserved.setText(s);
		dateClicked = dateChosen;
	}
	
	/**
	 * Visually updates the calendar when clicking next or previous
	 */
	public void updateCalendar()
	{
		String[] row = cal.getRowData();
		for(int i = 0; i < daybuttons.length; i++){
			daybuttons[i].setText(row[i]);
		}
		LocalDate current = cal.getRequestedDay();
		calendarLabel.setText(current.getMonth() +" "+current.getYear());
		repaint();
	}

	public void setField(boolean firstField){
		this.firstField = firstField;
	}
}
