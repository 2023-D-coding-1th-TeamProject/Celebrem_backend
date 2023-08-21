package Dcoding.Celebrem.domain.likes;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Likes {

    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private Member member;

    //--연관관계 메서드--//

    //--비즈니스 로직--//
    /**
     * 찜 추가
     */
    public static Likes createLikes(Profile profile, Member member) {
        Likes likes = new Likes();
        likes.profile = profile;
        likes.member = member;

        //Long likesCount = likes.increaseLikesCount();

        return likes;
    }
    public Long increaseLikesCount(){
        return this.profile.increaseLikesCount();
    }
    /**
     * 찜 취소
     */
    public Long cancelLikes() {
        return this.profile.decreaseLikesCount();
    }
}
