package io.netty.codec.quic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class QuicClient {

	// Configure the client.
	private final EventLoopGroup group;
	private final String host;
	private final int port;

	public QuicClient(EventLoopGroup eventLoopGroup, String host, int port) {
		this.group = eventLoopGroup;
		this.host = host;
		this.port = port;
		
	}

	public ChannelFuture start() throws Exception {
		
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<DatagramChannel>() {
				@Override
				public void initChannel(DatagramChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					// p.addLast(new LoggingHandler(LogLevel.INFO));
					p.addLast(new SimpleChannelInboundHandler<DatagramPacket>() {

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
							System.err.println("Received response; " + msg);
						}
					});
				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();
			return f;
	}

}
