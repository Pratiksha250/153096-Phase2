package com.capgemini.wallet.pl;

import java.math.BigDecimal;
import java.util.Scanner;

import com.capgemini.wallet.beans.Customer;
import com.capgemini.wallet.exception.InsufficientBalanceException;
import com.capgemini.wallet.exception.InvalidInputException;
import com.capgemini.wallet.service.WalletService;
import com.capgemini.wallet.service.WalletServiceImpl;

public class Client {

	public void menu() throws InsufficientBalanceException, InvalidInputException {

		WalletService service = new WalletServiceImpl();
		Customer customer;
		int choice;
		String selection;
		Scanner scanner = new Scanner(System.in);

		do {

			System.out.println("\n****************Welcome!!****************");
			System.out.println("\nChoose among the following menu: ");
			System.out.println("  1. Create Account");
			System.out.println("  2. Show Balance");
			System.out.println("  3. Fund Transfer");
			System.out.println("  4. Deposit");
			System.out.println("  5. Withdraw");
			System.out.println("  6. Exit");
			System.out.println("Enter your choice: ");
			BigDecimal amount;
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				try {
					System.out.println("Enter name:(Pascal Covention) ");
					String name = scanner.next();
					System.out.println("Enter phone number: (10 digits)");
					String mobileno = scanner.next();
					System.out.println("Enter amount: ");
					amount = scanner.nextBigDecimal();

					customer = service.createAccount(name, mobileno, amount);
					if (customer == null)
						System.out.println("Could not create account");
					else {
						System.out.println("Your account has been created, having the following details: ");
						System.out.println(customer);
					}
				} catch (InvalidInputException e) {
					System.err.println(e.getMessage());
				}
				// catch(InputMismatchException e) {
				// System.err.println("Incorrect amount");
				// }

				break;

			case 2: // show balance
				try {
					System.out.println("Enter mobile number");
					customer = service.showBalance(scanner.next());
					System.out.println(customer.getWallet());
				} catch (InvalidInputException e) {
					System.err.println(e.getMessage());
				}
				break;

			case 3: // fund transfer
				try {
					System.out.println("Enter sender mobile number");
					String sourceMobileNo = scanner.next();
					System.out.println("Enter reciever mobile number");
					String targetMobileNo = scanner.next();
					System.out.println("Enter amount:");
					amount = scanner.nextBigDecimal();
					customer = service.fundTransfer(sourceMobileNo, targetMobileNo, amount);
					System.out.println("Your updated balance: " + customer.getWallet().getBalance());
				} catch (InvalidInputException e) {
					System.err.println(e.getMessage());
				}
				 catch (InsufficientBalanceException e) {
						System.err.println(e.getMessage());
				}
				break;

			case 4:// deposit
				try {
					System.out.println("Enter mobile phone: ");
					String mobileNo = scanner.next();
					System.out.println("Enter the amount to be deposited: ");
					amount = new BigDecimal(scanner.next());
					customer = service.depositAmount(mobileNo, amount);
					System.out.println("Your updated balance: " + customer.getWallet().getBalance());
				} catch (InvalidInputException e) {
					System.err.println(e.getMessage());
				}

				break;

			case 5: // withdraw
				try {
					System.out.println("Enter mobile phone: ");
					String mobileNo = scanner.next();
					System.out.println("Enter the amount to be withdrawn: ");
					amount = new BigDecimal(scanner.next());
					customer = service.withdrawAmount(mobileNo, amount);
					System.out.println("Your updated balance: " + customer.getWallet().getBalance());

				}
				catch(InsufficientBalanceException e) {
					System.err.println(e.getMessage());
				} catch (InvalidInputException e) {
					System.err.println(e.getMessage());
				}
				break;

			case 6:
				System.out.println("****************Thankyou!****************");
				System.exit(0);

			default:
				System.out.println("Enter correct choice");

			}// switch case ends here
			System.out.println("\nDo you want to continue?(y/n)");
			selection = scanner.next();

		} while (selection.equalsIgnoreCase("y"));

		System.out.println("**********Thankyou!!************");
		scanner.close();
	}

	public static void main(String[] args) throws InvalidInputException, InsufficientBalanceException {
		Client client = new Client();
		client.menu();
	}// main ends here

}
