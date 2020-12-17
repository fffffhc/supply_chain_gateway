package com.scf.erdos.rabbitmq.comsumer;


import com.scf.erdos.rabbitmq.common.DetailResponse;

/**
 * Created by littlersmall on 16/5/12.
 */
public interface MessageConsumer {
    DetailResponse consume();
}
