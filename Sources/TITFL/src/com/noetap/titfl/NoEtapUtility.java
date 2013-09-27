package com.noetap.titfl;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getScreenWidth(Activity activity)
	{
		int sdkVer = android.os.Build.VERSION.SDK_INT;

		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		if (sdkVer < 13)
		{
			size.x = display.getHeight();
			size.y = display.getWidth();
		}
		else
		{
			display.getSize(size);
		}
		if (size.x < size.y)
		{
			return size.x;
		}
		else
		{
			return size.y;
		}
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static int getScreenHeight(Activity activity)
	{
		int sdkVer = android.os.Build.VERSION.SDK_INT;

		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		if (sdkVer < 13)
		{
			size.x = display.getHeight();
			size.y = display.getWidth();
		}
		else
		{
			display.getSize(size);
		}
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
	
	@SuppressWarnings("deprecation")
	public static Drawable createDrawableFromAsset(Activity activity, String assetName, int desiredW, int desiredH)
	{
		AssetManager am = activity.getAssets();
		InputStream bitmap = null;
		Drawable drawable = null;
		try 
		{
		    bitmap = am.open(assetName);
		    drawable = BitmapDrawable.createFromStream(bitmap, assetName);
		} 
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		finally 
		{
		    if (bitmap != null)
		    {
				try 
				{
					bitmap.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
		    }
		}

		if (drawable != null) 
		{
			Bitmap bitmapOrg = ((BitmapDrawable) drawable).getBitmap();
			int width = bitmapOrg.getWidth();
			int height = bitmapOrg.getHeight();
			float scaleWidth = ((float) desiredW) / width;
			float scaleHeight = ((float) desiredH) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
			drawable = new BitmapDrawable(resizedBitmap);
			BitmapDrawable bmDrawable = (BitmapDrawable) drawable;
			bmDrawable.setTargetDensity(activity.getResources().getDisplayMetrics());
		}
		return drawable;
	}

	public static Bitmap getBitmap(Activity activity, String png)
	{
		AssetManager am = activity.getAssets();
		InputStream stream = null;
		Bitmap bmp = null;
		try 
		{
			stream = am.open(png);
			bmp = BitmapFactory.decodeStream(stream);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if (stream != null)
			{
				try 
				{
					stream.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return bmp;
	}
}
