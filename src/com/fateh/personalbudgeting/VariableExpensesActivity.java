package com.fateh.personalbudgeting;

import java.util.Calendar;

import com.fateh.personalbudgeting.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VariableExpensesActivity extends ActivityExt {
	SharedPreferences mBudgetSettings;
	static final int DATE_DIALOG_ID = 0;
	static float variableExpenses = 0;
	
	DatabaseHelper db;
	ExpenseData expData;
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	static String monthName;
	
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
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_variableexpenses);
        // Retrieve the shared preferences
		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
        initCategoryRetriever();
        initDatePicker();
        initSaveData();
        initAddAnother();
	};
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
    	DatePickerDialog  dateDialog = null;
    	switch (id) {
		case DATE_DIALOG_ID:
			final TextView shopdatetTV = (TextView) findViewById(R.id.TextView_ShoppingDate);
			dateDialog = 
				new DatePickerDialog(this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								Time shoppingDateTime = new Time();
								shoppingDateTime.set(dayOfMonth, monthOfYear, year);
								long dtDob = shoppingDateTime.toMillis(true);
								shopdatetTV.setText(DateFormat.format("MMMM dd, yyyy", dtDob));
								Editor editor = mBudgetSettings.edit();
								editor.putLong(BUDGET_PREFERENCES_SHOPPINGDATE, dtDob);
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
    
    private void initSaveData()
    {
    	Button saveBtn = (Button) findViewById(R.id.Button_Save);
    	saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

		    	//Get the Intenet
		    	Intent data = getIntent();	
		    	
		    	EditText storeName = (EditText)findViewById(R.id.EditText_StoreName);
		    	EditText amountEditText = (EditText)findViewById(R.id.EditText_Amount);
		    	TextView dateTextView = (TextView)findViewById(R.id.TextView_ShoppingDate);
		    	variableExpenses += Float.valueOf(amountEditText.getText().toString());
		    	
		    	String category= storeName.getText().toString();
		    	String amount = amountEditText.getText().toString();
		    	String date = dateTextView.getText().toString();
		    	
				//Saving lastRecord
		    	SaveRecord(category, amount, date);
				Toast.makeText(VariableExpensesActivity.this, "Data Saved", 2).show();
				data.putExtra(BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES, variableExpenses);
				data.putExtra(BUDGET_PREFERENCES_SHOPPINGDATE_MONTH, monthName);
				setResult(RESULT_OK, data);
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
    
    
    private void initAddAnother(){
        
    	Button yesAnother = (Button)findViewById(R.id.Button_AddAnother);
    	yesAnother.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	EditText storeName = (EditText)findViewById(R.id.EditText_StoreName);
		    	EditText amountEditText = (EditText)findViewById(R.id.EditText_Amount);
		    	TextView dateTextView = (TextView)findViewById(R.id.TextView_ShoppingDate);
		    	variableExpenses += Float.valueOf(amountEditText.getText().toString());

		    	String category= storeName.getText().toString();
		    	String amount = amountEditText.getText().toString();
		    	String date = dateTextView.getText().toString();
		    	
		    	SaveRecord(category, amount, date);
		    	
		    	((EditText)findViewById(R.id.EditText_StoreName)).setText("");
		    	((EditText)findViewById(R.id.EditText_Amount)).setText("");

			}
		});
    }
    
	private void SaveRecord(String store, String amount, String date) {
		// TODO Auto-generated method stub

		db = new DatabaseHelper(getApplicationContext(), "name", null, 35);
		db.getWritableDatabase();
		expData = new ExpenseData();
		
		expData.mAmount = Float.parseFloat(amount);
		expData.mCategory = store;
		expData.mDate = date;
		db.AddVariableExpenseRecord(expData);	
	}
}

