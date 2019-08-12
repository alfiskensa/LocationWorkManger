package com.pratikbutani.workerexample.data;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.pratikbutani.workerexample.model.LocationHistory;
import com.pratikbutani.workerexample.util.DateTypeConverter;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public final class LocationDao_Impl implements LocationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfLocationHistory;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public LocationDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocationHistory = new EntityInsertionAdapter<LocationHistory>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `locationTbl`(`id`,`longitude`,`latitude`,`date`,`deviceInfo`,`employeeId`,`checkpointId`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LocationHistory value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindDouble(2, value.getLongitude());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindDouble(3, value.getLatitude());
        }
        final Long _tmp;
        _tmp = DateTypeConverter.toLong(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getDeviceInfo() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDeviceInfo());
        }
        if (value.getEmployeeId() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getEmployeeId());
        }
        if (value.checkpointId == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.checkpointId);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM locationTbl";
        return _query;
      }
    };
  }

  @Override
  public void insert(final LocationHistory locationHistory) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfLocationHistory.insert(locationHistory);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public LocationHistory selectLast() {
    final String _sql = "SELECT * FROM locationTbl where date in (SELECT max(date) FROM locationTbl)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDeviceInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceInfo");
      final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
      final int _cursorIndexOfCheckpointId = CursorUtil.getColumnIndexOrThrow(_cursor, "checkpointId");
      final LocationHistory _result;
      if(_cursor.moveToFirst()) {
        _result = new LocationHistory();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = DateTypeConverter.toDate(_tmp);
        _result.setDate(_tmpDate);
        final String _tmpDeviceInfo;
        _tmpDeviceInfo = _cursor.getString(_cursorIndexOfDeviceInfo);
        _result.setDeviceInfo(_tmpDeviceInfo);
        final Integer _tmpEmployeeId;
        if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
          _tmpEmployeeId = null;
        } else {
          _tmpEmployeeId = _cursor.getInt(_cursorIndexOfEmployeeId);
        }
        _result.setEmployeeId(_tmpEmployeeId);
        if (_cursor.isNull(_cursorIndexOfCheckpointId)) {
          _result.checkpointId = null;
        } else {
          _result.checkpointId = _cursor.getInt(_cursorIndexOfCheckpointId);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<LocationHistory> selectAll() {
    final String _sql = "SELECT * FROM locationTbl";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDeviceInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceInfo");
      final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
      final int _cursorIndexOfCheckpointId = CursorUtil.getColumnIndexOrThrow(_cursor, "checkpointId");
      final List<LocationHistory> _result = new ArrayList<LocationHistory>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LocationHistory _item;
        _item = new LocationHistory();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = DateTypeConverter.toDate(_tmp);
        _item.setDate(_tmpDate);
        final String _tmpDeviceInfo;
        _tmpDeviceInfo = _cursor.getString(_cursorIndexOfDeviceInfo);
        _item.setDeviceInfo(_tmpDeviceInfo);
        final Integer _tmpEmployeeId;
        if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
          _tmpEmployeeId = null;
        } else {
          _tmpEmployeeId = _cursor.getInt(_cursorIndexOfEmployeeId);
        }
        _item.setEmployeeId(_tmpEmployeeId);
        if (_cursor.isNull(_cursorIndexOfCheckpointId)) {
          _item.checkpointId = null;
        } else {
          _item.checkpointId = _cursor.getInt(_cursorIndexOfCheckpointId);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
