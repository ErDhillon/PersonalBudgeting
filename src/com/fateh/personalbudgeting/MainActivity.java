package com.fateh.personalbudgeting;

import com.fateh.personalbudgeting.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActivityExt {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ListView menuList = (ListView) findViewById(R.id.ListView_Menu);
        String[] items = {//getResources().getString(R.string.menu_item_personal),
        				  //getResources().getString(R.string.menu_item_account),
        				  getResources().getString(R.string.menu_item_budget),
        				  //getResources().getString(R.string.menu_item_expenses),
        				  getResources().getString(R.string.menu_item_expenseReports),
        };

        //Use data adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_item, items);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id)
        	{
        	TextView tView = (TextView) itemClicked;
        	String strText = tView.getText().toString();

        	if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_personal)))
        	{
        		startActivity(new Intent(MainActivity.this, PersonalActivity.class));
        	}
        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_account)))
        	{
        		startActivity(new Intent(MainActivity.this, AccountActivity.class));
        	}
        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_budget)))
        	{
        		startActivity(new Intent(MainActivity.this, BudgetActivity.class));
        	}
        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_expenses)))
        	{
        		startActivity(new Intent(MainActivity.this, VariableExpensesActivity.class));
        	}
        	else if(strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_expenseReports)))
        	{
        		startActivity(new Intent(MainActivity.this, ReportsActivity.class));
        	}

        	}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		   new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(R.string.no, null)
	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
                   finish();
				}
	        }).create().show();

	}
}
