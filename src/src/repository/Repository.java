package src.repository;

import src.ServiceType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Repository<OBJECT, KEYTYPEOFOBJECT> {
    //keytype을 식별자로 갖는 object의 레포지토리


    boolean isExist(KEYTYPEOFOBJECT objectId);

    OBJECT findById(KEYTYPEOFOBJECT objectId) throws IOException;

    List<OBJECT> findAll() throws IOException;

    void insert(OBJECT object);

    void save() throws IOException;

    int delete(OBJECT object) throws IOException;

    boolean support(ServiceType serviceType);

    void init() throws IOException;

}
