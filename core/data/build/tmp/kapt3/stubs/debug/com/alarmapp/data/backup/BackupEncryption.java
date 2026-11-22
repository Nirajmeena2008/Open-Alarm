package com.alarmapp.data.backup;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0004J\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/alarmapp/data/backup/BackupEncryption;", "", "()V", "ALGORITHM", "", "ITERATION_COUNT", "", "IV_LENGTH", "KEY_DERIVATION_ALGORITHM", "KEY_LENGTH", "SALT_LENGTH", "TAG_LENGTH_BITS", "decrypt", "", "encryptedData", "passphrase", "encrypt", "plaintext", "data_debug"})
public final class BackupEncryption {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ALGORITHM = "AES/GCM/NoPadding";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;
    @org.jetbrains.annotations.NotNull()
    public static final com.alarmapp.data.backup.BackupEncryption INSTANCE = null;
    
    private BackupEncryption() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] encrypt(@org.jetbrains.annotations.NotNull()
    byte[] plaintext, @org.jetbrains.annotations.NotNull()
    java.lang.String passphrase) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final byte[] decrypt(@org.jetbrains.annotations.NotNull()
    byte[] encryptedData, @org.jetbrains.annotations.NotNull()
    java.lang.String passphrase) {
        return null;
    }
}