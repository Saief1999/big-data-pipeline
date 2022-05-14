package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.RandomForestRegressor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.io.IOException;

public class SparkImportJob {

    private static String serviceEndpoint = "https://s3.wasabisys.com";

    private static String accessKey = "4DLBUG7T3I4ZY30MY800";
    private static String secretKey = "RQ8Cdm4ZHhZstZTXl9CEYDzBnnzghtz2KVOGaL71";
    private static String bucketName="forecast";
    private static final String fileName="forecast.model";

    private String filename ;

    public SparkImportJob(String filename) {
        this.filename = filename;
    }

    public void run() {
        SparkConf sparkConf = new SparkConf()
                .setAppName("org.example.SparkImport")
                .setMaster("local[*]");
        sparkConf.set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        var hadoopConfiguration = sparkContext.hadoopConfiguration();


        hadoopConfiguration.set("fs.s3a.endpoint", serviceEndpoint);
        hadoopConfiguration.set("fs.s3a.access.key", accessKey);
        hadoopConfiguration.set("fs.s3a.secret.key", secretKey);
        hadoopConfiguration.set("fs.s3.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        hadoopConfiguration.set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");

        SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sparkContext);

        Dataset<Row> dataset =  sqlContext.read().option("header","true").csv(String.format("s3a://%s/%s", bucketName, filename));

        var df=dataset.drop("product_name","store_name")
                .withColumn("quantity", dataset.col("quantity").cast("int"))
                .withColumn("year",  org.apache.spark.sql.functions.year(dataset.col("date")) )
                .withColumn("month",org.apache.spark.sql.functions.month(dataset.col("date")))
                .drop("date")
                .groupBy("month","year","store_id","product_id").sum("quantity").withColumnRenamed("sum(quantity)","quantity");
        System.out.println("Total rows: "+df.count());
        df.show(30);

        StringIndexer store_categorizer = new StringIndexer(),
                product_categorizer = new StringIndexer();
        store_categorizer.setInputCol("store_id");
        store_categorizer.setOutputCol("store");
        product_categorizer.setInputCol("product_id");
        product_categorizer.setOutputCol("product");
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"month","year","store", "product"})
                .setOutputCol("features");
        RandomForestRegressor regressorFitter = new RandomForestRegressor();
        regressorFitter.setFeaturesCol("features");
        regressorFitter.setPredictionCol("quantity_predicted");
        regressorFitter.setLabelCol("quantity");
        Pipeline pipeline = new Pipeline();
        pipeline.setStages(new PipelineStage[] {
            store_categorizer,
                product_categorizer,
                assembler,
                regressorFitter
        });
        var model=pipeline.fit(df);
        model.transform(df).show();
        try
        {
            model.save(fileName);
        }
        catch(IOException e)
        {
            System.err.println("Unable to save file, reason "+ e.getMessage());
        }
        df.write()
                .option("header",true)
                .csv("output");
    }
}
