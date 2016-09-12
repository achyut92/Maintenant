package com.self.achyut.maintenant.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.self.achyut.maintenant.R;
import com.self.achyut.maintenant.activity.EditLandlord;
import com.self.achyut.maintenant.activity.TenantDetails;
import com.self.achyut.maintenant.domain.ElectricityCharge;

import java.util.List;

/**
 * Created by boonkui on 06-09-2016.
 */
public class BillListAdapter extends ArrayAdapter<ElectricityCharge> {

    private Context context;
    private List<ElectricityCharge> chargeList;
    private double perUnitCharge;

    public BillListAdapter(Context context, int resource, List<ElectricityCharge> chargeList, double perUnitCharge) {
        super(context, resource, chargeList);
        this.context=context;
        this.chargeList = chargeList;
        this.perUnitCharge = perUnitCharge;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvDate,tvReading,tvUnits,tvCharges,tvTotal;



        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row, parent, false);
        ElectricityCharge currentBill = getItem(position);

        double charges = currentBill.getUnitsConsumed()*perUnitCharge;
        tvDate = (TextView)rowView.findViewById(R.id.date);
        tvReading = (TextView)rowView.findViewById(R.id.reading);
        tvUnits = (TextView)rowView.findViewById(R.id.units);
        tvCharges = (TextView)rowView.findViewById(R.id.eb_charge);
        tvTotal = (TextView)rowView.findViewById(R.id.total);

        tvDate.setText(DateHandler.dateToString(currentBill.getDateNoted()));
        tvReading.setText(String.valueOf(currentBill.getCurrentReading())+"(Reading)");
        tvUnits.setText(String.valueOf(currentBill.getUnitsConsumed())+"(Units)");
        tvCharges.setText("₹"+String.valueOf(charges)+"(EB Charges)");
        tvTotal.setText("₹"+String.valueOf(currentBill.getTotal())+"(Total)");

        return rowView;
    }


    @Override
    public ElectricityCharge getItem(int position) {
        return chargeList.get(position);
    }



}
