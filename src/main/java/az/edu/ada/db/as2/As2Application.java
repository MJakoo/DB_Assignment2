package az.edu.ada.db.as2;

import az.edu.ada.db.as2.menu.AdminMenu;
import az.edu.ada.db.as2.menu.CustomerMenu;
import az.edu.ada.db.as2.operation.*;

import java.sql.*;
import java.util.Scanner;

public class As2Application {
	private static final Scanner scanner = new Scanner(System.in);
	private static final AuthorOperations authorOps = new AuthorOperations();
	private static final BookOperations bookOps = new BookOperations();
	private static final CustomerOperations customerOps = new CustomerOperations();
	private static final OrderOperations orderOps = new OrderOperations();
	private static final OrderDetailOperations orderDetailOps = new OrderDetailOperations();

	public static void main(String[] args) throws SQLException {
		int choice;
		//  Menu
		do {
			System.out.println("\n=== Database Operations Menu ===");
			System.out.println("1. Admin Menu");
			System.out.println("2. Customer Menu");
			System.out.println("0. Exit");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();

			switch (choice) {
				case 1:
					adminMenu();
					break;
				case 2:
					customerMenu();
					break;
				case 0:
					System.out.println("Exiting the program.");
					break;
				default:
					System.out.println("Invalid choice. Please enter a number between 0 and 5.");
			}
		} while (choice != 0);
	}

	private static void adminMenu() throws SQLException {
		System.out.println("\n=== Admin Menu ===");
		System.out.println("1. Author Operations");
		System.out.println("2. Book Operation");
		System.out.println("0. Exit");
		System.out.print("Enter your choice: ");
		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
			case 1:
				AdminMenu.AuthorOperations();
				break;
			case 2:
				AdminMenu.BookOperations();
				break;
			case 0:
				System.out.println("Exiting the program.");
				return;
			default:
				System.out.println("Invalid choice. Please enter a valid number.");
		}
	}

	private static void customerMenu() throws SQLException {
		System.out.println("\n=<=<= Customer Menu =>=>=");
		System.out.println("1. Customer Operations");
		System.out.println("2. Order Operation");
		System.out.println("0. Exit");
		System.out.print("Enter your choice: ");
		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
			case 1:
				CustomerMenu.CustomerOperations();
				break;
			case 2:
				CustomerMenu.OrderOperations();
				break;
			case 0:
				System.out.println("Exiting the program.");
				return;
			default:
				System.out.println("Invalid choice. Please enter a valid number.");
		}
	}
}

