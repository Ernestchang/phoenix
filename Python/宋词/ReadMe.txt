
爬取流程
1. 找出页面中所有宋词链接
2. 找出待爬取链接
   对于每一个链接，找出标题、子标题、作者姓名，
   在poem表中查询是否已有该首词，
   如果没有把链接地址加入待爬取列表
3. 爬取待爬取链接地址列表中每一首词
   查询出标题、副标题、朝代名、作者名、原文，赏析
   根据朝代名，从dynasty表中查找对应id
   根据作者名，从author表中查询对应id
   根据标题，从tune表中查询对应id
   把数据insert入poem表中

基地址：   http://so.gushiwen.org/gushi/songsan.aspx

宋词链接格式
   <div class='son2'>
       <span>
           <a href="/view_47069.aspx"/>
       </span>
   </div>

宋词详情
获取标题、副标题，addition、朝代id，作者id，原文，赏析，创作背景

标题、副标题：
<div class="son1"><h1>标题及副标题</h1></div>
# 千秋岁·数声鶗鴂
1. 获取到文字
2. 获取到标题与副标题

原文及addition
