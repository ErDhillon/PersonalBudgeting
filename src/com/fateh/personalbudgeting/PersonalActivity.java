package com.fateh.personalbudgeting;


import java.util.Calendar;

import com.fateh.personalbudgeting.R;

import android.R.layout;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/***************
 * Not Used
 * @author administrator
 *
 */
public class PersonalActivity extends ActivityExt {

	SharedPreferences mGameSettings;
	static final int DATE_DIALOG_ID = 0;
	static final int REGISTER_ME_DIALOG_ID = 1;
    // Game preference values
    public static final String GAME_PREFERENCES = "BudgetPrefs";
    public static final String GAME_PREFERENCES_NICKNAME = "Nickname"; // String
    public static final String GAME_PREFERENCES_EMAIL = "Email"; // String
    public static final String GAME_PREFERENCES_PASSWORD = "Password"; // String
    public static final String GAME_PREFERENCES_DOB = "DOB"; // Long
    public static final String GAME_PREFERENCES_GENDER = "Gender"; // Integer, in array order: Male (1), Female (2), and Undisclosed (0)
    public static final String GAME_PREFERENCES_SCORE = "Score"; // Integer
    public static final String GAME_PREFERENCES_CURRENT_QUESTION = "CurQuestion"; // Integer
    public static final String GAME_PREFERENCES_AVATAR = "Avatar"; // String URL to image

    // XML Tag Names
    public static final String XML_TAG_QUESTION_BLOCK = "questions";
    public static final String XML_TAG_QUESTION = "question";
    public static final String XML_TAG_QUESTION_ATTRIBUTE_NUMBER = "number";
    public static final String XML_TAG_QUESTION_ATTRIBUTE_TEXT = "text";
    public static final String XML_TAG_QUESTION_ATTRIBUTE_IMAGEURL = "imageUrl";
    public static final int QUESTION_BATCH_SIZE = 15;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
        // Retrieve the shared preferences
        mGameSettings = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        // Initialize the nickname entry
        initNicknameEntry();
        // Initialize the email entry
        initEmailEntry();
        // Initialize the Password chooser
        initPasswordChooser();
        // Initialize the Date picker
        initDatePicker();
        // Initialize the spinner
        //initGenderSpinner();
	}

	@Override 
	protected Dialog onCreateDialog(int id)
	{
		DatePickerDialog dateDialog = null;
		switch(id)
		{
		case 0:
			final TextView dob = (TextView) findViewById(R.id.TextView_DOB_Info);
			new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							Time dateOfBirth = new Time();
							dateOfBirth.set(dayOfMonth, monthOfYear, year);
							long dtDob = dateOfBirth.toMillis(true);
							dob.setText(DateFormat.format("MMMM dd, yyyy", dtDob));
							Editor editor = mGameSettings.edit();
							editor.putLong(GAME_PREFERENCES_DOB, dtDob);
							editor.commit();
						}
					},0,0,0);
		return dateDialog;
		case 1:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.create();
			
			break;
		}
		return null;
	}
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	super.onPrepareDialog(id, dialog);
    	switch(id){
    	case DATE_DIALOG_ID:
    		DatePickerDialog dateDialog = (DatePickerDialog)(dialog);
    		int iDay, iMonth, iYear;
    		
    		if(mGameSettings.contains(GAME_PREFERENCES_DOB))
    		{
    			long msBirthDate = mGameSettings.getLong(GAME_PREFERENCES_DOB, 0);
    			Time dateOfBorth = new Time();
    			dateOfBorth.set(msBirthDate);
    			iDay = dateOfBorth.monthDay;
    			iMonth = dateOfBorth.month;
    			iYear = dateOfBorth.year;
    		}
    		else
    		{
    		Calendar cal = Calendar.getInstance();
    		iDay = cal.get(Calendar.DAY_OF_MONTH);
    		iMonth = cal.get(Calendar.MONTH);
    		iYear = cal.get(Calendar.YEAR);
    		}
    		
    		dateDialog.updateDate(iYear, iMonth, iDay);
    		return;
    	}
    };
//	private void initGenderSpinner() {
//		// TODO Auto-generated method stub
//	    final Spinner spinner = (Spinner) findViewById(R.id.Spinner_Gender);
//	    ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.genders,
//	            android.R.layout.simple_spinner_item);
//	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	    spinner.setAdapter(adapter);
//	    if (mGameSettings.contains(GAME_PREFERENCES_GENDER)) {
//	        spinner.setSelection(mGameSettings.getInt(GAME_PREFERENCES_GENDER, 0));
//	    }
//	    // Handle spinner selections
//	    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//	        public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition,
//	                long selectedId) {
//	            Editor editor = mGameSettings.edit();
//	            editor.putInt(GAME_PREFERENCES_GENDER, selectedItemPosition);
//	            editor.commit();
//	        }
//
//	        public void onNothingSelected(AdapterView<?> parent) {
//	        }
//	    });
//	}

	private void initDatePicker() {
		// TODO Auto-generated method stub
	    TextView dobInfo = (TextView) findViewById(R.id.TextView_DOB_Info);

	    if (mGameSettings.contains(GAME_PREFERENCES_DOB)) {
	        dobInfo.setText(DateFormat.format("MMMM dd, yyyy", mGameSettings.getLong(GAME_PREFERENCES_DOB, 0)));
	    } else {
	        dobInfo.setText(R.string.settings_button_dob);
	    }
	    
		Button pickDate = (Button) findViewById(R.id.Button_DOB);
		pickDate.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	private void initPasswordChooser() {
		// TODO Auto-generated method stub
		final EditText password1 = (EditText) findViewById(R.id.EditText_Password);
		final EditText password2 = (EditText) findViewById(R.id.EditText_ConfirmPassword);
		final TextView error = (TextView) findViewById(R.id.TextView_PwdProblem);
		password1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				String pass1 = password1.getText().toString();
				String pass2 = password2.getText().toString();
				if(pass1.equals(pass2))
					error.setText("Password Equal");
				else
					error.setText("Password Not Equal");
			}				
		});
	}

	private void initEmailEntry() {
		// TODO Auto-generated method stub
		
	}

	private void initNicknameEntry() {
		// TODO Auto-generated method stub
		//Save Nickname
	    final EditText nicknameText = (EditText) findViewById(R.id.EditText_Nickname);
	    if (mGameSettings.contains(GAME_PREFERENCES_NICKNAME)) {
	        nicknameText.setText(mGameSettings.getString(GAME_PREFERENCES_NICKNAME, ""));
	    }
	    nicknameText.setOnKeyListener(new View.OnKeyListener() {
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
	                String strNickname = nicknameText.getText().toString();
	                Editor editor = mGameSettings.edit();
	                editor.putString(GAME_PREFERENCES_NICKNAME, strNickname);
	                editor.commit();
	                return true;
	            }
	            return false;
	        }
	    });
	}
}
