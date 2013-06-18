package com.fateh.personalbudgeting;

import java.sql.Date;

/********************
 * Data structure to store information in DB
 * @author administrator
 *
 */
public class MonthlyProgress {

	public String mMonth;
	public Float mIncomeProgress;
	public Float mFixedExpensesProgress;
	public Float mVariableExpensesProgress;
	public Float mSavingProgress;
	public String mStatus;
	public Float mOverdrawnAmount;
	public String mRepeatOption;
	
	public Float GetIncomeProgress()
	{
	 return mIncomeProgress;
	}
	public void SetIncomeProgress(Float val)
	{
		mIncomeProgress = val;
	}
	
	public Float GetMonthlySaving()
	{
	 return mSavingProgress;
	}
	public void SetMonthlySaving(Float val)
	{
		mSavingProgress = val;
	}
	
	public Float GetFixedExpenseProgress()
	{
	 return mFixedExpensesProgress;
	}
	public void SetFixedExpenseProgress(Float val)
	{
		mFixedExpensesProgress = val;
	}
	
	public Float GetVariableExpenseProgress()
	{
	 return mVariableExpensesProgress;
	}
	public void SetVariableExpenseProgress(Float val)
	{
		mVariableExpensesProgress = val;
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
	
	public String GetMonthlyStatus()
	{
	 return mStatus;
	}
	public void SetMonthlyStatus(String val)
	{
	  mStatus = val;
	}

	public Float GetOverDrawnAmount()
	{
	 return mOverdrawnAmount;
	}
	public void SetOverDrawnAmount(Float val)
	{
		mOverdrawnAmount = val;
	}
	
}
