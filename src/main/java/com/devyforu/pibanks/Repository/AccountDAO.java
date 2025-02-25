package com.devyforu.pibanks.Repository;

import com.devyforu.pibanks.Model.Account;
import com.devyforu.pibanks.Model.Bank;
import com.devyforu.pibanks.Model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class AccountDAO implements CrudRepository<Account> {

    private Connection connection;
    private BankDAO bankDAO;
    private UserDAO userDAO;

    @Override
    public List<Account> findAll() {
        List<Account> accountList = new ArrayList<>();
        String sql = """
                 SELECT * FROM account;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id_bank = resultSet.getString("id_bank");
                String id_user = resultSet.getString("id_user");
                Bank bank = bankDAO.getById(id_bank);
                User user = userDAO.getById(id_user);
                Account account = new Account(
                        resultSet.getString("id"),
                        resultSet.getString("account_number"),
                        resultSet.getDouble("main_balance"),
                        resultSet.getDouble("loans"),
                        resultSet.getDouble("loans_interest"),
                        resultSet.getDouble("credit_allow"),
                        resultSet.getBoolean("over_draft_limit"),
                        resultSet.getDouble("interest_rate_before_7_days"),
                        resultSet.getDouble("interest_rate_after_7_days"),
                        user,
                        bank
                );
                accountList.add(account);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountList;
    }

    @Override
    public Account save(Account toSave) {
        String sql = """
                INSERT INTO "account" (account_number,id_user,id_bank) VALUES (?,?,?)
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, toSave.getAccountNumber());
            statement.setString(2, toSave.getUser().getId());
            statement.setString(3, toSave.getBank().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public void deleteByAccountNumber(String accountNumber) {
        String sql = """
                DELETE from "account" where account_number = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, accountNumber);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                Account deletedAccount = new Account(accountNumber);
                System.out.println("Account deleted" + deletedAccount);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Account getByAccountNumber(String accountNumber) {
        String sql = """
                Select * from "account" where account_number = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {
                String id_bank = resultSet.getString("id_bank");
                String id_user = resultSet.getString("id_user");
                Bank bank = bankDAO.getById(id_bank);
                User user = userDAO.getById(id_user);
                Account account = new Account();
                account.setId(resultSet.getString("id"));
                account.setAccountNumber(resultSet.getString("account_number"));
                account.setMainBalance(resultSet.getDouble("main_balance"));
                account.setLoans(resultSet.getDouble("loans"));
                account.setInterestLoans(resultSet.getDouble("loans_interest"));
                account.setCreditAllow(resultSet.getDouble("credit_allow"));
                account.setOverDraftLimit(resultSet.getBoolean("over_draft_limit"));
                account.setInterestRateBefore7Days(resultSet.getDouble("interest_rate_before_7_days"));
                account.setInterestRateAfter7Days(resultSet.getDouble("interest_rate_after_7_days"));
                account.setBank(bank);
                account.setUser(user);
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }

    public Account getById(String id) {
        String sql = """
                Select * from "account" where id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id_bank = resultSet.getString("id_bank");
                String id_user = resultSet.getString("id_user");
                Bank bank = bankDAO.getById(id_bank);
                User user = userDAO.getById(id_user);
                Account account = new Account();
                account.setId(resultSet.getString("id"));
                account.setAccountNumber(resultSet.getString("account_number"));
                account.setMainBalance(resultSet.getDouble("main_balance"));
                account.setLoans(resultSet.getDouble("loans"));
                account.setInterestLoans(resultSet.getDouble("loans_interest"));
                account.setCreditAllow(resultSet.getDouble("credit_allow"));
                account.setOverDraftLimit(resultSet.getBoolean("over_draft_limit"));
                account.setInterestRateBefore7Days(resultSet.getDouble("interest_rate_before_7_days"));
                account.setInterestRateAfter7Days(resultSet.getDouble("interest_rate_after_7_days"));
                account.setBank(bank);
                account.setUser(user);
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return null;
    }

    public void updateBalance(Account account) {
        String sql = """
                UPDATE "account" SET main_balance = ? WHERE account_number = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, account.getMainBalance());
            statement.setString(2, account.getAccountNumber());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("The balance of the account has been updated. ");
            }
        } catch (SQLException e) {
            System.out.println("Balance of the account can't be updated : " + e.getMessage());
        }
    }

    public double getBalanceByAccountNumber(String account_number) {
        String sql = """
                Select main_balance from "account" where account_number=? ; 
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account_number);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("main_balance");
            } else {
                throw new RuntimeException("Account not found with account number: " + account_number);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
