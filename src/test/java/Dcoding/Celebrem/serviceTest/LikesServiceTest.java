//package Dcoding.Celebrem.serviceTest;
//
//import Dcoding.Celebrem.domain.likes.Likes;
//import Dcoding.Celebrem.domain.member.Member;
//import Dcoding.Celebrem.domain.member.Profile;
//import Dcoding.Celebrem.repository.LikesRepository;
//import Dcoding.Celebrem.repository.MemberRepository;
//import Dcoding.Celebrem.repository.ProfileRepository;
//import Dcoding.Celebrem.service.LikesService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Description;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//public class LikesServiceTest {
//
//    @Autowired
//    private LikesService likesService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private ProfileRepository profileRepository;
//
//    @Autowired
//    private LikesRepository likesRepository;
//
//    @Description("addLikes(): 좋아요 클릭 시, likesCount 증가 확인")
//    @WithMockUser(username = "abc@abc")
//    @Test
//    void addLikesTest() throws Exception{
//        //given
//        Profile profile = makeProfile("testId", 100L, "test", "testUrl");
//        Member member = makeMember("abc@abc", "010-010", "password", "nickname");
//
//        //when
//        likesService.addLikes(1L);
//
//        Long actual = profile.getLikeCount();
//
//        //then
//        Assertions.assertEquals(1, actual);
//    }
//
//    @Description("cancelLikes(): 좋아요 취소 시 likesCount 감소 확인과 likes 테이블에서 삭제되는 지 확인")
//    @Test
//    public void cancelLikesTest() throws Exception {
//        //given
//        Profile profile = makeProfile("testId", 100L, "test", "testUrl");
//        Member member = makeMember("abc@abc", "010-010", "password", "nickname");
//
//        //when
////        Long increasedLikesCount = likesService.addLikes(1L, 1L);
//        Long resultLikesCount = likesService.cancelLikes(1L);
//
//        //then
//        Assertions.assertEquals(0, resultLikesCount);
//    }
//
//    @Description("findAll(): memberId로 찜한 목록을 가져올 수 있다.")
//    @Test
//    public void findAllTest() throws Exception {
//        //given
//        Profile profile1 = makeProfile("testId", 100L, "test", "testUrl");
//        Profile profile2 = makeProfile("testId2", 100L, "test2", "testUrl2");
//
//        Member member = makeMember("abc@abc", "010-010", "password", "nickname");
////
////        likesService.addLikes(1L, 1L);
////        likesService.addLikes(1L, 2L);
//
//        //when
//        List<Likes> likesList = likesService.findAll(1L);
//
//        //then
//        Assertions.assertEquals(2, likesList.size());
//    }
//
//    private Member makeMember(String email, String phoneNumber, String password, String nickname) {
//        Member member = new Member(email, phoneNumber, password, nickname, new Profile());
//        memberRepository.save(member);
//        return member;
//    }
//
//    private Profile makeProfile(String instagramId, long followerCount, String description, String profileUrl) {
//        Profile Profile = new Profile(instagramId, followerCount, description, profileUrl);
//        profileRepository.save(Profile);
//        return Profile;
//    }
//}
