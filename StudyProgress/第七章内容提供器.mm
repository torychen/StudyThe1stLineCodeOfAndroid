<map version="0.8.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node COLOR="#000000" CREATED="1541492399593" ID="Freemind_Link_1618038442" MODIFIED="1541492483739" TEXT="&#x7b2c;&#x4e03;&#x7ae0;&#x5185;&#x5bb9;&#x63d0;&#x4f9b;&#x5668;">
<edge COLOR="#009999" WIDTH="4"/>
<node COLOR="#000000" CREATED="1541492416113" ID="_" MODIFIED="1541492567140" POSITION="right" TEXT="&#x4e3b;&#x8981;&#x7528;&#x4e8e;&#x5728;&#x4e0d;&#x540c;&#x7684;&#x5e94;&#x7528;&#x7a0b;&#x5e8f;&#x4e4b;&#x95f4;&#x5171;&#x4eab;&#x6570;&#x636e;&#xff0c;&#x5b83;&#x53ef;&#x4ee5;&#x9009;&#x62e9;&#x53ea;&#x5bf9;&#x90a3;&#x4e00;&#x90e8;&#x5206;&#x6570;&#x636e;&#x5171;&#x4eab;&#xff0c;&#x4ece;&#x800c;&#x4fdd;&#x8bc1;&#x9690;&#x79c1;&#x6570;&#x636e;&#x4e0d;&#x4f1a;&#x6709;&#x6cc4;&#x6f0f;&#x98ce;&#x9669;&#x3002;">
<edge COLOR="#009999" WIDTH="4"/>
</node>
<node CREATED="1541493423137" ID="Freemind_Link_785453520" MODIFIED="1541493444609" POSITION="right" TEXT="&#x4e24;&#x79cd;&#x7528;&#x6cd5;">
<node CREATED="1541493445968" ID="Freemind_Link_1799318646" MODIFIED="1541493468606" TEXT="&#x4f7f;&#x7528;&#x73b0;&#x6709;&#x7684;&#x5185;&#x5bb9;&#x63d0;&#x4f9b;&#x5668;&#xff0c;&#x8bfb;&#x53d6;&#x548c;&#x64cd;&#x4f5c;&#x522b;&#x7684;&#x5e94;&#x7528;&#x7a0b;&#x5e8f;&#x7684;&#x6570;&#x636e;"/>
<node CREATED="1541493471035" ID="Freemind_Link_841617928" MODIFIED="1541493490011" TEXT="&#x521b;&#x5efa;&#x81ea;&#x5df1;&#x7684;&#x5185;&#x5bb9;&#x63d0;&#x4f9b;&#x5668;&#xff0c;&#x4f9b;&#x522b;&#x4eba;&#x8bfb;&#x53d6;&#x3002;"/>
</node>
<node CREATED="1541493522035" HGAP="25" ID="Freemind_Link_338986179" MODIFIED="1541493532609" POSITION="right" TEXT="&#x8bfb;&#x53d6;&#x522b;&#x4eba;&#x7684;&#x6570;&#x636e;" VSHIFT="56">
<node CREATED="1541493533930" ID="Freemind_Link_1754703907" MODIFIED="1541493637081" TEXT="&#x901a;&#x8fc7;Context getContentResolver&#xff08;&#xff09; &#x83b7;&#x5f97; ContentResolver&#xa;&#xa;ContentResolver&#xa;insert&#xff0c;delete&#xff0c;query&#xff0c;update &#x64cd;&#x4f5c;&#xff0c;&#x4f7f;&#x7528; &#x5185;&#x5bb9;URL&#x4f5c;&#x4e3a;&#x53c2;&#x6570;&#xa;"/>
<node CREATED="1541493678902" ID="Freemind_Link_1466077919" MODIFIED="1541493827121" TEXT="&#x5185;&#x5bb9;URL&#x683c;&#x5f0f;&#x3002;&#x4e24;&#x90e8;&#x5206;&#x7ec4;&#x6210; authority&#x548c;path&#xa;authority&#x901a;&#x5e38;&#x7528;&lt;&#x5305;&#x540d;&gt;.provider&#xa;path &#x7528;&#x8868;&#x540d;&#x5b57;&#x533a;&#x5206;&#xa;&#x4f8b;&#x5982;&#xa;content&#xff1a;//com.example.app.provider/table1&#xa;content&#xff1a;//com.example.app.provider/table2" VSHIFT="36"/>
<node CREATED="1541493830531" ID="Freemind_Link_103513643" MODIFIED="1541494015609" TEXT="&#x4f8b;&#x5b50;&#xff1a;&#xa;Uri uri = Uri.parse(&quot;content://com.example.app.provider/table1&quot;)&#xa;&#xa;Cursor cursor = getContentReslover().query(&#xa;uri, &#x6307;&#x5b9a;&#x67d0;&#x4e2a;&#x5e94;&#x7528;&#x7a0b;&#x5e8f;&#x7684;&#x67d0;&#x5f20;&#x8868;&#xa;projection, &#x6307;&#x5b9a;&#x67e5;&#x8be2;&#x7684;&#x5217;&#x540d;&#xa;selection, &#x6307;&#x5b9a;where &#x7ea6;&#x675f;&#x6761;&#x4ef6;&#xa;selectionArgs, where &#x5360;&#x4f4d;&#x7b26;&#xff0c;&#x5177;&#x4f53;&#x7684;&#x503c;&#xa;sortOrder&#xff0c; &#x67e5;&#x8be2;&#x7ed3;&#x679c;&#x7684;&#x6392;&#x5e8f;&#x65b9;&#x5f0f;&#xa;);"/>
<node CREATED="1541494040441" ID="Freemind_Link_1671090573" MODIFIED="1541494090994" TEXT="insert&#xa;&#xa;ContentValues values&#xa;value.put&#xff08;&#xff09;&#xa;getContenResolver().insert(uri, values)" VSHIFT="12"/>
</node>
<node CREATED="1541492580531" ID="Freemind_Link_731485136" MODIFIED="1541492584827" POSITION="left" TEXT="&#x8fd0;&#x884c;&#x65f6;&#x6743;&#x9650;">
<node CREATED="1541492667363" ID="Freemind_Link_428501691" MODIFIED="1541492724151" TEXT="&#x7528;&#x6237;&#x4e0d;&#x9700;&#x8981;&#x5728;&#x5b89;&#x88c5;&#x8f6f;&#x4ef6;&#x7684;&#x65f6;&#x5019;&#x4e00;&#x6b21;&#x6027;&#x6388;&#x6743;&#x6240;&#x6709;&#x6743;&#x9650;&#xff0c;&#x800c;&#x662f;&#x5728;&#x4f7f;&#x7528;&#x8fc7;&#x7a0b;&#x4e2d;&#xff0c;&#x518d;&#x5bf9;&#x67d0;&#x9879;&#x6743;&#x9650;&#x6388;&#x6743;"/>
<node CREATED="1541492753768" ID="Freemind_Link_1041762296" MODIFIED="1541492798785" TEXT="&#x666e;&#x901a;&#x6388;&#x6743;&#xff0c;&#x4e0d;&#x4f1a;&#x76f4;&#x63a5;&#x5f71;&#x54cd;&#x5230;&#x7528;&#x6237;&#x9690;&#x79c1;&#x7684;&#x6743;&#x9650;&#xff0c;&#x7cfb;&#x7edf;&#x4f1a;&#x81ea;&#x52a8;&#x6388;&#x6743;" VSHIFT="18"/>
<node CREATED="1541492778338" ID="Freemind_Link_1954088502" MODIFIED="1541492908209" TEXT="&#x5371;&#x9669;&#x6743;&#x9650;&#xff0c;&#x5219;&#x9700;&#x8981;&#x7528;&#x6237;&#x624b;&#x52a8;&#x786e;&#x8ba4;&#x3002; &#x7528;&#x6237;&#x6388;&#x6743;&#x6743;&#x9650;&#x7ec4;&#x4e2d;&#x7684;&#x4e00;&#x4e2a;&#x6743;&#x9650;&#xff0c;&#x5176;&#x5b83;&#x4e5f;&#x88ab;&#x540c;&#x65f6;&#x6388;&#x6743;&#x3002;" VSHIFT="34">
<node CREATED="1541492822163" ID="Freemind_Link_60183889" MODIFIED="1541492836195" TEXT="&#x4e5d;&#x7ec4;24&#x9879;&#x3002;"/>
</node>
<node CREATED="1541493258546" ID="Freemind_Link_1899102643" MODIFIED="1541493326841" TEXT="if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {&#xa;            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_CONTACTS }, 1);&#xa;&#xa;//callback&#xa;void onRequestPermissionsResult(&#xff09;" VSHIFT="48"/>
</node>
</node>
</map>
