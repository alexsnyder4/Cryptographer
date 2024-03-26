Cryptographer

Description:
Cryptographer is a Java-based command-line application that provides encryption and decryption functionalities for user-provided files. The application implements RSA encryption along with a Vigenere cipher for secure data transmission and ensures data confidentiality.

Features:

Key Generation: Automatically generates public and private keys necessary for encryption and decryption using the RSA algorithm.
File Encryption: Encrypts user-provided files using a combination of RSA encryption and the Vigenere cipher.
File Decryption: Decrypts encrypted files using the corresponding private key and Vigenere cipher.
File I/O Operations: Supports reading from and writing to files for encryption and decryption operations.
Console Output: Provides clear console output messages for encryption and decryption processes, including success or error messages.

How to Use:

Encrypting a File:

To encrypt a file, specify the source file path as the first argument and the desired encrypted file path as the second argument when running the application.

Example: java CryptographyAssign source.txt encrypted.txt

Decrypting a File:

To decrypt an encrypted file, specify the encrypted file path as the first argument and the desired decrypted file path as the second argument when running the application.

Example: java CryptographyAssign encrypted.txt decrypted.txt

Running the Application:

Compile the Java source file (CryptographyAssign.java) using javac CryptographyAssign.java.
Run the compiled class file using java CryptographyAssign.

Generating Keys:

Upon execution, the application automatically generates public and private keys and stores them in pubkey.dat and privkey.dat files.

Dependencies:

Java Development Kit (JDK)

License:
Cryptographer is licensed under the MIT License. See the LICENSE file for more details.

Acknowledgments:

Cryptographer utilizes cryptographic algorithms provided by the Java standard library.
