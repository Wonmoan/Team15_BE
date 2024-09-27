package kakao.rebit.challenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kakao.rebit.common.persistence.BaseEntity;
import kakao.rebit.member.entity.Member;

@Entity
@Table(name = "challenge_certification")
public class ChallengeCertification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String imageUrl;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected ChallengeCertification() {
    }

    public ChallengeCertification(String title, String imageUrl, String content,
        Challenge challenge, Member member) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.challenge = challenge;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Member getMember() {
        return member;
    }
}