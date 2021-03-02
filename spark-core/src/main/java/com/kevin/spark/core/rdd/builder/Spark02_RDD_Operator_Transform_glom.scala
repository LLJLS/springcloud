package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD并行计算
object Spark02_RDD_Operator_Transform_glom {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // int -> array
   sc.makeRDD(List(1,2,3,4),2).glom().collect().foreach(
     data => {
       println(data.mkString(","))
     }
   )

    // 各个分区的最大值求和
    val sum = sc.makeRDD(List(1, 2, 3, 4), 2).glom().map(array => {
      array.max
    }).collect().sum
    print(sum)





    // 停止环境
    sc.stop()
  }

}
