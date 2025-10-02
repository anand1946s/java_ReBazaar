package database;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import model.Product;

public class ItemDAO {
    private static final List<Product> items = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public static synchronized void addProduct(Product p) {
        if (p == null) return;
        p.setId(idCounter.getAndIncrement());
        items.add(p);
    }

    public static synchronized List<Product> getAllProducts() {
        return new ArrayList<>(items);
    }
}