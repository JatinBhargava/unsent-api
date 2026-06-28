package com.unsent.api.helper;

import com.unsent.api.repository.SequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceService {

    private final SequenceRepository sequenceRepository;

    public String generateStoryId() {
        Long nextVal = sequenceRepository.getNextStoryId();
        return "ST" + nextVal;
    }

    public Long nextStorySequenceValue(){
        return  sequenceRepository.getNextStoryId();
    }

    public String nextMessageSequenceValue(){
        return  sequenceRepository.getNextMessageId();
    }

    public String nextConversationSequenceValue(){
        return sequenceRepository.getNextConversationId();
    }

    public  String nextStoryContributionSequenceValue(){
        return sequenceRepository.getNextStoryContributionId();
    }
}
