package com.fateh.personalbudgeting;

import com.fateh.personalbudgeting.R;

import android.os.Bundle;
import android.content.Intent;

public class SplashActivity extends ActivityExt {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		
		//startAnimation();
	}
	
//	private void startAnimation() {
//        TextView logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
//        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//        logo1.startAnimation(fade1);
//        TextView logo2 = (TextView) findViewById(R.id.TextViewTopTitle);
//        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
//		logo2.startAnimation(fade2);
//		
//        // Transition to Main Menu when bottom title finishes animating
//        fade2.setAnimationListener(new AnimationListener() {
//            public void onAnimationEnd(Animation animation) {
//                // The animation has ended, transition to the Main Menu screen
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                SplashActivity.this.finish();
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            public void onAnimationStart(Animation animation) {
//            }
//        });
//
//		
//		Animation spin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
//		LayoutAnimationController controller = new LayoutAnimationController(spin);
//		TableLayout tlayout = (TableLayout) findViewById(R.id.TableLayout01);
//		for(int i=0; i<tlayout.getChildCount();i++)
//		{
//		TableRow row = (TableRow) tlayout.getChildAt(i);
//		row.setLayoutAnimation(controller);
//		}
//    }
//
//	@Override
//    protected void onPause() {
//    	// TODO Auto-generated method stub
//    	super.onPause();
//    	TextView logo1 = (TextView) findViewById(R.id.TextViewTopTitle);
//    	logo1.clearAnimation();
//    	
//    	TextView logo2 = (TextView) findViewById(R.id.TextViewBottomTitle);
//    	logo2.clearAnimation();
//		TableLayout tlayout = (TableLayout) findViewById(R.id.TableLayout01);
//		for(int i=0; i<tlayout.getChildCount();i++)
//		{
//		TableRow row = (TableRow) tlayout.getChildAt(i);
//		row.clearAnimation();
//		}
//  	
//    }
//	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		startAnimation();
//	}


}
