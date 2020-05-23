package io.netty.codec.quic;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class QuicServer {

	public static final int PORT = Integer.parseInt(System.getProperty("quic-port", "8443"));

	private final EventLoopGroup group;

	public QuicServer(EventLoopGroup eventLoopGroup) {
		group = eventLoopGroup;
	}

	public ChannelFuture start() throws Exception {

		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<DatagramChannel>() {

			@Override
			protected void initChannel(DatagramChannel ch) throws Exception {
				// TODO Have a dedicated channel initializer that handles TLS, etc.
				ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
					@Override
					public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
						// For now just echoes whatever we received
						System.err.println("====> Message received by the server");
						ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(packet.content()), packet.sender()));
					}
				});
			}
		});

		Channel ch = b.bind(PORT).sync().channel();
		System.err.println("Server started");
		return ch.closeFuture();
	}

}
