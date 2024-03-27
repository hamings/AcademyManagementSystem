package src;

import src.domain.*;
import src.repository.Repository;
import src.repository.RepositoryProvider;

import java.io.IOException;
import java.util.Queue;

public class Application {
    public static void main(String[] args) throws IOException {
        init();
        AmsApp app = new AmsApp();
        app.run();
    }
    public static void init() throws IOException {
        Repository<Student, String> studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
        Repository<Lecture, String> lectureRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        Repository<LectureRegistration, Long> lectureRegistrationRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTUREREGISTRATION);
        Repository<Teacher, String> teacherRepository = RepositoryProvider.getInstance().provide(ServiceType.TEACHER);
        Repository<Queue<Notification>, String> notificationRepository = RepositoryProvider.getInstance().provide(ServiceType.NOTIFICATION);

        Lecture lecture1 = new Lecture("이것이 자바?1","이것이 자바?", 0,0,"현우진","hyeon123");
        lectureRepository.insert(lecture1);
        Lecture lecture2 = new Lecture("저것도 자바?2","저것이 자바?", 1,1,"한석원","han123");
        lectureRepository.insert(lecture2);
        Lecture lecture3 = new Lecture("요리조리 자바3","요리조리", 2,2,"현우진","hyeon123");
        lectureRepository.insert(lecture3);

        Teacher teacher1 = new Teacher("hyeon123","Hyeon123!!","현우진","male","010-5555-5555","1987-02-10","hyeon123@gmail.com");
        teacher1.getLectureIdList().add("이것이 자바?1");
        teacher1.getLectureIdList().add("요리조리 자바3");
        teacher1.getLectureList().add(lecture1);
        teacher1.getLectureList().add(lecture3);
        teacherRepository.insert(teacher1);

        teacherRepository.insert(teacher1);
        Teacher teacher2 = new Teacher("han123","Han123!!","한석원","male","010-6666-6666","1964-11-07","han123@gmail.com");
        teacher2.getLectureIdList().add("저것도 자바?2");
        teacher2.getLectureList().add(lecture2);
        teacherRepository.insert(teacher2);



        LectureRegistration lectureRegistration1 = new LectureRegistration(1L,"이것이 자바?1","kim123","월요일",0);
        lectureRegistrationRepository.insert(lectureRegistration1);
        LectureRegistration lectureRegistration2 = new LectureRegistration(2L,"요리조리 자바3","kim123","수요일",2);
        lectureRegistrationRepository.insert(lectureRegistration2);
        LectureRegistration lectureRegistration3 = new LectureRegistration(3L,"저것도 자바?2","koo123","화요일",1);
        lectureRegistrationRepository.insert(lectureRegistration3);
        LectureRegistration lectureRegistration4 = new LectureRegistration(4L,"요리조리 자바3","koo123","수요일",2);
        lectureRegistrationRepository.insert(lectureRegistration4);
        LectureRegistration lectureRegistration5 = new LectureRegistration(5L,"저것도 자바?2","jung123","화요일",1);
        lectureRegistrationRepository.insert(lectureRegistration5);
        LectureRegistration lectureRegistration6 = new LectureRegistration(6L,"요리조리 자바3","jung123","수요일",2);
        lectureRegistrationRepository.insert(lectureRegistration6);
        LectureRegistration lectureRegistration7 = new LectureRegistration(7L,"이것이 자바?1","kwon123","월요일",0);
        lectureRegistrationRepository.insert(lectureRegistration7);
        LectureRegistration lectureRegistration8 = new LectureRegistration(8L,"저것도 자바?2","kwon123","화요일",1);
        lectureRegistrationRepository.insert(lectureRegistration8);
        LectureRegistration lectureRegistration9 = new LectureRegistration(9L,"요리조리 자바3","jung123","수요일",2);
        lectureRegistrationRepository.insert(lectureRegistration9);

        Student student1 = new Student("kim123","Kim123!!","김우재","male","010-1111-1111","1995-01-03","111-11-1","1234"); // 속성 채우기
        student1.getLectureRegistrationIdList().add(1L);
        student1.getLectureRegistrationIdList().add(2L);
        student1.getLectureRegistrationList().add(lectureRegistration1);
        student1.getLectureRegistrationList().add(lectureRegistration2);
        student1.setLectureCost(student1.getLectureCost()+200000L);
        studentRepository.insert(student1);

        studentRepository.insert(student1);
        Student student2 = new Student("koo123","Koo123!!","구태호","male","010-2222-2222","1999-12-10","222-22-2","1234"); // 속성 채우기
        student2.getLectureRegistrationIdList().add(3L);
        student2.getLectureRegistrationIdList().add(4L);
        student2.getLectureRegistrationList().add(lectureRegistration3);
        student2.getLectureRegistrationList().add(lectureRegistration4);
        student2.setLectureCost(student2.getLectureCost()+200000L);
        studentRepository.insert(student2);


        studentRepository.insert(student2);
        Student student3 = new Student("jung123","Jung123!!","정혜미","female","010-3333-3333","1996-12-17","333-33-3","4321"); // 속성 채우기
        student3.getLectureRegistrationIdList().add(5L);
        student3.getLectureRegistrationIdList().add(6L);
        student3.getLectureRegistrationList().add(lectureRegistration5);
        student3.getLectureRegistrationList().add(lectureRegistration6);
        student3.setLectureCost(student3.getLectureCost()+200000L);
        studentRepository.insert(student3);


        studentRepository.insert(student3);
        Student student4 = new Student("kwon123","Kwon123!!","권수현","female","010-4444-4444","1997-05-21","444-44-4","1234"); // 속성 채우기
        student4.getLectureRegistrationIdList().add(7L);
        student4.getLectureRegistrationIdList().add(8L);
        student4.getLectureRegistrationIdList().add(9L);
        student4.getLectureRegistrationList().add(lectureRegistration7);
        student4.getLectureRegistrationList().add(lectureRegistration8);
        student4.getLectureRegistrationList().add(lectureRegistration9);
        student4.setLectureCost(student4.getLectureCost()+300000L);
        studentRepository.insert(student4);

        studentRepository.save();
        teacherRepository.save();
        lectureRepository.save();
        lectureRegistrationRepository.save();
    }
}