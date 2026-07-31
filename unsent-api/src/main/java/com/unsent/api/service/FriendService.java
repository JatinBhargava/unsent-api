package com.unsent.api.service;

import com.unsent.api.dto.FriendDTO;
import com.unsent.api.dto.FriendRequestDTO;
import com.unsent.api.entity.Friend;
import com.unsent.api.repository.FriendRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public void saveUserToFriendRelation(FriendRequestDTO request){

        Friend friendship = new Friend();
        friendship.setUserId(request.getSender_id());
        friendship.setFriendId(request.getReceiver_id());
        friendRepository.save(friendship);
    }

    public void saveFriendToUserRelation(FriendRequestDTO request){

        Friend friendship = new Friend();
        friendship.setUserId(request.getReceiver_id());
        friendship.setFriendId(request.getSender_id());
        friendRepository.save(friendship);
    }

    @Cacheable(value = "FRIENDSHIP", key = "#userId")
    public List<FriendDTO> getListOfFriendByUserId(String userId){
        return friendRepository.getFriendListByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private FriendDTO toDto(Friend friend) {
        return FriendDTO.builder()
                .friendId(friend.getFriendId())
                .build();
    }
}
