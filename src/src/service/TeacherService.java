package src.service;

import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Student;
import src.domain.Teacher;
import src.repository.Repository;
import src.repository.RepositoryProvider;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TeacherService {
    private Teacher teacher;
    private Repository<Student,String> studentRepository;


    public TeacherService() throws IOException {
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
    }
    public Teacher getTeacher(){
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }


    /**
     * 강사의 강의 리스트를 보여주는 함수
     */
    public void showLectureList(){
        if(teacher.getLectureList().isEmpty()){
            System.out.println("                       [현재 담당하고있는 강의가 없습니다.]");
            return;
        }

        for(Lecture lecture: teacher.getLectureList()){
            String realLectureDay = "";
            if(lecture.getLectureDay() == 0){
                realLectureDay = "월요일";
            } else if(lecture.getLectureDay() == 1){
                realLectureDay = "화요일";
            }  else if(lecture.getLectureDay() == 2){
                realLectureDay = "수요일";
            }  else if(lecture.getLectureDay() == 3){
                realLectureDay = "목요일";
            } else if(lecture.getLectureDay() == 4){
                realLectureDay = "금요일";
            }

            String realLectureTime = "";
            if(lecture.getLectureTime() == 0){
                realLectureTime = "10:00 ~ 12:00";
            } else if(lecture.getLectureTime() == 1){
                realLectureTime = "13:00 ~ 14:50";
            }  else if(lecture.getLectureTime() == 2){
                realLectureTime = "15:00 ~ 16:50";
            }  else if(lecture.getLectureTime() == 3){
                realLectureTime = "17:00 ~ 19:00";
            }

            System.out.println("[강의이름]: " + lecture.getLectureName() + " [강의아이디]: " + lecture.getLectureId() + " [강의요일]: " + realLectureDay + " [강의시간]: " + realLectureTime);
        }
    }

    /**
     * 해당 강의를 수강하는 학생 리스트를 보여주는 함수
     * @param lectureId : 강의번호
     */
    public void showStudentListByLecture(String lectureId) throws IOException {
        Lecture findLecture = teacher.getLectureList().stream()
                .filter(lecture -> lecture.getLectureId().equals(lectureId))
                .findFirst()
                .get();

        List<LectureRegistration> lectureRegistrationList = findLecture.getLectureRegistrationList();
        if(lectureRegistrationList.isEmpty()){
            System.out.println("                [해당 강의를 수강하고 있는 학생이 없습니다.]              ");
            System.out.println();
            return;
        }
        for(LectureRegistration lectureRegistration: lectureRegistrationList){
            Student student = studentRepository.findById(lectureRegistration.getStudentId());
            System.out.println("학생명: "+student.getName());
        }
    }
    public boolean isTeacherLectureListEmpty(){
        return teacher.getLectureIdList().isEmpty();
    }
}
