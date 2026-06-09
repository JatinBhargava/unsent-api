package com.unsent.api.controller;

import com.unsent.api.dto.FriendDTO;
import com.unsent.api.service.FriendService;
import com.unsent.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/relationship")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/user/friend/list")
    public ApiResponse<Object> getFriendListByUserId(
            @RequestParam("userId") String userId
    ){
        List<FriendDTO> list = friendService.getListOfFriendByUserId(userId);
        return ApiResponse.builder()
                .message("Friend List for user: " + userId)
                .count((long) list.size())
                .data(list)
                .build();
    }

}
