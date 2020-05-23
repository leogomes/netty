package io.netty.codec.quic;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;

public class TestQuic {

	@Test
	public void basicEchoInteraction() throws Exception {
		QuicServer server = new QuicServer(new NioEventLoopGroup());
		QuicClient client = new QuicClient(new NioEventLoopGroup(), "127.0.01", QuicServer.PORT);
		ChannelFuture serverCloseFuture = server.start();
		ChannelFuture clientFuture = client.start();
		DatagramChannel ch = (DatagramChannel) clientFuture.channel();
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1", QuicServer.PORT);
		DatagramPacket pckt = new DatagramPacket(Unpooled.copiedBuffer("Quic test", UTF_8), addr);
		ch.writeAndFlush(pckt).sync();
		serverCloseFuture.channel().close().await();
	}

}
