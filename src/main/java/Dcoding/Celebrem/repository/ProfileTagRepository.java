package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.tag.ProfileTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileTagRepository extends JpaRepository<ProfileTag, Long> {

    List<ProfileTag> findAllByProfile_Id(Long profileId);
}
