package cn.nongph.components.hash;

import java.io.IOException;
import java.util.zip.CRC32;

/*
 **************************************************************************
 *                                                                        *
 *          General Purpose Hash Function Algorithms Library              *
 *                                                                        *
 * Author: Arash Partow - 2002                                            *
 * URL: http://www.partow.net                                             *
 * URL: http://www.partow.net/programming/hashfunctions/index.html        *
 *                                                                        *
 * Copyright notice:                                                      *
 * Free use of the General Purpose Hash Function Algorithms Library is    *
 * permitted under the guidelines and in accordance with the most current *
 * version of the Common Public License.                                  *
 * http://www.opensource.org/licenses/cpl1.0.php                          *
 *                                                                        *
 **************************************************************************
*/

public class GeneralHashFunctionLibrary {

	public long RSHash(String str) {
		int b = 378551;
		int a = 63689;
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = hash * a + str.charAt(i);
			a = a * b;
		}

		return hash;
	}
	/* End Of RS Hash Function */

	public long JSHash(String str) {
		long hash = 1315423911;

		for (int i = 0; i < str.length(); i++) {
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}

		return hash;
	}
	/* End Of JS Hash Function */

	public long PJWHash(String str) {
		long BitsInUnsignedInt = (long) (4 * 8);
		long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
		long OneEighth = (long) (BitsInUnsignedInt / 8);
		long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
		long hash = 0;
		long test = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << OneEighth) + str.charAt(i);

			if ((test = hash & HighBits) != 0) {
				hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
			}
		}

		return hash;
	}
	/* End Of P. J. Weinberger Hash Function */

	public long ELFHash(String str) {
		long hash = 0;
		long x = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << 4) + str.charAt(i);

			if ((x = hash & 0xF0000000L) != 0) {
				hash ^= (x >> 24);
			}
			hash &= ~x;
		}

		return hash;
	}
	/* End Of ELF Hash Function */

	public long BKDRHash(String str) {
		long seed = 131; // 31 131 1313 13131 131313 etc..
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash * seed) + str.charAt(i);
		}

		return hash;
	}
	/* End Of BKDR Hash Function */

	public long SDBMHash(String str) {
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}

		return hash;
	}
	/* End Of SDBM Hash Function */

	public long DJBHash(String str) {
		long hash = 5381;

		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + hash) + str.charAt(i);
		}

		return hash;
	}
	/* End Of DJB Hash Function */

	public long DEKHash(String str) {
		long hash = str.length();

		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
		}

		return hash;
	}
	/* End Of DEK Hash Function */

	public long BPHash(String str) {
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = hash << 7 ^ str.charAt(i);
		}

		return hash;
	}
	/* End Of BP Hash Function */

	public long FNVHash(String str) {
		long fnv_prime = 0x811C9DC5;
		long hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash *= fnv_prime;
			hash ^= str.charAt(i);
		}

		return hash;
	}
	/* End Of FNV Hash Function */

	public long APHash(String str) {
		long hash = 0xAAAAAAAA;

		for (int i = 0; i < str.length(); i++) {
			if ((i & 1) == 0) {
				hash ^= ((hash << 7) ^ str.charAt(i) * (hash >> 3));
			} else {
				hash ^= (~((hash << 11) + str.charAt(i) ^ (hash >> 5)));
			}
		}

		return hash;
	}
	/* End Of AP Hash Function */
	
	public long Time33Hash(String key) {
		long hash = 0;
		char[] cArr = key.toCharArray();

		for (int i = 0; i < cArr.length; ++i) {
			hash = (hash * 33) + cArr[i];
		}

		return hash;
	}
	
	public long CRC32Hash(String key) {
		CRC32 checksum = new CRC32();
		checksum.update(key.getBytes());
		long crc = checksum.getValue();
		return (crc >> 16) & 0x7fff;
	}

	public static void main(String args[]) throws IOException {

		GeneralHashFunctionLibrary ghl = new GeneralHashFunctionLibrary();

		String key = "abcdefghijklmnopqrstuvwxyz1234567890";

		System.out.println("General Purpose Hash Function Algorithms Test");
		System.out.println("By Arash Partow - 2002\n");
		System.out.println("Key: " + key);
		System.out.println(" 1. RS-Hash Function Value:   " + ghl.RSHash(key));
		System.out.println(" 2. JS-Hash Function Value:   " + ghl.JSHash(key));
		System.out.println(" 3. PJW-Hash Function Value:  " + ghl.PJWHash(key));
		System.out.println(" 4. ELF-Hash Function Value:  " + ghl.ELFHash(key));
		System.out.println(" 5. BKDR-Hash Function Value: " + ghl.BKDRHash(key));
		System.out.println(" 6. SDBM-Hash Function Value: " + ghl.SDBMHash(key));
		System.out.println(" 7. DJB-Hash Function Value:  " + ghl.DJBHash(key));
		System.out.println(" 8. DEK-Hash Function Value:  " + ghl.DEKHash(key));
		System.out.println(" 9. BP-Hash Function Value:   " + ghl.BPHash(key));
		System.out.println(" 9. FNV-Hash Function Value:  " + ghl.FNVHash(key));
		System.out.println("10. AP-Hash Function Value:   " + ghl.APHash(key));
		System.out.println("11. Time33-Hash Function Value:   " + ghl.Time33Hash(key));
		System.out.println("12. CRC32-Hash Function Value:   " + ghl.CRC32Hash(key));
	}
}
