package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     *  닉네임 기반 검색
     */
    public Page<Member> findInfluencerByName(String nickname, Pageable pageable) {
        return memberRepository.findAllByAuthorityAndNicknameContaining(Authority.ROLE_INFLUENCER, nickname, pageable);
    }
}
