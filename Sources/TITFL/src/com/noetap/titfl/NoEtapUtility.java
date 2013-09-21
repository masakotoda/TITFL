package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Display;

//In future, we may want to move this to library
public class NoEtapUtility 
{
	public static void showAlert(Context context, String title, String message)
	{
  		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

  		// Set title
  		alertDialogBuilder.setTitle(title);
  	 
  		// Set dialog message
  		alertDialogBuilder.setMessage(message);
  		alertDialogBuilder.setCancelable(false);
  		alertDialogBuilder.setPositiveButton("Got it!", 
  			new DialogInterface.OnClickListener() 
  			{
  				public void onClick(DialogInterface dialog, int id)
  				{
  					dialog.cancel();
  				}
  			});

  		// Create alert dialog
  		AlertDialog alertDialog = alertDialogBuilder.create();  	 
  		alertDialog.show();	
	}
	
	@SuppressLint("NewApi")
	public static int getScreenWidth(Activity activity)
	{
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		if (size.x < size.y)
		{
			return size.x;
		}
		else
		{
			return size.y;
		}
	}
	
	@SuppressLint("NewApi")
	public static int getScreenHeight(Activity activity)
	{
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		if (size.x < size.y)
		{
			return size.y;
		}
		else
		{
			return size.x;
		}
	}
	
	public static float getFactor(Activity activity)
	{
		int width = getScreenWidth(activity);
		float factor = width / 1080.f;
		return factor;		
	}
}
