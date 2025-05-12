package co.com.ancas.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Student  {
    private String name;
    private int age;
    private String city;
    private List<Integer> scores;


}
