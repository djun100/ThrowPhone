package com.throwapp;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class LocalhighscoreActivity extends ListActivity
{
   private ThrowndbadDB mDbHelper;
   private boolean refresh = false;
   SimpleCursorAdapter allThrows;
   Cursor curSor;
//   Facebook facebook = new Facebook("200385206700551");
   private SharedPreferences mPrefs;
    private Context CTX;
    Intent fbhandler;
   
   private Button deleteButton;
   private Button shareToFacebook;
   public Cursor c; 
    //Going through DB and printing all saved Throws
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localhighscore);
        
        mDbHelper = new ThrowndbadDB(this);
        mDbHelper.open();
        curSor = mDbHelper.fetchAllThrows();
       
        startManagingCursor(curSor);
        
        /*
         * comma seperated string into seperat variables
         */
        
        
        String[] resultList = new String[] { ThrowndbadDB.KEY_TITLE, ThrowndbadDB.KEY_BODY};
        
        
        
        int[] to = new int[] { R.id.title,R.id.result};
        
         Log.d("RecordThrow","int"+to);
          Log.d("RecordThrow","array = ON");
        
        // Now create an array adapter and set it to display using our row
        allThrows = new SimpleCursorAdapter(this, R.layout.throwrow, curSor, resultList, to);
        setListAdapter(allThrows);
        
        
        
        
        
        
    }
     
    @Override
protected void onListItemClick(ListView l, View v, int position, final long id) {
        
   mDbHelper = new ThrowndbadDB(this);
   mDbHelper.open();
   Cursor c = mDbHelper.fetchAllThrows();
   
   c.moveToPosition(position);
   final String rowId = c.getString(0);
   String title = c.getString(1);
   final String result = c.getString(2);
   
   String[] temp = result.split(": "); 
   
   
   String[] temp2 = temp[1].split(" ");
   String[] temp3 = temp[2].split(" ");
   String[] temp4 = temp[3].split(" ");
   
   
   
   
   
   final int power = Integer.parseInt(temp2[0]);
   final float hangtime = Float.parseFloat(temp3[0]);
   final int total = Integer.parseInt(temp4[0]);
   
   
   final int rowIntId = Integer.parseInt(rowId);
   
   TextView res = new TextView(this);
                
           res.setText(result);
           res.setPadding(30, 20, 0, 20);
   
           AlertDialog.Builder alert = new AlertDialog.Builder(this);
           alert.setTitle(title);
            
           alert.setView(res);
           
            
    alert.setPositiveButton(R.string.submitHighscore, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
             
            CTX = LocalhighscoreActivity.this;
                        boolean gotInterwebz = NetUtils.haveInternet(CTX);
                        if(gotInterwebz) {
                                
                            
                        fbhandler = new Intent(LocalhighscoreActivity.this,FbhandlerActivity.class);
                        
                        Log.d("throwdevice","just clicked");
                        
                        Bundle b = new Bundle();
                        
                        b.putInt("power", power);
                        b.putFloat("hangtime", hangtime);
                        b.putInt("total", total);
                        b.putString("type", "submitHs");
                        
                        fbhandler.putExtras(b);
                        
                        startActivityForResult(fbhandler,1);
                         
                        } else {
                                Toast.makeText(getBaseContext(),"no internet", 
                                    Toast.LENGTH_SHORT).show();
                        }
             
             
             
        }
    });
    alert.setNeutralButton(R.string.shareToFacebook, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
           
            CTX = LocalhighscoreActivity.this;
                        boolean gotInterwebz = NetUtils.haveInternet(CTX);
                        Log.d("Mafakka", "got response: " + gotInterwebz);
                        if(gotInterwebz) {
                            fbhandler = new Intent(LocalhighscoreActivity.this,FbhandlerActivity.class);
                        
                        Log.d("throwdevice","just clicked");
                        
                        Bundle b = new Bundle();
                        
                        b.putInt("power", power);
                        b.putFloat("hangtime", hangtime);
                        b.putInt("total", total);
                        b.putString("type", "shareToFb");
                        
                        fbhandler.putExtras(b);
                        
                        startActivityForResult(fbhandler,1);
                    } else {
                        Toast.makeText(getBaseContext(),R.string.fbErrorConnectToast, 
                                Toast.LENGTH_SHORT).show();
                    }
            
        }
    });
    alert.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            mDbHelper.deleteThrow(rowIntId);
            curSor.requery();
            
            Toast.makeText(getBaseContext(),R.string.deleted, 
            Toast.LENGTH_SHORT).show();
            
        }
    });
    
    alert.show();
      

}
 
     
     /*
      * keeps orientation standar-view
      */
            
     @Override
     public void onConfigurationChanged(Configuration newConfig){
         super.onConfigurationChanged(newConfig);
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     }
     
      
      
     
     
   
}