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

public class CalendarModel{
	private Calendar cal;
	private LocalDate now;
	private int monthsFromCurrent; //to navigate to next or previous month
	private LocalDate chosenDate;
	
	//for mvc model stuff
	private ArrayList<ChangeListener> listener;
	
	//rowData to save dates of a month into string, used for drawing calendar
	private String[] rowData; 
	
	//instantiate calendar to present time, save the current date in "now"
	public CalendarModel(){
		cal = Calendar.getInstance();
		now = LocalDate.of(cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
		monthsFromCurrent = 0;
		setRowData(); //set up row data with dates of the current month
		listener = new ArrayList<>();
		chosenDate = now;
	}
	
	//Return the first date of the you navigated to using pre and next
	public LocalDate getRequestedDay(){
		int requestedYear = cal.get(Calendar.YEAR);
		int requestedMonth = cal.get(Calendar.MONTH);
		int requestedDay = cal.get(Calendar.DAY_OF_MONTH);
		return LocalDate.of(requestedYear, requestedMonth+1, requestedDay);
	}
	
	/**
	 * Set the next month on the calendar
	 * @param eventMan An eventManager that manages events for this calendar
	 */
	public void nextMonth(){
		monthsFromCurrent = 1;
		this.setRowData();
	}
	
	/**
	 * Set the current month on the calendar
	 * @param eventMan An eventManager that manages events for this calendar
	 */
	public void currentMonth(){
		monthsFromCurrent = 0;
		this.setRowData();
	}
	
	/**
	 * Set the previous month on the calendar
	 * @param eventMan An eventManager that manages events for this calendar
	 */
	public void previousMonth(){
		monthsFromCurrent = -1;
		this.setRowData();
	}
	
	//fill row data with dates of next month(for days in the first and 
	// second week that is not in the month, "  " is assigned
	private void setRowData(){
		cal.add(Calendar.MONTH, monthsFromCurrent);
		
		//set calendar to current time
		cal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),1);
		
		//get other important dates
		int firstDayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	//	int weeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		int weeksInMonth = 6;
		int day = 1;
		//System.out.println(weeksInMonth);
		rowData = new String[weeksInMonth*7];
		for(int i = 0; i < weeksInMonth*7; i++){
			if(i < firstDayOfMonth - 1){
				rowData[i]="  ";
			}
			else if(i > firstDayOfMonth-2 + daysInMonth){
				rowData[i]="  ";
			}else{
				rowData[i]=""+day;
				day++;
			}
		}
		
	}
	
	//get the date in the present time(regardless of with month you navigated to
	public LocalDate getDateNow(){
		return now;
	}
	
	//return the rowData of the current month you navigated to
	public String[] getRowData(){
		return rowData.clone();
	}
	
	public void setChosenDate(LocalDate a){
		chosenDate = a;
	}
	
	public LocalDate getChosenDate(){
		return chosenDate;
	}
	
	public void attachListener(ChangeListener l){
		listener.add(l);
	}
	
	public void update(){
		for(int i = 0; i < listener.size(); i++){
			listener.get(i).stateChanged(new ChangeEvent(this));
		}
	}
	
}
