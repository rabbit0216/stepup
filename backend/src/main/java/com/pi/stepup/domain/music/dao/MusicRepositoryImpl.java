package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.Music;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MusicRepositoryImpl implements MusicRepository {
    private final EntityManager em;

    @Override
    public Music insert(Music music) {
        em.persist(music);
        return music;
    }

    @Override
    public Optional<Music> findOne(Long musicId) {
        Optional<Music> music = null;

        try {
            music = Optional.ofNullable(em.find(Music.class, musicId));
        } catch (NoResultException e) {
            music = Optional.empty();
        } finally {
            return music;
        }
    }

    @Override
    public List<Music> findAll() {
        return em.createQuery(
                        "SELECT m FROM Music m", Music.class
                )
                .getResultList();
    }

    @Override
    public List<Music> findAllByKeyword(String keyword) {
        return em.createQuery(
                        "SELECT m FROM Music m " +
                                "WHERE m.title LIKE concat('%', :keyword, '%') OR " +
                                "m.artist LIKE concat('%', :keyword, '%')", Music.class
                )
                .setParameter("keyword", keyword)
                .getResultList();
    }
}
