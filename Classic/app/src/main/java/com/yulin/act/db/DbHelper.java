package com.yulin.act.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private static final String DB_NAME = "classic.db";
    private static final int DB_VERSION = 1;

    private static DbHelper mInstance;

    private final Context mContext;    
    private final String mName;
    private final SQLiteDatabase.CursorFactory mFactory;
    private final int mNewVersion;

    private SQLiteDatabase mDatabase;
    private boolean mIsInitializing;

    private DbHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        // keeps a reference of the passed context in order to access to the application assets and resources.
        mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(true);
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(false);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        if (mDatabase != null) {
            if (!mDatabase.isOpen()) {
                // Darn!  The user closed the database by calling mDatabase.close().
                mDatabase = null;
            } else if (!writable || !mDatabase.isReadOnly()) {
                // The database is already open for business.
                return mDatabase;
            }
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }

        SQLiteDatabase db = mDatabase;
        try {
            mIsInitializing = true;

            db = createOrOpenDatabase();
            if (db == null) {
                return null;
            }

            onConfigure(db);

            final int version = db.getVersion();
            if (version != mNewVersion) {
                if (db.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " +
                            db.getVersion() + " to " + mNewVersion + ": " + mName);
                }

                db.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
                        if (version > mNewVersion) {
                            onDowngrade(db, version, mNewVersion);
                        } else {
                            onUpgrade(db, version, mNewVersion);
                        }
                    }
                    db.setVersion(mNewVersion);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            onOpen(db);

            if (db.isReadOnly()) {
                Log.w(TAG, "Opened " + mName + " in read-only mode");
            }

            mDatabase = db;
            return db;
        } catch (IOException e) {
            Log.d(TAG, "getDatabaseLocked: fail to open database for " + e.getMessage());
            e.printStackTrace();

            mDatabase = db;
            return db;
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase) {
                db.close();
            }
        }
    }

    /**
     * Close any open database object.
     */
    @Override
    public synchronized void close() {
        if (mIsInitializing) throw new IllegalStateException("Closed during initialization");

        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    private SQLiteDatabase createOrOpenDatabase() throws IOException {
        SQLiteDatabase db = openDatabase();
        if (db != null) {
            // database already exists
            int version = db.getVersion();
            if (version < mNewVersion) {
                copyDatabaseFromAssets();
                db = openDatabase();
            }
        } else {
            // database does not exist, copy it from assets and return it
            copyDatabaseFromAssets();
            db = openDatabase();
        }

        return db;
    }

    private SQLiteDatabase openDatabase(){
        try {
            final String path = mContext.getDatabasePath(mName).getPath();
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READWRITE);
            Log.i(TAG, "successfully opened database " + mName);
            return db;
        } catch (SQLiteException e) {
            Log.w(TAG, "could not open database " + mName + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     */
    private void copyDatabaseFromAssets() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(mName);

        // create database directory if not exist.
        String databaseDirPath = mContext.getApplicationInfo().dataDir + "/databases/";
        File databaseDir = new File(databaseDirPath);
        if (!databaseDir.exists()) {
            Log.d(TAG, "copyDatabaseFromAssets: make dir.");
            databaseDir.mkdir();
        }

        // Path to the just created empty db
        final String outFileName = mContext.getDatabasePath(mName).getPath();

        // Open the empty db as the output stream
        Log.d(TAG, "copyDatabaseFromAssets: db path " + outFileName);
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private static DbHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DbHelper.class) {
                if (mInstance == null) {
                    mInstance = new DbHelper(context);
                }
            }
        }

        return mInstance;
    }

    /**
     * 外部类使用该方法从DatabaseHelper的唯一的实例获取到SQLiteDatabase对象
     * 然后对数据库进行操作，操作完成后关闭
     * */
    public static SQLiteDatabase getSQLiteDb(Context context) {
        return getInstance(context).getWritableDatabase();
    }

}
