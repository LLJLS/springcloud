package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// 文件数据分区的分配
object Spark02_RDD_File_Partion1 {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // 1.数据以行为单位进行读取，因为spark采用hadoop的方式读取
    // 2.数据读取时以偏移量为单位，偏移量不会被重复读取
    /*
      100 => 012
      200 => 345
      3   => 6
     */
    // 3.数据分区的偏移量范围计算
    /*
      0 => [0,3] => 1,2
      1 => [3,6] => 3
      2 => [6,7] =>
     */
    val rdd = sc.textFile("datas/1.txt", 2)
    rdd.saveAsTextFile("output")
    // 停止环境
    sc.stop()
  }

}
