package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.tag.ProfileTag;
import Dcoding.Celebrem.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileTagRepository extends JpaRepository<ProfileTag, Long> {

    Optional<ProfileTag> findByTag(Tag tag);
}
