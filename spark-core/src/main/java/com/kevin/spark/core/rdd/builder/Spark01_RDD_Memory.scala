package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// 从集合（内存）中创建RDD
object Spark01_RDD_Memory {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // 从内存中创建RDD
    val seq = Seq[Int](1, 2, 3, 4)
//    val rdd = sc.parallelize(seq)
    val rdd = sc.makeRDD(seq) // makeRDD底层就是parallelize,只是让名字更容易理解
    rdd.collect().foreach(println) // 只有触发collect方法才会执行应用程序
    // 停止环境
    sc.stop()
  }

}
