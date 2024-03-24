package src.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Account {
    private String accountNumber;//계좌번호
    private String accountPassword;//계좌비밀번호
    private Long balance;//잔액
}
