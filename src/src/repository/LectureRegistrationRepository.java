package src.repository;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Student;
import src.util.FileSystem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LectureRegistrationRepository implements Repository<LectureRegistration, Long> {

    Map<String, Student> studentMap;
    Map<String, Lecture> lectureMap;
    Map<Long, LectureRegistration> objectMap;

    @Override
    public boolean isExist(Long objectId) {
        if (objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public LectureRegistration findById(Long objectId) {
        return objectMap.get(objectId);
    }

    @Override
    public List<LectureRegistration> findAll() {
        return objectMap.entrySet().stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public void insert(LectureRegistration lectureRegistration) {
        long key = objectMap.size() + 1;
        while (objectMap.containsKey(key)) {
            key++;
        }
        lectureRegistration.setId(key);
        objectMap.put(key, lectureRegistration);
    }

    @Override
    public void save() throws IOException {
        FileSystem.saveObjectMap(ServiceType.LECTUREREGISTRATION, objectMap);
    }

    @Override
    public int delete(LectureRegistration object) throws IOException {
        studentMap = FileSystem.loadObjectMap(ServiceType.STUDENT);
        lectureMap = FileSystem.loadObjectMap(ServiceType.LECTURE);

        Student student = studentMap.get(object.getStudentId());
        Lecture lecture = lectureMap.get(object.getLectureId());
        int idx = student.getLectureRegistrationIdList().indexOf(object.getId());
        student.getLectureRegistrationIdList().remove(idx);

        idx = lecture.getLectureRegistrationIdList().indexOf(object.getId());
        lecture.getLectureRegistrationIdList().remove(idx);
        if (student.getLectureRegistrationList() != null) student.getLectureRegistrationList().remove(object);
        if (lecture.getLectureRegistrationList() != null) lecture.getLectureRegistrationList().remove(object);

        FileSystem.saveObjectMap(ServiceType.LECTURE, lectureMap);
        FileSystem.saveObjectMap(ServiceType.STUDENT, studentMap);
        objectMap.remove(object.getId());
        return 1;
    }

    @Override
    public boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.LECTUREREGISTRATION ? true : false;
    }

    @Override
    public void init() throws IOException {
        objectMap = (Map<Long, LectureRegistration>) FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);

    }
}
