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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opId;

    private Long pdId;

    private String name;

    private String overlap;
//중복 여부 - overlap

    private String essential;
//    필수 여부-essential

    private String status;



}
