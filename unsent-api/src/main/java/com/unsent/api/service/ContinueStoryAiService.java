package com.unsent.api.service;

import com.unsent.api.aintegration.OpenAiClient;
import com.unsent.api.dto.ContinueStoryDTO;
import com.unsent.api.dto.DiaryEntryResponseDTO;
import com.unsent.api.helper.BuiltPrompt;
import com.unsent.api.repository.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContinueStoryAiService {

    private final DiaryService diaryService;
    private final BuiltPrompt buildPrompthelper;
    private final OpenAiClient openAiClient;

    public ContinueStoryDTO continueStory(ContinueStoryDTO request){

        DiaryEntryResponseDTO diaryEntry =
                diaryService.getEntryById(request.getStoryId());

        String prompt = buildPrompthelper.buildPrompt(diaryEntry.getContent());

        String generatedText = openAiClient.generate(prompt)
                .orElseThrow(() -> new RuntimeException("Failed to generate story continuation"));

        //diaryService.updateEntry();
        return ContinueStoryDTO.builder().generatedContent(generatedText).build();
    }
}
