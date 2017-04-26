package com.example.bartekpc.gl_shoppinglist;

import com.example.bartekpc.gl_shoppinglist.model.AppSettings;
import com.example.bartekpc.gl_shoppinglist.model.Catalog;
import com.example.bartekpc.gl_shoppinglist.model.Product;
import com.example.bartekpc.gl_shoppinglist.model.ProductFilterMode;
import com.example.bartekpc.gl_shoppinglist.model.ProductSortMode;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DatabaseController
{
    private static final String ID = "id";
    private static final String CATALOG_ID = "catalogId";
    private static final String IS_PURCHASED = "isPurchased";
    private static final String IS_FAVOURITE = "isFavourite";

    public static void addCatalog(final String name)
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

    static void deleteCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        Catalog result = realm.where(Catalog.class).equalTo(ID, catalog.getId()).findFirst();
        RealmResults<Product> productsIncludedInCatalog = realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).findAll();
        realm.beginTransaction();
        result.deleteFromRealm();
        productsIncludedInCatalog.deleteAllFromRealm();
        realm.commitTransaction();
    }

    static void updateCatalogName(final Catalog catalog, final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        Catalog result = realm.where(Catalog.class).equalTo(ID, catalog.getId()).findFirst();
        realm.beginTransaction();
        result.setName(name);
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
            return (long) (realm.where(Catalog.class).max(ID)) + 1;
        }
        return 0;
    }

    static Catalog getCatalog(final int index)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> results = realm.where(Catalog.class).findAll();
        return results.get(index);
    }

    public static int numberOfCatalogs()
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
            return (long) (realm.where(Product.class).max(ID)) + 1;
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

    static List<Product> getAllProducts()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    static List<Product> getAllProductsInCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    /*static List<Product> getAllProductsInCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalogId).findAllSorted(getSortMode(), Sort.ASCENDING);
    }*/

    static List<Product> getAllPurchasedProductsInCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).equalTo(IS_PURCHASED, true).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    static List<Product> getAllNotPurchasedProductsInCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalogId).equalTo(IS_PURCHASED, false).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    static void updateProduct(final int index, final long catalogId, final Product updatedProduct)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo(CATALOG_ID, catalogId).findAllSorted(getSortMode(), Sort.ASCENDING);
        realm.beginTransaction();
        Product product = results.get(index);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        realm.commitTransaction();
    }

    static void setProductFavourite(final Product product, final boolean favourite)
    {
        Realm realm = Realm.getDefaultInstance();
        Product result = realm.where(Product.class).equalTo(ID, product.getId()).findFirst();
        realm.beginTransaction();
        result.setFavourite(favourite);
        realm.commitTransaction();
    }

    static void setProductPurchased(final Product product, final boolean purchased)
    {
        Realm realm = Realm.getDefaultInstance();
        Product result = realm.where(Product.class).equalTo(ID, product.getId()).findFirst();
        realm.beginTransaction();
        result.setPurchased(purchased);
        realm.commitTransaction();
    }

    static void deleteProduct(final Product product)
    {
        Realm realm = Realm.getDefaultInstance();
        Product result = realm.where(Product.class).equalTo(ID, product.getId()).findFirst();
        realm.beginTransaction();
        result.deleteFromRealm();
        realm.commitTransaction();
    }

    static List<Product> getAllProductsInCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalogId).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    /*static List<Product> getAllProductsInCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).findAllSorted(getSortMode(), Sort.ASCENDING);
    }*/

    /*static Product getProductInCatalog(final long catalogId, final int index)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo(CATALOG_ID, catalogId).findAllSorted(getSortMode(), Sort.ASCENDING);
        return  results.get(index);
    }*/

    static void setSortMode(final ProductSortMode sortMode)
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        realm.beginTransaction();
        appSettings.setSortMode(sortMode);
        realm.commitTransaction();
    }

    private static String getSortMode()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        if(appSettings == null)
        {
            setUpDefaultAppSettings();
        }
        else
        {
            return appSettings.getSortMode();
        }
        return null;
    }

    static int getSortModeIndex()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        if(appSettings == null)
        {
            setUpDefaultAppSettings();
        }
        else
        {
            return appSettings.getSortModeIndex();
        }
        return -1;
    }

    static void setFilterMode(final ProductFilterMode filterMode)
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        realm.beginTransaction();
        appSettings.setFilterMode(filterMode);
        realm.commitTransaction();
    }

    static int getFilterModeIndex()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        if(appSettings == null)
        {
            setUpDefaultAppSettings();
        }
        else
        {
            return appSettings.getFilterModeIndex();
        }
        return -1;
    }

    private static void setUpDefaultAppSettings()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = new AppSettings(ProductSortMode.SORT_DEFAULT, ProductFilterMode.FILTER_ALL);
        realm.beginTransaction();
        realm.copyToRealm(appSettings);
        realm.commitTransaction();
    }

    static List<Product> getAllProductsInCatalogFiltered(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();

        switch(getFilterModeIndex())
        {
            case 0:
            {
                return realm.where(Product.class).equalTo(CATALOG_ID, catalogId).findAllSorted(getSortMode(), Sort.ASCENDING);
            }
            case 1:
            {
                return realm.where(Product.class).equalTo(CATALOG_ID, catalogId).equalTo(IS_PURCHASED, false).findAllSorted(getSortMode(), Sort.ASCENDING);
            }
            default:
            {
                return null;
            }
        }
    }

    static List<Product> getAllFavouriteProducts()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(IS_FAVOURITE, true).findAllSorted(getSortMode(), Sort.ASCENDING);
    }
}