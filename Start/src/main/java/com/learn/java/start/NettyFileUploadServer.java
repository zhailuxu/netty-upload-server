package com.learn.java.start;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyFileUploadServer {
	
	private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

	
	// （1.1）创建主从Reactor线程池
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	//启动服务
	public void init() throws Exception{

		
		Thread thread  = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					// 1.2创建启动类ServerBootstrap实例，用来设置客户端相关参数
					ServerBootstrap b = new ServerBootstrap();
					b.group(bossGroup, workerGroup)// 1.2.1设置主从线程池组
							.channel(NioServerSocketChannel.class)// 1.2.2指定用于创建客户端NIO通道的Class对象
							.handler(new LoggingHandler(LogLevel.INFO))// 1.2.4设置日志handler
							.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
							.childHandler(new ChannelInitializer<SocketChannel>() {// 1.2.5设置用户自定义handler
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									ChannelPipeline p = ch.pipeline();
									// 1.2.5.1 文件格式解析器
									p.addLast(new FileDecoder());
									// 1.2.5.2 业务处理器
									p.addLast(new NettyServerHandler());
								}
							});

					// 1.3 启动服务器
					ChannelFuture f = b.bind(PORT).sync();
					System.out.println("----Server Started----");

					// 1.4 同步等待服务socket关闭
					f.channel().closeFuture().sync();
				}catch(Exception e) {
					System.out.println("----Server error----" + e.getLocalizedMessage());
				}
				finally {
					// 1.5优雅关闭线程池组
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}				
			}
		});
		
		thread.start();
		
	}
}
