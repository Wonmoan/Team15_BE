package kakao.rebit.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import kakao.rebit.common.annotation.AllowAnonymous;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.LikesMemberResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.service.FeedService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.annotation.MemberInfoIfPresent;
import kakao.rebit.member.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds")
@Tag(name = "피드 API", description = "피드 관련 API")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @Operation(summary = "피드 목록 조회", description = "피드 목록을 조회합니다.")
    @AllowAnonymous
    @GetMapping
    public ResponseEntity<Page<FeedResponse>> getFeeds(
            @Parameter(hidden = true) @MemberInfoIfPresent MemberResponse memberResponse,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(feedService.getFeeds(memberResponse, pageable));
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @ApiResponse(content = @Content(schema = @Schema(oneOf = {FavoriteBookResponse.class, MagazineResponse.class, StoryResponse.class})))
    @GetMapping("/{feed-id}")
    public ResponseEntity<FeedResponse> getFeed(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        return ResponseEntity.ok().body(feedService.getFeedById(memberResponse, feedId));
    }

    @Operation(summary = "피드 생성", description = "피드를 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> createFeed(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @Valid @RequestBody CreateFeedRequest feedRequest) {
        Long feedId = feedService.createFeed(memberResponse, feedRequest);
        return ResponseEntity.created(URI.create("/feeds/" + feedId)).build();
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @DeleteMapping("/{feed-id}")
    public ResponseEntity<Void> deleteFeed(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        feedService.deleteFeedById(memberResponse, feedId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 좋아요
     */
    @Operation(summary = "좋아요 누른 멤버 목록 조회", description = "해당 피드에 좋아요를 누른 멤버 목록을 조회합니다.")
    @GetMapping("/{feed-id}/likes")
    public ResponseEntity<Page<LikesMemberResponse>> getLikesMembers(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok()
                .body(feedService.getLikesMembers(memberResponse, feedId, pageable));
    }

    @Operation(summary = "좋아요 추가", description = "좋아요를 추가합니다.")
    @PostMapping("/{feed-id}/likes")
    public ResponseEntity<Void> creatLikes(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        Long likesId = feedService.createLikes(memberResponse, feedId);
        String uri = String.format("/feeds/%d/likes/%d", feedId, likesId);
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @Operation(summary = "좋아요 삭제", description = "좋아요를 삭제합니다.")
    @DeleteMapping("/{feed-id}/likes")
    public ResponseEntity<Void> deleteLikes(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("feed-id") Long feedId) {
        feedService.deleteLikes(memberResponse, feedId);
        return ResponseEntity.noContent().build();
    }
}
