package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// flatMap
object Spark02_RDD_Operator_Transform_FlatMap {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // 嵌套集合
    sc.makeRDD(List(List(1,2),List(3,4))).flatMap(
      list => {
        list
      }
    ).collect().foreach(println)

    // 字符串
    sc.makeRDD(List("Hello World","Hello Spark")).flatMap(
      s => {
        s.split(" ")
      }
    ).collect().foreach(println)

    // 混合
    sc.makeRDD(List(List(1,2),List(3,4),5)).flatMap(
      data => {
        data match {
          case list:List[_] => list
          case data => List(data)
        }
      }
    ).collect().foreach(println)




    // 停止环境
    sc.stop()
  }

}
