package chen.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerChannelHandler0 extends SimpleChannelInboundHandler<HttpObject> {
    private HttpRequest request;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            request.getMethod();
            String uri = request.getUri();
            System.out.println("Uri:" + uri);
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(CharsetUtil.UTF_8));

            ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().add("content-type", "text/plain");
            response.headers().add("content-length", byteBuf.readableBytes());

            ctx.writeAndFlush(response);

        }
    }
}