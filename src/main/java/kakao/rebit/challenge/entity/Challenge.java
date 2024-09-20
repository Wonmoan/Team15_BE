package kakao.rebit.challenge.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.entity.Member;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "challenge")
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    private Integer minimumEntryFee;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "recruitment_start_date")),
            @AttributeOverride(name = "endDate", column = @Column(name = "recruitment_end_date"))
    })
    private Period recruitmentPeriod;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "challenge_start_date")),
            @AttributeOverride(name = "endDate", column = @Column(name = "challenge_end_date"))
    })
    private Period challengePeriod;

    @Embedded
    private HeadcountLimit headcountLimit;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT COUNT(1) FROM challenge_participant cp WHERE cp.challenge_id = id)")
    private int currentHeadcount;

    protected Challenge() {
    }

    public Challenge(Member member, String title, String content, String imageUrl, ChallengeType type, Integer minimumEntryFee,
            Period recruitmentPeriod,
            Period challengePeriod, HeadcountLimit headcountLimit) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.type = type;
        this.minimumEntryFee = minimumEntryFee;
        this.recruitmentPeriod = recruitmentPeriod;
        this.challengePeriod = challengePeriod;
        this.headcountLimit = headcountLimit;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ChallengeType getType() {
        return type;
    }

    public Integer getMinimumEntryFee() {
        return minimumEntryFee;
    }

    public Period getRecruitmentPeriod() {
        return recruitmentPeriod;
    }

    public Period getChallengePeriod() {
        return challengePeriod;
    }

    public HeadcountLimit getHeadcountLimit() {
        return headcountLimit;
    }

    public int getCurrentHeadcount() {
        return currentHeadcount;
    }

    public boolean isRecruiting(LocalDateTime now) {
        return recruitmentPeriod.contains(now);
    }

    public boolean isInProgress(LocalDateTime now) {
        return challengePeriod.contains(now);
    }

    public boolean isCompleted(LocalDateTime now) {
        return challengePeriod.isAfter(now);
    }

    public boolean isWithinHeadcountLimit(int currentHeadcount) {
        return headcountLimit.isWithinLimit(currentHeadcount);
    }
}