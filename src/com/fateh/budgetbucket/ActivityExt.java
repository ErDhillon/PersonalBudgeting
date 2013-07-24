package com.fateh.budgetbucket;

import android.app.Activity;

/***************
 * For Shared Preferences
 * @author administrator
 *
 */
public class ActivityExt extends Activity {

    // Game preference values
    public static final String BUDGET = "BudgetChoices";
    public static final String BUDGET_PREFERENCES_STORENAME = "Storename"; // String
    public static final String BUDGET_PREFERENCES_EMAIL = "Email"; // String
    public static final String BUDGET_PREFERENCES_PASSWORD = "Password"; // String
    public static final String BUDGET_PREFERENCES_DOB = "DOB"; // Long
    public static final String BUDGET_PREFERENCES_SHOPPINGDATE = "ShoppingDate"; // Long
    public static final String BUDGET_PREFERENCES_SHOPPINGDATE_MONTH = "month";
    public static final String BUDGET_PREFERENCES_CATEGORIES = "Category"; // 
    public static final String BUDGET_PREFERENCES_AMOUNT= "Amount"; // String URL to image
    public static final String BUDGET_PREFERENCES_TOTAL_FIXED_EXPENSES = "fixedExpenses";
    public static final String BUDGET_PREFERENCES_TOTAL_VARIABLE_EXPENSES = "variableExpenses";
    public static final String BUDGET_PREFERENCES_DATE = "date";
    public static final String BUDGET_PREFERENCES_CATEGORY = "category";
    public static final String BUDGET_PREFERENCES_REPEAT="repeat";
    public static final String BUDGET_MONTHLY_PROGRESS_STATUS_INITIAL = "InBudget";
    public static final String BUDGET_MONTHLY_PROGRESS_STATUS_ATPAR = "SavingMode";
    public static final String BUDGET_MONTHLY_PROGRESS_STATUS_OVERDRAWN = "Overdrawn";
    

    public static final String DEBUG_TAG = "PersonalBudgetLog";
}