package com.linjun.projectsend.ui.main;

import android.content.Intent;
import android.os.Handler;

import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.linjun.SendPacket;
import com.linjun.projectsend.R;
import com.linjun.projectsend.config.Const;
import com.linjun.projectsend.handler.HeartBeatInitializer;
import com.linjun.projectsend.ui.base.BaseActivity;
import com.txusballesteros.SnakeView;

import java.net.InetSocketAddress;

import butterknife.BindView;
import butterknife.OnClick;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class MainActivity extends BaseActivity {
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.snake)
    SnakeView snake;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_start)
    Button btnStart;
   private NioEventLoopGroup group;
    private  Channel channel;
    private  ChannelFuture channelFuture;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    public static MyHandler handler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        handler=new MyHandler();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        if (btnStart.getText().equals("开始")) {
            btnStart.setText("停止");
               tvResult.setText("正在连接服务器");
              connect(handler);
            tvResult.setText("");
            tvResult.append("开始启动定位服务...\n");
            Intent intet1 = new Intent(MainActivity.this, LocationService.class);
            startService(intet1);
        } else {
            btnStart.setText("开始");
            tvResult.setText(tvResult.getText()+"停止定位...\n");
            tvResult.setText(tvResult.getText()+"正在关闭服务\n");
            Intent intet2 = new Intent(MainActivity.this, LocationService.class);
            stopService(intet2);
            tvResult.setText(tvResult.getText()+"关闭服务成功\n");
        }
    }
  private   void connect(final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                try {
                    group = new NioEventLoopGroup();
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group);
                    bootstrap.channel(NioSocketChannel.class);
                    bootstrap.handler(new HeartBeatInitializer(handler));
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.option(ChannelOption.TCP_NODELAY, true);
                    channelFuture = bootstrap.connect(new InetSocketAddress(Const.HOST, Const.TCP_PORT));
                    channel = channelFuture.sync().channel();
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            handler.obtainMessage(0).sendToTarget();
                        }
                    });
                    channel.closeFuture().sync();
                } catch (Exception e) {
               group.shutdownGracefully();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String hello1 = new String("I'm in!");
                    SendPacket em1 = new SendPacket();
                    byte[] b1 = hello1.getBytes();
                    em1.setBytes(b1);
                    em1.setSumCountPackage(b1.length);
                    em1.setCountPackage(1);
                    em1.setSend_time(System.currentTimeMillis());
                    channel.writeAndFlush(em1);
                    break;
                case 1:
                    Log.i("s", msg.obj.toString());
                    tvResult.setText(tvResult.getText()+msg.obj.toString());
                    break;
                case 2:
                    Log.i("sww22ww", msg.obj.toString());
                    SendPacket  em=(SendPacket) msg.obj;
                    channel.writeAndFlush(em);
                    break;
                case 3:
                    break;

            }

        }
    }

}
