package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.api.java.JavaDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import org.apache.hadoop.conf.Configuration;

/**
 * CAN NOT BE USED
 */
public class SparkImportStreaming {
    public static void main(String[] args) {
//        SparkConf sparkConf = new SparkConf()
//                .setAppName("org.example.SparkImport")
//                .setMaster("local[*]");
//
//        /** Used for BATCH PROCESSING
//         JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
//         var hadoopConfiguration=sparkContext.hadoopConfiguration();
//         */
//
////        JavaStreamingContext streamingContext =
////                new JavaStreamingContext(sparkConf, Durations.seconds(30));
////        var hadoopConfiguration = streamingContext.sparkContext().hadoopConfiguration();
//
//        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
//        Configuration hadoopConfiguration=sparkContext.hadoopConfiguration();
//        JavaStreamingContext streamingContext= new JavaStreamingContext(sparkContext, Durations.seconds(15));
//
//        hadoopConfiguration.set("fs.s3a.endpoint", "https://s3.wasabisys.com");
//        hadoopConfiguration.set("fs.s3a.access.key","4DLBUG7T3I4ZY30MY800");
//        hadoopConfiguration.set("fs.s3a.secret.key","RQ8Cdm4ZHhZstZTXl9CEYDzBnnzghtz2KVOGaL71");
//
////        hadoopConfiguration.set("textinputformat.record.delimiter", "\u0003");
//
//        JavaDStream<String> stream = streamingContext.textFileStream("s3a://forecast/");
//
//        stream.foreachRDD( stringJavaRDD -> {
//            if (!stringJavaRDD.isEmpty()) {
//                System.out.println("Hello World");
//            }
//        });
//        // Start the computation
//        streamingContext.start();
//        try {
//            // Wait for the computation to terminate
//            streamingContext.awaitTermination();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }
}
