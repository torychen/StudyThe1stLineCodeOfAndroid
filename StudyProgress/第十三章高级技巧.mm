<map version="0.8.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node COLOR="#000000" CREATED="1541659373121" ID="Freemind_Link_197703325" MODIFIED="1541659403978" TEXT="&#x7b2c;&#x5341;&#x4e09;&#x7ae0;&#x9ad8;&#x7ea7;&#x6280;&#x5de7;">
<edge COLOR="#009999" WIDTH="4"/>
<node COLOR="#000000" CREATED="1541659388840" ID="_" MODIFIED="1541659403978" POSITION="right" TEXT="&#x83b7;&#x53d6;&#x5168;&#x5c40; Context">
<edge COLOR="#009999" WIDTH="4"/>
<node CREATED="1541659413783" ID="Freemind_Link_1491282644" MODIFIED="1541659479448" TEXT="&#x7ee7;&#x627f; Application&#xff0c; &#xa;&#x6e05;&#x5355;&#x6587;&#x4ef6;&#x4e2d;&#xa;anroid:name=&quot;&#x5b8c;&#x6574;&#x5305;&#x540d;.MyApplication&quot;"/>
</node>
<node CREATED="1541659593078" HGAP="22" ID="Freemind_Link_129632490" MODIFIED="1541659625521" POSITION="right" TEXT="Intent &#x4f20;&#x9012;&#x5bf9;&#x8c61;" VSHIFT="63">
<node CREATED="1541659628153" ID="Freemind_Link_1208119875" MODIFIED="1541659834318" TEXT="Serializable&#xa;&#x5e8f;&#x5217;&#x5316;&#xff0c; &#x8ba9;&#x4e00;&#x4e2a;&#x7c7b;&#x5b9e;&#x73b0; Serializable &#x63a5;&#x53e3;&#xa;&#xa;&#x4f8b;&#x5b50;&#xa;class Person implements Serialziable {}&#xa;&#xa;Person person;&#xa;Intent intent;&#xa;&#xa;&#x53d1;&#x9001;&#x5bf9;&#x8c61;&#xa;intent.putExtra&#xff08;&#x201c;person_data&#x201d;, person&#xff09;;&#xa;&#xa;&#x63a5;&#x6536;&#x5bf9;&#x8c61;&#xa;person = intent.getSerialbleExtra&#xff08;&#x201c;person_data&#x201d;&#xff09;;"/>
<node CREATED="1541659640306" HGAP="31" ID="Freemind_Link_455729736" MODIFIED="1541660024711" TEXT="Parcelable&#xff0c; &#x7c7b;&#x5b9e;&#x73b0; Parcelable&#xa;&#x6548;&#x7387;&#x6bd4; Serialzble &#x9ad8;&#x3002;&#xa;" VSHIFT="69"/>
</node>
<node CREATED="1541660314215" ID="Freemind_Link_1189391869" MODIFIED="1541660318840" POSITION="right" TEXT="&#x8c03;&#x8bd5;"/>
<node CREATED="1541660322519" ID="Freemind_Link_308612245" MODIFIED="1541660331017" POSITION="right" TEXT="&#x5b9a;&#x65f6;&#x4efb;&#x52a1;" VSHIFT="64">
<node CREATED="1541660332691" ID="Freemind_Link_174864646" MODIFIED="1541660385286" TEXT="Java Timer&#xff0c; &#x5982;&#x679c;cpu &#x4f11;&#x7720;&#xff0c; Timer &#x4efb;&#x52a1;&#x53ef;&#x80fd;&#x5f97;&#x4e0d;&#x5230;&#x6267;&#x884c;&#x3002;&#xa;&#xa;">
<node CREATED="1541660976943" ID="Freemind_Link_276679298" MODIFIED="1541661047031" TEXT="Timer timer = new Timer();&#xa;//&#x8868;&#x793a;&#x5728;3&#x79d2;&#x4e4b;&#x540e;&#x5f00;&#x59cb;&#x6267;&#x884c;&#xff0c;&#x5e76;&#x4e14;&#x6bcf;2&#x79d2;&#x6267;&#x884c;&#x4e00;&#x6b21;&#xa;timer.schedule(new MyTask(),3000,2000);&#xa;&#xa;class MyTask extends TimerTask{&#xa;&#xa;    //&#x5728;run&#x65b9;&#x6cd5;&#x4e2d;&#x7684;&#x8bed;&#x53e5;&#x5c31;&#x662f;&#x5b9a;&#x65f6;&#x4efb;&#x52a1;&#x6267;&#x884c;&#x65f6;&#x8fd0;&#x884c;&#x7684;&#x8bed;&#x53e5;&#x3002;&#xa;    public void run() {&#xa;        &#xa;    }&#xa;&#xa;}&#xa;"/>
</node>
<node CREATED="1541660386745" ID="Freemind_Link_1183828793" MODIFIED="1541661361680" TEXT="Alarm&#x673a;&#x5236;&#xff0c; &#x53ef;&#x4ee5;&#x5524;&#x9192;cpu&#xff0c;&#x6240;&#x6709;&#x540e;&#x53f0;&#x957f;&#x65f6;&#x95f4;&#x8fd0;&#x884c;&#xff0c;&#x4e0d;&#x4f1a;&#x6302;&#x6389;&#x3002;&#xa;&#xa;AlarmManager manager = getSystemService(Context.ALARM_SERVICE)&#xa;long triggerAtTime = SystemClock.elapsedRealtime() + 10ms&#xa;manager.set(AlarmManger.ELAPSE_REALTIME_WAKEUP&#xff0c; trigerAtTime&#xff0c; pendingIntent)&#xa;&#xa;AlarmManager.set&#xff08;&#xff09;&#x4f1a;&#x5c06;&#x51e0;&#x4e2a;&#x76f8;&#x8fd1;&#x7684; Alarm &#x4e00;&#x8d77;&#x6267;&#x884c;&#x3002;&#x6240;&#x4ee5;&#x4e0d;&#x7cbe;&#x786e;&#x3002;&#xa;AlarmManager.setExact&#xff08;&#xff09; &#x7cbe;&#x786e;&#x5b9a;&#x65f6;&#xa;&#xa;&#x907f;&#x514d;&#x88ab;Doze &#x6a21;&#x5f0f;&#x5f71;&#x54cd;&#xff0c;&#x8981;&#x8c03;&#x7528;&#x5982;&#x4e0b;&#x63a5;&#x53e3;&#xa;setAndAllowWHileIdle&#xff08;&#xff09;&#xa;setExactAndAllowWHileIdle&#xff08;&#xff09;" VSHIFT="26"/>
</node>
<node CREATED="1541660832572" ID="Freemind_Link_585960316" MODIFIED="1541660852377" POSITION="right" TEXT="&#x957f;&#x671f;&#x540e;&#x53f0;&#x670d;&#x52a1;" VSHIFT="44">
<node CREATED="1541660841125" ID="Freemind_Link_652375263" MODIFIED="1541660895313" TEXT="&#x5728;&#x670d;&#x52a1;&#x7684; onStartCommand&#x4e2d;&#xff0c; &#x542f;&#x52a8;&#x5b9a;&#x65f6;&#x4efb;&#x52a1;&#xff0c;&#x91cd;&#x65b0;&#x542f;&#x52a8;&#x670d;&#x52a1;&#x3002;"/>
</node>
<node CREATED="1541661083616" HGAP="29" ID="Freemind_Link_47820939" MODIFIED="1541661089705" POSITION="right" TEXT="Doze&#x6a21;&#x5f0f;" VSHIFT="111">
<node CREATED="1541661103546" ID="Freemind_Link_1538901574" MODIFIED="1541661304680" TEXT="&#x4f18;&#x5316;&#x7535;&#x91cf;&#xff0c; &#x8bbe;&#x5907;&#x672a;&#x63a5;&#x7535;&#x6e90;&#xff0c;&#x5c4f;&#x5e55;&#x5173;&#x95ed;&#x4e00;&#x6bb5;&#x65f6;&#x95f4;&#x540e;&#x8fdb;&#x5165; Doze&#x6a21;&#x5f0f;&#x3002;&#x7136;&#x540e;&#x95f4;&#x6b47;&#x9000;&#x51fa;Doze&#xff0c;&#x54cd;&#x5e94;&#x3002;&#xa;&#xa;Doze&#x6a21;&#x5f0f;&#x4e0b;&#xff1a;&#xa;&#x7981;&#x6b62;&#x7f51;&#x7edc;&#x8bbf;&#x95ee;&#xa;&#x5ffd;&#x7565;&#x5524;&#x9192;CPU&#x6216;&#x8005;&#x5c4f;&#x5e55;&#x64cd;&#x4f5c;&#xa;&#x4e0d;&#x518d;&#x626b;&#x63cf;WIFI&#xa;&#x4e0d;&#x518d;&#x6267;&#x884c;&#x540c;&#x6b65;&#x4efb;&#x52a1;&#xa;Alarm&#x4efb;&#x52a1;&#x4f1a;&#x5ef6;&#x8fdf;&#x5230;&#x4e0b;&#x6b21;&#x9000;&#x51fa; Doze &#x65f6;&#xff0c;&#x624d;&#x88ab;&#x6267;&#x884c;&#x3002;"/>
</node>
<node CREATED="1541661526708" HGAP="30" ID="Freemind_Link_1678382695" MODIFIED="1541661532497" POSITION="right" TEXT="&#x591a;&#x7a97;&#x53e3;&#x6a21;&#x5f0f;" VSHIFT="97">
<node CREATED="1541661535272" ID="Freemind_Link_583776112" MODIFIED="1541661638267" TEXT="&#x7981;&#x7528; &#x5728;&#x6e05;&#x5355;&#x6587;&#x4ef6;&#x5c5e;&#x6027;&#xa;&lt;application&#xa;android:resizalbeActiviyt=&quot;false&quot;&#xa;&#xa;&#x8bbe;&#x7f6e;&#x652f;&#x6301;&#x6a2a;&#x5c4f;&#x6216;&#x7ad6;&#x5c4f;&#xa;&lt;activity&#xa;android:screenOrientation=&quot;portrait&quot;"/>
</node>
<node CREATED="1541660026624" ID="Freemind_Link_1033761543" MODIFIED="1541660028099" POSITION="left" TEXT="&#x95ee;&#x9898;">
<node CREATED="1541660029744" ID="Freemind_Link_1639942031" MODIFIED="1541660040560" TEXT="&#x5bf9;&#x6bd4; Srializalbe &#x548c; Parcelable"/>
</node>
</node>
</map>
