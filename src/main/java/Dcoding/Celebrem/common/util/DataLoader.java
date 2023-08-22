package Dcoding.Celebrem.common.util;

import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.repository.LikesRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileTagRepository;
import Dcoding.Celebrem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final TagRepository tagRepository;
    private final ProfileTagRepository profileTagRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        tagRepository.save(new Tag("게임"));
        tagRepository.save(new Tag("스포츠"));
        tagRepository.save(new Tag("뷰티"));
        tagRepository.save(new Tag("건강"));
        tagRepository.save(new Tag("언어"));
        tagRepository.save(new Tag("IT"));
        tagRepository.save(new Tag("음식"));
    }
}
