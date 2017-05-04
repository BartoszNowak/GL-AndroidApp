package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProductCreationFragment extends Fragment
{
    private static final int NO_CATALOG = -1;
    private static final String CATALOG_ID = "CATALOG_ID";
    private static final String MIN_VALUE = "0";
    private static final String MAX_VALUE = "1000";
    private static final float PRICE_DEFAULT_VALUE = 0f;
    private static final float AMOUNT_DEFAULT_VALUE = 1f;

    @NotEmpty(messageId = R.string.field_cannot_be_empty)
    private EditText editText_productName;

    @MinNumberValue(value = MIN_VALUE, messageId = R.string.valid_input_number_values)
    @MaxNumberValue(value = MAX_VALUE, messageId = R.string.valid_input_number_values)
    private EditText editText_productPrice;

    @MinNumberValue(value = MIN_VALUE, messageId = R.string.valid_input_number_values)
    @MaxNumberValue(value = MAX_VALUE, messageId = R.string.valid_input_number_values)
    private EditText editText_productAmount;

    public static Fragment getInstance(final long catalogId)
    {
        final Bundle bundle = new Bundle();
        bundle.putLong(CATALOG_ID, catalogId);
        final ProductCreationFragment fragment = new ProductCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_product_creation, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        editText_productName = (EditText) getView().findViewById(R.id.editText_productName);
        editText_productPrice = (EditText) getView().findViewById(R.id.editText_productPrice);
        editText_productAmount = (EditText) getView().findViewById(R.id.editText_productAmount);
        final CheckBox checkBox_favourite = (CheckBox) getView().findViewById(R.id.checkBox_favourite);
        Button button_add = (Button) getView().findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                if(FormValidator.validate(ProductCreationFragment.this, new IValidationCallback()
                {
                    @Override
                    public void validationComplete(final boolean result, final List<FormValidator.ValidationFail> failedValidations, final List<View> passedValidations)
                    {
                        if(!failedValidations.isEmpty())
                        {
                            for(FormValidator.ValidationFail fail : failedValidations)
                            {
                                ((TextInputLayout) fail.view.getParent().getParent()).setError(fail.message);
                            }
                        }
                    }
                }))
                {
                    onValidationSuccess(checkBox_favourite);
                }
            }
        });
    }

    private void onValidationSuccess(final CheckBox checkBox_favourite)
    {
        final String productName = editText_productName.getText().toString();
        if(DatabaseController.findProduct(productName, getArguments().getLong(CATALOG_ID)) == null)
        {
            final Float productPrice;
            final Float productAmount;
            if(TextUtils.isEmpty(editText_productPrice.getText()))
            {
                productPrice = PRICE_DEFAULT_VALUE;
            }
            else
            {
                productPrice = Float.parseFloat(editText_productPrice.getText().toString());
            }
            if(TextUtils.isEmpty(editText_productAmount.getText()))
            {
                productAmount = AMOUNT_DEFAULT_VALUE;
            }
            else
            {
                productAmount = Float.parseFloat(editText_productAmount.getText().toString());
            }
            Product product = new Product(productName, productPrice, productAmount);
            product.setFavourite(checkBox_favourite.isChecked());
            DatabaseController.addProduct(product, getArguments().getLong(CATALOG_ID));
            if(product.isFavourite())
            {
                product.setAmount(AMOUNT_DEFAULT_VALUE);
                DatabaseController.addProduct(product, NO_CATALOG);
            }
            ((ProductAddActivity) getActivity()).finishActivity();
        }
        else
        {
            DialogFactory.getInformationDialog(getContext(), R.string.cant_add_product, R.string.product_already_on_list).show();
        }
    }
}
