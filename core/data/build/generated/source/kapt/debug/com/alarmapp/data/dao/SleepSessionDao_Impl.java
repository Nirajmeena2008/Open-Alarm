package com.alarmapp.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.alarmapp.data.converter.Converters;
import com.alarmapp.data.entity.SleepSessionEntity;
import com.alarmapp.domain.model.SleepQuality;
import com.alarmapp.domain.model.SleepStageRecord;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Instant;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SleepSessionDao_Impl implements SleepSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SleepSessionEntity> __insertionAdapterOfSleepSessionEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteSessionById;

  public SleepSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSleepSessionEntity = new EntityInsertionAdapter<SleepSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sleep_sessions` (`id`,`startTime`,`endTime`,`quality`,`stages`,`notes`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SleepSessionEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        final Long _tmp = __converters.fromInstant(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final Long _tmp_1 = __converters.fromInstant(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp_1);
        }
        final String _tmp_2 = __converters.fromSleepQuality(entity.getQuality());
        if (_tmp_2 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_2);
        }
        final String _tmp_3 = __converters.fromSleepStages(entity.getStages());
        if (_tmp_3 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_3);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
      }
    };
    this.__preparedStmtOfDeleteSessionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sleep_sessions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertSession(final SleepSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSleepSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSessionById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSessionById.acquire();
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSessionById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SleepSessionEntity>> getAllSessions() {
    final String _sql = "SELECT * FROM sleep_sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sleep_sessions"}, new Callable<List<SleepSessionEntity>>() {
      @Override
      @NonNull
      public List<SleepSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfStages = CursorUtil.getColumnIndexOrThrow(_cursor, "stages");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<SleepSessionEntity> _result = new ArrayList<SleepSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SleepSessionEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final Instant _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.toInstant(_tmp);
            final Instant _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.toInstant(_tmp_1);
            final SleepQuality _tmpQuality;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfQuality);
            }
            _tmpQuality = __converters.toSleepQuality(_tmp_2);
            final List<SleepStageRecord> _tmpStages;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfStages)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfStages);
            }
            _tmpStages = __converters.toSleepStages(_tmp_3);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new SleepSessionEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpQuality,_tmpStages,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<SleepSessionEntity> getSession(final String id) {
    final String _sql = "SELECT * FROM sleep_sessions WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sleep_sessions"}, new Callable<SleepSessionEntity>() {
      @Override
      @Nullable
      public SleepSessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfStages = CursorUtil.getColumnIndexOrThrow(_cursor, "stages");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final SleepSessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final Instant _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.toInstant(_tmp);
            final Instant _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.toInstant(_tmp_1);
            final SleepQuality _tmpQuality;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfQuality);
            }
            _tmpQuality = __converters.toSleepQuality(_tmp_2);
            final List<SleepStageRecord> _tmpStages;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfStages)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfStages);
            }
            _tmpStages = __converters.toSleepStages(_tmp_3);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result = new SleepSessionEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpQuality,_tmpStages,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
