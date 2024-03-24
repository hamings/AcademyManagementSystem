package src.service;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Student;
import src.domain.Teacher;
import src.repository.Repository;
import src.repository.RepositoryProvider;

import java.io.IOException;

public class TeacherService {
    private Teacher teacher;
    private Repository<Student,String> studentRepository;


    public TeacherService() throws IOException {
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    /**
     * 강사의 강의 리스트를 보여주는 함수
     */
    public void showLectureList(){
        for(Lecture lecture: teacher.getLectureList()){
            System.out.println(lecture);
        }
    }

    /**
     * 해당 강의를 수강하는 학생 리스트를 보여주는 함수
     * @param lectureId : 강의번호
     */
    public void showStudentListByLecture(String lectureId) throws IOException {
        Lecture findLecture = teacher.getLectureList().stream()
                .filter(lecture -> lecture.getLectureId().equals(lectureId))
                .findFirst().get();

        for(LectureRegistration lectureRegistration: findLecture.getLectureRegistrationList()){
            Student student = studentRepository.findById(lectureRegistration.getStudentId());
            System.out.println("학생명: "+student.getName());
        }
    }
}
