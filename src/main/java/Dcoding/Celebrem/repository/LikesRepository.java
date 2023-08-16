package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.likes.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
