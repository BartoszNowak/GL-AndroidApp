package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bartekpc.gl_shoppinglist.DatabaseController;
import com.example.bartekpc.gl_shoppinglist.DialogFactory;
import com.example.bartekpc.gl_shoppinglist.R;
import com.example.bartekpc.gl_shoppinglist.model.Product;

import java.util.List;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MaxNumberValue;
import eu.inmite.android.lib.validations.form.annotations.MinNumberValue;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.iface.IValidationCallback;

public class ProductEditActivity extends AppCompatActivity
{
    private static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";

    private long productId;
    private EditText editText_productName;
    private EditText editText_productPrice;
    private EditText editText_productAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_creation);
        Bundle extras = getIntent().getExtras();
        productId = extras.getLong(EXTRA_PRODUCT_ID);
        setTitle(DatabaseController.findProduct(productId).getName());
        editText_productName = (EditText) findViewById(R.id.editText_productName);
        editText_productPrice = (EditText) findViewById(R.id.editText_productPrice);
        editText_productAmount = (EditText) findViewById(R.id.editText_productAmount);
        Button button_add = (Button) findViewById(R.id.button_add);
        button_add.setText(R.string.edit);
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                editConfirmButtonClick();
            }
        });
    }

    private void editConfirmButtonClick()
    {
        Product productToUpdate = DatabaseController.findProduct(productId);
        String productName = editText_productName.getText().toString();
        if(DatabaseController.findProduct(productName, DatabaseController.findProduct(productId).getCatalogId()) == null)
        {
            float productPrice;
            float productAmount;
            if(TextUtils.isEmpty(editText_productName.getText()))
            {
                productName = productToUpdate.getName();
            }
            if(TextUtils.isEmpty(editText_productPrice.getText()))
            {
                productPrice = productToUpdate.getPrice();
            }
            else
            {
                productPrice = Float.parseFloat(editText_productPrice.getText().toString());
            }
            if(TextUtils.isEmpty(editText_productAmount.getText()))
            {
                productAmount = productToUpdate.getAmount();
            }
            else
            {
                productAmount = Float.parseFloat(editText_productAmount.getText().toString());
            }
            Product product = new Product(productName, productPrice, productAmount);
            DatabaseController.updateProduct(productToUpdate, product);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else
        {
            DialogFactory.getInformationDialog(ProductEditActivity.this, R.string.cant_add_product, R.string.product_already_on_list).show();
        }
    }
}
