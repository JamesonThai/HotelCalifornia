/**
 * ManagerRsvpViewFrame.java: Frame where manager can view reservation by month view and room view
 * Author: Kim Pham, and Jameson T.
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
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

//Note: if in this frame any mutator method of hotel is called, the view shall be updated by calling hotel.update() 
//      and the specification for the update() will be specified in stateChange(ChangeEvent e) method
// 		to comply with MVC requirement
public class ManagerRsvpViewFrame extends JFrame implements ChangeListener {
	private HotelReservationModel hotel;
	private JTextField dateField;;
	private CalendarDialog2 calendarChoser;
	private CalendarModel cal;
	private LocalDate dateClicked;
	private JTextArea roomInfo;
	private boolean isLuxury;
	private LocalDate dateAfter;
	/**
	 * Constructor of ManagerView
	 * @param hotel hotel Model to be modified from manager view
	 * @param cal Calendar to be modified
	 */
	public ManagerRsvpViewFrame(HotelReservationModel hotel, CalendarModel cal) {
		this.hotel = hotel;
		this.cal = cal;
		// month view panel: to be added to North of this frame
		JPanel monthViewPanel = new JPanel();
		// monthViewPanel's contents go here

		JButton CD = new JButton("ChangeDate");
		monthViewPanel.add(CD);
		dateField = new JTextField(10);
		monthViewPanel.add(dateField);

		CD.addActionListener(event -> {
			if (calendarChoser == null) {
				calendarChoser = new CalendarDialog2(ManagerRsvpViewFrame.this, cal);
				cal.attachListener(calendarChoser);
			}
			calendarChoser.setVisible(true);
		});

		
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
			if (i < 10)
				roomButtons[i].addActionListener(roomTypeSelected(true));
			if (i > 9)
				roomButtons[i].addActionListener(roomTypeSelected(false));
			roomButtonPanel.add(roomButtons[i]);
		}
		
		// textArea to show Info of room
		JPanel roomInfoPanel = new JPanel();

		roomInfo = new JTextArea(20, 20);
		JScrollPane scrollPane = new JScrollPane(roomInfo);
		roomInfoPanel.add(scrollPane);
		roomInfo.setEditable(false);
		
		// Setting layout
		roomViewPanel.add(roomButtonPanel, BorderLayout.WEST);
		roomViewPanel.add(roomInfoPanel, BorderLayout.CENTER);
		this.add(monthViewPanel, BorderLayout.NORTH);
		this.add(roomViewPanel, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		dateAfter = LocalDate.of(dateClicked.getYear(), dateClicked.getMonthValue(),dateClicked.getDayOfMonth());
	}

	public ActionListener roomTypeSelected(boolean isluxury){
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				isLuxury = isluxury;
				// setting luxury type then displaying in textField if it's available or not		
			}
			
		};
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		// Getting available room from checkInDate to CheckOut Date or are we just only checking for the current day
		
		LocalDate dateAfter = LocalDate.of(dateClicked.getYear(), dateClicked.getMonthValue(),dateClicked.getDayOfMonth());
		System.out.println(dateAfter);
		roomInfo.setText(hotel.getAvailableRoom(dateClicked, dateAfter, isLuxury));
	}

	/**
	 * Updating the DateField when you choose a date from the ChangeDate Button
	 * @param dateChosen
	 */
	public void update(LocalDate dateChosen) {
		// TODO Auto-generated method stub
		dateField.setText(dateChosen.toString());
		dateClicked = dateChosen;
	}
}
