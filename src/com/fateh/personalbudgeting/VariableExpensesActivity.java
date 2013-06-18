package com.fateh.personalbudgeting;

import java.util.ArrayList;
import java.util.Calendar;

import com.fateh.personalbudgeting.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Layout;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VariableExpensesActivity extends ActivityExt {
	SharedPreferences mBudgetSettings;
	static final int DATE_DIALOG_ID = 0;
	static final int REPEAT_OPTION_ID = 1;
	
	static Float variableExpenses = 0.0f;
	static Float variableExpenseFromTable= 0.0f;
	static Float actualIncome = 0.0f;
	static Float actualVariableExpMaxLimit = 0.0f;
	static Float actualSaving = 0.0f;
	
	//Progress Monthly
	static Float monthlyIncomeProgress = 0.0f;
	static Float monthlyFixedExpenseProgress = 0.0f;
	static Float monthlyVariableExpenseProgress = 0.0f;
	static Float monthlySavingProgress = 0.0f;
	static Float monthlyOverdrawnAmount = 0.0f;
	
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
	static String monthName;
	static String repeatOption = "ONCE";
    int day = 0;
	int month = 0 ;
    int year = 0;
	
	DatabaseHelper db;
	ExpenseData expData;
	MonthlyProgress monthlyProgress;
	
	public ArrayList<ExpenseData> outputFixedExpList = new ArrayList<ExpenseData>();
	public ArrayList<ActualBudgetData> actualLimits  = new ArrayList<ActualBudgetData>();
    public ArrayList<MonthlyProgress> monthlyProgressFromTable = new ArrayList<MonthlyProgress>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_variableexpenses);	
		
        // Retrieve the shared preferences
		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
        initCategoryRetriever();
        initDatePicker();
        initSaveData();
        initAddAnother();
        initClearData();
        initCurrentCalendarDate();
        InitRetrieveActualBudgetData(month);
        getCurrentMonthProgress(month);
        //InitRetrieveVariableExpenseData(month);
        InitSetControls();    
        InitSetControls();    
        InitDateForButtons();     
        //initSetRepeatOption();
	}
	
	private void initSetRepeatOption() {
		// TODO Auto-generated method stub
		((Button)findViewById(R.id.Button_Repeat)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(REPEAT_OPTION_ID);
				((TextView)findViewById(R.id.TextView_RepeatOption)).setText(repeatOption);

			}
		});
	}

private void initCurrentCalendarDate() {
		// TODO Auto-generated method stub
    final Calendar c = Calendar.getInstance();
    month = c.get(Calendar.MONTH);
    year = c.get(Calendar.YEAR);
	}

@Override
protected void onDestroy() {
	super.onDestroy();
	variableExpenses = 0.0f;
	variableExpenseFromTable = 0.0f;
	actualIncome = 0.0f;
	actualVariableExpMaxLimit = 0.0f;
}

	private void InitSetControls() {
		// TODO Auto-generated method stub
		//Current Date
		Calendar cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		Time shoppingDate = new Time();
		shoppingDate.set(day,month, year);
		long dtShopping = shoppingDate.toMillis(true);
		((TextView)findViewById(R.id.TextView_ShoppingDate)).setText(DateFormat.format("MMMM dd, yyyy", dtShopping));
		Editor editor = mBudgetSettings.edit();
		editor.putLong(BUDGET_PREFERENCES_SHOPPINGDATE, dtShopping);
		editor.commit();		
	}
	
	private void InitRetrieveActualBudgetData(int month) {
	// TODO Auto-generated method stub
    db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 1);
    db.getWritableDatabase();
	actualIncome = 0.0f;
	actualVariableExpMaxLimit = 0.0f;
    String currentMonth = MONTHS[month];
    actualLimits = db.getCurrentBudgetData(currentMonth);
    if (actualLimits.size() != 0)
    {
	    for(int i =0; i<actualLimits.size();i++)
	    {
	    	actualIncome = actualLimits.get(i).GetIncome();
	    	actualSaving = actualLimits.get(i).GetSaving();
	    	actualVariableExpMaxLimit = actualLimits.get(i).GetVariableActualExpense();
	    }
    }
    else
    {
    	actualIncome = 0.0f;
    	actualSaving = 0.0f;
    	actualVariableExpMaxLimit = 0.0f;
    	variableExpenseFromTable=0.0f;
    }
}

	
	private void getCurrentMonthProgress(int month)
	{
	    db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 1);
	    db.getWritableDatabase();
	    String currentMonth = MONTHS[month];
	    monthlyProgressFromTable = db.GetCurrentProgress(currentMonth);

	    if (monthlyProgressFromTable.size() != 0)
	    {
		    for(int i =0; i<monthlyProgressFromTable.size();i++)
		    {
		    	monthlyIncomeProgress = monthlyProgressFromTable.get(i).mIncomeProgress;
		    	monthlyFixedExpenseProgress = monthlyProgressFromTable.get(i).mFixedExpensesProgress;
		    	monthlyVariableExpenseProgress = monthlyProgressFromTable.get(i).mVariableExpensesProgress;
		    	monthlySavingProgress = monthlyProgressFromTable.get(i).mSavingProgress;
		    	monthlyOverdrawnAmount = monthlyProgressFromTable.get(i).mOverdrawnAmount;
		    }
	    }
	    else
	    {
	    	monthlyIncomeProgress = 0.0f;
	    	monthlySavingProgress = 0.0f;
	    	monthlyVariableExpenseProgress = 0.0f;
	    }
	    
		//Display Actual Monthly Income and Monthly Fixed Expense
		((TextView)findViewById(R.id.textViewActualIncome)).setText("$"+actualIncome);
		((TextView)findViewById(R.id.textViewActualSavings)).setText("$"+actualSaving);
		((TextView)findViewById(R.id.textViewActualVariableExpense)).setText("$"+actualVariableExpMaxLimit);
		
	    ((ProgressBar)findViewById(R.id.progressBarIncome)).setMax(actualIncome.intValue());
	    if(actualIncome.intValue()-monthlyIncomeProgress.intValue() < 0)
	    {
	    	((ProgressBar)findViewById(R.id.progressBarIncome)).setProgress(5);
			((TextView)findViewById(R.id.textViewIncomeProgress)).setText("OVER $"+(monthlyIncomeProgress-actualIncome));
	    }
	    else
	    {
	    	((ProgressBar)findViewById(R.id.progressBarIncome)).setProgress(actualIncome.intValue()-monthlyIncomeProgress.intValue());
			((TextView)findViewById(R.id.textViewIncomeProgress)).setText("BALANCE $"+(actualIncome-monthlyIncomeProgress));
	    }

		((ProgressBar)findViewById(R.id.progressBarSavings)).setMax(actualSaving.intValue());
	    if(actualSaving.intValue() - monthlyOverdrawnAmount.intValue() < 0)
	    {
	    	((ProgressBar)findViewById(R.id.progressBarSavings)).setProgress(5);
			((TextView)findViewById(R.id.textViewSavingProgress)).setText("OVER $"+(monthlyOverdrawnAmount - actualSaving));
	    }
	    else
	    {
	    	((ProgressBar)findViewById(R.id.progressBarSavings)).setProgress( (actualSaving.intValue() - monthlyOverdrawnAmount.intValue()) );
			((TextView)findViewById(R.id.textViewSavingProgress)).setText("BALANCE $"+(actualSaving - monthlyOverdrawnAmount));
	    }

		((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setMax(actualVariableExpMaxLimit.intValue());
		if(actualVariableExpMaxLimit.intValue()-monthlyVariableExpenseProgress.intValue() < 0)
		{
			((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setProgress(5);
			((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("OVER $"+(monthlyVariableExpenseProgress -actualVariableExpMaxLimit));
		}
		else
		{
			((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setProgress(actualVariableExpMaxLimit.intValue()-monthlyVariableExpenseProgress.intValue());
			((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("BALANCE $"+(actualVariableExpMaxLimit-monthlyVariableExpenseProgress));
		}
		
		//Display Balance for the Month
	    
	}
	
	private void InitRetrieveVariableExpenseData(int month) {
		// TODO Auto-generated method stub
    db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 1);
    db.getWritableDatabase();
    String currentMonth = MONTHS[month];
    variableExpenseFromTable = 0.0f;
    outputFixedExpList.clear();
    outputFixedExpList = db.getVariableExpenses(currentMonth);
    
    if(outputFixedExpList.size() != 0)
    {
	    for(int i = 0; i<outputFixedExpList.size();i++)
	    {
	    	variableExpenseFromTable += outputFixedExpList.get(i).GetAmount();
	    }
    }
    else
    {
    	variableExpenseFromTable = 0.0f;
    }
	((ProgressBar)findViewById(R.id.progressBarIncome)).setMax(actualIncome.intValue());
	((ProgressBar)findViewById(R.id.progressBarIncome)).setProgress(actualIncome.intValue()-variableExpenseFromTable.intValue());

	((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setMax(actualVariableExpMaxLimit.intValue());
	((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setProgress( (actualVariableExpMaxLimit.intValue()-variableExpenseFromTable.intValue()) );
	
	//Display Actual Monthly Income and Monthly Fixed Expense
	((TextView)findViewById(R.id.textViewActualIncome)).setText("$"+actualIncome);
	((TextView)findViewById(R.id.textViewActualVariableExpense)).setText("$"+actualVariableExpMaxLimit);
	
	//Display Balance for the Month
	((TextView)findViewById(R.id.textViewIncomeProgress)).setText("BALANCE $"+(actualIncome-variableExpenseFromTable));
	((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("BALANCE $"+(actualVariableExpMaxLimit-variableExpenseFromTable));

	}

	private void initCategoryRetriever() {
		// TODO Auto-generated method stub
    // Populate Spinner control with genders
    final Spinner spinner = (Spinner) findViewById(R.id.Spinner_CategoryList);
    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.variablecategories,
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
		
	    TextView shoppingDateTextView = (TextView) findViewById(R.id.TextView_ShoppingDate);

	    if (mBudgetSettings.contains(BUDGET_PREFERENCES_SHOPPINGDATE)) {
	    	shoppingDateTextView.setText(DateFormat.format("MMMM dd, yyyy", mBudgetSettings.getLong(BUDGET_PREFERENCES_SHOPPINGDATE, 0)));
	    } 
	    else {
	    	shoppingDateTextView.setText(R.string.settings_button_shopping_date);
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
    	Dialog  dialog = null;
    	LayoutInflater inflator;
    	AlertDialog.Builder builder = null;
    	AlertDialog passwordDialog = null;
    	View layout = null;
		switch (id) {
		case DATE_DIALOG_ID:
			final TextView shoppingDateTextView = (TextView) findViewById(R.id.TextView_ShoppingDate);
			dialog = 
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
			break;
		case REPEAT_OPTION_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.categories_list, (ViewGroup)findViewById(R.id.radio_groupDialog));	
			final RadioGroup radgrp = (RadioGroup)layout.findViewById(R.id.radio_groupDialog); 
			
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.settings_Shoping);
			VariableExpensesActivity.this.removeDialog(REPEAT_OPTION_ID);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int radioButtonID = radgrp.getCheckedRadioButtonId();
					RadioButton radioButton = (RadioButton) radgrp.findViewById(radioButtonID);					
					repeatOption = radioButton.getText().toString();
					if(radioButton.getText().toString().isEmpty())
						((TextView) findViewById(R.id.TextView_RepeatOption)).setText("ONCE");
					else
						((TextView) findViewById(R.id.TextView_RepeatOption)).setText(radioButton.getText().toString());					
					
					((TextView) findViewById(R.id.TextView_RepeatOption)).setText(radioButton.getText().toString());
					VariableExpensesActivity.this.removeDialog(REPEAT_OPTION_ID);
				}
				});
			dialog = builder.create();	
    	}
		return dialog;
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
    			long lshoppingDate = mBudgetSettings.getLong(BUDGET_PREFERENCES_SHOPPINGDATE, 0);
    			Time  shoppingDateTime= new Time();
    			shoppingDateTime.set(lshoppingDate);
    			iDay = shoppingDateTime.monthDay;
    			iMonth = shoppingDateTime.month;
    			iYear = shoppingDateTime.year;
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
    
	/*************
	 * Date Previous and Next Buttons
	 */
	private void InitDateForButtons() {
		// TODO Auto-generated method stub
	    monthName = MONTHS[month];
	    ((TextView)findViewById(R.id.textViewCurrentDate)).setText(monthName+" "+year);
	    ImageButton prev = (ImageButton)findViewById(R.id.imageButtonpPrevMonth);
	    ImageButton next = (ImageButton)findViewById(R.id.ImageButtonNextMonth);
	    
	    prev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				month = month-1;
			    monthName = MONTHS[month];
			    ((TextView)findViewById(R.id.textViewCurrentDate)).setText(monthName+" "+year);
			    InitRetrieveActualBudgetData(month);
			    InitRetrieveVariableExpenseData(month);
			}
		});
	    next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				month = month+1;
			    monthName = MONTHS[month];
			    ((TextView)findViewById(R.id.textViewCurrentDate)).setText(monthName+" "+year);
			    InitRetrieveActualBudgetData(month);
			    InitRetrieveVariableExpenseData(month);
			}
		});
	}
    
    @SuppressLint("NewApi")
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
				
		    	final EditText storeName = (EditText)findViewById(R.id.EditText_StoreName);
		    	final EditText amountEditText = (EditText)findViewById(R.id.EditText_Amount);
		    	final TextView dateTextView = (TextView)findViewById(R.id.TextView_ShoppingDate);
		    	
		    	if(storeName.getText().toString().isEmpty() ||  amountEditText.getText().toString().isEmpty() || dateTextView.getText().toString().isEmpty())
		    	{
	    		   	  new AlertDialog.Builder(VariableExpensesActivity.this)
	    		      .setTitle("Missing Value")
	    		      .setMessage("Please enter missing Values !!!")
	    		      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
						})
					.show();
		    	}
		    	else
		    	{
		    	variableExpenses += Float.valueOf(amountEditText.getText().toString());
		    	monthlyIncomeProgress += variableExpenses;
		    	monthlyVariableExpenseProgress += variableExpenses;	
		    	if((actualVariableExpMaxLimit - monthlyVariableExpenseProgress) < 0)
		    	{
		    		monthlyOverdrawnAmount += (monthlyVariableExpenseProgress- actualVariableExpMaxLimit);
		    	}		    	
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
		    	UpdateCurrentDisplay(monthlyIncomeProgress,monthlyVariableExpenseProgress);
		    	//No need to pass Year and Month, No Repeat Options.
		    	SaveRecord(category, amount, date, monthName);
		    	UpdateMonthlyProgress(monthName);
				
				
				Toast.makeText(VariableExpensesActivity.this, "Data Saved", 2).show();
				
				data.putExtra(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES, variableExpenses);
				data.putExtra(BUDGET_PREFERENCES_SHOPPINGDATE_MONTH, monthName);
		    	}
				finish();

			}
		});
    }
    
    
    /****************
     * Update Monthly Progress Information
     * @param fixedExpensesprogress
     * @param monthName
     */
    protected void UpdateMonthlyProgress(String monthName) {
		// TODO Auto-generated method stub
		//Get Current Information for MonthlyIncomeProgress, SavingProgress, FixedExpenseProgress
		db = new DatabaseHelper(getApplicationContext(), "name", null, 1);
		db.getWritableDatabase();
		
		monthlyProgress = new MonthlyProgress();
		if(monthlyOverdrawnAmount < 0)
			monthlyProgress.mStatus = "OVERDRAWN";
		else
			monthlyProgress.mStatus = "INBUDGET";
		
		monthlyProgress.mIncomeProgress = monthlyIncomeProgress;
		monthlyProgress.mFixedExpensesProgress = monthlyFixedExpenseProgress;
		monthlyProgress.mSavingProgress = monthlySavingProgress;
		monthlyProgress.mVariableExpensesProgress = monthlyVariableExpenseProgress;
		monthlyProgress.mMonth = monthName;
		monthlyProgress.mOverdrawnAmount = monthlyOverdrawnAmount;
		monthlyProgress.mRepeatOption = repeatOption;
		
		db.UpdateMonthlyProgress(monthlyProgress);
    	
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
		    	Toast.makeText(VariableExpensesActivity.this, "Data not Saved", Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    @SuppressLint("NewApi")
	private void initAddAnother(){
        
    	Button yesAnother = (Button)findViewById(R.id.Button_AddAnother);
    	yesAnother.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	EditText storeName = (EditText)findViewById(R.id.EditText_StoreName);
		    	EditText amountEditText = (EditText)findViewById(R.id.EditText_Amount);
		    	TextView dateTextView = (TextView)findViewById(R.id.TextView_ShoppingDate);
		    	
			    if(amountEditText.getText().toString().isEmpty() || storeName.getText().toString().isEmpty() ) {
			        //Ask the user if they want to quit
			        new AlertDialog.Builder(VariableExpensesActivity.this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Missing Values")
			        .setMessage("Please Enter the Missing Values !")
			        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                //Stop the activity
			                //finish();    
			            }
			        })
			        .show();
			    }
			    else
			    {
		    	variableExpenses += Float.valueOf(amountEditText.getText().toString());
		    	monthlyIncomeProgress += variableExpenses;
		    	monthlyVariableExpenseProgress += variableExpenses;
		    	if((actualVariableExpMaxLimit - monthlyVariableExpenseProgress) < 0)
		    	{
		    		monthlyOverdrawnAmount += (monthlyVariableExpenseProgress- actualVariableExpMaxLimit);
		    	}		    	
		    	String category= storeName.getText().toString();
		    	String amount = amountEditText.getText().toString();
		    	String date = dateTextView.getText().toString();
		    	
		    	UpdateCurrentDisplay(monthlyIncomeProgress,monthlyVariableExpenseProgress);
				//Saving lastRecord
				SaveRecord(category, amount, date, monthName);
		    	UpdateMonthlyProgress(monthName);
		    	
		    	Toast.makeText(VariableExpensesActivity.this, "Data Saved. Please Add Another Expense", Toast.LENGTH_SHORT).show();
		    	
		    	((EditText)findViewById(R.id.EditText_StoreName)).setText("");
		    	((EditText)findViewById(R.id.EditText_Amount)).setText("");
			    }

			}
		});
    }
    
    protected void UpdateCurrentDisplay(Float income, Float variableExpenseAmount) {
		// TODO Auto-generated method stub

    	//Display Actual Monthly Income and Monthly Fixed Expense
    	((TextView)findViewById(R.id.textViewActualIncome)).setText("$"+actualIncome);
    	((TextView)findViewById(R.id.textViewActualSavings)).setText("$"+actualSaving);
    	((TextView)findViewById(R.id.textViewActualVariableExpense)).setText("$"+actualVariableExpMaxLimit);

    	
    	((ProgressBar)findViewById(R.id.progressBarIncome)).setMax(actualIncome.intValue());
    	if(actualIncome.intValue()-income.intValue() < 0)
    	{
    		((ProgressBar)findViewById(R.id.progressBarIncome)).setProgress(5);
        	((TextView)findViewById(R.id.textViewIncomeProgress)).setText("OVER $"+(income-actualIncome));
    	}
    	else
    	{
    		((ProgressBar)findViewById(R.id.progressBarIncome)).setProgress(actualIncome.intValue()-income.intValue());
        	((TextView)findViewById(R.id.textViewIncomeProgress)).setText("BALANCE $"+(actualIncome-income));
    	}

    	((ProgressBar)findViewById(R.id.progressBarSavings)).setMax(actualSaving.intValue());
    	if(actualSaving.intValue()-monthlyOverdrawnAmount.intValue() < 0)
    	{
    		((ProgressBar)findViewById(R.id.progressBarSavings)).setProgress(5);
        	((TextView)findViewById(R.id.textViewSavingProgress)).setText("OVER $"+(monthlyOverdrawnAmount-actualSaving));
    	}
    	else
    	{
    		((ProgressBar)findViewById(R.id.progressBarSavings)).setProgress(actualSaving.intValue()-monthlyOverdrawnAmount.intValue());
        	((TextView)findViewById(R.id.textViewSavingProgress)).setText("BALANCE $"+(actualSaving-monthlyOverdrawnAmount));    		
    	}
    	
    	((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setMax(actualVariableExpMaxLimit.intValue());
    	if(actualVariableExpMaxLimit.intValue() - variableExpenseAmount.intValue() < 0)
    	{
        	((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setProgress(5);
        	((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("OVER $"+(variableExpenseAmount-variableExpenseAmount));        	
    	}
    	else
    	{
    		((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).setProgress( actualVariableExpMaxLimit.intValue() - variableExpenseAmount.intValue());
        	((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("BALANCE $"+(actualVariableExpMaxLimit-variableExpenseAmount));
    	}
 	}
    
	private void SaveRecord(String store, String amount, String date, String monthStr) {
		// TODO Auto-generated method stub

		db = new DatabaseHelper(getApplicationContext(), "name", null, 35);
		db.getWritableDatabase();
		expData = new ExpenseData();
		
		expData.mAmount = Float.parseFloat(amount);
		expData.mCategory = store;
		expData.mDate = date;
		expData.mMonth = monthStr;
		db.AddVariableExpenseRecord(expData);	
	}
}

