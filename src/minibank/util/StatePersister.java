package minibank.util;

import minibank.model.Account;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Object serialization and deserialization utility (Practical 11 Part B).
 */
public class StatePersister {

    /**
     * Serializes an array of Account objects to disk using ObjectOutputStream.
     */
    public static void save(Account[] accounts, Path file) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        try (OutputStream fos = Files.newOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(accounts);
            oos.flush();
        }
    }

    /**
     * Deserializes an array of Account objects from disk using ObjectInputStream.
     */
    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) {
            return new Account[0];
        }
        try (InputStream fis = Files.newInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Account[]) ois.readObject();
        }
    }
}
