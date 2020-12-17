package com.scf.erdos.rabbitmq.producer;

import com.scf.erdos.rabbitmq.common.DetailResponse;

public interface MessageProducer {


    DetailResponse send(Object message);

}