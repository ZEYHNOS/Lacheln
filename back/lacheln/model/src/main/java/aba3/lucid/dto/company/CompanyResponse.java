package aba3.lucid.dto.company;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse implements Serializable {

    //todo list
    //CompanyResponse 만들기

    private Long cpId;

    private String cpEmail;

    private String cpName;

    private String cpRepName;

    private String cpMainContact;

    private String cpAddress;

}
