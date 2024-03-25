package src.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data

public class StudyRoom {
    private static final int ROW = 5; // 임의 할당 -0> 추후 수정
    private static final int COL = 5;

    private boolean[][] checkSeat = {
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}
    };
    private Map<String, String> reservationMap = new HashMap<>();
}
