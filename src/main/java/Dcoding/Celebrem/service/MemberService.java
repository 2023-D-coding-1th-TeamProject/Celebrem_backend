package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.common.util.SecurityUtil;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void secession() {
        Member member = memberRepository.findMemberByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다."));
        memberRepository.delete(member);
    }
}
