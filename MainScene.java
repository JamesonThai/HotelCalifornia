
/**
 * MainScene.java: Initial Screen with choice of Guest or Manager
 * Author: Kim Pham
 */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.HotelReservationModel;

public class MainScene extends JFrame {
	private HotelReservationModel model;
	private GuestScene guestScene;
	private ManagerScene managerScene;
	private JButton guestButton;
	private JButton managerButton;
	private JPanel buttonFrame;

	/**
	 * Constructor of the main Scene
	 * 
	 * @param model
	 *            Hotel Model
	 */
	public MainScene(HotelReservationModel model) {
		this.model = model;
		this.setTitle("Hotel Reservation System");

		guestButton = new JButton("Guest");

		guestButton.addActionListener(event -> {
			guestScene = new GuestScene(model);
		});

		managerButton = new JButton("Manager");

		managerButton.addActionListener(event -> {
			if (managerScene == null)
				managerScene = new ManagerScene(model);
			else
				managerScene.setVisible(true);
		});

		buttonFrame = new JPanel();
		buttonFrame.add(guestButton);
		buttonFrame.add(managerButton);
		this.add(buttonFrame);

		this.setSize(250, 250);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
