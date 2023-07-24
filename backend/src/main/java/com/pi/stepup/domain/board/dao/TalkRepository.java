package com.pi.stepup.domain.board.dao;

import com.pi.stepup.domain.board.domain.Talk;

import java.util.List;
import java.util.Optional;

public interface TalkRepository {
    Talk insert(Talk talk);

    Optional<Talk> findOne(Long boardId);

    List<Talk> findAll();

    List<Talk> findAllByKeyword(String keyword);

    void delete(Long boardId);
}
