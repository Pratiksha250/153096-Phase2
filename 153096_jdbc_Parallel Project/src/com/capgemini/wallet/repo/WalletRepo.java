package com.capgemini.wallet.repo;


import com.capgemini.wallet.beans.Customer;
import com.capgemini.wallet.exception.InvalidInputException;

public interface WalletRepo {

	public boolean save(Customer customer) throws InvalidInputException;
	public Customer findOne(String mobileNo) throws InvalidInputException;
	public Customer update(Customer customer) throws InvalidInputException;
	
}
