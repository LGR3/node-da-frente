package com.urban;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

/**
 * Rabbit
 */
public class Rabbit {

    private Channel channel;
    private Connection connection;

    public Rabbit(String address) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(address);
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.connection = connection;

    }

    public Channel getChannel() {
        return this.channel;
    }

    public void publish(String topic, String message) throws IOException {
        this.channel.exchangeDeclare(topic, BuiltinExchangeType.FANOUT);
        this.channel.basicPublish(topic, "", null, message.getBytes("UTF-8"));
    }

    public void subscriber(String topic, Consumer consumer) throws IOException {
        this.subscriber(topic, consumer, false);
    }

    public void subscriber(String topic, Consumer consumer, Boolean durable) throws IOException {
        this.channel.exchangeDeclare(topic, BuiltinExchangeType.FANOUT, durable);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, topic, "");

        new Thread(() -> {
            try {
                this.channel.basicConsume(queueName, true, consumer);
            } catch (Exception e) {
                System.out.println("* Exceção numa thread de subscriber" + "\n" + e.toString());
            }
        }).start();
        // new Thread(new SubscriberThread(this.channel, queueName, consumer)).start();
    }

    public void close() {
        try {
            this.channel.close();
            this.connection.close();
        } catch (Exception e) {
        }
    }

}

// class SubscriberThread implements Runnable {
// private Channel channel;
// private String queueName;
// private Consumer consumer;

// public SubscriberThread(Channel channel, String queueName, Consumer consumer)
// {
// this.channel = channel;
// this.queueName = queueName;
// this.consumer = consumer;
// }

// public void run() {
// try {
// this.channel.basicConsume(this.queueName, true, this.consumer);
// } catch (Exception e) {
// System.out.println("Exceção numa thread de subscriber" + "\n" +
// e.toString());
// }
// }
// }