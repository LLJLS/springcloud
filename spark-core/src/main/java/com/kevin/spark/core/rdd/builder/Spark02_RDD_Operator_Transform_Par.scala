package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD并行计算
object Spark02_RDD_Operator_Transform_Par {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    /*
      1.rdd的计算一个分区内的数据是一个一个执行逻辑
        只有前面一个数据全部逻辑执行完毕，才会执行下一个数据
        分区内数据的执行是有序的。
      2.不同分区数据计算是无序的
     */
    val rdd = sc.makeRDD(List(1, 2, 3, 4),1)
    val mapRdd1 = rdd.map(num => {
      println(">>>>>>>"+num)
      num
    })

    val mapRdd2 = mapRdd1.map(num => {
      println("#######"+num)
      num
    })

    mapRdd2.collect()





    // 停止环境
    sc.stop()
  }

}
