/**
 * CalendarModel.java: model for manipulating calendar
 * Author: Kim Pham
 */
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarModel {
	private Calendar cal;
	private LocalDate now;
	private int monthsFromCurrent; // to navigate to next or previous month
	private LocalDate chosenDate;

	// for mvc model
	private ArrayList<ChangeListener> listener;

	// rowData to save dates of a month into string, used for drawing calendar
	private String[] rowData;

	/**
	 * Instantiate the calendat to present time, save the current date in "now"
	 */
	public CalendarModel() {
		cal = Calendar.getInstance();
		now = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
		monthsFromCurrent = 0;
		setRowData(); // set up row data with dates of the current month
		listener = new ArrayList<>();
		chosenDate = now;
	}

	/**
	 * Return the first date navigated too using pre and next
	 * 
	 * @return first date navigated to
	 */
	public LocalDate getRequestedDay() {
		int requestedYear = cal.get(Calendar.YEAR);
		int requestedMonth = cal.get(Calendar.MONTH);
		int requestedDay = cal.get(Calendar.DAY_OF_MONTH);
		return LocalDate.of(requestedYear, requestedMonth + 1, requestedDay);
	}

	/**
	 * Set the next month on the calendar
	 * 
	 * @param eventMan
	 *            An eventManager that manages events for this calendar
	 */
	public void nextMonth() {
		monthsFromCurrent = 1;
		this.setRowData();
	}

	/**
	 * Set the current month on the calendar
	 * 
	 * @param eventMan
	 *            An eventManager that manages events for this calendar
	 */
	public void currentMonth() {
		monthsFromCurrent = 0;
		this.setRowData();
	}

	/**
	 * Set the previous month on the calendar
	 * 
	 * @param eventMan
	 *            An eventManager that manages events for this calendar
	 */
	public void previousMonth() {
		monthsFromCurrent = -1;
		this.setRowData();
	}

	/**
	 * Fill in row data with dates of next month for days in the first and
	 * second week that is not in month, " " is assigned
	 */
	private void setRowData() {
		cal.add(Calendar.MONTH, monthsFromCurrent);

		// set calendar to current time
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);

		// get other important dates
		int firstDayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int weeksInMonth = 6;
		int day = 1;
		rowData = new String[weeksInMonth * 7];
		for (int i = 0; i < weeksInMonth * 7; i++) {
			if (i < firstDayOfMonth - 1) {
				rowData[i] = "  ";
			} else if (i > firstDayOfMonth - 2 + daysInMonth) {
				rowData[i] = "  ";
			} else {
				rowData[i] = "" + day;
				day++;
			}
		}

	}

	/**
	 * Get the date in the present time (regardless of which month navigated to)
	 * 
	 * @return return date in present time
	 */
	public LocalDate getDateNow() {
		return now;
	}

	/**
	 * 
	 * @return rowData of current month navigated to
	 */
	public String[] getRowData() {
		return rowData.clone();
	}

	/**
	 * overwriting chosenDate with param value
	 * 
	 * @param a
	 *            the localDate to be set as ChosenDate
	 */
	public void setChosenDate(LocalDate a) {
		chosenDate = a;
	}

	/**
	 * obtaining the chosenDate
	 * 
	 * @return chosenDate variable
	 */
	public LocalDate getChosenDate() {
		return chosenDate;
	}

	/**
	 * Attaches ChangeListener to model
	 * 
	 * @param l
	 *            changelistener to be added
	 */
	public void attachListener(ChangeListener l) {
		listener.add(l);
	}

	/**
	 * Updating the list of change listeners
	 */
	public void update() {
		for (int i = 0; i < listener.size(); i++) {
			listener.get(i).stateChanged(new ChangeEvent(this));
		}
	}

}
