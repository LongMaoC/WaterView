# WaterView## 日志  * 新增 ： 保持高度的暂停![image](https://github.com/LongMaoC/WaterView/blob/master/gif/waterview.gif)* [思路](#user-content-思路)* [使用](#user-content-使用)* [属性](#user-content-属性)* [反馈](#user-content-反馈)## 思路主要就是sin函数，y = Asin(wx+b)+h ，这个公式里：w影响周期，A影响振幅，h影响y位置，b为初相。根据函数计算出两个波浪的path ，最后进行绘制## 使用在root build.gradle中添加```allprojects {  repositories {    ...    maven { url "https://jitpack.io" }  }}```在app build.gradle中添加``` compile 'com.github.LongMaoC:WaterView:v1.0'```xml```<cxy.com.waterviewlib.WaterView        android:id="@+id/waterview"        android:layout_width="250dp"        android:layout_height="250dp" />```监听```waterView.setListener(new WaterView.Listener() {          @Override          public void finish() {              Toast.makeText(MainActivity.this, "已经满了！！！", Toast.LENGTH_SHORT).show();          }      });```### 属性| 属性                          | 说明                            |默认值|| :---------------------------: |:-------------------------------:|:-----------:|| waterview_paint_color_first   |  第一只画笔颜色    |#5353C7|| waterview_paint_color_second   | 第二只画笔颜色 |#9292DA|| waterview_frame_color   | 边框画笔颜色 |#5353C7|| waterview_frame_width   | 边框宽度，为0dp时不显示边框|1|| waterview_amplitude     | 振幅 ，参考值10到100之间        |20|| waterview_up_velocity   | 上升速度上升速度，参考值5     |5 || waterview_offset_increment_value   | 初项递增值，表示波浪的快慢 ，参考值0.4    |0.4    || waterview_sleep_time   | 界面更新速度,单位毫秒，参考值100     |100 || waterview_isShowFrame   | 是否显示边框     |false|## 反馈在使用中有任何问题、意见，或者有好的想法，欢迎反馈给我，一起实现~* 邮件(chenxingyu1112@gmail.com)* QQ: 1209101049 
 
