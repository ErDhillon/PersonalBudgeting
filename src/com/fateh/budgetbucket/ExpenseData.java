package com.fateh.budgetbucket;

import java.sql.Date;
/***************
 * Data Structure to store in DB for Report Generation
 * @author administrator
 *
 */
public class ExpenseData {

	public String mCategory="";
	public float mAmount;
	public String mDate;
	public String mMonth;
	
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

	public String GetMonth()
	{
	 return mMonth;
	}
	public void SetMonth(String val)
	{
	  mMonth = val;
	}
	
}
