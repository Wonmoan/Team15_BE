package kakao.rebit.feed.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kakao.rebit.common.domain.ImageKeyModifier;

public class CreateStoryRequest extends CreateFeedRequest {

    @NotBlank(message = "이미지는 필수입니다.")
    @Pattern(regexp = "^feed" + ImageKeyModifier.BASE_IMAGE_KEY_FORMAT, message = "피드 imageKey는 'feed/UUID/filename' 형식이어야 합니다.")
    private String imageKey;

    @NotBlank(message = "본문은 필수 입력 값입니다.")
    private String content;

    private CreateStoryRequest() {
    }

    public CreateStoryRequest(String type, Long bookId, String imageKey, String content) {
        super(type, bookId);
        this.imageKey = imageKey;
        this.content = content;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getContent() {
        return content;
    }
}
