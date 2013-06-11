package com.fateh.personalbudgeting;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*************
 * Database Operations are defined in this class
 * @author administrator
 *
 */
@SuppressLint("NewApi")
public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static String DATABASENAME = "PersonalBudgetDB";
	public static String FIXEDEXPTABLE = "fixedexpenses";
	public static String VARIABLEEXPTABLE = "variableexpenses";
	public static String BUDGETDATATABLE = "ActualLimits";
	public static String EXPENSEPROGRESSTABLE = "Expenses";

	// Fixed and Variable Expenses Columns names
    private static final String COLUMN_CATEGORY_NAME = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_MONTH = "month";
    
    // Budget Column Names
    private static final String COLUMN_BUDGET_MONTH = "month";   
    private static final String COLUMN_BUDGET_INCOME = "income";
    private static final String COLUMN_BUDGET_ACTUALFIXED = "actualfixedexpense";
    private static final String COLUMN_BUDGET_ACTUALVARIABLE = "actualvariableexpense";
    
    //Expense Column Name
    private static final String COLUMN_EXPENSE_MONTH = "month";
    private static final String COLUMN_EXPENSE_INCOMEBAL = "IncomeBalnce";
    private static final String COLUMN_EXPENSE_FIXEDBAL = "FixedBalance";
    private static final String COLUMN_EXPENSE_VARIABLEBAL = "VariableBalance";
    
    
	private ArrayList<ExpenseData> expenses = new ArrayList<ExpenseData>();
	private ArrayList<ActualBudgetData> actualexpenses = new ArrayList<ActualBudgetData>();
	
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
	static String monthName;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASENAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	/*****
	 * Create the Tables
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

        String CREATE_FIXEDEXP_TABLE = "CREATE TABLE IF NOT EXISTS " + FIXEDEXPTABLE + "("
        		+ COLUMN_MONTH + " TEXT, "
                + COLUMN_AMOUNT + " INTEGER, " 
        		+ COLUMN_CATEGORY_NAME + " TEXT,"
                + COLUMN_DATE + " TEXT" + ");";
		db.execSQL(CREATE_FIXEDEXP_TABLE);
		
        String CREATE_VARIABLEEXP_TABLE = "CREATE TABLE IF NOT EXISTS " + VARIABLEEXPTABLE + "("
        		+ COLUMN_MONTH + " TEXT, "
                + COLUMN_AMOUNT + " INTEGER, " 
        		+ COLUMN_CATEGORY_NAME + " TEXT,"
                + COLUMN_DATE + " TEXT" + ");";
		db.execSQL(CREATE_VARIABLEEXP_TABLE);	
		
        String CREATE_BUDGETINCOME_TABLE = "CREATE TABLE IF NOT EXISTS " + BUDGETDATATABLE + "("
        		+ COLUMN_BUDGET_MONTH + " TEXT, " 
        		+ COLUMN_BUDGET_INCOME + " INTEGER, " 
        		+ COLUMN_BUDGET_ACTUALFIXED + " INTEGER,"
                + COLUMN_BUDGET_ACTUALVARIABLE + " INTEGER" +");"; 
		db.execSQL(CREATE_BUDGETINCOME_TABLE);	
		
        String CREATE_EXPENSEPROGRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + EXPENSEPROGRESSTABLE + "("
        		+ COLUMN_EXPENSE_MONTH + " TEXT, " 
        		+ COLUMN_EXPENSE_INCOMEBAL + " INTEGER, " 
        		+ COLUMN_EXPENSE_FIXEDBAL + " INTEGER,"
                + COLUMN_EXPENSE_VARIABLEBAL + " INTEGER" +");"; 
		db.execSQL(CREATE_EXPENSEPROGRESS_TABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+ FIXEDEXPTABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ VARIABLEEXPTABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ BUDGETDATATABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ EXPENSEPROGRESSTABLE);
		onCreate(db);
	}
	
	public void AddFixedExpenseRecord(ExpenseData expdata, String repeatOption, int day, int year)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_CATEGORY_NAME,expdata.mCategory);
		contentValues.put(COLUMN_AMOUNT, expdata.mAmount);
		if(repeatOption.equals("MONTHLY"))
		{
			for(int i=0; i<12;i++)
			{
				expdata.mMonth = MONTHS[i];
				expdata.mDate = expdata.mMonth+""+day+","+year;
				contentValues.put(COLUMN_DATE, expdata.mDate);
				contentValues.put(COLUMN_MONTH, expdata.mMonth);
				db.insert(FIXEDEXPTABLE, null, contentValues);
			}
		}
		else
		{
			contentValues.put(COLUMN_DATE, expdata.mDate);
			contentValues.put(COLUMN_MONTH, expdata.mMonth);
			db.insert(FIXEDEXPTABLE, null, contentValues);
		}
			
		db.close();
	}

	public void AddVariableExpenseRecord(ExpenseData expdata)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_CATEGORY_NAME,expdata.mCategory);
		contentValues.put(COLUMN_AMOUNT, expdata.mAmount);
		contentValues.put(COLUMN_DATE, expdata.mDate);
		contentValues.put(COLUMN_MONTH, expdata.mMonth);
		db.insert(VARIABLEEXPTABLE, null, contentValues);

//		if(repeatOption.equals("MONTHLY"))
//		{
//			for(int i=0; i<12;i++)
//			{
//				expdata.mMonth = MONTHS[i];
//				contentValues.put(COLUMN_MONTH, expdata.mMonth);
//				db.insert(VARIABLEEXPTABLE, null, contentValues);
//			}
//		}
//		else
//		{
//			contentValues.put(COLUMN_MONTH, expdata.mMonth);
//		}

		db.close();
	}
	
	/************
	 * Insert the Budget Data into BudgetTable
	 * Called for Setting Activity's onDestroy method.
	 * @param bdata
	 */
	public void InsertBudgetLimits(ActualBudgetData bdata)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		Cursor cursor = null;
		contentValues.put(COLUMN_BUDGET_INCOME, bdata.mIncome);
		contentValues.put(COLUMN_BUDGET_ACTUALFIXED, bdata.mActualFixedExpenses);
		contentValues.put(COLUMN_BUDGET_ACTUALVARIABLE, bdata.mActualVariableExpenses);
		if(bdata.mRepeatOption.equals("MONTHLY"))
		{
			for(int i=0; i<12;i++)
			{
				bdata.mMonth = MONTHS[i];
				cursor = db.rawQuery("select * from "+ BUDGETDATATABLE +" Where "+COLUMN_BUDGET_MONTH + "=?", new String[]{bdata.mMonth});
				if(cursor.getCount() != 0)
				{
					db.update(BUDGETDATATABLE, contentValues, COLUMN_BUDGET_MONTH +" =? " , new String[]{bdata.mMonth});
				}
				else
				{
					contentValues.put(COLUMN_BUDGET_MONTH, bdata.mMonth);
					db.insert(BUDGETDATATABLE, null, contentValues);
				}
			}
		}
		else
		{
			cursor = db.rawQuery("select * from "+ BUDGETDATATABLE +" Where "+COLUMN_BUDGET_MONTH + "=?", new String[]{bdata.mMonth});
			if(cursor.getCount() != 0)
			{
				db.update(BUDGETDATATABLE, contentValues, COLUMN_BUDGET_MONTH +" =? " , new String[]{bdata.mMonth});
			}
			else
			{
				contentValues.put(COLUMN_BUDGET_MONTH, bdata.mMonth);
				db.insert(BUDGETDATATABLE, null, contentValues);
			}
		}
		db.close();
	}
	
	/*******
	 * If no month is passed, Get all the expenses
	 * otherwise get for particular month
	 * @return
	 */
	@SuppressLint("NewApi")
	public ArrayList<ExpenseData> getFixedExpenses(String month)
	{
		expenses.clear();
		Cursor cursor = null;
		SQLiteDatabase db = this.getWritableDatabase();
		if(month.isEmpty())
			cursor = db.rawQuery("select * from "+ FIXEDEXPTABLE, null);
		else
			cursor = db.rawQuery("select * from "+ FIXEDEXPTABLE +" Where "+COLUMN_EXPENSE_MONTH + "=?", new String[]{month});
		
		if(cursor.getCount() != 0)
		{
			if(cursor.moveToFirst())
			{
				do
				{
					ExpenseData fe = new ExpenseData();
					fe.mCategory = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
					fe.mAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT));
					fe.mDate = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
					expenses.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return expenses;
	}
	
	/*******
	 * If no month is passed, Get all the expenses
	 * otherwise get for particular month
	 * @return
	 */
	public ArrayList<ExpenseData> getVariableExpenses(String month)
	{
		expenses.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		if(month.isEmpty())
			cursor = db.rawQuery("select * from "+ VARIABLEEXPTABLE, null);
		else
			cursor = db.rawQuery("select * from "+ VARIABLEEXPTABLE +" Where "+COLUMN_EXPENSE_MONTH + "=?", new String[]{month});

		if(cursor.getCount() != 0)
		{
			if(cursor.moveToFirst())
			{
				do
				{
					ExpenseData fe = new ExpenseData();
					fe.mCategory = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
					fe.mAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT));
					fe.mDate = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
					expenses.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return expenses;
	}
	
/************
 * Get the Current BudgetData for a specific Month
 * @param month
 * @return
 */
	public ArrayList<ActualBudgetData> getCurrentBudgetData(String month)
	{
		actualexpenses.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ BUDGETDATATABLE +" Where "+COLUMN_BUDGET_MONTH + "=?", new String[]{month});
		if(cursor.getCount() != 0)
		{
			if(cursor.moveToFirst())
			{
				do
				{
					ActualBudgetData fe = new ActualBudgetData();
					fe.mIncome = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_INCOME));
					fe.mActualFixedExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_ACTUALFIXED));
					fe.mActualVariableExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_ACTUALVARIABLE));
					fe.mMonth = cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_MONTH));
					
					actualexpenses.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return actualexpenses;
	}	

/**************
 * Get CurrentBudgetData for all months
 * @return
 */
	public ArrayList<ActualBudgetData> getActualExpenses()
	{
		actualexpenses.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ BUDGETDATATABLE, null);
		if(cursor.getCount() != 0)
		{
			if(cursor.moveToFirst())
			{
				do
				{
					ActualBudgetData fe = new ActualBudgetData();
					fe.mIncome = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_INCOME));
					fe.mActualFixedExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_ACTUALFIXED));
					fe.mActualVariableExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_ACTUALVARIABLE));
					actualexpenses.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return actualexpenses;
	}	

}
