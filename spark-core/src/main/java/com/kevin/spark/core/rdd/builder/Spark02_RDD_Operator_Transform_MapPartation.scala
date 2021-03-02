package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD并行计算
object Spark02_RDD_Operator_Transform_MapPartation {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // mapPartitions:可以以分区为单位进行数据转换操作
    //               但是会将整个分区的数据加载到内存进行引用
    //               处理完的数据是不会被释放掉，因为存在对象的引用。
    //               在内存较小，数据量较大的场合下，容易出现内存溢出。
    val rdd = sc.makeRDD(List(1, 2, 3, 4), 2)

    rdd.mapPartitions(
      iter => {
        print(">>>>>>>>")
        iter.map(_*2)
      }
    )




    // 停止环境
    sc.stop()
  }

}
