package com.self.achyut.maintenant.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.domain.Landlord;
import com.self.achyut.maintenant.utils.SessionManager;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private EditText name,mobile;
    private Button next;
    private String landlord_name,landlord_mobile;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionManager = SessionManager.getInstance(getApplicationContext());

        if (sessionManager.checkLogin()){
            startActivity(new Intent(getApplicationContext(),TenantList.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inittializeWidgets();


        next.setOnClickListener(this);
    }

    private void inittializeWidgets() {

        name = (EditText)findViewById(R.id.name);
        mobile = (EditText)findViewById(R.id.mobile);
        next = (Button)findViewById(R.id.btn_next);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_next){
            landlord_name = name.getText().toString().trim();
            landlord_mobile = mobile.getText().toString().trim();

            if (landlord_mobile.isEmpty()||landlord_name.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please fill both the details.",Toast.LENGTH_SHORT).show();
            }else if (landlord_mobile.length()<10 || landlord_mobile.length()>10){
                Toast.makeText(getApplicationContext(),"Please enter 10 digit mobile number.",Toast.LENGTH_SHORT).show();
            }else {

                Landlord ll = new Landlord();
                ll.setName(landlord_name);
                ll.setMobile(landlord_mobile);
                ll.setNo_of_tenants(0);

                sessionManager.createLoginSession(ll);
                Intent i = new Intent(getApplicationContext(),AfterLogin.class);
                startActivity(i);
                finish();
            }
        }
    }
}
