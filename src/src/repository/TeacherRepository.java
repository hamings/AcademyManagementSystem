package src.repository;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Teacher;
import src.util.FileSystem;

import src.util.FileSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TeacherRepository extends Repository<Teacher, String>{
    private Map<String, Lecture> lectureMap;
    private Map<Long, LectureRegistration> lectureRegistrationMap;


    public Teacher findByIdLazyLoading(String objectId) {
        // 관리자 사용 쿼리메서드
        Teacher teacher = objectMap.get(objectId);
        teacher.setLectureList(new ArrayList<>());
        for(String id : teacher.getLectureIdList()) {
            teacher.getLectureList().add(lectureMap.get(id));
        }
        return teacher;
    }

    @Override
    public boolean isExist(String objectId) {
        if(objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public Teacher findById(String objectId) throws IOException {
        lectureRegistrationMap = FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);

        Teacher teacher = objectMap.get(objectId);
        if(teacher.getLectureList() == null) {
            teacher.setLectureList(new ArrayList<>());
            for (String id : teacher.getLectureIdList()) {
                Lecture l = lectureMap.get(id);
                if (l == null) continue;

                teacher.getLectureList().add(l);
                lectureMap.get(id).setLectureRegistrationList(
                        lectureRegistrationMap.entrySet().stream()
                                .filter(e -> e.getValue().getLectureId().equals(id))
                                .map(e -> e.getValue())
                                .collect(Collectors.toList())
                );
            }
        }
        return teacher;
    }


    @Override
    public List<Teacher> findAll() {
        List<Teacher> teacherList =
                objectMap.entrySet().stream()
                .map(e-> e.getValue())
                .collect(Collectors.toList());

        for(Teacher teacher : teacherList) {
            teacher.setLectureList(new ArrayList<>());
            for(String id : teacher.getLectureIdList()) {
                teacher.getLectureList().add(lectureMap.get(id));
            }
        }

        return teacherList;
    }

    @Override
    public void insert(Teacher teacher) {
        objectMap.put(teacher.getId(), teacher);
    }

    @Override
    public void save() throws IOException {
        FileSystem.saveObjectMap(ServiceType.TEACHER, objectMap);
    }

    @Override
    public int delete(Teacher object) {
        // 강사 퇴원 불가능
        //objectMap.remove(object.getId());
        return 1;
    }

    @Override
    boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.TEACHER ? true : false;
    }

    @Override
    void init() throws IOException {
        objectMap = FileSystem.loadObjectMap(ServiceType.TEACHER);
        lectureMap = FileSystem.loadObjectMap(ServiceType.LECTURE);
    }
}
