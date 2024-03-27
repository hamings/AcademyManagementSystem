package src.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Bank {
    private static List<Account> accountList;
    private static int successCount;

    static{
        accountList = new ArrayList<>();
        accountList.add(new Account("111-11-1", "1234", 1000000L));
        accountList.add(new Account("222-22-2", "1234", 1000000L));
        accountList.add(new Account("333-33-3", "1234", 1000000L));
        accountList.add(new Account("444-44-4", "1234", 50000L));
    }

    //결제승인체크(학생계좌번호, 계좌비밀번호 맞는지 체크)
    public boolean checkAccount(String accountNumber, String accountPassword){
        for (Account account : accountList) {
            if(account.getAccountNumber().equals(accountNumber) && account.getAccountPassword().equals(accountPassword)){
                return true;
            }
        }
        return false;
    }

    //학생학원비결제 진행
    public boolean paymentAccount(String accountNumber, Long lectureCost) {
        for (Account account : accountList) {
            if(account.getAccountNumber().equals(accountNumber)){
                if(account.getBalance() >= lectureCost) {
                    account.setBalance(account.getBalance() - lectureCost );
                    return true;
                } else return false;
            }
        }
        return false;
    }

    //학생잔액조회
    public long finalBalance(String accountNumber){
        for (Account account : accountList) {
            if(account.getAccountNumber().equals(accountNumber)){
                return account.getBalance();
            }
        }
        return 0;
    }

    //효성은행에 존재하는 계좌인지 체크
    public boolean checkAmsAccount(String accountNumber){
        for (Account account : accountList) {
            if(account.equals(accountNumber)){
                return true;
            }
        }
        System.out.println("[효성Bank에 존재하는 계좌가 아닙니다!]");
        System.out.println();
        return false;
    }
}

