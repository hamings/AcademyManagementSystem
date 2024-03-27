package src.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureRegistration implements Serializable {
    //student - lecture의 중간 객체
    private Long id;
    private String lectureId;
    private String studentId;
    private String lectureDay;
    private int lectureTime;

    //private transient Lecture lecture;
    //private transient Student student;
}
