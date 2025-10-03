package database;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import model.Product;
import java.io.*;

public class ItemDAO {
    private static final List<Product> items = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private static final File DATA_FILE = new File(System.getProperty("user.dir"), "data/items.dat");

    // --- favourites persistence ---
    private static final File FAV_FILE = new File(System.getProperty("user.dir"), "data/favs.dat");
    private static final Set<Integer> favouriteIds = new HashSet<>();

    static {
        loadFromFile();
        loadFavouritesFromFile(); // load favourites after items
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

    // --- favourites API ---
    public static synchronized void addToFavourites(int productId) {
        if (productId <= 0) return;
        if (favouriteIds.add(productId)) saveFavouritesToFile();
    }

    public static synchronized void removeFromFavourites(int productId) {
        if (favouriteIds.remove(productId)) saveFavouritesToFile();
    }

    public static synchronized boolean isFavourite(int productId) {
        return favouriteIds.contains(productId);
    }

    public static synchronized List<Product> getFavouriteProducts() {
        List<Product> out = new ArrayList<>();
        for (Product p : items) {
            if (favouriteIds.contains(p.getId())) out.add(p);
        }
        return out;
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

    // --- favourites persistence helpers ---
    private static void loadFavouritesFromFile() {
        if (!FAV_FILE.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FAV_FILE)))) {
            Object obj = ois.readObject();
            if (obj instanceof Set) {
                @SuppressWarnings("unchecked")
                Set<Integer> loaded = (Set<Integer>) obj;
                favouriteIds.clear();
                favouriteIds.addAll(loaded);
            }
        } catch (Exception e) {
            // ignore and start with empty favourites
            e.printStackTrace();
        }
    }

    private static void saveFavouritesToFile() {
        try {
            File parent = FAV_FILE.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FAV_FILE)))) {
                oos.writeObject(new HashSet<>(favouriteIds));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}