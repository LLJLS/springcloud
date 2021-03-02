package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// 从外部存储（文件）创建RDD
object Spark02_RDD_File_Whole {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // 从外部存储（文件）创建RDD
    // wholeTextFiles 生成元组数据，第一个元素是文件路径，第二个元素是文件内容
    val rdd = sc.wholeTextFiles("datas")
    rdd.collect().foreach(println)
    // 停止环境
    sc.stop()
  }

}
