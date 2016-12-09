
/**
 * SignUpDialog.java: dialog where user can sign up
 * Author: Kim Pham
 */

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.HotelReservationModel;

public class SignUpDialog extends JDialog {
	private JLabel nameLabel;
	private JTextField nameTextField;
	HotelReservationModel model;

	/**
	 * Constructor for SignUpDialog
	 * 
	 * @param owner
	 *            Determine JDialog to open to
	 * @param model
	 *            HotelReserVationModel
	 */
	public SignUpDialog(JDialog owner, HotelReservationModel model) {
		this.model = model;
		nameLabel = new JLabel("Enter name: ");
		nameTextField = new JTextField(20);
		JButton signUp = new JButton("Sign Up");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(nameLabel);
		panel.add(nameTextField);
		panel.add(signUp);
		this.add(panel);
		this.pack();
		signUp.addActionListener(event -> {
			String name = nameTextField.getText();
			if (!name.equals("")) {
				int i = model.signUpGuest(nameTextField.getText());
				if (i != -1) {
					JOptionPane.showMessageDialog(this, "Your signed up.Your user id is " + i);
					setVisible(false);
					owner.setVisible(false);
					owner.getOwner().setVisible(true);
				} else {
					JOptionPane.showMessageDialog(this, "Error signing up");
					setVisible(true);
				}
			} else
				JOptionPane.showMessageDialog(this, "Please enter a name");

		});

	}

}
