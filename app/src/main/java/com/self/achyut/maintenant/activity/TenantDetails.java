package com.self.achyut.maintenant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.dbHandler.TenantDataSource;
import com.self.achyut.maintenant.domain.Tenant;
import com.self.achyut.maintenant.utils.Constants;
import com.self.achyut.maintenant.utils.DateHandler;

import java.util.Date;

public class TenantDetails extends AppCompatActivity implements View.OnClickListener {

    private Tenant tenant;
    private TenantDataSource dataSource;
    private TextView title;
    private ImageButton edit,billList;
    private EditText etName,etMobile,etAdvance,etMaintenance,etRent,etUnitCharge,etDate;
    private Button save,calculate;
    private String name,mobile,advance,maintenance,rent,unitCharge;
    private Date date;
    private LinearLayout ll_details;
    private String tenantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_details);

        tenantId = getIntent().getStringExtra(Constants.ID_MOBILE);
        dataSource = new TenantDataSource(this);
        dataSource.open();
        tenant = dataSource.getTenantById(tenantId);

        initializeWidgets();

        save.setOnClickListener(this);
        edit.setOnClickListener(this);
        billList.setOnClickListener(this);
        calculate.setOnClickListener(this);
    }

    private void initializeWidgets() {

        title = (TextView)findViewById(R.id.title);
        title.setText(tenant.getName());
        edit = (ImageButton)findViewById(R.id.edit);
        billList = (ImageButton)findViewById(R.id.bill_list);
        etName = (EditText)findViewById(R.id.tenant_name);
        etName.setHint(tenant.getName()+"(Name)");
        etMobile = (EditText)findViewById(R.id.tenant_mobile);
        etMobile.setHint(tenant.getMobile()+"(Mobile)");
        etAdvance = (EditText)findViewById(R.id.tenant_advance);
        etAdvance.setHint( String.valueOf(tenant.getAdvance())+"(Advance)");
        etMaintenance = (EditText)findViewById(R.id.tenant_maintenance);
        etMaintenance.setHint(String.valueOf(tenant.getMaintenance())+"(Maintenance)");
        etRent = (EditText)findViewById(R.id.tenant_rent);
        etRent.setHint(String.valueOf(tenant.getRent())+"(Rent)");
        etUnitCharge = (EditText)findViewById(R.id.tenant_unit_charge);
        etUnitCharge.setHint(String.valueOf(tenant.getPerUnitCharge())+"(Per Unit Charge)");
        etDate = (EditText)findViewById(R.id.tenant_date);
        etDate.setHint(DateHandler.dateToString(tenant.getDateOccupied())+"(Date Occupied : DD-MM-YYYY)");
        save = (Button)findViewById(R.id.btn_save);
        calculate = (Button)findViewById(R.id.calculate_bill);
        ll_details = (LinearLayout)findViewById(R.id.tenant_details_layout);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_save:
                name = etName.getText().toString();
                mobile = etMobile.getText().toString();
                advance = etAdvance.getText().toString();
                maintenance = etMaintenance.getText().toString();
                rent = etRent.getText().toString();
                unitCharge = etUnitCharge.getText().toString();
                date = DateHandler.stringToDate(etDate.getText().toString());

                if (name.isEmpty() || mobile.isEmpty() || advance.isEmpty() || maintenance.isEmpty() || rent.isEmpty() ||
                        unitCharge.isEmpty() || !date.before(new Date())){
                    Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();
                }else if(mobile.length()<10){
                    Toast.makeText(getApplicationContext(),"Please enter 10 digit mobile number.",Toast.LENGTH_SHORT).show();
                }else if (Double.valueOf(advance)<1 ||Double.valueOf(maintenance)<1||Double.valueOf(rent)<1||Double.valueOf(unitCharge)<1){
                    Toast.makeText(getApplicationContext(),"Please enter an amount greater than 0.",Toast.LENGTH_SHORT).show();
                }else {
                    Tenant newTenant = new Tenant();
                    newTenant.setName(name);
                    newTenant.setMobile(mobile);
                    newTenant.setRent(Double.valueOf(rent));
                    newTenant.setMaintenance(Double.valueOf(maintenance));
                    newTenant.setAdvance(Double.valueOf(advance));
                    newTenant.setPerUnitCharge(Double.valueOf(unitCharge));
                    newTenant.setDateOccupied(date);
                    dataSource.updateTenant(newTenant,tenantId);
                    startActivityForResult(new Intent(getApplicationContext(),TenantList.class),1);
                    finish();
                }
                break;

            case R.id.edit:
                ll_details.setVisibility(View.VISIBLE);
                break;

            case R.id.calculate_bill:
                Intent i = new Intent(getApplicationContext(),ElectricBill.class);
                i.putExtra(Constants.ID_MOBILE,tenantId);
                startActivity(i);
                finish();
                break;

            case R.id.bill_list:
                Intent in = new Intent(getApplicationContext(),BillHistory.class);
                in.putExtra(Constants.ID_MOBILE,tenantId);
                startActivity(in);
                break;
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
