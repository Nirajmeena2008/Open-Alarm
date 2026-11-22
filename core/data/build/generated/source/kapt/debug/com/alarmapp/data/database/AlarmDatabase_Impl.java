package com.alarmapp.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.alarmapp.data.dao.AlarmDao;
import com.alarmapp.data.dao.AlarmDao_Impl;
import com.alarmapp.data.dao.PluginChallengeDao;
import com.alarmapp.data.dao.PluginChallengeDao_Impl;
import com.alarmapp.data.dao.RoutineDao;
import com.alarmapp.data.dao.RoutineDao_Impl;
import com.alarmapp.data.dao.SettingsDao;
import com.alarmapp.data.dao.SettingsDao_Impl;
import com.alarmapp.data.dao.SleepSessionDao;
import com.alarmapp.data.dao.SleepSessionDao_Impl;
import com.alarmapp.data.dao.WebhookDao;
import com.alarmapp.data.dao.WebhookDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AlarmDatabase_Impl extends AlarmDatabase {
  private volatile AlarmDao _alarmDao;

  private volatile SleepSessionDao _sleepSessionDao;

  private volatile RoutineDao _routineDao;

  private volatile SettingsDao _settingsDao;

  private volatile WebhookDao _webhookDao;

  private volatile PluginChallengeDao _pluginChallengeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `alarms` (`id` TEXT NOT NULL, `label` TEXT NOT NULL, `time` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, `daysOfWeek` TEXT NOT NULL, `ringtone` TEXT NOT NULL, `volume` REAL NOT NULL, `volumeRamp` TEXT, `vibration` TEXT NOT NULL, `dismissType` TEXT NOT NULL, `challengeConfig` TEXT, `snoozeDurationMinutes` INTEGER NOT NULL, `maxSnoozeCount` INTEGER NOT NULL, `currentSnoozeCount` INTEGER NOT NULL, `isSmartWakeEnabled` INTEGER NOT NULL, `smartWakeWindowMinutes` INTEGER NOT NULL, `nextTriggerTime` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sleep_sessions` (`id` TEXT NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER, `quality` TEXT NOT NULL, `stages` TEXT NOT NULL, `notes` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `routines` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `steps` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `settings` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY(`key`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `webhooks` (`id` TEXT NOT NULL, `url` TEXT NOT NULL, `events` TEXT NOT NULL, `secret` TEXT, `retryPolicy` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `plugin_challenges` (`pluginId` TEXT NOT NULL, `packageName` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `schemaJson` TEXT NOT NULL, PRIMARY KEY(`pluginId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '45fcaf051c9d89e870855ab91163913f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `alarms`");
        db.execSQL("DROP TABLE IF EXISTS `sleep_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `routines`");
        db.execSQL("DROP TABLE IF EXISTS `settings`");
        db.execSQL("DROP TABLE IF EXISTS `webhooks`");
        db.execSQL("DROP TABLE IF EXISTS `plugin_challenges`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAlarms = new HashMap<String, TableInfo.Column>(19);
        _columnsAlarms.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("daysOfWeek", new TableInfo.Column("daysOfWeek", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("ringtone", new TableInfo.Column("ringtone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("volume", new TableInfo.Column("volume", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("volumeRamp", new TableInfo.Column("volumeRamp", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("vibration", new TableInfo.Column("vibration", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("dismissType", new TableInfo.Column("dismissType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("challengeConfig", new TableInfo.Column("challengeConfig", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("snoozeDurationMinutes", new TableInfo.Column("snoozeDurationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("maxSnoozeCount", new TableInfo.Column("maxSnoozeCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("currentSnoozeCount", new TableInfo.Column("currentSnoozeCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("isSmartWakeEnabled", new TableInfo.Column("isSmartWakeEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("smartWakeWindowMinutes", new TableInfo.Column("smartWakeWindowMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("nextTriggerTime", new TableInfo.Column("nextTriggerTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlarms.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlarms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlarms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlarms = new TableInfo("alarms", _columnsAlarms, _foreignKeysAlarms, _indicesAlarms);
        final TableInfo _existingAlarms = TableInfo.read(db, "alarms");
        if (!_infoAlarms.equals(_existingAlarms)) {
          return new RoomOpenHelper.ValidationResult(false, "alarms(com.alarmapp.data.entity.AlarmEntity).\n"
                  + " Expected:\n" + _infoAlarms + "\n"
                  + " Found:\n" + _existingAlarms);
        }
        final HashMap<String, TableInfo.Column> _columnsSleepSessions = new HashMap<String, TableInfo.Column>(6);
        _columnsSleepSessions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSleepSessions.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSleepSessions.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSleepSessions.put("quality", new TableInfo.Column("quality", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSleepSessions.put("stages", new TableInfo.Column("stages", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSleepSessions.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSleepSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSleepSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSleepSessions = new TableInfo("sleep_sessions", _columnsSleepSessions, _foreignKeysSleepSessions, _indicesSleepSessions);
        final TableInfo _existingSleepSessions = TableInfo.read(db, "sleep_sessions");
        if (!_infoSleepSessions.equals(_existingSleepSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "sleep_sessions(com.alarmapp.data.entity.SleepSessionEntity).\n"
                  + " Expected:\n" + _infoSleepSessions + "\n"
                  + " Found:\n" + _existingSleepSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsRoutines = new HashMap<String, TableInfo.Column>(4);
        _columnsRoutines.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoutines.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoutines.put("steps", new TableInfo.Column("steps", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoutines.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoutines = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoutines = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoutines = new TableInfo("routines", _columnsRoutines, _foreignKeysRoutines, _indicesRoutines);
        final TableInfo _existingRoutines = TableInfo.read(db, "routines");
        if (!_infoRoutines.equals(_existingRoutines)) {
          return new RoomOpenHelper.ValidationResult(false, "routines(com.alarmapp.data.entity.RoutineEntity).\n"
                  + " Expected:\n" + _infoRoutines + "\n"
                  + " Found:\n" + _existingRoutines);
        }
        final HashMap<String, TableInfo.Column> _columnsSettings = new HashMap<String, TableInfo.Column>(2);
        _columnsSettings.put("key", new TableInfo.Column("key", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSettings = new TableInfo("settings", _columnsSettings, _foreignKeysSettings, _indicesSettings);
        final TableInfo _existingSettings = TableInfo.read(db, "settings");
        if (!_infoSettings.equals(_existingSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "settings(com.alarmapp.data.entity.SettingEntity).\n"
                  + " Expected:\n" + _infoSettings + "\n"
                  + " Found:\n" + _existingSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsWebhooks = new HashMap<String, TableInfo.Column>(6);
        _columnsWebhooks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWebhooks.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWebhooks.put("events", new TableInfo.Column("events", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWebhooks.put("secret", new TableInfo.Column("secret", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWebhooks.put("retryPolicy", new TableInfo.Column("retryPolicy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWebhooks.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWebhooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWebhooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWebhooks = new TableInfo("webhooks", _columnsWebhooks, _foreignKeysWebhooks, _indicesWebhooks);
        final TableInfo _existingWebhooks = TableInfo.read(db, "webhooks");
        if (!_infoWebhooks.equals(_existingWebhooks)) {
          return new RoomOpenHelper.ValidationResult(false, "webhooks(com.alarmapp.data.entity.WebhookEntity).\n"
                  + " Expected:\n" + _infoWebhooks + "\n"
                  + " Found:\n" + _existingWebhooks);
        }
        final HashMap<String, TableInfo.Column> _columnsPluginChallenges = new HashMap<String, TableInfo.Column>(5);
        _columnsPluginChallenges.put("pluginId", new TableInfo.Column("pluginId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginChallenges.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginChallenges.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginChallenges.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginChallenges.put("schemaJson", new TableInfo.Column("schemaJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPluginChallenges = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPluginChallenges = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPluginChallenges = new TableInfo("plugin_challenges", _columnsPluginChallenges, _foreignKeysPluginChallenges, _indicesPluginChallenges);
        final TableInfo _existingPluginChallenges = TableInfo.read(db, "plugin_challenges");
        if (!_infoPluginChallenges.equals(_existingPluginChallenges)) {
          return new RoomOpenHelper.ValidationResult(false, "plugin_challenges(com.alarmapp.data.entity.PluginChallengeEntity).\n"
                  + " Expected:\n" + _infoPluginChallenges + "\n"
                  + " Found:\n" + _existingPluginChallenges);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "45fcaf051c9d89e870855ab91163913f", "29cc693ef6e6d67b31ecb266a4212d00");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "alarms","sleep_sessions","routines","settings","webhooks","plugin_challenges");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `alarms`");
      _db.execSQL("DELETE FROM `sleep_sessions`");
      _db.execSQL("DELETE FROM `routines`");
      _db.execSQL("DELETE FROM `settings`");
      _db.execSQL("DELETE FROM `webhooks`");
      _db.execSQL("DELETE FROM `plugin_challenges`");
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
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AlarmDao.class, AlarmDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SleepSessionDao.class, SleepSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoutineDao.class, RoutineDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SettingsDao.class, SettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WebhookDao.class, WebhookDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PluginChallengeDao.class, PluginChallengeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AlarmDao alarmDao() {
    if (_alarmDao != null) {
      return _alarmDao;
    } else {
      synchronized(this) {
        if(_alarmDao == null) {
          _alarmDao = new AlarmDao_Impl(this);
        }
        return _alarmDao;
      }
    }
  }

  @Override
  public SleepSessionDao sleepSessionDao() {
    if (_sleepSessionDao != null) {
      return _sleepSessionDao;
    } else {
      synchronized(this) {
        if(_sleepSessionDao == null) {
          _sleepSessionDao = new SleepSessionDao_Impl(this);
        }
        return _sleepSessionDao;
      }
    }
  }

  @Override
  public RoutineDao routineDao() {
    if (_routineDao != null) {
      return _routineDao;
    } else {
      synchronized(this) {
        if(_routineDao == null) {
          _routineDao = new RoutineDao_Impl(this);
        }
        return _routineDao;
      }
    }
  }

  @Override
  public SettingsDao settingsDao() {
    if (_settingsDao != null) {
      return _settingsDao;
    } else {
      synchronized(this) {
        if(_settingsDao == null) {
          _settingsDao = new SettingsDao_Impl(this);
        }
        return _settingsDao;
      }
    }
  }

  @Override
  public WebhookDao webhookDao() {
    if (_webhookDao != null) {
      return _webhookDao;
    } else {
      synchronized(this) {
        if(_webhookDao == null) {
          _webhookDao = new WebhookDao_Impl(this);
        }
        return _webhookDao;
      }
    }
  }

  @Override
  public PluginChallengeDao pluginChallengeDao() {
    if (_pluginChallengeDao != null) {
      return _pluginChallengeDao;
    } else {
      synchronized(this) {
        if(_pluginChallengeDao == null) {
          _pluginChallengeDao = new PluginChallengeDao_Impl(this);
        }
        return _pluginChallengeDao;
      }
    }
  }
}
