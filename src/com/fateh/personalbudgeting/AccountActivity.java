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
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/********************
 * Settings Activity for setting various monthly Limits
 * @author administrator
 *
 */
@SuppressLint("NewApi")
public class AccountActivity extends ActivityExt {
	SharedPreferences mBudgetSettings;
	static final int ACCOUNT_DIALOG_ID = 1;
	static final int VARIABLE_EXPENSES_DIALOG_ID = 2;
	static final int FIXED_EXPENSES_DIALOG_ID = 3;
	static final int REPEAT_OPTION_ID = 4;
	static final int SAVING_DIALOG_ID = 5;
	
	static Float totalMonthlyIncome = 0.0f;
	static Float fixedExpenditureLimit = 0.0f;
	static Float variableExpenditureLimit = 0.0f;
	static Float monthlySavingAmount = 0.0f;
	static Float monthlyunallocatedAmount = 0.0f;
	
	static String monthName = "";
	static String repeatOption = "none";	
	
    int month = 0 ;
    int year = 0;

	DatabaseHelper db;
	ActualBudgetData 	bdata = new ActualBudgetData();
	MonthlyProgress monthlyProgress = new MonthlyProgress();
	
	public ArrayList<ActualBudgetData> actualLimits  = new ArrayList<ActualBudgetData>();

	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
		InitSetCurrentDate();
		InitRetrieveFromDatabase(month);
		InitSetIncome();
		InitSetSavingAmount();
		initSetFixedExpenseLimit();
		initSetVariableExpenseLimit();
		InitSaveDataToDatbase();
        initSetRepeatOption();
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

	private void InitSaveDataToDatbase() {
		// TODO Auto-generated method stub
		((ImageButton)findViewById(R.id.imageButtonSaveDB)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(totalMonthlyIncome != 0.0f && fixedExpenditureLimit != 0.0f && variableExpenditureLimit != 0.0f)
				{
					if((fixedExpenditureLimit + variableExpenditureLimit + monthlySavingAmount) > totalMonthlyIncome )
					{
		    		   	  new AlertDialog.Builder(AccountActivity.this)
		    		      .setTitle("Incorrect Data")
		    		      .setMessage("Total Monthly Income entered is less than the Expenses and Savings")
		    		      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
							})
						.show();
					}
					else if( (fixedExpenditureLimit + variableExpenditureLimit + monthlySavingAmount) <= totalMonthlyIncome )
					{
		    		   	monthlyunallocatedAmount = totalMonthlyIncome - (fixedExpenditureLimit + variableExpenditureLimit + monthlySavingAmount);
						if(monthlyunallocatedAmount > 0)
						{
			    		   	new AlertDialog.Builder(AccountActivity.this)
			    		      .setTitle("Unallocated Amount")
			    		      .setMessage("$ "+monthlyunallocatedAmount+" are not allocated. Do you want to add those to Savings?")
			    		      .setPositiveButton("YES", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									monthlySavingAmount += monthlyunallocatedAmount;
									((TextView)findViewById(R.id.textViewmonthlySavings)).setText("$"+monthlySavingAmount);
									monthlyunallocatedAmount = 0.0f;
								}
								})
							.setNegativeButton("NO", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}).show();
						}
						SaveToDatabase();
						Toast.makeText(AccountActivity.this, "Information Saved into Database.", Toast.LENGTH_SHORT).show();						
					}
				}
				else
				{
	    		   	  new AlertDialog.Builder(AccountActivity.this)
	    		      .setTitle("Missing Value")
	    		      .setMessage("Please enter missing Values.")
	    		      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
						})
					.show();

				}
			}
		});
	}

	/*********
	 * Retrieve Actual Values for monthlyIncome fixedExpenditureLimit,and VariableExpenditureLimit
	 */
	private void InitRetrieveFromDatabase(int month) {
		// TODO Auto-generated method stub
        db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 2);
        db.getWritableDatabase();

	    String currentMonth = MONTHS[month];
	    actualLimits = db.getCurrentBudgetData(currentMonth);
	    if(actualLimits.size() > 0)
	    {
	        	totalMonthlyIncome = actualLimits.get(0).GetIncome();
	        	monthlySavingAmount = actualLimits.get(0).GetSaving();
	        	fixedExpenditureLimit = actualLimits.get(0).GetFixedActualExpense();
	        	variableExpenditureLimit = actualLimits.get(0).GetVariableActualExpense();
	        	monthlyunallocatedAmount = actualLimits.get(0).GetUnallocatedAmount();
	        	monthName = actualLimits.get(0).GetMonth();
	    }
	    else
	    {
	    	totalMonthlyIncome = 0.0f;
	    	monthlySavingAmount = 0.0f;
	    	fixedExpenditureLimit = 0.0f;
	    	variableExpenditureLimit=0.0f;
	    	monthlyunallocatedAmount = 0.0f;
	    }
	    ((TextView)findViewById(R.id.textViewmonthlyIncome)).setText("$"+totalMonthlyIncome);
	    ((TextView)findViewById(R.id.textViewmonthlySavings)).setText("$"+monthlySavingAmount);
	    ((TextView)findViewById(R.id.textViewFixedLimit)).setText("$"+fixedExpenditureLimit);
		((TextView)findViewById(R.id.textViewVariableLimit)).setText("$"+variableExpenditureLimit);
		
		if(monthlyunallocatedAmount > 0.0f)
		{
			new AlertDialog.Builder(AccountActivity.this)
		      .setTitle("Amount Available")
		      .setMessage("$ "+monthlyunallocatedAmount+" are not allocated. Do you want to add those to Savings?")
		      .setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					monthlySavingAmount += monthlyunallocatedAmount;
					((TextView)findViewById(R.id.textViewmonthlySavings)).setText("$"+monthlySavingAmount);
					monthlyunallocatedAmount = 0.0f;
				}
				})
			.setNegativeButton("NO", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
	}

//	@Override
//	protected void onDestroy(){
//		super.onDestroy();
//
//		// Check if back button is pressed
//		// Persist to DB only then.
//		if(isFinishing())
//		{
//			Toast.makeText(AccountActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
//			SaveToDatabase();
//		}
//		else
//		{
//			Toast.makeText(AccountActivity.this, "Data not saved", Toast.LENGTH_SHORT).show();
//		}
//	}
	
	/************
	 * Save the values into DB 
	 * Here we will save information into two tables
	 * Table1 ACTUALLIMITS and Table 2 MONTHLYPROGRESS
	 */
	private void SaveToDatabase() {
		// TODO Auto-generated method stub
		if(monthName != null)
			bdata.mMonth = monthName;
		else
		{
		    final Calendar c = Calendar.getInstance();
		    int month = c.get(Calendar.MONTH);
		    bdata.mMonth = MONTHS[month];
		}
		bdata.mIncome = totalMonthlyIncome;
		bdata.mSaving = monthlySavingAmount;
		bdata.mActualFixedExpenses = fixedExpenditureLimit;
		bdata.mActualVariableExpenses = variableExpenditureLimit;
		bdata.mUnallocatedAmount = monthlyunallocatedAmount;
		bdata.mRepeatOption = repeatOption;
		db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 2);
		db.getWritableDatabase();
		if(bdata != null)
		db.InsertBudgetLimits(bdata);
		
		//Insert into MonthlyProgress for Initial Values
//		monthlyProgress.mMonth = bdata.mMonth;
//		monthlyProgress.mIncomeProgress = totalMonthlyIncome;
//		monthlyProgress.mSavingProgress = monthlySavingAmount;
//		monthlyProgress.mFixedExpensesProgress = fixedExpenditureLimit;
//		monthlyProgress.mVariableExpensesProgress = variableExpenditureLimit;
//		monthlyProgress.mStatus = "INITIAL";
//		monthlyProgress.mOverdrawnAmount = 0.0f;
//		monthlyProgress.mRepeatOption = repeatOption;
//		db.getWritableDatabase();
//		if(monthlyProgress != null)
//		db.UpdateMonthlyProgress(monthlyProgress);
	}

	/*************
	 * Date Previous and Next Buttons
	 */
	private void InitSetCurrentDate() {
		// TODO Auto-generated method stub
	    final Calendar c = Calendar.getInstance();
	    month = c.get(Calendar.MONTH);
	    year = c.get(Calendar.YEAR);
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
			    InitRetrieveFromDatabase(month);
			    
			}
		});
	    next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				month = month+1;
			    monthName = MONTHS[month];
			    ((TextView)findViewById(R.id.textViewCurrentDate)).setText(monthName+" "+year);
			    InitRetrieveFromDatabase(month);
			}
		});
	}

	/***********
	 * Function to make sure that monthlyIncome, FixedExpenseLimit and 
	 * VariablesExpenseLimit are repeated for all months
	 */
//	private void InitRepeatMonthly() {
//		// TODO Auto-generated method stub
//	    final Spinner spinner = (Spinner) findViewById(R.id.Spinner_Repeat);
//	    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.repeatlist,
//	            android.R.layout.simple_spinner_item);
//	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	    spinner.setAdapter(adapter);
//	    if (mBudgetSettings.contains(BUDGET_PREFERENCES_REPEAT)) {
//	        spinner.setSelection(mBudgetSettings.getInt(BUDGET_PREFERENCES_REPEAT, 0));
//	    }
//	    // Handle spinner selections
//	    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//	        public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
//	                long selectedId) {
//	            Editor editor = mBudgetSettings.edit();
//	            editor.putInt(BUDGET_PREFERENCES_REPEAT, selectedItemPosition);
//	            editor.commit();
//	        }
//
//	        public void onNothingSelected(AdapterView<?> parent) {
//	        }
//	    });
//	}

	private void initSetVariableExpenseLimit() {
		// TODO Auto-generated method stub
		((TextView)findViewById(R.id.textViewVariableLimit)).setText("$"+variableExpenditureLimit);
		ImageButton income = (ImageButton)findViewById(R.id.imageButtonVariableExpenses);
		 income.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(VARIABLE_EXPENSES_DIALOG_ID);
				((TextView)findViewById(R.id.textViewVariableLimit)).setText("$"+variableExpenditureLimit);
			
			}
		});
	}

	private void initSetFixedExpenseLimit() {
		// TODO Auto-generated method stub
		((TextView)findViewById(R.id.textViewFixedLimit)).setText("$"+fixedExpenditureLimit);		
		 ImageButton income = (ImageButton)findViewById(R.id.imageButtonFixedExpenses);
		 income.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(FIXED_EXPENSES_DIALOG_ID);
				((TextView)findViewById(R.id.textViewFixedLimit)).setText("$"+fixedExpenditureLimit);				
			}
		});
	}

	private void InitSetIncome() {
		// TODO Auto-generated method stub

	((TextView)findViewById(R.id.textViewmonthlyIncome)).setText("$"+totalMonthlyIncome);
	ImageButton income = (ImageButton)findViewById(R.id.imageButtonIncome);
	 income.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ACCOUNT_DIALOG_ID);
			((TextView)findViewById(R.id.textViewmonthlyIncome)).setText("$"+totalMonthlyIncome);
		}
	});
	}
	
	private void InitSetSavingAmount() {
		// TODO Auto-generated method stub

	((TextView)findViewById(R.id.textViewmonthlySavings)).setText("$"+monthlySavingAmount);
	ImageButton saving = (ImageButton)findViewById(R.id.imageButtonSaving);
	saving.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(SAVING_DIALOG_ID);
			((TextView)findViewById(R.id.textViewmonthlySavings)).setText("$"+monthlySavingAmount);
		}
	});
	}	
	
	/************
	 * Dialogs for MonthlyIncome, FixedIncome, VariableIncome.
	 */
    @SuppressWarnings("deprecation")
	@Override
    protected Dialog onCreateDialog(int id) {
    	Dialog  dialog = null;
    	LayoutInflater inflator = null;
    	View layout = null;
    	AlertDialog.Builder builder = null;
    	AlertDialog settingsDialog = null;
		final EditText p1; final EditText p2;   	
    	switch (id) {
		case ACCOUNT_DIALOG_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_incomelimit, (ViewGroup)findViewById(R.id.incomelimitDialog));
			p1 = (EditText) layout.findViewById(R.id.editTextSetIncome);
			p2 = (EditText) layout.findViewById(R.id.editTextSetIncomeOther);
			String s = "";
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle("SET INCOME");
			AccountActivity.this.removeDialog(ACCOUNT_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Float monthlyincome = 0.0f;
					Float monthlyIncomefromOtherSource = 0.0f;
					if(!p1.getText().toString().isEmpty())
						 monthlyincome = Float.parseFloat(p1.getText().toString());
					if(!p2.getText().toString().isEmpty())
						monthlyIncomefromOtherSource = Float.parseFloat(p2.getText().toString());
					
					if(monthlyincome >0.0f || monthlyIncomefromOtherSource > 0.0f)
					{
						totalMonthlyIncome = monthlyincome + monthlyIncomefromOtherSource;
						((TextView) findViewById(R.id.textViewmonthlyIncome)).setText("$"+totalMonthlyIncome);
						// TODO Auto-generated method stub
						AccountActivity.this.removeDialog(ACCOUNT_DIALOG_ID);
					}
					else
					{
						Toast.makeText(AccountActivity.this, "Please Enter Income", Toast.LENGTH_SHORT).show();
					}
				}
			});
			settingsDialog = builder.create();
			return settingsDialog; 

		case VARIABLE_EXPENSES_DIALOG_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_variableexpenselimit, (ViewGroup)findViewById(R.id.variableexpensesdialog));
			p1 = (EditText) layout.findViewById(R.id.editTextSetVariableExpenseLimit);

			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle("SET VARIABLE EXPENSE");
			AccountActivity.this.removeDialog(VARIABLE_EXPENSES_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//final Double totalIncome = Double.valueOf(p1.getText().toString()) + Double.valueOf(p2.getText().toString());;
					if(!p1.getText().toString().isEmpty())
					{
						variableExpenditureLimit = Float.parseFloat(p1.getText().toString());
					}
					else
					{
						variableExpenditureLimit = 0.0f;	
					}
					if(variableExpenditureLimit > 0)
					{
						((TextView) findViewById(R.id.textViewVariableLimit)).setText("$"+variableExpenditureLimit);
						AccountActivity.this.removeDialog(VARIABLE_EXPENSES_DIALOG_ID);
					}
					else
					{
						Toast.makeText(AccountActivity.this, "Please Enter Variable Expense", Toast.LENGTH_SHORT).show();
					}
				}
			});
			settingsDialog = builder.create();
            return settingsDialog; 

		case FIXED_EXPENSES_DIALOG_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_fixedexpenselimit, (ViewGroup)findViewById(R.id.fixedexpensesdialog));
			p1 = (EditText) layout.findViewById(R.id.editTextSetFixedExpenseLimit);

			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle("SET FIXED EXPENSE");
			AccountActivity.this.removeDialog(FIXED_EXPENSES_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(!p1.getText().toString().isEmpty())
					{
						fixedExpenditureLimit = Float.parseFloat(p1.getText().toString());
					}
					else
					{
						fixedExpenditureLimit = 0.0f;
					}
					if(fixedExpenditureLimit > 0)
					{
						((TextView) findViewById(R.id.textViewFixedLimit)).setText("$"+fixedExpenditureLimit);
						AccountActivity.this.removeDialog(FIXED_EXPENSES_DIALOG_ID);
					}
					{
						Toast.makeText(AccountActivity.this, "Please Enter Fixed Expenses", Toast.LENGTH_SHORT).show();
					}
				}
			});
			settingsDialog = builder.create();
            return settingsDialog;
		case REPEAT_OPTION_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.categories_list, (ViewGroup)findViewById(R.id.radio_groupDialog));	
			final RadioGroup radgrp = (RadioGroup)layout.findViewById(R.id.radio_groupDialog); 
			
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.settings_frequency);
			AccountActivity.this.removeDialog(REPEAT_OPTION_ID);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int radioButtonID = radgrp.getCheckedRadioButtonId();
					RadioButton radioButton = (RadioButton) radgrp.findViewById(radioButtonID);
					if(radioButton != null)
					{
						((TextView) findViewById(R.id.TextView_RepeatOption)).setText(radioButton.getText().toString());
						repeatOption = radioButton.getText().toString();
					}
					else
					{
						((TextView) findViewById(R.id.TextView_RepeatOption)).setText("ONCE");
						repeatOption = "ONCE";
						Toast.makeText(AccountActivity.this, "No Frequency Selected. Once by default", Toast.LENGTH_SHORT).show();
					}
					AccountActivity.this.removeDialog(REPEAT_OPTION_ID);
				}
				});
			dialog = builder.create();	
			return dialog;

		case SAVING_DIALOG_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.savingslimit, (ViewGroup)findViewById(R.id.savinglimitDialog));
			p1 = (EditText) layout.findViewById(R.id.editTextSetSavingAmountLimit);

			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle("SET SAVING AMOUNT");
			AccountActivity.this.removeDialog(SAVING_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//final Double totalIncome = Double.valueOf(p1.getText().toString()) + Double.valueOf(p2.getText().toString());;
					if(!p1.getText().toString().isEmpty())
					{
						 monthlySavingAmount = Float.parseFloat(p1.getText().toString());
						((TextView) findViewById(R.id.textViewmonthlySavings)).setText("$"+monthlySavingAmount);
						AccountActivity.this.removeDialog(SAVING_DIALOG_ID);
					}
					{
						Toast.makeText(AccountActivity.this, "Please Enter Saving Amount", Toast.LENGTH_SHORT).show();
					}
				}
			});
			settingsDialog = builder.create();
            return settingsDialog; 
			
		default:
			break;
		}
    	return null;
    };
}
