package kakao.rebit.challenge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kakao.rebit.common.domain.ImageKeyAccessor;
import kakao.rebit.common.persistence.BaseEntity;

@Entity
@Table(name = "challenge_verification", indexes = {
        @Index(name = "idx_challenge_verification_created_at", columnList = "createdAt")
})
public class ChallengeVerification extends BaseEntity implements ImageKeyAccessor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imageKey;

    @Column(length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_participation_id")
    private ChallengeParticipation challengeParticipation;

    protected ChallengeVerification() {
    }

    public ChallengeVerification(String title, String imageKey, String content, ChallengeParticipation challengeParticipation) {
        this.title = title;
        this.imageKey = imageKey;
        this.content = content;
        this.challengeParticipation = challengeParticipation;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getImageKey() {
        return imageKey;
    }

    public String getContent() {
        return content;
    }

    public ChallengeParticipation getChallengeParticipation() {
        return challengeParticipation;
    }
}
