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

public class LectureRegistrationRepository extends Repository<LectureRegistration, Long>{

    Map<String, Student> studentMap;
    Map<String, Lecture> lectureMap;
    @Override
    public boolean isExist(Long objectId) {
        if(objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public LectureRegistration findById(Long objectId) {
        return objectMap.get(objectId);
    }

    private List<LectureRegistration> findAllByLectureId(String lectureId) {
        return objectMap.entrySet().stream()
                .filter(e -> e.getValue().getLectureId().equals(lectureId))
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }
    @Override
    public List<LectureRegistration> findAll() {
        return objectMap.entrySet().stream()
                .map(e-> e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public void insert(LectureRegistration lectureRegistration) {
        lectureRegistration.setId((long)(objectMap.size() + 1 ));
        objectMap.put(lectureRegistration.getId(), lectureRegistration);
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

        student.getLectureRegistrationIdList().remove(object.getLectureId());
        lecture.getLectureRegistrationIdList().remove(object.getLectureId());
        if(student.getLectureRegistrationList() != null) student.getLectureRegistrationList().remove(object);
        if(lecture.getLectureRegistrationList() != null) lecture.getLectureRegistrationList().remove(object);

        objectMap.remove(object.getLectureId());
        return 1;
    }

    @Override
    boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.LECTUREREGISTRATION ? true : false;
    }

    @Override
    void init() throws IOException {
        objectMap = (Map<Long, LectureRegistration>) FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);
    }
}
