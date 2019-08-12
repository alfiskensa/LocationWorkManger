package com.pratikbutani.workerexample.data;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class AppDatabase_Impl extends AppDatabase {
  private volatile LocationDao _locationDao;

  private volatile TimesDao _timesDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `locationTbl` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `longitude` REAL, `latitude` REAL, `date` INTEGER, `deviceInfo` TEXT, `employeeId` INTEGER, `checkpointId` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tbl_times` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `date` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"9b4b58c4cf7109f83ddfc293af7c1591\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `locationTbl`");
        _db.execSQL("DROP TABLE IF EXISTS `tbl_times`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsLocationTbl = new HashMap<String, TableInfo.Column>(7);
        _columnsLocationTbl.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsLocationTbl.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0));
        _columnsLocationTbl.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0));
        _columnsLocationTbl.put("date", new TableInfo.Column("date", "INTEGER", false, 0));
        _columnsLocationTbl.put("deviceInfo", new TableInfo.Column("deviceInfo", "TEXT", false, 0));
        _columnsLocationTbl.put("employeeId", new TableInfo.Column("employeeId", "INTEGER", false, 0));
        _columnsLocationTbl.put("checkpointId", new TableInfo.Column("checkpointId", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocationTbl = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocationTbl = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocationTbl = new TableInfo("locationTbl", _columnsLocationTbl, _foreignKeysLocationTbl, _indicesLocationTbl);
        final TableInfo _existingLocationTbl = TableInfo.read(_db, "locationTbl");
        if (! _infoLocationTbl.equals(_existingLocationTbl)) {
          throw new IllegalStateException("Migration didn't properly handle locationTbl(com.pratikbutani.workerexample.model.LocationHistory).\n"
                  + " Expected:\n" + _infoLocationTbl + "\n"
                  + " Found:\n" + _existingLocationTbl);
        }
        final HashMap<String, TableInfo.Column> _columnsTblTimes = new HashMap<String, TableInfo.Column>(2);
        _columnsTblTimes.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsTblTimes.put("date", new TableInfo.Column("date", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTblTimes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTblTimes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTblTimes = new TableInfo("tbl_times", _columnsTblTimes, _foreignKeysTblTimes, _indicesTblTimes);
        final TableInfo _existingTblTimes = TableInfo.read(_db, "tbl_times");
        if (! _infoTblTimes.equals(_existingTblTimes)) {
          throw new IllegalStateException("Migration didn't properly handle tbl_times(com.pratikbutani.workerexample.model.Times).\n"
                  + " Expected:\n" + _infoTblTimes + "\n"
                  + " Found:\n" + _existingTblTimes);
        }
      }
    }, "9b4b58c4cf7109f83ddfc293af7c1591", "ac0c32ba889c952e88c9da5a69277c00");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "locationTbl","tbl_times");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `locationTbl`");
      _db.execSQL("DELETE FROM `tbl_times`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public LocationDao locationDao() {
    if (_locationDao != null) {
      return _locationDao;
    } else {
      synchronized(this) {
        if(_locationDao == null) {
          _locationDao = new LocationDao_Impl(this);
        }
        return _locationDao;
      }
    }
  }

  @Override
  public TimesDao timesDao() {
    if (_timesDao != null) {
      return _timesDao;
    } else {
      synchronized(this) {
        if(_timesDao == null) {
          _timesDao = new TimesDao_Impl(this);
        }
        return _timesDao;
      }
    }
  }
}
