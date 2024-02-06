package com.example.emppunching43;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private EditText edLogin,edPassword;
    private Button btnLogin,btnAdd;
    private Login  l1;
    private Intent intent;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beginApp();
    }
    public void beginApp(){
        btnAdd= (Button) findViewById(R.id.btnAdd);
        btnLogin = (Button) findViewById(R.id.btnLgn);
        edLogin=(EditText) findViewById(R.id.ed1);
        edPassword = (EditText) findViewById(R.id.ed2);
        mHandler = new Handler();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbh =new DatabaseHelper(MainActivity.this);
                l1 = new Login(-1,edLogin.getText().toString(),edPassword.getText().toString());
                boolean isValid =dbh.checkUserNamePassword(l1);
                if(isValid == false){
                    Toast.makeText(MainActivity.this,"User name or Password is not Valid!\nTry Again!",Toast.LENGTH_SHORT).show();
                }
                else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(MainActivity.this,MainPage.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this,"Success!!!!!",Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    l1=new Login(-1,edLogin.getText().toString(),edPassword.getText().toString());
                    Toast.makeText(MainActivity.this,l1.toString(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    l1 = new Login(-1,"Error" ,"Error");
                    Toast.makeText(MainActivity.this,"Error creating user!",Toast.LENGTH_SHORT).show();
                }
                DatabaseHelper dbh = new DatabaseHelper(MainActivity.this);
                boolean isRegistered = dbh.checkUserName(l1);
                if(isRegistered == true){
                    Toast.makeText(MainActivity.this,"Already registered",Toast.LENGTH_LONG).show();
                }else{
                    boolean b = dbh.adOne(l1);
                    Toast.makeText(MainActivity.this,"Success="+b+"\nRegistration success :)",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}