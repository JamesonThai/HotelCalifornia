import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.HotelReservationModel;

/**
 * Changes model from cancel inputs and displays booked rooms from user
 * Pop-up dialog for choosing dates
 *
 */
public class ViewCancelRsvp extends JFrame implements ChangeListener {
	private HotelReservationModel hotel;
	private JTextArea bookedRooms;
	private JTextField cancelInput;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;

	/**
	 * Constructor of Hotel that takes in the Hotel Model
	 * 
	 * @param hotel
	 *            Hotel being edited
	 */
	public ViewCancelRsvp(HotelReservationModel hotel) {
		this.hotel = hotel;
		hotel.attach(this);
		hotel.setGetReceipt(new HotelReservationModel.SimpleReceipt());

		// Labels
		JLabel msg1 = new JLabel("List Of Booked Rooms");
		JLabel msg2 = new JLabel("To Cancel, Input Listing Number");
		JLabel cancelLabel = new JLabel("Cancel Room: ");

		// Panels
		JPanel MainPnl;
		JPanel RoomsPnl;
		JPanel displayPnl;
		JPanel cancelPnl;
		JPanel navPnl;
		JPanel instruct;

		// Buttons
		JButton cancel = new JButton("Confirm");
		JButton done = new JButton("Done");

		// MainPnl Panel
		MainPnl = new JPanel();
		MainPnl.setLayout(new BorderLayout());

		bookedRooms = new JTextArea(25, 30);
		bookedRooms.setLineWrap(true);
		bookedRooms.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(bookedRooms);

		cancelInput = new JTextField(10);

		// Action Listeners
		// Display events with a number next to room and then User call number
		// to cancel
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String s = cancelInput.getText();
				cancelInput.setText("");
				int a = Integer.parseInt(s) - 1;
				String line = hotel.returnListOfBookedRooms().get(a);
				String[] t = line.split(",");
				System.out.println(t[0]);
				String[] CID = t[2].split("-");
				String[] COD = t[3].split("-");
				checkInDate = LocalDate.of(Integer.parseInt(CID[0]), Integer.parseInt(CID[1]),
						Integer.parseInt(CID[2]));
				checkOutDate = LocalDate.of(Integer.parseInt(COD[0]), Integer.parseInt(COD[1]),
						Integer.parseInt(COD[2]));
				hotel.cancelReservation(t[0], Integer.parseInt(t[1]), checkInDate, checkOutDate);
				hotel.update();
			}

		});

		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}

		});
		RoomsPnl = new JPanel();
		RoomsPnl.setLayout(new BorderLayout());
		RoomsPnl.add(msg1, BorderLayout.NORTH);
		RoomsPnl.add(scrollPane, BorderLayout.SOUTH);

		MainPnl.add(RoomsPnl, BorderLayout.WEST);

		displayPnl = new JPanel();
		displayPnl.setLayout(new BorderLayout());
		cancelPnl = new JPanel();
		cancelPnl.add(cancelLabel);
		cancelPnl.add(cancelInput);
		displayPnl.add(cancelPnl, BorderLayout.NORTH);

		navPnl = new JPanel();
		navPnl.add(cancel);
		navPnl.add(done);
		displayPnl.add(navPnl, BorderLayout.SOUTH);

		instruct = new JPanel();
		instruct.add(msg2);

		msg2.setBounds(10, 10, 200, 25);

		displayPnl.add(instruct, BorderLayout.CENTER);
		MainPnl.add(displayPnl, BorderLayout.EAST);
		this.add(MainPnl, BorderLayout.SOUTH);
		displayBookedRooms();

		this.setVisible(true);
		this.pack();
	}

	/**
	 * Display the list of Reserved Rooms
	 */
	public void displayBookedRooms() {
		String c = "No Booked Rooms!";
		int i = 0;
		//System.out.println("Help");
		if (hotel.printReceipt() != null) {
			c = "";
			ArrayList<String> temp = hotel.returnListOfBookedRooms();
			for (String t : temp) {
				i++;
			//	System.out.println("I'm here!");
				c += i + ". " + t + "\n";
			}
		}
		bookedRooms.setText(c);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		displayBookedRooms();
	}
}
