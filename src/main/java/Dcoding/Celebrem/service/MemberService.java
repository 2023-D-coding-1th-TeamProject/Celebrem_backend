package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NotFoundException("회원(id: " + id + ")를 찾을 수 없습니다.")
        );
        return member;
    }

    public Member findByEmailFetchProfile(String email) {
        Member member = memberRepository.findByEmailFetchProfile(email).orElseThrow(
                () -> new NotFoundException("회원(email: " + email + ")를 찾을 수 없습니다.")
        );
        return member;
    }

    public Member findByIdFetchProfile(Long memberId) {
        Member member = memberRepository.findByIdFetchProfile(memberId).orElseThrow(
                () -> new NotFoundException("회원(id: " + memberId + ")를 찾을 수 없습니다.")
        );
        return member;
    }

    /**
     *  닉네임 기반 검색
     */
    public Page<Member> findInfluencerByName(String nickname, Pageable pageable) {
        return memberRepository.findAllByAuthorityAndNicknameContaining(Authority.ROLE_INFLUENCER, nickname, pageable);
    }
}
