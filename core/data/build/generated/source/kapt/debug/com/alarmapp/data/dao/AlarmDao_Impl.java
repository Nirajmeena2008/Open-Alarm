package com.alarmapp.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.alarmapp.data.converter.Converters;
import com.alarmapp.data.entity.AlarmEntity;
import com.alarmapp.domain.model.ChallengeConfig;
import com.alarmapp.domain.model.DayOfWeek;
import com.alarmapp.domain.model.DismissType;
import com.alarmapp.domain.model.RingtoneSource;
import com.alarmapp.domain.model.VibrationPattern;
import com.alarmapp.domain.model.VolumeRamp;
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
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.Instant;
import kotlinx.datetime.LocalTime;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AlarmDao_Impl implements AlarmDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AlarmEntity> __insertionAdapterOfAlarmEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<AlarmEntity> __deletionAdapterOfAlarmEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAlarmById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAlarmEnabled;

  private final SharedSQLiteStatement __preparedStmtOfUpdateNextTrigger;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSnoozeState;

  public AlarmDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAlarmEntity = new EntityInsertionAdapter<AlarmEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `alarms` (`id`,`label`,`time`,`isEnabled`,`daysOfWeek`,`ringtone`,`volume`,`volumeRamp`,`vibration`,`dismissType`,`challengeConfig`,`snoozeDurationMinutes`,`maxSnoozeCount`,`currentSnoozeCount`,`isSmartWakeEnabled`,`smartWakeWindowMinutes`,`nextTriggerTime`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AlarmEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getLabel() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getLabel());
        }
        final String _tmp = __converters.fromLocalTime(entity.getTime());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final int _tmp_1 = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        final String _tmp_2 = __converters.fromDayOfWeekSet(entity.getDaysOfWeek());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_2);
        }
        final String _tmp_3 = __converters.fromRingtoneSource(entity.getRingtone());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        statement.bindDouble(7, entity.getVolume());
        final String _tmp_4 = __converters.fromVolumeRamp(entity.getVolumeRamp());
        if (_tmp_4 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_4);
        }
        final String _tmp_5 = __converters.fromVibrationPattern(entity.getVibration());
        if (_tmp_5 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_5);
        }
        final String _tmp_6 = __converters.fromDismissType(entity.getDismissType());
        if (_tmp_6 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_6);
        }
        final String _tmp_7 = __converters.fromChallengeConfig(entity.getChallengeConfig());
        if (_tmp_7 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_7);
        }
        statement.bindLong(12, entity.getSnoozeDurationMinutes());
        statement.bindLong(13, entity.getMaxSnoozeCount());
        statement.bindLong(14, entity.getCurrentSnoozeCount());
        final int _tmp_8 = entity.isSmartWakeEnabled() ? 1 : 0;
        statement.bindLong(15, _tmp_8);
        statement.bindLong(16, entity.getSmartWakeWindowMinutes());
        final Long _tmp_9 = __converters.fromInstant(entity.getNextTriggerTime());
        if (_tmp_9 == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, _tmp_9);
        }
        final Long _tmp_10 = __converters.fromInstant(entity.getCreatedAt());
        if (_tmp_10 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_10);
        }
        final Long _tmp_11 = __converters.fromInstant(entity.getUpdatedAt());
        if (_tmp_11 == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, _tmp_11);
        }
      }
    };
    this.__deletionAdapterOfAlarmEntity = new EntityDeletionOrUpdateAdapter<AlarmEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `alarms` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AlarmEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAlarmById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM alarms WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAlarmEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alarms SET isEnabled = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateNextTrigger = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alarms SET nextTriggerTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSnoozeState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alarms SET currentSnoozeCount = ?, nextTriggerTime = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAlarm(final AlarmEntity alarm, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAlarmEntity.insert(alarm);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAlarm(final AlarmEntity alarm, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAlarmEntity.handle(alarm);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAlarmById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAlarmById.acquire();
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
          __preparedStmtOfDeleteAlarmById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAlarmEnabled(final String id, final boolean isEnabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAlarmEnabled.acquire();
        int _argIndex = 1;
        final int _tmp = isEnabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdateAlarmEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNextTrigger(final String id, final Long nextTrigger,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateNextTrigger.acquire();
        int _argIndex = 1;
        if (nextTrigger == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, nextTrigger);
        }
        _argIndex = 2;
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
          __preparedStmtOfUpdateNextTrigger.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSnoozeState(final String id, final int snoozeCount, final Long nextTrigger,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSnoozeState.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, snoozeCount);
        _argIndex = 2;
        if (nextTrigger == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, nextTrigger);
        }
        _argIndex = 3;
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
          __preparedStmtOfUpdateSnoozeState.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AlarmEntity>> getAllAlarms() {
    final String _sql = "SELECT * FROM alarms ORDER BY time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alarms"}, new Callable<List<AlarmEntity>>() {
      @Override
      @NonNull
      public List<AlarmEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfRingtone = CursorUtil.getColumnIndexOrThrow(_cursor, "ringtone");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfVolumeRamp = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeRamp");
          final int _cursorIndexOfVibration = CursorUtil.getColumnIndexOrThrow(_cursor, "vibration");
          final int _cursorIndexOfDismissType = CursorUtil.getColumnIndexOrThrow(_cursor, "dismissType");
          final int _cursorIndexOfChallengeConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeConfig");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfMaxSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSnoozeCount");
          final int _cursorIndexOfCurrentSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSnoozeCount");
          final int _cursorIndexOfIsSmartWakeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSmartWakeEnabled");
          final int _cursorIndexOfSmartWakeWindowMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "smartWakeWindowMinutes");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AlarmEntity> _result = new ArrayList<AlarmEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AlarmEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final LocalTime _tmpTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTime);
            }
            _tmpTime = __converters.toLocalTime(_tmp);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final Set<DayOfWeek> _tmpDaysOfWeek;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            _tmpDaysOfWeek = __converters.toDayOfWeekSet(_tmp_2);
            final RingtoneSource _tmpRingtone;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfRingtone)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfRingtone);
            }
            _tmpRingtone = __converters.toRingtoneSource(_tmp_3);
            final float _tmpVolume;
            _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
            final VolumeRamp _tmpVolumeRamp;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfVolumeRamp)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfVolumeRamp);
            }
            _tmpVolumeRamp = __converters.toVolumeRamp(_tmp_4);
            final VibrationPattern _tmpVibration;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfVibration)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfVibration);
            }
            _tmpVibration = __converters.toVibrationPattern(_tmp_5);
            final DismissType _tmpDismissType;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDismissType)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDismissType);
            }
            _tmpDismissType = __converters.toDismissType(_tmp_6);
            final ChallengeConfig _tmpChallengeConfig;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfChallengeConfig)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfChallengeConfig);
            }
            _tmpChallengeConfig = __converters.toChallengeConfig(_tmp_7);
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpMaxSnoozeCount;
            _tmpMaxSnoozeCount = _cursor.getInt(_cursorIndexOfMaxSnoozeCount);
            final int _tmpCurrentSnoozeCount;
            _tmpCurrentSnoozeCount = _cursor.getInt(_cursorIndexOfCurrentSnoozeCount);
            final boolean _tmpIsSmartWakeEnabled;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsSmartWakeEnabled);
            _tmpIsSmartWakeEnabled = _tmp_8 != 0;
            final int _tmpSmartWakeWindowMinutes;
            _tmpSmartWakeWindowMinutes = _cursor.getInt(_cursorIndexOfSmartWakeWindowMinutes);
            final Instant _tmpNextTriggerTime;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfNextTriggerTime)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            }
            _tmpNextTriggerTime = __converters.toInstant(_tmp_9);
            final Instant _tmpCreatedAt;
            final Long _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.toInstant(_tmp_10);
            final Instant _tmpUpdatedAt;
            final Long _tmp_11;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.toInstant(_tmp_11);
            _item = new AlarmEntity(_tmpId,_tmpLabel,_tmpTime,_tmpIsEnabled,_tmpDaysOfWeek,_tmpRingtone,_tmpVolume,_tmpVolumeRamp,_tmpVibration,_tmpDismissType,_tmpChallengeConfig,_tmpSnoozeDurationMinutes,_tmpMaxSnoozeCount,_tmpCurrentSnoozeCount,_tmpIsSmartWakeEnabled,_tmpSmartWakeWindowMinutes,_tmpNextTriggerTime,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<AlarmEntity>> getEnabledAlarms() {
    final String _sql = "SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alarms"}, new Callable<List<AlarmEntity>>() {
      @Override
      @NonNull
      public List<AlarmEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfRingtone = CursorUtil.getColumnIndexOrThrow(_cursor, "ringtone");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfVolumeRamp = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeRamp");
          final int _cursorIndexOfVibration = CursorUtil.getColumnIndexOrThrow(_cursor, "vibration");
          final int _cursorIndexOfDismissType = CursorUtil.getColumnIndexOrThrow(_cursor, "dismissType");
          final int _cursorIndexOfChallengeConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeConfig");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfMaxSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSnoozeCount");
          final int _cursorIndexOfCurrentSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSnoozeCount");
          final int _cursorIndexOfIsSmartWakeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSmartWakeEnabled");
          final int _cursorIndexOfSmartWakeWindowMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "smartWakeWindowMinutes");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AlarmEntity> _result = new ArrayList<AlarmEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AlarmEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final LocalTime _tmpTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTime);
            }
            _tmpTime = __converters.toLocalTime(_tmp);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final Set<DayOfWeek> _tmpDaysOfWeek;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            _tmpDaysOfWeek = __converters.toDayOfWeekSet(_tmp_2);
            final RingtoneSource _tmpRingtone;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfRingtone)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfRingtone);
            }
            _tmpRingtone = __converters.toRingtoneSource(_tmp_3);
            final float _tmpVolume;
            _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
            final VolumeRamp _tmpVolumeRamp;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfVolumeRamp)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfVolumeRamp);
            }
            _tmpVolumeRamp = __converters.toVolumeRamp(_tmp_4);
            final VibrationPattern _tmpVibration;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfVibration)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfVibration);
            }
            _tmpVibration = __converters.toVibrationPattern(_tmp_5);
            final DismissType _tmpDismissType;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDismissType)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDismissType);
            }
            _tmpDismissType = __converters.toDismissType(_tmp_6);
            final ChallengeConfig _tmpChallengeConfig;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfChallengeConfig)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfChallengeConfig);
            }
            _tmpChallengeConfig = __converters.toChallengeConfig(_tmp_7);
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpMaxSnoozeCount;
            _tmpMaxSnoozeCount = _cursor.getInt(_cursorIndexOfMaxSnoozeCount);
            final int _tmpCurrentSnoozeCount;
            _tmpCurrentSnoozeCount = _cursor.getInt(_cursorIndexOfCurrentSnoozeCount);
            final boolean _tmpIsSmartWakeEnabled;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsSmartWakeEnabled);
            _tmpIsSmartWakeEnabled = _tmp_8 != 0;
            final int _tmpSmartWakeWindowMinutes;
            _tmpSmartWakeWindowMinutes = _cursor.getInt(_cursorIndexOfSmartWakeWindowMinutes);
            final Instant _tmpNextTriggerTime;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfNextTriggerTime)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            }
            _tmpNextTriggerTime = __converters.toInstant(_tmp_9);
            final Instant _tmpCreatedAt;
            final Long _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.toInstant(_tmp_10);
            final Instant _tmpUpdatedAt;
            final Long _tmp_11;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.toInstant(_tmp_11);
            _item = new AlarmEntity(_tmpId,_tmpLabel,_tmpTime,_tmpIsEnabled,_tmpDaysOfWeek,_tmpRingtone,_tmpVolume,_tmpVolumeRamp,_tmpVibration,_tmpDismissType,_tmpChallengeConfig,_tmpSnoozeDurationMinutes,_tmpMaxSnoozeCount,_tmpCurrentSnoozeCount,_tmpIsSmartWakeEnabled,_tmpSmartWakeWindowMinutes,_tmpNextTriggerTime,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<AlarmEntity> getAlarmById(final String id) {
    final String _sql = "SELECT * FROM alarms WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alarms"}, new Callable<AlarmEntity>() {
      @Override
      @Nullable
      public AlarmEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfRingtone = CursorUtil.getColumnIndexOrThrow(_cursor, "ringtone");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfVolumeRamp = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeRamp");
          final int _cursorIndexOfVibration = CursorUtil.getColumnIndexOrThrow(_cursor, "vibration");
          final int _cursorIndexOfDismissType = CursorUtil.getColumnIndexOrThrow(_cursor, "dismissType");
          final int _cursorIndexOfChallengeConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeConfig");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfMaxSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSnoozeCount");
          final int _cursorIndexOfCurrentSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSnoozeCount");
          final int _cursorIndexOfIsSmartWakeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSmartWakeEnabled");
          final int _cursorIndexOfSmartWakeWindowMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "smartWakeWindowMinutes");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final AlarmEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final LocalTime _tmpTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTime);
            }
            _tmpTime = __converters.toLocalTime(_tmp);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final Set<DayOfWeek> _tmpDaysOfWeek;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            _tmpDaysOfWeek = __converters.toDayOfWeekSet(_tmp_2);
            final RingtoneSource _tmpRingtone;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfRingtone)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfRingtone);
            }
            _tmpRingtone = __converters.toRingtoneSource(_tmp_3);
            final float _tmpVolume;
            _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
            final VolumeRamp _tmpVolumeRamp;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfVolumeRamp)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfVolumeRamp);
            }
            _tmpVolumeRamp = __converters.toVolumeRamp(_tmp_4);
            final VibrationPattern _tmpVibration;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfVibration)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfVibration);
            }
            _tmpVibration = __converters.toVibrationPattern(_tmp_5);
            final DismissType _tmpDismissType;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDismissType)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDismissType);
            }
            _tmpDismissType = __converters.toDismissType(_tmp_6);
            final ChallengeConfig _tmpChallengeConfig;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfChallengeConfig)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfChallengeConfig);
            }
            _tmpChallengeConfig = __converters.toChallengeConfig(_tmp_7);
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpMaxSnoozeCount;
            _tmpMaxSnoozeCount = _cursor.getInt(_cursorIndexOfMaxSnoozeCount);
            final int _tmpCurrentSnoozeCount;
            _tmpCurrentSnoozeCount = _cursor.getInt(_cursorIndexOfCurrentSnoozeCount);
            final boolean _tmpIsSmartWakeEnabled;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsSmartWakeEnabled);
            _tmpIsSmartWakeEnabled = _tmp_8 != 0;
            final int _tmpSmartWakeWindowMinutes;
            _tmpSmartWakeWindowMinutes = _cursor.getInt(_cursorIndexOfSmartWakeWindowMinutes);
            final Instant _tmpNextTriggerTime;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfNextTriggerTime)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            }
            _tmpNextTriggerTime = __converters.toInstant(_tmp_9);
            final Instant _tmpCreatedAt;
            final Long _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.toInstant(_tmp_10);
            final Instant _tmpUpdatedAt;
            final Long _tmp_11;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.toInstant(_tmp_11);
            _result = new AlarmEntity(_tmpId,_tmpLabel,_tmpTime,_tmpIsEnabled,_tmpDaysOfWeek,_tmpRingtone,_tmpVolume,_tmpVolumeRamp,_tmpVibration,_tmpDismissType,_tmpChallengeConfig,_tmpSnoozeDurationMinutes,_tmpMaxSnoozeCount,_tmpCurrentSnoozeCount,_tmpIsSmartWakeEnabled,_tmpSmartWakeWindowMinutes,_tmpNextTriggerTime,_tmpCreatedAt,_tmpUpdatedAt);
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

  @Override
  public Object getAlarm(final String id, final Continuation<? super AlarmEntity> $completion) {
    final String _sql = "SELECT * FROM alarms WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AlarmEntity>() {
      @Override
      @Nullable
      public AlarmEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeek");
          final int _cursorIndexOfRingtone = CursorUtil.getColumnIndexOrThrow(_cursor, "ringtone");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfVolumeRamp = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeRamp");
          final int _cursorIndexOfVibration = CursorUtil.getColumnIndexOrThrow(_cursor, "vibration");
          final int _cursorIndexOfDismissType = CursorUtil.getColumnIndexOrThrow(_cursor, "dismissType");
          final int _cursorIndexOfChallengeConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeConfig");
          final int _cursorIndexOfSnoozeDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozeDurationMinutes");
          final int _cursorIndexOfMaxSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSnoozeCount");
          final int _cursorIndexOfCurrentSnoozeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentSnoozeCount");
          final int _cursorIndexOfIsSmartWakeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSmartWakeEnabled");
          final int _cursorIndexOfSmartWakeWindowMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "smartWakeWindowMinutes");
          final int _cursorIndexOfNextTriggerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTriggerTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final AlarmEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final LocalTime _tmpTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTime);
            }
            _tmpTime = __converters.toLocalTime(_tmp);
            final boolean _tmpIsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_1 != 0;
            final Set<DayOfWeek> _tmpDaysOfWeek;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            _tmpDaysOfWeek = __converters.toDayOfWeekSet(_tmp_2);
            final RingtoneSource _tmpRingtone;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfRingtone)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfRingtone);
            }
            _tmpRingtone = __converters.toRingtoneSource(_tmp_3);
            final float _tmpVolume;
            _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
            final VolumeRamp _tmpVolumeRamp;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfVolumeRamp)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfVolumeRamp);
            }
            _tmpVolumeRamp = __converters.toVolumeRamp(_tmp_4);
            final VibrationPattern _tmpVibration;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfVibration)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfVibration);
            }
            _tmpVibration = __converters.toVibrationPattern(_tmp_5);
            final DismissType _tmpDismissType;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfDismissType)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfDismissType);
            }
            _tmpDismissType = __converters.toDismissType(_tmp_6);
            final ChallengeConfig _tmpChallengeConfig;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfChallengeConfig)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfChallengeConfig);
            }
            _tmpChallengeConfig = __converters.toChallengeConfig(_tmp_7);
            final int _tmpSnoozeDurationMinutes;
            _tmpSnoozeDurationMinutes = _cursor.getInt(_cursorIndexOfSnoozeDurationMinutes);
            final int _tmpMaxSnoozeCount;
            _tmpMaxSnoozeCount = _cursor.getInt(_cursorIndexOfMaxSnoozeCount);
            final int _tmpCurrentSnoozeCount;
            _tmpCurrentSnoozeCount = _cursor.getInt(_cursorIndexOfCurrentSnoozeCount);
            final boolean _tmpIsSmartWakeEnabled;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsSmartWakeEnabled);
            _tmpIsSmartWakeEnabled = _tmp_8 != 0;
            final int _tmpSmartWakeWindowMinutes;
            _tmpSmartWakeWindowMinutes = _cursor.getInt(_cursorIndexOfSmartWakeWindowMinutes);
            final Instant _tmpNextTriggerTime;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfNextTriggerTime)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfNextTriggerTime);
            }
            _tmpNextTriggerTime = __converters.toInstant(_tmp_9);
            final Instant _tmpCreatedAt;
            final Long _tmp_10;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.toInstant(_tmp_10);
            final Instant _tmpUpdatedAt;
            final Long _tmp_11;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.toInstant(_tmp_11);
            _result = new AlarmEntity(_tmpId,_tmpLabel,_tmpTime,_tmpIsEnabled,_tmpDaysOfWeek,_tmpRingtone,_tmpVolume,_tmpVolumeRamp,_tmpVibration,_tmpDismissType,_tmpChallengeConfig,_tmpSnoozeDurationMinutes,_tmpMaxSnoozeCount,_tmpCurrentSnoozeCount,_tmpIsSmartWakeEnabled,_tmpSmartWakeWindowMinutes,_tmpNextTriggerTime,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
