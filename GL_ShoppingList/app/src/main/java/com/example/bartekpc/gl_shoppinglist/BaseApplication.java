package com.example.bartekpc.gl_shoppinglist;

import android.app.Application;
import android.content.Context;

import com.example.bartekpc.gl_shoppinglist.model.Product;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
        final RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
