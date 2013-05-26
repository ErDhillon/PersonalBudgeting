package com.fateh.personalbudgeting;

import java.sql.Date;

public class BudgetData {

	public String mMonth;
	public Float mIncome;
	public Float mActualFixedExpenses;
	public Float mActualVariableExpenses;
	public Float mFixedExpProgress;
	public Float mVariableExpProgess;
	public Float mIncomeProgress;
	
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
	
	public Float GetFixedExpProgress()
	{
	 return mFixedExpProgress ;
	}
	public void SetFixedExpProgress(Float val)
	{
	  mFixedExpProgress = val;
	}

	public Float GetVarExpProgress()
	{
	 return mVariableExpProgess ;
	}
	public void SetVarExpProgress(Float val)
	{
	  mVariableExpProgess = val;
	}
	
	public Float GetIncomeProgress()
	{
	 return mIncomeProgress ;
	}
	public void SetIncomeProgress(Float val)
	{
	  mIncomeProgress = val;
	}	
	
}
