package database;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import model.Product;
import java.io.*;

public class ItemDAO {
    private static final List<Product> items = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private static final File DATA_FILE = new File(System.getProperty("user.dir"), "data/items.dat");

    static {
        loadFromFile();
    }

    public static synchronized void addProduct(Product p) {
        if (p == null) return;
        p.setId(idCounter.getAndIncrement());
        items.add(p);
        saveToFile();
    }

    public static synchronized List<Product> getAllProducts() {
        return new ArrayList<>(items);
    }

    private static void loadFromFile() {
        if (!DATA_FILE.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(DATA_FILE)))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<?> read = (List<?>) obj;
                items.clear();
                for (Object o : read) {
                    if (o instanceof Product) items.add((Product) o);
                }
                int maxId = items.stream().mapToInt(Product::getId).max().orElse(0);
                idCounter.set(maxId + 1);
            }
        } catch (Exception e) {
            // If loading fails, print stack trace and continue with empty list.
            e.printStackTrace();
        }
    }

    private static void saveToFile() {
        try {
            File parent = DATA_FILE.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(DATA_FILE)))) {
                oos.writeObject(items);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}