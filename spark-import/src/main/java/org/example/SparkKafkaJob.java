package org.example;

import org.apache.spark.SparkConf;
//import org.apache.spark.streaming.Duration;
//import org.apache.spark.streaming.api.java.JavaDStream;
//import org.apache.spark.streaming.api.java.JavaPairDStream;
//import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

public class SparkKafkaJob {

    private String zkQuorum;
    private String group;
    private String topic;
    private static int numThreads = 1 ;

    public SparkKafkaJob(String zkQuorum, String group, String topic) {
        this.zkQuorum = zkQuorum;
        this.group = group;
        this.topic = topic;
    }
    public void run() throws Exception{

//        SparkConf sparkConf = new SparkConf().setAppName("org.example.SparkKafkaJob");
//        // Creer le contexte avec une taille de batch de 2 secondes
//        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf,
//                new Duration(2000));
//
//        Map<String, Integer> topicMap = new HashMap<>();
//
//        topicMap.put(topic, numThreads);
//
//
//        JavaPairReceiverInputDStream<String, String> messages =
//                KafkaUtils.createStream(jssc, zkQuorum, group, topicMap);
//
//        messages.foreachRDD(stringStringJavaPairRDD -> {
//            if (!stringStringJavaPairRDD.isEmpty()) {
//                stringStringJavaPairRDD.foreach(tuple -> {
//                    System.out.println(tuple._2);
//                });
//            }
//        });
//        jssc.start();
//        jssc.awaitTermination();
    }
}
