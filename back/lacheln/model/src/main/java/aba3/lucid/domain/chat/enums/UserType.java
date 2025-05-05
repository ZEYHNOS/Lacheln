package aba3.lucid.domain.chat.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum UserType {

    U("사용자"),
    C("업체")
    ;

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserType fromValue(String value) {
        for (UserType userType : UserType.values()) {
            if (userType.getValue().equals(value)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
