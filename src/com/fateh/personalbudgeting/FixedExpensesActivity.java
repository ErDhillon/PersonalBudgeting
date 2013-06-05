package com.fateh.personalbudgeting;
import java.util.ArrayList;
import java.util.Calendar;

import com.fateh.personalbudgeting.R;


import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**************
 * Used to add, save and report Fixed Expenses
 * @author administrator
 *
 */
public class FixedExpensesActivity extends ActivityExt {
	SharedPreferences mBudgetSettings;
	static final int DATE_DIALOG_ID = 0;
	static Float fixedExpenses = 0.0f;
	static Float fixedExpenseProgress = 0.0f;
	static Float actualIncome = 0.0f;
	static Float actualFixedExpMaxLimit = 0.0f;
	
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
	static String monthName;
	
	DatabaseHelper db;
	ExpenseData expData;
	public ArrayList<ExpenseData> outputFixedExpList = new ArrayList<ExpenseData>();
	public ArrayList<ActualBudgetData> actualLimits  = new ArrayList<ActualBudgetData>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fixedexpenses);	
		
        // Retrieve the shared preferences
		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
		initCategoryRetriever();
        initDatePicker();
        initSaveData();
        initAddAnother();
        initClearData();
        InitRetrieveActualBudgetData();
        InitRetrieveFixedExpenseData();
        InitSetControls();
	}
	
private void InitSetControls() {
		// TODO Auto-generated method stub
		((ProgressBar)findViewById(R.id.progressBarIncome)).setMax(actualIncome.intValue());
		((ProgressBar)findViewById(R.id.progressBarIncome)).setProgress(actualIncome.intValue()-fixedExpenseProgress.intValue());

		((ProgressBar)findViewById(R.id.progressBarFixedExpenses)).setMax(actualFixedExpMaxLimit.intValue());
		((ProgressBar)findViewById(R.id.progressBarFixedExpenses)).setProgress( (actualFixedExpMaxLimit.intValue()-fixedExpenseProgress.intValue()) );
		
		
	}

private void InitRetrieveActualBudgetData() {
	// TODO Auto-generated method stub
    db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 1);
    db.getWritableDatabase();
    final Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH);
    String currentMonth = MONTHS[month];
    actualLimits = db.getCurrentBudgetData(currentMonth);
    
    for(int i =0; i<actualLimits.size();i++)
    {
    	actualIncome += actualLimits.get(i).GetIncome();
    	actualFixedExpMaxLimit += actualLimits.get(i).GetFixedActualExpense();
    }
}

private void InitRetrieveFixedExpenseData() {
		// TODO Auto-generated method stub
    db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 1);
    db.getWritableDatabase();
    final Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH);
    String currentMonth = MONTHS[month];
    
    outputFixedExpList.clear();
    outputFixedExpList = db.getFixedExpenses(currentMonth);
    for(int i =0; i<outputFixedExpList.size();i++)
    	{
    		fixedExpenseProgress += outputFixedExpList.get(i).GetAmount();
    	}
	}

	private void initCategoryRetriever() {
		// TODO Auto-generated method stub
    // Populate Spinner control with genders
    final Spinner spinner = (Spinner) findViewById(R.id.Spinner_CategoryList);
    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.fixedcategories,
            android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    if (mBudgetSettings.contains(BUDGET_PREFERENCES_CATEGORIES)) {
        spinner.setSelection(mBudgetSettings.getInt(BUDGET_PREFERENCES_CATEGORIES, 0));
    }
    // Handle spinner selections
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
                long selectedId) {
            Editor editor = mBudgetSettings.edit();
            editor.putInt(BUDGET_PREFERENCES_CATEGORIES, selectedItemPosition);
            editor.commit();
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    });
	}

	private void initDatePicker() {
		
	    TextView shoppingDate = (TextView) findViewById(R.id.TextView_ShoppingDate);

	    if (mBudgetSettings.contains(BUDGET_PREFERENCES_SHOPPINGDATE)) {
	    	shoppingDate.setText(DateFormat.format("MMMM dd, yyyy", mBudgetSettings.getLong(BUDGET_PREFERENCES_SHOPPINGDATE, 0)));
	    } 
	    else {
	    	shoppingDate.setText(R.string.settings_button_shopping_date);
	    }
	    
		ImageButton pickDate = (ImageButton) findViewById(R.id.imageButton_ShoppingDate);
		pickDate.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
			
	}	
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	DatePickerDialog  dateDialog = null;
    	switch (id) {
		case DATE_DIALOG_ID:
			final TextView shoppingDateTextView = (TextView) findViewById(R.id.TextView_ShoppingDate);
			dateDialog = 
				new DatePickerDialog(this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								Time shoppingDate = new Time();
								shoppingDate.set(dayOfMonth, monthOfYear, year);
								monthName = MONTHS[monthOfYear];
								long dtShopping = shoppingDate.toMillis(true);
								shoppingDateTextView.setText(DateFormat.format("MMMM dd, yyyy", dtShopping));
								Editor editor = mBudgetSettings.edit();
								editor.putLong(BUDGET_PREFERENCES_SHOPPINGDATE, dtShopping);
								editor.commit();
							}
						},0,0,0);
    	}
		return dateDialog;
	}
	
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	super.onPrepareDialog(id, dialog);
    	switch(id){
    	case DATE_DIALOG_ID:
    		DatePickerDialog dateDialog = (DatePickerDialog)(dialog);
    		int iDay, iMonth, iYear;
    		
    		if(mBudgetSettings.contains(BUDGET_PREFERENCES_SHOPPINGDATE))
    		{
    			long shoppingDate = mBudgetSettings.getLong(BUDGET_PREFERENCES_SHOPPINGDATE, 0);
    			Time shopDateTime = new Time();
    			shopDateTime.set(shoppingDate);
    			iDay = shopDateTime.monthDay;
    			iMonth = shopDateTime.month;
    			iYear = shopDateTime.year;
    		}
    		else
    		{
    		Calendar cal = Calendar.getInstance();
    		iDay = cal.get(Calendar.DAY_OF_MONTH);
    		iMonth = cal.get(Calendar.MONTH);
    		iYear = cal.get(Calendar.YEAR);
    		}
    		monthName = MONTHS[iMonth];
    		dateDialog.updateDate(iYear, iMonth, iDay);
    		return;
    	}
    };
    
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void initSaveData()
    {
    	Button saveBtn = (Button) findViewById(R.id.Button_Save);
    	saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Get the Intenet
				Intent data = getIntent();
				String category = null, amount = null, date = null;
				//SaveExpenses(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES, fixedExpenses);
		    	final EditText storeName = (EditText)findViewById(R.id.EditText_StoreName);
		    	final EditText amountEditText = (EditText)findViewById(R.id.EditText_Amount);
		    	final TextView dateTextView = (TextView)findViewById(R.id.TextView_ShoppingDate);

		    	if(storeName.getText().toString().isEmpty() ||  amountEditText.getText().toString().isEmpty() || dateTextView.getText().toString().isEmpty())
		    	{
		    		   	  new AlertDialog.Builder(FixedExpensesActivity.this)
		    		      .setMessage("Missing Value")
		    		      .setTitle("Do you want to Enter missing Values")
		    		      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
						    	while(storeName.getText().toString().isEmpty()|| amountEditText.getText().toString().isEmpty() || dateTextView.getText().toString().isEmpty())
							    	{
						    		   new AlertDialog.Builder(FixedExpensesActivity.this)
					    		      .setMessage("Missing Value")
					    		      .setTitle("Please enter Missing Values")
					    		      .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
										}
					    		      });
							    	}
							}
							})
			    		    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									finish();
								}
							})
						.show();
		  				Toast.makeText(FixedExpensesActivity.this, "Data Not Saved", 2).show();  
		    	}
		    	else
		    	{
		    	fixedExpenses += Float.valueOf(amountEditText.getText().toString());
		    	category= storeName.getText().toString();
		    	amount = amountEditText.getText().toString();
		    	date = dateTextView.getText().toString();
		    	if(monthName == null)
		    	{
		    		Calendar cal = Calendar.getInstance();
		    		int iDay = cal.get(Calendar.DAY_OF_MONTH);
		    		int iMonth = cal.get(Calendar.MONTH);
		    		int iYear = cal.get(Calendar.YEAR);
		    		monthName = MONTHS[iMonth];
		    	}
				//Saving lastRecord
				SaveRecord(category, amount, date, monthName);
		    	
				Toast.makeText(FixedExpensesActivity.this, "Data Saved", 2).show();
				data.putExtra(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES, fixedExpenses);
				data.putExtra(BUDGET_PREFERENCES_SHOPPINGDATE_MONTH, monthName);
				setResult(RESULT_OK, data);
		    	}
				finish();
			}
		});
    	
    }
    
    private void SavePreferences(String key, String value)
    {
    	Editor editor = mBudgetSettings.edit();
    	editor.putString(key, value);
    	editor.commit();
    }
    private void SaveExpenses(String key, float value)
    {
    	Editor editor = mBudgetSettings.edit();
    	editor.putFloat(key, value);
    	editor.commit();
    }
    
    private void initClearData()
    {
    	Button clear = (Button)findViewById(R.id.Button_Cancel);
    	clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		    	((EditText)findViewById(R.id.EditText_StoreName)).setText("");
		    	((EditText)findViewById(R.id.EditText_Amount)).setText("");
		    	Toast.makeText(FixedExpensesActivity.this, "Data not Saved", Toast.LENGTH_SHORT).show();
			}
		});
    }
    private void initAddAnother(){
    
    	Button yesAnother = (Button)findViewById(R.id.Button_AddAnother);
    	yesAnother.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Spinner s = (Spinner) findViewById(R.id.Spinner_CategoryList);
				
		    	EditText storeName = (EditText)findViewById(R.id.EditText_StoreName);
		    	EditText amountEditText = (EditText)findViewById(R.id.EditText_Amount);
		    	TextView dateTextView = (TextView)findViewById(R.id.TextView_ShoppingDate);
		    	fixedExpenses += Float.valueOf(amountEditText.getText().toString());
		    	String category= storeName.getText().toString();
		    	String amount = amountEditText.getText().toString();
		    	String date = dateTextView.getText().toString();
		    	
		    	SaveRecord(category, amount, date, monthName);
		    	Toast.makeText(FixedExpensesActivity.this, "Data Saved.Please Add Another Expense", Toast.LENGTH_SHORT).show();
		    	
		    	((EditText)findViewById(R.id.EditText_StoreName)).setText("");
		    	((EditText)findViewById(R.id.EditText_Amount)).setText("");
			}
		});
    }

    private void SaveRecord(String store, String amount, String date, String monthStr) {
		// TODO Auto-generated method stub

		db = new DatabaseHelper(getApplicationContext(), "name", null, 1);
		db.getWritableDatabase();
		expData = new ExpenseData();
		
		expData.mAmount = Float.parseFloat(amount);
		expData.mCategory = store;
		expData.mDate = date;
		expData.mMonth = monthStr;
		db.AddFixedExpenseRecord(expData);	
	}
		
}

