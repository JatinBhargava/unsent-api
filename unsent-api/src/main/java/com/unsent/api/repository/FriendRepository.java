package com.unsent.api.repository;

import com.unsent.api.dto.FriendDTO;
import com.unsent.api.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend,Long> {

    List<Friend> getFriendListByUserId(String userId);

}
