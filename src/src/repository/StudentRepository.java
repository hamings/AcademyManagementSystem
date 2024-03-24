package src.repository;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Student;
import src.util.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentRepository extends Repository<Student, String>{
    private Map<Long, LectureRegistration> lectureRegistrationMap; // 1:N 관계 객체 -> 좋은 구조는 아님...
    private Map<String, Lecture> lectureMap;

    @Override
    public boolean isExist(String objectId) {
        if(objectMap.get(objectId) != null) return true;
        return false;
    }

    @Override
    public Student findById(String objectId) {
        // 있으면 student객체, 없으면 null 반환 -> 주의! null 처리 해주어야한다.
        // student객체는 저장 될 때 반드시 lectureRegistrationId 객체를 null값으로 가지면 안된다 -> 생성자에서 생성하기
        Student student = objectMap.get(objectId);

        if(student.getLectureRegistrationList() == null) {
            student.setLectureRegistrationList(new ArrayList<>());
            for (Long id : student.getLectureRegistrationIdList()) {
                student.getLectureRegistrationList().add(lectureRegistrationMap.get(id));
            }
        }

        return student;
    }

    @Override
    public List<Student> findAll() {
        // 어차피 findAll 메서드를 통해서 수강관련 데이터 접근 안함 -> 바인딩 필요 x
        List<Student> studentList =
                objectMap.entrySet().stream()
                .map(e-> e.getValue())
                .collect(Collectors.toList());


        for(Student student : studentList) {
            if(student.getLectureRegistrationList() == null) {
                student.setLectureRegistrationList(new ArrayList<>());

                for (Long id : student.getLectureRegistrationIdList()) {
                    System.out.println(lectureRegistrationMap.get(id).getLectureId());
                    student.getLectureRegistrationList().add(lectureRegistrationMap.get(id));
                }
            }
        }

        return studentList;
    }

    @Override
    public void insert(Student student) {
        objectMap.put(student.getId(), student);
    }


    @Override
    public void save() throws IOException {
        FileSystem.saveObjectMap(ServiceType.STUDENT, objectMap);
    }

    @Override
    public int delete(Student object) throws IOException {
        // 연관관계 삭제 추가 -> 수강신청순회
        lectureMap = FileSystem.loadObjectMap(ServiceType.LECTURE);

        for(LectureRegistration lectureRegistration : object.getLectureRegistrationList()) {
            Lecture lecture = lectureMap.get(lectureMap.get(lectureRegistration.getLectureId()).getLectureId());
            lecture.getLectureRegistrationIdList().remove(lectureRegistration.getId());
            if(lecture.getLectureRegistrationList() != null) lecture.getLectureRegistrationList().remove(lectureRegistration);

            lectureRegistrationMap.remove(lectureRegistration.getId());
            lectureMap.remove(lectureMap.get(lectureRegistration.getLectureId()).getLectureId());
        }


        objectMap.remove(object.getId());
        FileSystem.saveObjectMap(ServiceType.LECTURE, lectureMap);
        FileSystem.saveObjectMap(ServiceType.LECTUREREGISTRATION, lectureRegistrationMap);
        return 1;
    }

    @Override
    boolean support(ServiceType serviceType) {
        return serviceType == ServiceType.STUDENT ? true : false;
    }

    @Override
    void init() throws IOException {
        objectMap = (Map<String, Student>)FileSystem.loadObjectMap(ServiceType.STUDENT);
        lectureRegistrationMap = (Map<Long, LectureRegistration>)FileSystem.loadObjectMap(ServiceType.LECTUREREGISTRATION);
    }
}
