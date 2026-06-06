package com.unsent.api.repository;

import com.unsent.api.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query(value = "SELECT fr.* FROM friend_requests fr INNER JOIN " +
            "(SELECT receiver_id, MAX(record_id) AS max_record_id FROM friend_requests " +
            "WHERE sender_id = :senderId GROUP BY receiver_id) " +
            "latest ON fr.record_id = latest.max_record_id",
            nativeQuery = true
    )
    List<FriendRequest> findLatestRequestsBySenderId(@Param("senderId") String senderId);
}
