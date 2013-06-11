package com.fateh.personalbudgeting;

import java.sql.Date;

/********************
 * Data structure to store information in DB
 * @author administrator
 *
 */
public class ActualBudgetData {

	public String mMonth;
	public Float mIncome;
	public Float mActualFixedExpenses;
	public Float mActualVariableExpenses;
	public String mRepeatOption;
	
	public Float GetIncome()
	{
	 return mIncome;
	}
	public void SetIncome(Float val)
	{
	  mIncome = val;
	}
	
	public Float GetFixedActualExpense()
	{
	 return mActualFixedExpenses;
	}
	public void SetFixedActualExpense(Float val)
	{
		mActualFixedExpenses = val;
	}
	
	public Float GetVariableActualExpense()
	{
	 return mActualVariableExpenses;
	}
	public void SetVariableActualExpense(Float val)
	{
	  mActualVariableExpenses = val;
	}
	
	public String GetMonth()
	{
	 return mMonth;
	}
	public void SetMonth(String val)
	{
	  mMonth = val;
	}

	public String GetIsRepeat()
	{
	 return mRepeatOption;
	}
	public void SetIsRepeat(String val)
	{
	  mRepeatOption = val;
	}
	
}
