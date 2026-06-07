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

    @PostMapping("/sendFriendRequest")
    public void sendFriendRequest(@RequestBody FriendRequestDTO request){
        friendRequestService.sendFriendRequest(request);
    }

    @PostMapping("/acceptFriendRequest")
    public void acceptFriendRequest(@RequestBody FriendRequestDTO request){
        friendRequestService.acceptFriendRequest(request);
    }

    @PostMapping("/rejectFriendRequest")
    public void rejectFriendRequest(@RequestBody FriendRequestDTO request){
        friendRequestService.rejectFriendRequest(request);
    }

    @GetMapping("/request/recived")
    public ApiResponse<List<FriendRequestDTO>> getRecivedRequest(@RequestParam("senderId") String senderId){
        List<FriendRequestDTO> recivedRequest = friendRequestService.getRecivedRequest(senderId);
        return ApiResponse.<List<FriendRequestDTO>>builder()
                .message("Friend requests: ")
                .count((long) recivedRequest.size())
                .data(recivedRequest)
                .build();
    }

}