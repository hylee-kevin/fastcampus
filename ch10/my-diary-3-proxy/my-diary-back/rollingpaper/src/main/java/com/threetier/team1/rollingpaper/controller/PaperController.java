package com.threetier.team1.rollingpaper.controller;

import com.threetier.team1.rollingpaper.DTO.ApiResponseDTO;
import com.threetier.team1.rollingpaper.DTO.PaperDTO;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.CreatePaperInfo;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.DeletePaperInfo;
import com.threetier.team1.rollingpaper.DTO.PaperDTO.ShowListInfo;
import com.threetier.team1.rollingpaper.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*", allowedHeaders = "*", methods = {GET,POST,PUT,DELETE})
public class PaperController {

    private final PaperService paperService;

    @GetMapping("/")
    public ResponseEntity<List<ShowListInfo>> getAllPapers() {
        List<ShowListInfo> papers = paperService.getList();
        return ResponseEntity.status(HttpStatus.OK).body(papers);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createPaper(@RequestBody @Valid CreatePaperInfo createPaperInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO("bad_request"));
        }

        paperService.write(createPaperInfo);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO("success"));
    }

    @DeleteMapping("/")
    public ResponseEntity<Object> deletePaper(@RequestBody DeletePaperInfo deletePaperInfo) {
        int result = paperService.delete(deletePaperInfo);
        if(result == 200) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDTO("success"));
        }
        if(result == 406) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiResponseDTO("not_acceptable"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDTO("unauthorized"));
    }
}
