package aba3.lucid.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfig {

    /**
     * Spring에서는 기본적으로 Jackson의 ObjectMapper를 사용해 JSON 직렬화 및 역직렬화 작업을 수행합니다.
     * 만약 애플리케이션 컨텍스트에 ObjectMapper 빈이 등록되어 있으면 Spring이 해당 빈을 사용합니다.
     * 이 클래스는 Spring에서 사용할 커스터마이징된 ObjectMapper 빈을 생성하여 JSON 변환 로직을 조정합니다.
     */

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();

        // Jdk8Module을 등록하여 Optional 같은 JDK 8 이후 추가된 클래스에 대한 지원을 활성화합니다.
        objectMapper.registerModule(new Jdk8Module());

        // JavaTimeModule을 등록하여 Java 8의 java.time 패키지 클래스 (e.g., LocalDate, LocalDateTime) 직렬화 지원을 추가합니다.
        objectMapper.registerModule(new JavaTimeModule());

        // 역직렬화 과정에서 JSON에 정의되지 않은 필드가 있을 때 예외를 발생시키지 않고 무시하도록 설정합니다.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 빈 Java 객체를 직렬화하려 할 때 예외를 발생시키지 않도록 설정합니다.
        // 객체에 데이터가 없더라도 JSON으로 변환이 가능합니다.
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 날짜를 직렬화할 때 타임스탬프로 기록하지 않고, ISO-8601 형식으로 표현되도록 설정합니다.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // JSON 필드명을 Snake Case(예: first_name) 형식으로 변환합니다.
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm")));
    }


}
