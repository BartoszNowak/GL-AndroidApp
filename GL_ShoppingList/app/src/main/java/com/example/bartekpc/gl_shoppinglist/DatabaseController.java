package com.example.bartekpc.gl_shoppinglist;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BartekPC on 4/7/2017.
 */

public class DatabaseController
{
    static void addCatalog(final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Catalog catalog = new Catalog();
        catalog.setId(getNextCatalogKey());
        catalog.setName(name);
        realm.copyToRealm(catalog);
        realm.commitTransaction();
    }

    static List<Catalog> getAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Catalog.class).findAll();
    }

    static void deleteCatalog(final int index)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> results = realm.where(Catalog.class).findAll();
        realm.beginTransaction();
        Catalog catalog = results.get(index);
        catalog.deleteFromRealm();
        realm.commitTransaction();
    }

    static void updateCatalogName(final int index, final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> results = realm.where(Catalog.class).findAll();
        realm.beginTransaction();
        Catalog catalog = results.get(index);
        catalog.setName(name);
        realm.commitTransaction();
    }

    static void deleteAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> results = realm.where(Catalog.class).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private static long getNextCatalogKey()
    {
        Realm realm = Realm.getDefaultInstance();
        if(getAllCatalogs().size() != 0)
        {
            return (long) (realm.where(Catalog.class).max("id")) + 1;
        }
        return 0;
    }

    static Catalog getCatalog(final int index)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> results = realm.where(Catalog.class).findAll();
        return results.get(index);
    }

    static int numberOfCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> results = realm.where(Catalog.class).findAll();
        return results.size();
    }

    private static long getNextProductKey()
    {
        Realm realm = Realm.getDefaultInstance();
        if(getAllProducts().size() != 0)
        {
            return (long) (realm.where(Product.class).max("id")) + 1;
        }
        return 0;
    }

    static void addProduct(final Product product, final int catalogIndex)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        product.setId(getNextProductKey());
        product.setCatalogId(catalogIndex);
        realm.copyToRealm(product);
        realm.commitTransaction();
    }

    private static List<Product> getAllProducts()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).findAll();
    }

    static List<Product> getAllProductsInCatalog(final int catalogIndex)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo("catalogId", catalogIndex).findAll();
    }

    static void deleteProductFromCatalog(final int index, final int catalogIndex)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogIndex).findAll();
        realm.beginTransaction();
        Product product = results.get(index);
        product.deleteFromRealm();
        realm.commitTransaction();
    }

    static void deleteAllProductsFromCatalog(final int catalogIndex)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogIndex).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }
}