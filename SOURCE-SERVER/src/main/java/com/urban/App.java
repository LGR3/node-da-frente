package com.urban;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import java.util.concurrent.TimeoutException;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class App {

    public static void main(String[] args) {
        String filename = "/home/wison/dados_02.csv";
        String rabbitAddress = "localhost";
        EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();
        engine.getEPAdministrator().getConfiguration().addEventType(BusEvent.class);

        try {
            Rabbit rmq = new Rabbit(rabbitAddress);
            Channel channel = rmq.getChannel();
            Hashtable<String, Integer> mapBus = new Hashtable<String, Integer>();
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                        byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("- rabbitmq received '" + message + "'");

                    if (!mapBus.containsKey(message)) {
                        String q = "select content from BusEvent#length(1) where name = '" + message + "'";
                        EPStatement statement = engine.getEPAdministrator().createEPL(q);
                        statement.addListener((newData, oldData) -> {
                            String content = (String) newData[0].get("content");
                            System.out.println("- enviando " + content);
                            try {
                                rmq.publish(message, content);
                            } catch (IOException e) {
                                System.out.println("* Erro para publicar " + content + ": " + e);
                            }
                        });
                        mapBus.put(message, 1);
                    } else {
                        System.out.println("- já está sendo publicado: " + message);
                    }

                }
            };
            rmq.subscriber("bus", consumer);

            BufferedReader buffer = new BufferedReader(new FileReader(filename));
            String line;
            GenerateEvent ge = new GenerateEvent(engine, 2);
            new Thread(ge).start();
            buffer.readLine(); // skip first line
            while ((line = buffer.readLine()) != null) {
                // System.out.println("Onibus: " + line.split(",")[0]);
                // TODO: publicar onibus em fila especial
                ge.addEvent(line);
            }

            try {
                buffer.close();
            } catch (Exception ef) {
            }
        } catch (TimeoutException e) {
            System.out.println("* Timeout para conectar com o rabbitmq " + e);
        } catch (IOException e) {
            System.out.println("* Erro com o rabbitmq? " + e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}