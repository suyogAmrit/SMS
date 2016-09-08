package com.suyogcomputech.sms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Pintu on 9/8/2016.
 */
public class EditSelectedProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtItemBrand;
    Spinner spnQuantity;
    private RadioGroup productSizeRadioGroup;
    private RadioButton small, medium, large, extraLarge, doubleExtraLarge, tripleExtraLarge;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        toolbar = (Toolbar) findViewById(R.id.toolbarEditProduct);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        txtItemBrand=(TextView)findViewById(R.id.txtBrandName);
        spnQuantity=(Spinner)findViewById(R.id.spnQuantity);

    }

    public void btnEditProduct(View view) {

    }
}
