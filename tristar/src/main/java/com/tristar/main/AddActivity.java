package com.tristar.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tristar.db.DbHelper;
import com.tristar.utils.SessionData;

@SuppressWarnings("ALL")
public class AddActivity extends Activity implements OnClickListener {
private Button btn_save;
private EditText edit_first,edit_last,edit_pass,edit_companyname;
  Button cancel;
ImageView clearServer, clearAdd, clearPort,clearcompanyname;
private DbHelper mHelper;
private SQLiteDatabase dataBase;
private String id,fname,lname,lpass,lcompanyname;

private boolean isUpdate; 
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        
        btn_save=(Button)findViewById(R.id.add);
        edit_first=(EditText)findViewById(R.id.edt_companyname);
        edit_last=(EditText)findViewById(R.id.edt_username);
        edit_pass = (EditText)findViewById(R.id.edt_password);
        edit_companyname = (EditText)findViewById(R.id.edt_companynewname);
        clearServer = (ImageView)findViewById(R.id.clear_companyname);

        clearAdd = (ImageView)findViewById(R.id.clear_adduserid);
        clearPort = (ImageView)findViewById(R.id.clear_addpassword);
        clearcompanyname = (ImageView)findViewById(R.id.clear_companynamedup);
        cancel = (Button)findViewById(R.id.cancel);





       isUpdate=getIntent().getExtras().getBoolean("update");
        if(isUpdate)
        {
        	id=getIntent().getExtras().getString("ID");
        	fname=getIntent().getExtras().getString("Fname");
        	lname=getIntent().getExtras().getString("Lname");
        	lpass =getIntent().getExtras().getString("Fpass");
        	lcompanyname = getIntent().getExtras().getString("lcompanyname");
        	edit_first.setText(fname);
        	edit_last.setText(lname);
        	edit_pass.setText(lpass);
        	edit_companyname.setText(lcompanyname);
        	
        }
         
         btn_save.setOnClickListener(this);
         clearServer.setOnClickListener(this);
         clearAdd.setOnClickListener(this);
         clearPort.setOnClickListener(this);
         clearcompanyname.setOnClickListener(this);
         cancel.setOnClickListener(this);
         
         mHelper=new DbHelper(this);
        
    }

	public void onClick(View v) {
		if(btn_save == v)
		{
		fname=edit_first.getText().toString().trim();
		Log.d("firstname", ""+fname);
		lname=edit_last.getText().toString().trim();
		Log.d("lnamename", ""+lname);
		lpass = edit_pass.getText().toString().trim();
		Log.d("lpassname", ""+lpass);
		lcompanyname = edit_companyname.getText().toString().trim();
		Log.d("lcompanyname", ""+lcompanyname);
		if(fname.length()>0 && lname.length()>0 && lpass.length()>0 && lcompanyname.length()>0)
				 
		{
			saveData();
			SessionData.getInstance().setSelectedserver(fname+","+lname +","+ lpass+","+lcompanyname);
			
		}
		else
		{
			AlertDialog.Builder alertBuilder=new AlertDialog.Builder(AddActivity.this);
			alertBuilder.setTitle("Field is Empty");
			alertBuilder.setMessage("Please, Enter All The Field");
			alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
					
				}
			});
			alertBuilder.create().show();
		}
		}
		else if(clearServer == v){
			edit_companyname.setText("");
        }
        else if(clearAdd == v){
        	edit_last.setText("");          
        }
        else if(clearPort == v){
        	edit_pass.setText("");
        }
        else if(clearcompanyname == v)
        {
        	edit_first.setText("");
        	
        }
        else if(cancel == v){
            Intent i = new Intent(AddActivity.this, DisplayActivity.class);
            startActivity(i);
            finish();
        }

		
		
	}

	
	private void saveData(){
		dataBase=mHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put(DbHelper.KEY_FNAME,fname);
		values.put(DbHelper.KEY_LNAME,lname );
		values.put(DbHelper.KEY_LPASS,lpass);
		values.put(DbHelper.KEY_LCOMPANYNAME,lcompanyname);
	
		
		System.out.println("");
		if(isUpdate)
		{    
			dataBase.update(DbHelper.TABLE_NAME, values, DbHelper.KEY_ID+"="+id, null);
		}
		else
		{
			dataBase.insert(DbHelper.TABLE_NAME, null, values);
		}
		dataBase.close();
		finish();
		
		
	}

}
