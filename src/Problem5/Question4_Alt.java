package Problem5;
// Problem Statement
// An online shopping application allows customers to purchase products from an inventory. Before
// confirming an order, the system must verify whether the requested quantity is available.
// Develop a Java application that uses custom exceptions to manage inventory-related errors.
// Application Requirements
// 1. Create a class Inventory with:
// o productId
// o productName
// o availableQuantity
// 2. Implement the following methods:
// o addStock(int quantity)
// o purchaseProduct(int quantity)
// o displayStock()
// 3. Create the following custom exceptions:
// o InsufficientStockException: When the requested quantity exceeds available
// stock.
// o InvalidQuantityException: When the requested quantity is zero or negative.
// o MaximumStockLimitException: When adding stock causes the total quantity to
// exceed 500.
// 4. Update the inventory only when the transaction is successful.

class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}

class MaximumStockLimitException extends Exception {
    public MaximumStockLimitException(String message) {
        super(message);
    }
}

class Inventory {
    private final int productId;
    private final String productName;
    private int availableQuantity;

    public Inventory(int productId, String productName, int availableQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
    }
    public int getAvailableQuantity() {
    return availableQuantity;
   }

    public void addStock(int quantity)
            throws MaximumStockLimitException, InvalidQuantityException {

        if (quantity <= 0) {
            throw new InvalidQuantityException(
                "Invalid quantity. Enter a positive value."
            );
        }

        if (availableQuantity + quantity > 500) {
            throw new MaximumStockLimitException(
                "Maximum stock limit of 500 exceeded."
            );
        }

        availableQuantity += quantity;
    }

    public void purchaseProduct(int quantity)
            throws InsufficientStockException, InvalidQuantityException {

        if (quantity <= 0) {
            throw new InvalidQuantityException(
                "Invalid quantity. Enter a positive value."
            );
        }

        if (quantity > availableQuantity) {
            throw new InsufficientStockException(
                "Insufficient stock. Available: " + availableQuantity
            );
        }

        availableQuantity -= quantity;
    }

    public void displayStock() {
        System.out.println("Product ID: " + productId);
        System.out.println("Product Name: " + productName);
        System.out.println("Available Quantity: " + availableQuantity);
    }
}


public class Question4_Alt {
    public static void main(String[] args) {

        // TC01
        testPurchase(100, 20);

        // TC02
        testPurchase(100, 100);

        // TC03
        testPurchase(100, 120);

        // TC04
        testPurchase(100, -5);

        // TC05
        testAddStock(100, 50);

        // TC06
        testAddStock(450, 100);

        // TC07
        testAddStock(100, 0);
    }

    private static void testPurchase(int initial, int quantity) {
        Inventory obj = new Inventory(101, "Laptop", initial);

        try {
            obj.purchaseProduct(quantity);
            System.out.println("Remaining stock: " + getStock(obj));

        } catch (InvalidQuantityException | InsufficientStockException e) {
            System.out.println(e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
    }

    private static void testAddStock(int initial, int quantity) {
        Inventory obj = new Inventory(101, "Laptop", initial);

        try {
            obj.addStock(quantity);
            System.out.println("Updated stock: " + getStock(obj));

        } catch (InvalidQuantityException | MaximumStockLimitException e) {
            System.out.println(e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
    }

    private static int getStock(Inventory obj) {
        return obj.getAvailableQuantity();
    }
}
