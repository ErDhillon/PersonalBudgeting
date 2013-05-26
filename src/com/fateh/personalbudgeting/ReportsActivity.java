package com.fateh.personalbudgeting;


import java.util.ArrayList;

import com.fateh.personalbudgeting.R;

import android.R.color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ReportsActivity extends ActivityExt {
	
	public ArrayList<ExpenseData> outputFixedExpList = new ArrayList<ExpenseData>();
	public ArrayList<ExpenseData> outputVariableExpList = new ArrayList<ExpenseData>();
	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports);
        TabHost host = (TabHost) findViewById(R.id.TabHost1);
        host.setup();

        // Fixed Expenses tab
        TabSpec fixedExpensesTab = host.newTabSpec("fixedexpenseTab");
        fixedExpensesTab.setIndicator(getResources().getString(R.string.fixed), getResources().getDrawable(
                android.R.drawable.star_on));
        fixedExpensesTab.setContent(R.id.FIXED);
        host.addTab(fixedExpensesTab);

        // Variable Expenses tab
        TabSpec variableExpenseTab = host.newTabSpec("variableexpenseTab");
        variableExpenseTab.setIndicator(getResources().getString(R.string.variable), getResources().getDrawable(
                android.R.drawable.star_on));
        variableExpenseTab.setContent(R.id.VARIABLE);
        host.addTab(variableExpenseTab);
        
        // Set the default tab
        host.setCurrentTabByTag("fixedexpenseTab");

        // Retrieve the TableLayout references
        TableLayout fixedExpenseTable = (TableLayout) findViewById(R.id.TableLayout_FixedExpenses);
        TableLayout variableExpenseTable = (TableLayout) findViewById(R.id.TableLayout_VariableExpenses);

        // Give each TableLayout a yellow header row with the column names
        initializeHeaderRow(fixedExpenseTable);
        initializeHeaderRow(variableExpenseTable);

        
        db = new DatabaseHelper(getApplicationContext(), "DATABASE", null, 33);
        db.getWritableDatabase();
        
        outputFixedExpList.clear();
        outputFixedExpList = db.getFixedExpenses();
        for(int i =0; i<outputFixedExpList.size();i++)
        {
        	String category = outputFixedExpList.get(i).GetCategory();
        	Float amount = outputFixedExpList.get(i).GetAmount();
        	String date = outputFixedExpList.get(i).GetDate();
        	insertScoreRow(fixedExpenseTable, date, category, amount.toString());
        }
        
        outputVariableExpList.clear();
        outputVariableExpList = db.getVariableExpenses();
        for(int i =0; i<outputVariableExpList.size();i++)
        {
        	String category = outputVariableExpList.get(i).GetCategory();
        	Float amount = outputVariableExpList.get(i).GetAmount();
        	String date = outputVariableExpList.get(i).GetDate();
        	insertScoreRow(variableExpenseTable, date, category, amount.toString());
        }
    }

    /**
     * Add a header {@code TableRow} to the {@code TableLayout} (styled)
     * 
     * @param scoreTable
     *            the {@code TableLayout} that the header row will be added to
     */
    private void initializeHeaderRow(TableLayout scoreTable) {
        // Create the Table header row
        TableRow headerRow = new TableRow(this);

        int textColor = getResources().getColor(R.color.logo_color);
        float textSize = getResources().getDimension(R.dimen.version_size);

        addTextToRowWithValues(headerRow, getResources().getString(R.string.reportdate), color.holo_red_light, com.fateh.personalbudgeting.R.dimen.help_text_size);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.reportcategory), color.holo_blue_bright, textSize);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.reportamt), color.holo_purple, textSize);
        scoreTable.addView(headerRow);
    }

    /**
     * {@code processScores()} helper method -- Inserts a new score {@code TableRow} in the {@code TableLayout}
     * 
     * @param scoreTable
     *            The {@code TableLayout} to add the score to
     * @param scoreValue
     *            The value of the score
     * @param scoreRank
     *            The ranking of the score
     * @param scoreUserName
     *            The user who made the score
     */
    private void insertScoreRow(final TableLayout expenseTable, String date, String category, String amount) {
        final TableRow newRow = new TableRow(this);

        int textColor = getResources().getColor(R.color.menu_glow);
        float textSize = getResources().getDimension(R.dimen.version_size);

        addTextToRowWithValues(newRow, date, textColor, textSize);
        addTextToRowWithValues(newRow, category, textColor, textSize);
        addTextToRowWithValues(newRow, amount, textColor, textSize);
        expenseTable.addView(newRow);
    }

    /**
     * {@code insertScoreRow()} helper method -- Populate a {@code TableRow} with three columns of {@code TextView} data (styled)
     * 
     * @param tableRow
     *            The {@code TableRow} the text is being added to
     * @param text
     *            The text to add
     * @param textColor
     *            The color to make the text
     * @param textSize
     *            The size to make the text
     */
    private void addTextToRowWithValues(final TableRow tableRow, String text, int textColor, float textSize) {
        TextView textView = new TextView(this);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setText(text);
        tableRow.addView(textView);
    }

}
