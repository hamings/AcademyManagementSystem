package src.repository;

import src.ServiceType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Repository<OBJECT, KEYTYPEOFOBJECT> {
    //keytype을 식별자로 갖는 object의 레포지토리
    boolean isSaved = false;
    Map<KEYTYPEOFOBJECT, OBJECT> objectMap;
    public abstract boolean isExist(KEYTYPEOFOBJECT objectId);
    public abstract OBJECT findById(KEYTYPEOFOBJECT objectId) throws IOException;
    public abstract List<OBJECT> findAll() throws IOException;
    public abstract void insert(OBJECT object);
    public abstract void save() throws IOException;
    public abstract int delete(OBJECT object) throws IOException;
    abstract boolean support(ServiceType serviceType);
    abstract void init() throws IOException;

}
