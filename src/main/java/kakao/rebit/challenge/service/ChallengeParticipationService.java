package kakao.rebit.challenge.service;

import kakao.rebit.challenge.dto.ChallengeParticipationMemberResponse;
import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.dto.ChallengeResponse;
import kakao.rebit.challenge.entity.Challenge;
import kakao.rebit.challenge.entity.ChallengeParticipation;
import kakao.rebit.challenge.exception.participation.ParticipationAlreadyExistsException;
import kakao.rebit.challenge.exception.participation.ParticipationNotFoundException;
import kakao.rebit.challenge.exception.participation.ParticipationNotParticipantException;
import kakao.rebit.challenge.repository.ChallengeParticipationRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import kakao.rebit.s3.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChallengeParticipationService {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final S3Service s3Service;
    private final ChallengeParticipationRepository challengeParticipationRepository;

    public ChallengeParticipationService(MemberService memberService,
            ChallengeService challengeService,
            S3Service s3Service,
            ChallengeParticipationRepository challengeParticipationRepository) {
        this.memberService = memberService;
        this.challengeService = challengeService;
        this.s3Service = s3Service;
        this.challengeParticipationRepository = challengeParticipationRepository;
    }

    @Transactional(readOnly = true)
    public Page<ChallengeParticipationMemberResponse> getChallengeParticipationsById(Long challengeId, Pageable pageable) {
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Page<ChallengeParticipation> challengeParticipants =
                challengeParticipationRepository.findAllByChallenge(challenge, pageable);
        return challengeParticipants.map(this::toParticipationMemberResponse);
    }

    @Transactional(readOnly = true)
    public ChallengeParticipationMemberResponse getChallengeParticipationById(Long participantId) {
        ChallengeParticipation challengeParticipation = findChallengeParticipationByIdOrThrow(participantId);
        return toParticipationMemberResponse(challengeParticipation);
    }

    private ChallengeParticipation findChallengeParticipationByIdOrThrow(Long participantId) {
        return challengeParticipationRepository.findById(participantId)
                .orElseThrow(() -> ParticipationNotFoundException.EXCEPTION);
    }

    @Transactional(readOnly = true)
    public ChallengeParticipation findChallengeParticipationByMemberAndChallengeOrThrow(Member member, Challenge challenge) {
        return challengeParticipationRepository.findByMemberAndChallenge(member, challenge)
                .orElseThrow(() -> ParticipationNotFoundException.EXCEPTION);
    }

    @Transactional
    public Long createChallengeParticipation(MemberResponse memberResponse, Long challengeId,
            ChallengeParticipationRequest challengeParticipationRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Challenge challenge = challengeService.findChallengeByIdOrThrow(challengeId);
        Integer entryFee = challengeParticipationRequest.entryFee();

        if (challengeParticipationRepository.existsByChallengeAndMember(challenge, member)) {
            throw ParticipationAlreadyExistsException.EXCEPTION;
        }

        // 참여 가능 여부 검증 수행
        ChallengeParticipation challengeParticipation = ChallengeParticipation.of(challenge, member, entryFee);
        member.usePoints(entryFee); // 포인트 차감

        return challengeParticipationRepository.save(challengeParticipation).getId();
    }

    @Transactional
    public void cancelParticipation(MemberResponse memberResponse, Long participantId) {
        ChallengeParticipation challengeParticipation = findChallengeParticipationByIdOrThrow(participantId);

        if (!challengeParticipation.getMember().getId().equals(memberResponse.id())) {
            throw ParticipationNotParticipantException.EXCEPTION;
        }

        challengeParticipationRepository.delete(challengeParticipation);
    }

    private ChallengeParticipationMemberResponse toParticipationMemberResponse(ChallengeParticipation challengeParticipation) {
        Member member = challengeParticipation.getMember();
        return new ChallengeParticipationMemberResponse(
                challengeParticipation.getId(),
                member.getId(),
                member.getNickname(),
                member.getImageKey(),
                s3Service.getDownloadUrl(member.getImageKey()).presignedUrl(),
                challengeParticipation.getCreatedAt(),
                challengeParticipation.getEntryFee()
        );
    }

    public Page<ChallengeResponse> getMyChallenges(MemberResponse memberResponse, Pageable pageable) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Page<ChallengeParticipation> challengeParticipations =
                challengeParticipationRepository.findAllByMember(member, pageable);
        return challengeParticipations.map(ChallengeParticipation::getChallenge)
                .map(challengeService::toChallengeResponse);
    }
}
