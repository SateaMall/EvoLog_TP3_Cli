package org.example;

import org.example.model.Product;
import org.example.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Scanner scanner = new Scanner(System.in);
    private static RestTemplate restTemplate = new RestTemplate();
    private static String baseUrl = "http://localhost:8080/api";

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== User Product CLI ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    register();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age. Please enter a number.");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User(name, age, email, password);

        try {
            ResponseEntity<User> response = restTemplate.postForEntity(
                    baseUrl + "/users/register", user, User.class);
            System.out.println("Registration successful! User ID: " + response.getBody().getId());
        } catch (Exception ex) {
            System.out.println("Registration failed: " + ex.getMessage());
        }
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            ResponseEntity<User> response = restTemplate.postForEntity(
                    baseUrl + "/users/login", user, User.class);
            System.out.println("Login successful! Welcome, " + response.getBody().getName());
            userMenu();
        } catch (Exception ex) {
            System.out.println("Login failed: " + ex.getMessage());
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. Display All Products");
            System.out.println("2. Fetch Product by ID");
            System.out.println("3. Add New Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayAllProducts();
                    break;
                case "2":
                    fetchProductById();
                    break;
                case "3":
                    addNewProduct();
                    break;
                case "4":
                    updateProduct();
                    break;
                case "5":
                    deleteProduct();
                    break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void displayAllProducts() {
        try {
            ResponseEntity<Product[]> response = restTemplate.getForEntity(
                    baseUrl + "/products", Product[].class);
            List<Product> products = List.of(response.getBody());
            System.out.println("\n--- All Products ---");
            for (Product product : products) {
                System.out.println("ID: " + product.getId());
                System.out.println("Name: " + product.getName());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Expiration Date: " + product.getExpirationDate());
                System.out.println("-----------------------");
            }
        } catch (Exception ex) {
            System.out.println("Failed to retrieve products: " + ex.getMessage());
        }
    }

    private static void fetchProductById() {
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();

        try {
            ResponseEntity<Product> response = restTemplate.getForEntity(
                    baseUrl + "/products/" + id, Product.class);
            Product product = response.getBody();
            System.out.println("\n--- Product Details ---");
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Expiration Date: " + product.getExpirationDate());
        } catch (Exception ex) {
            System.out.println("Product not found: " + ex.getMessage());
        }
    }

    private static void addNewProduct() {
        System.out.println("\n--- Add New Product ---");
        System.out.print("Product ID: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Please enter a number.");
            return;
        }

        System.out.print("Expiration Date (YYYY-MM-DD): ");
        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        Product product = new Product(id, name, price, expirationDate);

        try {
            ResponseEntity<Product> response = restTemplate.postForEntity(
                    baseUrl + "/products", product, Product.class);
            System.out.println("Product added successfully! Product ID: " + response.getBody().getId());
        } catch (Exception ex) {
            System.out.println("Failed to add product: " + ex.getMessage());
        }
    }

    private static void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        String id = scanner.nextLine();

        System.out.print("New Name: ");
        String name = scanner.nextLine();

        System.out.print("New Price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Please enter a number.");
            return;
        }

        System.out.print("New Expiration Date (YYYY-MM-DD): ");
        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        Product updatedProduct = new Product();
        updatedProduct.setName(name);
        updatedProduct.setPrice(price);
        updatedProduct.setExpirationDate(expirationDate);

        try {
            HttpEntity<Product> requestEntity = new HttpEntity<>(updatedProduct);
            ResponseEntity<Product> response = restTemplate.exchange(
                    baseUrl + "/products/" + id, HttpMethod.PUT, requestEntity, Product.class);
            System.out.println("Product updated successfully!");
        } catch (Exception ex) {
            System.out.println("Failed to update product: " + ex.getMessage());
        }
    }

    private static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        String id = scanner.nextLine();

        try {
            restTemplate.delete(baseUrl + "/products/" + id);
            System.out.println("Product deleted successfully!");
        } catch (Exception ex) {
            System.out.println("Failed to delete product: " + ex.getMessage());
        }
    }
}