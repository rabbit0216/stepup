package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceCreateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceReserveRequestDto;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceUpdateRequestDto;
import com.pi.stepup.domain.dance.dto.DanceResponseDto.DanceFindResponseDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicFindResponseDto;
import java.util.List;

public interface DanceService {

    void create(DanceCreateRequestDto danceCreateRequestDto);

    DanceFindResponseDto readOne(Long randomDanceId);

    void update(DanceUpdateRequestDto danceUpdateRequestDto);

    void delete(Long randomDanceId);

    List<MusicFindResponseDto> readAllDanceMusic(Long randomDanceId);

    List<DanceFindResponseDto> readAllMyOpenDance(String id);

    void createReservation(DanceReserveRequestDto danceReservationRequestDto);

    void deleteReservation(Long reservationId, String id);
}
