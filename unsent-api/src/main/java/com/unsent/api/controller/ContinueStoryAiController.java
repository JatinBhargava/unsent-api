package com.unsent.api.controller;

import com.unsent.api.dto.ContinueStoryDTO;
import com.unsent.api.service.ContinueStoryAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/diary/ai")
public class ContinueStoryAiController {

    private final ContinueStoryAiService continueStoryAiService;

    @PostMapping("/story/continue")
    public ContinueStoryDTO continueStory(@RequestBody ContinueStoryDTO request){
        return continueStoryAiService.continueStory(request);
    }

}
