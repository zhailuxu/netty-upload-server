package com.learn.java.start;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FileDecoder extends ByteToMessageDecoder {

	private static final int fileLength = 4;
	private static final int fileNameLength = 128;

	@Override
	protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Object decoded = decode(ctx, in);
		if (decoded != null) {
			out.add(decoded);
		}
	}

	protected Object decode(@SuppressWarnings("UnusedParameters") ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		//2.1半包
		if (in.readableBytes() < fileLength + fileNameLength) {
			return null;
		}
        //2.2读取文件大小
		in.markReaderIndex();
		int length = in.readInt();   
        //2.3半包
		System.out.println(in.readableBytes() + ":" + length );
		if (in.readableBytes() <length + fileNameLength) {

			in.resetReaderIndex();

			return null;
		}

        //2.4 读取 文件名+文件内容
		return in.readRetainedSlice(length + fileNameLength);
	}

}
