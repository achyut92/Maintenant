package com.self.achyut.maintenant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

public class EditLandlord extends AppCompatActivity implements View.OnClickListener {

    private EditText etName,etMobile,etNumber;
    private SessionManager sessionManager;
    private Button btUpdate;
    private TenantDataSource dataSource;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_landlord);

        prepareDialogBox();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        initializeWidget();
        sessionManager = SessionManager.getInstance(getApplicationContext());

        Landlord ll = sessionManager.getLandlordFromPref();
        dataSource = new TenantDataSource(this);
        dataSource.open();

        etName.setText(ll.getName());
        etMobile.setText(ll.getMobile());
        etNumber.setText(String.valueOf(ll.getNo_of_tenants()));

        btUpdate.setOnClickListener(this);
    }

    private void prepareDialogBox() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING");
        builder.setMessage("Editing may result in resetting all tenant details.");
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
    }

    private void initializeWidget() {

        etName = (EditText)findViewById(R.id.et_name);
        etMobile = (EditText)findViewById(R.id.et_mobile);
        etNumber = (EditText)findViewById(R.id.et_no_of_tenants);
        btUpdate = (Button)findViewById(R.id.update);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),TenantList.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.update) {

            String landlord_name = etName.getText().toString().trim();
            String landlord_mobile = etMobile.getText().toString().trim();
            int num_of_tenants = 0;

            try {
                num_of_tenants = Integer.valueOf(etNumber.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Please enter a number.", Toast.LENGTH_SHORT).show();
            }

            if (landlord_mobile.isEmpty() || landlord_name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill both the details.", Toast.LENGTH_SHORT).show();
            } else if (landlord_mobile.length() < 10 || landlord_mobile.length() > 10) {
                Toast.makeText(getApplicationContext(), "Please enter 10 digit mobile number.", Toast.LENGTH_SHORT).show();
            } else if (num_of_tenants < 1) {
                Toast.makeText(getApplicationContext(), "Please enter a number greater than zero.", Toast.LENGTH_SHORT).show();
            } else {

                Landlord ll = new Landlord();
                ll.setName(landlord_name);
                ll.setMobile(landlord_mobile);
                ll.setNo_of_tenants(num_of_tenants);
                sessionManager.createLoginSession(ll);
                dataSource.deleteTable();
                dataSource.createEmptyRows(num_of_tenants);
                dataSource.close();
                Toast.makeText(getApplicationContext(), "Your details has been updated.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),TenantList.class));
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }
}
