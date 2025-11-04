package com.ftotp.core;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

import com.ftotp.crypto.KeyFile;
import com.ftotp.crypto.Params;
import com.ftotp.exception.UserException;
import com.ftotp.hotp.HOTP;
import com.ftotp.util.Constants;
import com.ftotp.util.Hex;

// Main class for ft_otp application
// Provides methods to generate a key file and print OTP codes
public class FtOtp {
	// Default key file name
	private static final String DEFAULT_KEYFILE = Constants.DEFAULT_KEY_FILENAME;

	private FtOtp() {

	}
	// Generates a new key file from a hexadecimal seed and a passphrase
	// hexKeyPath: path to the file containing the hexadecimal seed
	public static void generate(String hexKeyPath) throws Exception {
		String hex = Files.readString(Path.of(hexKeyPath)).trim().replaceAll("\\s+", "");
		if (!Hex.isHex(hex) || hex.length() < Constants.MIN_HEX_KEY_LENGTH) {
			throw new UserException(Constants.ERROR_KEY_TOO_SHORT);
		}
		byte[] seed = Hex.fromHex(hex);

		Console console = System.console();
		char[] pass1, pass2;
		if (console != null) {
			pass1 = console.readPassword(Constants.PROMPT_ENTER_PASSPHRASE);
			pass2 = console.readPassword(Constants.PROMPT_CONFIRM_PASSPHRASE);
		} else {
			try (Scanner sc = new Scanner(System.in)) {
				System.out.print(Constants.PROMPT_ENTER_PASSPHRASE);
				pass1 = sc.nextLine().toCharArray();
				System.out.print(Constants.PROMPT_CONFIRM_PASSPHRASE);
				pass2 = sc.nextLine().toCharArray();
			}
		}
		if (!Arrays.equals(pass1, pass2))
			throw new UserException(Constants.ERROR_PASSPHRASE_MISMATCH);

		KeyFile kf = KeyFile.encrypt(seed, pass1, Params.defaultParams());
		Arrays.fill(seed, (byte) 0);
		Arrays.fill(pass1, '\0');
		Arrays.fill(pass2, '\0');

		Path out = Path.of(DEFAULT_KEYFILE);
		Files.writeString(out, kf.serialize());
		System.out.printf(Constants.SUCCESS_KEY_SAVED + "%n", DEFAULT_KEYFILE);
	}

	// Prints the current OTP code using the key file and a passphrase
	// keyFilePath: path to the key file
	public static void printOtp(String keyFilePath) throws Exception {
		KeyFile kf = KeyFile.deserialize(Files.readString(Path.of(keyFilePath)));

		Console console = System.console();
		char[] pass;
		if (console != null)
			pass = console.readPassword(Constants.PROMPT_ENTER_PASSPHRASE);
		else {
			System.out.print(Constants.PROMPT_ENTER_PASSPHRASE);
			try (Scanner sc = new Scanner(System.in)) {
				pass = sc.nextLine().toCharArray();
			}
		}
		byte[] seed = kf.decrypt(pass);
		Arrays.fill(pass, '\0');

		long timeStep = kf.getParams().getTimestepSeconds();
		long counter = Instant.now().getEpochSecond() / timeStep;
		int digits = kf.getParams().getDigits();
		String algo = kf.getParams().getHmac();

		int code = HOTP.generate(seed, counter, algo);
		Arrays.fill(seed, (byte) 0);

		int mod = (int) Math.pow(10, digits);
		int otp = Math.floorMod(code, mod);
		System.out.printf("%0" + digits + "d%n", otp);
	}
}
