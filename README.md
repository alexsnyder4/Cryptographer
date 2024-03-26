Cryptographer

DISCLAIMER
This tool is only built to process txt files, any other files may have unexpected errors.

Description:
Cryptographer is a Java-based application that provides encryption and decryption functionalities for user-provided txt files. The application implements RSA encryption along with a Vigenere cipher for secure data transmission and ensures data confidentiality.

Features:

Key Generation: Automatically generates public and private keys necessary for encryption and decryption using the RSA algorithm.
File Encryption: Encrypts user-provided files using a combination of RSA encryption and the Vigenere cipher.
File Decryption: Decrypts encrypted files using the corresponding private key and Vigenere cipher.
File I/O Operations: Supports reading from and writing to files for encryption and decryption operations.
Console Output: Provides clear console output messages for encryption and decryption processes, including success or error messages.

How to Use:

Start:

Open a terminal window and navigate to the directory with Cryptographer.java file. Compile using these commands:
    javac Cryptographer.java
    java Cryptographer
The program will execute and propt the user for the next steps.
    
Encrypting and Decrypting a File:

To encrypt and decrypt a file, specify the source file path or name when prompted for a filename. 

Example: 
   "Please enter a txt filename: "
   test.txt or test (file extensions are optional)

Generating Keys:

Upon execution, the application automatically generates public and private keys and stores them in pubkey.dat and privkey.dat files.

Dependencies:

Java Development Kit (JDK)

License:

Cryptographer is licensed under the MIT License. See the LICENSE file for more details.

Acknowledgments:

Cryptographer utilizes cryptographic algorithms provided by the Java standard library.
