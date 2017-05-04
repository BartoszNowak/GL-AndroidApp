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
    private static final int NO_CATALOG = -1;
    private static final String ID = "id";
    private static final String NAME = "name";
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

    public static List<Catalog> getAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Catalog.class).findAllSorted(ID, Sort.ASCENDING);
    }

    public static void deleteCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        Catalog result = realm.where(Catalog.class).equalTo(ID, catalog.getId()).findFirst();
        RealmResults<Product> productsIncludedInCatalog = realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).findAll();
        realm.beginTransaction();
        result.deleteFromRealm();
        productsIncludedInCatalog.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static void updateCatalogName(final Catalog catalog, final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        Catalog result = realm.where(Catalog.class).equalTo(ID, catalog.getId()).findFirst();
        realm.beginTransaction();
        result.setName(name);
        realm.commitTransaction();
    }

    public static void deleteAllCatalogs()
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Catalog> catalogRealmResults = realm.where(Catalog.class).findAll();
        RealmResults<Product> productRealmResults = realm.where(Product.class).notEqualTo(CATALOG_ID, NO_CATALOG).findAll();
        realm.beginTransaction();
        catalogRealmResults.deleteAllFromRealm();
        productRealmResults.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public static long getNextCatalogKey()
    {
        Realm realm = Realm.getDefaultInstance();
        if(getAllCatalogs().size() != 0)
        {
            return (long) (realm.where(Catalog.class).max(ID)) + 1;
        }
        return 0;
    }

    public static Catalog getCatalog(final int index)
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

    public static Catalog findCatalog(final String name)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Catalog.class).equalTo(NAME, name).findFirst();
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

    public static void addProduct(final Product product, final long catalogId)
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
        return realm.where(Product.class).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    public static List<Product> getAllProductsInCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    public static List<Product> getAllPurchasedProductsInCatalog(final Catalog catalog)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(CATALOG_ID, catalog.getId()).equalTo(IS_PURCHASED, true).findAllSorted(getSortMode(), Sort.ASCENDING);
    }

    public static void updateProduct(final Product productToUpdate, final Product updatedProduct)
    {
        Realm realm = Realm.getDefaultInstance();
        Product result = realm.where(Product.class).equalTo(ID, productToUpdate.getId()).findFirst();
        realm.beginTransaction();
        result.setName(updatedProduct.getName());
        result.setPrice(updatedProduct.getPrice());
        result.setFavourite(updatedProduct.isFavourite());
        result.setAmount(updatedProduct.getAmount());
        realm.commitTransaction();
    }

    public static void setProductPurchased(final Product product, final boolean purchased)
    {
        Realm realm = Realm.getDefaultInstance();
        Product result = realm.where(Product.class).equalTo(ID, product.getId()).findFirst();
        realm.beginTransaction();
        result.setPurchased(purchased);
        realm.commitTransaction();
    }

    public static void deleteProduct(final Product product)
    {
        Realm realm = Realm.getDefaultInstance();
        Product result = realm.where(Product.class).equalTo(ID, product.getId()).findFirst();
        realm.beginTransaction();
        result.deleteFromRealm();
        realm.commitTransaction();
    }

    public static void setSortMode(final ProductSortMode sortMode)
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
        return getAppSettings().getSortMode();
    }

    public static int getSortModeIndex()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        if(appSettings == null)
        {
            setUpDefaultAppSettings();
        }
        return getAppSettings().getSortModeIndex();
    }

    public static void setFilterMode(final ProductFilterMode filterMode)
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        realm.beginTransaction();
        appSettings.setFilterMode(filterMode);
        realm.commitTransaction();
    }

    public static int getFilterModeIndex()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = realm.where(AppSettings.class).findFirst();
        if(appSettings == null)
        {
            setUpDefaultAppSettings();
        }
        return getAppSettings().getFilterModeIndex();
    }

    private static void setUpDefaultAppSettings()
    {
        Realm realm = Realm.getDefaultInstance();
        AppSettings appSettings = new AppSettings(ProductSortMode.SORT_DEFAULT, ProductFilterMode.FILTER_ALL);
        realm.beginTransaction();
        realm.copyToRealm(appSettings);
        realm.commitTransaction();
    }

    private static AppSettings getAppSettings()
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(AppSettings.class).findFirst();
    }

    public static List<Product> getAllProductsInCatalogFiltered(final long catalogId)
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
        }
        return null;
    }

    public static Product findProduct(final String name, final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(NAME, name).equalTo(CATALOG_ID, catalogId).findFirst();
    }

    public static Product findProduct(final String name, final long catalogId, final boolean favourite)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(NAME, name).equalTo(CATALOG_ID, catalogId).equalTo(IS_FAVOURITE, favourite).findFirst();
    }

    public static Product findProduct(final long id)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(ID, id).findFirst();
    }

    public static List<Product> getAllFavouriteProductsFromCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(IS_FAVOURITE, true).equalTo(CATALOG_ID, catalogId).findAll();
    }

    public static List<Product> getAllNonFavouriteProductsFromCatalog(final long catalogId)
    {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Product.class).equalTo(IS_FAVOURITE, false).equalTo(CATALOG_ID, catalogId).findAll();
    }

    public static void setAllProductsWithNameFavourite(final String name, final boolean favourite)
    {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> results = realm.where(Product.class).equalTo(NAME, name).notEqualTo(CATALOG_ID, NO_CATALOG).findAll();
        realm.beginTransaction();
        for(Product product : results)
        {
            product.setFavourite(favourite);
        }
        realm.commitTransaction();
    }
}