package com.kevin.spark.core.test

import java.io.ObjectInputStream
import java.net.ServerSocket

object Executor1 {

  def main(args: Array[String]): Unit = {
    // 启动服务器
    val server = new ServerSocket(9999)
    print("服务器启动,等待客户端发数据")

    // 等待客户端的连接
    val client = server.accept()
    val in = client.getInputStream
    val inObj = new ObjectInputStream(in)
    val subTask = inObj.readObject().asInstanceOf[SubTask]
    val compute = subTask.compute()
//    val i = in.read()
    print("9999接收到客户端发送的数据："+compute)
//    in.close()
    inObj.close()
    in.close()
    server.close()
  }

}
