/**
 * SignInDialog.java: Dialog where user can sign in
 * Author: Kim Pham
 */

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.HotelReservationModel;

public class SignInDialog extends JDialog {
	JLabel nameLabel;
	JTextField nameField;
	JLabel idLabel;
	JTextField idField;
	JButton signIn;
	HotelReservationModel model;
	private SignUpDialog signUpDialog;
	private JButton signUp;

	/**
	 * Constructor for Sign In dialog
	 * 
	 * @param owner
	 *            frame to assign which to popup
	 * @param model
	 *            HotelReservationModel
	 */
	public SignInDialog(JFrame owner, HotelReservationModel model) {
		super(owner, "Sign Up", true);
		this.model = model;
		nameLabel = new JLabel("UserName");
		idLabel = new JLabel("User id ");
		nameField = new JTextField(20);
		idField = new JTextField(20);
		signIn = new JButton("Sign In");
		signUp = new JButton("New User? Sign Up");

		JPanel panel = new JPanel();
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		p1.add(nameLabel);
		p1.add(nameField);
		p2.add(idLabel);
		p2.add(idField);
		p3.add(signIn);
		p3.add(signUp);

		panel.setLayout(new BorderLayout());
		panel.add(p1, BorderLayout.NORTH);
		panel.add(p2, BorderLayout.CENTER);
		panel.add(p3, BorderLayout.SOUTH);
		signIn.addActionListener(event -> {
			String name = nameField.getText();
			String num = idField.getText();
			if (name.isEmpty() || !num.matches("^[0-9]*$") || num.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter valid name and Id");
			} else {
				int id = Integer.parseInt(idField.getText());
				boolean result = model.signInGuest(name, id);
				if (result) {
					setVisible(false);
					owner.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(owner, "User does not exsit");
					setVisible(true);
				}
			}
		});

		signUp.addActionListener(event -> {
			if (signUpDialog == null) {
				signUpDialog = new SignUpDialog(this, model);
				signUpDialog.setVisible(true);
				this.setVisible(false);
			}
		});

		this.add(panel);
		this.pack();
	}
}
