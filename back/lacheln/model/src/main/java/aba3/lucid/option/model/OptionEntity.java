package aba3.lucid.option.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="option")
@Builder
@Getter
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long opId;

    private Long pdId;

    private String name;

    private String overlap;

    private String essential;

    private String status;



}
