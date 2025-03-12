package aba3.lucid.domain.user.service;

import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.country.entity.CountryEntity;
import aba3.lucid.domain.user.enums.CountryEnum;
import aba3.lucid.domain.board.dto.BoardRequest;
import aba3.lucid.domain.board.dto.BoardResponse;
import aba3.lucid.domain.board.repository.BoardRepository;
import aba3.lucid.domain.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CountryRepository countryRepository;

    //게시판 생성
    public BoardResponse createBoard(BoardRequest request) {
        CountryEntity country = countryRepository.findById(CountryEnum.valueOf(request.getCountryId()))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 국가 코드입니다."));

        BoardEntity board = BoardEntity.builder()
                .country(country)
                .boardName(request.getBoardName())
                .build();

        BoardEntity saveBoard = boardRepository.save(board);

        return new BoardResponse(
                saveBoard.getBoardId(),
                saveBoard.getCountry().getCountryName(),
                saveBoard.getBoardName()
        );
    }


}

