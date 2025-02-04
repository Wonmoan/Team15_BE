package kakao.rebit.challenge.fixture;

import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeType;
import kakao.rebit.challenge.fixture.values.ChallengeDefaultValues;
import kakao.rebit.member.entity.Member;

public class ChallengeFixture {

    public static ChallengeRequest createDefaultRequest() {
        ChallengeDefaultValues defaults = ChallengeDefaultValues.INSTANCE;
        return createRequest(
                defaults.title(),
                defaults.content(),
                defaults.challengeType()
        );
    }

    public static ChallengeRequest createRequest(String title, String content, ChallengeType challengeType) {
        ChallengeDefaultValues defaults = ChallengeDefaultValues.INSTANCE;
        return new ChallengeRequest(
                title,
                content,
                defaults.imageKey(),
                challengeType,
                defaults.minimumEntryFee(),
                defaults.recruitmentStartDate(),
                defaults.recruitmentEndDate(),
                defaults.challengeStartDate(),
                defaults.challengeEndDate(),
                defaults.minHeadcount(),
                defaults.maxHeadcount()
        );
    }

    public static Challenge createDefault(Member host) {
        return createWithPeriod(host, TestPeriod.recruiting());
    }

    public static Challenge createRecruiting(Member host) {
        return createWithPeriod(host, TestPeriod.recruiting());
    }

    public static Challenge createRecruitmentEnded(Member host) {
        return createWithPeriod(host, TestPeriod.recruitmentEnded());
    }

    public static Challenge createOngoing(Member host) {
        return createWithPeriod(host, TestPeriod.ongoing());
    }

    public static Challenge createCompleted(Member host) {
        return createWithPeriod(host, TestPeriod.completed());
    }

    private static Challenge createWithPeriod(Member host, TestPeriod period) {
        ChallengeDefaultValues defaults = ChallengeDefaultValues.INSTANCE;
        return new Challenge(
                host,
                defaults.title(),
                defaults.content(),
                defaults.imageKey(),
                defaults.challengeType(),
                defaults.minimumEntryFee(),
                period.recruitmentPeriod(),
                period.challengePeriod(),
                defaults.headcountLimit()
        );
    }
}
