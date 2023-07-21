package com.batton.memberservice.mq;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final RabbitAdmin rabbitAdmin;

    public void createQueueForMember(String memberId) {
        Queue queue = new Queue("user.queue." + memberId, true, false, false);
        rabbitAdmin.declareQueue(queue);
    }
}
