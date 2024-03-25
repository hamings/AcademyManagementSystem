package src.repository;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.util.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LectureRepository extends Repository<Lecture, String>{

    private Map<Long, LectureRegistration> lectureRegistrationMap; // 1:N 관계 객체 -> 좋은 구조는 아님...

    @Override
    public boolean isExist(String objectId) {
        if(objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public Lecture findById(String objectId) {
        Lecture lecture = objectMap.get(objectId);

        if(lecture.getLectureRegistrationList() == null) {
            lecture.setLectureRegistrationList(new ArrayList<>());
            for (Long id : lecture.getLectureRegistrationIdList()) {
                lecture.getLectureRegistrationList().add(lectureRegistrationMap.get(id));
            }
        }

        return lecture;
    }

    @Override
    public List<Lecture> findAll() throws IOException {
        lectureRegistrationMap = (Map<Long, LectureRegistration>)FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);

        List<Lecture> lectureList =
                objectMap.entrySet().stream()
                .map(e-> e.getValue())
                .collect(Collectors.toList());

        for(Lecture lecture : lectureList) {
            if(lecture.getLectureRegistrationList() == null) {

                lecture.setLectureRegistrationList(new ArrayList<>());
                for (Long id : lecture.getLectureRegistrationIdList()) {
                    lecture.getLectureRegistrationList().add(lectureRegistrationMap.get(id));
                }
            }
        }
        return lectureList;
    }

    @Override
    public void insert(Lecture lecture) {
        objectMap.put(lecture.getLectureId(), lecture);
    }

    @Override
    public void save() throws IOException {
        FileSystem.saveObjectMap(ServiceType.LECTURE, objectMap);
    }

    @Override
    public int delete(Lecture object) {
        objectMap.remove(object.getLectureId());
        return 1;
    }

    @Override
    boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.LECTURE ? true : false;
    }

    @Override
    void init() throws IOException {
        objectMap = (Map<String, Lecture>)FileSystem.loadObjectMap(ServiceType.LECTURE);
        lectureRegistrationMap = (Map<Long, LectureRegistration>)FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);
    }
}
