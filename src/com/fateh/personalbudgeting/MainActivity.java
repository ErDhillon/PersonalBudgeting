package com.fateh.personalbudgeting;

import com.fateh.personalbudgeting.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/************
 * Application Statring Point
 * @author administrator
 *
 */
public class MainActivity extends ActivityExt {
	SharedPreferences mBudgetSettings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // Retrieve the shared preferences
		mBudgetSettings = getSharedPreferences(BUDGET, Context.MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		initBudgetSettings();
		initFixedExpenses();
		initVariableExpenses();
		initReports();
		
		
	}
	
private void initReports() {
		// TODO Auto-generated method stub
	ImageButton buttonreport = (ImageButton) findViewById(R.id.imageButtonMenuReport);
	buttonreport.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MainActivity.this, ReportsActivity.class));
		}
	});

	}

private void initVariableExpenses() {
		// TODO Auto-generated method stub
		ImageButton button = (ImageButton) findViewById(R.id.imageButtonMenuVariableExpense);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, VariableExpensesActivity.class));
			}
		});	
	}

private void initFixedExpenses() {
		// TODO Auto-generated method stub
	// TODO Auto-generated method stub
		ImageButton button = (ImageButton) findViewById(R.id.imageButtonMenuFixedExpense);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, FixedExpensesActivity.class));
			}
		});		
	}

private void initBudgetSettings() {
		// TODO Auto-generated method stub
	ImageButton button = (ImageButton) findViewById(R.id.imageButtonMenuSettings);
	button.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MainActivity.this, AccountActivity.class));
		}
	});
	}

//	@Override
//	public void onBackPressed() {
//		//super.onBackPressed();
//		// TODO Auto-generated method stub
//		   new AlertDialog.Builder(this)
//	        .setTitle("Really Exit?")
//	        .setMessage("Are you sure you want to exit?")
//	        .setNegativeButton(R.string.no, null)
//	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//                   finish();
//				}
//	        }).create().show();
//	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
	        //Ask the user if they want to quit
	        new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Really Exit")
	        .setMessage("Are you sure you want to exit?")
	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                //Stop the activity
	                finish();    
	            }
	        })
	        .setNegativeButton(R.string.no, null)
	        .show();

	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }
	}
	
}


/***************
 * When using ListView
 */
//        ListView menuList = (ListView) findViewById(R.id.ListView_Menu);
//        String[] items = {//getResources().getString(R.string.menu_item_personal),
//        				  //getResources().getString(R.string.menu_item_account),
//        				  getResources().getString(R.string.menu_item_budget),
//        				  //getResources().getString(R.string.menu_item_expenses),
//        				  getResources().getString(R.string.menu_item_expenseReports),
//        };
//        //Use data adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_item, items);
//        menuList.setAdapter(adapter);
//        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        	public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id)
//        	{
//        	TextView tView = (TextView) itemClicked;
//        	String strText = tView.getText().toString();
//
//        	if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_personal)))
//        	{
//        		startActivity(new Intent(MainActivity.this, PersonalActivity.class));
//        	}
//        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_account)))
//        	{
//        		startActivity(new Intent(MainActivity.this, AccountActivity.class));
//        	}
//        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_budget)))
//        	{
//        		startActivity(new Intent(MainActivity.this, BudgetActivity.class));
//        	}
//        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_expenses)))
//        	{
//        		startActivity(new Intent(MainActivity.this, VariableExpensesActivity.class));
//        	}
//        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_expenseReports)))
//        	{
//        		startActivity(new Intent(MainActivity.this, ReportsActivity.class));
//        	}
//
//        	}
//		});
