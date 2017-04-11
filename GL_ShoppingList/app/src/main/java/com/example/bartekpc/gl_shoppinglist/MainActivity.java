package com.example.bartekpc.gl_shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
{
    private EditText editText_name;
    private EditText editText_age;
    private TextView textView_log;
    private Button button_save;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_age = (EditText) findViewById(R.id.editText_age);
        textView_log = (TextView) findViewById(R.id.textView_log);
        button_save = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //saveToDatabase("mleko", 2);
                //updateLogs();
                final Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveToDatabase(final String name, final float price)
    {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Product product = realm.createObject(Product.class);
                    product.setName(name);
                    product.setPrice(price);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
    }

    private void updateLogs()
    {
        Realm realm = null;
        try
        {
            realm = Realm.getDefaultInstance();
            RealmResults<Product> productRealmResults = realm.where(Product.class).findAll();
            String output = "";
            for (Product product : productRealmResults)
            {
                output += product.toString();
            }
            textView_log.setText(output);
        }
        finally
        {
            if(realm != null)
            {
                realm.close();
            }
        }

    }
}
