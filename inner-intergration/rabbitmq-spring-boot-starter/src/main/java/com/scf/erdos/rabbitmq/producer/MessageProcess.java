package com.scf.erdos.rabbitmq.producer;



import com.scf.erdos.rabbitmq.common.DetailResponse;


public interface MessageProcess<T> {
    DetailResponse process(T message);
}
