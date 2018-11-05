<map version="0.8.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node COLOR="#000000" CREATED="1541411531632" ID="Freemind_Link_270258532" MODIFIED="1541411841017" TEXT="&#x7b2c;&#x56db;&#x7ae0;&#x788e;&#x7247;">
<edge COLOR="#009999" WIDTH="4"/>
<node COLOR="#000000" CREATED="1541411571934" ID="_" MODIFIED="1541411841018" POSITION="left" TEXT="&#x77e5;&#x8bc6;&#x70b9;">
<edge COLOR="#009999" WIDTH="4"/>
<node COLOR="#000000" CREATED="1541411582653" ID="Freemind_Link_1102251805" MODIFIED="1541411841018" TEXT="&#x788e;&#x7247;Fragment&#x662f;&#x5d4c;&#x5165;&#x5230;&#x6d3b;&#x52a8;&#x4e2d;&#x7684;UI&#x7247;&#x6bb5;&#x3002;&#xa;&#xa;&#x5982;&#x5e73;&#x677f;&#x4e0a;&#xff0c;&#x65b0;&#x95fb;&#x5e94;&#x7528;&#x7a0b;&#x5e8f;&#x3002;&#xa;&#x53ef;&#x4ee5;&#x5c06;&#x65b0;&#x95fb;&#x6807;&#x9898;&#x5217;&#xff0c;&#x548c;&#x65b0;&#x95fb;&#x8be6;&#x7ec6;&#x5185;&#x5bb9;&#xff0c;&#x5206;&#x522b;&#x653e;&#x5728;&#x4e24;&#x4e2a;&#x788e;&#x7247;&#x4e2d;&#xff0c;&#x7136;&#x540e;&#x5728;&#x540c;&#x4e00;&#x4e2a;&#x6d3b;&#x52a8;&#x91cc;&#x5f15;&#x5165;&#x8fd9;&#x4e24;&#x4e2a;&#x788e;&#x7247;&#x3002;&#xa;">
<edge COLOR="#009999" WIDTH="4"/>
</node>
<node COLOR="#000000" CREATED="1541411788386" ID="Freemind_Link_210033806" MODIFIED="1541411841020" TEXT="&#x7528; support &#x5e93;&#x7684; Fragment">
<edge COLOR="#009999" WIDTH="4"/>
</node>
<node COLOR="#000000" CREATED="1541411802506" ID="Freemind_Link_927305650" MODIFIED="1541411841020" TEXT="&#x65b0;&#x5efa;&#x7c7b;&#x7ee7;&#x627f;&#x81ea;Fragment" VSHIFT="22">
<edge COLOR="#009999" WIDTH="4"/>
</node>
<node CREATED="1541411933021" HGAP="21" ID="Freemind_Link_1601278743" MODIFIED="1541411944180" TEXT="fragment &#x6709;&#x81ea;&#x5df1;&#x7684;&#x5e03;&#x5c40;xml&#x6587;&#x4ef6;" VSHIFT="35"/>
<node CREATED="1541411949231" ID="Freemind_Link_1538713370" MODIFIED="1541412040323" TEXT="&#x5728;&#x4e3b;&#x5e03;&#x5c40;&#x6587;&#x4ef6;&#x5b89;&#x88c5;&#x9700;&#x8981;&#x6392;&#x5217;&#xa;&#xa;&lt;LinearLayout&#xa;    &lt;fragment&#xa;          android:name=&quot;your fragement layout xml file&quot;" VSHIFT="54"/>
<node CREATED="1541412501370" ID="Freemind_Link_346730665" MODIFIED="1541412521714" TEXT="&#x5728;&#x788e;&#x7247;&#x4e2d;&#x6a21;&#x62df;&#x8fd4;&#x56de;&#x6808; addToBackStack&#xff08;&#xff09;" VSHIFT="31"/>
<node CREATED="1541412523564" ID="Freemind_Link_449029526" MODIFIED="1541412587923" TEXT="&#x5728;&#x788e;&#x7247;&#x548c;&#x6d3b;&#x52a8;&#x4e4b;&#x95f4;&#x901a;&#x4fe1;&#xa;findFragmentById&#xff08;&#xff09; &#x53ef;&#x4ee5;&#x8ba9;&#x6d3b;&#x52a8;&#x83b7;&#x5f97;&#x788e;&#x7247;&#x7684;&#x5b9e;&#x4f8b;&#xa;&#xa;getActivity&#xff08;&#xff09; &#x53ef;&#x4ee5;&#x83b7;&#x5f97;&#x5f53;&#x524d;&#x7684;&#x6d3b;&#x52a8;"/>
</node>
<node CREATED="1541412054311" ID="Freemind_Link_873908913" MODIFIED="1541412467480" POSITION="right" TEXT="&#x52a8;&#x6001;&#x6dfb;&#x52a0;&#x788e;&#x7247;" VSHIFT="42">
<icon BUILTIN="bookmark"/>
<node CREATED="1541412136751" ID="Freemind_Link_1091590381" MODIFIED="1541412455353" TEXT="&#x4e3b;&#x8981;&#x5206;5&#x6b65;&#xa;1. &#x521b;&#x5efa;&#x5f85;&#x6dfb;&#x52a0;&#x788e;&#x7247;&#x7684;&#x5b9e;&#x4f8b;&#xa;2. &#x83b7;&#x53d6;FragmentManager&#xff0c; getSupportFragmentManager&#xff08;&#xff09;&#xa;3. &#x5f00;&#x542f;&#x4e00;&#x4e2a;&#x4e8b;&#x52a1;&#xff0c; beginTransaction&#xff08;&#xff09;&#xa;4. &#x5411;&#x5bb9;&#x5668;&#x6dfb;&#x52a0;&#x6216;&#x66ff;&#x6362;&#x788e;&#x7247;&#xff0c;&#x4e00;&#x822c;&#x7528; replace&#xff08;&#xff09;&#xff0c;&#x9700;&#x8981;&#x4f20;&#x5165;&#x5bb9;&#x5668;&#x7684;id&#x548c;&#x5f85;&#x6dfb;&#x52a0;&#x7684;&#x788e;&#x7247;&#x5b9e;&#x4f8b;&#x3002;&#xa;5. &#x63d0;&#x4ea4;&#x4e8b;&#x52a1; commit&#xff08;&#xff09;"/>
</node>
<node CREATED="1541412655106" ID="Freemind_Link_1953319890" MODIFIED="1541412852895" POSITION="right" TEXT="&#x788e;&#x7247;&#x7684;&#x751f;&#x547d;&#x5468;&#x671f;&#xa;onAttach()-&#x5f53;&#x788e;&#x7247;&#x548c;&#x6d3b;&#x52a8;&#x5efa;&#x7acb;&#x5173;&#x8054;&#x65f6;&#x8c03;&#x7528;&#xa;onCreate()&#xa;onCreateView()-&#x4e3a;&#x788e;&#x7247;&#x521b;&#x5efa;&#x89c6;&#x56fe;&#x65f6;&#x8c03;&#x7528;&#xa;onActivityCreated()-&#x786e;&#x4fdd;&#x4e0e;&#x788e;&#x7247;&#x5411;&#x5173;&#x8054;&#x7684;&#x6d3b;&#x52a8;&#x4e00;&#x5b9a;&#x5df2;&#x7ecf;&#x521b;&#x5efa;&#x5b8c;&#x6bd5;&#x65f6;&#x8c03;&#x7528;&#xa;onStart()&#xa;onResume()&#xa;onPause&#xa;onStop&#xa;onDestoryView-&#x5f53;&#x4e0e;&#x788e;&#x7247;&#x5173;&#x8054;&#x7684;&#x6d3b;&#x52a8;&#x88ab;&#x79fb;&#x9664;&#x65f6;&#x8c03;&#x7528;&#xa;onDestroy&#xa;onDetach-&#x5f53;&#x788e;&#x7247;&#x4e0e;&#x6d3b;&#x52a8;&#x63a5;&#x89e6;&#x5173;&#x8054;&#x65f6;&#x8c03;&#x7528;"/>
</node>
</map>
