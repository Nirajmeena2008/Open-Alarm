package com.alarmapp.data.backup

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BackupEncryptionTest {

    @Test
    fun `encrypt and decrypt roundtrip matches original plaintext`() {
        val originalText = "Super secret alarm backup payload with JSON data 123456789"
        val originalBytes = originalText.toByteArray(Charsets.UTF_8)
        val passphrase = "correct-horse-battery-staple-2026"

        val encrypted = BackupEncryption.encrypt(originalBytes, passphrase)
        assertThat(encrypted).isNotEqualTo(originalBytes)

        val decrypted = BackupEncryption.decrypt(encrypted, passphrase)
        val decryptedText = decrypted.toString(Charsets.UTF_8)

        assertThat(decryptedText).isEqualTo(originalText)
    }

    @Test
    fun `decrypt with incorrect passphrase fails`() {
        val originalBytes = "Important alarm data".toByteArray(Charsets.UTF_8)
        val encrypted = BackupEncryption.encrypt(originalBytes, "valid-password")

        assertThrows<Exception> {
            BackupEncryption.decrypt(encrypted, "wrong-password")
        }
    }

    @Test
    fun `decrypt with truncated data throws IllegalArgumentException`() {
        val corruptData = ByteArray(10) // Less than minimum header size
        assertThrows<IllegalArgumentException> {
            BackupEncryption.decrypt(corruptData, "any-password")
        }
    }
}
