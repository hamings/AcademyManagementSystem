package src.service;

import src.ServiceType;
import src.domain.Admin;
import src.domain.Student;
import src.domain.Teacher;
import src.repository.Repository;
import src.repository.RepositoryProvider;

import java.io.IOException;
import java.util.ArrayList;

public class UserService {
    private Repository<Student,String> studentRepository;
    private Repository<Teacher,String> teacherRepository;
    private Admin admin;

    public UserService() throws IOException {
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
        this.teacherRepository = RepositoryProvider.getInstance().provide(ServiceType.TEACHER);
        this.admin = Admin.getInstance();
    }
    public Student loginStudent(String id, String pw) throws IOException {

        Student student = studentRepository.findById(id);
        if(student!=null && student.getPassword().equals(pw)){
            return student;
        }
        System.out.println("로그인 실패");
        return null;

    }
    public Teacher loginTeacher(String id, String pw) throws IOException {
        Teacher teacher = teacherRepository.findById(id);
        if(teacher!=null && teacher.getPassword().equals(pw)){
            return teacher;
        }
        System.out.println("로그인 실패");
        return null;

    }
    public Admin loginAdmin(String id, String pw){
        if(admin.getId().equals(id)&&admin.getPassword().equals(pw)){
            return admin;
        }
        System.out.println("로그인 실패");
        return null;
    }
    public Object logout(){
        return null;
    }
    public void signInStudent(String id, String password, String name, String gender, String phoneNumber, String birthday, String accountNumber, String accountPassword ) throws IOException {
        Student newStudent = Student.builder()
                .id(id)
                .password(password)
                .name(name)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .accountNumber(accountNumber)
                .lectureCost(0L)
                .accountPassword(accountPassword)
                .lectureRegistrationIdList(new ArrayList<>())
                .lectureRegistrationList(new ArrayList<>())
                .build();
        studentRepository.insert(newStudent);
        studentRepository.save();
    }
    public void signInTeacher(String id, String password, String name, String gender, String phoneNumber, String birthday, String email) throws IOException {
        Teacher newTeacher = Teacher.builder()
                .id(id)
                .password(password)
                .name(name)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .email(email)
                .lectureIdList(new ArrayList<>())
                .lectureList(new ArrayList<>())
                .build();
        teacherRepository.insert(newTeacher);
        teacherRepository.save();

    }
    public boolean isExistStudent(String studentId){
        return studentRepository.isExist(studentId);
    }
    public boolean isExistTeacher(String teacherId){
        return teacherRepository.isExist(teacherId);
    }
}
