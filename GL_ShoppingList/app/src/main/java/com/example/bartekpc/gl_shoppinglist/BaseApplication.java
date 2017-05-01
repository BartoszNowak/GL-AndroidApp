package com.example.bartekpc.gl_shoppinglist;

import android.app.Application;
import android.content.Context;

import com.example.bartekpc.gl_shoppinglist.model.Product;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by BartekPC on 4/4/2017.
 */

public class BaseApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
        final RealmConfiguration config = new RealmConfiguration.Builder().build();
        /*final RealmConfiguration config = new RealmConfiguration.Builder().initialData(new Realm.Transaction()
        {
            @Override
            public void execute(final Realm realm)
            {
                Product product = realm.createObject(Product.class);
                product.setId(0);
                product.setName("Mleko");
                product.setCatalogId(-1);
            }
        }).build();*/
        Realm.setDefaultConfiguration(config);
    }
}
