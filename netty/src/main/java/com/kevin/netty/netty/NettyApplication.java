package com.kevin.netty.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyApplication {

    public static void main(String[] args){

        // 1.创建连接线程组和业务处理线程组(两个都是无线循环的)
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //用于处理服务器端接收客户端连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //进行网络通信（读写）

        // 2.创建服务器端的启动对象,并配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();//辅助工具类，用于服务器通道的一系列配置
        // 使用链式编程进行设置
        bootstrap.group(bossGroup, workerGroup) //绑定两个线程组
        .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel的模式,作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列等待连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true) // 设置保持活动连接状态
        .childHandler(new ChannelInitializer<SocketChannel>() { // 给我们的workgroup的 EventLoop 对应的管道设置处理器
            // 向 pipline 设置处理器
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ServerHandler());
            }
        });
        System.out.println("Server is ready");

        ChannelFuture cf = null;
        try {
            // 3.绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器
            cf = bootstrap.bind(6668).sync();
            // 4.对关闭通道进行更新
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅地关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}

class ServerHandler  extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的消息
     * @param ctx 上下文：包含 管道pipeline,通道channel
     * @param msg 客户端发送过来的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        // 将msg转变成ByteBuf,ByteBuf是netty提供的，跟nio的ByteBuffer无关
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送信息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush,包含 write 和 flush
        // 将数据写入到缓存，并刷新
        ChannelFuture cf = ctx.writeAndFlush(Unpooled.copiedBuffer("hello,netty world",CharsetUtil.UTF_8));
    }

    /**
     * 处理异常，一般是关闭
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.channel().close();
    }
}


