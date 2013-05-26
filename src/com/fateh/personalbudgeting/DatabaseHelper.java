package com.fateh.personalbudgeting;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static String DATABASENAME = "PersonalBudgetDB";
	public static String FIXEDEXPTABLE = "fixedexpenses";
	public static String VARIABLEEXPTABLE = "variableexpenses";
	public static String BUDGETDATATABLE = "budgetdata";

	   // Fixed and Variable Expenses Columns names
    private static final String COLUMN_CATEGORY_NAME = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    
    // Budget Column Names
    private static final String COLUMN_BUDGET_MONTH = "month";   
    private static final String COLUMN_BUDGET_INCOME = "income";
    private static final String COLUMN_BUDGET_ACTUALFIXED = "actualfixed";
    private static final String COLUMN_BUDGET_ACTUALVARIABLE = "actualvariable";
    private static final String COLUMN_BUDGET_FIXEDEXPPROGRESS = "fixedProgress";
    private static final String COLUMN_BUDGET_VAREXPPROGRESS = "variableProgress"; 
    private static final String COLUMN_BUDGET_INCOMEPROGRESS = "incomeProgress"; 
    
	private ArrayList<ExpenseData> fixedexpensesList = new ArrayList<ExpenseData>();
	private ArrayList<BudgetData> actualexpenses = new ArrayList<BudgetData>();
	
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASENAME, null, 36);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

        String CREATE_FIXEDEXP_TABLE = "CREATE TABLE IF NOT EXISTS " + FIXEDEXPTABLE + "("
                + COLUMN_AMOUNT + " INTEGER, " 
        		+ COLUMN_CATEGORY_NAME + " TEXT,"
                + COLUMN_DATE + " TEXT" + ");";
		db.execSQL(CREATE_FIXEDEXP_TABLE);
		
        String CREATE_VARIABLEEXP_TABLE = "CREATE TABLE IF NOT EXISTS " + VARIABLEEXPTABLE + "("
                + COLUMN_AMOUNT + " INTEGER, " 
        		+ COLUMN_CATEGORY_NAME + " TEXT,"
                + COLUMN_DATE + " TEXT" + ");";
		db.execSQL(CREATE_VARIABLEEXP_TABLE);	
		
        String CREATE_BUDGETINCOME_TABLE = "CREATE TABLE IF NOT EXISTS " + BUDGETDATATABLE + "("
        		+ COLUMN_BUDGET_MONTH + " TEXT, " 
        		+ COLUMN_BUDGET_INCOME + " INTEGER, " 
        		+ COLUMN_BUDGET_ACTUALFIXED + " INTEGER,"
                + COLUMN_BUDGET_ACTUALVARIABLE + " INTEGER, " 
        		+ COLUMN_BUDGET_FIXEDEXPPROGRESS + " INTEGER, "
        		+ COLUMN_BUDGET_VAREXPPROGRESS + " INTEGER, " 
        		+ COLUMN_BUDGET_INCOMEPROGRESS +" INTEGER" +");";
		db.execSQL(CREATE_BUDGETINCOME_TABLE);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+ FIXEDEXPTABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ VARIABLEEXPTABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ BUDGETDATATABLE);
		onCreate(db);
	}
	
	public void AddFixedExpenseRecord(ExpenseData expdata)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_CATEGORY_NAME,expdata.mCategory);
		contentValues.put(COLUMN_AMOUNT, expdata.mAmount);
		contentValues.put(COLUMN_DATE, expdata.mDate);
		db.insert(FIXEDEXPTABLE, null, contentValues);
		db.close();
	}

	public void AddVariableExpenseRecord(ExpenseData expdata)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_CATEGORY_NAME,expdata.mCategory);
		contentValues.put(COLUMN_AMOUNT, expdata.mAmount);
		contentValues.put(COLUMN_DATE, expdata.mDate);
		db.insert(VARIABLEEXPTABLE, null, contentValues);
		db.close();
	}
	
	
	public void InsertBudgetLimits(BudgetData bdata)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(COLUMN_BUDGET_INCOME, bdata.mIncome);
		contentValues.put(COLUMN_BUDGET_ACTUALFIXED, bdata.mActualFixedExpenses);
		contentValues.put(COLUMN_BUDGET_ACTUALVARIABLE, bdata.mActualVariableExpenses);
		contentValues.put(COLUMN_BUDGET_FIXEDEXPPROGRESS, bdata.mFixedExpProgress);
		contentValues.put(COLUMN_BUDGET_VAREXPPROGRESS, bdata.mVariableExpProgess);
		contentValues.put(COLUMN_BUDGET_INCOMEPROGRESS, bdata.mIncomeProgress);
		
		Cursor cursor = db.rawQuery("select * from "+ BUDGETDATATABLE +" Where "+COLUMN_BUDGET_MONTH + "=?", new String[]{bdata.mMonth});
		if(cursor.getCount() != 0)
		{
			db.update(BUDGETDATATABLE, contentValues, COLUMN_BUDGET_MONTH +" =? " , new String[]{bdata.mMonth});
		}
		else
		{
			contentValues.put(COLUMN_BUDGET_MONTH, bdata.mMonth);
			db.insert(BUDGETDATATABLE, null, contentValues);
		}
		db.close();
	}
	
	public ArrayList<ExpenseData> getFixedExpenses()
	{
		fixedexpensesList.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ FIXEDEXPTABLE, null);
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
					fixedexpensesList.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return fixedexpensesList;
	}
	
	public ArrayList<BudgetData> getCurrentBudgetData()
	{
		actualexpenses.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ BUDGETDATATABLE +" Where "+COLUMN_BUDGET_MONTH + "=?", new String[]{"MAY"});
		if(cursor.getCount() != 0)
		{
			if(cursor.moveToFirst())
			{
				do
				{
					BudgetData fe = new BudgetData();
					fe.mIncome = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_INCOME));
					fe.mActualFixedExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_ACTUALFIXED));
					fe.mActualVariableExpenses = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_ACTUALVARIABLE));
					fe.mFixedExpProgress = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_FIXEDEXPPROGRESS));
					fe.mVariableExpProgess = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_VAREXPPROGRESS));
					fe.mMonth = cursor.getString(cursor.getColumnIndex(COLUMN_BUDGET_MONTH));
					fe.mIncomeProgress = cursor.getFloat(cursor.getColumnIndex(COLUMN_BUDGET_INCOMEPROGRESS));
					
					actualexpenses.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return actualexpenses;
	}	
	
	public ArrayList<ExpenseData> getVariableExpenses()
	{
		fixedexpensesList.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ VARIABLEEXPTABLE, null);
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
					fixedexpensesList.add(fe);
				}while(cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return fixedexpensesList;
	}	

	public ArrayList<BudgetData> getActualExpenses()
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
					BudgetData fe = new BudgetData();
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
