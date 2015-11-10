package com.example.s188884;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyBroadcastReceiver extends BroadcastReceiver
{
	
	@Override
	public void onReceive(Context context,	Intent intent)	
	{	
 		Intent i = new Intent(context, PeriodiskService.class);	
	 	context.startService(i);
		 	
		 	
		 	// 86 400 000 millisekunder i ett døgn
	}

}
