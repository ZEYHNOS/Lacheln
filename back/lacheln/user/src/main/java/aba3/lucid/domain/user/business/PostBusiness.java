package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.dto.PostRequest;
import aba3.lucid.domain.board.dto.PostResponse;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.board.entity.PostImageEntity;
import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.board.repository.BoardRepository;
import aba3.lucid.domain.board.repository.PostImageRepository;
import aba3.lucid.domain.board.repository.PostRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Business
@RequiredArgsConstructor
public class PostBusiness {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final BoardRepository boardRepository;
    //TODO 아직 JWT 구현이 안 돼서 일단 직접 userId 들고와서 기능 구현하고 확인 함
    private final UsersRepository usersRepository;

    /**
     * 게시글 생성
     */
    public PostResponse createPost(PostRequest postRequest, String userId) {
        // 1. 게시판 존재 여부 확인
        BoardEntity board = boardRepository.findById(postRequest.getBoardId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다: " + postRequest.getBoardId()));

        // 2. 사용자 존재 여부 확인
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다: " + userId));

        // 3. 전체 & 인기 게시판은 글 직접 작성할 수 없음
        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체 게시판과 인기 게시판에는 글을 작성할 수 없습니다.");
        }

        // 4. 게시글 저장
        PostEntity post = PostEntity.builder()
                .postTitle(postRequest.getPostTitle())
                .postContent(postRequest.getPostContent())
                .board(board)
                .usersEntity(user) //TODO JWT, 로그인 구현 전까지만
                .postCreate(LocalDateTime.now())
                .postUpdate(LocalDateTime.now()) // 작성하면 수정일도 동일하게 설정
                .postStatus(PostStatus.CREATED)
                .build();

        PostEntity savePost = postRepository.save(post);

        // 5. 이미지 URL 저장
        List<PostImageEntity> postImages = postRequest.getImageUrls().stream()
                .map(url -> PostImageEntity.builder()
                        .post(savePost)
                        .postImageUrl(url)
                        .build())
                .collect(Collectors.toList());

        postImageRepository.saveAll(postImages);

        // 6. 저장된 게시글과 이미지 URL 반환
        List<String> imageUrls = postImages.stream().map(PostImageEntity::getPostImageUrl).toList();

        return new PostResponse(
                savePost.getPostId(),
                savePost.getPostTitle(),
                savePost.getPostContent(),
                savePost.getPostCreate(),
                savePost.getPostUpdate(),
                savePost.getPostStatus(),
                savePost.getBoard().getBoardId(),
                savePost.getBoard().getBoardName(),
                savePost.getUsersEntity().getUserId(),
                imageUrls
        );
    }
}
