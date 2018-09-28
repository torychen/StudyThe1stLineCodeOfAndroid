# StudyThe1stLineCodeOfAndroid
My code of the book "The first line code of Android"

At office, use TortoiseGit, need to generate PuTTYgen required key as the following steps:

+ generate ssh key

    + mkdir ~/.ssh when it does NOT exist

    + ssh-keygen -t rsa -b 4096 -C "your email"

+ copy public key in **id_rsa.pub** to git hub

+  ssh -T git@github.com

+ when use TortoiseGit

    + puttygen, load  id_rsa
        
    + Pageant, add key by the file just generated.


#  编程笔记
## XML基础
tag（标签），attributes:name="value"（属性）， content（内容）， node（节点）

一个节点，有一个标签，有0到多个属性，0到1个内容， 内容可以是简单的字符串，或者是0到多个子节点。

```
<tag attributes:name="value">  content  </tag>

<tag attributes:name="value">  
    <tag attributes:name="value">  content  </tag>
</tag>
```

## 活动（Activity）（Java）
任何活动，都必须重写 `onCreate（）`方法。

活动，通过 `setContentView(R.layout.first_layout);` 加载一个布局。

任何活动，都必须在`AndroidManifest.xml`中注册。

活动的属性有，`name`， `label`

活动的内容有， `intent-filter`

`intent-filter`的内容有`action`和`category`

```
AndroidManifest.xml

<activity android:name=".FirstActivity" android:label="This is FirstActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"></action>
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
</activity>
```

## 布局（Layout）（XML）
布局用来显示界面。 控件，如 Button 什么的都是在 布局文件中。

## 按钮控件（Button）
按钮控件，在布局文件中声明，包含几个`android`属性， id, text, layout_width, layout_height 等

其中`id` 属性， 这样写 `android:id="@+id/<name>"`


```
layout/first_layout.xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/button_1"
        android:text="Button 1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</LinearLayout>
```


# Android Studio 快捷键
C-F9        build
S-F10		run
C-F2		stop

