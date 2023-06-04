package chen.netty.unsplit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private AtomicInteger counter = new AtomicInteger(1);

    /*** 客户端读取到网络数据后的处理*/
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println(String.format("客户端第%d次接收：" + msg.toString(CharsetUtil.UTF_8),
                counter.getAndIncrement()));
    }

    /*** 客户端被通知channel活跃后，输出*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg = null;
        String request = "test1test2test3test4";
        for (int i = 0; i < 100; i++) {
            msg = Unpooled.buffer(request.length());
            msg.writeBytes(request.getBytes());
            ctx.writeAndFlush(msg);
        }
    }

    /*** 发生异常后的处理*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}