package com.pwc.sdc.recruit.business.finder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by lhuang126 on 7/12/2016.
 */
public class JobFinderDataSource extends JobFinderModel {
    private SQLiteDatabase mDB;
    private Context mContext;

    public JobFinderDataSource(Context context) {
        mContext = context;
        testInitDb();
    }

    @Override
    public void getJobFinderNameCursor(String jobName, JobFinderModel.LoadJobNameCallback callback) {
        if (jobName == null) {
            jobName = "";
        }
        final String tableName = "tb_test";
        final String q = String.format("select * from %s where %s like '%s", new String[]{tableName, JOB_FINDER_NAME, jobName}) + "%'";
        callback.onJobNameLoaded(mDB.rawQuery(q, null));
    }

    @Override
    protected void getJobFinderCursor(String jobFinderName, LoadJobNameCallback callback) {
        if (jobFinderName == null) {
            jobFinderName = "";
        }
        final String tableName = "tb_test";
        final String q = String.format("select * from %s where %s like '%s", new String[]{tableName, JOB_FINDER_NAME, jobFinderName}) + "%'";
        callback.onJobNameLoaded(mDB.rawQuery(q, null));
    }


    @Override
    void testInitDb() {
        mDB = SQLiteDatabase.openOrCreateDatabase(
                mContext.getFilesDir() + "/my.db3", null);
        Cursor cursor = null;
        try {
            final String tableName = "tb_test";
            final String column = "tb_name";
            final String q = String.format("select * from %s where %s like '%s", new String[]{tableName, column, ""}) + "%'";
            if ((cursor = mDB.rawQuery(q, null)).getColumnCount() != 0) {
                return;
            }
            String insertSql = "insert into tb_test values (null,?,?)";

            mDB.execSQL(insertSql, new Object[]{"aaa", 1});

            mDB.execSQL(insertSql, new Object[]{"aab", 2});

            mDB.execSQL(insertSql, new Object[]{"aac", 3});

            mDB.execSQL(insertSql, new Object[]{"aad", 4});

            mDB.execSQL(insertSql, new Object[]{"aae", 5});

            mDB.execSQL(insertSql, new Object[]{"aae", 6});

            mDB.execSQL(insertSql, new Object[]{"aae", 7});

            mDB.execSQL(insertSql, new Object[]{"aae", 8});

            mDB.execSQL(insertSql, new Object[]{"few", 9});

            mDB.execSQL(insertSql, new Object[]{"aae", 10});

            mDB.execSQL(insertSql, new Object[]{"fwa", 11});

            mDB.execSQL(insertSql, new Object[]{"cd", 12});

            mDB.execSQL(insertSql, new Object[]{"aae", 13});

            mDB.execSQL(insertSql, new Object[]{"fg", 14});

            mDB.execSQL(insertSql, new Object[]{"jy", 15});

            mDB.execSQL(insertSql, new Object[]{"aae", 16});

            mDB.execSQL(insertSql, new Object[]{"ku", 17});

            mDB.execSQL(insertSql, new Object[]{"aae", 18});

            mDB.execSQL(insertSql, new Object[]{"li", 19});

            mDB.execSQL(insertSql, new Object[]{"op", 20});

        } catch (Exception e) {

            String sql = "create table tb_test (_id integer primary key autoincrement,tb_name varchar(20),tb_age integer)";

            mDB.execSQL(sql);

            String insertSql = "insert into tb_test values (null,?,?)";

            mDB.execSQL(insertSql, new Object[]{"aaa", 1});

            mDB.execSQL(insertSql, new Object[]{"aab", 2});

            mDB.execSQL(insertSql, new Object[]{"aac", 3});

            mDB.execSQL(insertSql, new Object[]{"aad", 4});

            mDB.execSQL(insertSql, new Object[]{"aae", 5});

            mDB.execSQL(insertSql, new Object[]{"aae", 6});

            mDB.execSQL(insertSql, new Object[]{"aae", 7});

            mDB.execSQL(insertSql, new Object[]{"aae", 8});

            mDB.execSQL(insertSql, new Object[]{"few", 9});

            mDB.execSQL(insertSql, new Object[]{"aae", 10});

            mDB.execSQL(insertSql, new Object[]{"fwa", 11});

            mDB.execSQL(insertSql, new Object[]{"cd", 12});

            mDB.execSQL(insertSql, new Object[]{"aae", 13});

            mDB.execSQL(insertSql, new Object[]{"fg", 14});

            mDB.execSQL(insertSql, new Object[]{"jy", 15});

            mDB.execSQL(insertSql, new Object[]{"aae", 16});

            mDB.execSQL(insertSql, new Object[]{"ku", 17});

            mDB.execSQL(insertSql, new Object[]{"aae", 18});

            mDB.execSQL(insertSql, new Object[]{"li", 19});

            mDB.execSQL(insertSql, new Object[]{"op", 20});
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
