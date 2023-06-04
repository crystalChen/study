package chen.netty.unsplit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger counter = new AtomicInteger(1);

    /*** 服务端读取到网络数据后的处理*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String request = in.toString(CharsetUtil.UTF_8);
        //1024个字符分割，正式Netty默认的TCP缓冲区大小
        System.out.println(String.format("服务端第%d次接收到:" + request, counter.getAndIncrement()));
        String resp = "服务端返回的数据:" + request + '\n';
        ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));

    }

    /*** 发生异常后的处理*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}