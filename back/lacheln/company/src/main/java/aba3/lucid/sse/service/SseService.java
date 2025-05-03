package aba3.lucid.sse.service;

import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
    // 현재 구조는 회사 ID(companyId)당 하나의 SseEmitter만 저장되므로
    // 동일한 회사 ID로 여러 사용자가 접속할 경우 마지막 연결만 유지되고 이전 연결은 끊어짐
    // -> 여러 관리자(사용자)가 동시에 같은 회사 알림을 수신해야 할 경우 List<SseEmitter>로 확장 필요
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long companyId) throws IOException {
        Validator.throwIfInvalidId(companyId);

        // TODO 시간 설정
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 현재 60초
        emitters.put(companyId, emitter);

        emitter.onCompletion(() -> emitters.remove(companyId));
        emitter.onTimeout(() -> emitters.remove(companyId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("SSE 연결 성공"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(companyId);
        }

        return emitter;
    }

    // TODO MutualAlert 로 변경하기
    public void sendAlert(Long companyId, CompanyAlertDto dto) {
        SseEmitter emitter = emitters.get(companyId);
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event().name("alert").data(dto));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(companyId);
        }
    }

}
