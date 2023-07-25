package com.pi.stepup.domain.music.service;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicApplyServiceTest {
    @InjectMocks
    MusicApplyServiceImpl musicApplyService;

    @Mock
    MusicApplyRepository musicApplyRepository;

    @Mock
    UserRepository userRepository;

    private MusicApplySaveRequestDto musicApplySaveRequestDto;
    private MusicApply musicApply;
    private User user;

    @Test
    @BeforeEach
    public void init() {
        musicApplySaveRequestDto = MusicApplySaveRequestDto.builder()
                .artist("artist")
                .title("title")
                .content("content")
                .writerId("writer")
                .build();

        user = User.builder()
                .id("writer")
                .build();

        musicApply = MusicApply.builder()
                .title(musicApplySaveRequestDto.getTitle())
                .artist(musicApplySaveRequestDto.getArtist())
                .content(musicApplySaveRequestDto.getContent())
                .writer(user)
                .build();
    }

    @Test
    @DisplayName("노래 신청 등록 테스트")
    public void createMusicApplyServiceTest() {
        when(musicApplyRepository.insert(any(MusicApply.class))).thenReturn(musicApply);
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        MusicApply result = musicApplyService.create(musicApplySaveRequestDto);
        assertThat(result.getTitle()).isEqualTo(musicApply.getTitle());
    }

    @Test
    @DisplayName("노래 신청 목록 조회 테스트")
    public void readAllMusicApplyServiceTest() {
        String keyword = "";
        List<MusicApply> makedMusicApply = makeMusicApply();
        doReturn(makedMusicApply)
                .when(musicApplyRepository).findAll(keyword);

        List<MusicApplyFindResponseDto> musicApplies = musicApplyService.readAll(keyword);
        assertThat(musicApplies.size()).isEqualTo(makedMusicApply.size());
    }

    @Test
    @DisplayName("노래 신청 목록 키워드 조회 테스트")
    public void readAllMusicApplyByKeywordServiceTest() {
        String keyword = "1";
        List<MusicApply> madeMusicApply = makeMusicApply();
        List<MusicApply> keywordMusicApply = makeMusicApplyByKeyword(madeMusicApply, keyword);

        doReturn(keywordMusicApply)
                .when(musicApplyRepository).findAll(keyword);

        List<MusicApplyFindResponseDto> musicApplies = musicApplyService.readAll(keyword);
        assertThat(musicApplies.size()).isEqualTo(keywordMusicApply.size());
    }

    @Test
    @DisplayName("노래 신청 상세 조회 테스트")
    public void readOneMusicApplyServiceTest() {
        when(musicApplyRepository.findOne(any())).thenReturn(Optional.of(musicApply));

        MusicApplyFindResponseDto result = musicApplyService.readOne(musicApply.getMusicApplyId());

        assertThat(result.getTitle()).isEqualTo(musicApply.getTitle());
    }

    @Test
    @DisplayName("노래 신청 삭제 테스트")
    public void deleteMusicApplyServiceTest() {
        Long musicId = 1L;

        musicApplyService.delete(musicId);

        verify(musicApplyRepository, only()).delete(musicId);
    }

    private List<MusicApply> makeMusicApply() {
        List<MusicApply> musicApplies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MusicApply tmp = MusicApply.builder().
                    title("title" + i)
                    .artist("artist" + (i + 1))
                    .writer(user)
                    .build();
            musicApplies.add(tmp);
        }
        return musicApplies;
    }

    private List<MusicApply> makeMusicApplyByKeyword(List<MusicApply> musicApplies, String keyword) {
        List<MusicApply> result = new ArrayList<>();

        for (MusicApply m : musicApplies) {
            if (m.getTitle().contains(keyword) || m.getArtist().contains(keyword)) {
                result.add(m);
            }
        }
        return result;
    }

}