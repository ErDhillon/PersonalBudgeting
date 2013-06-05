package com.fateh.personalbudgeting;

import java.util.ArrayList;
import java.util.Calendar;

import com.fateh.personalbudgeting.R;

import android.R.string;
import android.net.ParseException;
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

/***************************
 * This class will be deprecated
 * @author administrator
 *
 */
@SuppressLint("NewApi")
public class BudgetActivity extends ActivityExt {

	SharedPreferences mBudgetSettings = null;
	private boolean mStartingChildActivity;
	static final int ACCOUNT_DIALOG_ID = 1;
	static final int VARIABLE_EXPENSES_DIALOG_ID = 2;
	static final int FIXED_EXPENSES_DIALOG_ID = 3;

	static Float totalMonthlyIncome=0.0f;
	static Float fixedExpenditure= 0.0f;
	static Float variableExpenditure = 0.0f;
	static Float fixedProgressBalance = 0.0f;
	static Float variableProgressBalance = 0.0f;
	static Float incomeProgressBalance = 0.0f;
	
	static Integer prevFixedProgressBalanceInt = 0;
	static Integer prevVariableProgressBalanceInt = 0;
	
	static String prevTextViewFixedExp;
	static String prevTextViewVariableExp;
	
	static String prevTextViewfixedProgressBalance ;
	static String prevTextViewvariableProgressBalance ;
	static String prevTextViewincomeProgressBalance ;

	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

	DatabaseHelper db;
	ActualBudgetData 	bdata = new ActualBudgetData();
	public ArrayList<ActualBudgetData> budgetInformation  = new ArrayList<ActualBudgetData>();
	ProgressBar fixedProgressBar, variableProgressBar, IncomeProgessBar;
	static String monthName;
	
	static Boolean isFirstTimeCreation = true;
	
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
		//ProgressBars
		IncomeProgessBar = 			(ProgressBar) findViewById(R.id.progressBarIncome);
		fixedProgressBar = 			(ProgressBar) findViewById(R.id.progressBarFixedExpenses);
		variableProgressBar = 		(ProgressBar) findViewById(R.id.progressBarVariableExpenses);
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

@Override
protected void onPause() {
	super.onPause();
	Log.e(DEBUG_TAG, "On Pause Activity BUDGET");	
	
};
	
	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		//Actual Expenses
		String dbActualFixed =  ((EditText) findViewById(R.id.editTextActualFExpenses)).getText().toString();
		String dbActualVariable =  ((EditText) findViewById(R.id.editTextActualVExpenses)).getText().toString();
		String dbActualIncome = ((TextView)findViewById(R.id.textViewIncome)).getText().toString();
		
		//Expenses Values
		String currFixedExpense = ((TextView)findViewById(R.id.textViewFixedExpense)).getText().toString();
		String currVariableExpense = ((TextView)findViewById(R.id.textViewVariableExpense)).getText().toString();

		//Income ProgressBar
		Integer incomeProgress = ((ProgressBar)findViewById(R.id.progressBarIncome)).getProgress();
		String dbMonth = ((TextView)findViewById(R.id.textViewMonthname)).getText().toString();
		
		if(isFinishing())
		{
			// TODO Auto-generated method stub
			if(monthName != null)
				bdata.mMonth = monthName;
			else
			{
			    final Calendar c = Calendar.getInstance();
			    int month = c.get(Calendar.MONTH);
			    bdata.mMonth = MONTHS[month];
			}
			if(!dbActualFixed.isEmpty())
				bdata.mActualFixedExpenses = Float.parseFloat(dbActualFixed);
			else
				bdata.mActualFixedExpenses = 0.0f;
			
			if(!dbActualVariable.isEmpty())
				bdata.mActualVariableExpenses = Float.parseFloat(dbActualVariable);
			else
				bdata.mActualVariableExpenses = 0.0f;
			
			if(!dbActualIncome.isEmpty())
				bdata.mIncome = Float.parseFloat(dbActualIncome);
			else
				bdata.mActualVariableExpenses = 0.0f;

			SaveBudgetInformation(bdata);
			isFirstTimeCreation = true;
		}
		else
		{
			isFirstTimeCreation = false;
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
			EditText actualVExp = (EditText)findViewById(R.id.editTextActualVExpenses);
			
			if(!(actualFExp.getText().toString().isEmpty() && actualVExp.getText().toString().isEmpty())) 
			{
				Float fixProgress = Float.parseFloat(actualFExp.getText().toString());
				Float varProgress = Float.parseFloat(actualVExp.getText().toString());
				fixedProgressBar.setMax(fixProgress.intValue());
				fixedProgressBar.setProgress(fixProgress.intValue());
				((TextView)findViewById(R.id.textViewFixedExpProgress)).setText("Balance $"+fixProgress);

				variableProgressBar.setMax(varProgress.intValue());
				variableProgressBar.setProgress(varProgress.intValue());
				((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("Balance $"+varProgress);
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

private void SaveBudgetInformation(ActualBudgetData bdata) {
			// TODO Auto-generated method stub
	db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 36);
	db.getWritableDatabase();
	if(bdata != null)
	db.InsertBudgetLimits(bdata);
}

private void initRetrieveData() {
		// TODO Auto-generated method stub
        db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 36);
        db.getWritableDatabase();

        if(isFirstTimeCreation)
        {
	        budgetInformation = db.getCurrentBudgetData("MAY");
	        for(int i =0; i<budgetInformation.size();i++)
	        {
	        	Float income = budgetInformation.get(i).GetIncome();
	        	Float actualfixedexp = budgetInformation.get(i).GetFixedActualExpense();
	        	Float actualvariableexpense = budgetInformation.get(i).GetVariableActualExpense();
	        	String month = budgetInformation.get(i).GetMonth();
	        	
	        	((EditText)findViewById(R.id.editTextActualFExpenses)).setText(actualfixedexp.toString());
	        	((EditText)findViewById(R.id.editTextActualVExpenses)).setText(actualvariableexpense.toString());	        	
	        	
	        	((TextView)findViewById(R.id.textViewIncome)).setText(income.toString());
	        	((TextView)findViewById(R.id.textViewMonthname)).setText(month);
	        	
//	        	((TextView)findViewById(R.id.textViewFixedExpense)).setText(fixedProgress.toString());
//	        	((TextView)findViewById(R.id.textViewVariableExpense)).setText(variableProgress.toString());
//	        	
//	        	//Set the ProgressBar
//				IncomeProgessBar.setMax(income.intValue());
//				IncomeProgessBar.setProgress(incomeProgress.intValue());
//	        	((TextView)findViewById(R.id.textViewIncomeProgress)).setText("Balance: $"+incomeProgress);
//				
//	        	fixedProgressBar.setMax(actualfixedexp.intValue());
//				fixedProgressBar.setProgress((actualfixedexp.intValue()-fixedProgress.intValue()));
//				((TextView)findViewById(R.id.textViewFixedExpProgress)).setText("Balance: $"+ (actualfixedexp-fixedProgress));
//	
//				variableProgressBar.setMax(actualvariableexpense.intValue());
//				variableProgressBar.setProgress(actualvariableexpense.intValue()-variableProgress.intValue());
//				((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("Balance: $"+ (actualvariableexpense-variableProgress));
	        }     
        }
        else
        {
        	Toast.makeText(BudgetActivity.this, "Data Not Retrieved", Toast.LENGTH_SHORT).show();
        }
}
		
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		EditText actualFExp = (EditText)findViewById(R.id.editTextActualFExpenses);
		EditText actualVExp = (EditText)findViewById(R.id.editTextActualVExpenses);
		Float actualFixedExpense = Float.parseFloat(actualFExp.getText().toString());
		Float actualVariableExpense = Float.parseFloat(actualVExp.getText().toString());

		fixedProgressBar.setMax(actualFixedExpense.intValue());
		variableProgressBar.setMax(actualVariableExpense.intValue());
		IncomeProgessBar.setMax(totalMonthlyIncome.intValue());

		if(resultCode == RESULT_OK)
		{
			if(data != null)
			{

				if(monthName == null)
				{
				    final Calendar c = Calendar.getInstance();
				    int month = c.get(Calendar.MONTH);
				    monthName = MONTHS[month];
				}
				((TextView)findViewById(R.id.textViewMonthname)).setText(monthName);
				if(data.getExtras().containsKey(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES))
				{
					 Bundle bundle = data.getExtras();
					 fixedExpenditure =  bundle.getFloat(BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES);
					 fixedProgressBalance = actualFixedExpense - fixedExpenditure;
					 incomeProgressBalance -=  fixedExpenditure;
					 
					 fixedProgressBar.setProgress(fixedProgressBalance.intValue());
					 ((TextView)findViewById(R.id.textViewFixedExpProgress)).setText("Balance: $"+fixedProgressBalance);
					 
					 ((TextView)findViewById(R.id.textViewFixedExpense)).setText(fixedExpenditure.toString());
					 ((TextView)findViewById(R.id.textViewVariableExpense)).setText(prevTextViewVariableExp);

					 variableProgressBar.setProgress(prevVariableProgressBalanceInt);
					 ((TextView)findViewById(R.id.textViewVariableExpProgress)).setText(prevTextViewvariableProgressBalance);
					 
				}
				else if(data.getExtras().containsKey(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES))
				{
					 Bundle bundle = data.getExtras();
					 variableExpenditure =  bundle.getFloat(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES);
					 variableProgressBalance = actualVariableExpense - variableExpenditure;
					 incomeProgressBalance -= variableExpenditure;
					
					 variableProgressBar.setProgress(variableProgressBalance.intValue());
					 ((TextView)findViewById(R.id.textViewVariableExpProgress)).setText("Balance: $"+variableProgressBalance);

					 ((TextView)findViewById(R.id.textViewFixedExpense)).setText(prevTextViewFixedExp);
					 ((TextView)findViewById(R.id.textViewVariableExpense)).setText(variableExpenditure.toString());
					 
					 fixedProgressBar.setProgress(prevFixedProgressBalanceInt);
					 ((TextView)findViewById(R.id.textViewFixedExpProgress)).setText(prevTextViewfixedProgressBalance);
				}
				
				IncomeProgessBar.setProgress(incomeProgressBalance.intValue());
				((TextView)findViewById(R.id.textViewIncomeProgress)).setText("Balance: $"+incomeProgressBalance);
				((TextView)findViewById(R.id.textViewIncome)).setText(totalMonthlyIncome.toString());
			}
		}
		else
		{
				((TextView)findViewById(R.id.textViewMonthname)).setText(monthName);
				
				((TextView)findViewById(R.id.textViewIncomeProgress)).setText(prevTextViewincomeProgressBalance);
				((TextView)findViewById(R.id.textViewIncome)).setText(totalMonthlyIncome.toString());
				IncomeProgessBar.setProgress(incomeProgressBalance.intValue());
				
				((TextView)findViewById(R.id.textViewFixedExpProgress)).setText(prevTextViewfixedProgressBalance);
				fixedProgressBar.setProgress(prevFixedProgressBalanceInt);
				((TextView)findViewById(R.id.textViewFixedExpense)).setText(prevTextViewFixedExp);
				
				((TextView)findViewById(R.id.textViewVariableExpProgress)).setText(prevTextViewvariableProgressBalance);
				variableProgressBar.setProgress(prevVariableProgressBalanceInt);
				((TextView)findViewById(R.id.textViewVariableExpense)).setText(prevTextViewVariableExp);
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
				prevTextViewfixedProgressBalance = ((TextView)findViewById(R.id.textViewFixedExpProgress)).getText().toString();
				prevTextViewincomeProgressBalance = ((TextView)findViewById(R.id.textViewIncomeProgress)).getText().toString();
				prevTextViewvariableProgressBalance = ((TextView)findViewById(R.id.textViewVariableExpProgress)).getText().toString();
				
				prevVariableProgressBalanceInt = ((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).getProgress();
				prevFixedProgressBalanceInt = ((ProgressBar)findViewById(R.id.progressBarFixedExpenses)).getProgress();
				
				prevTextViewFixedExp = ((TextView)findViewById(R.id.textViewFixedExpense)).getText().toString();
				prevTextViewVariableExp = ((TextView)findViewById(R.id.textViewVariableExpense)).getText().toString();				
				
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
				prevTextViewfixedProgressBalance = ((TextView)findViewById(R.id.textViewFixedExpProgress)).getText().toString();
				prevTextViewincomeProgressBalance = ((TextView)findViewById(R.id.textViewIncomeProgress)).getText().toString();
				prevTextViewvariableProgressBalance = ((TextView)findViewById(R.id.textViewVariableExpProgress)).getText().toString();
				
				prevVariableProgressBalanceInt = ((ProgressBar)findViewById(R.id.progressBarVariableExpenses)).getProgress();
				prevFixedProgressBalanceInt = ((ProgressBar)findViewById(R.id.progressBarFixedExpenses)).getProgress();
				
				prevTextViewFixedExp = ((TextView)findViewById(R.id.textViewFixedExpense)).getText().toString();
				prevTextViewVariableExp = ((TextView)findViewById(R.id.textViewVariableExpense)).getText().toString();
				
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
			EditText p1; EditText p2;
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflator.inflate(R.layout.activity_incomelimit, (ViewGroup)findViewById(R.id.incomelimitDialog));
			p1 = (EditText) layout.findViewById(R.id.editTextSetIncome);
			p2 = (EditText) layout.findViewById(R.id.editTextSetIncomeOther);
			builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.settings_Shoping);
			BudgetActivity.this.removeDialog(ACCOUNT_DIALOG_ID);
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Float monthlyincome = 0.0f;
					Float monthlyIncomefromOtherSource = 0.0f;
					if(!((EditText)findViewById(R.id.editTextSetIncome)).getText().toString().isEmpty())
						monthlyincome = Float.valueOf(((EditText)findViewById(R.id.editTextSetIncome)).getText().toString());
					
					if(!((EditText)findViewById(R.id.editTextSetIncomeOther)).getText().toString().isEmpty())
						monthlyIncomefromOtherSource = Float.valueOf(((EditText)findViewById(R.id.editTextSetIncomeOther)).getText().toString());
					
					if(monthlyincome >0.0f && monthlyIncomefromOtherSource > 0.0f)
					{
						totalMonthlyIncome = monthlyincome + monthlyIncomefromOtherSource;
						// TODO Auto-generated method stub
						((TextView)findViewById(R.id.textViewIncome)).setText(totalMonthlyIncome.toString());
						IncomeProgessBar.setMax(totalMonthlyIncome.intValue());
						IncomeProgessBar.setProgress(totalMonthlyIncome.intValue());
						BudgetActivity.this.removeDialog(ACCOUNT_DIALOG_ID);
						((TextView)(findViewById(R.id.textViewIncomeProgress))).setText("Balance: $"+totalMonthlyIncome);
						incomeProgressBalance = totalMonthlyIncome;
					}
					else
					{
						Toast.makeText(BudgetActivity.this, "Please Enter Income", Toast.LENGTH_SHORT).show();
						
					}
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
