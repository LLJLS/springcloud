package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// RDD并行计算
object Spark02_RDD_Operator_Transform_MapPartitionWithIndex {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    val rdd = sc.makeRDD(List(1, 2, 3, 4),1)
    // 只获取某个分区数据
    rdd.mapPartitionsWithIndex(
      (index,iter)=>{
        if (index == 1) {
          iter
        } else {
          Nil.iterator
        }
      }
    ).collect()

    // 显示数据是哪个分区的
    rdd.mapPartitionsWithIndex(
      (index,iter) => {
        iter.map(
          num => {
            (index,num)
          }
        )
      }
    ).collect()
    // 停止环境
    sc.stop()
  }

}
