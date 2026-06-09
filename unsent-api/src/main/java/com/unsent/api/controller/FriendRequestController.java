package com.unsent.api.controller;

import com.unsent.api.dto.FriendRequestDTO;
import com.unsent.api.service.FriendRequestService;
import com.unsent.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping("/send/friend/request")
    public void sendFriendRequest(@RequestBody FriendRequestDTO request){
        friendRequestService.sendFriendRequest(request);
    }

    @PostMapping("/accept/friend/request")
    public void acceptFriendRequest(@RequestBody FriendRequestDTO request){
        friendRequestService.acceptFriendRequest(request);
    }

    @PostMapping("/reject/friend/request")
    public void rejectFriendRequest(@RequestBody FriendRequestDTO request){
        friendRequestService.rejectFriendRequest(request);
    }

    @GetMapping("/request/recived")
    public ApiResponse<List<FriendRequestDTO>> getRecivedRequest(@RequestParam("receiverId") String receiverId){
        List<FriendRequestDTO> recivedRequest = friendRequestService.getRecivedRequest(receiverId);
        return ApiResponse.<List<FriendRequestDTO>>builder()
                .message("Friend requests: ")
                .count((long) recivedRequest.size())
                .data(recivedRequest)
                .build();
    }

    @GetMapping(("/request/status"))
    public FriendRequestDTO getStatusBetweenSenderAndReceiver(
            @RequestParam("senderId") String senderId,
            @RequestParam("receiverId") String receiverId
    ){
        return friendRequestService.getStatusBetweenSenderandReceiver(senderId,receiverId);
    }

}