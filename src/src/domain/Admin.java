package src.domain;

import lombok.Getter;

@Getter
public class Admin {
    private String id;
    private String password;

    private Admin(){
        this.id = "admin";
        this.password = "admin123";
    }
    //TODO 내용 수정 필요
    public static Admin getInstance(){
        return new Admin();
    }

}