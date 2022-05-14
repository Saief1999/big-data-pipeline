package org.example;

public class SparkImport {
    public static void main(String[] args) throws Exception{

        if(args.length == 0){
            System.out.println("You need to provide the filename!");
            return;
        }

        org.example.SparkImportJob importJob = new org.example.SparkImportJob(args[0]);
        importJob.run();

//        if (args.length < 3) {
//            System.out.println("3 args required!");
//            return ;
//        }
//        SparkKafkaJob sparkKafkaJob = new SparkKafkaJob(args[0],args[1],args[2]);
//        sparkKafkaJob.run();

    }
}
