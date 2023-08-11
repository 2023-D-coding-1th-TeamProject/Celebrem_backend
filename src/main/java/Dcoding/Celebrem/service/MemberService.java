package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.Member;
import Dcoding.Celebrem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findMemberById(Long id){
        return memberRepository.findById(id).get();
    }
}
