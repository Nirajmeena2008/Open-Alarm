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
import com.alarmapp.data.converter.Converters;
import com.alarmapp.data.entity.WebhookEntity;
import com.alarmapp.domain.model.RetryPolicy;
import com.alarmapp.domain.model.WebhookEvent;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WebhookDao_Impl implements WebhookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WebhookEntity> __insertionAdapterOfWebhookEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteWebhookById;

  public WebhookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWebhookEntity = new EntityInsertionAdapter<WebhookEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `webhooks` (`id`,`url`,`events`,`secret`,`retryPolicy`,`isEnabled`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WebhookEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getUrl() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUrl());
        }
        final String _tmp = __converters.fromWebhookEvents(entity.getEvents());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        if (entity.getSecret() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSecret());
        }
        final String _tmp_1 = __converters.fromRetryPolicy(entity.getRetryPolicy());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final int _tmp_2 = entity.isEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
      }
    };
    this.__preparedStmtOfDeleteWebhookById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM webhooks WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertWebhook(final WebhookEntity webhook, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWebhookEntity.insert(webhook);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteWebhookById(final String id, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteWebhookById.acquire();
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
          __preparedStmtOfDeleteWebhookById.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<WebhookEntity>> getWebhooks() {
    final String _sql = "SELECT * FROM webhooks ORDER BY url ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"webhooks"}, new Callable<List<WebhookEntity>>() {
      @Override
      @NonNull
      public List<WebhookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfEvents = CursorUtil.getColumnIndexOrThrow(_cursor, "events");
          final int _cursorIndexOfSecret = CursorUtil.getColumnIndexOrThrow(_cursor, "secret");
          final int _cursorIndexOfRetryPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "retryPolicy");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final List<WebhookEntity> _result = new ArrayList<WebhookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WebhookEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final Set<WebhookEvent> _tmpEvents;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfEvents)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfEvents);
            }
            _tmpEvents = __converters.toWebhookEvents(_tmp);
            final String _tmpSecret;
            if (_cursor.isNull(_cursorIndexOfSecret)) {
              _tmpSecret = null;
            } else {
              _tmpSecret = _cursor.getString(_cursorIndexOfSecret);
            }
            final RetryPolicy _tmpRetryPolicy;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfRetryPolicy)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfRetryPolicy);
            }
            _tmpRetryPolicy = __converters.toRetryPolicy(_tmp_1);
            final boolean _tmpIsEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp_2 != 0;
            _item = new WebhookEntity(_tmpId,_tmpUrl,_tmpEvents,_tmpSecret,_tmpRetryPolicy,_tmpIsEnabled);
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
