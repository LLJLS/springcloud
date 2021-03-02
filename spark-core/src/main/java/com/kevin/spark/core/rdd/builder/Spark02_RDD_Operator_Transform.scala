package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD转换算子
object Spark02_RDD_Operator_Transform {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    def mapFunction(num:Int) : Int = {
      num*2
    }


//    val mapRdd = rdd.map(mapFunction)
//    val mapRdd = rdd.map((num:Int)=>{num*2}) // 匿名函数
//    val mapRdd = rdd.map((num:Int)=>num*2) // 至简原则
//    val mapRdd = rdd.map(num=>num*2) // 至简原则
    val mapRdd = rdd.map(_*2) // 至简原则


    mapRdd.collect().foreach(println)

    // 停止环境
    sc.stop()
  }

}
