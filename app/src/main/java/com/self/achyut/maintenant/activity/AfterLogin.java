package com.self.achyut.maintenant.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.dbHandler.TenantDataSource;
import com.self.achyut.maintenant.domain.Landlord;
import com.self.achyut.maintenant.utils.SessionManager;

public class AfterLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText num_tenants;
    private Button next;
    private int num_of_tenants;
    private SessionManager sessionManager;
    private TenantDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        initializeWidget();
        sessionManager = SessionManager.getInstance(getApplicationContext());
        dataSource = new TenantDataSource(this);
        next.setOnClickListener(this);
    }

    private void initializeWidget() {

        num_tenants = (EditText)findViewById(R.id.num_tenants);
        next = (Button)findViewById(R.id.bt_next);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bt_next){

            try {
                num_of_tenants = Integer.valueOf(num_tenants.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(),"Please enter a number.",Toast.LENGTH_SHORT).show();
            }

            if (num_of_tenants<1){
                Toast.makeText(getApplicationContext(),"Please enter a number greater than zero.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Thank you.",Toast.LENGTH_SHORT).show();

                Landlord ll = sessionManager.getLandlordFromPref();
                ll.setNo_of_tenants(num_of_tenants);
                sessionManager.createLoginSession(ll);

                dataSource.open();
                dataSource.createEmptyRows(num_of_tenants);
                dataSource.close();

                startActivity(new Intent(getApplicationContext(), TenantList.class));
                finish();
            }
        }
    }
}
