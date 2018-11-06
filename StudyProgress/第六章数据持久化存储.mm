<map version="0.8.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node COLOR="#000000" CREATED="1541486053690" ID="Freemind_Link_1778020228" MODIFIED="1541486107340" TEXT="&#x7b2c;&#x516d;&#x7ae0;&#x6570;&#x636e;&#x6301;&#x4e45;&#x5316;&#x5b58;&#x50a8;">
<edge COLOR="#009999" WIDTH="4"/>
<node COLOR="#000000" CREATED="1541486082115" ID="_" MODIFIED="1541486107340" POSITION="right" TEXT="&#x4e09;&#x79cd;&#x65b9;&#x5f0f;&#xff1a;&#x6587;&#x4ef6;&#xff0c;SharedPreference&#x548c; &#x6570;&#x636e;&#x5e93;">
<edge COLOR="#009999" WIDTH="4"/>
</node>
<node CREATED="1541486511827" HGAP="25" ID="Freemind_Link_1859985639" MODIFIED="1541486517426" POSITION="right" TEXT="&#x6587;&#x4ef6;" VSHIFT="44">
<node CREATED="1541486520051" ID="Freemind_Link_1760297009" MODIFIED="1541486775776" TEXT="Context  openFileOutput&#xff08;&#x6587;&#x4ef6;&#x540d;&#xff0c;&#x64cd;&#x4f5c;&#x6a21;&#x5f0f;&#xff09;&#xa;&#x6587;&#x4ef6;&#x540d;&#xff0c;&#x4e0d;&#x80fd;&#x5305;&#x542b;&#x8def;&#x5f84;&#xff0c;&#x56e0;&#x4e3a;&#x9ed8;&#x8ba4;&#x8def;&#x5f84;/data/data/&lt;pacagename&gt;/files&#xa;&#xa;&#x64cd;&#x4f5c;&#x6a21;&#x5f0f;&#xff0c;MODE_PRIVATE(&#x9ed8;&#x8ba4;), MODE_APPEND&#xa;&#xa;&#x8fd4;&#x56de; FileOutputStream &#xa;&#xa;BufferWriter(new OutputStreamWriter(FileOutputStream ))"/>
<node CREATED="1541487391962" ID="Freemind_Link_1091119190" MODIFIED="1541487558561" TEXT="&#x8bfb;&#x6587;&#x4ef6;&#xa;FileInputSream in = openFileInput(&quot;file name&quot;)&#xa;reader = BufferReader(new InputSreamReader(in))&#xa;line = reader.readLine()&#xa;&#xa;content = StringBuilder.append(line)&#xa;content.toString()&#xa;"/>
</node>
<node CREATED="1541487699757" ID="Freemind_Link_76036766" MODIFIED="1541488312440" POSITION="right" TEXT="SharedPreference&#xa;&#x7528;&#x952e;&#x503c;&#x5bf9;&#x6765;&#x5b58;&#x50a8;&#x6570;&#x636e;&#xff0c; &#x4fdd;&#x5b58;&#x4e3a; xml &#x6587;&#x4ef6;&#x3002;&#xa;&#xa;&#x4e09;&#x79cd;&#x65b9;&#x6cd5;&#x83b7;&#x5f97; SharedPreference &#x5bf9;&#x8c61;" VSHIFT="54">
<node CREATED="1541487770363" ID="Freemind_Link_10248762" MODIFIED="1541487907394" TEXT="Context getSharePreference&#xff08;filename&#xff0c; &#x4ec5;MODE_PRIVATE&#xff09;&#xa;&#x9ed8;&#x8ba4;&#x8def;&#x5f84; /data/data/&lt;pacage name&gt;/share_prefs/"/>
<node CREATED="1541487784471" ID="Freemind_Link_803237288" MODIFIED="1541488074520" TEXT="Activity getPrefrence&#xff08;&#x4ec5;MODE_PRIVATE&#xff09;&#xa;&#x6587;&#x4ef6;&#x540d;&#x9ed8;&#x8ba4; &#x4e3a;&#x5f53;&#x524d;&#x6d3b;&#x52a8;&#x7684;&#x7c7b;&#x540d;&#xa;" VSHIFT="47"/>
<node CREATED="1541487800019" HGAP="21" ID="Freemind_Link_645762553" MODIFIED="1541488160856" TEXT="PreferenceManger&#xff0c; getDefaultSharedPrefreence&#xff08;Context&#xff09;&#xa;&#x4f7f;&#x7528;&#x5f53;&#x524d;&#x5305;&#x540d;&#xff0c;&#x6765;&#x547d;&#x540d;&#x6587;&#x4ef6;&#x3002;" VSHIFT="31"/>
<node CREATED="1541488190315" ID="Freemind_Link_47418655" MODIFIED="1541488194034" TEXT="&#x4f7f;&#x7528;&#x65b9;&#x6cd5;">
<node CREATED="1541488196542" ID="Freemind_Link_654947328" MODIFIED="1541488401833" TEXT="//&#x5199;&#x7528; Editor&#xa;SharePreference.Editor editor = getSharePrefrence(&quot;data&quot;, MODE_PRIVATE).editor();&#xa;&#xa;editor.putString()&#xa;editor.putInt()&#xa;editor.putBoolean()&#xa;editor.apply()&#xa;&#xa;//&#x8bfb;&#x76f4;&#x63a5;&#x7528; SharePreference&#xa;SharePreference sp = getSharePrefrence(&quot;data&quot;, MODE_PRIVATE)&#xa;sp.getString()&#xa;sp.getInt()"/>
</node>
</node>
<node CREATED="1541488502288" ID="Freemind_Link_1762182655" MODIFIED="1541488508583" POSITION="right" TEXT="&#x6570;&#x636e;&#x5e93;SQLite">
<node CREATED="1541488551907" ID="Freemind_Link_1953799430" MODIFIED="1541488616169" TEXT="SQLiteHelper&#xff1a;&#xff1a;&#xa;&#x5fc5;&#x987b;&#x91cd;&#x5199; onCreate&#xff08;&#xff09;&#xff0c; onUpgrade&#xff08;&#xff09;"/>
<node CREATED="1541488622159" ID="Freemind_Link_1731991351" MODIFIED="1541488747529" TEXT="getReadableDataBase&#xff08;Context&#xff0c; name&#xff0c;Cursor&#xff0c; version&#xff09;&#xff1a;&#x78c1;&#x76d8;&#x6ee1;&#xff0c;&#x4e0d;&#x4f1a;&#x62a5;&#x9519;&#xa;&#xa;getWriteableDataBase&#xff08;Context&#xff0c; name&#xff0c;Cursor&#xff0c; version&#xff09;&#xff1a;&#x78c1;&#x76d8;&#x6ee1;&#xff0c;&#x4f1a;&#x62a5;&#x9519;&#xa;&#xa;&#x9ed8;&#x8ba4;&#x8def;&#x5f84;&#xff1a;/data/data/&lt;pacage name&gt;/databases&#xa;&#xa;"/>
<node CREATED="1541489101639" ID="Freemind_Link_526929476" MODIFIED="1541489130097" TEXT="SQLiteDatabase.execSQL&#xa;sql &#x57fa;&#x672c;&#x64cd;&#x4f5c;" VSHIFT="58"/>
<node CREATED="1541489229230" HGAP="26" ID="Freemind_Link_1447463516" MODIFIED="1541489564946" TEXT="SQLiteDatabase &#x57fa;&#x672c;&#x64cd;&#x4f5c;&#xa;insert(&quot;Book&quot;, null, ContentValues)&#xa;update(&quot;Book&quot;, ContentValues, &quot;name = ?&quot;, new String[]{&quot;XXX&quot;})&#xa;delete(&quot;Book&quot;, &quot;page &gt; ?&quot;, new String[] {&quot;500&quot;})&#xa;&#xa;" VSHIFT="35"/>
<node CREATED="1541489572723" ID="Freemind_Link_867402526" MODIFIED="1541489689425" TEXT="query&#xff08;&#xff09; &#x53c2;&#x6570;&#xa;table &#x8868;&#x540d;&#xa;columns &#x5217;&#x540d;&#xa;selection &#x6307;&#x5b9a; where &#x7684;&#x7ea6;&#x675f;&#x6761;&#x4ef6;&#xa;selectionArgs &#x4e3a; where &#x5360;&#x4f4d;&#x7b26;&#x63d0;&#x4f9b;&#x5177;&#x4f53;&#x7684;&#x503c;&#xa;groupBy &#x5236;&#x5b9a;&#x9700;&#x8981; groupBy&#x7684;&#x5217;&#xa;having&#xa;orderBy&#xa;"/>
</node>
<node CREATED="1541487207654" ID="Freemind_Link_865509428" MODIFIED="1541487209914" POSITION="left" TEXT="&#x8c03;&#x8bd5;">
<node CREATED="1541487210790" ID="Freemind_Link_522922035" MODIFIED="1541487218978" TEXT="Android Device Monitor"/>
<node CREATED="1541487638798" ID="Freemind_Link_1715618379" MODIFIED="1541487668465" TEXT="TextUtils.isEmpty&#xff08;&#xff09;&#xa;&#x53ef;&#x4ee5;&#x5224;&#x65ad; null&#xff0c; &#x6216;&#x8005; &#x7a7a;&#x4e32;&#x5373;&#x201c;&#x201d;"/>
<node CREATED="1541488953253" ID="Freemind_Link_1376331892" MODIFIED="1541488960650" TEXT="adb shell" VSHIFT="29"/>
</node>
<node CREATED="1541488471955" ID="Freemind_Link_1164360276" MODIFIED="1541488483545" POSITION="left" TEXT="&#x4f8b;&#x5b50;&#xff0c;&#x8bb0;&#x4f4f;&#x5bc6;&#x7801;" VSHIFT="48"/>
<node CREATED="1541488810638" ID="Freemind_Link_1000666407" MODIFIED="1541488816939" POSITION="left" TEXT="SQL &#x57fa;&#x672c;&#x64cd;&#x4f5c;">
<node CREATED="1541488817768" ID="Freemind_Link_1925284153" MODIFIED="1541488839568" TEXT="&#x5efa;&#x8868;&#xff0c;&#x4e3b;&#x952e;&#xff0c;&#x6570;&#x636e;&#x7c7b;&#x578b;"/>
</node>
</node>
</map>
