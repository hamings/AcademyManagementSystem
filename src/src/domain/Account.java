package src.domain;


import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable {
    private String accountNumber;
    private String accountPassword;
    private Long balance;
}
