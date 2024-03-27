package src.repository;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Student;
import src.domain.Teacher;
import src.util.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LectureRepository implements Repository<Lecture, String> {

    private Map<Long, LectureRegistration> lectureRegistrationMap; // 1:N 관계 객체 -> 좋은 구조는 아님...
    private Map<String, Teacher> teacherMap;
    private Map<String, Student> studentMap;
    Map<String, Lecture> objectMap;

    @Override
    public boolean isExist(String objectId) {
        if (objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public Lecture findById(String objectId) {
        Lecture lecture = objectMap.get(objectId);

        if (lecture == null) return null;

        if (lecture.getLectureRegistrationList() == null) {
            lecture.setLectureRegistrationList(new ArrayList<>());
            for (Long id : lecture.getLectureRegistrationIdList()) {
                lecture.getLectureRegistrationList().add(lectureRegistrationMap.get(id));
            }
        }

        return lecture;
    }

    @Override
    public List<Lecture> findAll() throws IOException {
        lectureRegistrationMap = (Map<Long, LectureRegistration>) FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);

        List<Lecture> lectureList =
                objectMap.entrySet().stream()
                        .map(e -> e.getValue())
                        .collect(Collectors.toList());

        for (Lecture lecture : lectureList) {
            if (lecture.getLectureRegistrationList() == null) {

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
    public int delete(Lecture object) throws IOException {
        // 강의삭제 -> 선생님 강의리스트에서 삭제
        // 강의삭제 -> 수강신청정보리스트 돌면서 학생의 수강신청리스트에서 수강객체 삭제
        // 강의삭제 -> 수강신청정보리스트 돌면서 수강신청객체 삭제


        String teacherId = object.getLectureTeacherId();
        Teacher teacher = teacherMap.get(teacherId);

        List<String> lectrueIdList = teacher.getLectureIdList();
        lectrueIdList.remove(object.getLectureId());

        List<Lecture> lectureList = teacher.getLectureList();
        if (lectureList != null) {
            lectureList.remove(object);
        }

        List<Long> lectureRegistrationIdList = object.getLectureRegistrationIdList();
        for (Long lectureRegistrationId : lectureRegistrationIdList) {
            LectureRegistration lectureRegistration = lectureRegistrationMap.get(lectureRegistrationId);
            String studentId = lectureRegistration.getStudentId();

            Student student = studentMap.get(studentId);
            List<Long> lectureRegistrationIdListOfStudent = student.getLectureRegistrationIdList();
            lectureRegistrationIdListOfStudent.remove(lectureRegistrationIdListOfStudent.indexOf(lectureRegistrationId));

            List<LectureRegistration> lectureRegistrationListOfStudent = student.getLectureRegistrationList();
            if (lectureRegistrationListOfStudent != null) {
                lectureRegistrationListOfStudent.remove(lectureRegistration);
            }

            lectureRegistrationMap.remove(lectureRegistrationId);
        }

        objectMap.remove(object.getLectureId());


        FileSystem.saveObjectMap(ServiceType.STUDENT, studentMap);
        FileSystem.saveObjectMap(ServiceType.TEACHER, teacherMap);
        FileSystem.saveObjectMap(ServiceType.LECTUREREGISTRATION, lectureRegistrationMap);
        return 1;
    }

    @Override
    public boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.LECTURE ? true : false;
    }

    @Override
    public void init() throws IOException {
        objectMap = (Map<String, Lecture>) FileSystem.loadObjectMap(ServiceType.LECTURE);
        lectureRegistrationMap = (Map<Long, LectureRegistration>) FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);
        teacherMap = (Map<String, Teacher>) FileSystem.loadObjectMap(ServiceType.TEACHER);
        studentMap = (Map<String, Student>) FileSystem.loadObjectMap(ServiceType.STUDENT);
    }
}
