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
    private static final int NO_CATALOG = -1;
    private static final String CATALOG_ID = "CATALOG_ID";
    private static final String MIN_VALUE = "0";
    private static final String MAX_VALUE = "1000";
    private static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    private static final float PRICE_DEFAULT_VALUE = 0f;
    private static final float AMOUNT_DEFAULT_VALUE = 1f;

    private long productId;

    @NotEmpty(messageId = R.string.field_cannot_be_empty)
    private EditText editText_productName;

    @MinNumberValue(value = MIN_VALUE, messageId = R.string.valid_input_number_values)
    @MaxNumberValue(value = MAX_VALUE, messageId = R.string.valid_input_number_values)
    private EditText editText_productPrice;

    @MinNumberValue(value = MIN_VALUE, messageId = R.string.valid_input_number_values)
    @MaxNumberValue(value = MAX_VALUE, messageId = R.string.valid_input_number_values)
    private EditText editText_productAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_creation);
        Bundle extras = getIntent().getExtras();
        productId = extras.getLong(EXTRA_PRODUCT_ID);
        editText_productName = (EditText) findViewById(R.id.editText_productName);
        editText_productPrice = (EditText) findViewById(R.id.editText_productPrice);
        editText_productAmount = (EditText) findViewById(R.id.editText_productAmount);
        final CheckBox checkBox_favourite = (CheckBox) findViewById(R.id.checkBox_favourite);
        Button button_add = (Button) findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                if (FormValidator.validate(ProductEditActivity.this, new IValidationCallback()
                {
                    @Override
                    public void validationComplete(final boolean result, final List<FormValidator.ValidationFail> failedValidations, final List<View> passedValidations)
                    {
                        if (!failedValidations.isEmpty())
                        {
                            for (FormValidator.ValidationFail fail : failedValidations)
                            {
                                ((TextInputLayout) fail.view.getParent().getParent()).setError(fail.message);
                            }
                        }
                    }
                }))
                {
                    final String productName = editText_productName.getText().toString();
                    final Float productPrice;
                    final Float productAmount;
                    if (TextUtils.isEmpty(editText_productPrice.getText()))
                    {
                        productPrice = PRICE_DEFAULT_VALUE;
                    } else
                    {
                        productPrice = Float.parseFloat(editText_productPrice.getText().toString());
                    }
                    if (TextUtils.isEmpty(editText_productAmount.getText()))
                    {
                        productAmount = AMOUNT_DEFAULT_VALUE;
                    } else
                    {
                        productAmount = Float.parseFloat(editText_productAmount.getText().toString());
                    }
                    Product product = new Product(productName, productPrice, productAmount);
                    product.setFavourite(checkBox_favourite.isChecked());
                    Product productToUpdate = DatabaseController.findProduct(productId);
                    DatabaseController.updateProduct(productToUpdate, product);
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}
