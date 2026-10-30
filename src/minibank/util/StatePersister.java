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

public class StatePersister {

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
