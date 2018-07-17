package com.capgemini.wallet.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.capgemini.wallet.beans.Customer;
import com.capgemini.wallet.beans.Wallet;
import com.capgemini.wallet.exception.InvalidInputException;

public class WalletRepoImpl implements WalletRepo {

	Customer customer;
	Connection connection;

	public WalletRepoImpl() {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "corp123");

		}

		catch (Exception e) {

		}
	}

	@Override
	public boolean save(Customer customer) throws InvalidInputException {
		try {
			String insertQuery = "INSERT INTO CUSTOMER VALUES (?,?,?)";
			PreparedStatement stmt = connection.prepareStatement(insertQuery);
			stmt.setString(1, customer.getMobileNo());
			stmt.setString(2, customer.getName());
			stmt.setBigDecimal(3, customer.getWallet().getBalance());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			System.err.println("Phone number already exists");
		} catch (Exception e) {
			System.out.println("Incorrect input");
		}
		return false;
	}

	@Override
	public Customer findOne(String mobileNo) throws InvalidInputException {
		try {
			String searchQuery = "SELECT * FROM CUSTOMER WHERE MOBILENUMBER=?";
			PreparedStatement pStmt = connection.prepareStatement(searchQuery);
			pStmt.setString(1, mobileNo);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setMobileNo(rs.getString(1));
				customer.setName(rs.getString(2));
				customer.setWallet(new Wallet(rs.getBigDecimal(3)));
				return customer;
			}
		} catch (Exception e) {	
		}
		return null;
	}

	public Customer update(Customer customer) throws InvalidInputException {

		String updateQuery = "update customer set balance=? where mobilenumber=?";

		try {
			PreparedStatement pStmt = connection.prepareStatement(updateQuery);
			pStmt.setBigDecimal(1, customer.getWallet().getBalance());
			pStmt.setString(2, customer.getMobileNo());
			pStmt.executeUpdate();
			return customer;
		} catch (SQLException e) {

		}
		return customer;
	}

	public boolean truncate() {
		try {
			String truncateQuery = "truncate customer";
			Statement pStmt = connection.createStatement();
			pStmt.executeUpdate(truncateQuery);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

}
