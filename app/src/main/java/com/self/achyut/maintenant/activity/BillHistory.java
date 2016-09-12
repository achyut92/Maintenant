package com.self.achyut.maintenant.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.dbHandler.TenantDataSource;
import com.self.achyut.maintenant.domain.ElectricityCharge;
import com.self.achyut.maintenant.domain.Tenant;
import com.self.achyut.maintenant.utils.BillListAdapter;
import com.self.achyut.maintenant.utils.Constants;

import java.util.List;

public class BillHistory extends AppCompatActivity {

    private Tenant tenant;
    private TenantDataSource dataSource;
    private ListView lvBills;
    private List<ElectricityCharge> chargeList;
    private BillListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);

        initialize();
        String tenant_id = getIntent().getStringExtra(Constants.ID_MOBILE);
        tenant = dataSource.getTenantById(tenant_id);
        chargeList = dataSource.getBillForTenant(tenant_id);

        listAdapter = new BillListAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,chargeList,tenant.getPerUnitCharge());
        lvBills.setAdapter(listAdapter);
    }

    private void initialize() {
        dataSource = new TenantDataSource(this);
        dataSource.open();
        lvBills = (ListView)findViewById(R.id.list_bills);

    }
}
