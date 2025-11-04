package com.ftotp.core;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

import com.ftotp.crypto.KeyFile;
import com.ftotp.crypto.Params;
import com.ftotp.exception.UserException;
import com.ftotp.hotp.HOTP;
import com.ftotp.util.Hex;

// Main class for ft_otp application
// Provides methods to generate a key file and print OTP codes
public class FtOtp {
	// Default key file name
	private static final String DEFAULT_KEYFILE = "ft_otp.key";

	private FtOtp() {

	}
	// Generates a new key file from a hexadecimal seed and a passphrase
	// hexKeyPath: path to the file containing the hexadecimal seed
	public static void generate(String hexKeyPath) throws Exception {
		String hex = Files.readString(Path.of(hexKeyPath)).trim().replaceAll("\\s+", "");
		if (!Hex.isHex(hex) || hex.length() < 64) {
			throw new UserException("key must be at least 64 hexadecimal characters.");
		}
		byte[] seed = Hex.fromHex(hex);

		Console console = System.console();
		char[] pass1, pass2;
		if (console != null) {
			pass1 = console.readPassword("Enter passphrase: ");
			pass2 = console.readPassword("Confirm passphrase: ");
		} else {
			try (Scanner sc = new Scanner(System.in)) {
				System.out.print("Enter passphrase: ");
				pass1 = sc.nextLine().toCharArray();
				System.out.print("Confirm passphrase: ");
				pass2 = sc.nextLine().toCharArray();
			}
		}
		if (!Arrays.equals(pass1, pass2))
			throw new UserException("passphrases do not match");

		KeyFile kf = KeyFile.encrypt(seed, pass1, Params.defaultParams());
		Arrays.fill(seed, (byte) 0);
		Arrays.fill(pass1, '\0');
		Arrays.fill(pass2, '\0');

		Path out = Path.of(DEFAULT_KEYFILE);
		Files.writeString(out, kf.serialize());
		System.out.println("Key was successfully saved in " + DEFAULT_KEYFILE + ".");
	}

	// Prints the current OTP code using the key file and a passphrase
	// keyFilePath: path to the key file
	public static void printOtp(String keyFilePath) throws Exception {
		KeyFile kf = KeyFile.deserialize(Files.readString(Path.of(keyFilePath)));

		Console console = System.console();
		char[] pass;
		if (console != null)
			pass = console.readPassword("Enter passphrase: ");
		else {
			System.out.print("Enter passphrase: ");
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
