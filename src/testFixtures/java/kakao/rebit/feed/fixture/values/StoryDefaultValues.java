package kakao.rebit.feed.fixture.values;

import java.util.UUID;

public record StoryDefaultValues(
        String type,
        Long bookId,
        String imageKey,
        String content
) {

    public static final StoryDefaultValues INSTANCE = new StoryDefaultValues(
            "S",
            1L,
            "feed/" + UUID.randomUUID() + "/default_image",
            "테스트용 컨텐츠"
    );
}
