package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD的分区规则
object Spark01_RDD_Memory_Par1 {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核
    val sc = new SparkContext(sf)

    // 创建RDD
    // makeRDD->SparkContext.parallelize->ParallelCollectionPartition.slice->
    val rdd = sc.makeRDD(List(1, 2, 3, 4), 3)
    rdd.saveAsTextFile("output")
    // 停止环境
    sc.stop()
  }

}
