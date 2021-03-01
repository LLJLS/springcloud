package com.kevin.spark.core.test

import java.io.{ObjectOutput, ObjectOutputStream}
import java.net.Socket

object Driver {

  def main(args: Array[String]): Unit = {
    // 连接服务器
    val client1 = new Socket("localhost", 9999)
    val client2 = new Socket("localhost", 8888)
    print("客户端启动,准备发送数据")
    //
    val out1 = client1.getOutputStream
    val objOut1 = new ObjectOutputStream(out1)
    val task = new Task()

    val subTask1 = new SubTask()
    subTask1.datas = task.datas.take(2)
    subTask1.logic = task.logic
    objOut1.writeObject(subTask1)
    objOut1.flush()
    objOut1.close()
    out1.close()
//    out.write(2)
//    out.flush()
//    out.close()
    client1.close()


    val out2 = client2.getOutputStream
    val objOut2 = new ObjectOutputStream(out2)

    val subTask2 = new SubTask()
    subTask2.datas = task.datas.takeRight(2)
    subTask2.logic = task.logic
    objOut2.writeObject(subTask2)
    objOut2.flush()
    objOut2.close()
    out2.close()
    //    out.write(2)
    //    out.flush()
    //    out.close()
    client2.close()
  }

}
