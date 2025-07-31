package ro.mdx.meditation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "teacher_remuneration")
public class TeacherRemuneration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "base_students", nullable = false)
    private int baseStudents;

    @Column(name = "base_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseAmount;

    @Column(name = "step_students", nullable = false)
    private int stepStudents;

    @Column(name = "step_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal stepAmount;
}
