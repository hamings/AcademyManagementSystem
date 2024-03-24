package src.repository;

import src.ServiceType;
import src.domain.Notification;
import src.util.FileSystem;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class NotificationRepository extends Repository<Queue<Notification>, String>{
    @Override
    public boolean isExist(String objectId) {
        return false;
    }

    @Override
    public Queue<Notification> findById(String objectId) throws IOException {
        Queue q = objectMap.get(objectId);
        if(q == null) objectMap.put(objectId, new LinkedList<>());

        return objectMap.get(objectId);
    }

    @Override
    public List<Queue<Notification>> findAll() {
        return null;
    }

    @Override
    public void insert(Queue<Notification> object) {

    }

    @Override
    public void save() throws IOException {
        FileSystem.saveObjectMap(ServiceType.LECTURE, objectMap);
    }

    @Override
    public int delete(Queue<Notification> object) throws IOException {
        return 0;
    }



    @Override
    boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.NOTIFICATION ? true : false;
    }

    @Override
    void init() throws IOException {
        super.objectMap = (Map<String, Queue<Notification>>) FileSystem.loadObjectMap(ServiceType.NOTIFICATION);
    }
}
