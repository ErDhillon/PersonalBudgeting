package com.fateh.personalbudgeting;

import java.sql.Date;

public class ExpenseData {

	public String mCategory="";
	public float mAmount;
	public String mDate;
	
	public String GetCategory()
	{
	 return mCategory;
	}
	public void SetCategory(String val)
	{
	  mCategory = val;
	}
	
	public float GetAmount()
	{
	 return mAmount;
	}
	public void SetAmount(float val)
	{
	  mAmount = val;
	}
	
	public String GetDate()
	{
	 return mDate;
	}
	public void SetDate(String val)
	{
	  mDate = val;
	}
	
}
