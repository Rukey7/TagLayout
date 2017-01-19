# TagLayout
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html) [![](https://jitpack.io/v/Rukey7/TagLayout.svg)](https://jitpack.io/#Rukey7/TagLayout)

### 多功能的标签流布局

## Screenshot

##### 不同标签形状：

![](https://raw.githubusercontent.com/Rukey7/TagLayout/master/ScreenShot/tag_shape.png)

##### 标签单选和多选模式：

![](https://raw.githubusercontent.com/Rukey7/TagLayout/master/ScreenShot/tag_choice.png) 

##### 标签编辑模式：

![](https://raw.githubusercontent.com/Rukey7/TagLayout/master/ScreenShot/tag_edit.png)  

##### 标签换一换模式：

![](https://raw.githubusercontent.com/Rukey7/TagLayout/master/ScreenShot/tag_change.gif) 

##### 单个标签的其它用法：
 
![](https://raw.githubusercontent.com/Rukey7/TagLayout/master/ScreenShot/tag_view.gif)

## dependence

你需要在项目的根 `build.gradle` 加入如下JitPack仓库链接：

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

接着在你的需要依赖的Module的`build.gradle`加入依赖:

```gradle
compile 'com.github.Rukey7:TagLayout:{lastest-version}'
```

其中 `{lastest-version}` 为最新的版本，你可以查看上面显示的jitpack版本信息，也可以到[jitpack.io](https://jitpack.io/#Rukey7/IjkPlayerView)仓库查看。

## Usage


在布局中直接使用：

```xml

	<!-- 标签布局 -->
	<com.dl7.tag.TagLayout
        android:id="@+id/tag_layout_1"
        style="@style/TagLayout.RandomColor"
        app:tag_layout_fit_num="3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

	<!-- 单个标签独立使用 -->
	<com.dl7.tag.TagView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="20dp"
            android:text="删除"
            app:tag_border_color="@android:color/holo_red_light"
            app:tag_icon="@mipmap/ic_delete"
            app:tag_press_feedback="true"
            app:tag_text_color="@android:color/holo_red_light"/>
```

### 属性设置

标签布局属性（有对应接口）：

|name|format|description|
|:---:|:---:|:---:|
| tag_layout_mode | enum | {normal，edit，change，single_choice，multi_choice}, 分别为正常、编辑、换一换、单选和多选等模式
| tag_layout_shape | enum | {round_rect，arc，rect}，标签形状分别为圆角矩形、圆弧形和直角矩形，默认round_rect
| tag_layout_random_color | boolean | 随机颜色
| tag_layout_press_feedback | boolean | 按压反馈效果
| tag_layout_fit_num | integer | 设置一行固定显示几个标签
| tag_layout_bg_color | color | 标签布局背景颜色
| tag_layout_border_color | color | 标签布局边框颜色
| tag_layout_border_radius | dimension | 标签布局边框圆角弧度
| tag_layout_border_width | dimension | 标签布局边框大小
| tag_layout_vertical_interval | dimension | 标签垂直间隔
| tag_layout_horizontal_interval | dimension | 标签水平间隔
| tag_view_bg_color | color | 标签背景颜色
| tag_view_border_color | color | 标签边框颜色
| tag_view_text_color | color | 标签字体颜色
| tag_view_bg_color_check | color | 标签选中背景颜色
| tag_view_border_color_check | color | 标签选中边框颜色
| tag_view_text_color_check | color | 标签选中字体颜色
| tag_view_border_width | dimension | 标签边框大小
| tag_view_border_radius | dimension | 标签边框圆角弧度
| tag_view_vertical_padding | dimension | 标签垂直填充
| tag_view_horizontal_padding | dimension | 标签水平填充
| tag_view_icon_padding | dimension | 标签icon和文字的间隔
| tag_view_text_size | float | 标签字体大小，单位sp

标签属性：

|name|format|description|
|:---:|:---:|:---:|
| tag_mode | enum | {normal，check，icon_check_invisible，icon_check_change}, 分别为正常、可选中、选中图标消失和选中换图标等模式
| tag_shape | enum | {round_rect，arc，rect}，标签形状分别为圆角矩形、圆弧形和直角矩形，默认round_rect
| tag_auto_check | boolean | 使能自动点击选中操作
| tag_press_feedback | boolean | 按压反馈效果
| tag_checked | boolean | 初始选中状态
| tag_icon | reference | 标签图标
| tag_icon_change | reference | 标签选中时替换的图标（icon_check_change模式）
| tag_text_check | string | 标签选中时替换的字符
| tag_bg_color | color | 标签背景颜色
| tag_border_color | color | 标签边框颜色
| tag_text_color | color | 标签字体颜色
| tag_bg_color_check | color | 标签选中背景颜色
| tag_border_color_check | color | 标签选中边框颜色
| tag_text_color_check | color | 标签选中字体颜色
| tag_border_width | dimension | 标签边框大小
| tag_border_radius | dimension | 标签边框圆角弧度
| tag_vertical_padding | dimension | 标签垂直填充
| tag_horizontal_padding | dimension | 标签水平填充
| tag_icon_padding | dimension | 标签icon和文字的间隔

License
-------

    Copyright 2017 Rukey7

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
