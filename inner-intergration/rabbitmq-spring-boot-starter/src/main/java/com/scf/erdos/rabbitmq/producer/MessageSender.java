package com.scf.erdos.rabbitmq.producer;

import com.scf.erdos.rabbitmq.common.DetailResponse;

public interface MessageSender {


    DetailResponse send(Object message);

    DetailResponse send(MessageWithTime messageWithTime);
}