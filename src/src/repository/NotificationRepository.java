package src.repository;

import src.ServiceType;
import src.domain.Notification;
import src.util.FileSystem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationRepository extends Repository<Notification, Integer>{
    @Override
    public boolean isExist(Integer objectId) {
        if(objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public Notification findById(Integer objectId) {
        return null;
    }

    public List<Notification> findByStudentId(String studentId) {
        return objectMap.entrySet().stream()
                .filter(e -> e.getValue().getStudentId() == studentId)
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findAll() {
        return objectMap.entrySet().stream()
                .map(e-> e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public void insert(Notification notification) {
        objectMap.put(notification.getNotificationId(), notification);
    }

    @Override
    public void save() throws IOException {
        FileSystem.saveObjectMap(ServiceType.LECTURE, objectMap);
    }

    @Override
    public int delete(Notification object) {
        objectMap.remove(object.getNotificationId());
        return 1;
    }

    @Override
    boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.NOTIFICATION ? true : false;
    }

    @Override
    void init() throws IOException {
        super.objectMap = (Map<Integer, Notification>) FileSystem.loadObjectMap(ServiceType.NOTIFICATION);
    }
}
