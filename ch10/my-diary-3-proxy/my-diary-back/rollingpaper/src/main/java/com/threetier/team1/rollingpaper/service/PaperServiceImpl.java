package com.threetier.team1.rollingpaper.service;

import com.threetier.team1.rollingpaper.DTO.PaperDTO;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.CreatePaperInfo;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.DeletePaperInfo;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.ShowListInfo;
import com.threetier.team1.rollingpaper.domain.Paper;
import com.threetier.team1.rollingpaper.repository.PaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaperServiceImpl implements PaperService{

    private final PaperRepository paperRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ShowListInfo> getList() {
        List<Paper> papers = paperRepository.findAll();
        return papers.stream().map(ShowListInfo::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void write(CreatePaperInfo createPaperInfo) {
            Paper paper = Paper.fromDTO(createPaperInfo);
            paperRepository.save(paper);
    }

    @Override
    public int delete(DeletePaperInfo deletePaperInfo) {
        Optional<Paper> optional = paperRepository.findById(deletePaperInfo.getId());

        if(optional.isEmpty()) {
            return 406;
        }
        Paper paper = optional.get();
        if(paper.getPassword().equals(deletePaperInfo.getPassword())) {
            paperRepository.deleteById(paper.getId());
            return 200;
        }
        return 403;
    }
}
