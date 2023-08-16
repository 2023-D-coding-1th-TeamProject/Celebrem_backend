package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
