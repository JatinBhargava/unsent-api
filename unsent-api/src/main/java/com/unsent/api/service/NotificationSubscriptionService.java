package com.unsent.api.service;

import com.unsent.api.dto.NotificationSubscriptionDTO;
import com.unsent.api.entity.NotificationSubscription;
import com.unsent.api.helper.SequenceService;
import com.unsent.api.repository.NotificationSubscriptionRepository;
import com.unsent.util.CrudOperation;
import com.unsent.util.NotificationSubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSubscriptionService {

    private final NotificationSubscriptionRepository notificationSubscriptionRepository;
    private final SequenceService sequenceService;

    public NotificationSubscriptionDTO subscribe(NotificationSubscriptionDTO request){

        isUserSubscribed(request.getEmail());
        NotificationSubscription notificationSubscription = NotificationSubscription.builder()
                .subscriptionId(sequenceService.nextNotificationSubscriptionSequenceValue())
                .email(request.getEmail())
                .userId(request.getUserId())
                .email(request.getEmail().trim().toLowerCase())
                .status(NotificationSubscriptionStatus.ACTIVE.getCode())
                .build();

        NotificationSubscription saveSubscription = notificationSubscriptionRepository.save(notificationSubscription);

        return mappedToNotificationDTO(saveSubscription);

    }

    private boolean isUserSubscribed(final String email){
        return notificationSubscriptionRepository.existsByEmailIgnoreCase(email);
    }

    private NotificationSubscriptionDTO mappedToNotificationDTO(NotificationSubscription subscription){
        return NotificationSubscriptionDTO.builder()
                .subscriptionId(subscription.getSubscriptionId())
                .userId(subscription.getUserId())
                .email(subscription.getEmail())
                .status(subscription.getStatus())
                .build();
    }

}
