package com.alarmapp.data.backup

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object BackupEncryption {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATION_COUNT = 65536
    private const val KEY_LENGTH = 256
    private const val SALT_LENGTH = 16
    private const val IV_LENGTH = 12
    private const val TAG_LENGTH_BITS = 128

    fun encrypt(plaintext: ByteArray, passphrase: String): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH).also { random.nextBytes(it) }
        val iv = ByteArray(IV_LENGTH).also { random.nextBytes(it) }

        val keySpec = PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM)
        val keyBytes = factory.generateSecret(keySpec).encoded
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val cipher = Cipher.getInstance(ALGORITHM)
        val gcmSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)

        val ciphertext = cipher.doFinal(plaintext)

        // Prepend salt + iv to ciphertext
        val output = ByteArray(SALT_LENGTH + IV_LENGTH + ciphertext.size)
        System.arraycopy(salt, 0, output, 0, SALT_LENGTH)
        System.arraycopy(iv, 0, output, SALT_LENGTH, IV_LENGTH)
        System.arraycopy(ciphertext, 0, output, SALT_LENGTH + IV_LENGTH, ciphertext.size)
        return output
    }

    fun decrypt(encryptedData: ByteArray, passphrase: String): ByteArray {
        require(encryptedData.size >= SALT_LENGTH + IV_LENGTH + (TAG_LENGTH_BITS / 8)) {
            "Invalid encrypted payload size"
        }

        val salt = ByteArray(SALT_LENGTH)
        val iv = ByteArray(IV_LENGTH)
        val ciphertext = ByteArray(encryptedData.size - SALT_LENGTH - IV_LENGTH)

        System.arraycopy(encryptedData, 0, salt, 0, SALT_LENGTH)
        System.arraycopy(encryptedData, SALT_LENGTH, iv, 0, IV_LENGTH)
        System.arraycopy(encryptedData, SALT_LENGTH + IV_LENGTH, ciphertext, 0, ciphertext.size)

        val keySpec = PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM)
        val keyBytes = factory.generateSecret(keySpec).encoded
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val cipher = Cipher.getInstance(ALGORITHM)
        val gcmSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

        return cipher.doFinal(ciphertext)
    }
}
