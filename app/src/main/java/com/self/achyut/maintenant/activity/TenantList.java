package com.self.achyut.maintenant.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.dbHandler.TenantDataSource;
import com.self.achyut.maintenant.domain.Landlord;
import com.self.achyut.maintenant.domain.Tenant;
import com.self.achyut.maintenant.utils.Constants;
import com.self.achyut.maintenant.utils.SessionManager;
import java.util.List;

public class TenantList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SessionManager sessionManager;
    private ListView tenantList;
    private List<Tenant> tenants;
    private TenantDataSource dataSource;
    private ArrayAdapter<Tenant> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_list);

        initializeWidget();
        dataSource = new TenantDataSource(this);
        dataSource.open();
        sessionManager = SessionManager.getInstance(getApplicationContext());
        tenants = dataSource.getAllTenants();



        arrayAdapter = new ArrayAdapter<Tenant>(this,android.R.layout.simple_list_item_1,tenants);
        tenantList.setAdapter(arrayAdapter);

        tenantList.setOnItemClickListener(this);
    }


    private void initializeWidget() {

        tenantList = (ListView)findViewById(R.id.list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tenant_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_landlord:
                startActivity(new Intent(getApplicationContext(),EditLandlord.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tenant tenant = (Tenant) parent.getItemAtPosition(position);
        Intent i = new Intent(getApplicationContext(),TenantDetails.class);
        i.putExtra(Constants.ID_MOBILE,tenant.getMobile());
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            arrayAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
