package aba3.lucid.domain.board.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 공통 페이징 응답 포맷
 * - 어떤 데이터든 리스트를 페이징으로 감싸서 반환할 때 사용
 * - 게시글, 댓글, 검색 결과 등 모두 사용 가능
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PagedResponse<T> {

    private List<T> content;       // 실제 데이터 목록 (예: 게시글 리스트)
    private int page;              // 현재 페이지 번호 (1부터 시작)
    private int size;              // 한 페이지당 데이터 개수
    private long totalElements;    // 전체 데이터 수
    private int totalPages;        // 전체 페이지 수
    private boolean hasNext;       // 다음 페이지 존재 여부
    private boolean hasPrevious;   // 이전 페이지 존재 여부
}