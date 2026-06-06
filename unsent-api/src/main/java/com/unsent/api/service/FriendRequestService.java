package com.unsent.api.service;


import com.unsent.api.dto.FriendRequestDTO;
import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.FriendRequest;
import com.unsent.api.entity.User;
import com.unsent.api.repository.FriendRequestRepository;
import com.unsent.util.FriendRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public void sendFriendRequest(FriendRequestDTO request){
        validateSenderandReciver(request);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderId(request.getSender_id());
        friendRequest.setReceiverId(request.getReceiver_id());
        friendRequest.setRequestStatus(FriendRequestStatus.PENDING.getCode());
        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(FriendRequestDTO request){
        validateSenderandReciver(request);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderId(request.getSender_id());
        friendRequest.setReceiverId(request.getReceiver_id());
        friendRequest.setRequestStatus(FriendRequestStatus.ACCEPTED.getCode());
        friendRequestRepository.save(friendRequest);
    }

    public void rejectFriendRequest(FriendRequestDTO request){
        validateSenderandReciver(request);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderId(request.getSender_id());
        friendRequest.setReceiverId(request.getReceiver_id());
        friendRequest.setRequestStatus(FriendRequestStatus.REJECTED.getCode());
        friendRequestRepository.save(friendRequest);
    }

    public boolean validateSenderandReciver(FriendRequestDTO request){

        UserDTO isSenderExist = userService.findByUserId(request.getSender_id());
        UserDTO isReciverExist = userService.findByUserId(request.getSender_id());

        if(Optional.ofNullable(isSenderExist).isPresent()
                && Optional.ofNullable(isReciverExist).isPresent())
            return true;
        return false;
    }

    public List<FriendRequestDTO> getRecivedRequest(String senderId){

        List<FriendRequest> senderPendingRequest =
                friendRequestRepository.findLatestRequestsBySenderId(senderId);

        return  senderPendingRequest.stream().map(this::mapSenderandReciverDetails).toList();
    }

    private FriendRequestDTO mapSenderandReciverDetails(FriendRequest friendRequest) {
        return FriendRequestDTO.builder()
                .sender_id(friendRequest.getSenderId())
                .receiver_id(friendRequest.getReceiverId())
                .request_status(friendRequest.getRequestStatus())
                .build();
    }


}
