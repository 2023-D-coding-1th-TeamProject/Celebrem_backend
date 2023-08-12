package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
