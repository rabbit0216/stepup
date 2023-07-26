package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.DanceMusic;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.domain.Reservation;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceReserveRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.music.dao.MusicRepository;
import com.pi.stepup.domain.music.domain.Music;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DanceServiceImpl implements DanceService {

    private final DanceRepository danceRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm");

    @Override
    @Transactional
    public void create(DanceCreateRequestDto danceCreateRequestDto) {
        User host = userRepository.findById(danceCreateRequestDto.getHostId()).orElseThrow();
        RandomDance randomDance = danceCreateRequestDto.toEntity(host);

        List<Long> danceMusicIdList = danceCreateRequestDto.getDanceMusicIdList();
        for (int i = 0; i < danceMusicIdList.size(); i++) {
            Music music = musicRepository.findOne(danceMusicIdList.get(i)).orElseThrow();
            DanceMusic danceMusic = DanceMusic.createDanceMusic(music);
            randomDance.addDanceMusicAndSetThis(danceMusic);
        }

        RandomDance createdDance = danceRepository.insert(randomDance);
        log.debug("create: {}", createdDance);
    }

    @Override
    public DanceFindResponseDto readOne(Long randomDanceId) {
        RandomDance randomDance = danceRepository.findOne(randomDanceId).orElseThrow();
        return DanceFindResponseDto.builder().randomDance(randomDance).build();
    }

    @Override
    @Transactional
    public void update(DanceUpdateRequestDto danceUpdateRequestDto) {
        User host = userRepository.findById(danceUpdateRequestDto.getHostId()).orElseThrow();
        RandomDance randomDance = danceUpdateRequestDto.toEntity(host);
        RandomDance updateDance = danceRepository.update(randomDance);
        log.debug("update: {}", updateDance);
    }

    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        danceRepository.delete(randomDanceId);
    }

    @Override
    public List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId) {
        List<MusicFindResponseDto> allDanceMusic = new ArrayList<>();

        List<DanceMusic> danceMusicList = danceRepository.findAllDanceMusic(randomDanceId);
        for (int i = 0; i < danceMusicList.size(); i++) {
            Long musicId = danceMusicList.get(i).getMusic().getMusicId();
            Music music = musicRepository.findOne(musicId).orElseThrow();
            MusicFindResponseDto musicFindResponseDto = MusicFindResponseDto.builder()
                .music(music).build();
            allDanceMusic.add(musicFindResponseDto);
        }

        return allDanceMusic;
    }

    @Override
    public List<DanceFindResponseDto> readAllMyOpenDance(String id) {
        List<DanceFindResponseDto> allMyOpenDance = new ArrayList<>();

        List<RandomDance> randomDanceList = danceRepository.findAllMyOpenDance(id);
        for (int i = 0; i < randomDanceList.size(); i++) {
            RandomDance randomDance = randomDanceList.get(i);
            DanceFindResponseDto danceFindResponseDto
                = DanceFindResponseDto.builder().randomDance(randomDance).build();
            allMyOpenDance.add(danceFindResponseDto);
        }
        return allMyOpenDance;
    }

    // TODO : 자기가 개최한 거에 예약 못 하도록
    @Override
    public void createReservation(DanceReserveRequestDto danceReserveRequestDto) {
        User host = userRepository.findById(danceReserveRequestDto.getId()).orElseThrow();
        RandomDance randomDance = danceRepository.findOne(danceReserveRequestDto.getRandomDanceId())
            .orElseThrow();

        Reservation reservation
            = Reservation.builder().randomDance(randomDance).user(host).build();

        Reservation createReservation = danceRepository.insertReservation(reservation);
        log.debug("reserve: {}", createReservation);
    }

    @Override
    public void deleteReservation(Long randomDanceId, String id) {
        Long userId = userRepository.findById(id).orElseThrow().getUserId();
        Reservation reservation = danceRepository.findReservation(randomDanceId, userId).orElseThrow();
//        danceRepository.findOne(reservation.getReservationId());
        danceRepository.deleteReserevation(reservation.getReservationId());
    }

}