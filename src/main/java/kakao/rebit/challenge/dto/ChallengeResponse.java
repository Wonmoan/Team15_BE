package kakao.rebit.challenge.dto;

import java.time.LocalDateTime;
import kakao.rebit.challenge.entity.ChallengeType;

public record ChallengeResponse(
        Long id,
        CreatorResponse creator,
        String title,
        String content,
        String imageKey,
        String presignedUrl,
        ChallengeType type,
        Integer minimumEntryFee,
        LocalDateTime recruitmentStartDate,
        LocalDateTime recruitmentEndDate,
        LocalDateTime challengeStartDate,
        LocalDateTime challengeEndDate,
        Integer minHeadcount,
        Integer maxHeadcount,
        LocalDateTime createdAt,
        Integer currentHeadcount,
        Integer totalEntryFee
) {

}
