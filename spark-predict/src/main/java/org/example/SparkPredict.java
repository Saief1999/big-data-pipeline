package org.example;

public class SparkPredict {

    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("You need to provide the filename & the model path!");
            return;
        }

        org.example.SparkPredictJob predictJobJob = new SparkPredictJob(args[0],args[1]);
        predictJobJob.run();
    }
}
