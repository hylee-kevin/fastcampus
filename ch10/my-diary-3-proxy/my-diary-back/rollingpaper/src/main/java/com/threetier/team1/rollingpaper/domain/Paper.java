package com.threetier.team1.rollingpaper.domain;

import com.threetier.team1.rollingpaper.DTO.PaperDTO;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.CreatePaperInfo;
import lombok.*;

import javax.persistence.*;

@Table(name = "paper")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String content;


    public static Paper fromDTO(CreatePaperInfo createPaperInfo) {
        return Paper.builder()
                .password(createPaperInfo.getPassword())
                .nickname(createPaperInfo.getNickname())
                .content(createPaperInfo.getContent())
                .build();
    }

}
