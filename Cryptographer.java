
import java.io.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;


public class Cryptographer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("This tool will take a '.txt' source file and encrypt it using a secure RSA encryption technique and create a new file called 'encrypted_(source file name) \nThe tool will then decrypt the file and create a new file called 'decrypted_(source file name) where the user may view their encrypted and decrypted files as well as the keys used.");
        
        String sourceFileName = null;
        while (sourceFileName == null) 
        {
            System.out.println("Please enter a txt filename: ");
            sourceFileName = scanner.nextLine();
            sourceFileName = fileExistsAndUpdate(sourceFileName); // Validate source file name

            if (sourceFileName == null) 
            {
                System.out.println("File not found. Please enter a valid txt filename.");
            }
        }
        try {
            makeKeys();
            // Encrypt
            encrypt(sourceFileName, "encrypted_" + sourceFileName);

            // Decrypt
            decrypt(sourceFileName, "decrypted_" + sourceFileName);

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    // Generating and storing keys to files pubkey.dat and privkey.dat
    public static void makeKeys() throws IOException {
        // Generate public, secret, and N keys
        System.out.println("\nGenerating keys");
        BigInteger p, s, N;
        Random rand = new Random();
        BigInteger x = new BigInteger(500, rand).nextProbablePrime();
        BigInteger y = new BigInteger(500, rand).nextProbablePrime();

        // phi = (x-1)(y-1)
        BigInteger phi = (x.subtract(BigInteger.ONE)).multiply(y.subtract(BigInteger.ONE));
        N = x.multiply(y);

        // Generate p where GCD(Phi,p) = 1
        do {
            p = new BigInteger(500,rand).nextProbablePrime();
        } while (!phi.gcd(p).equals(BigInteger.ONE));

        // create s where psMOD(PHI) = 1
        s = p.modInverse(phi);

        // ensure s is between 0 and PHI
        if (s.compareTo(BigInteger.ZERO) < 0) 
        {
            s = s.add(phi);
        } 
        else if (s.compareTo(phi) > 0) 
        {
            s = s.subtract(phi);
        }

        try (ObjectOutputStream pubKeyOut = new ObjectOutputStream(new FileOutputStream("pubkey.dat"));
             ObjectOutputStream privKeyOut = new ObjectOutputStream(new FileOutputStream("privkey.dat"))) {

            pubKeyOut.writeObject(p);
            pubKeyOut.writeObject(N);
            privKeyOut.writeObject(s);
            privKeyOut.writeObject(N);
            /* 
            System.out.println("-----------------------Keys-------------------");
            System.out.println("p: " + p);
            System.out.println("s: " + s);
            System.out.println("N: " + N);
            System.out.println("----------------------------------------------");
            */
        }
    }

    // Encrypt the source file using a Vigenere cipher and RSA
    public static void encrypt(String sourceFileName, String encryptedFileName)
            throws IOException {

        BigInteger p = null;
        BigInteger N = null;

        try 
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("pubkey.dat"));
            p = (BigInteger) ois.readObject();
            N = (BigInteger) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            ObjectOutputStream bigStream = new ObjectOutputStream(new FileOutputStream("Big.dat"));

            // Make random Vigenere key of size 32 bytes
            byte[] vigKey = new byte[32];
            Random rand = new Random();

            // ensure vigKey values are between 0 and 127
            rand.nextBytes(vigKey);
            for (int i = 0; i < vigKey.length; i++) 
            {
                vigKey[i] = (byte) Math.abs(vigKey[i] % 128);
            }

            BigInteger vigKeyInteger = new BigInteger(String.valueOf(BigInteger.ZERO));

            //Change vigKey values using Horners method
            for (int i = 31; i >= 0; i--) 
            { 
                vigKeyInteger = vigKeyInteger.add(BigInteger.valueOf(vigKey[i])).multiply(BigInteger.valueOf(128));
            }
            //Encrypt Big Integer using RSA method
            BigInteger encryptedVigKey = vigKeyInteger.modPow(p, N);

            //Fill Big.dat with encryption key in bigInteger form
            bigStream.writeObject(encryptedVigKey);

            // Encrypt the source file using the encrypted Vigenere key
            int vigKeyIndex = 0;
            int next;
            try (InputStream sourceStream = new FileInputStream(sourceFileName)) {
                OutputStream encryptedStream = new FileOutputStream(encryptedFileName);

                while ((next = sourceStream.read()) != -1) {
                    int encryptedByte = (next + vigKey[vigKeyIndex]) % 128;
                    encryptedStream.write(encryptedByte);
                    vigKeyIndex = (vigKeyIndex + 1) % vigKey.length;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nFile " + sourceFileName + " has been encrypted");
        System.out.println("----------------------------------------------");

    }

        // Decrypt the encrypted file using RSA and the decrypted Vigenere key
    public static void decrypt(String encryptedFileName, String decryptedFileName)
            throws IOException {
        try (ObjectInputStream bigStream = new ObjectInputStream(new FileInputStream("Big.dat"));
             InputStream encryptedData = new FileInputStream(encryptedFileName);
             OutputStream decryptedData = new FileOutputStream(decryptedFileName)) {

            BigInteger s = null;
            BigInteger N = null;

            try 
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("privkey.dat"));
                s = (BigInteger) ois.readObject();
                N = (BigInteger) ois.readObject();

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // Reading in vigKey from file
            BigInteger encryptedVigKey = (BigInteger) bigStream.readObject();
            BigInteger decryptedVigKey = encryptedVigKey.modPow(s, N);
            byte[] vigKey = new byte[32];

            // Reversing Horners method
            for (int i = 0; i < 32; i++) {
                if (i ==0)
                {
                    decryptedVigKey = decryptedVigKey.divide(BigInteger.valueOf(128));
                }
                else {
                    decryptedVigKey = decryptedVigKey.subtract(BigInteger.valueOf(vigKey[i-1])).divide(BigInteger.valueOf(128));
                }
                vigKey[i] = (byte) decryptedVigKey.mod(BigInteger.valueOf(128)).intValue();
            }

            int vigKeyIndex = 0;
            int nextByte;

            while ((nextByte = encryptedData.read()) != -1) 
            {
                int decryptedByte = (nextByte - vigKey[vigKeyIndex]) % 128;
                if (decryptedByte < 0) {
                    decryptedByte += 128;
                }
                //Write to file decrypted message
                decryptedData.write(decryptedByte);
                vigKeyIndex = (vigKeyIndex + 1) % vigKey.length;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
        }
        System.out.println("\nFile " + encryptedFileName + " has been decrypted");
        System.out.println("----------------------------------------------");
    }
    public static String fileExistsAndUpdate(String filename) {
        if (filename != null) {
            if (!filename.endsWith(".txt")) {
                filename = filename + ".txt";
            }
            File file = new File(filename);
            if (file.exists()) {
                return filename; // File exists, return the updated filename
            } else {
                return null; // File does not exist
            }
        } else {
            return null; // Filename is null
        }
    }
}