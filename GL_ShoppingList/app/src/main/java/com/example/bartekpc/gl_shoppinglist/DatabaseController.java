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
        RealmResults<Catalog> catalogRealmResults = realm.where(Catalog.class).findAll();
        RealmResults<Product> productRealmResults = realm.where(Product.class).findAll();
        realm.beginTransaction();
        catalogRealmResults.deleteAllFromRealm();
        productRealmResults.deleteAllFromRealm();
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

    static void addProduct(final Product product, final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        product.setId(getNextProductKey());
        product.setCatalogId(catalogId);
        realm.copyToRealm(product);
        realm.commitTransaction();
    }

    private static List<Product> getAllProducts()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).findAll();
    }

    static List<Product> getAllProductsInCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo("catalogId", catalogId).findAll();
    }

    static List<Product> getAllPurchasedProductsInCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo("catalogId", catalogId).equalTo("isPurchased", true).findAll();
    }

    static List<Product> getAllFavouriteProductsInCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo("catalogId", catalogId).equalTo("isFavourite", true).findAll();
    }

    static void deleteAllProductsFromCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogId).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    static void updateProduct(final int index, final long catalogId, final Product updatedProduct)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogId).findAll();
        realm.beginTransaction();
        Product product = results.get(index);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        realm.commitTransaction();
    }

    static void setProductFavourite(final int index, final long catalogId, final boolean favourite)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogId).findAll();
        realm.beginTransaction();
        Product product = results.get(index);
        product.setFavourite(favourite);
        realm.commitTransaction();
    }

    static void setProductPurchased(final int index, final long catalogId, final boolean purchased)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogId).findAll();
        realm.beginTransaction();
        Product product = results.get(index);
        product.setPurchased(purchased);
        realm.commitTransaction();
    }

    static void deleteProduct(final int index, final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo("catalogId", catalogId).findAll();
        realm.beginTransaction();
        Product product = results.get(index);
        product.deleteFromRealm();
        realm.commitTransaction();
    }
}