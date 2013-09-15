package com.noetap.titfl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
}
