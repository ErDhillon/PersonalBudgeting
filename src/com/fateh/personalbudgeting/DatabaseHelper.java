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
	public static String ACTUALLIMITSDATATABLE = "ActualLimits";
	public static String MONTHLYPROGRESSTABLE = "MonthlyProgress";

	// Fixed and Variable Expenses Columns names
    private static final String COLUMN_CATEGORY_NAME = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_MONTH = "month";
    
    // ACTUAL LIMITS Column Names
    private static final String COLUMN_ACTUAL_MONTH = "month";   
    private static final String COLUMN_ACTUAL_INCOME = "income";
    private static final String COLUMN_ACTUAL_SAVING = "saving";
    private static final String COLUMN_ACTUAL_UNALLOCATED = "unallocated";
    private static final String COLUMN_ACTUAL_FIXEDLIMIT = "actualFixedFxpenseLimit";
    private static final String COLUMN_ACTUAL_VARIABLELIMIT = "actualVariableExpenseLimit";
    
    //Monthly Progress Column Name
    private static final String COLUMN_PROGRESS_MONTH = "month";
    private static final String COLUMN_PROGRESS_INCOMEBALANCE = "IncomeBalnce";
    private static final String COLUMN_PROGRESS_SAVINGBALANCE = "Savings";
    private static final String COLUMN_PROGRESS_FIXEDBALANCE = "FixedBalance";
    private static final String COLUMN_PROGRESS_VARIABLEBALANCE = "VariableBalance";
    private static final String COLUMN_PROGRESS_STATUS = "MonthStatus";
    private static final String COLUMN_PROGRESS_OVERDRAWN = "OverdrawnAmount";
  
	private ArrayList<ExpenseData> expenses = new ArrayList<ExpenseData>();
	private ArrayList<ActualBudgetData> actualexpenses = new ArrayList<ActualBudgetData>();
	private ArrayList<MonthlyProgress> monthlyProgress = new ArrayList<MonthlyProgress>();
	
	
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
	static String monthName;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASENAME, null, 2);
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
		
        String CREATE_BUDGETINCOME_TABLE = "CREATE TABLE IF NOT EXISTS " + ACTUALLIMITSDATATABLE + "("
        		+ COLUMN_ACTUAL_MONTH + " TEXT, " 
        		+ COLUMN_ACTUAL_INCOME + " INTEGER, "
        		+ COLUMN_ACTUAL_SAVING + " INTEGER, "
        		+ COLUMN_ACTUAL_FIXEDLIMIT + " INTEGER,"
                + COLUMN_ACTUAL_VARIABLELIMIT + " INTEGER," 
        		+ COLUMN_ACTUAL_UNALLOCATED+ " INTEGER" +");"; 
		db.execSQL(CREATE_BUDGETINCOME_TABLE);	
		
        String CREATE_MONTHLYPROGRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + MONTHLYPROGRESSTABLE + "("
        		+ COLUMN_PROGRESS_MONTH + " TEXT, " 
        		+ COLUMN_PROGRESS_INCOMEBALANCE + " INTEGER, " 
        		+ COLUMN_PROGRESS_SAVINGBALANCE + " INTEGER, " 
        		+ COLUMN_PROGRESS_FIXEDBALANCE + " INTEGER,"
                + COLUMN_PROGRESS_VARIABLEBALANCE + " INTEGER,"
                + COLUMN_PROGRESS_STATUS + " TEXT, "
                + COLUMN_PROGRESS_OVERDRAWN + " INTEGER "
                +");"; 
		db.execSQL(CREATE_MONTHLYPROGRESS_TABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+ FIXEDEXPTABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ VARIABLEEXPTABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ ACTUALLIMITSDATATABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ MONTHLYPROGRESSTABLE);
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
		db.close();
	}
	
	
	public void UpdateMonthlyProgress(MonthlyProgress monthlyProgress)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		Cursor cursor = null;
		contentValues.put(COLUMN_PROGRESS_INCOMEBALANCE, monthlyProgress.mIncomeProgress);
		contentValues.put(COLUMN_PROGRESS_SAVINGBALANCE, monthlyProgress.mSavingProgress);
		contentValues.put(COLUMN_PROGRESS_FIXEDBALANCE, monthlyProgress.mFixedExpensesProgress);
		contentValues.put(COLUMN_PROGRESS_VARIABLEBALANCE, monthlyProgress.mVariableExpensesProgress);
		contentValues.put(COLUMN_PROGRESS_STATUS, monthlyProgress.mStatus);
		contentValues.put(COLUMN_PROGRESS_OVERDRAWN, monthlyProgress.mOverdrawnAmount);
		
		if(monthlyProgress.mRepeatOption.equals("MONTHLY"))
		{
			for(int i=0; i<12;i++)
			{
				monthlyProgress.mMonth = MONTHS[i];
				cursor = db.rawQuery("select * from "+ MONTHLYPROGRESSTABLE +" Where "+COLUMN_PROGRESS_MONTH + "=?", new String[]{monthlyProgress.mMonth});
				if(cursor.getCount() != 0)
				{
					db.update(MONTHLYPROGRESSTABLE, contentValues, COLUMN_PROGRESS_MONTH +" =? " , new String[]{monthlyProgress.mMonth});
					
				}
				else
				{
					contentValues.put(COLUMN_PROGRESS_MONTH, monthlyProgress.mMonth);
					db.insert(MONTHLYPROGRESSTABLE, null, contentValues);
				}
			}
		}
		{
			cursor = db.rawQuery("select * from "+ MONTHLYPROGRESSTABLE +" Where "+COLUMN_PROGRESS_MONTH + "=?", new String[]{monthlyProgress.mMonth});
			if(cursor.getCount() != 0)
			{
				db.update(MONTHLYPROGRESSTABLE, contentValues, COLUMN_PROGRESS_MONTH +" =? " , new String[]{monthlyProgress.mMonth});
				
			}
			else
			{
				contentValues.put(COLUMN_PROGRESS_MONTH, monthlyProgress.mMonth);
				db.insert(MONTHLYPROGRESSTABLE, null, contentValues);
			}
		}
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
		contentValues.put(COLUMN_ACTUAL_INCOME, bdata.mIncome);
		contentValues.put(COLUMN_ACTUAL_SAVING, bdata.mSaving);
		contentValues.put(COLUMN_ACTUAL_FIXEDLIMIT, bdata.mActualFixedExpenses);
		contentValues.put(COLUMN_ACTUAL_VARIABLELIMIT, bdata.mActualVariableExpenses);
		contentValues.put(COLUMN_ACTUAL_UNALLOCATED, bdata.mUnallocatedAmount);
		
		if(bdata.mRepeatOption.equals("MONTHLY"))
		{
			for(int i=0; i<12;i++)
			{
				bdata.mMonth = MONTHS[i];
				InsertRecordIntoActualLimitTable(bdata.mMonth, contentValues, db, cursor);
			}
		}
		else
		{
			InsertRecordIntoActualLimitTable(bdata.mMonth, contentValues, db, cursor);
		}
		db.close();
	}
	private void InsertRecordIntoActualLimitTable(String month, ContentValues contentValues, SQLiteDatabase db, Cursor cursor )
	{
		cursor = db.rawQuery("select * from "+ ACTUALLIMITSDATATABLE +" Where "+COLUMN_ACTUAL_MONTH+ "=?", new String[]{month});
		if(cursor.getCount() != 0)
		{
			db.update(ACTUALLIMITSDATATABLE, contentValues, COLUMN_ACTUAL_MONTH +" =? " , new String[]{month});
		}
		else
		{
			contentValues.put(COLUMN_ACTUAL_MONTH, month);
			db.insert(ACTUALLIMITSDATATABLE, null, contentValues);
		}
	}
	
	/**********************
	 * Get Current Progress for a given Month
	 * @param month
	 * @return
	 */
	public ArrayList<MonthlyProgress> GetCurrentProgress(String month)
	{
		monthlyProgress.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ MONTHLYPROGRESSTABLE +" Where "+COLUMN_PROGRESS_MONTH + "=?", new String[]{month});
		if(cursor.getCount() != 0)
		{
			if(cursor.moveToFirst())
			{
				do
				{
					MonthlyProgress prog = new MonthlyProgress();
					prog.mIncomeProgress = cursor.getFloat(cursor.getColumnIndex(COLUMN_PROGRESS_INCOMEBALANCE));
					prog.mSavingProgress = cursor.getFloat(cursor.getColumnIndex(COLUMN_PROGRESS_SAVINGBALANCE));
					prog.mFixedExpensesProgress = cursor.getFloat(cursor.getColumnIndex(COLUMN_PROGRESS_FIXEDBALANCE));
					prog.mVariableExpensesProgress = cursor.getFloat(cursor.getColumnIndex(COLUMN_PROGRESS_VARIABLEBALANCE));
					prog.mMonth = cursor.getString(cursor.getColumnIndex(COLUMN_PROGRESS_MONTH));
					prog.mOverdrawnAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_PROGRESS_OVERDRAWN));
					monthlyProgress.add(prog);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return monthlyProgress;
		
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
			Cursor cursor = db.rawQuery("select * from "+ ACTUALLIMITSDATATABLE +" Where "+COLUMN_ACTUAL_MONTH + "=?", new String[]{month});
			if(cursor.getCount() != 0)
			{
				if(cursor.moveToFirst())
				{
					do
					{
						ActualBudgetData fe = new ActualBudgetData();
						fe.mIncome = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_INCOME));
						fe.mSaving = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_SAVING));
						fe.mActualFixedExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_FIXEDLIMIT));
						fe.mActualVariableExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_VARIABLELIMIT));
						fe.mMonth = cursor.getString(cursor.getColumnIndex(COLUMN_ACTUAL_MONTH));
						fe.mUnallocatedAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_UNALLOCATED));
						
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
			Cursor cursor = db.rawQuery("select * from "+ ACTUALLIMITSDATATABLE, null);
			if(cursor.getCount() != 0)
			{
				if(cursor.moveToFirst())
				{
					do
					{
						ActualBudgetData fe = new ActualBudgetData();
						fe.mIncome = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_INCOME));
						fe.mSaving = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_SAVING));
						fe.mActualFixedExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_FIXEDLIMIT));
						fe.mActualVariableExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_VARIABLELIMIT));
						fe.mUnallocatedAmount = cursor.getFloat(cursor.getColumnIndex(COLUMN_ACTUAL_UNALLOCATED));
						
						actualexpenses.add(fe);
					}while(cursor.moveToNext());
				}
			}
			cursor.close();
			db.close();
			return actualexpenses;
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
			cursor = db.rawQuery("select * from "+ FIXEDEXPTABLE +" Where "+COLUMN_MONTH + "=?", new String[]{month});
		
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
			cursor = db.rawQuery("select * from "+ VARIABLEEXPTABLE +" Where "+COLUMN_MONTH + "=?", new String[]{month});

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
	

}
