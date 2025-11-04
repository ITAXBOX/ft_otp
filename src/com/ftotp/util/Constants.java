package com.ftotp.util;

// Single source of truth for all constants used throughout the ft_otp application.
// This class centralizes configuration values, cryptographic parameters, and magic numbers
// to improve maintainability and consistency.
public final class Constants {

	// Default key file magic header
	public static final String KEY_FILE_MAGIC = "FTOTP1";

	// Default key file name for storing encrypted seed
	public static final String DEFAULT_KEY_FILENAME = "ft_otp.key";

	// Minimum required length for hexadecimal key in characters
	public static final int MIN_HEX_KEY_LENGTH = 64;

	// Default number of digits in the generated OTP code
	public static final int DEFAULT_OTP_DIGITS = 6;

	// Default time step in seconds for TOTP (time window for validity)
	public static final long DEFAULT_TIMESTEP_SECONDS = 30;

	// Default HMAC algorithm for OTP generation
	public static final String DEFAULT_HMAC_ALGORITHM = "HmacSHA1";

	// Default number of iterations for PBKDF2 key derivation
	public static final int DEFAULT_PBKDF2_ITERATIONS = 150_000;

	// Default AES key size in bytes (256-bit)
	public static final int DEFAULT_AES_KEY_BYTES = 32;

	// Salt length in bytes for PBKDF2
	public static final int SALT_LENGTH_BYTES = 16;

	// Initialization Vector (IV) length in bytes for AES-GCM
	public static final int IV_LENGTH_BYTES = 12;

	// GCM authentication tag length in bits
	public static final int GCM_TAG_LENGTH_BITS = 128;

	// AES cipher mode specification
	public static final String AES_CIPHER_MODE = "AES/GCM/NoPadding";

	// PBKDF2 algorithm specification
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";

	// Counter byte array length for HOTP (64-bit counter)
	public static final int HOTP_COUNTER_BYTES = 8;

	// Bit mask for dynamic truncation offset extraction
	public static final int TRUNCATION_OFFSET_MASK = 0x0f;

	// Bit mask for most significant byte in truncation (removes sign bit)
	public static final int TRUNCATION_MSB_MASK = 0x7f;

	// Bit mask for extracting byte values
	public static final int BYTE_MASK = 0xff;

	// Usage instruction message
	public static final String USAGE_MESSAGE = "Usage: ./ft_otp -g <hex_key_file> | -k <ft_otp.key>";

	// Error message for invalid key length
	public static final String ERROR_KEY_TOO_SHORT = "key must be at least 64 hexadecimal characters.";

	// Error message for mismatched passphrases
	public static final String ERROR_PASSPHRASE_MISMATCH = "passphrases do not match";

	// Error message for invalid key file format
	public static final String ERROR_INVALID_KEY_FILE = "invalid key file format or corrupted data";

	// Prompt for entering passphrase
	public static final String PROMPT_ENTER_PASSPHRASE = "Enter passphrase: ";

	// Prompt for confirming passphrase
	public static final String PROMPT_CONFIRM_PASSPHRASE = "Confirm passphrase: ";

	// Success message template for key generation
	public static final String SUCCESS_KEY_SAVED = "Key was successfully saved in %s.";
}
