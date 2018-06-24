package com.idax.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 服务端 ChannelInitializer
 * 服务端通道初始化
 */
public class WebsocketChatServerInitializer extends
		ChannelInitializer<SocketChannel> {	//1

	@Override
    public void initChannel(SocketChannel ch) throws Exception {//2
		 ChannelPipeline pipeline = ch.pipeline();

		//HttpServerCodec: 针对http协议进行编解码
        pipeline.addLast(new HttpServerCodec());
		/**
		 * 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
		 * 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
		 */
		pipeline.addLast(new HttpObjectAggregator(64*1024));
		//ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpRequestHandler("/ws"));
		//用于处理websocket, /ws为访问websocket时的uri
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler());

    }
}
