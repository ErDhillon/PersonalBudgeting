package com.fateh.personalbudgeting;

import java.util.ArrayList;

import com.fateh.personalbudgeting.R;

import android.R.string;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BudgetActivity extends ActivityExt {

	SharedPreferences mBudgetSettings = null;
	private boolean mStartingChildActivity;
	static final int ACCOUNT_DIALOG_ID = 1;
	static final int VARIABLE_EXPENSES_DIALOG_ID = 2;
	static final int FIXED_EXPENSES_DIALOG_ID = 3;
	static Float actualFixedExpenses= 0.0f;
	static Float actualVariableExpenses=0.0f;
	static Float totalMonthlyIncomeInt= 0.0f;
	static Float totalMonthlyIncome=0.0f;
	static Float fixedExpenditure= 0.0f;
	static Float variableExpenditure = 0.0f;
	static Float fixedProgressBalance = 0.0f;
	static Float variableProgressBalance = 0.0f;
	static Float incomeProgressBalance = 0.0f;
	
	static String prevfixedProgressBalance ;
	static String prevvariableProgressBalance ;
	static String previncomeProgressBalance ;

	DatabaseHelper db;
	BudgetData 	bdata = new BudgetData();
	public ArrayList<BudgetData> budgetInformation  = new ArrayList<BudgetData>();
	ProgressBar fixedProgressBar, variableProgressBar, IncomeProgessBar;
	TextView incomeProgressTextView, FixedProgressTextView, VariableProgressTextView;
	TextView incomeTextView, FixedTextView, VariableTextView;
	String monthName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);
        // Retrieve the shared preferences
		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
		Log.e(DEBUG_TAG, "On Create Called");
		InitControls();
        initRetrieveData();
		InitSetActuals();
        InitSetIncome();
        InitSetFixedExpenses();
        InitSetVariableExpenses();


	}
	private void InitControls() {
		// TODO Auto-generated method stub
		IncomeProgessBar = (ProgressBar)findViewById(R.id.progressBarIncome);
		fixedProgressBar = 			(ProgressBar) findViewById(R.id.progressBarFixedExpenses);
		variableProgressBar = 		(ProgressBar) findViewById(R.id.progressBarVariableExpenses);
		incomeProgressTextView = 	(TextView)	  findViewById(R.id.textViewIncomeProgress);
		FixedProgressTextView  = 	(TextView) 	  findViewById(R.id.textViewFixedExpProgress);
		VariableProgressTextView  = (TextView) 	  findViewById(R.id.textViewVariableExpProgress);
		
		incomeTextView = (TextView)findViewById(R.id.textViewIncome);
		FixedTextView = (TextView)findViewById(R.id.textViewFixedExpense);
		VariableTextView = (TextView)findViewById(R.id.textViewVariableExpense);

	}
	@Override
	protected void onStart() {
		super.onStart();
		Log.e(DEBUG_TAG, "On Start Called");
	};
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.e(DEBUG_TAG, "On Stop Called");
	};
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.e(DEBUG_TAG, "On Restart");
	};

//	@Override
//	protected void onResume() {
//		super.onResume();
//		Log.e(DEBUG_TAG, "OnResume Activity BUDGET");	
//		setContentView(R.layout.activity_budget);
//        // Retrieve the shared preferences
//		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
//		InitSetActuals();
//		InitSetIncome();
//        InitSetFixedExpenses();
//        InitSetVariableExpenses();
//		//initRetrieveData();
//	}

@Override
protected void onPause() {
	super.onPause();
	Log.e(DEBUG_TAG, "On Pause Activity BUDGET");	
	
};
	
	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		if(isFinishing())
		{
		// TODO Auto-generated method stub

		Log.e(DEBUG_TAG, "Saving data into database");

		bdata.mMonth = monthName;
		bdata.mActualFixedExpenses = Float.parseFloat(((EditText)findViewById(R.id.editTextActualFExpenses)).getText().toString());
		bdata.mFixedExpProgress = fixedProgressBalance;
		bdata.mActualVariableExpenses = actualVariableExpenses;
		bdata.mVariableExpProgess = variableProgressBalance;
		bdata.mIncome = totalMonthlyIncome;
		bdata.mIncomeProgress = incomeProgressBalance;
		SaveBudgetInformation(bdata);
		}
		else
		{
		Log.e(DEBUG_TAG, "Activity not finished. Data is not saved");
		}
	}
	
private void InitSetActuals() {
		// TODO Auto-generated method stub
	
	Button setActuals = (Button)findViewById(R.id.buttonSetActuals);
	setActuals.setOnClickListener(new View.OnClickListener() {
//		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			EditText actualFExp = (EditText)findViewById(R.id.editTextActualFExpenses);
			actualFixedExpenses = Float.parseFloat(actualFExp.getText().toString());
			
			EditText actualVExp = (EditText)findViewById(R.id.editTextActualVExpenses);
			actualVariableExpenses = Float.parseFloat(actualVExp.getText().toString());
			
			if(actualFixedExpenses > 0 && actualVariableExpenses > 0) 
			{
				fixedProgressBar.setMax(actualFixedExpenses.intValue());
				fixedProgressBar.setProgress(actualFixedExpenses.intValue());
				FixedProgressTextView.setText("Balance $"+actualFixedExpenses);

				variableProgressBar.setMax(actualVariableExpenses.intValue());
				variableProgressBar.setProgress(actualVariableExpenses.intValue());
				VariableProgressTextView.setText("Balance $"+actualVariableExpenses);
			}
			else
			{
			Toast.makeText(BudgetActivity.this, "Please Enter Actual Limits for the Month", Toast.LENGTH_LONG).show();
			}
			fixedProgressBar.setVisibility(View.VISIBLE);	
			variableProgressBar.setVisibility(View.VISIBLE);
		}
	});	
}

private void SaveBudgetInformation(BudgetData bdata) {
			// TODO Auto-generated method stub
	db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 36);
	db.getWritableDatabase();
	if(bdata != null)
	db.InsertBudgetLimits(bdata);
}

private void initRetrieveData() {
		// TODO Auto-generated method stub
        db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 33);
        db.getWritableDatabase();

        budgetInformation = db.getCurrentBudgetData();
        for(int i =0; i<budgetInformation.size();i++)
        {
        	Float income = budgetInformation.get(i).GetIncome();
        	Float actualfixedexp = budgetInformation.get(i).GetFixedActualExpense();
        	Float actualvariableexpense = budgetInformation.get(i).GetVariableActualExpense();
        	Float fixedProgress = budgetInformation.get(i).GetFixedExpProgress();
        	Float variableProgress = budgetInformation.get(i).GetVarExpProgress();
        	Float incomeProgress =  budgetInformation.get(i).GetIncomeProgress();
        	
        	((EditText)findViewById(R.id.editTextActualFExpenses)).setText(actualfixedexp.toString());
        	((EditText)findViewById(R.id.editTextActualVExpenses)).setText(actualvariableexpense.toString());	        	
        	incomeTextView.setText(income.toString());

        	//Set the ProgressBar
			IncomeProgessBar.setMax(budgetInformation.get(i).GetIncome().intValue());
			IncomeProgessBar.setProgress(incomeProgress.intValue());
        	incomeProgressTextView.setText("Balance: $"+incomeProgress);
			
        	fixedProgressBar.setMax(actualfixedexp.intValue());
			fixedProgressBar.setProgress(fixedProgress.intValue());
			FixedProgressTextView.setText("Balance: $"+ fixedProgress);

			variableProgressBar.setMax(actualvariableexpense.intValue());
			variableProgressBar.setProgress(variableProgress.intValue());
			VariableProgressTextView.setText("Balance: $"+ variableProgress);
        	
        }     
}
		
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK)
		{
			if(data != null)
			{
				monthName = data.getExtras().getString(BUDGET_PREFERENCES_SHOPPINGDATE_MONTH);
				if(data.getExtras().containsKey(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES))
				{
					 Bundle bundle = data.getExtras();
					 fixedExpenditure =  bundle.getFloat(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES);
					 fixedProgressBalance = actualFixedExpenses - fixedExpenditure;
					 incomeProgressBalance -=  fixedExpenditure;
				}
				else if(data.getExtras().containsKey(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES))
				{
					 Bundle bundle = data.getExtras();
					 variableExpenditure =  bundle.getFloat(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES);
					 variableProgressBalance = actualVariableExpenses - variableExpenditure;
					 incomeProgressBalance -= variableExpenditure;
				}
				if(fixedExpenditure != null)
				{
					FixedTextView.setText(fixedExpenditure.toString());
				}
				if(variableExpenditure != null)
				{
					VariableTextView.setText(variableExpenditure.toString());
				}
				if(totalMonthlyIncome != null) 
				{
					incomeTextView.setText(totalMonthlyIncome.toString());
				}
				IncomeProgessBar.setMax(totalMonthlyIncomeInt.intValue());
				IncomeProgessBar.setProgress(incomeProgressBalance.intValue());
				incomeProgressTextView.setText("Balance: $"+incomeProgressBalance);

				if(actualFixedExpenses > 0 && fixedProgressBalance == 0)
				{
					fixedProgressBar.setMax(actualFixedExpenses.intValue());
					fixedProgressBar.setProgress(actualFixedExpenses.intValue());
					FixedProgressTextView.setText("Balance: $"+actualFixedExpenses);
				}
				else if(actualFixedExpenses > 0 && fixedProgressBalance > 0)
				{
				fixedProgressBar.setMax(actualFixedExpenses.intValue());
				fixedProgressBar.setProgress(fixedProgressBalance.intValue());
				FixedProgressTextView.setText("Balance: $"+fixedProgressBalance);
				}
				else if(fixedProgressBalance < 0)
				{
					fixedProgressBar.setProgress(0);
					FixedProgressTextView.setText("Exceeded Monthly Limits");
				}
				
				if(actualVariableExpenses > 0 && variableProgressBalance == 0)
				{
				variableProgressBar.setMax(actualVariableExpenses.intValue());
				variableProgressBar.setProgress(actualVariableExpenses.intValue());
				VariableProgressTextView.setText("Balance: $"+actualVariableExpenses);
				}
				else if(actualVariableExpenses > 0 && variableProgressBalance > 0)
				{
				variableProgressBar.setMax(actualVariableExpenses.intValue());
				variableProgressBar.setProgress(variableProgressBalance.intValue());
				VariableProgressTextView.setText("Balance: $"+variableProgressBalance);
				}
				
				else if(variableProgressBalance < 0)
				{
					variableProgressBar.setProgress(0);
					VariableProgressTextView.setText("Exceeded Montly Limit");
				}
			
			}
		}
		else
		{
				incomeProgressTextView.setText(previncomeProgressBalance);
				IncomeProgessBar.setMax(totalMonthlyIncomeInt.intValue());
				IncomeProgessBar.setProgress(totalMonthlyIncomeInt.intValue());
				
				FixedProgressTextView.setText(prevfixedProgressBalance);
				VariableProgressTextView.setText(prevvariableProgressBalance);
		}
	};
	

	private void InitSetVariableExpenses() {
		// TODO Auto-generated method stub
		ImageButton variableBtn  =  (ImageButton)findViewById(R.id.imageButtonVariableExpenses);
		variableBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showDialog(VARIABLE_EXPENSES_DIALOG_ID);
				prevfixedProgressBalance = FixedProgressTextView.getText().toString();
				previncomeProgressBalance = incomeProgressTextView.getText().toString();
				prevvariableProgressBalance = VariableProgressTextView.getText().toString();
				
				Intent data = new Intent(BudgetActivity.this, VariableExpensesActivity.class);
				data.putExtra(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES, 0);
				startActivityForResult(data, 1);
			}
		});
	}
	private void InitSetFixedExpenses() {
		// TODO Auto-generated method stub
		ImageButton variableBtn  =  (ImageButton)findViewById(R.id.imageButtonFixedExpenses);
		variableBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				prevfixedProgressBalance = FixedProgressTextView.getText().toString();
				previncomeProgressBalance = incomeProgressTextView.getText().toString();
				prevvariableProgressBalance = VariableProgressTextView.getText().toString();
				
				Intent data = new Intent(BudgetActivity.this, FixedExpensesActivity.class);
				data.putExtra(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES, 0);
				startActivityForResult(data, 1);

			}
		});
	}
	private void InitSetIncome() {
		// TODO Auto-generated method stub
	 ImageButton income = (ImageButton)findViewById(R.id.imageButtonIncome);
	 income.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(ACCOUNT_DIALOG_ID);
			
		}
	});
	}
    @SuppressWarnings("deprecation")
	@Override
    protected Dialog onCreateDialog(int id) {
    	DatePickerDialog  dateDialog = null;
    	LayoutInflater inflator = null;
    	View layout = null;
    	AlertDialog.Builder builder = null;
    	AlertDialog passwordDialog = null;
    	switch (id) {
		case ACCOUNT_DIALOG_ID:

			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_account, (ViewGroup)findViewById(R.id.accountDialog));
			final EditText p1 = (EditText) layout.findViewById(R.id.EditTextAccountIncome);
			final EditText p2 = (EditText) layout.findViewById(R.id.EditTextAccountIncomeOther);
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.settings_Shoping);
			BudgetActivity.this.removeDialog(ACCOUNT_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					totalMonthlyIncome = Float.valueOf(p1.getText().toString()) + Float.valueOf(p2.getText().toString());
					// TODO Auto-generated method stub
					((TextView)findViewById(R.id.textViewIncome)).setText(totalMonthlyIncome.toString());
					IncomeProgessBar.setMax(totalMonthlyIncome.intValue());
					IncomeProgessBar.setProgress(totalMonthlyIncome.intValue());
					BudgetActivity.this.removeDialog(ACCOUNT_DIALOG_ID);
					incomeProgressTextView.setText("Balance: $"+totalMonthlyIncome);
					incomeProgressBalance = totalMonthlyIncome;
				}
			});

            passwordDialog = builder.create();
            return passwordDialog; 
		case VARIABLE_EXPENSES_DIALOG_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_variableexpenses, (ViewGroup)findViewById(R.id.variableexpensesdialog));
			
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.settings_Shoping);
			BudgetActivity.this.removeDialog(VARIABLE_EXPENSES_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//final Double totalIncome = Double.valueOf(p1.getText().toString()) + Double.valueOf(p2.getText().toString());;
					// TODO Auto-generated method stub
					//((EditText)findViewById(R.id.editTextIncome)).setText(totalIncome.toString());
					Toast.makeText(BudgetActivity.this, "Hello", Toast.LENGTH_SHORT);
						
					BudgetActivity.this.removeDialog(VARIABLE_EXPENSES_DIALOG_ID);
				}
			});
            passwordDialog = builder.create();
            return passwordDialog; 
		case FIXED_EXPENSES_DIALOG_ID:
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_fixedexpenses, (ViewGroup)findViewById(R.id.fixedexpensesdialog));

			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.settings_Shoping);
			BudgetActivity.this.removeDialog(FIXED_EXPENSES_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//final Double totalIncome = Double.valueOf(p1.getText().toString()) + Double.valueOf(p2.getText().toString());;
					// TODO Auto-generated method stub
					//((EditText)findViewById(R.id.editTextIncome)).setText(totalIncome.toString());
					Toast.makeText(BudgetActivity.this, "Hello Fixed", Toast.LENGTH_SHORT);
						
					BudgetActivity.this.removeDialog(FIXED_EXPENSES_DIALOG_ID);
				}
			});
            passwordDialog = builder.create();
            return passwordDialog; 
		default:
			break;
		}
    	return null;
    };
}
