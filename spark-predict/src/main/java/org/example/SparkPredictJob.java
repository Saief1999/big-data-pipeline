package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.regression.RandomForestRegressionModel;
import org.apache.spark.ml.regression.RandomForestRegressor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class SparkPredictJob {

    private String filename ;
    private String modelpath;
    public SparkPredictJob(String modelpath, String filename) {
        this.modelpath = modelpath;
        this.filename  = filename;

    }

    public void run() {

        //RandomForestRegressor __= new RandomForestRegressor();

        SparkConf sparkConf = new SparkConf()
                .setAppName("org.example.SparkImport")
                .setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sparkContext);
        Dataset<Row> dataset =  sqlContext.read().option("header","true").csv(filename);

        var df=dataset.drop("product_name","store_name")
                .withColumn("quantity", dataset.col("quantity").cast("int"))
                .withColumn("year",  org.apache.spark.sql.functions.year(dataset.col("date")) )
                .withColumn("month",org.apache.spark.sql.functions.month(dataset.col("date")))
                .drop("date")
                .groupBy("month","year","store_id","product_id").sum("quantity").withColumnRenamed("sum(quantity)","quantity");

       var model = PipelineModel.load(modelpath);

        model.transform(df).sample(.1).show();
    }
}
