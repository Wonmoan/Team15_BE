package kakao.rebit.feed.repository;

import java.util.Set;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Likes;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT l FROM Likes l JOIN FETCH l.member WHERE l.feed = :feed")
    Page<Likes> findAllByFeedWithMember(@Param("feed") Feed feed, Pageable pageable);

    @Query("SELECT l.feed.id FROM Likes l WHERE l.member = :member")
    Set<Long> findFeedIdsByMember(@Param("member") Member member);

    Boolean existsByMemberAndFeed(Member member, Feed feed);

    void deleteByMemberAndFeed(Member member, Feed feed);
}
