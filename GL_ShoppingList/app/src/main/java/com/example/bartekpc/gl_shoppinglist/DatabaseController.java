package com.example.bartekpc.gl_shoppinglist;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BartekPC on 4/7/2017.
 */

public class DatabaseController
{
    public static void addProductToDatabase(final String name, final float price)
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

    static void addCatalog(final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ListCatalog catalog = new ListCatalog();
        catalog.setId(getAllCatalogs().size());
        catalog.setName(name);
        realm.copyToRealm(catalog);
        realm.commitTransaction();
    }

    static List<ListCatalog> getAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        //RealmResults<ListCatalog> catalogs = realm.where(ListCatalog.class).findAll();
        return realm.where(ListCatalog.class).findAll();
    }

    static ListCatalog getCatalogById(final long id)
    {
        return null;
    }
}
