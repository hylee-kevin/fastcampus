package com.threetier.team1.rollingpaper.DTO;

import com.threetier.team1.rollingpaper.domain.Paper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class PaperDTO {
    @Getter
    @AllArgsConstructor
    public static class CreatePaperInfo {
        @NotBlank(message = "닉네임은 필수 항목입니다.")
        private String nickname;
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        private String password;
        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowListInfo {
        private Long id;
        private String nickname;
        private String content;

        public static ShowListInfo fromEntity(Paper paper) {
            return ShowListInfo.builder()
                    .id(paper.getId())
                    .nickname(paper.getNickname())
                    .content(paper.getContent())
                    .build();
        }
    }
    @Getter
    public static class DeletePaperInfo {
        private Long id;
        private String password;
    }
}
