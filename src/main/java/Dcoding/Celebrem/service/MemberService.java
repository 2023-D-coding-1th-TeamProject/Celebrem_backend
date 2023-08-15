package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.dto.member.MemberCreateRequestDto;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import Dcoding.Celebrem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()){
            return optionalMember.get();
        }
        return null;
    }

    /**
     *  닉네임 기반 검색
     */
    public Page<Member> findInfluencerByName(String nickname, Pageable pageable) {
        return memberRepository.findAllByAuthorityAndNicknameContaining(Authority.ROLE_INFLUENCER, nickname, pageable);
    }
}
