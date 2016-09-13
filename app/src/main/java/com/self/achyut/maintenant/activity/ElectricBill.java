package com.self.achyut.maintenant.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.dbHandler.TenantDataSource;
import com.self.achyut.maintenant.domain.ElectricityCharge;
import com.self.achyut.maintenant.domain.Landlord;
import com.self.achyut.maintenant.domain.Tenant;
import com.self.achyut.maintenant.utils.Constants;
import com.self.achyut.maintenant.utils.SessionManager;

import java.util.Date;

public class ElectricBill extends AppCompatActivity implements View.OnClickListener {

    private String tenant_id;
    private EditText etPrev,etCurr,etUnits,etTotal;
    private Button calculate,send,reset;
    private TenantDataSource dataSource;
    private Tenant tenant;
    private SessionManager sessionManager;
    private Landlord landlord;
    private ElectricityCharge charge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_bill);

        tenant_id = getIntent().getStringExtra(Constants.ID_MOBILE);
        dataSource = new TenantDataSource(this);
        sessionManager = SessionManager.getInstance(this);
        landlord = sessionManager.getLandlordFromPref();
        dataSource.open();
        tenant = dataSource.getTenantById(tenant_id);

        initializeWidgets();

        try{
            etPrev.setText(String.valueOf(dataSource.getPreviousMonthReading(tenant_id)));
        }catch (Exception e){
            e.printStackTrace();
        }
        calculate.setOnClickListener(this);
        reset.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    private void initializeWidgets() {
        etCurr = (EditText)findViewById(R.id.curr_read);
        etPrev = (EditText)findViewById(R.id.prev_read);
        etUnits = (EditText)findViewById(R.id.units);
        etTotal = (EditText)findViewById(R.id.total);
        calculate = (Button)findViewById(R.id.calculate_bill);
        send = (Button)findViewById(R.id.send_message);
        send.setEnabled(false);
        reset = (Button)findViewById(R.id.reset);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.calculate_bill:
                double curr = 0;
                double prev = 0;
                try {
                    curr = Double.valueOf(etCurr.getText().toString());
                    prev = Double.valueOf(etPrev.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),Constants.ERROR_IN_READINGS,Toast.LENGTH_SHORT).show();
                    return;
                }
                double unit = curr-prev;
                if (unit<0){
                    Toast.makeText(getApplicationContext(),Constants.ERROR_IN_UNITS,Toast.LENGTH_SHORT).show();
                    etPrev.setText("");
                    etCurr.setText("");
                    return;
                }
                etUnits.setText(String.valueOf(unit));
                double total = (unit*tenant.getPerUnitCharge())+tenant.getRent()+tenant.getMaintenance();
                etTotal.setText("₹"+String.valueOf(total));
                charge = new ElectricityCharge();
                charge.setDateNoted(new Date());
                charge.setTotal(total);
                charge.setUnitsConsumed(unit);
                charge.setCurrentReading(curr);
                charge.setPreviousReading(prev);
                send.setEnabled(true);

                break;

            case R.id.reset:
                etCurr.setText("");
                etPrev.setText("");
                etTotal.setText("");
                etUnits.setText("");
                send.setEnabled(false);

                break;

            case R.id.send_message:
                String messageBuilder = "From "+landlord.getName()+" to "+tenant.getName()+".\nCurrent Reading - "+charge.getCurrentReading()+"\n"+
                        "Previous Reading - "+charge.getPreviousReading()+"\nUnits Consumed - "+charge.getUnitsConsumed()+
                        "\nElectricity Charge - ₹"+(charge.getUnitsConsumed()*tenant.getPerUnitCharge())+
                        "\nRent - ₹"+tenant.getRent()+"\nMaintenance - ₹"+tenant.getMaintenance()+"\nTotal - ₹"+
                        charge.getTotal();
                //Toast.makeText(getApplicationContext(),messageBuilder,Toast.LENGTH_LONG).show();

                if (dataSource.createBill(charge,tenant_id)){
                    Toast.makeText(getApplicationContext(),Constants.SUCCESS_DB,Toast.LENGTH_LONG).show();
                    sendSMS(messageBuilder);
                } else  {
                    Toast.makeText(getApplicationContext(),Constants.ERROR_DB,Toast.LENGTH_LONG).show();
                    break;
                }
        }

    }

    private void sendSMS(final String messageBuilder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send SMS");
        builder.setMessage(messageBuilder);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(tenant.getMobile(), null, messageBuilder, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                }

                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
