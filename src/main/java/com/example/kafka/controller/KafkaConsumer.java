package com.example.kafka.controller;

import com.example.kafka.constrant.ApplicationConstant;
import com.example.kafka.model.Student;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/consume")
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ConcurrentKafkaListenerContainerFactory<String, Student> factory;

    @GetMapping("/message")
    public List<Student> receiveMessage() {
        List<Student> students = new ArrayList<>();
        ConsumerFactory<String, Student> consumerFactory = (ConsumerFactory<String, Student>) factory.getConsumerFactory();
        Consumer<String, Student> consumer = consumerFactory.createConsumer();
        try {
            consumer.subscribe(Arrays.asList(ApplicationConstant.TOPIC_NAME));
            ConsumerRecords<String, Student> consumerRecords = consumer.poll(Duration.ofDays(1));
            Iterable<ConsumerRecord<String, Student>> records = consumerRecords.records(ApplicationConstant.TOPIC_NAME);
            Iterator<ConsumerRecord<String, Student>> iterator = records.iterator();

            while (iterator.hasNext()) {
                students.add(iterator.next().value());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
