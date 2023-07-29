package com.pi.stepup.domain.board.dto.notice;

import com.pi.stepup.domain.board.domain.Notice;
import lombok.Builder;
import lombok.Getter;

public class NoticeResponseDto {
    @Getter
    public static class NoticeInfoResponseDto {
        private final Long boardId;
        private final String title;
        private final String content;
        private final String writerName;
        private final String profileImg;
        private final String fileURL;
        private final String boardType;
        private final Long randomDanceId;

        @Builder
        public NoticeInfoResponseDto(Notice notice) {
            this.boardId = notice.getBoardId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.writerName = notice.getWriter().getNickname();
            this.profileImg = notice.getWriter().getProfileImg();
            this.fileURL = notice.getFileURL();
            this.boardType = notice.getBoardType();
            this.randomDanceId = notice.getRandomDance().getRandomDanceId();
        }
    }
}