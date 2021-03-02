package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD的并行度和分区
object Spark01_RDD_Memory_Par {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核
    sf.set("spark.default.parallelism","8" )
    val sc = new SparkContext(sf)

    // 创建RDD
    // 从内存中创建RDD
    // makeRDD 的第一个参数是数据,第二个参数是分区数量
    // 分区数量在没有指定的情况下，会取默认值
    // 这个默认值是取的当前运行环境的最大核心数
    // 这是核心代码 scheduler.conf.getInt("spark.default.parallelism", totalCores)
    val rdd = sc.makeRDD(List(1, 2, 3, 4), 5)
    rdd.saveAsTextFile("output")
    // 停止环境
    sc.stop()
  }

}
