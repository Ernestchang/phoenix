
查找待爬取链接列表
# /type.aspx?p=3&c=%e5%85%83%e4%bb%a3&x=%e6%9b%b2
1. 找出页面中所有符合条件的链接
2. 将链接加入待爬取列表

爬取作者流程
1. 找出页面中所有元曲链接
2. 找出作者姓名并写入数据库
   对于每一个链接，作者姓名，
   在author表中查询是否已有该作者
   如果没有，把作者姓名加入author表

基地址：   http://so.gushiwen.org/type.aspx?p=1&c=%E5%85%83%E4%BB%A3&x=%E6%9B%B2


页面链接格式
    <div class="pages">
        <a href="/type.aspx?p=3&c=%e5%85%83%e4%bb%a3&x=%e6%9b%b2">
    </div>  

找出作者姓名
<div class="main3">
    <div class="typeleft">
        <div class="sons">
        </div>
    </div>
</div>
