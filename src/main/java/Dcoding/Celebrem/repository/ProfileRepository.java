package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByMember_Id(Long memberId);
}
