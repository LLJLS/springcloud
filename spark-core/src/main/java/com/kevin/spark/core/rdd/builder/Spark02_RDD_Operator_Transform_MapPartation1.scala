package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// MapPartitions的小功能
object Spark02_RDD_Operator_Transform_MapPartation1 {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    val mapRdd = rdd.mapPartitions(
      iter => {
        List(iter.max).iterator // 核心
      }
    )
    mapRdd.collect()
    // 停止环境
    sc.stop()
  }

}
