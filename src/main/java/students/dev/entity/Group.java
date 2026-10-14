package students.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "study_groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "study_groups")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "study_groups")
    private Set<Course> courses = new HashSet<>();


}
