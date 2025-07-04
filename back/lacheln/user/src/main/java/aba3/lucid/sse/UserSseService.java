package aba3.lucid.sse;

import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.alert.dto.UserAlertDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) throws IOException {
        if (userId == null || userId.isBlank()) {
            return null;
        }
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("SSE Connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(userId);
        }

        return emitter;
    }

    @Async
    public void sendAlert(String userId, UserAlertDto dto) {
        SseEmitter emitter = emitters.getOrDefault(userId, null);

        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event().name("alert").data(dto));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(userId);
        }
    }

}
