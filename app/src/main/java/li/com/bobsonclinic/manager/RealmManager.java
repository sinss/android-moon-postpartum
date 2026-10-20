
package li.com.bobsonclinic.manager;

import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import li.com.bobsonclinic.callbacks.OnBasicCallback;
import li.com.bobsonclinic.callbacks.OnFetchCallback;
import li.com.bobsonclinic.callbacks.OnSingleFetchCallback;

import static li.com.bobsonclinic.util.Config.databaseVersion;

/**
 * Created by Leo on 07/03/2017.
 */

public class RealmManager {
    //Singleton
    private static RealmManager instance;
    /**
     * Get current instance as a singleton.
     * @return BluetoothLEController instance
     */
    public static RealmManager shared() {
        if (instance == null){
            synchronized (RealmManager.class){
                if (instance == null){
                    instance = new RealmManager();
                }
            }
        }
        return instance;
    }

    private RealmConfiguration config;
    //Realm shared instance
    public Realm getRealm() {
        return Realm.getInstance(config);
    }

    public static void init() {
        RealmManager.shared();
    }

    public RealmManager() {
        super();
        config = new RealmConfiguration.Builder()
                .schemaVersion(databaseVersion)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    public <E extends RealmObject> void save(final E item, final boolean update, @Nullable final OnBasicCallback cb) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (update) {
                    realm.copyToRealmOrUpdate(item);
                } else {
                    realm.copyToRealm(item);
                }
            }
        });
    }

    public <E extends RealmObject> void save(final List<E> items, final boolean update, @Nullable final OnBasicCallback cb) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (update) {
                    realm.copyToRealmOrUpdate(items);
                } else {
                    realm.copyToRealm(items);
                }
            }
        });
    }

    public <E extends RealmObject> void delete(final E item, Class c, final @Nullable OnBasicCallback cb) {
        Realm realm = getRealm();
        Class<E> cls = (Class<E>) ((ParameterizedType) item.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        final RealmResults<E> result = realm.where(cls).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
                if (cb != null) {
                    cb.onSuccess();
                }
            }
        });
    }

    public void deleteBetween(Class c, String field, Date from, Date to, final @Nullable OnBasicCallback cb) {
        Realm relam = getRealm();
        final RealmResults result = getRealm().where(c).between(field, from, to).findAll();
        relam.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
                if (cb != null) {
                    cb.onSuccess();
                }
            }
        });
    }

    public <E extends RealmObject> void fetchFirstItem(Class<E> c, @Nullable OnSingleFetchCallback<E> cb) {
        Realm realm = getRealm();
        E item = realm.where(c).findFirst();
        if (item == null) {
            if (cb != null) {
                cb.onFailure(new Exception("Not found"));
            }
        } else {
            if (cb != null) {
                cb.onSuccess(item);
            }
        }
    }

    public <E extends  RealmObject> void fetchFirstItem(Class<E> c, String equalTo, Integer value, @Nullable OnSingleFetchCallback<E> cb) {
        Realm realm = getRealm();
        E item = realm.where(c).equalTo(equalTo, value).findFirst();
        if (item == null) {
            if (cb != null) {
                cb.onFailure(new Exception("Not found"));
            }
        } else {
            if (cb != null) {
                cb.onSuccess(item);
            }
        }
    }

    public <E extends  RealmObject> void fetchFirstItem(Class c, String equalTo, String value, @Nullable OnSingleFetchCallback<E> cb) {
        Realm realm = getRealm();
        E item = (E) realm.where(c).equalTo(equalTo, value).findFirst();
        if (item == null) {
            if (cb != null) {
                cb.onFailure(new Exception("Not found"));
            }
        } else {
            if (cb != null) {
                cb.onSuccess(item);
            }
        }
    }

    public <E extends  RealmObject> void fetchFirstItem(Class c, String equalTo, Date value, @Nullable OnSingleFetchCallback<E> cb) {
        Realm realm = getRealm();
        E item = (E) realm.where(c).equalTo(equalTo, value).findFirst();
        if (item == null) {
            if (cb != null) {
                cb.onFailure(new Exception("Not found"));
            }
        } else {
            if (cb != null) {
                cb.onSuccess(item);
            }
        }
    }

    /**
     * Fetch all items from database
     * @param c
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * Fetch all items from database
     * @param c
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, boolean value, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, String value, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, boolean value, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, String value, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, Date value, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, Date value, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items between two date
     * @param c
     * @param field
     * @param from
     * @param to
     * @param cb
     * */
    public <E extends RealmObject> void fetchItemsBetween(Class c, String field, Date from, Date to, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).between(field, from, to).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items between two date
     * @param c
     * @param field
     * @param from
     * @param to
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItemsBetween(Class c, String field, Date from, Date to, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).between(field, from, to).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    public <E extends RealmObject> void deleteItemsBetween(Class c, String field, Date from, Date to, final @Nullable OnBasicCallback cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).between(field, from, to).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
                if (cb != null) {
                    cb.onSuccess();
                }
            }
        });
    }

    public <E extends  RealmObject> void fetchItemsInRange(Class c, String field1, Date date1, String field2, Date date2, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c)
                .greaterThanOrEqualTo(field1, date1)
                .greaterThanOrEqualTo(field2, date2)
                .findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    public <E extends  RealmObject> void fetchItemsInRange(Class c, String field1, int value, String field2, Date date1, String field3, Date date2, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c)
                .greaterThanOrEqualTo(field2, date1)
                .greaterThanOrEqualTo(field3, date2)
                .equalTo(field1, value)
                .findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, int value, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, int value, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, float value, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, float value, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, long value, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll();
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * fetch items with parameter
     * @param c
     * @param equalTo
     * @param value
     * @param sortBy
     * @param sort
     * @param cb
     * */
    public <E extends RealmObject> void fetchItems(Class c, String equalTo, long value, String sortBy, Sort sort, final @Nullable OnFetchCallback<E> cb) {
        final Realm realm = getRealm();
        final RealmResults<E> result = realm.where(c).equalTo(equalTo, value).findAll().sort(sortBy, sort);
        List<E> items = realm.copyFromRealm(result);
        if (cb != null) {
            cb.onSuccess(items);
        }
    }

    /**
     * Clear table with given table
     * @param c
     * */
    public void clearTable(final Class c) {
        Runnable executor = new Runnable() {
            @Override
            public void run() {
                Realm realm = getRealm();
                realm.delete(c);
            }
        };
        realmTransaction(executor);
    }



    /**
     * Update realm data must inside an realm transaction block , so pass an executable to executed
     * @param execute
     * */
    public void realmTransaction(final Runnable execute) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                execute.run();
            }
        });
    }

    public void clearAll() {
        //TODO: 清空所有資料
        final Realm realm = getRealm();
        Runnable executor = new Runnable() {
            @Override
            public void run() {
                realm.deleteAll();
            }
        };
        realmTransaction(executor);
    }
}
