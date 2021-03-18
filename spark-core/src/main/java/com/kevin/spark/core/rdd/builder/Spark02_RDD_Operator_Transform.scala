package com.kevin.spark.core.rdd.builder

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

// RDD转换算子
object Spark02_RDD_Operator_Transform {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    val rdd1 = sc.makeRDD(List("Hello", "World", "Hello", "Spark"))
    def mapFunction(num:Int) : Int = {
      num*2
    }


//    val mapRdd = rdd.map(mapFunction)
//    val mapRdd = rdd.map((num:Int)=>{num*2}) // 匿名函数
//    val mapRdd = rdd.map((num:Int)=>num*2) // 至简原则
//    val mapRdd = rdd.map(num=>num*2) // 至简原则
    val mapRdd = rdd.map(_*2) // 至简原则
    mapRdd.collect().foreach(println)

    // glom
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

    // groupBy
    // groupBy会把数据打乱重新组合，这就是shuffle
    rdd1.groupBy(_.charAt(0)).collect().foreach(println)

    // filter
    rdd.filter(_%2!=0).collect().foreach(println)

    // sample 按指定规则抽取数据
    val rdd2 = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    // param1:是否放回
    // param2:抽取概率,不设置param3的时候才有效
    // param3:种子,数据起始值
    val str = rdd2.sample(false, 0.4, 1).collect().mkString(",")
    print(str)

    // distinct,scala使用hashset去重，spark是用mapredce的处理方式
    val rdd3 = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4, 0, 0))
    rdd3.distinct().foreach(print)

    // coalece 缩减分区数量
    // param1:缩减到多少分区
    // param2:是否打乱重组,默认false，不打乱
    // 可以扩大分区,前提是必须打乱
    val rdd4 = sc.makeRDD(List(1, 2, 3, 4,5,6),3)
//    rdd4.coalesce(2).saveAsTextFile("output")
//    rdd4.coalesce(2,true).saveAsTextFile("output")

    // repartition 增加分区，底层就是用的coalesce(2,true)
//    rdd4.repartition(8).saveAsTextFile("output")

    // sortBy
    // 分区有序,默认升序
    // param1:排序规则
    // param2:默认true,升序,false,降序
    val rdd5 = sc.makeRDD(List(9, 2, 5, 4,1,2),3)
    rdd5.sortBy(t=>t).foreach(println)
    val rdd6 = sc.makeRDD(List(("1",2),("2",3),("0",2)),3)
    // 以数字来排序
    rdd6.sortBy(t=>t._1.toInt).foreach(println)

    // 双value类型：交集，并集，差集,两数据类型必须一致，拉链可以一致，分区数量要保持一致
    val rdd7 = sc.makeRDD(List(1, 2, 3, 4))
    val rdd8 = sc.makeRDD(List(3, 4, 5, 6))

    // intersection 交集
    rdd7.intersection(rdd8).collect().mkString(":")foreach(print)
    println()
    // union 并集
    rdd7.union(rdd8).collect().mkString(":")foreach(print)
    println()

    // subtract 差集
    rdd7.subtract(rdd8).collect().mkString(":")foreach(print)
    println()

    // zip 拉链
    rdd7.zip(rdd8).collect().mkString(":")foreach(print)
    println()

    // partitionBy
    // 只能用于键值对，可以改变数据的位置
    // 如果分区器的分区数量不一致，就会生成一个新的分区器
    val rdd9 = sc.makeRDD(List(1, 2, 3, 4))
    val rdd10 = rdd9.map((_, 1))
    rdd10.partitionBy(new HashPartitioner(2)).saveAsTextFile("output")

    // reduceByKey
    // 相同key聚合
    // 至少两个相同key才可以两两聚合，只有一个话，不能
    val rdd11 = sc.makeRDD(List(
      ("a",1),("a",2),("b",3),("b",5)
    ))
    rdd11.reduceByKey((x,y)=>{x+y}).collect().foreach(println)

    // groupByKey
    // spark中，shuffle操作必须落盘处理，不能在内存中数据等待，会导致内存溢出。Shuffle的操作性能非常低。
    // 从shuffle角度：reduceByKey比groupByKey性能高，因为reduceByKey支持分区内预聚合功能，可以有效减少shuffle时落盘的数据量
    // 从功能角度：reduceByKey其实包含分组和聚合功能，groupByKey只能分组。
    rdd11.groupByKey().collect().foreach(println)

    // aggregateByKey
    println("------------------------------aggregateByKey-----------------------------")
    val rdd12 = sc.makeRDD(List(
      ("a",1),("a",2),("a",3),("a",5)
    ),2)
    rdd12.aggregateByKey(0/*初始值*/)(// 最终返回的数据结果应该和初始值的类型保持一致
      (x,y)=>math.max(x,y),// 分区内计算规则
      (x,y)=>x+y           // 分区间计算规则
    ).collect().foreach(println)
    // 计算平均值
    rdd12.aggregateByKey((0,0))(// 第一个是值初始值，第二个是次数初始值
      (t,v) => {
        (t._1 + v,t._2 + 1)
      },
      (t1,t2)=>{
        (t1._1+t2._1,t1._2+t2._2)
      }
    ).mapValues{
      case (num,cnt) => {
        num / cnt
      }
    }.collect().foreach(println)

    // foldByKey
    println("------------------------------foldByKey-----------------------------")
    rdd12.foldByKey(0)(_+_).collect().foreach(println) /*当分区内和分区间的计算规则一样时，可以用这个方法简化*/
    //
    rdd12.combineByKey(
      (v) => {
        (v,1)
      },
      (k1:Int,v1:Int) => {
      (k1)
    }

    )
    // 停止环境
    sc.stop()
  }

}
