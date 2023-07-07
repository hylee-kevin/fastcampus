package com.threetier.team1.rollingpaper.service;

import com.threetier.team1.rollingpaper.DTO.PaperDTO;
import com.threetier.team1.rollingpaper.domain.Paper;
import com.threetier.team1.rollingpaper.repository.PaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PaperServiceImplTest {
    @Autowired
    PaperRepository paperRepository;
    long id;

    @BeforeEach
    void init() {
        Paper paper = Paper.fromDTO(new PaperDTO.CreatePaperInfo("kim","1234","hihi!"));
        id = paperRepository.save(paper).getId();
    }

    @Test
    void delete(){
        assertThrows(NoSuchElementException.class, () -> {
            paperRepository.findById(id+100).get();
        });
    }
}