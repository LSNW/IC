This Java program encrypts and decrypts images and features a GUI.
Requires at least Java 14.

Disclaimer: This project was created out of a personal curiosity for cryptography; I am not a certified cryptographer. For serious encryption/decryption services I would advise searching for professional applications.

Instructions:
1. Download and extract .zip
2. Open 'Identifiers.txt' and write an 'alias/identifier' for each key you would like to generate (one per line).
   The names themselves have no significance to the program, it is simply for you to keep track of whom you send files to and receive from.
   For every 'alias' that doesn't already exist in the app, a key will be created with that same name.
3. Open ImCrypter, and select a file. File extensions may either be .jpg (image) or .ic (encrypted file).
4. Select a corresponding key (identifier) to encrypt/decrypt with.
5. You will be prompted for the password. If you have never edited the key to change its password (see below), it is by default just the single character "i".
6. The bottom left corner will indicate if the process has successfully gone through or if there were any issues.

The keystore is the respository that houses all the keys. If you do not wish to modify the key passwords and want to have this for personal use, then you may not need the following.

Accessing the keystore:
1. Open the 'Keys' file in the root directory using a keystore management app (I recommend Keystore Explorer).
2. Enter the password. The keystore password is "i".
3. You will now be able to interact with the keys. If prompted for key password, and you haven't changed it, it is "i".

NOTE: In order to share files with another user, the two of you must have an identical key (again, the particular name doesn't matter). This means that one of you must first have
the app generate a key, then you must you must access the keystore, and share that key with the other user by some means. This could be by something like e-mail (less secure) or
ideally by direct USB transfer (more secure). They then must include the name of the key, or whatever they rename it to, in 'Identifiers.txt'.
