package kakao.rebit.feed.mapper;

import kakao.rebit.book.entity.Book;
import kakao.rebit.feed.dto.request.create.CreateFavoriteBookRequest;
import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.create.CreateMagazineRequest;
import kakao.rebit.feed.dto.request.create.CreateStoryRequest;
import kakao.rebit.feed.dto.response.AuthorResponse;
import kakao.rebit.feed.dto.response.FeedBookResponse;
import kakao.rebit.feed.dto.response.FavoriteBookResponse;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.dto.response.MagazineResponse;
import kakao.rebit.feed.dto.response.StoryResponse;
import kakao.rebit.feed.entity.FavoriteBook;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Magazine;
import kakao.rebit.feed.entity.Story;
import kakao.rebit.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class FeedMapper {

    /**
     * Entity -> DTO(Response) 변환
     */
    public FeedResponse toFeedResponse(Feed feed) {
        return switch (feed) {
            case FavoriteBook favoriteBook -> toFavoriteBookResponse(favoriteBook);
            case Magazine magazine -> toMagazineResponse(magazine);
            case Story story -> toStoryResponse(story);
            default -> throw new IllegalStateException("유효하지 않는 피드입니다.");
        };
    }

    /**
     * DTO(CreateRequest) -> Entity 변환
     */
    public Feed toFeed(Member member, Book book, CreateFeedRequest feedRequest) {
        return switch (feedRequest) {
            case CreateFavoriteBookRequest favoriteBookRequest ->
                    createFavoriteBook(member, book, favoriteBookRequest);
            case CreateMagazineRequest magazineRequest ->
                    createMagazine(member, book, magazineRequest);
            case CreateStoryRequest storyRequest -> createStory(member, book, storyRequest);
            default -> throw new IllegalStateException("유효하지 않는 피드입니다.");
        };
    }

    private FavoriteBookResponse toFavoriteBookResponse(FavoriteBook favoriteBook) {
        return new FavoriteBookResponse(
                favoriteBook.getId(),
                this.toAuthorResponse(favoriteBook.getMember()),
                this.toBookResponse(favoriteBook.getBook()),
                favoriteBook.getType(), favoriteBook.getLikes(), favoriteBook.getBriefReview(),
                favoriteBook.getFullReview());
    }

    private MagazineResponse toMagazineResponse(Magazine magazine) {
        return new MagazineResponse(
                magazine.getId(),
                this.toAuthorResponse(magazine.getMember()),
                this.toBookResponse(magazine.getBook()),
                magazine.getType(),
                magazine.getLikes(),
                magazine.getName(),
                magazine.getImageUrl(),
                magazine.getContent()
        );
    }

    private StoryResponse toStoryResponse(Story story) {
        return new StoryResponse(
                story.getId(),
                this.toAuthorResponse(story.getMember()),
                this.toBookResponse(story.getBook()),
                story.getType(),
                story.getLikes(),
                story.getImageUrl(),
                story.getContent()
        );
    }

    private AuthorResponse toAuthorResponse(Member member) {
        return new AuthorResponse(
                member.getId(),
                member.getNickname(),
                member.getImageUrl()
        );
    }

    private FeedBookResponse toBookResponse(Book book) {
        if (book == null) {
            return null;
        }
        return new FeedBookResponse(
            book.getId(),
            book.getIsbn(),
            book.getTitle(),
            book.getDescription(),
            book.getAuthor(),
            book.getPublisher(),
            book.getCover(),       // imageUrl을 cover로 수정
            book.getPubDate()       // pubDate 추가
        );
    }

    private FavoriteBook createFavoriteBook(Member member, Book book,
            CreateFavoriteBookRequest request) {
        return new FavoriteBook(
                member,
                book,
                request.getBriefReview(),
                request.getFullReview()
        );
    }

    private Magazine createMagazine(Member member, Book book, CreateMagazineRequest request) {
        return new Magazine(
                member,
                book,
                request.getName(),
                request.getImageUrl(),
                request.getContent()
        );
    }

    private Story createStory(Member member, Book book, CreateStoryRequest request) {
        return new Story(
                member,
                book,
                request.getImageUrl(),
                request.getContent()
        );
    }
}