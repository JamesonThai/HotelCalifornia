/**
 * CalendarDialog.java: Pop-up dialog for choosing dates
 * Author: Kim Pham
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.CalendarModel;

public class CalendarDialog extends JDialog implements ChangeListener {
	CalendarModel calendar;
	private JButton[] daybuttons;
	private JLabel calendarLabel;
	private LocalDate dateChosen;
	private boolean firstField;
	
	public CalendarDialog(MakeRsvpFrame owner, CalendarModel model){
		super(owner, "Calendar",true);
		JPanel calendarPanel = new JPanel();
		calendar = model;
		calendarLabel = new JLabel();
		calendarPanel.setLayout(new BorderLayout());
		
		//set up navigation and label
		//label
		LocalDate now = calendar.getDateNow();
		calendarLabel.setText(now.getMonth()+" "+now.getYear());
		
		//navigation
		JButton pre = new JButton("<");
		JButton next = new JButton(">");
		
		JPanel navigation = new JPanel();
		navigation.add(pre);
		navigation.add(calendarLabel);
		navigation.add(next);
		calendarPanel.add(navigation, BorderLayout.NORTH);
		
		//add listener to button
		pre.addActionListener(event->{
			calendar.previousMonth();
			calendar.update();
		});
		
		next.addActionListener(event->{
			calendar.nextMonth();
			calendar.update();
		});
		
		//set up button panel
		JPanel buttonPanel = new JPanel();
		JButton[] titleButtons = new JButton[7];
		
		//get month data from calendar model
		String[] row = calendar.getRowData();
		
		//set gridbad layout for buttonPanel to display button
		buttonPanel.setLayout(new GridLayout(7, 7));
		
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
					if(chosenButton.getText().matches("^[0-9]*$")){
					int dayValue = Integer.parseInt(chosenButton.getText());
					LocalDate currentMonthYear = calendar.getRequestedDay();
					dateChosen = LocalDate.of(currentMonthYear.getYear(), currentMonthYear.getMonthValue(), dayValue);
					this.setVisible(false);
					owner.update(dateChosen,firstField);	
					}
				}
			});
			
			buttonPanel.add(daybuttons[i]);
		}
		
		calendarPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.add(calendarPanel);
		
		this.pack();
		calendarPanel.setVisible(true);		
		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		String[] row = calendar.getRowData();
		for(int i = 0; i < daybuttons.length; i++){
			daybuttons[i].setText(row[i]);
		}
		LocalDate current = calendar.getRequestedDay();
		calendarLabel.setText(current.getMonth() +" "+current.getYear());
		repaint();
	}
	
	public void setField(boolean firstField){
		this.firstField = firstField;
	}
	
	
	
}
