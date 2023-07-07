package com.threetier.team1.rollingpaper.service;

import com.threetier.team1.rollingpaper.DTO.PaperDTO;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.CreatePaperInfo;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.DeletePaperInfo;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.ShowListInfo;
import com.threetier.team1.rollingpaper.domain.Paper;

import java.util.List;

public interface PaperService {
    List<ShowListInfo> getList();

    void write(CreatePaperInfo createPaperInfo);

    int delete(DeletePaperInfo deletePaperInfo);
}
