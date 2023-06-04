package chen.netty.lineSplit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

public class LineBaseEchoServer {

    public static final int PORT = 9998;

    public static void main(String[] args) throws InterruptedException {
        LineBaseEchoServer lineBaseEchoServer = new LineBaseEchoServer();
        System.out.println("服务器即将启动");
        lineBaseEchoServer.start();
    }

    public void start() throws InterruptedException {
        final LineBaseServerHandler serverHandler = new LineBaseServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)/*指定使用NIO进行网络传输*/
                    .localAddress(new InetSocketAddress(PORT))/*指定服务器监听端口*/
                    /*服务端每接收到一个连接请求，就会新启一个socket通信，也就是channel，
                    所以下面这段代码的作用就是为这个子channel增加handle*/
                    .childHandler(new ChannelInitializerImp());
            ChannelFuture f = b.bind().sync();/*异步绑定到服务器，sync()会阻塞直到完成*/
            System.out.println("服务器启动完成，等待客户端的连接和数据.....");
            f.channel().closeFuture().sync();/*阻塞直到服务器的channel关闭*/
        } finally {
            group.shutdownGracefully().sync();/*优雅关闭线程组*/
        }
    }

    private static class ChannelInitializerImp extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            //添加换行解码器
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new LineBaseServerHandler());
        }
    }

}