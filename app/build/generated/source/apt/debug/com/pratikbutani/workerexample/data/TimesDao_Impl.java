package com.pratikbutani.workerexample.data;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.pratikbutani.workerexample.model.Times;
import com.pratikbutani.workerexample.util.DateTypeConverter;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Date;

@SuppressWarnings("unchecked")
public final class TimesDao_Impl implements TimesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTimes;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public TimesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTimes = new EntityInsertionAdapter<Times>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `tbl_times`(`id`,`date`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Times value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        final Long _tmp;
        _tmp = DateTypeConverter.toLong(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM tbl_times";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Times time) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTimes.insert(time);
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
  public Times selectLast() {
    final String _sql = "SELECT * FROM tbl_times LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final Times _result;
      if(_cursor.moveToFirst()) {
        _result = new Times();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = DateTypeConverter.toDate(_tmp);
        _result.setDate(_tmpDate);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
