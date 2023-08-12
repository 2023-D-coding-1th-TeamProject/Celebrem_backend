package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.dto.member.MemberCreateRequestDto;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import Dcoding.Celebrem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;


}
