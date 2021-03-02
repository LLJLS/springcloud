package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD转换算子--查看日志信息
object Spark02_RDD_Operator_Transform_Log {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    val rdd = sc.textFile("datas/test.log")
    val map = rdd.map(
      line => {
        val data = line.split(" ")
        data(6)
      }
    )
    map.collect().foreach(println)

    // 停止环境
    sc.stop()
  }

}
