package com.unsent.api.repository;

import com.unsent.api.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query(value = "SELECT fr.* FROM friend_requests fr " +
            "INNER JOIN (" +
            "SELECT sender_id, MAX(record_id) AS max_record_id " +
            "FROM friend_requests " +
            "WHERE receiver_id = :receiverId " +
            "GROUP BY sender_id" +
            ") latest ON fr.record_id = latest.max_record_id",
            nativeQuery = true)
    List<FriendRequest> findLatestRequestsByReceiverId(@Param("receiverId") String receiverId);

    Optional<FriendRequest> findTopBySenderIdAndReceiverIdOrderByHostTsDesc(String senderId, String receiverId);
}
