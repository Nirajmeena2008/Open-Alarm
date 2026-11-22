package com.alarmapp.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.alarmapp.data.entity.PluginChallengeEntity;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PluginChallengeDao_Impl implements PluginChallengeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PluginChallengeEntity> __insertionAdapterOfPluginChallengeEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeletePluginById;

  public PluginChallengeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPluginChallengeEntity = new EntityInsertionAdapter<PluginChallengeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `plugin_challenges` (`pluginId`,`packageName`,`title`,`description`,`schemaJson`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PluginChallengeEntity entity) {
        if (entity.getPluginId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPluginId());
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPackageName());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        if (entity.getSchemaJson() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSchemaJson());
        }
      }
    };
    this.__preparedStmtOfDeletePluginById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM plugin_challenges WHERE pluginId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertPlugin(final PluginChallengeEntity plugin,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPluginChallengeEntity.insert(plugin);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePluginById(final String pluginId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePluginById.acquire();
        int _argIndex = 1;
        if (pluginId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, pluginId);
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
          __preparedStmtOfDeletePluginById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PluginChallengeEntity>> getInstalledPlugins() {
    final String _sql = "SELECT * FROM plugin_challenges ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plugin_challenges"}, new Callable<List<PluginChallengeEntity>>() {
      @Override
      @NonNull
      public List<PluginChallengeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPluginId = CursorUtil.getColumnIndexOrThrow(_cursor, "pluginId");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSchemaJson = CursorUtil.getColumnIndexOrThrow(_cursor, "schemaJson");
          final List<PluginChallengeEntity> _result = new ArrayList<PluginChallengeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PluginChallengeEntity _item;
            final String _tmpPluginId;
            if (_cursor.isNull(_cursorIndexOfPluginId)) {
              _tmpPluginId = null;
            } else {
              _tmpPluginId = _cursor.getString(_cursorIndexOfPluginId);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpSchemaJson;
            if (_cursor.isNull(_cursorIndexOfSchemaJson)) {
              _tmpSchemaJson = null;
            } else {
              _tmpSchemaJson = _cursor.getString(_cursorIndexOfSchemaJson);
            }
            _item = new PluginChallengeEntity(_tmpPluginId,_tmpPackageName,_tmpTitle,_tmpDescription,_tmpSchemaJson);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
