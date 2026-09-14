package Problem2;

import java.util.Scanner;

public class OOPs_Problem {
    
    // Product class
    static class Product {
        String name;
        int price;
        
        Product(String name, int price) {
            this.name = name;
            this.price = price;
        }
    }
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Array to store products
        Product[] products = new Product[n];
        int productCount = 0;
        
        // Array to store results of GET operations
        int[] results = new int[n];
        int resultCount = 0;
        
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split(" ");
            
            String operation = parts[0];
            
            switch (operation) {
                case "ADD" ->  {
                    String name = parts[1];
                    int price = Integer.parseInt(parts[2]);
                    
                    // Check if product already exists
                    boolean exists = false;
                    for (int j = 0; j < productCount; j++) {
                        if (products[j].name.equals(name)) {
                            exists = true;
                            break;
                        }
                    }
                    
                    // Add only if it doesn't exist
                    if (!exists) {
                        products[productCount] = new Product(name, price);
                        productCount++;
                    }
                }
                    
                case "UPDATE" ->  {
                    String updateName = parts[1];
                    int updatePrice = Integer.parseInt(parts[2]);
                    
                    // Find and update the product
                    for (int j = 0; j < productCount; j++) {
                        if (products[j].name.equals(updateName)) {
                            products[j].price = updatePrice;
                            break;
                        }
                    }
                }
                    
                case "GET" ->  {
                    String getName = parts[1];
                    
                    // Find the product and get its price
                    int getPrice = -1;
                    for (int j = 0; j < productCount; j++) {
                        if (products[j].name.equals(getName)) {
                            getPrice = products[j].price;
                            break;
                        }
                    }
                    
                    results[resultCount] = getPrice;
                    resultCount++;
                }
            }
        }
        
        // Print results in array format
        System.out.print("[");
        for (int i = 0; i < resultCount; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(results[i]);
        }
        System.out.println("]");
        
        scanner.close();
    }
}
}