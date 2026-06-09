package com.unsent.api.service;


import com.unsent.api.dto.FriendRequestDTO;
import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.FriendRequest;
import com.unsent.api.entity.User;
import com.unsent.api.repository.FriendRequestRepository;
import com.unsent.helper.BusinessException;
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

    public static final String FR001 = "FR001";
    public static final String FRIEND_REQUEST_ALREADY_PENDING =
            "Friend request already pending";

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public void sendFriendRequest(FriendRequestDTO request){
        validateSenderandReciver(request);
        isPendingRequestValid(request);
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

    public List<FriendRequestDTO> getRecivedRequest(String receiverId){

        List<FriendRequest> senderPendingRequest =
                friendRequestRepository.findLatestRequestsByReceiverId(receiverId);

        return  senderPendingRequest.stream().map(this::mapSenderandReciverDetails).toList();
    }

    public FriendRequestDTO getStatusBetweenSenderandReceiver(FriendRequestDTO request){

        String status = "";
        Optional<FriendRequest> latestRecordOfSenderAndReceiver =
                friendRequestRepository
                        .findTopBySenderIdAndReceiverIdOrderByHostTsDesc(request.getSender_id(),
                                request.getReceiver_id());

        if(latestRecordOfSenderAndReceiver.isPresent()
        && latestRecordOfSenderAndReceiver.get().getRequestStatus()
                .equals(FriendRequestStatus.PENDING.getCode())){
            status = FriendRequestStatus.PENDING.getCode();
        }
        else if (latestRecordOfSenderAndReceiver.isPresent()
                && latestRecordOfSenderAndReceiver.get().getRequestStatus()
                .equals(FriendRequestStatus.ACCEPTED.getCode())) {
            status = FriendRequestStatus.ACCEPTED.getCode();
        }
        else if (latestRecordOfSenderAndReceiver.isPresent()
                && latestRecordOfSenderAndReceiver.get().getRequestStatus()
                .equals(FriendRequestStatus.REJECTED.getCode())) {
            status = FriendRequestStatus.REJECTED.getCode();
        }
        return FriendRequestDTO.builder().request_status(status).build();
    }

    private FriendRequestDTO mapSenderandReciverDetails(FriendRequest friendRequest) {
        return FriendRequestDTO.builder()
                .sender_id(friendRequest.getSenderId())
                .receiver_id(friendRequest.getReceiverId())
                .request_status(friendRequest.getRequestStatus())
                .build();
    }

    public boolean validateSenderandReciver(FriendRequestDTO request){

        UserDTO isSenderExist = userService.findByUserId(request.getSender_id());
        UserDTO isReciverExist = userService.findByUserId(request.getSender_id());

        if(Optional.ofNullable(isSenderExist).isPresent()
                && Optional.ofNullable(isReciverExist).isPresent())
            return true;
        return false;
    }

    public boolean isPendingRequestValid(FriendRequestDTO request){

        Optional<FriendRequest> user = friendRequestRepository
                .findTopBySenderIdAndReceiverIdOrderByHostTsDesc
                        (request.getSender_id(),request.getReceiver_id());

        if(user.isPresent()){
            boolean isValid = user.get().getRequestStatus()
                    .equals(FriendRequestStatus.PENDING.getCode());

            if(isValid){
                throw new BusinessException(FR001, FRIEND_REQUEST_ALREADY_PENDING);
            }
        }
        return true;
    }


}
