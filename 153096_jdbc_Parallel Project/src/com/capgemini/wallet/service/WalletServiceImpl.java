package com.capgemini.wallet.service;

import java.math.BigDecimal;
import java.util.Scanner;

import com.capgemini.wallet.beans.Customer;
import com.capgemini.wallet.beans.Wallet;
import com.capgemini.wallet.exception.InsufficientBalanceException;
import com.capgemini.wallet.exception.InvalidInputException;
import com.capgemini.wallet.repo.WalletRepo;
import com.capgemini.wallet.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {

	WalletRepo repo = new WalletRepoImpl();
	Customer customer;
	Wallet wallet;

	Scanner scanner = new Scanner(System.in);

	public WalletServiceImpl(WalletRepo repo) {
		this.repo = repo;
	}

	public WalletServiceImpl() {
	}

	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws InvalidInputException {

		wallet = new Wallet(amount);
		customer = new Customer(name, mobileNo, wallet);
		boolean b1 = isValid(customer);

		if (b1) {
			boolean b = repo.save(customer);
			if (b)
				return customer;
			//else
				//throw new InvalidInputException("Could not create account");
		} //else
			//throw new InvalidInputException("Incorrect data");
		return null;
	}

	@Override
	public Customer showBalance(String mobileno) throws InvalidInputException {
		Customer customer = repo.findOne(mobileno);
		if (customer != null) {
			return customer;
		} else
			throw new InvalidInputException("Invalid mobile no ");

	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) throws InvalidInputException, InsufficientBalanceException{
		
		Customer customerSource = repo.findOne(sourceMobileNo);
		Customer customerTarget = repo.findOne(targetMobileNo);
		if(customerSource!=null && customerTarget!=null) {
			int i = customerSource.getWallet().getBalance().compareTo(amount);

			if (i != -1) {
				
				Wallet wallet0 = customerSource.getWallet();
				wallet0.setBalance(wallet0.getBalance().subtract(amount));
				customerSource.setWallet(wallet0);
				customer = repo.update(customerSource);

				Wallet wallet1 = customerTarget.getWallet();
				wallet1.setBalance(wallet1.getBalance().add(amount));
				customerTarget.setWallet(wallet1);
				customer = repo.update(customerTarget);
				
				return customerSource;

			} 
			else 
				throw new InsufficientBalanceException("Balance insufficient");	
		}
		else
			throw new InvalidInputException("Invalid phone number");
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws InvalidInputException {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {

			wallet = customer.getWallet();
			wallet.setBalance(wallet.getBalance().add(amount));

			customer.setWallet(wallet);
			customer = repo.update(customer);
			return customer;
		} else
			throw new InvalidInputException("Invalid mobile no ");

	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws InvalidInputException, InsufficientBalanceException {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {

			wallet = customer.getWallet();
			// amount validation here
			int i = wallet.getBalance().compareTo(amount);
			if (i != -1) {
				wallet.setBalance(wallet.getBalance().subtract(amount));

				customer.setWallet(wallet);
				// boolean b = repo.save(customer);
				customer = repo.update(customer);
				return customer;
			} 
			else {
				throw new InsufficientBalanceException("INSUFFICIENT BALANCE");
			}

			
		} 
		else
			throw new InvalidInputException("Invalid mobile no ");

	}

	public void truncate() {
		boolean b = ((WalletRepoImpl) repo).truncate();
	}
	
	
	@Override
	public boolean isValid(Customer customer) throws InvalidInputException {
		boolean b = false;
		if(isNameValid(customer.getName())	&&	isPhoneNumberValid(customer.getMobileNo())/*	&& isAmountValid(customer.getWallet().getBalance())*/)
			b=true;
		return b;
	}

	
	
	public boolean isPhoneNumberValid(String phone) throws InvalidInputException{
		boolean bool=false;
		if(phone!=null) {
			if(String.valueOf(phone).matches("[1-9][0-9]{9}"))
				bool=true;
			else
				throw new InvalidInputException("Invalid phone number");
		}
		else
			throw new InvalidInputException("Phone number cannot be null");
		return bool;
	}
	

	public boolean isNameValid(String name) throws InvalidInputException{
		boolean bool=false;
		if(name!=null) {
			if(name.matches("[A-Z][a-z]*"))
				bool=true;
			else
				throw new InvalidInputException("Incorrect name");
		}
		else
			throw new InvalidInputException("Name cannot be null");
		return bool;	
			
		
	}
	
//	public boolean isAmountValid(BigDecimal amount)throws InvalidInputException {
//		boolean bool=false;
//		if(amount!=null) {
//			if (amount.toString().matches("[0-9]*"))
//				bool=true;
//			else
//				throw new InvalidInputException("Incorrect amount has been entered");
//		}
//		else
//			throw new InvalidInputException("Amount cannot be null");
//		
//		return false;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
